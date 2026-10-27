#ifndef BASIC_OPERATIONS_H
#define BASIC_OPERATIONS_H

#include <stdexcept>

/**
 * @brief Reverses an array or a specified portion of it in-place
 *        using the two-pointer technique.
 *
 * Algorithm:
 * 1. Validate the starting index and array size.
 * 2. Initialize two pointers:
 *    - left at the specified starting index (default 0).
 *    - right at the last index (size - 1).
 * 3. While the left pointer is smaller than the right pointer:
 *    - Swap the elements at the left and right indices.
 *    - Increment the left pointer.
 *    - Decrement the right pointer.
 * 4. Stop when the pointers meet or cross.
 *
 * @param arr    The array containing the elements to reverse.
 * @param size   The exclusive ending index of the reversal range.
 * @param start  The starting index (inclusive), defaults to 0.
 *
 * @return void Modifies the original array directly.
 *
 * @throws std::out_of_range If size is negative, start is negative,
 *                           or start exceeds size.
 *
 * @note Reverses elements within the range [start, size).
 *       By default, reverses the entire array when start = 0
 *       and size equals the array length.
 *       Uses the two-pointer technique.
 *       Handles empty and single-element ranges.
 *       Does not allocate additional storage.
 *       Requires a valid array containing at least size elements.
 *
 * Time Complexity:
 * - Best:  O(1) - Range contains zero or one element.
 * - Worst: O(m) - Where m = size - start.
 *
 * Auxiliary Space: O(1)
 */
inline void reverse(int arr[], int size, int start = 0)
{
    if (size < 0 || start < 0 || start > size)
        throw std::out_of_range("Invalid reversal range.");

    // Initialize pointers at opposite ends of the selected range.
    int left = start, right = size - 1;

    while (left < right)
    {
        // Swap elements at the left and right indices.
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;

        // Move both pointers inward.
        left++;
        right--;
    }
}

#endif // BASIC_OPERATIONS_H
