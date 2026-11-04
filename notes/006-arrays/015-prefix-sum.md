# 015 — Prefix Sum: Range Sum Queries & Equilibrium Point

> **One-line takeaway:** Precompute running totals once — `prefix[i] = arr[0] + … + arr[i]` — and the sum of **any** range becomes one subtraction: `prefix[r] − prefix[l − 1]`. Pay `O(n)` up front, then answer every range-sum question in `O(1)`.

📄 [`prefix_sum.cpp`](../../programs/cpp/data_structures/arrays/intermediate/prefix_sum.cpp) ·
📄 [`equilibrium.cpp`](../../programs/cpp/data_structures/arrays/intermediate/equilibrium.cpp) ·
📄 [`PrefixSum.java`](../../programs/java/arrays/basic/PrefixSum.java) ·
📄 [`GetEquilibriumPoint.java`](../../programs/java/arrays/basic/GetEquilibriumPoint.java)

---

## 1. What Is a Prefix Sum?

```text
index        0   1   2   3   4   5   6   7
arr          2   8   3   9   6   5   4   8
prefix       2  10  13  22  28  33  37  45

prefix[i] = sum of arr[0 … i]
```

Built in one left → right pass — each entry is the previous total plus one element:

```text
prefix[0] = arr[0]
prefix[i] = prefix[i − 1] + arr[i]
```

**Analogy — an odometer.** The odometer shows total distance since the car was new. To
find how far you drove on Tuesday, you don't replay the trip — you subtract Monday night's
reading from Tuesday night's. Prefix sums are odometer readings for an array.

---

## 2. 💡 The Core Identity

```text
sum(l … r)  =  prefix[r]  −  prefix[l − 1]

               [ arr[0] … arr[l−1] | arr[l] … arr[r] ]
               └──── prefix[l−1] ──┘
               └─────────────── prefix[r] ──────────┘
                                   └── what we want ─┘
```

> 🔑 **Everything up to `r`, minus everything before `l`, leaves exactly `l … r`.** The
> shared part cancels out.

This one line is the whole technique. Every application below is it in disguise.

---

# Card 1 — Range Sum Queries

📄 [`prefix_sum.cpp`](../../programs/cpp/data_structures/arrays/intermediate/prefix_sum.cpp)

### 🎯 Problem

Given a **fixed** array and **many** queries `(l, r)`, return the sum of `arr[l … r]` for
each.

### The naive version

```cpp
int sum = 0;
for (int i = l; i <= r; i++) sum += arr[i];     // per query
```

`O(n)` per query → `O(n · q)` for `q` queries. With `n = q = 10⁵` that's `10¹⁰` — far too slow.

### 💻 Code

```cpp
vector<int> get_prefix_sum(const int arr[], int size)
{
    vector<int> prefix_sum(size);
    prefix_sum[0] = arr[0];
    for (int i = 1; i < size; i++)
        prefix_sum[i] = prefix_sum[i - 1] + arr[i];      // build once: O(n)
    return prefix_sum;
}

int get_sum(const vector<int> &prefix_sum, int start, int end)
{
    if (start < 0 || start > end || end >= prefix_sum.size())
        throw out_of_range("Invalid subarray range.");

    if (start == 0)                                       // nothing before the range
        return prefix_sum[end];
    return prefix_sum[end] - prefix_sum[start - 1];       // answer: O(1)
}
```

### 🔍 Trace — `arr = [2, 8, 3, 9, 6, 5, 4, 8]`

```text
prefix       2  10  13  22  28  33  37  45

sum(2, 5)  = prefix[5] − prefix[1]  =  33 − 10  =  23      (3 + 9 + 6 + 5)
sum(1, 3)  = prefix[3] − prefix[0]  =  22 −  2  =  20      (8 + 3 + 9)
sum(4, 7)  = prefix[7] − prefix[3]  =  45 − 22  =  23      (6 + 5 + 4 + 8)
sum(0, 2)  = prefix[2]              =  13                  (start == 0 case)
```

### The `start == 0` special case — and the sentinel trick

`prefix[start − 1]` doesn't exist when `start == 0`, hence the `if`. The common alternative
is a prefix array **one longer**, with a leading `0`:

```text
P[0] = 0,   P[i + 1] = P[i] + arr[i]           (P has n + 1 entries)

sum(l … r)  =  P[r + 1] − P[l]                 no special case at all
```

```text
arr     2   8   3   9 …
P    0  2  10  13  22 …
```

> 💡 `P[i]` = "sum of the first `i` elements". The off-by-one moves into the definition,
> and every query becomes uniform. Most competitive programmers write it this way.

### ⏱️ Complexity

