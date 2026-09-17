#include <iostream>

using namespace std;

/*
 * Problem Statement:
 * ------------------
 * There are n people standing in a circle, numbered from 0 to n - 1.
 * Starting from person 0, every k-th person is eliminated.
 * After each elimination, counting continues from the next person.
 *
 * The process continues until only one person remains.
 *
 * Find the 0-based index of the surviving person.
 *
 * Function: josepheus
 * -------------------
 * Finds the position of the survivor using recursion.
 *
 * Parameters:
 *   n - Number of people in the circle.
 *   k - Elimination interval; every k-th person is eliminated.
 *
 * Returns:
 *   The 0-based index of the surviving person.
 *
 * Recurrence:
 *   J(n, k) = (J(n - 1, k) + k) % n
 *
 * Base Case:
 *   J(0, k) = 0
 *
 * Time Complexity:
 *   O(n)
 *
 * Space Complexity:
 *   O(n) due to the recursive call stack.
 */
int josepheus(int n, int k)
{
    // Base case: no people means survivor position is 0.
    if (n == 0)
        return 0;

    // Find the survivor's position for n - 1 people.
    int i = josepheus(n - 1, k);

    // Shift the position by k and wrap around the circle.
    return (i + k) % n;
}

int main()
{
    cout << josepheus(7, 3) << endl;

    return 0;
}
