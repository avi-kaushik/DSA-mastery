# 016 — Difference Array: Maximum Appearing Element in Ranges

> **One-line takeaway:** To add `+1` to a whole range `[L, R]`, don't touch every value — mark `+1` at `L` and `−1` at `R + 1`. One **prefix sum** at the end turns those marks into the real counts. Range updates become `O(1)` each; the difference array is the **inverse** of a prefix sum.

📄 [`maximum_appearing_element.cpp`](../../programs/cpp/data_structures/arrays/intermediate/maximum_appearing_element.cpp) ·
📄 [`MaximumAppearingElementInRanges.java`](../../programs/java/arrays/basic/MaximumAppearingElementInRanges.java)

---

## 1. What We Want

You're given `n` inclusive ranges as two arrays, `left[i] … right[i]`. Which value is
covered by the **most** ranges? (On a tie, return the smallest.)

```text
left  = [1, 2, 5]
right = [3, 5, 7]

ranges     [1 … 3]   [2 … 5]   [5 … 7]

value      0  1  2  3  4  5  6  7
covered    0  1  2  2  1  2  1  1
                 ↑
           answer: 2   (ties with 3 and 5 — smallest wins)
```

---

## 2. The Naive Version — Paint Every Value

```cpp
vector<int> count(max_right + 1, 0);
for (int i = 0; i < n; i++)
    for (int v = left[i]; v <= right[i]; v++)      // walk the whole range
        count[v]++;
```

`O(n · m)`, where `m` is the average range length. A thousand ranges of length a million
→ a billion increments.

---

## 3. 💡 The Insight — Mark Where Coverage Changes, Not Where It Is

> **Coverage only changes at two places per range: it goes up by 1 at `L` and down by 1
> just after `R`.** Record just those two changes. A running total (prefix sum) of the
> changes rebuilds the coverage at every value.

```text
range [2 … 5]:     mark +1 at 2,   −1 at 6

index     0   1   2   3   4   5   6   7
diff      0   0  +1   0   0   0  −1   0
prefix    0   0   1   1   1   1   0   0      ← exactly the coverage of [2 … 5]
```

**Analogy — counting people in a room.** You don't need to know where everyone is at every
minute. Log `+1` when someone walks in and `−1` when someone walks out. The number of people
in the room at any time is the **running total** of the log up to that time.

> 🔑 Each range now costs **`O(1)`** — two writes — no matter how long it is.

---

## 4. Why the Prefix Sum Undoes It

Define the difference array as how much the coverage **changes** from one index to the next:

```text
diff[i] = cover[i] − cover[i − 1]
```

Summing the changes telescopes back to the value:

```text
diff[0] + diff[1] + … + diff[i]
  = cover[0] + (cover[1] − cover[0]) + … + (cover[i] − cover[i−1])
  = cover[i]                                            ← everything else cancels
```

| | Prefix sum ([note 015](015-prefix-sum.md)) | Difference array |
|---|---|---|
| Turns… | values → running totals | values → changes between neighbours |
| Makes cheap | **range queries** (sum of `l … r` in `O(1)`) | **range updates** (add to `l … r` in `O(1)`) |
| Inverse of | difference | prefix sum |

> Think calculus: prefix sum is **integration**, difference is **differentiation**. Each
> undoes the other.

---

## 5. 💻 Code (implemented)

```cpp
int get_maximum_appearing_element(const int left[], const int right[], int size)
{
    if (size == 0) return 0;

    int frequency_limit = get_maximum_value(right, size) + 2;   // indices 0 … max_right + 1
    vector<int> frequency(frequency_limit, 0);

    for (int i = 0; i < size; i++)            // 1. two marks per range — O(1) each
    {
        frequency[left[i]]++;                 //    coverage starts at left[i]
        frequency[right[i] + 1]--;            //    and stops just after right[i]
    }

    vector<int> prefix_sum = get_prefix_sum(frequency);    // 2. marks → real coverage

    int maximum_appearing_element = 0;        // 3. first index with the highest coverage
    for (int i = 1; i < frequency_limit; i++)
        if (prefix_sum[i] > prefix_sum[maximum_appearing_element])   // strict > keeps the smallest on ties
            maximum_appearing_element = i;

    return maximum_appearing_element;
}
```

### Why `max_right + 2`?

```text
indices needed:   0 … max_right        the values themselves
                  max_right + 1        where the LAST range's −1 lands
total size:       max_right + 2
```

Size it `max_right + 1` and `right[i] + 1` writes past the end for the range that reaches
the maximum.

---

## 6. 🔍 Trace — `left = [1, 2, 5]`, `right = [3, 5, 7]`

