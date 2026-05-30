package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.Node;

public class RemoveDuplicatesFromSortedLinkedList {
    /**
     * Removes duplicate nodes from a sorted linked list.
     * 
     * Logic:
     * Traverse the linked list and compare each node
     * with its next node. If both values are same,
     * remove the duplicate node by updating the link.
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static void removeDuplicates(Node<Integer> head) {

        Node<Integer> current = head;

        while (current != null && current.next != null) {

            // Remove duplicate node
            if (current.data.equals(current.next.data)) {
                current.next = current.next.next;
            }

            // Move to next distinct node
            else {
                current = current.next;
            }
        }
    }

    public static void main(String[] args) {
        Node<Integer> head = ManageNodesInSinglyLinkedList.insertAtEnd(8, null);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(15, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(15, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(30, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(30, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(50, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(50, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(50, head);
        head = ManageNodesInSinglyLinkedList.insertAtEnd(50, head);

        System.out.print("Original Linked List: ");
        TraverseSimpleLinkedList.print(head);

        removeDuplicates(head);

        System.out.print("Linked List After Removing Duplicates: ");
        TraverseSimpleLinkedList.print(head);
    }
}
