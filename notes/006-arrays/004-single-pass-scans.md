# 004 — Single-Pass Scans: Largest, Second Largest, Is Sorted

> **One-line takeaway:** Walk the array **once**, carrying a tiny bit of **state** — a best-so-far, a top two, or the previous element. Most "find / check something" array problems reduce to one question: *"What do I need to remember about the elements I've already seen?"*

📄 [`largest_elements.cpp`](../../programs/cpp/data_structures/arrays/basic/largest_elements.cpp) ·
📄 [`basic_operations.cpp`](../../programs/cpp/data_structures/arrays/basic/basic_operations.cpp)

---

## How These Notes Are Organised

The first section is the shared idea. Then each problem gets a **card** in the fixed layout
from the [notes README](../README.md):

```text
🎯 Problem   💡 Insight   💻 Code   🔍 Trace   ⏱️ Complexity   ⚠️ Pitfalls   🔀 Variants
```

> **Revising in a hurry?** Read §1 and the three 💡 Insight lines.

---

## 1. The Single-Pass Template

```text
state = something built from arr[0]
for i = 1 … n−1:
    update state using arr[i]          ← the only line that changes between problems
answer = state
```

| Problem | State carried | Update rule |
|---|---|---|
| Largest | `largest` | `largest = max(largest, arr[i])` |
| Second largest | `largest`, `second`, `found` | demote or replace — Card 2 |
| Is sorted | nothing — just look at `arr[i − 1]` | any `arr[i] < arr[i − 1]` → `false` |

### Why `O(n)` is the best possible

> **Every element might be the answer.** If an algorithm skipped even one element, an
> adversary could put the maximum exactly there. So any correct algorithm must look at all
> `n` elements → `Ω(n)`, and one pass achieves it.

This "you must at least read the input" argument is the standard way to prove a lower bound
in interviews.

### Why not just sort?

```text
sort, then read the answer      O(n log n) time, and it MODIFIES the array (or copies it)
one pass with state             O(n) time, O(1) space, array untouched
```

Sorting is the answer you give only to show you know the better one.

---

# Card 1 — Largest Element

### 🎯 Problem

Return the largest value in a non-empty array.

### 💡 Insight

> **Keep the champion so far; every element challenges it once.** Seed the champion with a
> real element, never a made-up value.

### 💻 Code

```cpp
int largest_element(const int arr[], int size)
{
    if (size <= 0)
        throw out_of_range("Array must contain at least one element.");

    int largest = arr[0];                  // seed with a REAL element
    for (int i = 1; i < size; i++)
        if (arr[i] > largest)
            largest = arr[i];
    return largest;
}
```

### 🔍 Trace — `[64, 12, 1, 57, 12, 54]`

```text
seed    largest = 64
12      12 > 64? no
1       no
57      no
12      no
54      no
answer  64
```

### ⏱️ Complexity

`n − 1` comparisons → `O(n)` time, `O(1)` space.

### ⚠️ Pitfalls

| ✗ Mistake | Breaks on |
|---|---|
| Seeding `largest = 0` | `[-5, -2, -9]` returns `0` — a value that isn't in the array |
| Seeding with `INT_MIN` | Works, but hides the empty-array case (returns `INT_MIN` instead of failing) |
| No empty check | `arr[0]` on an empty array is out of bounds |

> Java: the same seed problem with `Integer.MIN_VALUE`. Seeding with `arr[0]` is
> language-independent and always correct.

### 🔀 Variants

| Follow-up | Change |
|---|---|
| Return the **index** of the largest | Carry `best_index`; compare `arr[i] > arr[best_index]` |
| First vs last index of the max | `>` keeps the first; `>=` keeps the last |
| Min **and** max together | Compare elements in **pairs**: smaller vs min, larger vs max → `3n/2` comparisons instead of `2n` |

---

# Card 2 — Second Largest Element ⭐

### 🎯 Problem

Return the largest value that is **strictly smaller** than the maximum (the second largest
*distinct* value). Report if none exists.

```text
[64, 12, 1, 57, 12, 54]   →  57
[10, 10, 5]               →  5        (not 10 — duplicates of the max don't count)
[7, 7, 7]                 →  none
```

