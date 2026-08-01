package programs.java.techniques.recursion.intermediate;

public class JosephusProblem {

    /*
     * Problem:
     * There are 'n' people standing in a circle.
     * Starting from the first person, every kth person is eliminated.
     * The process continues until only one person remains.
     *
     * Return the position (0-based index) of the last surviving person.
     *
     * Recursive Idea:
     * 1. If there is only one person, that person survives.
     * 2. Solve the problem for (n - 1) people.
     * 3. When adding the nth person back into the circle,
     * adjust the survivor's position by shifting it by 'k' positions.
     *
     * Time Complexity:
     * O(n)
     *
     * Space Complexity:
     * O(n)
     * (Due to recursion call stack)
     *
     * @param n Total number of people in the circle.
     * 
     * @param k Every kth person is eliminated.
     * 
     * @return Position (0-based index) of the surviving person.
     */
    public static int getRemainingValue(int n, int k) {

        // Base Case:
        // If only one person is present, that person survives.
        if (n == 1)
            return 0;

        // Recursively find the survivor for (n - 1) people
        // and adjust the position for the current circle.
        return (getRemainingValue(n - 1, k) + k) % n;
    }

    public static void main(String[] args) {

        int n = 5;
        int k = 3;

        int survivor = getRemainingValue(n, k);

        System.out.println("Survivor Position (0-based): " + survivor);
        System.out.println("Survivor Position (1-based): " + (survivor + 1));
    }
}
