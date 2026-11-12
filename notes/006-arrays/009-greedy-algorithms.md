# 009 — Greedy Algorithms

> **One-line takeaway:** A greedy algorithm makes the **choice that looks best right now**, commits to it, and never looks back. It's usually the fastest and simplest solution — but it's only **correct** when you can argue that a locally best choice can never block a globally best answer. The skill is not writing greedy code; it's knowing **when greedy is allowed**.

---

## 1. The Problem This Concept Solves

Optimisation problems ask for the best of many possible answers: the fewest coins, the most
meetings, the biggest profit. The honest way is to try every combination — usually
exponential. Greedy bets that you can skip almost all of them:

```text
repeat until done:
    pick the option that looks best RIGHT NOW
    commit to it — never undo
```

When the bet is safe, an exponential search becomes a single pass (often after sorting).
When it isn't, you get a fast, confident, **wrong** answer.

---

## 2. The Core Idea — Local Choices, Never Revisited

**Analogy — hiking in thick fog.** You can only see one step ahead, so you always step
uphill. On a single smooth hill, that takes you straight to the summit. In a mountain range,
it can strand you on a small peak while the real summit is somewhere else.

```text
single hill (greedy works)          mountain range (greedy fails)

        ▲  ← you end here                ▲            ▲▲  ← real summit
       ╱ ╲                              ╱ ╲  you    ╱  ╲
      ╱   ╲                            ╱   ╲ stop  ╱    ╲
  ───╱     ╲───                    ───╱     ╲─────╱      ╲───
```

> 🔑 Greedy is perfect for "single hill" problems and wrong for "mountain range" ones.
> Every greedy question is really asking: **which kind of landscape is this?**

### Greedy vs the alternatives

| | Brute force | Dynamic programming | **Greedy** |
|---|---|---|---|
| Explores | every possible solution | every **subproblem**, once | **one** path |
| Revisits choices | yes | effectively (tries every option per state) | never |
| Typical speed | exponential | polynomial | `O(n)` or `O(n log n)` |
| Always correct? | yes | yes (if the recurrence is right) | **only for some problems** |

---

## 3. When Is Greedy Allowed?

Two properties must hold:

```text
greedy-choice property    some optimal solution starts with the greedy choice
optimal substructure      after that choice, what's left is the same problem, smaller
```

DP needs optimal substructure too. The **greedy-choice property** is what lets greedy skip
trying the alternatives.

### How to argue it in an interview

**1. The exchange argument** (most common):

> *"Take any optimal solution. If it doesn't make my greedy choice, swap the greedy choice
> in. The result is no worse. So there's always an optimal solution that agrees with
> greedy — and I can repeat this at every step."*

**2. Greedy stays ahead:**

> *"After every step, greedy's partial solution is at least as good as anyone else's after
> the same number of steps — so it's at least as good at the end."*

If you can't make either argument, **hunt for a counter-example** before writing code.
Small, adversarial inputs kill wrong greedy ideas fast.

---

## 4. When Greedy Fails — the Classic Counter-Example

**Fewest coins** to make an amount, by always taking the biggest coin that fits:

```text
coins {1, 2, 5, 10, 20, 50, 100, 200, 500, 2000}, make 76
greedy:   50 + 20 + 5 + 1   →  4 coins   ✅ optimal
```

```text
coins {1, 3, 4}, make 6
greedy:   4 + 1 + 1         →  3 coins
optimal:  3 + 3             →  2 coins   ❌
```

Taking the `4` *looked* best but **blocked** the better combination. Real currencies are
designed so greedy works ("canonical" coin systems); arbitrary coin sets aren't — those
need DP.

### 🧠 Pause and predict

Fractional knapsack (you may take part of an item) is solved greedily by best value per
kilogram. Why does the same rule fail for **0/1** knapsack (whole items only)?

<details>
<summary>Answer</summary>

With whole items, a high value-per-kg item can **waste capacity**: it might leave a gap
nothing else fits into, while two slightly worse items would have filled the bag exactly.
Taking it can block a better combination — the coin-change failure again. With fractions,
there's never a gap: you top up with part of the next item, so the greedy choice can't
block anything.

```text
capacity 50    items (weight, value): (10, 60) (20, 100) (30, 120)
ratio-greedy, whole items:  10 + 20 = 30 kg → value 160 (30 kg item no longer fits)
optimal:                    20 + 30 = 50 kg → value 220
```

</details>

> 🔑 **Warning sign:** if committing to one choice can rule out a better combination later,
> greedy is suspect — reach for DP.

---

## 5. Worked Example — Stock Buy & Sell, Unlimited Trades

`arr[i]` is a stock price on day `i`. Buy and sell as often as you like, holding at most one
share at a time. Maximise profit.

```text
[1, 5, 8, 3, 12]   →  16      (buy 1 sell 8, buy 3 sell 12)
```

### The greedy choice

> **Take every upward day-to-day step. Skip every downward one.**

```text
prices    1 → 5 → 8 → 3 → 12
steps       +4  +3  −5  +9
take        +4  +3   ✗  +9      = 16
```

