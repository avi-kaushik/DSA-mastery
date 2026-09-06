# 008 — Analysis of Loops

> **One-line takeaway:** Don't count loops — count **how many times the body actually runs**. Sequential loops **add**, nested loops **multiply**, and how the counter changes (`i++` vs `i*=2`) decides everything.

---

## 1. The Only Question That Matters

```text
How many times does the innermost statement execute, as a function of n?
```

Not "how many loops are there". A triple-nested loop can be `O(n)` and a single loop can be
`O(2ⁿ)`.

---

## 2. Common Single-Loop Patterns

### O(1) — constant

```java
for (int i = 0; i < 100; i++) { ... }     // bound doesn't involve n
```

Runs 100 times regardless of `n` → **O(1)**.

### O(n) — linear

```java
for (int i = 0; i < n; i++) { ... }       // i += c also gives O(n)
```

Runs `n` times → **O(n)**. Even `i += 5` is `n/5` = **O(n)**.

### O(log n) — multiply or divide by a constant

```java
for (int i = 1; i < n; i = i * 2) { ... }
```

```text
i takes values: 1, 2, 4, 8, ..., 2ᵏ
Stops when 2ᵏ ≥ n   ⟹   k = log₂ n
```

→ **O(log n)**. Same for the shrinking version:

```java
while (n > 1) { n = n / 2; }              // O(log n)
```

⚠️ The base changes with the step: `i = i * 3` → `log₃ n`, still **O(log n)**.

### O(log log n) — squaring

```java
for (int i = 2; i < n; i = i * i) { ... }  // O(log log n)
```

### O(√n) — square-root loop

```java
for (int i = 1; i * i <= n; i++) { ... }
```

Stops when `i > √n` → **O(√n)**. (Classic in primality testing and divisor loops.)

---

## 3. Sequential Loops — ADD

```java
for (int i = 0; i < n; i++) { ... }       // O(n)
for (int j = 0; j < n; j++) { ... }       // O(n)
```

```text
O(n) + O(n) = O(2n) = O(n)
```

With different orders, the **larger wins**:

```java
for (int i = 0; i < n; i++) { ... }              // O(n)
for (int i = 0; i < n; i++)
    for (int j = 0; j < n; j++) { ... }          // O(n²)
```

```text
O(n) + O(n²) = O(n²)
```

> **Sum rule:** `O(f) + O(g) = O(max(f, g))`

---

## 4. Nested Loops — MULTIPLY

```java
for (int i = 0; i < n; i++)
    for (int j = 0; j < n; j++)
        { ... }
```

```text
n × n = n²   →  O(n²)
```

Three levels → `O(n³)`. But **only when the bounds are independent of each other and of a
constant**:

```java
for (int i = 0; i < n; i++)
    for (int j = 0; j < 10; j++)      // constant inner bound
        { ... }
```

```text
n × 10 = 10n  →  O(n)     NOT O(n²)
```

```java
for (int i = 0; i < n; i++)
    for (int j = 1; j < n; j = j * 2)
        { ... }
```

```text
n × log n  →  O(n log n)
```

> **Product rule:** nested independent loops multiply their counts.

---

## 5. Multiple Variables — Keep Them Separate

```java
for (int i = 0; i < n; i++)
    for (int j = 0; j < m; j++)
        { ... }
```

```text
O(n · m)      ⚠️ NOT O(n²) unless you know m = n
```

Sequential, independent inputs:

```java
for (int i = 0; i < n; i++) { ... }
for (int j = 0; j < m; j++) { ... }
```

```text
O(n + m)      ⚠️ NOT O(n) unless m ≤ n
```

This matters constantly for:

```text
Matrices → O(rows × cols)
Graphs   → O(V + E) for BFS/DFS, not O(V²)
Two arrays / two strings → O(n + m)
```

---

## 6. 🧮 Dependent (Triangular) Loops

```java
for (int i = 0; i < n; i++)
    for (int j = i; j < n; j++)
        { ... }
```

The inner loop does **not** run `n` times each pass:

```text
i = 0 → n times
i = 1 → n−1 times
i = 2 → n−2 times
...
i = n−1 → 1 time
```

Total:

```text
n + (n−1) + (n−2) + ... + 1  =  n(n+1)/2  =  (n² + n)/2
```

Drop constants and lower terms → **O(n²)**.

> Half of `n²` is still `n²`. **Triangular nesting doesn't change the order** — but you must
> know *why* to answer the follow-up.

---

## 7. 🧮 The Harmonic Pattern — O(n log n)

A pattern that looks quadratic but isn't:

```java
for (int i = 1; i <= n; i++)
    for (int j = i; j <= n; j = j + i)
        { ... }
```

The inner loop runs `n/i` times, so the total is:

```text
n/1 + n/2 + n/3 + ... + n/n  =  n · (1 + ½ + ⅓ + ... + 1/n)
                             =  n · Hₙ
```

