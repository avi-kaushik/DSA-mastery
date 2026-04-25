package programs.java.arrays.multidimensional;

/*
 * Program: Print Boundary of a Matrix
 *
 * Approach:
 * We print only the boundary elements of the matrix.
 * Boundary includes:
 * - Top row
 * - Right column
 * - Bottom row (in reverse)
 * - Left column (in reverse)
 *
 * Important:
 * We handle edge cases separately to avoid duplicate printing.
 *
 * Cases:
 * 1. If there is only one row, print the entire row.
 * 2. If there is only one column, print all elements top to bottom.
 * 3. Otherwise:
 *    - Print top row (left to right)
 *    - Print right column (excluding first and last element)
 *    - Print bottom row (right to left)
 *    - Print left column (excluding first and last element, bottom to top)
 *
 * Example:
 * Input:
 * {
 *   {1, 2, 3, 4},
 *   {5, 6, 7, 8},
 *   {9, 10, 11, 12}
 * }
 *
 * Output:
 * 1 2 3 4 8 12 11 10 9 5
 *
 * Time Complexity: O(m * n)
 * (we may traverse most elements once)
 *
 * Space Complexity: O(1)
 * (no extra space used)
 */

public class PrintMatrixBoundary {

    /*
     * Function to print boundary elements of matrix
     */
    static void printMatrixBoundary(int arr[][]) {

        int rows = arr.length, columns = arr[0].length;

        // case 1: single row
        if (rows == 1) {
            for (int i = 0; i < columns; i++) {
                System.out.print(arr[0][i] + " ");
            }
        }

        // case 2: single column
        else if (columns == 1) {
            for (int i = 0; i < rows; i++) {
                System.out.print(arr[i][0] + " ");
            }
        }

        // general case
        else {

            // top row
            for (int i = 0; i < columns; i++) {
                System.out.print(arr[0][i] + " ");
            }

            // right column (excluding corners)
            for (int i = 1; i < rows - 1; i++) {
                System.out.print(arr[i][columns - 1] + " ");
            }

            // bottom row (reverse)
            for (int i = columns - 1; i >= 0; i--) {
                System.out.print(arr[rows - 1][i] + " ");
            }

            // left column (reverse, excluding corners)
            for (int i = rows - 2; i > 0; i--) {
                System.out.print(arr[i][0] + " ");
            }
        }

        System.out.println();
    }

    public static void main(String[] args) {

        // sample input
        int arr[][] = {
                { 1, 2, 3, 4 },
                { 5, 6, 7, 8 },
                { 9, 10, 11, 12 }
        };

        printMatrixBoundary(arr);
    }
}
