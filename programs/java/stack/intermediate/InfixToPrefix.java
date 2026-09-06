package programs.java.stack.intermediate;

import java.util.ArrayDeque;

import programs.java.stack.common.InfixExpressionUtil;

public class InfixToPrefix {

    /**
     * Converts the currently accumulated operand into an integer and pushes it
     * onto the operand stack.
     *
     * Since prefix expressions are traversed from right to left, operand
     * characters are accumulated in reverse order. The operand is therefore
     * reversed before being converted into an integer.
     *
     * The operand buffer is cleared after it has been pushed so that the next
     * operand can be accumulated independently.
     *
     * @param stack   Stack storing operands for prefix evaluation.
     * @param operand Buffer containing the current operand being built.
     */
    private static void _pushOperand(ArrayDeque<Integer> stack, StringBuilder operand) {
        if (operand.length() > 0) {
            stack.push(Integer.parseInt(operand.reverse().toString()));
            operand.setLength(0);
        }
    }

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

    /**
     * Evaluates a prefix expression and returns its computed result.
     *
     * Working:
     * 1. Traverse the expression from right to left.
     * 2. Accumulate operand characters until a whitespace or operator is found.
     * 3. Push each completed operand onto the stack.
     * 4. When an operator is encountered, pop the top two operands, evaluate
     * the operation, and push the result back onto the stack.
     * 5. After traversal, the stack contains the final evaluated result.
     *
     * The expression must contain whitespace-separated operands when using
     * multi-digit numbers.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     *
     * @param pattern Prefix expression to be evaluated.
     * @return Result of the evaluated expression.
     * @throws IllegalArgumentException If the prefix expression is invalid.
     */
    public static int evaluate(String pattern) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();

        StringBuilder operand = new StringBuilder();

        for (int i = pattern.length() - 1; i >= 0; i--) {
            char character = pattern.charAt(i);

            // Push the accumulated operand when a separator is encountered.
            if (Character.isWhitespace(character)) {
                _pushOperand(stack, operand);
                continue;
            }

            if (InfixExpressionUtil.isOperator(character)) {

                // Push any operand accumulated before processing the operator.
                _pushOperand(stack, operand);

                if (stack.size() < 2)
                    throw new IllegalArgumentException("Invalid prefix expression.");

                int operand1 = stack.pop();
                int operand2 = stack.pop();

                // Evaluate the current operation and push the result.
                int result = InfixExpressionUtil.parseExpression(operand1, operand2, character);
                stack.push(result);
            } else {

                // Accumulate operand characters while traversing backwards.
                operand.append(character);
            }
        }

        // Push the final operand accumulated during traversal.
        _pushOperand(stack, operand);

        if (stack.size() != 1)
            throw new IllegalArgumentException("Invalid prefix expression.");

        return stack.pop();
    }

    public static void main(String[] args) {
        String pattern = "x + y * z";

        System.out.println("Infix expression: " + pattern);
        System.out.println("Prefix: " + convert(pattern));

        String evalPattern = "+ * 10 2 3";

        System.out.println("Prefix expression: " + evalPattern);
        System.out.println("Prefix value: " + evaluate(evalPattern));
    }
}
