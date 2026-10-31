#include <algorithm>
#include <iostream>
#include <stdexcept>
#include "../../../include/arrays/print.h"

using namespace std;

/**
 * @brief Finds the maximum sum of a contiguous subarray using Kadane's Algorithm.
 *
 * @details
 * Uses Kadane's Algorithm, a space-optimized Dynamic Programming approach,
 * to find the maximum sum among all non-empty contiguous subarrays.
 *
 * At each index, the algorithm decides whether to:
 * - Start a new subarray from the current element.
 * - Extend the maximum-sum subarray ending at the previous index.
 *
 * The larger of these two choices becomes the maximum subarray sum
 * ending at the current index.
 *
 * Algorithm (Kadane's Algorithm - Dynamic Programming):
 * 1. Validate that the array size is not negative.
 * 2. Return 0 if the array is empty.
 * 3. Initialize two variables with the first array element:
 *    - current: Maximum subarray sum ending at the current index.
 *    - sum: Maximum subarray sum encountered so far.
 * 4. Traverse the array from index 1 to size - 1.
 * 5. For each element:
 *    - Calculate the maximum between starting a new subarray
 *      and extending the previous maximum-sum subarray.
 *    - Update current with the larger result.
 *    - Update sum if current exceeds the maximum found so far.
 * 6. Return the maximum subarray sum.
 *
 * @param arr  The array containing positive, negative, or zero values.
 * @param size The number of elements in the array.
 *
 * @return The maximum sum of any non-empty contiguous subarray.
 *         Returns 0 if the array is empty.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Uses Kadane's Algorithm with Dynamic Programming and O(1)
 *       auxiliary space optimization.
 *       A subarray must contain at least one element for non-empty input.
 *       Correctly handles arrays containing only negative elements.
 *       Initializes both accumulators with arr[0] instead of 0
 *       to ensure correct results for all-negative arrays.
 *       Requires a valid array containing at least size elements.
 *       Does not modify the original array.
 *       Assumes all intermediate sums fit within the range of int.
 *
 * @par Dynamic Programming Recurrence:
 *      Let dp[i] represent the maximum sum of a non-empty contiguous
 *      subarray ending exactly at index i.
 *
 *      dp[i] = max(arr[i], dp[i - 1] + arr[i])
 *
 *      The final answer is the maximum value among all dp[i].
 *
 *      Since only dp[i - 1] is required to calculate dp[i],
 *      the algorithm uses a single variable instead of a DP array.
 *
 * @par Key Observation:
 *      At every index, the maximum-sum subarray ending at that index
 *      must either start at the current element or extend the best
 *      subarray ending at the previous index.
 *
 *      Extending a negative-sum prefix cannot improve the result
 *      compared with starting a new subarray at the current element.
 *
 * @par Loop Invariant:
 *      After processing index i:
 *      - current stores the maximum sum of any non-empty contiguous
 *        subarray ending exactly at index i.
 *      - sum stores the maximum sum of any non-empty contiguous
 *        subarray within the processed prefix arr[0...i].
 *
 * @par Time Complexity:
 *      Best:  O(1) - The array is empty.
 *      Worst: O(n) - Every element is examined once.
 *
 * @par Auxiliary Space:
 *      O(1) - Uses only two accumulators and a loop variable.
 */
int get_maximum_sum(const int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    // An empty array has no non-empty subarray.
    if (size == 0)
        return 0;

    // Initialize with the first element to handle all-negative arrays.
    int sum = arr[0], current = arr[0];

    for (int i = 1; i < size; i++)
    {
        // Either start a new subarray or extend the previous best ending here.
        current = max(arr[i], current + arr[i]);

        // Maintain the maximum subarray sum encountered so far.
        sum = max(sum, current);
    }

    return sum;
}

int main()
{
    int arr[] = {2, 3, -8, 7, -1, 2, 3};
    int size = sizeof arr / sizeof arr[0];

    cout << "Array: ";
    print_array(arr, size);

    cout << "Maximum subarray sum in the given array is: "
         << get_maximum_sum(arr, size) << endl;

    return 0;
}
