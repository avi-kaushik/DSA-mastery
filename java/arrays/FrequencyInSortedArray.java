// Class to print the frequency of each unique element in a sorted integer array.

class FrequencyInSortedArray {

    /**
     * Prints the frequency of each unique element in a sorted integer array.
     *
     * The array must be sorted in non-decreasing order. This method counts
     * consecutive equal elements and prints their frequency. Since the array is
     * sorted, all identical elements are adjacent, allowing a single linear
     * pass without using extra data structures.
     *
     * Time Complexity: O(n) - Each element is visited exactly once.
     *
     * Space Complexity: O(1) - Uses only a constant amount of extra variables.
     *
     * Auxiliary Space: O(1) - No recursion or additional storage is required.
     *
     * @param arr The sorted integer array whose element frequencies are to be
     * printed. If the array is empty, no output is produced.
     *
     * Example:
     *
     * Input: int[] arr = {10, 10, 10, 20, 20, 30};
     *
     * Output: Frequency of 10: 3 Frequency of 20: 2 Frequency of 30: 1
     *
     */
    public static void printFrequenciesInSortedArray(int arr[]) {
        if (arr.length == 0) {
            return; // avoid error for empty array
        }

        int count = 1;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == arr[i - 1]) {
                count++; // Same element as previous → increment count
            } else {
                // New element found => print previous element's frequency
                System.out.println("Frequency of " + arr[i - 1] + ": " + count);
                count = 1; // Reset count for the new element
            }
        }

        // print last element frequency
        System.out.println("Frequency of " + arr[arr.length - 1] + ": " + count);
    }

    public static void main(String[] args) {
        int arr[] = {10, 10, 20, 30, 30, 30};

        printFrequenciesInSortedArray(arr);
    }
}
