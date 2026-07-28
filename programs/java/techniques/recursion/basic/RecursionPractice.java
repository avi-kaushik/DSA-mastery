package programs.java.techniques.recursion.basic;

public class RecursionPractice {

    /**
     * Returns the nth Fibonacci number using recursion.
     *
     * @param n The position in the Fibonacci sequence.
     * @return The nth Fibonacci number.
     * @throws IllegalArgumentException If n is negative.
     */
    public static int getFibonacci(int n) {
        // Validate the input.
        // Fibonacci numbers are not defined for negative values.
        if (n < 0)
            throw new IllegalArgumentException("n cannot be negative.");

        // Base case:
        // The 0th Fibonacci number is 0.
        if (n == 0)
            return 0;

        // Base case:
        // The 1st Fibonacci number is 1.
        if (n == 1)
            return 1;

        // Recursively calculate the current Fibonacci number
        // by summing the previous two Fibonacci numbers.
        return getFibonacci(n - 1) + getFibonacci(n - 2);
    }

    /**
     * Returns the factorial of a given number using recursion.
     *
     * @param n The number whose factorial is to be calculated.
     * @return The factorial of n.
     */
    public static int getFactorial(int n) {
        // Base case:
        // The factorial of 0 is defined as 1.
        if (n == 0)
            return 1;

        // Recursively multiply the current number
        // with the factorial of the previous number.
        return n * getFactorial(n - 1);
    }

    /**
     * Prints numbers from 1 to n using recursion.
     *
     * @param n The upper limit of the sequence.
     */
    public static void print1ToN(int n) {
        // Base case:
        // Stop the recursion once n becomes 0.
        if (n == 0)
            return;

        // Recursively print numbers from 1 to (n - 1).
        print1ToN(n - 1);

        // Print the current number while returning
        // from the recursive calls.
        System.out.println(n);
    }

    /**
     * Prints numbers from n to 1 using recursion.
     *
     * @param n The starting number of the sequence.
     */
    public static void printNTo1(int n) {
        // Base case:
        // Stop the recursion once n becomes 0.
        if (n == 0)
            return;

        // Print the current number before making
        // the recursive call.
        System.out.println(n);

        // Recursively print the remaining numbers.
        printNTo1(n - 1);
    }

    public static void main(String[] args) {
        print1ToN(4);

        System.out.println();

        printNTo1(4);

        System.out.println("Factorial of 5: " + getFactorial(5));
        System.out.println("7th Fibonacci: " + getFibonacci(7));
    }
}
