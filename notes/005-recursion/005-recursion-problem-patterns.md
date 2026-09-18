# 005 — Recursion Problem Patterns

> **One-line takeaway:** Recursive problems are not infinitely varied — almost all of them fall into **seven shapes**. Naming the shape gives you the recurrence, the recurrence gives you the complexity, and the complexity tells you whether you're done or need memoisation.

This is the page that turns "I've seen this before" into "I know which template to write".

---

## 1. Why Patterns, Not Problems

Memorising Tower of Hanoi teaches you Tower of Hanoi. Recognising that it is
*"solve smaller, do one atomic action, solve smaller again"* also gives you the
N-disk variants, recursive tree printing, and half of divide & conquer.

```text
Pattern  →  Recurrence  →  Complexity  →  Is it good enough?
```

---

## 2. The Seven Shapes

| # | Pattern | Shape | Recurrence | Cost |
|---|---|---|---|---|
| 1 | **Linear reduction** | peel one, recurse | `T(n) = T(n−1) + O(1)` | `O(n)` |
| 2 | **Two-pointer shrink** | ends move inward | `T(n) = T(n−2) + O(1)` | `O(n)` |
| 3 | **Halving** | cut the input in half | `T(n) = T(n/2) + O(1)` | `O(log n)` |
| 4 | **Best-of-choices** | try every option, take max/min | `T(n) = k·T(n−c) + O(1)` | `O(kⁿ)` ⚠️ |
| 5 | **Solve–act–solve** | smaller, one action, smaller | `T(n) = 2T(n−1) + O(1)` | `O(2ⁿ)` |
| 6 | **Position remapping** | solve smaller, transform answer | `T(n) = T(n−1) + O(1)` | `O(n)` |
| 7 | **Include / exclude** | take it or skip it | `T(n) = 2T(n−1) + O(1)` | `O(2ⁿ)` |

> Patterns 4, 5 and 7 are exponential. That is not always a bug — Hanoi genuinely *has*
> `2ⁿ − 1` moves — but for **counting/optimising** problems (pattern 4) it means
> memoisation is waiting to be applied.

---

## 3. Pattern 1 — Linear Reduction

```cpp
T f(int n) {
    if (n == 0) return identity;
    return combine(piece(n), f(n - 1));
}
```

```text
Examples: factorial, sum 1..n, print 1..n, sum of digits, length of a list
Cost:     O(n) time, O(n) stack
```

**Signal:** the input is a single counter and one unit comes off each step.

---

## 4. Pattern 2 — Two-Pointer Shrink

```cpp
T f(const string &s, int i, int j) {
    if (i >= j) return identity;
    return combine(s[i], s[j], f(s, i + 1, j - 1));
}
```

```text
Examples: palindrome check, reverse a string/array, "is it symmetric"
Cost:     O(n) time, O(n/2) = O(n) stack
```

**Signal:** the problem is about **pairs at opposite ends**.
⚠️ Base case must be `i >= j`, never `i == j` ([note 003 §4](003-writing-base-cases.md)).

---

## 5. Pattern 3 — Halving (Divide & Conquer)

```cpp
T f(int n) {
    if (base) return identity;
    T half = f(n / 2);                    // ONE call, result REUSED
    return combine(half, half);
}
```

```text
Examples: binary search, fast power, merge sort, quick sort, tree height
Cost:     O(log n) with one call · O(n log n) with two calls + O(n) merge
```

**Signal:** the input can be **split by a constant factor**, not just decremented.
⚠️ Reusing the stored result is mandatory — calling `f(n/2)` twice throws away the entire
gain ([note 004 §7](004-easy-recursion-problems.md)).

---

## 6. Pattern 4 — Best-of-Choices ⭐

```cpp
int f(int n) {
    if (n == 0) return 0;                          // success
    if (n < 0)  return IMPOSSIBLE;                 // dead branch

    int best = max({ f(n - a), f(n - b), f(n - c) });
    return best >= 0 ? best + 1 : IMPOSSIBLE;
}
```

```text
Examples: rope cutting, coin change, minimum steps to reach N, staircase ways
Cost:     O(kⁿ) — exponential, k = number of choices
Fix:      memoise on n → O(n·k)
```

**Signal:** wording like *"maximum / minimum / number of ways"* plus **a fixed menu of
moves**. You try every move and combine the sub-answers with `max`, `min` or `+`.

> **This is the doorway to dynamic programming.** The recursion *is* the DP recurrence;
> adding a cache is the only difference.

⚠️ This pattern needs a **sentinel for impossible** that is distinguishable from a real
answer — see the rope-cutting card in
[Classic Recursion Problems](006-classic-recursion-problems.md).

---

## 7. Pattern 5 — Solve, Act, Solve ⭐

```cpp
void f(int n, Ctx a, Ctx b, Ctx c) {
    if (n == 0) return;
    f(n - 1, a, c, b);          // solve the smaller problem, moved aside
    action(n, a, c);            // one atomic step
    f(n - 1, b, a, c);          // solve the smaller problem again
}
```

```text
Examples: Tower of Hanoi, printing a binary tree in-order, some puzzle solvers
Cost:     T(n) = 2T(n−1) + O(1)  →  O(2ⁿ)
```

