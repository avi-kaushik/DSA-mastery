# 013 — Minimum Consecutive Flips (Minimum Group Flips)

> **One-line takeaway:** In a binary array, groups of `0`s and `1`s **alternate**, so the two kinds of groups differ in count by at most one — and the type of the **first** group is never the smaller side. So never flip the first element's type; flip every group of the **other** type. One pass over the group boundaries, no counting needed.

📄 [`flips.cpp`](../../programs/cpp/data_structures/arrays/intermediate/flips.cpp) ·
📄 [`MaximumConsecutiveFlips.java`](../../programs/java/arrays/basic/MaximumConsecutiveFlips.java)

---

## 1. What We Want

A binary array. One **flip** inverts a **contiguous range** (every `0` ↔ `1` inside it).
Make all elements equal using the **minimum number of flips**, and print the ranges.

```text
[0, 1, 1, 0, 0]              →  From 1 to 2.                        (1 flip)
[1, 0, 0, 1, 0, 1, 1]        →  From 1 to 2.  From 4 to 4.          (2 flips)
[1, 1, 1]                    →  (nothing — already equal)
```

---

## 2. Think in Groups, Not Elements

A **group** is a maximal run of equal values — the same "run" idea as
[note 008](008-counting-runs.md).

```text
[1 | 0 0 | 1 | 0 | 1 1]
 G1   G2   G3  G4   G5        five groups:   1-groups: G1 G3 G5     0-groups: G2 G4
```

### Why flipping whole groups is enough

Flipping part of a group splits it and creates **more** groups to fix. Flipping exactly one
group merges it with both neighbours. So an optimal answer flips whole groups, one at a time,
all of the **same type** — and needs exactly as many flips as there are groups of that type.

```text
the answer  =  min( number of 0-groups,  number of 1-groups )
```

---

## 3. 💡 The Insight — the First Group Always Wins

> **Groups alternate: `A B A B A …`. So the type that starts the array has either the
> same number of groups as the other type, or one more. It's never fewer.**

```text
starts with A, ends with A:   A B A B A     A-groups = B-groups + 1
starts with A, ends with B:   A B A B       A-groups = B-groups
```

> 🔑 So **the type of `arr[0]` is never the cheaper one to flip.** Flip every group whose
> value differs from `arr[0]` — it's always the minimum (tied, at worst).

No counting pass needed — you only have to **find the boundaries** of the groups that differ
from `arr[0]`.

### The naive alternative

```text
pass 1: count 0-groups and 1-groups
pass 2: print the ranges of the smaller type
```

Also `O(n)`, but two passes and more code. The insight removes pass 1.

---

## 4. Finding the Boundaries

Every boundary is an index `i` with `arr[i] != arr[i − 1]`. There are two kinds:

```text
arr[i] != arr[0]    →  a group to flip STARTS at i         print "From i to "
arr[i] == arr[0]    →  a group to flip ENDED at i − 1      print "i − 1."
```

And the classic run problem: if the array **ends** inside a flip-group, no boundary ever
closes it — so close it after the loop.

---

## 5. 💻 Code (implemented)

```cpp
void minimum_consecutive_flips(const int arr[], int size)
{
    if (size == 0) return;

    for (int i = 1; i < size; i++)
    {
        if (arr[i] != arr[i - 1])                    // a group boundary
        {
            if (arr[i] != arr[0])
                cout << "From " << i << " to ";      // a flip-group starts here
            else
                cout << i - 1 << "." << endl;        // the flip-group ended at i − 1
        }
    }

    if (arr[size - 1] != arr[0])                     // still open at the end?
        cout << size - 1 << "." << endl;             //   → close it
}
```

---

## 6. 🔍 Trace — `[0, 1, 1, 0, 0]`

```text
arr[0] = 0  →  flip every group of 1s

i   arr[i−1] → arr[i]   boundary?   arr[i] vs arr[0]   print
1       0 → 1              ✓          differs          "From 1 to "
2       1 → 1              ✗
3       1 → 0              ✓          same             "2."
4       0 → 0              ✗
end: arr[4] = 0 == arr[0]  → nothing open

output:  From 1 to 2.
```

### 🔍 Trace — ends inside a flip-group, `[1, 0, 0, 1, 0, 1, 1, 0]`

```text
arr[0] = 1  →  flip every group of 0s

i=1  1→0  differs   "From 1 to "
i=3  0→1  same      "2."
i=4  1→0  differs   "From 4 to "
i=5  0→1  same      "4."
i=7  1→0  differs   "From 7 to "
end: arr[7] = 0 ≠ arr[0]  → "7."     ← without the final check, this line is left hanging

output:  From 1 to 2.   From 4 to 4.   From 7 to 7.        (3 flips; 1-groups = 3 too — tie)
```

---

## 7. ⏱️ Complexity

`O(n)` time — one pass. `O(1)` space — nothing stored, ranges printed as they're found.

---

## 8. ⚠️ Pitfalls

| ✗ Mistake | Result |
|---|---|
| Missing the final "still open" check | Output ends with a dangling `From x to` |
| Flipping groups of `arr[0]`'s type | Can be one flip too many |
| Comparing `arr[i]` with `arr[i − 1]` to decide start vs end | Every boundary differs from its left neighbour — you need `arr[0]` to tell starts from ends |
| Assuming non-binary input | The "two alternating types" argument needs exactly two values (the repo notes this) |
| Flipping individual elements | `O(n)` flips instead of `O(groups)` |

---

## 9. 🔀 Variants

| Follow-up | Approach |
|---|---|
| Return only the **number** of flips | Count boundaries `b`; groups `= b + 1`; answer `= ⌊(b + 1) / 2⌋` |
| Min flips to make the array **alternating** (`0101…`) | Not groups — compare with both patterns `0101…` and `1010…`, take the fewer mismatches |
| Min flips with a **fixed window of `k`** (flip exactly `k` consecutive) | Greedy left → right + sliding window of "active flips" |
| Max consecutive ones with `k` single-element flips | Sliding window ([note 014](014-sliding-window.md)) |

```text
count-only check:  [1 0 0 1 0 1 1]   boundaries = 4  → groups = 5 → ⌊5/2⌋ = 2 ✅
```

---

## 10. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **What's a flip?** | Inverting a contiguous range. |
| **Why flip whole groups?** | Partial flips create more groups to fix. |
| **Minimum number of flips?** | `min(#0-groups, #1-groups)`. |
| **Which type to flip?** | Whichever **differs from `arr[0]`** — the first type never has fewer groups. |
| **Why?** | Groups alternate, so the first type has equal or one more group. |
| **How do you spot a flip-group start / end?** | At a boundary: `arr[i] != arr[0]` → start; `arr[i] == arr[0]` → end at `i − 1`. |
| **The classic bug?** | Forgetting to close a flip-group that runs to the end. |
| **Count-only formula?** | `⌊(boundaries + 1) / 2⌋`. |
| **Complexity?** | `O(n)` time, `O(1)` space, one pass. |

---

## 11. The Mental Model

```text
            binary array  →  alternating groups  A B A B …
                                  │
              first type has ≥ as many groups as the other
                                  │
                     flip every group ≠ arr[0]
                                  │
               walk boundaries (arr[i] != arr[i − 1])
                     ╱                         ╲
           arr[i] != arr[0]               arr[i] == arr[0]
           "From i to"                    "i − 1."
                                  │
               after the loop: last group ≠ arr[0]?  → close at n − 1
```
