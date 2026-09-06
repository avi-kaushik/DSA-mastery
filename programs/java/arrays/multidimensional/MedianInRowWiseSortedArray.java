package programs.java.arrays.multidimensional;

/*
 * MedianInRowWiseSortedArray
 *
 * This class provides a method to find the median
 * in a row-wise sorted matrix.
 *
 * Row-wise sorted means:
 * - Every individual row is sorted
 * - Entire matrix is NOT necessarily sorted
 *
 * Example:
 *
 * 1 3 5
 * 2 6 9
 * 3 6 9
 *
 * Sorted order:
 * 1 2 3 3 5 6 6 9 9
 *
 * Median = 5
 *
 * Approach:
 *
 * We use Binary Search on Answer.
 *
 * Instead of searching for the median directly,
 * we search on the value range between:
 *
 * - Minimum element in matrix
 * - Maximum element in matrix
 *
 * For every mid value:
 *
 * 1. Count how many elements are <= mid
 * 2. If count is smaller than required median position,
 *    move right.
 * 3. Otherwise move left while keeping mid.
 *
 * To count efficiently:
 * - Since every row is sorted,
 *   we use Upper Bound on each row.
 *
 * Upper Bound:
 * - First index having value > target
 * - This index also equals:
 *   count of elements <= target
 *
 * Time Complexity:
 * O(log(max-min) * r * log(c))
 *
 * Space Complexity:
 * O(1)
 */
public class MedianInRowWiseSortedArray {

    /*
     * Finds the median in a row-wise sorted matrix.
     *
     * @param arr Input row-wise sorted matrix
     *
     * @return Median element
     */
    static int median(int arr[][]) {

        int r = arr.length;
        int c = arr[0].length;

        /*
         * Minimum possible median can exist between:
         * - smallest first element
         * - largest last element
         *
         * because every row is sorted.
         */
        int low = Integer.MAX_VALUE;
        int high = Integer.MIN_VALUE;

        /*
         * Find search space for binary search.
         */
        for (int i = 0; i < r; i++) {

            low = Math.min(low, arr[i][0]);
            high = Math.max(high, arr[i][c - 1]);
        }

        /*
         * Required median position.
         *
         * Example:
         * Total elements = 9
         * Median position = 5
         */
        int desired = (r * c + 1) / 2;

        /*
         * Binary Search on Answer.
         *
         * Goal:
         * Find the smallest value whose
         * count of smaller/equal elements
         * becomes >= desired.
         */
        while (low < high) {

            int mid = low + (high - low) / 2;

            /*
             * Count how many elements
             * are <= mid.
             */
            int count = 0;

            for (int i = 0; i < r; i++) {

                /*
                 * upperBound returns:
                 * count of elements <= mid
                 * in current row.
                 */
                count += upperBound(arr[i], mid);
            }

            /*
             * If count is smaller than required,
             * median exists on right side.
             */
            if (count < desired) {
                low = mid + 1;
            }

            /*
             * Otherwise:
             * - mid may itself be the answer
             * - or answer may exist on left side
             *
             * So keep mid.
             */
            else {
                high = mid;
            }
        }

        /*
         * low and high converge
         * to the median.
         */
        return low;
    }

    /*
     * Finds Upper Bound in a sorted array.
     *
     * Upper Bound:
     * First index having value > target
     *
     * This index also equals:
     * count of elements <= target
     *
     * Example:
     *
     * arr = [1,3,5,5,7]
     * target = 5
     *
     * Upper Bound index = 4
     *
     * because:
     * arr[4] = 7 > 5
     *
     * Also:
     * 4 elements are <= 5
     *
     * @param arr Sorted array
     * 
     * @param value Target value
     *
     * @return Upper Bound index
     */
    static int upperBound(int arr[], int value) {

        /*
         * Search space:
         * [0, arr.length]
         *
         * We use arr.length instead of
         * arr.length - 1 because upper bound
         * may lie after the last element.
         */
        int low = 0;
        int high = arr.length;

        /*
         * Find first index
         * where arr[index] > value
         */
        while (low < high) {

            int mid = low + (high - low) / 2;

            /*
             * Current element belongs to
             * left side of upper bound.
             *
             * So discard left including mid.
             */
            if (arr[mid] <= value) {
                low = mid + 1;
            }

            /*
             * Current element is greater than value.
             *
             * mid may be the first valid index,
             * so keep it and move left.
             */
            else {
                high = mid;
            }
        }

        /*
         * low becomes:
         * first index having value > target
         */
        return low;
    }

    public static void main(String[] args) {

        int[][] matrix = {
                { 1, 3, 5 },
                { 2, 6, 9 },
                { 3, 6, 9 }
        };

        System.out.println(median(matrix));
    }
}
