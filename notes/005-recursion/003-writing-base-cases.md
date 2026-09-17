# 003 — Writing Base Cases

> **One-line takeaway:** The base case is the smallest input you can answer **without recursing**. Getting it wrong is the #1 source of recursion bugs — and the three things to get right are *when* it triggers, *what value* it returns, and whether every call actually *reaches* it.

---

## 1. What a Base Case Really Is

> **The base case is the point where the problem is small enough to answer directly.**

It is not "the place where you stop" — it is **an answer**. The recursive case builds on
top of that answer, so a wrong base value silently poisons every result above it.

```cpp
int factorial(int n) {
    if (n == 0) return 1;             // ← an ANSWER: 0! = 1
    return n * factorial(n - 1);
}
```

```text
BASE CASE      = smallest input, answered directly, no recursion
RECURSIVE CASE = a little work + a call on something smaller
```

---

## 2. Three Questions to Write One

```text
1. WHEN does it trigger?     the condition
2. WHAT does it return?      the value
3. Is it REACHABLE?          does every path shrink toward it?
```

Miss any one and you get a different bug:

| Missed | Symptom |
|---|---|
| Condition wrong | Infinite recursion, or stops one step too early/late |
| Value wrong | Runs fine, returns wrong answers |
| Not reachable | Infinite recursion even though a base case exists |

---

## 3. The Condition — Pick the Smallest *Valid* Input

Start from the question: **what is the tiniest input where the answer is obvious?**

| Input shape | Typical base condition |
|---|---|
| A counter `n` | `n == 0` (or `n == 1`) |
| Array with index `i` | `i == arr.size()` or `i < 0` |
| Range `(start, end)` | `start >= end` or `start > end` |
| String | `s.empty()` or `start >= end` |
| Linked list | `node == nullptr` |
| Tree | `root == nullptr` |
| 2D grid `(r, c)` | out of bounds, or the target cell |

📄 [`print_numbers.cpp`](../../programs/cpp/techniques/recursion/print_numbers.cpp)

```cpp
void print_number_series(int n) {
    if (n == 0) return;               // nothing left to print
    print_number_series(n - 1);
    cout << n;
}
```

---

## 4. ⚠️ `>=` vs `==` — the Off-by-One That Bites

📄 [`palindrome.cpp`](../../programs/cpp/techniques/recursion/palindrome.cpp)

```cpp
bool is_palindrome(const string &word, int start, int end) {
    if (start >= end) return true;    // ← why >= and not ==?
    return word[start] == word[end] &&
           is_palindrome(word, start + 1, end - 1);
}
```

Because the two pointers **don't always meet** — for even-length strings they *cross*:

```text
"madam"  (odd, n = 5)          "abba"  (even, n = 4)
 0 1 2 3 4                      0 1 2 3
 start=0 end=4                  start=0 end=3
 start=1 end=3                  start=1 end=2
 start=2 end=2  ← they MEET     start=2 end=1  ← they CROSSED
        ==  works                      == never true → infinite recursion 💥
```

> **Rule:** when two indices move toward each other, the base case must catch **meeting
> *and* crossing**. `start >= end`, never `start == end`.

---

## 5. The Value — Use the Identity Element

The base case must return the value that **doesn't change the result** when combined.

| Combining operation | Base value | Why |
|---|---|---|
| `+` (sum) | `0` | `x + 0 = x` |
| `*` (product) | `1` | `x * 1 = x` |
| `&&` (all true) | `true` | `x && true = x` |
| `\|\|` (any true) | `false` | `x \|\| false = x` |
| `max` | `INT_MIN` / sentinel | never wins by accident |
| `min` | `INT_MAX` / sentinel | same |
| string concat | `""` | empty is neutral |
| counting paths | `1` at the target | one way to "already be there" |

```cpp
int sum(int n)  { if (n == 0) return 0; return n + sum(n - 1); }   // 0 for +
int fact(int n) { if (n == 0) return 1; return n * fact(n - 1); }  // 1 for *
```

> ⚠️ `fact(n)` with `return 0` compiles, runs, never crashes — and returns **0 for every
> input**. A wrong base *value* is the quietest recursion bug there is.

---

## 6. Sometimes You Need More Than One

### Two base cases — Fibonacci

```cpp
int fib(int n) {
    if (n == 0) return 0;
    if (n == 1) return 1;             // BOTH are needed
    return fib(n - 1) + fib(n - 2);
}
```

Because `fib(2)` calls `fib(1)` **and** `fib(0)`. With only `n == 0`, the call `fib(1)`
would recurse into `fib(-1)` and run away.

> **Rule:** you need one base case for **every way** the recursion can bottom out. A
> function that steps down by 2 needs two of them.

### A "success" base and a "failure" base — Rope Cutting

📄 [`rope_cutting.cpp`](../../programs/cpp/techniques/recursion/rope_cutting.cpp)

```cpp
if (n == 0) return 0;                 // SUCCESS: cut exactly, 0 more pieces
if (n < 0)  return -1;                // FAILURE: overshot, this path is invalid
```

