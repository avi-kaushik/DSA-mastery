package programs.java.queue.basic;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Generates and prints the first n numbers whose decimal representation
 * contains only the digits 5 and 6, in ascending numeric order.
 *
 * The sequence goes: 5, 6, 55, 56, 65, 66, 555, 556, ... Each number is formed
 * by appending a 5 or a 6 to a previously generated number, which is a natural
 * fit for a breadth-first (queue-based) expansion.
 */
public class GenerateNumberWithDigits {

    /**
     * Prints the first n numbers built only from the digits 5 and 6, separated
     * by commas, in ascending order.
     *
     * The method seeds a queue with the two single-digit values "5" and "6".
     * On each iteration it dequeues the smallest unprinted value, prints it, and
     * enqueues its two "children" formed by appending "5" and "6". Because
     * shorter numbers are always generated before longer ones, and the 5-child
     * is always enqueued before the 6-child, the dequeue order is exactly
     * ascending numeric order.
     *
     * Numbers are handled as Strings to sidestep integer overflow, since the
     * values grow quickly and can exceed the range of int/long for larger n.
     *
     * Time complexity: O(n). Space complexity: O(n) for the queue.
     *
     * @param n how many numbers to print; if n is 0 or negative, nothing prints
     */
    public static void generate(int n) {
        Queue<String> queue = new ArrayDeque<>();

        // Seed with the two base cases — every later number descends from these.
        queue.offer("5");
        queue.offer("6");

        for (int i = 0; i < n; i++) {
            // Dequeue the current smallest unprinted value.
            String current = queue.poll();

            System.out.print(current);

            // Print a separator after every element except the last.
            if (i != n - 1)
                System.out.print(", ");

            // Enqueue the two children. The "5" child is added before the "6"
            // child so that smaller values are always dequeued first.
            queue.offer(current + "5");
            queue.offer(current + "6");
        }
    }

    public static void main(String[] args) {
        generate(10);
    }
}
