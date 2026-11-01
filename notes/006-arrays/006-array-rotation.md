# 006 — Array Rotation: By One, By K, and the Reversal Trick

> **One-line takeaway:** Rotating left by `k` means swapping two blocks: `A B → B A`. The slick way is **three reversals** — reverse `A`, reverse `B`, reverse everything — which gives `O(n)` time with **zero** extra space. And always start with `k = k % n`.

📄 [`left_rotate.cpp`](../../programs/cpp/data_structures/arrays/basic/left_rotate.cpp) ·
📄 [`basic_operations.h`](../../programs/cpp/include/arrays/basic_operations.h)

---

## 1. What Rotation Means

```text
left rotate by 2                          right rotate by 2

[ 19  16 | 42  12  28 ]                   [ 19  16  42 | 12  28 ]
    A          B                                A           B
         ↓                                           ↓
[ 42  12  28 | 19  16 ]                   [ 12  28 | 19  16  42 ]
      B          A                             B          A
```

Every element moves `k` places; the ones that fall off one end wrap around to the other.
Think of a **circular conveyor belt** — rotation just changes which item is in front of you.

### The index mapping

```text
left rotate by k:     new[i] = old[(i + k) % n]
                      the element at old index j lands at (j − k + n) % n

right rotate by k:    new[i] = old[(i − k + n) % n]
```

This formula is the **naive `O(n)` / `O(n)` solution** (fill a new array with it), and the
foundation of the juggling method in §6.

### 🔑 Step zero: normalise `k`

```text
k = k % n
```

Rotating by `n` gives the same array, so `k = 1,000,002` on a 5-element array is really
`k = 2`. Skipping this is the **#1 bug** — it either wastes `O(n·k)` work or indexes past the
end. Guard `n <= 1` first so `% n` can't divide by zero.

---

# Card 1 — Left Rotate by One

### 🎯 Problem

Move every element one place left; the first element goes to the end.

### 💡 Insight

> **Only one element is at risk of being overwritten — the first. Save it, shift
> everyone, drop it at the back.**

### 💻 Code

```cpp
void left_rotate_by_one(int arr[], int size)
{
    if (size <= 1) return;                    // nothing to rotate

    int first = arr[0];                       // 1. save the one that would be lost
    for (int i = 0; i < size - 1; i++)
        arr[i] = arr[i + 1];                  // 2. shift everyone left
    arr[size - 1] = first;                    // 3. drop it at the back
}
```

### 🔍 Trace — `[19, 16, 42, 12, 28]`

```text
first = 19
shift     [16 42 12 28 28]
place     [16 42 12 28 19]
```

### ⏱️ Complexity

`O(n)` time, `O(1)` space. It's the same "save before you overwrite" move as delete in
[note 002](002-array-operations.md) — a delete at index 0 that re-inserts at the end.

---

# Card 2 — Left Rotate by `k` ⭐

### 🎯 Problem

Rotate left by `k` places, in place if possible.

### Approach 1 — rotate by one, `k` times

```cpp
for (int r = 0; r < k; r++) left_rotate_by_one(arr, size);
```

`O(n · k)` time — with `k ≈ n` that's `O(n²)`. Correct but too slow; mention it only as the
starting point.

### Approach 2 — temp array (implemented as `left_rotate`)

> **Save block `A` (the first `k`), slide block `B` to the front, put `A` after it.**

```cpp
void left_rotate(int arr[], int size, int rotate)
{
    if (size <= 1) return;
    int k = rotate % size;
    if (k == 0) return;

    vector<int> temp(k);
    for (int i = 0; i < k; i++)                   // 1. save A = arr[0 … k)
        temp[i] = arr[i];

    for (int i = 0; i < size - k; i++)            // 2. slide B left by k
        arr[i] = arr[i + k];

    int remaining = size - k;
    for (int i = remaining; i < size; i++)        // 3. A goes to the last k slots
        arr[i] = temp[i - remaining];             //    temp index = how far into the tail
}
```

```text
[19 16 42 12 28],  k = 2
save A       temp = [19 16]
slide B      [42 12 28 12 28]
place A      [42 12 28 19 16]   ✅
```

`O(n)` time, **`O(k)` space**. Good, but the interviewer will ask: *"Without the extra array?"*

### Approach 3 — the reversal trick (implemented as `left_rotate_using_reversal`)

### 💡 Insight

> **Reversing a block twice restores it.** Reverse each block on its own, then reverse the
> whole thing: the outer reverse swaps the blocks' order *and* un-reverses each of them.

```text
(Aʳ Bʳ)ʳ  =  (Bʳ)ʳ (Aʳ)ʳ  =  B A
```

(Reversing a concatenation reverses the order of the pieces **and** each piece — like
reading a sentence backwards letter by letter.)

### 💻 Code

```cpp
void left_rotate_using_reversal(int arr[], int size, int rotate)
{
    if (size <= 1) return;
    int k = rotate % size;
    if (k == 0) return;

    reverse(arr, k);           // reverse A = [0, k)
    reverse(arr, size, k);     // reverse B = [k, size)
    reverse(arr, size);        // reverse all
}
```

This is why `reverse` in [`basic_operations.h`](../../programs/cpp/include/arrays/basic_operations.h)
takes a `start` — it reverses the half-open range `[start, size)`.

### 🔍 Trace — `[19, 16, 42, 12, 28]`, `k = 2`

