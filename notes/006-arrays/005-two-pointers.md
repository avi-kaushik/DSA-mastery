# 005 — Two Pointers

> **One-line takeaway:** Two indices, each with a clear **meaning**, that only ever move **forward** (or inward). Because neither pointer goes back, they make at most `2n` moves in total — so a problem that looks like "check every pair" (`O(n²)`) becomes `O(n)`. The trick is never the pointers themselves; it's the **reason you're allowed to move one** without missing anything.

---

## 1. The Problem This Concept Solves

Two kinds of pain lead to two pointers.

**Pain 1 — checking every pair.**

> *Is there a pair in a **sorted** array that sums to `T`?*

The obvious way tries all pairs: `n(n−1)/2` of them → `O(n²)`.

**Pain 2 — building a new array when you're not allowed one.**

> *Remove every `0` from the array, in place, keeping the order.*

The obvious ways either delete one element at a time (each delete shifts the tail →
`O(n²)`), or copy the keepers into a second array (`O(n)` extra space).

Two pointers fixes both — `O(n)` time, `O(1)` extra space.

---

## 2. The Core Idea — Every Move Must Eliminate Something

> 🔑 **A pointer may move only when you can prove that the element it's leaving behind is
> no longer needed.** That proof is the whole algorithm.

Picture a sorted bookshelf and two friends looking for two books whose page counts add
to exactly 500. One starts at the thinnest book, the other at the thickest.

```text
thin ──────────────────────────────────── thick
 l →                                    ← r

sum too small?  the thin book can't work with ANYONE — even the thickest is not enough
                → the left friend steps right

sum too big?    the thick book can't work with ANYONE — even the thinnest is too much
                → the right friend steps left
```

Every step **retires one book for good**. After at most `n` steps the friends meet, and
every possible pair has been either checked or ruled out.

---

## 3. The Three Shapes

```text
1. OPPOSITE ENDS (converging)        2. SAME DIRECTION (read / write)     3. TWO ARRAYS
                                                                          
 l →               ← r                write                               i →
 [ 1  2  3  4  5  6 ]                   ↓                                 [ 1  4  7 ]
                                      [ 3  0  4  0  5 ]                   j →
 meet in the middle                      ↑                                [ 2  3  9 ]
                                        read  (always ≥ write)
                                                                          advance the smaller
```

| | Opposite ends | Read / write (slow–fast) | Two arrays |
|---|---|---|---|
| **Pointers mean** | the two ends of what's still possible | `read`: next to inspect · `write`: next slot to fill | current position in each array |
| **Moves** | one or both inward | `read` every step, `write` only on "keep" | the one with the smaller value |
| **Typical uses** | reverse, palindrome, pair sum in sorted array | remove, dedupe, compact, partition | merge, intersection, union |
| **Needs** | symmetry, or sortedness to decide which side to drop | a keep/discard rule | both arrays sorted |

---

## 4. Shape 1 — Opposite Ends

### The simplest case: reverse

Swap the two ends, then the problem is the same thing one step smaller.

```cpp
int l = 0, r = n - 1;
while (l < r)
    swap(arr[l++], arr[r--]);
```

```text
[1 2 3 4 5]  →  [5 2 3 4 1]  →  [5 4 3 2 1]      stop: l == r, the middle is already home
```

`⌊n/2⌋` swaps — each fixes **two** positions. A palindrome check is the same walk with
"compare" instead of "swap".

### The interesting case: pair sum in a sorted array

```cpp
int l = 0, r = n - 1;
while (l < r) {
    int sum = arr[l] + arr[r];
    if (sum == T) return true;
    if (sum < T)  l++;          // arr[l] is too small even with the biggest partner
    else          r--;          // arr[r] is too big even with the smallest partner
}
return false;
```

```text
[1  3  4  6  8  11],  T = 10          (showing the VALUES under l and r)

arr[l]=1  arr[r]=11   sum 12  > 10  → r−−   (11 can't pair with anything: 11 + 1 is already too big)
arr[l]=1  arr[r]=8    sum  9  < 10  → l++   (1 can't pair with anything: 1 + 8 is the best it gets)
arr[l]=3  arr[r]=8    sum 11  > 10  → r−−
arr[l]=3  arr[r]=6    sum  9  < 10  → l++
arr[l]=4  arr[r]=6    sum 10  ✅
```

### Why it never misses a pair

