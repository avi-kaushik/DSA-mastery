# 001 — Number & Digit Basics

> **One-line takeaway:** Almost every "maths" problem reduces to repeatedly applying `% 10` and `/ 10`. Because each step removes one digit, these algorithms are **`O(log₁₀ n)`** — not `O(n)`.

---

## 1. Why Maths Problems Appear in Interviews

They test whether you reason about **numbers** rather than iterate blindly:

```text
Loop from 1 to n        →  O(n)     ← the naive instinct
Loop while n > 0, n/=10 →  O(log n) ← digit-based
Loop while i*i <= n     →  O(√n)    ← factor-based
```

> The whole subject is recognising which of those three shapes the problem has.

---

## 2. ⚠️ The Input Size Trap

For a number `n`, the input size is **the number of digits**, not `n` itself.

```text
Digits of n  =  ⌊log₁₀ n⌋ + 1
Bits of n    =  ⌊log₂ n⌋ + 1
```

So a loop that runs `n` times is **exponential** in the input size:

```text
for (i = 1; i <= n; i++)     → O(n) = O(2^digits)   ⚠️ exponential in input length
while (n > 0) n /= 10;       → O(log n)             ✓ linear in input length
```

This is why "`O(√n)` primality" is considered slow in cryptography but fine in interviews —
`√n` is still exponential in the bit length.

---

## 3. The Two Operations Everything Is Built On

```java
n % 10      // last digit
n / 10      // remove the last digit  (integer division)
```

```text
n = 1234
──────────────────────────────
n % 10 = 4      n / 10 = 123
n % 10 = 3      n / 10 = 12
n % 10 = 2      n / 10 = 1
n % 10 = 1      n / 10 = 0   ← loop ends
```

Each iteration removes exactly one digit → **`O(log₁₀ n)` iterations**.

---

## 4. Counting Digits

📄 [`CountDigits.java`](../../programs/java/mathematics/CountDigits.java)

```java
int count(int x) {
    int c = 0;
    while (x > 0) { c++; x /= 10; }
    return c;
}
```

```text
Time  = O(log₁₀ n)      one iteration per digit
Space = O(1)
```

### The O(1) alternative

```java
int c = (int) Math.floor(Math.log10(n)) + 1;
```

```text
Time = O(1)   but floating-point — can be off by one for large values.
```

> **Interview answer:** mention both, then say you'd use the loop because it's exact.
> ⚠️ Handle `n = 0` separately — both versions get it wrong (`0` has 1 digit).

---

## 5. Reversing a Number

```java
int reverse(int n) {
    int rev = 0;
    while (n > 0) {
        rev = rev * 10 + n % 10;   // shift left, append digit
        n /= 10;
    }
    return rev;
}
```

```text
rev = rev * 10 + digit    ← the core idiom, worth memorising
```

```text
n = 1234
rev: 0 → 4 → 43 → 432 → 4321
```

```text
Time = O(log n),  Space = O(1)
```

⚠️ **Overflow:** reversing a valid `int` can exceed `int` range (`1999999999` reverses to
`9999999991`). Use `long`, or check `rev > (Integer.MAX_VALUE - digit) / 10` before each step.

---

## 6. Palindrome Number

📄 [`PalindromeNumber.java`](../../programs/java/mathematics/PalindromeNumber.java)

```java
boolean isPalindrome(int n) {
    if (n < 0) return false;          // −121 is not a palindrome
    int rev = 0, copy = n;
    while (copy > 0) {
        rev = rev * 10 + copy % 10;
        copy /= 10;
    }
    return rev == n;
}
```

```text
Time = O(log n),  Space = O(1)
```

### The overflow-free variant

Reverse only **half** the number and compare:

```java
boolean isPalindrome(int n) {
    if (n < 0 || (n % 10 == 0 && n != 0)) return false;
    int rev = 0;
    while (n > rev) { rev = rev * 10 + n % 10; n /= 10; }
    return n == rev || n == rev / 10;   // odd-length case drops middle digit
}
```

> **Why it's better:** `rev` never exceeds half the digits, so it cannot overflow. Mentioning
> this shows you thought past the obvious solution.

