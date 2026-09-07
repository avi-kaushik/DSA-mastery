# 002 — Tail Recursion

> **One-line takeaway:** A call is **tail recursive** when the recursive call is the *very last thing* the function does — nothing is left pending after it returns. That lets a compiler reuse the current stack frame instead of pushing a new one, turning `O(n)` space into `O(1)`. **C++ usually does this; Java never does.**

---

## 1. What Is Tail Recursion?

> **A recursive call is in *tail position* if its result is returned directly, with no
> further work waiting to happen after it.**

```java
// TAIL — nothing happens after the call returns
int sum(int n, int acc) {
    if (n == 0) return acc;
    return sum(n - 1, acc + n);      // ← the answer IS the call's answer
}
```

```java
// NOT TAIL — a multiplication is still pending
int fact(int n) {
    if (n == 0) return 1;
    return n * fact(n - 1);          // ← must wait for the call, then multiply
}
```

The difference is small on the page but total in behaviour: in the first, the current frame
has nothing left to do; in the second, it must survive until the child returns.

---

## 2. How to Spot It — One Question

> **"After the recursive call returns, does this function still have work to do?"**

```text
No  → tail recursive
Yes → not tail recursive
```

| Code | Tail? | Why |
|---|---|---|
| `return f(n - 1);` | ✅ | Returned as-is |
| `return n * f(n - 1);` | ❌ | Multiply pending |
| `return f(n - 1) + 1;` | ❌ | Addition pending |
| `return 1 + f(n - 1);` | ❌ | Same — addition pending |
| `f(n - 1); return;` | ✅ | `void`, nothing after |
| `f(n - 1); print(n);` | ❌ | A print is pending |
| `return cond ? f(a) : f(b);` | ✅ | Both branches return directly |
| `return f(a) + f(b);` | ❌ | Two calls, both must return first |

> ⚠️ **Tree recursion can never be fully tail recursive.** With two recursive calls, the
> first one always has the second still waiting behind it.

---

## 3. Head vs Tail Recursion

📄 [`print_numbers.cpp`](../../programs/cpp/techniques/recursion/print_numbers.cpp) ·
📄 [`RecursionPractice.java`](../../programs/java/techniques/recursion/basic/RecursionPractice.java)

The two functions in `print_numbers.cpp` are this distinction exactly:

```cpp
// HEAD recursion — work happens on the way back UP
void print_number_series(int n) {
    if (n == 0) return;
    print_number_series(n - 1);
    cout << n;                  // pending work → NOT tail
}

// TAIL recursion — work happens on the way DOWN
void print_number_series_reverse(int n) {
    if (n == 0) return;
    cout << n;
    print_number_series_reverse(n - 1);   // nothing after → tail
}
```

```java
// the same pair in Java
void print1ToN(int n) {
    if (n == 0) return;
    print1ToN(n - 1);
    System.out.println(n);      // pending work → NOT tail
}

void printNTo1(int n) {
    if (n == 0) return;
    System.out.println(n);
    printNTo1(n - 1);           // nothing after → tail
}
```

```text
HEAD  →  frames must be kept, because each one still has work to do
TAIL  →  frames are finished; only the call itself remains
```

> Both are documented as `Space: O(n)` for the call stack, which is correct **as written**.
> But `print_number_series_reverse` is tail recursive, so `g++ -O2` can collapse it to a
> loop and make it `O(1)` — while `print_number_series` stays `O(n)` no matter the
> optimisation level, because the `cout` is still pending. Same file, same complexity
> comment, different ceiling.

That's the connection to §4 of
[Introduction to Recursion](001-introduction-to-recursion.md): **work after the call is
exactly what makes a function non-tail.**

---

## 4. Why It Matters — Tail Call Optimisation

If nothing is pending, the current frame is dead weight. A compiler can **overwrite it**
instead of pushing a new one — the call becomes a jump, and the recursion becomes a loop.

```text
WITHOUT TCO                        WITH TCO
                                   
[f(5)]                             [f(5)]   ← frame reused
[f(5)][f(4)]                       [f(4)]   ← same slot
[f(5)][f(4)][f(3)]                 [f(3)]   ← same slot
[f(5)][f(4)][f(3)][f(2)]           [f(2)]
[f(5)][f(4)][f(3)][f(2)][f(1)]     [f(1)]
                                   
Space = O(n)                       Space = O(1)
Risk  = stack overflow             Risk  = none
```

