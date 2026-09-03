# 003 — Primes, Divisors & Factorization

> **One-line takeaway:** Divisors come in **pairs** `(i, n/i)`, so you only ever loop to `√n`. For *one* number use `O(√n)`; for *all* numbers up to `n` use the **Sieve of Eratosthenes** at `O(n log log n)`.

---

## 1. The √n Insight — the Foundation of This Whole Page

> **If `n = a × b`, then one of `a`, `b` must be ≤ `√n`.**

**Proof:** suppose both `a > √n` and `b > √n`. Then `a × b > √n × √n = n`, contradicting
`a × b = n`. ∎

```text
n = 36
1 × 36        ← pairs mirror around √36 = 6
2 × 18
3 × 12
4 × 9
6 × 6         ← the pivot
```

Every divisor above `√n` is just `n / (a divisor below √n)`. **Checking up to `√n` finds
everything.**

```text
Loop to n    → O(n)
Loop to √n   → O(√n)      for n = 10¹², that's 10⁶ instead of 10¹²
```

---

## 2. Primality Test — O(√n)

📄 [`PrimeNumber.java`](../../programs/java/mathematics/PrimeNumber.java)

```java
boolean isPrime(int n) {
    if (n < 2)      return false;
    if (n == 2)     return true;
    if (n % 2 == 0) return false;
    for (int i = 3; i * i <= n; i += 2)     // odds only
        if (n % i == 0) return false;
    return true;
}
```

```text
Time  = O(√n)      halved again by skipping evens
Space = O(1)
```

### Edge cases that catch people out

```text
n = 1  → NOT prime (only one divisor)
n = 2  → prime, and the ONLY even prime
n = 0, negatives → not prime
```

### The 6k ± 1 optimisation

Every prime > 3 is of the form `6k ± 1`:

```java
if (n <= 3) return n > 1;
if (n % 2 == 0 || n % 3 == 0) return false;
for (int i = 5; i * i <= n; i += 6)
    if (n % i == 0 || n % (i + 2) == 0) return false;
return true;
```

> Still `O(√n)`, but ~3× fewer iterations. Worth mentioning as a refinement.

---

## 3. Printing All Divisors — O(√n)

📄 [`FactorAndMultipleMathematics.java`](../../programs/java/mathematics/FactorAndMultipleMathematics.java)

The naive `for (i = 1; i <= n; i++)` is `O(n)`. Using the pairing:

```java
void printDivisors(int n) {
    int i;
    for (i = 1; i * i <= n; i++)             // ascending: small half
        if (n % i == 0) print(i);

    for (int j = i - 1; j >= 1; j--)         // descending: large half
        if (n % j == 0) print(n / j);
}
```

```text
Time = O(√n),  Space = O(1)     — and the output stays SORTED
```

### Why the second loop starts at `i − 1`

After the first loop, `i` has gone one step past `√n`. Starting the reverse pass at `i − 1`:

```text
• covers the largest small-divisor found, and
• prints a perfect square's root ONCE
  n = 36: first loop prints ... 6; if the second started at i it would print 36/6 = 6 again
```

> This off-by-one is the entire difficulty of the problem — be ready to explain it.

### 🧮 Divisor count from the prime factorization

```text
n = p₁^a₁ × p₂^a₂ × ... × p_k^a_k

Number of divisors  d(n) = (a₁+1)(a₂+1)...(a_k+1)
Sum of divisors     σ(n) = Π (pᵢ^(aᵢ+1) − 1)/(pᵢ − 1)
```

```text
36 = 2² × 3²  →  d(36) = (2+1)(2+1) = 9  ✓  {1,2,3,4,6,9,12,18,36}
```

> **A perfect square has an odd number of divisors** — the `√n` pairs with itself. That's the
> answer to the classic "bulb switching / locker" puzzle.

---

## 4. Prime Factorization — O(√n)

```java
void primeFactors(int n) {
    for (int i = 2; i * i <= n; i++)
        while (n % i == 0) { print(i); n /= i; }
    if (n > 1) print(n);          // leftover is prime
}
```

```text
60 → 2 2 3 5
```

### Three things to understand

**Why the inner `while`?** A factor can repeat: `8 = 2 × 2 × 2`.

**Why is the leftover prime?** Dividing out every factor ≤ `√n` leaves a number with no
factor ≤ its own square root — so if it is `> 1` it must itself be prime. This is what
handles a large prime input such as `n = 999983`.

**Why not check that `i` is prime?** By the time we reach `i`, every smaller prime has been
fully divided out, so a composite `i` can never divide `n`. Composite candidates are
harmless, which is why no primality test is needed inside the loop.

```text
Time  = O(√n),  Space = O(1)
```

---

## 5. All Primes ≤ n — the Naive Way

```java
for (int i = 2; i <= n; i++)
    if (isPrime(i)) print(i);
```

```text
Time = O(n √n)      ← too slow for n = 10⁶
```

---

## 6. Sieve of Eratosthenes — O(n log log n)

📄 [`PrimeNumber.java`](../../programs/java/mathematics/PrimeNumber.java)

> **Idea:** instead of testing each number, *mark the multiples* of each prime as composite.

```java
void sieve(int n) {
    boolean[] isPrime = new boolean[n + 1];
    Arrays.fill(isPrime, 2, n + 1, true);

    for (int i = 2; i * i <= n; i++)
        if (isPrime[i])
            for (int j = i * i; j <= n; j += i)   // start at i*i
                isPrime[j] = false;

    for (int i = 2; i <= n; i++)
        if (isPrime[i]) print(i);
}
```

