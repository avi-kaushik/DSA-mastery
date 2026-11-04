# 014 — Sliding Window: Max Sum of `k` Elements & Subarray with Given Sum

> **One-line takeaway:** A window is a contiguous subarray `[left, right]`. When it moves one step, only **one element enters and one leaves** — so update the window's sum in `O(1)` instead of recomputing it. Fixed-size windows slide; variable-size windows **grow on the right and shrink on the left**. Either way, every element enters and leaves at most once → `O(n)`.

📄 [`sliding_window.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sliding_window.cpp) ·
📄 [`MaximumSumSubarrayOfSizeK.java`](../../programs/java/arrays/basic/MaximumSumSubarrayOfSizeK.java) ·
📄 [`SubArrayWithGivenSum.java`](../../programs/java/arrays/basic/SubArrayWithGivenSum.java)

---

## 1. The Idea — Don't Recompute What Barely Changed

Sum of every 3 consecutive elements of `[1, 8, 30, −5, 20, 7]`:

```text
window [0..2]   1 + 8 + 30         = 39
window [1..3]       8 + 30 − 5     = 33
window [2..4]           30 − 5 + 20 = 45
```

Two of the three numbers are **shared** between neighbouring windows. Recomputing them is
wasted work.

```text
next window = previous window − element that LEFT + element that ENTERED

[ 1   8  30 ] −5  20   7        sum = 39
  1 [ 8  30  −5 ] 20   7        sum = 39 − 1 + (−5) = 33
  ↑ out       ↑ in
```

> 🔑 **The whole technique:** keep an aggregate (sum, count, max, …) of the current window
> and update it **incrementally** as the window moves.

### Two kinds of windows

| | Fixed size | Variable size |
|---|---|---|
| **Size** | always `k` | grows and shrinks |
| **Each step** | add `arr[i]`, remove `arr[i − k]` | add on the right; **while** invalid, remove on the left |
| **Typical question** | "best sum/avg/count of `k` consecutive" | "longest/shortest subarray satisfying X", "does a subarray with sum S exist" |
| **In this note** | Card 1 | Card 2 |

---

# Card 1 — Maximum Sum of `k` Consecutive Elements

**Fixed window** · 📄 [`sliding_window.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sliding_window.cpp) (`maximum_sum_of_consecutive_elements`)

### 🎯 Problem

Find the maximum sum of any `k` consecutive elements.

```text
[1, 8, 30, −5, 20, 7],  k = 3   →  45     (30 − 5 + 20)
```

### The naive version

```cpp
for (int i = 0; i + k <= size; i++) {      // every window start
    int sum = 0;
    for (int j = i; j < i + k; j++) sum += arr[j];
    best = max(best, sum);
}
```

`O(n · k)` — every window is summed from scratch. With `k ≈ n/2` that's `O(n²)`.

### 💡 Insight

> **Build the first window once. After that, each slide is one subtraction and one
> addition.**

### 💻 Code

```cpp
int maximum_sum_of_consecutive_elements(const int arr[], int size, int k)
{
    if (k <= 0 || k > size)
        throw out_of_range("Invalid window size.");

    int maximum = 0;
    for (int i = 0; i < k; i++)                 // 1. sum the first window
        maximum += arr[i];

    int window_sum = maximum;
    for (int i = k; i < size; i++)              // 2. slide: arr[i] in, arr[i − k] out
    {
        window_sum += arr[i] - arr[i - k];
        maximum = max(maximum, window_sum);
    }
    return maximum;
}
```

> Note `maximum` is seeded with the **first window's sum**, not `0` or `INT_MIN` — a real
> candidate, so all-negative arrays work (same lesson as [note 004](004-single-pass-scans.md)).

### 🔍 Trace — `[1, 8, 30, −5, 20, 7]`, `k = 3`

```text
first window  [1 8 30]                     sum = 39    max = 39
i = 3   in −5,  out 1    39 − 1 − 5   =    33          max = 39
i = 4   in 20,  out 8    33 − 8 + 20  =    45          max = 45
i = 5   in 7,   out 30   45 − 30 + 7  =    22          max = 45
answer → 45
```

### ⏱️ Complexity

`k` + `(n − k)` steps → **`O(n)`** time, `O(1)` space. Independent of `k`.

### ⚠️ Pitfalls

| ✗ Mistake | Result |
|---|---|
| Seeding the answer with `0` instead of the first window's sum | All-negative arrays return `0` |
| Removing `arr[i − k + 1]` instead of `arr[i − k]` | Window size drifts to `k − 1` |
| No check for `k > size` | Reads past the end building the first window |
| Updating `max` before sliding | Skips the last window |

