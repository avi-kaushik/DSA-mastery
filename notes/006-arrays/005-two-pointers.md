# 005 — Two Pointers: Reverse, Remove Duplicates, Move Zeros

> **One-line takeaway:** Two indices, two jobs. Either they **walk towards each other** (reverse), or one **reads** every element while the other **writes** only the ones you keep (remove / compact). Both turn "build a new array" into **in-place, `O(1)` space** — and the read/write version turns repeated `O(n)` deletes into a single `O(n)` pass.

📄 [`basic_operations.h`](../../programs/cpp/include/arrays/basic_operations.h) ·
📄 [`remove_elements.cpp`](../../programs/cpp/data_structures/arrays/basic/remove_elements.cpp) ·
📄 [`move_zeros.cpp`](../../programs/cpp/data_structures/arrays/basic/move_zeros.cpp)

---

## 1. Why Two Pointers?

Take "remove every `20` from `[10, 20, 20, 30, 20]`". Two obvious approaches, both bad:

```text
A. repeated delete      find a 20, shift the tail left, repeat     O(n) per delete → O(n²)
B. new array            copy the non-20s into a second array       O(n) time, but O(n) space
```

Two pointers gets the best of both: **one pass** like B, **no extra array** like A.

```text
C. read / write         read every element, write the keepers      O(n) time, O(1) space ✅
                        over the front of the SAME array
```

> 💡 The general trick: when you'd naturally build a new array, ask whether you can build
> it **over the front of the old one**, because you've already finished reading that part.

---

## 2. The Two Shapes

```text
OPPOSITE ENDS  (converging)                SAME DIRECTION  (read / write)

 left →             ← right                 write
 [ 1  2  3  4  5 ]                            ↓
                                            [ 10 20 20 30 30 ]
 meet in the middle                              ↑
                                                read  (always ahead of, or level with, write)
```

| | Opposite ends | Read / write (slow / fast) |
|---|---|---|
| **Used for** | reverse, palindrome, pair-sum in a sorted array | remove, dedupe, compact, partition |
| **Moves** | both inward every step | `read` every step, `write` only on keep |
| **Stops** | `left >= right` | `read` reaches the end |
| **Returns** | usually nothing (in-place change) or a found pair | the **new logical size** = `write` |

---

# Card 1 — Reverse an Array (Opposite Ends)

**Shape: converging** · 📄 [`basic_operations.h`](../../programs/cpp/include/arrays/basic_operations.h)

### 🎯 Problem

Reverse the elements of `arr[start … size)` in place. By default, the whole array.

### 💡 Insight

> **The first and last elements swap places, then the problem is the same thing one step
> smaller.** Swap the ends, move both pointers inward, stop when they meet.

### 💻 Code

```cpp
inline void reverse(int arr[], int size, int start = 0)     // reverses [start, size)
{
    if (size < 0 || start < 0 || start > size)
        throw std::out_of_range("Invalid reversal range.");

    int left = start, right = size - 1;
    while (left < right)
    {
        int temp = arr[left];          // swap the two ends
        arr[left] = arr[right];
        arr[right] = temp;
        left++;                        // move both inward
        right--;
    }
}
```

### 🔍 Trace

```text
odd length  [1 2 3 4 5]
  l=0 r=4  swap → [5 2 3 4 1]
  l=1 r=3  swap → [5 4 3 2 1]
  l=2 r=2  stop — the middle is already in place                 2 swaps

even length [1 2 3 4]
  l=0 r=3  swap → [4 2 3 1]
  l=1 r=2  swap → [4 3 2 1]
  l=2 r=1  stop — pointers crossed                               2 swaps
```

### ⏱️ Complexity

`⌊n/2⌋` swaps → `O(n)` time, `O(1)` space. Each swap fixes **two** positions.

### ⚠️ Pitfalls

- `left < right`, not `<=`: with `<=` the middle element swaps with itself — harmless, but
  shows you don't know why the loop stops.
- The range is **half-open** `[start, size)`: `size` is the exclusive end, so the last
  element touched is `arr[size − 1]`.
- Looping `i` from `0` to `n` and swapping `arr[i]` with `arr[n−1−i]` reverses **twice** —
  back to the original. Stop at `n/2`.

### 🔀 Variants

| Follow-up | Note |
|---|---|
| Reverse a **sub-range** | That's what `start` is for — the rotation trick in [note 006](006-array-rotation.md) depends on it |
| Palindrome check | Same pointers, compare instead of swap; stop at the first mismatch |
| Pair with a given sum in a **sorted** array | Sum too small → `left++`; too big → `right--` (📄 [`TwoPointerApproachToFindGivenPairSum.java`](../../programs/java/arrays/searching/TwoPointerApproachToFindGivenPairSum.java)) |
| Library | `std::reverse(a, a + n)` · `Collections.reverse(list)` |

---

## 3. The Read / Write Template ⭐

Every problem in the rest of this note is this loop with a different **keep condition**:

