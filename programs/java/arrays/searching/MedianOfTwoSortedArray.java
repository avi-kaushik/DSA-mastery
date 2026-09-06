package programs.java.arrays.searching;

import java.util.Arrays;

public class MedianOfTwoSortedArray {

    /*
     * APPROACH 1 (OPTIMAL) - BINARY SEARCH
     * -----------------------------------
     * Time Complexity: O(log(min(n1, n2)))
     * Space Complexity: O(1)
     *
     * IDEA:
     * We are NOT merging arrays.
     * We are trying to find a CORRECT PARTITION such that:
     *
     * left part <= right part
     *
     * Partition both arrays:
     * arr1 -> [left1 | right1]
     * arr2 -> [left2 | right2]
     *
     * Condition for correct partition:
     * max(left1, left2) <= min(right1, right2)
     *
     * We always binary search on SMALLER array to reduce complexity.
     *
     * WHY (n1 + n2 + 1) / 2 ?
     * To ensure left half contains equal elements (or +1 for odd case)
     * so that median lies in LEFT part.
     */

    static double getMedianOfSortedArray(int arr1[], int arr2[]) {

        // Ensure arr1 is always the smaller array
        if (arr1.length > arr2.length) {
            return getMedianOfSortedArray(arr2, arr1);
        }

        int n1 = arr1.length, n2 = arr2.length;

        int start = 0, end = n1;

        while (start <= end) {

            // Partition for arr1
            int mid1 = (start + end) / 2;

            // Partition for arr2 (calculated automatically)
            int mid2 = (n1 + n2 + 1) / 2 - mid1;

            /*
             * Handle edge cases:
             * If no elements on left/right, use -infinity / +infinity
             */
            int left1 = (mid1 == 0) ? Integer.MIN_VALUE : arr1[mid1 - 1];
            int left2 = (mid2 == 0) ? Integer.MIN_VALUE : arr2[mid2 - 1];

            int right1 = (mid1 == n1) ? Integer.MAX_VALUE : arr1[mid1];
            int right2 = (mid2 == n2) ? Integer.MAX_VALUE : arr2[mid2];

            /*
             * CHECK: Correct Partition
             */
            if (left1 <= right2 && left2 <= right1) {

                // EVEN total elements
                if ((n1 + n2) % 2 == 0) {
                    int maxLeft = Math.max(left1, left2);
                    int minRight = Math.min(right1, right2);

                    // IMPORTANT: use 2.0 to avoid integer division
                    return ((double) maxLeft + minRight) / 2;
                }
                // ODD total elements
                else {
                    return Math.max(left1, left2);
                }
            }

            /*
             * If left1 is too big → move left
             */
            else if (left1 > right2) {
                end = mid1 - 1;
            }

            /*
             * If left2 is too big → move right
             */
            else {
                start = mid1 + 1;
            }
        }

        // Should never reach here for valid input
        return 0.0;
    }

    /*
     * APPROACH 2 (BRUTE FORCE / SIMPLE)
     * ---------------------------------
     * Time Complexity: O(n1 + n2)
     * Space Complexity: O(n1 + n2)
     *
     * IDEA:
     * Merge both arrays (like merge sort)
     * Then directly find median from merged array
     */

    static double getMedianOfSortedArray2(int arr1[], int arr2[]) {

        int n1 = arr1.length;
        int n2 = arr2.length;

        int[] merged = new int[n1 + n2];

        int i = 0, j = 0, k = 0;

        // Merge step (like merge sort)
        while (i < n1 && j < n2) {
            if (arr1[i] < arr2[j]) {
                merged[k++] = arr1[i++];
            } else {
                merged[k++] = arr2[j++];
            }
        }

        // Copy remaining elements
        while (i < n1) {
            merged[k++] = arr1[i++];
        }

        while (j < n2) {
            merged[k++] = arr2[j++];
        }

        int total = n1 + n2;

        // EVEN case
        if (total % 2 == 0) {
            return (merged[total / 2 - 1] + merged[total / 2]) / 2.0;
        }

        // ODD case
        else {
            return merged[total / 2];
        }
    }

    public static void main(String[] args) {

        int arr1[] = { 1, 2, 3, 4, 5, 6 };
        int arr2[] = { 10, 20, 30, 40, 50, 60 };

        System.out.println("1st Array: " + Arrays.toString(arr1));
        System.out.println("2nd Array: " + Arrays.toString(arr2));

        System.out.println("Median (Brute): " + getMedianOfSortedArray2(arr1, arr2));
        System.out.println("Median (Optimal): " + getMedianOfSortedArray(arr1, arr2));
    }
}
