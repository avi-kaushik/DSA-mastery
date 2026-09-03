package programs.java.mathematics;

// Contains operations related to prime numbers.
public class PrimeNumber {

    /**
     * Checks whether the given number is a prime number.
     *
     * A prime number is a number greater than 1 that has only two factors:
     * 1 and itself.
     *
     * The algorithm first handles numbers smaller than 2, the number 2,
     * and even numbers. It then checks only odd divisors from 3 up to
     * the square root of the given number.
     *
     * Checking only up to the square root is sufficient because if a number
     * has a factor greater than its square root, it must also have a factor
     * smaller than its square root.
     *
     * Time Complexity: O(sqrt(n))
     * Space Complexity: O(1)
     *
     * @param n the number to check
     * @return true if n is prime, otherwise false
     */
    public static boolean isPrime(int n) {

        if (n == 2)
            return true;

        if (n < 2 || n % 2 == 0)
            return false;

        for (int i = 3; i <= Math.sqrt(n); i += 2) {

            if (n % i == 0)
                return false;
        }

        return true;
    }

    /**
     * Prints all prime factors of the given number.
     *
     * The algorithm checks potential factors starting from 2. Whenever a
     * factor is found, it repeatedly divides the number by that factor
     * until the factor is no longer present.
     *
     * Repeated division is necessary because a prime factor can occur
     * multiple times in the prime factorization.
     *
     * The loop only checks factors up to the square root of the remaining
     * number. After all possible factors have been removed, if the remaining
     * number is greater than 1, it must itself be a prime factor.
     *
     * For example:
     * 60 = 2 * 2 * 3 * 5
     *
     * Time Complexity: O(sqrt(n))
     * Space Complexity: O(1)
     *
     * @param n the number whose prime factors are to be printed
     */
    public static void printPrimeFactors(int n) {

        if (n == 1)
            return;

        for (int i = 2; i * i <= n; i++) {

            while (n % i == 0) {
                System.out.print(i + " ");
                n = n / i;
            }
        }

        if (n > 1)
            System.out.print(n);

        System.out.println();
    }

    public static void main(String[] args) {

        System.out.printf("Is %d prime? %s\n", 139, isPrime(139) ? "Yes" : "No");

        System.out.printf("Prime factors of %d: ", 21);
        printPrimeFactors(21);
    }
}