| | Build | Per query | `q` queries |
|---|---|---|---|
| Naive | — | `O(n)` | `O(n · q)` |
| **Prefix sum** | `O(n)` | **`O(1)`** | **`O(n + q)`** |

Space `O(n)` for the prefix array.

### ⚠️ Limits

- **Static data only.** Change one element and every prefix after it changes → `O(n)` to
  rebuild. For many updates **and** queries, use a **Fenwick tree** or **segment tree**
  (`O(log n)` for both).
- **Overflow.** Prefix sums grow with `n` — `10⁵` values of `10⁵` already exceeds `int`.
  Use `long long` / `long`.
- Works for anything with an **inverse**: sums (−), XOR (XOR again), counts. Not for min/max
  — you can't "subtract" a minimum (that needs a sparse table or segment tree).

---

# Card 2 — Equilibrium Point

📄 [`equilibrium.cpp`](../../programs/cpp/data_structures/arrays/intermediate/equilibrium.cpp)

### 🎯 Problem

An index `i` is an **equilibrium point** if the sum of elements strictly to its left equals
the sum strictly to its right. Does one exist?

```text
[3, 4, 8, −9, 20, 6]   →  yes, index 4:  3 + 4 + 8 − 9 = 6  =  6
[4, 2, −2]             →  yes, index 0:  (nothing) = 0  =  2 − 2
[1, 2, 3]              →  no
```

> The element at `i` itself belongs to **neither** side. An empty side sums to `0`.

### Three approaches

| Approach | Idea | Time | Space |
|---|---|---|---|
| Naive | for each `i`, sum both sides from scratch | `O(n²)` | `O(1)` |
| Prefix + suffix arrays | `left = prefix[i−1]`, `right = suffix[i+1]` | `O(n)` | `O(n)` |
| **Total + running prefix** (implemented) | `right = total − left − arr[i]` | `O(n)` | **`O(1)`** |

### 💡 Insight

> **You don't need a suffix array: the right side is whatever is left of the total.** If
> you know the grand total and the running left sum, then
> `right = total − left − arr[i]`.

```text
[ ……… left ……… | arr[i] | ……… right ……… ]
└──────────────────── total ────────────────────┘
```

### 💻 Code

```cpp
bool is_equilibrium_present(const int arr[], int size)
{
    if (size == 0) return false;

    int total_sum = get_total_sum(arr, size);        // pass 1: the grand total
    int prefix_sum = 0;                              // sum strictly LEFT of i

    for (int i = 0; i < size; i++)                   // pass 2
    {
        int suffix_sum = total_sum - prefix_sum - arr[i];   // sum strictly RIGHT of i
        if (prefix_sum == suffix_sum)
            return true;
        prefix_sum += arr[i];                        // i joins the left side for i + 1
    }
    return false;
}
```

### 🔍 Trace — `[3, 4, 8, −9, 20, 6]`, total = `32`

```text
i   arr[i]   left   right = 32 − left − arr[i]   equal?
0     3        0          29                      ✗
1     4        3          25                      ✗
2     8        7          17                      ✗
3    −9       15          26                      ✗
4    20        6           6                      ✅  → true
```

### 🔍 Trace — `[4, 2, −2]`, total = `4`

```text
i = 0   left 0   right = 4 − 0 − 4 = 0   ✅    (an empty left side counts as 0)
```

### Why the update order matters

```text
compute right, compare, THEN add arr[i] to left     ✅  arr[i] is on neither side
add arr[i] to left first                            ❌  arr[i] counted on the left
```

The same "use, then update" discipline as maximum difference in
[note 007](007-running-extremes.md).

### ⏱️ Complexity

Two passes → `O(n)` time, `O(1)` space.

### ⚠️ Pitfalls

| ✗ Mistake | Result |
|---|---|
| Including `arr[i]` in either side | Different (wrong) definition |
| Updating `left` before comparing | `arr[i]` counted on the left |
| Assuming only positives | Negatives are fine here — no monotonicity needed (unlike sliding window) |
| Skipping `i = 0` and `i = n − 1` | Both can be equilibrium points (empty side = 0) |

### 🔀 Variants

| Follow-up | Change |
|---|---|
| Return the **first** equilibrium index ("pivot index") | Return `i` instead of `true` |
| Split into **three** parts of equal sum | `total % 3 == 0`; find prefix reaching `total/3`, then `2·total/3` later |
| Product of array **except self** | Same idea with products: prefix product × suffix product, no division |

---

## 3. The Power Extension — Prefix Sum + Hash Map

> **A subarray sum is a difference of two prefix sums.** So "is there a subarray summing to
> `k`?" becomes "have I seen a prefix equal to `current_prefix − k`?" — a hash lookup.

