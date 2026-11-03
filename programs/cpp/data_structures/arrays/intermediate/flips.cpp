#include <iostream>
#include <stdexcept>

using namespace std;

/**
 * @brief Prints the minimum consecutive groups that must be flipped
 *        to make all elements of a binary array equal.
 *
 * @details
 * Uses a Greedy Approach with consecutive-group detection.
 * Since groups of 0s and 1s alternate, flipping every group whose
 * value differs from the first element minimizes the number of flips.
 *
 * Algorithm (Greedy + Group Transitions):
 * 1. Validate the array size and handle an empty array.
 * 2. Traverse the array from index 1 to size - 1.
 * 3. Whenever consecutive elements differ:
 *    - If the current element differs from the first element,
 *      print the starting index of a group to flip.
 *    - Otherwise, print the ending index of that group.
 * 4. If the last element differs from the first element,
 *    close the final group at index size - 1.
 *
 * @param arr  The input binary array containing 0s and 1s.
 * @param size The number of elements in the array.
 *
 * @return void Prints the minimum flip ranges to standard output.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Assumes the array contains only 0s and 1s.
 *       Prints zero-based inclusive index ranges.
 *       Does not modify the original array.
 *       Requires a valid array containing at least size elements.
 *
 * @par Key Observation:
 *      Groups of 0s and 1s alternate, so their counts differ by
 *      at most one. The value opposite to the first element has
 *      no more groups than the first element's value.
 *      Therefore, flipping those groups is always optimal.
 *
 * @par Time Complexity:
 *      Best:  O(1) - Empty array.
 *      Worst: O(n) - Single linear traversal.
 *
 * @par Auxiliary Space:
 *      O(1) - No additional data structures are required.
 */
void minimum_consecutive_flips(const int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    if (size == 0)
        return;

    for (int i = 1; i < size; i++)
    {
        // Detect a transition between consecutive groups.
        if (arr[i] != arr[i - 1])
        {
            if (arr[i] != arr[0])
                cout << "From " << i << " to "; // Start of a group to flip.
            else
                cout << i - 1 << "." << endl; // End of the group.
        }
    }

    // Close the final group if it extends to the last index.
    if (arr[size - 1] != arr[0])
        cout << size - 1 << "." << endl;
}

int main()
{
    int arr[] = {0, 1, 1, 0, 0};
    int size = sizeof arr / sizeof arr[0];

    minimum_consecutive_flips(arr, size);

    return 0;
}
