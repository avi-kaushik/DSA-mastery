#include <algorithm>
#include <iostream>
#include <stdexcept>
#include "../../../include/arrays/print.h"

using namespace std;

/**
 * @brief Finds the maximum sum of exactly k consecutive elements
 *        using the Fixed-Size Sliding Window technique.
 *
 * Algorithm (Fixed-Size Sliding Window):
 * 1. Validate the array size and handle an empty array.
 * 2. Validate that the window size k is within [1, size].
 * 3. Calculate the sum of the first k elements.
 * 4. Initialize the current window sum with the first window's sum.
 * 5. Slide the window across the remaining elements:
 *    - Add the incoming element and subtract the outgoing element.
 *    - Update the maximum sum if the current window sum is greater.
 * 6. Return the maximum window sum.
 *
 * @param arr  The input array containing integer elements.
 * @param size The number of elements in the array.
 * @param k    The number of consecutive elements in each window.
 *
 * @return The maximum sum among all contiguous subarrays of size k.
 *         Returns 0 if the array is empty.
 *
 * @throws std::out_of_range If size is negative or k is invalid
 *         for a non-empty array.
 *
 * @note Supports positive, negative, and zero values.
 *       Requires 1 <= k <= size for non-empty arrays.
 *       Does not modify the original array.
 *       Assumes intermediate sums fit within the range of int.
 *
 * @par Key Observation:
 *      Consecutive windows of size k share k - 1 elements.
 *      Therefore, the next window sum can be calculated by
 *      subtracting the outgoing element and adding the incoming one.
 *
 * @par Time Complexity:
 *      Best:  O(1) - Empty array.
 *      Worst: O(n) - Initial window construction and linear traversal.
 *
 * @par Auxiliary Space:
 *      O(1) - Uses a constant number of variables.
 */
int maximum_sum_of_consecutive_elements(const int arr[], int size, int k)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    if (size == 0)
        return 0;

    if (k <= 0 || k > size)
        throw out_of_range("Invalid window size.");

    int maximum = 0;

    // Calculate the sum of the first complete window.
    for (int i = 0; i < k; i++)
        maximum += arr[i];

    int window_sum = maximum;

    // Slide the window by removing the outgoing and adding the incoming element.
    for (int i = k; i < size; i++)
    {
        window_sum += arr[i] - arr[i - k];

        // Preserve the largest window sum encountered so far.
        maximum = max(maximum, window_sum);
    }

    return maximum;
}

/**
 * @brief Checks whether a non-empty contiguous subarray exists
 *        with a sum equal to the given target.
 *
 * Algorithm (Variable-Size Sliding Window):
 * 1. Validate the array size and handle an empty array.
 * 2. Initialize the window boundaries and running sum.
 * 3. Traverse the array, expanding the window from the right.
 * 4. While the window sum exceeds the target:
 *    - Remove the leftmost element from the sum.
 *    - Advance the starting boundary.
 * 5. If a non-empty window matches the target, return true.
 * 6. Return false if no matching subarray is found.
 *
 * @param arr  The input array containing non-negative integers.
 * @param size The number of elements in the array.
 * @param sum  The target subarray sum.
 *
 * @return true if a non-empty contiguous subarray with the
 *         target sum exists; false otherwise.
 *
 * @throws std::out_of_range If size is negative.

 * @par Time Complexity:
 *      Best:  O(1) - Empty array or an immediate match.
 *      Worst: O(n) - Each element enters and leaves the window
 *             at most once.
 *
 * @par Auxiliary Space:
 *      O(1) - Uses a constant number of variables.
 */
bool is_sub_array_with_given_sum_present(const int arr[], int size, int sum)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    if (size <= 0)
        return false;

    int start = 0, end = 0, window_sum = 0;

    for (int i = 0; i < size; i++)
    {
        // Expand the window by including the current element.
        window_sum += arr[i];
        end++;

        // Shrink the window until its sum is within the target.
        while (window_sum > sum && start < end)
        {
            window_sum -= arr[start];
            start++;
        }

        // A matching window must contain at least one element.
        if (start < end && window_sum == sum)
            return true;
    }

    return false;
}

int main()
{
    int arr[] = {1, 8, 30, -5, 20, 7};
    int size = sizeof arr / sizeof arr[0];
    int k = 3;

    cout << "Array: ";
    print_array(arr, size);

    cout << "Maximum sum of " << k << " consecutive elements is "
         << maximum_sum_of_consecutive_elements(arr, size, k) << "." << endl;

    int sum = 45;

    cout << "Is sub-array with sum of " << sum << " present? "
         << (is_sub_array_with_given_sum_present(arr, size, sum) ? "yes" : "no") << endl;

    return 0;
}
