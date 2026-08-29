# 009 — Math Refresher for Complexity Analysis

> **One-line takeaway:** You need surprisingly little maths for DSA — logs, exponents, a handful of series formulas, and the limit test. This page is the whole toolkit on one screen.

---

## 1. Logarithms

### Definition

```text
log_b x = y     ⟺     bʸ = x
```

In DSA, `log n` means `log₂ n` unless stated otherwise (because we keep halving things).

### Properties (memorise these)

```text
log(a · b)   = log a + log b
log(a / b)   = log a − log b
log(aᵇ)      = b · log a
log_b b      = 1
log_b 1      = 0
b^(log_b x)  = x
log_b x      = log_c x / log_c b        ← change of base
```

### Why the base is irrelevant in Big-O

```text
log₂ n = log₁₀ n / log₁₀ 2 = 3.32 × log₁₀ n
```

`3.32` is a constant → dropped. Hence:

```text
O(log₂ n) = O(log₁₀ n) = O(ln n) = O(log n)
```

⚠️ **Only for logs.** For exponents the base matters enormously: `2ⁿ ≠ Θ(3ⁿ)`.

### Values worth knowing instantly

| n | log₂ n |
|---:|---:|
| 2 | 1 |
| 16 | 4 |
| 1,024 (2¹⁰) | 10 |
| 10⁶ | ≈ 20 |
| 10⁹ | ≈ 30 |

> **Rule of thumb:** `2¹⁰ ≈ 10³`. So `log₂(10⁶) ≈ 20` — binary search over a million items
> takes ~20 steps.

---

## 2. Exponents

```text
aᵐ · aⁿ  = aᵐ⁺ⁿ
aᵐ / aⁿ  = aᵐ⁻ⁿ
(aᵐ)ⁿ    = aᵐⁿ
a⁰       = 1
a⁻ⁿ      = 1 / aⁿ
a^(1/2)  = √a
```