> **Invariant:** if a valid pair exists, both of its elements are still inside `[l, r]`.

When `arr[l] + arr[r] < T`, every partner of `arr[l]` inside the window is `≤ arr[r]`, so
every sum involving `arr[l]` is `< T`. Discarding `l` loses nothing. The `>` case is the
mirror image. Each step keeps the invariant, so when the pointers meet without a match,
no pair existed.

### 🧠 Pause and predict

Why does this break on an **unsorted** array?

<details>
<summary>Answer</summary>

The elimination argument needs "everything between `l` and `r` is `≤ arr[r]`". Without
sorting, a bigger partner for `arr[l]` might be hiding in the middle, so discarding `l` can
throw away the answer. Unsorted pair sum uses a **hash set** instead (`O(n)` time, `O(n)`
space) — or sort first (`O(n log n)`).

</details>

> 💡 **3-Sum** is this idea plus one loop: fix the first element, then run pair-sum on the
> rest → `O(n²)` instead of `O(n³)`.

---

## 5. Shape 2 — Read / Write (Compaction)

This is the answer to Pain 2.

> **Don't delete the elements you don't want — copy the ones you do want forward.**

```cpp
int write = 0;
for (int read = 0; read < n; read++)
    if (keep(arr[read]))
        arr[write++] = arr[read];
// arr[0 … write) is the result; write is the new size
```

### Why overwriting is safe

> **Invariant:** `write ≤ read`, and `arr[0 … write)` is the answer for `arr[0 … read)`.

`write` never overtakes `read`, so every slot you overwrite has **already been read**.
Nothing unread is destroyed.

```text
remove 0s from [3 0 4 0 5]

read  value  keep?   array            write
 0      3     ✓     [3 0 4 0 5]         1
 1      0     ✗                         1
 2      4     ✓     [3 4 4 0 5]         2
 3      0     ✗                         2
 4      5     ✓     [3 4 5 0 5]         3
result: first 3 slots → [3 4 5]      (the tail is leftover junk, outside the new size)
```

### One loop, many problems — only the keep rule changes

| Problem | Keep `arr[read]` when… | Extra |
|---|---|---|
| Remove every `x` | `arr[read] != x` | — |
| Remove duplicates (**sorted**) | `arr[read] != arr[read − 1]` | start both at `1` |
| Keep at most `k` copies (**sorted**) | its run length so far `≤ k` | count the run |
| Move zeros to the end | `arr[read] != 0` | then fill `write … n−1` with `0` |

> 💡 **Why "sorted" matters for duplicates:** sorting makes equal values **adjacent**, so
> "have I seen this before?" becomes "is it equal to my left neighbour?" — an `O(1)` check
> instead of a hash lookup. More on adjacency in [Runs & Boundaries](008-runs-and-boundaries.md).

### Compare with the naive way

```text
delete-and-shift each unwanted element     O(n) per delete  →  O(n²)
copy keepers into a new array              O(n) time, O(n) space
read / write                               O(n) time, O(1) space   ✅
```

### Stable vs unstable

Copying forward keeps the kept elements in their original order (**stable**). If order
doesn't matter, there are faster tricks — e.g. "overwrite the unwanted element with the
**last** element and shrink" — but they scramble the order. Always ask whether order
matters.

### Partitioning — the same shape with swaps

Swap instead of copy, and you split the array into "keepers | the rest" in one pass:

```cpp
int write = 0;
for (int read = 0; read < n; read++)
    if (arr[read] < pivot)
        swap(arr[write++], arr[read]);     // small values gather at the front
```

This is the heart of Quick Sort's partition. With **three** pointers (`low`, `mid`, `high`)
you can split into three groups in one pass — the **Dutch National Flag** algorithm
(sort an array of `0`s, `1`s and `2`s).

---

## 6. Shape 3 — One Pointer per Array

Merging two sorted arrays: look at the front of each, take the smaller, advance that pointer.

```cpp
int i = 0, j = 0;
while (i < n && j < m)
    out.push_back(a[i] <= b[j] ? a[i++] : b[j++]);
while (i < n) out.push_back(a[i++]);       // one side may have leftovers
while (j < m) out.push_back(b[j++]);
```

```text
a = [1 4 7]    b = [2 3 9]
take 1 (a) · 2 (b) · 3 (b) · 4 (a) · 7 (a) · 9 (b)   →  [1 2 3 4 7 9]
```

