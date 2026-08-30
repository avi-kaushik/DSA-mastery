# 014 — Master Theorem

> **One-line takeaway:** For `T(n) = aT(n/b) + f(n)`, compare `f(n)` against `n^(log_b a)`. Whichever is bigger wins; if they tie, multiply by `log n`. That's the entire theorem — and it's just the recursion tree's three shapes written as a formula.

---

## 1. The Form It Applies To

```text
T(n) = a·T(n/b) + f(n)        with  a ≥ 1,  b > 1,  f(n) > 0
```

```text
a    = number of subproblems
n/b  = size of each subproblem
f(n) = cost of dividing + combining
```

Define the **critical exponent**:

```text
        n^(log_b a)      ← the "leaf cost" — total work at the bottom of the tree
```

Then the whole method is:

```text
Compare  f(n)  with  n^(log_b a)
```

```text
f smaller  →  leaves dominate   →  Θ(n^log_b a)
f equal    →  balanced          →  Θ(n^log_b a · log n)
f bigger   →  root dominates    →  Θ(f(n))
```

> Those are exactly the three recursion-tree shapes from [note 013](013-recursion-tree-method.md).

---

## 2. The Three Cases (Precisely)

Let `c = log_b a`, so the critical function is `n^c`.

### Case 1 — Leaves dominate

```text
If  f(n) = O(n^(c − ε))   for some ε > 0        (f is POLYNOMIALLY smaller)
then  T(n) = Θ(n^c)
```

### Case 2 — Balanced

```text
If  f(n) = Θ(n^c)
then  T(n) = Θ(n^c · log n)
```

Extended form (worth knowing):

```text
If  f(n) = Θ(n^c · log^k n)   with k ≥ 0
then  T(n) = Θ(n^c · log^(k+1) n)
```

### Case 3 — Root dominates

```text
If  f(n) = Ω(n^(c + ε))   for some ε > 0        (f is POLYNOMIALLY larger)
AND the regularity condition holds:
        a·f(n/b) ≤ k·f(n)   for some k < 1 and large n
then  T(n) = Θ(f(n))
```

> The **regularity condition** just says `f` grows smoothly enough that the root really is
> the biggest level. It holds for every polynomial `f` you'll meet in practice — check it
> only if asked.

---

## 3. The Recipe

```text
1. Identify a, b, f(n).
2. Compute c = log_b a  and the critical function n^c.
3. Compare f(n) to n^c:
       f much smaller  → Case 1 → Θ(n^c)
       f same order    → Case 2 → Θ(n^c log n)
       f much larger   → Case 3 → Θ(f(n))
4. If f only differs by a log factor, use extended Case 2.
```

### 🧮 Worked — Merge Sort: `T(n) = 2T(n/2) + n`

```text
a = 2, b = 2, f(n) = n
c = log₂ 2 = 1        →  n^c = n
f(n) = n = Θ(n¹)      →  Case 2
T(n) = Θ(n log n)     ✓
```

### 🧮 Worked — Binary Search: `T(n) = T(n/2) + 1`

```text
a = 1, b = 2, f(n) = 1
c = log₂ 1 = 0        →  n^c = n⁰ = 1
f(n) = 1 = Θ(1)       →  Case 2
T(n) = Θ(1 · log n) = Θ(log n)   ✓
```

### 🧮 Worked — Tree traversal: `T(n) = 2T(n/2) + 1`

```text
a = 2, b = 2, f(n) = 1
c = log₂ 2 = 1        →  n^c = n
f(n) = 1 = O(n^(1−ε)) with ε = 1   →  Case 1
T(n) = Θ(n)           ✓
```

### 🧮 Worked — `T(n) = 3T(n/4) + n²`

```text
a = 3, b = 4, f(n) = n²
c = log₄ 3 ≈ 0.79     →  n^0.79
f(n) = n² = Ω(n^(0.79+ε))   →  Case 3
Regularity: 3·(n/4)² = 3n²/16 ≤ k·n² with k = 3/16 < 1  ✓
T(n) = Θ(n²)          ✓
```

### 🧮 Worked — Strassen: `T(n) = 7T(n/2) + n²`

