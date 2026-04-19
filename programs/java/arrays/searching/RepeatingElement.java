package programs.java.arrays.searching;

import java.util.Arrays;

public class RepeatingElement {

    /**
     * Finds the repeating element in an array using Floyd's Cycle Detection
     * Algorithm.
     *
     * Assumptions:
     * - Array size = n + 1
     * - Elements are in range [1, n]
     * - Exactly one element is repeated (can appear multiple times)
     *
     * Approach:
     * - Treat array as a linked list where value = next index
     * - Detect cycle using slow & fast pointers
     * - Find entry point of cycle (duplicate element)
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    static int getRepeatingElement(int arr[]) {

        // Step 1: Initialize slow and fast pointers
        // Both start from the "logical start" of traversal (arr[0])
        int slow = arr[0];
        int fast = arr[0];

        // Step 2: Detect cycle
        // slow moves 1 step, fast moves 2 steps
        // They are guaranteed to meet inside the cycle
        do {
            slow = arr[slow]; // move by 1 step
            fast = arr[arr[fast]]; // move by 2 steps
        } while (slow != fast);

        // Step 3: Find cycle entry (duplicate element)
        // Reset slow to start of traversal
        slow = arr[0];

        // Move both pointers one step at a time
        // They will meet at the cycle entry point
        while (slow != fast) {
            slow = arr[slow];
            fast = arr[fast];

            // Debug logs (optional)
            // System.err.println("Slow: " + slow + ", Fast: " + fast);
        }

        // Step 4: Return the duplicate element
        return slow;
    }

    public static void main(String[] args) {
        int arr[] = { 1, 5, 1, 4, 2, 3 };

        System.out.println("Array => " + Arrays.toString(arr));
        System.out.println("The repeating element is: " + getRepeatingElement(arr));
    }
}
