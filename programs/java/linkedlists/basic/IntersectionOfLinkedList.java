package programs.java.linkedlists.basic;

import programs.java.linkedlists.common.Node;

public class IntersectionOfLinkedList {

    /**
     * Finds the intersection node of two singly linked lists.
     *
     * Logic:
     * 1. Find the length of both linked lists.
     * 2. Move the pointer of the longer linked list ahead by the
     * difference in lengths.
     * 3. Traverse both linked lists together.
     * 4. The first common node encountered is the intersection point.
     *
     * Time Complexity: O(n + m)
     * Space Complexity: O(1)
     */
    public static Node<Integer> getIntersection(Node<Integer> head1, Node<Integer> head2) {

        // Find length of both linked lists
        int length1 = TraverseSimpleLinkedList.length(head1);
        int length2 = TraverseSimpleLinkedList.length(head2);

        Node<Integer> largeList = null, smallList = null;
        int diff = 0;

        // Determine which linked list is larger and calculate
        // the difference between their lengths
        if (length1 > length2) {
            largeList = head1;
            smallList = head2;
            diff = length1 - length2;
        } else {
            largeList = head2;
            smallList = head1;
            diff = length2 - length1;
        }

        // Move the pointer of the larger linked list ahead by
        // 'diff' nodes so that both pointers have equal number
        // of nodes remaining until the end
        while (diff != 0) {
            largeList = largeList.next;
            diff--;
        }

        // Traverse both linked lists simultaneously
        while (largeList != null && smallList != null) {

            // If both pointers point to the same node,
            // intersection point is found
            if (largeList == smallList)
                return largeList;

            // Move both pointers by one node
            largeList = largeList.next;
            smallList = smallList.next;
        }

        // No intersection exists between the linked lists
        return null;
    }

    public static void main(String[] args) {
        Node<Integer> n1 = new Node<>(10);
        Node<Integer> n2 = new Node<>(30);
        Node<Integer> n3 = new Node<>(15);
        Node<Integer> n4 = new Node<>(25);
        Node<Integer> n5 = new Node<>(14);
        Node<Integer> n6 = new Node<>(44);
        Node<Integer> n7 = new Node<>(12);

        // Connect nodes
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;
        n5.next = n6;
        n6.next = n7;

        Node<Integer> head1 = n1;

        Node<Integer> n8 = new Node<>(48);
        Node<Integer> n9 = new Node<>(19);
        Node<Integer> n10 = new Node<>(10);

        n8.next = n9;
        n9.next = n10;

        Node<Integer> head2 = n8;
        n10.next = n5;

        System.out.print("1st Linked List: ");
        TraverseSimpleLinkedList.print(head1);

        System.out.print("2nd Linked List: ");
        TraverseSimpleLinkedList.print(head2);

        System.out.println("Intersection point of both linked list is: " + getIntersection(head1, head2));
    }
}
