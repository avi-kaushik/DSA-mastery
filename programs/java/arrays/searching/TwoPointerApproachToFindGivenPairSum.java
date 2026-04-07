package programs.java.arrays.searching;

import java.util.Arrays;

/**
 * This class checks whether a pair with a given sum exists in a sorted array.
 * 
 * Approach: Two Pointer Technique
 * 
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 * 
 * IMPORTANT: The input array must be sorted.
 */
public class TwoPointerApproachToFindGivenPairSum {

    /**
     * Checks if there exists a pair in the array whose sum equals the given value.
     * 
     * @param arr Sorted input array
     * @param sum Target sum
     * @return true if such a pair exists, otherwise false
     */
    static boolean pairSumExists(int arr[], int sum) {
        int low = 0, high = arr.length - 1;
        int currentSum;

        /**
         * Continue until the two pointers meet
         */
        while (low < high) {
            currentSum = arr[low] + arr[high];

            /**
             * Case 1: Pair found
             */
            if (currentSum == sum)
                return true;

            /**
             * Case 2: Sum is too large → decrease it
             * Move high pointer left
             */
            else if (currentSum > sum) {
                high -= 1;
            }

            /**
             * Case 3: Sum is too small → increase it
             * Move low pointer right
             */
            else {
                low += 1;
            }
        }

        /**
         * No pair found
         */
        return false;
    }

    /**
     * Checks whether a triplet with the given sum exists in the array.
     *
     * IMPORTANT:
     * This algorithm ONLY works if the input array is sorted in non-decreasing
     * order.
     * If the array is not sorted, the two-pointer logic will fail.
     *
     * Approach:
     * - Fix one element (arr[i])
     * - Use two-pointer technique on the remaining array
     *
     * Time Complexity: O(n^2)
     * Space Complexity: O(1)
     */
    static boolean tripletPairSumExists(int arr[], int sum) {

        /**
         * Traverse the array and fix one element at a time
         */
        for (int i = 0; i < arr.length - 2; i++) {

            // Initialize two pointers for the remaining subarray
            int low = i + 1;
            int high = arr.length - 1;

            /**
             * Apply two-pointer approach to find remaining two elements
             */
            while (low < high) {

                int currentSum = arr[i] + arr[low] + arr[high];

                /**
                 * Case 1: Triplet found
                 */
                if (currentSum == sum)
                    return true;

                /**
                 * Case 2: Sum is too large → decrease it
                 * Move high pointer to the left
                 */
                else if (currentSum > sum)
                    high--;

                /**
                 * Case 3: Sum is too small → increase it
                 * Move low pointer to the right
                 */
                else
                    low++;
            }
        }

        /**
         * No triplet found
         */
        return false;
    }

    public static void main(String[] args) {
        int arr[] = { 1, 2, 3, 6, 12 }; // must be sorted
        int sum = 14;

        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("The pair sum " + sum + " " +
                (pairSumExists(arr, sum) ? "exists" : "doesn't exist") +
                " in the array");
        System.out.println("The triplet pair sum " + sum + " " +
                (tripletPairSumExists(arr, sum) ? "exists" : "doesn't exist") +
                " in the array");
    }
}
