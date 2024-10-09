package webdrivermgr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CompareBuilds {

	public static void main(String[] args) throws IOException {
		String line = "", line1 = "";
		String splitBy = ",";
		BufferedReader br = new BufferedReader(new FileReader(
				export_Result_to_Excel.project_path + "\\UI_Automation_Result_Summary_test2020-08-17.csv"));
		BufferedReader br1 = new BufferedReader(new FileReader(
				export_Result_to_Excel.project_path + "\\UI_Automation_Result_Summary_test2020-08-13.csv"));

		while (((line = br.readLine()) != null)) // returns a Boolean value
		{
			String[] rows = line.split(splitBy);
			String[] rows1 = line.split(splitBy);
			System.out.println(rows[0] + " " + rows[1] + " " + rows[2] + " " + rows[3]);
			System.out.println(rows1[0] + " " + rows1[1] + " " + rows1[2] + " " + rows1[3]);
			if (!rows[3].equalsIgnoreCase(rows1[3])) {
				System.out.println("no of requests changed for " + rows[0]);
			}
		}
	}
}
