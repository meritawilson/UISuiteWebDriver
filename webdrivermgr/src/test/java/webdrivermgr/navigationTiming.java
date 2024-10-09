package webdrivermgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class navigationTiming {
	static ArrayList<HashMap<String, String>> renderedList = new ArrayList<>();
	static ArrayList<Double> timeTaken = new ArrayList<Double>();
	static ArrayList<String> requestUrl = new ArrayList<String>();
	static ArrayList<Integer> transferSize = new ArrayList<Integer>();
	static ArrayList<Double> networkDurationArr = new ArrayList<Double>();
	static double startTime = 0.0;
	static double ETETime = 0.0;
	static double frontEndTime = 0.0;
	static double backEndTime = 0.0;
	static double exactBackendTime = 0.0;
	static long totalSize = 0;
	static int noOfRequests = 0;

	public static void theMain(String action) throws InterruptedException {

		timeTaken = new ArrayList<Double>();
		requestUrl = new ArrayList<String>();
		transferSize = new ArrayList<Integer>();
		networkDurationArr = new ArrayList<Double>();

		System.out.println(action);
		renderTheData(Mainclass.chdriver);
		getNoOfRequests(Mainclass.chdriver);
		getTimeTakenAndSizeForAllRequests(action);
		getTotalSize();
		getLargestSizeOfResources();
		getETETime(action);
		// getNetworkDuration();
		getFrontEndTime();
		exportToexcel(action);
		exportToSummaryExcel(action);
		export_hotspot_results();
		clearperformaceData(Mainclass.chdriver);

	}

	public static void export_hotspot_results() {
		export_Result_to_Excel.export_hotspot_results();
	}

	public static void clearperformaceData(ChromeDriver chdriver) {

		chdriver.executeScript("return performance.clearResourceTimings()");
		ArrayList requestsList = (ArrayList) chdriver
				.executeScript("return window.performance.getEntriesByType(\"resource\")");
		if (requestsList == null) {
			System.out.println("performance Object is cleared");
		}
	}

	public static void exportToexcel(String action) {

		export_Result_to_Excel.export_Detailed_Results(action, requestUrl, timeTaken, transferSize);
	}

	public static void exportToSummaryExcel(String action) {
		export_Result_to_Excel.export_Summary_results(action);
	}

//each request in ArrayList with values as hashmap
	public static void renderTheData(ChromeDriver chdriver) {
		ArrayList requestsList = (ArrayList) chdriver
				.executeScript("return window.performance.getEntriesByType(\"resource\")");
		renderedList = new ArrayList();
		for (var i = 0; i < requestsList.size(); i++) {
			HashMap<String, String> hm = new HashMap<>();
			var ele = requestsList.get(i);
			String requestStr = ele.toString();
			// System.out.println(requestStr);
			requestStr = takeawaycommas(requestStr);
			String[] requestStrList = requestStr.split(",");

			for (int j = 0; j < requestStrList.length; j++) {

				String[] requestDetail = requestStrList[j].split("=");

				if (requestDetail[0] != null) {
					requestDetail[0] = requestDetail[0].replaceAll("\\s", "");
				}
				if (requestDetail.length > 1 && requestDetail[1] != null) {
					requestDetail[1] = requestDetail[1].replaceAll("\\s", "");

					hm.put(requestDetail[0], requestDetail[1]);
				}
			}
			renderedList.add(hm);
		}
	}

	public static String takeawaycommas(String requestStr) {
		int index1 = requestStr.indexOf("name=");
		int index2 = requestStr.indexOf(", nextHopProtocol=");
		String str = requestStr.substring(index1 + 5, index2);
		// System.out.println(str);
		String strOld = str;
		if (str != null && str.contains(",")) {
			str = str.replaceAll(",", "");
			requestStr = requestStr.replace(strOld, str);

		}
		if (str != null && str.contains("=")) {
			str = str.replaceAll("=", "");
			requestStr = requestStr.replace(strOld, str);
		}
		return requestStr;
	}

	public static void getNoOfRequests(ChromeDriver chdriver) {
		noOfRequests = renderedList.size();
		System.out.println("Number of requests: " + noOfRequests);
	}

	public static void getTimeTakenAndSizeForAllRequests(String action) {
		int max = 0, index = 0;
		Integer size = 0;
		for (int i = 0; i < renderedList.size(); i++) {

			HashMap<String, String> hm = renderedList.get(i);
			String reqName = hm.get("name");
			size = Integer.parseInt(hm.get("transferSize"));
			Double timeTakenReq = Double.parseDouble(hm.get("duration"));
			timeTaken.add(timeTakenReq);
			requestUrl.add(action + "_" + reqName);
			transferSize.add(size);
			if (size > max) {
				max = size;
				index = i;
			}
		}
		System.out.println("Request with largest File is" + (requestUrl.get(index)) + "of size " + size);
	}

	public static void getLargestSizeOfResources() {
		Double maxtime = 0.0;
		// find index of ele with largest size
		// get index of ele from requestUrl Array
		int index = 0;
		for (int i = 0; i < timeTaken.size(); i++) {
			if (timeTaken.get(i) > maxtime) {
				maxtime = timeTaken.get(i);
				index = i;
			}
		}
		System.out.println("Request which takes more time is " + (requestUrl.get(index)) + "of max time " + maxtime);
	}

	public static double getETETime(String action) {
		// run via loop to maximum RESPONSE END so until then still rendering will take
		// place
		// or
		// getstartime end & responseend of all request - ete time
		if (action.equalsIgnoreCase("LoginPage") || action.equalsIgnoreCase("ServicesPage")
				|| action.equalsIgnoreCase("HomePage")) {
			ETETime = 0;
			for (int i = 0; i < renderedList.size(); i++) {
				HashMap<String, String> hm = renderedList.get(i);
				Double responseEnd = Double.parseDouble(hm.get("responseEnd"));

				if (responseEnd > ETETime) {
					ETETime = responseEnd;
				}
			}
			System.out.println("The ETE TIME : " + ETETime);
		} else {
			ETETime = 0;
			/*
			 * for(int i=0;i<timeTaken.size();i++) { ETETime+=timeTaken.get(i); }
			 * System.out.println("The ETE TIME else part: " + ETETime);
			 */

			HashMap<String, String> hm = renderedList.get(0);
			HashMap<String, String> hm1 = renderedList.get(renderedList.size() - 1);
			Double responseEnd = Double.parseDouble(hm1.get("responseEnd"));
			Double responseStart = Double.parseDouble(hm.get("startTime"));
			System.out.println("The ETE TIME else part: :" + (responseEnd - responseStart));
		}
		return ETETime;
	}

	public static double getTotalSize() {
		totalSize = 0;
		for (int i = 0; i < transferSize.size(); i++) {
			totalSize += transferSize.get(i);
		}
		return totalSize;
	}

	// work in progress
	public static void getFrontEndTime() {

		double diff = 0.0;
		frontEndTime = 0.0;
		double maxResTime = 0.0;
		String hotspotReq = null;
		for (int i = 0; i < renderedList.size() - 1; i++) {
			HashMap<String, String> hmCurr = renderedList.get(i);
			HashMap<String, String> hmNext = renderedList.get(i + 1);
			Double startTimeCurr = Double.parseDouble(hmCurr.get("startTime"));
			Double responseEndCurr = Double.parseDouble(hmCurr.get("responseEnd"));

			String reqName = hmCurr.get("name");
// only if there is diff between largest and the next request it will be added
			if (startTimeCurr > maxResTime) {
				if (maxResTime != 0.0) {
					diff += (startTimeCurr - maxResTime);

					// System.out.println(diff + " " + " startTimeCurr : " + startTimeCurr + "
					// maxResTime : " + maxResTime
					// + " " + hotspotReq + " " + reqName);
				}
			}
			// method to get the biggest response End
			if (responseEndCurr > maxResTime) {
				maxResTime = responseEndCurr;
				hotspotReq = reqName;
				// System.out.println("-------^" + maxResTime + " " + hotspotReq);
			}
		}
		/*
		 * if (startTimeNext > responseEndCurr) { diff = startTimeNext -
		 * responseEndCurr; System.out.println(" diff : " + diff + " req Url " + reqName
		 * + " responseEndCurr : " + responseEndCurr + "startTimeNext : " +
		 * startTimeNext); frontEndTime += diff; }
		 */
		// System.out.println("everytme FrontEnd time : " + frontendTime);

		System.out.println("FrontEnd time : " + diff);

	}
}