---

## 7. Other One-Liners Built on the Same Loop

| Problem | Core line | Complexity |
|---|---|---|
| Sum of digits | `sum += n % 10` | `O(log n)` |
| Largest digit | `max = max(max, n % 10)` | `O(log n)` |
| Digital root (repeated digit sum) | `1 + (n − 1) % 9` | **`O(1)`** ✨ |
| Armstrong number | `sum += (n%10)^k` | `O(log n)` |
| Count of a specific digit | `if (n % 10 == d) c++` | `O(log n)` |
| Base conversion | `n % b`, `n /= b` | `O(log_b n)` |

> **Digital root** is a nice closed form: the repeated digit sum of `n` is `1 + (n−1) % 9`
> for `n > 0`. It follows from `n ≡ digitSum(n) (mod 9)`.

---

## 8. 🧮 Integer Pitfalls in Java

```text
Integer overflow   int  max = 2,147,483,647  ≈ 2.1 × 10⁹
                   long max ≈ 9.2 × 10¹⁸
                   → use long for products, factorials, sums of large arrays

Integer division   7 / 2 = 3     (truncates toward zero, NOT floor)
                  −7 / 2 = −3    ⚠️ floor would be −4

Modulo of negatives  −7 % 3 = −1 in Java   (not 2)
                     safe positive mod: ((a % m) + m) % m

Overflow-safe mid   (lo + hi) / 2      can overflow
                    lo + (hi − lo) / 2 ✓
```

### `i * i <= n` beats `i <= Math.sqrt(n)`

```java
for (int i = 3; i <= Math.sqrt(n); i += 2)   // floating point, recomputed
for (int i = 3; i * i <= n;        i += 2)   // exact integer arithmetic  ✓
```

> `Math.sqrt` returns a `double`; for large `n` rounding can include or exclude the boundary
> incorrectly. `i * i <= n` is exact and usually faster. ⚠️ Use `long` for `i * i` if `n`
> approaches `int` range.

---

## 9. Series Formulas You'll Reuse

Maths problems lean on the same sums as complexity analysis
(see [Progressions & Series](../002-analysis-of-algorithms/010-progressions-and-series.md)):

```text
1 + 2 + ... + n        = n(n+1)/2
1² + 2² + ... + n²     = n(n+1)(2n+1)/6
1³ + 2³ + ... + n³     = [n(n+1)/2]²
1 + 3 + 5 + ... (n odd)= n²
a + ar + ... + arⁿ⁻¹   = a(rⁿ − 1)/(r − 1)
```

These turn `O(n)` loops into `O(1)` formulas — a common "optimise this" follow-up.

---

## 10. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **Complexity of digit loops?** | `O(log₁₀ n)` — each `n /= 10` removes one digit. |
| **Why isn't a `1..n` loop linear time?** | Input size is `log n` digits, so `O(n)` is exponential in the input length. |
| **Number of digits in `n`?** | `⌊log₁₀ n⌋ + 1`, or count with a loop (exact, handles 0). |
| **How do you reverse a number?** | `rev = rev * 10 + n % 10` while dividing `n` by 10. |
| **Overflow risk in reversal?** | Yes — use `long`, or reverse only half for palindrome checks. |
| **Is −121 a palindrome?** | No, by the usual convention — the sign breaks symmetry. |
| **`−7 % 3` in Java?** | `−1`. For a positive result use `((a % m) + m) % m`. |
| **Why prefer `i*i <= n` to `i <= Math.sqrt(n)`?** | Exact integer arithmetic, no floating-point rounding at the boundary. |
| **Safe midpoint?** | `lo + (hi − lo) / 2` avoids overflow. |
| **Digital root of `n`?** | `1 + (n − 1) % 9` — `O(1)`. |

---

## 11. The Mental Model

```text
                 A number problem
                        │
          ┌─────────────┼─────────────┐
          ↓             ↓             ↓
     digit-based    factor-based   formula-based
     n % 10, n/10   i*i <= n       closed form
          │             │             │
      O(log n)        O(√n)          O(1)
```
