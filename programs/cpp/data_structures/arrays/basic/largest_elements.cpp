#include <iostream>
#include <climits>
#include <stdexcept>
#include "../../../include/arrays/print.h"

using namespace std;

/**
 * @brief Finds the largest element in an array using linear traversal.
 *
 * Algorithm:
 * 1. Validate that the array contains at least one element.
 * 2. Initialize the largest element with the first array element.
 * 3. Traverse the remaining elements from index 1 to size - 1.
 * 4. If an element is greater than the current largest,
 *    update the largest value.
 * 5. Return the largest element after completing the traversal.
 *
 * @param arr   The array in which the largest element is searched.
 * @param size  The current number of elements in the array.
 *
 * @return The largest element present in the array.
 *
 * @throws std::out_of_range If size is zero or negative.
 *
 * @note Works on both sorted and unsorted arrays.
 *       Handles negative values and duplicate elements.
 *       Does not modify the original array.
 *       Requires a valid array containing at least size elements.
 *
 * Time Complexity:
 * - Best:  O(n) - Every element is examined.
 * - Worst: O(n) - Every element is examined.
 *
 * Auxiliary Space: O(1)
 */
int largest_element(const int arr[], int size)
{
    if (size <= 0)
        throw out_of_range("Array must contain at least one element.");

    int largest = arr[0];

    for (int i = 1; i < size; i++)
        if (arr[i] > largest)
            largest = arr[i];

    return largest;
}

/**
 * @brief Finds the second-largest distinct element in an array
 *        using a single linear traversal.
 *
 * Algorithm:
 * 1. Validate that the array contains at least two elements.
 * 2. Initialize the largest element with the first array element,
 *    and use a Boolean flag to track the second-largest element.
 * 3. Traverse the remaining elements from index 1 to size - 1.
 * 4. If the current element is greater than the largest:
 *    - Update second_largest with the previous largest.
 *    - Update largest with the current element.
 *    - Mark the second-largest element as found.
 * 5. Otherwise, if the current element is smaller than largest
 *    and greater than the existing second_largest (if found),
 *    update second_largest and mark it as found.
 * 6. If no second-largest distinct element exists, throw an exception.
 * 7. Return the second-largest distinct element.
 *
 * @param arr   The array in which the second-largest element is searched.
 * @param size  The current number of elements in the array.
 *
 * @return The second-largest distinct element present in the array.
 *
 * @throws std::out_of_range If the array contains fewer than two elements.
 * @throws std::runtime_error If no second-largest distinct element exists.
 *
 * @note Works on both sorted and unsorted arrays.
 *       Handles negative values, duplicate elements, and INT_MIN.
 *       Does not modify the original array.
 *       Requires a valid array containing at least size elements.
 *
 * Time Complexity:
 * - Best:  O(n) - Every element is examined.
 * - Worst: O(n) - Every element is examined.
 *
 * Auxiliary Space: O(1)
 */
int second_largest_element(const int arr[], int size)
{
    if (size < 2)
        throw out_of_range("Array must contain at least two elements.");

    int largest = arr[0], second_largest = INT_MIN;
    bool found = false;

    for (int i = 1; i < size; i++)
    {
        if (arr[i] > largest)
        {
            second_largest = largest;
            largest = arr[i];
            found = true;
        }
        else if (arr[i] < largest &&
                 (!found || arr[i] > second_largest))
        {
            second_largest = arr[i];
            found = true;
        }
    }

    if (!found)
        throw runtime_error("No second-largest distinct element exists.");

    return second_largest;
}

int main(int argc, char const *argv[])
{
    int arr[] = {64, 12, 1, 57, 12, 54};
    int size = sizeof arr / sizeof arr[0];

    print_array(arr, size);

    cout << "Largest element in the above array is: "
         << largest_element(arr, size) << endl;

    cout << "Second Largest element in the above array is: "
         << second_largest_element(arr, size) << endl;

    return 0;
}
