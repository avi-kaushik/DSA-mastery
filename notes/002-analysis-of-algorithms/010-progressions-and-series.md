# 010 — Progressions & Series for Algorithm Analysis

> **One-line takeaway:** Analysing a loop or a recursion tree always ends in a **sum**. Four series — arithmetic, geometric, harmonic and arithmetico-geometric — cover almost every sum you will ever meet, and the single most useful fact is that a **geometric series is dominated by its largest term**.

---

## 1. Why Series Matter

```text
Loop           →  Σ (cost per iteration)
Recursion tree →  Σ (cost per level)
```

You cannot finish either analysis without collapsing a sum into a closed form. That is the
whole job of this page.

---

## 2. Arithmetic Progression (AP)

**Constant difference** between consecutive terms.

```text
a, a+d, a+2d, a+3d, ...
```

| Quantity | Formula |
|---|---|
| nth term | `aₙ = a + (n−1)d` |
| Sum of n terms | `Sₙ = n/2 · [2a + (n−1)d]` = `n/2 · (first + last)` |

### The two cases you'll actually use

```text
1 + 2 + 3 + ... + n         = n(n+1)/2        = Θ(n²)
1 + 3 + 5 + ... + (2n−1)    = n²              = Θ(n²)
```

### Where it appears

```java
for (int i = 0; i < n; i++)
    for (int j = i; j < n; j++)   // n + (n−1) + ... + 1
        { ... }
```

```text
Σ = n(n+1)/2 = Θ(n²)
```

> **Signal:** the inner loop's length shrinks (or grows) by a **constant** each pass → AP → `Θ(n²)`.

---

## 3. Geometric Progression (GP) — the important one

**Constant ratio** between consecutive terms.

```text
a, ar, ar², ar³, ...
```

| Quantity | Formula |
|---|---|
| nth term | `aₙ = a·rⁿ⁻¹` |
| Sum of n terms (`r ≠ 1`) | `Sₙ = a(rⁿ − 1)/(r − 1)` |
| Sum of n terms (`r = 1`) | `Sₙ = a·n` |
| Infinite sum (`\|r\| < 1`) | `S∞ = a/(1 − r)` |

### The infinite sum — `a/(1 − r)`

When the ratio is less than 1, the terms shrink fast enough that even an **endless** GP
adds up to a finite value:

```text
a + ar + ar² + ar³ + ...  =  a / (1 − r)        (only when |r| < 1)
```

```text
n + n/2 + n/4 + ...      →  a = n, r = ½    →  n / (1 − ½)  =  2n
n + 3n/4 + 9n/16 + ...   →  a = n, r = ¾    →  n / (1 − ¾)  =  4n
1 + ⅓ + ⅑ + ...          →  a = 1, r = ⅓    →  1 / (1 − ⅓)  =  1.5
```

⚠️ For `r ≥ 1` there is no finite sum — use the `Sₙ` formula above and take the last term.

> **Why this one matters most:** a shrinking recursion cost has exactly this shape, so the
> whole series collapses to a single division instead of a level-by-level count.

### 🔑 The three regimes — memorise this

```text
r > 1   (growing)   → the sum is dominated by the LAST term
                      1 + 2 + 4 + ... + 2ᵏ = 2ᵏ⁺¹ − 1  ≈  2 × (last term)
                      ⟹ Θ(last term)

r = 1   (flat)      → the sum is (number of terms) × (term)
                      c + c + ... (k times) = ck
                      ⟹ Θ(k · term)

r < 1   (shrinking) → the sum is dominated by the FIRST term
                      n + n/2 + n/4 + ... < 2n
                      ⟹ Θ(first term)
```

```text
   r > 1                r = 1                r < 1
   ▁▂▃▅█                █████                █▅▃▂▁
   last term wins       all equal            first term wins
```

> **This single table decides most recursion trees.** Whether the per-level cost grows,
> stays flat, or shrinks tells you the answer immediately.

### Why "shrinking sums are bounded" is so useful

```text
n + n/2 + n/4 + n/8 + ...  =  n(1 + ½ + ¼ + ...)  =  n · (1/(1−½))  =  2n  =  Θ(n)
```

Even with **infinitely many terms**, the total is only twice the first term.

### Where GP appears

```text
Recursion tree level costs           → 2ᵏ nodes at level k
Dynamic array doubling               → 1 + 2 + 4 + ... + n = 2n − 1 = Θ(n) total
Nodes in a binary tree of height h   → 2⁰ + 2¹ + ... + 2ʰ = 2ʰ⁺¹ − 1
Loop with i *= 2                     → the count of terms is log n
```

### 🧮 Worked: why appending to a dynamic array is O(1) amortised

Resizing doubles capacity, copying all elements each time:

```text
Copies = 1 + 2 + 4 + ... + n/2 + n  =  2n − 1  =  Θ(n)
```

`Θ(n)` copies spread over `n` appends → **`Θ(1)` per append on average**. That result is
pure GP.

---

## 4. Harmonic Series

```text
Hₙ = 1 + 1/2 + 1/3 + ... + 1/n
```

```text
Hₙ ≈ ln n + γ      (γ ≈ 0.577, Euler–Mascheroni)
Hₙ = Θ(log n)
```

⚠️ It looks like it should converge — it does **not**. It diverges, just very slowly.

### Where it appears

```java
for (int i = 1; i <= n; i++)
    for (int j = i; j <= n; j += i)     // inner runs n/i times
        { ... }
```

```text
n/1 + n/2 + n/3 + ... + n/n  =  n·Hₙ  =  Θ(n log n)
```

