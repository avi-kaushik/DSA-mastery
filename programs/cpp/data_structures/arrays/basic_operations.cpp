#include <iostream>
#include <stdexcept>

using namespace std;

/**
 * @brief Inserts an element at a specified position in an array.
 *
 * Algorithm:
 * 1. Validate the array size, capacity, and insertion position.
 * 2. Shift elements one position to the right, starting from
 *    the last occupied element.
 * 3. Insert the new element at the specified position.
 * 4. Return the updated logical size.
 *
 * @param arr       The array in which the element is inserted.
 * @param value     The element to insert.
 * @param position  The zero-based insertion index.
 * @param size      The current number of elements.
 * @param capacity  The maximum number of elements.
 *
 * @return The updated logical size of the array.
 *
 * @note Valid insertion positions range from 0 to size.
 *       Insertion at position == size requires no shifting.
 *
 * Time Complexity:
 * - Best:  O(1) - Insertion at the end.
 * - Worst: O(n) - Insertion at the beginning.
 *
 * Auxiliary Space: O(1)
 */
int insert(int arr[], int value, int position, int size, int capacity)
{
    if (size < 0 || size > capacity)
        throw out_of_range("Invalid array size.");

    if (size == capacity)
        throw runtime_error("Array is full.");

    if (position < 0 || position > size)
        throw out_of_range("Invalid insertion position.");

    // Shift elements one position to the right.
    for (int i = size; i > position; i--)
        arr[i] = arr[i - 1];

    // Insert the element and return the updated size.
    arr[position] = value;
    return size + 1;
}

/**
 * @brief Removes the first occurrence of a value from an array.
 *
 * Algorithm:
 * 1. Validate the logical size of the array.
 * 2. Search for the first occurrence of the specified value.
 * 3. If the value is absent, return the original size.
 * 4. Shift subsequent elements one position to the left.
 * 5. Return the updated logical size.
 *
 * @param arr       The array from which the element is removed.
 * @param value     The value to remove.
 * @param size      The current number of elements.
 * @param capacity  The maximum number of elements.
 *
 * @return The updated logical size of the array.
 *
 * @note Only the first occurrence is removed.
 *       The relative order of remaining elements is preserved.
 *       Unused array slots are not cleared.
 *
 * Time Complexity:
 * - Best:  O(1) - Element found at the last position.
 * - Worst: O(n) - Searching and/or shifting elements.
 *
 * Auxiliary Space: O(1)
 */
int remove(int arr[], int value, int size, int capacity)
{
    if (size <= 0 || size > capacity)
        throw runtime_error("Invalid array size.");

    int position = 0;

    // Find the first occurrence of the value.
    while (position < size && arr[position] != value)
        position++;

    // Return unchanged size if the value is absent.
    if (position == size)
        return size;

    // Shift subsequent elements one position to the left.
    for (int i = position; i < size - 1; i++)
        arr[i] = arr[i + 1];

    return size - 1;
}

/**
 * @brief Prints the logically occupied elements of an array.
 *
 * @param arr   The array to print.
 * @param size  The current number of elements.
 *
 * Time Complexity: O(n)
 * Auxiliary Space: O(1)
 */
void print(const int arr[], int size)
{
    for (int i = 0; i < size; i++)
        cout << arr[i] << " ";

    cout << endl;
}

int main()
{
    /*
     * @brief
     * array_capacity -> Maximum number of elements.
     * arr            -> Fixed-capacity integer array.
     * size           -> Number of currently occupied elements.
     */
    constexpr int array_capacity = 9;
    int arr[array_capacity] = {};
    int size = 0;

    // Insert elements.
    size = insert(arr, 4, 0, size, array_capacity);
    size = insert(arr, 12, 1, size, array_capacity);
    size = insert(arr, 9, 2, size, array_capacity);
    size = insert(arr, 18, 3, size, array_capacity);
    size = insert(arr, 14, 4, size, array_capacity);

    // Remove elements.
    size = remove(arr, 12, size, array_capacity);
    size = remove(arr, 18, size, array_capacity);

    // Print the final array.
    print(arr, size);

    return 0;
}
