# 006 — Omega Notation (Lower Bound)

> **One-line takeaway:** `f(n) = Ω(g(n))` means **f grows at least as fast as g** — a *floor*. Its real power is proving that **no algorithm can do better** than a certain cost.

---

## 1. Definition

> **`f(n) = Ω(g(n))` if there exist positive constants `c` and `n₀` such that
> `0 ≤ c·g(n) ≤ f(n)` for all `n ≥ n₀`.**

```text
∃ c > 0, ∃ n₀ > 0 :  0 ≤ c·g(n) ≤ f(n)   ∀ n ≥ n₀
```

```text
 cost
   ▲
   │             f(n)
   │            ╱
   │          ╱ ╱
   │        ╱ ╱
   │      ╱ ╱   c·g(n)  ← the floor
   │    ╱ ╱
   │  ╱  ┆
   └─────┆──────────────▶ n
         n₀
```

It is the exact mirror image of Big-O — the inequality simply flips.

```text
f = O(g)  ⟺  g = Ω(f)
```

---

## 2. 🧮 Worked Proof

**Claim:** `3n + 5 = Ω(n)`

Find `c`, `n₀` with `c·n ≤ 3n + 5`.

```text
For all n ≥ 1:   3n ≤ 3n + 5
⟹  c = 3,  n₀ = 1     ✓
```

Therefore `3n + 5 = Ω(n)`. ∎

**Second example:** `n² + n = Ω(n²)`

```text
For n ≥ 1:   n² ≤ n² + n
⟹  c = 1,  n₀ = 1
```

Note `n² + n` is also `Ω(n)` and `Ω(log n)` — like O, **Ω can be loose**, just in the other
direction. The tightest lower bound here is `Ω(n²)`.

---

## 3. Loose in the Opposite Direction

```text
n²  = Ω(n²)     ✓ tight
n²  = Ω(n)      ✓ loose
n²  = Ω(log n)  ✓ very loose
n²  = Ω(1)      ✓ trivially true for anything
```

> Every function is `Ω(1)`. A lower bound is only interesting when it's **tight**.

---

## 4. The Real Use: Lower Bounds on *Problems*

This is where Ω earns its place. It answers a question O cannot:

> **"What is the minimum work ANY correct algorithm must do?"**

Classic results:

```text
Comparison-based sorting  → Ω(n log n)
        (a decision tree with n! leaves has depth ≥ log₂(n!) = Ω(n log n))

Searching an unsorted array → Ω(n)
        (you must look at every element; skipping one could skip the answer)

Reading the input at all    → Ω(n)
```

Meaning: merge sort at `Θ(n log n)` is **provably optimal** among comparison sorts. No
cleverness will beat it. That's a statement about the *problem*, not an algorithm.

> **O bounds an algorithm. Ω bounds a problem.** That framing answers most Ω questions.

---

## 5. 🧮 Why Comparison Sorting Is Ω(n log n)

Worth knowing the sketch — it's a favourite follow-up:

```text
n elements have n! possible orderings
Each comparison has 2 outcomes → a binary decision tree
A binary tree of height h has ≤ 2ʰ leaves
We need at least n! leaves to distinguish all orderings

  2ʰ ≥ n!
  h  ≥ log₂(n!)
```

And by Stirling's approximation:

```text
log₂(n!) = Θ(n log n)
```

Therefore any comparison sort needs `Ω(n log n)` comparisons in the worst case. ∎

(Counting sort / radix sort beat this only because they **don't compare** elements.)

---

## 6. ⚠️ Common Mistakes

| ✗ Wrong | ✓ Right |
|---|---|
| "Ω means best case" | Ω is a lower bound on a function; best case is a choice of input |
| "This runs in at least `O(n)`" | Meaningless mixing — say "at least `Ω(n)`" or "at most `O(n)`" |
| "Linear search is `Ω(n)`" | Its *worst case* is `Ω(n)`; its best case is `Ω(1)` |
| Quoting `Ω(1)` as informative | Everything is `Ω(1)`; give a tight bound |

---

## 7. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **Define Omega.** | `f = Ω(g)` if ∃ `c, n₀ > 0` with `0 ≤ c·g(n) ≤ f(n)` for all `n ≥ n₀`. |
| **Relation to Big-O?** | Exact mirror: `f = O(g)` ⟺ `g = Ω(f)`. |
| **Prove `3n + 5 = Ω(n)`.** | `3n ≤ 3n + 5` for `n ≥ 1`, so `c = 3`, `n₀ = 1`. |
| **Does Ω mean best case?** | No. Ω bounds a function from below; you can state Ω of the worst case too. |
| **Where is Ω actually useful?** | Proving lower bounds for problems — e.g. comparison sorting is `Ω(n log n)`. |
| **Why is comparison sorting `Ω(n log n)`?** | The decision tree needs `n!` leaves, so its height is `≥ log₂(n!) = Θ(n log n)`. |
| **How do counting/radix sort beat it?** | They don't compare elements, so the decision-tree bound doesn't apply. |

---

## 8. The Mental Model

```text
f(n) = Ω(g(n))
        │
        ↓
"For large enough n, f never drops below some constant multiple of g."
        │
        ↓
  A FLOOR: it can't get better than this.
```
