#include <algorithm>
#include <iostream>
#include <stdexcept>
#include "../../../include/arrays/print.h"

using namespace std;

/**
 * @brief Finds the maximum number of consecutive ones in a binary array.
 *
 * @details
 * Uses a single linear traversal with consecutive-element counting and
 * a running maximum to determine the longest contiguous sequence of ones.
 *
 * Algorithm (Consecutive Counting + Running Maximum):
 * 1. Validate that the array size is not negative.
 * 2. Initialize two counters:
 *    - current: Tracks the length of the current consecutive sequence of ones.
 *    - maximum: Tracks the longest consecutive sequence found so far.
 * 3. Traverse the array from left to right.
 * 4. For each element:
 *    - If the element is 1, increment the current counter.
 *    - Otherwise, reset the current counter to 0.
 *    - Update maximum with the larger of maximum and current.
 * 5. Return the maximum consecutive count.
 *
 * @param arr  The binary array containing zeros and ones.
 * @param size The number of elements in the array.
 *
 * @return The length of the longest contiguous sequence of ones.
 *         Returns 0 if the array is empty or contains no ones.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Assumes the input is a binary array containing only 0 and 1.
 *       Any value other than 1 is treated as a sequence break.
 *       Handles empty and single-element arrays without special cases.
 *       Does not modify the original array.
 *       Requires a valid array containing at least size elements.
 *
 * @par Key Observation:
 *      A consecutive sequence of ones continues as long as the current
 *      element equals 1. Encountering 0 terminates the sequence.
 *
 *      By updating the maximum after every element, the algorithm
 *      automatically accounts for sequences ending at the last index.
 *
 * @par Loop Invariant:
 *      After processing index i:
 *      - current stores the number of consecutive ones ending at i.
 *      - maximum stores the longest sequence of consecutive ones
 *        encountered in the prefix arr[0...i].
 *
 * @par Time Complexity:
 *      Best:  O(1) - The array is empty.
 *      Worst: O(n) - Every element is examined exactly once.
 *
 * @par Auxiliary Space:
 *      O(1) - Uses only two counters and a loop variable.
 */
int maximum_consecutive_ones(const int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    // Track the current sequence length and longest sequence found.
    int maximum = 0, current = 0;

    for (int i = 0; i < size; i++)
    {
        if (arr[i] == 1)
            current++; // Extend the current sequence of ones.
        else
            current = 0; // A non-one element breaks the sequence.

        // Update the longest sequence, including one ending at the last index.
        maximum = max(maximum, current);
    }

    return maximum;
}

int main()
{
    int arr[] = {1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1};
    int size = sizeof arr / sizeof arr[0];

    cout << "Original Array: ";
    print_array(arr, size);

    cout << "The maximum consecutive ones in the given array is "
         << maximum_consecutive_ones(arr, size) << "." << endl;

    return 0;
}
