package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.DoublyNode;

/**
 * Demonstrates traversal of a Doubly Linked List.
 *
 * Doubly Linked List Structure:
 * 
 * Each node contains:
 * - data : actual value
 * - next : reference to next node
 * - prev : reference to previous node
 *
 * In this example:
 *
 * node1 <-> node2 <-> node3
 */
public class TraverseDoublyLinkedList {

    /**
     * Traverses and prints the doubly linked list
     * from head to tail.
     *
     * @param head starting node of linked list
     */
    public static void print(DoublyNode<Integer> head) {

        // Start traversal from head node
        DoublyNode<Integer> current = head;

        /**
         * Continue traversal until current becomes null.
         *
         * current = null means:
         * we reached end of linked list.
         */
        while (current != null) {

            // Print current node's data
            System.out.print(current.data + (current.next != null ? ", " : ""));
            // Move to next node.
            current = current.next;
        }

        // Move cursor to next line after printing list
        System.out.println();
    }

    public static void main(String[] args) {

        // Create nodes
        DoublyNode<Integer> node1 = new DoublyNode<>(21);
        DoublyNode<Integer> node2 = new DoublyNode<>(15);
        DoublyNode<Integer> node3 = new DoublyNode<>(47);

        // Connect node1 and node2
        node1.next = node2;

        node2.prev = node1;
        node2.next = node3;

        // Connect node2 and node3
        node3.prev = node2;

        // Print linked list
        print(node1);
    }
}
