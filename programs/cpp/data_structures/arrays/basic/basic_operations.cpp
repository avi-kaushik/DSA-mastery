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
 * @brief Searches for the first occurrence of a value in an array
 *        using linear search.
 *
 * Algorithm:
 * 1. Traverse the array from the first to the last element.
 * 2. Compare each element with the target value.
 * 3. If a match is found, return its index immediately.
 * 4. If no match is found, return -1.
 *
 * @param arr    The array in which the value is searched.
 * @param value  The target value to search for.
 * @param size   The current number of elements in the array.
 *
 * @return The zero-based index of the first occurrence,
 *         or -1 if the value is not found.
 *
 * @note Works on both sorted and unsorted arrays.
 *       Does not modify the original array.
 *       If duplicates exist, returns the first occurrence.
 *
 * Time Complexity:
 * - Best:    O(1) - Target found at the first index.
 * - Average: O(n) - Target found around the middle.
 * - Worst:   O(n) - Target at the end or not found.
 *
 * Auxiliary Space: O(1)
 */
int search(const int arr[], int value, int size)
{
    for (int i = 0; i < size; i++)
        if (arr[i] == value)
            return i;

    return -1;
}

/**
 * @brief Checks whether an array is sorted in non-decreasing
 *        (ascending) order using a single linear traversal.
 *
 * Algorithm:
 * 1. Validate that the array contains at least one element.
 * 2. Traverse the array from index 1 to size - 1.
 * 3. Compare each element with its preceding element.
 * 4. If the current element is smaller than the preceding element,
 *    return false immediately, as the order is violated.
 * 5. If no ordering violation is found, return true.
 *
 * @param arr   The array to check for sorted order.
 * @param size  The current number of elements in the array.
 *
 * @return true if the array is sorted in non-decreasing order,
 *         false otherwise.
 *
 * @throws std::out_of_range If size is zero or negative.
 *
 * @note Checks for non-decreasing order, allowing duplicate values.
 *       For strictly increasing order, use <= instead of <.
 *       Handles negative numbers and single-element arrays.
 *       Does not modify the original array.
 *       Requires a valid array containing at least size elements.
 *
 * Time Complexity:
 * - Best:  O(1) - First adjacent pair violates the order.
 * - Worst: O(n) - All adjacent elements must be checked.
 *
 * Auxiliary Space: O(1)
 */
bool is_sorted(const int arr[], int size)
{
    if (size <= 0)
        throw out_of_range("Array must contain at least one element.");

    for (int i = 1; i < size; i++)
        if (arr[i] < arr[i - 1])
            return false;

    return true;
}

/**
 * @brief Reverses an array in-place using the two-pointer technique.
 *
 * Algorithm:
 * 1. Validate that the array size is not negative.
 * 2. Initialize two pointers:
 *    - left at the first index (0).
 *    - right at the last index (size - 1).
 * 3. While the left pointer is smaller than the right pointer:
 *    - Swap the elements at the left and right indices.
 *    - Increment the left pointer.
 *    - Decrement the right pointer.
 * 4. Stop when the pointers meet or cross.
 *
 * @param arr   The array to reverse.
 * @param size  The current number of elements in the array.
 *
 * @return void Modifies the original array directly.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Uses the two-pointer technique.
 *       Reverses the array in-place without additional storage.
 *       Handles even-sized, odd-sized, and single-element arrays.
 *       An empty array remains unchanged.
 *       Requires a valid array containing at least size elements.
 *
 * Time Complexity:
 * - Best:  O(1) - Array contains zero or one element.
 * - Worst: O(n) - Approximately n/2 swaps are performed.
 *
 * Auxiliary Space: O(1)
 */
void reverse(int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    int left = 0, right = size - 1;

    while (left < right)
    {
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;

        left++;
        right--;
    }
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
    size = insert(arr, 18, 5, size, array_capacity);

    // Remove elements.
    size = remove(arr, 12, size, array_capacity);
    size = remove(arr, 18, size, array_capacity);

    // Print the final array.
    print(arr, size);

    // Search the array element.
    cout << "The index of 9 is: " << search(arr, 9, size) << endl;

    // Reverse the array.
    reverse(arr, size);

    // Print the final array.
    print(arr, size);

    return 0;
}
