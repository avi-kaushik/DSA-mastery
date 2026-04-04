/**
 * Demonstrates the Sliding Window Technique
 * to find the maximum sum of k consecutive elements in an array.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 */
public class MaximumSumSubarrayOfSizeK {

    /**
     * Returns the maximum sum of any k consecutive elements in the array.
     *
     * @param arr Input integer array
     * @param k   Size of the sliding window (number of consecutive elements)
     * @return Maximum sum of k consecutive elements
     */
    public static int getMaximumSumOfElements(int arr[], int k) {

        // Edge case: If window size is greater than array length
        if (arr.length < k)
            return 0;

        int sum = 0; // Stores maximum sum found so far
        int currentSum = 0; // Stores sum of current window

        // Calculate sum of first window (first k elements)
        for (int i = 0; i < k; i++) {
            currentSum += arr[i];
        }

        // Initialize max sum with first window sum
        sum = currentSum;

        // Slide the window from index k to end of array
        for (int i = k; i < arr.length; i++) {
            int elementLeaving = arr[i - k];

            // Update current window sum:
            // Remove element leaving & add new element entering
            currentSum = currentSum - elementLeaving + arr[i];

            sum = Math.max(sum, currentSum);
        }

        return sum;
    }

    public static void main(String[] args) {
        int k = 3;
        int arr[] = { 4, -1, 5, 1, 2, -8, 8, 1, 7, 4 };

        System.out.println("The maximum sum of " + k + " consecutive elements is: " +
                getMaximumSumOfElements(arr, k));
    }
}
