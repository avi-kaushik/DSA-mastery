package programs.java.stack.intermediate;

import programs.java.arrays.multidimensional.Print2DArray;

public class LargestRectangleWithOnes {

    /**
     * Finds the largest rectangular area consisting of only 1's in a binary matrix.
     *
     * Approach:
     * - Treat each row as a base of a histogram.
     * - Build a running histogram where each column stores consecutive 1's height.
     * - For each row's histogram, compute the largest rectangle area using
     * the Largest Rectangle in Histogram (monotonic stack) approach.
     * - Track the maximum area across all rows.
     *
     * Time Complexity: O(rows * cols)
     * Space Complexity: O(cols)
     */
    static int getLargestRectangleWithOnes(int[][] arr) {

        // Number of columns in matrix
        int cols = arr[0].length;

        // Histogram array storing consecutive 1's height for each column
        int[] height = new int[cols];

        // Stores the maximum rectangle area found
        int largest = 0;

        // Traverse each row and build histogram step by step
        for (int i = 0; i < arr.length; i++) {

            // Update histogram based on current row values
            for (int j = 0; j < cols; j++) {
                if (arr[i][j] == 1)
                    height[j] += 1;
                else
                    height[j] = 0;
            }

            // Compute largest rectangle area for current histogram
            int units = LargestRectangularAreaInHistogram.getLargestAreaV2(height);

            // Update global maximum
            largest = Math.max(largest, units);
        }

        // Return final result
        return largest;
    }

    public static void main(String[] args) {

        int[][] arr = {
                { 0, 1, 1, 0 },
                { 1, 1, 1, 1 },
                { 1, 1, 1, 1 },
                { 1, 1, 0, 0 },
        };

        System.out.println("Array");
        Print2DArray.printArray(arr);

        System.out.println("Largest rectangle with 1s: " +
                getLargestRectangleWithOnes(arr));
    }
}
