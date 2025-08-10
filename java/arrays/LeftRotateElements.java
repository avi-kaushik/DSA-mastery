
import java.util.Arrays;

// Class to left rotate the elements of an array.
class LeftRotateElements {

    /**
     * Rotates the elements of the given array to the left by one position.
     *
     * The first element of the array is moved to the end, and all other
     * elements are shifted one position to the left. This operation is
     * performed in-place, modifying the original array.
     *
     * @param arr the array to be rotated
     * @return the rotated array (same reference as {@code arr})
     * @throws IllegalArgumentException if {@code arr} is {@code null} or empty
     */
    public static int[] leftRotateByOne(int arr[]) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }

        // Save the value of the first element.
        int firstElement = arr[0];

        // Left rotate all the values until the last value.
        // Note: the last iteration will be the second last element with expression *i < arr.length - 1*.
        // This *i < arr.length - 1* will left rotate the last value with expression arr[i] = arr[i + 1].
        // This way, except the 1st value, all other values will get rotated.
        for (int i = 0; i < arr.length - 1; i++) {
            arr[i] = arr[i + 1];
        }

        // Assign the saved first element at the last index of the array.
        arr[arr.length - 1] = firstElement;

        // Returns the complete the left rotated array by one.
        return arr;
    }

    /**
     * Left rotates the given array by {@code n} positions.
     *
     * A left rotation moves each element of the array to its left by one
     * position, and the first element moves to the end of the array. This
     * process is repeated {@code n} times. For example:
     *
     * arr = {12, 4, 2, 41, 87}, n = 2 result = {2, 41, 87, 12, 4}
     *
     * This implementation uses an auxiliary array of size {@code n} to store
     * the first {@code n} elements, shifts the remaining elements to the left,
     * and then appends the saved elements to the end.
     *
     * Time Complexity: O(n) — each element is moved at most once.
     *
     * Space Complexity: O(n) — extra space used for storing {@code n} elements.
     *
     * @param arr the array to be rotated; must not be {@code null}
     * @param n the number of positions to rotate; if greater than array length,
     * it is reduced modulo the array length
     * @return the rotated array (modifies the original array in place)
     * @throws IllegalArgumentException if {@code arr} is null or empty, or if
     * {@code n} is negative
     */
    @SuppressWarnings("ManualArrayToCollectionCopy")
    public static int[] leftRotateAlgo1(int arr[], int n) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }
        if (n < 0) {
            throw new IllegalArgumentException("Rotation count cannot be negative");
        }

        n = n % arr.length; // Prevent out-of-bounds
        if (n == 0) {
            return arr; // No rotation needed
        }

        /* Algorithm 1:
        Step 1: Save first n values in an new array.
        Step 2: Swap existing values in the array.
        Step 3: Replace the last n items with the previously saved first n items.
         */
        int newArr[] = new int[n];

        // Copying first n elements into a new array.
        int i;
        for (i = 0; i < newArr.length; i++) {
            newArr[i] = arr[i];
        }

        // Left rotating the values from index n to last. 
        // This way, the items from first index to *array.length - n* items will get left rotated.
        for (int j = i; j < arr.length; j++) {
            arr[j - n] = arr[j];
        }

        // Copying the first n saved element to the last 3 elements in the original array.
        for (int k = 0; k < newArr.length; k++) {
            arr[arr.length - n + k] = newArr[k];
        }

        return arr;
    }

    public static void main(String[] args) {
        // int arr[] = {48, 12, 4, 89, 4, 2, 78, 41, 87};
        int arr[] = {12, 4, 2, 41, 87, 78, 89, 12, 45, 10};

        System.out.println("Array input: " + Arrays.toString(arr));

        System.out.println("Left rotated by one: " + Arrays.toString(leftRotateByOne(arr)));
        System.out.println("Left rotated by 3: " + Arrays.toString(leftRotateAlgo1(arr, 3)));
    }
}
