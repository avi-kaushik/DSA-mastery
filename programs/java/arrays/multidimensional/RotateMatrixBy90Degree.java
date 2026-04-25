package programs.java.arrays.multidimensional;

/*
 * Program: Rotate Matrix by 90 Degrees (Clockwise and Anti-Clockwise)
 *
 * Approach:
 * We use transpose as the base operation, then reverse rows or columns.
 *
 * Key idea:
 * - Transpose converts rows into columns
 * - After that:
 *   - Reverse rows  -> clockwise rotation
 *   - Reverse columns -> anti-clockwise rotation
 *
 * Important:
 * This approach only works for square matrices (n x n).
 *
 * Example:
 * Input:
 * {
 *   {1, 2, 3},
 *   {4, 5, 6},
 *   {7, 8, 9}
 * }
 *
 * Clockwise Output:
 * {
 *   {7, 4, 1},
 *   {8, 5, 2},
 *   {9, 6, 3}
 * }
 *
 * Anti-Clockwise Output:
 * {
 *   {3, 6, 9},
 *   {2, 5, 8},
 *   {1, 4, 7}
 * }
 *
 * Time Complexity: O(n * n)
 * (transpose + reversal)
 *
 * Space Complexity: O(1)
 * (in-place modification)
 */

public class RotateMatrixBy90Degree {

    /*
     * Function to rotate matrix by 90 degrees anti-clockwise
     */
    static int[][] rotateMatrixBy90AntiClockwise(int arr[][]) throws Exception {

        // first step: transpose the matrix
        TransposeOfMatrix.convertToTranspose(arr);

        int n = arr.length;

        // second step: reverse each column
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n / 2; j++) {

                int temp = arr[j][i];

                arr[j][i] = arr[n - j - 1][i];
                arr[n - j - 1][i] = temp;

                // debug print (can be removed later)
                System.out.println("Swapping " + j + ", " + i + " with " + (n - j - 1) + ", " + i);
            }
        }

        return arr;
    }

    /*
     * Function to rotate matrix by 90 degrees clockwise
     */
    static int[][] rotateMatrixBy90Clockwise(int arr[][]) throws Exception {

        // first step: transpose the matrix
        TransposeOfMatrix.convertToTranspose(arr);

        int n = arr.length;

        // second step: reverse each row
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n / 2; j++) {

                int temp = arr[i][j];

                arr[i][j] = arr[i][n - j - 1];
                arr[i][n - j - 1] = temp;
            }
        }

        return arr;
    }

    public static void main(String[] args) throws Exception {

        // sample input
        int arr[][] = {
                { 1, 2, 3, 4 },
                { 5, 6, 7, 8 },
                { 9, 10, 11, 12 },
                { 13, 14, 15, 16 }
        };

        // call clockwise rotation
        rotateMatrixBy90Clockwise(arr);

        // print result
        Print2DArray.printArray(arr);
    }
}
