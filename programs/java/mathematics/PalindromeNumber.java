package programs.java.mathematics;

/**
 * Checks whether a number is a palindrome.
 */
public class PalindromeNumber {

    /**
     * Reverses the digits of the number by repeatedly extracting the last
     * digit and appending it to the result, then compares the reversed number
     * with the original number to determine whether it is a palindrome.
     *
     * @param number the number to check
     * @return true if the number is a palindrome, otherwise false
     */
    public static boolean isPalindrome(int number) {

        int original = Math.abs(number), num = Math.abs(number);

        int result = 0;

        while (num > 0) {

            int digit = num % 10;

            result = (result * 10) + digit;

            num /= 10;
        }

        return original == result;
    }

    public static void main(String[] args) {

        int number = 8668;

        System.out.println("Is number " + number + " palindrome? " + (isPalindrome(number) ? "Yes" : "No"));
    }
}
