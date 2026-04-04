package programs.java.arrays.searching;

import java.util.Arrays;

/**
 * This class demonstrates how to find a peak element in an array using Binary
 * Search.
 * 
 * A peak element is an element that is greater than or equal to its neighbors.
 * The array may contain multiple peaks; we can return any one of them.
 * 
 * Time Complexity: O(log n)
 * Space Complexity: O(1)
 */
public class PeakElement {

    /**
     * Finds a peak element in the given array.
     * 
     * @param arr Input array of integers
     * @return The value of a peak element
     */
    static int getPeakElement(int arr[]) {
        int low = 0, high = arr.length - 1;

        /**
         * We use binary search to reduce the search space.
         * At each step, we check the slope between arr[mid] and arr[mid + 1].
         */
        while (low < high) {
            int mid = (low + high) / 2;

            /**
             * Case 1: Increasing slope
             * arr[mid] < arr[mid + 1]
             * 
             * This means we are moving uphill, so a peak must exist
             * on the right side of mid.
             */
            if (arr[mid] < arr[mid + 1]) {
                low = mid + 1;
            }
            /**
             * Case 2: Decreasing slope
             * arr[mid] >= arr[mid + 1]
             * 
             * This means we are moving downhill, so a peak lies
             * on the left side OR at mid itself.
             * 
             * We include mid in the search space because it could be the peak.
             */
            else {
                high = mid;
            }
        }

        /**
         * When low == high, we have found a peak element.
         * Returning the value of the peak.
         */
        return arr[low];
    }

    public static void main(String[] args) {
        int arr[] = { 5, 10, 20, 15, 7 };

        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("The peak element in the given array is: " +
                getPeakElement(arr));
    }
}
