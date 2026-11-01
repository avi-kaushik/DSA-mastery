# 009 — Greedy Algorithms & Stock Buy and Sell

> **One-line takeaway:** A greedy algorithm takes the **best-looking choice right now** and never looks back. It's fast and simple — but only **correct** when you can argue that a locally best choice never blocks a globally best answer. Stock buy & sell (unlimited trades) is the poster child: **collect every upward step**.

📄 [`stocks.cpp`](../../programs/cpp/data_structures/arrays/intermediate/stocks.cpp)

---

## 1. What Is a Greedy Algorithm?

> **A greedy algorithm builds a solution step by step, always making the choice that looks
> best at that moment, and never undoing a choice.**

```text
repeat until done:
    pick the option that looks best RIGHT NOW
    commit to it — never revisit
```

**Analogy — hiking in fog.** You can only see one step ahead, so you always step uphill.
On a single smooth hill, that gets you to the summit. On a mountain range, it can strand you
on a small peak with the real summit somewhere else. Greedy is perfect for "single hill"
problems and wrong for "mountain range" ones — the skill is telling them apart.

### Greedy vs the alternatives

| | Brute force | Dynamic programming | **Greedy** |
|---|---|---|---|
| Explores | every possible solution | every **subproblem**, once | **one** path |
| Revisits choices | yes | effectively yes (tries all options per state) | never |
| Typical speed | exponential | polynomial (`O(n²)`, `O(n·k)`, …) | `O(n)` or `O(n log n)` |
| Correct? | always | always (if the recurrence is right) | **only for some problems** |

> 💡 Many greedy algorithms **sort first**, then make one pass. That's why `O(n log n)` is
> the most common greedy complexity.

---

## 2. When Does Greedy Work?

Two properties must hold:

```text
greedy-choice property    some optimal solution begins with the greedy choice
optimal substructure      after making that choice, what's left is the same
                          problem, smaller — and its optimum completes the answer
```

DP also needs optimal substructure. The **greedy-choice property** is what lets greedy skip
trying the alternatives.

### How to justify a greedy in an interview

**1. Exchange argument** (most common):

> *"Take any optimal solution. If it doesn't make my greedy choice, swap the greedy choice
> in. The result is no worse. So there's always an optimal solution that agrees with greedy
> — repeat for every step."*

**2. Greedy stays ahead:**

> *"After every step, greedy's partial solution is at least as good as any other
> algorithm's partial solution after the same number of steps. So it's at least as good
> at the end."*

If you can't make either argument, **look for a counter-example** before coding.

---

## 3. When Greedy Fails — the Classic Counter-Example

**Minimum coins** to make an amount:

```text
coins {1, 2, 5, 10, 20, 50, 100, 200, 500, 2000}  (Indian currency), make 76
greedy (largest first):  50 + 20 + 5 + 1   →  4 coins   ✅ optimal
```

```text
coins {1, 3, 4}, make 6
greedy (largest first):  4 + 1 + 1   →  3 coins
optimal:                 3 + 3       →  2 coins   ❌ greedy loses
```

Taking the `4` *looked* best but **blocked** the better combination. Real currencies are
designed ("canonical") so that greedy works; arbitrary coin sets aren't — those need DP.

> 🔑 **The warning sign:** if committing to one choice can rule out a better combination
> later, greedy is suspect. Reach for DP.

The same story with knapsack:

| Problem | Greedy? |
|---|---|
| **Fractional** knapsack (can take part of an item) | ✅ best value/weight first |
| **0/1** knapsack (whole items only) | ❌ needs DP — a high-ratio item can waste capacity |

---

## 4. Greedy Classics to Recognise

| Problem | Greedy rule |
|---|---|
| Activity selection / max non-overlapping intervals | Sort by **end** time; take each activity that starts after the last one ended |
| Fractional knapsack | Sort by value/weight, take greedily, split the last item |
| Minimum coins (canonical coins) | Largest coin that fits |
| Job sequencing with deadlines | Highest profit first, place in the latest free slot before its deadline |
| Huffman coding | Repeatedly merge the two least-frequent nodes |
| Dijkstra (non-negative weights) | Always finalise the closest unvisited vertex |
| Kruskal / Prim (minimum spanning tree) | Cheapest edge that doesn't form a cycle / cheapest edge out of the tree |
| **Stock buy & sell, unlimited trades** | Take every positive day-to-day rise — the card below |

---

# Card — Stock Buy & Sell (Unlimited Transactions)

**Greedy** · 📄 [`stocks.cpp`](../../programs/cpp/data_structures/arrays/intermediate/stocks.cpp)

### 🎯 Problem

`arr[i]` is a stock's price on day `i`. You may buy and sell as many times as you like, but
you can hold **at most one share** at a time (sell before you buy again). Maximise total
profit.

```text
[1, 5, 8, 3, 12]   →   16      (buy 1 sell 8, buy 3 sell 12)
[5, 4, 3, 2]       →    0      (never trade)
```

### Three approaches

| Approach | Idea | Time |
|---|---|---|
| Brute force | try every set of non-overlapping buy/sell pairs, recursively | exponential |
| Valley → peak | buy at every local minimum, sell at the next local maximum | `O(n)` |
| **Sum of rises** (implemented) | add every positive `arr[i] − arr[i−1]` | `O(n)` |

### 💡 Insight

