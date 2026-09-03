# 002 — GCD, LCM & the Euclidean Algorithm

> **One-line takeaway:** `gcd(a, b) = gcd(b, a % b)` — replacing the larger number by the remainder shrinks the problem *fast*, giving `O(log(min(a,b)))`. Everything else (LCM, fractions, modular inverse) is built on top of it.

---

## 1. Definitions

```text
GCD / HCF  = largest number that divides BOTH a and b
LCM        = smallest number divisible BY both a and b
```

```text
a = 12 = 2² × 3
b = 18 = 2  × 3²

GCD = 2¹ × 3¹ = 6     ← take the MINIMUM power of each shared prime
LCM = 2² × 3² = 36    ← take the MAXIMUM power of each prime
```

That prime-factor view explains the identity in §5 immediately.

---

## 2. The Naive Approach (and why it's bad)

```java
int gcd(int a, int b) {
    int res = Math.min(a, b);
    while (res > 0) {
        if (a % res == 0 && b % res == 0) return res;
        res--;
    }
    return 1;
}
```

```text
Time = O(min(a, b))     ← for a = 10⁹ this is a billion iterations
```

---

## 3. The Euclidean Algorithm

📄 [`FactorAndMultipleMathematics.java`](../../programs/java/mathematics/FactorAndMultipleMathematics.java)

> **Key identity:** `gcd(a, b) = gcd(b, a mod b)`, with `gcd(a, 0) = a`.

```java
int gcd(int a, int b) {
    while (b != 0) {
        int rem = a % b;
        a = b;
        b = rem;
    }
    return a;
}
```

Recursive form (identical work, `O(log n)` stack):

```java
int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }
```

### Trace: `gcd(48, 18)`

```text
 a    b    a % b
────────────────
48   18      12
18   12       6
12    6       0
 6    0     ─── b == 0, answer = 6
```

> Note you don't need to pass the larger number first. If `a < b`, the first iteration
> computes `a % b = a` and simply **swaps them** — one wasted step, no bug.

---

## 4. 🧮 Why It Works

Let `d` divide both `a` and `b`. Write `a = qb + r`, so `r = a − qb`.

```text
d | a  and  d | b   ⟹   d | (a − qb)   ⟹   d | r
```

So **every common divisor of `(a, b)` is also a common divisor of `(b, r)`.** The reverse
holds by the same argument on `a = qb + r`. The two pairs have *identical* sets of common
divisors, hence the same greatest one:

```text
gcd(a, b) = gcd(b, a mod b)          ∎
```

The base case `gcd(a, 0) = a` holds because every number divides 0.

---

## 5. 🧮 Why It's O(log(min(a, b)))

**Claim:** after two iterations the larger value is at least **halved**.

```text
If b ≤ a/2   →  the remainder a % b < b ≤ a/2          ✓ already halved
If b > a/2   →  a = 1·b + r, so r = a − b < a/2        ✓ halved
```

Either way the value drops below half within two steps, so the number of steps is
`O(log₂ n)`:

```text
Time  = O(log(min(a, b)))
Space = O(1) iterative,  O(log n) recursive (call stack)
```

### The exact worst case: consecutive Fibonacci numbers

```text
gcd(F₍ₙ₊₁₎, Fₙ)  takes the maximum number of steps
gcd(21, 13) → gcd(13, 8) → gcd(8, 5) → gcd(5, 3) → gcd(3, 2) → gcd(2, 1) → gcd(1, 0)
```

Because each quotient is 1, nothing gets skipped — the slowest possible descent.
**Lamé's theorem:** the number of steps is at most 5 × (number of decimal digits of the
smaller number).

> This is the classic follow-up: *"what input makes Euclid slowest?"* → **consecutive
> Fibonacci numbers.**

---

## 6. LCM via GCD

> **Identity:** `a × b = gcd(a, b) × lcm(a, b)`

From the prime-power view in §1: for each prime, `min(x,y) + max(x,y) = x + y`. ∎

```java
int lcm(int a, int b) {
    return (a / gcd(a, b)) * b;      // divide FIRST
}
```

