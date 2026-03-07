
import java.util.Arrays;

// Class to move zeros to end of an array.
class MoveZerosToEnd {

    /**
     * Moves all zeros in the given array to the end while maintaining the
     * relative order of non-zero elements. The operation is performed in-place.
     *
     * Time Complexity: O(n) - We traverse the array only once.
     *
     * Space Complexity: - O(1) extra space (modifies array in-place).
     *
     * Auxiliary Space: O(1)
     *
     * @param arr The input array of integers.
     * @return The same array with all zeros moved to the end.
     */
    public static int[] moveZerosToEnd(int arr[]) {
        if (arr.length == 0) {
            return new int[0]; // Edge case: empty array
        }

        int index = 0;
        for (int i = 0; i < arr.length; i++) {

            /**
             * If the current element is non-zero: - Place it at the 'index'
             * position. - If 'i' and 'index' are different, set arr[i] to 0 to
             * simulate "movement". - Increment the 'index' to point to the next
             * position for non-zero.
             *
             * This ensures: - All non-zero elements are pushed to the front in
             * their original order. - All zero elements are moved to the end.
             */
            if (arr[i] != 0) {
                // Case 1, if there is no zeros found, index and i should be same. So, value will be same.
                // Case 2, if n zeros found, then it will insert this non zero value, to the zero contained index.
                // Also this case will update the i index to zero and this value is swapped with the i index.
                arr[index] = arr[i];

                // Adding the zero to the i, as this value is swapped. so updating it to zero.
                // This way the value will be either of the following:
                // 1). Zero will get moved at the end. 
                // 2). Ff it is not end, then this value will get ready to handle zeros like this normal logic.
                if (i != index) {
                    arr[i] = 0;
                }

                index++;
            }
        }

        return arr;
    }

    public static void main(String[] args) {
        int arr[] = {1, 8, 4, 14, 0, 0, 8, 0, 46, 28, 0, 1};

        System.out.println("Array: " + Arrays.toString(arr));

        int result[] = moveZerosToEnd(arr);
        System.out.println("Result: " + Arrays.toString(result));
    }
}