### 🔀 Variants

| Follow-up | Aggregate kept |
|---|---|
| Max **average** of `k` consecutive | Same sum; divide at the end |
| Count windows with sum `≥ x` | Same sum, a counter |
| **Distinct** elements in every window | Hash map of counts (📄 [`CountDistinctElementsInEveryWindow.java`](../../programs/java/hashing/intermediate/CountDistinctElementsInEveryWindow.java)) |
| **Maximum** of every window | Not a sum — needs a **monotonic deque** (📄 [`MaximumsOfSubArraysWithGivenSize.java`](../../programs/java/dequeue/intermediate/MaximumsOfSubArraysWithGivenSize.java)) |

> 💡 Sums are easy to "un-add". A **max** isn't — when the max leaves the window, you don't
> know the next max. That's why window-max needs a deque.

---

# Card 2 — Subarray with a Given Sum (Non-Negative Numbers)

**Variable window** · 📄 [`sliding_window.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sliding_window.cpp) (`is_sub_array_with_given_sum_present`)

### 🎯 Problem

Given an array of **non-negative** integers, is there a non-empty **contiguous** subarray
whose sum equals `sum`?

```text
[1, 4, 20, 3, 10, 5],  sum = 33   →  yes   (20 + 3 + 10)
[1, 4, 0, 0, 3, 10, 5], sum = 7   →  yes   (4 + 0 + 0 + 3)
[1, 4],                sum = 0    →  no    (non-empty only)
```

### The naive version

```cpp
for (int i = 0; i < size; i++) {
    int s = 0;
    for (int j = i; j < size; j++) {
        s += arr[j];
        if (s == sum) return true;
    }
}
```

`O(n²)` — every start, every end.

### 💡 Insight

> **With non-negative numbers, the window sum is monotonic:** growing the window on the
> right can only **increase** it, shrinking on the left can only **decrease** it. So: grow
> while it's too small, shrink while it's too big — and you never need to move a pointer
> backwards.

```text
window sum  <  target   →  include the next element (expand right)
window sum  >  target   →  drop the leftmost element (shrink left)
window sum  == target   →  found
```

### 💻 Code

```cpp
bool is_sub_array_with_given_sum_present(const int arr[], int size, int sum)
{
    int start = 0, end = 0, window_sum = 0;          // window = [start, end)
    for (int i = 0; i < size; i++)
    {
        window_sum += arr[i];                        // 1. expand right
        end++;

        while (window_sum > sum && start < end)      // 2. shrink left while too big
        {
            window_sum -= arr[start];
            start++;
        }

        if (start < end && window_sum == sum)        // 3. non-empty and exact?
            return true;
    }
    return false;
}
```

### 🔍 Trace — `[1, 4, 20, 3, 10, 5]`, `sum = 33`

```text
i   add   window           sum    action
0    1    [1]                1    < 33
1    4    [1 4]              5    < 33
2   20    [1 4 20]          25    < 33
3    3    [1 4 20 3]        28    < 33
4   10    [1 4 20 3 10]     38    > 33 → drop 1 → 37 → drop 4 → 33
          [20 3 10]         33    == 33  ✅ found
```

### Why it's `O(n)` despite the nested `while`

```text
start only ever moves forward, and never past end.
Over the WHOLE run: end moves n times, start moves at most n times.
Total work  ≤  2n   →   O(n)       (amortized — same argument as note 003)
```

> 🔑 A `while` inside a `for` is **not** automatically `O(n²)`. Count how far each pointer
> can travel in total.

### ⚠️ Why negatives break it

```text
[5, −3, 2],  sum = 2           true answers: [5, −3] and [2]

i=0  window [5]        sum 5  > 2 → shrink → []        sum 0
i=1  window [−3]       sum −3
i=2  window [−3 2]     sum −1                          → returns false ❌
```

Dropping `5` was a mistake: a **later negative** would have brought the sum back down.
Once numbers can be negative, "too big → shrink" is no longer a safe decision — the sum
isn't monotonic.

> ⚠️ The repo's demo array `[1, 8, 30, −5, 20, 7]` contains `−5`, outside the documented
> "non-negative" assumption. It happens to find `45` correctly, but that's luck, not
> guarantee.

**With negatives, use prefix sums + a hash set** — `O(n)` time, `O(n)` space:

```cpp
unordered_set<int> seen = {0};               // prefix sum of the empty prefix
int prefix = 0;
for (int i = 0; i < size; i++) {
    prefix += arr[i];
    if (seen.count(prefix - sum)) return true;   // some earlier prefix p: prefix − p = sum
    seen.insert(prefix);
}
return false;
```

See [Prefix Sum](015-prefix-sum.md) for why `prefix − earlier_prefix` is a subarray sum.

### ⏱️ Complexity

| Approach | Time | Space | Negatives? |
|---|---|---|---|
| Every start/end | `O(n²)` | `O(1)` | ✅ |
| **Sliding window** (implemented) | `O(n)` | `O(1)` | ❌ |
| Prefix sum + hash set | `O(n)` | `O(n)` | ✅ |

### ⚠️ Pitfalls

| ✗ Mistake | Result |
|---|---|
| Using it with negative numbers | Silently wrong (see above) |
| `if` instead of `while` when shrinking | One drop may not be enough — e.g. `38 → 37` still too big |
| Accepting an empty window | `sum = 0` would always be "found" — hence `start < end` |
| Checking `== sum` before shrinking | Misses matches that appear only after dropping |

---

## 2. The Variable-Window Template

Most variable-window problems are this loop:

```cpp
int left = 0;
for (int right = 0; right < n; right++) {
    add(arr[right]);                     // 1. expand
    while (window is invalid)            // 2. shrink until valid again
        remove(arr[left++]);
    update answer with [left, right];    // 3. record
}
```

| Problem | "Invalid" when… | Answer |
|---|---|---|
| Subarray with sum `S` (non-negative) | `sum > S` | found if `sum == S` |
| Max consecutive ones with `k` flips | `zeros > k` | max length ([note 008](008-counting-runs.md)) |
| Longest subarray with sum `≤ S` (non-negative) | `sum > S` | max length |
| Smallest subarray with sum `≥ S` (non-negative) | — shrink **while** `sum ≥ S`, recording length each time | min length |
| Longest substring without repeating characters | a character count `> 1` | max length |

> 💡 The precondition is always the same: **shrinking must never make the window "more
> valid" in a way you'd regret** — i.e. the property must be monotonic in the window size.

---

## 3. Sliding Window vs Two Pointers vs Prefix Sum

```text
two pointers (note 005)    two indices with independent jobs (read/write, converge)
sliding window             two indices that bound ONE contiguous range + its aggregate
prefix sum (note 015)      precomputed running totals → any range sum in O(1)
```

Sliding window is a **special case** of the two-pointer idea: both pointers move forward,
and everything between them is the window.

---

## 4. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Recomputing each window from scratch | `O(n · k)` instead of `O(n)` |
| Variable window on data with negatives | Wrong answers, no error |
| Off-by-one when removing (`i − k` vs `i − k + 1`) | Window size drifts |
| `if` instead of `while` for shrinking | Window stays invalid |
| Calling a nested `while` `O(n²)` | It's `O(n)` — each pointer moves at most `n` times |
| Using a sum-style window for window **max** | Max can't be "un-added" — use a deque |

---

## 5. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Core idea?** | Update the window's aggregate in `O(1)` as one element enters and one leaves. |
| **Fixed-window update?** | `sum += arr[i] − arr[i − k]`. |
| **Seed for max-sum-of-k?** | The first window's sum. |
| **Variable-window rule?** | Expand right; **while** invalid, shrink left; record. |
| **Why `O(n)` with a nested `while`?** | Each pointer moves forward at most `n` times in total. |
| **Precondition for subarray-sum window?** | Non-negative numbers — the sum must be monotonic. |
| **Counter-example with negatives?** | `[5, −3, 2]`, sum `2` → window says no, answer is yes. |
| **Subarray sum with negatives?** | Prefix sum + hash set, `O(n)` time and space. |
| **Window maximum?** | Monotonic deque — a max can't be removed by subtraction. |
| **Sliding window vs two pointers?** | Window = two pointers bounding one contiguous range. |

---

## 6. The Mental Model

```text
                 contiguous range [left, right]
                             │
            keep its aggregate; update in O(1)
                             │
          ┌──────────────────┴──────────────────┐
          ↓                                     ↓
     FIXED size k                         VARIABLE size
  + arr[i]  − arr[i − k]          expand right · WHILE invalid shrink left
          │                                     │
   max sum of k consecutive        subarray with sum S (non-negative only!)
                                   max ones with k flips
          │                                     │
          └────── each element in & out once → O(n) ──────┘
                             │
             negatives / no monotonicity?  →  prefix sum + hash
```
