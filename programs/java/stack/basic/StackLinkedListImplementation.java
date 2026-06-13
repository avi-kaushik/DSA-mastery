package programs.java.stack.basic;

import programs.java.stack.common.IntStackList;

public class StackLinkedListImplementation {
    public static void main(String[] args) {
        IntStackList stack = new IntStackList();

        System.out.println("Initial Stack: " + stack);
        System.out.println("Is Empty: " + stack.isEmpty());

        System.out.println("\nPush 10");
        stack.push(10);
        System.out.println("Stack: " + stack);

        System.out.println("\nPush 20");
        stack.push(20);
        System.out.println("Stack: " + stack);

        System.out.println("\nPush 30");
        stack.push(30);
        System.out.println("Stack: " + stack);

        System.out.println("\nPeek");
        System.out.println("Top Element: " + stack.peek());

        System.out.println("\nSize");
        System.out.println("Size: " + stack.size());

        System.out.println("\nPop");
        System.out.println("Removed Element: " + stack.pop());
        System.out.println("Stack: " + stack);

        System.out.println("\nPop");
        System.out.println("Removed Element: " + stack.pop());
        System.out.println("Stack: " + stack);

        System.out.println("\nPeek");
        System.out.println("Top Element: " + stack.peek());

        System.out.println("\nSize");
        System.out.println("Size: " + stack.size());

        System.out.println("\nPop");
        System.out.println("Removed Element: " + stack.pop());
        System.out.println("Stack: " + stack);

        System.out.println("\nIs Empty");
        System.out.println("Is Empty: " + stack.isEmpty());
    }
}
