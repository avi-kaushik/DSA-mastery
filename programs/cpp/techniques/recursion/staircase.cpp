#include <iostream>

using namespace std;

/**
 * @brief Counts the total number of ways to reach the nth staircase step
 *        when a person can move either 1 step or 2 steps at a time.
 *
 * Recursive state:
 * - maximum_staircase_steps(n) represents the number of ways to climb
 *   exactly n remaining stairs.
 *
 * Recursive choices:
 * - Take 1 step:
 *      The remaining problem becomes maximum_staircase_steps(n - 1).
 *
 * - Take 2 steps:
 *      The remaining problem becomes maximum_staircase_steps(n - 2).
 *
 * Since both choices represent different valid ways of reaching the top,
 * their results are added:
 *
 *      ways(n) = ways(n - 1) + ways(n - 2)
 *
 * Base case:
 * - n = 0:
 *      There is exactly one valid way to reach the destination:
 *      take no more steps.
 *
 * - n = 1:
 *      There is exactly one way to reach the destination:
 *      take one step.
 *
 * @param n Number of stairs remaining to reach the top.
 *
 * @return Total number of distinct ways to climb n stairs
 *         using jumps of size 1 or 2.
 *
 * @complexity
 * Time: O(2^n)
 * - Each recursive call can create up to two additional recursive calls.
 * - The same subproblems are recalculated multiple times.
 *
 * Space: O(n)
 * - The maximum recursion depth occurs when repeatedly taking
 *   the 1-step branch.
 */
int maximum_staircase_steps(int n)
{
    // Reaching 0 or 1 remaining stairs has exactly one valid completion.
    if (n <= 1)
        return 1;

    // Count ways after choosing either a 1-step or 2-step move.
    return maximum_staircase_steps(n - 1) +
           maximum_staircase_steps(n - 2);
}

int main()
{
    int n = 5;

    cout << "Maximum ways to reach " << n << " steps: "
         << maximum_staircase_steps(n)
         << endl;

    return 0;
}
