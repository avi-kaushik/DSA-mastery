# 012 — Sliding Window

> **One-line takeaway:** A **window** is a contiguous range `[left, right]` plus a running summary of what's inside (its sum, count, frequencies…). When the window moves, only the element that **enters** and the one that **leaves** change the summary — so update it in `O(1)` instead of recomputing. Fixed windows slide; variable windows **grow on the right and shrink on the left**. Every element enters and leaves once → `O(n)`.

---

## 1. The Problem This Concept Solves

> *Maximum sum of any `k` consecutive elements.*

The direct way sums every window from scratch:

```text
[ 1  8  30 −5  20  7 ],  k = 3

window 0…2    1 + 8 + 30          = 39
window 1…3        8 + 30 − 5      = 33
window 2…4            30 − 5 + 20 = 45
window 3…5               −5 + 20 + 7 = 22
```

Look at two neighbouring windows: they share `k − 1` elements. Recomputing them is pure
waste — `O(n·k)` total, which is `O(n²)` when `k` is around `n/2`.

---

## 2. The Core Idea — Only the Edges Change

> 🔑 **next summary = previous summary − what left + what entered**

```text
[ 1   8  30 ] −5  20   7        sum = 39
  1 [ 8  30  −5 ] 20   7        sum = 39 − 1 + (−5) = 33
  ↑ leaves     ↑ enters
```

**Analogy — a train window.** As the train moves, one tree slides out of view on one side and
one new tree slides in on the other. To count the trees you can see, you don't recount the
whole view — you add one and subtract one.

---

## 3. Fixed-Size Windows

The window is always exactly `k` wide.

```cpp
int sum = 0;
for (int i = 0; i < k; i++) sum += arr[i];      // build the first window once
int best = sum;                                  // seed with a REAL window

for (int i = k; i < n; i++) {
    sum += arr[i] - arr[i - k];                  // arr[i] enters, arr[i − k] leaves
    best = max(best, sum);
}
```

```text
first window [1 8 30]           sum 39   best 39
+(−5) −1                        sum 33   best 39
+20   −8                        sum 45   best 45
+7    −30                       sum 22   best 45   → 45
```

`O(n)` time regardless of `k`, `O(1)` space.

> Seed `best` with the first window's sum, not `0` — an all-negative array would otherwise
> return `0` (the [seeding lesson](004-one-pass-thinking.md) again).

---

## 4. Variable-Size Windows

Now the window's size is **part of the answer**:

> *Is there a contiguous subarray (non-negative numbers) whose sum is exactly `S`?*

```text
window sum  <  S    →  too small: let the next element in (grow right)
window sum  >  S    →  too big:   push the oldest element out (shrink left)
window sum  == S    →  found
```

```cpp
int left = 0, sum = 0;
for (int right = 0; right < n; right++) {
    sum += arr[right];                       // 1. grow
    while (sum > S && left <= right)         // 2. shrink while too big
        sum -= arr[left++];
    if (sum == S && left <= right) return true;   // 3. check (non-empty)
}
return false;
```

```text
[1, 4, 20, 3, 10, 5],  S = 33

add 1, 4, 20, 3        window [1 4 20 3]     sum 28   too small
add 10                 window [1 4 20 3 10]  sum 38   too big
  drop 1               window [4 20 3 10]    sum 37   too big
  drop 4               window [20 3 10]      sum 33   ✅
```

### The general template

```cpp
int left = 0;
for (int right = 0; right < n; right++) {
    add(arr[right]);                        // expand
    while (window is invalid)               // repair
        remove(arr[left++]);
    record answer for [left, right];        // use
}
```

Most variable-window problems differ only in *what "invalid" means* and *what you record*:

| Problem | Invalid when… | Record |
|---|---|---|
| Subarray with sum `S` (non-negative) | `sum > S` | found if `sum == S` |
| Longest subarray with sum `≤ S` (non-negative) | `sum > S` | max length |
| Longest run of 1s with up to `k` flips | zeros `> k` | max length |
| Longest substring without repeats | some count `> 1` | max length |
| Smallest subarray with sum `≥ S` | — shrink **while** `sum ≥ S`, recording each time | min length |

---

## 5. Why It's `O(n)` — Despite the Nested Loop

```text
right moves forward n times.
left  moves forward, never back, and never past right → at most n times in total.
Total work ≤ 2n.
```

> 🔑 A `while` inside a `for` is **not** automatically `O(n²)`. Count how far each pointer can
> travel over the **whole run**. This amortised argument is the same one behind dynamic
> arrays ([note 003](003-static-vs-dynamic-arrays.md)).

---

## 6. The Precondition — Monotonicity

Shrinking is only safe if "too big" can only be fixed by removing from the left — i.e. if
the summary moves in **one direction** as the window grows.

```text
non-negative numbers:  grow → sum never decreases · shrink → sum never increases   ✅ monotonic
with negatives:        a later negative could bring a "too big" sum back down        ❌
```

### 🧠 Pause and predict

What does the variable window return for `[5, −3, 2]` with `S = 2`?

