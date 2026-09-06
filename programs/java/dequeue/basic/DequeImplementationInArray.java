package programs.java.dequeue.basic;

/**
 * This class shows how to build a Deque (Double Ended Queue) using a
 * simple array. A Deque lets us add/remove elements from BOTH ends
 * (front and rear) - unlike a normal Queue where we can only add at
 * rear and remove from front.
 *
 * We use a "circular array" trick here. That means when front or rear
 * reaches the end of the array, it wraps back around to index 0
 * instead of stopping. This helps us reuse empty slots left after
 * deletions, so we don't waste space.
 */
public class DequeImplementationInArray {

    public static class Deque {

        // Fixed-size array to store our deque elements.
        private int[] items;

        // Tracks how many elements are currently in the deque.
        private int size = 0;

        // Index of the current front element in the array.
        private int front = 0;

        /**
         * Creates an empty Deque with a fixed capacity.
         *
         * @param capacity max number of elements this deque can hold
         */
        public Deque(int capacity) {
            items = new int[capacity];
        }

        /**
         * Checks if the deque has no elements.
         *
         * @return true if deque is empty, false otherwise
         */
        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * Checks if the deque has reached its max capacity.
         *
         * @return true if deque is full, false otherwise
         */
        public boolean isFull() {
            return size == items.length;
        }

        /**
         * Adds a new element at the FRONT of the deque.
         *
         * Logic: we move "front" one step backward (circularly) and
         * place the new value there. This is why front moves BEFORE
         * we insert - we're making room for the new front element.
         *
         * @param data value to insert at the front
         * @throws IllegalStateException if deque is already full
         */
        public void insertFront(int data) {
            if (isFull())
                throw new IllegalStateException("Deque is full.");

            // Move front one step left. Adding "items.length" before %
            // avoids getting a negative index when front is 0.
            // Example: if front = 0, length = 5 -> (0 - 1 + 5) % 5 = 4
            front = (front - 1 + items.length) % items.length;

            items[front] = data;

            size++;
        }

        /**
         * Adds a new element at the REAR of the deque.
         *
         * Logic: rear index is always "front + size" (wrapped using %).
         * So we place data at that empty slot, no need to move front.
         *
         * @param data value to insert at the rear
         * @throws IllegalStateException if deque is already full
         */
        public void insertRear(int data) {
            if (isFull())
                throw new IllegalStateException("Deque is full.");

            // Find the next empty slot right after the current last element.
            int idx = (front + size) % items.length;

            items[idx] = data;

            size++;
        }

        /**
         * Removes the element from the FRONT of the deque.
         *
         * Logic: we don't actually erase the value from the array,
         * we just move "front" one step ahead. The old value is
         * considered "deleted" since size no longer counts it, and
         * it will get overwritten whenever a new element is inserted
         * into that same slot later.
         *
         * @throws IllegalStateException if deque is empty
         */
        public void deleteFront() {
            if (isEmpty())
                throw new IllegalStateException("Deque is empty.");

            // Shift front pointer ahead by one (wrap around if needed).
            front = (front + 1) % items.length;
            size--;
        }

        /**
         * Removes the element from the REAR of the deque.
         *
         * Logic: same idea as deleteFront - since rear is calculated
         * using "front + size - 1", simply reducing size by 1 is
         * enough to "drop" the last element. No index math needed here.
         *
         * @throws IllegalStateException if deque is empty
         */
        public void deleteRear() {
            if (isEmpty())
                throw new IllegalStateException("Deque is empty.");

            size--;
        }

        /**
         * Returns the value at the front of the deque without removing it.
         *
         * @return value at the front
         * @throws IllegalStateException if deque is empty
         */
        public int getFront() {
            if (isEmpty())
                throw new IllegalStateException("Deque is empty.");

            return items[front];
        }

        /**
         * Returns the value at the rear of the deque without removing it.
         *
         * @return value at the rear
         * @throws IllegalStateException if deque is empty
         */
        public int getRear() {
            if (isEmpty())
                throw new IllegalStateException("Deque is empty.");

            // rear = front + (size - 1), because rear is always
            // "size - 1" steps ahead of front (0-indexed).
            int rear = (front + size - 1) % items.length;
            return items[rear];
        }
    }

    public static void main(String[] args) {

        // Creating a deque that can hold max 5 elements.
        Deque deque = new Deque(5);

        // Deque so far (front -> rear): 10, 20
        deque.insertRear(10);
        deque.insertRear(20);

        // Deque so far (front -> rear): 5, 10, 20
        deque.insertFront(5);

        // Deque so far (front -> rear): 1, 5, 10, 20
        deque.insertFront(1);

        System.out.println("Front : " + deque.getFront());
        System.out.println("Rear  : " + deque.getRear());

        // Removes 20 from rear. Deque now: 1, 5, 10
        deque.deleteRear();

        System.out.println("\nAfter deleting rear:");
        System.out.println("Front : " + deque.getFront());
        System.out.println("Rear  : " + deque.getRear());

        // Removes 1 from front. Deque now: 5, 10
        deque.deleteFront();

        System.out.println("\nAfter deleting front:");
        System.out.println("Front : " + deque.getFront());
        System.out.println("Rear  : " + deque.getRear());
    }
}
