#include <algorithm>
#include <iostream>
#include <stdexcept>
#include <vector>
#include "../../../include/arrays/print.h"

using namespace std;

/**
 * @brief Calculates the total units of rainwater trapped between elevation bars
 *        using the Prefix and Suffix Maximum approach.
 *
 * @details
 * Uses precomputed left and right maximum heights to determine how much
 * rainwater can be trapped above each bar.
 *
 * The water level at any index is limited by the shorter of the tallest
 * boundaries available on its left and right.
 *
 * Formula:
 *     trapped_water[i] = min(left_max[i], right_max[i]) - arr[i]
 *
 * Algorithm (Prefix and Suffix Maximum):
 * 1. Validate that the array size is not negative.
 * 2. Return 0 if the array contains fewer than three bars.
 * 3. Initialize two auxiliary arrays:
 *    - left_max[i]:  Maximum height from index 0 through i.
 *    - right_max[i]: Maximum height from index i through size - 1.
 * 4. Traverse from left to right to populate the left_max array
 *    by maintaining the maximum height encountered so far.
 * 5. Traverse from right to left to populate the right_max array
 *    by maintaining the maximum height encountered so far.
 * 6. Traverse the original array and calculate trapped water at each index:
 *    - Determine the possible water level using the smaller boundary.
 *    - Subtract the current bar's height from that water level.
 *    - Add the resulting trapped water to the total.
 * 7. Return the total trapped units of rainwater.
 *
 * @param arr  The array representing non-negative elevation heights.
 * @param size The number of elevation bars in the array.
 *
 * @return The total units of rainwater trapped between the elevation bars.
 *         Returns 0 if the array contains fewer than three elements.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Assumes all elevation heights are non-negative integers.
 *       Requires a valid array containing at least size elements.
 *       At least three bars are required to trap rainwater.
 *       The left and right maximum arrays include the current bar.
 *       Consequently, the calculated trapped water is never negative.
 *       Does not modify the original array.
 *       Assumes the total trapped water fits within the range of int.
 *
 * @par Key Observation:
 *      The amount of water trapped above a bar depends on the shorter
 *      of the maximum boundaries available on its left and right.
 *
 *      A taller boundary on only one side cannot retain additional water
 *      if the opposite boundary is shorter.
 *
 * @par Invariants:
 *      - After processing index i in the left-to-right traversal,
 *        left_max[i] stores the maximum height in arr[0...i].
 *
 *      - After processing index i in the right-to-left traversal,
 *        right_max[i] stores the maximum height in arr[i...size-1].
 *
 * @par Time Complexity:
 *      Best:  O(1) - Array contains fewer than three elements.
 *      Worst: O(n) - Three linear traversals of the array.
 *
 * @par Auxiliary Space:
 *      O(n) - Two auxiliary arrays of size n.
 */
int get_trapped_units(const int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    // At least three bars are required to trap rainwater.
    if (size <= 2)
        return 0;

    // Store the maximum heights encountered from both directions.
    vector<int> left_max(size), right_max(size);

    int left_max_pointer = arr[0];
    left_max[0] = arr[0];

    // Build the prefix maximum array from left to right.
    for (int i = 1; i < size; i++)
    {
        left_max_pointer = max(arr[i], left_max_pointer);
        left_max[i] = left_max_pointer;
    }

    int right_max_pointer = arr[size - 1];
    right_max[size - 1] = arr[size - 1];

    // Build the suffix maximum array from right to left.
    for (int i = size - 2; i >= 0; i--)
    {
        right_max_pointer = max(arr[i], right_max_pointer);
        right_max[i] = right_max_pointer;
    }

    int total = 0;

    // Calculate the trapped rainwater at each index.
    for (int i = 0; i < size; i++)
    {
        // The shorter boundary determines the maximum water level.
        int possible_trapped_height = min(left_max[i], right_max[i]);

        // Subtract the bar's height to obtain the trapped water.
        total += possible_trapped_height - arr[i];
    }

    return total;
}

int main()
{
    int arr[] = {3, 0, 1, 2, 5};
    int size = sizeof arr / sizeof arr[0];

    cout << "Array: ";
    print_array(arr, size);

    cout << "Total Possible trapped units: "
         << get_trapped_units(arr, size) << endl;

    return 0;
}
