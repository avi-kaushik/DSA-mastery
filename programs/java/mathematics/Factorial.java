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

    public static void main(String[] args) {

        System.out.println("Factorial of 6: " + getFactorial(6));

        System.out.println("Factorial of 10 (Recursive method): " + getFactorialRecursive(10));
    }
}
