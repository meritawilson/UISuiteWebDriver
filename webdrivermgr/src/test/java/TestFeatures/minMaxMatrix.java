package TestFeatures;

public class minMaxMatrix {
	public static void main(String args[]) {
		int mat[][] = new int[3][3];
		int col = 3;
		int row = 3;
		mat[0][0] = 1;
		mat[0][1] = 3;
		mat[0][2] = 4;
		mat[1][0] = 5;
		mat[1][1] = 2;
		mat[1][2] = 9;
		mat[2][0] = 8;
		mat[2][1] = 7;
		mat[2][2] = 6;

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				int x = mat[i][j];
				// System.out.print(x + "");
				findMinMax(x, col, row, i, j, mat);
			}
			// System.out.println();
		}
	}

	static void findMinMax(int x, int col, int row, int iIndex, int jIndex, int[][] mat) {
		// find max in row
		// find min in row
		int maxcount = 1, mincount = 1, maxcountC = 1, mincountC = 1;
		for (int i = 0; i < row; i++) {
			// find max
			if (x > mat[iIndex][i]) {
				maxcount++;
			}
			// find min
			if (x < mat[iIndex][i]) {
				mincount++;
			}

		}
		// find max in col
		// find min in col
		for (int i = 0; i < col; i++) {
			// find max
			if (x > mat[i][jIndex]) {
				maxcountC++;
			}
			// find min
			if (x < mat[i][jIndex]) {
				mincountC++;
			}

		}
		if (maxcount == row || maxcountC == col) {
			System.out.println("yes MAX of row " + x);
		}

		if (mincount == row || mincountC == col) {
			System.out.println("yes MIN of row " + x);
		}

	}
}
