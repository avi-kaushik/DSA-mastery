package programs.java.arrays.searching;

/**
 * Finds the floor value of the square root of a number using Binary Search.
 *
 * The floor square root is the largest integer whose square
 * is less than or equal to the given number.
 *
 * Example:
 * x = 27
 * sqrt(27) ≈ 5.19
 * Output = 5
 *
 * Time Complexity: O(log x)
 * Space Complexity: O(1)
 */
public class SquareRoot {

    /**
     * Returns the floor square root of a number.
     *
     * @param x the number whose square root is to be found
     * @return floor value of √x
     */
    static int getSquareRoot(int x) {

        int root = 0;
        int low = 0;
        int high = x;

        while (low <= high) {

            int mid = (low + high) / 2;
            int sqr = mid * mid;

            // Perfect square
            if (sqr == x) {
                return mid;
            }

            // mid is too large
            else if (sqr > x) {
                high = mid - 1;
            }

            // mid is a valid candidate
            else {
                root = mid;
                low = mid + 1;
            }
        }

        return root;
    }

    public static void main(String[] args) {

        int x = 27;

        System.out.println(getSquareRoot(x));
    }
}
