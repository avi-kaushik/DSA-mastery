# 007 — Patterns & Cheat Sheet

> **One-line takeaway:** Every maths problem is one of four shapes — **digit-based `O(log n)`**, **factor-based `O(√n)`**, **sieve-based `O(n log log n)`**, or **formula-based `O(1)`**. Identify the shape and the solution follows.

Read this page last, right before an interview.

---

## 1. The Four Shapes

```text
                    A maths problem
                          │
      ┌──────────┬────────┴────────┬──────────┐
      ↓          ↓                 ↓          ↓
  DIGIT-BASED  FACTOR-BASED   SIEVE-BASED  FORMULA
  n%10, n/10   loop i*i<=n    precompute   closed form
   O(log n)      O(√n)        O(n lg lg n)   O(1)
      │            │               │           │
  count digits  isPrime        all primes   sum 1..n
  reverse       divisors       SPF table    digital root
  palindrome    prime factors  many queries nCr
  digit sum     gcd (log)                   trailing zeros
```

> **Ask first:** am I working with the *digits* of `n`, the *factors* of `n`, a *range* of
> numbers, or is there a closed form?

---

## 2. Problem → Technique Map

| Problem | Technique | Time |
|---|---|---|
| Count digits | `n /= 10` loop | `O(log n)` |
| Reverse a number | `rev = rev*10 + n%10` | `O(log n)` |
| Palindrome number | Reverse (half) and compare | `O(log n)` |
| Sum of digits / digital root | Digit loop / `1+(n−1)%9` | `O(log n)` / `O(1)` |
| GCD | Euclid: `gcd(b, a%b)` | `O(log min)` |
| LCM | `(a/gcd)*b` | `O(log min)` |
| Is `n` prime | Trial division to `√n` | `O(√n)` |
| All divisors | Pair `(i, n/i)` to `√n` | `O(√n)` |
| Count divisors | `Π(aᵢ+1)` from factorization | `O(√n)` |
| Prime factorization | Divide out to `√n` | `O(√n)` |
| All primes ≤ `n` | Sieve of Eratosthenes | `O(n lg lg n)` |
| Factorize many numbers | SPF sieve | `O(log x)` per query |
| `x^n` | Binary exponentiation | `O(log n)` |
| `x^n mod m` | Modular exponentiation | `O(log n)` |
| Modular inverse | Fermat `a^(m−2)` | `O(log m)` |
| Factorial | Iterative loop | `O(n)` |
| Trailing zeros in `n!` | `Σ⌊n/5ᵏ⌋` | `O(log₅ n)` |
| Single `nCr` | Multiplicative loop | `O(r)` |
| Many `nCr` | Pascal / factorial + inverses | `O(n²)` / `O(1)` per query |
| Sum `1..n` | `n(n+1)/2` | `O(1)` |
| Fibonacci, huge `n` | Matrix exponentiation | `O(log n)` |

---

## 3. Complexity Derivations — Know *Why*, Not Just *What*

| Complexity | Where it comes from |
|---|---|
| `O(log₁₀ n)` | Each `n /= 10` removes one digit; `n` has `⌊log₁₀ n⌋+1` digits |
| `O(log₂ n)` | Each step halves the value (`n /= 2`, binary exponentiation) |
| `O(log min(a,b))` | Euclid: the larger value at least halves every **two** steps |
| `O(log₅ n)` | Trailing zeros: `i` multiplies by 5 each iteration |
| `O(√n)` | If `n = a×b`, one factor is ≤ `√n` — so checking to `√n` suffices |
| `O(n log log n)` | Sieve: work is `n·Σ1/p` over primes, and `Σ1/p ≈ ln ln n` (Mertens) |
| `O(n log n)` | Marking multiples of **every** number: `n·Σ1/i = n·Hₙ ≈ n ln n` |

> The last two rows are the same sum restricted differently. **Skipping composites is what
> turns `log n` into `log log n`.**

---

## 4. Series You'll Reuse

Full treatment in
[Progressions & Series](../002-analysis-of-algorithms/010-progressions-and-series.md).

```text
1 + 2 + ... + n         = n(n+1)/2                Θ(n²)
1² + 2² + ... + n²      = n(n+1)(2n+1)/6          Θ(n³)
1³ + 2³ + ... + n³      = [n(n+1)/2]²             Θ(n⁴)
1 + 3 + 5 + ... (n odd) = n²
a + ar + ... + arⁿ⁻¹    = a(rⁿ−1)/(r−1)           GP
1 + ½ + ⅓ + ... + 1/n   = Hₙ ≈ ln n               Θ(log n)
Σ 1/p over primes ≤ n   ≈ ln ln n                 → the sieve bound
log 1 + ... + log n     = log(n!) = Θ(n log n)
```

**Where each shows up:**

```text
AP (n(n+1)/2)  → triangular loops, "sum 1..n" problems
GP             → binary exponentiation, doubling, recursion trees
Harmonic       → nested loop `j += i` → O(n log n)
Σ1/p           → Sieve of Eratosthenes → O(n log log n)
```

