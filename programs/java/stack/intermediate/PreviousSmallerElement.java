package programs.java.stack.intermediate;

import java.util.ArrayDeque;
import java.util.Arrays;

public class PreviousSmallerElement {

    /**
     * Finds the nearest smaller element on the left side for every element
     * in the given array.
     *
     * Uses a monotonic increasing stack to maintain candidate elements.
     * Elements greater than or equal to the current element are removed
     * because they can never become a previous smaller element for the
     * current element or any future element smaller than it.
     *
     * After removing invalid candidates:
     * - If the stack is empty, no previous smaller element exists.
     * - Otherwise, the stack top represents the nearest smaller element.
     *
     * The stack stores indices to provide access to both element positions
     * and values when required.
     * 
     * Time Complexity: O(n)
     *
     * Each element is pushed exactly once and popped at most once.
     *
     * Space Complexity: O(n)
     *
     * The stack can contain all elements in the worst case.
     *
     * @param arr Input array.
     * @return Array containing the previous smaller element for each position.
     */
    static int[] previousSmaller(int[] arr) {

        if (arr == null || arr.length == 0)
            return new int[0];

        ArrayDeque<Integer> stack = new ArrayDeque<>();

        int[] series = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {

            // Remove elements that cannot act as a previous smaller element.
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                stack.pop();
            }

            // Stack top represents the nearest smaller element on the left.
            series[i] = stack.isEmpty() ? -1 : arr[stack.peek()];

            // Add current element as a candidate element.
            stack.push(i);
        }

        return series;
    }

    public static void main(String[] args) {

        int[] arr = { 15, 10, 18, 12, 4, 6, 2, 8 };

        System.out.println("Array: " + Arrays.toString(arr));

        System.out.println(
                "Previous Smaller Series: "
                        + Arrays.toString(previousSmaller(arr)));
    }
}
