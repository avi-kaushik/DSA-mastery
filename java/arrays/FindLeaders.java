
class FindLeaders {

    /**
     * This function prints all the "leaders" in the given array. A leader is
     * defined as an element that is strictly greater than all the elements to
     * its right.
     *
     * The function works by:
     *
     * 1. Starting from the rightmost element (which is always a leader).
     *
     * 2. Tracking the current leader as we move from right to left.
     *
     * 3. Whenever we find an element greater than the current leader, we update
     * the leader and print it.
     *
     * Time Complexity: O(n) → The array is traversed exactly once from right to
     * left.
     *
     * Space Complexity: O(1) → No extra data structures are used apart from a
     * few variables.
     *
     * Auxiliary Space: O(1) → Constant additional memory usage.
     *
     * Input Example: arr = {14, 12, 98, 1, 54, 12, 21, 4, 8, 4}
     *
     * Output Example: Leader: 4 Leader: 8 Leader: 21 Leader: 54 Leader: 98
     *
     * (Note: Leaders are printed in reverse order, i.e., from right to left.)
     */
    public static void printLeaders(int arr[]) {
        int leader = arr[arr.length - 1]; // The rightmost element is always a leader
        System.out.println("Leader: " + leader);

        // Traverse the array from second last element to the start
        for (int i = arr.length - 2; i >= 0; i--) {
            if (arr[i] > leader) { // Found a new leader
                leader = arr[i];
                System.out.println("Leader: " + leader);
            }
        }
    }

    public static void main(String[] args) {
        int arr[] = {14, 12, 98, 1, 54, 12, 21, 4, 8, 4};

        printLeaders(arr);
    }

}
