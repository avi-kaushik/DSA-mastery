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

    /**
     * Reverses the linked list using recursion.
     * 
     * Logic:
     * Reverse the current node link and recursively
     * process the remaining linked list.
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(n) - recursion stack
     */
    public static Node<Integer> reverseRecursive(Node<Integer> head) {
        return reverseRecursive(head, null);
    }

    /**
     * Helper recursive method for reversing the linked list.
     */
    private static Node<Integer> reverseRecursive(Node<Integer> head, Node<Integer> prev) {

        // When current node becomes null, prev will be the new head
        if (head == null)
            return prev;

        // Store next node before reversing the link
        Node<Integer> next = head.next;

        // Reverse current node link
        head.next = prev;

        // Recursively reverse remaining linked list
        return reverseRecursive(next, head);
    }

    /**
     * Reverses the linked list using recursion.
     * 
     * Logic:
     * Recursively reverse the remaining linked list.
     * During recursion unwinding, make the next node
     * point to the current node and remove the old link.
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(n) - recursion stack
     */
    private static Node<Integer> reverseRecursive2(Node<Integer> head) {

        // Base case:
        // Empty list or last node reached
        if (head == null || head.next == null)
            return head;

        // Reverse the remaining linked list
        Node<Integer> restHead = reverseRecursive2(head.next);

        // Current next node becomes the tail
        // of the reversed remaining linked list
        Node<Integer> restTail = head.next;

        // Attach current node at the end
        restTail.next = head;

        // Remove old forward link
        head.next = null;

        // Return head of reversed linked list
        return restHead;
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
        TraverseSimpleLinkedList.print(reverseRecursive2(head));
    }
}
