package programs.java.queue.basic;

import programs.java.queue.common.IntQueueList;

public class QueueLinkedListImplementation {

    public static void main(String[] args) {

        IntQueueList queue = new IntQueueList();

        System.out.println("========== ENQUEUE ==========");
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        System.out.println();

        System.out.println("========== DEQUEUE ==========");
        System.out.println("Removed : " + queue.dequeue());
        System.out.println("Removed : " + queue.dequeue());

        System.out.println();

        System.out.println("====== ENQUEUE MORE ======");
        queue.enqueue(40);
        queue.enqueue(50);
        queue.enqueue(60);

        System.out.println();

        System.out.println("====== EMPTY THE QUEUE ======");

        while (!queue.isEmpty()) {
            System.out.println("Removed : " + queue.dequeue());
        }

        System.out.println();

        System.out.println("Queue Empty : " + queue.isEmpty());
    }
}
