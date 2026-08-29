# Asymptotic Analysis — Brief Notes

> **Asymptotic Analysis is the study of how an algorithm's resource requirements grow as the input size becomes very large.**

---

## 1. Why Do We Need Asymptotic Analysis?

Suppose two algorithms solve the same problem:

```js
// Algorithm A
for (let i = 0; i < n; i++) {
    // work
}
```

```js
// Algorithm B
for (let i = 0; i < n; i++) {
    for (let j = 0; j < n; j++) {
        // work
    }
}
```

For `n = 10`:

```text
A → ~10 operations
B → ~100 operations
```

For `n = 1,000`:

```text
A → ~1,000
B → ~1,000,000
```

For `n = 1,000,000`:

```text
A → ~1,000,000
B → ~1,000,000,000,000
```

The difference becomes enormous as the input grows.

Therefore, instead of asking:

> "How many milliseconds does this algorithm take on my computer?"

we ask:

> **"How does the amount of work grow as the input size `n` grows?"**

That growth behavior is what asymptotic analysis describes.

---

# 2. What Does "Asymptotic" Mean?

**Asymptotic** refers to the behavior of a function as its input approaches infinity.

In algorithm analysis:

```text
n → ∞
```

We are interested in what happens when the input becomes extremely large.

For example:

```text
f(n) = 3n + 10
```

As `n` becomes very large, the `3n` term dominates the constant `10`.

So:

```text
3n + 10  →  O(n)
```

The exact value of `3n + 10` is less important than the fact that its growth is **linear**.

### Core idea

> **Asymptotic analysis focuses on growth rate rather than exact execution time.**

---

# 3. What Do We Analyze?

There are two main resources.

```text
                 Asymptotic Analysis
                         │
              ┌──────────┴──────────┐
              ↓                     ↓
       Time Complexity       Space Complexity
              │                     │
       How computation       How memory usage
       grows with n          grows with n
```

### Time Complexity

Measures how the amount of computational work grows with input size.

### Space Complexity

Measures how memory requirements grow with input size.

---

# 4. Input Size — What Is `n`?

Before calculating complexity, first identify what the input size actually means.

For example:

```text
Array       → n = number of elements
String      → n = number of characters
Linked List → n = number of nodes
Graph       → V = vertices, E = edges
Matrix      → may use rows × columns
```

If a matrix contains `n` rows and `m` columns:

```text
Total elements = n × m
```

So visiting every cell takes:

```text
O(nm)
```

not automatically `O(n²)`.

> **Never calculate complexity before identifying what the input variables represent.**

---

# 5. Time Cost Model / RAM Model

Before deriving Big-O, we need a way to estimate the amount of computational work.

A commonly used theoretical model is the:

> **RAM (Random Access Machine) Model**, also called the **unit-cost model**.

The basic assumption is that fundamental operations take constant time.

For example:

```text
Assignment              → constant
Arithmetic operation    → constant
Comparison              → constant
Variable access         → constant
Increment/decrement     → constant
Array indexing*         → constant
```

So conceptually:

```js
x = a + b;
```

can be viewed as a constant amount of work:

```text
Read a
Read b
Add
Assign result
```

Therefore:

```text
Cost = O(1)
```

### Why do we need this model?

Actual execution time depends on many things:

```text
CPU
RAM
Cache
Programming language
Compiler
Operating system
Hardware
Implementation details
```

So instead of measuring milliseconds, we count **basic operations** under a simplified model.

This allows us to compare algorithms independently of a particular machine.

> **The Time Cost Model converts an algorithm into a mathematical function describing how its computational work grows with `n`.**

---

# 6. Computing Time Cost

Consider:

```js
let sum = 0;

for (let i = 0; i < n; i++) {
    sum = sum + arr[i];
}
```

Each iteration performs a constant amount of work.

If one iteration costs approximately `c`, then:

```text
Total Cost ≈ c × n
```

So:

```text
T(n) = cn
```

and therefore:

```text
T(n) = O(n)
```

We can even conceptually count the operations:

```text
Initialization       → 1
Loop conditions      → n + 1
Increment            → n
Array access         → n
Addition             → n
Assignment           → n
```

The exact expression might look like:

```text
T(n) = c₁ + c₂(n + 1) + c₃n + c₄n + c₅n + c₆n
```

which simplifies to:

```text
T(n) = an + b
```

Therefore:

```text
T(n) = O(n)
```

### Important

We normally don't need to count every CPU instruction.

The purpose of the cost model is to determine:

> **How many constant-cost operations are performed as a function of `n`?**

---

# 7. Why Don't We Care About Exact Constants?

Suppose:

```text
T₁(n) = 5n
T₂(n) = 100n
```

Both are:

```text
O(n)
```

because both grow **linearly**.

If `n` doubles:

```text
5n    → 10n
100n  → 200n
```

Both grow by the same factor.

The constants affect actual performance, but they don't change the fundamental growth pattern.

Therefore:

```text
O(5n) → O(n)
O(100n) → O(n)
O(n/2) → O(n)
```

---

# 8. Why Do We Ignore Lower-Order Terms?

Consider:

```text
T(n) = n² + n + 100
```

For small `n`, all terms may matter.

But as `n` becomes extremely large:

```text
n²
```

grows much faster than:

```text
n
```

and `n` grows much faster than the constant `100`.

For example, at:

```text
n = 1,000,000
```

we get:

```text
n²  = 1,000,000,000,000
n   = 1,000,000
100 = 100
```

The `n²` term dominates.

Therefore:

```text
n² + n + 100
        ↓
      O(n²)
```

### Simplification Rule

When deriving Big-O:

1. Remove constant factors.
2. Keep the fastest-growing term.
3. Ignore lower-order terms.

---

# 9. Common Growth Rates

The most important complexity classes are:

```text
O(1)
O(log n)
O(n)
O(n log n)
O(n²)
O(n³)
O(2ⁿ)
O(n!)
```

Their general growth relationship is:

```text
O(1)
   <
O(log n)
   <
O(n)
   <
O(n log n)
   <
O(n²)
   <
O(n³)
   <
O(2ⁿ)
   <
O(n!)
```

As `n` becomes large, each class further down the list generally becomes much more expensive.

---

# 10. O(1) — Constant Time

An algorithm is `O(1)` when its amount of work does not depend on the size of the input.

Example:

```js
function getFirst(arr) {
    return arr[0];
}
```

Whether the array has:

```text
10 elements
```

or:

```text
10,000,000 elements
```

we perform essentially the same operation.

Therefore:

```text
Time = O(1)
```

### Important

`O(1)` does not literally mean "one operation."

It means:

> **A bounded/constant amount of work independent of `n`.**

For example:

```text
100 operations
```

is still:

```text
O(1)
```

---

# 11. O(log n) — Logarithmic Time

Logarithmic complexity usually appears when the problem size is repeatedly reduced by a constant factor.

Consider:

```text
n
↓
n/2
↓
n/4
↓
n/8
↓
...
↓
1
```

How many times can we divide `n` by 2 before reaching 1?

Approximately:

```text
log₂ n
```

Therefore:

```text
O(log n)
```

### Example

```js
for (let i = 1; i < n; i *= 2) {
    // work
}
```

The values are:

```text
1
2
4
8
16
32
64
...
```

After `k` iterations:

```text
i = 2ᵏ
```

We stop when:

```text
2ᵏ ≥ n
```

Therefore:

```text
k ≈ log₂ n
```

So:

```text
Time = O(log n)
```

### Mental Model

> **Repeatedly dividing or multiplying the problem size by a constant → think `O(log n)`.**

Classic example:

```text
Binary Search → O(log n)
```

---

# 12. O(n) — Linear Time

The amount of work grows proportionally with `n`.

Example:

```js
function findMax(arr) {
    let max = arr[0];

    for (const value of arr) {
        if (value > max) {
            max = value;
        }
    }

    return max;
}
```

We may inspect every element.

```text
n = 10       → ~10 iterations
n = 1,000    → ~1,000 iterations
n = 1,000,000 → ~1,000,000 iterations
```

