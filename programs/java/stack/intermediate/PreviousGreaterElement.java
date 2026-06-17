package programs.java.stack.intermediate;

import java.util.ArrayDeque;
import java.util.Arrays;

public class PreviousGreaterElement {

    /**
     * Finds the nearest greater element on the left side for every element
     * in the given array.
     *
     * Uses a monotonic decreasing stack to maintain candidate elements.
     * Elements smaller than or equal to the current element are removed
     * because they can never become a previous greater element for the
     * current element or any future element greater than it.
     *
     * After removing invalid candidates:
     * - If the stack is empty, no previous greater element exists.
     * - Otherwise, the stack top represents the nearest greater element.
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
     * @return Array containing the previous greater element for each position.
     */
    static int[] previousGreater(int[] arr) {

        if (arr == null || arr.length == 0)
            return new int[0];

        ArrayDeque<Integer> stack = new ArrayDeque<>();

        int[] series = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {

            // Remove elements that cannot act as a previous greater element.
            while (!stack.isEmpty() && arr[stack.peek()] <= arr[i]) {
                stack.pop();
            }

            // Stack top represents the nearest greater element on the left.
            series[i] = stack.isEmpty() ? -1 : arr[stack.peek()];

            // Add current element as a candidate for upcoming elements.
            stack.push(i);
        }

        return series;
    }

    public static void main(String[] args) {

        int[] arr = { 15, 10, 18, 12, 4, 6, 2, 8 };

        System.out.println("Array: " + Arrays.toString(arr));

        System.out.println(
                "Previous Greater Series: "
                        + Arrays.toString(previousGreater(arr)));
    }
}
