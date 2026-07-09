package programs.java.queue.basic;

import java.util.ArrayDeque;
import java.util.Queue;

/*
 * Stack (LIFO) built using just one queue (FIFO).
 *
 * A queue gives back the oldest element first, but a stack needs the newest
 * one first - so they work in opposite orders. To fix this, every time we
 * push a value we rotate the queue so the new value ends up at the FRONT.
 * That way the front of the queue is always the top of the stack, and pop /
 * top / isEmpty stay super fast (O(1)). Only push does extra work.
 */
public class ImplementStackUsingQueue {

    // Stack class with over the top of Queue data structure.
    public static class Stack {
        private Queue<Integer> queue = new ArrayDeque<>();

        // Returns the top element but doesn't remove it.
        // The newest element is kept at the front, so we just peek the front.
        // O(1)
        public int top() {
            // peek() on an empty queue returns null, and turning null
            // into an int crashes with NullPointerException. So check first.
            if (queue.isEmpty())
                throw new IllegalStateException("Stack is empty");

            return queue.peek();
        }

        // True if there's nothing in the stack.
        // O(1)
        public boolean isEmpty() {
            return queue.isEmpty();
        }

        // Removes and returns the top element.
        // O(1)
        public int pop() {
            // poll() returns null on an empty queue,
            // and unboxing null to int throws NPE. So guard against it.
            if (queue.isEmpty())
                throw new IllegalStateException("Stack is empty");

            return queue.poll();
        }

        // Adds a value on top of the stack.
        // O(n) because of the rotation.
        public void push(int value) {
            // step 1: add the new value at the back
            queue.offer(value);

            // step 2: move every OLD element from the front to the back,
            // one by one. After this the new value ends up at the front.
            //
            // note: queue.size() doesn't change during the loop, because each
            // step removes one (poll) and adds one (offer). So this runs
            // exactly (size - 1) times, which is what we want.
            for (int i = 0; i < queue.size() - 1; i++) {
                queue.offer(queue.poll());
            }
        }
    }

    public static void main(String[] args) {
        Stack stack = new Stack();

        System.out.println("isEmpty (expected true): " + stack.isEmpty());

        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println("Pushed: 10, 20, 30");

        System.out.println("top (expected 30): " + stack.top());
        System.out.println("isEmpty (expected false): " + stack.isEmpty());

        System.out.println("pop (expected 30): " + stack.pop());
        System.out.println("pop (expected 20): " + stack.pop());
        System.out.println("top (expected 10): " + stack.top());

        stack.push(40);
        System.out.println("Pushed: 40");
        System.out.println("top (expected 40): " + stack.top());

        System.out.println("pop (expected 40): " + stack.pop());
        System.out.println("pop (expected 10): " + stack.pop());
        System.out.println("isEmpty (expected true): " + stack.isEmpty());
    }
}