```text
start              [ 19 16 | 42 12 28 ]
reverse A          [ 16 19 | 42 12 28 ]
reverse B          [ 16 19 | 28 12 42 ]
reverse all        [ 42 12 28 | 19 16 ]   ✅
```

> Analogy: two words on a strip of paper, `HELLO WORLD`. Write each word backwards
> (`OLLEH DLROW`), then read the whole strip backwards: `WORLD HELLO`.

### ⏱️ Complexity

Each element is swapped about twice (once in its block, once overall) → `O(n)` time,
**`O(1)` space**.

---

## 2. Right Rotation

```text
right by k   =   left by (n − k)
```

Or reorder the reversals — reverse the **whole** array first, then each block:

```cpp
reverse(arr, size);        // [19 16 42 12 28] → [28 12 42 16 19]
reverse(arr, k);           //                  → [12 28 | 42 16 19]
reverse(arr, size, k);     //                  → [12 28 | 19 16 42]   ✅ right by 2
```

---

## 3. Bonus — The Juggling Algorithm (GCD Cycles)

Another `O(n)` / `O(1)` method that interviewers sometimes ask about by name.

> **Following `j → j + k (mod n)` from any start visits a cycle of positions.** There are
> exactly `gcd(n, k)` such cycles, each of length `n / gcd(n, k)`. Move every element along
> its cycle with one temp variable.

```cpp
int g = gcd(n, k);
for (int s = 0; s < g; s++) {               // one pass per cycle
    int temp = arr[s], j = s;
    while (true) {
        int next = (j + k) % n;             // who should move into slot j?
        if (next == s) break;               // cycle closed
        arr[j] = arr[next];
        j = next;
    }
    arr[j] = temp;
}
```

```text
n = 6, k = 2  →  gcd = 2  →  two cycles:   0 → 2 → 4 → 0     1 → 3 → 5 → 1
```

Every element moves **exactly once** (fewer writes than reversal), but it's harder to get
right from memory. Reversal is the safer interview answer.

---

## 4. All Approaches Compared

| Approach | Time | Space | Verdict |
|---|---|---|---|
| New array via `(i + k) % n` | `O(n)` | `O(n)` | Simplest to reason about |
| Rotate by one, `k` times | `O(n·k)` | `O(1)` | Too slow — starting point only |
| Temp array of `k` | `O(n)` | `O(k)` | Good; implemented as `left_rotate` |
| **Reversal** | `O(n)` | `O(1)` | ⭐ The standard best answer |
| Juggling | `O(n)` | `O(1)` | Fewest writes; trickiest to write |

### Library equivalents

```cpp
std::rotate(arr, arr + k, arr + n);     // C++: left rotate by k (arr + k becomes the front)
```

```java
Collections.rotate(list, -k);           // Java: POSITIVE distance rotates RIGHT, so negate
```

> ⚠️ Java's `Collections.rotate` direction is the opposite of what most people assume.

---

## 5. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| No `k % n` | `k > n` reads/writes out of bounds, or wastes cycles |
| `k % n` when `n == 0` | Division by zero — guard `size <= 1` first |
| Rotate-by-one `k` times "because it's simple" | `O(n·k)` — up to `O(n²)` |
| Reversing `[0, k]` inclusive instead of `[0, k)` | Off-by-one — element `k` lands in the wrong block |
| Reversing all **last** for a right rotation | That's a left rotation — for right, reverse all **first** |
| Mixing up left vs right | Left: first `k` go to the back. Right: last `k` come to the front. |

> ⚠️ In this repo's `left_rotate.cpp`, the third demo in `main()` is labelled "using reversal
> method" but calls `left_rotate` — so `left_rotate_using_reversal` is never exercised.

---

## 6. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **First step for any rotate-by-k?** | `k %= n` (after guarding `n <= 1`). |
| **Left rotation as blocks?** | `A B → B A`, where `A` is the first `k`. |
| **Index mapping?** | Left: `new[i] = old[(i + k) % n]`. |
| **Rotate by one?** | Save `arr[0]`, shift left, put it at the end. `O(n)`, `O(1)`. |
| **Temp-array approach?** | Save first `k`, slide the rest left, append the saved. `O(n)`, `O(k)`. |
| **Best rotate-by-k?** | Reversal: reverse `[0,k)`, reverse `[k,n)`, reverse all. `O(n)`, `O(1)`. |
| **Why does reversal work?** | `(Aʳ Bʳ)ʳ = B A` — the outer reverse swaps blocks and undoes the inner reverses. |
| **Right rotate by `k`?** | Left by `n − k`; or reverse all first, then each block. |
| **Juggling algorithm?** | Move elements along `gcd(n, k)` cycles of `j → j + k`. `O(n)`, `O(1)`. |
| **C++ / Java library?** | `std::rotate(a, a + k, a + n)` · `Collections.rotate(list, -k)` |

---

## 7. The Mental Model

```text
              rotate left by k   =   A B  →  B A
                          │
               guard n <= 1 · k = k % n
                          │
     ┌──────────────┬─────┴──────────┬──────────────────┐
     ↓              ↓                ↓                  ↓
 by one × k     temp array       reversal ⭐          juggling
 O(n·k)         O(n), O(k)    Aʳ · Bʳ · (all)ʳ     gcd(n,k) cycles
                              O(n), O(1)           O(n), O(1)
```
