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
     * Evaluates the given arithmetic operation using the supplied operands.
     *
     * @param operand1 Left-hand operand.
     * @param operand2 Right-hand operand.
     * @param operator Arithmetic operator to be applied.
     * @return Result of the evaluated expression.
     * @throws RuntimeException If the operator is not supported.
     */
    private static int _parseExpression(int operand1, int operand2, char operator) {
        switch (operator) {
            case '/':
                return operand1 / operand2;

            case '*':
                return operand1 * operand2;

            case '+':
                return operand1 + operand2;

            case '-':
                return operand1 - operand2;

            case '^':
                return (int) Math.pow(operand1, operand2);

            default:
                throw new RuntimeException("Invalid operator.");
        }
    }

    /**
     * Converts the currently accumulated operand into an integer and pushes it
     * onto the operand stack.
     *
     * The operand buffer is cleared after it has been pushed so that the next
     * operand can be accumulated independently.
     *
     * @param stack   Stack storing operands for postfix evaluation.
     * @param operand Buffer containing the current operand being built.
     */
    private static void _pushOperand(ArrayDeque<Integer> stack, StringBuilder operand) {
        if (operand.length() > 0) {
            stack.push(Integer.parseInt(operand.toString()));
            operand.setLength(0);
        }
    }

    /**
     * Determines whether the operator at the top of the stack should be moved
     * to the postfix expression before pushing the current operator.
     *
     * During a normal infix to postfix conversion, operators having higher or
     * equal precedence are popped first. The exponent operator is
     * right-associative and therefore is not popped when another exponent
     * operator is encountered.
     *
     * During infix to prefix conversion, the infix expression is first reversed.
     * Reversing the expression also reverses operator associativity. In this
     * case, only operators having strictly higher precedence are popped.
     *
     * @param stackOperator        Operator currently present at the top of the
     *                             stack.
     * @param currentOperator      Operator currently being processed.
     * @param reverseAssociativity {@code true} when processing a reversed infix
     *                             expression for prefix conversion, otherwise
     *                             {@code false}.
     * @return {@code true} if the stack operator should be popped, otherwise
     *         {@code false}.
     */
    private static boolean _shouldPop(char stackOperator,
            char currentOperator,
            boolean reverseAssociativity) {

        if (reverseAssociativity)
            return _precedence(stackOperator) > _precedence(currentOperator);

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
     * 5. For operators, pop operators according to precedence and associativity
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
        return convert(pattern, false);
    }

    /**
     * Converts an infix expression into its equivalent postfix expression.
     *
     * This overload allows the associativity rules to be adjusted when the
     * expression being processed is a reversed infix expression during
     * infix to prefix conversion.
     *
     * @param pattern              Infix expression to be converted.
     * @param reverseAssociativity {@code true} if the expression is a reversed
     *                             infix expression used for prefix conversion,
     *                             otherwise {@code false}.
     * @return Equivalent postfix expression.
     */
    public static String convert(String pattern, boolean reverseAssociativity) {
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

            // Move operators according to precedence and associativity before
            // pushing the current operator.
            else if (_isOperator(character)) {
                while (!stack.isEmpty()
                        && stack.peek() != '('
                        && _shouldPop(stack.peek(), character, reverseAssociativity)) {
                    builder.append(stack.pop());
                }

                stack.push(character);
            }

            // Operand characters are directly appended to the postfix expression.
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

    /**
     * Evaluates a postfix expression containing space-separated integer operands.
     *
     * Working:
     * 1. Traverse the expression from left to right.
     * 2. Accumulate digits to form complete operands.
     * 3. Push completed operands onto the stack whenever a whitespace or
     * operator is encountered.
     * 4. For every operator, pop the top two operands, evaluate the expression,
     * and push the result back onto the stack.
     * 5. After traversal, push any remaining operand onto the stack.
     * 6. The final value remaining in the stack is the evaluated result.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     *
     * @param pattern Postfix expression to be evaluated.
     * @return Evaluated result of the postfix expression.
     * @throws IllegalArgumentException If the postfix expression is invalid.
     */
    public static int evaluate(String pattern) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();

        // Stores the digits of the current operand being built.
        StringBuilder operand = new StringBuilder();

        for (char character : pattern.toCharArray()) {

            // Whitespace indicates the end of the current operand.
            if (Character.isWhitespace(character)) {
                _pushOperand(stack, operand);
                continue;
            }

            if (_isOperator(character)) {

                // Push the operand accumulated before the operator.
                _pushOperand(stack, operand);

                // Every operator requires two operands.
                if (stack.size() < 2)
                    throw new IllegalArgumentException("Invalid postfix expression.");

                int operand2 = stack.pop();
                int operand1 = stack.pop();

                // Evaluate the expression and push the result back onto the stack.
                stack.push(_parseExpression(operand1, operand2, character));
            }

            // Continue building the current operand.
            else {
                operand.append(character);
            }
        }

        // Push the final operand if the expression does not end with whitespace.
        _pushOperand(stack, operand);

        // A valid postfix expression must leave exactly one value in the stack.
        if (stack.size() != 1)
            throw new IllegalArgumentException("Invalid postfix expression.");

        return stack.pop();
    }

    public static void main(String[] args) {
        String pattern = "a * (b + c)";

        System.out.println("Infix expression: " + pattern);
        System.out.println("Postfix: " + convert(pattern));

        String evalPattern = "10 20 3 + *";

        System.out.println("Postfix expression: " + evalPattern);
        System.out.println("Postfix value: " + evaluate(evalPattern));
    }
}
