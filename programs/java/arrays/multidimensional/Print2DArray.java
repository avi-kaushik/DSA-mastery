package programs.java.arrays.multidimensional;

public class Print2DArray {
    static void printArray(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print("{ ");

            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + (j != arr[i].length - 1 ? ", " : ""));
            }

            System.out.println(" }");
        }
    }

    public static void main(String[] args) {
        int[][] arr = { { 10, 20 }, { 30, 40 }, { 50, 60 } };
        printArray(arr);
    }
}