package programs.java.stack.basic;

import programs.java.stack.common.IntStack;

public class StackArrayImplementation {

    public static void main(String[] args) {

        System.out.println("========== Stack Demo ==========\n");

        IntStack stack = new IntStack(10);

        System.out.println("Created Stack with Capacity = 10");

        System.out.println("\n----- Initial State -----");
        System.out.println("Is Stack Empty? : " + stack.isEmpty());
        System.out.println("Current Size    : " + stack.size());

        System.out.println("\n----- Push Operations -----");

        System.out.println("Push(10)");
        stack.push(10);

        System.out.println("Push(12)");
        stack.push(12);

        System.out.println("Push(9)");
        stack.push(9);

        System.out.println("Current Top Element : " + stack.peek());
        System.out.println("Current Size        : " + stack.size());

        System.out.println("\n----- Peek Operation -----");

        System.out.println("Peek() : " + stack.peek());

        System.out.println("\n----- Pop Operation -----");

        System.out.println("Pop() : " + stack.pop());

        System.out.println("Top Element After Pop : " + stack.peek());

        System.out.println("\n----- Push More Elements -----");

        System.out.println("Push(35)");
        stack.push(35);

        System.out.println("Current Top Element : " + stack.peek());
        System.out.println("Current Size        : " + stack.size());

        System.out.println("\n----- Final State -----");

        System.out.println("Is Stack Empty? : " + stack.isEmpty());
        System.out.println("Current Size    : " + stack.size());
        System.out.println("Top Element     : " + stack.peek());

        System.out.println("\n========== End ==========");
    }
}
