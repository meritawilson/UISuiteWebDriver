package webdrivermgr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import webdrivermgr.POJO;

public class PushDataToElk {
	private static HttpClient client;
	static String perfELKURL = null;
	static String reportPath = null;
	static String scenario = null;
	static String environment = null;
	static String BuildNumber = null;
	static float thresholdValueInMillis;
	static float thresholdPercent;
	static int RunId;
	static ArrayList<String> currentBuildResultsJSONs;
	static ArrayList<String> previousBuildResultsJSONs;
	static String strFieldToCompare = "timeTaken";
	static int exitCode = 0;

	public static void main(String[] arguments) {
		System.out.println("Validating CSV File Argument");

		File csvFile = new File(arguments[0]);
		if (csvFile.exists()) {
			System.out.println("CSV File Exists !!");
			reportPath = arguments[0];
			BuildNumber = arguments[1];
			scenario = arguments[2];
			environment = arguments[3];
			perfELKURL = arguments[4];
			thresholdPercent = Float.parseFloat(arguments[5]);
			thresholdValueInMillis = Float.parseFloat(arguments[6]);
			RunId = Integer.parseInt(arguments[7]);
		} else {
			System.out.println("File specified in the 1st Argument doesn't exists !!");
			System.exit(-2);
		}
		File fCSVReportPath = new File(reportPath);
		readCSVToPOJO(fCSVReportPath);
		// publishArrayListToELK(currentBuildResultsJSONs);
		fetchLatestBuildResultsFromELK();
		compareJsonsOfDifferentBuilds(currentBuildResultsJSONs, previousBuildResultsJSONs);
	}

