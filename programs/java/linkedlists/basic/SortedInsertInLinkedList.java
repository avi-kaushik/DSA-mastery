package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.Node;

public class SortedInsertInLinkedList {

    /**
     * Insert a node into a sorted singly linked list in sorted order.
     *
     * Logic:
     *
     * -> If list is empty, new node becomes head
     * -> If value is smaller than head, insert at beginning
     * -> Otherwise, find correct position using traversal
     * -> Insert node by adjusting links
     *
     * Key Idea:
     *
     * We always stop at the node BEFORE the correct position,
     * then insert the new node after it.
     *
     * Time Complexity : O(n)
     * Space Complexity : O(1)
     */
    static Node<Integer> insertSorted(Node<Integer> head, int n) {

        Node<Integer> newNode = new Node<>(n);

        // Case 1: empty list
        if (head == null)
            return newNode;

        // Case 2: insert at beginning
        if (n < head.data) {
            newNode.next = head;
            return newNode;
        }

        Node<Integer> current = head;

        // Move until we find correct insertion position
        while (current.next != null && current.next.data <= n) {

            current = current.next;
        }

        // Insert new node after current
        newNode.next = current.next;
        current.next = newNode;

        return head;
    }

    public static void main(String[] args) {

        Node<Integer> head = insertSorted(null, 12);
        head = insertSorted(head, 15);
        head = insertSorted(head, 64);
        head = insertSorted(head, 48);
        head = insertSorted(head, 19);
        head = insertSorted(head, 6);

        TraverseSimpleLinkedList.print(head);
    }
}
