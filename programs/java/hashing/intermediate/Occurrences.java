package programs.java.hashing.intermediate;

import java.util.HashMap;

public class Occurrences {

    /**
     * Prints elements occurring more than n/k times in the given array.
     *
     * Working:
     * - Use HashMap to store each element and its frequency.
     * - Calculate the occurrence threshold as n / k.
     * - Traverse the frequency map and print elements whose frequency
     * is greater than the threshold.
     *
     * @param arr input array
     * @param k   divisor used to calculate the occurrence threshold
     *
     *            Time Complexity: O(n) average
     *            Space Complexity: O(n)
     */
    public static void printMoreThanNByKOccurrences(int arr[], int k) {

        HashMap<Integer, Integer> frequencies = new HashMap<>();

        int threshold = arr.length / k;

        // Calculate the frequency of each element.
        for (int element : arr) {
            frequencies.put(
                    element,
                    frequencies.getOrDefault(element, 0) + 1);
        }

        // Print elements occurring more than n/k times.
        for (int element : frequencies.keySet()) {
            if (frequencies.get(element) > threshold)
                System.out.println(element);
        }
    }

    public static void main(String[] args) {

        int arr[] = { 10, 10, 20, 30, 20, 10, 10 };

        printMoreThanNByKOccurrences(arr, 2);
    }
}
