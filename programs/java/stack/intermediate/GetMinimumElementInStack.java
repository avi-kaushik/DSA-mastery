package programs.java.stack.intermediate;

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

    public static void main(String[] args) {
        Stack s = new Stack(6);

        System.out.println("Push 20");
        s.push(20);

        System.out.println("Push 10");
        s.push(10);

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
    }
}