```text
a = 7, b = 2, f(n) = n²
c = log₂ 7 ≈ 2.807    →  n^2.807
f(n) = n² = O(n^(2.807−ε))   →  Case 1
T(n) = Θ(n^log₂7) ≈ Θ(n^2.81)    ✓  (beats naive Θ(n³))
```

### 🧮 Worked — extended Case 2: `T(n) = 2T(n/2) + n log n`

```text
c = 1, n^c = n
f(n) = n log n = Θ(n¹ · log¹ n)   →  extended Case 2 with k = 1
T(n) = Θ(n · log² n)   ✓
```

⚠️ Standard Case 3 does **not** apply here: `n log n` is bigger than `n`, but not
**polynomially** bigger (`n log n / n = log n`, which is smaller than any `nᵋ`).

---

## 4. Reference Table

| Recurrence | `a` | `b` | `f(n)` | `n^log_b a` | Case | Result |
|---|---:|---:|---|---|:---:|---|
| `T(n/2) + 1` | 1 | 2 | `1` | `n⁰ = 1` | 2 | `Θ(log n)` |
| `T(n/2) + n` | 1 | 2 | `n` | `1` | 3 | `Θ(n)` |
| `2T(n/2) + 1` | 2 | 2 | `1` | `n` | 1 | `Θ(n)` |
| `2T(n/2) + n` | 2 | 2 | `n` | `n` | 2 | `Θ(n log n)` |
| `2T(n/2) + n²` | 2 | 2 | `n²` | `n` | 3 | `Θ(n²)` |
| `2T(n/2) + n log n` | 2 | 2 | `n log n` | `n` | 2-ext | `Θ(n log² n)` |
| `4T(n/2) + n` | 4 | 2 | `n` | `n²` | 1 | `Θ(n²)` |
| `4T(n/2) + n²` | 4 | 2 | `n²` | `n²` | 2 | `Θ(n² log n)` |
| `4T(n/2) + n³` | 4 | 2 | `n³` | `n²` | 3 | `Θ(n³)` |
| `3T(n/2) + n` | 3 | 2 | `n` | `n^1.58` | 1 | `Θ(n^log₂3)` — Karatsuba |
| `7T(n/2) + n²` | 7 | 2 | `n²` | `n^2.81` | 1 | `Θ(n^log₂7)` — Strassen |
| `8T(n/2) + n²` | 8 | 2 | `n²` | `n³` | 1 | `Θ(n³)` — naive matrix multiply |
| `3T(n/4) + n²` | 3 | 4 | `n²` | `n^0.79` | 3 | `Θ(n²)` |
| `9T(n/3) + n` | 9 | 3 | `n` | `n²` | 1 | `Θ(n²)` |

> **Reading the pattern:** more branching (`a↑`) or smaller shrinkage (`b↓`) pushes work to
> the leaves; heavier combining (`f↑`) pushes work to the root.

---

## 5. ⚠️ When the Master Theorem Does NOT Apply

| Situation | Example | Why it fails | What to use |
|---|---|---|---|
| Unequal subproblem sizes | `T(n/3) + T(2n/3) + n` | Not the form `aT(n/b)` | Recursion tree → `Θ(n log n)` |
| Subtractive recurrence | `T(n−1) + n` | Size decreases by a constant, not a factor | Iteration → `Θ(n²)` |
| `f(n)` not polynomially comparable | `2T(n/2) + n/log n` | Gap is `log n`, smaller than any `nᵋ` | Recursion tree → `Θ(n log log n)` |
| Non-monotonic or negative `f` | `f(n) = n²·sin n` | Violates the assumptions | Other methods |
| `a` not constant | `T(n) = nT(n/2) + n` | `a` must be a constant | Iteration |
| Regularity fails (Case 3) | contrived `f` | Root doesn't actually dominate | Recursion tree |

> **The "polynomially smaller/larger" requirement is the trap.** `n` vs `n log n` and `n` vs
> `n/log n` are *not* polynomially separated, so Cases 1 and 3 don't apply — these are the
> gaps between the cases.

---

## 6. Master Theorem for Decrease & Conquer