### Three approaches

| Approach | Idea | Time | Notes |
|---|---|---|---|
| Sort | sort descending, walk past the copies of `arr[0]` | `O(n log n)` | Modifies the array |
| Two passes | pass 1: find `max`. pass 2: largest element `< max` | `O(n)` | Simplest correct `O(n)` — great under pressure |
| **One pass** | carry `largest` and `second` together | `O(n)` | What's implemented |

### 💡 Insight

> **Every element falls into exactly one of three cases:** it beats the champion (so the
> champion is **demoted** to second), it ties the champion (ignore it), or it's below the
> champion and might beat the current second.

| `arr[i]` compared with… | Action |
|---|---|
| `> largest` | `second = largest`, `largest = arr[i]` ← **demotion** |
| `== largest` | nothing — a duplicate of the max is not "second" |
| `< largest` and (`> second` or no second yet) | `second = arr[i]` |
| `<= second` | nothing |

### 💻 Code

```cpp
int second_largest_element(const int arr[], int size)
{
    if (size < 2)
        throw out_of_range("Array must contain at least two elements.");

    int largest = arr[0], second_largest = INT_MIN;
    bool found = false;                         // has a valid second been seen?

    for (int i = 1; i < size; i++)
    {
        if (arr[i] > largest)                   // new champion
        {
            second_largest = largest;           //   old one is demoted
            largest = arr[i];
            found = true;
        }
        else if (arr[i] < largest &&            // strictly below → skips duplicates of max
                 (!found || arr[i] > second_largest))
        {
            second_largest = arr[i];
            found = true;
        }
    }

    if (!found)
        throw runtime_error("No second-largest distinct element exists.");
    return second_largest;
}
```

### 🔍 Trace 1 — `[64, 12, 1, 57, 12, 54]`

```text
                    largest   second   found
seed                  64        —        no
12   < 64, no second  64       12       yes
1    < 12             64       12
57   > 12             64       57
12   < 57             64       57
54   < 57             64       57
answer → 57
```

### 🔍 Trace 2 — the demotion and duplicate cases, `[5, 10, 10, 8]`

```text
seed                  5        —        no
10   > 5  → demote    10       5        yes     ← old champion becomes second
10   == 10 → ignore   10       5                ← duplicate of max skipped
8    < 10, > 5        10       8
answer → 8
```

### Why the `found` flag exists

```text
[INT_MIN, 5]    the true answer IS INT_MIN
                if "second == INT_MIN" meant "not found", we'd wrongly report none
[7, 7, 7]       nothing is ever strictly below 7 → found stays false → no answer
```

> 💡 A **sentinel value** (`INT_MIN`, `-1`) can collide with real data. A separate boolean
> can't. Use a flag (or `optional<int>` in C++, `Integer`/`OptionalInt` in Java) whenever
> "no answer" is possible.

### ⏱️ Complexity

One pass, at most 2 comparisons per element → `O(n)` time, `O(1)` space.

### ⚠️ Pitfalls

| ✗ Mistake | Breaks on |
|---|---|
| `else if (arr[i] > second)` without `arr[i] < largest` | `[10, 10, 5]` → returns `10` |
| Forgetting the demotion | `[5, 10]` → second never becomes `5` |
| Using `INT_MIN` as "not found" | `[INT_MIN, 5]` — a valid answer is reported as missing |
| Seeding `second = arr[1]` | `[5, 10]` → `largest = 5, second = 10` — the invariant `second < largest` is broken from the start |
| No "no answer" path | `[7, 7, 7]` silently returns garbage |

### 🔀 Variants

| Follow-up | Approach |
|---|---|
| Second **smallest** | Mirror every comparison |
| Third largest | Carry three values with a cascade of demotions |
| `k`-th largest, general `k` | Min-heap of size `k` → `O(n log k)`; Quickselect → `O(n)` average |
| Second largest **allowing** duplicates (`[10,10,5]` → `10`) | Drop the `arr[i] < largest` condition; use `>=` for demotion |

---

# Card 3 — Is the Array Sorted?

### 🎯 Problem

