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

int main()
{
    int arr[] = {1, 8, 30, -5, 20, 7};
    int size = sizeof arr / sizeof arr[0];
    int k = 3;

    cout << "Array: ";
    print_array(arr, size);

    cout << "Maximum sum of " << k << " consecutive elements is "
         << maximum_sum_of_consecutive_elements(arr, size, k) << "." << endl;

    return 0;
}
