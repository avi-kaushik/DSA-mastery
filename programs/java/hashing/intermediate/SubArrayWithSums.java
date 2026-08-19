package programs.java.hashing.intermediate;

import java.util.Arrays;
import java.util.HashSet;

public class SubArrayWithSums {

    /*
     * Problem:
     * --------
     * Given an array, determine whether there exists a sub-array
     * whose sum is equal to 0.
     *
     * 
     * Approach:
     * ---------
     * Use Prefix Sum + HashSet.
     *
     * Prefix Sum:
     * The sum of all elements from index 0 to the current index.
     *
     * If the same prefix sum occurs twice, then the elements
     * between those two positions must have a sum of 0.
     *
     *
     * Special Case:
     * -------------
     * If the current prefix sum itself becomes 0,
     * then the sub-array from index 0 to the current index
     * has sum 0.
     *
     *
     * Time Complexity:
     * ----------------
     * O(n)
     *
     * We traverse the array once.
     *
     *
     * Space Complexity:
     * -----------------
     * O(n)
     *
     * In the worst case, all prefix sums can be different.
     */
    public static boolean isSubArrayWithZeroSumPresent(int[] arr) {

        HashSet<Integer> prefixSums = new HashSet<>();

        int previousSum = 0;

        for (int num : arr) {

            // Calculate current prefix sum
            previousSum += num;

            // If prefix sum is 0, sub-array from index 0 has sum 0.
            // If prefix sum already exists, the elements between
            // the previous occurrence and current occurrence sum to 0.
            if (previousSum == 0 || prefixSums.contains(previousSum)) {
                return true;
            }

            // Store prefix sum for future comparisons
            prefixSums.add(previousSum);
        }

        return false;
    }

    /*
     * Problem:
     * --------
     * Given an array and a target sum, determine whether there exists
     * a sub-array whose sum is equal to the given sum.
     *
     *
     * Approach:
     * ---------
     * Use Prefix Sum + HashSet.
     *
     * 
     * Let:
     *
     * currentPrefixSum - previousPrefixSum = targetSum
     *
     * Therefore:
     *
     * previousPrefixSum = currentPrefixSum - targetSum
     *
     * For every current prefix sum, check whether
     * (currentPrefixSum - targetSum) already exists in the HashSet.
     *
     * If it exists, then the elements between the previous prefix sum
     * and current prefix sum form a sub-array whose sum is equal
     * to the target sum.
     *
     *
     * Why HashSet?
     * ------------
     * We only need to know whether a prefix sum has appeared before.
     * We don't need to store its index.
     *
     * HashSet provides average O(1) lookup.
     *
     *
     * Time Complexity:
     * ----------------
     * O(n)
     *
     * The array is traversed only once.
     *
     *
     * Space Complexity:
     * -----------------
     * O(n)
     *
     * In the worst case, all prefix sums can be different.
     */
    public static boolean isSubArrayWithGivenSumPresent(int[] arr, int sum) {

        HashSet<Integer> prefixSums = new HashSet<>();

        int previousSum = 0;

        for (int num : arr) {

            // Calculate current prefix sum
            previousSum += num;

            /*
             * Check two possibilities:
             *
             * 1. Current prefix sum equals the target sum.
             * -> Sub-array starts from index 0.
             *
             * 2. A previous prefix sum exists such that:
             *
             * currentPrefixSum - previousPrefixSum = targetSum
             *
             * -> Sub-array starts somewhere after index 0.
             */
            if (previousSum == sum || prefixSums.contains(previousSum - sum)) {
                return true;
            }

            // Store prefix sum for future comparisons
            prefixSums.add(previousSum);
        }

        return false;
    }

    public static void main(String[] args) {

        int[] arr = { 1, 4, 13, -3, -10, 5 };

        System.out.println("Array: " + Arrays.toString(arr));

        System.out.println(
                "Is sub-array present with 0 sum: "
                        + isSubArrayWithZeroSumPresent(arr));

        System.out.println(
                "Is sub-array present with sum 14: "
                        + isSubArrayWithGivenSumPresent(arr, 14));
    }
}
