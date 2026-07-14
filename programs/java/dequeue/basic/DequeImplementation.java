package programs.java.dequeue.basic;

import programs.java.dequeue.common.DequeueList;

public class DequeImplementation {
    public static void main(String[] args) {
        DequeueList<Integer> deque = new DequeueList<>();

        // Insert elements at the front
        deque.insertFront(20);
        deque.insertFront(10);

        // Insert elements at the rear
        deque.insertRear(30);
        deque.insertRear(40);

        System.out.println("Front Element : " + deque.peekFirst());
        System.out.println("Rear Element  : " + deque.peekLast());
        System.out.println("Size          : " + deque.size());

        System.out.println();

        System.out.println("Removed Front : " + deque.removeFront());
        System.out.println("Removed Rear  : " + deque.removeRear());

        System.out.println();

        System.out.println("Front Element : " + deque.peekFirst());
        System.out.println("Rear Element  : " + deque.peekLast());
        System.out.println("Size          : " + deque.size());

        System.out.println();

        while (!deque.isEmpty()) {
            System.out.println("Removed : " + deque.removeFront());
        }

        System.out.println();
        System.out.println("Deque Empty : " + deque.isEmpty());
    }
}
