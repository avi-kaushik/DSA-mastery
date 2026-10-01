#include <iostream>

using namespace std;

/**
 * @brief Searches for a value in a sorted array using recursive binary search.
 *
 * The function repeatedly divides the search range into half by comparing
 * the target value with the middle element of the current range.
 *
 * @param arr  The sorted array in which the value will be searched.
 * @param val  The value to search for.
 * @param low  The starting index of the current search range.
 * @param high The ending index of the current search range.
 *
 * @return The index of the value if found, otherwise -1.
 *
 * @complexity
 * Time: O(log n)
 * Space: O(log n) due to recursive call stack.
 */
int binary_search(int arr[], int val, int low, int high)
{
    // Base case: search range becomes empty.
    if (low > high)
        return -1;

    // Find the middle index of the current search range.
    int mid = low + (high - low) / 2;

    // Target found at the middle index.
    if (arr[mid] == val)
    {
        return mid;
    }

    // Target is smaller, so search the left half.
    else if (arr[mid] > val)
    {
        return binary_search(arr, val, low, mid - 1);
    }

    // Target is larger, so search the right half.
    else
    {
        return binary_search(arr, val, mid + 1, high);
    }
}

/**
 * @brief Initializes recursive binary search for the complete array.
 *
 * This is a wrapper function that sets the initial search range from
 * index 0 to length - 1.
 *
 * @param arr    The sorted array in which the value will be searched.
 * @param length The number of elements in the array.
 * @param val    The value to search for.
 *
 * @return The index of the value if found, otherwise -1.
 */
int binary_search(int arr[], int length, int val)
{
    return binary_search(arr, val, 0, length - 1);
}

int main()
{
    int arr[] = {10, 20, 30, 40, 50};
    int val = 40;

    // Calculate length while arr is still an actual array.
    int length = sizeof(arr) / sizeof(arr[0]);

    int index = binary_search(arr, length, val);

    if (index != -1)
        cout << "Value " << val << " found at index " << index << endl;
    else
        cout << "Value " << val << " not found in the array." << endl;

    return 0;
}
