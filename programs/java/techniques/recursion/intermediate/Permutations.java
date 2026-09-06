package programs.java.techniques.recursion.intermediate;

public class Permutations {

    /**
     * Swap two characters in the given character array.
     *
     * @param pattern Character array in which characters need to be swapped.
     * @param a       Index of the first character.
     * @param b       Index of the second character.
     */
    private static void swapCharacters(char[] pattern, int a, int b) {
        char temp = pattern[a];

        pattern[a] = pattern[b];
        pattern[b] = temp;
    }

    /**
     * Generate and print all possible permutations of a given character array.
     *
     * Approach:
     * 1. Fix one character at the current index.
     * 2. Swap the current character with every possible character from the
     * current index to the end of the array.
     * 3. Recursively generate permutations for the remaining characters.
     * 4. After the recursive call, swap the characters back (Backtracking)
     * so the original order is restored before trying the next possibility.
     *
     * Time Complexity:
     * O(n × n!)
     * There are n! permutations, and printing each permutation takes O(n).
     *
     * Auxiliary Space Complexity:
     * O(n)
     * Due to the recursion call stack.
     *
     * @param pattern Character array whose permutations are to be generated.
     * @param i       Current index whose character is being fixed.
     */
    public static void permutate(char[] pattern, int i) {
        if (i == pattern.length - 1) {
            System.out.println(pattern);
            return;
        }

        for (int j = i; j < pattern.length; j++) {
            swapCharacters(pattern, j, i);
            permutate(pattern, i + 1);
            swapCharacters(pattern, j, i);
        }
    }

    /**
     * Generate and print all permutations of the given String.
     *
     * Converts the String into a character array and starts the recursive
     * permutation generation from index 0.
     *
     * @param pattern String whose permutations are to be generated.
     */
    public static void permutate(String pattern) {
        permutate(pattern.toCharArray(), 0);
    }

    public static void main(String[] args) {
        permutate("ABC");
    }
}
