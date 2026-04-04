package programs.java.arrays.searching;

import java.util.Arrays;

/**
 * This program counts the number of 1s in a sorted binary array.
 *
 * The array is assumed to contain only 0s followed by 1s.
 *
 * Approach:
 * 1. Use Binary Search to find the first occurrence of 1.
 * 2. All elements after that index must be 1.
 * 3. Count = array length - firstOccurrence
 *
 * Time Complexity: O(log n)
 * Space Complexity: O(1)
 */
public class Count1InSorted {

    /**
     * Counts the number of 1s in a sorted binary array.
     *
     * @param arr Sorted array containing only 0s and 1s
     * @return Number of 1s in the array
     */
    static int countOnesInSortedArray(int arr[]) {

        // Find first occurrence of 1
        int firstOccurrence = FirstOccurrence.firstOccurrence(arr, 1);

        // If 1 is not present
        if (firstOccurrence == -1) {
            return 0;
        }

        // All elements from firstOccurrence to end are 1
        return arr.length - firstOccurrence;
    }

    public static void main(String[] args) {

        int arr[] = { 0, 0, 0, 1, 1, 1, 1, 1 };

        System.out.println("Given array: " + Arrays.toString(arr));

        System.out.println(
                "The occurrence count of 1 is " + countOnesInSortedArray(arr));
    }
}
