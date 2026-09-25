#include <iostream>
#include <string>

using namespace std;

/**
 * @brief Swaps two characters in a string.
 *
 * The function exchanges the characters located at the given
 * indices in the string.
 *
 * Since the string is passed by reference, the original string
 * is modified directly and no value needs to be returned.
 *
 * Time complexity:  O(1)
 * Space complexity: O(1)
 *
 * Time complexity:
 * Swapping two characters requires only a constant number of
 * operations, so the time complexity is O(1).
 *
 * Space complexity:
 * Only one temporary character variable is used, so the
 * auxiliary space complexity is O(1).
 *
 * @param s The string whose characters need to be swapped.
 * @param a The index of the first character.
 * @param b The index of the second character.
 *
 * @return void
 */
void swap_characters(string &s, int a, int b)
{
    char temp = s[a];
    s[a] = s[b];
    s[b] = temp;
}

/**
 * @brief Prints all possible permutations of a given string using recursion
 *        and backtracking.
 *
 * At every recursive level, one position in the string is fixed.
 *
 * For the current position n, every character from index n to the
 * end of the string is considered as a possible candidate.
 *
 * For each candidate:
 * 1. Swap the candidate character with the character at position n.
 * 2. Recursively generate permutations for the remaining positions.
 * 3. Swap the characters back to restore the previous state.
 *
 * The final swap is the backtracking step. It restores the string
 * so that the next candidate can be explored correctly.
 *
 * When n becomes equal to the length of the string, all positions
 * have been fixed and one complete permutation has been generated.
 *
 * For a string of length n, the total number of permutations is n!.
 *
 * Time complexity:  O(n * n!)
 * Space complexity: O(n)
 *
 * Time complexity:
 * There are n! possible permutations of a string containing n
 * distinct characters.
 *
 * Printing each permutation requires O(n) time because the complete
 * string contains n characters.
 *
 * Therefore, the total time complexity is O(n * n!).
 *
 * Space complexity:
 * The maximum recursion depth is n because one string position is
 * fixed at each recursive level.
 *
 * The algorithm performs swaps directly on the original string,
 * so it does not create a new string for every recursive call.
 *
 * Therefore, the auxiliary space required by the recursion call
 * stack is O(n).
 *
 * @param s The string whose permutations need to be generated.
 * @param n The index of the position currently being fixed.
 *
 * @return void
 */
void print_permutations(string &s, int n = 0)
{
    // All positions have been fixed, so one complete
    // permutation has been generated.
    if (n == s.length())
    {
        cout << s << " ";
        return;
    }

    // Try every remaining character at the current position.
    for (int i = n; i < s.length(); i++)
    {
        // Place the character at index i into the current position n.
        swap_characters(s, n, i);

        // Recursively generate all permutations for the remaining positions.
        print_permutations(s, n + 1);

        // Backtrack by restoring the string to its previous state.
        swap_characters(s, n, i);
    }
}

int main()
{
    string s = "ABC";

    cout << "Permutations for " << s << ": ";
    print_permutations(s);

    cout << endl;

    return 0;
}
