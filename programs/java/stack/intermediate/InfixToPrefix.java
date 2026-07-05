package programs.java.stack.intermediate;

public class InfixToPrefix {

    /**
     * Reverses the given infix expression and swaps opening and closing
     * parentheses.
     *
     * This transformation prepares the expression for conversion into
     * postfix notation, which can then be reversed to obtain the
     * equivalent prefix expression.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     *
     * @param pattern Infix expression to be reversed.
     * @return Reversed expression with opening and closing parentheses
     *         interchanged.
     */
    private static String _reversePattern(String pattern) {
        StringBuilder expression = new StringBuilder(pattern).reverse();

        for (int i = 0; i < expression.length(); i++) {
            char character = expression.charAt(i);

            // Swap opening parenthesis with closing parenthesis.
            if (character == '(')
                expression.setCharAt(i, ')');

            // Swap closing parenthesis with opening parenthesis.
            else if (character == ')')
                expression.setCharAt(i, '(');
        }

        return expression.toString();
    }

    /**
     * Converts an infix expression into its equivalent prefix expression.
     *
     * Working:
     * 1. Reverse the infix expression.
     * 2. Swap opening and closing parentheses.
     * 3. Convert the transformed expression into postfix notation.
     * 4. Reverse the postfix expression to obtain the prefix expression.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     *
     * @param pattern Infix expression to be converted.
     * @return Equivalent prefix expression.
     */
    public static String convert(String pattern) {

        // Reverse the expression and interchange parentheses.
        String reverse = _reversePattern(pattern);

        // Convert the reversed infix expression into postfix notation.
        String expression = InfixToPostfix.convert(reverse, true);

        // Reverse the postfix expression to obtain the prefix expression.
        return new StringBuilder(expression).reverse().toString();
    }

    public static void main(String[] args) {
        String pattern = "x + y * z";

        System.out.println("Infix expression: " + pattern);
        System.out.println("Prefix: " + convert(pattern));
    }
}
