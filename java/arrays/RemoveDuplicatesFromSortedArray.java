
import java.util.Arrays;

// Class to remove duplicates from the sorted array.
class RemoveDuplicatesFromSortedArray {

    /**
     * Removes duplicate elements from a sorted array.
     *
     * This program removes the duplicate elements from a sorted integer array
     * by modifying the array in-place and then returns a new array containing
     * only the unique elements.
     *
     * Core Idea: Since the input array is sorted, any duplicates will appear
     * next to each other. We iterate through the array and place only the
     * unique elements at the front using a secondary pointer (`index`) which
     * keeps track of the position to insert the next unique element.
     *
     * Time Complexity: O(n) - We traverse the array only once.
     *
     * Space Complexity: - O(1) extra space (modifies array in-place) - O(n) if
     * we consider the new array returned by Arrays.copyOf()
     *
     * Auxiliary Space: O(1)
     *
     * @param arr The input sorted array with possible duplicates.
     * @return A new array containing only the unique elements.
     */
    public static int[] removeDuplicates(int arr[]) {
        if (arr.length == 0) {
            return new int[0]; // Edge case: empty array
        }

        int index = 1; // Index where the next unique element should be placed
        // We don't need to validate 1st element.

        for (int i = 1; i < arr.length; i++) {

            // Compare current element with the last unique element
            // If the given index element is not same as previous, are incrementing index as normal. 
            if (arr[i] != arr[index - 1]) {
                // *Case 1:* If element is unique, it's overriding same index value with same index value.
                // Until there are unique elements, variable `index` and `i` will be same.

                // *Case 2*: If n entry found of duplicate element, then there will be a difference of n between these indexes.
                // In this case, it will left shift the values by n.
                // This is what we want, n duplicates will get removed by the left shift happened here.
                arr[index] = arr[i];
                index++;
            }

            // If the element is duplicate of previous element, then we are not doing anything.
            /* By this way, index is not incrementing line above line #49, and when we find the next 
            non duplicating element, it will see this index, and insert it there.
            This way, all the duplicated element will be swapped with the next non duplicates values. */
        }

        // Return a new array with only the unique elements
        return Arrays.copyOf(arr, index);
    }

    public static void main(String[] args) {
        int arr[] = {4, 5, 9, 10, 10, 15, 20, 20};

        System.out.println("Array: " + Arrays.toString(arr));

        int result[] = removeDuplicates(arr);
        System.out.println("Array without duplicates: " + Arrays.toString(result));
    }
}
