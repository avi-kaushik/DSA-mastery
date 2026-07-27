package programs.java.techniques.recursion.basic;

public class RecursionPractice {

    /**
     * Prints numbers from 1 to n using recursion.
     *
     * @param n The upper limit of the sequence.
     */
    public static void print1ToN(int n) {
        // Base case:
        // Stop the recursion once n becomes 0.
        if (n == 0)
            return;

        // Recursively print numbers from 1 to (n - 1).
        print1ToN(n - 1);

        // Print the current number while returning
        // from the recursive calls.
        System.out.println(n);
    }

    /**
     * Prints numbers from n to 1 using recursion.
     *
     * @param n The starting number of the sequence.
     */
    public static void printNTo1(int n) {
        // Base case:
        // Stop the recursion once n becomes 0.
        if (n == 0)
            return;

        // Print the current number before making
        // the recursive call.
        System.out.println(n);

        // Recursively print the remaining numbers.
        printNTo1(n - 1);
    }

    public static void main(String[] args) {
        print1ToN(4);

        System.out.println();

        printNTo1(4);
    }
}
