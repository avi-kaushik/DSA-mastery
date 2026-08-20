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

    public static void main(String[] args) {
        int arr[] = { 0, 0, 1, 1, 1, 1, 1, 0 };

        System.out.println("Arrays: " + Arrays.toString(arr));

        System.out.println("Longest Sub-array with equal number of 0s and 1s: " +
                getLongestSubArray(arr));
    }
}
