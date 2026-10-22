#include <iostream>
#include <stdexcept>
#include "../../include/arrays/print.h"

using namespace std;

/**
 * @brief Removes duplicate elements from a sorted array in-place
 *        using the two-pointer technique.
 *
 * Algorithm:
 * 1. Validate the array size.
 * 2. Return immediately for arrays containing at most one element.
 * 3. Initialize a write pointer at index 1.
 * 4. Traverse the array from index 1 using a read pointer.
 * 5. Copy each newly encountered distinct element to the
 *    write position and advance the write pointer.
 * 6. Return the updated logical array size.
 *
 * @param arr   The sorted array from which duplicates are removed.
 * @param size  The current number of elements in the array.
 *
 * @return The number of distinct elements remaining.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Requires the array to be sorted in non-decreasing order.
 *       Modifies the original array while preserving order.
 *       Elements beyond the returned size are irrelevant.
 *
 * Time Complexity:
 * - Best:  O(1) - Array contains at most one element.
 * - Worst: O(n) - Every element is examined once.
 *
 * Auxiliary Space: O(1)
 */
int remove_duplicates(int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    if (size <= 1)
        return size;

    // Tracks the next position for a distinct element.
    // The first element is already considered unique.
    int pointer = 1;

    // Read each element and identify new distinct values.
    for (int i = 1; i < size; i++)
    {
        // In a sorted array, duplicates are adjacent.
        if (arr[i] != arr[i - 1])
        {
            arr[pointer] = arr[i];
            pointer++;
        }
    }

    // The write pointer equals the number of unique elements.
    return pointer;
}

/**
 * @brief Removes excess duplicate elements from a sorted array
 *        in-place, retaining at most K occurrences of each
 *        distinct value using the read/write two-pointer technique.
 *
 * Algorithm:
 * 1. Validate that the array size and occurrence limit are
 *    non-negative.
 * 2. If the array is empty or the occurrence limit is zero,
 *    return 0.
 * 3. Initialize a write pointer at index 1, retaining the first
 *    element, and an occurrence counter to 1.
 * 4. Traverse the array using a read pointer from index 1
 *    to size - 1.
 * 5. For each element:
 *    - If it equals the previous element, increment the
 *      occurrence counter.
 *    - Otherwise, reset the occurrence counter to 1.
 *    - If the occurrence count does not exceed the limit,
 *      copy the element to the write position and advance
 *      the write pointer.
 * 6. Return the write pointer as the updated logical array size.
 *
 * @param arr               The sorted array to modify.
 * @param size              The current number of elements.
 * @param occurrence_limit  Maximum allowed occurrences of each
 *                          distinct value.
 *
 * @return The updated logical size containing at most K
 *         occurrences of each distinct element.
 *
 * @throws std::out_of_range If size or occurrence_limit is negative.
 *
 * @note Requires the input array to be sorted in non-decreasing order.
 *       Works for any non-negative occurrence limit, including zero.
 *       Modifies the original array while preserving relative order.
 *       Handles empty, single-element, and all-duplicate arrays.
 *       Elements beyond the returned logical size are irrelevant.
 *       Requires a valid array containing at least size elements.
 *
 * Time Complexity:
 * - Best:  O(1) - Array is empty or occurrence limit is zero.
 * - Worst: O(n) - Every element is examined once.
 *
 * Auxiliary Space: O(1)
 */
int remove_duplicates_at_most_k(int arr[], int size, int occurrence_limit)
{
    if (size < 0 || occurrence_limit < 0)
        throw out_of_range("Array size and occurrence limit must be non-negative.");

    // No elements can be retained if the array is empty or K is zero.
    if (size == 0 || occurrence_limit == 0)
        return 0;

    // The first element is retained.
    // pointer tracks the next available write position.
    int pointer = 1, occurrence_counter = 1;

    // Traverse the original elements to count consecutive occurrences.
    for (int i = 1; i < size; i++)
    {
        // Sorted elements with equal values appear consecutively.
        if (arr[i] == arr[i - 1])
            occurrence_counter++;
        else
            occurrence_counter = 1;

        // Retain only the first K occurrences of each distinct value.
        if (occurrence_counter <= occurrence_limit)
        {
            arr[pointer] = arr[i];
            pointer++;
        }
    }

    // The write pointer equals the total number of retained elements.
    return pointer;
}

/**
 * @brief Removes all occurrences of a specified value from an array
 *        in-place using the read/write two-pointer technique.
 *
 * Algorithm:
 * 1. Validate that the array size is not negative.
 * 2. Initialize a write pointer at index 0.
 * 3. Traverse the array using a read pointer from index 0
 *    to size - 1.
 * 4. For each element that differs from the target value:
 *    - Copy it to the current write pointer position.
 *    - Increment the write pointer.
 * 5. Return the write pointer as the updated logical array size.
 *
 * @param arr    The array from which the values are removed.
 * @param size   The current number of elements in the array.
 * @param value  The target value to remove.
 *
 * @return The updated logical size after removing all occurrences.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Works on both sorted and unsorted arrays.
 *       Removes all occurrences of the target value.
 *       Modifies the original array while preserving relative order.
 *       Elements beyond the returned size are irrelevant.
 *       Requires a valid array containing at least size elements.
 *
 * Time Complexity:
 * - Best:  O(1) - Array is empty.
 * - Worst: O(n) - Every element is examined once.
 *
 * Auxiliary Space: O(1)
 */
int remove_values(int arr[], int size, int value)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    // Tracks the next position for an element to retain.
    int pointer = 0;

    // Read every element and retain values other than the target.
    for (int i = 0; i < size; i++)
    {
        if (arr[i] != value)
        {
            arr[pointer] = arr[i];
            pointer++;
        }
    }

    // The write pointer equals the number of retained elements.
    return pointer;
}

int main()
{
    int arr[] = {10, 20, 20, 30, 30};
    int size = sizeof arr / sizeof arr[0];

    cout << "Original Array: ";
    print_array(arr, size);

    size = remove_duplicates(arr, size);

    cout << "Array with removed duplicates: ";
    print_array(arr, size);

    cout << "Array with removed 20: ";
    size = remove_values(arr, size, 20);
    print_array(arr, size);

    int arr2[] = {10, 10, 10, 20, 20, 30, 30, 30, 30, 40, 40, 40, 40, 40};
    int size2 = sizeof arr2 / sizeof arr2[0];

    cout << "Updated Array: ";
    print_array(arr2, size2);

    size2 = remove_duplicates_at_most_k(arr2, size2, 2);

    cout << "Array with at most " << 2 << " elements: ";
    print_array(arr2, size2);

    return 0;
}
