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

    public static void main(String[] args) {

        System.out.println("2^3 = " + computePower(2, 3));
    }
}
