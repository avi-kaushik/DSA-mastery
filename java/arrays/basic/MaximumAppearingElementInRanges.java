/**
 * Finds the maximum appearing element in given ranges.
 *
 * Each range [start[i], end[i]] represents a set of numbers.
 * The goal is to find the number that appears in the maximum number of ranges.
 *
 * Approach:
 * Uses the Difference Array + Prefix Sum technique.
 *
 * 1. Increase count at start[i]
 * 2. Decrease count at end[i] + 1
 * 3. Build prefix sum to compute actual frequencies
 * 4. The index with the maximum value in the count array
 * represents the maximum appearing element.
 *
 * Time Complexity: O(n + k)
 * Space Complexity: O(k)
 * (k = maximum possible value, here assumed 100)
 */
public class MaximumAppearingElementInRanges {

    /**
     * Returns the element that appears in the maximum number of ranges.
     *
     * @param start array containing starting points of ranges
     * @param end   array containing ending points of ranges
     * @return element with the highest frequency across ranges
     */
    static int getMaximumAppearingElement(int[] start, int[] end) {

        int count[] = new int[101];
        int totalRanges = start.length;

        // Mark start and end+1 positions
        for (int i = 0; i < totalRanges; i++) {
            count[start[i]]++;
            count[end[i] + 1]--;
        }

        // Build prefix sum to get actual frequencies
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // Find index with maximum frequency
        int maxIndex = 0;
        for (int i = 1; i < count.length; i++) {
            if (count[i] > count[maxIndex]) {
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    public static void main(String[] args) {

        int startRange[] = { 1, 2, 5, 15 };
        int endRange[] = { 5, 8, 7, 18 };

        System.out.println("Maximum appearing element in the given range is: "
                + getMaximumAppearingElement(startRange, endRange));
    }
}
