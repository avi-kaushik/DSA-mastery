# 001 — Introduction to Recursion

> **One-line takeaway:** Recursion is a function solving a problem by calling itself on a **smaller version** of the same problem. Every recursive function needs exactly two things: a **base case** that stops it, and a **recursive case** that moves toward that base case.

---

## 1. What Is Recursion?

> **Recursion is when a function calls itself to solve a smaller instance of the same
> problem, until the problem becomes small enough to answer directly.**

The idea only works when a problem is **self-similar** — when the answer for `n` can be
expressed using the answer for something smaller.

```text
factorial(5) = 5 × factorial(4)
factorial(4) = 4 × factorial(3)
...
factorial(1) = 1              ← small enough to answer directly
```

Notice we never explained *how* to compute `factorial(4)`. We only said the bigger problem
is the smaller one plus a little work. That is the whole technique.

---

## 2. The Two Mandatory Parts

📄 [`RecursionPractice.java`](../../programs/java/techniques/recursion/basic/RecursionPractice.java)

```java
// Java
int factorial(int n) {
    if (n == 0) return 1;             // 1. BASE CASE — stop here
    return n * factorial(n - 1);      // 2. RECURSIVE CASE — smaller input
}
```

```cpp
// C++
int factorial(int n) {
    if (n == 0) return 1;             // 1. BASE CASE
    return n * factorial(n - 1);      // 2. RECURSIVE CASE
}
```

> The **syntax is identical** in Java and C++. What differs is the *runtime behaviour* —
> stack limits, overflow handling and tail-call optimisation. See §7.

| Part | Purpose | What happens without it |
|---|---|---|
| **Base case** | The smallest input, answered directly | Infinite recursion → stack overflow |
| **Recursive case** | Calls itself on a **smaller** input | Never reaches the base case → same crash |

### The rule to check every single time

```text
1. Is there a base case?
2. Does every recursive call move CLOSER to it?
```

```text
factorial(n - 1)     ✓ n shrinks toward 0
factorial(n)         ✗ never changes → infinite recursion
factorial(n + 1)     ✗ moves away → infinite recursion
```

> ⚠️ A base case that exists but is never *reached* is the same bug as having none.
> `factorial(-1)` runs forever even though `n == 0` is handled.

---

## 3. How It Actually Runs — the Call Stack

Each call gets its own **stack frame** holding its parameters and local variables. Calls
pile up on the way down, then unwind on the way back.

```text
factorial(4)

  call  factorial(4)   →  needs factorial(3)      [f4]
  call  factorial(3)   →  needs factorial(2)      [f4, f3]
  call  factorial(2)   →  needs factorial(1)      [f4, f3, f2]
  call  factorial(1)   →  needs factorial(0)      [f4, f3, f2, f1]
  call  factorial(0)   →  returns 1               [f4, f3, f2, f1, f0]  ← deepest
  ────────────────────────── unwinding ──────────────────────────
  factorial(1) = 1 × 1 = 1                        [f4, f3, f2, f1]
  factorial(2) = 2 × 1 = 2                        [f4, f3, f2]
  factorial(3) = 3 × 2 = 6                        [f4, f3]
  factorial(4) = 4 × 6 = 24                       [f4]
```

**Two phases, and both matter:**

```text
Winding   (going down)  →  calls are made, nothing is computed yet
Unwinding (coming back) →  results are combined
```

> Every recursive function costs `O(depth)` memory because of these frames — see
> [Space Complexity](../002-analysis-of-algorithms/015-space-complexity.md).

### ⚠️ When the stack runs out — and how the two languages differ

