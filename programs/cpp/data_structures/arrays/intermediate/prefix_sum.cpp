#include <iostream>
#include <vector>
#include <stdexcept>
#include "../../../include/arrays/print.h"

using namespace std;

/**
 * @brief Constructs a prefix sum array for efficient range sum queries.
 *
 * Algorithm (Prefix Sum):
 * 1. Validate the array size and handle an empty array.
 * 2. Initialize the first prefix sum with the first array element.
 * 3. Traverse the remaining elements, adding each element to
 *    the preceding prefix sum.
 * 4. Return the constructed prefix sum array.
 *
 * @param arr  The input array containing integer elements.
 * @param size The number of elements in the array.
 *
 * @return A vector where prefix_sum[i] represents the sum of
 *         elements from index 0 through i (inclusive).
 *         Returns an empty vector for an empty array.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Supports positive, negative, and zero values.
 *       Does not modify the original array.
 *       Requires a valid input array containing at least size elements.
 *       Assumes intermediate sums fit within the range of int.
 *
 * @par Time Complexity:
 *      Best:  O(1) - Empty array.
 *      Worst: O(n) - Single linear traversal.
 *
 * @par Auxiliary Space:
 *      O(n) - Stores n prefix sums in an auxiliary vector.
 */
vector<int> get_prefix_sum(const int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    if (size == 0)
        return {};

    vector<int> prefix_sum(size);

    // The first prefix sum equals the first element.
    prefix_sum[0] = arr[0];

    // Build cumulative sums from left to right.
    for (int i = 1; i < size; i++)
        prefix_sum[i] = prefix_sum[i - 1] + arr[i];

    return prefix_sum;
}

/**
 * @brief Calculates the sum of elements within an inclusive index range
 *        using a precomputed prefix sum array.
 *
 * Algorithm (Range Sum Query):
 * 1. Validate the starting and ending indices.
 * 2. If start is 0, return the prefix sum at end.
 * 3. Otherwise, subtract the prefix sum preceding start
 *    from the prefix sum at end.
 *
 * @param prefix_sum The precomputed prefix sum array, passed by const reference.
 * @param start      The inclusive starting index of the subarray.
 * @param end        The inclusive ending index of the subarray.
 *
 * @return The sum of elements in the range [start, end].
 *
 * @throws std::out_of_range If the requested range is invalid.
 *
 * @note Requires a correctly constructed prefix sum array.
 *       Uses zero-based inclusive indices.
 *       Does not modify the prefix sum array.
 *       Assumes the result fits within the range of int.
 *
 * @par Formula:
 *      sum(start, end) = prefix_sum[end] - prefix_sum[start - 1]
 *
 *      When start == 0:
 *      sum(0, end) = prefix_sum[end]
 *
 * @par Time Complexity:
 *      O(1) - Direct lookup and subtraction.
 *
 * @par Auxiliary Space:
 *      O(1) - No additional data structures or vector copies.
 */
int get_sum(const vector<int> &prefix_sum, int start, int end)
{
    if (start < 0 || start > end ||
        static_cast<size_t>(end) >= prefix_sum.size())
        throw out_of_range("Invalid subarray range.");

    // No preceding prefix sum exists when the range starts at index 0.
    if (start == 0)
        return prefix_sum[end];

    // Remove the sum of elements preceding the requested range.
    return prefix_sum[end] - prefix_sum[start - 1];
}

int main()
{
    int arr[] = {2, 8, 3, 9, 6, 5, 4, 8};
    int size = sizeof arr / sizeof arr[0];

    cout << "Array: ";
    print_array(arr, size);

    vector<int> prefix_sum = get_prefix_sum(arr, size);

    cout << "Sum of index 2 to 5: " << get_sum(prefix_sum, 2, 5) << endl;
    cout << "Sum of index 1 to 3: " << get_sum(prefix_sum, 1, 3) << endl;
    cout << "Sum of index 4 to 7: " << get_sum(prefix_sum, 4, 7) << endl;

    return 0;
}
