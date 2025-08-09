
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

    public static void main(String[] args) {
        // int arr[] = {48, 12, 4, 89, 4, 2, 78, 41, 87};
        int arr[] = {12, 4, 2, 41, 87};

        System.out.println("Array input: " + Arrays.toString(arr));
        System.out.println("Left rotated by one: " + Arrays.toString(leftRotateByOne(arr)));
    }
}
