import java.util.Arrays;

/**
 * This class calculates the Maximum Circular Subarray Sum.
 *
 * A circular subarray means the array is considered circular,
 * so the subarray can wrap from the end to the beginning.
 *
 * Example:
 * {5, -3, 5}
 * Circular maximum subarray = 5 + 5 = 10
 */
public class MaximumCircularSubArrayCount {

    /**
     * Returns the maximum circular subarray sum.
     *
     * Approach:
     * 1. Use Kadane’s Algorithm to find normal maximum subarray sum.
     * 2. Use modified Kadane to find minimum subarray sum.
     * 3. Total sum of array - minimum subarray sum
     * gives maximum circular subarray sum.
     * 4. Handle edge case when all elements are negative.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    static int getMaximumSubArrayCount(int arr[]) {

        int sum = arr[0]; // Stores final maximum normal subarray sum
        int currentMax = arr[0]; // Running maximum sum (Kadane)

        int min = arr[0]; // Stores final minimum subarray sum
        int currentMin = arr[0]; // Running minimum sum

        int total = arr[0]; // Stores total sum of array

        for (int i = 1; i < arr.length; i++) {

            // Normal Kadane's Algorithm (Maximum Subarray)
            currentMax = Math.max(currentMax + arr[i], arr[i]);
            sum = Math.max(sum, currentMax);

            // Modified Kadane's Algorithm (Minimum Subarray)
            currentMin = Math.min(currentMin + arr[i], arr[i]);
            min = Math.min(min, currentMin);

            // Add to total sum
            total += arr[i];
        }

        // If all elements are negative,
        // total - min becomes 0 (incorrect).
        // So return normal maximum.
        if (sum < 0)
            return sum;

        // 1. Normal Maximum Subarray
        // 2. Circular Maximum Subarray (total - min)
        return Math.max(sum, total - min);
    }

    public static void main(String[] args) {
        int arr[] = { 6, -5, 3, 4, -11, 5, 1, 7 };

        System.out.println("Given array: " + Arrays.toString(arr));
        System.out.println("Maximum circular sub-array count: " + getMaximumSubArrayCount(arr));
    }
}
