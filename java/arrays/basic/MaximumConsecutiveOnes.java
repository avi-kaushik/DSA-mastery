/**
 * This class provides a utility method to find
 * the maximum number of consecutive 1s in a binary array.
 */
public class MaximumConsecutiveOnes {
    /**
     * Returns the maximum number of consecutive 1s in the given array.
     *
     * Approach:
     * - Traverse the array once.
     * - Maintain a counter for current streak of 1s.
     * - Reset the counter whenever a 0 is encountered.
     * - Continuously track the maximum streak found so far.
     *
     * Time Complexity: O(n)
     * We traverse the array exactly once.
     *
     * Space Complexity: O(1)
     * Only two integer variables are used.
     *
     * @param arr An integer array containing only 0s and 1s
     * @return Maximum count of consecutive 1s
     */
    static int getMaximumConsecutiveOne(int arr[]) {
        int currentCount = 0, maximumCount = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1) {
                currentCount++;
                if (currentCount > maximumCount) {
                    maximumCount = currentCount;
                }
            } else {
                currentCount = 0;
            }
        }

        return maximumCount;
    }

    public static void main(String[] args) {
        int arr[] = { 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0 };
        System.out.println("Maximum consecutive ones are: " + getMaximumConsecutiveOne(arr));
    }
}