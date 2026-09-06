package programs.java.queue.basic;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

/**
 * Demonstrates two approaches for reversing the order of elements in a queue.
 * 
 * A queue is a FIFO (First-In-First-Out) structure, so reversing it means the
 * element that was at the front ends up at the back, and vice versa. Both
 * methods reverse the queue in place — the same {@code Queue} object is
 * mutated and no new queue is returned.
 */
public class ReverseQueue {

    /**
     * Reverses the given queue in place using recursion.
     * 
     * The call stack itself is used as auxiliary storage. Each recursive call
     * removes the current front element and holds it in a local variable until
     * the queue is empty; as the recursion unwinds, the held elements are added
     * back in reverse order.
     * 
     * Time complexity: O(n). Space complexity: O(n) due to the recursion depth.
     *
     * @param queue the queue to reverse; modified directly (must not be null)
     */
    public static void reverseRecursive(Queue<Integer> queue) {
        // Base case: an empty queue is already "reversed" — stop recursing.
        if (queue.isEmpty())
            return;

        // Remove the current front element and keep it on this stack frame.
        int value = queue.poll();

        // Reverse the remaining (smaller) queue before re-inserting this value.
        reverseRecursive(queue);

        // Unwinding phase: re-add the held value at the back. Because the
        // deepest frame runs offer() first, the original front lands at the back.
        queue.offer(value);
    }

    /**
     * Reverses the given queue in place using an explicit auxiliary stack.
     * 
     * All elements are dequeued and pushed onto a stack; popping them off (LIFO)
     * and enqueueing them back produces the reversed order. This is the
     * iterative counterpart to {@link #reverseRecursive(Queue)} and avoids deep
     * recursion, making it safer for very large queues.
     * 
     * Time complexity: O(n). Space complexity: O(n) for the stack.
     *
     * @param queue the queue to reverse; modified directly (must not be null)
     */
    public static void reverseIterative(Queue<Integer> queue) {
        Stack<Integer> stack = new Stack<>();

        // Drain the queue into the stack. The queue's front ends up at the
        // bottom of the stack.
        while (!queue.isEmpty()) {
            stack.push(queue.poll());
        }

        // Pop back into the queue. The stack's top (the queue's old back) is
        // enqueued first, so the original order is reversed.
        while (!stack.isEmpty()) {
            queue.offer(stack.pop());
        }
    }

    public static void main(String[] args) {
        // ArrayDeque is the recommended Queue implementation (faster than
        // LinkedList and non-synchronized).
        Queue<Integer> queue = new ArrayDeque<>();

        // Enqueue elements; offer() adds to the back of the queue.
        queue.offer(10);
        queue.offer(20);
        queue.offer(30);
        queue.offer(40);
        queue.offer(50);

        System.out.println("Original Queue: " + queue);

        reverseIterative(queue);
        System.out.println("Reversed Queue (using iterative method): " + queue);

        reverseRecursive(queue);
        System.out.println("Reversed Queue (using resursive method): " + queue);
    }
}
