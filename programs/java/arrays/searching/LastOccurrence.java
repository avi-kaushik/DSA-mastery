package programs.java.arrays.searching;

import java.util.Arrays;

/**
 * Program to find the LAST OCCURRENCE of an element in a sorted array.
 *
 * This implementation demonstrates two approaches:
 *
 * 1. Recursive Binary Search
 * 2. Iterative Binary Search
 *
 * When duplicate elements exist in a sorted array, a normal binary search
 * may return any occurrence. This program ensures that the LAST occurrence
 * of the target element is returned.
 *
 * Example:
 * Input Array: [5, 5, 5, 5]
 * Target: 5
 *
 * Output:
 * Last Occurrence Index = 3
 *
 * Time Complexity: O(log n)
 * Space Complexity:
 * - Recursive: O(log n) (due to recursion stack)
 * - Iterative: O(1)
 */
public class LastOccurrence {

    /**
     * Recursive Binary Search to find the LAST occurrence of an element.
     *
     * @param arr  Sorted input array
     * @param low  Starting index of the current search space
     * @param high Ending index of the current search space
     * @param n    Target element to search
     *
     * @return Index of the last occurrence of the element,
     *         or -1 if the element is not found.
     */
    public static int lastElementRecursive(int arr[], int low, int high, int n) {

        // Base condition: search space becomes invalid
        if (low > high) {
            return -1;
        }

        // Calculate middle index
        int mid = (low + high) / 2;

        // Case 1: Target element found
        if (arr[mid] == n) {

            /*
             * Check if this is the LAST occurrence.
             *
             * Two possibilities:
             *
             * 1. mid is the last index of the array
             * 2. The next element is different
             *
             * If either condition is true, mid is the last occurrence.
             */
            if (mid == arr.length - 1 || arr[mid + 1] != arr[mid]) {
                return mid;
            }

            /*
             * If the next element is the same,
             * then another occurrence exists on the RIGHT side.
             * Continue searching in the right half.
             */
            else {
                return lastElementRecursive(arr, mid + 1, high, n);
            }
        }

        // Case 2: Target is smaller than mid element → search LEFT half
        else if (arr[mid] > n) {
            return lastElementRecursive(arr, low, mid - 1, n);
        }

        // Case 3: Target is greater than mid element → search RIGHT half
        else {
            return lastElementRecursive(arr, mid + 1, high, n);
        }
    }

    /**
     * Iterative Binary Search to find the LAST occurrence of an element.
     *
     * @param arr Sorted input array
     * @param n   Target element
     *
     * @return Index of the last occurrence of the element,
     *         or -1 if the element is not found.
     */
    static int lastOccurrence(int arr[], int n) {

        int low = 0;
        int high = arr.length - 1;

        // Variable to store potential last occurrence
        int idx = -1;

        // Continue while search space is valid
        while (low <= high) {

            // Calculate middle index
            int mid = (low + high) / 2;

            // Case 1: Target found
            if (arr[mid] == n) {

                /*
                 * Store the current index and move RIGHT
                 * to check if another occurrence exists.
                 */
                idx = mid;
                low = mid + 1;
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

        // Return last occurrence index
        return idx;
    }

    /**
     * Driver method to test the algorithm
     */
    public static void main(String[] args) {

        int arr[] = { 5, 5, 5, 5 };
        int num = 5;

        // Print input array
        System.out.println("Given array: " + Arrays.toString(arr));

        // Test iterative solution
        System.out.println("Last occurrence (iterative): " + lastOccurrence(arr, num));

        // Test recursive solution
        System.out.println("Last occurrence (recursive): "
                + lastElementRecursive(arr, 0, arr.length - 1, num));
    }
}
