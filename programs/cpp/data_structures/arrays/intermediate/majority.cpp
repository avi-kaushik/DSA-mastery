#include <algorithm>
#include <iostream>
#include <stdexcept>
#include "../../../include/arrays/print.h"

using namespace std;

/**
 * @brief Finds the majority element in an array using the
 *        Boyer-Moore Majority Vote Algorithm.
 *
 * @details
 * Uses a voting mechanism to identify the element occurring more
 * than floor(n / 2) times by canceling pairs of different elements.
 *
 * Algorithm (Boyer-Moore Majority Vote):
 * 1. Validate the array size and handle an empty array.
 * 2. Initialize the vote count and candidate to 0.
 * 3. Traverse the array from left to right.
 * 4. For each element:
 *    - If the vote count is 0, select the current element as the candidate.
 *    - If the element matches the candidate, increment the vote count.
 *    - Otherwise, decrement the vote count.
 * 5. Return the final candidate.
 *
 * @param arr  The input array containing integer elements.
 * @param size The number of elements in the array.
 *
 * @return The majority element, assuming one exists.
 *         Returns 0 if the array is empty.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Assumes a majority element exists for every non-empty input.
 *       A majority element occurs more than floor(size / 2) times.
 *       Without this guarantee, the returned candidate requires
 *       verification through an additional traversal.
 *       Does not modify the original array.
 *       Requires a valid array containing at least size elements.
 *
 * @par Key Observation:
 *      Canceling pairs of different elements cannot eliminate a true
 *      majority element because it occurs more frequently than all
 *      other elements combined.
 *
 * @par Time Complexity:
 *      Best:  O(1) - Empty array.
 *      Worst: O(n) - Single linear traversal.
 *
 * @par Auxiliary Space:
 *      O(1) - Uses only a candidate and vote counter.
 */
int get_majority_element(const int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    if (size == 0)
        return 0;

    int vote = 0, candidate = 0;

    for (int i = 0; i < size; i++)
    {
        // Select a new candidate when previous votes cancel out.
        if (vote == 0)
            candidate = arr[i];

        // Matching elements add votes; different elements cancel them.
        if (arr[i] == candidate)
            vote++;
        else
            vote--;
    }

    return candidate;
}

int main()
{
    int arr[] = {8, 3, 4, 8, 8};
    int size = sizeof arr / sizeof arr[0];

    cout << "Array: ";
    print_array(arr, size);

    cout << "Majority element in the given array is "
         << get_majority_element(arr, size) << "." << endl;

    return 0;
}
