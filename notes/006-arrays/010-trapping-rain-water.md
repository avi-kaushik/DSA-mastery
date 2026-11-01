# 010 — Trapping Rain Water

> **One-line takeaway:** Water above bar `i` rises to the **shorter of the tallest walls on its left and right**, minus the bar itself. Precompute those two walls (prefix max + suffix max) and every index becomes an `O(1)` formula. The famous follow-up does the same with **two pointers** and no extra arrays.

📄 [`trapping_rain_water.cpp`](../../programs/cpp/data_structures/arrays/intermediate/trapping_rain_water.cpp)

---

## 1. What We Want

`arr[i]` is the height of a bar of width 1. After it rains, how many units of water are
trapped between the bars?

```text
[3, 0, 1, 2, 5]                    ≈ = water

             █
             █
 █  ≈  ≈  ≈  █
 █  ≈  ≈  █  █
 █  ≈  █  █  █
 3  0  1  2  5       water per column: 0 + 3 + 2 + 1 + 0 = 6
```

### Think per column, not per pool

The tempting approach is to find "pools" — valleys between peaks — and fill each one.
Pools nest inside each other, have uneven floors, and get messy fast.

> 🔑 **Ask a simpler question for every single column: how high can water stand right
> here?** Add up the answers. No pools, no nesting.

---

## 2. The Formula

Water above column `i` can't rise above the tallest wall on its left — it would spill out
that side. Same for the right. So it's capped by the **shorter** of the two:

```text
water[i]  =  min( leftMax[i], rightMax[i] )  −  arr[i]

leftMax[i]   = tallest bar in arr[0 … i]       ← includes i itself
rightMax[i]  = tallest bar in arr[i … n−1]     ← includes i itself
```

```text
           tallest left        tallest right
               █                     █
               █        water        █
               █   ≈ ≈ ≈ ≈ ≈ ≈ ≈ ≈   █     ← level = the SHORTER wall
               █   ≈ ≈ ≈ ▓ ≈ ≈ ≈ ≈   █
               █   ≈ ≈ ≈ ▓ ≈ ≈ ≈ ≈   █
                         ↑
                    bar i (its own height is subtracted)
```

> 💡 Including `arr[i]` in both maxes guarantees `min(…) >= arr[i]`, so the result is
> **never negative** — a bar taller than everything around it simply holds `0`. No special
> case needed.

Two more facts fall straight out of the formula:

```text
first and last bars   always 0 — one of their walls is themselves
fewer than 3 bars     nothing can be trapped
```

---

## 3. Approach 1 — Naive: Find the Walls for Every Column

```cpp
int total = 0;
for (int i = 1; i < size - 1; i++) {
    int left = arr[i], right = arr[i];
    for (int j = 0; j < i; j++)        left  = max(left, arr[j]);    // scan left
    for (int j = i + 1; j < size; j++) right = max(right, arr[j]);   // scan right
    total += min(left, right) - arr[i];
}
```

`O(n²)` time, `O(1)` space. Correct — but every column re-scans the whole array.

> The walls are **running extremes** ([note 007](007-running-extremes.md)). The left wall
> is a prefix max, the right wall is a suffix max. But each column needs **both at once**,
> and you can't carry a suffix max while walking left → right. So: **store** them.

---

## 4. Approach 2 — Prefix & Suffix Max Arrays (implemented)

### 💻 Code

```cpp
int get_trapped_units(const int arr[], int size)
{
    if (size <= 2) return 0;                       // need at least 3 bars

    vector<int> left_max(size), right_max(size);

    left_max[0] = arr[0];                          // prefix max: left → right
    for (int i = 1; i < size; i++)
        left_max[i] = max(left_max[i - 1], arr[i]);

    right_max[size - 1] = arr[size - 1];           // suffix max: right → left
    for (int i = size - 2; i >= 0; i--)
        right_max[i] = max(right_max[i + 1], arr[i]);

    int total = 0;
    for (int i = 0; i < size; i++)                 // combine per column
        total += min(left_max[i], right_max[i]) - arr[i];
    return total;
}
```

(The repo version carries a running `left_max_pointer` / `right_max_pointer` variable while
filling the arrays — same values, same result.)

### 🔍 Trace — `[3, 0, 1, 2, 5]`

```text
i            0   1   2   3   4
arr          3   0   1   2   5
left_max     3   3   3   3   5
right_max    5   5   5   5   5
min          3   3   3   3   5
water        0   3   2   1   0      → 6
```

### ⏱️ Complexity

Three passes → `O(n)` time. Two helper arrays → `O(n)` space.

---

## 5. Approach 3 — Two Pointers, `O(1)` Space ⭐

Interviewers almost always ask: *"Can you do it without the extra arrays?"*

### 💡 Insight

> **You don't need both walls exactly — only the shorter one.** Keep a pointer at each end.
> If the left bar is lower than the right bar, then the right side certainly has a wall at
> least that tall — so the water at `left` is decided by `left_max` **alone**. Settle that
> column and move `left` inward. Otherwise do the same from the right.

