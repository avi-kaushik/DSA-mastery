package programs.java.dequeue.common;

import java.util.NoSuchElementException;

import programs.java.linkedlists.common.DoublyNode;

/*
 * Implementation of a Deque (Double Ended Queue) using a Doubly Linked List.
 *
 * A Deque allows insertion, deletion and peek operations from both the
 * front and rear in constant time.
 *
 */
public class DequeueList<T> {
    private DoublyNode<T> front;
    private DoublyNode<T> rear;
    private int size = 0;

    /*
     * Inserts an element at the front of the deque.
     *
     * If the deque is empty:
     * front and rear both point to the new node.
     *
     * Otherwise:
     * New node points to the current front.
     * Current front points back to the new node.
     * Move front to the new node.
     *
     * Time Complexity: O(1)
     */
    public void insertFront(T data) {
        DoublyNode<T> node = new DoublyNode<>(data);

        if (isEmpty()) {
            front = rear = node;
        } else {
            node.next = front;
            front.prev = node;
            front = node;
        }

        size++;
    }

    /*
     * Inserts an element at the rear of the deque.
     *
     * If the deque is empty:
     * front and rear both point to the new node.
     *
     * Otherwise:
     * Current rear points to the new node.
     * New node points back to the current rear.
     * Move rear to the new node.
     *
     * Time Complexity: O(1)
     */
    public void insertRear(T data) {
        DoublyNode<T> node = new DoublyNode<>(data);

        if (isEmpty()) {
            front = rear = node;
        } else {
            rear.next = node;
            node.prev = rear;
            rear = node;
        }

        size++;
    }

    /*
     * Removes and returns the front element.
     *
     * Throws NoSuchElementException if the deque is empty.
     *
     * Steps:
     * Store the front element.
     * Move front to the next node.
     *
     * If the deque becomes empty:
     * rear becomes null.
     *
     * Otherwise:
     * Remove the backward link of the new front.
     *
     * Time Complexity: O(1)
     */
    public T removeFront() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty.");

        T value = front.data;

        front = front.next;

        if (front != null)
            front.prev = null;
        else
            rear = null;

        size--;

        return value;
    }

    /*
     * Removes and returns the rear element.
     *
     * Throws NoSuchElementException if the deque is empty.
     *
     * Steps:
     * Store the rear element.
     * Move rear to the previous node.
     *
     * If the deque becomes empty:
     * front becomes null.
     *
     * Otherwise:
     * Remove the forward link of the new rear.
     *
     * Time Complexity: O(1)
     */
    public T removeRear() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty.");

        T value = rear.data;

        rear = rear.prev;

        if (rear != null)
            rear.next = null;
        else
            front = null;

        size--;

        return value;
    }

    /*
     * Returns the front element without removing it.
     *
     * Throws NoSuchElementException if the deque is empty.
     *
     * Time Complexity: O(1)
     */
    public T peekFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty.");

        return front.data;
    }

    /*
     * Returns the rear element without removing it.
     *
     * Throws NoSuchElementException if the deque is empty.
     *
     * Time Complexity: O(1)
     */
    public T peekLast() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty.");

        return rear.data;
    }

    /*
     * Returns true if the deque contains no elements.
     *
     * Time Complexity: O(1)
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /*
     * Returns the number of elements present in the deque.
     *
     * Time Complexity: O(1)
     */
    public int size() {
        return size;
    }
}
