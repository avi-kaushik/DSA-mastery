
import java.util.Arrays;

// Class to calculate the maximum profit form it's per day prices.
class StockBuySell {

    /**
     * Prints potential profit actions (sell points and suggested next buy
     * points) based on the given stock prices.
     * 
     * The logic here is: - If the current day is the last day OR today's price
     * is higher than tomorrow's price, it suggests selling ("Opt out") at
     * today's price. - If it's not the last day, it also suggests buying the
     * next day ("Next buy").
     *
     * 
     * Note:
     * This function ensures it will not recommend a transaction that leads to a
     * direct loss, as it sells before a drop or on the last day. However, it
     * does not always identify the optimal buy-sell points (local minima and
     * maxima). This means it may miss some profitable opportunities where
     * buying slightly earlier and selling slightly later could yield higher
     * profit.
     *
     * Example: For prices = {45, 12, 45, 12, 45, 112, 98, 101} Output may
     * include: Opt out at: 45 Next buy at: 12 ...
     *
     * Time Complexity: O(n) - The function iterates through the array
     * once, performing O(1) operations per iteration.
     *
     * Space Complexity: O(1) - No additional data structures are used
     * that scale with input size.
     *
     * Auxiliary Space: O(1) - Only a few variables are used for
     * iteration and temporary checks.
     *
     * @param prices an array of integers representing stock prices for each day
     */
    public static void getProfitActions(int prices[]) {
        System.out.println("\nProfit actions: ");

        for (int today = 0; today < prices.length; today++) {

            if (today == prices.length - 1 || prices[today] > prices[today + 1]) {
                System.out.println("Opt out at: " + prices[today]);

                if (today != prices.length - 1) {
                    System.out.println("Next buy at: " + prices[today + 1]);
                }

                System.out.println();
            }
        }
    }

    /**
     * Calculates the maximum profit achievable from stock prices over several
     * days by performing multiple buy and sell transactions.
     *
     * The algorithm works by summing all the profitable differences between
     * consecutive days. Whenever today's price is higher than yesterday's
     * price, we assume a buy at yesterday's price and a sell at today's price,
     * adding the difference to the total profit.
     *
     * @param pricesPerDay an array of integers representing the stock price for
     * each day
     * @return the maximum profit achievable by performing multiple transactions
     *
     * Example: For prices = {45, 12, 45, 12, 45, 112, 98, 101} The function
     * will calculate: 12 → 45 = +33 12 → 45 = +33 45 → 112 = +67 98 → 101 = +3
     * Total profit = 136
     */
    public static int maximumProfit(int pricesPerDay[]) {
        int profit = 0;

        for (int today = 1; today < pricesPerDay.length; today++) {
            // If today's price is greater than yesterday's, then we are in profit.
            if (pricesPerDay[today] > pricesPerDay[today - 1]) {
                // Calculate the profit points.
                profit += pricesPerDay[today] - pricesPerDay[today - 1];
            }

            // If the yesterday process are higher then don't need to do anything.
        }

        return profit;
    }

    public static void main(String[] args) {
        // int prices[] = {45, 12, 45, 12, 9, 45, 112, 98, 101};
        int prices[] = {100, 80, 60, 70, 90};

        System.out.println("Prices (per day): " + Arrays.toString(prices));

        getProfitActions(prices);
        System.out.println("Max profit: " + maximumProfit(prices));
    }
}
