# 006 — Circular Thinking: Rotation, Modular Indexing & the Reversal Trick

> **One-line takeaway:** Bend the array into a **ring** and many problems get simpler: index `i + k` wraps with `% n`, rotating is just "start reading from somewhere else", and a rotation is a **swap of two blocks** `A B → B A`, which three **reversals** perform in place. The same ring view later solves circular subarray problems.

---

## 1. The Problem This Concept Solves

Some data has no real beginning or end: the days of the week, seats around a table, a
playlist on repeat, a clock face. Storing it in an array forces you to *pick* a start —
and then problems ask you to move that start, or to let things "wrap around".

```text
array view                      ring view

[ 19 16 42 12 28 ]                   19
  0  1  2  3  4                  28       16
                                   12   42
                               "the last is followed by the first"
```

Without the ring view you write special cases for "falling off the end". With it, one
formula handles everything.

---

## 2. The Core Idea — `% n` Is the Ring

> 🔑 **On a ring of size `n`, the position `k` steps after `i` is `(i + k) % n`.**

```text
n = 5,  i = 3,  k = 4      (3 + 4) % 5 = 2        3 → 4 → 0 → 1 → 2
```

Moving **backwards** needs care, because `%` of a negative number is negative in C++ and
Java:

```text
(i − k) % n           may be negative     ❌    (−1 % 5 == −1)
(i − k + n) % n       safe for 0 ≤ k ≤ n  ✅
((i − k) % n + n) % n safe for any k      ✅
```

### Normalise first

Walking `n` steps around a ring of `n` lands you where you started. So a rotation by `k` is
the same as a rotation by `k % n`:

```text
rotate a 5-element array by 1,000,002   ≡   rotate by 2
```

> ⚠️ Always `k %= n` before rotating — after guarding `n == 0`, since `% 0` is undefined.

---

## 3. Rotation — Same Ring, New Starting Point

**Left rotation by `k`:** everything moves `k` places left; the first `k` wrap to the back.

```text
[ 19  16 | 42  12  28 ]        →        [ 42  12  28 | 19  16 ]
    A          B                              B          A
```

Two ways to *see* the same thing:

```text
ring view       the ring didn't change — we just start reading at index k
                new[i] = old[(i + k) % n]

block view      the array is two blocks, A (first k) and B (the rest); swap them
                A B  →  B A
```

The ring view gives the easy solution. The block view gives the clever one.

---

## 4. Four Ways to Rotate — and What Each Teaches

### ① Copy through the formula — `O(n)` time, `O(n)` space

```cpp
for (int i = 0; i < n; i++) out[i] = arr[(i + k) % n];
```

Simplest to get right. Uses a second array.

### ② Rotate by one, `k` times — `O(n·k)` time, `O(1)` space

Save `arr[0]`, shift everyone left, put it at the end. Repeat `k` times. Correct, but with
`k ≈ n` it's `O(n²)`. It's the "obvious" answer you mention only to improve on.

### ③ Save one block — `O(n)` time, `O(k)` space

Copy block `A` aside, slide block `B` to the front, put `A` after it. Fast, but still needs
`k` extra slots.

### ④ The reversal trick — `O(n)` time, `O(1)` space ⭐

> **Reverse `A`. Reverse `B`. Reverse the whole thing. Done.**

```text
start           [ 19 16 | 42 12 28 ]
reverse A       [ 16 19 | 42 12 28 ]
reverse B       [ 16 19 | 28 12 42 ]
reverse all     [ 42 12 28 | 19 16 ]   ✅
```

```cpp
k %= n;
reverse(arr, arr + k);          // A
reverse(arr + k, arr + n);      // B
reverse(arr, arr + n);          // all
```

---

## 5. Why the Reversal Trick Works

Reversing a sequence made of two pieces does **two** things at once: it swaps the order of
the pieces **and** reverses each piece.

```text
(X Y)ʳ  =  Yʳ Xʳ
```

So if you **pre-reverse** each piece, the final reverse undoes those reversals while
swapping the pieces:

```text
(Aʳ Bʳ)ʳ  =  (Bʳ)ʳ (Aʳ)ʳ  =  B A          reversing twice restores a block
```

**Paper-strip analogy:** write `HELLO WORLD` on a strip. Reverse each word in place →
`OLLEH DLROW`. Now read the whole strip backwards → `WORLD HELLO`. The words swapped places,
and each reads correctly again.

### 🧠 Pause and predict

How do you **right**-rotate by `k` with reversals?

<details>
<summary>Answer</summary>

Either left-rotate by `n − k`, or swap the order of the steps: **reverse all first**, then
reverse the first `k` and the last `n − k`.

```text
[19 16 42 12 28], right by 2
reverse all      [28 12 42 16 19]
reverse [0,2)    [12 28 | 42 16 19]
reverse [2,5)    [12 28 | 19 16 42]   ✅  (the last two came to the front)
```

</details>

---

## 6. Bonus — Rotation as Cycles (the Juggling Algorithm)

The ring view hides another structure. Follow where each element must go:
`j → j + k (mod n)`. Starting from any index, you trace a **cycle** that returns to the
start.