Return `true` if the array is in non-decreasing order.

### 💡 Insight

> **Sortedness is a property of adjacent pairs.** If every neighbour pair is in order, the
> whole array is (order is transitive). One out-of-order pair is enough to answer `false`.

### 💻 Code

```cpp
bool is_sorted(const int arr[], int size)
{
    if (size <= 0)
        throw out_of_range("Array must contain at least one element.");

    for (int i = 1; i < size; i++)
        if (arr[i] < arr[i - 1])       // compare each element with its left neighbour
            return false;              // first violation → stop immediately
    return true;
}
```

### 🔍 Trace

```text
[4  9  14  18]       4≤9 ✓   9≤14 ✓   14≤18 ✓          → true   (3 comparisons)
[4  18  9  14]       4≤18 ✓  18≤9 ✗  stop               → false  (2 comparisons)
```

### ⏱️ Complexity

| Case | Comparisons | Time |
|---|---|---|
| Best — first pair is out of order | 1 | `O(1)` |
| Worst — sorted (must check every pair) | `n − 1` | `O(n)` |

Space `O(1)`. The **early exit** is why the best case is constant.

### ⚠️ Pitfalls

- `<` vs `<=` decides the definition. `arr[i] < arr[i − 1]` allows equal neighbours
  (non-decreasing). For **strictly** increasing, fail on `arr[i] <= arr[i − 1]`.
- Comparing with a **running max** instead of the neighbour works but adds a variable for
  nothing.
- The repo version **throws** on an empty array. Mathematically an empty or single-element
  array is sorted — most interviewers expect `true`. State your choice.
- The recursive version has an empty-array hole — see
  [Recursion on Arrays §4](../005-recursion/009-recursion-on-arrays.md).

### 🔀 Variants

| Follow-up | Approach |
|---|---|
| Sorted in **either** direction | Check non-decreasing **or** non-increasing (or detect the direction from the first unequal pair) |
| Is it a **rotated** sorted array? | Count "drops" (`arr[i] < arr[i−1]`) including the wrap-around pair `arr[0] < arr[n−1]`; valid if drops `≤ 1` |
| How many elements are out of place? | Different problem — compare with a sorted copy |

---

## 2. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Seeding with `0` | Wrong on all-negative arrays |
| Sentinel instead of a flag | Real values collide with "not found" |
| Treating duplicates of the max as second largest | `[10, 10, 5]` → `10` |
| Strict vs non-strict comparison chosen by accident | Wrong answer on arrays with equal neighbours |
| Sorting to find the max | `O(n log n)` for an `O(n)` problem |

---

## 3. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Pattern behind these problems?** | One pass, carry a small state, update per element. |
| **Why can't you beat `O(n)`?** | Any unread element could be the answer. |
| **Seed for largest?** | `arr[0]` — never `0` (all-negative arrays). |
| **Second largest — the three cases?** | Beats max → demote. Equals max → ignore. Below max and above second → new second. |
| **Why `arr[i] < largest` in the else branch?** | Duplicates of the max must not become the second largest. |
| **Why the `found` flag?** | `INT_MIN` can be a real value; and `[7,7,7]` has no answer. |
| **Simplest correct `O(n)` second largest?** | Two passes: find max, then the largest element `< max`. |
| **`k`-th largest?** | Heap of size `k` (`O(n log k)`) or Quickselect (`O(n)` average). |
| **How to check sorted?** | Every adjacent pair in order; return `false` on the first violation. |
| **Best case of is-sorted?** | `O(1)` — the first pair is out of order. |
| **Min and max with fewer comparisons?** | Process in pairs → `3n/2` instead of `2n`. |

---

## 4. The Mental Model

```text
          "What must I remember about what I've seen?"
                            │
        ┌───────────────────┼────────────────────┐
        ↓                   ↓                    ↓
     one best            top two             the previous
     (largest)       (second largest)        element (sorted?)
        │                   │                    │
   seed with arr[0]   demote · ignore ties    compare neighbours
                      · flag for "none"       · exit on first ✗
        │                   │                    │
        └──────── one pass · O(n) · O(1) ────────┘
```
