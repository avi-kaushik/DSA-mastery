# 013 — Prefix Sums

> **One-line takeaway:** Precompute running totals once — `P[i]` = sum of the first `i` elements — and the sum of **any** range becomes one subtraction: `P[r + 1] − P[l]`. Pay `O(n)` up front, then answer every range-sum question in `O(1)`. And because *every subarray sum is a difference of two prefix sums*, "find a subarray with sum `k`" turns into "have I seen the right prefix before?" — a hash lookup.

---

## 1. The Problem This Concept Solves

> *Given a fixed array and many questions "what's the sum from index `l` to `r`?", answer
> them all.*

Summing each range directly costs `O(n)` per question. With `10⁵` elements and `10⁵`
questions, that's `10¹⁰` operations — hopeless.

The same pain shows up in disguise:

```text
"Is there an index where the left sum equals the right sum?"     (equilibrium)
"How many subarrays sum to exactly k?"                           (even with negatives)
"Sum of any rectangle in a grid?"                                 (2-D)
```

---

## 2. The Core Idea — Odometer Readings

**Analogy — a car's odometer.** It shows the total distance since the car was new. To find
how far you drove on Tuesday, you don't replay the trip — you subtract Monday night's reading
from Tuesday night's.

A prefix-sum array is a list of odometer readings for the array:

```text
index        0   1   2   3   4   5   6   7
arr          2   8   3   9   6   5   4   8

P        0   2  10  13  22  28  33  37  45        P[i] = sum of the first i elements
         ↑
     P[0] = 0 — the reading before anything happened
```

```text
P[0] = 0
P[i + 1] = P[i] + arr[i]                 one pass, O(n)
```

### The identity that does everything

```text
sum(arr[l … r])  =  P[r + 1] − P[l]

  [ arr[0] … arr[l−1] | arr[l] … arr[r] ]
  └──────── P[l] ─────┘
  └──────────────── P[r + 1] ───────────┘
                      └── what we want ─┘
```

> 🔑 **Everything up to `r`, minus everything before `l`, leaves exactly `l … r`.** The shared
> part cancels out.

```text
sum(2 … 5) = P[6] − P[2] = 33 − 10 = 23        (3 + 9 + 6 + 5 ✓)
sum(0 … 2) = P[3] − P[0] = 13 −  0 = 13        no special case for l = 0
```

### Why the leading `0`?

Many implementations store `prefix[i]` = sum of `arr[0 … i]` (same length as the array).
Then the formula is `prefix[r] − prefix[l − 1]`, which breaks at `l = 0` and needs an `if`.
Shifting by one and putting `0` in front makes every query uniform. Same idea, fewer bugs.

---

## 3. What It Costs — and What It Buys

| | Build | Per query | `q` queries |
|---|---|---|---|
| Sum each range directly | — | `O(n)` | `O(n · q)` |
| **Prefix sums** | `O(n)` | **`O(1)`** | **`O(n + q)`** |

The trade: `O(n)` extra memory for the table, in exchange for never re-adding the same
numbers again.

---

## 4. Worked Example — Equilibrium Point

> An index `i` is an **equilibrium point** if the sum strictly to its left equals the sum
> strictly to its right.

```text
[3, 4, 8, −9, 20, 6]    index 4:   3 + 4 + 8 − 9 = 6   and   6 = 6   ✅
```

With prefix sums: `left = P[i]`, `right = P[n] − P[i + 1]`. But notice you don't even need the
table:

> **The right side is just whatever's left of the total.**
> `right = total − left − arr[i]`

```cpp
int total = 0;
for (int x : arr) total += x;

int left = 0;
for (int i = 0; i < n; i++) {
    int right = total - left - arr[i];
    if (left == right) return i;          // or true
    left += arr[i];                       // arr[i] joins the left side for the NEXT index
}
return -1;
```

```text
total = 32
i   arr[i]   left   right = 32 − left − arr[i]
0     3        0         29
1     4        3         25
2     8        7         17
3    −9       15         26
4    20        6          6    ✅
```

