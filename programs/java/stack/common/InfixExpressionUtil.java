package programs.java.stack.common;

import java.util.ArrayDeque;

public class InfixExpressionUtil {

    /**
     * Checks whether the given character is a supported arithmetic operator.
     *
     * @param character Character to be checked.
     * @return {@code true} if the character is an operator, otherwise
     *         {@code false}.
     */
    public static boolean isOperator(char character) {
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
    public static int precedence(char operator) {
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
     * Converts the currently accumulated operand into an integer and pushes it
     * onto the operand stack.
     *
     * The operand buffer is cleared after it has been pushed so that the next
     * operand can be accumulated independently.
     *
     * @param stack   Stack storing operands for postfix evaluation.
     * @param operand Buffer containing the current operand being built.
     */
    public static void pushOperand(ArrayDeque<Integer> stack, StringBuilder operand) {
        if (operand.length() > 0) {
            stack.push(Integer.parseInt(operand.toString()));
            operand.setLength(0);
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
    public static int parseExpression(int operand1, int operand2, char operator) {
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
}
