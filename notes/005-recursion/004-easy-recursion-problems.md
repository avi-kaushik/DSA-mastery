# 004 — Easy Recursion Problems

> **One-line takeaway:** The easy problems all share one shape — **peel one piece off, recurse on the rest, combine**. Work them until writing the recurrence is automatic; every hard recursion problem is built from this reflex.

All code here is C++, matching
[`programs/cpp/techniques/recursion/`](../../programs/cpp/techniques/recursion/).

---

## 1. The Shape They All Share

```text
f(input) = combine( one piece , f(smaller input) )
```

```cpp
ReturnType f(Input x) {
    if (smallest) return identity;        // base case  (note 003)
    return combine(piece(x), f(shrink(x)));
}
```

Everything below is that template with different `combine` and `shrink`.

---

## 2. Print 1 to N and N to 1

📄 [`print_numbers.cpp`](../../programs/cpp/techniques/recursion/print_numbers.cpp)

```cpp
// N down to 1 — print BEFORE recursing (work on the way down)
void print_number_series_reverse(int n) {
    if (n == 0) return;
    cout << n;
    print_number_series_reverse(n - 1);
}

// 1 up to N — print AFTER recursing (work on the way up)
void print_number_series(int n) {
    if (n == 0) return;
    print_number_series(n - 1);
    cout << n;
}
```

```text
Same structure, one line moved → opposite output.
Time O(n) · Space O(n) stack
```

> **The lesson:** placing work before or after the call chooses the *order* of processing.
> Before → top-down, after → bottom-up. This is the entire trick behind post-order tree
> traversal and printing a linked list in reverse. See
> [Introduction to Recursion §4](001-introduction-to-recursion.md).

---

## 3. Factorial

```cpp
int factorial(int n) {
    if (n == 0) return 1;                 // identity for multiplication
    return n * factorial(n - 1);
}
```

```text
f(n) = n × f(n−1),  f(0) = 1
Time O(n) · Space O(n)
```

⚠️ Overflows fast: `12!` is the largest that fits in a 32-bit `int`, `20!` in a 64-bit
`long long`.

---

## 4. Sum of 1 to N

```cpp
int sum(int n) {
    if (n == 0) return 0;                 // identity for addition
    return n + sum(n - 1);
}
```

```text
f(n) = n + f(n−1),  f(0) = 0
Time O(n) · Space O(n)
```

> The `O(1)` answer is `n(n+1)/2`. Mentioning that after giving the recursive solution is
> exactly the instinct interviewers are checking for.

---

## 5. Sum of Digits

```cpp
int sum_of_digits(int n) {
    if (n == 0) return 0;
    return (n % 10) + sum_of_digits(n / 10);
}
```

```text
peel  = n % 10   (last digit)
shrink= n / 10   (drop last digit)
Time O(log₁₀ n) · Space O(log₁₀ n)
```

> Note the complexity: **`log n`, not `n`** — each call removes a *digit*, not a unit.

---

## 6. Palindrome Check

📄 [`palindrome.cpp`](../../programs/cpp/techniques/recursion/palindrome.cpp)

```cpp
bool is_palindrome(const string &word, int start, int end) {
    if (start >= end) return true;                     // met OR crossed
    return word[start] == word[end] &&
           is_palindrome(word, start + 1, end - 1);
}

bool is_palindrome(const string &word) {               // wrapper
    return is_palindrome(word, 0, word.length() - 1);
}
```

```text
Two pointers shrink toward each other.
Time O(n) · Space O(n) stack
```

Two things worth noticing:

- **`start >= end`, not `==`** — even-length strings make the pointers cross
  ([note 003 §4](003-writing-base-cases.md)).
- **`&&` short-circuits**, so a mismatch stops the recursion immediately instead of
  checking the rest.
- ⚠️ `word.length()` is `size_t` (unsigned). For an empty string, `length() - 1` wraps to a
  huge number. Guard with `if (word.empty()) return true;` in the wrapper.

---

## 7. Power — x ⁿ

```cpp
// linear: O(n)
int power(int x, int n) {
    if (n == 0) return 1;
    return x * power(x, n - 1);
}

// fast: O(log n) — square the base, halve the exponent
int fast_power(int x, int n) {
    if (n == 0) return 1;
    int half = fast_power(x, n / 2);      // ONE call, result reused
    return (n % 2 == 0) ? half * half : half * half * x;
}
```

> ⚠️ Writing `fast_power(x, n/2) * fast_power(x, n/2)` computes the same subproblem twice
> and collapses back to `O(n)`. Storing it in a variable is the whole optimisation.
> Full treatment in [Fast Exponentiation](../003-mathematics/004-exponentiation.md).

---

## 8. Reverse a String

