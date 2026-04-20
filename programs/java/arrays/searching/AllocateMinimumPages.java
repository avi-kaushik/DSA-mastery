package programs.java.arrays.searching;

import java.util.Arrays;

public class AllocateMinimumPages {

    /**
     * Problem:
     * We are given an array where each element represents number of pages in a
     * book.
     * We need to allocate books to k students such that:
     * 1. Each student gets contiguous books
     * 2. Each book is assigned to only one student
     * 3. We minimize the maximum pages assigned to any student
     *
     * Approach:
     * Binary Search on Answer
     */
    public static int getMinPages(int arr[], int k) {

        // Step 1: Find total pages (sum) and maximum single book (max)
        // These define our search space
        int sum = 0, max = 0;

        for (int num : arr) {
            sum += num; // total pages
            max = Math.max(max, num); // largest book
        }

        // Search space:
        // Minimum possible answer = max (at least one student must take the largest
        // book)
        // Maximum possible answer = sum (one student takes all books)
        int low = max, high = sum;

        // This will store our final answer (minimum valid maxPages)
        int result = sum;

        // Binary Search starts
        while (low <= high) {

            // Try a middle value as the maximum pages allowed per student
            int mid = (low + high) / 2;

            // These variables help simulate allocation
            int currentSum = 0; // pages assigned to current student
            int students = 1; // at least one student is needed

            // Step 2: Check feasibility
            // Try to allocate books such that no student gets more than 'mid' pages
            for (int num : arr) {

                // If adding current book exceeds the limit
                if (currentSum + num > mid) {
                    students++; // assign new student
                    currentSum = num; // start with this book
                } else {
                    currentSum += num; // continue with same student
                }

                // Optimization:
                // If students already exceed k, no need to continue
                if (students > k)
                    break;
            }

            // Step 3: Binary search decision

            if (students > k) {
                // Too many students needed → mid is too small
                // Increase the allowed pages
                low = mid + 1;
            } else {
                // Allocation is possible
                // Store this as a potential answer
                result = mid;

                // Try to find a smaller valid answer
                high = mid - 1;
            }
        }

        // Final minimized maximum pages
        return result;
    }

    public static void main(String[] args) {
        int arr[] = { 10, 20, 30, 40 };

        System.out.println("Array => " + Arrays.toString(arr));

        System.out.println("The minimised minimum allocated pages are: " + getMinPages(arr, 2));
    }
}
