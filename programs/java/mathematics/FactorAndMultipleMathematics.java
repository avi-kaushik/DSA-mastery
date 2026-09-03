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

    /**
     * Prints all divisors of the given number in ascending order.
     *
     * Divisors occur in pairs. If i is a divisor of n, then n / i is also
     * a divisor of n.
     *
     * The first loop checks only up to the square root of n and prints the
     * smaller divisor from each divisor pair.
     *
     * After the first loop, i is one position beyond the square root of n.
     * The second loop goes backward from i - 1 and prints the corresponding
     * larger divisor from each pair.
     *
     * The second loop starts from i - 1 because the value i itself was not
     * checked by the first loop. This also prevents the square-root divisor
     * from being printed twice when n is a perfect square.
     *
     * Time Complexity: O(sqrt(n))
     * Space Complexity: O(1)
     *
     * @param n the number whose divisors are to be printed
     */
    public static void printDivisors(int n) {

        int i;

        for (i = 1; i * i <= n; i++) {

            if (n % i == 0)
                System.out.print(i + " ");
        }

        for (int j = i - 1; j >= 1; j--) {

            if (n % j == 0)
                System.out.print(n / j + " ");
        }

        System.out.println();
    }

    public static void main(String[] args) {

        System.out.println("HCF of (90, 120): " + getHighestCommonFactor(90, 120));

        System.out.println("LCM of (90, 120): " + getLowestCommonMultiple(90, 120));

        System.out.print("Divisors of 100: ");
        printDivisors(100);
    }
}
