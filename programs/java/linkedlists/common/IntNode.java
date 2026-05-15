package programs.java.linkedlists.common;

/**
 * Node class for singly linked list
 *
 * Structure:
 *
 * -------------------
 * | data | next |
 * -------------------
 *
 * Example:
 *
 * 20 -> 10 -> 30
 *
 * Here:
 * data = value stored in node
 * next = reference of next node
 */
public class IntNode {

    // Value stored inside node
    public int data;

    // Reference of next node
    public IntNode next;

    /**
     * Create node with given value
     *
     * Example:
     * new IntNode(20)
     *
     * Creates:
     * -------------------
     * | 20 | null |
     * -------------------
     */
    public IntNode(int x) {

        // Store data inside node
        data = x;

        // Initially next node is null
        next = null;
    }

}
