package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.Node;

public class PairwiseSwapNodesInLinkedList {

    /**
     * Pairwise Swap Nodes In Linked List
     *
     * Swaps every adjacent pair of nodes by modifying links between nodes.
     *
     * Approach:
     * 1. Handle edge cases where the list is empty or contains only one node.
     * 2. Swap the first pair separately and update the head pointer.
     * 3. Use a previous pointer to track the tail of the last swapped pair.
     * 4. Traverse the remaining list pair by pair.
     * 5. Connect the previous swapped pair with the current pair.
     * 6. Swap the current pair by reversing the link between them.
     * 7. Move to the next pair and continue until fewer than two nodes remain.
     * 8. Attach any remaining node to the end of the swapped list.
     *
     * Time Complexity: O(n)
     * Auxiliary Space: O(1)
     */
    public static Node<Integer> swapPair(Node<Integer> head) {
        if (head == null || head.next == null)
            return head;

        // Store the starting node of the next pair
        Node<Integer> current = head.next.next;

        // Tail of the first swapped pair
        Node<Integer> prev = head;

        // Swap the first pair and update head
        head = head.next;
        head.next = prev;

        while (current != null && current.next != null) {

            // Connect previous swapped pair to current pair
            prev.next = current.next;

            // Current node becomes the tail after swapping
            prev = current;

            // Store the starting node of the next pair
            Node<Integer> next = current.next.next;

            // Swap current pair
            current.next.next = current;

            // Move to next pair
            current = next;
        }

        // Attach remaining node if present
        prev.next = current;

        return head;
    }

    /**
     * Pairwise Swap Nodes In Linked List
     *
     * Swaps every adjacent pair of nodes by modifying links between nodes.
     *
     * Approach:
     * 1. Handle edge cases where the list is empty or contains only one node.
     * 2. Store the second node as the new head of the linked list.
     * 3. Traverse the list pair by pair.
     * 4. Swap the current pair by reversing the link between them.
     * 5. Look ahead and connect the tail of the current swapped pair to the
     * head of the next swapped pair.
     * 6. Move to the next pair and continue until all pairs are processed.
     *
     * Time Complexity: O(n)
     * Auxiliary Space: O(1)
     */
    public static Node<Integer> swapPair2(Node<Integer> head) {
        if (head == null || head.next == null)
            return head;

        Node<Integer> current = head;

        // Head after first swap
        Node<Integer> outputHead = head.next;

        while (current != null && current.next != null) {

            // Store the starting node of the next pair
            Node<Integer> nextCurrent = current.next.next;

            // Second node of current pair
            Node<Integer> next = current.next;

            // Swap current pair
            next.next = current;

            // Connect current swapped pair with next swapped pair
            if (nextCurrent != null && nextCurrent.next != null) {
                current.next = nextCurrent.next;
            } else {
                current.next = nextCurrent;
            }

            // Move to next pair
            current = nextCurrent;
        }

        return outputHead;
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

        head = swapPair(head);

        System.out.print("Swapped Pair Linked List: ");
        TraverseSimpleLinkedList.print(head);
    }
}