Therefore:

```text
O(n)
```

### Mental Model

> **Process each element once → usually `O(n)`.**

---

# 13. O(n log n)

This commonly appears when:

```text
n work
×
log n levels
```

Therefore:

```text
O(n log n)
```

Classic examples:

```text
Merge Sort
Heap Sort
```

A common code pattern:

```js
for (let i = 0; i < n; i++) {
    let x = n;

    while (x > 1) {
        x /= 2;
    }
}
```

The outer loop:

```text
O(n)
```

The inner loop repeatedly halves `x`:

```text
O(log n)
```

Therefore:

```text
O(n × log n)
= O(n log n)
```

---

# 14. O(n²) — Quadratic Time

Usually appears when we process every element against many/all other elements.

Example:

```js
for (let i = 0; i < n; i++) {
    for (let j = 0; j < n; j++) {
        // work
    }
}
```

Outer loop:

```text
n
```

Inner loop:

```text
n
```

Total:

```text
n × n = n²
```

Therefore:

```text
O(n²)
```

Typical examples:

```text
Bubble Sort
Selection Sort
Brute-force pair comparisons
```

### Mental Model

> **For each element, process every element again → often `O(n²)`.**

---

# 15. O(n³) — Cubic Time

Three independent nested loops:

```js
for (...) {
    for (...) {
        for (...) {
            // work
        }
    }
}
```

Total:

```text
n × n × n
= n³
```

Therefore:

```text
O(n³)
```

---

# 16. O(2ⁿ) — Exponential Time

Exponential complexity often appears when recursion repeatedly branches into multiple subproblems.

Example:

```js
function fib(n) {
    if (n <= 1) return n;

    return fib(n - 1) + fib(n - 2);
}
```

The recursion branches:

```text
                 fib(n)
                /      \
          fib(n-1)     fib(n-2)
           /   \         /   \
         ...   ...     ...   ...
```

The number of recursive calls grows exponentially.

The naive Fibonacci implementation is commonly characterized as:

```text
O(2ⁿ)
```

This becomes impractical very quickly.

---

# 17. O(n!) — Factorial Time

Factorial complexity commonly appears when generating every possible permutation.

Number of permutations of `n` elements:

```text
n!
```

For example:

```text
3! = 6
5! = 120
10! = 3,628,800
20! = 2,432,902,008,176,640,000
```

Therefore:

```text
Generating all permutations → O(n!)
```

This grows extremely rapidly.

---

# 18. Sequential vs Nested Work

This is one of the most important rules in complexity analysis.

### Sequential loops

```js
for (...) {
    // O(n)
}

for (...) {
    // O(n)
}
```

Add their costs:

```text
O(n) + O(n)
= O(2n)
= O(n)
```

### Nested loops

```js
for (...) {
    for (...) {
        // work
    }
}
```

Multiply their costs:

```text
O(n) × O(n)
= O(n²)
```

### Remember

```text
Sequential → ADD
Nested     → MULTIPLY
```

---

# 19. Different Input Sizes

Consider:

```js
for (let i = 0; i < n; i++) {
    for (let j = 0; j < m; j++) {
        // work
    }
}
```

The complexity is:

```text
O(nm)
```

Do not write `O(n²)` unless you know:

```text
m = n
```

This becomes especially important for:

```text
Matrices
Graphs
Multiple arrays
Multiple strings
```

---

# 20. Triangular Nested Loops

Consider:

```js
for (let i = 0; i < n; i++) {
    for (let j = i; j < n; j++) {
        // work
    }
}
```

The inner loop does not execute exactly `n` times every time.

The total work is approximately:

```text
(n - 1) + (n - 2) + ... + 1
```

Using the sum:

```text
n(n - 1) / 2
```

which expands to:

```text
(n² - n) / 2
```

Ignoring constants and lower-order terms:

```text
O(n²)
```

### Lesson

> **Don't just look at the number of loops. Determine how many times the body actually executes.**

---

# 21. Constant-Bound Loops

Consider:

