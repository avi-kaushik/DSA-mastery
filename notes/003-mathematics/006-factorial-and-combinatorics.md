# 006 — Factorials & Combinatorics

> **One-line takeaway:** `n!` overflows almost immediately, so the interesting questions are never "compute it" but "count something with it" — trailing zeros, `nCr`, arrangements. The recurring trick is **counting prime factors instead of computing the number**.

---

## 1. Factorial — Both Forms

📄 [`Factorial.java`](../../programs/java/mathematics/Factorial.java)

```java
// iterative — preferred
int fact(int n) {
    int r = 1;
    for (int i = 2; i <= n; i++) r *= i;
    return r;
}

// recursive
int fact(int n) {
    return (n <= 1) ? 1 : n * fact(n - 1);
}
```

| Version | Time | Space |
|---|---|---|
| Iterative | `O(n)` | `O(1)` ✓ |
| Recursive | `O(n)` | `O(n)` — call stack |

> Same time, different space. The recursion holds `n` stack frames — see
> [Space Complexity](../002-analysis-of-algorithms/015-space-complexity.md).

### ⚠️ How fast it overflows

```text
12!  = 479,001,600           ← largest that fits in int
13!  overflows int
20!  = 2.4 × 10¹⁸            ← largest that fits in long
21!  overflows long
```

> **So any problem with `n > 20` is not asking you to compute `n!`.** It wants a count, a
> modular value, or a property. Recognising that is half the answer.

---

## 2. Trailing Zeros in n! — the Classic

📄 [`Factorial.java`](../../programs/java/mathematics/Factorial.java)

**Question:** how many zeros does `n!` end with — without computing `n!`?

### The reasoning

```text
A trailing zero comes from a factor of 10 = 2 × 5.
So: zeros = number of (2, 5) pairs in the prime factorization of n!
```

Multiples of 2 are far more common than multiples of 5, so **5s are the bottleneck**:

```text
zeros = count of factor 5 in n!
```

But `25 = 5²` contributes two 5s, `125 = 5³` contributes three, and so on. Hence
**Legendre's formula**:

```text
zeros = ⌊n/5⌋ + ⌊n/25⌋ + ⌊n/125⌋ + ⌊n/625⌋ + ...
```

```java
int trailingZeros(int n) {
    int zeros = 0;
    for (int i = 5; i <= n; i *= 5) zeros += n / i;
    return zeros;
}
```

### Worked: n = 100

```text
⌊100/5⌋   = 20
⌊100/25⌋  =  4
⌊100/125⌋ =  0   stop
──────────────────
zeros     = 24
```

```text
Time  = O(log₅ n)      ← i multiplies by 5 each step
Space = O(1)
```

> ⚠️ `i *= 5` can overflow `int` when `i` grows past 4×10⁸. Use `long i`, or loop on
> `n / i` instead.

**Generalisation** — the exponent of any prime `p` in `n!` is `Σ ⌊n/pᵏ⌋`. That's the same
formula, and it's how you compute `nCr mod p` for small primes.

---

## 3. Permutations & Combinations

```text
Permutations (order matters)     nPr = n! / (n−r)!
Combinations (order doesn't)     nCr = n! / (r! (n−r)!)
```

```text
nPr = number of ways to ARRANGE r of n
nCr = number of ways to CHOOSE  r of n
```

### Essential identities

```text
nC0 = nCn = 1
nCr = nC(n−r)                       symmetry — compute the smaller side
nCr = (n−1)C(r−1) + (n−1)C(r)       Pascal's rule → the DP recurrence
Σ nCr for r = 0..n = 2ⁿ             total subsets
nCr = nPr / r!
```

### Computing nCr without overflow

```java
long nCr(int n, int r) {
    if (r > n - r) r = n - r;             // symmetry: fewer iterations
    long res = 1;
    for (int i = 0; i < r; i++) {
        res = res * (n - i) / (i + 1);    // exact at every step
    }
    return res;
}
```

```text
Time = O(r),  Space = O(1)
```

> **Why the interleaved division is safe:** after `i+1` iterations the accumulated value is
> `C(n, i+1)`, always an integer — the product of any `k` consecutive integers is divisible
> by `k!`. Computing `n!` and `r!` separately would overflow far sooner.

### Pascal's triangle (DP)

