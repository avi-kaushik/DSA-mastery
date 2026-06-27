package programs.java.stack.intermediate;

import java.util.ArrayDeque;

public class InfixToPostfix {

    /**
     * Checks whether the given character is a supported arithmetic operator.
     *
     * @param character Character to be checked.
     * @return {@code true} if the character is an operator, otherwise
     *         {@code false}.
     */
    private static boolean _isOperator(char character) {
        return character == '*' || character == '/'
                || character == '+' || character == '-'
                || character == '^';
    }

    /**
     * Returns the precedence value of the given operator.
     * A higher precedence value indicates that the operator should be evaluated
     * first.
     *
     * @param operator Operator whose precedence needs to be determined.
     * @return Precedence value of the operator, or {@code -1} for unsupported
     *         characters.
     */
    private static int _precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;

            case '*':
            case '/':
                return 2;

            case '^':
                return 3;

            default:
                return -1;
        }
    }

    /**
     * Determines whether the operator at the top of the stack should be moved
     * to the postfix expression before pushing the current operator.
     *
     * Operators having higher or equal precedence are popped first.
     * The exponent operator is right-associative and therefore is not popped
     * when another exponent operator is encountered.
     *
     * @param stackOperator   Operator currently present at the top of the stack.
     * @param currentOperator Operator currently being processed.
     * @return {@code true} if the stack operator should be popped, otherwise
     *         {@code false}.
     */
    private static boolean _shouldPop(char stackOperator, char currentOperator) {
        if (stackOperator == '^' && currentOperator == '^')
            return false;

        return _precedence(stackOperator) >= _precedence(currentOperator);
    }

    /**
     * Converts an infix expression into its equivalent postfix expression.
     *
     * Working:
     * 1. Traverse the expression from left to right.
     * 2. Append operands directly to the postfix expression.
     * 3. Push opening parentheses onto the stack.
     * 4. When a closing parenthesis is encountered, pop operators until the
     * matching opening parenthesis is found.
     * 5. For operators, pop all operators having higher or equal precedence
     * before pushing the current operator onto the stack.
     * 6. After traversal, move all remaining operators from the stack to the
     * postfix expression.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     *
     * @param pattern Infix expression to be converted.
     * @return Equivalent postfix expression.
     */
    public static String convert(String pattern) {
        ArrayDeque<Character> stack = new ArrayDeque<>();

        StringBuilder builder = new StringBuilder();

        for (char character : pattern.toCharArray()) {

            // Ignore whitespaces.
            if (Character.isWhitespace(character))
                continue;

            // Opening parenthesis acts as a boundary for operator precedence.
            if (character == '(') {
                stack.push(character);
            }

            // Pop operators until the matching opening parenthesis is reached.
            else if (character == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    builder.append(stack.pop());
                }

                // Remove the matching opening parenthesis.
                if (!stack.isEmpty())
                    stack.pop();
            }

            // Move higher or equal precedence operators before pushing the current
            // operator.
            else if (_isOperator(character)) {
                while (!stack.isEmpty()
                        && stack.peek() != '('
                        && _shouldPop(stack.peek(), character)) {
                    builder.append(stack.pop());
                }

                stack.push(character);
            }

            // Operands are directly added to the postfix expression.
            else {
                builder.append(character);
            }
        }

        // Move any remaining operators to the postfix expression.
        while (!stack.isEmpty()) {
            builder.append(stack.pop());
        }

        return builder.toString();
    }

    public static void main(String[] args) {
        String pattern = "a * (b + c)";

        System.out.println("Infix expression: " + pattern);
        System.out.println("Postfix: " + convert(pattern));
    }
}
