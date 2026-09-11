#include <iostream>
#include <algorithm>

using namespace std;

/**
 * Returns the maximum number of pieces into which a rope of length n
 * can be cut using pieces of lengths a, b, and c.
 *
 * Approach:
 * - Try cutting the rope using a, b, and c.
 * - Recursively find the maximum cuts possible for each choice.
 * - Add 1 for the current cut if at least one choice gives a valid result.
 * - Return -1 if the rope cannot be cut exactly using the given lengths.
 *
 * Base cases:
 * - If any cut length is non-positive, return -1.
 * - If n == 0, the rope has been completely cut, so return 0.
 * - If n < 0, the current combination is invalid, so return -1.
 *
 * Time Complexity: O(3^n)
 * Space Complexity: O(n) - recursion stack
 */
int get_maximum_cuts(int n, int a, int b, int c)
{
    // Edge case: cut lengths must be positive.
    if (a <= 0 || b <= 0 || c <= 0)
        return -1;

    // Base case: rope has been completely cut.
    if (n == 0)
        return 0;

    // Base case: remaining rope length is negative.
    if (n < 0)
        return -1;

    // Try all three possible cuts and choose the maximum result.
    int cuts = max({get_maximum_cuts(n - a, a, b, c),
                    get_maximum_cuts(n - b, a, b, c),
                    get_maximum_cuts(n - c, a, b, c)});

    // If a valid combination exists, count the current cut.
    return cuts >= 0 ? cuts + 1 : -1;
}

int main()
{
    cout
        << "Maximum cuts of 2, 5 and 1 in a rope of size 5 are: "
        << get_maximum_cuts(5, 2, 5, 1)
        << endl;

    return 0;
}
