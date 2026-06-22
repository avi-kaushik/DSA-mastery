package programs.java.stack.common;

// Stack implementation using Array.
public class IntStack {

    // Array used to store stack elements.
    private int[] items;

    // Stores index of the current top element.
    private int top;

    /**
     * Creates a stack with fixed capacity.
     *
     * @param length Maximum number of elements stack can hold.
     */
    public IntStack(int length) {
        items = new int[length];
        top = -1;
    }

    /**
     * Checks whether stack contains any element.
     *
     * @return true if stack is empty.
     */
    public boolean isEmpty() {
        return top < 0;
    }

    /**
     * Checks whether stack has reached maximum capacity.
     * 
     * @return true if stack is full.
     */
    public boolean isFull() {
        return top == items.length - 1;
    }

    /**
     * Inserts an element at the top of the stack.
     * 
     * @param data Element to be inserted.
     *
     * @throws RuntimeException when stack is full.
     */
    public int push(int data) {
        if (isFull()) {
            throw new RuntimeException("Stack Overflow");
        }

        items[++top] = data;

        return data;
    }

    /**
     * Removes and returns the top element.
     * 
     * @return Top element of stack.
     *
     * @throws RuntimeException when stack is empty.
     */
    public int pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack Underflow");
        }

        return items[top--];
    }

    /**
     * Returns the top element without removing it.
     *
     * @return Top element of stack.
     *
     * @throws RuntimeException when stack is empty.
     */
    public int peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is Empty");
        }

        return items[top];
    }

    // Returns total number of elements currently present.
    public int size() {
        return top + 1;
    }
}
