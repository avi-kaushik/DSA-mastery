package programs.java.hashing.basic;

import java.util.HashMap;

public class IntersectionOfTwoUnsortedArrays {

    /**
     * Prints the distinct elements that are present in both given arrays.
     *
     * Stores all elements of the second array in a HashMap and checks
     * whether each element of the first array exists in the map.
     * The element is removed after being found to ensure that duplicate
     * elements are not printed more than once.
     *
     * Time Complexity: O(n + m) average
     * Space Complexity: O(m)
     *
     * Where:
     * - n = length of arr1
     * - m = length of arr2
     *
     * @param arr1 first array
     * @param arr2 second array
     */
    public static void printIntersection(int[] arr1, int[] arr2) {
        HashMap<Integer, Integer> elements = new HashMap<>();

        for (int i : arr2) {
            elements.put(i, 1);
        }

        System.out.println("Intersection values: ");

        for (int i : arr1) {
            if (elements.containsKey(i)) {
                System.out.print(i + " ");

                // Remove the element to prevent duplicate intersection values.
                elements.remove(i);
            }
        }

        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr1 = { 10, 15, 20, 25, 30, 50 };
        int[] arr2 = { 30, 5, 15, 80 };

        printIntersection(arr1, arr2);
    }
}
