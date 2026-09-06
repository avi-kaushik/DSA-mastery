package programs.java.arrays.searching;

import java.util.Arrays;

/**
 * This program counts the number of occurrences of a given element
 * in a sorted array using Binary Search.
 *
 * Approach:
 * 1. Find the first occurrence of the element.
 * 2. Find the last occurrence of the element.
 * 3. Count = (lastOccurrence - firstOccurrence + 1)
 *
 * Time Complexity: O(log n)
 * Space Complexity: O(1)
 */
public class CountOccurenciesInSorted {

    /**
     * Counts how many times the number 'n' appears in a sorted array.
     *
     * @param arr Sorted integer array
     * @param n   Element whose occurrences we want to count
     * @return Total number of occurrences of n
     */
    static int countOccurrenciesInSortedArray(int arr[], int n) {

        // Find first occurrence of the element
        int firstOccurrence = FirstOccurrence.firstOccurrence(arr, n);

        // If element is not present in the array
        if (firstOccurrence == -1) {
            return 0;
        }

        // Find last occurrence of the element
        int lastOccurrence = LastOccurrence.lastOccurrence(arr, n);

        // Total count of the element
        return lastOccurrence - firstOccurrence + 1;
    }

    public static void main(String[] args) {

        int arr[] = { 10, 20, 20, 20, 30, 30 };
        int n = 20;

        System.out.println("Given array: " + Arrays.toString(arr));

        System.out.println(
                "The occurrence count of " + n + " is " + countOccurrenciesInSortedArray(arr, n));
    }
}