<details>
<summary>Answer</summary>

`false` — but `[5, −3]` and `[2]` both sum to `2`.

```text
add 5      sum 5 > 2 → drop 5 → empty
add −3     sum −3
add 2      sum −1                         never equals 2 → false ❌
```

Dropping `5` was the mistake: the `−3` that came next would have fixed the sum. With
negatives, use **prefix sums + a hash set** instead — see [Prefix Sums §6](013-prefix-sums.md).

</details>

> ⚠️ Before writing a variable window, say out loud **why shrinking never throws away an
> answer**. If you can't, the window is the wrong tool.

---

## 7. Which Summaries Can Slide?

Removing an element from the summary must be cheap.

| Summary | Remove the leaving element by… | Slides in `O(1)`? |
|---|---|---|
| sum, count | subtracting | ✅ |
| frequency map | decrementing its count | ✅ (hash map) |
| number of distinct values | decrement; drop the key at 0 | ✅ |
| **max / min** | ??? — if the max leaves, who's next? | ❌ needs a **monotonic deque** |

> 💡 A max can't be "un-added". That's why "maximum of every window of size `k`" uses a
> deque that keeps candidates in decreasing order — a different tool built on the same window.

---

## 8. Sliding Window vs Its Cousins

```text
two pointers (note 005)      two indices with separate jobs (converge, read/write)
sliding window               two indices that bound ONE contiguous range + its summary
runs (note 008)              a window that resets the moment the condition fails
prefix sums (note 013)       any range sum in O(1) — no monotonicity needed
```

A sliding window is a two-pointer technique where everything **between** the pointers
matters. A run is a window with zero tolerance for bad elements.

---

## 9. Recognising Sliding-Window Problems

```text
"k consecutive elements", "every window of size k"             → fixed window
"longest / shortest contiguous subarray (substring) such that …" → variable window
"at most k …", "no more than k distinct / zeros / changes"       → variable window
… and the values are non-negative (or the condition is monotonic)
```

If the condition **isn't** monotonic (negatives in a sum), reach for prefix sums instead.

---

## 10. Common Mistakes

| ✗ Mistake | Why it's wrong |
|---|---|
| Recomputing each window | `O(n·k)` instead of `O(n)` |
| Removing `arr[i − k + 1]` instead of `arr[i − k]` | The window size drifts |
| Seeding the best with `0` | All-negative arrays return `0` |
| `if` instead of `while` when shrinking | One removal may not be enough |
| Variable window on data with negatives | Silently wrong answers |
| Accepting an empty window | `S = 0` would always be "found" |
| Expecting a sum-style window to track the max | Max can't be removed — use a deque |

---

## 11. Practice in This Repo

| Problem | Window | 📄 |
|---|---|---|
| Maximum sum of `k` consecutive elements | fixed | [`sliding_window.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sliding_window.cpp) |
| Subarray with a given sum (non-negative) | variable | [`sliding_window.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sliding_window.cpp) |
| Distinct elements in every window | fixed + frequency map | [`CountDistinctElementsInEveryWindow.java`](../../programs/java/hashing/intermediate/CountDistinctElementsInEveryWindow.java) |
| Maximum of every window | fixed + monotonic deque | [`MaximumsOfSubArraysWithGivenSize.java`](../../programs/java/dequeue/intermediate/MaximumsOfSubArraysWithGivenSize.java) |

> ⚠️ The demo array in `sliding_window.cpp` contains `−5`, outside the documented
> "non-negative" assumption of the subarray-sum function. It happens to find the answer —
> by luck, not by guarantee.

---

## 12. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **The core idea?** | Keep a summary of the window; update it with what enters and what leaves. |
| **Fixed-window update?** | `sum += arr[i] − arr[i − k]`. |
| **Variable-window loop?** | Grow right; **while** invalid, shrink left; record. |
| **Why `O(n)` with a nested `while`?** | Each pointer moves forward at most `n` times in total. |
| **The precondition?** | Monotonicity — e.g. non-negative numbers for sums. |
| **Counter-example with negatives?** | `[5, −3, 2]`, `S = 2` → window says no, answer is yes. |
| **What to use with negatives?** | Prefix sums + hash set. |
| **Which summaries slide easily?** | Sums, counts, frequency maps — anything you can "un-add". |
| **Window maximum?** | Monotonic deque. |
| **Window vs run?** | A run is a window that tolerates zero bad elements. |

---

## 13. The Mental Model

```text
                contiguous range [left, right] + its SUMMARY
                                  │
                 update in O(1): − what left, + what entered
                                  │
          ┌───────────────────────┴───────────────────────┐
          ↓                                               ↓
    FIXED size k                                   VARIABLE size
  + arr[i] − arr[i − k]                  grow right · WHILE invalid shrink left
          │                                               │
  best sum of k consecutive                 needs MONOTONICITY (non-negatives)
                                                          │
          └────── each element in & out once → O(n) ─────┘
                                  │
        negatives? → prefix sums + hash  ·  window max? → deque
```
