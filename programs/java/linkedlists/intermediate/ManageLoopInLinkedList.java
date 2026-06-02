package programs.java.linkedlists.intermediate;

import programs.java.linkedlists.common.Node;

/**
 * Utility class for managing loops in a linked list.
 *
 * Supported operations:
 * 1. Detect loop
 * 2. Find length of loop
 * 3. Find starting node of loop
 * 4. Remove loop
 *
 * Most operations in this class use Floyd's Cycle Detection
 * Algorithm (Tortoise and Hare).
 */
public class ManageLoopInLinkedList {

    /**
     * Detect whether a linked list contains a loop.
     *
     * Approach:
     * 1. Use two pointers:
     * - Slow pointer moves one node at a time.
     * - Fast pointer moves two nodes at a time.
     * 2. If a loop exists, both pointers eventually meet.
     * 3. If fast reaches the end of the list, no loop exists.
     *
     * Time Complexity : O(n)
     * Space Complexity: O(1)
     */
    public static boolean isLoop(Node<Integer> head) {
        Node<Integer> slow = head, fast = head;

        while (fast != null && fast.next != null) {

            // Move slow by one node
            slow = slow.next;

            // Move fast by two nodes
            fast = fast.next.next;

            // Loop detected
            if (slow == fast) {
                return true;
            }
        }

        // No loop found
        return false;
    }

    public static void main(String[] args) {

        // Create nodes
        Node<Integer> n1 = new Node<>(10);
        Node<Integer> n2 = new Node<>(30);
        Node<Integer> n3 = new Node<>(15);
        Node<Integer> n4 = new Node<>(25);
        Node<Integer> n5 = new Node<>(14);
        Node<Integer> n6 = new Node<>(44);
        Node<Integer> n7 = new Node<>(12);

        // Connect nodes
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;
        n5.next = n6;
        n6.next = n7;

        // Create loop
        n7.next = n3;

        Node<Integer> head = n1;

        System.out.println("Loop Exists: " + isLoop(head));
    }
}
