package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.Node;

public class TraverseSimpleLinkedList {
    /**
     * Calculates the length of a linked list.
     *
     * Logic:
     * 1. Start traversal from the head node.
     * 2. Visit each node one by one.
     * 3. Increment the length counter for every node visited.
     * 4. Return the total count of nodes.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static int length(Node<Integer> head) {

        // Start traversal from the head node
        Node<Integer> current = head;

        int length = 0;

        // Traverse the linked list until the end is reached
        while (current != null) {

            // Move to the next node
            current = current.next;

            // Increment length for every node visited
            length++;
        }

        // Return total number of nodes in the linked list
        return length;
    }

    /**
     * Traverse and print the linked list using loop
     *
     * Example:
     * 20 -> 10 -> 30
     *
     * Output:
     * 20, 10, 30
     */
    public static void print(Node<Integer> node) {

        // Start traversal from head node
        Node<Integer> current = node;

        // Traverse until current node becomes null
        while (current != null) {

            // Print current node's data
            System.out.print(current.data + (current.next != null ? ", " : ""));

            // Move to next node
            current = current.next;
        }

        System.out.println();
    }

    /**
     * Traverse and print the linked list recursively
     *
     * Example:
     * 20 -> 10 -> 30
     *
     * Output:
     * 20, 10, 30
     */
    public static void printRecursive(Node<Integer> node) {

        // Base condition:
        // If node becomes null, stop recursion
        if (node == null) {
            System.out.println();
            return;
        }

        // Print current node's data
        System.out.print(node.data + (node.next != null ? ", " : ""));

        // Recursively traverse remaining linked list
        printRecursive(node.next);
    }

    public static void main(String[] args) {

        // Create nodes
        Node<Integer> node1 = new Node<>(20);
        Node<Integer> node2 = new Node<>(10);
        Node<Integer> node3 = new Node<>(30);

        /**
         * Connect nodes
         *
         * node1 -> node2 -> node3
         *
         * 20 -> 10 -> 30
         */
        node1.next = node2;
        node2.next = node3;

        // Iterative traversal
        print(node1);

        // Recursive traversal
        printRecursive(node1);
    }
}
