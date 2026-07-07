package programs.java.queue.common;

import programs.java.linkedlists.common.Node;

// Queue implementation using a Singly Linked List.
public class IntQueueList {

    /**
     * Points to the first element of the queue.
     * All dequeue operations happen from here.
     */
    private Node<Integer> front;

    /**
     * Points to the last element of the queue.
     * All enqueue operations happen here.
     */
    private Node<Integer> rear;

    /**
     * Maintains the total number of elements
     * currently present in the queue.
     */
    private int size = 0;

    /**
     * Inserts an element at the rear of the queue.
     *
     * Time Complexity : O(1)
     * Space Complexity: O(1)
     */
    public void enqueue(int number) {

        // Create a new node.
        Node<Integer> node = new Node<>(number);

        // If the queue is empty,
        // both front and rear should point
        // to the newly created node.
        if (isEmpty()) {
            front = rear = node;
        } else {

            // Attach the new node after the current rear.
            rear.next = node;

            // Move rear to the newly inserted node.
            rear = node;
        }

        // Increase the number of elements.
        size++;
    }

    /**
     * Removes and returns the front element.
     *
     * Time Complexity : O(1)
     * Space Complexity: O(1)
     *
     * @return removed element
     */
    public int dequeue() {

        // Queue underflow.
        if (isEmpty())
            throw new RuntimeException("Queue is empty.");

        // Store the value before removing it.
        int value = front.data;

        // Move front to the next node.
        front = front.next;

        // One element has been removed.
        size--;

        // If queue becomes empty after deletion,
        // rear should also become null.
        if (front == null) {
            rear = null;
        }

        return value;
    }

    /**
     * Returns true if queue contains no elements.
     *
     * Time Complexity : O(1)
     */
    public boolean isEmpty() {
        return size == 0;
    }
}