`O(n)` time, `O(1)` space. Note the order: **use** `left`, **then** add `arr[i]` — the same
"use the summary, then update it" rule from [note 007](007-prefix-suffix-extremes.md).

---

## 5. The Big Idea — Subarray Sums Are Differences of Prefixes

Turn the identity around:

```text
sum(l … r) = k     ⇔     P[r + 1] − P[l] = k     ⇔     P[l] = P[r + 1] − k
```

> 🔑 **A subarray ending here sums to `k` exactly when some earlier prefix equals
> `current_prefix − k`.** So walk once, keep every prefix you've seen in a hash map, and ask.

### Count subarrays with sum exactly `k` — negatives allowed

```cpp
unordered_map<int, int> seen = {{0, 1}};      // the empty prefix, seen once
int prefix = 0, count = 0;
for (int x : arr) {
    prefix += x;
    if (seen.count(prefix - k)) count += seen[prefix - k];
    seen[prefix]++;
}
```

```text
arr = [1, −1, 1],  k = 1

x    prefix   look for prefix − 1   times seen   count   seen after
1      1             0                   1          1     {0:1, 1:1}
−1     0            −1                   0          1     {0:2, 1:1}
1      1             0                   2          3     {0:2, 1:2}

subarrays: [1] at 0, [1 −1 1], [1] at 2      → 3
```

### 🧠 Pause and predict

Why must the map start with `{0: 1}`?

<details>
<summary>Answer</summary>

It represents the **empty prefix** (`P[0] = 0`). Without it, a subarray that starts at index
`0` — whose sum is just the current prefix — would need "an earlier prefix equal to `0`",
and there wouldn't be one. `[5]` with `k = 5` would report `0` subarrays.

</details>

### One idea, several questions — only the map changes

| Question | Map stores |
|---|---|
| Does a subarray with sum `k` exist? | set of prefixes seen |
| How many subarrays sum to `k`? | prefix → how many times seen |
| Longest subarray with sum `k`? | prefix → **first** index seen |
| Longest subarray with equal 0s and 1s | treat `0` as `−1`, then longest with sum `0` |

> 💡 This is the fix for the [Sliding Window](012-sliding-window.md)'s blind spot: windows
> need non-negative numbers; prefix sums + hashing don't care about signs.

---

## 6. Beyond Sums — Anything With an "Undo"

Prefix sums work because subtraction **undoes** addition. Any operation with an inverse works
the same way:

| Operation | Range query | Works? |
|---|---|---|
| sum | `P[r+1] − P[l]` | ✅ |
| count of something | same as sum of 0/1 flags | ✅ |
| XOR | `X[r+1] ^ X[l]` (XOR undoes itself) | ✅ |
| product (no zeros) | `P[r+1] / P[l]` | ✅ (careful with zeros and overflow) |
| **max / min** | ??? | ❌ no inverse — use a sparse table or segment tree |

---

## 7. Two Dimensions — Inclusion–Exclusion

`P[i][j]` = sum of the rectangle from `(0, 0)` to `(i − 1, j − 1)`.

```text
sum of rectangle (r1, c1) … (r2, c2)
   =  P[r2+1][c2+1]  −  P[r1][c2+1]  −  P[r2+1][c1]  +  P[r1][c1]
         whole           strip above      strip left      corner removed twice → add back
```

`O(R · C)` to build, `O(1)` per rectangle. The 1-D identity, applied to areas.

---

## 8. Limits

- **Static data only.** Changing one element changes every prefix after it → `O(n)` per
  update. With many updates **and** queries mixed together, use a **Fenwick tree** or
  **segment tree** (`O(log n)` each).
- **Overflow.** Prefix sums grow with `n`. Use `long long` / `long` when values or `n` are large.
- **No max/min ranges** (no inverse — §6).

The opposite situation — many **range updates**, then read the values — is handled by
prefix sums' mirror image: [Difference Arrays](014-difference-arrays.md).