```text
Time  = O(n) either way — TCO saves SPACE, not time.
```

> The optimisation is also called **sibling call optimisation**, because it applies to a
> tail call to *any* function, not just the same one.

---

## 5. ⚠️ Language Support — the Part That Actually Matters

| Language | Tail call optimised? | Notes |
|---|---|---|
| **Java** | ❌ **Never** | The JVM keeps every frame — stack traces and the security model walk the stack. Not in the JVM spec, and no version has added it. |
| **C++** | ⚠️ **Usually, not guaranteed** | GCC/Clang at `-O2` (`-foptimize-sibling-calls`), MSVC at `/O2`. **Nothing at `-O0`.** |
| **C** | ⚠️ Same as C++ | Compiler-dependent optimisation |
| **Kotlin** | ✅ With `tailrec` | The compiler *errors* if the function isn't actually tail recursive |
| **Scala** | ✅ With `@tailrec` | Same — compile-time verified |
| **Scheme / Erlang** | ✅ Guaranteed | Required by the language standard |
| **Python** | ❌ Never | Default limit ~1000 frames |

### The two you care about

```java
// Java — tail recursive in shape, still O(n) stack. It WILL overflow.
int sum(int n, int acc) {
    if (n == 0) return acc;
    return sum(n - 1, acc + n);       // sum(1_000_000, 0) → StackOverflowError
}
```

```cpp
// C++ — the same function, compiled with g++ -O2, runs in O(1) space
int sum(int n, int acc = 0) {
    if (n == 0) return acc;
    return sum(n - 1, acc + n);       // sum(1'000'000) is fine at -O2
}
// ...but the SAME code at -O0 crashes. The optimisation is not part of the language.
```

### ⚠️ C++ gotcha: destructors can silently break TCO

A local object with a non-trivial destructor must be destroyed **after** the call returns —
so there *is* pending work, and the call is no longer in tail position:

```cpp
int f(int n) {
    std::vector<int> tmp(100);        // destructor must run after the call
    if (n == 0) return 0;
    return f(n - 1);                  // ✗ NOT a real tail call — TCO won't apply
}
```

> This is why you cannot rely on TCO in C++ by inspection alone. If it matters, verify with
> `g++ -O2 -S` and look for a `jmp` instead of a `call`.

**The safe interview line:** *"C++ compilers usually eliminate tail calls at `-O2`, but the
standard doesn't guarantee it and Java never does — so I'd treat the space as `O(n)` unless
I've verified otherwise."*

---

## 6. 🧮 Converting Non-Tail → Tail with an Accumulator

The standard technique: **carry the partial result down as a parameter** instead of
building it up on the way back.

### Factorial

```java
// NOT tail — the multiply waits for the call
int fact(int n) {
    if (n == 0) return 1;
    return n * fact(n - 1);
}

// TAIL — multiply BEFORE recursing, pass the result down
int fact(int n, int acc) {
    if (n == 0) return acc;
    return fact(n - 1, acc * n);
}
int fact(int n) { return fact(n, 1); }    // wrapper hides the accumulator
```

```text
fact(4)                     fact(4, 1)
= 4 * fact(3)               = fact(3, 4)
= 4 * (3 * fact(2))         = fact(2, 12)
= 4 * (3 * (2 * fact(1)))   = fact(1, 24)
= ... unwinds to 24         = fact(0, 24) = 24     ← nothing to unwind
```

> **The key shift:** the non-tail version computes on the way *up*; the tail version
> computes on the way *down*, so by the time it hits the base case the answer is already
> finished.

### Fibonacci — needs two accumulators

```cpp
// C++ — O(n) time, and O(1) space with TCO
int fib(int n, int a = 0, int b = 1) {
    if (n == 0) return a;
    return fib(n - 1, b, a + b);
}
```

Compare with the naive `fib(n-1) + fib(n-2)`, which is `O(2ⁿ)` *and* not tail recursive.

### Reversing a linked list

```java
Node reverse(Node curr, Node prev) {
    if (curr == null) return prev;
    Node next = curr.next;
    curr.next = prev;
    return reverse(next, curr);        // tail
}
```

---

