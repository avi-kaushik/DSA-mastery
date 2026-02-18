// Class for find the maximun difference of the values in an array.

class MaximumDifference {

    /**
     *
     * This function finds the maximum difference between two elements in the
     * array such that the larger element comes after the smaller element in the
     * array.
     *
     * Steps: 1. Initialize the minimum value as the first element.
     *
     * 2. Initialize the difference as the difference between the second and
     * first element.
     *
     * 3. Traverse the array from the second element onward: - Calculate the
     * difference between the current element and the minimum value seen so far.
     * - Update the maximum difference if the current difference is greater. -
     * Update the minimum value if the current element is smaller than the
     * current minimum.
     *
     * Time Complexity: O(n) → We traverse the array only once.
     *
     * Space Complexity: O(1) → We use only a few extra variables regardless of
     * input size.
     *
     * Auxiliary Space: O(1) → No additional data structures are used.
     *
     * Input Example: arr = {2, 3, 10, 6, 4, 8, 1}
     *
     * Output Example: 8 (Explanation: The maximum difference is 10 - 2 = 8)
     *
     * Throws: IllegalArgumentException if the input array has fewer than 2
     * elements.
     */
    public static int getMaximunDifference(int arr[]) {
        if (arr == null || arr.length < 2) {
            throw new IllegalArgumentException("Array must have at least two elements");
        }

        int minValue = arr[0];
        int difference = arr[1] - arr[0]; // Initialize with first possible difference

        for (int i = 1; i < arr.length; i++) {
            int currentDifference = arr[i] - minValue;

            if (currentDifference > difference) {
                difference = currentDifference;
            }

            if (arr[i] < minValue) {
                minValue = arr[i];
            }
        }

        return difference;
    }

    public static void main(String[] args) {
        int[] arr = {2, 3, 10, 6, 4, 8, 1};
        System.out.println("Maximum Difference: " + getMaximunDifference(arr));
    }
}
