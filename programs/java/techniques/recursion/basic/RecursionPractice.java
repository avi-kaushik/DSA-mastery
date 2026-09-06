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
     * Returns the sum of the first n natural numbers using recursion.
     *
     * @param n The upper limit of the sequence.
     * @return The sum of the first n natural numbers.
     * @throws IllegalArgumentException If n is negative.
     */
    public static int getSum(int n) {
        // Validate the input.
        // The sum of natural numbers is not defined for negative values.
        if (n < 0)
            throw new IllegalArgumentException("n cannot be negative.");

        // Base case:
        // The sum of the first 0 natural numbers is 0.
        if (n == 0)
            return 0;

        // Recursively add the current number
        // to the sum of the previous numbers.
        return n + getSum(n - 1);
    }

    /**
     * Returns the sum of the digits of a given number using recursion.
     *
     * @param digit The number whose digits are to be summed.
     * @return The sum of the digits.
     * @throws IllegalArgumentException If digit is negative.
     */
    public static int getSumOfDigits(int digit) {
        // Validate the input.
        // The sum of digits is not defined for negative values.
        if (digit < 0)
            throw new IllegalArgumentException("digit cannot be negative.");

        // Base case:
        // Once no digits remain, return 0.
        if (digit == 0)
            return 0;

        // Extract the last digit.
        int lastDigit = digit % 10;

        // Remove the last digit.
        int remainingDigits = digit / 10;

        // Recursively add the last digit
        // to the sum of the remaining digits.
        return lastDigit + getSumOfDigits(remainingDigits);
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
        System.out.println("Sum of first 5 numbers: " + getSum(5));
        System.out.println("Sum of digits 1549: " + getSumOfDigits(1549));
    }
}
