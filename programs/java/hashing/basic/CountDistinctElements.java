package programs.java.hashing.basic;

import java.util.Arrays;
import java.util.HashSet;

public class CountDistinctElements {

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

    public static void main(String[] args) {
        int[] arr = { 10, 20, 10, 20, 30 };

        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("Total distinct elements: " + getTotalDistinctElements(arr));
    }
}
