package programs.java.arrays.multidimensional;

/*
 * RotateMatrixBy180Degree
 *
 * This class provides a method to rotate a 2D matrix by 180 degrees in place.
 *
 * A 180 degree rotation means:
 * - First row becomes last row (reversed)
 * - Second row becomes second last row (reversed), and so on
 *
 * Approach:
 * 1. Swap rows from top and bottom while reversing elements.
 * 2. If the matrix has an odd number of rows, reverse the middle row separately.
 *
 * Time Complexity: O(n * m)
 * Space Complexity: O(1)
 */
public class RotateMatrixBy180Degree {

    /*
     * Rotates the given 2D matrix by 180 degrees clockwise in place.
     *
     * @param arr Input 2D matrix
     * 
     * @return Rotated matrix (same reference, modified in place)
     */
    static int[][] rotateMatrixBy180Clockwise(int arr[][]) throws Exception {

        // Number of rows
        int n = arr.length;

        /*
         * Step 1:
         * Swap top and bottom rows while reversing their elements
         */
        for (int i = 0; i < arr.length / 2; i++) {

            int oppositeRow = n - i - 1;
            int len = arr[oppositeRow].length;

            for (int j = 0; j < len; j++) {

                // Swap element with its mirrored position
                int temp = arr[oppositeRow][len - j - 1];

                arr[oppositeRow][len - j - 1] = arr[i][j];
                arr[i][j] = temp;
            }
        }

        /*
         * Step 2:
         * If number of rows is odd, reverse the middle row
         */
        if (arr.length % 2 != 0) {

            int mid = arr.length / 2;
            int len = arr[mid].length;

            for (int i = 0; i < len / 2; i++) {

                int temp = arr[mid][len - i - 1];

                arr[mid][len - i - 1] = arr[mid][i];
                arr[mid][i] = temp;
            }
        }

        return arr;
    }

    public static void main(String[] args) throws Exception {

        // Sample input matrix
        int arr[][] = {
                { 1, 2, 3, 4 },
                { 5, 6, 7, 8 },
                { 9, 10, 11, 12 },
                { 13, 14, 15, 16 },
        };

        // Perform rotation
        rotateMatrixBy180Clockwise(arr);

        // Print result
        Print2DArray.printArray(arr);
    }
}