## 7. Tail Recursion → Loop (Mechanical)

Once a function is tail recursive, converting it to iteration is **purely mechanical** —
which is the real payoff in a language like Java that won't do it for you.

```text
int f(params) {                    int f(params) {
    if (base) return acc;              while (!base) {
    return f(newParams);       →           params = newParams;
}                                      }
                                       return acc;
                                   }
```

```java
// tail recursive
int sum(int n, int acc) {
    if (n == 0) return acc;
    return sum(n - 1, acc + n);
}

// mechanical loop version — O(1) space, no compiler magic needed
int sum(int n) {
    int acc = 0;
    while (n != 0) { acc += n; n--; }
    return acc;
}
```

> **Rule of thumb for Java:** if you've written it tail recursively, you've already done the
> hard part — just write the loop. That's the optimisation, done by hand.

---

## 8. When Tail Recursion Is *Not* Worth It

```text
✗ Tree recursion       two calls → the first can never be in tail position
                       (tree traversal, merge sort, naive fibonacci)

✗ Readability cost     fact(n, acc) is less obvious than n * fact(n-1);
                       the accumulator is bookkeeping, not the idea

✗ Java                 you gain no space at all — write the loop instead

✗ Debug builds in C++  -O0 gives no optimisation, so it still overflows
```

> **Where it genuinely pays:** a deep *linear* recursion in C++ compiled with optimisation,
> or a language with guaranteed TCO. Everywhere else, treat "make it tail recursive" as a
> step on the way to writing a loop.

---

## 9. Common Mistakes

| ✗ Mistake | Why it's wrong |
|---|---|
| "`return n * f(n-1);` is tail recursive" | The multiply is pending — it isn't |
| "Java optimises tail calls" | It never has and doesn't plan to |
| "Tail recursion is faster" | It saves **space**, not time — same `O(n)` work |
| "Any recursion can be made tail recursive" | Tree recursion can't be, without an explicit stack |
| Relying on TCO in a C++ debug build | `-O0` performs no elimination |
| Forgetting the wrapper | Callers shouldn't have to pass `acc` — hide it |
| Wrong accumulator seed | `fact(n, 0)` returns 0 for every input; it must be 1 |

---

## 10. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What is tail recursion?** | The recursive call is the last operation — its result is returned directly with no pending work. |
| **How do you test for it?** | Ask whether the function still has work to do after the call returns. If yes, it isn't tail. |
| **Is `return n * f(n-1)` tail recursive?** | No — the multiplication is pending. |
| **Why does it matter?** | The frame can be reused, so space drops from `O(n)` to `O(1)` and the recursion becomes a loop. |
| **Does it make code faster?** | No — it saves space, not time. |
| **Does Java optimise tail calls?** | Never. Stack traces and the security model require the frames. |
| **Does C++?** | Usually at `-O2` (GCC/Clang/MSVC), but it isn't guaranteed by the standard and doesn't happen at `-O0`. |
| **What can silently block TCO in C++?** | A local object with a non-trivial destructor — it must run after the call, so the call isn't in tail position. |
| **How do you make a function tail recursive?** | Add an accumulator parameter and compute on the way down instead of on the way back up. |
| **Tail factorial?** | `fact(n, acc) → fact(n-1, acc*n)`, seeded with `acc = 1`, behind a wrapper. |
| **Can tree recursion be tail recursive?** | No — the first of two calls always has the second waiting behind it. |
| **Which languages guarantee TCO?** | Scheme and Erlang by standard; Kotlin (`tailrec`) and Scala (`@tailrec`) verify it at compile time. |
| **Head vs tail recursion?** | Head does its work on the way up (frames must be kept); tail does its work on the way down. |

---

## 11. The Mental Model

```text
                    A recursive call
                           │
                           ↓
        Is anything left to do after it returns?
                           │
            ┌──────────────┴──────────────┐
           YES                            NO
            ↓                             ↓
       NOT TAIL                        TAIL CALL
   frame must be kept              frame can be reused
            ↓                             ↓
        O(n) space              ┌─────────┴─────────┐
            ↓                   ↓                   ↓
   add an accumulator      C++ at -O2           Java (always)
   to move work downward    → O(1) space        → still O(n)
            ↓                                        ↓
        TAIL CALL                            write the loop yourself
```
