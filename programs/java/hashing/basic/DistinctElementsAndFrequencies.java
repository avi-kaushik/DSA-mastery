package programs.java.hashing.basic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DistinctElementsAndFrequencies {

    /**
     * Returns the total number of distinct elements in the given array.
     *
     * Uses a HashSet to store unique elements. Since HashSet does not
     * allow duplicate elements, its size represents the total number
     * of distinct elements.
     *
     * Time Complexity: O(n) average
     * Space Complexity: O(n)
     *
     * @param arr array of elements
     * @return total number of distinct elements
     */
    public static int getTotalDistinctElements(int[] arr) {
        HashSet<Integer> elements = new HashSet<>();

        for (int element : arr) {
            // Duplicate elements are ignored by HashSet.
            elements.add(element);
        }

        return elements.size();
    }

    /**
     * Returns the total number of distinct elements present in both arrays.
     *
     * Uses a HashSet to store elements from both arrays. Since HashSet
     * does not allow duplicate elements, its size represents the total
     * number of distinct elements in the union of both arrays.
     *
     * Time Complexity: O(n + m) average
     * Space Complexity: O(n + m)
     *
     * @param arr1 first array
     * @param arr2 second array
     * @return total number of distinct elements in both arrays
     */
    public static int getUnionTotalDistinctElements(int[] arr1, int[] arr2) {
        HashSet<Integer> elements = new HashSet<>();

        // Add elements from the first array to the HashSet.
        for (int i : arr1)
            elements.add(i);

        // Add elements from the second array to the same HashSet.
        for (int i : arr2)
            elements.add(i);

        return elements.size();
    }

    /**
     * Returns the current frequency of the given value.
     *
     * Returns 0 if the value does not exist in the frequency map.
     *
     * Time Complexity: O(1) average
     *
     * @param frequencies map containing element frequencies
     * @param value       element whose frequency is required
     * @return current frequency of the given value
     */
    private static int getFrequency(HashMap<Integer, Integer> frequencies, int value) {
        return frequencies.containsKey(value) ? frequencies.get(value) : 0;
    }

    /**
     * Calculates and prints the frequency of each distinct element
     * in the given array.
     *
     * Uses a HashMap where each element is stored as a key and its
     * frequency is stored as the corresponding value.
     *
     * Time Complexity: O(n) average
     * Space Complexity: O(n)
     *
     * @param arr array of elements
     */
    public static void printFrequencies(int[] arr) {
        HashMap<Integer, Integer> frequencies = new HashMap<>();

        for (int value : arr) {
            frequencies.put(value, getFrequency(frequencies, value) + 1);
        }

        System.out.println("Frequencies: ");

        for (Map.Entry<Integer, Integer> m : frequencies.entrySet()) {
            System.out.println(m.getKey() + " = " + m.getValue());
        }
    }

    public static void main(String[] args) {
        int[] arr = { 10, 20, 10, 20, 30 };

        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("Total distinct elements: " + getTotalDistinctElements(arr));
        System.out.println();

        printFrequencies(arr);
    }
}
