# 011 — Kadane's Algorithm: Maximum Subarray Sum (+ Circular)

> **One-line takeaway:** At every index ask one question — *"Is the best subarray ending just before me worth carrying, or should I start fresh here?"* If the running sum has gone negative, it can only drag you down, so **drop it**. One pass, `O(n)` time, `O(1)` space.

📄 [`sub_array.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sub_array.cpp)

---

## 1. What We Want

Find the largest sum of a **contiguous, non-empty** subarray.

```text
[ 2  3  −8 | 7  −1  2  3 ]   →   7 − 1 + 2 + 3 = 11
[ −3  −1  −2 ]               →   −1        (all negative: the best single element)
[ 5  −2  5 ]                 →   8         (worth crossing the dip)
```

### Subarray vs subsequence vs subset — get the word right

| Term | Contiguous? | Order kept? | Count for size `n` | "Max sum" answer |
|---|---|---|---|---|
| **Subarray** | ✅ | ✅ | `n(n+1)/2` | **Kadane** |
| Subsequence | ❌ | ✅ | `2ⁿ` | sum of all positives (or max element if none) |
| Subset | ❌ | ❌ | `2ⁿ` | same as subsequence |

> ⚠️ Mixing these up is the most common way to answer the wrong question. "Subarray" and
> "substring" always mean **contiguous**.

---

## 2. Brute Force — and What It Wastes

### `O(n³)` — every subarray, summed from scratch

```cpp
for (int i = 0; i < n; i++)
    for (int j = i; j < n; j++) {
        int sum = 0;
        for (int k = i; k <= j; k++) sum += arr[k];
        best = max(best, sum);
    }
```

### `O(n²)` — every start, extend the sum

```cpp
for (int i = 0; i < n; i++) {
    int sum = 0;
    for (int j = i; j < n; j++) {
        sum += arr[j];                 // sum of arr[i … j], built incrementally
        best = max(best, sum);
    }
}
```

The `O(n²)` version reuses work along each row. Kadane reuses work **between** rows: instead
of fixing the **start**, it fixes the **end**.

---

## 3. Building the Insight — Fix the End

Define one thing per index:

```text
best_ending_here[i]  =  the largest sum of a subarray that ENDS exactly at index i
```

A subarray ending at `i` either:

```text
1. is just arr[i] on its own                       → start fresh
2. extends the best subarray ending at i − 1       → best_ending_here[i−1] + arr[i]
```

Nothing else is possible — anything ending at `i` with more than one element **contains** a
subarray ending at `i − 1`, and the best one of those is `best_ending_here[i−1]`.

### 💡 Insight

> **A negative prefix never helps.** If `best_ending_here[i−1] < 0`, adding it to `arr[i]`
> makes things worse — so start fresh. Otherwise, extend.

Like a business: if the ledger so far is in the red, a new owner would rather start a fresh
company than inherit the debt. If it's in profit, keep going — even through a bad day.

---

## 4. The Recurrence — It's Dynamic Programming

```text
best_ending_here[0] = arr[0]
best_ending_here[i] = max( arr[i],  best_ending_here[i−1] + arr[i] )
                           start new     extend the previous one

answer = max over all i of best_ending_here[i]
```

Each value depends only on the **previous** one → keep it in a single variable instead of an
array. That's why Kadane is called **space-optimised DP**. (It's sometimes also called
greedy, because each step makes a local "extend or restart" decision — both views are fair.)

```text
DP table     best_ending_here[0 … n−1]    O(n) space
Kadane       current                      O(1) space    ← same values, only the last kept
```

---

## 5. The Code (implemented)

```cpp
int get_maximum_sum(const int arr[], int size)
{
    if (size == 0) return 0;

    int sum = arr[0], current = arr[0];          // seed with a REAL element
    for (int i = 1; i < size; i++)
    {
        current = max(arr[i], current + arr[i]); // extend or restart
        sum = max(sum, current);                 // remember the best seen anywhere
    }
    return sum;
}
```

| Variable | Meaning |
|---|---|
| `current` | best sum of a subarray **ending at `i`** |
| `sum` | best sum of a subarray ending **anywhere so far** — the answer |

### The equivalent "reset to zero" form you'll often see

```cpp
int best = arr[0], current = 0;
for (int i = 0; i < size; i++) {
    current += arr[i];
    best = max(best, current);     // 1. record FIRST
    if (current < 0) current = 0;  // 2. then drop a negative prefix
}
```

Same algorithm. The order of steps 1 and 2 matters — swap them and an all-negative array
returns `0`.

---

## 6. 🔍 Trace — `[2, 3, −8, 7, −1, 2, 3]`

```text
i     arr    extend      restart    current   best
0      2       —            —          2        2
1      3      5            3           5        5
2     −8     −3           −8          −3        5
3      7      4            7    ←      7        7    restart: −3 is dead weight
4     −1      6           −1           6        7    extend through the dip
5      2      8            2           8        8
6      3     11            3          11       11  ✅
```

Two moments to notice:

- **i = 3** — the carried sum is `−3`, so starting fresh at `7` beats extending.
- **i = 4** — `−1` lowers `current`, but it stays **positive**, so we keep it: the run might
  recover (and it does).

### 🔍 All-negative — `[−3, −1, −2]`

```text
i     arr    extend    restart    current   best
0     −3       —          —         −3       −3
1     −1      −4         −1         −1       −1
2     −2      −3         −2         −2       −1   → −1 ✅ (the largest single element)
```

---

## 7. ⏱️ Complexity

| Approach | Time | Space |
|---|---|---|
| Every subarray, summed from scratch | `O(n³)` | `O(1)` |
| Every start, running sum | `O(n²)` | `O(1)` |
| DP table of `best_ending_here` | `O(n)` | `O(n)` |
| **Kadane** | **`O(n)`** | **`O(1)`** |

`O(n)` is optimal — any element you skip could flip the answer.

---

## 8. ⚠️ Pitfalls

| ✗ Mistake | Result |
|---|---|
| Seeding `best = 0` / `current = 0` (with the `max(arr[i], …)` form) | All-negative `[−3, −1, −2]` returns `0` (the empty subarray) instead of `−1` |
| Resetting `current` to `0` **before** updating `best` | Same all-negative bug |
| Returning `current` instead of `best` | Gives the best sum ending at the **last** index only |
| Restarting on any negative **element** | Wrong — `[5, −2, 5]` should be `8`; only a negative **running sum** triggers a restart |
| Confusing subarray with subsequence | Subsequence max is just "sum the positives" |
| `int` overflow on big inputs | Use `long long` / `long` |

> 🔑 The repo version seeds with `arr[0]` and uses `max(arr[i], current + arr[i])`, which
> handles all-negative arrays correctly — the answer is the largest single element. If the
> problem allows an **empty** subarray (answer `0`), say so and seed with `0`.

---

## 9. 🔀 Variants

### Return the subarray itself

Record where the current run started; save `(start, i)` whenever `best` improves.

```cpp
int best = arr[0], current = arr[0], start = 0, best_l = 0, best_r = 0;
for (int i = 1; i < size; i++) {
    if (arr[i] > current + arr[i]) { current = arr[i]; start = i; }   // restart here
    else                             current += arr[i];               // extend
    if (current > best) { best = current; best_l = start; best_r = i; }
}
```

### ⭐ Maximum circular subarray sum (implemented)

📄 [`sub_array.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sub_array.cpp) (`get_maximum_circular_sum`)

