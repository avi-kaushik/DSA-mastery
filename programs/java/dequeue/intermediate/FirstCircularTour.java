package programs.java.dequeue.intermediate;

/**
 * Finds the starting petrol pump from which a vehicle can complete a
 * full circular tour of all pumps without running out of petrol.
 *
 * At each pump, the vehicle gains "petrol[i]" fuel and needs
 * "distance[i]" fuel to reach the next pump. If total petrol
 * available across all pumps is >= total distance to cover, a
 * valid starting point always exists.
 * 
 * Greedy Approach:
 *
 * 1. Traverse every petrol pump exactly once.
 *
 * 2. At every station, calculate the net fuel balance:
 *
 * balance = petrol[i] - distance[i]
 *
 * 3. Maintain the remaining fuel while travelling from the current
 * candidate starting station.
 *
 * 4. If the remaining fuel becomes negative, it means the current
 * starting station cannot complete the tour.
 *
 * Moreover, none of the stations between the current starting
 * station and the current index can be a valid answer, since
 * they would have even less fuel available before reaching
 * this point.
 *
 * Therefore, safely discard all of them and make the next
 * station the new candidate.
 *
 * 5. Also maintain the overall fuel balance of the entire circular
 * route.
 *
 * If the total balance is negative after visiting every station,
 * completing the tour is impossible because the overall petrol
 * available is less than the total distance required.
 *
 * Time Complexity : O(n)
 * Space Complexity: O(1)
 */
public class FirstCircularTour {

    /**
     * Finds the index of the pump to start from for a valid circular tour.
     *
     * @param petrol   petrol available at each pump
     * @param distance distance to the next pump from each pump
     * @return starting pump index for a valid tour, or -1 if no tour is possible
     */
    public static int evaluate(int petrol[], int distance[]) {
        int start = 0, current = 0, total = 0;

        for (int i = 0; i < distance.length; i++) {
            int balance = petrol[i] - distance[i];

            current += balance;
            total += balance;

            // If running balance goes negative, this starting point
            // (and every pump tried since) can't reach here. Reset and
            // try the next pump as the new starting point.
            if (current < 0) {
                start = i + 1;
                current = 0;
            }
        }

        // Total tells us if a full tour is possible at all, regardless
        // of start point - if total petrol < total distance, no start
        // works.
        return total < 0 ? -1 : start;
    }

    public static void main(String[] args) {
        int petrol[] = { 4, 8, 7, 4 };
        int distance[] = { 6, 5, 3, 5 };

        System.out.println("Output: " + evaluate(petrol, distance));
    }
}
