package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.IntNode;

public class ManageNodesInSinglyLinkedList {

    /*
     * Insert a new node at the beginning of the linked list
     *
     * Time Complexity:
     * O(1)
     */
    public static IntNode insertAtBeginning(int n, IntNode head) {
        IntNode newHead = new IntNode(n);

        // Point new node to current head
        newHead.next = head;

        // Return new node as updated head
        return newHead;
    }

    /*
     * Insert a new node at the end of the linked list
     *
     * Time Complexity:
     * O(n)
     */
    public static IntNode insertAtEnd(int n, IntNode head) {
        IntNode node = new IntNode(n);

        // If linked list is empty, new node becomes head
        if (head == null) {
            return node;
        }

        IntNode current = head;

        // Traverse till the last node
        while (current.next != null) {
            current = current.next;
        }

        // Attach new node at the end
        current.next = node;

        return head;
    }

    /*
     * Insert a new node at a specific position
     *
     * Position indexing starts from 1
     *
     * Time Complexity:
     * O(n)
     */
    public static IntNode insertAtPosition(IntNode head, int num, int position) {

        // Return existing list for invalid positions
        if (position <= 0) {
            return head;
        }

        IntNode newNode = new IntNode(num);

        // Insert node at beginning
        if (position == 1) {
            newNode.next = head;
            return newNode;
        }

        // Position is invalid if list is empty and position > 1
        if (head == null) {
            return head;
        }

        IntNode current = head;

        // Traverse till node before target position
        for (int i = 1; i < position - 1 && current != null; i++) {
            current = current.next;
        }

        // Return existing list if position is out of bounds
        if (current == null) {
            return head;
        }

        // Connect new node to next node
        newNode.next = current.next;

        // Connect previous node to new node
        current.next = newNode;

        return head;
    }

    /*
     * Delete the head node from the linked list
     *
     * Time Complexity:
     * O(1)
     */
    public static IntNode deleteHead(IntNode head) {

        // Return null if linked list is empty
        if (head == null)
            return head;

        // Return next node as new head
        return head.next;
    }

    /*
     * Delete the last node from the linked list
     *
     * Time Complexity:
     * O(n)
     */
    public static IntNode deleteTail(IntNode head) {

        // Return null if linked list is empty
        if (head == null)
            return null;

        // Return null if linked list contains only one node
        if (head.next == null)
            return null;

        IntNode current = head;

        // Traverse till second last node
        while (current.next.next != null) {
            current = current.next;
        }

        // Remove last node reference
        current.next = null;

        return head;
    }

    public static void main(String[] args) {
        System.out.println("Initializing linked list...");

        // Initialize empty linked list
        IntNode head = null;

        // Insert nodes at end
        head = insertAtEnd(10, head);
        head = insertAtEnd(30, head);
        head = insertAtEnd(15, head);
        head = insertAtEnd(25, head);

        TraverseSimpleLinkedList.print(head);

        System.out.println("\nInserting 21 at position 3...");
        // Insert node at specific position
        head = insertAtPosition(head, 21, 3);

        TraverseSimpleLinkedList.print(head);

        System.out.println("\nDeleting first node...");
        head = deleteHead(head);

        // Print linked list
        TraverseSimpleLinkedList.print(head);

        System.out.println("\nDeleting last node...");
        head = deleteTail(head);

        TraverseSimpleLinkedList.print(head);

        System.out.println("\nFinal Linked List:");
        TraverseSimpleLinkedList.print(head);
    }
}
