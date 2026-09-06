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
     * Delete head node from doubly linked list
     *
     * Steps:
     * 1. If linked list is empty, return null
     * 2. If linked list has only one node, return null
     * 3. Move head to next node
     * 4. Remove backward connection of new head
     * 5. Return updated head
     *
     * Time Complexity:
     * O(1)
     *
     * @param head current head of linked list
     * @return updated head after deletion
     */
    public static DoublyNode<Integer> deleteHead(DoublyNode<Integer> head) {

        // If linked list is empty
        if (head == null) {
            return null;
        }

        // If linked list has only one node
        if (head.next == null) {
            return null;
        }

        // Move head to next node
        head = head.next;

        // Remove backward connection
        head.prev = null;

        // Return updated head
        return head;
    }

    /**
     * Delete last node from doubly linked list
     *
     * Steps:
     * 1. If linked list is empty, return null
     * 2. If linked list has only one node, return null
     * 3. Traverse to second last node
     * 4. Store last node reference
     * 5. Remove forward connection from second last node
     * 6. Remove backward connection from last node
     * 7. Return original head
     *
     * Time Complexity:
     * O(n)
     *
     * Space Complexity:
     * O(1)
     *
     * @param head current head of linked list
     * @return head after deleting last node
     */
    public static DoublyNode<Integer> deleteLastNode(
            DoublyNode<Integer> head) {

        // If linked list is empty or has only one node
        if (head == null || head.next == null)
            return null;

        // Start traversal from head
        DoublyNode<Integer> current = head;

        // Traverse until second last node
        while (current.next.next != null) {
            current = current.next;
        }

        // Store last node reference
        DoublyNode<Integer> last = current.next;

        // Remove forward connection
        current.next = null;

        // Remove backward connection
        last.prev = null;

        // Return original head
        return head;
    }

    /**
     * Reverse doubly linked list
     *
     * Steps:
     * 1. Traverse linked list
     * 2. Store current prev reference temporarily
     * 3. Swap current node's prev and next references
     * 4. Move to next node using swapped prev reference
     * 5. Return new head using temp.prev
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

        // If linked list is empty or has only one node, return same head
        if (head == null || head.next == null)
            return head;

        // Start traversal from head
        DoublyNode<Integer> current = head;

        // Temporary reference used during swapping
        DoublyNode<Integer> temp = null;

        // Traverse linked list
        while (current != null) {

            // Store current prev reference temporarily
            temp = current.prev;

            // Swap prev and next references
            current.prev = current.next;
            current.next = temp;

            // Move to next node using swapped prev reference
            current = current.prev;
        }

        // Return new head
        return temp.prev;
    }

    public static void main(String[] args) {

        System.out.println("=== Create Nodes ===");

        // Create nodes
        DoublyNode<Integer> node1 = new DoublyNode<>(21);
        DoublyNode<Integer> node2 = new DoublyNode<>(15);
        DoublyNode<Integer> node3 = new DoublyNode<>(47);

        System.out.println("node1 = " + node1.data);
        System.out.println("node2 = " + node2.data);
        System.out.println("node3 = " + node3.data);

        System.out.println("\n=== Connect Nodes ===");

        // Connect node1 and node2
        node1.next = node2;

        node2.prev = node1;
        node2.next = node3;

        // Connect node2 and node3
        node3.prev = node2;

        System.out.println("Connected:");
        System.out.println(node1.data + " <-> " + node2.data);
        System.out.println(node2.data + " <-> " + node3.data);

        System.out.println("\nCurrent List:");
        TraverseDoublyLinkedList.print(node1);

        System.out.println("\n=== Insert At Beginning ===");

        DoublyNode<Integer> head = insertAtBeginning(47, node1);

        System.out.println("Inserted 47");

        System.out.println("Current List:");
        TraverseDoublyLinkedList.print(head);

        System.out.println("\n=== Insert At End ===");

        head = insertAtEnd(64, head);

        System.out.println("Inserted 64");

        System.out.println("Current List:");
        TraverseDoublyLinkedList.print(head);

        System.out.println("\n=== Reverse List ===");

        head = reverse(head);

        System.out.println("Reversed List:");

        TraverseDoublyLinkedList.print(head);

        System.out.println("\n=== Delete Head ===");

        head = deleteHead(head);

        System.out.println("Deleted Head");

        System.out.println("Current List:");
        TraverseDoublyLinkedList.print(head);

        System.out.println("\n=== Delete Last Node ===");

        head = deleteLastNode(head);

        System.out.println("Deleted Last Node");

        System.out.println("Final List:");
        TraverseDoublyLinkedList.print(head);

        System.out.println("\n=== Completed ===");
    }
}