> **Every valley-to-peak climb equals the sum of the daily rises inside it.** So instead of
> finding valleys and peaks, just add up every positive day-to-day difference and skip
> every fall.

```text
prices    1 → 5 → 8 → 3 → 12
diffs       +4  +3  −5  +9
take        +4  +3   ✗  +9      =  16

valley/peak:  (8 − 1) + (12 − 3)  =  7 + 9  =  16       same answer
```

### 💻 Code — sum of rises (implemented)

```cpp
int get_maximum_profit(const int arr[], int size)
{
    if (size <= 1) return 0;                     // need two days to trade

    int profit = 0;
    for (int i = 1; i < size; i++)
        if (arr[i] > arr[i - 1])                 // greedy: grab every upward step
            profit += arr[i] - arr[i - 1];
    return profit;
}
```

### 💻 Code — valley → peak (when you need the actual trades)

```cpp
int i = 0, profit = 0;
while (i < size - 1) {
    while (i < size - 1 && arr[i + 1] <= arr[i]) i++;   // slide down to a valley
    int buy = arr[i];
    while (i < size - 1 && arr[i + 1] >= arr[i]) i++;   // climb up to a peak
    profit += arr[i] - buy;                              // buy at valley, sell at peak
}
```

### 🔍 Trace — `[1, 5, 8, 3, 12]`

```text
i   arr[i−1] → arr[i]   rise?   profit
1      1 → 5             +4        4
2      5 → 8             +3        7
3      8 → 3             −5 ✗      7
4      3 → 12            +9       16
```

### Why greedy is *correct* here

For any climb from a valley `a` to a peak `b` through `x₁ ≤ x₂ ≤ … ≤ xₖ`:

```text
b − a  =  (x₁ − a) + (x₂ − x₁) + … + (b − xₖ)          ← telescoping sum
```

- Every **rising** step adds to profit → including it can only help.
- Every **falling** step subtracts → an optimal plan never holds a share through it.
- No choice blocks a later one: you can always sell today and rebuy today, so taking one
  rise never costs you another.

That last point is exactly what fails in the coin example — and exactly why greedy works
here.

### ⏱️ Complexity

`O(n)` time, `O(1)` space.

### ⚠️ Pitfalls

| ✗ Mistake | Result |
|---|---|
| Treating each `+diff` as a literal trade | It's the **profit**, not a plan — `+4, +3` means "hold from day 0 to day 2" |
| Using `>=` | Harmless (adds 0), but `>` states the intent |
| Applying this to the one-transaction version | Over-counts — that version is max difference ([note 007](007-running-extremes.md)) |
| Applying it with a fee per trade | Each tiny rise would pay a fee — greedy breaks |

### 🔀 The Stock Family — Know Which Tool Each Needs

| Variant | Tool | Time | Key idea |
|---|---|---|---|
| **One** transaction | Running prefix min | `O(n)` | `max(price − min_so_far)`, floored at 0 → [note 007](007-running-extremes.md) |
| **Unlimited** transactions | Greedy (this card) | `O(n)` | sum of positive diffs |
| At most **2** transactions | DP with 4 states | `O(n)` | best after buy₁, sell₁, buy₂, sell₂ |
| At most **k** transactions | DP | `O(n·k)` | greedy can't choose *which* rises to keep |
| With **cooldown** after selling | DP (state machine) | `O(n)` | states: holding / just sold / resting |
| With a **transaction fee** | DP (2 states) | `O(n)` | small rises aren't worth the fee |

> 💡 The moment the problem **limits or charges** for transactions, choices start blocking
> each other — and greedy gives way to DP.

---

## 5. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Assuming greedy works without an argument | Silent wrong answers (coins `{1,3,4}`) |
| Confusing fractional and 0/1 knapsack | Greedy on 0/1 knapsack is wrong |
| Activity selection sorted by **start** time | Wrong — one long early activity blocks many short ones |
| Using the unlimited-trades formula for one trade | Over-counts profit |
| Not testing small counter-examples | The fastest way to kill a wrong greedy idea |

---

## 6. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **What is greedy?** | Take the locally best choice each step; never revisit. |
| **When is it correct?** | Greedy-choice property + optimal substructure. |
| **How to justify greedy?** | Exchange argument, or "greedy stays ahead". |
| **Classic greedy failure?** | Coins `{1,3,4}`, amount 6: greedy gives 3 coins, optimal is 2. |
| **Fractional vs 0/1 knapsack?** | Fractional → greedy by ratio. 0/1 → DP. |
| **Activity selection rule?** | Sort by end time; take whatever starts after the last end. |
| **Stock, unlimited trades?** | Sum every positive `arr[i] − arr[i−1]`. |
| **Why does that work?** | A valley → peak gain telescopes into the sum of its daily rises. |
| **Stock, one trade?** | Track min so far; best `price − min`, floored at 0. |
| **Stock, at most k / fee / cooldown?** | DP — choices now block each other. |
| **Typical greedy complexity?** | `O(n log n)` (sort + pass) or `O(n)`. |

---

## 7. The Mental Model

```text
                     Greedy choice
                           │
           can it ever block a better future?
              ╱                          ╲
            no                           yes
             │                            │
   prove it (exchange /            use DP / search
   stays ahead), then code               │
             │                    coin change {1,3,4}
   stock (unlimited): take        0/1 knapsack
   every rise · O(n)              stock with k trades / fee
```
