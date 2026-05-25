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

        // Insert node at beginning
        head = insertAtBeginning(head, 12);

        TraverseCircularLinkedList.print(head);
    }
}
