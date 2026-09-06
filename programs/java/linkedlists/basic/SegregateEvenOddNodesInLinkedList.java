package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.Node;

public class SegregateEvenOddNodesInLinkedList {
    /**
     * Segregate even and odd nodes in a linked list while maintaining
     * their relative order.
     * 
     * Example:
     * Input: 17 -> 15 -> 8 -> 12 -> 10 -> 5 -> 4
     * Output: 8 -> 12 -> 10 -> 4 -> 17 -> 15 -> 5
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static Node<Integer> segregate(Node<Integer> head) {

        Node<Integer> current = head,
                oddList = null,
                evenList = null,
                lastEven = null,
                lastOdd = null;

        // Traverse the linked list
        while (current != null) {

            // Store current node
            Node<Integer> currentNode = current;

            // Move to next node before modifying links
            current = current.next;

            // Disconnect current node from original list
            currentNode.next = null;

            // If current node contains even data
            if (currentNode.data % 2 == 0) {

                // First even node
                if (evenList == null) {
                    evenList = lastEven = currentNode;
                }

                // Append node to end of even list
                else {
                    lastEven.next = currentNode;
                    lastEven = currentNode;
                }
            }

            // If current node contains odd data
            else {

                // First odd node
                if (oddList == null) {
                    oddList = lastOdd = currentNode;
                }

                // Append node to end of odd list
                else {
                    lastOdd.next = currentNode;
                    lastOdd = currentNode;
                }
            }
        }

        // If all nodes are even or all nodes are odd,
        // original list is already segregated
        if (oddList == null || evenList == null)
            return head;

        // Connect even list with odd list
        lastEven.next = oddList;

        // Return head of segregated list
        return evenList;
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

        segregate(head);

        System.out.print("Segregated Linked List: ");
        TraverseSimpleLinkedList.print(head);

    }
}
