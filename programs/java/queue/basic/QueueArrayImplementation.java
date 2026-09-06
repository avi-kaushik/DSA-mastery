package programs.java.queue.basic;

import programs.java.queue.common.IntQueue;

public class QueueArrayImplementation  {

    public static void main(String[] args) {

        IntQueue queue = new IntQueue(5);

        System.out.println("Enqueue Operations");
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        System.out.println("Front : " + queue.peek());

        System.out.println();

        System.out.println("Dequeue Operations");
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());

        System.out.println();

        System.out.println("Enqueue More Elements");
        queue.enqueue(40);
        queue.enqueue(50);
        queue.enqueue(60);

        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }
    }
}
