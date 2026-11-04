#include <iostream>
#include <stdexcept>
#include "../../../include/arrays/print.h"

using namespace std;

/**
 * @brief Calculates the sum of all elements in an array.
 *
 * Algorithm (Linear Traversal):
 * 1. Initialize the total sum to 0.
 * 2. Traverse the array and accumulate each element.
 * 3. Return the total sum.
 *
 * @param arr  The input array containing integer elements.
 * @param size The number of elements in the array.
 *
 * @return The sum of all array elements, or 0 for an empty array.
 *
 * @note Requires size >= 0 and a valid array containing at least
 *       size elements. Assumes the total fits within int.
 *
 * @par Time Complexity:
 *      O(n) - Single traversal of the array.
 *
 * @par Auxiliary Space:
 *      O(1) - Uses one accumulator.
 */
int get_total_sum(const int arr[], int size)
{
    int total = 0;

    for (int i = 0; i < size; i++)
        total += arr[i];

    return total;
}

/**
 * @brief Checks whether an equilibrium index exists in an array.
 *
 * @details
 * Uses the Total Sum + Running Prefix Sum approach to determine
 * whether any index has equal sums on its left and right sides.
 * The element at the equilibrium index is excluded from both sums.
 *
 * Algorithm (Total Sum + Running Prefix Sum):
 * 1. Validate the array size and handle an empty array.
 * 2. Calculate the total sum of all elements.
 * 3. Initialize the running prefix sum to 0.
 * 4. Traverse the array from left to right.
 * 5. For each index:
 *    - Calculate the suffix sum by subtracting the prefix sum
 *      and current element from the total sum.
 *    - Return true if the prefix and suffix sums are equal.
 *    - Otherwise, add the current element to the prefix sum.
 * 6. Return false if no equilibrium index is found.
 *
 * @param arr  The input array containing integer elements.
 * @param size The number of elements in the array.
 *
 * @return true if an equilibrium index exists; false otherwise.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Supports positive, negative, and zero values.
 *       An equilibrium index can be the first or last index.
 *       Does not modify the original array.
 *       Requires a valid array containing at least size elements.
 *       Assumes intermediate sums fit within the range of int.
 *
 * @par Key Observation:
 *      Total Sum = Left Sum + arr[i] + Right Sum
 *
 *      Therefore:
 *      Right Sum = Total Sum - Left Sum - arr[i]
 *
 *      This eliminates the need for prefix and suffix arrays.
 *
 * @par Loop Invariant:
 *      Before processing index i, prefix_sum contains the sum
 *      of all elements strictly before index i.
 *
 * @par Time Complexity:
 *      Best:  O(n) - Calculating the total sum requires a full traversal.
 *      Worst: O(n) - Two linear traversals.
 *
 * @par Auxiliary Space:
 *      O(1) - Uses a constant number of variables.
 */
bool is_equilibrium_present(const int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    if (size == 0)
        return false;

    int total_sum = get_total_sum(arr, size);
    int prefix_sum = 0;

    for (int i = 0; i < size; i++)
    {
        // Calculate the sum strictly to the right of the current index.
        int suffix_sum = total_sum - prefix_sum - arr[i];

        // An equilibrium index has equal left and right sums.
        if (prefix_sum == suffix_sum)
            return true;

        // Include the current element in the next index's left sum.
        prefix_sum += arr[i];
    }

    return false;
}

int main()
{
    int arr[] = {4, 2, -2};
    int size = sizeof arr / sizeof arr[0];

    cout << "Array: ";
    print_array(arr, size);

    cout << "Is equilibrium present in the given array? "
         << (is_equilibrium_present(arr, size) ? "Yes" : "No")
         << endl;

    return 0;
}
