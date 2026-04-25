package programs.java.arrays.multidimensional;

/*
 * Program: Print 2D Array in Snake Pattern
 *
 * Approach:
 * We go through the matrix row by row.
 * - For even index rows, we print elements from left to right.
 * - For odd index rows, we print elements from right to left.
 *
 * This way, the output looks like a snake or zig-zag pattern.
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
 * { 1, 2, 3, 4 }
 * { 8, 7, 6, 5 }
 * { 9, 10, 11, 12 }
 *
 * Time Complexity: O(m * n)
 * where m = number of rows and n = number of columns
 *
 * Space Complexity: O(1)
 * No extra space is used, we are just printing values
 */

public class PrintInSnakePattern {

    /*
     * Function to print the matrix in snake pattern
     */
    static void printSnakePattern(int arr[][]) {

        // loop through each row
        for (int i = 0; i < arr.length; i++) {

            System.out.print("{ ");

            // even row, print left to right
            if (i % 2 == 0) {
                for (int j = 0; j < arr[i].length; j++) {
                    // avoid extra comma at the end
                    System.out.print(arr[i][j] + (j != arr[i].length - 1 ? ", " : ""));
                }
            }
            // odd row, print right to left
            else {
                for (int j = arr[i].length - 1; j >= 0; j--) {
                    // avoid extra comma at the end
                    System.out.print(arr[i][j] + (j != 0 ? ", " : ""));
                }
            }

            System.out.println(" }");
        }
    }

    public static void main(String[] args) {

        // sample input matrix
        int arr[][] = {
                { 1, 2, 3, 4 },
                { 5, 6, 7, 8 },
                { 9, 10, 11, 12 }
        };

        printSnakePattern(arr);
    }
}