The **harmonic series** `Hₙ ≈ ln n`, so:

```text
n · ln n  →  O(n log n)
```

This is exactly the Sieve of Eratosthenes' inner structure (which refines to
`O(n log log n)` because it only starts at primes).

---

## 8. Loops That Modify the Counter Inside

```java
for (int i = 0; i < n; i++) {
    if (someCondition) i = i * 2;
}
```

Never trust the header alone — **read the body** for counter updates, `break`, `continue`
and early `return`. These change the count (and often create a best/worst-case split).

```java
while (i < n) {
    if (x) i++;        // slow path
    else   i *= 2;     // fast path
}
```

```text
Best case  → O(log n)
Worst case → O(n)
```

---

## 9. Pattern Reference Table

| Loop | Iterations | Complexity |
|---|---|---|
| `i = 0; i < c; i++` | `c` | `O(1)` |
| `i = 0; i < n; i++` | `n` | `O(n)` |
| `i = 0; i < n; i += k` | `n/k` | `O(n)` |
| `i = 1; i < n; i *= 2` | `log₂ n` | `O(log n)` |
| `i = n; i > 0; i /= 2` | `log₂ n` | `O(log n)` |
| `i = 2; i < n; i *= i` | `log log n` | `O(log log n)` |
| `i = 1; i*i <= n; i++` | `√n` | `O(√n)` |
| two nested `n` loops | `n²` | `O(n²)` |
| `n` loop × constant loop | `cn` | `O(n)` |
| `n` loop × `log n` loop | `n log n` | `O(n log n)` |
| `n` loop × `√n` loop | `n√n` | `O(n^1.5)` |
| `j = i; j < n; j++` (triangular) | `n(n+1)/2` | `O(n²)` |
| `j = i; j <= n; j += i` (harmonic) | `n·Hₙ` | `O(n log n)` |
| `n` loop and `m` loop nested | `nm` | `O(nm)` |

---

## 10. 🧮 Summation Formulas Used Above

```text
Arithmetic     1 + 2 + 3 + ... + n        = n(n+1)/2        = Θ(n²)
Squares        1² + 2² + ... + n²         = n(n+1)(2n+1)/6  = Θ(n³)
Geometric      1 + 2 + 4 + ... + 2ᵏ       = 2ᵏ⁺¹ − 1        = Θ(2ᵏ)
Geometric (<1) 1 + ½ + ¼ + ...           < 2                = Θ(1)
Harmonic       1 + ½ + ⅓ + ... + 1/n      ≈ ln n            = Θ(log n)
Constant sum   c + c + ... (n times)      = cn              = Θ(n)
```

> The geometric-series fact — a doubling sum is dominated by its **last** term — is why
> dynamic-array resizing is `O(n)` total, not `O(n²)`.

---

## 11. Step-by-Step Method

```text
1. Identify n — what is the input size?
2. Find the innermost repeated work.
3. Count the innermost loop's iterations (watch the update: ++, +=k, *=2, i*i).
4. Walk outward: nested → multiply, sequential → add.
5. Check whether inner bounds depend on the outer variable → use a summation.
6. Check the body for counter changes, break, return.
7. Simplify: drop constants, keep the dominant term.
8. Analyse space separately (arrays, maps, recursion stack).
```

---

## 12. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **Sequential vs nested loops?** | Sequential add → `O(max(f,g))`; nested multiply → `O(f·g)`. |
| **Why is `i *= 2` logarithmic?** | `i` reaches `2ᵏ`; it stops when `2ᵏ ≥ n`, so `k = log₂ n`. |
| **Are two nested loops always `O(n²)`?** | No — a constant inner bound gives `O(n)`, a doubling inner loop gives `O(n log n)`. |
| **Complexity of `for j = i; j < n; j++`?** | `n(n+1)/2` iterations = `O(n²)` — triangular, but still quadratic. |
| **`for j = i; j <= n; j += i` inside an `n` loop?** | Harmonic sum `n·Hₙ` = `O(n log n)`. |
| **`for (i = 1; i*i <= n; i++)`?** | `O(√n)`. |
| **Nested loops over `n` and `m`?** | `O(nm)` — never collapse to `O(n²)` without knowing `m = n`. |
| **BFS/DFS complexity form?** | `O(V + E)`, a two-variable bound — not `O(V²)`. |

---

## 13. The Mental Model

```text
             Look at the LOOP BODY, not the loop count
                              │
          ┌───────────────────┼───────────────────┐
          ↓                   ↓                   ↓
   How does the        Do bounds depend      Are loops nested
   counter change?     on outer vars?        or sequential?
          │                   │                   │
    ++ → n              j = i → summation    nested → ×
    *=2 → log n         j += i → harmonic    sequential → +
    i*i → √n
```
