package programs.java.dequeue.intermediate;

import java.util.ArrayDeque;

/**
 * Finds the maximum value in every sliding window (sub-array) of a
 * fixed size, as the window slides over the array from left to right.
 *
 * Uses a Deque to solve this in O(n) time instead of checking every
 * window one by one.
 *
 * The deque stores indexes of array elements, not the values
 * themselves, and is maintained so that:
 * - The index at the front always holds the current window's max.
 * - Values stay in decreasing order from front to rear.
 */
public class MaximumsOfSubArraysWithGivenSize {

    /**
     * Prints the maximum value of every sliding window of given size.
     *
     * @param arr  the input array to scan
     * @param size the size of each sliding window
     */
    public static void getMaximumsOfSubArrays(int arr[], int size) {

        ArrayDeque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < arr.length; i++) {

            // Remove front index if it has fallen outside the current
            // window's range (window range is [i - size + 1, i]).
            if (!deque.isEmpty() && deque.peekFirst() <= i - size) {
                deque.pollFirst();
            }

            // Remove indexes from the rear whose values are smaller
            // than the current element - they can never be the max
            // again since the current element is bigger and will
            // outlast them in the window.
            while (!deque.isEmpty() && arr[i] > arr[deque.peekLast()]) {
                deque.pollLast();
            }

            deque.offerLast(i);

            if (i + 1 >= size)
                System.out.println(arr[deque.peekFirst()]);
        }
    }

    public static void main(String[] args) {
        int arr[] = { 10, 8, 5, 12, 15, 7, 6 };

        getMaximumsOfSubArrays(arr, 3);
    }
}