```cpp
int write = <first slot to fill>;
for (int read = <start>; read < size; read++)
    if (<keep arr[read]?>)
        arr[write++] = arr[read];
return write;                          // new size; arr[0 … write−1] is the answer
```

### The invariant — why it's safe to overwrite

```text
at every step:   arr[0 … write)  =  the answer for the elements arr[0 … read)
and              write <= read
```

> 🔑 `write` never overtakes `read`, so every slot you overwrite has **already been read**.
> Nothing unread is ever destroyed. That one fact is what makes in-place compaction legal.

### What's left after `write`?

```text
[ 10 20 30 | 30 30 ]       return 3
  └ answer ┘ └ junk ┘      leftovers from the old array — outside the logical size, ignored
```

Exactly the capacity/size idea from [note 002](002-array-operations.md): the function
returns the new size, and the caller must use it.

---

# Card 2 — Remove Every Occurrence of a Value

**Shape: read / write** · 📄 [`remove_elements.cpp`](../../programs/cpp/data_structures/arrays/basic/remove_elements.cpp)

### 🎯 Problem

Remove all occurrences of `value` in place; return the new size. Order of the rest must be
preserved.

### 💡 Insight

> **Don't delete the bad elements — copy the good ones forward.** Deleting shifts the whole
> tail each time; copying forward touches each element once.

### 💻 Code

```cpp
int remove_values(int arr[], int size, int value)
{
    int pointer = 0;                          // next slot for a kept element
    for (int i = 0; i < size; i++)
        if (arr[i] != value)
            arr[pointer++] = arr[i];
    return pointer;
}
```

### 🔍 Trace — remove `20` from `[10, 20, 20, 30, 20]`

```text
read  arr[read]  keep?   array after                write
 0       10       ✓     [10 20 20 30 20]             1
 1       20       ✗                                  1
 2       20       ✗                                  1
 3       30       ✓     [10 30 20 30 20]             2
 4       20       ✗                                  2
return 2  →  [10 30]
```

### ⏱️ Complexity

`O(n)` time, `O(1)` space — versus `O(n²)` for "find and delete" repeatedly.

---

# Card 3 — Remove Duplicates from a Sorted Array

### 🎯 Problem

Keep one copy of each value in a **sorted** array; return the number of distinct values.

### 💡 Insight

> **In a sorted array, equal values are adjacent.** "Is this a duplicate?" becomes "is it
> equal to its left neighbour?" — an `O(1)` check, no set required.

### 💻 Code

```cpp
int remove_duplicates(int arr[], int size)
{
    if (size <= 1) return size;               // 0 or 1 element: already unique

    int pointer = 1;                          // arr[0] is always kept
    for (int i = 1; i < size; i++)
        if (arr[i] != arr[i - 1])             // first of a new value
            arr[pointer++] = arr[i];
    return pointer;
}
```

### 🔍 Trace — `[10, 20, 20, 30, 30]`

```text
read  arr[read]  arr[read−1]  new value?   array after          write
 1       20          10           ✓        [10 20 20 30 30]       2
 2       20          20           ✗                               2
 3       30          20           ✓        [10 20 30 30 30]       3
 4       30          30           ✗                               3
return 3  →  [10 20 30]
```

> 💡 Reading `arr[i − 1]` is safe even though the front is being rewritten: `i − 1 >=
> pointer − 1`, so that slot is either untouched or was just rewritten **with the same
> value**.

### ⚠️ Pitfalls

- **Unsorted input** — non-adjacent duplicates survive. Use a hash set instead (`O(n)` time,
  `O(n)` space), or sort first (`O(n log n)`).
- Starting at `pointer = 1` without the `size <= 1` guard returns `1` for an empty array.

---

# Card 4 — Keep At Most `k` Copies of Each Value

### 🎯 Problem

In a sorted array, keep at most `k` occurrences of every value. (`k = 1` is Card 3.)

### 💡 Insight

> **Count the length of the current run.** Reset to 1 on a new value, keep while the count
> is `<= k`.

### 💻 Code

```cpp
int remove_duplicates_at_most_k(int arr[], int size, int occurrence_limit)
{
    if (size == 0 || occurrence_limit == 0) return 0;

    int pointer = 1, occurrence_counter = 1;
    for (int i = 1; i < size; i++)
    {
        if (arr[i] == arr[i - 1]) occurrence_counter++;   // run continues
        else                      occurrence_counter = 1; // new value

        if (occurrence_counter <= occurrence_limit)
            arr[pointer++] = arr[i];
    }
    return pointer;
}
```

### 🔍 Trace — `k = 2`, `[10, 10, 10, 20, 30, 30, 30]`

```text
read  value  run  keep?    write
 1     10     2     ✓        2
 2     10     3     ✗        2
 3     20     1     ✓        3
 4     30     1     ✓        4
 5     30     2     ✓        5
 6     30     3     ✗        5
return 5  →  [10 10 20 30 30]
```

### The counter-free alternative

```cpp
int write = 0;
for (int read = 0; read < size; read++)
    if (write < k || arr[read] != arr[write - k])     // "the element k places back
        arr[write++] = arr[read];                      //  in the OUTPUT isn't me"
```

