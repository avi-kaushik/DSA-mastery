package programs.java.stack.common;

/**
 * Implements two stacks using a single array.
 *
 * Stack 1 grows from left to right.
 * Stack 2 grows from right to left.
 *
 * Both stacks share the same array and can utilize
 * the available space dynamically.
 */
public class TwoStack {

    // Array used to store elements of both stacks.
    private int[] items;

    // Maximum number of elements that can be stored.
    private int capacity;

    // Index of the top element in Stack 1.
    private int top1;

    // Index of the top element in Stack 2.
    private int top2;

    /**
     * Creates two stacks sharing the same array.
     *
     * @param length Maximum number of elements that can be stored.
     */
    public TwoStack(int length) {
        items = new int[length];
        capacity = length;

        top1 = -1;
        top2 = length;
    }

    /**
     * Pushes an element onto Stack 1.
     *
     * @param data Element to be pushed.
     * @throws RuntimeException If no space is available.
     */
    public void push1(int data) {
        if (top1 + 1 >= top2)
            throw new RuntimeException("Stack Overflow");

        items[++top1] = data;
    }

    /**
     * Pushes an element onto Stack 2.
     *
     * @param data Element to be pushed.
     * @throws RuntimeException If no space is available.
     */
    public void push2(int data) {
        if (top2 - 1 <= top1)
            throw new RuntimeException("Stack Overflow");

        items[--top2] = data;
    }

    /**
     * Removes and returns the top element from Stack 1.
     *
     * @return Top element of Stack 1.
     * @throws RuntimeException If Stack 1 is empty.
     */
    public int pop1() {
        if (isEmpty1())
            throw new RuntimeException("Stack Underflow");

        int data = items[top1];
        top1--;

        return data;
    }

    /**
     * Removes and returns the top element from Stack 2.
     *
     * @return Top element of Stack 2.
     * @throws RuntimeException If Stack 2 is empty.
     */
    public int pop2() {
        if (isEmpty2())
            throw new RuntimeException("Stack Underflow");

        int data = items[top2];
        top2++;

        return data;
    }

    /**
     * Returns the number of elements present in Stack 1.
     *
     * @return Size of Stack 1.
     */
    public int size1() {
        return top1 + 1;
    }

    /**
     * Returns the number of elements present in Stack 2.
     *
     * @return Size of Stack 2.
     */
    public int size2() {
        return capacity - top2;
    }

    /**
     * Checks whether Stack 1 is empty.
     *
     * @return True if Stack 1 contains no elements, otherwise false.
     */
    public boolean isEmpty1() {
        return top1 < 0;
    }

    /**
     * Checks whether Stack 2 is empty.
     *
     * @return True if Stack 2 contains no elements, otherwise false.
     */
    public boolean isEmpty2() {
        return capacity == top2;
    }
}
