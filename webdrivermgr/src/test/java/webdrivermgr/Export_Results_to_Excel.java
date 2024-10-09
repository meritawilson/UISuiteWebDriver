package webdrivermgr;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Export_Results_to_Excel {
	public static void main(String[] args) {

	}

	public static void export_SummaryResults(String action) {

		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("UI_Automation_Results_Summary" + java.time.LocalDate.now());
			int rownum = 0;
			Row row = sheet.createRow(rownum++);
			Cell cell = row.createCell(0);
			cell.setCellValue("User Action");
			cell = row.createCell(1);
			cell.setCellValue("Time Taken");
			cell = row.createCell(2);
			cell.setCellValue("Size");
			cell = row.createCell(3);
			cell.setCellValue("No of Requests");
			// total Results
			row = sheet.createRow(rownum++);
			cell = row.createCell(0);
			cell.setCellValue(action);
			cell = row.createCell(1);
			double totaltimetaken = navigationTiming.ETETime;
			long totalSize = navigationTiming.totalSize;
			cell.setCellValue(totaltimetaken);
			cell = row.createCell(2);
			cell.setCellValue(totalSize);
			cell = row.createCell(3);
			cell.setCellValue(navigationTiming.noOfRequests);

			FileOutputStream out = new FileOutputStream(
					new File("C:\\Users\\mewilson\\Desktop\\work\\hawk\\bugs\\excelRes\\UI_Automation_Result_Summary"
							+ java.time.LocalDate.now() + ".csv"));
			workbook.write(out);
			out.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {

		}

	}

	public static void export_Results(String action, ArrayList name, ArrayList timetaken, ArrayList transferSize) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("UI_Automation_Results" + java.time.LocalDate.now());
			int rownum = 0;
			Row row = sheet.createRow(rownum++);
			Cell cell = row.createCell(0);
			cell.setCellValue("Requests");
			cell = row.createCell(1);
			cell.setCellValue("Time Taken");
			cell = row.createCell(2);
			cell.setCellValue("Size");
			// total Results
			row = sheet.createRow(rownum++);
			cell = row.createCell(0);
			cell.setCellValue(action);
			cell = row.createCell(1);
			double totaltimetaken = navigationTiming.ETETime;
			long totalSize = navigationTiming.totalSize;
			cell.setCellValue(totaltimetaken);
			cell = row.createCell(2);
			cell.setCellValue(totalSize);

			for (int i = 0; i < name.size(); i++) {
				// Creates a new row in the sheet
				row = sheet.createRow(rownum++);
				int cellnum = 0;
				cell = row.createCell(cellnum++);
				cell.setCellValue((String) name.get(i));
				cell = row.createCell(cellnum++);
				cell.setCellValue((Double) timetaken.get(i));
				cell = row.createCell(cellnum++);
				cell.setCellValue((Integer) transferSize.get(i));
			}

			FileOutputStream out = new FileOutputStream(
					new File("C:\\Users\\mewilson\\Desktop\\work\\hawk\\bugs\\excelRes\\UI_Automation_Result"
							+ java.time.LocalDate.now() + ".csv"));
			workbook.write(out);
			out.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {

		}
	}

	public void export_Results(Map<String, Object[]> results) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("UI_Automation_Results");
			Set<String> keyset = results.keySet();
			int rownum = 0;

			for (String key : keyset) {
				// Creates a new row in the sheet
				Row row = sheet.createRow(rownum++);
				Object[] objArr = results.get(key);

				int cellnum = 0;
				for (Object obj : objArr) {
					// Creates a cell in the next column of that row
					Cell cell = row.createCell(cellnum++);
					if (obj instanceof String) {
						cell.setCellValue((String) obj);
					} else if (obj instanceof Long) {
						cell.setCellValue((Long) obj);
					}
				}
			}
			FileOutputStream out = new FileOutputStream(
					new File("C:\\Users\\mewilson\\Desktop\\work\\hawk\\bugs\\excelRes\\UI_Automation_Result.csv"));
			workbook.write(out);
			out.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {

		}
	}
}
