#include <iostream>
#include <string>

using namespace std;

/**
 * @brief Prints all possible subsets of a given string using recursion.
 *
 * For every character in the string, we have two choices:
 * 1. Exclude the current character from the subset.
 * 2. Include the current character in the subset.
 *
 * This creates a binary recursion tree where each level represents
 * a character and each branch represents one of the two choices.
 *
 * For a string of length n, the total number of possible subsets is 2^n.
 *
 * Time complexity:  O(n * 2^n)
 * Space complexity: O(n)
 *
 * Time complexity:
 * There are 2^n possible subsets, and printing a subset can take
 * up to O(n) time. Therefore, the total time complexity is O(n * 2^n).
 *
 * Space complexity:
 * The recursion depth can reach n, so the recursion call stack
 * requires O(n) auxiliary space.
 *
 * @param pattern The string whose subsets need to be generated.
 * @param current The subset constructed so far.
 * @param depth The index of the character currently being considered.
 *
 * @return void
 */
void print_subsets(const string &pattern, string current = "", int depth = 0)
{
    // All characters have been considered, so the current subset is complete.
    if (pattern.length() == depth)
    {
        cout << current << " ";
        return;
    }

    // Exclude the current character from the subset.
    print_subsets(pattern, current, depth + 1);

    // Include the current character in the subset.
    print_subsets(pattern, current + pattern[depth], depth + 1);
}

/**
 * @brief Counts the number of subsets whose sum is equal to the given target sum.
 *
 * For every element in the array, we have two choices:
 * 1. Exclude the current element from the subset.
 * 2. Include the current element in the subset and reduce the required sum
 *    by the value of that element.
 *
 * This creates a binary recursion tree where each level represents
 * an array element and each branch represents one of the two choices.
 *
 * When no elements are left to consider:
 * - If the remaining sum is 0, a valid subset has been found.
 * - Otherwise, the current subset does not produce the required sum.
 *
 * For an array of size n, there can be up to 2^n possible subsets.
 *
 * Time complexity:  O(2^n)
 * Space complexity: O(n)
 *
 * Time complexity:
 * At every element, the function makes two recursive calls:
 * one excluding the element and one including it.
 * Therefore, the recursion tree can contain approximately 2^n calls,
 * resulting in O(2^n) time complexity.
 *
 * Space complexity:
 * The maximum recursion depth is n because one array element is
 * processed at each recursive level. Therefore, the recursion call
 * stack requires O(n) auxiliary space.
 *
 * @param arr The array whose subsets need to be considered.
 * @param depth The number of array elements currently available
 *              for consideration.
 * @param sum The remaining target sum that needs to be formed.
 *
 * @return The number of subsets whose sum is equal to the target sum.
 */
int subset_sum(int arr[], int depth, int sum)
{
    // No elements are left to consider.
    // If the remaining sum is 0, the current subset is valid.
    if (depth == 0)
        return sum == 0 ? 1 : 0;

    // Exclude the current element from the subset.
    int exclude = subset_sum(arr, depth - 1, sum);

    // Include the current element in the subset and reduce the
    // required sum by the value of the current element.
    int include = subset_sum(arr, depth - 1, sum - arr[depth - 1]);

    // Return the total number of valid subsets found through
    // both the exclude and include choices.
    return exclude + include;
}

int main()
{
    string pattern = "ABC";

    cout << "Subsets of " << pattern << ": ";

    print_subsets(pattern);

    int arr[] = {4, 2, 6, 4, 9};

    int n = sizeof(arr) / sizeof(arr[0]);

    cout << "\nThe total subsets with sum 8 in 4,2,6,4,9 are " << subset_sum(arr, n, 8) << endl;

    return 0;
}