```cpp
void reverse(string &s, int start, int end) {
    if (start >= end) return;
    swap(s[start], s[end]);
    reverse(s, start + 1, end - 1);
}
```

```text
Same two-pointer shape as palindrome — check vs swap is the only difference.
Time O(n) · Space O(n)
```

---

## 9. GCD — Euclid

```cpp
int gcd(int a, int b) {
    if (b == 0) return a;
    return gcd(b, a % b);
}
```

```text
Tail recursive. Time O(log min(a,b)) · Space O(log n), or O(1) with TCO at -O2.
```

> See [GCD & Euclidean Algorithm](../003-mathematics/002-gcd-lcm-euclidean.md) for why it's
> logarithmic, and [Tail Recursion](002-tail-recursion.md) for the `-O2` note.

---

## 10. Fibonacci — and Why It's the Odd One Out

```cpp
int fib(int n) {
    if (n == 0) return 0;
    if (n == 1) return 1;                 // TWO base cases
    return fib(n - 1) + fib(n - 2);       // TWO recursive calls
}
```

```text
Time O(2ⁿ)  ← catastrophic: fib(40) is 300M+ calls
Space O(n)  ← depth only
```

Every other problem on this page makes **one** call and is linear. Fibonacci makes **two**,
so its recursion tree branches and the cost explodes.

```text
                fib(5)
            ╱           ╲
       fib(4)           fib(3)      ← fib(3) computed twice
      ╱     ╲           ╱    ╲
  fib(3)  fib(2)   fib(2)  fib(1)   ← and again...
```

The fix is memoisation (`O(n)`), or two rolling variables (`O(1)` space).

> **Recognise the split:** one recursive call → linear. Two or more → exponential unless
> you cache. That distinction is the bridge into dynamic programming.

---

## 11. Summary Table

| Problem | Recurrence | Base case | Time | Space |
|---|---|---|---|---|
| Print N→1 | print, then `f(n−1)` | `n == 0` | `O(n)` | `O(n)` |
| Print 1→N | `f(n−1)`, then print | `n == 0` | `O(n)` | `O(n)` |
| Factorial | `n × f(n−1)` | `n == 0` → `1` | `O(n)` | `O(n)` |
| Sum 1..n | `n + f(n−1)` | `n == 0` → `0` | `O(n)` | `O(n)` |
| Sum of digits | `n%10 + f(n/10)` | `n == 0` → `0` | `O(log n)` | `O(log n)` |
| Palindrome | `s[i]==s[j] && f(i+1,j−1)` | `i >= j` → `true` | `O(n)` | `O(n)` |
| Reverse string | swap, then `f(i+1,j−1)` | `i >= j` | `O(n)` | `O(n)` |
| Power (linear) | `x × f(x,n−1)` | `n == 0` → `1` | `O(n)` | `O(n)` |
| Power (fast) | `f(x,n/2)²` | `n == 0` → `1` | `O(log n)` | `O(log n)` |
| GCD | `f(b, a%b)` | `b == 0` → `a` | `O(log n)` | `O(log n)` |
| Fibonacci | `f(n−1) + f(n−2)` | `n==0`, `n==1` | `O(2ⁿ)` | `O(n)` |

---

## 12. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What shape do all easy recursion problems share?** | Peel one piece off, recurse on the rest, combine the two. |
| **Print 1→N vs N→1?** | Same code; print after the call for ascending, before it for descending. |
| **Why is sum-of-digits `O(log n)` and not `O(n)`?** | Each call removes a digit, and `n` has `log₁₀ n` digits. |
| **Why `start >= end` in palindrome?** | Even-length strings make the pointers cross without ever being equal. |
| **Why does `&&` matter in palindrome?** | It short-circuits, so a mismatch stops the recursion immediately. |
| **Fast power in one line?** | `x^n = (x^(n/2))²`, times `x` again when `n` is odd — `O(log n)`. |
| **The classic fast-power bug?** | Calling `f(x, n/2)` twice instead of storing it — that's `O(n)` again. |
| **Why is naive Fibonacci exponential?** | Two recursive calls per level, so the tree branches: `O(2ⁿ)` with massive recomputation. |
| **One call vs two calls?** | One → linear. Two or more → exponential unless memoised. |
| **Space for all of these?** | `O(depth)` for the call stack, even when nothing is allocated. |

---

## 13. The Mental Model

```text
             An "easy" recursion problem
                        │
                        ↓
        What is ONE piece I can peel off?
                        │
        ┌───────────────┼───────────────┐
        ↓               ↓               ↓
    a number        a digit         a character
     n → n−1       n%10, n/10      s[i] and s[j]
        │               │               │
        └───────────────┼───────────────┘
                        ↓
          How do I COMBINE it with f(rest)?
             +   ×   &&   swap   print
                        ↓
          How many calls do I make?
              one → O(n)      two → O(2ⁿ)
```
