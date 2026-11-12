# 010 — Kadane's Algorithm: Thinking in "Best Ending Here"

> **One-line takeaway:** To find the best **contiguous** subarray, stop asking "where does it start?" and ask "what's the best subarray that **ends right here**?" That answer depends only on the same answer one step back: either **extend** it or **start fresh**. A negative running sum can only drag you down, so drop it. One pass, `O(n)` time, `O(1)` space — and the "best ending here" idea solves a whole family of problems.

---

## 1. The Problem This Concept Solves

> *Find the largest sum of a contiguous, non-empty subarray.*

```text
[ 2  3  −8 | 7  −1  2  3 ]    →   11     (7 − 1 + 2 + 3)
[ −3  −1  −2 ]                →   −1     (all negative: the best single element)
[ 5  −2  5 ]                  →    8     (worth crossing the dip)
```

There are `n(n+1)/2` subarrays. Trying them all costs `O(n³)` (sum each from scratch) or
`O(n²)` (extend a running sum from each start). Both ask *"where does the best subarray
start?"* — and that question has too many answers.

### Subarray ≠ subsequence

| | Contiguous? | Count | "Max sum" answer |
|---|---|---|---|
| **Subarray** | ✅ | `n(n+1)/2` | Kadane |
| Subsequence / subset | ❌ | `2ⁿ` | add up all positives (or the max element if none) |

> ⚠️ "Subarray" and "substring" always mean **contiguous**. Mixing these up answers the
> wrong question.

---

## 2. The Core Idea — Fix the End, Not the Start

Every subarray ends **somewhere**. So if we knew, for every index `i`,

```text
best_ending_here[i]  =  the largest sum of a subarray that ENDS EXACTLY at i
```

then the answer is just the largest of those `n` values.

And `best_ending_here[i]` is easy, because a subarray ending at `i` can only be one of two
things:

```text
1. just arr[i] on its own                        → start fresh
2. arr[i] glued onto a subarray ending at i − 1  → and the best such is best_ending_here[i−1]
```

```text
best_ending_here[i] = max( arr[i],  best_ending_here[i − 1] + arr[i] )
                           start new     extend
```

> 🔑 **The whole insight:** if the best sum ending just before me is **negative**, carrying
> it only makes me worse — so start fresh. If it's positive (even if it went down a bit),
> keep it.

**Analogy — inheriting a business.** You can take over the shop next door (with its ledger)
or open your own from zero. If the ledger is in the red, you'd rather start fresh. If it's
in profit — even after a bad day — you keep it.

---

## 3. From Recurrence to `O(1)` Space

That recurrence is **dynamic programming**: each state depends only on the previous one. So
you don't need the array of `best_ending_here` values — one variable is enough.

```cpp
int best = arr[0], current = arr[0];          // seed with a real element
for (int i = 1; i < n; i++) {
    current = max(arr[i], current + arr[i]);  // best sum ending at i: extend or restart
    best    = max(best, current);             // best sum ending anywhere so far
}
```

| Variable | Means |
|---|---|
| `current` | best sum of a subarray **ending at `i`** |
| `best` | best sum of a subarray ending **anywhere so far** — the answer |

> It's called space-optimised DP. Some call it greedy, because each step makes a local
> "extend or restart" choice. Both views are fair; the **DP view** is the one that
> generalises.

### 🔍 Watch it run — `[2, 3, −8, 7, −1, 2, 3]`

```text
i    arr   extend   restart   current   best
0     2      —         —         2        2
1     3      5         3         5        5
2    −8     −3        −8        −3        5
3     7      4         7    ←    7        7     the carried −3 is dead weight → restart
4    −1      6        −1         6        7     still positive → keep it through the dip
5     2      8         2         8        8
6     3     11         3        11       11  ✅
```

### 🧠 Pause and predict

Someone "simplifies" the rule to *restart whenever `arr[i]` is negative*. What does that
return for `[5, −2, 5]`?

<details>
<summary>Answer</summary>

`5` instead of `8`. The rule is about the **running sum** being negative, not the current
element. A single negative element inside a strong run (`5 + (−2) = 3 > 0`) is worth
carrying, because what comes next (`+5`) more than makes up for it.

</details>

---

## 4. Why It's Correct

Take the true best subarray, and say it ends at index `e`. At step `e`, `current` holds the
best sum of **any** subarray ending at `e` — so it's at least as large as the true best. And
`best` records the largest `current` ever seen. So `best` can't miss it. ∎

The only claim to trust is the recurrence: a best subarray ending at `i` is either `arr[i]`
alone, or `arr[i]` plus a best subarray ending at `i − 1` (if it used a worse one, swapping
in the best one would improve it).

---

## 5. Seeding and the All-Negative Case

```text
seed with arr[0]       [−3, −1, −2]  →  −1   ✅  (largest single element)
seed with 0            [−3, −1, −2]  →   0   ❌  (that's the EMPTY subarray)
```

You'll often see this equivalent "reset to zero" form:

```cpp
int best = arr[0], current = 0;
for (int x : arr) {
    current += x;
    best = max(best, current);     // 1. record first
    if (current < 0) current = 0;  // 2. then drop a negative prefix
}
```

Same algorithm. Swap steps 1 and 2 and the all-negative case breaks.

