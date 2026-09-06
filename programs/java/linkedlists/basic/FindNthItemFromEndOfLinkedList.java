package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.Node;

public class FindNthItemFromEndOfLinkedList {

    /**
     * Finds the nth node from the end of the linked list
     * using the two pointer approach.
     * 
     * Logic:
     * Move the first pointer 'pos' nodes ahead.
     * Then move both pointers one step at a time.
     * When the first pointer reaches the end,
     * the second pointer will be at the required node.
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static Node<Integer> findFromEnd(Node<Integer> head, int pos) {
        if (head == null)
            return head;

        Node<Integer> first = head;

        // Move first pointer 'pos' nodes ahead
        for (int i = 0; i < pos; i++) {
            if (first == null)
                return null;

            first = first.next;
        }

        Node<Integer> second = head;

        // Move both pointers until first reaches the end
        while (first != null) {
            first = first.next;
            second = second.next;
        }

        return second;
    }

    public static void main(String[] args) {
        // Initialize empty linked list
        Node<Integer> head = null;

        // Insert nodes at end
        head = ManageNodesInSinglyLinkedList.insertAtEnd(10, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(30, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(15, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(25, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(14, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(44, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(12, head);

        System.out.print("Linked List: ");
        TraverseSimpleLinkedList.print(head);

        int position = 2;

        Node<Integer> result = findFromEnd(head, position);

        if (result != null)
            System.out.println("The node at position " + position +
                    " from the end is " + result.data);
        else
            System.out.println("Invalid position: " + position);
    }
}
