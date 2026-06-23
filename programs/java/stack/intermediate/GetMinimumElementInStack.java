package programs.java.stack.intermediate;

import programs.java.stack.common.IntStack;

public class GetMinimumElementInStack {

    public static class Stack {
        private final int[] items;
        private final int[] minStack;

        private int minTop = -1;
        private int top = -1;

        /**
         * Creates a stack with fixed capacity.
         *
         * @param length Maximum number of elements stack can hold.
         */
        public Stack(int length) {
            items = new int[length];
            minStack = new int[length];
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

            if (minTop == -1 || data <= minStack[minTop]) {
                minStack[++minTop] = data;
            }

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

            int popped = items[top--];

            if (popped == minStack[minTop]) {
                minTop--;
            }

            return popped;
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

        /**
         * Returns the minimum element currently present in the stack.
         *
         * @return Minimum element in the stack.
         *
         * @throws RuntimeException when stack is empty.
         */
        public int getMin() {
            if (isEmpty()) {
                throw new RuntimeException("Stack is Empty");
            }

            return minStack[minTop];
        }
    }

    /**
     * Stack implementation that supports retrieving the minimum element
     * in O(1) time using O(1) extra space.
     *
     * This is achieved by storing encoded values in the stack whenever a
     * new minimum element is pushed. These encoded values preserve enough
     * information to reconstruct previous minimums during pop operations.
     *
     * Encoding formula:
     * encodedValue = (2 * newMin) - previousMin
     *
     * A value smaller than the current minimum indicates that it is an
     * encoded value rather than a normal stack element.
     *
     * Decoding formula:
     * previousMin = (2 * currentMin) - encodedValue
     */
    public static class StackV2 extends IntStack {

        private int min = 0;

        public StackV2(int length) {
            super(length);
        }

        /**
         * Pushes an element onto the stack.
         *
         * If the new element becomes the new minimum, an encoded value is
         * stored instead of the actual value, and the minimum is updated.
         *
         * @param data element to be pushed
         * @return value stored in the stack (encoded or original)
         */
        @Override
        public int push(int data) {
            int res = data;

            if (isEmpty()) {
                min = data;
            } else if (data < min) {
                int lastMin = min;
                res = 2 * data - lastMin;
                min = data;
            }

            return super.push(res);
        }

        /**
         * Removes and returns the top element from the stack.
         *
         * If the popped value is smaller than the current minimum, it is
         * an encoded value and is used to restore the previous minimum.
         *
         * @return top element (decoded if required)
         */
        @Override
        public int pop() {
            int popped = super.pop();

            if (popped < min) {
                int lastMin = min;
                min = 2 * min - popped;
                popped = lastMin;
            }

            return popped;
        }

        /**
         * Returns the top element without removing it.
         *
         * If the stored value is encoded, the actual top element is the
         * current minimum.
         *
         * @return top element of the stack
         */
        @Override
        public int peek() {
            int data = super.peek();
            return data < min ? min : data;
        }

        /**
         * Returns the minimum element currently present in the stack.
         *
         * @return current minimum element
         * @throws RuntimeException if the stack is empty
         */
        public int getMin() {
            if (isEmpty()) {
                throw new RuntimeException("Stack is Empty");
            }

            return min;
        }
    }

    public static void main(String[] args) {

        StackV2 s = new StackV2(10);

        System.out.println("Push 20");
        s.push(20);

        System.out.println("Push 10");
        s.push(10);

        System.out.println("Peek: " + s.peek());
        System.out.println("Current Min: " + s.getMin());

        System.out.println("Push 6");
        s.push(6);
        System.out.println("Current Min: " + s.getMin());

        System.out.println("Pop: " + s.pop());

        System.out.println("Push 16");
        s.push(16);
        System.out.println("Current Min: " + s.getMin());

        System.out.println("Pop: " + s.pop());

        System.out.println("Push 4");
        s.push(4);
        System.out.println("Current Min: " + s.getMin());

        System.out.println("Push -5");
        s.push(-5);

        System.out.println("Push -10");
        s.push(-10);

        System.out.println("Push -15");
        s.push(-15);

        System.out.println("Current Min: " + s.getMin());

        System.out.println("Pop: " + s.pop());
        System.out.println("Current Min: " + s.getMin());

        System.out.println("Pop: " + s.pop());
        System.out.println("Current Min: " + s.getMin());

        System.out.println("Pop: " + s.pop());
        System.out.println("Current Min: " + s.getMin());
    }
}
