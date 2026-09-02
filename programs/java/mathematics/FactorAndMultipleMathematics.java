package programs.java.mathematics;

// Contains mathematical operations related to factors and multiples.
public class FactorAndMultipleMathematics {

    /**
     * Finds the highest common factor of two numbers using the Euclidean Algorithm.
     *
     * The Euclidean Algorithm is an efficient method for finding the highest
     * common factor by repeatedly replacing the larger number with the remainder
     * obtained when it is divided by the smaller number.
     *
     * The key observation is that the common factors of two numbers are the same
     * as the common factors of the smaller number and their remainder.
     *
     * This process continues until the remainder becomes 0. The last non-zero
     * divisor is the highest common factor.
     *
     * Time Complexity: O(log(min(x, y)))
     * Space Complexity: O(1)
     *
     * @param x the first number
     * @param y the second number
     * @return the highest common factor of x and y
     */
    public static int getHighestCommonFactor(int x, int y) {

        int a = Math.max(x, y), b = Math.min(x, y);

        while (b != 0) {

            int remainder = a % b;

            a = b;

            b = remainder;
        }

        return a;
    }

    /**
     * Finds the lowest common multiple of two numbers using their highest
     * common factor.
     *
     * The lowest common multiple is the smallest positive number that is
     * divisible by both numbers.
     *
     * The relationship between HCF and LCM is:
     *
     * LCM(x, y) = (x / HCF(x, y)) * y
     *
     * The division is performed before multiplication to reduce the possibility
     * of integer overflow.
     *
     * Time Complexity: O(log(min(x, y)))
     * Space Complexity: O(1)
     *
     * @param x the first number
     * @param y the second number
     * @return the lowest common multiple of x and y
     */
    public static int getLowestCommonMultiple(int x, int y) {

        return (x / getHighestCommonFactor(x, y)) * y;
    }

    public static void main(String[] args) {

        System.out.println("HCF of (90, 120): " + getHighestCommonFactor(90, 120));

        System.out.println("LCM of (90, 120): " + getLowestCommonMultiple(90, 120));
    }
}
