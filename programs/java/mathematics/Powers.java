package programs.java.mathematics;

/**
 * Contains mathematical operations related to powers and exponentiation.
 */
public class Powers {

    /**
     * Computes x raised to the power n using recursive exponentiation by squaring.
     *
     * The algorithm reduces the exponent by half at every recursive call.
     *
     * For an even exponent:
     * x^n = (x^(n/2)) * (x^(n/2))
     *
     * For an odd exponent:
     * x^n = (x^(n/2)) * (x^(n/2)) * x
     *
     * The base case is n = 0, where x^0 = 1.
     *
     * This approach avoids performing n individual multiplications and reduces
     * the number of recursive calls by dividing the exponent by 2 at each step.
     *
     * This implementation supports non-negative values of n.
     *
     * Time Complexity: O(log(n))
     * Space Complexity: O(log(n))
     *
     * @param x the base number
     * @param n the non-negative exponent
     * @return x raised to the power n
     */
    public static int computePower(int x, int n) {

        if (n == 0)
            return 1;

        int halfPower = computePower(x, n / 2);
        int power = halfPower * halfPower;

        if (n % 2 == 0)
            return power;
        else
            return power * x;
    }

    /**
     * Computes the power of a number using iterative exponentiation by squaring.
     *
     * The algorithm uses the binary representation of the exponent to reduce
     * the number of multiplications required to compute x^n.
     *
     * If the current exponent is odd, the current value of x is multiplied
     * into the result. The value of x is then squared, and the exponent is
     * divided by 2. Squaring x generates the powers x^1, x^2, x^4, x^8, ...
     * while dividing n by 2 processes the corresponding binary digits of n.
     *
     * For example, when n = 13:
     *
     * 13 = 8 + 4 + 1
     *
     * Therefore:
     *
     * x^13 = x^8 * x^4 * x^1
     *
     * The algorithm generates these powers through repeated squaring and
     * multiplies only the powers required by the binary representation of n.
     *
     * This reduces the number of iterations from O(n) to O(log(n)).
     *
     * Time Complexity: O(log(n))
     * Space Complexity: O(1)
     *
     * @param x the base number
     * @param n the non-negative exponent
     * @return x raised to the power of n
     */
    public static int computePowerIterative(int x, int n) {

        int power = 1;

        while (n > 0) {
            if (n % 2 != 0)
                power *= x;

            x *= x;
            n /= 2;
        }

        return power;
    }

    public static void main(String[] args) {

        System.out.println("2^3 = " + computePower(2, 3));
    }
}
