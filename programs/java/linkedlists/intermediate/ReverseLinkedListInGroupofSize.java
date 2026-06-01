package programs.java.linkedlists.intermediate;

import programs.java.linkedlists.basic.ManageNodesInSinglyLinkedList;
import programs.java.linkedlists.basic.TraverseSimpleLinkedList;
import programs.java.linkedlists.common.Node;

public class ReverseLinkedListInGroupofSize {

    /**
     * Reverse a linked list in groups of size k (iterative approach).
     * 
     * Approach:
     * 1. Reverse one group of k nodes.
     * 2. Connect previous reversed group with current reversed group.
     * 3. Repeat until all nodes are processed.
     *
     * Time Complexity : O(n)
     * Space Complexity: O(1)
     */
    public static Node<Integer> reverse(Node<Integer> head, int groupK) {

        // Empty list or invalid group size
        if (head == null || groupK <= 1)
            return head;

        Node<Integer> current = head, prevFirst = null;
        boolean isFirstPass = true;

        while (current != null) {

            /*
             * first -> First node of current group.
             * After reversal, it becomes the tail of the group.
             */
            Node<Integer> first = current;

            Node<Integer> prev = null;
            int i = 0;

            // Reverse current group of size k
            while (current != null && i < groupK) {
                Node<Integer> next = current.next;

                current.next = prev;

                prev = current;
                current = next;

                i++;
            }

            /*
             * At this point:
             *
             * prev -> Head of reversed group
             * first -> Tail of reversed group
             * current -> First node of next group
             */

            // Update overall head after reversing first group
            if (isFirstPass) {
                head = prev;
                isFirstPass = false;
            } else {

                // Connect previous group's tail with current group's head
                prevFirst.next = prev;
            }

            // Store current group's tail for next connection
            prevFirst = first;
        }

        return head;
    }

    /**
     * Reverse a linked list in groups of size k (recursive).
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
    public static Node<Integer> reverseRecursive(Node<Integer> head, int groupK) {

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
            Node<Integer> nextHead = reverseRecursive(current, groupK);

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
