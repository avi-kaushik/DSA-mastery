#include <iostream>
#include <stdexcept>
#include <vector>
#include "../../include/arrays/print.h"

using namespace std;

/**
 * @brief Left-rotates an array by one position in-place
 *        using sequential element shifting.
 *
 * Algorithm:
 * 1. Validate that the array size is not negative.
 * 2. If the array contains zero or one element, return immediately.
 * 3. Store the first element in a temporary variable.
 * 4. Traverse the array from index 0 to size - 2.
 * 5. Shift each subsequent element one position to the left.
 * 6. Place the original first element at the last index.
 *
 * @param arr   The array to left-rotate.
 * @param size  The current number of elements in the array.
 *
 * @return void Modifies the original array directly.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Performs a circular left rotation by exactly one position.
 *       Preserves all elements and their relative cyclic order.
 *       Modifies the original array without additional storage.
 *       Works on both sorted and unsorted arrays.
 *       Handles empty, single-element, and duplicate-filled arrays.
 *       Requires a valid array containing at least size elements.
 *
 * Time Complexity:
 * - Best:  O(1) - Array contains zero or one element.
 * - Worst: O(n) - Shifts n - 1 elements.
 *
 * Auxiliary Space: O(1)
 */
void left_rotate_by_one(int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    // Empty and single-element arrays remain unchanged.
    if (size <= 1)
        return;

    // Preserve the first element before it gets overwritten.
    int first = arr[0];

    // Shift each remaining element one position to the left.
    for (int i = 0; i < size - 1; i++)
        arr[i] = arr[i + 1];

    // Move the original first element to the last position.
    arr[size - 1] = first;
}

/**
 * @brief Left-rotates an array by K positions using a temporary array.
 *
 * Algorithm:
 * 1. Validate that the array size and rotation count are non-negative.
 * 2. If the array contains zero or one element, return immediately.
 * 3. Normalize the rotation count using k = rotate % size.
 * 4. If k is zero, return because no rotation is required.
 * 5. Allocate a temporary array of size k.
 * 6. Copy the first k elements of the original array into
 *    the temporary array.
 * 7. Shift the remaining elements k positions to the left.
 * 8. Copy the temporarily stored elements back into the last
 *    k positions of the original array, preserving their order.
 *
 * @param arr     The array to left-rotate.
 * @param size    The current number of elements in the array.
 * @param rotate  The number of positions to rotate left.
 *
 * @return void Modifies the original array directly.
 *
 * @throws std::out_of_range If size or rotate is negative.
 * @throws std::bad_alloc If temporary storage allocation fails.
 *
 * @note Performs a circular left rotation by K positions.
 *       Normalizes rotations greater than or equal to the array size.
 *       Preserves all elements and their relative cyclic order.
 *       Modifies the original array using temporary storage.
 *       Works on both sorted and unsorted arrays.
 *       Handles empty, single-element, and duplicate-filled arrays.
 *       Requires a valid array containing at least size elements.
 *
 * Time Complexity:
 * - Best:  O(1) - Array size <= 1 or normalized rotation is zero.
 * - Worst: O(n) - Copies and shifts array elements.
 *
 * Auxiliary Space: O(k) - Temporary storage for k elements,
 *                         where k = rotate % size.
 */
void left_rotate(int arr[], int size, int rotate)
{
    if (size < 0 || rotate < 0)
        throw out_of_range("Array size and rotation count must be non-negative.");

    // Empty and single-element arrays remain unchanged.
    if (size <= 1)
        return;

    // Normalize rotations to avoid unnecessary full cycles.
    int k = rotate % size;

    if (k == 0)
        return;

    // Preserve the first k elements before they are overwritten.
    vector<int> temp(k);

    for (int i = 0; i < k; i++)
        temp[i] = arr[i];

    // Shift the remaining elements k positions to the left.
    for (int i = 0; i < size - k; i++)
        arr[i] = arr[i + k];

    // The last k positions will hold the preserved elements.
    int remaining = size - k;

    // Map each destination index to its corresponding temporary index.
    for (int i = remaining; i < size; i++)
        arr[i] = temp[i - remaining];
}

int main()
{
    int arr[] = {19, 16, 42, 12, 28};
    int size = sizeof arr / sizeof arr[0];

    cout << "Array: ";
    print_array(arr, size);

    left_rotate_by_one(arr, size);

    cout << "Array after Left-rotate: ";
    print_array(arr, size);

    left_rotate(arr, size, 2);
    cout << "Array after Left-rotate by 2: ";
    print_array(arr, size);

    return 0;
}
