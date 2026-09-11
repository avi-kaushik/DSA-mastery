#include <iostream>

using namespace std;

/**
 * Prints the steps required to move n disks from the source rod
 * to the destination rod using the auxiliary rod.
 *
 * Approach:
 * - To move n disks, first move n - 1 disks from source to auxiliary.
 * - Move the largest disk (n) from source to destination.
 * - Move the remaining n - 1 disks from auxiliary to destination.
 * - The same process is recursively repeated for n - 1 disks.
 *
 * Base case:
 * - If n == 0, there are no disks to move, so return.
 *
 * Time Complexity: O(2^n)
 * Space Complexity: O(n) - recursion stack
 */
void move_disks(int n, char source, char auxiliary, char destination)
{
    // Base case: no disks to move.
    if (n == 0)
        return;

    // Move n - 1 disks from source to auxiliary
    // using destination as temporary storage.
    move_disks(n - 1, source, destination, auxiliary);

    // Move the largest disk from source to destination.
    cout << "Move " << n << " from " << source << " to " << destination << endl;

    // Move n - 1 disks from auxiliary to destination
    // using source as temporary storage.
    move_disks(n - 1, auxiliary, source, destination);
}

/**
 * Starts the Tower of Hanoi process using the default rods:
 * A as source, B as auxiliary, and C as destination.
 */
void move_disks(int n)
{
    move_disks(n, 'A', 'B', 'C');
}

int main()
{
    int disks = 3;

    cout << "Steps to move " << disks << " disks: " << endl;

    move_disks(disks);

    return 0;
}
