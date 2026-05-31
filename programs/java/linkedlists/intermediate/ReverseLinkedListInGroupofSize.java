package programs.java.linkedlists.intermediate;

import programs.java.linkedlists.basic.ManageNodesInSinglyLinkedList;
import programs.java.linkedlists.basic.TraverseSimpleLinkedList;
import programs.java.linkedlists.common.Node;

public class ReverseLinkedListInGroupofSize {

    /**
     * Reverse a linked list in groups of size k.
     *
     * Approach:
     * 1. Reverse first k nodes using standard linked list reversal.
     * 2. Recursively reverse the remaining linked list.
     * 3. Connect the tail of the current reversed group to the head
     * returned by the recursive call.
     *
     * Time Complexity : O(n)
     * Space Complexity: O(n/k) (recursive call stack)
     */
    public static Node<Integer> reverse(Node<Integer> head, int groupK) {

        // Empty list or invalid group size
        if (head == null)
            return head;

        Node<Integer> current = head;
        Node<Integer> prev = null;

        int i = 1;

        // Reverse first k nodes
        while (current != null && i <= groupK) {
            Node<Integer> next = current.next;

            current.next = prev;

            prev = current;

            current = next;

            i++;
        }

        /*
         * At this point:
         * prev -> Head of reversed group
         * head -> Tail of reversed group
         * current -> First node of next group
         */

        // Recursively reverse remaining groups
        if (current != null) {
            Node<Integer> nextHead = reverse(current, groupK);

            // Connect current group's tail to next reversed group
            head.next = nextHead;
        }

        // Return new head of current reversed group
        return prev;
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

        int groupK = 3;

        head = reverse(head, groupK);

        System.out.print("Grouped Reversed List: ");
        TraverseSimpleLinkedList.print(head);
    }
}
