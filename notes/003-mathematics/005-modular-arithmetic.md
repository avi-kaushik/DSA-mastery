# 005 — Modular Arithmetic

> **One-line takeaway:** Answers are asked "mod 10⁹+7" because real answers overflow. Addition, subtraction and multiplication pass straight through the modulus — **division does not**, and needs a modular inverse.

---

## 1. Why `mod 10⁹ + 7` Is Everywhere

Counting problems produce astronomically large answers (`n!`, `2ⁿ`, path counts). Rather
than demand big-integer arithmetic, problems ask for the answer modulo a large prime.

```text
10⁹ + 7 = 1,000,000,007
```

Why that number:

```text
• It is PRIME       → every non-zero value has a modular inverse (Fermat applies)
• ~10⁹              → fits comfortably in an int
• Its square ~10¹⁸  → a product of two residues still fits in a long  ✓
```

> That last line is the practical reason: `(a % m) * (b % m)` stays inside `long`.

---

## 2. The Rules That Work

```text
(a + b) mod m = ((a mod m) + (b mod m)) mod m
(a − b) mod m = ((a mod m) − (b mod m) + m) mod m      ⚠️ note the + m
(a × b) mod m = ((a mod m) × (b mod m)) mod m
(a ^ b) mod m = modular exponentiation                 (note 004)
```

```text
(a / b) mod m ≠ ((a mod m) / (b mod m)) mod m          ✗ DIVISION DOES NOT WORK
```

### Why subtraction needs `+ m`

Java's `%` keeps the sign of the dividend:

```text
(3 − 10) % 7  =  −7 % 7  =  0     ok here, but in general:
−4 % 7 = −4   in Java     (mathematically it should be 3)
```

```java
long sub(long a, long b, long m) { return ((a - b) % m + m) % m; }
```

> **Always normalise after a subtraction.** This is the single most common modular bug.

---

## 3. Modular Inverse — Division's Replacement

> **The modular inverse of `a` is the value `a⁻¹` with `a · a⁻¹ ≡ 1 (mod m)`.**

Then division becomes multiplication:

```text
(a / b) mod m  =  (a × b⁻¹) mod m
```

### When does it exist?

```text
b⁻¹ mod m exists  ⟺  gcd(b, m) = 1
```

So if `m` is prime, every `b` from 1 to `m−1` has an inverse.

### Method 1 — Fermat's Little Theorem (m prime) ✓ the usual choice

> **Fermat:** if `m` is prime and `a` is not a multiple of `m`, then `a^(m−1) ≡ 1 (mod m)`.

Multiply both sides by `a⁻¹`:

```text
a^(m−2) ≡ a⁻¹ (mod m)
```

```java
long inverse(long a, long m) {      // m must be PRIME
    return powerMod(a, m - 2, m);   // note 004
}
```

```text
Time = O(log m)
```

### Method 2 — Extended Euclid (any `m` with gcd = 1)

Solve `a·x + m·y = 1`; then `x mod m` is the inverse. See
[GCD & Euclidean](002-gcd-lcm-euclidean.md).

```text
Time = O(log m)
```

| Method | Requires | Use when |
|---|---|---|
| Fermat (`a^(m−2)`) | `m` prime | Almost always — `m = 10⁹+7` |
| Extended Euclid | `gcd(a, m) = 1` | `m` is not prime |

---

## 4. Practical Patterns

### Safe helpers

```java
static final long MOD = 1_000_000_007L;

long add(long a, long b) { return (a + b) % MOD; }
long sub(long a, long b) { return ((a - b) % MOD + MOD) % MOD; }
long mul(long a, long b) { return (a % MOD) * (b % MOD) % MOD; }
long div(long a, long b) { return mul(a, powerMod(b, MOD - 2, MOD)); }
```

### Take the mod *early and often*

```java
// ✗ overflows before the mod ever runs
long bad = 1;
for (int i = 1; i <= n; i++) bad *= i;
return bad % MOD;

// ✓ stays bounded the whole way
long good = 1;
for (int i = 1; i <= n; i++) good = good * i % MOD;
return good;
```

> **Rule:** apply `% MOD` after *every* multiplication and addition, never once at the end.

---

## 5. Other Facts Worth Knowing

```text
Euler's totient   φ(n) = count of k ≤ n with gcd(k, n) = 1
                  φ(p) = p − 1 for prime p
                  φ(p^k) = p^k − p^(k−1)

Euler's theorem   a^φ(m) ≡ 1 (mod m)      when gcd(a, m) = 1
                  (Fermat is the special case m prime)

Huge exponents    a^b mod m  →  reduce b mod φ(m)

Congruence        a ≡ b (mod m)  means  m divides (a − b)

Divisibility      n is divisible by 3 (or 9) ⟺ its digit sum is
                  n is divisible by 11 ⟺ its alternating digit sum is
```

The digit-sum rules follow from `10 ≡ 1 (mod 9)` and `10 ≡ −1 (mod 11)` — a neat way to
show you understand *why* the school rule works.

---

## 6. Where It Shows Up

| Problem | Modular need |
|---|---|
| Count paths / ways (DP) | Every addition mod `M` |
| `nCr mod M` | Factorials + modular inverse |
| Hashing (rolling hash) | Polynomial evaluation mod a large prime |
| Fibonacci for huge `n` | Matrix exponentiation mod `M` |
| Cyclic indexing | `(i + 1) % n` for circular arrays |
| Checking divisibility | Digit-sum congruences |

---

## 7. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **Why `10⁹+7`?** | It's prime (so inverses exist), fits in an `int`, and its square fits in a `long`. |
| **Which operations distribute over mod?** | Addition, subtraction and multiplication — not division. |
| **Why `+ m` after subtracting?** | Java's `%` can return a negative value; adding `m` normalises it. |
| **How do you divide under a modulus?** | Multiply by the modular inverse: `(a/b) mod m = a · b⁻¹ mod m`. |
| **When does an inverse exist?** | Exactly when `gcd(b, m) = 1`; guaranteed for all non-zero `b` if `m` is prime. |
| **State Fermat's little theorem.** | For prime `m` and `a` not divisible by `m`: `a^(m−1) ≡ 1 (mod m)`, so `a⁻¹ = a^(m−2)`. |
| **Inverse when `m` isn't prime?** | Extended Euclid, provided `gcd(a, m) = 1`. |
| **Cost of computing an inverse?** | `O(log m)` by either method. |
| **When do you apply the mod?** | After every operation — never accumulate first and mod at the end. |
| **Why is a number divisible by 9 iff its digit sum is?** | Because `10 ≡ 1 (mod 9)`, so `n ≡ digitSum(n) (mod 9)`. |

---

## 8. The Mental Model

```text
              Answer will be huge
                       │
                       ↓
              work mod 10⁹+7
                       │
       ┌───────────────┼───────────────┐
       ↓               ↓               ↓
     + and ×        subtraction     division
   mod freely       add m first     multiply by
                                    the inverse
                                         │
                                         ↓
                              m prime → a^(m−2)
                              else    → extended Euclid
```