```cpp
int profit = 0;
for (int i = 1; i < n; i++)
    if (arr[i] > arr[i - 1]) profit += arr[i] - arr[i - 1];
```

### Why it's allowed here

Any climb from a valley `a` to a peak `b` is the sum of the daily rises inside it — a
**telescoping** sum:

```text
b − a  =  (x₁ − a) + (x₂ − x₁) + … + (b − xₖ)
```

- Taking a rise can only **add** profit.
- Holding through a fall can only **lose** profit.
- Taking one rise never **blocks** another — you can sell today and buy again today.

That third point is exactly what fails in the coin example. Here no choice blocks any other,
so the landscape is a "single hill" and greedy is exact.

> ⚠️ The `+4, +3` steps are the **profit**, not a literal trading plan — `+4 +3` is the same
> as holding from day 0 to day 2.

### Where greedy stops working — the stock family

| Variant | Tool | Why |
|---|---|---|
| **One** trade | running prefix min | best `price − cheapest earlier price` ([note 007](007-prefix-suffix-extremes.md)) |
| **Unlimited** trades | greedy | rises never block each other |
| At most **k** trades | DP, `O(n·k)` | now you must choose *which* rises to keep — choices block each other |
| With a **fee** per trade | DP (2 states) | a small rise may not be worth a fee |
| With a **cooldown** | DP (3 states) | selling today blocks buying tomorrow |

> 💡 The moment trades are **limited or charged**, choices start blocking each other, and
> greedy hands over to DP. That transition is a great thing to say out loud in an interview.

---

## 6. Greedy Classics to Recognise

| Problem | Greedy rule | Why it's safe |
|---|---|---|
| Activity selection (max non-overlapping intervals) | sort by **end** time; take whatever starts after the last end | finishing earliest leaves the most room |
| Fractional knapsack | best value/weight first, split the last item | no gaps can form |
| Minimum coins (canonical coins) | largest coin that fits | the coin system is designed for it |
| Minimum platforms / meeting rooms | process events in time order | coverage only depends on order |
| Huffman coding | merge the two rarest symbols | exchange argument |
| Dijkstra (non-negative weights) | finalise the closest unvisited vertex | nothing can make it closer later |
| Kruskal / Prim | cheapest safe edge | cut property |
| Stock, unlimited trades | take every rise | rises never block each other |

> 💡 Many greedy algorithms **sort first**, then make one pass — which is why `O(n log n)` is
> the most common greedy complexity.

---

## 7. Recognising Greedy Problems

```text
"maximum number of …", "minimum number of …"  with intervals, deadlines, or weights
"schedule", "select", "assign" with a natural ordering (end time, ratio, size)
choices that don't constrain each other later
```

…and the honest test: *can I find a small input where the obvious greedy choice loses?*
If yes → DP. If you can't, and you can sketch an exchange argument → greedy.

---

## 8. Common Mistakes

| ✗ Mistake | Why it's wrong |
|---|---|
| Assuming greedy works without an argument | Coins `{1, 3, 4}` |
| Activity selection sorted by **start** time | One long early activity blocks many short ones |
| Greedy on 0/1 knapsack | High-ratio items can waste capacity |
| Using the unlimited-trades rule for a single trade | Over-counts profit |
| Not testing small counter-examples | The fastest way to kill a wrong idea |

---

## 9. Practice in This Repo

| Problem | Greedy choice | 📄 |
|---|---|---|
| Stock buy & sell, unlimited trades | take every positive step | [`stocks.cpp`](../../programs/cpp/data_structures/arrays/intermediate/stocks.cpp) |
| Stock buy & sell, one trade | *not greedy* — prefix min | [`maximum_difference.cpp`](../../programs/cpp/data_structures/arrays/basic/maximum_difference.cpp) |

---

## 10. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **What is greedy?** | Take the locally best choice each step, never revisit it. |
| **When is it correct?** | Greedy-choice property + optimal substructure. |
| **How do you justify it?** | Exchange argument, or "greedy stays ahead". |
| **Classic failure?** | Coins `{1, 3, 4}`, amount 6: greedy 3 coins, optimal 2. |
| **Fractional vs 0/1 knapsack?** | Fractional → greedy by ratio. 0/1 → DP. |
| **Activity selection rule?** | Sort by end time; take what starts after the last end. |
| **Stock, unlimited trades?** | Sum every positive day-to-day difference. |
| **Why does that work?** | A valley-to-peak gain telescopes into its daily rises; rises never block each other. |
| **When does stock need DP?** | Limited trades, fees, or cooldowns — choices start blocking each other. |
| **Typical complexity?** | `O(n log n)` (sort + pass) or `O(n)`. |

---

## 11. The Mental Model

```text
                    the greedy choice
                           │
          can it ever BLOCK a better future?
             ╱                          ╲
           no                           yes
            │                            │
   argue it (exchange /            use DP / search
   stays ahead), then code               │
            │                    coins {1,3,4} · 0/1 knapsack
   single hill:                  stock with k trades / fee
   activity selection ·
   fractional knapsack ·
   stock with unlimited trades
```
