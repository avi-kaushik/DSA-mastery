package programs.java.linkedlists.common;

/**
 * Represents a node in a Doubly Linked List.
 *
 * @param <T> the type of data stored in the node
 */
public class DoublyNode<T> {

    /**
     * The actual data/value stored inside the node.
     */
    public T data;

    /**
     * Reference to the next node in the linked list.
     */
    public DoublyNode<T> next;

    /**
     * Reference to the previous node in the linked list.
     */
    public DoublyNode<T> prev;

    /**
     * Creates a new doubly linked list node.
     *
     * @param data the value to store inside the node
     */
    public DoublyNode(T data) {
        this.data = data;
    }
}