**Signal:** the task can't be done directly because something is **in the way** — so you
recursively move the obstruction, act, then recursively restore it.

> The exponential cost here is **inherent**, not wasteful: the answer itself has `2ⁿ − 1`
> steps, so no algorithm can be faster.

---

## 8. Pattern 6 — Position Remapping ⭐

```cpp
int f(int n) {
    if (n == 1) return 0;                 // known answer for the smallest case
    return transform(f(n - 1));           // map the smaller answer up to size n
}
```

```text
Examples: Josephus problem, some circular-array questions
Cost:     O(n) time, O(n) stack (O(1) iteratively)
```

**Signal:** after one step the problem becomes **the same problem at size n−1, but
renumbered**. You don't simulate — you solve the smaller instance and translate the answer
back into the original numbering.

> This is the subtlest of the seven, and the one most worth practising: it turns an
> `O(n·k)` simulation into an `O(n)` formula.

---

## 9. Pattern 7 — Include / Exclude

```cpp
void f(int i, State cur) {
    if (i == n) { record(cur); return; }
    f(i + 1, cur);                  // EXCLUDE item i
    cur.add(item[i]);
    f(i + 1, cur);                  // INCLUDE item i
    cur.remove(item[i]);            // undo — backtracking
}
```

```text
Examples: generate all subsets, permutations, N-Queens, sudoku, combination sum
Cost:     O(2ⁿ) for subsets, O(n!) for permutations
```

**Signal:** *"generate all …"* or *"find every combination that …"*.
⚠️ The `undo` step is what makes it backtracking rather than plain recursion.

> Full treatment — the decision tree, the template and the four knobs — in
> [The Subsets Pattern](007-subsets-pattern.md).

📄 [`subsets.cpp`](../../programs/cpp/techniques/recursion/subsets.cpp) ·
📄 [`GenerateSubset.java`](../../programs/java/techniques/recursion/intermediate/GenerateSubset.java) ·
📄 [`Permutations.java`](../../programs/java/techniques/recursion/intermediate/Permutations.java)

---

## 10. Which Pattern Is This? — Decision Guide

```text
Read the problem, then ask in this order:

1. Am I generating ALL possibilities?          → Pattern 7  (include/exclude)
2. Is it max / min / count with a fixed menu?  → Pattern 4  (best-of-choices) → then memoise
3. Does something block the direct action?     → Pattern 5  (solve–act–solve)
4. Does one step renumber the SAME problem?    → Pattern 6  (position remapping)
5. Can I split the input by a factor?          → Pattern 3  (halving)
6. Are the ends of the input paired?           → Pattern 2  (two-pointer)
7. Otherwise, peel one unit off                → Pattern 1  (linear)
```

### Cost signatures to recognise instantly

```text
one call,  n−1      → O(n)          patterns 1, 2, 6
one call,  n/2      → O(log n)      pattern 3
two calls, n/2      → O(n log n)    merge sort
two calls, n−1      → O(2ⁿ)         patterns 5, 7
k calls,   n−c      → O(kⁿ)         pattern 4   ⚠️ memoise
n calls,   n−1      → O(n!)         permutations
```

Full derivations in
[Recurrence Relations](../002-analysis-of-algorithms/011-recurrence-relations.md).

---

## 11. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **How do you classify a recursion problem?** | By how many calls it makes and how the input shrinks — that gives the recurrence, which gives the cost. |
| **"Maximum/minimum/number of ways" with fixed moves?** | Best-of-choices: try every move, combine with max/min/+, then memoise. |
| **Which pattern is Tower of Hanoi?** | Solve–act–solve: move the obstruction, do one step, restore it. `T(n)=2T(n−1)+1`. |
| **Which pattern is Josephus?** | Position remapping: solve size `n−1`, then translate the answer back. |
| **Which pattern is rope cutting?** | Best-of-choices with a sentinel for impossible paths. |
| **Which pattern is "generate all subsets"?** | Include/exclude backtracking — `O(2ⁿ)`. |
| **When is exponential acceptable?** | When the *output* is exponential (Hanoi's `2ⁿ−1` moves). When it's recomputation, memoise. |
| **What links recursion to DP?** | Best-of-choices recursion *is* the DP recurrence; DP just adds a cache. |
| **Signature of `O(log n)` recursion?** | One call on `n/2`, with the result stored and reused. |
| **Signature of `O(2ⁿ)`?** | Two recursive calls per level on `n−1`. |

---

## 12. The Mental Model

```text
                    A recursive problem
                            │
                            ↓
              How many calls, and how does n shrink?
                            │
      ┌─────────┬───────────┼───────────┬─────────┐
      ↓         ↓           ↓           ↓         ↓
   1 call    1 call     k calls     2 calls    n calls
    n−1        n/2         n−c         n−1        n−1
      │         │           │           │         │
    O(n)    O(log n)      O(kⁿ)       O(2ⁿ)     O(n!)
      │         │           │           │         │
   linear /  divide &    best-of-   solve-act-  permute
   2-pointer conquer     choices      solve /
   / remap               ⚠️ memoise  include-
                                      exclude
```