Less famous, but it handles `T(n) = a·T(n−b) + f(n)` where `f(n) = O(n^k)`:

```text
a < 1   →   T(n) = O(n^k)
a = 1   →   T(n) = O(n^(k+1))
a > 1   →   T(n) = O(n^k · a^(n/b))
```

Examples:

```text
T(n) = T(n−1) + 1     → a=1, k=0  →  O(n)
T(n) = T(n−1) + n     → a=1, k=1  →  O(n²)
T(n) = 2T(n−1) + 1    → a=2, k=0  →  O(2ⁿ)
T(n) = 3T(n−1) + n    → a=3, k=1  →  O(n·3ⁿ)
```

> Note the shape: `a > 1` with a subtractive recurrence is **always exponential**. That is
> why memoisation matters so much for these.

---

## 7. Akra–Bazzi (Just Be Aware of It)

The general tool for unequal splits, `T(n) = Σ aᵢT(n/bᵢ) + f(n)`. Find `p` such that
`Σ aᵢ/bᵢᵖ = 1`, then integrate. Rarely needed in interviews — mentioning that it exists,
and that the **recursion tree handles those cases fine**, is enough.

---

## 8. Full Recipe: Analysing Any Recursive Algorithm

Putting notes 011–014 together:

```text
1. Write the recurrence     → a, n/b or n−c, f(n), base case      (011)
2. Pick a solving method:
       aT(n/b) + f(n)?      → Master Theorem                      (014)
       unequal split?       → Recursion Tree                      (013)
       T(n−c) form?         → Iteration                           (012)
       argument is √n?      → change of variable n = 2ᵐ           (012)
3. Sum with the right series formula                              (010)
4. Prove by induction if rigour is demanded                       (012)
5. Analyse SPACE separately: max recursion depth + allocations    (011)
6. Sanity-check against the standard catalogue                    (011)
```

---

## 9. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **State the master theorem.** | For `T(n)=aT(n/b)+f(n)`, compare `f(n)` with `n^(log_b a)`: smaller → `Θ(n^log_b a)`, equal → `Θ(n^log_b a · log n)`, larger → `Θ(f(n))`. |
| **What is `n^(log_b a)`?** | The total cost of the leaves — `a^(log_b n)` leaf nodes. |
| **Which case is merge sort?** | Case 2: `a=b=2`, `n^log₂2 = n = f(n)` → `Θ(n log n)`. |
| **Which case is binary search?** | Case 2 with `c = 0`: `n⁰ = 1 = f(n)` → `Θ(log n)`. |
| **Strassen's complexity?** | `7T(n/2)+n²`, Case 1 → `Θ(n^log₂7) ≈ Θ(n^2.81)`. |
| **What is the regularity condition?** | `a·f(n/b) ≤ k·f(n)` for some `k < 1` — needed for Case 3 so the root truly dominates. |
| **Why doesn't it solve `2T(n/2)+n/log n`?** | `n/log n` isn't *polynomially* smaller than `n`, so it falls in the gap between cases; the tree gives `Θ(n log log n)`. |
| **Why doesn't it solve `T(n/3)+T(2n/3)+n`?** | Unequal subproblem sizes aren't of the form `aT(n/b)`; use a recursion tree → `Θ(n log n)`. |
| **Master theorem for `T(n)=aT(n−b)+n^k`?** | `a<1` → `O(n^k)`; `a=1` → `O(n^(k+1))`; `a>1` → `O(n^k a^(n/b))`. |
| **How does it relate to recursion trees?** | It's the closed form of the three tree shapes: leaf-heavy, balanced, root-heavy. |

---

## 10. The Mental Model

```text
        T(n) = a·T(n/b) + f(n)
                    │
                    ↓
     Leaf work  n^(log_b a)   vs   Root work  f(n)
                    │
   ┌────────────────┼────────────────┐
   ↓                ↓                ↓
 leaves           equal            root
 bigger                           bigger
   ↓                ↓                ↓
Θ(n^log_b a)  Θ(n^log_b a·log n)  Θ(f(n))
 Case 1           Case 2          Case 3
```
