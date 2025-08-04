// Class to search for the largest integer in an array

class LargestElement {

    /**
     * Finds and returns the largest element in the given array.
     *
     * Time Complexity: O(n) – where n is the number of elements in the array. -
     * We iterate through the entire array once.
     *
     * Space Complexity: O(1) - No extra space is used except a few variables.
     *
     * Auxiliary Space: O(1) - No recursive calls or additional data structures
     * are used.
     *
     * @param arr The input array of integers.
     * @return The largest integer in the array.
     */
    public static int findLargestElement(int arr[]) {
        // Assume the first element is the largest initially
        int currentLargest = arr[0];

        // Traverse the rest of the array to find the actual largest
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > currentLargest) {
                currentLargest = arr[i];    // Update if current element is larger
            }
        }

        return currentLargest;
    }

    public static void main(String[] args) {
        int numbers[] = {4, 8, 48, 28, 28, 46};

        int largest = findLargestElement(numbers);

        // Print the largest element in the array
        System.out.println("Largest element in the array is " + largest);
    }
}
