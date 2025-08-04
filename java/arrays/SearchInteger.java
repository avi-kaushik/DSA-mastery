
// This class provides a method to search for a specific integer in an array.
class SearchInteger {

    /**
     * Searches for a specific number in the given array.
     *
     * Time Complexity: O(n) – where n is the number of elements in the array. -
     * In the worst case, we might have to look at every element once.
     *
     * Space Complexity: O(1) - No extra space is used apart from loop counter
     * and input parameters.
     *
     * Auxiliary Space: O(1) - No recursion or additional data structures are
     * used.
     *
     * @param arr The input array of integers.
     * @param number The integer to search for in the array.
     * @return The index of the number if found; otherwise, -1.
     */
    public static int searchInteger(int arr[], int number) {
        // Loop through each element in the array
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == number) {
                return i;   // Return index if the number found at any index.
            }
        }

        return -1;  // Return -1 if the specified element not found in the given array.
    }

    public static void main(String[] args) {
        int numbers[] = {4, 8, 48, 27, 28, 46};
        int number = 4;

        int index = searchInteger(numbers, number);

        System.out.println("Index of " + number + " is " + index);
    }
}
