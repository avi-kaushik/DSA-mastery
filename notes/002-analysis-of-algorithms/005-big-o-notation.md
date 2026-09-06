# 005 — Big-O Notation (Upper Bound)

> **One-line takeaway:** `f(n) = O(g(n))` means **f never grows faster than g**, for large `n`, ignoring constants. It's a *ceiling* — a guarantee that things won't get worse than this.

---

## 1. Definition

> **`f(n) = O(g(n))` if there exist positive constants `c` and `n₀` such that
> `0 ≤ f(n) ≤ c·g(n)` for all `n ≥ n₀`.**

```text
∃ c > 0, ∃ n₀ > 0 :  0 ≤ f(n) ≤ c·g(n)   ∀ n ≥ n₀
```

```text
 cost
   ▲
   │            c·g(n)  ← the ceiling
   │           ╱
   │          ╱  f(n)
   │         ╱ ╱
   │        ╱╱
   │      ╱╱
   │   ╱╱ ┆
   └──────┆──────────────▶ n
          n₀
       (before n₀, anything goes)
```

Two escape hatches make the definition work:

- **`c`** — lets you ignore constant factors (`1000n` is still `O(n)`).
- **`n₀`** — lets you ignore small inputs entirely.

---

## 2. 🧮 Worked Proof

**Claim:** `3n + 5 = O(n)`

Find `c` and `n₀` such that `3n + 5 ≤ c·n`.

```text
For n ≥ 1:      5 ≤ 5n
So:             3n + 5 ≤ 3n + 5n = 8n
```

Choose `c = 8`, `n₀ = 1`:

```text
3n + 5 ≤ 8n     for all n ≥ 1     ✓
```

Therefore `3n + 5 = O(n)`. ∎

> The constants are not unique — `c = 4, n₀ = 5` also works (`3n+5 ≤ 4n` when `n ≥ 5`).
> **You only need to exhibit one valid pair.**

**Second example:** `2n² + 3n + 1 = O(n²)`

```text
For n ≥ 1:   3n ≤ 3n²  and  1 ≤ n²
So:          2n² + 3n + 1 ≤ 2n² + 3n² + n² = 6n²
⟹ c = 6, n₀ = 1
```

---

## 3. 🧮 How to Disprove a Big-O Claim

**Claim:** `n² ≠ O(n)`

Suppose it were. Then `n² ≤ c·n` for all `n ≥ n₀`, so dividing by `n`:

```text
n ≤ c      for all n ≥ n₀
```

But `n` grows without bound and `c` is fixed — contradiction (take `n = c + 1`).
Therefore `n² ≠ O(n)`. ∎

> **Technique:** assume it holds, simplify, and derive "an unbounded quantity is bounded".

---

## 4. Big-O Is an Upper Bound, Not "The Answer"

Every one of these is **true**:

```text
n  = O(n)       ✓ tight
n  = O(n log n) ✓ loose
n  = O(n²)      ✓ loose
n  = O(n!)      ✓ ridiculous but valid
```

Because O is only `≤`. Convention:

> **State the tightest upper bound you can prove.** In interviews, a loose bound reads as
> a wrong answer even though it's technically correct.

---

## 5. Useful Properties

```text
Constant factor    O(c·f)          = O(f)
Sum (sequential)   O(f) + O(g)     = O(max(f, g))
Product (nested)   O(f) × O(g)     = O(f·g)
Transitivity       f=O(g), g=O(h)  ⟹ f = O(h)
Reflexivity        f = O(f)
Polynomial         aₖnᵏ + … + a₀   = O(nᵏ)
```

The **sum rule** is why sequential code collapses so cleanly:

```text
O(n) + O(n²) + O(log n) = O(n²)
```

---

## 6. Common Mistakes

| ✗ Wrong | ✓ Right |
|---|---|
| "Big-O means worst case" | Big-O is an upper bound on a *function*; you can state the Big-O of the best case too |
| "Binary search is O(n)" | Technically true, but the tight bound is `O(log n)` |
| `O(2n) = O(n)` written as `O(2n)` | Drop the constant: just `O(n)` |
| "`O(n²)` is always slower than `O(n)`" | Only for large `n`; constants can dominate at small `n` |
| Adding nested loops | Nested loops **multiply**, sequential loops add |
| `O(n + m) = O(n)` | Only if `m ≤ n`; otherwise keep both |

---

## 7. When You'll Actually Use Big-O

- **Stating guarantees:** "this API responds in `O(log n)` regardless of data size."
- **Rejecting an approach early:** constraints say `n = 10⁶`, your idea is `O(n²)` → discard
  before coding it.
- **Comparing candidate solutions** in an interview before writing any code.

---

## 8. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **Define Big-O.** | `f = O(g)` if ∃ `c, n₀ > 0` with `0 ≤ f(n) ≤ c·g(n)` for all `n ≥ n₀`. |
| **What do `c` and `n₀` do?** | `c` absorbs constant factors; `n₀` lets us ignore small inputs. |
| **Prove `3n + 5 = O(n)`.** | For `n ≥ 1`, `3n + 5 ≤ 8n`, so `c = 8`, `n₀ = 1`. |
| **Is `n = O(n²)`?** | Yes — correct but loose; the tight bound is `O(n)`. |
| **Prove `n² ≠ O(n)`.** | It would force `n ≤ c` for all large `n`, impossible for fixed `c`. |
| **`O(f) + O(g)` = ?** | `O(max(f, g))` — sequential work is dominated by the larger term. |
| **Does Big-O mean worst case?** | No — it's an upper bound on whichever function you're bounding. |

---

## 9. The Mental Model

```text
f(n) = O(g(n))
        │
        ↓
"For large enough n, f never rises above some constant multiple of g."
        │
        ↓
   A PROMISE: it won't get worse than this.
```
