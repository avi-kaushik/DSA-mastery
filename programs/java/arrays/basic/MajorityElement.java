import java.util.Arrays;

/**
 * Finds the majority element in an array using
 * Boyer–Moore Voting Algorithm.
 *
 * Majority element = element appearing more than n/2 times.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 */
public class MajorityElement {
    /*
     * The idea is to maintain a candidate element and its vote count.
     *
     * 1. Assume the first element as the candidate and set count = 1.
     *
     * 2. Traverse the array:
     * - If the current element is equal to the candidate,
     * increment the count.
     * - If it is different, decrement the count.
     *
     * 3. If count becomes 0, it means the current candidate
     * has been completely canceled out by other elements.
     * So we select the current element as the new candidate
     * and reset count to 1.
     *
     * Why this works:
     * If an element appears more than n/2 times,
     * it cannot be fully canceled by other elements.
     * After all pair cancellations, the majority element
     * will remain as the final candidate.
     *
     * Note:
     * This algorithm only guarantees correctness if
     * a majority element (> n/2 occurrences) exists.
     * Otherwise, a second verification pass is required.
     */
    static int getMajorityElement(int arr[]) {
        int majorityElement = arr[0], chances = 1;

        for (int i = 1; i < arr.length; i++) {
            if (majorityElement == arr[i]) {
                chances++;
            } else {
                chances--;
            }

            // If count becomes zero, choose new candidate
            if (chances == 0) {
                majorityElement = arr[i];
                chances = 1;
            }

            System.out.println("i " + i + " chances: " + chances + " majority element: " + majorityElement);
        }

        int presentFrequency = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == majorityElement) {
                presentFrequency++;
            }
        }

        return presentFrequency > arr.length / 2 ? majorityElement : -1;
    }

    public static void main(String[] args) {
        int arr[] = { 1, 4, 1, 5, 4, 1, 1, 7 };

        System.out.println("Given array: " + Arrays.toString(arr));
        System.out.println("The majority element for the above array is: " + getMajorityElement(arr));
    }
}
