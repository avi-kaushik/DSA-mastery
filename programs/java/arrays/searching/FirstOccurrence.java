package programs.java.arrays.searching;

import java.util.Arrays;

/**
 * Program to find the FIRST OCCURRENCE of an element in a sorted array.
 *
 * This program demonstrates two approaches:
 *
 * 1. Recursive Binary Search
 * 2. Iterative Binary Search
 *
 * The goal is to find the first index where the target element appears,
 * even if duplicates exist in the array.
 *
 * Example:
 * Input Array: [5, 5, 5, 5]
 * Target: 5
 *
 * Output:
 * Index of first occurrence = 0
 *
 * Time Complexity: O(log n)
 * Space Complexity:
 * - Recursive: O(log n) (due to recursion stack)
 * - Iterative: O(1)
 */
public class FirstOccurrence {

    /**
     * Recursive Binary Search to find the first occurrence of an element.
     *
     * @param arr  Sorted input array
     * @param low  Starting index of the search space
     * @param high Ending index of the search space
     * @param n    Target element to search
     * @return Index of first occurrence of n, or -1 if not found
     */
    public static int searchElementRecursive(int arr[], int low, int high, int n) {

        // Base condition: search space becomes invalid
        if (low > high) {
            return -1;
        }

        // Calculate middle index
        int mid = (low + high) / 2;

        // Case 1: Target element found
        if (arr[mid] == n) {

            /*
             * Check if this is the first occurrence.
             * Two possibilities:
             *
             * 1. mid is the first index (mid == 0)
             * 2. Previous element is different
             *
             * If either is true, this is the first occurrence.
             */
            if (mid == 0 || arr[mid - 1] != arr[mid]) {
                return mid;
            }

            /*
             * If previous element is also equal,
             * then first occurrence must be on the LEFT side.
             */
            else {
                return searchElementRecursive(arr, low, mid - 1, n);
            }
        }

        // Case 2: Target is smaller → search LEFT half
        else if (arr[mid] > n) {
            return searchElementRecursive(arr, low, mid - 1, n);
        }

        // Case 3: Target is greater → search RIGHT half
        else {
            return searchElementRecursive(arr, mid + 1, high, n);
        }
    }

    /**
     * Iterative Binary Search to find the first occurrence.
     *
     * @param arr Sorted input array
     * @param n   Target element
     * @return Index of first occurrence of n, or -1 if not found
     */
    static int firstOccurrence(int arr[], int n) {

        int low = 0;
        int high = arr.length - 1;

        // Variable to store potential first occurrence
        int idx = -1;

        // Continue while search space is valid
        while (low <= high) {

            // Calculate middle index
            int mid = (low + high) / 2;

            // Case 1: Target found
            if (arr[mid] == n) {

                /*
                 * Store index and move LEFT
                 * to check if an earlier occurrence exists.
                 */
                idx = mid;
                high = mid - 1;
            }

            // Case 2: Target smaller → search LEFT half
            else if (arr[mid] > n) {
                high = mid - 1;
            }

            // Case 3: Target greater → search RIGHT half
            else {
                low = mid + 1;
            }
        }

        // Return first occurrence index
        return idx;
    }

    /**
     * Driver method to test the algorithm
     */
    public static void main(String[] args) {

        int arr[] = { 5, 5, 5, 5 };

        // Element to search
        int num = 5;

        // Print input array
        System.out.println("Given array: " + Arrays.toString(arr));

        // Print result using iterative method
        System.out.println("Index of " + num + " is " + firstOccurrence(arr, num));

        // Print result using recursive method
        System.out.println("Index of " + num + " (recursive) is "
                + searchElementRecursive(arr, 0, arr.length - 1, num));
    }
}
