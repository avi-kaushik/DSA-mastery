
import java.util.Arrays;

// Class to check if the given array is sorted in ascending order or not.
class CheckSorted {

    /**
     * Checks if the given integer array is sorted in non-decreasing (ascending)
     * order.
     *
     * A non-decreasing array means that for every i from 1 to n-1, arr[i] >=
     * arr[i-1]. Duplicate elements are allowed.
     *
     * @param arr The array of integers to be checked.
     * @return true if the array is sorted in ascending order (non-decreasing),
     * false otherwise.
     *
     * Time Complexity: O(n) - Where n is the number of elements in the array. -
     * The function makes a single pass through the array.
     *
     * Space Complexity: O(1) - No extra space is used apart from a few
     * primitive variables.
     *
     * Auxiliary Space: O(1) - No recursive calls or additional data structures
     * are used.
     */
    public static boolean isSorted(int arr[]) {
        for (int i = 1; i < arr.length; i++) {
            // If each item is greater than or equal to it's previous element, then it will be a sorted array.
            // Checking for each item, if each item is greater than or equal to it's previous element.
            if (arr[i] < arr[i - 1]) {
                // Return false, if we found an array whose current item is smaller than it's previous item.
                return false;
            }
        }

        // If execution reaches here, it means the loop has done the completion, and found the array sorted.
        return true;
    }

    public static void main(String[] args) {
        int arr[] = {10, 12, 45, 54, 99, 101, 107};

        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("Is sorted: " + isSorted(arr));
    }
}
