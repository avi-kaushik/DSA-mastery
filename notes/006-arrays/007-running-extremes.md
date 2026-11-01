# 007 — Running Extremes: Leaders & Maximum Difference

> **One-line takeaway:** When each element must be compared with **everything on one side** of it, don't re-scan that side — **carry its max (or min) along** as you walk. One remembered number replaces a whole inner loop, collapsing `O(n²)` into `O(n)`.

📄 [`leaders.cpp`](../../programs/cpp/data_structures/arrays/basic/leaders.cpp) ·
📄 [`maximum_difference.cpp`](../../programs/cpp/data_structures/arrays/basic/maximum_difference.cpp)

---

## 1. The Idea — Replace "All of Them" with "The Best of Them"

Both problems in this note start as a nested loop:

```text
for each i:
    for each j on one side of i:      ← O(n) inner loop
        compare arr[i] with arr[j]
                                       → O(n²)
```

But look at what the inner loop actually needs:

```text
"is arr[i] ≥ EVERY element to its right?"   ⇔   "is arr[i] ≥ the MAX to its right?"
"best arr[j] − arr[i] over EVERY i before j" ⇔   "arr[j] − the MIN before j"
```

> 🔑 **If only the best value on one side matters, keep that one value.** Update it as
> you move, and each step becomes `O(1)`.

| Side the question looks at | Walk direction | Carry |
|---|---|---|
| Everything to the **right** | right → left | **suffix** max / min |
| Everything to the **left** | left → right | **prefix** max / min |

> 💡 The walk direction is forced: you must have **already visited** the side you're
> summarising. Right side → walk from the right.

---

# Card 1 — Leaders in an Array

**Running suffix max** · 📄 [`leaders.cpp`](../../programs/cpp/data_structures/arrays/basic/leaders.cpp)

### 🎯 Problem

An element is a **leader** if it is `>=` every element to its **right**. Print all leaders.
(The last element is always a leader — nothing is to its right.)

```text
[4, 18, 4, 8, 6, 4, 2]   →   leaders: 18, 8, 6, 4, 2
```

### The naive version

```cpp
for (int i = 0; i < size; i++) {
    bool leader = true;
    for (int j = i + 1; j < size; j++)
        if (arr[j] > arr[i]) { leader = false; break; }
    if (leader) cout << arr[i] << " ";
}
```

`O(n²)` — every element re-scans its whole right side.

### 💡 Insight

> **"Greater than everything to the right" = "greater than the max of the right".** Walk
> from the right, keep the max so far; any element that reaches it is a leader and becomes
> the new max.

### 💻 Code

```cpp
void print_leaders(const int arr[], int size)
{
    if (size == 0) return;

    int largest = arr[size - 1];               // the rightmost is always a leader
    cout << largest << " ";

    for (int i = size - 2; i >= 0; i--)        // walk right → left
    {
        if (arr[i] >= largest)                 // beats (or ties) everything to its right
        {
            largest = arr[i];
            cout << largest << " ";
        }
    }
    cout << endl;
}
```

### 🔍 Trace — `[4, 18, 4, 8, 6, 4, 2]`

```text
i   arr[i]   max to the right   leader?   new max
6     2           —               ✅         2
5     4           2               ✅         4
4     6           4               ✅         6
3     8           6               ✅         8
2     4           8               ✗          8
1    18           8               ✅        18
0     4          18               ✗         18

printed:  2 4 6 8 18        ← right-to-left order!
```

### ⏱️ Complexity

`O(n)` time, `O(1)` space — down from `O(n²)`.

### ⚠️ Pitfalls

- **Output order is reversed.** The walk is right → left, so leaders come out that way. If
  the problem wants original order, push into a vector and reverse it (`O(n)` space) — or
  print in reverse with recursion/stack.
- **`>=` vs `>` is a definition choice.** With `>=`, `[5, 5]` has two leaders; with `>`,
  only the last one. Ask which the interviewer means.
- Sanity check: read right → left, leaders are always **non-decreasing** (each one beat the
  previous max).

### 🔀 Variants

| Follow-up | Change |
|---|---|
| Leaders in original order | Collect, then reverse |
| Count leaders only | Counter instead of print |
| Elements greater than everything to the **left** | Walk left → right with a prefix max |
| "Next greater element" for **each** index | Not a single max anymore — needs a **stack** (`O(n)`) |

---

# Card 2 — Maximum Difference `arr[j] − arr[i]` with `j > i`

**Running prefix min** · 📄 [`maximum_difference.cpp`](../../programs/cpp/data_structures/arrays/basic/maximum_difference.cpp)

### 🎯 Problem

Find the maximum value of `arr[j] − arr[i]` where `j` comes **after** `i`.

```text
[2, 4, 10, 5, 4, 1]   →   10 − 2 = 8
[7, 9, 5, 6, 3, 2]    →    9 − 7 = 2
[10, 6, 2]            →    6 − 10 = −4     (decreasing: the least-bad forced pair)
```

### The naive version

