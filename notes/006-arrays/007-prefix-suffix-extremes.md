# 007 — Prefix & Suffix Extremes: "Everything to My Left / Right"

> **One-line takeaway:** When each element must be compared with **everything on one side** of it, you never need *all* of that side — only its **best** value (max or min). Carry that best value as you walk (running extreme), or precompute it for every index (prefix/suffix arrays) when you need **both** sides at once. Either way, a hidden inner loop disappears and `O(n²)` becomes `O(n)`.

---

## 1. The Problem This Concept Solves

A whole family of questions has this shape:

```text
for each index i:
    compare arr[i] with EVERY element on its left   (or right, or both)
```

Written directly, that's a loop inside a loop — `O(n²)`. Some examples:

```text
"Is arr[i] bigger than everything to its right?"            (leaders)
"What's the best arr[j] − arr[i] with i before j?"          (max difference / one stock trade)
"How much water can stand above bar i?"                     (trapping rain water)
```

They feel different. They're the same concept.

---

## 2. The Core Idea — a Side Collapses to One Number

> 🔑 **"Compare with every element on a side" is usually "compare with the best element on
> that side".**

```text
"arr[i] ≥ EVERY element to its right"     ⇔   "arr[i] ≥ the MAX to its right"
"best arr[j] − arr[i] over EVERY i < j"   ⇔   "arr[j] − the MIN before j"
"tallest wall to my left"                 ⇔   "the MAX of everything to my left"
```

A whole side — possibly thousands of elements — collapses into **one number**: its
extreme. That's the same "summary of what you've seen" idea from
[One-Pass Thinking](004-one-pass-thinking.md), aimed at a side instead of the whole array.

**Analogy — the high-jump scoreboard.** To know whether the next jumper set a record, you
don't replay every earlier jump. You look at one number on the board: the best so far.

---

## 3. Which Direction Do You Walk?

You can only summarise what you've **already seen**. So the side you care about decides the
walk direction:

| You need, for each `i`, the extreme of… | Walk | Called |
|---|---|---|
| everything **left** of `i` | left → right | **prefix** max / min |
| everything **right** of `i` | right → left | **suffix** max / min |
| **both** sides at once | precompute one or both as arrays, then combine | prefix + suffix arrays |

```text
prefix max of [3 0 1 2 5]   →   [3 3 3 3 5]      "tallest so far, from the left"
suffix max of [3 0 1 2 5]   →   [5 5 5 5 5]      "tallest so far, from the right"
```

### Carry a variable, or store an array?

```text
each i needs ONE side, and you process i in walk order    → carry ONE variable    O(1) space
each i needs BOTH sides                                   → store arrays          O(n) space
```

You can't carry the right side's max while walking left → right — you haven't seen it yet.
That single fact decides between the two forms.

---

## 4. Worked Example — Leaders (Suffix Max, Carried)

An element is a **leader** if it's `≥` everything to its right.

```text
[4  18  4  8  6  4  2]        leaders: 18, 8, 6, 4, 2
```

Walk from the right, carrying the max seen so far. An element is a leader exactly when it
reaches that max — and then it *becomes* the new max.

```text
←  walk     2    4    6    8    4    18    4
max so far  −    2    4    6    8     8   18
leader?     ✅   ✅   ✅   ✅   ✗    ✅    ✗
```

Two things fall out of the concept:

- Leaders come out **right to left** (reverse them if order matters).
- Read right to left, leaders never decrease — each one beat the previous max.

`>=` vs `>` is a definition choice: with `>=`, `[5, 5]` has two leaders; with `>`, one.

---

## 5. Worked Example — Best Later-Minus-Earlier (Prefix Min, Carried)

> *Maximum `arr[j] − arr[i]` with `j > i`.* The same as: *buy a stock once, sell later — max
> profit?*

Fix the **sell** day `j`. Its best partner is the **cheapest day before it**. So walk left →
right, carrying the minimum so far.

```cpp
int best = arr[1] - arr[0], lowest = arr[0];
for (int j = 1; j < n; j++) {
    best   = max(best, arr[j] - lowest);   // 1. use the min of days BEFORE j
    lowest = min(lowest, arr[j]);          // 2. then let day j join the "before" side
}
```

```text
[2  4  10  5  4  1]
j       4    10    5    4    1
lowest  2     2    2    2    2   → 1
diff    2     8    3    2   −1
best    2     8    8    8    8     → buy at 2, sell at 10
```

### 🧠 Pause and predict

What goes wrong if you swap the two lines inside the loop?

<details>
<summary>Answer</summary>

`lowest` would include `arr[j]` itself, so day `j` gets paired with **itself** — a difference
of `0`. On a decreasing array like `[10, 6, 2]` you'd answer `0` instead of `−4`. The rule:
**use the summary, then update it.** The summary must describe the side **strictly before** `j`.

</details>

> 💡 Why not just `max − min`? Because the max might come **before** the min (`[10, 1]`).
> The prefix summary enforces the order "earlier, then later" for free.

For the stock version, profit floors at `0` (you may choose not to trade): seed `best = 0`.

---

## 6. Worked Example — Trapping Rain Water (Both Sides)

This is the showcase problem for the concept, because each position needs **both** sides.

```text
bars [3 0 1 2 5]                         ≈ = water

             █
             █
 █  ≈  ≈  ≈  █
 █  ≈  ≈  █  █
 █  ≈  █  █  █
 3  0  1  2  5          trapped: 0 + 3 + 2 + 1 + 0 = 6
```

### Think per column, not per pool

