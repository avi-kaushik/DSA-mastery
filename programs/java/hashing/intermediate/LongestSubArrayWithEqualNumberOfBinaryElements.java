package programs.java.hashing.intermediate;

import java.util.Arrays;

public class LongestSubArrayWithEqualNumberOfBinaryElements {

    /**
     * Finds the length of the longest subarray containing an equal number of 0s and
     * 1s.
     *
     * Converts 0s to -1s so that a subarray with equal 0s and 1s has a sum of 0,
     * then uses the longest subarray with given sum logic.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public static int getLongestSubArray(int arr[]) {

        // Convert 0s to -1s so equal 0s and 1s produce a sum of 0.
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0)
                arr[i] = -1;
        }

        return SubArrayWithSums.longestSubArrayWithGivenSum(arr, 0);
    }

    /**
     * Problem:
     * --------
     * Finds the length of the longest sub-array having the same sum
     * in two arrays.
     *
     * Approach:
     * ---------
     * Use the Brute Force approach.
     *
     * For every possible starting index, calculate the sum of the
     * corresponding sub-arrays in both arrays.
     *
     * If both sums are equal, calculate the length of the current
     * sub-array and update the maximum length.
     *
     * Time Complexity: O(n^2)
     * Space Complexity: O(1)
     */
    public static int longestSubArraySpanWithSameSumBruteForce(int arr1[], int arr2[]) {

        int res = 0;

        for (int i = 0; i < arr1.length; i++) {

            int sum1 = 0;
            int sum2 = 0;

            for (int j = i; j < arr2.length; j++) {

                sum1 += arr1[j];
                sum2 += arr2[j];

                if (sum1 == sum2) {
                    res = Math.max(res, j - i + 1);
                }
            }
        }

        return res;
    }

    /**
     * Problem:
     * --------
     * Finds the length of the longest sub-array having the same sum
     * in two arrays.
     *
     * Approach:
     * ---------
     * Transform the problem into a longest zero-sum sub-array problem.
     *
     * The required condition is:
     *
     * sum(arr1[i...j]) == sum(arr2[i...j])
     *
     * Move both sums to one side:
     *
     * sum(arr1[i...j]) - sum(arr2[i...j]) == 0
     *
     * Create a difference array:
     *
     * arr[i] = arr1[i] - arr2[i]
     *
     * Therefore:
     *
     * Same sum in arr1 and arr2
     * |
     * Difference array
     * |
     * Longest zero-sum sub-array
     *
     * The longestSubArrayWithGivenSum() method uses Prefix Sum + HashMap
     * to find the longest sub-array with sum 0.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public static int longestSubArraySpanWithSameSum(int arr1[], int arr2[]) {

        int arr[] = new int[arr1.length];

        // Create a difference array.
        for (int i = 0; i < arr1.length; i++) {
            arr[i] = arr1[i] - arr2[i];
        }

        // Find the longest zero-sum sub-array in the difference array.
        return SubArrayWithSums.longestSubArrayWithGivenSum(arr, 0);
    }

    public static void main(String[] args) {
        int arr[] = { 0, 0, 1, 1, 1, 1, 1, 0 };

        System.out.println("Arrays: " + Arrays.toString(arr));

        System.out.println("Longest Sub-array with equal number of 0s and 1s: " +
                getLongestSubArray(arr));

        int arr1[] = { 0, 1, 0, 0, 0, 0 };
        int arr2[] = { 1, 0, 1, 0, 0, 1 };

        System.out.println("Longest Sub-array with span with same sum: "
                + longestSubArraySpanWithSameSum(arr1, arr2));
    }
}
