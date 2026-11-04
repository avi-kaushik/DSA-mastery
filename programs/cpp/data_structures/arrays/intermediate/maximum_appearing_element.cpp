#include <algorithm>
#include <climits>
#include <iostream>
#include <stdexcept>
#include <vector>
#include "../../../include/arrays/print.h"

using namespace std;

/**
 * @brief Finds the maximum element in an array.
 *
 * Algorithm (Linear Traversal):
 * 1. Initialize the maximum to INT_MIN.
 * 2. Traverse the array and update the maximum whenever a larger element is found.
 * 3. Return the maximum value.
 *
 * @param arr  The input array.
 * @param size The number of elements in the array.
 *
 * @return The maximum element, or INT_MIN if the array is empty.
 *
 * @note Assumes a valid array with size >= 0.
 *
 * @par Time Complexity:
 *      O(n) - Single traversal.
 *
 * @par Auxiliary Space:
 *      O(1) - Uses a single variable.
 */
int get_maximum_value(const int arr[], int size)
{
    int maximum = INT_MIN;

    for (int i = 0; i < size; i++)
        maximum = max(maximum, arr[i]);

    return maximum;
}

/**
 * @brief Constructs a prefix sum array from an input vector.
 *
 * Algorithm (Prefix Sum):
 * 1. Handle an empty vector.
 * 2. Initialize the first prefix sum with the first element.
 * 3. Traverse the remaining elements, adding each element to
 *    the preceding prefix sum.
 * 4. Return the prefix sum array.
 *
 * @param arr The input vector, passed by constant reference.
 *
 * @return A vector where prefix_sum[i] is the cumulative sum
 *         of elements from index 0 through i (inclusive).
 *
 * @note Does not modify the input vector.
 *       Assumes cumulative sums fit within int.
 *
 * @par Time Complexity:
 *      Best:  O(1) - Empty vector.
 *      Worst: O(n) - Single traversal.
 *
 * @par Auxiliary Space:
 *      O(n) - Stores n prefix sums.
 */
vector<int> get_prefix_sum(const vector<int> &arr)
{
    int size = arr.size();

    if (size == 0)
        return {};

    vector<int> prefix_sum(size);

    // Initialize the first cumulative sum.
    prefix_sum[0] = arr[0];

    // Accumulate values from left to right.
    for (int i = 1; i < size; i++)
        prefix_sum[i] = prefix_sum[i - 1] + arr[i];

    return prefix_sum;
}

/**
 * @brief Finds the smallest integer appearing in the maximum number
 *        of given inclusive ranges.
 *
 * Algorithm (Difference Array + Prefix Sum):
 * 1. Validate the array size and handle empty input.
 * 2. Find the maximum right endpoint to determine the frequency array size.
 * 3. Construct a difference array:
 *    - Increment frequency[left[i]] to mark the beginning of a range.
 *    - Decrement frequency[right[i] + 1] to mark where it stops contributing.
 * 4. Compute prefix sums to obtain the coverage frequency of each integer.
 * 5. Find the index with the highest frequency.
 * 6. Return that index as the maximum appearing element.
 *
 * @param left  Array containing the inclusive starting points of ranges.
 * @param right Array containing the inclusive ending points of ranges.
 * @param size  Number of ranges.
 *
 * @return The smallest integer appearing in the maximum number of ranges,
 *         or 0 if no ranges are provided.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Assumes valid ranges: 0 <= left[i] <= right[i].
 *       Assumes endpoints are sufficiently bounded for direct indexing.
 *       Assumes frequency_limit and coverage counts fit within int.
 *       Does not modify the input arrays.
 *       Uses strict comparison (>) to return the smallest element on ties.
 *
 * @par Key Observation:
 *      For each inclusive range [L, R]:
 *
 *      difference[L]     += 1
 *      difference[R + 1] -= 1
 *
 *      Taking the prefix sum of the difference array gives
 *      the number of ranges covering each integer.
 *
 * @par Time Complexity:
 *      O(n + M), where n is the number of ranges and
 *      M is the maximum right endpoint.
 *
 * @par Auxiliary Space:
 *      O(M) - Stores the difference and prefix sum arrays.
 */
int get_maximum_appearing_element(const int left[], const int right[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    if (size == 0)
        return 0;

    int frequency_limit = get_maximum_value(right, size) + 2;

    vector<int> frequency(frequency_limit, 0);

    // Mark where each inclusive range begins and stops contributing.
    for (int i = 0; i < size; i++)
    {
        frequency[left[i]]++;
        frequency[right[i] + 1]--;
    }

    // Convert difference values into actual coverage frequencies.
    vector<int> prefix_sum = get_prefix_sum(frequency);

    int maximum_appearing_element = 0;

    // Strict comparison preserves the smallest element in case of ties.
    for (int i = 1; i < frequency_limit; i++)
    {
        if (prefix_sum[i] > prefix_sum[maximum_appearing_element])
            maximum_appearing_element = i;
    }

    return maximum_appearing_element;
}

int main()
{
    int left[] = {1, 2, 5, 15, 18, 20, 22};
    int right[] = {5, 8, 7, 30, 25, 24, 24};

    int size = sizeof left / sizeof left[0];

    cout << "Left Array: ";
    print_array(left, size);

    cout << "Right Array: ";
    print_array(right, size);

    cout << "Maximum appearing element in the given left-right ranges is "
         << get_maximum_appearing_element(left, right, size) << "." << endl;

    return 0;
}
