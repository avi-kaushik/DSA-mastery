package programs.java.arrays.searching;

import java.util.Arrays;

/**
 * Problem:
 * Search an element in an Infinite Sorted Array.
 *
 * Since the array is considered "infinite", we cannot rely on the array length
 * to perform a normal binary search across the entire array.
 *
 * Approach:
 * 1. First identify a range where the element may exist.
 * 2. Expand the range exponentially (1, 2, 4, 8, 16...).
 * 3. Once the range is found, apply Binary Search within that range.
 *
 * This approach avoids scanning the entire array and keeps the
 * time complexity efficient.
 *
 * Time Complexity:
 * Range Expansion : O(log n)
 * Binary Search : O(log n)
 * Overall : O(log n)
 *
 * Space Complexity:
 * O(1)
 */
public class InfiniteSortedArray {

    /**
     * Searches an element in a sorted infinite array.
     *
     * @param arr Sorted array
     * @param x   Element to search
     * @return Index of the element if found, otherwise -1
     */
    static int getInfiniteSortedArray(int arr[], int x) {

        // Edge case: if the element is at index 0
        if (arr[0] == x)
            return 0;

        // Start with index 1 and expand range exponentially
        int i = 1;

        /*
         * Keep doubling the index until:
         * 1. We go beyond array bounds OR
         * 2. We find an element greater than or equal to x
         */
        while (i < arr.length && arr[i] < x) {
            i *= 2;
        }

        /*
         * Once the range is identified, perform binary search
         * between i/2 and i
         */
        int low = i / 2;
        int high = Math.min(i, arr.length - 1);

        return RecursiveBinarySearch.searchElement(arr, low, high, x);
    }

    public static void main(String[] args) {

        int arr[] = { 1, 10, 15, 20, 40, 50, 90, 100, 120, 500 };
        int x = 120;

        System.out.println("Given array: " + Arrays.toString(arr));

        int result = getInfiniteSortedArray(arr, x);

        if (result != -1)
            System.out.println("Element " + x + " found at index: " + result);
        else
            System.out.println("Element " + x + " not found in the array.");
    }
}
