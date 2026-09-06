package programs.java.arrays.searching;

import java.util.Arrays;

/**
 * SimpleBinarySearch demonstrates the Binary Search algorithm
 * using an iterative approach.
 *
 * Binary Search works only on a sorted array.
 * The algorithm repeatedly divides the search space in half
 * to locate the target element efficiently.
 *
 * Algorithm Steps:
 * 1. Start with two pointers: low (start of array) and high (end of array)
 * 2. Find the middle index of the current range
 * 3. Compare the middle element with the target
 * - If equal → element found
 * - If middle element is greater → search left half
 * - If middle element is smaller → search right half
 * 4. Repeat until the element is found or the range becomes invalid
 *
 * Time Complexity : O(log n)
 * Space Complexity : O(1)
 */
class SimpleBinarySearch {

    /**
     * Searches for a target element in a sorted array using Binary Search.
     *
     * @param arr The sorted input array
     * @param n   The element to search for
     * @return Index of the element if found, otherwise -1
     */
    public static int searchElement(int arr[], int n) {

        // Initialize search boundaries
        int low = 0;
        int high = arr.length - 1;

        // Continue searching while the search space is valid
        while (low <= high) {

            // Calculate middle index
            int mid = (low + high) / 2;

            // Case 1: Element found
            if (arr[mid] == n) {
                return mid;
            }

            // Case 2: Target is smaller => search left half
            else if (arr[mid] > n) {
                high = mid - 1;
            }

            // Case 3: Target is greater => search right half
            else {
                low = mid + 1;
            }
        }

        // Element not present in array
        return -1;
    }

    public static void main(String[] args) {

        // Sorted array (Binary Search requires sorted input)
        int arr[] = { 10, 20, 40, 50, 50, 60, 70 };

        // Element to search
        int num = 60;

        // Print input array
        System.out.println("Given array: " + Arrays.toString(arr));

        // Print result
        System.out.println("Index of " + num + " is " + searchElement(arr, num));
    }
}
