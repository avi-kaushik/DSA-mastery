
import java.util.Arrays;

// This class provides a method to delete an integer and left shift the right elements of an array.
class DeleteElement {

    /**
     * Deletes the first occurrence of 'num' from the array in-place. The array
     * remains fixed in size, so the last element is set to 0.
     *
     * @param arr The array to delete from.
     * @param num The number to delete.
     * @return true if deletion was successful, false if the number was not
     * found.
     */
    public static boolean deleteElement(int arr[], int num) {
        int i;

        // Find the index of the number to delete
        for (i = 0; i < arr.length; i++) {
            if (arr[i] == num) {
                break;
            }
        }

        // Return false, if no ocurrnce found in the given array.
        if (i == arr.length) {
            return false;
        }

        // Left shift the remaining elements in the array.
        for (int j = i; j < arr.length; j++) {
            arr[j] = arr[j + 1];
        }

        // Optional: Set last element to 0 (or any default value)
        arr[arr.length - 1] = 0;
        return true;
    }

    public static void main(String[] args) {
        int arr[] = {48, 49, 45, 12, 98, 14};

        deleteElement(arr, 45);
        System.out.println("The updated array is: " + Arrays.toString(arr));
    }
}
