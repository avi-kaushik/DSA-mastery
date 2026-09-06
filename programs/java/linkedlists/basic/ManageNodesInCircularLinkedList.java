package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.Node;

/**
 * Insert Node at Beginning in Circular Linked List
 *
 * Important Property:
 * -------------------
 * In a circular linked list:
 *
 * tail.next = head
 *
 * This means if we already have the tail node, we can directly access the head
 * node.
 *
 * Approach:
 * ---------
 * 1. Create a new node
 * 2. Make new node point to current head
 * 3. Make tail point to new node
 * 4. Return new node as updated head
 *
 * Time Complexity:
 * ----------------
 * O(1)
 *
 * Space Complexity:
 * -----------------
 * O(1)
 */
public class ManageNodesInCircularLinkedList {

    public static Node<Integer> insertAtBeginning(Node<Integer> head, int n) {
        Node<Integer> newNode = new Node<>(n);

        if (head == null) {
            newNode.next = newNode;
            return newNode;
        }

        newNode.next = head.next;
        head.next = newNode;

        int temp = head.data;

        head.data = newNode.data;
        newNode.data = temp;

        return head;
    }

    public static Node<Integer> insertAtBeginningUsingTail(Node<Integer> tail, int n) {

        // Create new node
        Node<Integer> newNode = new Node<>(n);

        // If circular linked list is empty, make node point to itself.
        if (tail == null) {
            newNode.next = newNode;

        } else {
            // Make new node point to current head.
            newNode.next = tail.next;

            // Now new node becomes the new head.
            tail.next = newNode;
        }

        // Return updated head node
        return newNode;
    }

    /**
     * Insert node at end using only head pointer.
     */
    public static Node<Integer> insertAtEnd(Node<Integer> head, int n) {

        // Create new node
        Node<Integer> newNode = new Node<Integer>(n);

        // Handle empty circular linked list
        if (head == null) {

            newNode.next = newNode;

        } else {

            // Insert new node after head
            newNode.next = head.next;
            head.next = newNode;

            // Swap data between head and new node
            int temp = newNode.data;
            newNode.data = head.data;
            head.data = temp;
        }

        // Return updated tail node
        return newNode;
    }

    /**
     * Delete head node from circular linked list.
     */
    public static Node<Integer> deleteHead(Node<Integer> head) {

        // Return if circular linked list is empty
        if (head == null)
            return head;

        // Return null if only one node exists
        if (head.next == head)
            return null;

        // Copy next node data into head
        head.data = head.next.data;

        // Remove next node
        head.next = head.next.next;

        // Return updated head
        return head;
    }

    /**
     * Delete node at given position in circular linked list.
     *
     * Note:
     * -----
     * This implementation does not validate position bounds.
     *
     * If position is greater than linked list length,
     * it may throw NullPointerException.
     *
     * Time Complexity:
     * ----------------
     * O(n)
     *
     * Space Complexity:
     * -----------------
     * O(1)
     */
    public static Node<Integer> deleteNode(Node<Integer> head, int position) {

        // Return if circular linked list is empty
        if (head == null)
            return head;

        // Delete head node
        if (position == 1)
            return deleteHead(head);

        // Start traversal from head
        Node<Integer> current = head;

        // Traverse to previous node
        for (int i = 1; i < position - 1; i++) {
            current = current.next;
        }

        // Remove target node
        current.next = current.next.next;

        // Return updated head
        return head;
    }

    public static void main(String[] args) {

        Node<Integer> head = new Node<>(4);
        Node<Integer> node2 = new Node<>(8);
        Node<Integer> node3 = new Node<>(4);
        Node<Integer> tail = new Node<>(5);

        // Creating circular links
        head.next = node2;
        node2.next = node3;
        node3.next = tail;

        // Last node points back to head
        tail.next = head;

        System.out.println("Initial Circular Linked List:");
        TraverseCircularLinkedList.print(head);

        System.out.println();

        // Insert node at beginning
        System.out.println("Insert 12 at beginning");
        head = insertAtBeginning(head, 12);

        System.out.println("After insertion at beginning:");
        TraverseCircularLinkedList.print(head);

        System.out.println();

        // Insert node at end
        System.out.println("Insert 9 at end");
        head = insertAtEnd(head, 9);

        System.out.println("After insertion at end:");
        TraverseCircularLinkedList.print(head);

        System.out.println();

        // Delete head node
        System.out.println("Delete head node");
        head = deleteHead(head);

        System.out.println("After deleting head:");
        TraverseCircularLinkedList.print(head);

        System.out.println();

        // Delete node at position 3
        System.out.println("Delete node at position 3");
        head = deleteNode(head, 3);

        System.out.println("After deleting node at position 3:");
        TraverseCircularLinkedList.print(head);
    }
}
