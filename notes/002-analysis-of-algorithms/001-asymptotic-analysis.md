# 001 — Analysis of Algorithms & Asymptotic Analysis

> **One-line takeaway:** We don't measure an algorithm in *seconds* — we measure how its work **grows with the input size**. That machine-independent measure is asymptotic analysis.

---

## 1. What Is Analysis of Algorithms?

> **Analysis of an algorithm means determining the amount of resources — time and memory — it needs, expressed as a function of the input size.**

Two algorithms can solve the same problem. Analysis is how we decide which one to use
**before** wasting time implementing both.

---

## 2. Why Not Just Measure the Running Time?

The obvious idea — run it and time it — fails badly:

| Problem with measuring time | Why it breaks comparison |
|---|---|
| **Machine dependent** | Fast CPU makes a bad algorithm look good |
| **Language / compiler dependent** | Same algorithm, different timings in C++ vs Java |
| **Input dependent** | Fast on one input, slow on another |
| **Needs implementation** | You must code both before you can compare |
| **Doesn't predict scale** | Fast at `n = 100` says nothing about `n = 10⁶` |

```text
Experimental analysis  → run it, measure it   (machine dependent)
Theoretical analysis   → reason about it      (machine independent) ✓
```

> We want a way to compare **ideas**, not **machines**.

---

## 3. What Does "Asymptotic" Mean?

**Asymptotic** = the behaviour of a function as its input approaches infinity.

```text
n → ∞
```

We deliberately ignore small inputs, because at small `n` *everything* is fast. The
difference only appears when the input grows.

```text
f(n) = 3n + 10
```

As `n` grows huge, the `3n` term dominates and the `10` becomes irrelevant:

```text
3n + 10  →  O(n)
```

> **Asymptotic analysis studies the growth rate of an algorithm's resource usage, not its exact cost.**

---

## 4. The RAM / Unit-Cost Model

To count "work" without a real machine, we assume a simplified computer:

> **RAM (Random Access Machine) model** — each basic operation costs **1 unit of time**.

```text
Assignment            → O(1)
Arithmetic (+ - * /)  → O(1)
Comparison            → O(1)
Array indexing arr[i] → O(1)
Variable read/write   → O(1)
Increment/decrement   → O(1)
```

So this is a constant amount of work:

```java
int x = a + b;   // O(1)
```

This model is what lets us say "O(n)" and have it mean the same thing on every machine.

---

## 5. First: What Is `n`?

**Never** start analysing before you know what the input size means.

```text
Array        → n = number of elements
String       → n = number of characters
Linked List  → n = number of nodes
Matrix       → n × m = rows × columns
Graph        → V = vertices, E = edges
A number x   → n = number of digits/bits ≈ log₂ x   ⚠️ not x itself
```

The last one catches people out: a loop `for (i = 1; i <= x; i++)` is `O(x)`, which is
**exponential** in the number of bits of `x`.

---

## 6. Building the Cost Function `T(n)`

```java
int sum = 0;                      // 1 unit
for (int i = 0; i < n; i++) {     // init 1, test n+1, incr n
    sum = sum + arr[i];           // n times, constant each
}
```

Counting:

```text
T(n) = c₁ + c₂(n + 1) + c₃n + c₄n
     = an + b
```

Simplify → drop constants and lower-order terms:

```text
T(n) = O(n)
```

> In practice you don't count every instruction. You ask: **how many times does the
> repeated work run, as a function of n?**

---

## 7. Why Drop Constants and Lower-Order Terms?

Take `T(n) = n² + 100n + 5000`:

| n | n² | 100n | 5000 | dominant |
|---:|---:|---:|---:|---|
| 10 | 100 | 1,000 | 5,000 | constant |
| 100 | 10,000 | 10,000 | 5,000 | tie |
| 1,000 | 1,000,000 | 100,000 | 5,000 | **n²** |
| 1,000,000 | 10¹² | 10⁸ | 5,000 | **n²** overwhelmingly |

The fastest-growing term eventually swallows everything else.

```text
Simplification rules
1. Drop constant factors      → O(5n)  = O(n)
2. Keep the dominant term     → n²+n   = O(n²)
3. Drop lower-order terms     → n²+100n+5000 = O(n²)
```

---

## 8. Time Complexity vs Space Complexity

```text
              Asymptotic Analysis
                      │
        ┌─────────────┴─────────────┐
        ↓                           ↓
 Time Complexity            Space Complexity
 how work grows             how memory grows
```

**Space** splits into two parts:

```text
Total Space = Input Space + Auxiliary Space
                                  ↑
                        extra memory the algorithm
                        itself allocates
```

When we say "this runs in O(1) space", we almost always mean **auxiliary space**.

```java
// O(1) auxiliary space — a few variables regardless of n
int total = 0;
for (int x : arr) total += x;
```

⚠️ Recursion is not free: `n` nested calls hold `n` stack frames → `O(n)` space.

---

## 9. Limitations of Asymptotic Analysis

Worth knowing — interviewers like this question.

- **Constants are ignored but real.** `2n` and `1000n` are both `O(n)`; one is 500× slower.
- **Only meaningful for large `n`.** For tiny inputs an `O(n²)` algorithm may beat
  `O(n log n)` (this is why real sort libraries switch to insertion sort for small arrays).
- **Ignores hardware reality** — cache locality, memory access patterns, branch prediction.
- **Average case is hard** — it needs an assumption about the input distribution.

> Asymptotic analysis answers **"does it scale?"**, not **"how many milliseconds?"**

---

## 10. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What is analysis of algorithms?** | Determining the time and space an algorithm needs as a function of its input size. |
| **Why not measure execution time?** | It depends on machine, language, compiler and input, and requires implementing every candidate first. |
| **What is asymptotic analysis?** | Studying how resource usage grows as `n → ∞`, ignoring constants and lower-order terms. |
| **What is the RAM model?** | A machine model where every basic operation costs one unit of time — it makes counting operations machine-independent. |
| **Why ignore constants?** | They don't change the growth rate; `5n` and `100n` both double when `n` doubles. |
| **Why keep only the dominant term?** | For large `n` it overwhelms every lower-order term. |
| **Auxiliary vs total space?** | Auxiliary is the extra memory the algorithm allocates; total also includes the input. |
| **Limitations of Big-O?** | Hides constants and hardware effects, and is only meaningful for large inputs. |

---

## 11. The Mental Model

```text
Code
 ↓  count repeated work
T(n) = an + b
 ↓  drop constants & lower terms
Order of growth
 ↓  express formally
O / Θ / Ω
 ↓  compare with constraints
Is it fast enough?
```