> If the problem **allows** an empty subarray (answer `0`), say so and seed with `0`.

---

## 6. The Family — Same Idea, Different State

"Best ending here" is a **way of thinking**, not just one algorithm.

| Problem | State ending at `i` | Rule |
|---|---|---|
| Max subarray sum | best sum | `max(x, cur + x)` |
| Min subarray sum | smallest sum | `min(x, cur + x)` |
| Longest streak of 1s ([note 008](008-runs-and-boundaries.md)) | streak length | `x == 1 ? cur + 1 : 0` |
| Longest alternating even-odd run | run length | parity differs ? `cur + 1` : `1` |
| Max product subarray | **both** largest and smallest product | a negative swaps them |
| Max subarray **with its indices** | best sum + its start | remember `start` on every restart |

### Max product — why you need two states

A negative number turns the **smallest** product into the **largest**:

```cpp
int best = arr[0], hi = arr[0], lo = arr[0];
for (int i = 1; i < n; i++) {
    if (arr[i] < 0) swap(hi, lo);        // a negative flips max ↔ min
    hi = max(arr[i], hi * arr[i]);
    lo = min(arr[i], lo * arr[i]);
    best = max(best, hi);
}
```

```text
[2, −3, −4]   →  24      the "worst" product −6 times −4 becomes the best
```

> 💡 When one number isn't enough to summarise "ending here", carry two. That's the general
> lesson of DP state design.

---

## 7. Circular Subarrays — the Complement Trick

On a **ring** (the last element is followed by the first), the best subarray either wraps
around the end or it doesn't.

```text
doesn't wrap     →  plain Kadane

wraps            →  [ a a a | m m m m | b b b ]
                      kept      LEFT OUT   kept      the wrap is b b b + a a a

wrap sum = total − (sum of the middle part left out)
         → maximise the wrap  ⇔  MINIMISE the middle
```

> 🔑 **A wrapping subarray is the complement of a non-wrapping one.** Run max-Kadane and
> min-Kadane in the same loop, then answer `max(kadane_max, total − kadane_min)`.

```text
[2, 3, −8, 7, −1, 2, 3]     kadane_max 11 · kadane_min −8 · total 8
                            wrap = 8 − (−8) = 16      answer = max(11, 16) = 16
                            (7 − 1 + 2 + 3 + 2 + 3, wrapping past the end)
```

**One exception:** if every element is negative, the minimum subarray is the whole array and
`total − kadane_min = 0` — the **empty** subarray. So when `kadane_max < 0`, return
`kadane_max`.

This is the ring view from [Circular Thinking](006-circular-thinking-and-rotation.md) in action.

---

## 8. Recognising the Concept

```text
"maximum / minimum sum (or product) of a CONTIGUOUS subarray"     → Kadane
"longest/best stretch where …"                                    → best ending here
"circular array" + subarray sum                                   → Kadane + complement
"max sum of a sub-rectangle in a matrix"                          → fix two rows, Kadane on columns
```

**Not Kadane:** a fixed length `k` ([Sliding Window](012-sliding-window.md)); "sum equals
exactly `k`" ([Prefix Sums](013-prefix-sums.md)); non-contiguous selections (just take the
positives).

---

## 9. Common Mistakes

| ✗ Mistake | Why it's wrong |
|---|---|
| Restarting on a negative **element** | Only a negative **running sum** should trigger a restart |
| Seeding with `0` | All-negative arrays return `0` |
| Resetting before recording `best` | Same all-negative bug |
| Returning `current` instead of `best` | That's only the best sum ending at the last index |
| Max product with a single state | Misses negative × negative |
| Circular without the all-negative guard | Returns `0` (empty subarray) |

---

## 10. Practice in This Repo

| Problem | Idea | 📄 |
|---|---|---|
| Maximum subarray sum | extend or restart | [`sub_array.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sub_array.cpp) |
| Maximum circular subarray sum | max-Kadane + min-Kadane, complement | [`sub_array.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sub_array.cpp) |
| Longest even-odd subarray | "best ending here" as a run length | [`sub_array.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sub_array.cpp) |

---

## 11. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **The question Kadane asks at each index?** | What's the best subarray ending **here**? |
| **The rule?** | `cur = max(arr[i], cur + arr[i])`, `best = max(best, cur)`. |
| **When do you restart?** | When the carried running sum is negative. |
| **DP or greedy?** | Space-optimised DP over "best ending here". |
| **All-negative array?** | Seed with `arr[0]` → the largest element. |
| **Max product?** | Carry both max and min ending here; a negative swaps them. |
| **Circular version?** | `max(kadane_max, total − kadane_min)`, unless all are negative. |
| **Why does the circular trick work?** | A wrapping subarray is the complement of a middle one. |
| **Complexity?** | `O(n)` time, `O(1)` space. |

---

## 12. The Mental Model

```text
          every subarray ends SOMEWHERE
                       │
      best ending at i = arr[i] alone  OR  arr[i] + best ending at i − 1
                       │
          carried sum negative? → restart  ·  else → extend
                       │
              best = max over all endings
                       │
    ┌──────────────────┼────────────────────┐
    ↓                  ↓                    ↓
 more state         other rules          ring
 (max AND min       (min sum, streaks,   total − min subarray
  for products)      run lengths)        (unless all negative)
```
