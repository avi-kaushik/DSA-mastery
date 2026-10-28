#include <algorithm>
#include <iostream>
#include <stdexcept>
#include "../../../include/arrays/print.h"

using namespace std;

/**
 * @brief Finds the maximum difference between two array elements
 *        using a single traversal with a running minimum.
 *
 * Algorithm:
 * 1. Validate that the array size is not negative.
 * 2. If the array contains fewer than two elements, return 0.
 * 3. Initialize:
 *    - difference with the difference between the second and
 *      first elements.
 *    - smallest with the first array element.
 * 4. Traverse the array from index 1 to size - 1.
 * 5. For each element:
 *    - Calculate the difference between the current element
 *      and the smallest element encountered before it.
 *    - Update the maximum difference if the current difference
 *      is greater than the previously recorded difference.
 *    - Update the smallest element encountered so far.
 * 6. Return the maximum difference after completing the traversal.
 *
 * @param arr   The array in which the maximum difference is calculated.
 * @param size  The current number of elements in the array.
 *
 * @return The maximum difference arr[j] - arr[i], where i < j.
 *         Returns 0 if the array contains fewer than two elements.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note The first element must appear before the second element
 *       when calculating the difference (i < j).
 *       Supports positive, negative, and zero differences.
 *       Works on both sorted and unsorted arrays.
 *       Does not modify the original array.
 *       Requires a valid array containing at least size elements.
 *       Assumes integer subtraction does not overflow.
 *
 * Time Complexity:
 * - Best:  O(1) - Array contains fewer than two elements.
 * - Worst: O(n) - Every element is examined once.
 *
 * Auxiliary Space: O(1)
 */
int get_maximum_difference(int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    // At least two elements are required to calculate a difference.
    if (size <= 1)
        return 0;

    // Initialize the maximum difference using the first valid pair.
    // Track the smallest element encountered so far.
    int difference = arr[1] - arr[0], smallest = arr[0];

    for (int i = 1; i < size; i++)
    {
        // Calculate the maximum difference using an earlier minimum.
        difference = max(difference, arr[i] - smallest);

        // Update the running minimum for subsequent iterations.
        smallest = min(smallest, arr[i]);
    }

    return difference;
}

int main()
{
    int arr[] = {2, 4, 10, 5, 4, 1};
    int size = sizeof arr / sizeof arr[0];

    cout << "Array: ";
    print_array(arr, size);

    cout << "Maximum difference in elements in the given array is "
         << get_maximum_difference(arr, size) << "." << endl;

    return 0;
}