---

## 5. Edge Cases Checklist

Run through this before declaring any maths solution done:

```text
□ n = 0        digit loops produce 0 digits; factorial 0! = 1
□ n = 1        not prime; has one divisor; no prime factors
□ n = 2        prime, and the only even prime
□ Negatives    palindrome? digit loops? gcd of negatives?
□ Overflow     int max 2.1×10⁹, long max 9.2×10¹⁸
               12! fits int, 20! fits long
□ Perfect square  divisor pairing prints the root twice if unguarded
□ Java % sign  −7 % 3 = −1 → normalise with ((a%m)+m)%m
□ i * i        use long if n is near int range
□ Math.sqrt    prefer i*i <= n (exact integer comparison)
□ Division     integer division truncates toward zero, not floor
```

---

## 6. Java Snippets to Have Ready

```java
// gcd / lcm
int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }
long lcm(int a, int b) { return (long)(a / gcd(a, b)) * b; }

// primality
boolean isPrime(int n) {
    if (n < 2) return false;
    if (n % 2 == 0) return n == 2;
    for (int i = 3; i * i <= n; i += 2)
        if (n % i == 0) return false;
    return true;
}

// modular power
long powMod(long x, long n, long m) {
    long r = 1; x %= m;
    while (n > 0) {
        if ((n & 1) == 1) r = r * x % m;
        x = x * x % m;
        n >>= 1;
    }
    return r;
}

// trailing zeros in n!
int zeros(int n) {
    int c = 0;
    for (long i = 5; i <= n; i *= 5) c += n / i;
    return c;
}

// single nCr
long nCr(int n, int r) {
    if (r > n - r) r = n - r;
    long res = 1;
    for (int i = 0; i < r; i++) res = res * (n - i) / (i + 1);
    return res;
}
```

---

## 7. Optimisation Ladder

The progression an interviewer wants to hear:

```text
"Check every number 1..n"              O(n)
        ↓  divisors come in pairs
"Check only up to √n"                  O(√n)
        ↓  many queries, not one
"Precompute with a sieve"              O(n log log n) once, O(1) lookup
        ↓  is there a closed form?
"Use the formula directly"             O(1)
```

Concrete examples of the same ladder:

```text
Primes ≤ n:     O(n√n)  → sieve O(n lg lg n)
x^n:            O(n)    → squaring O(log n)
Sum 1..n:       O(n)    → n(n+1)/2, O(1)
Trailing zeros: compute n! (impossible) → count 5s, O(log n)
Divisors:       O(n)    → pairing O(√n)
GCD:            O(min)  → Euclid O(log min)
```

> **Every one of these replaces "iterate over the value" with "reason about its structure".**
> That sentence is a good thing to actually say out loud in an interview.

---

## 8. Master Quick Recall

| Question | Crisp answer |
|---|---|
| **Why is a `1..n` loop not linear time?** | Input size is `log n` digits, so `O(n)` is exponential in the input length. |
| **Why check only to `√n`?** | If `n = a×b`, one factor must be ≤ `√n`. |
| **Euclid's complexity and worst case?** | `O(log min(a,b))`; worst case is consecutive Fibonacci numbers. |
| **GCD–LCM relation?** | `a×b = gcd×lcm`; compute LCM as `(a/gcd)*b` to avoid overflow. |
| **Sieve complexity and why?** | `O(n log log n)` from `n·Σ1/p` with `Σ1/p ≈ ln ln n`. |
| **Why does the sieve's inner loop start at `i*i`?** | Smaller multiples were already marked by smaller primes. |
| **Fast exponentiation?** | Square the base, halve the exponent — `O(log n)`, `O(1)` iteratively. |
| **Trailing zeros in `n!`?** | `Σ⌊n/5ᵏ⌋`; 5s are scarcer than 2s. |
| **How do you divide modulo `m`?** | Multiply by the modular inverse; `a^(m−2)` when `m` is prime. |
| **Largest factorial in `int` / `long`?** | `12!` / `20!`. |
| **One number vs a whole range?** | `O(√n)` trial division vs a sieve. |
| **How do you spot the technique?** | Digits → `O(log n)`; factors → `O(√n)`; a range → sieve; otherwise look for a closed form. |

---

## 9. The Mental Model

```text
                   Read the problem
                          │
                          ↓
              What is the loop iterating over?
                          │
   ┌──────────┬───────────┼───────────┬──────────┐
   ↓          ↓           ↓           ↓          ↓
 digits    factors      range     exponent   nothing —
 of n      of n       of numbers   bits      it's a formula
   │          │           │           │          │
O(log n)   O(√n)    O(n lg lg n)  O(log n)     O(1)
   │          │           │           │          │
   └──────────┴───────────┴───────────┴──────────┘
                          │
                          ↓
           State the complexity AND why it holds
```
