package programs.java.mathematics;

/**
 * Calculates the factorial of a non-negative integer.
 */
public class Factorial {

    /**
     * Calculates the factorial recursively by multiplying the current number
     * with the factorial of the preceding number until reaching the base case.
     * 
     * Recurrence Relation: T(n) = T(n-1) + O(1)
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     *
     * @param n the non-negative integer whose factorial is to be calculated
     * @return the factorial of the given number
     * @throws RuntimeException if the given number is negative
     */
    public static int getFactorialRecursive(int n) {

        if (n < 0)
            throw new RuntimeException("Can't get factorial of negative number");

        if (n == 1 || n == 0)
            return 1;

        return n * getFactorialRecursive(n - 1);
    }

    /**
     * Calculates the factorial iteratively by repeatedly multiplying the
     * current number with the result and decrementing it until reaching 1.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     *
     * @param n the non-negative integer whose factorial is to be calculated
     * @return the factorial of the given number
     * @throws RuntimeException if the given number is negative
     */
    public static int getFactorial(int n) {

        if (n < 0)
            throw new RuntimeException("Can't get factorial of negative number");

        int factorial = 1;

        while (n > 1) {

            factorial *= n;
            n--;
        }

        return factorial;
    }

    /**
     * This number of trailing zeros will be equal to the number of factors of 5
     * present in the factorial number.
     *
     * Reason-
     * - A trailing zero is achieved when a factor of 2 is multiplied by a factor of
     * 5.
     * - The total number of factors of 2 present in a factorial is more than the
     * number of factors of 5.
     * - So, the number of factors of 5 determines the number of trailing zeros.
     *
     * 1. Check how many multiples of 5 are present.
     * 2. Save that count in a variable.
     * 3. Now check how many multiples of 25 are present.
     * - Numbers like 25, 50, 75 etc. have an extra factor of 5, so they add
     * one extra trailing zero compared to numbers having a single factor of 5.
     * 4. Add this count to the same variable.
     * 5. Continue checking for multiples of 125, 625 and more, as these numbers
     * contain three, four and more factors of 5 respectively.
     * 6. Continue until the current power of 5 becomes greater than the given
     * number.
     * 7. Return the count value as the answer.
     *
     * Time Complexity: O(log₅(n))
     * Space Complexity: O(1)
     *
     * @param n the number whose factorial's trailing zeros are to be counted
     * @return the number of trailing zeros in n!
     */
    public static int countTrailingZero(int n) {

        int zeros = 0;

        for (int i = 5; i <= n; i = i * 5) {
            zeros += n / i;
        }

        return zeros;
    }

    public static void main(String[] args) {

        System.out.println("Factorial of 6: " + getFactorial(6));

        System.out.println("Factorial of 10 (Recursive method): " + getFactorialRecursive(10));

        System.out.println("Trailing zeros in factorial of 10: " + countTrailingZero(10));
    }
}
