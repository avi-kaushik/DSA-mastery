package programs.java.techniques.recursion.basic;

public class Palindrome {

    /**
     * Checks whether a given string is a palindrome using recursion.
     *
     * @param pattern The string to be checked.
     * @param start   The starting index of the current substring.
     * @param end     The ending index of the current substring.
     * @return {@code true} if the string is a palindrome; otherwise {@code false}.
     * @throws IllegalArgumentException If the input string is {@code null}.
     */
    public static boolean isPalindrome(String pattern, int start, int end) {
        // Validate the input.
        // A null string cannot be checked for palindrome.
        if (pattern == null)
            throw new IllegalArgumentException("String cannot be null.");

        // Base case:
        // If the pointers have met or crossed, all corresponding
        // characters have matched, so the string is a palindrome.
        if (start >= end)
            return true;

        // If the characters at the current positions do not match,
        // the string is not a palindrome.
        if (pattern.charAt(start) != pattern.charAt(end))
            return false;

        // Recursively compare the next pair of characters
        // by moving the pointers toward the center.
        return isPalindrome(pattern, start + 1, end - 1);
    }

    public static void main(String[] args) {
        String pattern = "abba";

        System.out.println("String: " + pattern);
        System.out.println("Is Palindrome: " + isPalindrome(pattern, 0, pattern.length() - 1));
    }
}
