# 004 — Asymptotic Notations (Overview)

> **One-line takeaway:** Asymptotic notations are the vocabulary for bounding a cost function. **O** = at most, **Ω** = at least, **Θ** = exactly (up to constants). Everything else is detail.

---

## 1. Why Notations at All?

After analysis you have a messy function:

```text
T(n) = 3n² + 17n + 250
```

Nobody wants to carry that around. Asymptotic notation is the standard way to say
*"this behaves like n², and I don't care about the rest."*

> **Asymptotic notation classifies functions by their growth rate, ignoring constant
> factors and small inputs.**

---

## 2. The Three Notations at a Glance

| Notation | Name | Meaning | Read as | Analogy |
|---|---|---|---|---|
| `O(g)` | Big-O | **Upper** bound | "grows no faster than" | `≤` |
| `Ω(g)` | Big-Omega | **Lower** bound | "grows at least as fast as" | `≥` |
| `Θ(g)` | Big-Theta | **Tight** bound | "grows exactly like" | `=` |

```text
                 c·g(n)          ← O : ceiling
                ╱
       f(n) ───╱──────────       ← the actual cost
              ╱
         c'·g(n)                 ← Ω : floor

  Θ  =  both a ceiling and a floor of the same shape
```

All three only claim anything for `n ≥ n₀` — **small inputs are explicitly excluded.**

---

## 3. The Shared Formal Template

Every definition has the same three ingredients: **constants**, a **threshold `n₀`**, and
an **inequality**.

```text
f(n) = O(g(n))   ⟺  ∃ c > 0, n₀ > 0 :  0 ≤ f(n) ≤ c·g(n)            ∀ n ≥ n₀
f(n) = Ω(g(n))   ⟺  ∃ c > 0, n₀ > 0 :  0 ≤ c·g(n) ≤ f(n)            ∀ n ≥ n₀
f(n) = Θ(g(n))   ⟺  ∃ c₁,c₂ > 0, n₀ :  c₁·g(n) ≤ f(n) ≤ c₂·g(n)     ∀ n ≥ n₀
```

Read `∃` as "there exist" and `∀ n ≥ n₀` as "for all sufficiently large n".

> The whole meaning of "asymptotic" lives in `n ≥ n₀`: **we only care about large inputs.**

---

## 4. How They Relate

```text
Θ(g)  =  O(g)  ∩  Ω(g)
```

> **A function is Θ(g) if and only if it is both O(g) and Ω(g).**

This is the single most useful fact about the three:

```text
3n + 5  is  O(n)   ✓
3n + 5  is  Ω(n)   ✓
   ⟹    3n + 5  is  Θ(n)   ✓
```

And the ordering of the sets:

```text
   f = Θ(g)  ⟹  f = O(g)   and   f = Ω(g)
   f = O(g)  ⇏  f = Θ(g)              (the bound may be loose)
```

---

## 5. ⚠️ The `=` Is a Lie (Notation Abuse)

`f(n) = O(g(n))` is **not** an equation. `O(g)` is a *set of functions*, and the honest
notation is:

```text
f(n) ∈ O(g(n))
```

Consequences:

```text
n = O(n²)  ✓        but      O(n²) = n   ✗   (meaningless — not symmetric)
```

Everyone still writes `=`. Just know it means "belongs to".

---

## 6. Tight vs Loose Bounds

A bound can be **correct but useless**:

```text
n      = O(n)      ✓ tight
n      = O(n²)     ✓ correct, loose
n      = O(2ⁿ)     ✓ correct, absurdly loose
```

All three are true statements. Convention:

> **Always state the tightest bound you can prove.** Saying binary search is `O(n)` is not
> wrong, but in an interview it reads as not knowing it's `O(log n)`.

---

## 7. Little-o and Little-omega (Strict Bounds)

Occasionally asked; two more notations for **strict** growth comparison.

| Notation | Meaning | Analogy | Limit test |
|---|---|---|---|
| `o(g)` | Strictly slower — bound is **not** tight | `<` | `lim f/g = 0` |
| `ω(g)` | Strictly faster | `>` | `lim f/g = ∞` |

```text
n     = o(n²)   ✓     (n grows strictly slower than n²)
n²    = o(n²)   ✗     (same order — it's Θ, not o)
n²    = ω(n)    ✓
```

Full picture:

```text
o(g)  <  O(g)  ≤  Θ(g)  ≤  Ω(g)  <  ω(g)
 <         ≤        =        ≥        >
```

---

## 8. 🧮 The Limit Shortcut

Instead of hunting for `c` and `n₀`, use limits:

```text
        f(n)
  lim  ──────  =   0    →  f = O(g)  and  f = o(g)
 n→∞    g(n)       c>0  →  f = Θ(g)
                   ∞    →  f = Ω(g)  and  f = ω(g)
```

Example:

```text
f(n) = 3n² + 5n,  g(n) = n²
lim (3n² + 5n) / n² = 3   (a positive constant)
⟹  f = Θ(n²)
```

This is usually the fastest way to answer "is `f` O/Θ/Ω of `g`?" in an interview.

---

## 9. Which One Do People Actually Use?

```text
In theory  → Θ is the precise statement
In practice→ everyone says "Big-O" and usually means Θ of the worst case
```

That's acceptable in conversation, but knowing the difference is exactly what separates a
memorised answer from an understood one.

```text
"Merge sort is O(n log n)"       ← what people say
"Merge sort is Θ(n log n)"       ← what is actually true (all cases)
"Comparison sorting is Ω(n log n)" ← a lower bound on the PROBLEM, not an algorithm
```

That last line is the real use of Ω: proving **no algorithm can do better**.

---

## 10. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What are asymptotic notations?** | A vocabulary for bounding a function's growth rate, ignoring constants and small inputs. |
| **O vs Ω vs Θ?** | O = upper bound (≤), Ω = lower bound (≥), Θ = tight bound (both). |
| **Relation between them?** | `Θ(g) = O(g) ∩ Ω(g)` — Θ holds exactly when both O and Ω hold. |
| **What is `n₀` for?** | It restricts the claim to large inputs; small-`n` behaviour is deliberately ignored. |
| **Is `n = O(n²)` valid?** | Yes — correct but loose. Always quote the tightest bound you can prove. |
| **Why is `f = O(g)` notation abuse?** | `O(g)` is a set; the correct reading is `f ∈ O(g)`, so it isn't symmetric. |
| **little-o vs Big-O?** | `o` is strict (`f/g → 0`, bound never tight); `O` allows equality of order. |
| **Where is Ω genuinely used?** | Lower bounds on problems, e.g. comparison-based sorting is `Ω(n log n)`. |
| **Fastest way to classify f vs g?** | The limit `f/g`: 0 → O/o, positive constant → Θ, ∞ → Ω/ω. |

---

## 11. The Mental Model

```text
                    f(n)
                      │
      ┌───────────────┼───────────────┐
      ↓               ↓               ↓
  at most         exactly         at least
   O(g)            Θ(g)            Ω(g)
     ≤               =               ≥
      └──────── Θ = O ∩ Ω ──────────┘
```