```text
size = 7 + 2 = 9   (indices 0 … 8)

mark [1 … 3]    +1 at 1, −1 at 4
mark [2 … 5]    +1 at 2, −1 at 6
mark [5 … 7]    +1 at 5, −1 at 8

index      0   1   2   3   4   5   6   7   8
diff       0  +1  +1   0  −1  +1  −1   0  −1
prefix     0   1   2   2   1   2   1   1   0
                   ↑
       first maximum (2) at index 2  →  answer 2
```

### 🔍 The repo's demo

```text
left  = [1, 2, 5, 15, 18, 20, 22]
right = [5, 8, 7, 30, 25, 24, 24]

values 22 … 24 sit inside [15…30], [18…25], [20…24], [22…24]   → covered 4 times
answer → 22   (the smallest of the tied values)
```

---

## 7. ⏱️ Complexity

| Approach | Time | Space |
|---|---|---|
| Paint every value | `O(n · m)` | `O(M)` |
| **Difference array** | **`O(n + M)`** | `O(M)` |

`n` = number of ranges, `m` = average range length, `M` = largest endpoint.

> ⚠️ The cost depends on **`M`, the value range**, not just `n`. Three ranges ending near
> `10⁹` would need a billion-slot array. Then switch to a **sweep line**: sort the `2n`
> events `(L, +1)` and `(R + 1, −1)` and scan them — `O(n log n)` time, `O(n)` space,
> independent of `M`.

---

## 8. The General Tool — Many Range Updates, Then Read

The maximum-appearing problem is one use of a broader pattern:

> **"Apply `q` updates of the form *add `v` to every element in `[l, r]`*, then report the
> final array."**

```cpp
vector<long long> diff(n + 1, 0);
for (auto [l, r, v] : updates) {          // each update: O(1)
    diff[l]     += v;
    diff[r + 1] -= v;
}
long long running = 0;
for (int i = 0; i < n; i++) {             // one prefix pass: O(n)
    running += diff[i];
    arr[i]  += running;
}
```

`O(n + q)` instead of `O(n · q)`.

> ⚠️ This works when **all updates come before all reads**. If reads and updates are mixed,
> you need a Fenwick tree or segment tree with lazy propagation.

### Problems that are this pattern in disguise

| Problem | Ranges are… | Then |
|---|---|---|
| Maximum appearing element (this note) | the given `[L, R]` | max of the coverage |
| Minimum railway platforms | `[arrival, departure]` | max coverage = platforms needed |
| Car pooling — can one car carry everyone? | `[pickup, drop)` with `v = passengers` | any coverage `> capacity`? |
| Corporate flight bookings | `[first, last]` with `v = seats` | the final array |
| Meeting rooms needed | `[start, end)` | max coverage |

> 💡 Watch the endpoint convention: a passenger who gets off at stop `5` frees the seat **at**
> `5` → half-open `[pickup, drop)`, so the `−v` goes at `drop`, not `drop + 1`.

---

## 9. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Putting `−1` at `R` instead of `R + 1` | The range's last value isn't counted |
| Array sized `max_right + 1` | `right + 1` writes out of bounds |
| Forgetting the prefix-sum pass | Reading the raw marks instead of coverage |
| `>=` when picking the max | Returns the **largest** tied value, not the smallest |
| Using it with huge or negative coordinates | Enormous array / negative index — sort events or compress coordinates |
| Mixing updates and queries | Difference arrays only work for "all updates, then reads" |

---

## 10. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Difference array in one line?** | `+v` at `L`, `−v` at `R + 1`; a prefix sum rebuilds the values. |
| **Why does the prefix sum work?** | Summing neighbour-to-neighbour changes telescopes back to the value. |
| **Relation to prefix sums?** | Inverses — like differentiation and integration. |
| **Prefix sum makes cheap…?** | Range **queries**. Difference array makes cheap range **updates**. |
| **Max appearing element complexity?** | `O(n + M)` time and `O(M)` space, `M` = largest endpoint. |
| **Why size `max_right + 2`?** | The `−1` for the largest range lands at `max_right + 1`. |
| **Smallest value on ties?** | Strict `>` when scanning left → right. |
| **Endpoints too large?** | Sweep line over sorted events, `O(n log n)`. |
| **Limitation?** | All updates must happen before the reads. |
| **Same idea elsewhere?** | Min platforms, meeting rooms, car pooling, flight bookings. |

---

## 11. The Mental Model

```text
          many "add v to [L, R]" updates
                        │
       don't paint the range — mark where it CHANGES
                        │
            diff[L] += v       diff[R + 1] −= v       O(1) per range
                        │
            one prefix sum over diff                  O(M)
                        │
             real values / coverage at every index
                        │
       ┌────────────────┼────────────────────┐
       ↓                ↓                    ↓
  max coverage     any > capacity?      final array
  (max appearing,  (car pooling)        (flight bookings)
   platforms)
                        │
      huge coordinates → sort events (sweep line)
```
