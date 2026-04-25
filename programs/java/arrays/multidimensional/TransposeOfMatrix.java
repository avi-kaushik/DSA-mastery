package programs.java.arrays.multidimensional;

/*
 * Program: Transpose of a Matrix (In-place)
 *
 * Approach:
 * We swap elements across the diagonal of the matrix.
 * That means:
 * - arr[i][j] becomes arr[j][i]
 * - We only swap elements where j > i to avoid double swapping
 *
 * Important:
 * This in-place approach only works for square matrices (n x n).
 * For non-square matrices, we cannot do this directly and need a new matrix.
 *
 * Example:
 * Input:
 * {
 *   {1, 2, 3},
 *   {4, 5, 6},
 *   {7, 8, 9}
 * }
 *
 * Output:
 * {
 *   {1, 4, 7},
 *   {2, 5, 8},
 *   {3, 6, 9}
 * }
 *
 * Time Complexity: O(n * n)
 * (we traverse upper triangle of the matrix)
 *
 * Space Complexity: O(1)
 * (no extra space used, in-place swapping)
 */

public class TransposeOfMatrix {

    /*
     * Function to transpose the matrix in-place and print it
     */
    static void printTransposeOfAMatrix(int arr[][]) {

        // check if matrix is square
        if (arr.length != arr[0].length) {
            System.out.println("Transpose in-place only works for square matrices");
            return;
        }

        int n = arr.length;

        // swap elements across diagonal
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                int temp = arr[i][j];

                arr[i][j] = arr[j][i];
                arr[j][i] = temp;
            }
        }

        // print result
        Print2DArray.printArray(arr);
    }

    public static void main(String[] args) {

        // sample input
        int arr[][] = {
                { 1, 2, 3, 4 },
                { 5, 6, 7, 8 },
                { 9, 10, 11, 12 },
                { 13, 14, 15, 16 }
        };

        printTransposeOfAMatrix(arr);
    }
}
