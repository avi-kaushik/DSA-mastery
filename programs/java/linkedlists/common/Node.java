package programs.java.linkedlists.common;

/**
 * Represents a node in a Singly Linked List.
 *
 * Structure:
 *
 * -------------------------
 * | data | next |
 * -------------------------
 *
 * Example:
 *
 * 20 -> 10 -> 30 -> null
 *
 * Here:
 * data = value stored inside the node
 * next = reference to the next node
 *
 * @param <T> the type of data stored in the node
 */
public class Node<T> {

    /**
     * Value/data stored inside the node.
     */
    public T data;

    /**
     * Reference to the next node.
     */
    public Node<T> next;

    /**
     * Creates a node with the given value.
     *
     * Example:
     *
     * new Node<>(20)
     *
     * Creates:
     *
     * -------------------------
     * | 20 | null |
     * -------------------------
     *
     * @param data the value to store inside the node
     */
    public Node(T data) {

        // Store value inside node
        this.data = data;

        // Initially next node is null
        this.next = null;
    }

    /**
     * Returns string representation of node data.
     *
     * Useful while printing linked lists.
     *
     * @return node data as string
     */
    @Override
    public String toString() {
        return String.valueOf(data);
    }
}
