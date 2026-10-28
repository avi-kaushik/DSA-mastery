
#include <iostream>
#include <stdexcept>
#include "../../../include/arrays/print.h"

using namespace std;

/**
 * @brief Moves all zero elements to the end of an array in-place
 *        using the read/write two-pointer technique.
 *
 * Algorithm:
 * 1. Validate that the array size is not negative.
 * 2. Initialize a write pointer at index 0.
 * 3. Traverse the array using a read pointer from index 0
 *    to size - 1.
 * 4. For each non-zero element:
 *    - Copy it to the current write pointer position.
 *    - Increment the write pointer.
 * 5. Traverse the remaining array positions from the write
 *    pointer to size - 1.
 * 6. Fill each remaining position with zero.
 *
 * @param arr   The array whose zero elements are to be moved.
 * @param size  The current number of elements in the array.
 *
 * @return void Modifies the original array directly.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Preserves the relative order of non-zero elements.
 *       Moves all zeroes to the end without changing the array size.
 *       Modifies the original array without additional storage.
 *       Works on both sorted and unsorted arrays.
 *       Handles empty, single-element, and all-zero arrays.
 *       Requires a valid array containing at least size elements.
 *
 * Time Complexity:
 * - Best:  O(n) - First traversal examines all n elements.
 * - Worst: O(n) - Both traversals perform at most 2n iterations.
 *
 * Auxiliary Space: O(1)
 */
void move_zeros(int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    // Tracks the next position for a non-zero element.
    int pointer = 0;

    // Phase 1: Compact non-zero elements at the beginning.
    for (int i = 0; i < size; i++)
    {
        if (arr[i] != 0)
        {
            // The write pointer never exceeds the read pointer,
            // so unread elements are not overwritten.
            arr[pointer] = arr[i];
            pointer++;
        }
    }

    // Phase 2: Fill the remaining positions with zeroes.
    for (int i = pointer; i < size; i++)
        arr[i] = 0;
}

int main()
{
    int arr[] = {10, 0, 20, 0, 30};
    int size = sizeof arr / sizeof arr[0];

    cout << "Original Array: ";
    print_array(arr, size);

    move_zeros(arr, size);

    cout << "Array after moving zeros to the end: ";
    print_array(arr, size);

    return 0;
}
