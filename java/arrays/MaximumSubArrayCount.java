import java.util.Arrays;

/**
 * This class provides a method to compute the
 * Maximum Subarray Sum using Kadane's Algorithm.
 */
public class MaximumSubArrayCount {

    /**
     * Finds the maximum possible sum of a contiguous subarray.
     *
     * Approach (Kadane's Algorithm):
     * --------------------------------
     * At every index, we decide:
     * 1. Start a new subarray from the current element.
     * 2. Extend the previous subarray by adding the current element.
     *
     * Recurrence Relation:
     * currentMax = max(arr[i], currentMax + arr[i])
     * globalMax = max(globalMax, currentMax)
     *
     * Why it works:
     * If the previous running sum becomes smaller than the current element,
     * it is better to start fresh from the current element.
     *
     * Time Complexity: O(n)
     * We traverse the array once.
     *
     * Space Complexity: O(1)
     * Only two variables are used for tracking.
     *
     * Edge Case:
     * Works correctly even if all elements are negative.
     *
     * @param arr Input integer array (can contain positive and negative numbers)
     * @return Maximum sum of any contiguous subarray
     */
    static int getMaximumSubArrayCount(int arr[]) {
        int sum = arr[0];
        int currentMax = arr[0];
        for (int i = 1; i < arr.length; i++) {
            currentMax = Math.max((currentMax + arr[i]), arr[i]);
            sum = Math.max(currentMax, sum);
        }
        return sum;
    }

    public static void main(String[] args) {
        int arr[] = { 6, -9, 3, 4 };

        System.out.println("Given array: " + Arrays.toString(arr));
        System.out.println("Maximum sub-array count: " + getMaximumSubArrayCount(arr));
    }
}
