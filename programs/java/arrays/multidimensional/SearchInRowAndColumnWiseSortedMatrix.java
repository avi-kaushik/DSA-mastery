package programs.java.arrays.multidimensional;

public class SearchInRowAndColumnWiseSortedMatrix {

    /*
     * This function searches a number in a matrix
     * where:
     * 1. Each row is sorted
     * 2. Each column is sorted
     * 
     * Approach:
     * Start from top-right corner.
     * 
     * Why top-right?
     * - Left side contains smaller values
     * - Bottom side contains greater values
     * 
     * So:
     * - If current value is greater than target,
     * move left
     * - If current value is smaller than target,
     * move down
     * - If equal, return true
     */
    static boolean searchInRowAndColumnWiseSortedMatrix(int arr[][], int num) {

        // Total number of rows
        int rows = arr.length;

        // Total number of columns
        int cols = arr[0].length;

        // Start from first row
        int i = 0;

        // Start from last column
        int j = cols - 1;

        /*
         * Continue until:
         * 1. Row index stays inside matrix
         * 2. Column index stays inside matrix
         */
        while (i < rows && j >= 0) {

            // If target found
            if (arr[i][j] == num) {
                return true;
            }

            /*
             * If current value is greater than target,
             * move left because left side contains
             * smaller values
             */
            else if (arr[i][j] > num) {
                j--;
            }

            /*
             * If current value is smaller than target,
             * move down because bottom side contains
             * greater values
             */
            else {
                i++;
            }
        }

        // Target not found
        return false;
    }

    public static void main(String[] args) {

        int arr[][] = {
                { 10, 20, 30, 40 },
                { 15, 25, 35, 45 },
                { 27, 29, 37, 48 },
                { 32, 33, 39, 50 },
        };

        System.out.println("Element found: " +
                searchInRowAndColumnWiseSortedMatrix(arr, 37));
    }
}