Pools nest inside each other and get messy. Ask a simpler question for each column: *how
high can water stand right here?*

> Water above column `i` can't rise above the tallest wall on its left (it would spill out
> that side), nor above the tallest wall on its right. So it rises to the **shorter** of
> the two.

```text
water[i] = min( tallest on the left, tallest on the right ) − arr[i]
         = min( prefixMax[i],       suffixMax[i]         ) − arr[i]
```

Including `arr[i]` in both maxes means the result is never negative — a bar taller than
its surroundings simply holds `0`.

### Building it from the concept

```text
need both sides  →  can't carry both while walking one way  →  store them

i            0   1   2   3   4
arr          3   0   1   2   5
prefixMax    3   3   3   3   5       walk →
suffixMax    5   5   5   5   5       walk ←
min          3   3   3   3   5
water        0   3   2   1   0       total 6
```

`O(n)` time, `O(n)` space — down from `O(n²)` for rescanning both sides at every column.

### The `O(1)`-space upgrade — two pointers

Do we really need both maxes exactly? Only the **smaller** one matters.

> Put a pointer at each end. If the left bar is lower than the right bar, then the right
> side certainly has a wall at least that tall — so the water at the left pointer is
> decided by the left max **alone**. Settle that column, move inward. Otherwise, mirror it.

```cpp
int l = 0, r = n - 1, lmax = 0, rmax = 0, total = 0;
while (l < r) {
    if (arr[l] < arr[r]) { lmax = max(lmax, arr[l]); total += lmax - arr[l]; l++; }
    else                 { rmax = max(rmax, arr[r]); total += rmax - arr[r]; r--; }
}
```

This is the [two-pointer](005-two-pointers.md) elimination idea meeting the prefix/suffix
idea: you settle whichever side's answer is already **certain**.

---

## 7. The Same Concept, Other Operations

The "summary of a side" doesn't have to be a max or min:

| Summary of a side | Gives you | Note |
|---|---|---|
| max / min | leaders, max difference, rain water | this note |
| **sum** | range sums, equilibrium point | [Prefix Sums](013-prefix-sums.md) |
| product | "product of array except self" (prefix product × suffix product, no division) | — |
| count of something | "how many smaller elements to my left" (needs more than one number → a tree) | — |

> ⚠️ Max and min can be **carried** but not **subtracted**. You can get "sum of `l … r`"
> from two prefix sums, but you can't get "max of `l … r`" from two prefix maxes. Range-max
> queries need a different structure (sparse table, segment tree).

---

## 8. Recognising the Concept

```text
"for each element, compared with all elements to its left / right …"
"greater than everything after it"                        → suffix max
"best later − earlier", "buy then sell"                   → prefix min
"depends on the tallest/highest on both sides"            → prefix + suffix arrays
"can you do it in O(1) space?" (after the arrays version) → two pointers on the smaller side
```

---

## 9. Common Mistakes

| ✗ Mistake | Why it's wrong |
|---|---|
| Walking the wrong direction | The summary would need elements you haven't seen yet |
| Updating the summary before using it | The element is compared with itself |
| Answering `max − min` for an ordered pair | Ignores "earlier, then later" |
| `max` of the two walls in rain water | Water would float above the shorter wall |
| Excluding `arr[i]` from its own prefix/suffix max | Negative water on tall bars |
| Expecting prefix maxes to answer range-max queries | Max can't be "subtracted" |

---

## 10. Practice in This Repo

| Problem | Side summary | 📄 |
|---|---|---|
| Leaders | suffix max, carried | [`leaders.cpp`](../../programs/cpp/data_structures/arrays/basic/leaders.cpp) |
| Maximum difference (`j > i`) | prefix min, carried | [`maximum_difference.cpp`](../../programs/cpp/data_structures/arrays/basic/maximum_difference.cpp) |
| Trapping rain water | prefix + suffix max, stored | [`trapping_rain_water.cpp`](../../programs/cpp/data_structures/arrays/intermediate/trapping_rain_water.cpp) |
| Stock buy & sell (unlimited trades) | *a different idea* — greedy | [note 009](009-greedy-algorithms.md) |

---

## 11. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **The core trick?** | Replace "compare with every element on a side" by "compare with that side's extreme". |
| **How to pick the walk direction?** | Walk from the side you're summarising, so it's already been seen. |
| **Carry a variable or store an array?** | One side → carry. Both sides → store prefix/suffix arrays. |
| **Leaders?** | Walk right → left with a running max; output comes out reversed. |
| **Max `arr[j] − arr[i]`, `j > i`?** | Running prefix min; use it, **then** update it. |
| **Why not `max − min`?** | The min must come **before** the max. |
| **Rain water at column `i`?** | `min(prefixMax[i], suffixMax[i]) − arr[i]`. |
| **Rain water in `O(1)` space?** | Two pointers — settle the side with the lower bar. |
| **Why can't prefix maxes answer range-max queries?** | Max has no inverse; you can't subtract it. |

---

## 12. The Mental Model

```text
          "compare arr[i] with EVERYTHING on a side"
                           │
            only that side's EXTREME matters
                           │
        ┌──────────────────┼──────────────────────┐
        ↓                  ↓                      ↓
   LEFT side           RIGHT side             BOTH sides
   walk →              walk ←                 store prefix & suffix
   prefix min/max      suffix min/max         arrays, then combine
        │                  │                      │
   max difference       leaders              trapping rain water
   one-trade stock                                 │
                                       O(1) space: two pointers,
                                       settle the lower side first
        └──────────── O(n²) → O(n) ───────────────┘
```
