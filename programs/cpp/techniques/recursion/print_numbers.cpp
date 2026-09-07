#include <iostream>
#include <stdexcept>

using namespace std;

/**
 * Calculates the factorial of a non-negative integer using recursion.
 *
 * The factorial of n is defined as:
 *     n! = n * (n - 1)!
 *
 * The recursion continues until it reaches the base case n = 0 or n = 1,
 * where the factorial is 1.
 *
 * Example:
 *     factorial(5)
 *     Output: 120
 *
 * @param n The non-negative integer whose factorial is to be calculated.
 * @throws runtime_error If n is negative.
 *
 * @complexity
 * Time:  O(n)
 * Space: O(n) - Recursion call stack.
 */
int factorial(int n)
{
    // Edge case: factorial is defined only for non-negative integers.
    if (n < 0)
        throw runtime_error("Number must be non-negative.");

    // Base case: 0! and 1! are both 1.
    if (n == 0 || n == 1)
        return 1;

    // Recursive case: n! = n * (n - 1)!.
    return n * factorial(n - 1);
}

/**
 * Calculates the Fibonacci number at index n using recursion.
 *
 * The Fibonacci sequence is defined as:
 *     F(0) = 0
 *     F(1) = 1
 *     F(n) = F(n - 1) + F(n - 2)
 *
 * This implementation uses the direct recursive definition, which
 * results in repeated calculation of the same subproblems.
 *
 * Example:
 *     fibonacci_number(4)
 *     Output: 3
 *
 * @param n The non-negative index of the Fibonacci number.
 * @throws runtime_error If n is negative.
 *
 * @complexity
 * Time:  O(2^n) - Exponential due to overlapping recursive calls.
 * Space: O(n) - Maximum recursion depth.
 */
int fibonacci_number(int n)
{
    // Edge case: Fibonacci index must be non-negative.
    if (n < 0)
        throw runtime_error("Number must be non-negative.");

    // Base cases: F(0) = 0 and F(1) = 1.
    if (n == 0 || n == 1)
        return n;

    // Recursive case: F(n) = F(n - 1) + F(n - 2).
    return fibonacci_number(n - 1) + fibonacci_number(n - 2);
}

/**
 * Calculates the sum of digits of a positive integer using recursion.
 *
 * Approach:
 * - Extract the last digit using the modulo (%) operator.
 * - Remove the last digit using integer division (/).
 * - Recursively calculate the sum of the remaining digits.
 *
 * Base Case:
 * - When number becomes 0 or negative, return 0.
 *
 * Time Complexity: O(log n)
 * Auxiliary Space: O(log n) due to the recursive call stack.
 *
 * @param number The positive integer whose digits are to be summed.
 * @return The sum of all digits.
 */
int get_sum_of_digits(int number)
{
    if (number <= 0)
        return 0;

    return (number % 10) + get_sum_of_digits(number / 10);
}

/**
 * Prints the numbers from 1 to n in ascending order using recursion.
 *
 * The function first recursively processes n - 1 and prints the current
 * number after the recursive call returns. This causes the numbers to be
 * printed during the unwinding phase of recursion.
 *
 * Example:
 *     print_number_series(4)
 *     Output: 1234
 *
 * @param n The upper limit of the number series.
 * @throws runtime_error If n is negative.
 *
 * @complexity
 * Time:  O(n)
 * Space: O(n) - Recursion call stack.
 */
void print_number_series(int n)
{
    // Edge case: n must be non-negative.
    if (n < 0)
        throw runtime_error("Number must be non-negative.");

    // Base case: stop recursion when n reaches 0.
    if (n == 0)
        return;

    // Recursive case: process the previous number first.
    print_number_series(n - 1);

    // Print the current number during recursion unwinding.
    cout << n;
}

/**
 * Prints the numbers from n to 1 in descending order using recursion.
 *
 * The current number is printed before making the recursive call, so
 * the numbers are printed during the downward phase of recursion.
 *
 * Example:
 *     print_number_series_reverse(4)
 *     Output: 4321
 *
 * @param n The upper limit of the number series.
 * @throws runtime_error If n is negative.
 *
 * @complexity
 * Time:  O(n)
 * Space: O(n) - Recursion call stack.
 */
void print_number_series_reverse(int n)
{
    // Edge case: n must be non-negative.
    if (n < 0)
        throw runtime_error("Number must be non-negative.");

    // Base case: stop recursion when n reaches 0.
    if (n == 0)
        return;

    // Print the current number before recursion.
    cout << n;

    // Recursive case: process the previous number.
    print_number_series_reverse(n - 1);
}

/**
 * Calculates the sum of the first n natural numbers using recursion.
 *
 * The sum of the first n natural numbers is defined as:
 *     1 + 2 + 3 + ... + n
 *
 * The function recursively calculates the sum of the first n - 1
 * natural numbers and adds n to the result.
 *
 * Example:
 *     sum_of_natural_numbers_series(4)
 *     Output: 10
 *
 * @param n The number of natural numbers to sum. Must be positive.
 * @throws runtime_error If n is not a positive integer.
 *
 * @complexity
 * Time:  O(n)
 * Space: O(n) - Recursion call stack.
 */
int sum_of_natural_numbers_series(int n)
{
    // Edge case: n must be a positive integer.
    if (n <= 0)
        throw runtime_error("Number must be a positive integer.");

    // Base case: the sum of the first natural number is 1.
    if (n == 1)
        return 1;

    // Recursive case: sum of first n numbers = n + sum of first n - 1 numbers.
    return n + sum_of_natural_numbers_series(n - 1);
}

/**
 * Program entry point.
 *
 * @return 0 if the program executes successfully.
 */
int main()
{
    int n = 4;

    cout << "Number series of " << n << ": ";
    print_number_series(n);
    cout << '\n';

    cout << "Number series (in reverse) of " << n << ": ";
    print_number_series_reverse(n);
    cout << '\n';

    cout << "Factorial of 5: " << factorial(5) << endl;

    cout << "4th Fibonacci Number: " << fibonacci_number(4) << endl;

    cout << "Sum of first 10 natural numbers: " << sum_of_natural_numbers_series(10) << endl;

    cout << "Sum of digits of number 4557: " << get_sum_of_digits(4557) << endl;

    return 0;
}
