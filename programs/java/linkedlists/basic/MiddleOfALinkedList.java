package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.IntNode;

public class MiddleOfALinkedList {

    /**
     * Find the middle node of a singly linked list using slow & fast pointer.
     *
     * Logic:
     *
     * -> slow moves 1 step at a time
     * -> fast moves 2 steps at a time
     *
     * When fast reaches the end, slow will be standing at the middle node.
     *
     * Time Complexity : O(n)
     * Space Complexity : O(1)
     */
    public static int middle(IntNode head) {

        // Empty linked list
        if (head == null)
            return -1;

        IntNode slow = head;
        IntNode fast = head;

        while (fast != null && fast.next != null) {

            // Move slow by 1 step
            slow = slow.next;

            // Move fast by 2 steps
            fast = fast.next.next;
        }

        // slow will be standing at middle node
        return slow.data;
    }

    public static void main(String[] args) {

        IntNode head = ManageNodesInSinglyLinkedList.insertAtEnd(12, null);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(15, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(8, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(53, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(37, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(5, head);

        TraverseSimpleLinkedList.print(head);

        System.out.println("The middle of this list is " + middle(head));
    }
}
