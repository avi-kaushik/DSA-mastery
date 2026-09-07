#include <iostream>
#include <string>

using namespace std;

/**
 * Recursive helper function that checks the specified range of the string.
 *
 * @param word  The string to check.
 * @param start The index of the first character in the current range.
 * @param end   The index of the last character in the current range.
 * @return true if the specified range is a palindrome, otherwise false.
 */
bool is_palindrome(const string &word, int start, int end)
{
    // Base case: no characters or only one character remains.
    if (start >= end)
        return true;

    // Current outer characters must match, and the inner range
    // must also be a palindrome.
    return (word[start] == word[end]) &&
           is_palindrome(word, start + 1, end - 1);
}

/**
 * Checks whether a given string is a palindrome using recursion.
 *
 * A palindrome reads the same from left to right and right to left.
 *
 * Approach:
 * - Compare the characters at the current start and end positions.
 * - If they match, recursively check the remaining inner portion.
 * - If they don't match, the string is not a palindrome.
 *
 * Base Case:
 * - When start >= end, all required character pairs have been
 *   successfully compared, so the string is a palindrome.
 *
 * Time Complexity: O(n)
 * Auxiliary Space: O(n) due to the recursive call stack.
 *
 * @param word The string to check.
 * @return true if the string is a palindrome, otherwise false.
 */
bool is_palindrome(const string &word)
{
    return is_palindrome(word, 0, word.length() - 1);
}

int main()
{
    string word = "madam";

    cout << "Word: " + word << endl;
    cout << "Is Palindrome? " << (is_palindrome(word) ? "Yes" : "No") << endl;

    return 0;
}