That is the Sieve of Eratosthenes' structure (which tightens to `Θ(n log log n)` because it
only iterates over primes).

---

## 5. Arithmetico-Geometric Series

Terms of the form `i · rⁱ`. Shows up in recursion trees where the level cost is
`(depth) × (shrinking factor)` — most famously in **heap construction**.

```text
Σ i·2ⁱ    (i = 1..n)   =  (n − 1)·2ⁿ⁺¹ + 2      = Θ(n·2ⁿ)
Σ i/2ⁱ    (i = 1..∞)   =  2                     = Θ(1)   ← converges!
Σ i/2ⁱ    (i = 1..n)   <  2
```

### 🧮 Worked: why build-heap is O(n), not O(n log n)

A heap of `n` nodes has about `n/2^(h+1)` nodes at height `h`, each costing `O(h)` to sift down:

```text
Total = Σ (n / 2^(h+1)) · h     for h = 0 .. log n
      = (n/2) · Σ h/2^h
      < (n/2) · 2
      = n
      = Θ(n)
```

The convergent series `Σ h/2ʰ = 2` is what collapses the expected `n log n` down to `n`.

> **Lesson:** most nodes are near the bottom where the work is cheap. The maths captures
> that exactly.

---

## 6. Telescoping Series

When consecutive terms cancel:

```text
Σ (aᵢ − aᵢ₋₁)  =  aₙ − a₀
```

This is precisely what the **iteration method** for recurrences does:

```text
T(n)   − T(n−1) = n
T(n−1) − T(n−2) = n−1
...
T(1)   − T(0)   = 1
──────────────────────  add all
T(n)   − T(0)   = 1 + 2 + ... + n = n(n+1)/2
```

> **Telescoping turns a recurrence into a plain sum.** Keep it in mind for note 012.

---

## 7. Sums of Logs

```text
log 1 + log 2 + ... + log n  =  log(n!)  =  Θ(n log n)
```

(By Stirling's approximation.) This appears whenever `n` operations each cost `O(log n)` —
e.g. inserting `n` items into a balanced BST or a heap.

---

## 8. Master Reference Table

| Series | Closed form | Order |
|---|---|---|
| Constant | `c·n` | `Θ(n)` |
| Arithmetic `1+2+…+n` | `n(n+1)/2` | `Θ(n²)` |
| Odd numbers | `n²` | `Θ(n²)` |
| Squares `1²+…+n²` | `n(n+1)(2n+1)/6` | `Θ(n³)` |
| Cubes `1³+…+n³` | `[n(n+1)/2]²` | `Θ(n⁴)` |
| Geometric `r>1` | `a(rⁿ−1)/(r−1)` | `Θ(last term)` |
| Powers of 2 | `2ⁿ⁺¹−1` | `Θ(2ⁿ)` |
| Geometric `r<1` | `< a/(1−r)` | `Θ(first term)` |
| Halving `n+n/2+n/4+…` | `< 2n` | `Θ(n)` |
| Harmonic `Hₙ` | `≈ ln n` | `Θ(log n)` |
| `n·Hₙ` | `≈ n ln n` | `Θ(n log n)` |
| `Σ i·2ⁱ` | `(n−1)2ⁿ⁺¹+2` | `Θ(n·2ⁿ)` |
| `Σ i/2ⁱ` | `→ 2` | `Θ(1)` |
| `Σ log i` | `log(n!)` | `Θ(n log n)` |

---

## 9. How to Recognise Which Series You Have

```text
Terms differ by a CONSTANT      → Arithmetic  → Θ(n × largest) → usually Θ(n²)
Terms differ by a RATIO         → Geometric   → Θ(largest term)
Terms are n/1, n/2, n/3, ...    → Harmonic    → Θ(n log n)
Terms are i × rⁱ                → Arith-Geo   → converges if r < 1
Consecutive terms cancel        → Telescoping → aₙ − a₀
```

---

## 10. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **Sum of an AP?** | `n/2 · (first + last)`; `1+2+…+n = n(n+1)/2 = Θ(n²)`. |
| **Sum of a GP?** | `a(rⁿ−1)/(r−1)`; infinite with `\|r\|<1` is `a/(1−r)`. |
| **The key GP insight?** | `r>1` → dominated by the last term; `r<1` → dominated by the first term; `r=1` → terms × count. |
| **Why is `n + n/2 + n/4 + …` only `Θ(n)`?** | It's a shrinking GP summing to `2n` even with infinite terms. |
| **Why is dynamic-array append `O(1)` amortised?** | Doubling copies `1+2+…+n = 2n−1 = Θ(n)` total, spread over `n` appends. |
| **Why is build-heap `O(n)`?** | `Σ (n/2^(h+1))·h = (n/2)·Σ h/2^h < n`, since `Σ h/2^h` converges to 2. |
| **What is the harmonic sum's order?** | `Hₙ ≈ ln n = Θ(log n)`; it diverges, slowly. |
| **Where does `n·Hₙ` come from?** | Inner loops of length `n/i`, e.g. the sieve — gives `Θ(n log n)`. |
| **What is telescoping?** | Consecutive terms cancel so `Σ(aᵢ − aᵢ₋₁) = aₙ − a₀`; it's how the iteration method solves recurrences. |

---

## 11. The Mental Model

```text
              A sum shows up
                    │
       ┌────────────┼────────────┐
       ↓            ↓            ↓
   ratio r?     constant d?   terms n/i?
       │            │            │
    GEOMETRIC   ARITHMETIC    HARMONIC
       │            │            │
  r>1: last    Θ(n × max)     Θ(n log n)
  r=1: n×term
  r<1: first
```