**Problem:** the array is a ring — the last element is followed by the first. Find the
maximum subarray sum, where a subarray may **wrap around** the end.

```text
[5, −3, 5]   →  10      (5 + 5, wrapping: index 2 → index 0)
```

**Naive:** start at every index, walk `n` steps with `(i + j) % n`, keep a running sum →
`O(n²)`.

**💡 Insight — the best subarray either wraps, or it doesn't.**

```text
doesn't wrap   →  plain Kadane                                      (case 1)

wraps          →  [ a a a | m m m m | b b b ]
                    └─────┘           └─────┘   the subarray = b b b + a a a
                            └───────┘           what's LEFT OUT is a middle block

                  wrap sum = total − middle sum
                  maximise the wrap  ⇔  MINIMISE the middle             (case 2)
```

> 🔑 **A wrapping subarray is the complement of a non-wrapping one.** So run Kadane twice
> in the same loop — once for the max, once (with `min`) for the min — and take
> `max(kadane_max, total − kadane_min)`.

**💻 Code**

```cpp
int get_maximum_circular_sum(const int arr[], int size)
{
    if (size == 0) return 0;

    int total = arr[0];
    int maximum = arr[0], current_maximum = arr[0];
    int minimum = arr[0], current_minimum = arr[0];

    for (int i = 1; i < size; i++)
    {
        current_maximum = max(arr[i], arr[i] + current_maximum);   // standard Kadane
        maximum = max(maximum, current_maximum);

        current_minimum = min(arr[i], arr[i] + current_minimum);   // Kadane, mirrored
        minimum = min(minimum, current_minimum);

        total += arr[i];
    }

    if (maximum < 0) return maximum;          // all negative → see below
    return max(maximum, total - minimum);     // best of "no wrap" and "wrap"
}
```

