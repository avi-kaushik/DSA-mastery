# 004 — Fast Exponentiation (Binary Exponentiation)

> **One-line takeaway:** To compute `x^n`, don't multiply `n` times. **Square the base and halve the exponent** — `O(log n)` instead of `O(n)`. The exponent's binary representation is why it works.

---

## 1. The Naive Way

```java
int power(int x, int n) {
    int res = 1;
    for (int i = 0; i < n; i++) res *= x;
    return res;
}
```

```text
Time = O(n)
```

For `n = 10⁹` that is a billion multiplications. We can do it in ~30.

---

## 2. The Core Identity

```text
        ⎧ (x^(n/2))²        if n is even
x^n  =  ⎨
        ⎩ x · (x^(n/2))²    if n is odd
```

```text
x^10 = (x^5)²
x^5  = x · (x^2)²
x^2  = (x^1)²
```

Each step **halves** the exponent, so there are `log₂ n` steps.

---

## 3. Recursive Version

📄 [`Powers.java`](../../programs/java/mathematics/Powers.java)

```java
int power(int x, int n) {
    if (n == 0) return 1;

    int half = power(x, n / 2);      // ONE call, result reused
    int sq   = half * half;

    return (n % 2 == 0) ? sq : sq * x;
}
```

```text
Time  = O(log n)
Space = O(log n)     ← recursion stack
```

### ⚠️ The mistake that ruins it

```java
return power(x, n/2) * power(x, n/2);   // ✗ TWO calls → T(n) = 2T(n/2) + 1 → O(n)
```

Storing the result in a variable makes it `T(n) = T(n/2) + O(1)` → `O(log n)`.
**Recomputing the same subproblem throws away the entire optimisation** — the recurrence
tells you instantly (see
[Recurrence Relations](../002-analysis-of-algorithms/011-recurrence-relations.md)).

---

## 4. Iterative Version — O(1) Space

📄 [`Powers.java`](../../programs/java/mathematics/Powers.java)

```java
int power(int x, int n) {
    int res = 1;
    while (n > 0) {
        if (n % 2 == 1) res *= x;   // bit is set → take this power
        x *= x;                     // x, x², x⁴, x⁸, ...
        n /= 2;                     // shift to the next bit
    }
    return res;
}
```

```text
Time  = O(log n)
Space = O(1)          ← preferred in interviews
```

### 🧮 Why it works — read the exponent in binary

```text
n = 13 = 1101₂ = 8 + 4 + 1

x¹³ = x⁸ · x⁴ · x¹
```

The loop generates `x¹, x², x⁴, x⁸, …` by repeated squaring and multiplies in only the ones
whose bit is set:

```text
step   n    n%2   res            x
──────────────────────────────────────
  1   13     1    x¹             x²
  2    6     0    x¹             x⁴
  3    3     1    x¹·x⁴ = x⁵     x⁸
  4    1     1    x⁵·x⁸ = x¹³    x¹⁶
  5    0     -    x¹³  ✓
```

> **The binary view is the explanation.** `n` has `log₂ n` bits, one iteration per bit →
> `O(log n)`. Bit-shift form: `n >>= 1` and `(n & 1) == 1`.

---

## 5. Modular Exponentiation

The version you actually need in problems, since powers overflow instantly:

```java
long powerMod(long x, long n, long m) {
    long res = 1;
    x %= m;                          // reduce first
    while (n > 0) {
        if ((n & 1) == 1) res = res * x % m;
        x = x * x % m;
        n >>= 1;
    }
    return res;
}
```

```text
Time = O(log n),  Space = O(1)
```

Correct because `(a · b) mod m = ((a mod m) · (b mod m)) mod m` — see
[Modular Arithmetic](005-modular-arithmetic.md).

⚠️ Use `long`. With `m` near 10⁹, `x * x` reaches 10¹⁸ — fits in `long`, overflows `int`.

---

## 6. ⚠️ Overflow in the Plain Version

`Powers.java` uses `int`, which is fine for small inputs but silently wraps for large ones:

```text
2³¹ overflows int    (int max ≈ 2.1 × 10⁹)
2⁶³ overflows long
```

```text
Interview answer: "I'd use long, or take everything mod 10⁹+7 if the problem asks for it."
```

---

## 7. Related Uses of the Same Trick

| Problem | How squaring applies |
|---|---|
| `x^n mod m` | Modular exponentiation |
| Fibonacci in `O(log n)` | Matrix exponentiation of `[[1,1],[1,0]]^n` |
| Modular inverse (prime `m`) | `a^(m−2) mod m` — Fermat's little theorem |
| `a^(b^c)` | Reduce the exponent mod `φ(m)` (Euler) |
| Repeated function application | Binary lifting (e.g. LCA in trees) |

> **Binary lifting** — jumping `2^k` steps at a time — is the same idea applied to pointers
> instead of numbers. Recognising it as "exponentiation by squaring" makes LCA much easier
> to remember.

---

## 8. Complexity Summary

| Version | Time | Space |
|---|---|---|
| Naive loop | `O(n)` | `O(1)` |
| Recursive squaring | `O(log n)` | `O(log n)` stack |
| Iterative squaring | `O(log n)` | `O(1)` ✓ |
| Two-call recursion (bug) | `O(n)` | `O(log n)` |
| Modular exponentiation | `O(log n)` | `O(1)` |

---

## 9. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **How do you compute `x^n` fast?** | Exponentiation by squaring: square the base, halve the exponent → `O(log n)`. |
| **The identity?** | `x^n = (x^(n/2))²` for even `n`, `x · (x^(n/2))²` for odd `n`. |
| **Why `O(log n)`?** | The exponent halves each step, so there is one iteration per bit of `n`. |
| **Why is the iterative version preferred?** | Same `O(log n)` time but `O(1)` space — no recursion stack. |
| **What's wrong with `power(x,n/2) * power(x,n/2)`?** | It solves the same subproblem twice: `T(n)=2T(n/2)+1 = O(n)`. Store it in a variable. |
| **How does the iterative version relate to binary?** | Each set bit of `n` contributes one precomputed power: `x¹³ = x⁸·x⁴·x¹`. |
| **How do you avoid overflow?** | Use `long` and take `mod m` at every multiplication. |
| **Modular exponentiation identity?** | `(a·b) mod m = ((a mod m)·(b mod m)) mod m`. |
| **Where else does squaring appear?** | Matrix exponentiation for Fibonacci, modular inverse via Fermat, and binary lifting for LCA. |

---

## 10. The Mental Model

```text
              x^n
               │
               ↓
     Is the exponent even?
        ╱             ╲
      yes              no
       │                │
   (x^(n/2))²      x · (x^(n/2))²
       │                │
       └──── halve n ───┘
               │
               ↓
        log₂ n steps
               │
               ↓
   iterative = same, reading n's bits
```
