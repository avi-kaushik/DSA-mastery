# 007 — Theta Notation (Tight Bound)

> **One-line takeaway:** `f(n) = Θ(g(n))` means f is sandwiched between two constant multiples of g — **the exact growth rate**. It is the most precise of the three, and the one you should quote when you can.

---

## 1. Definition

> **`f(n) = Θ(g(n))` if there exist positive constants `c₁`, `c₂` and `n₀` such that
> `c₁·g(n) ≤ f(n) ≤ c₂·g(n)` for all `n ≥ n₀`.**

```text
∃ c₁, c₂ > 0, ∃ n₀ > 0 :  c₁·g(n) ≤ f(n) ≤ c₂·g(n)   ∀ n ≥ n₀
```

```text
 cost
   ▲          c₂·g(n)   ← ceiling
   │         ╱
   │        ╱ ╱ f(n)    ← trapped in the band
   │       ╱ ╱╱
   │      ╱ ╱╱  c₁·g(n) ← floor
   │     ╱╱╱
   │   ╱╱┆
   └─────┆──────────────▶ n
         n₀
```

`f` is squeezed inside the band. It can't escape above or below — it **is** the same shape
as `g`.

---

## 2. The Key Identity

```text
Θ(g)  =  O(g)  ∩  Ω(g)
```

> **`f = Θ(g)` if and only if `f = O(g)` AND `f = Ω(g)`.**

This is the practical way to prove Θ — prove both halves:

```text
3n + 5 ≤ 8n   for n ≥ 1     ⟹  O(n)   ✓
3n + 5 ≥ 3n   for n ≥ 1     ⟹  Ω(n)   ✓
                            ⟹  Θ(n)   ✓
```

Combined statement: `c₁ = 3`, `c₂ = 8`, `n₀ = 1`.

---

## 3. 🧮 Worked Proof

**Claim:** `½n² − 3n = Θ(n²)`

**Upper bound:** for `n ≥ 1`,

```text
½n² − 3n ≤ ½n²        (subtracting 3n only makes it smaller)
⟹ c₂ = ½
```

**Lower bound:** we need `½n² − 3n ≥ c₁n²`. Try `c₁ = ¼`:

```text
½n² − 3n ≥ ¼n²
⟺ ¼n²    ≥ 3n
⟺ n      ≥ 12
⟹ c₁ = ¼, n₀ = 12
```

So with `c₁ = ¼`, `c₂ = ½`, `n₀ = 12`:

```text
¼n² ≤ ½n² − 3n ≤ ½n²    for all n ≥ 12     ✓
```

Therefore `½n² − 3n = Θ(n²)`. ∎

> Notice `n₀ = 12` was **necessary** here — the lower bound is false for small `n`
> (at `n = 4`, `½n² − 3n = −4`). This is exactly what `n₀` is for.

---

## 4. Θ Is Not Always Available

You can only state Θ when the upper and lower bounds **match**. When an algorithm's cost
depends on the input, there may be no single Θ for the algorithm overall.

```text
Insertion sort:
    best case  = Θ(n)
    worst case = Θ(n²)

    "Insertion sort is Θ(n²)"      ✗  false as a blanket statement
    "Its worst case is Θ(n²)"      ✓
    "Insertion sort is O(n²)"      ✓  (upper bound holds always)
    "Insertion sort is Ω(n)"       ✓  (lower bound holds always)
```

Whereas:

```text
Merge sort:
    best = average = worst = Θ(n log n)
    ⟹ "Merge sort is Θ(n log n)"   ✓  no qualifier needed
```

> **Rule:** Θ applies to a *specific case* unless all cases coincide.

---

## 5. Useful Θ Facts

```text
Polynomials    aₖnᵏ + … + a₁n + a₀   = Θ(nᵏ)     (aₖ > 0)
Log base       Θ(log₂ n) = Θ(log₁₀ n) = Θ(log n)
Reflexive      f = Θ(f)
Symmetric      f = Θ(g)  ⟺  g = Θ(f)
Transitive     f = Θ(g), g = Θ(h)  ⟹  f = Θ(h)
Limit test     lim f/g = c, 0 < c < ∞   ⟹  f = Θ(g)
```

Θ is an **equivalence relation** — it partitions functions into growth classes. O and Ω are
not symmetric, so they aren't.

---

## 6. O vs Θ vs Ω — Side by Side

For `f(n) = 3n + 5`:

| Statement | True? | Why |
|---|---|---|
| `f = O(n)` | ✓ | tight upper bound |
| `f = O(n²)` | ✓ | correct, loose |
| `f = Ω(n)` | ✓ | tight lower bound |
| `f = Ω(1)` | ✓ | correct, loose |
| `f = Θ(n)` | ✓ | both bounds match |
| `f = Θ(n²)` | ✗ | no valid `c₁` — `3n+5` never reaches `c₁n²` |
| `f = Θ(1)` | ✗ | no valid `c₂` — it grows |

> Θ is the strictest claim: **the only one that can be wrong in both directions.**

---

## 7. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **Define Theta.** | `f = Θ(g)` if ∃ `c₁, c₂, n₀ > 0` with `c₁g(n) ≤ f(n) ≤ c₂g(n)` for all `n ≥ n₀`. |
| **Relation to O and Ω?** | `Θ = O ∩ Ω` — prove both bounds and you have Θ. |
| **When can't you state Θ?** | When best and worst case differ, e.g. insertion sort — then Θ applies per case. |
| **Is `3n + 5 = Θ(n²)`?** | No — the lower bound fails; it's `O(n²)` but not `Ω(n²)`. |
| **Θ of a polynomial?** | `Θ(nᵏ)` where `k` is the highest degree. |
| **Why is Θ an equivalence relation?** | It's reflexive, symmetric and transitive — it groups functions of identical growth. |
| **Why do people say O when they mean Θ?** | Convention/laziness; O is a safer claim, and worst-case Θ usually equals the quoted O. |

---

## 8. The Mental Model

```text
        c₂·g(n)   ─────────  can't go above
                     f(n)
        c₁·g(n)   ─────────  can't go below
                     │
                     ↓
        f and g are THE SAME SHAPE
```