```js
for (let i = 0; i < n; i++) {
    for (let j = 0; j < 10; j++) {
        // work
    }
}
```

The inner loop runs exactly 10 times.

Therefore:

```text
n × 10
= 10n
= O(n)
```

It is **not** `O(n²)`.

### Lesson

A nested loop is not automatically quadratic.

> **The bounds of the loops matter.**

---

# 22. Example: `O(n√n)`

```js
for (let i = 0; i < n; i++) {
    for (let j = 0; j < Math.sqrt(n); j++) {
        // work
    }
}
```

Outer loop:

```text
O(n)
```

Inner loop:

```text
O(√n)
```

Therefore:

```text
O(n√n)
```

which is also:

```text
O(n³ᐟ²)
```

---

# 23. Best Case, Average Case & Worst Case

An algorithm's complexity can depend on the input.

Consider Linear Search:

```js
function search(arr, target) {
    for (let i = 0; i < arr.length; i++) {
        if (arr[i] === target) {
            return i;
        }
    }

    return -1;
}
```

### Best Case

Target is the first element:

```text
1 comparison
```

Therefore:

```text
Best Case = O(1)
```

### Worst Case

Target is the last element or doesn't exist:

```text
n comparisons
```

Therefore:

```text
Worst Case = O(n)
```

### Average Case

On average, roughly half the elements may be inspected:

```text
n/2
```

But:

```text
O(n/2) = O(n)
```

Therefore:

```text
Average Case = O(n)
```

---

# 24. Big-O, Big-Ω and Big-Θ

These are the three major asymptotic notations.

```text
Big-O     → Upper Bound
Big-Ω     → Lower Bound
Big-Θ     → Tight Bound
```

### Big-O

Describes an asymptotic upper bound.

```text
f(n) = O(g(n))
```

Informally:

> `f(n)` does not grow faster than the order of `g(n)` asymptotically.

---

### Big-Ω

Describes an asymptotic lower bound.

```text
f(n) = Ω(g(n))
```

Informally:

> `f(n)` grows at least as fast as the order of `g(n)` asymptotically.

---

### Big-Θ

Describes a tight asymptotic bound.

```text
f(n) = Θ(g(n))
```

Informally:

> `f(n)` grows at the same asymptotic rate as `g(n)`.

For example:

```text
3n + 10 = O(n)
3n + 10 = Ω(n)
3n + 10 = Θ(n)
```

In practical DSA discussions, people frequently use **Big-O** as the general term for describing complexity, even when the tight bound is what they really mean.

---

# 25. Formal Definition of Big-O

Mathematically:

```text
f(n) = O(g(n))
```

if there exist positive constants `c` and `n₀` such that:

```text
0 ≤ f(n) ≤ c · g(n)
```

for every:

```text
n ≥ n₀
```

### Example

Suppose:

```text
f(n) = 3n + 5
```

For `n ≥ 1`:

```text
3n + 5 ≤ 3n + 5n
       = 8n
```

Therefore, choosing:

```text
c = 8
n₀ = 1
```

gives:

```text
3n + 5 = O(n)
```

You generally don't need formal proofs for every DSA problem, but this definition explains what Big-O means mathematically.

---

# 26. Time Complexity Is Not Actual Runtime

Suppose:

```text
Algorithm A → 2n + 100
Algorithm B → 1000n + 20
```

Both are:

```text
O(n)
```

But B can still be significantly slower in practice.

Big-O abstracts away:

```text
Constant factors
Hardware
CPU speed
Compiler optimizations
Language
Cache behavior
Implementation details
```

Its main purpose is to understand:

> **How well does the algorithm scale as the input grows?**

---

# 27. Space Complexity

Consider:

```js
function sum(arr) {
    let total = 0;

    for (const x of arr) {
        total += x;
    }

    return total;
}
```

The algorithm uses only a fixed number of extra variables:

```text
total
x
```

Therefore:

```text
Auxiliary Space = O(1)
```

Even though the input array contains `n` elements.

### Input Space vs Auxiliary Space

```text
Total Space
     │
     ├── Input Space
     │
     └── Auxiliary Space
             ↑
       Extra memory used
       by the algorithm
```

