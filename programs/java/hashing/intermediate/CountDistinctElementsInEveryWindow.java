package programs.java.hashing.intermediate;

import java.util.Arrays;
import java.util.HashMap;

public class CountDistinctElementsInEveryWindow {

    /**
     * Prints the count of distinct elements in every window of size k.
     *
     * Working:
     * - Use HashMap to store each element and its frequency in the current window.
     * - Initialize the first window and print the number of distinct elements.
     * - Slide the window by removing the outgoing element and decreasing its
     * frequency.
     * - Remove the element when its frequency becomes 0.
     * - Add the incoming element and increase its frequency.
     * - HashMap size represents the number of distinct elements in the window.
     *
     * Time Complexity: O(n) average
     * Space Complexity: O(k)
     * 
     * @param arr input array
     * @param k   size of the sliding window
     */
    public static void printWindow(int arr[], int k) {

        HashMap<Integer, Integer> frequencies = new HashMap<>();

        // Add the first k elements to the frequency map.
        for (int i = 0; i < k; i++) {
            frequencies.put(arr[i], frequencies.getOrDefault(arr[i], 0) + 1);
        }

        System.out.print(frequencies.size() + " ");

        // Slide the window through the remaining elements.
        for (int i = k; i < arr.length; i++) {

            // Remove the outgoing element from the current window.
            int elementToRemove = arr[i - k];

            frequencies.put(
                    elementToRemove,
                    frequencies.get(elementToRemove) - 1);

            // Remove the element if it no longer exists in the window.
            if (frequencies.get(elementToRemove) == 0)
                frequencies.remove(elementToRemove);

            // Add the incoming element to the current window.
            frequencies.put(
                    arr[i],
                    frequencies.getOrDefault(arr[i], 0) + 1);

            // HashMap size represents the number of distinct elements.
            System.out.print(frequencies.size() + " ");
        }

        System.out.println();
    }

    public static void main(String[] args) {

        int arr[] = { 30, 20, 30, 10, 30, 40, 10 };

        System.out.println("Array: " + Arrays.toString(arr));

        System.out.print("Distinct elements in windows: ");
        printWindow(arr, 3);
    }
}
