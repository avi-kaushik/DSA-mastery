package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.Node;

/**
 * Traverse Circular Linked List
 *
 * Circular Linked List:
 * ---------------------
 * In a circular linked list, the last node points back to the head node
 * instead of NULL.
 *
 * Traversal Logic:
 * ----------------
 *
 * 1. Print the head node separately
 * 2. Move to next node
 * 3. Continue traversal until we again reach the head node
 *
 * Time Complexity:
 * ----------------
 * O(n)
 *
 * Space Complexity:
 * -----------------
 * O(1)
 */
public class TraverseCircularLinkedList {

    public static void print(Node<Integer> head) {

        // If linked list is empty
        if (head == null)
            return;

        // Start traversal from head
        Node<Integer> current = head;

        // Print head node separately
        System.out.print(current.data + " ");

        // Move to next node
        current = current.next;

        /**
         * Continue traversal until we again reach the head node.
         *
         * This condition prevents infinite looping.
         */
        while (current != head) {

            System.out.print(current.data + " ");

            current = current.next;
        }

        System.out.println();
    }

    public static void main(String[] args) {

        Node<Integer> head = new Node<>(4);
        Node<Integer> node2 = new Node<>(8);
        Node<Integer> node3 = new Node<>(4);
        Node<Integer> node4 = new Node<>(5);

        // Creating circular links
        head.next = node2;
        node2.next = node3;
        node3.next = node4;

        // Last node points back to head
        node4.next = head;

        print(head);
    }
}
