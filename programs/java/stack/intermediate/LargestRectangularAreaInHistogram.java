package programs.java.stack.intermediate;

import java.util.ArrayDeque;
import java.util.Arrays;

public class LargestRectangularAreaInHistogram {

    /**
     * Finds the index of the nearest smaller element on the left side
     * for every element in the array.
     *
     * A monotonic increasing stack is used to efficiently remove all
     * elements that cannot act as the previous smaller element.
     *
     * If no smaller element exists, -1 is stored.
     *
     * @param arr Input histogram heights.
     * @return Array containing previous smaller element indices.
     */
    private static int[] previousSmallerIndex(int[] arr) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();

        int[] series = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                stack.pop();
            }

            series[i] = stack.isEmpty() ? -1 : stack.peek();

            stack.push(i);
        }

        return series;
    }

    /**
     * Finds the index of the nearest smaller element on the right side
     * for every element in the array.
     *
     * A monotonic increasing stack is used while traversing from right
     * to left to efficiently identify the next smaller element.
     *
     * If no smaller element exists, arr.length is stored because the
     * rectangle can extend until the end of the histogram.
     *
     * @param arr Input histogram heights.
     * @return Array containing next smaller element indices.
     */
    private static int[] nextSmallerIndex(int[] arr) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();

        int[] series = new int[arr.length];

        for (int i = arr.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                stack.pop();
            }

            series[i] = stack.isEmpty() ? arr.length : stack.peek();

            stack.push(i);
        }

        return series;
    }

    /**
     * Calculates the largest rectangular area that can be formed
     * in a histogram.
     *
     * For every bar:
     * - Height is the current bar height.
     * - Width is determined using the nearest smaller elements
     * on both sides.
     *
     * Width Formula:
     * width = rightBoundary - leftBoundary - 1
     *
     * The subtraction of one excludes the boundary elements
     * themselves because they are smaller than the current bar
     * and cannot be included in the rectangle.
     *
     * @param arr Input histogram heights.
     * @return Largest rectangular area in the histogram.
     */
    static int getLargestArea(int[] arr) {
        int largest = 0;

        // Find prevous smaller indexes.
        int[] previousSmaller = previousSmallerIndex(arr);

        // Find next smaller indexes.
        int[] nextSmaller = nextSmallerIndex(arr);

        for (int i = 0; i < arr.length; i++) {
            // Height is just same as the value of the element.
            int height = arr[i];

            int leftBoundary = previousSmaller[i];
            int rightBoundary = nextSmaller[i];

            // Width is the number of element between previous and next smaller indices.
            int width = rightBoundary - leftBoundary - 1;

            int area = width * height;

            largest = Math.max(largest, area);
        }

        return largest;
    }

    public static void main(String[] args) {
        int[] arr = { 6, 2, 5, 4, 1, 5, 6 };

        System.out.println("Arrays: " + Arrays.toString(arr));

        System.out.println("Largest area of histogram: " + getLargestArea(arr));
    }
}
