
class TrappingRainWater {

    /**
     * Calculates total trapped rain water.
     *
     * Approach:
     * 1. For each index, water trapped depends on:
     * min(max height on left, max height on right) - current height
     * 2. Precompute:
     * - leftMax[i] = maximum height from 0 → i
     * - rightMax[i] = maximum height from i → n-1
     *
     * Time Complexity : O(n)
     * Space Complexity : O(n)
     */
    public static int getUnitsStored(int arr[]) {
        // If less than 3 bars, water cannot be trapped
        if (arr.length <= 2)
            return 0;

        // leftMax[i] stores maximum height from index 0 to i
        int[] leftMax = new int[arr.length];

        // rightMax[i] stores maximum height from index i to n-1
        int[] rightMax = new int[arr.length];

        // Initialize first element
        leftMax[0] = arr[0];

        // Fill prefix maximum array
        for (int i = 1; i < arr.length; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], arr[i]);
        }

        // Initialize last element
        rightMax[arr.length - 1] = arr[arr.length - 1];

        // Fill suffix maximum array
        for (int i = arr.length - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], arr[i]);
        }

        int totalUnits = 0;

        // Calculate trapped water at each index
        for (int i = 1; i < arr.length - 1; i++) {
            totalUnits += Math.min(leftMax[i], rightMax[i]) - arr[i];
        }

        return totalUnits;
    }

    public static void main(String[] args) {
        int arr[] = { 3, 1, 2, 1, 3 };
        System.out.println("Units stored: " + getUnitsStored(arr));
    }
}