⚠️ **Never write `(a * b) / gcd(a, b)`** — `a * b` can overflow before the division. Dividing
first is exact because `gcd(a,b)` always divides `a`.

```text
Time = O(log(min(a,b)))   — dominated by the GCD
```

---

## 7. Extended Euclidean Algorithm

Solves **Bézout's identity**: find `x, y` with

```text
a·x + b·y = gcd(a, b)
```

```java
// returns gcd, and sets x, y
int extGcd(int a, int b, int[] xy) {
    if (b == 0) { xy[0] = 1; xy[1] = 0; return a; }
    int[] t = new int[2];
    int g = extGcd(b, a % b, t);
    xy[0] = t[1];
    xy[1] = t[0] - (a / b) * t[1];
    return g;
}
```

```text
Time = O(log(min(a,b))),  Space = O(log n) stack
```

### Why you care: the modular inverse

```text
a·x ≡ 1 (mod m)   has a solution  ⟺  gcd(a, m) = 1
```

Extended Euclid gives that `x` directly. See
[Modular Arithmetic](005-modular-arithmetic.md).

---

## 8. Useful GCD Properties

```text
gcd(a, 0)     = a
gcd(a, 1)     = 1
gcd(a, a)     = a
gcd(a, b)     = gcd(b, a)                     symmetric
gcd(a, b, c)  = gcd(gcd(a, b), c)             associative → extends to arrays
gcd(ka, kb)   = k · gcd(a, b)
lcm(a, b, c)  = lcm(lcm(a, b), c)
```

**Coprime** means `gcd(a, b) = 1`. Consecutive integers are always coprime:
`gcd(n, n+1) = 1`.

### GCD of an array

```java
int g = arr[0];
for (int x : arr) {
    g = gcd(g, x);
    if (g == 1) break;          // can never decrease further — early exit
}
```

```text
Time = O(n log(max))
```

> The `g == 1` early exit is a small optimisation interviewers like to see.

---

## 9. Where GCD/LCM Show Up

| Problem | Idea |
|---|---|
| Reduce a fraction `a/b` | Divide both by `gcd(a, b)` |
| Two lights blinking together | `lcm` of the intervals |
| Tile an `a × b` floor with square tiles | Largest tile side = `gcd(a, b)` |
| Cut ropes into equal max-length pieces | `gcd` of the lengths |
| Points on a line segment (lattice points) | `gcd(|dx|, |dy|) − 1` between endpoints |
| Check if `a` and `b` are coprime | `gcd(a, b) == 1` |
| Modular inverse | Extended Euclid |

---

## 10. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **State the Euclidean algorithm.** | `gcd(a, b) = gcd(b, a mod b)`, stopping at `gcd(a, 0) = a`. |
| **Why is it correct?** | `a = qb + r` means any common divisor of `(a,b)` divides `r`, so both pairs share the same common divisors. |
| **Complexity?** | `O(log(min(a, b)))` — the larger value at least halves every two steps. |
| **Worst-case input?** | Consecutive Fibonacci numbers; by Lamé's theorem, steps ≤ 5 × digits of the smaller number. |
| **Relation between GCD and LCM?** | `a × b = gcd(a,b) × lcm(a,b)`. |
| **How do you compute LCM safely?** | `(a / gcd) * b` — divide before multiplying to avoid overflow. |
| **What does extended Euclid give you?** | `x, y` with `ax + by = gcd(a,b)` — used for the modular inverse. |
| **When does a modular inverse exist?** | Exactly when `gcd(a, m) = 1`. |
| **GCD of an array?** | Fold with `gcd`, breaking early if it hits 1: `O(n log max)`. |
| **Is `gcd(n, n+1)` ever > 1?** | No — consecutive integers are always coprime. |

---

## 11. The Mental Model

```text
        gcd(a, b)
            │
            ↓
     replace a with b,
     replace b with a % b
            │
            ↓
     remainder shrinks fast
     (halves every 2 steps)
            │
            ↓
        b == 0  →  answer is a
            │
            ↓
     lcm = (a / gcd) * b
```