| | Java | C++ |
|---|---|---|
| **What happens** | Throws `StackOverflowError` | **Undefined behaviour** — usually a segfault / crash |
| **Can you catch it?** | Technically yes (it's a `Throwable`), but you shouldn't | No portable way |
| **Default stack size** | ~512 KB – 1 MB per thread | Linux: 8 MB main thread · Windows: 1 MB |
| **Rough usable depth** | ~10,000 – 20,000 frames | ~100,000+ frames (bigger stack, smaller frames) |
| **How to increase it** | `java -Xss8m` | `ulimit -s 65536` (Linux), linker flag on Windows |

```text
Missing / unreachable base case  → crash immediately
Correct code, but depth too big  → still crashes → rewrite iteratively
```

> **The important difference:** Java tells you what went wrong. C++ just dies — often with
> no useful message, and sometimes it silently corrupts memory first. Depth discipline
> matters more in C++.

---

## 4. ⚠️ Work Before vs After the Recursive Call

Two functions with the *same* structure produce opposite output, purely from where the
work sits.

📄 [`RecursionPractice.java`](../../programs/java/techniques/recursion/basic/RecursionPractice.java)

```java
// Java
void printNTo1(int n) {
    if (n == 0) return;
    System.out.println(n);      // work BEFORE the call
    printNTo1(n - 1);
}
// output: 5 4 3 2 1

void print1ToN(int n) {
    if (n == 0) return;
    print1ToN(n - 1);
    System.out.println(n);      // work AFTER the call
}
// output: 1 2 3 4 5
```

```cpp
// C++
void printNTo1(int n) {
    if (n == 0) return;
    std::cout << n << "\n";     // work BEFORE the call
    printNTo1(n - 1);
}
// output: 5 4 3 2 1

void print1ToN(int n) {
    if (n == 0) return;
    print1ToN(n - 1);
    std::cout << n << "\n";     // work AFTER the call
}
// output: 1 2 3 4 5
```

```text
Work BEFORE the call  →  happens on the way DOWN   (in call order)
Work AFTER the call   →  happens on the way UP     (in reverse order)
```

> **This is the single most useful insight for reading recursive code.** Anything placed
> after the recursive call runs in reverse — which is why the same pattern prints a linked
> list backwards, or builds a post-order traversal.

---

## 5. Recursion vs Iteration

Anything recursive can be written with a loop, and vice versa.

| | Recursion | Iteration |
|---|---|---|
| **Extra memory** | `O(depth)` call stack | `O(1)` |
| **Speed** | Slower — function-call overhead | Faster |
| **Code length** | Usually shorter, closer to the definition | Often more bookkeeping |
| **Risk** | Stack overflow | Infinite loop |
| **Best for** | Trees, graphs, backtracking, divide & conquer | Linear scans, simple accumulation |

```java
// Java — same result, O(1) space
int factIterative(int n) {
    int r = 1;
    for (int i = 2; i <= n; i++) r *= i;
    return r;
}
```

```cpp
// C++ — same result, O(1) space
int factIterative(int n) {
    int r = 1;
    for (int i = 2; i <= n; ++i) r *= i;
    return r;
}
```

> **Rule of thumb:** if the data structure is itself recursive (a tree, a nested
> structure), recursion is the natural fit. For a flat sequence, prefer a loop.

---

## 6. Types of Recursion

### By how it calls itself

```text
DIRECT      f() calls f()
INDIRECT    f() calls g(), and g() calls f()          (mutual recursion)
```

⚠️ In C++, mutual recursion needs a **forward declaration**, because the compiler reads
top to bottom:

```cpp
void g(int n);              // forward declaration — required
void f(int n) { if (n > 0) g(n - 1); }
void g(int n) { if (n > 0) f(n - 1); }
```

Java has no such requirement — methods in a class can call each other in any order.

### By how many calls per level

```text
LINEAR      one recursive call        factorial, sum of digits      → O(n) calls
TREE        two or more calls         fibonacci, tree traversal     → branches out
```

```text
LINEAR                    TREE
  f(4)                          f(4)
   │                          ╱     ╲
  f(3)                    f(3)       f(2)
   │                     ╱   ╲       ╱  ╲
  f(2)                f(2)   f(1)  f(1) f(0)
   │
  f(1)              one chain          exponential blow-up
```

### By where the call sits

```text
TAIL RECURSION      the recursive call is the LAST thing done
                    void f(int n) { if (n==0) return; print(n); f(n-1); }

NON-TAIL (HEAD)     work still remains after the call returns
                    int f(int n) { return n * f(n-1); }   ← the multiply waits
```

---

## 7. Recursion in Java vs C++

Same code, different guarantees. This is the section to reread before a C++ interview.

### Tail-call optimisation (TCO)

A tail call *could* reuse the current frame instead of pushing a new one, turning `O(n)`
stack into `O(1)`. Whether that happens depends entirely on the language and compiler.

```text
JAVA   ✗ NEVER optimises tail calls.
         The JVM keeps every frame (stack traces and security checks depend on them).
         A tail-recursive Java method is still O(n) space.

C++    ~ USUALLY optimises them — but it is NOT guaranteed by the standard.
         GCC / Clang do it at -O2 (via -foptimize-sibling-calls); MSVC does it too.
         At -O0 (a debug build) there is NO optimisation → it will still overflow.
```

```cpp
// C++ — tail recursive: nothing happens after the call returns
int sumTail(int n, int acc = 0) {
    if (n == 0) return acc;
    return sumTail(n - 1, acc + n);      // g++ -O2 turns this into a loop
}
```

```java
// Java — same shape, but ALWAYS uses O(n) stack
int sumTail(int n, int acc) {
    if (n == 0) return acc;
    return sumTail(n - 1, acc + n);      // no optimisation, ever
}
```

> **Never rely on TCO in an interview answer.** Say: *"C++ compilers usually eliminate
> tail calls at `-O2`, but it isn't guaranteed, and Java never does — so I'd treat the
> space as `O(n)` unless I've verified otherwise."*

### What a frame costs

```text
JAVA   Objects are references (4–8 bytes). Passing a big array or list copies
       only the reference → frame stays small.

C++    Passing by VALUE copies the whole object into every frame.
       void solve(std::vector<int> v)        ✗ O(n) copy per call → O(n·depth) space
       void solve(const std::vector<int>& v) ✓ pass by reference → O(1) per frame
```

> ⚠️ **This is the classic C++ recursion bug.** A backtracking function that takes a
> `vector` by value can be accidentally `O(n²)` in both time and space. Always pass
> containers by `const&` (or `&` when you mutate and undo).

### Other differences worth knowing

| Topic | Java | C++ |
|---|---|---|
| **Stack overflow** | `StackOverflowError` (diagnosable) | Undefined behaviour / segfault |
| **Default depth** | ~10⁴ frames | ~10⁵ frames (larger stack) |
| **Change stack size** | `-Xss` JVM flag | `ulimit -s`, or a dedicated thread |
| **Mutual recursion** | Works in any order | Needs a forward declaration |
| **Compile-time recursion** | Not available | `constexpr` — computed during compilation |
| **Recursive lambda** | Not directly (use a method) | Needs `std::function`, or `auto&& self` |

```cpp
// C++ only — evaluated at COMPILE time, zero runtime cost
constexpr int factorial(int n) {
    return (n == 0) ? 1 : n * factorial(n - 1);
}
static_assert(factorial(5) == 120);       // checked by the compiler
```

```cpp
// C++ — a recursive lambda (handy in competitive programming)
std::function<int(int)> fact = [&](int n) {
    return n == 0 ? 1 : n * fact(n - 1);
};
```

---

## 8. How to Think Recursively

The mistake is trying to trace the whole call tree in your head. Don't.

```text
1. What is the smallest input I can answer immediately?   → base case
2. If I ASSUME the function already works for n−1,
   how do I build the answer for n?                        → recursive case
3. Does my input actually shrink every call?               → termination
```

### The "recursive leap of faith"

> **Trust that the recursive call returns the right answer, and only write the one step
> that combines it.**

```java
int sum(int n) {
    if (n == 0) return 0;         // step 1
    return n + sum(n - 1);        // step 2: trust sum(n-1), add n
}
```

You never verify `sum(n-1)` by hand. You verify the base case and the single combining
step, and induction does the rest.

---

## 9. Common Mistakes

| ✗ Mistake | Result | Fix |
|---|---|---|
| No base case | Stack overflow | Always write the base case first |
| Base case unreachable | Same crash | Check the input truly shrinks |
| Wrong base value | Silently wrong answers | `factorial(0)` must be 1, not 0 |
| Forgetting to `return` the recursive call | Returns garbage / nothing | `return f(n-1)`, not just `f(n-1)` |
| Recomputing the same subproblem | Exponential time | Memoise — see below |
| Ignoring stack depth | Crash on large `n` | Convert to iteration |
| **C++:** passing containers by value | `O(n)` copy per frame | Pass by `const&` |
| **C++:** relying on TCO in a debug build | Overflow at `-O0` | Don't depend on it |

### The recomputation trap

```java
int fib(int n) {
    if (n <= 1) return n;
    return fib(n - 1) + fib(n - 2);     // fib(n-2) computed many times over
}
```

```text
Time  = O(2ⁿ)     ← catastrophic
Space = O(n)      ← depth only
```

`fib(40)` makes over 300 million calls. Caching results (memoisation) brings it to `O(n)`.

---

## 10. Analysing Recursive Code

Recursive cost is written as a **recurrence**, then solved:

```text
factorial     T(n) = T(n−1) + O(1)     → O(n)
fibonacci     T(n) = T(n−1) + T(n−2)   → O(2ⁿ)
binary search T(n) = T(n/2) + O(1)     → O(log n)
merge sort    T(n) = 2T(n/2) + O(n)    → O(n log n)
hanoi         T(n) = 2T(n−1) + O(1)    → O(2ⁿ)
```

```text
TIME  = total number of calls     (all nodes of the recursion tree)
SPACE = maximum depth             (one root-to-leaf path)
```

Full treatment in
[Recurrence Relations](../002-analysis-of-algorithms/011-recurrence-relations.md) and the
[Recursion Tree Method](../002-analysis-of-algorithms/013-recursion-tree-method.md).

---

## 11. Where Recursion Is Used

| Area | Why recursion fits |
|---|---|
| **Trees & graphs** | The structure is defined recursively — a tree is a node plus subtrees |
| **Divide & conquer** | Merge sort, quick sort, binary search |
| **Backtracking** | N-Queens, sudoku, subsets, permutations |
| **Dynamic programming** | Top-down memoised solutions |
| **Parsing** | Expressions, JSON, nested structures |
| **Classic puzzles** | Tower of Hanoi, Josephus problem |

Existing programs in this repo:
📄 [`Palindrome.java`](../../programs/java/techniques/recursion/basic/Palindrome.java) ·
📄 [`TowerOfHanoi.java`](../../programs/java/techniques/recursion/intermediate/TowerOfHanoi.java) ·
📄 [`Permutations.java`](../../programs/java/techniques/recursion/intermediate/Permutations.java) ·
📄 [`GenerateSubset.java`](../../programs/java/techniques/recursion/intermediate/GenerateSubset.java)

---

## 12. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What is recursion?** | A function that solves a problem by calling itself on a smaller instance of the same problem. |
| **What are its two required parts?** | A base case that stops it, and a recursive case that moves toward that base case. |
| **What happens without a base case?** | Infinite recursion until the call stack overflows. |
| **How does it work internally?** | Each call gets a stack frame; frames pile up while winding down and unwind as results return. |
| **Why does recursion use memory?** | Every pending call holds a frame, so space is `O(maximum depth)`. |
| **Time vs space for recursion?** | Time follows the total number of calls; space follows only the depth. |
| **Work before vs after the recursive call?** | Before → runs in call order (top-down); after → runs in reverse order (bottom-up). |
| **Recursion vs iteration?** | Iteration is faster and `O(1)` space; recursion is clearer for recursive structures like trees. |
| **What is tail recursion?** | The recursive call is the last operation performed. |
| **Is tail recursion optimised?** | C++ compilers usually do at `-O2` (not guaranteed, and never at `-O0`); **Java never does**. |
| **Stack overflow in Java vs C++?** | Java throws `StackOverflowError`; C++ is undefined behaviour — typically a segfault. |
| **Typical safe depth?** | ~10⁴ frames in Java, ~10⁵ in C++ (bigger default stack). |
| **The classic C++ recursion bug?** | Passing a container by value — it copies per frame. Use `const&`. |
| **Linear vs tree recursion?** | One call per level (`O(n)` calls) vs multiple calls per level (branches exponentially). |
| **Why is naive Fibonacci so slow?** | It recomputes the same subproblems — `O(2ⁿ)` time; memoisation reduces it to `O(n)`. |
| **Anything C++ can do that Java can't here?** | `constexpr` recursion, evaluated entirely at compile time. |

---

## 13. The Mental Model

```text
                  A recursive problem
                          │
                          ↓
         Can I express f(n) using f(smaller)?
                          │
          ┌───────────────┴───────────────┐
          ↓                               ↓
     BASE CASE                      RECURSIVE CASE
  smallest input,                 do a little work +
  answered directly               call on smaller input
          │                               │
          └───────────────┬───────────────┘
                          ↓
                    winding down
                  (frames pile up)
                          ↓
                    base case hit
                          ↓
                     unwinding
                 (results combine)
```