---

## 9. Recognising Prefix-Sum Problems

```text
"sum of elements from l to r", many queries                 → prefix table
"left sum equals right sum", "split into equal parts"       → total + running prefix
"number of subarrays with sum k" (negatives possible)       → prefix + hash map
"longest subarray with sum k / equal 0s and 1s"             → prefix + first-index map
"sum of a sub-rectangle"                                    → 2-D prefix
```

---

## 10. Common Mistakes

| ✗ Mistake | Why it's wrong |
|---|---|
| `prefix[r] − prefix[l]` (inclusive prefixes) | Drops `arr[l]` — off by one |
| Inclusive prefixes without an `l == 0` case | Reads `prefix[−1]` |
| Forgetting `{0: 1}` in the hash-map version | Misses subarrays starting at index 0 |
| Adding `arr[i]` to `left` before comparing (equilibrium) | `arr[i]` counted on the left |
| `int` prefixes on big input | Overflow |
| Prefix sums on frequently updated data | `O(n)` per update |
| Trying range-max with prefix maxes | Max can't be subtracted |

---

## 11. Practice in This Repo

| Problem | Idea | 📄 |
|---|---|---|
| Range sum queries in `O(1)` | prefix table | [`prefix_sum.cpp`](../../programs/cpp/data_structures/arrays/intermediate/prefix_sum.cpp) · [`PrefixSum.java`](../../programs/java/arrays/basic/PrefixSum.java) |
| Equilibrium point | total + running prefix | [`equilibrium.cpp`](../../programs/cpp/data_structures/arrays/intermediate/equilibrium.cpp) · [`GetEquilibriumPoint.java`](../../programs/java/arrays/basic/GetEquilibriumPoint.java) |
| Subarray sums with hashing | prefix + hash map | [`SubArrayWithSums.java`](../../programs/java/hashing/intermediate/SubArrayWithSums.java) |
| Equal 0s and 1s | `0 → −1`, prefix + first index | [`LongestSubArrayWithEqualNumberOfBinaryElements.java`](../../programs/java/hashing/intermediate/LongestSubArrayWithEqualNumberOfBinaryElements.java) |

> `prefix_sum.cpp` uses the inclusive form (`prefix[i]` = sum of `arr[0 … i]`), so its
> `get_sum` has the `start == 0` special case — the one the leading-`0` form removes.

---

## 12. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Prefix sum?** | `P[0] = 0`, `P[i+1] = P[i] + arr[i]` — running totals. |
| **Range sum?** | `P[r+1] − P[l]`. |
| **Why the leading 0?** | It removes the `l == 0` special case. |
| **Cost?** | `O(n)` build, `O(1)` per query → `O(n + q)`. |
| **Equilibrium point?** | `left == total − left − arr[i]`; add `arr[i]` to `left` **after** checking. |
| **Subarray sum = `k`, with negatives?** | Prefix + hash map: look for `prefix − k`; seed `{0: 1}`. |
| **Why seed `{0: 1}`?** | It's the empty prefix — needed for subarrays starting at 0. |
| **Which operations work?** | Anything with an inverse: sum, count, XOR. Not max/min. |
| **2-D rectangle sum?** | Inclusion–exclusion on a 2-D prefix table. |
| **Updates mixed with queries?** | Fenwick / segment tree. |

---

## 13. The Mental Model

```text
           P[i] = total of the first i elements      (build once, O(n))
                              │
              sum(l … r) = P[r + 1] − P[l]            (shared part cancels)
                              │
     ┌────────────────────────┼────────────────────────────┐
     ↓                        ↓                            ↓
 many range queries      left vs right split        subarray sum = k
 O(1) each               right = total − left −     seen (prefix − k)?
                         arr[i] · O(1) space         hash map · negatives OK
                              │
          needs an inverse (sum, XOR, count) · static data · watch overflow
                              │
              the reverse direction → difference arrays (note 014)
```
