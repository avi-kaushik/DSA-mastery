import java.util.Arrays;

/**
 * PrefixSum demonstrates two important preprocessing techniques
 * used in range query problems:
 *
 * 1️⃣ Normal Prefix Sum
 * 2️⃣ Weighted Prefix Sum
 *
 * These techniques allow answering range queries in O(1) time
 * after O(n) preprocessing.
 *
 * ------------------------------------------------------------
 * NORMAL PREFIX SUM
 * ------------------------------------------------------------
 *
 * prefix[i] = arr[0] + arr[1] + ... + arr[i]
 *
 * Range sum query:
 *
 * sum(s,e) =
 * prefix[e] if s == 0
 * prefix[e] - prefix[s-1] otherwise
 *
 *
 * ------------------------------------------------------------
 * WEIGHTED PREFIX SUM
 * ------------------------------------------------------------
 *
 * weighted[i] = arr[0]*1 + arr[1]*2 + ... + arr[i]*(i+1)
 *
 * This allows us to answer queries of the form:
 *
 * arr[s]*1 + arr[s+1]*2 + arr[s+2]*3 + ...
 *
 * which frequently appears in competitive programming problems.
 *
 * Formula used:
 *
 * weightedRange = weighted[e] - weighted[s-1]
 * normalRange = prefix[e] - prefix[s-1]
 *
 * result = weightedRange - s * normalRange
 *
 *
 * ------------------------------------------------------------
 * Time Complexity
 * ------------------------------------------------------------
 *
 * Preprocessing: O(n)
 * Query: O(1)
 *
 * Space Complexity: O(n)
 *
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
     * Builds a weighted prefix sum array.
     *
     * weighted[i] stores:
     *
     * arr[0]*1 + arr[1]*2 + arr[2]*3 + ... + arr[i]*(i+1)
     *
     * Example:
     * arr = [4, 4, 1, 9]
     *
     * weighted =
     * [
     * 4*1,
     * 4*1 + 4*2,
     * 4*1 + 4*2 + 1*3,
     * 4*1 + 4*2 + 1*3 + 9*4
     * ]
     *
     * = [4, 12, 15, 51]
     *
     * @param arr input array
     * @return weighted prefix array
     *
     *         Time Complexity: O(n)
     *         Space Complexity: O(n)
     */
    static int[] preloadWeightedSum(int arr[]) {

        int weighted[] = new int[arr.length];

        // first element
        weighted[0] = arr[0] * 1;

        // build weighted prefix
        for (int i = 1; i < arr.length; i++) {
            weighted[i] = weighted[i - 1] + arr[i] * (i + 1);
        }

        return weighted;
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

    /**
     * Returns the weighted sum of elements between index s and e.
     *
     * Computes:
     *
     * arr[s]*1 + arr[s+1]*2 + arr[s+2]*3 + ...
     *
     * Instead of:
     *
     * arr[s]*(s+1) + arr[s+1]*(s+2) ...
     *
     * This is done using the formula:
     *
     * weightedRange = weighted[e] - weighted[s-1]
     * normalRange = prefix[e] - prefix[s-1]
     *
     * result = weightedRange - (s * normalRange)
     *
     * @param weighted weighted prefix array
     * @param sum      normal prefix array
     * @param s        start index
     * @param e        end index
     * @return weighted range sum
     *
     *         Time Complexity: O(1)
     */
    static int getWeightedSum(int weighted[], int sum[], int s, int e) {

        int weightedSum = (s == 0) ? weighted[e] : weighted[e] - weighted[s - 1];

        int normalSum = (s == 0) ? sum[e] : sum[e] - sum[s - 1];

        return weightedSum - (s * normalSum);
    }

    public static void main(String[] args) {
        int arr[] = { 4, 4, 1, 9, 4, 6, 7, 2, 5, 1 };

        System.out.println("Original Array: " + Arrays.toString(arr));

        int prefix[] = preloadSum(arr);
        int weighted[] = preloadWeightedSum(arr);

        System.out.println("Sum of index 1 to 4: " + getSum(prefix, 1, 4));
        System.out.println("Sum of index 5 to 6: " + getSum(prefix, 5, 6));
        System.out.println("Sum of index 6 to 9: " + getSum(prefix, 6, 9));

        System.out.println("Weighted Sum of index 2 to 5: "
                + getWeightedSum(weighted, prefix, 2, 5));
    }
}
