package programs.java.arrays.multidimensional;

/*
 * SpiralTraversal
 *
 * This class provides a method to print a 2D matrix in spiral order.
 * Spiral order means traversing the matrix layer by layer in the following sequence:
 * 1. Left to Right (top row)
 * 2. Top to Bottom (right column)
 * 3. Right to Left (bottom row)
 * 4. Bottom to Top (left column)
 *
 * Time Complexity: O(m * n)
 * Space Complexity: O(1)
 */
public class SpiralTraversal {

    /*
     * Prints the given 2D matrix in spiral order.
     *
     * @param arr Input 2D matrix
     */
    static void printSpiralMatrix(int arr[][]) {

        // Handle edge case: null or empty matrix
        if (arr == null || arr.length == 0 || arr[0].length == 0) {
            return;
        }

        // Initialize boundaries
        int top = 0; // starting row
        int bottom = arr.length - 1; // ending row
        int left = 0; // starting column
        int right = arr[0].length - 1; // ending column

        /*
         * Continue traversal while there are rows and columns remaining
         */
        while (top <= bottom && left <= right) {

            // Step 1: Traverse top row from left to right
            for (int i = left; i <= right; i++) {
                System.out.print(arr[top][i] + ", ");
            }

            // Step 2: Traverse right column from top to bottom
            for (int i = top + 1; i <= bottom; i++) {
                System.out.print(arr[i][right] + ", ");
            }

            /*
             * Step 3: Traverse bottom row from right to left
             * Only if top is less than bottom to avoid duplicate row traversal
             */
            if (top < bottom) {
                for (int i = right - 1; i >= left; i--) {
                    System.out.print(arr[bottom][i] + ", ");
                }
            }

            /*
             * Step 4: Traverse left column from bottom to top
             * Only if left is less than right to avoid duplicate column traversal
             */
            if (left < right) {
                for (int i = bottom - 1; i > top; i--) {
                    System.out.print(arr[i][left] + ", ");
                }
            }

            // Move inward to the next layer
            top++;
            bottom--;
            left++;
            right--;
        }

        // Print new line after traversal
        System.out.println();
    }

    public static void main(String[] args) {

        // Sample matrix
        int arr[][] = {
                { 1, 2, 3, 4 },
                { 5, 6, 7, 8 },
                { 9, 10, 11, 12 },
                { 13, 14, 15, 16 }
        };

        printSpiralMatrix(arr);
    }
}