```cpp
int best = arr[1] - arr[0];
for (int i = 0; i < size; i++)
    for (int j = i + 1; j < size; j++)
        best = max(best, arr[j] - arr[i]);
```

`O(n²)` — every pair.

### 💡 Insight

> **Fix `j` as the "sell" index. Its best partner is simply the smallest element before
> it.** So walk left → right carrying the prefix minimum; the answer is the best
> `arr[j] − min_so_far`.

### 💻 Code

```cpp
int get_maximum_difference(int arr[], int size)
{
    if (size <= 1) return 0;                     // need at least one pair

    int difference = arr[1] - arr[0],            // seed with the first REAL pair
        smallest   = arr[0];

    for (int i = 1; i < size; i++)
    {
        difference = max(difference, arr[i] - smallest);   // 1. use the min BEFORE i
        smallest   = min(smallest, arr[i]);                // 2. then let i join the prefix
    }
    return difference;
}
```

### 🔍 Trace — `[2, 4, 10, 5, 4, 1]`

```text
i   arr[i]   min before i   arr[i] − min   best   min after
1     4          2              2            2        2
2    10          2              8            8 ✅     2
3     5          2              3            8        2
4     4          2              2            8        2
5     1          2             −1            8        1
answer → 8   (buy at 2, sell at 10)
```

### Why the update order matters

```text
compute THEN update min     arr[i] is compared with elements strictly BEFORE it   ✅
update min THEN compute     arr[i] may be compared with ITSELF → difference 0     ❌
```

On `[10, 6, 2]` the wrong order returns `0`; the correct answer is `−4`.

### ⏱️ Complexity

`O(n)` time, `O(1)` space.

### ⚠️ Pitfalls

| ✗ Mistake | Result |
|---|---|
| Updating `smallest` before computing the difference | Pairs `i` with itself → `0` on decreasing arrays |
| Seeding `difference = 0` | `[10, 6, 2]` gives `0` instead of `−4` (if a pair is mandatory) |
| Answering `max − min` | Ignores order — the min might come **after** the max (`[10, 1]`) |
| Tracking the running **max** instead | Answers "max `arr[i] − arr[j]`" — the opposite question |

### 🔀 Variants

| Follow-up | Change |
|---|---|
| **Stock buy & sell, one transaction** | Identical, but profit floors at `0` (you may skip trading): seed `best = 0` |
| Return the pair of indices | Remember the index of the min, and `(min_index, i)` when `best` improves |
| Minimum difference `arr[j] − arr[i]`, `j > i` | Carry the prefix **max**, minimise |
| Max `arr[j] − arr[i]` with **any** order | Just `max − min` — order no longer matters |

> 🔗 **Multiple** stock transactions is a different idea — summing every upward step — see
> [note 009](009-greedy-and-stock-buy-sell.md).

---

## 2. Carry a Variable, or Store an Array?

```text
each element needs ONE side, processed in walk order    →  carry a single variable
                                                            (leaders, maximum difference)

each element needs BOTH sides at once                   →  precompute prefix AND suffix
                                                            ARRAYS, then combine
                                                            (trapping rain water)
```

You can't carry the right side's max while walking left → right — you haven't seen it yet.
So when both sides matter, **store** one or both as arrays first.

```text
leaders               suffix max     carried            this note
maximum difference    prefix min     carried            this note
trapping rain water   prefix max + suffix max, stored   → note 010
kadane                best sum ending here, carried     → note 011
```

---

## 3. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Walking the wrong direction | You'd need information you haven't seen yet |
| Forgetting leaders print reversed | Output order surprises the interviewer |
| Updating the extreme before using it | Element compared with itself |
| `max − min` for an ordered-pair problem | Ignores the `j > i` constraint |
| Keeping a nested loop "for safety" | `O(n²)` — the whole point is to remove it |

---

## 4. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Core trick?** | Replace "compare with every element on one side" by "compare with that side's best". |
| **How to pick the walk direction?** | Walk from the side you're summarising, so it's already been seen. |
| **Leader?** | `>=` everything on its right. Walk right → left with a running max. |
| **Leaders' output order?** | Right to left — reverse it if needed. |
| **Max difference with `j > i`?** | Running prefix min; answer `max(arr[j] − min_before_j)`. |
| **Order of the two updates?** | Compute the difference first, then update the min. |
| **Why not `max − min`?** | The min must come **before** the max. |
| **Related interview problem?** | Stock buy/sell with one transaction (profit floored at 0). |
| **When do you store arrays instead?** | When each element needs both sides — trapping rain water. |
| **Complexity?** | `O(n)` time, `O(1)` space — down from `O(n²)`. |

---

## 5. The Mental Model

```text
         "element i vs everything on ONE side"
                         │
            only that side's BEST matters?
                         │ yes
            ┌────────────┴────────────┐
            ↓                         ↓
       right side                 left side
    walk ← · suffix max       walk → · prefix min
            │                         │
         leaders              maximum difference
     (prints reversed)       (use, THEN update min)
                                      │
                               ≈ one-shot stock
```
