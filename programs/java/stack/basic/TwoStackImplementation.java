package programs.java.stack.basic;

import programs.java.stack.common.TwoStack;

public class TwoStackImplementation {

    public static void main(String[] args) {

        TwoStack stack = new TwoStack(10);

        System.out.println("=== Push Elements ===");

        stack.push1(10);
        System.out.println("Pushed 10 into Stack 1");

        stack.push1(20);
        System.out.println("Pushed 20 into Stack 1");

        stack.push1(30);
        System.out.println("Pushed 30 into Stack 1");

        stack.push2(100);
        System.out.println("Pushed 100 into Stack 2");

        stack.push2(200);
        System.out.println("Pushed 200 into Stack 2");

        stack.push2(300);
        System.out.println("Pushed 300 into Stack 2");

        System.out.println();

        System.out.println("Stack 1 Size: " + stack.size1());
        System.out.println("Stack 2 Size: " + stack.size2());

        System.out.println();

        System.out.println("=== Pop Elements ===");

        System.out.println("Popped from Stack 1: " + stack.pop1());
        System.out.println("Popped from Stack 1: " + stack.pop1());

        System.out.println("Popped from Stack 2: " + stack.pop2());
        System.out.println("Popped from Stack 2: " + stack.pop2());

        System.out.println();

        System.out.println("Stack 1 Size: " + stack.size1());
        System.out.println("Stack 2 Size: " + stack.size2());

        System.out.println();

        System.out.println("Stack 1 Empty: " + stack.isEmpty1());
        System.out.println("Stack 2 Empty: " + stack.isEmpty2());
    }
}