If the output already has `k` copies of this value, they'd fill `arr[write−k … write−1]` —
so `arr[write − k]` would equal it. Elegant, but the counter version is easier to explain.

---

# Card 5 — Move Zeros to the End

📄 [`move_zeros.cpp`](../../programs/cpp/data_structures/arrays/basic/move_zeros.cpp)

### 🎯 Problem

Move every `0` to the end, keeping the **relative order** of the non-zero elements.

### 💡 Insight

> **It's "remove all zeros", followed by refilling the tail with zeros.** The count of zeros
> is simply `size − write`.

### 💻 Code — two phases (implemented)

```cpp
void move_zeros(int arr[], int size)
{
    int pointer = 0;
    for (int i = 0; i < size; i++)          // phase 1: compact non-zeros (Card 2 with value 0)
        if (arr[i] != 0)
            arr[pointer++] = arr[i];

    for (int i = pointer; i < size; i++)    // phase 2: fill the rest with zeros
        arr[i] = 0;
}
```

### 🔍 Trace — `[10, 0, 20, 0, 30]`

```text
phase 1    [10 20 30 | 0 30]      write = 3    (tail is leftover junk)
phase 2    [10 20 30   0  0]
```

### One-phase alternative — swap instead of copy

```cpp
int write = 0;
for (int read = 0; read < size; read++)
    if (arr[read] != 0)
        swap(arr[write++], arr[read]);      // zeros drift right as non-zeros jump left
```

```text
[10 0 20 0 30]
read 0: swap(0,0)   [10 0 20 0 30]   w=1
read 2: swap(1,2)   [10 20 0 0 30]   w=2
read 4: swap(2,4)   [10 20 30 0 0]   w=3
```

| | Two-phase | Swap |
|---|---|---|
| Passes | 2 | 1 |
| Writes | exactly `n` | 2 per non-zero (including pointless self-swaps before the first zero) |
| Non-zero order preserved? | ✅ | ✅ |

Both `O(n)` time, `O(1)` space.

### ⚠️ Pitfalls

- Forgetting phase 2 — the tail still holds old non-zero values.
- "Swap every zero with the last element" moves zeros to the end but **scrambles** the order.

---

## 4. All Four Read/Write Problems Side by Side

| Problem | `write` starts | `read` starts | Keep `arr[read]` when… | Needs sorted? | Extra step |
|---|---|---|---|---|---|
| Remove `value` | 0 | 0 | `arr[read] != value` | no | — |
| Remove duplicates | 1 | 1 | `arr[read] != arr[read − 1]` | **yes** | guard `size <= 1` |
| At most `k` | 1 | 1 | run length `<= k` | **yes** | track the run |
| Move zeros | 0 | 0 | `arr[read] != 0` | no | fill tail with `0` |

> **That's the pattern to recognise.** The loop never changes; only *where you start* and
> *what you keep* do. The same template also drives partitioning (Quick Sort, Dutch National
> Flag) later.

---

## 5. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Returning nothing / ignoring the new size | Caller prints junk past the logical end |
| Dedupe on an **unsorted** array | Non-adjacent duplicates survive |
| Repeated delete-and-shift to remove values | `O(n²)` instead of `O(n)` |
| `left <= right` in reverse | Harmless self-swap, but signals shaky reasoning |
| Reversing with a full `0 … n` loop | Reverses twice — no change |
| Forgetting phase 2 in move zeros | Old values remain at the tail |
| Swap-with-last for move zeros | Correct zeros, broken order |

---

## 6. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Two kinds of two pointers?** | Opposite ends (converge) and read/write (same direction). |
| **Why is read/write safe in place?** | `write <= read` — you only overwrite what you've already read. |
| **The invariant?** | `arr[0 … write)` is the answer for everything read so far. |
| **What does a remove function return?** | The new logical size (= `write`). |
| **Why not delete each unwanted element?** | Each delete shifts → `O(n²)`; read/write is `O(n)`. |
| **Remove duplicates — keep condition?** | `arr[read] != arr[read − 1]`; requires a sorted array. |
| **Unsorted duplicates?** | Hash set, `O(n)` time and space. |
| **At most `k` copies?** | Count the current run; keep while `run <= k`. |
| **Move zeros?** | Compact non-zeros to the front, then fill the rest with `0`. Order preserved. |
| **Reverse — how many swaps?** | `⌊n/2⌋`; stop when `left >= right`. |
| **Complexity of everything here?** | `O(n)` time, `O(1)` extra space. |

---

## 7. The Mental Model

```text
                    Two pointers
                         │
          ┌──────────────┴───────────────┐
          ↓                              ↓
    OPPOSITE ENDS                  READ / WRITE
    swap & converge          read everything, write keepers
    stop when l >= r          write <= read  →  safe in place
          │                              │
   reverse [start, size)     remove value · dedupe (sorted) ·
   palindrome · pair sum      at most k (sorted) · move zeros
          │                              │
          └──── O(n) time · O(1) space ──┘
```
