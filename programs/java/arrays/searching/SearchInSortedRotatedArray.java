package programs.java.arrays.searching;

import java.util.Arrays;

/**
 * Problem:
 * Search an element in a Sorted Rotated Array.
 *
 * A sorted rotated array is an array that was originally sorted but then
 * rotated
 * at some pivot point.
 *
 * Example:
 * Sorted Array: [10, 20, 30, 40, 50, 100]
 * Rotated Array: [100, 10, 20, 30, 40, 50]
 *
 * We need to find the index of a given element efficiently.
 *
 * Why normal Binary Search doesn't work directly?
 * Because rotation breaks the complete sorted order.
 *
 * Key Observation:
 * In a rotated sorted array, at least ONE half of the array is always sorted.
 *
 * Strategy:
 * 1. Perform Binary Search.
 * 2. Determine which half is sorted (left or right).
 * 3. Check if the target element lies within the sorted half.
 * 4. If yes, search within that half.
 * 5. Otherwise search in the other half.
 *
 * Time Complexity:
 * O(log n)
 *
 * Space Complexity:
 * O(1)
 */
public class SearchInSortedRotatedArray {

    static int searchInSortedRotatedArray(int arr[], int n) {

        // Initialize search boundaries
        int low = 0;
        int high = arr.length - 1;

        // Binary search loop
        while (low <= high) {

            // Calculate mid index
            int mid = (low + high) / 2;

            // If the element is found, return its index
            if (arr[mid] == n)
                return mid;

            /**
             * Check if the LEFT half is sorted.
             *
             * If arr[low] <= arr[mid], it means the portion
             * from low => mid is sorted.
             */
            if (arr[low] <= arr[mid]) {

                /**
                 * Check if the target lies within this sorted left half.
                 */
                if (n >= arr[low] && n < arr[mid]) {

                    // Target lies in left half => move high
                    high = mid - 1;

                } else {

                    // Target lies in right half
                    low = mid + 1;
                }

            } else {

                /**
                 * If left half is not sorted,
                 * then the RIGHT half must be sorted.
                 */

                /**
                 * Check if the target lies within this sorted right half.
                 */
                if (n > arr[mid] && n <= arr[high]) {

                    // Target lies in right half
                    low = mid + 1;

                } else {

                    // Target lies in left half
                    high = mid - 1;
                }
            }
        }

        // Element not found
        return -1;
    }

    public static void main(String[] args) {

        // Example rotated sorted array
        int arr[] = { 100, 10, 20, 30, 40, 50 };

        // Element to search
        int num = 10;

        // Print input array
        System.out.println("Given array: " + Arrays.toString(arr));

        // Call search function
        int index = searchInSortedRotatedArray(arr, num);

        // Print result
        System.out.println("Index of " + num + " in a sorted & rotated array is " + index);
    }
}