Powers of two (the table you'll use most):

```text
2⁸  = 256          2¹⁶ = 65,536          2³² ≈ 4.3 × 10⁹
2¹⁰ = 1,024        2²⁰ ≈ 10⁶            2⁶⁴ ≈ 1.8 × 10¹⁹
```

Useful limits: `int` overflows past `2³¹−1 ≈ 2.1 × 10⁹`; `long` past `2⁶³−1 ≈ 9.2 × 10¹⁸`.

---

## 3. Summation Formulas

These convert a loop into a closed form.

| Series | Formula | Order |
|---|---|---|
| Constant | `c + c + … (n terms) = cn` | `Θ(n)` |
| **Arithmetic** | `1 + 2 + … + n = n(n+1)/2` | `Θ(n²)` |
| Odd numbers | `1 + 3 + 5 + … + (2n−1) = n²` | `Θ(n²)` |
| **Squares** | `1² + 2² + … + n² = n(n+1)(2n+1)/6` | `Θ(n³)` |
| Cubes | `1³ + 2³ + … + n³ = [n(n+1)/2]²` | `Θ(n⁴)` |
| **Geometric (r > 1)** | `1 + r + r² + … + rᵏ = (rᵏ⁺¹ − 1)/(r − 1)` | `Θ(rᵏ)` |
| Powers of 2 | `1 + 2 + 4 + … + 2ᵏ = 2ᵏ⁺¹ − 1` | `Θ(2ᵏ)` |
| Geometric (r < 1) | `1 + ½ + ¼ + … < 2` | `Θ(1)` |
| **Harmonic** | `Hₙ = 1 + ½ + ⅓ + … + 1/n ≈ ln n` | `Θ(log n)` |

### The two most useful intuitions

```text
Increasing geometric → dominated by the LAST term
   1 + 2 + 4 + ... + n  ≈  2n  =  Θ(n)
   (why dynamic array doubling costs O(n) total, not O(n²))

Decreasing geometric → dominated by the FIRST term
   n + n/2 + n/4 + ...  ≈  2n  =  Θ(n)
   (why building a heap is O(n), not O(n log n))
```

---

## 4. Factorials & Stirling

```text
n! = n × (n−1) × … × 2 × 1
0! = 1
```

```text
3! = 6        5! = 120        10! ≈ 3.6 × 10⁶        20! ≈ 2.4 × 10¹⁸
```

Stirling's approximation:

```text
n! ≈ √(2πn) · (n/e)ⁿ
```

The consequence you actually use:

```text
log(n!) = Θ(n log n)
```

> This one line is the proof that comparison-based sorting needs `Ω(n log n)` comparisons.

Growth: `n! > 2ⁿ` for `n ≥ 4`, and `nⁿ > n!`.

---

## 5. Floors and Ceilings

```text
⌊x⌋ = largest integer ≤ x        ⌊3.7⌋ = 3
⌈x⌉ = smallest integer ≥ x       ⌈3.2⌉ = 4
x − 1 < ⌊x⌋ ≤ x ≤ ⌈x⌉ < x + 1
```

They appear everywhere (binary search midpoints, tree heights) and are **always safe to
drop in asymptotic analysis**:

```text
Θ(⌈n/2⌉) = Θ(n)
Height of a complete binary tree with n nodes = ⌊log₂ n⌋ = Θ(log n)
```

---

## 6. Combinatorics (Quick Reference)

```text
Permutations of n items            = n!
Permutations of r from n           = n! / (n−r)!
Combinations "n choose r"          = C(n,r) = n! / (r!(n−r)!)
Number of subsets of an n-set      = 2ⁿ
Number of binary strings of length n = 2ⁿ
```

Why the classic complexities look how they do:

```text
All subsets      → 2ⁿ   → O(2ⁿ)
All permutations → n!   → O(n!)
All pairs        → C(n,2) = n(n−1)/2 → O(n²)
```

---

## 7. Binary Tree Maths

```text
Levels are 0-indexed from the root.

Nodes at level i                  = 2ⁱ
Max nodes in a tree of height h   = 2ʰ⁺¹ − 1
Height of a balanced tree, n nodes = ⌊log₂ n⌋ = Θ(log n)
Min height with n nodes            = ⌊log₂ n⌋
Max height with n nodes (skewed)   = n − 1
Leaves in a full binary tree       = internal nodes + 1
```

> This is why balanced trees give `O(log n)` and a degenerate (skewed) tree collapses to
> `O(n)` — the same structure, a different height.

---

## 8. The Limit (Ratio) Test

The fastest way to compare two growth functions:

```text
        f(n)
  lim  ──────  =  ⎧ 0    →  f grows slower   ⟹ f = O(g), f = o(g)
 n→∞    g(n)      ⎨ c>0  →  same order       ⟹ f = Θ(g)
                  ⎩ ∞    →  f grows faster   ⟹ f = Ω(g), f = ω(g)
```

Examples:

```text
lim (log n)/n      = 0   →  log n is slower than n
lim (n²)/(n log n) = ∞   →  n² is faster than n log n
lim (3n²+5n)/n²    = 3   →  Θ(n²)
```

**The master ordering to memorise:**

```text
1 < log log n < log n < (log n)² < √n < n < n log n < n² < n³ < 2ⁿ < 3ⁿ < n! < nⁿ
```

Two rules that settle most questions:

```text
Any polylog beats any polynomial:     (log n)^k  =  o(n^ε)   for any ε > 0
Any polynomial beats any exponential: n^k        =  o(2ⁿ)
```

---

## 9. Recurrences (Just Enough)

Recursive algorithms produce recurrences. The three you should recognise on sight:

| Recurrence | Solution | Example |
|---|---|---|
| `T(n) = T(n−1) + O(1)` | `Θ(n)` | Linear recursion |
| `T(n) = T(n−1) + O(n)` | `Θ(n²)` | Naive quicksort worst case |
| `T(n) = T(n/2) + O(1)` | `Θ(log n)` | Binary search |
| `T(n) = T(n/2) + O(n)` | `Θ(n)` | Quickselect (average) |
| `T(n) = 2T(n/2) + O(1)` | `Θ(n)` | Tree traversal |
| `T(n) = 2T(n/2) + O(n)` | `Θ(n log n)` | Merge sort |
| `T(n) = 2T(n−1) + O(1)` | `Θ(2ⁿ)` | Tower of Hanoi, naive Fibonacci |

The intuition for `2T(n/2) + O(n)`:

```text
level 0:        n              → n work
level 1:     n/2 + n/2         → n work
level 2:  n/4+n/4+n/4+n/4      → n work
   ...
there are log₂ n levels × n work per level = n log n
```

---

## 10. Quick Recall — Formula Sheet

| Need | Formula |
|---|---|
| Sum `1..n` | `n(n+1)/2` |
| Sum of squares | `n(n+1)(2n+1)/6` |
| Powers of 2 sum | `2ᵏ⁺¹ − 1` |
| Harmonic sum | `≈ ln n = Θ(log n)` |
| Change of base | `log_b x = log₂ x / log₂ b` |
| `log(n!)` | `Θ(n log n)` |
| Subsets of n items | `2ⁿ` |
| Permutations | `n!` |
| Balanced tree height | `⌊log₂ n⌋` |
| `2¹⁰` | `1,024 ≈ 10³` |
| `log₂(10⁶)` | `≈ 20` |
| Merge-sort recurrence | `2T(n/2) + O(n) = Θ(n log n)` |

---

## 11. The Mental Model

```text
Loop / recursion
       ↓
Write the count as a SUM or RECURRENCE
       ↓
Apply a closed-form formula
       ↓
Drop constants and lower-order terms
       ↓
Order of growth
```
