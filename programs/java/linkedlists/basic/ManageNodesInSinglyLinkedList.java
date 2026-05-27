package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.Node;

public class ManageNodesInSinglyLinkedList {

    /*
     * Insert a new node at the beginning of the linked list
     *
     * Time Complexity:
     * O(1)
     */
    public static Node<Integer> insertAtBeginning(int n, Node<Integer> head) {

        Node<Integer> newHead = new Node<>(n);

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
    public static Node<Integer> insertAtEnd(int n, Node<Integer> head) {

        Node<Integer> node = new Node<>(n);

        // If linked list is empty, new node becomes head
        if (head == null) {
            return node;
        }

        Node<Integer> current = head;

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
    public static Node<Integer> insertAtPosition(Node<Integer> head, int num, int position) {

        // Return existing list for invalid positions
        if (position <= 0) {
            return head;
        }

        Node<Integer> newNode = new Node<>(num);

        // Insert node at beginning
        if (position == 1) {
            newNode.next = head;
            return newNode;
        }

        // Position is invalid if list is empty and position > 1
        if (head == null) {
            return head;
        }

        Node<Integer> current = head;

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
    public static Node<Integer> deleteHead(Node<Integer> head) {

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
    public static Node<Integer> deleteTail(Node<Integer> head) {

        // Return null if linked list is empty
        if (head == null)
            return null;

        // Return null if linked list contains only one node
        if (head.next == null)
            return null;

        Node<Integer> current = head;

        // Traverse till second last node
        while (current.next.next != null) {
            current = current.next;
        }

        // Remove last node reference
        current.next = null;

        return head;
    }

    /*
     * Search data in linked list using iterative approach
     *
     * Returns:
     * Position of node if data is found
     * -1 if data is not found
     *
     * Position indexing starts from 1
     *
     * Time Complexity:
     * O(n)
     */
    public static int searchDataIterative(Node<Integer> head, int data) {

        Node<Integer> current = head;

        // Track current node position
        int position = 1;

        // Traverse linked list node by node
        while (current != null) {

            // Return position if data is found
            if (current.data == data)
                return position;

            // Move to next position
            position++;

            // Move to next node
            current = current.next;
        }

        // Return -1 if data is not present
        return -1;
    }

    /*
     * Search data in linked list using recursive approach
     *
     * Returns:
     * Position of node if data is found
     * -1 if data is not found
     *
     * Position indexing starts from 1
     *
     * Time Complexity:
     * O(n)
     *
     * Auxiliary Space:
     * O(n) due to recursion stack
     */
    public static int searchDataRecursive(Node<Integer> head, int data) {

        // Return -1 if data is not found
        if (head == null)
            return -1;

        // Return 1 if current node contains target data
        if (head.data == data)
            return 1;

        // Search data in remaining linked list
        int res = searchDataRecursive(head.next, data);

        // Return -1 if data is not found in remaining nodes
        if (res == -1)
            return -1;

        // Add current node position while recursion backtracks
        return res + 1;
    }

    public static void main(String[] args) {

        System.out.println("Initializing linked list...");

        // Initialize empty linked list
        Node<Integer> head = null;

        // Insert nodes at end
        head = insertAtEnd(10, head);
        head = insertAtEnd(30, head);
        head = insertAtEnd(15, head);
        head = insertAtEnd(25, head);

        // Print linked list
        TraverseSimpleLinkedList.print(head);

        System.out.println("\nInserting 21 at position 3...");

        // Insert node at specific position
        head = insertAtPosition(head, 21, 3);

        // Print linked list
        TraverseSimpleLinkedList.print(head);

        System.out.println("\nDeleting first node...");

        // Delete first node
        head = deleteHead(head);

        // Print linked list
        TraverseSimpleLinkedList.print(head);

        System.out.println("\nDeleting last node...");

        // Delete last node
        head = deleteTail(head);

        // Print linked list
        TraverseSimpleLinkedList.print(head);

        System.out.println("\nFinal Linked List:");

        // Print final linked list
        TraverseSimpleLinkedList.print(head);

        System.out.println("\nSearching 15 using iterative approach...");

        // Search node using iterative approach
        System.out.println(searchDataIterative(head, 15));

        System.out.println("\nSearching 21 using recursive approach...");

        // Search node using recursive approach
        System.out.println(searchDataRecursive(head, 21));
    }
}
