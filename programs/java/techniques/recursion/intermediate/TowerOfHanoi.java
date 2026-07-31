package programs.java.techniques.recursion.intermediate;

public class TowerOfHanoi {

    /**
     * Move 'n' disks from the Source rod to the Destination rod using the Helper
     * rod.
     *
     * Rules:
     * 1. Only one disk can be moved at a time.
     * 2. A larger disk cannot be placed on top of a smaller disk.
     * 3. Only the top disk of a rod can be moved.
     *
     * Recursive Idea:
     *
     * To move 'n' disks from Source -> Destination:
     *
     * Step 1:
     * Move the top (n - 1) disks from Source to Helper using Destination as the
     * temporary rod.
     *
     * Step 2:
     * Move the largest (nth) disk from Source to Destination.
     *
     * Step 3:
     * Move the (n - 1) disks from Helper to Destination using Source as the
     * temporary rod.
     *
     * Base Case:
     * If only one disk is left, move it directly from Source to Destination.
     *
     * Time Complexity:
     * T(n) = 2T(n - 1) + 1
     * => O(2^n)
     *
     * Auxiliary Space:
     * O(n)
     * (Maximum recursion depth is n.)
     *
     * @param n           Number of disks to move.
     * @param source      The rod from which the disks are moved.
     * @param helper      The temporary rod used during the movement.
     * @param destination The rod to which the disks are moved.
     */
    public static void move(int n, char source, char helper, char destination) {
        // Base Case:
        // If there is only one disk, move it directly.
        if (n == 1) {
            System.out.println("Move 1 from " + source + " to " + destination);
            return;
        }

        // Step 1:
        // Move the top (n - 1) disks from Source to Helper.
        move(n - 1, source, destination, helper);

        // Step 2:
        // Move the largest disk from Source to Destination.
        System.out.println("Move " + n + " from " + source + " to " + destination);

        // Step 3:
        // Move the (n - 1) disks from Helper to Destination.
        move(n - 1, helper, source, destination);
    }

    public static void main(String[] args) {
        move(3, 'A', 'B', 'C');
    }
}
