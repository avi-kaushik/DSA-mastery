/**
 * Finds the length of the longest subarray where
 * elements alternate between even and odd.
 */
public class LongestEvenOddSubArray {

    /**
     * Returns the maximum length of a contiguous subarray
     * with alternating even and odd numbers.
     * 
     * Example:
     * Input : {5, 10, 20, 6, 3, 8}
     * Parity : O, E, E, E, O, E
     * Output : 3 (subarray: 6, 3, 8)
     *
     * Time Complexity : O(n)
     * Space Complexity : O(1)
     */
    static int getLongestEvenOddSubArray(int arr[]) {

        // Edge case: empty array
        if (arr.length == 0) {
            return 0;
        }

        // current -> length of current alternating streak
        // maximumLength -> stores best result found so far
        int current = 1;
        int maximumLength = 1;

        // Start from second element
        for (int i = 1; i < arr.length; i++) {

            // Check parity of previous and current element
            boolean isPrevEven = arr[i - 1] % 2 == 0;
            boolean isCurrentEven = arr[i] % 2 == 0;

            /*
             * Alternation condition:
             * If previous and current parity are different,
             * then sequence continues.
             *
             * (isPrevEven && !isCurrentEven) || (!isPrevEven && isCurrentEven)
             * Simplified form:
             * isPrevEven != isCurrentEven
             */
            if (isPrevEven != isCurrentEven) {
                current++;
                maximumLength = Math.max(maximumLength, current);
            } else {
                current = 1;
            }
        }

        return maximumLength;
    }

    public static void main(String[] args) {
        int arr[] = { 5, 10, 20, 6, 3, 8 };

        System.out.println(
                "Longest Even-Odd Subarray Length: " +
                        getLongestEvenOddSubArray(arr));
    }
}