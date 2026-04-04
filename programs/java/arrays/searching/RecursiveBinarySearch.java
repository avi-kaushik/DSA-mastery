package programs.java.arrays.searching;

import java.util.Arrays;

/**
 * RecursiveBinarySearch demonstrates the Binary Search algorithm
 * using a recursive approach.
 *
 * Binary Search works only on a sorted array.
 * The algorithm repeatedly divides the search space in half
 * until the target element is found or the search range becomes invalid.
 *
 * Algorithm Steps:
 * 1. Define the search boundaries using low and high indices
 * 2. Find the middle index of the current range
 * 3. Compare the middle element with the target
 * - If equal → element found
 * - If middle element is greater → search left half
 * - If middle element is smaller → search right half
 * 4. Continue the process recursively until the element is found
 * or the search range becomes invalid
 *
 * Time Complexity : O(log n)
 * Space Complexity : O(log n) due to recursive call stack
 */
class RecursiveBinarySearch {

    /**
     * Searches for a target element in a sorted array using
     * the recursive Binary Search algorithm.
     *
     * @param arr  The sorted input array
     * @param low  The starting index of the current search range
     * @param high The ending index of the current search range
     * @param n    The element to search for
     * @return Index of the element if found, otherwise -1
     */
    public static int searchElement(int arr[], int low, int high, int n) {

        // Base condition: search space becomes invalid
        if (low > high) {
            return -1;
        }

        // Calculate middle index
        int mid = (low + high) / 2;

        // Case 1: Element found
        if (arr[mid] == n) {
            return mid;
        }

        // Case 2: Target is smaller => search left half
        else if (arr[mid] > n) {
            return searchElement(arr, low, mid - 1, n);
        }

        // Case 3: Target is greater => search right half
        else {
            return searchElement(arr, mid + 1, high, n);
        }
    }

    public static void main(String[] args) {

        // Sorted array (Binary Search requires sorted input)
        int arr[] = { 10, 20, 40, 50, 50, 60, 70 };

        // Element to search
        int num = 60;

        // Initialize search boundaries
        int low = 0;
        int high = arr.length - 1;

        // Print input array
        System.out.println("Given array: " + Arrays.toString(arr));

        // Print result
        System.out.println("Index of " + num + " is " + searchElement(arr, low, high, num));
    }
}
