#include <iostream>
#include <stdexcept>
#include "../../include/arrays/print.h"

using namespace std;

/**
 * @brief Counts and prints the frequency of each distinct element
 *        in a sorted array using a single linear traversal.
 *
 * Algorithm:
 * 1. Validate that the array size is not negative.
 * 2. If the array is empty, return immediately.
 * 3. Initialize a frequency counter to 1 for the first element.
 * 4. Traverse the array from index 1 to size - 1.
 * 5. For each element:
 *    - If it equals the previous element, increment the frequency.
 *    - Otherwise, print the previous element and its frequency,
 *      then reset the counter to 1 for the new element.
 * 6. After traversal, print the frequency of the final group,
 *    which has not yet been printed.
 *
 * @param arr   The sorted array whose element frequencies are counted.
 * @param size  The current number of elements in the array.
 *
 * @return void Prints each distinct element and its frequency
 *         directly to standard output.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Requires the input array to be sorted in non-decreasing order.
 *       Equal elements must appear consecutively.
 *       Prints each distinct element exactly once with its frequency.
 *       Handles empty, single-element, and all-duplicate arrays.
 *       Supports negative values and duplicate elements.
 *       Does not modify the original array.
 *       Requires a valid array containing at least size elements.
 *
 * Time Complexity:
 * - Best:  O(1) - Array is empty or contains one element.
 * - Worst: O(n) - Every element is examined once.
 *
 * Auxiliary Space: O(1)
 */
void print_frequencies_of_elements_in_sorted_array(const int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    // An empty array contains no elements to count.
    if (size == 0)
        return;

    // The first element begins a group with frequency 1.
    int current_frequency = 1;

    // Traverse the array and count consecutive equal elements.
    for (int i = 1; i < size; i++)
    {
        if (arr[i] == arr[i - 1])
        {
            // Continue counting the current group.
            current_frequency++;
        }
        else
        {
            // The previous group has ended; print its frequency.
            cout << "Element: " << arr[i - 1]
                 << ", Frequency: " << current_frequency << endl;

            // Start counting the newly encountered distinct element.
            current_frequency = 1;
        }
    }

    // The final group has no next element to trigger the else block.
    cout << "Element: " << arr[size - 1]
         << ", Frequency: " << current_frequency << endl;
}

int main()
{
    int arr[] = {10, 10, 10, 20, 30, 30};
    int size = sizeof arr / sizeof arr[0];

    cout << "Array: ";
    print_array(arr, size);

    print_frequencies_of_elements_in_sorted_array(arr, size);

    return 0;
}
