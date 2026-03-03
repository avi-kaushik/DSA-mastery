import java.util.Arrays;

/**
 * PrefixSum demonstrates how to preprocess an array
 * to answer range sum queries in O(1) time.
 *
 * Concept:
 * 1. Precompute prefix sums in O(n)
 * 2. Answer each range query in O(1)
 */
public class PrefixSum {

    /**
     * Builds and returns a prefix sum array.
     *
     * prefix[i] stores sum of elements from index 0 to i.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     *
     * @param arr Original input array
     * @return Prefix sum array
     */
    static int[] preloadSum(int arr[]) {
        int prefix[] = new int[arr.length];

        // First element remains same
        prefix[0] = arr[0];

        // Build prefix sum array
        for (int i = 1; i < arr.length; i++) {
            prefix[i] = prefix[i - 1] + arr[i];
        }

        return prefix;
    }

    /**
     * Returns sum of elements from index s to e (inclusive).
     *
     * Uses prefix sum formula:
     * If s == 0 → sum = prefix[e]
     * Else → sum = prefix[e] - prefix[s - 1]
     *
     * Time Complexity: O(1)
     *
     * @param prefix Prefix sum array
     * @param s      Start index (inclusive)
     * @param e      End index (inclusive)
     * @return Sum of elements in range [s, e]
     * @throws IllegalArgumentException if range is invalid
     */
    static int getSum(int prefix[], int s, int e) {
        if (s < 0 || e >= prefix.length || s > e) {
            throw new IllegalArgumentException("Invalid range");
        }

        if (s == 0) {
            return prefix[e];
        }

        return prefix[e] - prefix[s - 1];
    }

    public static void main(String[] args) {
        int arr[] = { 4, 4, 1, 9, 4, 6, 7, 2, 5, 1 };

        System.out.println("Original Array: " + Arrays.toString(arr));

        int prefix[] = preloadSum(arr);

        System.out.println("Sum of index 1 to 4: " + getSum(prefix, 1, 4));
        System.out.println("Sum of index 5 to 6: " + getSum(prefix, 5, 6));
        System.out.println("Sum of index 6 to 9: " + getSum(prefix, 6, 9));
    }
}
