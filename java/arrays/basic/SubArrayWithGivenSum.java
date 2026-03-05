import java.util.Arrays;

/*
 * Problem:
 * Check whether a subarray with a given sum exists in the array.
 *
 * Important Condition:
 * This solution works ONLY when all numbers in the array are positive.
 *
 * Approach Used:
 * Sliding Window Technique (Two Pointers)
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 */
public class SubArrayWithGivenSum {

    /*
     * This method checks if there exists a continuous subarray
     * whose sum is equal to the given target.
     *
     * How it works:
     * - We maintain a window using two pointers:
     * start → beginning of the window
     * end → end of the window
     *
     * - We keep expanding the window by increasing 'end'
     * - If the sum becomes greater than target,
     * we shrink the window from the left (increase 'start')
     * - If at any point sum == target, we return true
     */
    public static boolean validateSumInSubArray(int arr[], int target) {
        int currentSum = 0, start = 0;

        for (int end = 0; end < arr.length; end++) {
            currentSum += arr[end];

            // If sum exceeds target, shrink window from the left until currentSum <= target
            while (currentSum > target) {
                currentSum -= arr[start];
                start++;
            }

            // Check if we found the target sum
            if (currentSum == target) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int arr[] = { 4, 2, 5, 7, 1, 4, 6, 7 };
        int sum = 13;

        System.out.println("Given array: " + Arrays.toString(arr));

        boolean exists = validateSumInSubArray(arr, sum);

        System.out.println("Sum %d %s in a subarray."
                .formatted(sum, exists ? "exists" : "does not exist"));
    }
}
