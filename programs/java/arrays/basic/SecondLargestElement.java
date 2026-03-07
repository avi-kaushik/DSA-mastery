
import java.util.Arrays;

// Class to find the second largest element in the given array.
class SecondLargestElement {

    /**
     * Finds the second largest unique element in the given array.
     *
     * Time Complexity: O(n) — where n is the number of elements in the array. -
     * Single traversal of the array is done to determine the largest and second
     * largest.
     *
     * Space Complexity: O(1) - No extra data structures are used that depend on
     * input size.
     *
     * Auxiliary Space: O(1) - Only a few variables (`largest`, `secondLargest`)
     * are used regardless of input size.
     *
     * @param arr The array of integers.
     * @return The second largest element in the array.
     * @throws IllegalArgumentException if the array has fewer than two
     * elements.
     * @throws RuntimeException if there is no second largest element (e.g., all
     * elements might be equal).
     */
    public static int findSecondLargestNumber(int arr[]) {
        // Validate that array has at least two elements
        if (arr.length < 2) {
            throw new IllegalArgumentException("Array must contain at least two elements.");
        }

        // Initialise the elements with default values.
        int largest = arr[0], secondLargest = Integer.MIN_VALUE;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > largest) {
                // If we found Largest, then update that value to latest, and previous latest to secondLargest.

                secondLargest = largest;
                largest = arr[i];
            } else if (arr[i] > secondLargest && arr[i] != largest) {
                // Update secondLargest only if it's distinct from largest

                secondLargest = arr[i];
            }
        }

        // If secondLargest was not updated.
        if (secondLargest == Integer.MIN_VALUE) {
            throw new RuntimeException("No second largest element found (all elements might be equal).");
        }

        return secondLargest;
    }

    public static void main(String[] args) {
        int arr[] = {84, 24, 78, 45, 92, 98, 12, 88};

        System.out.println("Finding Second largest element in the array " + Arrays.toString(arr));
        System.out.println("Second largest element in the array is: " + findSecondLargestNumber(arr));
    }
}
