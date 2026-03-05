/**
 * Utility class to find an equilibrium element in an array.
 *
 * An equilibrium point is an index where the sum of elements
 * on the left side is equal to the sum of elements on the right side.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 */
public class GetEquilibriumPoint {

    /**
     * Returns the equilibrium element from the given array.
     *
     * The method uses a prefix sum array to calculate the sum
     * of elements before and after each index.
     *
     * @param arr input integer array
     * @return equilibrium element if found, otherwise -1
     */
    static int getEquilibriumPoint(int arr[]) {

        int n = arr.length;

        int leftSum[] = new int[n];
        leftSum[0] = arr[0];

        int totalSum = arr[0];

        // Build prefix sum and calculate total sum
        for (int i = 1; i < n; i++) {
            leftSum[i] = leftSum[i - 1] + arr[i];
            totalSum += arr[i];
        }

        // Check equilibrium condition
        for (int i = 0; i < n; i++) {

            int currentLeftSum = (i == 0) ? 0 : leftSum[i - 1];
            int currentRightSum = totalSum - leftSum[i];

            if (currentLeftSum == currentRightSum)
                return arr[i];
        }

        return -1;
    }

    public static void main(String[] args) {

        int arr[] = { 4, 2, -2 };

        System.out.println(getEquilibriumPoint(arr));
    }
}
