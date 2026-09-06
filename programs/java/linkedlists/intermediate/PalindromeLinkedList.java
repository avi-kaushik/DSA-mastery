package programs.java.linkedlists.intermediate;

import programs.java.linkedlists.common.Node;

public class PalindromeLinkedList {

    // Traverse and print the linked list using loop
    private static void print(Node<String> node) {

        // Start traversal from head node
        Node<String> current = node;

        // Traverse until current node becomes null
        while (current != null) {

            // Print current node's data
            System.out.print(current.data);

            // Move to next node
            current = current.next;
        }

        System.out.println();
    }

    // Find middle node of the linked list using
    // fast and slow pointer approach
    private static Node<String> middle(Node<String> head) {

        // Return if linked list is empty
        if (head == null)
            return head;

        // Initialize both pointers at head node
        Node<String> slow = head, fast = head;

        // Traverse linked list until fast pointer
        // reaches the end
        while (fast != null && fast.next != null) {

            // Move slow pointer by one node
            slow = slow.next;

            // Move fast pointer by two nodes
            fast = fast.next.next;
        }

        // Return middle node
        return slow;
    }

    // Reverse linked list iteratively
    private static Node<String> reverse(Node<String> head) {

        // Return if linked list is empty
        if (head == null)
            return head;

        // Initialize current and previous pointers
        Node<String> current = head, prev = null;

        // Traverse linked list until current
        // pointer becomes null
        while (current != null) {

            // Store next node reference
            Node<String> next = current.next;

            // Reverse current node link
            current.next = prev;

            // Move previous pointer forward
            prev = current;

            // Move current pointer forward
            current = next;
        }

        // Return new head of reversed linked list
        return prev;
    }

    // Check whether linked list is palindrome
    public static boolean isPalindrome(Node<String> head) {

        // Find middle node of the linked list
        Node<String> middle = middle(head);

        // Reverse second half of the linked list
        Node<String> h2 = reverse(middle);

        // Initialize traversal pointers for
        // first and second halves
        Node<String> c1 = head, c2 = h2;

        // Assume linked list is palindrome initially
        boolean palindrome = true;

        // Traverse and compare both halves
        while (c2 != null) {

            // If data does not match,
            // linked list is not palindrome
            if (!c1.data.equals(c2.data)) {
                palindrome = false;
                break;
            }

            // Move both pointers to next nodes
            c1 = c1.next;
            c2 = c2.next;
        }

        // Restore original linked list structure
        reverse(h2);

        // Return palindrome status
        return palindrome;
    }

    public static void main(String[] args) {

        // Create linked list nodes
        Node<String> head = new Node<String>("R");
        Node<String> node2 = new Node<String>("A");
        Node<String> node3 = new Node<String>("D");
        Node<String> node4 = new Node<String>("A");
        Node<String> node5 = new Node<String>("R");

        // Connect nodes to form linked list
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        // Print linked list
        System.out.print("Linked List: ");
        print(head);

        // Check and print palindrome status
        System.out.println("Palindrome: " + isPalindrome(head));
    }
}
