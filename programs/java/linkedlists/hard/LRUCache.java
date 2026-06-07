package programs.java.linkedlists.hard;

import java.util.HashMap;

import programs.java.linkedlists.basic.TraverseDoublyLinkedList;
import programs.java.linkedlists.common.DoublyNode;

public class LRUCache {
    public static HashMap<Integer, DoublyNode<Integer>> cacheMap = new HashMap<>();
    public static DoublyNode<Integer> head;
    public static DoublyNode<Integer> tail;

    public static int capacity = 4;

    /**
     * LRU Cache Implementation
     *
     * Approach:
     * 1. Use HashMap to store node references for O(1) lookup.
     * 2. Use Doubly Linked List to maintain usage order.
     * 3. Head represents Most Recently Used (MRU) node.
     * 4. Tail represents Least Recently Used (LRU) node.
     * 5. Existing nodes are moved to front when accessed.
     * 6. New nodes are inserted at front.
     * 7. Tail node is removed when cache reaches capacity.
     *
     * Time Complexity: O(1)
     * Space Complexity: O(n)
     */
    public static void put(int data) {
        DoublyNode<Integer> node;

        // If data already exists in cache
        if (cacheMap.containsKey(data)) {

            // Get existing node
            node = cacheMap.get(data);

            // Remove node from its current position
            removeNode(node);

            // Move node to the front as it is recently used
            addToFront(node);

        } else {

            // Remove least recently used node if cache is full
            if (cacheMap.size() >= capacity) {

                // Remove node from HashMap
                cacheMap.remove(tail.data);

                // Remove node from doubly linked list
                removeNode(tail);
            }

            // Create new node
            node = new DoublyNode<Integer>(data);

            // Add node at the front as most recently used
            addToFront(node);

            // Store node reference in HashMap
            cacheMap.put(data, node);
        }
    }

    /**
     * Add node at the front of doubly linked list.
     *
     * Approach:
     * 1. Connect node before current head.
     * 2. Update previous reference of current head.
     * 3. Make node the new head.
     * 4. Update tail when inserting first node.
     *
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     */
    private static void addToFront(DoublyNode<Integer> node) {

        // New head node should not have any previous node
        node.prev = null;

        // Connect node with current head
        node.next = head;

        // Connect current head back to new node
        if (head != null)
            head.prev = node;

        // Make node the new head
        head = node;

        // First node becomes both head and tail
        if (tail == null)
            tail = node;
    }

    /**
     * Remove a node from doubly linked list.
     *
     * Approach:
     * 1. Connect previous node with next node.
     * 2. Update head if first node is removed.
     * 3. Update tail if last node is removed.
     * 4. Disconnect removed node completely.
     *
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     */
    private static void removeNode(DoublyNode<Integer> node) {

        // Nothing to remove
        if (node == null)
            return;

        // Connect previous node with next node
        if (node.prev != null)
            node.prev.next = node.next;

        // Update head if removing first node
        else
            head = node.next;

        // Connect next node with previous node
        if (node.next != null)
            node.next.prev = node.prev;

        // Update tail if removing last node
        else
            tail = node.prev;

        // Disconnect node completely from list
        node.prev = null;
        node.next = null;
    }

    public static void main(String[] args) {
        put(4);
        put(18);
        put(9);
        put(12);
        put(14);
        put(18);
        put(12);
        put(12);
        put(19);
        put(7);
        put(7);

        TraverseDoublyLinkedList.print(head);
    }
}