When we say an algorithm uses `O(1)` extra space, we usually mean:

> **Constant auxiliary space beyond the input.**

---

# 28. Example: O(n) Auxiliary Space

```js
function double(arr) {
    const result = [];

    for (const x of arr) {
        result.push(x * 2);
    }

    return result;
}
```

The result array contains `n` elements.

Therefore:

```text
Auxiliary Space = O(n)
```

---

# 29. Recursion and Space Complexity

Consider:

```js
function countdown(n) {
    if (n === 0) return;

    countdown(n - 1);
}
```

The recursive calls form a chain:

```text
countdown(n)
     ↓
countdown(n-1)
     ↓
countdown(n-2)
     ↓
...
     ↓
countdown(1)
```

There can be `n` active stack frames.

Therefore:

```text
Time  = O(n)
Space = O(n)
```

The memory comes from the **call stack**.

> **Recursive calls consume stack space.**

---

# 30. Time-Space Trade-Off

Sometimes we can make an algorithm faster by using additional memory.

Example:

```text
Brute Force:
Time  = O(n²)
Space = O(1)
```

Using hashing:

```text
Time  = O(n)
Space = O(n)
```

So:

```text
More Space
     ↓
Less Time
```

This is called a:

> **Time-Space Trade-Off**

There is not always one universally best algorithm. The constraints determine which trade-off is appropriate.

---

# 31. Constraints Determine Acceptable Complexity

Complexity should always be interpreted together with the input constraints.

Rough interview intuition:

```text
n ≤ 10
→ O(n!), O(2ⁿ) may be possible

n ≤ 20
→ O(2ⁿ) may be possible

n ≤ 100
→ O(n³) may be possible

n ≤ 1,000
→ O(n²) often possible

n ≤ 100,000
→ O(n log n) / O(n) usually preferred

n ≤ 1,000,000
→ O(n) or close is generally preferred
```

These are only heuristics.

Actual feasibility depends on:

```text
Time limit
Language
Constant factors
Operations performed
Hardware
```

### Important principle

> **An algorithm's complexity cannot be judged independently of its constraints.**

---

# 32. How to Analyze an Algorithm

When you see unfamiliar code, follow this process:

### Step 1 — Identify the input size

```text
What does n represent?
```

### Step 2 — Identify the basic operation

```text
What actual work is being repeated?
```

### Step 3 — Count iterations

```text
How many times does that work execute?
```

### Step 4 — Analyze loops

Ask:

```text
Sequential?
Nested?
Dependent?
```

### Step 5 — Look for growth/shrinkage

Patterns such as:

```text
i++
i *= 2
n /= 2
```

have different growth behavior.

### Step 6 — Analyze recursion

Ask:

```text
How many recursive calls?
How many branches?
How deep is the recursion?
How much work happens per call?
```

### Step 7 — Combine costs

```text
Sequential → ADD
Nested     → MULTIPLY
```

### Step 8 — Simplify

Remove:

```text
Constant factors
Lower-order terms
```

### Step 9 — Analyze space separately

Look for:

```text
Arrays
Hash Maps
Sets
Objects
Temporary data
Recursion stack
```

---

# 33. A Complete Example

Consider:

```js
function findPair(arr, target) {
    for (let i = 0; i < arr.length; i++) {
        for (let j = i + 1; j < arr.length; j++) {
            if (arr[i] + arr[j] === target) {
                return [i, j];
            }
        }
    }

    return [];
}
```

Let:

```text
n = arr.length
```

The outer loop runs approximately `n` times.

The inner loop runs:

```text
n - 1
n - 2
n - 3
...
1
```

times.

Total work:

```text
(n - 1) + (n - 2) + ... + 1
```

Using the sum:

```text
n(n - 1) / 2
```

which is:

```text
(n² - n) / 2
```

Ignoring constants and lower-order terms:

```text
Time = O(n²)
```

The algorithm uses only a constant amount of additional memory apart from the returned pair:

```text
Auxiliary Space = O(1)
```

Therefore:

```text
┌────────────────────┐
│ findPair           │
├────────────────────┤
│ Time  → O(n²)      │
│ Space → O(1)       │
└────────────────────┘
```

Now suppose we use a hash map/set to remember previously seen values.

We can potentially achieve:

```text
Time  → O(n)
Space → O(n)
```

This is a practical example of using complexity analysis to **identify a bottleneck and improve an algorithm**.

---

# 34. The Most Important Patterns

| Pattern | Complexity | Reason |
|---|---:|---|
| Fixed amount of work | `O(1)` | Independent of `n` |
| One traversal | `O(n)` | Work grows with `n` |
| Repeated halving | `O(log n)` | `n → n/2 → n/4...` |
| `n` work across `log n` levels | `O(n log n)` | Multiply |
| Two independent `n` loops | `O(n)` | Add → `2n` |
| Two nested `n` loops | `O(n²)` | Multiply → `n²` |
| Three nested `n` loops | `O(n³)` | Multiply → `n³` |
| Binary branching recursion | often `O(2ⁿ)` | Number of calls grows exponentially |
| All permutations | `O(n!)` | `n!` possible arrangements |

---

# 35. The Core Rules to Remember

### Rule 1 — Sequential work adds

```text
O(n) + O(n)
= O(n)
```

---

### Rule 2 — Nested work multiplies

```text
O(n) × O(n)
= O(n²)
```

---

### Rule 3 — Repeated division usually means logarithmic

```text
n → n/2 → n/4 → ...
```

```text
→ O(log n)
```

---

### Rule 4 — Ignore constant factors

```text
O(5n) → O(n)
O(100n) → O(n)
```

---

### Rule 5 — Keep the dominant term

```text
O(n² + n + 100)
→ O(n²)
```

---

### Rule 6 — Nested does not automatically mean quadratic

```text
n × 10 → O(n)
n × √n → O(n√n)
n × n → O(n²)
```

Always examine the loop bounds.

---

### Rule 7 — Space must be analyzed separately

An algorithm can be:

```text
Time  → O(n)
Space → O(1)
```

or:

```text
Time  → O(n)
Space → O(n)
```

---

# 36. Complexity as a Problem-Solving Tool

Complexity analysis should not be something you do only after solving a problem.

It should be part of your problem-solving process:

```text
Understand
    ↓
Constraints
    ↓
Brute Force
    ↓
Analyze Complexity
    ↓
Find Bottleneck
    ↓
Observation
    ↓
Optimal Approach
    ↓
Analyze Complexity Again
```

For example:

```text
Brute Force
    ↓
O(n²)
    ↓
Find repeated work
    ↓
Use Hashing
    ↓
O(n) Time
O(n) Space
```

The real skill is not simply saying:

> "This is O(n)."

The real skill is being able to explain:

> **What work is being performed, how often it is performed, why that produces `O(n)`, and whether the work can be reduced.**

---

# 37. Final Mental Model

When analyzing an algorithm, think:

```text
                    INPUT SIZE
                        │
                        ↓
                 How does work grow?
                        │
       ┌────────────────┼────────────────┐
       ↓                ↓                ↓
   Fixed work       Process items     Reduce problem
       │                │                │
     O(1)             O(n)            O(log n)
       │
       └─────────────────────────────────┐
                                         ↓
                                  Combine operations
                                         │
                           ┌─────────────┴─────────────┐
                           ↓                           ↓
                      Sequential                    Nested
                         ADD                       MULTIPLY
                           │                           │
                      O(n+n) = O(n)             O(n×n) = O(n²)
```

### The ultimate idea

> **Asymptotic Analysis is not about memorizing Big-O values. It is about understanding how the amount of computational work and memory changes as the input grows.**

The progression should become:

```text
Code
 ↓
Count Work
 ↓
Build Cost Function T(n)
 ↓
Identify Growth
 ↓
Simplify
 ↓
Express as Big-O / Ω / Θ
 ↓
Evaluate Against Constraints
 ↓
Look for a Better Algorithm
```

That reasoning is the foundation you will repeatedly use throughout DSA.