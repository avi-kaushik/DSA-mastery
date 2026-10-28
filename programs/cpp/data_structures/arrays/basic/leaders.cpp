#include <iostream>
#include <stdexcept>
#include "../../../include/arrays/print.h"

using namespace std;

/**
 * @brief Finds and prints all leader elements of an array using
 *        right-to-left traversal with a running maximum.
 *
 * Algorithm:
 * 1. Validate that the array size is not negative.
 * 2. If the array is empty, return immediately.
 * 3. Initialize the largest element with the last array element,
 *    which is always considered a leader.
 * 4. Print the last element and traverse the remaining elements
 *    from index size - 2 to 0.
 * 5. For each element:
 *    - Compare it with the largest element encountered to its right.
 *    - If the current element is greater than or equal to the
 *      largest, it is a leader.
 *    - Update the largest value and print the leader.
 * 6. Print a newline after completing the traversal.
 *
 * @param arr   The array in which leader elements are identified.
 * @param size  The current number of elements in the array.
 *
 * @return void Prints the leader elements directly to standard output.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note An element is considered a leader if it is greater than
 *       or equal to every element to its right.
 *       The rightmost element is always a leader.
 *       Prints leaders in reverse order (right to left).
 *       Handles duplicate values, negative numbers, and INT_MIN.
 *       Does not modify the original array.
 *       Requires a valid array containing at least size elements.
 *
 * Time Complexity:
 * - Best:  O(n) - Every element is examined once.
 * - Worst: O(n) - Every element is examined once.
 *
 * Auxiliary Space: O(1)
 */
void print_leaders(const int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    // An empty array has no leaders.
    if (size == 0)
        return;

    // The rightmost element is always a leader.
    int largest = arr[size - 1];
    cout << largest << " ";

    // Traverse from right to left, maintaining the suffix maximum.
    for (int i = size - 2; i >= 0; i--)
    {
        // A leader must be >= every element to its right.
        if (arr[i] >= largest)
        {
            largest = arr[i];
            cout << largest << " ";
        }
    }

    cout << endl;
}

int main()
{
    int arr[] = {4, 18, 4, 8, 6, 4, 2};
    int size = sizeof arr / sizeof arr[0];

    cout << "Array: ";
    print_array(arr, size);

    cout << "Leaders in this array: ";
    print_leaders(arr, size);

    return 0;
}
