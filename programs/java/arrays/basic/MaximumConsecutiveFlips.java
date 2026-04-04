class MaximumConsecutiveFlips {

    /**
     * Prints the minimum group ranges that need to be flipped
     * so that all elements in the binary array become the same.
     *
     * @param arr Binary input array (contains only 0 and 1)
     */
    static void flip(int arr[]) {

        for (int i = 1; i < arr.length; i++) {

            // Detect group boundary (group change)
            if (arr[i] != arr[i - 1]) {

                // If new group is different from first element,
                // this group must be flipped (start index)
                if (arr[i] != arr[0]) {
                    System.out.print("Flip from index " + i + " to ");
                }

                // Otherwise, close the previously started flip group
                else {
                    System.out.println(i - 1);
                }
            }
        }

        // If last element is different from first element,
        // close the final flip group
        if (arr[arr.length - 1] != arr[0]) {
            System.out.println(arr.length - 1);
        }
    }

    public static void main(String[] args) {
        int arr[] = { 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1 };
        flip(arr);
    }
}