### 💻 Code

```cpp
int l = 0, r = size - 1, lmax = 0, rmax = 0, total = 0;
while (l < r) {
    if (arr[l] < arr[r]) {                 // left is the limiting side
        lmax = max(lmax, arr[l]);
        total += lmax - arr[l];
        l++;
    } else {                               // right is the limiting side
        rmax = max(rmax, arr[r]);
        total += rmax - arr[r];
        r--;
    }
}
```

### 🔍 Trace — `[2, 0, 4, 0, 1]` (expected `3`)

```text
l  r   arr[l] arr[r]   side    max         water   total
0  4     2      1      right   rmax = 1      0       0
0  3     2      0      right   rmax = 1      1       1
0  2     2      4      left    lmax = 2      0       1
1  2     0      4      left    lmax = 2      2       3
2  2   stop (l == r)                                 3 ✅
```

### Why it's correct

```text
Claim: when arr[l] < arr[r], the water at l is exactly  lmax − arr[l].

1. Every bar in arr[0 … l] got processed on a LEFT move, which only happens when that
   bar was lower than some bar still on the right.
   → every bar in arr[0 … l] is lower than some bar to the right of l
   → lmax  ≤  rightMax[l]

2. So min(leftMax[l], rightMax[l])  =  min(lmax, rightMax[l])  =  lmax.        ∎
```

In words: the left max was built only from bars that **lost to something on the right**, so
it can never exceed the right wall. The shorter wall is known — and that's all the formula
needs. The mirror argument covers right moves.

### ⏱️ Complexity

`O(n)` time (each bar visited once), `O(1)` space.

---

## 6. Approach 4 — Monotonic Stack (Awareness Only)

Keep a stack of indices with **decreasing** heights. When a taller bar arrives, pop the
bar on top — it's a "floor" — and fill the water between the new bar and the bar below it
on the stack, **layer by layer** (horizontally) instead of column by column.

`O(n)` time, `O(n)` space. Worth knowing it exists; the two-pointer version is the one to
write.

---

## 7. All Approaches Compared

| Approach | Time | Space | Fills water… |
|---|---|---|---|
| Naive — scan both sides per column | `O(n²)` | `O(1)` | per column |
| **Prefix + suffix max arrays** (implemented) | `O(n)` | `O(n)` | per column |
| Two pointers | `O(n)` | `O(1)` | per column |
| Monotonic stack | `O(n)` | `O(n)` | per horizontal layer |

---

## 8. Variants

| Problem | How it differs |
|---|---|
| **Container With Most Water** | Pick **two lines** to maximise `min(h[l], h[r]) × (r − l)`; bars in between are ignored. Also two pointers — always move the shorter side. |
| Trapping Rain Water II (2-D grid) | Min-heap flooding from the boundary inwards |
| Water above each bar (array output) | Same formula, store instead of sum |

> ⚠️ Don't mix up the two: in **trapping** rain water every bar matters and water fills
> around them; in **container**, only two lines matter and the area is a rectangle.

---

## 9. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| `max` of the two walls instead of `min` | Water "floats" above the shorter wall |
| Excluding `arr[i]` from its own max | Negative water on tall bars |
| Thinking in pools/valleys | Messy nested-valley logic — go per column |
| Fewer than 3 bars without a guard | Fine with the formula, but say why the answer is 0 |
| Two pointers: moving the **taller** side | The water level on that side isn't decided yet → wrong totals |
| `int` overflow on huge inputs | Use `long long` / `long` for the total |

---

## 10. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Water at index `i`?** | `min(leftMax[i], rightMax[i]) − arr[i]` |
| **Why `min`?** | Water spills over the shorter wall. |
| **Why include `arr[i]` in the maxes?** | Guarantees non-negative water without a special case. |
| **Why think per column?** | Pools nest and get messy; columns are independent. |
| **Naive complexity?** | `O(n²)` — rescan both sides for each column. |
| **Implemented complexity?** | `O(n)` time, `O(n)` space (prefix & suffix max arrays). |
| **`O(1)` space idea?** | Two pointers — advance the lower side; its running max is its water level. |
| **Why does the two-pointer version work?** | The lower side's max can't exceed the other side's wall, so it's the binding limit. |
| **Do the end bars ever hold water?** | Never — they are their own wall on one side. |
| **Container With Most Water vs this?** | Container: two lines, rectangle area. Rain water: every bar, per-column water. |

---

## 11. The Mental Model

```text
           per column:   how high can water stand here?
                                    │
                      min( tallest left, tallest right )
                                    │
                         − own height  =  water[i]
                                    │
        ┌───────────────────────────┼───────────────────────────┐
        ↓                           ↓                           ↓
  rescan both sides        store prefix & suffix max     two pointers: settle
  per column               then combine                  the LOWER side first
  O(n²) · O(1)             O(n) · O(n)                   O(n) · O(1) ⭐
```
