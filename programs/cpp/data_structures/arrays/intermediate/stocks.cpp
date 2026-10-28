#include <iostream>
#include <stdexcept>
#include "../../../include/arrays/print.h"

using namespace std;

/**
 * @brief Calculates the maximum profit from unlimited stock
 *        transactions using the Greedy Approach.
 *
 * Algorithm (Greedy - Positive Adjacent Differences):
 * 1. Validate that the array size is not negative.
 * 2. If the array contains fewer than two prices, return 0.
 * 3. Initialize the total profit to 0.
 * 4. Traverse the array from index 1 to size - 1.
 * 5. For each pair of consecutive prices:
 *    - If the current price is greater than the previous price,
 *      add their positive difference to the total profit.
 *    - Otherwise, ignore the difference since it cannot
 *      contribute to the maximum profit.
 * 6. Return the accumulated maximum profit.
 *
 * @param arr   The array containing stock prices in chronological order.
 * @param size  The number of stock prices (trading days).
 *
 * @return The maximum profit obtainable through unlimited
 *         non-overlapping buy/sell transactions.
 *         Returns 0 if fewer than two prices are available.
 *
 * @throws std::out_of_range If size is negative.
 *
 * @note Uses the Greedy Approach by accumulating every positive
 *       difference between consecutive stock prices.
 *       Assumes unlimited transactions, no transaction fees,
 *       no cooldown periods, and at most one stock held at a time.
 *       Each transaction must involve buying before selling.
 *       Assumes stock prices are non-negative integers.
 *       Does not modify the original array.
 *       Requires a valid array containing at least size elements.
 *       Assumes integer arithmetic does not overflow.
 *
 * @par Greedy Choice:
 *      Capture every positive increase between consecutive days
 *      while ignoring price decreases and unchanged prices.
 *
 * @par Why Greedy Works:
 *      The profit from buying at the beginning and selling at
 *      the end of an increasing interval equals the sum of all
 *      positive adjacent differences within that interval.
 *      This follows the telescoping sum property.
 *
 *      Since unlimited transactions are permitted, collecting
 *      every positive increase produces the maximum total profit.
 *
 * Time Complexity:
 * - Best:  O(1) - Array contains fewer than two prices.
 * - Worst: O(n) - Every adjacent pair is examined once.
 *
 * Auxiliary Space: O(1)
 */
int get_maximum_profit(const int arr[], int size)
{
    if (size < 0)
        throw out_of_range("Invalid array size.");

    // At least two prices are required for a buy/sell transaction.
    if (size <= 1)
        return 0;

    int profit = 0;

    // Greedy: Accumulate every profitable adjacent price increase.
    for (int i = 1; i < size; i++)
    {
        // Ignore price decreases and unchanged prices.
        if (arr[i] > arr[i - 1])
            profit += arr[i] - arr[i - 1];
    }

    return profit;
}

int main()
{
    int arr[] = {1, 5, 8, 3, 12};
    int size = sizeof arr / sizeof arr[0];

    cout << "Array: ";
    print_array(arr, size);

    cout << "Maximum profit in the above stock movement is "
         << get_maximum_profit(arr, size) << "." << endl;

    return 0;
}
