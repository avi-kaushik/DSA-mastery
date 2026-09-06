package programs.java.linkedlists.intermediate;

import programs.java.linkedlists.common.RandomNode;

public class CloneLinkedListWithRandomPointer {
    public static void print(RandomNode<Integer> head) {
        // Start traversal from head node
        RandomNode<Integer> current = head;

        // Traverse until current node becomes null
        while (current != null) {

            // Print current node's data
            System.out.print(current.data + (current.next != null ? ", " : ""));

            // Move to next node
            current = current.next;
        }

        System.out.println();
    }

    /**
     * Clone a linked list having next and random pointers.
     *
     * Approach:
     * 1. Insert cloned nodes between original nodes.
     * 2. Assign random pointers for cloned nodes.
     * 3. Separate original and cloned linked lists.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static RandomNode<Integer> clone(RandomNode<Integer> head) {
        // Return if linked list is empty
        if (head == null)
            return head;

        RandomNode<Integer> current = head;

        // Insert cloned nodes between original nodes
        while (current != null) {
            RandomNode<Integer> newNode = new RandomNode<Integer>(current.data);

            newNode.next = current.next;
            current.next = newNode;

            current = current.next.next;
        }

        current = head;

        // Assign random pointers for cloned nodes
        while (current != null) {
            current.next.random = current.random == null ? null : current.random.next;

            current = current.next.next;
        }

        RandomNode<Integer> original = head, newHead = head.next, cloned = head.next;

        // Separate original and cloned linked lists
        while (original != null) {

            // Restore original list
            original.next = original.next.next;

            // Connect cloned list
            if (cloned.next != null)
                cloned.next = cloned.next.next;

            original = original.next;
            cloned = cloned.next;
        }

        return newHead;
    }

    public static void main(String[] args) {
        RandomNode<Integer> node1 = new RandomNode<Integer>(58);
        RandomNode<Integer> node2 = new RandomNode<Integer>(18);
        RandomNode<Integer> node3 = new RandomNode<Integer>(35);
        RandomNode<Integer> node4 = new RandomNode<Integer>(44);
        RandomNode<Integer> node5 = new RandomNode<Integer>(87);
        RandomNode<Integer> node6 = new RandomNode<Integer>(15);
        RandomNode<Integer> node7 = new RandomNode<Integer>(71);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;
        node6.next = node7;

        node1.random = node5;
        node2.random = node1;
        node6.random = node2;
        node7.random = node4;
        node4.random = node3;
        node3.random = node6;
        node5.random = node7;

        System.out.print("Linked list: ");
        print(node1);

        RandomNode<Integer> clonedHead = clone(node1);

        System.out.print("Cloned Linked list: ");
        print(clonedHead);
    }
}
