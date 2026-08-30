# 011 — Recurrence Relations

> **One-line takeaway:** A recurrence expresses an algorithm's cost **in terms of itself on smaller inputs**. Writing it correctly is three questions: *how many recursive calls, on what size, plus how much extra work?*

---

## 1. What Is a Recurrence Relation?

> **A recurrence relation is an equation that defines `T(n)` — the cost on input size `n` —
> using `T` of smaller inputs, together with a base case.**

```text
T(n) = 2T(n/2) + n          ← recursive case
T(1) = 1                    ← base case
```

A loop's cost you can just count. A recursive function's cost you cannot — the function's
cost depends on itself. The recurrence is how you write that down before solving it.

```text
Recursive code  →  Recurrence  →  Solve  →  Θ(...)
                     (note 011)   (012/013/014)
```

---

## 2. Anatomy

```text
        T(n)  =  a · T(n/b)  +  f(n)
                 │     │         │
                 │     │         └── work done OUTSIDE the recursive calls
                 │     │             (dividing + combining)
                 │     └── size of each subproblem
                 └── number of recursive calls
```

| Symbol | Meaning | How to find it in code |
|---|---|---|
| `a` | Number of recursive calls made | Count the recursive calls that actually execute |
| `n/b` or `n−c` | Size passed to each call | Look at the argument |
| `f(n)` | Non-recursive work in one call | Everything else: loops, partitioning, merging |
| `T(base)` | Cost of the stopping case | The `if` that returns without recursing |

---

## 3. ⚠️ The Base Case Is Not Optional

```text
T(n) = 2T(n/2) + n           ← incomplete, unsolvable
T(n) = 2T(n/2) + n, T(1) = 1 ← complete
```

The base case is what terminates the expansion and fixes the constant. In asymptotic work
we usually write `T(1) = Θ(1)` and stop worrying — but you must know it exists, because it
decides **how deep** the recursion goes.

> For asymptotics the base-case *value* rarely matters; the base-case *size* always does.

---

## 4. How to Write a Recurrence From Code

### Recipe

```text
1. Define T(n) = cost on input of size n.
2. Count the recursive calls → a
3. Note the argument passed → n/b (divide) or n−c (decrease)
4. Add up all non-recursive work in that one call → f(n)
   (loops, comparisons, merges, partitions — NOT the work inside the calls)
5. Write the base case.
```

### Example A — Binary Search

```java
int bs(int[] a, int lo, int hi, int x) {
    if (lo > hi) return -1;              // base:  Θ(1)
    int mid = (lo + hi) / 2;             // Θ(1)
    if (a[mid] == x) return mid;         // Θ(1)
    if (a[mid] < x) return bs(a, mid+1, hi, x);   // ONE call
    else            return bs(a, lo, mid-1, x);   // ONE call
}
```

```text
a = 1 (only one branch runs), size = n/2, f(n) = Θ(1)

T(n) = T(n/2) + Θ(1),   T(1) = Θ(1)      ⟹  Θ(log n)
```

> ⚠️ Two recursive calls appear in the code but only **one executes** → `a = 1`, not 2.
> Count calls **on the executed path**, not calls that are typed.

### Example B — Merge Sort

```java
void mergeSort(int[] a, int l, int r) {
    if (l >= r) return;                   // Θ(1)
    int m = (l + r) / 2;
    mergeSort(a, l, m);                   // T(n/2)
    mergeSort(a, m + 1, r);               // T(n/2)
    merge(a, l, m, r);                    // Θ(n)
}
```

```text
T(n) = 2T(n/2) + Θ(n),  T(1) = Θ(1)      ⟹  Θ(n log n)
```

### Example C — Tower of Hanoi

```java
void hanoi(int n, char from, char to, char aux) {
    if (n == 0) return;
    hanoi(n - 1, from, aux, to);          // T(n−1)
    System.out.println(from + "->" + to); // Θ(1)
    hanoi(n - 1, aux, to, from);          // T(n−1)
}
```

```text
T(n) = 2T(n−1) + Θ(1),  T(0) = Θ(1)      ⟹  Θ(2ⁿ)
```

### Example D — Naive Fibonacci

```java
int fib(int n) {
    if (n <= 1) return n;
    return fib(n - 1) + fib(n - 2);
}
```

```text
T(n) = T(n−1) + T(n−2) + Θ(1)            ⟹  Θ(φⁿ) ≈ Θ(1.618ⁿ), bounded by O(2ⁿ)
```

---

## 5. Two Families of Recurrences

```text
DIVIDE & CONQUER          T(n) = a·T(n/b) + f(n)
  size divided by a factor       merge sort, binary search, Strassen
  depth = log_b n

DECREASE & CONQUER        T(n) = a·T(n−c) + f(n)
  size reduced by a constant     factorial, Hanoi, linear recursion
  depth = n/c
```

The depth difference is why one family gives logs and the other gives linear/exponential
answers.

```text
n → n/2 → n/4 → ... → 1     depth = log₂ n      (divide)
n → n−1 → n−2 → ... → 1     depth = n           (decrease)
```

