package programs.java.techniques.recursion.intermediate;

import java.util.Arrays;

public class GenerateSubset {

    public static void generate(String s) {
        generateSubsets(s, "", 0);
    }

    /**
     * Generate all possible subsets (Power Set) of a given String.
     *
     * Approach:
     * 1. For every character, recursively explore the possibility of excluding it.
     * 2. Recursively explore the possibility of including it.
     * 3. Once all characters have been processed, print the constructed subset.
     *
     * Time Complexity: O(n × 2ⁿ)
     * Space Complexity: O(n)
     *
     * @param s       Input string.
     * @param current Subset constructed so far.
     * @param i       Index of the character currently being processed.
     * 
     */
    private static void generateSubsets(String s, String current, int i) {

        // Print the constructed subset after processing all characters
        if (i == s.length()) {
            System.out.print(current + " ");
            return;
        }

        // Exclude the current character
        generateSubsets(s, current, i + 1);

        // Include the current character
        generateSubsets(s, current + s.charAt(i), i + 1);
    }

    /**
     * Print all possible subsets (Power Set) of the given String.
     *
     * @param s Input string.
     */
    public static void printSubsets(String s) {
        System.out.print("Subsets of " + s + ": ");

        generate(s);

        System.out.println();
    }

    public static int getTotalSubsetsWithGivenSum(int[] arr, int sum) {
        return getTotalSubsetsWithGivenSum(arr, arr.length, sum);
    }

    /**
     * Count the total number of subsets whose sum is equal to the given target.
     *
     * Approach:
     * 1. For every element, recursively explore the possibility of excluding it.
     * 2. Recursively explore the possibility of including it in the subset.
     * 3. If all elements have been processed, return:
     * - 1 if the required sum has been formed.
     * - 0 otherwise.
     * 4. The total number of valid subsets is the sum of both recursive calls.
     *
     * Time Complexity: O(2ⁿ)
     * Space Complexity: O(n)
     *
     * @param arr Input array.
     * @param i   Number of elements currently being considered.
     * @param sum Target sum to be formed.
     * @return Total number of subsets whose sum equals the given target.
     */
    private static int getTotalSubsetsWithGivenSum(int[] arr, int i, int sum) {

        // If no elements are left,
        // return 1 if the required sum has been formed; otherwise return 0.
        if (i == 0)
            return sum == 0 ? 1 : 0;

        // Exclude the current element
        int exclude = getTotalSubsetsWithGivenSum(arr, i - 1, sum);

        // Include the current element
        int include = getTotalSubsetsWithGivenSum(arr, i - 1, sum - arr[i - 1]);

        // Total subsets = Exclude + Include
        return exclude + include;
    }

    public static void main(String[] args) {
        printSubsets("ABC");
        System.out.println();

        int[] arr = { 10, 5, 2, 3, 6 };

        System.out.println("Array with subsets: " + Arrays.toString(arr));
        System.out.println("Total subsets with sum of 8 are " + getTotalSubsetsWithGivenSum(arr, 8));
    }
}