```java
int[][] c = new int[n + 1][n + 1];
for (int i = 0; i <= n; i++) {
    c[i][0] = 1;
    for (int j = 1; j <= i; j++)
        c[i][j] = c[i-1][j-1] + c[i-1][j];
}
```

```text
Time = O(n²),  Space = O(n²)   → O(n) with a rolling 1D array
```

Use it when you need **many** `nCr` values; use the loop above for a single one.

### nCr mod p (prime)

```text
nCr mod p = n! · (r!)⁻¹ · ((n−r)!)⁻¹   mod p
```

Precompute factorials and their inverses once:

```text
Precompute: O(n)      Each query: O(1)
```

Inverses come from Fermat — see [Modular Arithmetic](005-modular-arithmetic.md).

---

## 4. Counting Formulas Worth Remembering

| Situation | Count |
|---|---|
| Arrange `n` distinct items | `n!` |
| Arrange `r` of `n` | `n!/(n−r)!` |
| Choose `r` of `n` | `n!/(r!(n−r)!)` |
| Subsets of an `n`-set | `2ⁿ` |
| Binary strings of length `n` | `2ⁿ` |
| Arrangements with repeats (`AABB`) | `n!/(a! b! …)` |
| Choose `r` from `n` **with** repetition | `(n+r−1)C r` |
| Ways to pair `2n` people | `(2n)! / (2ⁿ · n!)` |
| Balanced bracket sequences / BST shapes | Catalan `Cₙ = (2n)C n /(n+1)` |

```text
Catalan: 1, 1, 2, 5, 14, 42, 132, ...
```

> **Catalan numbers** answer a surprising number of interview questions: valid parentheses,
> distinct BSTs with `n` nodes, ways to triangulate a polygon, monotonic lattice paths.

---

## 5. Complexity Summary

| Task | Time | Space |
|---|---|---|
| `n!` iterative | `O(n)` | `O(1)` |
| `n!` recursive | `O(n)` | `O(n)` |
| Trailing zeros of `n!` | `O(log₅ n)` | `O(1)` |
| Exponent of prime `p` in `n!` | `O(log_p n)` | `O(1)` |
| Single `nCr` (multiplicative) | `O(r)` | `O(1)` |
| All `nCr` (Pascal) | `O(n²)` | `O(n²)` or `O(n)` |
| `nCr mod p` after precompute | `O(1)` | `O(n)` |

---

## 6. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **Largest factorial fitting in int / long?** | `12!` in `int`, `20!` in `long`. |
| **Trailing zeros in `n!`?** | `⌊n/5⌋ + ⌊n/25⌋ + ⌊n/125⌋ + …` — `O(log₅ n)`. |
| **Why count 5s and not 2s?** | A zero needs a 2×5 pair, and 2s are far more plentiful, so 5s are the limiting factor. |
| **Why the `⌊n/25⌋` term?** | 25 contributes *two* 5s, 125 three, and so on — each higher power adds one more. |
| **Trailing zeros of 100!?** | `20 + 4 = 24`. |
| **nPr vs nCr?** | `nPr` counts arrangements (order matters); `nCr` counts selections (it doesn't). |
| **Pascal's rule?** | `nCr = (n−1)C(r−1) + (n−1)Cr` — the DP recurrence. |
| **Compute `nCr` without overflow?** | Multiply and divide alternately: `res = res*(n−i)/(i+1)`, using `nCr = nC(n−r)` to shorten the loop. |
| **Why is that division always exact?** | The partial product is `C(n, i+1)`, an integer — any `k` consecutive integers are divisible by `k!`. |
| **`nCr mod p` for many queries?** | Precompute factorials and inverse factorials in `O(n)`, answer each in `O(1)`. |
| **Sum of all `nCr`?** | `2ⁿ` — the number of subsets. |
| **Where do Catalan numbers appear?** | Valid parentheses, distinct BSTs, polygon triangulations: `Cₙ = (2n)Cn/(n+1)`. |

---

## 7. The Mental Model

```text
        A factorial appears in the problem
                       │
                       ↓
              Is n larger than 20?
             ╱                    ╲
           no                     yes
            │                       │
     just compute it       DON'T compute n!
     (iterative, O(1) space)        │
                          ┌─────────┴─────────┐
                          ↓                   ↓
                  count prime factors    work mod 10⁹+7
                  (trailing zeros,       (factorials +
                   Legendre)              inverse factorials)
```
