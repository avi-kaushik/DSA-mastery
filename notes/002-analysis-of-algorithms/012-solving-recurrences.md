# 012 — Solving Recurrences: Iteration & Substitution

> **One-line takeaway:** Two general-purpose methods. **Iteration** = expand the recurrence until you see a pattern, then sum it. **Substitution** = guess the answer, then prove it by induction. Iteration finds the answer; substitution proves it.

---

## 1. The Three Methods (and When to Use Which)

| Method | What it does | Best for | Note |
|---|---|---|---|
| **Iteration / Expansion** | Unroll until a pattern appears, then sum | Any recurrence; especially `n−c` types | This page |
| **Recursion Tree** | Draw it, sum per level | Divide & conquer, visual intuition | [013](013-recursion-tree-method.md) |
| **Master Theorem** | Plug into a formula | `aT(n/b) + f(n)` only | [014](014-master-theorem.md) |
| **Substitution** | Guess, then prove by induction | Verifying a guess rigorously | This page |

> Recursion tree and iteration are the same computation — one drawn, one written.

---

## 2. Method 1 — Iteration (Back-Substitution)

### The procedure

```text
1. Write T(n) using the recurrence.
2. Replace T(smaller) with its own expansion. Repeat 2–3 times.
3. DO NOT simplify too early — keep the pattern visible.
4. Generalise to the k-th expansion.
5. Find the k that reaches the base case.
6. Substitute that k and sum the series.
```

---

### 🧮 Example 1 — `T(n) = T(n−1) + 1`, `T(1) = 1`

```text
T(n) = T(n−1) + 1
     = [T(n−2) + 1] + 1  = T(n−2) + 2
     = [T(n−3) + 1] + 2  = T(n−3) + 3
     ...
     = T(n−k) + k
```

Base case reached when `n − k = 1` → `k = n − 1`:

```text
T(n) = T(1) + (n − 1) = 1 + n − 1 = n
```

```text
⟹ T(n) = Θ(n)
```

---

### 🧮 Example 2 — `T(n) = T(n−1) + n`, `T(0) = 0`

```text
T(n) = T(n−1) + n
     = T(n−2) + (n−1) + n
     = T(n−3) + (n−2) + (n−1) + n
     ...
     = T(n−k) + [(n−k+1) + ... + n]
```

At `k = n`:

```text
T(n) = T(0) + (1 + 2 + ... + n)
     = n(n+1)/2                     ← arithmetic series
```

```text
⟹ T(n) = Θ(n²)
```

> This is quick sort's worst case, and the reason it degrades to quadratic.

---

### 🧮 Example 3 — `T(n) = T(n/2) + 1`, `T(1) = 1`

```text
T(n) = T(n/2) + 1
     = T(n/4) + 1 + 1     = T(n/2²) + 2
     = T(n/8) + 3         = T(n/2³) + 3
     ...
     = T(n/2ᵏ) + k
```

Base case when `n/2ᵏ = 1` → `2ᵏ = n` → **`k = log₂ n`**:

```text
T(n) = T(1) + log₂ n = 1 + log₂ n
```

```text
⟹ T(n) = Θ(log n)          ← binary search
```

> **The step that matters:** solving `n/2ᵏ = 1` for `k` is where the logarithm comes from.
> Everything logarithmic in DSA traces back to this line.

---

### 🧮 Example 4 — `T(n) = 2T(n/2) + n`, `T(1) = 1`

```text
T(n) = 2T(n/2) + n
     = 2[2T(n/4) + n/2] + n      = 4T(n/4) + n + n     = 4T(n/4) + 2n
     = 4[2T(n/8) + n/4] + 2n     = 8T(n/8) + 3n
     ...
     = 2ᵏ·T(n/2ᵏ) + k·n
```

Base case at `k = log₂ n`, where `2ᵏ = n`:

```text
T(n) = n·T(1) + n·log₂ n
     = n + n log₂ n
```

```text
⟹ T(n) = Θ(n log n)        ← merge sort
```

Notice **each expansion contributed exactly `n`**, and there were `log n` of them. That is
the entire `n log n` story.

---

### 🧮 Example 5 — `T(n) = 2T(n−1) + 1`, `T(0) = 0`

```text
T(n) = 2T(n−1) + 1
     = 2[2T(n−2) + 1] + 1        = 4T(n−2) + 2 + 1
     = 4[2T(n−3) + 1] + 3        = 8T(n−3) + 4 + 2 + 1
     ...
     = 2ᵏT(n−k) + (2ᵏ⁻¹ + ... + 2 + 1)
     = 2ᵏT(n−k) + (2ᵏ − 1)       ← geometric series
```

At `k = n`:

```text
T(n) = 2ⁿ·T(0) + 2ⁿ − 1 = 2ⁿ − 1
```

```text
⟹ T(n) = Θ(2ⁿ)             ← Tower of Hanoi, exactly 2ⁿ − 1 moves
```

---

### 🧮 Example 6 — `T(n) = T(√n) + 1`

A change of variable makes it tractable. Let `n = 2ᵐ`, so `m = log₂ n` and `√n = 2^(m/2)`:

```text
T(2ᵐ) = T(2^(m/2)) + 1
Define S(m) = T(2ᵐ):
S(m) = S(m/2) + 1        ← now it's Example 3
S(m) = Θ(log m)
T(n) = Θ(log log n)
```

> **Trick to remember:** when the argument is `√n`, substitute `n = 2ᵐ`.

---

### The telescoping view

Iteration is telescoping in disguise:

```text
T(n)   − T(n−1) = n
T(n−1) − T(n−2) = n−1
   ...
T(1)   − T(0)   = 1
─────────────────────── add
T(n)   − T(0)   = Σ i = n(n+1)/2
```

---

## 3. Method 2 — Substitution (Guess & Prove by Induction)

Iteration *finds* an answer but hand-waves the pattern. Substitution *proves* it.

### The procedure

```text
1. GUESS the form of the answer (from iteration, a recursion tree, or the catalogue).
2. Assume it holds for all smaller inputs (inductive hypothesis).
3. Substitute into the recurrence and show it holds for n.
4. Verify the base case.
```

You must prove the bound **with explicit constants**, not with `O(...)` inside the induction.

---

### 🧮 Example — prove `T(n) = 2T(n/2) + n` is `O(n log n)`

**Guess:** `T(n) ≤ c·n log₂ n` for some constant `c > 0` and all `n ≥ n₀`.

**Inductive hypothesis:** assume it holds for `n/2`:

```text
T(n/2) ≤ c·(n/2)·log₂(n/2)
```

**Inductive step:**

```text
T(n) = 2T(n/2) + n
     ≤ 2 · [c·(n/2)·log₂(n/2)] + n
     = c·n·log₂(n/2) + n
     = c·n·(log₂ n − 1) + n              ← log(n/2) = log n − 1
     = c·n·log₂ n − c·n + n
     ≤ c·n·log₂ n                         provided  −c·n + n ≤ 0
                                          i.e.      c ≥ 1
```

**Base case:** for `n = 2`, `T(2) = 2T(1) + 2`. With `T(1) = 1`, `T(2) = 4`, and
`c·2·log₂2 = 2c`, so `c ≥ 2` works.

**Conclusion:** with `c = 2` and `n₀ = 2`, `T(n) ≤ 2n log₂ n`, so `T(n) = O(n log n)`. ∎

> Note `n = 1` fails (`log₂1 = 0`, so the bound would force `T(1) ≤ 0`). Starting the
> induction at `n₀ = 2` is legitimate — asymptotics only care about large `n`.

---

### ⚠️ Pitfall: the "sloppy O" fallacy

A famous wrong proof that `T(n) = 2T(n/2) + n` is `O(n)`:

```text
T(n) ≤ 2·(c·n/2) + n = c·n + n = (c+1)·n = O(n)      ✗ WRONG
```

The error: `(c+1)n` is **not** `≤ cn`. The constant grew, so the hypothesis was never
re-established. You must land on **exactly the same constant** you assumed.

> **Rule:** the inductive step must reproduce the hypothesis's form and constant, not a
> bigger one.

---

### 🧮 Pitfall: sometimes you must *strengthen* the hypothesis

For `T(n) = 2T(n/2) + 1`, guessing `T(n) ≤ cn` fails:

```text
T(n) ≤ 2·c·(n/2) + 1 = cn + 1  ⊄  cn      ✗
```

Subtract a lower-order term — guess `T(n) ≤ cn − d`:

```text
T(n) ≤ 2(c·n/2 − d) + 1 = cn − 2d + 1 ≤ cn − d     provided  d ≥ 1     ✓
```

> **Counter-intuitive but standard:** proving a *stronger* statement is often easier,
> because the surplus `−d` absorbs the extra term.

---

## 4. Choosing a Method

```text
Is it of the form a·T(n/b) + f(n)?
        │
   yes ─┴─ no
    │       │
Master      Is the argument n − c?
Theorem         │
(014)      yes ─┴─ no
            │       │
        Iteration   Recursion tree
        (this page) (013) or change of variable
                    │
                    └── then Substitution to PROVE it
```

---

## 5. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **Name the methods for solving recurrences.** | Iteration/expansion, recursion tree, master theorem, and substitution (guess + induction). |
| **How does iteration work?** | Expand repeatedly, generalise to the k-th step, find the `k` that hits the base case, then sum the series. |
| **Where does `log n` come from?** | Solving `n/2ᵏ = 1` gives `k = log₂ n` — the number of halvings. |
| **Solve `T(n) = T(n−1) + n`.** | Expands to `1+2+…+n = n(n+1)/2` → `Θ(n²)`. |
| **Solve `T(n) = 2T(n−1) + 1`.** | `2ⁿ − 1` → `Θ(2ⁿ)` — Tower of Hanoi. |
| **Solve `T(n) = 2T(n/2) + n`.** | `n` per level × `log n` levels → `Θ(n log n)`. |
| **What is the substitution method?** | Guess the bound, assume it for smaller inputs, prove it for `n` by induction with explicit constants. |
| **Why can't you use `O()` inside induction?** | The constant can silently grow (`cn + n = (c+1)n`), which never re-establishes the hypothesis. |
| **What if the guess almost works?** | Strengthen it — subtract a lower-order term, e.g. prove `T(n) ≤ cn − d`. |
| **How do you handle `T(n) = T(√n) + 1`?** | Substitute `n = 2ᵐ` to turn it into `S(m) = S(m/2) + 1` → `Θ(log log n)`. |

---

## 6. The Mental Model

```text
        Recurrence
             │
             ↓
   Expand 3 times, look for the pattern
             │
             ↓
   Generalise to step k:  T(n) = ...(k)...
             │
             ↓
   Find k where the base case is hit
      n − k = 1   →  k = n
      n/2ᵏ = 1    →  k = log n
             │
             ↓
   Substitute k and SUM (AP / GP / harmonic)
             │
             ↓
   Prove it by induction if rigour is required
```
