
import java.util.Arrays;

// Class to reverse the elements of the given array.
class ReverseArray {

    /**
     * Reverses the given array in-place.
     *
     * @param arr the input array to be reversed
     * @return the reversed array
     *
     * Time Complexity: O(n) - The loop with run n/2 times.
     *
     * Space Complexity: O(1) - No extra memory usage with given input.
     *
     * Auxiliary Space: O(1) - No additional data structures used.
     */
    public static int[] reverse(int arr[]) {
        int temp, index;

        // Running this loop half times the size of the array.
        // Need to run this half times, because we are swapping the half elements with next half elements. 
        // So, whole array will be updated midway only.
        for (int i = 0; i < arr.length / 2; i++) {
            temp = arr[i];

            // Index of the symetric element from the right.
            index = arr.length - 1 - i;

            arr[i] = arr[index];
            arr[index] = temp;
        }

        // Returning the array, once the elements are reversed.
        return arr;
    }

    public static void main(String[] args) {
        int arr[] = {10, 65, 12, 38, 12, 98, 48};

        System.out.println("Array Input: " + Arrays.toString(arr));

        int reversed[] = reverse(arr);
        System.out.println("Reversed Array: " + Arrays.toString(reversed));
    }
}
