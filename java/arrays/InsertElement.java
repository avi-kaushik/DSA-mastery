
import java.util.Arrays;

/**
 * This class provides functionality to insert an element at a specific position
 * within a fixed-length array by shifting elements to the right.
 *
 * ⚠️ Note: This approach assumes that the array has enough space and will
 * overwrite the last element during insertion.
 */
class InsertElement {

    /**
     * Inserts an element into a fixed-size array at the given position by
     * shifting elements to the right.
     *
     * Time Complexity: O(n) - In the worst case (inserting at the beginning),
     * all elements need to be shifted.
     *
     * Space Complexity: O(1) - No new array is created; only in-place changes.
     *
     * Auxiliary Space: O(1) - No recursion or extra data structures used.
     *
     * @param arr The original fixed-size array.
     * @param position The index at which to insert the new element.
     * @param num The element to insert.
     * @return The position if insertion is successful; otherwise -1 (e.g., if
     * position == arr.length).
     */
    public static int insertElement(int arr[], int position, int num) {
        // Cannot insert if the position is equal to array length (no space to shift)
        if (position == arr.length) {
            return -1;
        }

        // Shift elements to the right from the end to the target position
        for (int i = arr.length - 1; i > position; i--) {
            arr[i] = arr[i - 1];
        }

        arr[position] = num;
        return position;
    }

    public static void main(String[] args) {
        int arr[] = {4, 48, 4, 19, 24, 78};

        insertElement(arr, 3, 98);
        System.err.println(Arrays.toString(arr));
    }
}
