# 002 — Order of Growth

> **One-line takeaway:** Order of growth is the *shape* of a cost function — the single term that decides how the algorithm behaves when the input gets large. Comparing algorithms = comparing their order of growth.

---

## 1. What Is Order of Growth?

> **The order of growth of an algorithm is the rate at which its cost increases as the input size increases.**

Two functions with the same order of growth are considered **equally good**
asymptotically, no matter their constants:

```text
T₁(n) = 5n        ┐
T₂(n) = 100n      ├─ all O(n): double n → double the work
T₃(n) = n/2 + 900 ┘
```

The question is never *"how much work?"* but **"how does the work change when `n` changes?"**

```text
n doubles →  O(1)      : no change
             O(log n)  : +1 step
             O(n)      : 2× work
             O(n log n): slightly more than 2×
             O(n²)     : 4× work
             O(n³)     : 8× work
             O(2ⁿ)     : squares the work  ☠️
```

---

## 2. The Growth Hierarchy

```text
O(1) < O(log n) < O(√n) < O(n) < O(n log n) < O(n²) < O(n³) < O(2ⁿ) < O(n!)
```

```text
 cost
   ▲                      2ⁿ  n²
   │                      │   │
   │                      │  │        n log n
   │                      │ │      ／
   │                      ││   ／        n
   │                      ││／      ────────
   │                    ／│ ──────
   │              ／────── ─────────────── log n
   │        ────────────────────────────── 1
   └──────────────────────────────────────────▶  n
```

### Real numbers (approximate operation counts)

| n | log n | n | n log n | n² | 2ⁿ |
|---:|---:|---:|---:|---:|---:|
| 10 | 3 | 10 | 33 | 100 | 1,024 |
| 100 | 7 | 100 | 664 | 10,000 | 10³⁰ |
| 1,000 | 10 | 1,000 | 10⁴ | 10⁶ | ☠️ |
| 10⁶ | 20 | 10⁶ | 2×10⁷ | 10¹² | ☠️ |

> The gap between `O(n)` and `O(n²)` at `n = 10⁶` is **a million times**. That is the whole
> reason this subject exists.

---

## 3. Where Each Growth Class Comes From

| Growth | Typical source | Example |
|---|---|---|
| `O(1)` | Fixed work, no loop over input | Array access, hash lookup (avg) |
| `O(log n)` | Problem size divided by a constant each step | Binary search, balanced BST search |
| `O(√n)` | Loop while `i*i ≤ n` | Primality check, divisor loop |
| `O(n)` | One pass over the input | Linear search, sum of array |
| `O(n log n)` | `log n` levels × `O(n)` work per level | Merge sort, heap sort, sorting in general |
| `O(n²)` | Every element vs every element | Bubble/selection/insertion sort, all pairs |
| `O(n³)` | Three nested loops | Naive matrix multiplication, Floyd–Warshall |
| `O(2ⁿ)` | Include/exclude branching recursion | All subsets, naive Fibonacci |
| `O(n!)` | All orderings | All permutations, brute-force TSP |

---

## 4. Rules for Reducing to Order of Growth

```text
Rule 1 — Drop constant factors
         O(5n)      → O(n)
         O(n/2)     → O(n)
         O(3n²)     → O(n²)

Rule 2 — Keep only the dominant term
         n² + n + 100     → O(n²)
         2ⁿ + n¹⁰⁰        → O(2ⁿ)
         n log n + n      → O(n log n)

Rule 3 — Constants inside logs vanish
         O(log₂ n) = O(log₁₀ n) = O(log n)      (base change is a constant factor)
         O(log n²) = O(2 log n) = O(log n)

Rule 4 — Different variables stay separate
         O(n + m) is NOT O(n)      unless you know m ≤ n
         O(nm)    is NOT O(n²)     unless m = n
```

---

## 5. 🧮 Math You Need Here

### Why the log base doesn't matter

Change-of-base formula:

```text
log_b n = log₂ n / log₂ b
```

`1 / log₂ b` is a **constant**, and constants are dropped. Hence:

```text
O(log₂ n) = O(log₁₀ n) = O(ln n) = O(log n)
```

⚠️ This is true for **logs**, not for exponents: `O(2ⁿ) ≠ O(3ⁿ)`.

### Log and exponent identities (used constantly)

```text
log(ab)   = log a + log b
log(a/b)  = log a − log b
log(aᵇ)   = b · log a
a^log_a n = n
2^(log₂ n) = n
log₂ 1 = 0,  log₂ 2 = 1,  log₂ 1024 = 10,  log₂ 10⁶ ≈ 20
```

Useful powers of two:

```text
2¹⁰ ≈ 10³ (1,024)      2²⁰ ≈ 10⁶       2³⁰ ≈ 10⁹
```

So `log₂(10⁶) ≈ 20` and `log₂(10⁹) ≈ 30` — handy for instant estimates.

### Comparing two growth functions formally

Use the limit (ratio) test:

```text
        f(n)
  lim  ──────  =  0    → f grows slower  → f = O(g), and f ≠ Θ(g)
 n→∞    g(n)      c    → same order      → f = Θ(g)
                  ∞    → f grows faster  → g = O(f)
```

Example — is `log n` slower than `√n`?

```text
lim (log n / √n) = 0     →  log n grows slower  ✓
```

**Ordering worth memorising:**

```text
constants < log n < (log n)² < √n < n < n log n < n² < n³ < 2ⁿ < n! < nⁿ
```

> Any polylogarithm beats any polynomial; any polynomial beats any exponential.

---

## 6. Order of Growth vs Constraints

Growth only matters relative to the input limits. Assuming ~10⁸ simple operations/second:

| Input size `n` | Complexity that usually fits |
|---|---|
| `n ≤ 10–12` | `O(n!)` |
| `n ≤ 20–25` | `O(2ⁿ)` |
| `n ≤ 500` | `O(n³)` |
| `n ≤ 5,000` | `O(n²)` |
| `n ≤ 10⁶` | `O(n log n)` |
| `n ≤ 10⁸` | `O(n)` |
| `n` huge / 10¹⁸ | `O(log n)` or `O(1)` |

> **Read the constraints first — they tell you the target complexity before you start.**

---

## 7. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What is order of growth?** | The rate at which an algorithm's cost increases with input size — the dominant term of its cost function. |
| **Why is `O(5n)` the same as `O(n)`?** | Constant factors don't change the growth rate; both double when `n` doubles. |
| **Why is the log base ignored?** | Changing base multiplies by a constant (`log_b n = log₂n / log₂b`), and constants are dropped. |
| **Is `O(n + m)` the same as `O(n)`?** | No — unless you know `m` is bounded by `n`. Keep independent variables separate. |
| **How do you compare two functions formally?** | Take `lim f(n)/g(n)`: 0 → f is smaller, constant → same order, ∞ → f is bigger. |
| **Order the common classes.** | `1 < log n < √n < n < n log n < n² < n³ < 2ⁿ < n!` |
| **What does `n` doubling do to `O(n²)`?** | Quadruples the work. |

---

## 8. The Mental Model

```text
             What happens when n doubles?
                        │
   ┌────────────┬───────┴───────┬─────────────┐
   ↓            ↓               ↓             ↓
 nothing     +1 step        2× work       4× work
  O(1)       O(log n)         O(n)         O(n²)
```
