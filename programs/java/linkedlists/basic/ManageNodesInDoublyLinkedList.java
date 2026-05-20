package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.DoublyNode;

public class ManageNodesInDoublyLinkedList {
    /**
     * Insert new node at beginning of doubly linked list
     *
     * Steps:
     * 1. Create new node
     * 2. Point new node to current head
     * 3. Point current head back to new node
     * 4. Return new node as updated head
     *
     * Time Complexity:
     * O(1)
     *
     * @param n    value to insert
     * @param head current head of linked list
     * @return updated head node
     */
    public static DoublyNode<Integer> insertAtBeginning(int n, DoublyNode<Integer> head) {

        // Create new node
        DoublyNode<Integer> newHead = new DoublyNode<>(n);

        // Connect new node to current head
        newHead.next = head;

        // If linked list already exists, connect current head back to new node
        if (head != null) {
            head.prev = newHead;
        }

        // Return updated head
        return newHead;
    }

    /**
     * Insert new node at end of doubly linked list
     *
     * Steps:
     * 1. Create new node
     * 2. If linked list is empty, return new node as head
     * 3. Traverse to last node
     * 4. Connect last node to new node
     * 5. Connect new node back to last node
     * 6. Return original head
     *
     * Time Complexity:
     * O(n)
     *
     * @param n    value to insert
     * @param head current head of linked list
     * @return head of updated linked list
     */
    public static DoublyNode<Integer> insertAtEnd(int n, DoublyNode<Integer> head) {

        // Create new node
        DoublyNode<Integer> node = new DoublyNode<>(n);

        // If linked list is empty, new node becomes head
        if (head == null) {
            return node;
        }

        // Start traversal from head
        DoublyNode<Integer> current = head;

        // Traverse until last node
        while (current.next != null) {
            current = current.next;
        }

        // Connect last node to new node
        current.next = node;

        // Connect new node back to last node
        node.prev = current;

        // Return original head
        return head;
    }

    /**
     * Reverse doubly linked list
     *
     * Steps:
     * 1. Traverse linked list
     * 2. Swap current node's prev and next references
     * 3. Store latest processed node
     * 4. Move to next node using swapped prev reference
     * 5. Return latest processed node as new head
     *
     * Time Complexity:
     * O(n)
     *
     * Space Complexity:
     * O(1)
     *
     * @param head current head of linked list
     * @return new head of reversed linked list
     */
    public static DoublyNode<Integer> reverse(DoublyNode<Integer> head) {

        // If linked list is empty, return same head
        if (head == null)
            return head;

        // Start traversal from head
        DoublyNode<Integer> current = head;

        // Stores latest processed node
        DoublyNode<Integer> latestHead = null;

        // Traverse linked list
        while (current != null) {

            // Store current prev reference temporarily
            DoublyNode<Integer> temp = current.prev;

            // Swap prev and next references
            current.prev = current.next;
            current.next = temp;

            // Update latest processed node
            latestHead = current;

            // Move to next node using swapped prev reference
            current = current.prev;
        }

        // Return new head
        return latestHead;
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

        DoublyNode<Integer> head = insertAtBeginning(47, node1);
        head = insertAtEnd(64, head);

        head = reverse(head);

        TraverseDoublyLinkedList.print(head);
    }
}
