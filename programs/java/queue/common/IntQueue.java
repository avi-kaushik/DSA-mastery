package programs.java.queue.common;

public class IntQueue {

    // Array to store queue elements.
    private final int[] items;

    // Points to the front element of the queue.
    private int front = 0;

    // Keeps track of the current number of elements.
    private int size = 0;

    public IntQueue(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity must be greater than 0.");

        items = new int[capacity];
    }

    /**
     * Inserts an element at the rear of the queue.
     *
     * Time Complexity : O(1)
     */
    public void enqueue(int number) {
        if (isFull())
            throw new RuntimeException("Queue is full.");

        // Rear is calculated using circular indexing.
        int rear = (front + size) % items.length;

        items[rear] = number;
        size++;
    }

    /**
     * Removes and returns the front element.
     *
     * Time Complexity : O(1)
     */
    public int dequeue() {
        if (isEmpty())
            throw new RuntimeException("Queue is empty.");

        int value = items[front];

        // Move front circularly.
        front = (front + 1) % items.length;

        size--;

        return value;
    }

    /**
     * Returns the front element without removing it.
     *
     * Time Complexity : O(1)
     */
    public int peek() {
        if (isEmpty())
            throw new RuntimeException("Queue is empty.");

        return items[front];
    }

    /**
     * Returns true if queue contains no elements.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns true if queue is full.
     */
    public boolean isFull() {
        return size == items.length;
    }

    /**
     * Returns the current number of elements.
     */
    public int size() {
        return size;
    }
}
