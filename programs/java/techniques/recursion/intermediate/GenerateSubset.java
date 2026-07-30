package programs.java.techniques.recursion.intermediate;

public class GenerateSubset {

    public static void generate(String s) {
        generateSubsets(s, "", 0);
    }

    /**
     * Generate all possible subsets (Power Set) of a given String.
     *
     * Approach:
     * 1. For every character, recursively explore the possibility of excluding it.
     * 2. Recursively explore the possibility of including it.
     * 3. Once all characters have been processed, print the constructed subset.
     *
     * Time Complexity: O(n × 2ⁿ)
     * Space Complexity: O(n)
     *
     * @param s       Input string.
     * @param current Subset constructed so far.
     * @param i       Index of the character currently being processed.
     * 
     */
    private static void generateSubsets(String s, String current, int i) {

        // Print the constructed subset after processing all characters
        if (i == s.length()) {
            System.out.println(current);
            return;
        }

        // Exclude the current character
        generateSubsets(s, current, i + 1);

        // Include the current character
        generateSubsets(s, current + s.charAt(i), i + 1);
    }

    public static void main(String[] args) {
        generate("ABC");
    }
}