```text
n = 6, k = 2:      0 → 2 → 4 → 0          1 → 3 → 5 → 1          two cycles
n = 5, k = 2:      0 → 2 → 4 → 1 → 3 → 0                        one cycle
```

> There are exactly **`gcd(n, k)`** cycles, each of length `n / gcd(n, k)`.

Move every element one step along its cycle using a single temporary variable, and the
array is rotated — each element written **exactly once**. It's elegant and uses the fewest
writes, but it's easy to fumble from memory; the reversal trick is the safer interview
answer.

---

## 7. Beyond Rotation — Other Uses of the Ring

### Walking a circular array twice

"Next greater element, but the array is circular" — the next greater for the last element
might be at the front. Loop `i` from `0` to `2n − 1` and use `arr[i % n]`: you get every
element's full circle without copying.

### Doubling the array

Conceptually appending the array to itself turns every wrap-around range into an ordinary
contiguous range:

```text
ring [a b c d]   →   line [a b c d a b c d]
the wrapping range  d → a → b   is just the plain range  [3 … 5]  of the doubled line
```

### The complement trick

On a ring, a range that **wraps** is exactly what's left after removing a range that
**doesn't**. That's how the maximum circular subarray sum is solved — total minus the
minimum ordinary subarray. See [Kadane §7](010-kadanes-algorithm.md).

---

## 8. Recognising Circular Problems

```text
"rotate", "shift by k", "cyclic"               → rotation (reversal trick)
"circular array", "wraps around", "ring"       → % n indexing, walk 2n, or complement
"arranged in a circle" (people, gas stations)  → ring view; often a single pass with % n
"is B a rotation of A?"                        → B is a contiguous part of A + A
```

---

## 9. Common Mistakes

| ✗ Mistake | Why it's wrong |
|---|---|
| Not reducing `k %= n` | Out-of-bounds indexing, or wasted full cycles |
| `k % n` with `n == 0` | Division by zero |
| `(i − k) % n` | Negative result in C++/Java |
| Rotating by one `k` times | `O(n·k)` |
| Reversing `[0, k]` inclusive | Off by one — element `k` ends up in the wrong block |
| Mixing up left and right | Left: the first `k` go to the back. Right: the last `k` come to the front |
| Java `Collections.rotate(list, k)` for a left rotation | Positive distance rotates **right**; use `−k` |

---

## 10. Practice in This Repo

| Problem | What it exercises | 📄 |
|---|---|---|
| Left rotate by one | save, shift, place | [`left_rotate.cpp`](../../programs/cpp/data_structures/arrays/basic/left_rotate.cpp) |
| Left rotate by `k` (temp block) | block view, `k %= n` | [`left_rotate.cpp`](../../programs/cpp/data_structures/arrays/basic/left_rotate.cpp) |
| Left rotate by `k` (reversal) | the reversal identity | [`left_rotate.cpp`](../../programs/cpp/data_structures/arrays/basic/left_rotate.cpp) + [`basic_operations.h`](../../programs/cpp/include/arrays/basic_operations.h) |
| Maximum circular subarray sum | complement trick on a ring | [`sub_array.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sub_array.cpp) |

> ⚠️ In `left_rotate.cpp`, the demo labelled "using reversal method" calls `left_rotate`, so
> the reversal version is never actually run by `main()`.

Library equivalents: `std::rotate(a, a + k, a + n)` (C++, left by `k`) ·
`Collections.rotate(list, −k)` (Java, left by `k`).

---

## 11. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **`k` steps forward on a ring of `n`?** | `(i + k) % n` |
| **`k` steps backward, safely?** | `(i − k + n) % n` (or `((i − k) % n + n) % n`) |
| **First step of any rotation?** | Guard `n == 0`, then `k %= n`. |
| **Left rotation as blocks?** | `A B → B A`, where `A` is the first `k`. |
| **Reversal trick?** | Reverse `[0,k)`, reverse `[k,n)`, reverse all — `O(n)`, `O(1)`. |
| **Why does it work?** | `(Aʳ Bʳ)ʳ = B A` — reversing twice restores each block. |
| **Right rotation?** | Left by `n − k`, or reverse all first, then each block. |
| **Juggling algorithm?** | Move elements along `gcd(n, k)` cycles; each written once. |
| **Circular "next" problems?** | Walk `i` from `0` to `2n − 1` with `arr[i % n]`. |
| **Is B a rotation of A?** | Same length and B appears inside `A + A`. |

---

## 12. The Mental Model

```text
                 array  →  bend it into a RING
                                │
                 position after k steps = (i + k) % n
                                │
        ┌───────────────────────┼────────────────────────┐
        ↓                       ↓                        ↓
     ROTATION              WRAP-AROUND              COMPLEMENT
  A B → B A              walk 2n with i % n,      wrapping range =
  reverse A, B, all      or double the array      total − inner range
  O(n) · O(1)                                     (circular Kadane)
        │
  k %= n first · gcd(n, k) cycles if you want fewest writes
```
