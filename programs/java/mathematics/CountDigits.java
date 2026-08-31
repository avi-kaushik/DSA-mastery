package programs.java.mathematics;

// Counts the number of digits in an integer.
public class CountDigits {

    /**
     * Counts the number of digits in the given integer.
     *
     * Logic:
     * - Dividing an integer by 10 removes its last digit.
     * - Repeatedly divide the number by 10 until it becomes 0.
     * - The number of divisions performed is the number of digits.
     *
     * Example:
     * 1587 → 158 → 15 → 1 → 0
     * 1 2 3 4 divisions
     *
     * Edge cases:
     * - 0 has 1 digit, so it is handled separately.
     * - For negative numbers, the negative sign is ignored.
     *
     * Time Complexity: O(log₁₀(n))
     * Space Complexity: O(1)
     *
     * @param x the integer whose digits need to be counted
     * @return the number of digits in x
     */
    public static int count(int x) {
        if (x == 0) {
            return 1;
        }

        x = Math.abs(x);

        int digits = 0;

        while (x > 0) {
            x = x / 10;
            digits++;
        }

        return digits;
    }

    public static void main(String[] args) {
        int number = 1587;

        System.out.println("Total digits in " + number + " are " + count(number));
    }
}