### Trace for n = 30

```text
start   2  3  4  5  6  7  8  9 10 ... 30
p=2           x     x     x  x       (mark 4,6,8,10,...)
p=3                 x        x       (mark 9,15,21,27 — 6,12 already gone)
p=5                                  (mark 25)
left:   2  3  5  7 11 13 17 19 23 29
```

### The two optimisations, and why they are correct

```text
1. Outer loop stops at i*i <= n
   Any composite <= n has a factor <= sqrt(n), so it is already marked by then.

2. Inner loop starts at j = i*i, not 2i
   Every multiple k*i with k < i was already marked when the smaller prime k
   was processed.  (For i = 5: 10, 15, 20 were killed by 2 and 3.)
```

### 🧮 Why O(n log log n)

Total marking work is the sum, over primes `p ≤ n`, of `n/p`:

```text
n/2 + n/3 + n/5 + n/7 + n/11 + ...
= n · Σ (1/p)  over primes p ≤ n
```

By **Mertens' theorem**, `Σ 1/p ≈ ln ln n`. Therefore:

```text
Time  = O(n log log n)     ← very nearly linear
Space = O(n)               ← the boolean array
```

> Compare with `Σ 1/i ≈ ln n` (the harmonic sum). If we marked multiples of *every* number
> instead of only primes, we would get `O(n log n)`. Skipping composites is what buys the
> second log. See
> [Progressions & Series](../002-analysis-of-algorithms/010-progressions-and-series.md).

```text
Naive primes ≤ n : O(n√n)      ≈ 10⁹ ops at n = 10⁶
Sieve            : O(n lg lg n) ≈ 3 × 10⁶ ops
```

---

## 7. Sieve Variants Worth Knowing

### Smallest Prime Factor (SPF) sieve — factorize in O(log n)

```java
int[] spf = new int[n + 1];
for (int i = 2; i <= n; i++)
    if (spf[i] == 0)
        for (int j = i; j <= n; j += i)
            if (spf[j] == 0) spf[j] = i;

// then factorize any x <= n in O(log x):
while (x > 1) { print(spf[x]); x /= spf[x]; }
```

> **Use it when** you must factorize *many* numbers. Precompute once, then answer each query
> in `O(log x)` instead of `O(√x)`.

### Segmented sieve

For primes in `[L, R]` where `R` is huge (10¹²) but `R − L` is small: sieve up to `√R`, then
use those primes to mark the window. Space `O(√R + (R−L))`.

### Other facts worth having

```text
Primes below n     ≈ n / ln n            (Prime Number Theorem)
Twin primes        (3,5), (5,7), (11,13), ...
Miller-Rabin       probabilistic test, O(k log³ n) — for very large n
```

---

## 8. Complexity Summary

| Task | Approach | Time | Space |
|---|---|---|---|
| Is `n` prime? | Trial division to `√n` | `O(√n)` | `O(1)` |
| All divisors of `n` | Pairing to `√n` | `O(√n)` | `O(1)` |
| Count divisors | From prime factorization | `O(√n)` | `O(1)` |
| Prime factors of `n` | Divide out to `√n` | `O(√n)` | `O(1)` |
| All primes ≤ `n` | Naive `isPrime` loop | `O(n√n)` | `O(1)` |
| All primes ≤ `n` | Sieve | `O(n log log n)` | `O(n)` |
| Factorize many numbers | SPF sieve | `O(n log log n)` precompute, `O(log x)` per query | `O(n)` |

> **The decision rule:** one number → `O(√n)` trial division. Many numbers or a whole range →
> sieve. That single sentence answers most prime questions.

---

## 9. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **Why loop only to `√n`?** | If `n = a×b`, one factor must be ≤ `√n`; otherwise their product would exceed `n`. |
| **Is 1 prime?** | No — a prime needs exactly two distinct divisors. 2 is the smallest, and the only even prime. |
| **How do you print divisors in `O(√n)` and keep them sorted?** | Ascending loop prints `i` up to `√n`; a reverse loop from `i−1` prints `n/i`. |
| **Why does the reverse loop start at `i − 1`?** | `i` overshot `√n`; starting one back avoids printing a perfect square's root twice. |
| **How many divisors does `n` have?** | `Π(aᵢ+1)` over its prime powers. Perfect squares have an odd count. |
| **Why is the leftover in prime factorization prime?** | After removing all factors ≤ `√n`, anything > 1 that remains has no smaller factor. |
| **Why no primality check inside the factorization loop?** | Smaller primes were already divided out, so a composite `i` can never divide `n`. |
| **Sieve complexity, and why?** | `O(n log log n)` — total work is `n·Σ1/p` over primes, and `Σ1/p ≈ ln ln n`. |
| **Why start the inner sieve loop at `i*i`?** | Smaller multiples of `i` were already marked by smaller primes. |
| **Sieve space?** | `O(n)` — its one real cost, and why huge `n` needs a segmented sieve. |
| **One number vs a range?** | One → `O(√n)` trial division; a range → sieve. |
| **Factorize many queries fast?** | Precompute a smallest-prime-factor sieve, then each factorization is `O(log x)`. |

---

## 10. The Mental Model

```text
              Prime / divisor question
                        │
          ┌─────────────┴─────────────┐
          ↓                           ↓
    ONE number                  ALL numbers ≤ n
          │                           │
    loop i*i <= n                  Sieve
          │                           │
   O(√n), O(1) space        O(n lg lg n), O(n) space
          │                           │
  isPrime / divisors /          primes list /
  prime factorization           SPF for fast factorization
```