```text
sum(l … r) = k   ⇔   P[r + 1] − P[l] = k   ⇔   P[l] = P[r + 1] − k
```

### Count subarrays with sum exactly `k` (negatives allowed)

```cpp
unordered_map<int, int> freq = {{0, 1}};       // the empty prefix, seen once
int prefix = 0, count = 0;
for (int i = 0; i < size; i++) {
    prefix += arr[i];
    auto it = freq.find(prefix - k);           // earlier prefixes that leave exactly k
    if (it != freq.end()) count += it->second;
    freq[prefix]++;
}
```

```text
arr = [1, −1, 1],  k = 1                         freq starts as {0: 1}

i   arr[i]   prefix   look up prefix − k   found   count   freq after
0     1        1             0               1        1     {0:1, 1:1}
1    −1        0            −1               0        1     {0:2, 1:1}
2     1        1             0               2        3     {0:2, 1:2}

subarrays:  [1] (index 0),  [1 −1 1] (0…2),  [1] (index 2)      → 3
```

`O(n)` time, `O(n)` space — and unlike the sliding window ([note 014](014-sliding-window.md)),
**negatives are fine**.

| Problem | Map stores |
|---|---|
| Does a subarray with sum `k` exist? | set of prefixes |
| Count subarrays with sum `k` | prefix → how many times seen |
| Longest subarray with sum `k` | prefix → **first** index seen |
| Longest subarray with equal 0s and 1s | treat `0` as `−1`; longest with sum `0` |

> ⚠️ Seed the map with prefix `0` (the empty prefix). Without it, subarrays that start at
> index `0` are never found.

---

## 4. Bonus — 2-D Prefix Sums

Same idea on a matrix: `P[i][j]` = sum of the rectangle from `(0, 0)` to `(i − 1, j − 1)`.

```text
sum of rectangle (r1, c1) … (r2, c2)
    =  P[r2+1][c2+1]  −  P[r1][c2+1]  −  P[r2+1][c1]  +  P[r1][c1]
                         └ remove above ┘  └ remove left ┘   └ added back: removed twice ┘
```

`O(R · C)` build, `O(1)` per rectangle query — inclusion–exclusion on areas.

---

## 5. Where Prefix Sums Show Up

```text
range sum queries               Card 1
equilibrium / pivot index       Card 2
subarray sum = k (negatives)    §3
difference arrays               the INVERSE operation — note 016
trapping rain water             prefix MAX, not sum (note 010) — same "precompute from the left" idea
```

---

## 6. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| `prefix[r] − prefix[l]` | Drops `arr[l]` — off by one |
| No `start == 0` case (without a sentinel) | Reads `prefix[−1]` |
| `int` prefix sums on large input | Overflow |
| Using prefix sums on a frequently **updated** array | `O(n)` per update — use a Fenwick/segment tree |
| Forgetting `{0: 1}` in the hash-map version | Misses subarrays starting at index 0 |
| Prefix "min" or "max" range queries by subtraction | Max/min have no inverse — doesn't work |

---

## 7. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Prefix sum definition?** | `prefix[i] = arr[0] + … + arr[i]`, built in one pass. |
| **Range sum formula?** | `prefix[r] − prefix[l − 1]` (or `P[r+1] − P[l]` with a leading 0). |
| **Why the leading-0 version?** | Removes the `l == 0` special case. |
| **Cost?** | `O(n)` build, `O(1)` per query → `O(n + q)` total. |
| **When not to use it?** | Frequent updates (→ Fenwick/segment tree); min/max queries. |
| **Equilibrium point?** | `left == total − left − arr[i]`, with `left` updated **after** the check. |
| **Equilibrium space?** | `O(1)` — the total replaces the suffix array. |
| **Subarray sum `k` with negatives?** | Prefix sum + hash map of earlier prefixes; seed with `0`. |
| **2-D range sum?** | Inclusion–exclusion on a 2-D prefix table. |
| **Overflow advice?** | Use `long long` / `long` for prefix sums. |

---

## 8. The Mental Model

```text
              prefix[i] = running total up to i       (build once, O(n))
                                │
            sum(l … r) = prefix[r] − prefix[l − 1]    (O(1), the shared part cancels)
                                │
     ┌──────────────────────────┼───────────────────────────┐
     ↓                          ↓                           ↓
range sum queries        equilibrium point          subarray sum = k
O(n + q)                 right = total − left −     prefix − k seen before?
                         arr[i]  ·  O(1) space      hash map · negatives OK
                                │
                    inverse operation → difference array (note 016)
```