---

## 6. Standard Recurrence Catalogue

Recognise these on sight — most interview recurrences are on this list.

| Recurrence | Solution | Classic example |
|---|---|---|
| `T(n) = T(n−1) + O(1)` | `Θ(n)` | Linear recursion, factorial |
| `T(n) = T(n−1) + O(n)` | `Θ(n²)` | Quick sort worst case, selection sort |
| `T(n) = T(n−1) + O(log n)` | `Θ(n log n)` | — |
| `T(n) = 2T(n−1) + O(1)` | `Θ(2ⁿ)` | Tower of Hanoi, subsets |
| `T(n) = T(n−1) + T(n−2) + O(1)` | `Θ(φⁿ)` | Naive Fibonacci |
| `T(n) = n·T(n−1) + O(1)` | `Θ(n!)` | Permutations |
| `T(n) = T(n/2) + O(1)` | `Θ(log n)` | Binary search |
| `T(n) = T(n/2) + O(n)` | `Θ(n)` | Quickselect (average) |
| `T(n) = 2T(n/2) + O(1)` | `Θ(n)` | Tree traversal, tree height |
| `T(n) = 2T(n/2) + O(n)` | `Θ(n log n)` | Merge sort, quick sort best case |
| `T(n) = 2T(n/2) + O(n²)` | `Θ(n²)` | — |
| `T(n) = 3T(n/2) + O(n)` | `Θ(n^log₂3)` = `Θ(n^1.58)` | Karatsuba multiplication |
| `T(n) = 7T(n/2) + O(n²)` | `Θ(n^log₂7)` = `Θ(n^2.81)` | Strassen matrix multiply |
| `T(n) = T(n/3) + T(2n/3) + O(n)` | `Θ(n log n)` | Unbalanced split |
| `T(n) = T(√n) + O(1)` | `Θ(log log n)` | — |

---

## 7. Recurrences for Space, Too

Time is not the only thing recursion costs. **Every pending call holds a stack frame.**

```text
Auxiliary space = (maximum recursion DEPTH) × (frame size) + explicit allocations
```

```text
Merge sort     time Θ(n log n)   space Θ(n)      ← the merge buffer dominates the Θ(log n) stack
Quick sort     time Θ(n log n)   space Θ(log n)  ← stack only (in-place), Θ(n) worst case
Binary search  time Θ(log n)     space Θ(log n) recursive, Θ(1) iterative
Hanoi          time Θ(2ⁿ)        space Θ(n)      ← depth n, though 2ⁿ calls are made
```

> ⚠️ **Number of calls ≠ stack depth.** Hanoi makes `2ⁿ` calls but only `n` are ever alive at
> once. Space follows the **depth** of the tree; time follows its **total node count**.

---

## 8. Common Mistakes

| ✗ Mistake | ✓ Correct |
|---|---|
| Counting typed calls instead of executed calls | Binary search is `T(n/2) + O(1)`, not `2T(n/2) + O(1)` |
| Including the recursive work inside `f(n)` | `f(n)` is **only** the non-recursive work of one call |
| Forgetting the base case | It sets the recursion depth |
| Assuming an even split | Quick sort's worst case is `T(n−1) + O(n)`, not `2T(n/2) + O(n)` |
| Writing `T(n) = 2T(n/2)` and ignoring the merge | The `+ f(n)` term is usually what decides the answer |
| Confusing call count with stack depth | Time ← total nodes; space ← tree depth |

---

## 9. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What is a recurrence relation?** | An equation defining `T(n)` in terms of `T` on smaller inputs, plus a base case. |
| **What do `a`, `b`, `f(n)` mean?** | `a` = number of recursive calls, `n/b` = subproblem size, `f(n)` = non-recursive work per call. |
| **Binary search recurrence?** | `T(n) = T(n/2) + O(1)` → `Θ(log n)` — only one branch executes. |
| **Merge sort recurrence?** | `T(n) = 2T(n/2) + O(n)` → `Θ(n log n)`. |
| **Tower of Hanoi recurrence?** | `T(n) = 2T(n−1) + O(1)` → `Θ(2ⁿ)`, with `2ⁿ − 1` moves. |
| **Divide vs decrease & conquer?** | Divide splits by a factor (`n/b`, depth `log n`); decrease reduces by a constant (`n−c`, depth `n`). |
| **Why does the base case matter?** | It terminates the expansion and determines the recursion depth. |
| **Space complexity of recursion?** | Max recursion depth × frame size — the depth of the tree, not its node count. |
| **Quick sort's space?** | `Θ(log n)` average stack depth, `Θ(n)` worst case. |

---

## 10. The Mental Model

```text
Recursive function
        │
        ↓
Ask three questions:
   How many calls?          → a
   On what size?            → n/b  or  n−c
   Extra work per call?     → f(n)
        │
        ↓
   T(n) = a·T(n/b) + f(n)
        │
        ↓
Solve it (iteration / recursion tree / master theorem)
```
