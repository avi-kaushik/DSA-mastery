package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.Node;

public class MergeTwoSortedLinkedList {

    /**
     * Merge two sorted linked lists into a single sorted linked list.
     *
     * Approach:
     * 1. Handle edge cases where either of the linked lists is empty.
     * 2. Compare the first nodes of both linked lists and choose the smaller node
     * as the head of the merged linked list.
     * 3. Maintain a tail pointer which always points to the last node of the
     * merged linked list.
     * 4. Traverse both linked lists simultaneously:
     * - Compare current nodes from both linked lists.
     * - Attach the smaller node to the merged linked list.
     * - Move the corresponding pointer forward.
     * - Update tail to the newly attached node.
     * 5. Once any one linked list is exhausted, attach the remaining nodes of
     * the other linked list directly to the merged linked list.
     *
     * Time Complexity: O(m + n)
     * - Each node from both linked lists is processed exactly once.
     *
     * Space Complexity: O(1)
     * - No extra linked list is created.
     * - Existing nodes are rearranged in-place.
     */
    public static Node<Integer> merge(Node<Integer> head1, Node<Integer> head2) {
        if (head1 == null)
            return head2;

        if (head2 == null)
            return head1;

        // Head of the merged sorted linked list
        Node<Integer> newHead = null;

        // Always points to the last node of the merged linked list
        Node<Integer> tail = null;

        // Used to traverse the first linked list
        Node<Integer> current1 = head1;

        // Used to traverse the second linked list
        Node<Integer> current2 = head2;

        // Select the smaller node as the head of the merged linked list.
        // Since this node is already included in the result,
        // move the corresponding pointer to the next node.
        if (head1.data <= head2.data) {
            newHead = tail = head1;
            current1 = current1.next;
        } else {
            newHead = tail = head2;
            current2 = current2.next;
        }

        // Compare nodes from both linked lists and always attach
        // the smaller node at the end of the merged linked list.
        while (current1 != null && current2 != null) {
            if (current1.data <= current2.data) {
                tail.next = current1;
                tail = current1;

                current1 = current1.next;
            } else {
                tail.next = current2;
                tail = current2;

                current2 = current2.next;
            }

        }

        // One linked list has been completely processed.
        // Attach all remaining nodes from the other linked list.
        if (current2 == null)
            tail.next = current1;

        else if (current1 == null)
            tail.next = current2;

        return newHead;
    }

    public static void main(String[] args) {
        Node<Integer> head1 = ManageNodesInSinglyLinkedList.insertAtEnd(10, null);
        head1 = ManageNodesInSinglyLinkedList.insertAtEnd(22, head1);
        head1 = ManageNodesInSinglyLinkedList.insertAtEnd(28, head1);
        head1 = ManageNodesInSinglyLinkedList.insertAtEnd(35, head1);
        head1 = ManageNodesInSinglyLinkedList.insertAtEnd(45, head1);

        Node<Integer> head2 = ManageNodesInSinglyLinkedList.insertAtEnd(15, null);
        head2 = ManageNodesInSinglyLinkedList.insertAtEnd(32, head2);

        System.out.print("2nd Linked list: ");
        TraverseSimpleLinkedList.print(head2);

        System.out.print("1st Linked list: ");
        TraverseSimpleLinkedList.print(head1);

        System.out.println();

        System.out.print("Merged Sorted Linked list: ");
        TraverseSimpleLinkedList.print(merge(head2, head1));
    }
}
