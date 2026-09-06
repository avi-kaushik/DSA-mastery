package programs.java.techniques.recursion.intermediate;

public class RopeCuttingProblem {

    /**
     * Returns the maximum among the three given values.
     *
     * @param a First value.
     * @param b Second value.
     * @param c Third value.
     * @return The largest value among a, b and c.
     */
    private static int getMax(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }

    /**
     * Returns the maximum number of pieces that can be obtained by cutting
     * a rope of length {@code n} into pieces of length {@code a}, {@code b},
     * or {@code c}.
     *
     * <p>
     * The method recursively tries all three possible cuts and chooses
     * the one that results in the maximum number of valid pieces.
     * </p>
     *
     * <p>
     * Returns {@code -1} if it is impossible to cut the rope exactly
     * using the given lengths.
     * </p>
     *
     * @param n The current length of the rope.
     * @param a First allowed cut length.
     * @param b Second allowed cut length.
     * @param c Third allowed cut length.
     * @return The maximum number of pieces, or {@code -1} if no valid
     *         combination of cuts exists.
     */
    public static int getTotalCutsPossibleInRope(int n, int a, int b, int c) {

        // Base case:
        // The rope has been completely cut into valid pieces.
        if (n == 0)
            return 0;

        // Base case:
        // The rope length became negative, which means
        // the current sequence of cuts is invalid.
        if (n < 0)
            return -1;

        // Recursively try all three possible cuts and
        // choose the one that produces the maximum pieces.
        int res = getMax(
                getTotalCutsPossibleInRope(n - a, a, b, c),
                getTotalCutsPossibleInRope(n - b, a, b, c),
                getTotalCutsPossibleInRope(n - c, a, b, c));

        // If none of the three recursive calls produced
        // a valid solution, propagate the failure.
        if (res == -1)
            return -1;

        // Count the current cut and return the total.
        return res + 1;
    }

    public static void main(String[] args) {
        System.out.println("Total cuts possible in rope: "
                + getTotalCutsPossibleInRope(23, 12, 9, 11));
    }
}