	public static void readCSVToPOJO(File fJMeterResultCSVFilePath) {
		POJO PerfPojo;
		currentBuildResultsJSONs = new ArrayList<String>();

		try {
			FileReader fr = new FileReader(fJMeterResultCSVFilePath);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			String[] PerfResultsArray;
			int linecount = 0;
			/* Sample CSV File Header */
			/*
			 * Label|# Samples|Average|Median|90% Line|95% Line|99% Line|Min|Max|Error
			 * %|Throughput|Received KB/sec|Std. Dev.
			 */
			while ((line = br.readLine()) != null) {
				if (linecount > 0) {
					PerfResultsArray = line.split(",");
					PerfPojo = new POJO();
					PerfPojo.setUserAction(PerfResultsArray[0]);
					PerfPojo.setTimeTaken(Double.parseDouble(PerfResultsArray[1]));
					PerfPojo.setTotalSize(Float.parseFloat(PerfResultsArray[2]));
					PerfPojo.setNoOfRequests(Integer.parseInt(PerfResultsArray[3]));
					PerfPojo.setBuildNumber(BuildNumber);
					PerfPojo.setEnvironment(environment);
					PerfPojo.setScenario(scenario);
					PerfPojo.setStatus("DRAFT");
					PerfPojo.setRunId(RunId);
					String Results = convertPOJOtoJSON(PerfPojo);
					System.out.println(Results);
					currentBuildResultsJSONs.add(Results);
				}
				linecount++;
			}
			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void fetchLatestBuildResultsFromELK() {
		ArrayList<Double> getBuildsListInDesc;
		String strAggregationQuery = "{\"size\":0,\"query\":{\"bool\":{\"must\":[{\"bool\":{\"must\":[{\"bool\":{\"must\":[{\"wildcard\":{\"scenario\":{\"wildcard\":\""
				+ PushDataToElk.scenario
				+ "\",\"boost\":1}}}],\"adjust_pure_negative\":true,\"boost\":1}},{\"wildcard\":{\"environment\":{\"wildcard\":\""
				+ PushDataToElk.environment
				+ "\",\"boost\":1}}}],\"adjust_pure_negative\":true,\"boost\":1}},{\"wildcard\":{\"status\":{\"wildcard\":\"Passed\",\"boost\":1}}}],\"adjust_pure_negative\":true,\"boost\":1}},\"_source\":false,\"stored_fields\":\"_none_\",\"aggregations\":{\"groupby\":{\"filters\":{\"filters\":[{\"match_all\":{\"boost\":1}}],\"other_bucket\":false,\"other_bucket_key\":\"_other_\"},\"aggregations\":{\"GetMaxOfRunIdForGivenFilter\":{\"max\":{\"field\":\"runId\"}}}}}}";
		System.out.println("Substituted Aggregation Query : " + strAggregationQuery);
		String response = getFromELK(client, perfELKURL, "_search", strAggregationQuery);
		getBuildsListInDesc = getLastBuildNumber(response);
		if (!getBuildsListInDesc.isEmpty()) {
			int getLastSuccessfulRunID = (int) Double.parseDouble(getBuildsListInDesc.get(0) + "");
			String getBuildQuery = "{\"query\":{\"bool\":{\"must\":[{\"match\":{\"environment\":\""
					+ PushDataToElk.environment + "\"}},{\"match\":{\"status\":\"Passed\"}},{\"match\":{\"scenario\":\""
					+ PushDataToElk.scenario + "\"}},{\"match\":{\"runId\":" + getLastSuccessfulRunID + "}}]}}}";
			System.out.println("Get Previous Successful Results : " + getBuildQuery);
			String LatestResults = getFromELK(client, perfELKURL, "_search", getBuildQuery);
			getResultsJSONs(LatestResults);
		} else {
			System.out.println(
					"Results Query has not fetched any results - Either Query could be wrong or No Previous Results Exists !!!");
		}
	}

	public static void getResultsJSONs(String response) {
		String JSONResults = "";
		previousBuildResultsJSONs = new ArrayList<String>();
		ArrayList<String> TempResults = new ArrayList<String>();
		String strNoResults = "\"hits\":{\"total\":{\"value\":0,\"relation\":\"eq\"},\"max_score\":null,\"hits\":[]}";
		// System.out.println("Present Results : "+ response);
		if (!response.contains(strNoResults)) {
			System.out.println("Previous results are present");
			String pathForJSONResults = "$.hits.hits.*._source";
			TempResults = JsonPath.read(Configuration.defaultConfiguration().jsonProvider().parse(response),
					pathForJSONResults);
			// System.out.println("Previous Build Results [Temp] :
			// "+TempResults.toString());
			JSONResults = TempResults.toString();
			JSONResults = JSONResults.replaceAll("\\[\\{", "");
			JSONResults = JSONResults.replaceAll("\\}\\]", "");
			JSONResults = JSONResults.replaceAll("\\\\", "");
			// System.out.println("Replaced Prefix & Suffix : " + JSONResults);

			String[] jsonArray = JSONResults.split("\\},\\{");

			for (String itrJSONs : jsonArray) {
				// System.out.println("NoParse : "+"{"+itrJSONs+"}");
				// System.out.println("TempResultsJSONS : "+JsonPath.parse(itrJSONs).json());
				previousBuildResultsJSONs.add("{" + itrJSONs.toString() + "}");
			}
		} else {
			System.out.println("Previous results are empty");
			previousBuildResultsJSONs = null;
		}
		// Print Static Arraylist
		// for (String itrSJSONs : previousBuildResultsJSONs) {
		// System.out.println(itrSJSONs);
		// previousBuildResultsJSONs.add(itrJSONs);
		// }
	}

	public static ArrayList<Double> getLastBuildNumber(String ResponseJSON) {
		// List<String> ResultsArray;
		ArrayList<Double> Buildnumbers = new ArrayList<Double>();
		boolean hasNull = true;
		// System.out.println("InputJSON :" + ResponseJSON);
		// String pathForResultsArray = "$.aggregations.my_agg..key";
		// $.aggregations..value
		String pathForResultsArray = "$.aggregations..value";
		Buildnumbers = JsonPath.read(Configuration.defaultConfiguration().jsonProvider().parse(ResponseJSON),
				pathForResultsArray);
		hasNull = isAllNulls(Buildnumbers);
		if (!hasNull) {
			System.out.println(Buildnumbers.toString());
			Collections.sort(Buildnumbers, Collections.reverseOrder());
			return Buildnumbers;
		} else {
			Buildnumbers.clear();
			return Buildnumbers; // Empty
		}
	}

	public static boolean isAllNulls(Iterable<?> array) {
		for (Object element : array)
			if (element != null)
				return false;
		return true;
	}

	public static String getFromELK(HttpClient client, String perfELKURL, String Operation, String requestBody) {
		String getResponse = "";

		try {
			client = new DefaultHttpClient();

			HttpPost postRequest = new HttpPost(perfELKURL + "/" + Operation);
			postRequest.addHeader("content-type", "application/json");
			StringEntity userEntity = new StringEntity(requestBody);
			postRequest.setEntity(userEntity);

			HttpResponse response1 = client.execute(postRequest);
			int statusCode = response1.getStatusLine().getStatusCode();
			System.out.println("status line" + response1.getStatusLine() + " userEntity :" + userEntity
					+ " requestBody : " + requestBody);

			if (statusCode != 200) {
				System.out.println(
						"Check Availability of Elastic Search Service and IndexName : " + perfELKURL + "/" + Operation);
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			} else {
				// System.out.println("Request is Successful");
				BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
				String line1 = "";
				while ((line1 = rd1.readLine()) != null) {
					getResponse = getResponse + line1;
				}
				// System.out.println("Results JSON : " + temp);
			}
		} catch (ClientProtocolException CPE) {
			CPE.getMessage();
		} catch (IOException IOE) {
			IOE.getMessage();
		} finally {
			client.getConnectionManager().shutdown();
		}
		return getResponse;
	}

	public static String convertPOJOtoJSON(POJO POJOResults) {
		String ResultsJSON = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			ResultsJSON = mapper.writeValueAsString(POJOResults);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return ResultsJSON;
	}

	public static void compareJsonsOfDifferentBuilds(ArrayList<String> currentBuildResultsJSONs,
			ArrayList<String> previousBuildResultsJSONs) {
		ArrayList<String> currentBuildJsonRequests;
		if ((currentBuildResultsJSONs != null)) {
			System.out.println("Present build JSONs are present ");
			if (currentBuildResultsJSONs != null && previousBuildResultsJSONs == null) {
				System.out.println("Creating Baseline with Results against threshold value");

				// validate Current Results against accepted threshold
				boolean status = VerifyResultsAgainstThreshold(currentBuildResultsJSONs, strFieldToCompare,
						thresholdValueInMillis);
				if (status) {
					UpdateResultStatusInJSONs(currentBuildResultsJSONs, "Passed");
				} else {
					UpdateResultStatusInJSONs(currentBuildResultsJSONs, "Failed");
				}
				publishArrayListToELK(currentBuildResultsJSONs);
			} else if (currentBuildResultsJSONs != null && previousBuildResultsJSONs != null) {
				// System.out.println("JSON Results are not Null");

				if (currentBuildResultsJSONs.size() < previousBuildResultsJSONs.size()) {
					// Current Build JSONs list is lesser than Previous Build JSON - Few Scenario's
					// are removed ??
					System.out.println(
							"Current Build JSONs list is lesser than Previous Build JSON - Few Scenario's are removed ??");
				}
				if (currentBuildResultsJSONs.size() > previousBuildResultsJSONs.size()) {
					// Current Build JSONs list is greater than Previous Build JSON - Few Scenario's
					// are added ??
					System.out.println(
							"Current Build JSONs list is greater than Previous Build JSON - Few Scenario's are added ??");
				}
				// Get Current Build JSON Request Name
				// Iterate the Current Build JSONs Request Names against
				// PreviousBuildResultsJSONs for Response Times Comparison

				// currentBuildJsonRequests = getUniqueRequestNames(currentBuildResultsJSONs);
				currentBuildJsonRequests = (currentBuildResultsJSONs);
				boolean isBuildRegressed = true;
				for (String itrJSONs : currentBuildJsonRequests) {
					float oldValue = getResponseTimes(itrJSONs, previousBuildResultsJSONs);
					float newValue = getResponseTimes(itrJSONs, currentBuildResultsJSONs);
					String result = CompareResponseTimes(oldValue, newValue, PushDataToElk.thresholdPercent);
					System.out.println("Request Name : " + itrJSONs + " - " + result);
					if (result.equals("Regressed")) {
						isBuildRegressed = true;
						break;
					} else {
						isBuildRegressed = false;
					}
				}

				if (isBuildRegressed == false) {
					UpdateResultStatusInJSONs(currentBuildResultsJSONs, "Passed");
					File f = new File("Performance.success");
					try {
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					exitCode = 0;
				} else {
					UpdateResultStatusInJSONs(currentBuildResultsJSONs, "Failed");
					File f = new File("Performance.failure");
					try {
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					exitCode = -1;
				}
				publishArrayListToELK(currentBuildResultsJSONs);
			}
		}
		System.out.println("Current Build");
		// currentBuildResultsJSONs.forEach(System.out::print);
		currentBuildResultsJSONs.forEach(value -> System.out.println(value));
		// Print Previous Build

		if (previousBuildResultsJSONs != null) {
			System.out.println("Previous Build");
			// previousBuildResultsJSONs.forEach(System.out::print);
			previousBuildResultsJSONs.forEach(value -> System.out.println(value));
		}
	}

	public static boolean VerifyResultsAgainstThreshold(ArrayList<String> currentBuildResultsJSONs,
			String strFieldToCompare, float thresholdValueInMillis) {
		boolean isSuccess = false;
		if (currentBuildResultsJSONs != null) {
			for (int itr = 0; itr <= currentBuildResultsJSONs.size() - 1; itr++) {
				try {
					ObjectNode node;
					node = new ObjectMapper().readValue(currentBuildResultsJSONs.get(itr), ObjectNode.class);

					if (node.has(strFieldToCompare)) {
						if (Float.compare(Float.parseFloat(node.get(strFieldToCompare).toString()),
								thresholdValueInMillis) > 0) {
							isSuccess = false;
							System.out.println("Threshold Breached : Build Failed");
							break;
						} else {
							isSuccess = true;
						}
					} else {
						System.out.println("strFieldToCompare is not present");
					}

				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("Current build json array list is empty");
		}
		return isSuccess;
	}

	// what is draft
	public static void UpdateResultStatusInJSONs(ArrayList<String> currentBuildResultsJSONs, String buildStatus) {
		if (currentBuildResultsJSONs != null && (buildStatus.equals("Failed") || buildStatus.equals("Passed"))) {
			for (int itr = 0; itr <= currentBuildResultsJSONs.size() - 1; itr++) {
				String jsonResult = currentBuildResultsJSONs.get(itr);
				if (jsonResult.contains("DRAFT")) {
					jsonResult = jsonResult.replaceAll("DRAFT", buildStatus);
					currentBuildResultsJSONs.set(itr, jsonResult);
					// System.out.println("Replaced DRAFT STATUS to "+ buildStatus);
				} else {
					System.out.println("JSON Status element doesn't have DRAFT element");
				}
			}
		} else {
			System.out.println("JSON Array list is Empty or Status is not Valid !!");
		}
	}

	public static void publishArrayListToELK(ArrayList<String> currentBuildResultsJSONs) {
		for (String itrSJSONs : currentBuildResultsJSONs) {
			String pushresponse = pushValidatedResultsToELK(client, perfELKURL, "_Doc", itrSJSONs);
			System.out.println("pushresponse : " + pushresponse);
		}
	}

	public static String pushValidatedResultsToELK(HttpClient client, String perfELKURL, String Operation,
			String jsonToPublish) {
		// System.out.println("Pushing Results To ELK");
		String temp = "";
		try {
			client = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(perfELKURL + "/_doc");
			postRequest.addHeader("content-type", "application/json");
			StringEntity userEntity = new StringEntity(jsonToPublish);
			postRequest.setEntity(userEntity);
			HttpResponse response1 = client.execute(postRequest);

			int statusCode = response1.getStatusLine().getStatusCode();
			if (statusCode != 201) {
				System.out.println("Request Failure : " + statusCode);
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			} else {
				// System.out.println("Request is Successful");
				BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
				String line1 = "";
				while ((line1 = rd1.readLine()) != null) {
					temp = temp + line1;
					// System.out.println("LineByLine : " + temp);
				}
			}
		} catch (ClientProtocolException CPE) {
			CPE.getMessage();
		} catch (IOException IOE) {
			IOE.getMessage();
		} finally {
			client.getConnectionManager().shutdown();
		}
		return temp;
	}

	public static float getResponseTimes(String requestId, ArrayList<String> resultsJSONs) {
		float percentResponseTimes = -1f;
		for (String itrJSONs : resultsJSONs) {
			if (itrJSONs.contains(requestId)) {
				try {
					ObjectNode node = new ObjectMapper().readValue(itrJSONs, ObjectNode.class);
					if (node.has(strFieldToCompare)) {
						percentResponseTimes = Float.parseFloat(node.get(strFieldToCompare).toString());
					}
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return percentResponseTimes;
	}

	public static String CompareResponseTimes(float old, float present, float allowedVarianceInPercent) {
		String comparisonResult = null;
		if (Float.compare(present, thresholdValueInMillis) <= 0) {
			if (Float.compare(old, present) == 0) {
				comparisonResult = "NotRegressed";
			} else if (Float.compare(old, present) > 0) {
				float percentDiff = ((present - old) * 100) / old;
				if (percentDiff > allowedVarianceInPercent) {
					comparisonResult = "NotRegressed";
				} else {
					System.out.println("Regressed : " + percentDiff + " " + old + " " + present);
					comparisonResult = "Regressed";
				}
			} else {
				comparisonResult = "Regressed";
			}
		} else {
			comparisonResult = "Regressed";
		}
		return comparisonResult;
	}
}
