package programs.java.stack.intermediate;

import java.util.ArrayDeque;
import java.util.Arrays;

public class StockSpanProblem {

    /**
     * Computes stock span for each element in the array.
     *
     * Span of an element is the number of consecutive elements before it
     * (including itself) that are less than or equal to the current element.
     *
     * @param arr Input stock prices.
     * @return Span array.
     */
    static int[] getSpan(int[] arr) {

        // Return empty span array for null or empty input.
        if (arr == null || arr.length == 0) {
            return new int[0];
        }

        // Stores indices of elements in decreasing order.
        ArrayDeque<Integer> stack = new ArrayDeque<>();

        int[] span = new int[arr.length];

        // First element always has span 1.
        stack.push(0);
        span[0] = 1;

        for (int i = 1; i < arr.length; i++) {

            // Remove all previous elements that are
            // smaller than or equal to current element.
            while (!stack.isEmpty() && arr[stack.peek()] <= arr[i]) {
                stack.pop();
            }

            // If stack becomes empty,
            // current element is greater than all previous elements.
            // Otherwise, span is the distance from previous greater element.
            span[i] = stack.isEmpty() ? i + 1 : i - stack.peek();

            // Store current index for future elements.
            stack.push(i);
        }

        return span;
    }

    public static void main(String[] args) {
        int[] arr = { 18, 16, 13, 14, 11, 16 };

        // Print input array.
        System.out.println("Given array: " + Arrays.toString(arr));

        // Print stock span.
        System.out.println("Span: " + Arrays.toString(getSpan(arr)));
    }
}
