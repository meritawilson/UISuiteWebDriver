package webdrivermgr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

public class export_Result_to_Excel {
	static String project_path = "C:\\Users\\mewilson\\Desktop\\work\\hawk\\bugs\\excelRes";

	public static void readElementsfromCSV() {

	}

	public static void writeHeaders() {
		File file = new File(project_path + "\\UI_Automation_Result_Detailed" + java.time.LocalDate.now() + ".csv");
		try {
			FileWriter outputfile = new FileWriter(file);

			CSVWriter writer = new CSVWriter(outputfile);

			String[] header = { "Requests", "Time Taken", "Size" };
			writer.writeNext(header);
			writer.close();

			File file1 = new File(
					project_path + "\\UI_Automation_Result_Summary_test" + java.time.LocalDate.now() + ".csv");
			FileWriter outputfile1 = new FileWriter(file1);
			CSVWriter writer1 = new CSVWriter(outputfile1);
			String[] header1 = { "User Action", "Time Taken", "Size", "No of Requests" };
			writer1.writeNext(header1);
			writer1.close();

			File file2 = new File(
					project_path + "\\UI_Automation_Result_HotSpots" + java.time.LocalDate.now() + ".csv");
			FileWriter outputfile2 = new FileWriter(file2);
			CSVWriter writer2 = new CSVWriter(outputfile2);
			String[] header2 = { "User Action", "Request ", "Time Taken", "Size" };
			writer2.writeNext(header2);
			writer2.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void export_Detailed_Results(String action, ArrayList name, ArrayList timetaken,
			ArrayList transferSize) {
		System.out.println(name);
		File file = new File(project_path + "\\UI_Automation_Result_Detailed" + java.time.LocalDate.now() + ".csv");
		try {

			FileWriter outputfile = new FileWriter(file, true);

			CSVWriter writer = new CSVWriter(outputfile);

			double totaltimetaken = navigationTiming.ETETime;
			long totalSize = navigationTiming.totalSize;
			String[] Total = { action, "" + totaltimetaken, "" + totalSize, "" + name.size() };
			writer.writeNext(Total);
			for (int i = 0; i < name.size(); i++) {

				String[] data1 = { (String) name.get(i), "" + (Double) timetaken.get(i),
						"" + (Integer) transferSize.get(i) };
				writer.writeNext(data1);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void export_Summary_results(String action) {

		File file = new File(project_path + "\\UI_Automation_Result_Summary_test" + java.time.LocalDate.now() + ".csv");
		try {
			FileWriter outputfile = new FileWriter(file, true);

			CSVWriter writer = new CSVWriter(outputfile);

			double totaltimetaken = navigationTiming.ETETime;
			long totalSize = navigationTiming.totalSize;

			String[] data1 = { action, "" + totaltimetaken, "" + totalSize, "" + navigationTiming.noOfRequests };
			writer.writeNext(data1);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void export_hotspot_results() {
		int indexMax1 = 0, indexMax2 = 0, indexMax3 = 0;
		Double maxV1 = 0.0, maxV2 = 0.0, maxV3 = 0.0;
		for (int i = 0; i < navigationTiming.timeTaken.size(); i++) {
			// only if greater than 100ms
			if (navigationTiming.timeTaken.get(i) > 100) {
				if (navigationTiming.timeTaken.get(i) > maxV1) {
					maxV3 = maxV2;
					maxV2 = maxV1;
					maxV1 = navigationTiming.timeTaken.get(i);

					indexMax3 = indexMax2;
					indexMax2 = indexMax1;
					indexMax1 = i;
				} else if (navigationTiming.timeTaken.get(i) > maxV2) {
					maxV3 = maxV2;
					maxV2 = navigationTiming.timeTaken.get(i);

					indexMax3 = indexMax2;
					indexMax2 = i;
				} else if (navigationTiming.timeTaken.get(i) > maxV3) {
					maxV3 = navigationTiming.timeTaken.get(i);

					indexMax3 = i;
				}
			}
		}

		int sizeIndexMax1 = 0, sizeIndexMax2 = 0, sizeIndexMax3 = 0;
		int sizemaxV1 = 0, sizemaxV2 = 0, sizemaxV3 = 0;
		for (int i = 0; i < navigationTiming.transferSize.size(); i++) {
			// not less than 200kb
			Integer sizethreshold = 200000;
			if (navigationTiming.transferSize.get(i) > sizethreshold) {
				System.out.println(navigationTiming.transferSize.get(i) + " " + sizethreshold + " "
						+ ((navigationTiming.transferSize.get(i) > sizethreshold) ? "true" : "false"));
				if (navigationTiming.transferSize.get(i) > sizemaxV1) {
					sizemaxV3 = sizemaxV2;
					sizemaxV2 = sizemaxV1;
					sizemaxV1 = navigationTiming.transferSize.get(i);

					sizeIndexMax3 = sizeIndexMax2;
					sizeIndexMax2 = sizeIndexMax1;
					sizeIndexMax1 = i;
				} else if (navigationTiming.transferSize.get(i) > sizemaxV2) {
					sizemaxV3 = sizemaxV2;
					sizemaxV2 = navigationTiming.transferSize.get(i);

					sizeIndexMax3 = sizeIndexMax2;
					sizeIndexMax2 = i;
				} else if (navigationTiming.transferSize.get(i) > sizemaxV3) {
					sizemaxV3 = navigationTiming.transferSize.get(i);

					sizeIndexMax3 = i;
				}
			}
		}

		System.out.println(maxV3 + " " + maxV2 + " " + maxV1);
		System.out.println(sizemaxV3 + " " + sizemaxV2 + " " + sizemaxV1);

		File file = new File(project_path + "\\UI_Automation_Result_HotSpots" + java.time.LocalDate.now() + ".csv");
		try {
			FileWriter outputfile = new FileWriter(file, true);
			CSVWriter writer = new CSVWriter(outputfile);

			if (maxV1 != 0) {
				String[] row3 = { "Max-Time 1", navigationTiming.requestUrl.get(indexMax1), "" + maxV1,
						"" + navigationTiming.transferSize.get(indexMax1) };
				writer.writeNext(row3);
			}
			if (maxV2 != 0) {
				String[] row2 = { "Max-Time 2", navigationTiming.requestUrl.get(indexMax2), "" + maxV2,
						"" + navigationTiming.transferSize.get(indexMax2) };
				writer.writeNext(row2);
			}
			if (maxV3 != 0) {
				String[] row1 = { "Max-Time 3", navigationTiming.requestUrl.get(indexMax3), "" + maxV3,
						"" + navigationTiming.transferSize.get(indexMax3) };
				writer.writeNext(row1);
			}
			if (sizemaxV1 != 0) {
				String[] row6 = { "Max-Size 1", navigationTiming.requestUrl.get(sizeIndexMax1),
						"" + navigationTiming.timeTaken.get(sizeIndexMax1), "" + sizemaxV1 };
				writer.writeNext(row6);
			}
			if (sizemaxV2 != 0) {
				String[] row5 = { "Max-Size 2", navigationTiming.requestUrl.get(sizeIndexMax2),
						"" + navigationTiming.timeTaken.get(sizeIndexMax2), "" + sizemaxV2, };
				writer.writeNext(row5);
			}

			if (sizemaxV3 != 0) {
				String[] row4 = { "Max-Size 3", navigationTiming.requestUrl.get(sizeIndexMax3),
						"" + navigationTiming.timeTaken.get(sizeIndexMax3), "" + sizemaxV3, };
				writer.writeNext(row4);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
