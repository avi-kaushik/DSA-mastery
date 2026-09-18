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

int main()
{
    string pattern = "ABC";

    cout << "Subsets of " << pattern << ": ";

    print_subsets(pattern);

    return 0;
}
