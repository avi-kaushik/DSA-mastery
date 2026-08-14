package programs.java.hashing.basic;

import java.util.Arrays;
import java.util.HashSet;

public class PairWithTheGivenSum {

    /**
     * Checks whether a pair of elements exists whose sum is equal to the
     * given target sum.
     *
     * Uses a HashSet to store elements that have already been visited.
     * For each element, checks whether its required complement
     * (sum - element) already exists in the HashSet.
     *
     * Time Complexity: O(n) average
     * Space Complexity: O(n)
     *
     * @param arr array of elements
     * @param sum target sum
     * @return true if a pair with the given sum exists, otherwise false
     */
    public static boolean isPairExists(int[] arr, int sum) {
        HashSet<Integer> elements = new HashSet<>();

        for (int element : arr) {
            int required = sum - element;

            if (elements.contains(required))
                return true;

            // Store the current element for future complement checks.
            elements.add(element);
        }

        return false;
    }

    public static void main(String[] args) {
        int[] arr = { 2, 3, 8, 15, -1 };
        int sum = 17;

        System.out.println("Array: " + Arrays.toString(arr));

        System.out.printf(
                "A pair %s with the given sum %d%n",
                isPairExists(arr, sum) ? "exists" : "doesn't exist",
                sum);
    }
}
