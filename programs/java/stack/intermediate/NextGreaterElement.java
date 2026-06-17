package programs.java.stack.intermediate;

import java.util.ArrayDeque;
import java.util.Arrays;

public class NextGreaterElement {

    /**
     * Finds the nearest greater element on the right side for every element
     * in the given array.
     *
     * Uses a monotonic decreasing stack to maintain candidate elements.
     * Elements smaller than or equal to the current element are removed
     * because they can never become a next greater element for the current
     * element or any element positioned further to the left.
     *
     * After removing invalid candidates:
     * - If the stack is empty, no next greater element exists.
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
     * @return Array containing the next greater element for each position.
     */
    static int[] nextGreater(int[] arr) {

        if (arr == null || arr.length == 0)
            return new int[0];

        ArrayDeque<Integer> stack = new ArrayDeque<>();

        int[] series = new int[arr.length];

        for (int i = arr.length - 1; i >= 0; i--) {

            // Remove elements that cannot act as a next greater element.
            while (!stack.isEmpty() && arr[stack.peek()] <= arr[i]) {
                stack.pop();
            }

            // Stack top represents the nearest greater element on the right.
            series[i] = stack.isEmpty() ? -1 : arr[stack.peek()];

            // Add current element as a candidate for upcoming elements.
            stack.push(i);
        }

        return series;
    }

    public static void main(String[] args) {

        int[] arr = { 5, 15, 10, 8, 6, 12, 9, 18 };

        System.out.println("Array: " + Arrays.toString(arr));

        System.out.println("Next Greater Series: " + Arrays.toString(nextGreater(arr)));
    }
}
