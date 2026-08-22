package programs.java.hashing.intermediate;

import java.util.Arrays;
import java.util.HashSet;

public class LongestConsecutiveSubsequences {

    /**
     * Finds the length of the longest consecutive subsequence.
     *
     * Working:
     * - Store all elements in a HashSet for O(1) average lookup.
     * - Treat an element as the start of a sequence only if element - 1
     * does not exist in the HashSet.
     * - Keep checking consecutive elements and track the maximum length.
     *
     * Time Complexity: O(n) average
     * Space Complexity: O(n)
     *
     * @param arr input array
     * @return length of the longest consecutive subsequence
     */
    public static int getLongestConsecutiveSubsequence(int arr[]) {

        HashSet<Integer> subsequences = new HashSet<>();

        for (int element : arr) {
            subsequences.add(element);
        }

        int longest = 0;

        for (int element : subsequences) {

            // Start counting only from the beginning of a sequence.
            if (!subsequences.contains(element - 1)) {

                int current = 1;

                while (subsequences.contains(element + current)) {
                    current++;
                }

                longest = Math.max(longest, current);
            }
        }

        return longest;
    }

    public static void main(String[] args) {
        int arr[] = { 2, 9, 4, 3, 10 };

        System.out.println("Arrays: " + Arrays.toString(arr));
        System.out.println("Longest consecutive subsequence: " +
                getLongestConsecutiveSubsequence(arr));
    }
}
