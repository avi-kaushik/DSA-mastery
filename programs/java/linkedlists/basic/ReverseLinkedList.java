package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.Node;

public class ReverseLinkedList {

    /**
     * Reverses the linked list using an iterative approach.
     * 
     * Logic:
     * Traverse the linked list and reverse the links
     * one by one using previous, current and next pointers.
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static Node<Integer> reverse(Node<Integer> head) {
        if (head == null)
            return head;

        Node<Integer> current = head;
        Node<Integer> prev = null;

        while (current != null) {

            // Store next node before breaking the link
            Node<Integer> next = current.next;

            // Reverse current node link
            current.next = prev;

            // Move prev and current one step ahead
            prev = current;
            current = next;
        }

        // Prev will be the new head of reversed linked list
        return prev;
    }

    public static void main(String[] args) {
        Node<Integer> head = ManageNodesInSinglyLinkedList.insertAtEnd(12, null);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(15, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(8, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(53, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(37, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(5, head);

        System.out.print("Linked List: ");
        TraverseSimpleLinkedList.print(head);

        System.out.println();
        System.out.print("Reversed: ");
        TraverseSimpleLinkedList.print(reverse(head));
    }
}