`O(n + m)`. Same elimination idea: the smaller front element can't be beaten by anything
later in either array, so it's safe to output it now. This is the "merge" in Merge Sort.

---

## 7. Why It's `O(n)` — Counting Moves, Not Loops

```text
opposite ends     the gap r − l shrinks by ≥ 1 every step         →  ≤ n steps
read / write      read moves n times; write moves ≤ n times        →  ≤ 2n
two arrays        every step advances i or j                       →  ≤ n + m
```

> 🔑 Judge two-pointer code by **how far each pointer can travel in total**, not by how
> many loops you see. A `while` inside a `for` is still `O(n)` if the inner pointer never
> moves backward — that's exactly the [Sliding Window](012-sliding-window.md) argument.

---

## 8. Recognising Two-Pointer Problems

```text
"sorted array" + "pair / triplet with sum …"          → opposite ends
"palindrome", "reverse"                               → opposite ends
"in place", "O(1) extra space", "remove", "compact"   → read / write
"move all X to the end/front", "partition"            → read / write (swap)
"two sorted arrays", "merge", "intersection"          → one pointer per array
"contiguous subarray with sum/length …"               → sliding window (a cousin)
```

### Where it doesn't apply

- **No order to exploit.** Pair sum on an unsorted array → hashing.
- **Answers that need to look back.** If discarding an element might be wrong later, you
  can't move the pointer — that's a sign of DP or a different structure.

---

## 9. Common Mistakes

| ✗ Mistake | Why it's wrong |
|---|---|
| `l <= r` in reverse / pair sum | Pairs an element with itself |
| Opposite-ends pair sum on unsorted data | The elimination argument needs order |
| Returning nothing from an in-place remove | The caller can't tell where valid data ends |
| Dedupe by neighbours on an unsorted array | Non-adjacent duplicates survive |
| Forgetting leftovers when merging | One array's tail never gets copied |
| Calling nested pointer loops `O(n²)` | Count total pointer movement instead |

---

## 10. Practice in This Repo

| Problem | Shape | 📄 |
|---|---|---|
| Reverse (whole array or a sub-range) | opposite ends | [`basic_operations.h`](../../programs/cpp/include/arrays/basic_operations.h) |
| Remove a value · remove duplicates · keep at most `k` | read / write | [`remove_elements.cpp`](../../programs/cpp/data_structures/arrays/basic/remove_elements.cpp) |
| Move zeros to the end | read / write + fill | [`move_zeros.cpp`](../../programs/cpp/data_structures/arrays/basic/move_zeros.cpp) |
| Pair with a given sum (sorted) | opposite ends | [`TwoPointerApproachToFindGivenPairSum.java`](../../programs/java/arrays/searching/TwoPointerApproachToFindGivenPairSum.java) |
| Trapping rain water in `O(1)` space | opposite ends | [note 007 §6](007-prefix-suffix-extremes.md) |

---

## 11. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **What makes two pointers work?** | Every move discards an element you can prove is no longer needed. |
| **Three shapes?** | Opposite ends · read/write (same direction) · one pointer per array. |
| **Pair sum in a sorted array — the rule?** | Sum too small → `l++`; too big → `r−−`. |
| **Why can't it miss a pair?** | Invariant: any valid pair is still inside `[l, r]`. |
| **Unsorted pair sum?** | Hash set, `O(n)` time and space. |
| **Read/write invariant?** | `write ≤ read`; `arr[0…write)` is the answer so far. |
| **Why is overwriting safe?** | You only write into slots you've already read. |
| **Why sort before deduping?** | Equal values become adjacent — compare with the left neighbour. |
| **Partition / Dutch flag?** | Read/write with swaps; three pointers for three groups. |
| **How to judge the complexity?** | Total distance each pointer travels — never backward → `O(n)`. |

---

## 12. The Mental Model

```text
                   "every pair" or "rebuild the array"
                                 │
              can I PROVE an element is no longer needed?
                                 │ yes
      ┌──────────────────────────┼───────────────────────────┐
      ↓                          ↓                           ↓
 OPPOSITE ENDS              READ / WRITE                TWO ARRAYS
 drop the side that       keep → copy forward        output the smaller
 can't work any more      write ≤ read = safe        front, advance it
      │                          │                           │
 reverse · palindrome     remove · dedupe ·            merge · intersect
 pair sum (sorted)        move zeros · partition
      │                          │                           │
      └────────── pointers never move back → O(n) ──────────┘
```