These must be **different values**, because `0` is a legitimate answer ("the rope is fully
used") while `-1` means "impossible". Collapsing both to `0` would report success for
combinations that overshoot.

```text
n == 0  →   0   valid, contributes to the count
n <  0  →  -1   invalid, must poison the whole branch
```

> This is the single subtlety of the whole problem — see
> [Classic Recursion Problems](006-classic-recursion-problems.md).

---

## 7. Base Case vs Input Validation

They look alike and are not the same thing.

```cpp
int get_maximum_cuts(int n, int a, int b, int c) {
    if (a <= 0 || b <= 0 || c <= 0) return -1;   // VALIDATION — bad arguments
    if (n == 0) return 0;                        // BASE CASE — smallest valid input
    if (n < 0)  return -1;                       // BASE CASE — dead branch
    ...
}
```

| | Base case | Validation / guard |
|---|---|---|
| **Purpose** | Answers the smallest input | Rejects illegal input |
| **Runs** | On every recursive descent | Ideally once, at the top |
| **Example** | `n == 0` | `n < 0` is not allowed at all |

📄 [`print_numbers.cpp`](../../programs/cpp/techniques/recursion/print_numbers.cpp) throws
on `n < 0` — that's validation. Re-checking it in every frame is harmless but wasteful;
the cleaner pattern is a **public wrapper that validates once**, calling a private
recursive helper that assumes valid input:

```cpp
bool is_palindrome(const string &word) {          // public: sets up the call
    return is_palindrome(word, 0, word.length() - 1);
}
bool is_palindrome(const string &w, int s, int e) // private: base case only
```

> `palindrome.cpp` already uses this wrapper shape. It's the idiom to copy.

---

## 8. ⚠️ Reachability — the Silent Killer

A base case that exists but is never hit is the same bug as having none.

```cpp
int f(int n) {
    if (n == 0) return 1;
    return f(n - 2);          // ✗ f(5) → 3 → 1 → -1 → -3 ... never hits 0
}
```

```text
Every recursive call must move the input CLOSER to the base case,
by a strictly positive amount, on EVERY path.
```

Common reachability failures:

```text
f(n)          ✗ input never changes
f(n + 1)      ✗ moves away
f(n - 2)      ✗ skips over n == 0 for odd n   → use  n <= 0
f(n / 2)      ⚠️ for n == 1, integer division gives 0 — fine; but n/2 with n==0 loops
arr[i]        ✗ if i isn't advanced on some branch
```

> **Quick fix for step-by-more-than-one:** widen the condition. `n <= 0` instead of
> `n == 0` catches every overshoot.

---

## 9. Base Cases by Problem Shape — Cheat Table

| Problem | Base case | Returns |
|---|---|---|
| `factorial(n)` | `n == 0` | `1` |
| `sum(1..n)` | `n == 0` | `0` |
| `fib(n)` | `n == 0`, `n == 1` | `0`, `1` |
| `print 1..n` | `n == 0` | nothing (`void`) |
| `is_palindrome(s, i, j)` | `i >= j` | `true` |
| `power(x, n)` | `n == 0` | `1` |
| `sum of digits(n)` | `n == 0` | `0` |
| `gcd(a, b)` | `b == 0` | `a` |
| `reverse list(node)` | `node == nullptr` | `nullptr` |
| `tree height(root)` | `root == nullptr` | `0` |
| `binary search(lo, hi)` | `lo > hi` | `-1` (not found) |
| `hanoi(n)` | `n == 0` | nothing |
| `josephus(n, k)` | `n == 0` (or `n == 1`) | `0` |
| `rope cut(n)` | `n == 0` / `n < 0` | `0` / `-1` |

---

## 10. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| No base case | Immediate stack overflow |
| `start == end` for two-pointer | Infinite recursion on even lengths |
| Wrong base value (`0` for a product) | Always returns 0 — silent |
| One base case where two are needed | Runs past the bottom (`fib(-1)`) |
| Same value for "done" and "impossible" | Invalid paths counted as valid |
| Base case placed *after* the recursive call | It never runs — recurse first, crash first |
| Validating inside the recursion | Works, but wasteful; validate in a wrapper |

```cpp
// ✗ base case unreachable — the call happens first
void f(int n) {
    f(n - 1);
    if (n == 0) return;
}
```

---

## 11. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What is a base case?** | The smallest input answered directly, without recursing — it's an answer, not just a stop. |
| **What three things must it get right?** | The condition, the returned value, and being reachable from every path. |
| **Why `start >= end` and not `==`?** | Two pointers moving toward each other can cross without meeting on even-length input. |
| **Why does `factorial` return 1 and `sum` return 0?** | The identity element of the combining operation — `x*1 = x`, `x+0 = x`. |
| **Why does Fibonacci need two base cases?** | It steps down by 1 *and* 2, so it can bottom out in two different places. |
| **Base case vs validation?** | The base case answers the smallest valid input; validation rejects illegal input, ideally once in a wrapper. |
| **Why does rope cutting need `0` and `-1`?** | `0` means the rope was cut exactly; `-1` means impossible. Merging them would count invalid paths as valid. |
| **What if the input steps by 2?** | Use `n <= 0`, otherwise odd inputs skip straight over `n == 0`. |
| **Worst kind of base-case bug?** | A wrong return *value* — it never crashes, it just quietly returns wrong answers. |

---

## 12. The Mental Model

```text
              Writing a recursive function
                          │
                          ↓
         What is the SMALLEST input I can answer?
                          │
          ┌───────────────┼───────────────┐
          ↓               ↓               ↓
       WHEN?           WHAT?         REACHABLE?
    the condition    the value      does n shrink
          │               │          on every path?
   n==0 / i>=j /     identity:            │
   node==nullptr     0, 1, true,          │
          │          "", nullptr          │
          └───────────────┼───────────────┘
                          ↓
              Does the recursion need MORE
              than one base case?
                  (fib → 2, rope → success + failure)
```
