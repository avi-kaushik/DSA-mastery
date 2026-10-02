#include <algorithm>
#include <iostream>
#include "../../include/arrays/print.h"

using namespace std;

/**
 * @brief Finds the first occurrence of a target value in an array using recursion.
 *
 * The function starts searching from the beginning of the array and
 * recursively checks each element until the target value is found.
 *
 * @param arr    The array in which the target value will be searched.
 * @param size   The number of elements in the array.
 * @param target The value to search for.
 * @param index  The current index being checked.
 *
 * @return The index of the first occurrence if found, otherwise -1.
 *
 * @complexity
 * Time: O(n)
 * Space: O(n) due to recursive call stack.
 */
int first_occurrence(int arr[], int size, int target, int index = 0)
{
    // Base case: reached the end of the array.
    if (index >= size)
        return -1;

    // Return the current index if the target is found.
    if (arr[index] == target)
        return index;

    // Continue searching in the remaining array.
    return first_occurrence(arr, size, target, index + 1);
}

/**
 * @brief Recursively searches for the last occurrence of a target value
 *        from right to left.
 *
 * @param arr    The array in which the target value will be searched.
 * @param target The value to search for.
 * @param index  The current index being checked.
 *
 * @return The index of the last occurrence if found, otherwise -1.
 *
 * @complexity
 * Time: O(n)
 * Space: O(n) due to recursive call stack.
 */
int last_occurrence_recursive(int arr[], int target, int index)
{
    // Base case: moved before the first index of the array.
    if (index < 0)
        return -1;

    // Return the current index if the target is found.
    if (arr[index] == target)
        return index;

    // Continue searching toward the beginning of the array.
    return last_occurrence_recursive(arr, target, index - 1);
}

/**
 * @brief Finds the last occurrence of a target value in an array.
 *
 * This wrapper function initializes the recursive search from the
 * last index of the array.
 *
 * @param arr    The array in which the target value will be searched.
 * @param size   The number of elements in the array.
 * @param target The value to search for.
 *
 * @return The index of the last occurrence if found, otherwise -1.
 */
int last_occurrence(int arr[], int size, int target)
{
    return last_occurrence_recursive(arr, target, size - 1);
}

/**
 * @brief Checks whether an array is sorted in non-decreasing order using recursion.
 *
 * The function compares the current element with the next element.
 * If the current pair is in the correct order, it recursively checks
 * the remaining elements of the array.
 *
 * @param arr   The array to check.
 * @param size  The number of elements in the array.
 * @param index The current index being checked.
 *
 * @return true if the array is sorted in non-decreasing order,
 *         otherwise false.
 *
 * @complexity
 * Time: O(n)
 * Space: O(n) due to recursive call stack.
 */
bool is_sorted(int arr[], int size, int index = 0)
{
    // Base case: reached the last element of the array.
    if (index + 1 == size)
        return true;

    // Current pair must be sorted, and the remaining array must also be sorted.
    return arr[index] <= arr[index + 1] &&
           is_sorted(arr, size, index + 1);
}

/**
 * @brief Finds the minimum element in an array recursively.
 *
 * Recursive state:
 * - get_minimum(arr, size) represents the minimum value
 *   among the first `size` elements of the array.
 *
 * Recursive idea:
 * - The recursive call finds the minimum among the first
 *   `size - 1` elements:
 *
 *      get_minimum(arr, size - 1)
 *
 * - The result is then compared with the current last element:
 *
 *      arr[size - 1]
 *
 * - The smaller value becomes the minimum for the first
 *   `size` elements.
 *
 * Recursive relation:
 *
 *      minimum(size)
 *      = min(minimum(size - 1), arr[size - 1])
 *
 * Base case:
 * - size = 1
 *      A single-element array has that element as its minimum.
 *
 * @param arr Array whose minimum element needs to be found.
 * @param size Number of elements currently being considered.
 *
 * @return Minimum value among the first `size` elements.
 *
 * @complexity
 * Time: O(n)
 * - Each element is processed once.
 *
 * Space: O(n)
 * - The recursion stack can grow up to `n` calls.
 */
int get_minimum(int arr[], int size)
{
    if (size == 1)
        return arr[0];

    int index = size - 1;

    return min(get_minimum(arr, size - 1), arr[index]);
}

/**
 * @brief Finds the maximum element in an array recursively.
 *
 * Recursive state:
 * - get_maximum(arr, size) represents the maximum value
 *   among the first `size` elements of the array.
 *
 * Recursive idea:
 * - The recursive call finds the maximum among the first
 *   `size - 1` elements:
 *
 *      get_maximum(arr, size - 1)
 *
 * - The result is then compared with the current last element:
 *
 *      arr[size - 1]
 *
 * - The larger value becomes the maximum for the first
 *   `size` elements.
 *
 * Recursive relation:
 *
 *      maximum(size)
 *      = max(maximum(size - 1), arr[size - 1])
 *
 * Base case:
 * - size = 1
 *      A single-element array has that element as its maximum.
 *
 * @param arr Array whose maximum element needs to be found.
 * @param size Number of elements currently being considered.
 *
 * @return Maximum value among the first `size` elements.
 *
 * @complexity
 * Time: O(n)
 * - Each element is processed once.
 *
 * Space: O(n)
 * - The recursion stack can grow up to `n` calls.
 */
int get_maximum(int arr[], int size)
{
    if (size == 1)
        return arr[0];

    int index = size - 1;

    return max(get_maximum(arr, size - 1), arr[index]);
}

int main()
{
    int arr[] = {1, 3, 9, 15, 14, 9, 37};
    int size = sizeof(arr) / sizeof(arr[0]);

    int target = 9;

    cout << "Array: ";
    print_array(arr, size);

    cout << "Index of First Occurrence of " << target << " is: "
         << first_occurrence(arr, size, target)
         << endl;

    cout << "Index of Last Occurrence of " << target << " is: "
         << last_occurrence(arr, size, target)
         << endl;

    cout << "Is array sorted? " << is_sorted(arr, size) << endl;

    cout << "Mimimum value in the array: " << get_minimum(arr, size) << endl;

    cout << "Maximum value in the array: " << get_maximum(arr, size) << endl;

    return 0;
}
