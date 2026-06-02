package programs.java.linkedlists.intermediate;

import programs.java.linkedlists.basic.TraverseSimpleLinkedList;
import programs.java.linkedlists.common.Node;

/**
 * Utility class for managing loops in a linked list.
 *
 * Supported operations:
 * 1. Detect loop
 * 2. Find length of loop
 * 3. Find starting node of loop
 * 4. Remove loop
 *
 * Most operations in this class use Floyd's Cycle Detection
 * Algorithm (Tortoise and Hare).
 */
public class ManageLoopInLinkedList {
    /**
     * Detect and remove a loop from a linked list.
     *
     * Approach:
     * 1. Detect loop using Floyd's Cycle Detection Algorithm.
     * 2. If no loop exists, return.
     * 3. If loop starts at head, find the last node in the loop
     * and break the connection.
     * 4. Otherwise, find the node just before the start of the
     * loop and remove the connection.
     *
     * Time Complexity : O(n)
     * Space Complexity: O(1)
     */
    public static void deleteLoop(Node<Integer> head) {

        Node<Integer> slow = head, fast = head;

        // Detect loop using Floyd's Cycle Detection Algorithm
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            // Loop detected
            if (slow == fast) {
                break;
            }
        }

        // No loop found
        if (slow != fast)
            return;

        /*
         * Special Case:
         * Loop starts at head node.
         *
         * Example:
         * 10 -> 20 -> 30 -> 40
         * ^ |
         * |_______________|
         */
        if (fast == head) {

            // Find last node in the loop
            while (fast.next != head) {
                fast = fast.next;
            }

            // Remove loop
            fast.next = null;

            return;
        }

        // Move slow to head
        slow = head;

        /*
         * At this point:
         *
         * slow -> Starts from head
         * fast -> Starts from meeting point
         *
         * Move both pointers one step at a time until:
         *
         * slow.next == fast.next
         *
         * Then:
         * slow -> Node before loop starting node
         * fast -> Last node in the loop
         * 
         * This loop will stop when slow and fast pointer will reach to the end of the
         * cycle.
         */
        while (slow.next != fast.next) {
            slow = slow.next;
            fast = fast.next;
        }

        // Remove loop
        fast.next = null;
    }

    /**
     * Detect whether a linked list contains a loop.
     *
     * Approach:
     * 1. Use two pointers:
     * - Slow pointer moves one node at a time.
     * - Fast pointer moves two nodes at a time.
     * 2. If a loop exists, both pointers eventually meet.
     * 3. If fast reaches the end of the list, no loop exists.
     *
     * Time Complexity : O(n)
     * Space Complexity: O(1)
     */
    public static boolean isLoop(Node<Integer> head) {
        Node<Integer> slow = head, fast = head;

        while (fast != null && fast.next != null) {

            // Move slow by one node
            slow = slow.next;

            // Move fast by two nodes
            fast = fast.next.next;

            // Loop detected
            if (slow == fast) {
                return true;
            }
        }

        // No loop found
        return false;
    }

    /**
     * Finds the length of a loop in a singly linked list using Floyd's
     * Cycle Detection Algorithm (Tortoise and Hare Algorithm).
     *
     * Logic:
     * 1. Initialize slow and fast pointers at the head.
     * 2. Move slow by one node and fast by two nodes.
     * 3. If slow and fast meet, a loop exists in the linked list.
     * 4. Keep one pointer at the meeting point and move the other pointer
     * through the loop until it reaches the meeting point again.
     * 5. Count the number of nodes visited during this traversal.
     * 6. The count represents the length of the loop.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     * 
     */
    public static int length(Node<Integer> head) {
        Node<Integer> slow = head, fast = head;

        while (fast != null && fast.next != null) {

            // Move slow by one node
            slow = slow.next;

            // Move fast by two nodes
            fast = fast.next.next;

            // Loop detected
            if (slow == fast) {
                int length = 1;

                slow = slow.next;

                while (slow != fast) {
                    slow = slow.next;
                    length++;
                }

                return length;
            }
        }

        return 0;
    }

    /**
     * Finds the starting node of a loop in a linked list using Floyd's Cycle
     * Detection Algorithm.
     *
     * Logic:
     * 1. Use slow and fast pointers to detect whether a loop exists.
     * 2. If slow and fast meet, reset slow to head.
     * 3. Move both pointers one step at a time.
     * 4. The node where they meet again is the starting node of the loop.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static Node<Integer> startOfTheLoop(Node<Integer> head) {
        if (head == null)
            return head;

        Node<Integer> slow = head, fast = head;

        // Use fast-slow pointers
        while (fast != null && fast.next != null) {

            // Move fast pointer by 1 node
            slow = slow.next;

            // Move fast pointer by 2 nodes
            fast = fast.next.next;

            // Cycle found if both pointer meet each other.
            if (slow == fast) {

                // Move slow to head
                slow = head;

                /*
                 * At this point:
                 *
                 * slow -> Starts from head
                 * fast -> Starts from the meeting point inside the loop
                 *
                 * Move both pointers one step at a time until:
                 *
                 * slow == fast
                 *
                 * According to Floyd's Cycle Detection proof:
                 *
                 * Distance(head -> start of loop)
                 * =
                 * Distance(meeting point -> start of loop)
                 *
                 * Therefore, both pointers will meet at the
                 * starting node of the loop.
                 */
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }

                // Both fast and slow will be the starting node.
                return fast;

            }
        }

        // Returns null, if not found anything.
        return null;
    }

    /**
     * Finds the last node of a loop in a singly linked list using Floyd's
     * Cycle Detection Algorithm (Tortoise and Hare Algorithm).
     *
     * Logic:
     * 1. Use Floyd's Cycle Detection Algorithm to detect whether a loop exists.
     * 2. If slow and fast pointers meet, a loop is present.
     * 3. Move slow pointer to the head while keeping fast at the meeting point.
     * 4. Move both pointers one node at a time until:
     *
     * slow.next == fast.next
     *
     * 5. At this point:
     * slow -> Node before the start of the loop
     * fast -> Last node of the loop
     *
     * 6. Return fast as the last node of the loop.
     *
     * Special Case:
     * If the loop starts at the head node, move fast around the cycle
     * until fast.next becomes head. The current fast node will be the
     * last node of the loop.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public static Node<Integer> endOfTheLoop(Node<Integer> head) {
        if (head == null)
            return head;

        Node<Integer> slow = head, fast = head;

        // Use fast-slow pointers
        while (fast != null && fast.next != null) {

            // Move fast pointer by 1 node
            slow = slow.next;

            // Move fast pointer by 2 nodes
            fast = fast.next.next;

            // Cycle found if both pointer meet each other.
            if (slow == fast) {

                slow = head;

                // Loop starts at head
                if (slow == fast) {

                    while (fast.next != head) {
                        fast = fast.next;
                    }

                    return fast;
                }

                while (slow.next != fast.next) {
                    slow = slow.next;
                    fast = fast.next;
                }

                return fast;
            }
        }

        // Returns null, if not found anything.
        return null;
    }

    public static void main(String[] args) {

        // Create nodes
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

        // Create loop
        n7.next = n3;

        Node<Integer> head = n1;

        System.out.println("Loop Exists: " + isLoop(head));

        System.out.println("Start of the Loop: " + startOfTheLoop(head));
        System.out.println("End of the Loop: " + endOfTheLoop(head));

        System.out.println("Length of the Loop: " + length(head));

        System.out.println("Deleting loop...");
        deleteLoop(head);

        System.out.print("Linked list: ");
        TraverseSimpleLinkedList.print(head);
    }
}