**🔍 Trace — `[2, 3, −8, 7, −1, 2, 3]`** (the repo's demo array)

```text
kadane max   = 11      (7 − 1 + 2 + 3)
kadane min   = −8      (just −8)
total        = 8
wrap         = 8 − (−8) = 16      (7 − 1 + 2 + 3 + 2 + 3, wrapping past the end)
answer       = max(11, 16) = 16
```

**The all-negative exception**

```text
[−3, −1, −2]     kadane max = −1      min = −6 (the whole array)     total = −6
                 total − min = 0   ← "leave out everything" = the EMPTY subarray ❌
```

When every element is negative, the minimum subarray is the whole array, and the "wrap"
becomes empty — not allowed. `maximum < 0` happens exactly when every element is negative,
so return plain Kadane then. (If `maximum >= 0`, a `0` from the empty case can never beat
it, so no other guard is needed.)

**⏱️ Complexity:** one pass → `O(n)` time, `O(1)` space.

| ⚠️ Pitfall | Result |
|---|---|
| Missing the `maximum < 0` check | All-negative arrays return `0` |
| Computing `total − minimum` only | Misses the non-wrapping case (`[−2, 5, −2]` → wrap gives `3`, answer is `5`) |
| Brute force with `(i + j) % n` | Correct but `O(n²)` |

### Maximum product subarray

A negative number **flips** the largest product into the smallest and vice versa, so carry
both:

```cpp
int best = arr[0], hi = arr[0], lo = arr[0];
for (int i = 1; i < size; i++) {
    if (arr[i] < 0) swap(hi, lo);             // a negative flips max ↔ min
    hi = max(arr[i], hi * arr[i]);
    lo = min(arr[i], lo * arr[i]);
    best = max(best, hi);
}
```

### Others

| Follow-up | Approach |
|---|---|
| Minimum subarray sum | Kadane with `min` |
| Max sum of length **exactly `k`** | Not Kadane — **sliding window** (📄 [`MaximumSumSubarrayOfSizeK.java`](../../programs/java/arrays/basic/MaximumSumSubarrayOfSizeK.java)) |
| Max sum rectangle in a 2-D matrix | Fix a pair of rows, collapse columns, Kadane on the result → `O(R² · C)` |

---

## 10. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Restarting on a negative element instead of a negative sum | Splits good runs at every dip |
| Zero-seeding | Wrong on all-negative arrays |
| Forgetting the all-negative exception in the circular version | Returns `0` |
| Max product without tracking the minimum | Misses negative × negative |
| Using Kadane for a fixed-length window | Wrong tool — use a sliding window |

---

## 11. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **The one decision per index?** | Extend the previous subarray, or start fresh at `arr[i]`. |
| **When do you restart?** | When the running sum is negative — it can only lower what follows. |
| **What does `current` mean?** | Best sum of a subarray **ending at `i`**. |
| **Recurrence?** | `cur = max(arr[i], cur + arr[i])`, `best = max(best, cur)`. |
| **DP or greedy?** | Space-optimised DP over "best sum ending here" (also readable as greedy). |
| **All-negative array?** | Seed with `arr[0]` → returns the largest element. |
| **Reset-to-zero form gotcha?** | Update `best` **before** resetting. |
| **How to get the indices?** | Remember `start` on restart; save `(start, i)` when `best` improves. |
| **Circular version?** | `max(kadane, total − min_subarray)`, unless all elements are negative. |
| **Max product?** | Track both max and min ending here; a negative swaps them. |
| **Complexity?** | `O(n)` time, `O(1)` space. |

---

## 12. The Mental Model

```text
                     at index i
                          │
        is (best ending at i−1) negative?
             ╱                     ╲
           no                      yes
            │                       │
     extend the run       restart at arr[i] (drop the debt)
            └──────────┬────────────┘
                       ↓
            best = max(best, current)
                       ↓
                 O(n) · O(1)
                       │
     ┌─────────────────┼──────────────────┐
     ↓                 ↓                  ↓
  indices         circular:         product: carry
  (track start)   total − min         max AND min
```
