package programs.java.stack.common;

import java.util.EmptyStackException;

import programs.java.linkedlists.common.DoublyNode;

/**
 * Stack implementation using a Doubly Linked List.
 *
 * Top of the stack is maintained using the tail pointer.
 *
 * Time Complexity:
 * Push - O(1)
 * Pop - O(1)
 * Peek - O(1)
 * Size - O(1)
 */
public class IntStackList {

    /**
     * Points to the top element of the stack.
     */
    private DoublyNode<Integer> tail;

    /**
     * Maintains the current number of elements in the stack.
     */
    private int size;

    public IntStackList() {
        size = 0;
    }

    /**
     * Checks whether the stack contains any elements.
     *
     * Time Complexity: O(1)
     */
    public boolean isEmpty() {
        return tail == null;
    }

    /**
     * Pushes a new element onto the top of the stack.
     *
     * Creates a new node and links it after the current tail.
     * The new node becomes the new top of the stack.
     *
     * Time Complexity: O(1)
     */
    public int push(int data) {
        DoublyNode<Integer> node = new DoublyNode<>(data);

        if (tail != null) {
            node.prev = tail;
            tail.next = node;
        }

        tail = node;

        size++;

        return tail.data;
    }

    /**
     * Removes and returns the top element from the stack.
     *
     * The tail is moved to the previous node and the removed
     * node is disconnected from the list.
     *
     * Throws EmptyStackException if the stack is empty.
     *
     * Time Complexity: O(1)
     */
    public int pop() {
        if (tail == null)
            throw new EmptyStackException();

        DoublyNode<Integer> toRemove = tail;

        tail = tail.prev;

        if (tail != null)
            tail.next = null;

        toRemove.prev = null;

        size--;

        return toRemove.data;
    }

    /**
     * Returns the top element without removing it.
     *
     * Throws EmptyStackException if the stack is empty.
     *
     * Time Complexity: O(1)
     */
    public int peek() {
        if (tail == null)
            throw new EmptyStackException();

        return tail.data;
    }

    /**
     * Returns the number of elements currently present
     * in the stack.
     *
     * Time Complexity: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * Returns a string representation of the stack
     * from top to bottom.
     *
     * Time Complexity: O(n)
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        DoublyNode<Integer> curr = tail;

        while (curr != null) {
            sb.append(curr.data).append(" ");
            curr = curr.prev;
        }

        return sb.toString();
    }
}
