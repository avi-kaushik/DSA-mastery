package programs.java.stack.basic;

import java.util.ArrayDeque;

public class BalancedParenthesis {

    /**
     * Checks whether the given brackets form a valid pair.
     *
     * @param openingBracket Opening bracket.
     * @param closingBracket Closing bracket.
     * @return True if brackets match, otherwise false.
     */
    private static boolean isMatchingPair(char openingBracket, char closingBracket) {
        return (openingBracket == '{' && closingBracket == '}')
                || (openingBracket == '(' && closingBracket == ')')
                || (openingBracket == '[' && closingBracket == ']');
    }

    /**
     * Checks whether the given parenthesis pattern is balanced.
     *
     * @param pattern Parenthesis pattern to validate.
     * @return True if pattern is balanced, otherwise false.
     */
    public static boolean isValid(String pattern) {

        // Stack used to store opening brackets.
        ArrayDeque<Character> stack = new ArrayDeque<>();

        // Process each character in the pattern.
        for (char s : pattern.toCharArray()) {

            // Store opening brackets in stack.
            if (s == '{' || s == '(' || s == '[') {
                stack.push(s);
            } else {

                // Pattern is invalid if no opening bracket exists.
                if (stack.isEmpty())
                    return false;

                // Retrieve current opening bracket for validation.
                char peek = stack.peek();

                // Remove opening bracket if matching closing bracket is found.
                if (isMatchingPair(peek, s)) {
                    stack.pop();
                } else {

                    // Pattern is invalid if brackets do not match.
                    return false;
                }
            }
        }

        // Pattern is balanced only if all opening brackets are matched.
        return stack.isEmpty();
    }

    public static void main(String[] args) {

        System.out.println("() -> " + isValid("()"));
        System.out.println("[] -> " + isValid("[]"));
        System.out.println("{} -> " + isValid("{}"));

        System.out.println("()[]{} -> " + isValid("()[]{}"));
        System.out.println("{[()]} -> " + isValid("{[()]}"));

        System.out.println("(] -> " + isValid("(]"));
        System.out.println("([)] -> " + isValid("([)]"));

        System.out.println("(( -> " + isValid("(("));
        System.out.println("{{{ -> " + isValid("{{{"));

        System.out.println(")) -> " + isValid("))"));
        System.out.println("}}} -> " + isValid("}}}"));

        System.out.println(" -> " + isValid(""));
    }
}
