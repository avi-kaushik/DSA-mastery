# 015 — Space Complexity & Auxiliary Space

> **One-line takeaway:** **Space complexity** counts *all* memory an algorithm needs, input included. **Auxiliary space** counts only the *extra* memory it allocates. Interviews almost always mean auxiliary space — and recursion's hidden call stack counts.

---

## 1. What Is Space Complexity?

> **Space complexity is the total memory an algorithm needs, as a function of input size `n`.**

Just like time, we express it asymptotically — `O(1)`, `O(n)`, `O(n²)` — and ignore constants.

```text
Total Space  =  Input Space  +  Auxiliary Space
                     ↑                ↑
              memory holding      extra memory the
              the input itself    algorithm allocates
```

---

## 2. The Distinction That Matters

| Term | Includes the input? | Example: summing an array of `n` |
|---|---|---|
| **Space complexity** | ✅ Yes | `O(n)` — the array itself |
| **Auxiliary space** | ❌ No | `O(1)` — just a `sum` variable |

```java
int sum(int[] arr) {
    int total = 0;                  // one variable
    for (int x : arr) total += x;
    return total;
}
```

```text
Space complexity = O(n)     (the input array must exist)
Auxiliary space  = O(1)     (only `total` and `x`)
```

> **When an interviewer asks "what's the space complexity?", they nearly always mean
> auxiliary space.** Saying "`O(1)` auxiliary, `O(n)` including the input" is the answer
> that shows you know the difference.

Why auxiliary is the useful measure: the input has to exist no matter which algorithm you
pick, so it can't distinguish between them. The *extra* memory can.

---

## 3. What Actually Consumes Space

```text
1. Input space          the data passed in
2. Variables            counters, accumulators, pointers        usually O(1)
3. Data structures      arrays, maps, sets, lists you allocate
4. Recursion stack      one frame per pending call              ⚠️ easy to miss
5. Output space         the result you return                   (often excluded)
```

### The output-space convention

If a problem *requires* returning `n` items, that `O(n)` is unavoidable, so it's usually
excluded from auxiliary space:

```java
int[] doubled(int[] arr) {
    int[] result = new int[arr.length];   // required output
    ...
}
```

```text
Auxiliary space = O(1) excluding output  /  O(n) including it
```

State which convention you're using — both are defensible, and saying so preempts the
follow-up question.

---

## 4. ⚠️ Recursion Is Not Free

Every pending call holds a **stack frame** (parameters, locals, return address).

```text
Auxiliary space from recursion  =  maximum recursion DEPTH  ×  O(1) per frame
```

```java
int fact(int n) {
    if (n == 0) return 1;
    return n * fact(n - 1);       // n frames alive at the deepest point
}
```

```text
Time  = O(n)
Space = O(n)     ← the call stack, even though no array is allocated
```

Compare the iterative version:

```java
int fact(int n) {
    int r = 1;
    for (int i = 2; i <= n; i++) r *= i;
    return r;                     // Space = O(1)
}
```

### 🔑 Why depth, and not the number of calls?

This is the part that's easy to forget, so here's the mechanism.

**The rule the machine follows:**

```text
Calling a function   →  PUSH a frame onto the call stack
Returning from it    →  POP that frame off (memory is reclaimed immediately)
```

So the memory in use at any instant is **how many frames are on the stack right now** —
that is, **how deep you currently are**. The peak over the whole run is the maximum depth.

```text
Space = the deepest the stack ever gets,  NOT how many calls happened in total.
```

**Why only one path is ever alive:** a parent cannot finish until its children finish, so
the frames on the stack are always exactly the chain from the original call down to the one
executing now — a single root-to-leaf path. Siblings never coexist: the left call is fully
popped before the right call is pushed.

### 🧮 Watch the stack move

```java
void f(int n) {
    if (n == 0) return;
    f(n - 1);      // left
    f(n - 1);      // right
}
```

`f(3)` makes **15 calls** in total, but watch the stack:

```text
step  action           call stack                depth
────────────────────────────────────────────────────────
 1    call f(3)        [f3]                        1
 2    call f(2)        [f3, f2]                    2
 3    call f(1)        [f3, f2, f1]                3
 4    call f(0)        [f3, f2, f1, f0]            4   ← peak
 5    return f(0)      [f3, f2, f1]                3
 6    call f(0)        [f3, f2, f1, f0]            4   ← peak again, SAME memory
 7    return f(0)      [f3, f2, f1]                3
 8    return f(1)      [f3, f2]                    2
 9    call f(1)        [f3, f2, f1]                3
      ...                                          ...
15    return f(3)      []                          0
```

Step 6 is the whole point: that second `f(0)` **reuses the exact memory step 5 freed**.
Fifteen calls happen, but the stack never holds more than four frames.

```text
15 calls  →  TIME  = O(2ⁿ)      every call costs time, and time is never given back
 4 frames →  SPACE = O(n)       memory is returned on every pop and reused
```

> **Time accumulates; space is recycled.** That single sentence is the whole difference.

### Reading it off the recursion tree

```text
        f(3)              ← depth 1
       ╱    ╲
    f(2)    f(2)          ← depth 2
    ╱  ╲    ╱  ╲
  f(1) f(1)f(1) f(1)      ← depth 3
  ...

TIME  = count ALL the nodes         → O(2ⁿ)
SPACE = length of ONE root-to-leaf path → O(n)
```

That's the same tree from [note 013](013-recursion-tree-method.md), read two different ways.

```text
Tower of Hanoi:  2ⁿ − 1 calls made,  only n frames alive at once
                 Time  = O(2ⁿ)
                 Space = O(n)
```

### ⚠️ When a frame isn't O(1)

`O(depth)` assumes each frame holds a constant amount of data. If a call allocates
something of size `n`, multiply:

```java
void solve(List<Integer> path, int n) {
    List<Integer> copy = new ArrayList<>(path);   // O(n) per frame!
    solve(copy, n - 1);
}
```

```text
Space = depth × per-frame size = O(n) × O(n) = O(n²)
```

Passing a reference instead of copying keeps it at `O(n)`. This is the usual reason a
backtracking solution uses more memory than expected.

```text
Space = (max depth) × (size of one frame)
```

### Tail recursion

A tail call could reuse the frame, giving `O(1)` — but **Java and Python do not optimise
tail calls**. Assume `O(depth)` unless you know the compiler eliminates it (some C/C++
builds do).

---

## 5. Worked Examples

### O(1) — constant

```java
void swap(int[] a, int i, int j) {
    int t = a[i]; a[i] = a[j]; a[j] = t;    // one temp
}
```

A fixed number of variables regardless of `n` → `O(1)`. Note that `O(1)` doesn't mean *one*
variable — 50 variables is still `O(1)`.

### O(n) — linear

```java
Set<Integer> seen = new HashSet<>();
for (int x : arr) seen.add(x);              // up to n entries → O(n)
```

### O(n) — recursion depth

```java
int height(Node root) {
    if (root == null) return 0;
    return 1 + Math.max(height(root.left), height(root.right));
}
```

```text
Balanced tree → depth O(log n) → space O(log n)
Skewed tree   → depth O(n)     → space O(n)     ← worst case
```

### O(n²) — a 2D table

```java
int[][] dp = new int[n][n];                 // O(n²)
```

### 🧮 A DP space optimisation

```java
// O(n × m) table
int[][] dp = new int[n][m];

// but if row i only reads row i−1, keep two rows:
int[] prev = new int[m], curr = new int[m];   // O(m)
```

```text
O(n·m)  →  O(m)      same time, far less memory
```

> **Rolling-array optimisation** — one of the most common follow-ups after you give a DP
> solution. Time is unchanged; only space drops.

---

## 6. Space of Common Algorithms

### Sorting

| Algorithm | Auxiliary space | Why |
|---|---|---|
| Bubble / Selection / Insertion | `O(1)` | In-place swaps |
| Heap Sort | `O(1)` | In-place, iterative sift |
| Quick Sort | `O(log n)` avg, `O(n)` worst | Recursion stack only |
| Merge Sort (array) | `O(n)` | The merge buffer |
| Merge Sort (linked list) | `O(log n)` | No buffer needed — just the stack |
| Counting Sort | `O(k)` | `k` = value range |
| Radix Sort | `O(n + k)` | Buckets |

> **Why merge sort loses to quick sort in practice** despite the same `Θ(n log n)` time:
> that `O(n)` buffer. Quick sort sorts in place.

### Searching & traversal

| Algorithm | Auxiliary space |
|---|---|
| Linear search | `O(1)` |
| Binary search (iterative) | `O(1)` |
| Binary search (recursive) | `O(log n)` — stack |
| BFS | `O(V)` — the queue |
| DFS (recursive) | `O(V)` — the stack, `O(h)` for a tree |
| Hash-based lookup | `O(n)` for the table |

### Data structures

| Structure | Space for `n` items |
|---|---|
| Array | `O(n)` |
| Linked list | `O(n)` — plus a pointer per node |
| Hash table | `O(n)` |
| BST / Heap | `O(n)` |
| Trie | `O(total characters × alphabet)` |
| Graph (adjacency list) | `O(V + E)` |
| Graph (adjacency matrix) | `O(V²)` |

---

## 7. In-Place Algorithms

> **An algorithm is in-place if it uses `O(1)` auxiliary space** (some definitions allow
> `O(log n)` for the recursion stack).

```text
In-place     → reverse array, heap sort, quick sort (by convention), bubble sort
Not in-place → merge sort (array), counting sort, most hashing approaches
```

```java
// in-place reverse — O(1) auxiliary
void reverse(int[] a) {
    for (int i = 0, j = a.length - 1; i < j; i++, j--) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }
}
```

⚠️ In-place algorithms **modify the input**. If the caller still needs the original, you
must copy it first — and then you're back to `O(n)`.

---

## 8. Time–Space Trade-Off

Extra memory usually buys speed:

```text
Two Sum, brute force:   check all pairs        O(n²) time,  O(1) space
Two Sum, hashing:       remember seen values   O(n)  time,  O(n) space
```

```text
Fibonacci naive:        O(2ⁿ) time,  O(n) space
Fibonacci memoised:     O(n)  time,  O(n) space
Fibonacci two vars:     O(n)  time,  O(1) space
```

```text
More space  →  less time      (caching, memoisation, hash maps, precomputation)
Less space  →  more time      (recompute instead of store)
```

> There is no universally best point on this curve — **the constraints decide.** An embedded
> device may forbid the `O(n)` table that a server would happily use.

---

## 9. How to Analyse Space — Step by Step

```text
1. Count the variables                    → usually O(1)
2. Find every allocation                  → arrays, maps, sets, lists, strings
   ...and size each by n
3. Find the maximum recursion depth       → × O(1) per frame
4. Decide about the output                → state whether you're counting it
5. Take the MAXIMUM of concurrent usage, not the sum of everything ever allocated
6. Drop constants → asymptotic answer
```

### Step 5 is the subtle one

```java
for (int i = 0; i < n; i++) {
    int[] temp = new int[n];    // allocated n times...
    // ...but only ONE is alive at a time
}
```

```text
Space = O(n),  not O(n²)
```

Space is **reused**; time is not. That's the fundamental asymmetry between the two measures.

---

## 10. Common Mistakes

| ✗ Mistake | ✓ Correct |
|---|---|
| Forgetting the recursion stack | A recursive function is at least `O(depth)` space |
| Counting total allocations over time | Count the **maximum alive at once** |
| Confusing call count with depth | Hanoi: `2ⁿ` calls, `O(n)` space |
| Saying "recursive binary search is `O(1)` space" | It's `O(log n)` — the stack |
| Ignoring string immutability | Building a string in a loop can allocate `O(n²)` in Java without `StringBuilder` |
| Assuming `O(1)` means one variable | It means a *constant* number, independent of `n` |
| Forgetting a skewed tree | Tree recursion is `O(h)`: `O(log n)` balanced, `O(n)` skewed |

---

## 11. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What is space complexity?** | Total memory as a function of input size — input space plus auxiliary space. |
| **Space complexity vs auxiliary space?** | Space complexity includes the input; auxiliary space is only the extra memory allocated. |
| **Which do interviews mean?** | Auxiliary space — the input is common to every algorithm, so it can't distinguish them. |
| **Does recursion use space?** | Yes — one stack frame per pending call, so `O(max depth)`. |
| **Why is the call stack `O(depth)` and not `O(calls)`?** | A frame is pushed on call and popped on return, so only the current root-to-leaf chain is alive; a sibling call reuses the memory the previous one freed. |
| **Hanoi: `2ⁿ` calls, so `O(2ⁿ)` space?** | No — only `n` frames are alive at once, so `O(n)`. Time accumulates; space is recycled. |
| **When is recursion space worse than `O(depth)`?** | When a frame isn't `O(1)` — e.g. copying an `n`-sized list per call gives `depth × n = O(n²)`. |
| **Merge sort vs quick sort space?** | Merge sort `O(n)` for the buffer; quick sort `O(log n)` average stack, `O(n)` worst. |
| **What does in-place mean?** | `O(1)` auxiliary space (often `O(log n)` allowed for the stack); it modifies the input. |
| **Recursive vs iterative binary search space?** | `O(log n)` vs `O(1)`. |
| **DFS space on a tree?** | `O(h)` — `O(log n)` balanced, `O(n)` skewed. |
| **Allocating an `n`-array inside an `n`-loop?** | `O(n)`, not `O(n²)` — only one is alive at a time; memory is reused. |
| **What is the time–space trade-off?** | Extra memory (caching, hashing, memoisation) usually buys lower time, and vice versa. |
| **How do you reduce DP space?** | Rolling array — keep only the rows the recurrence reads, e.g. `O(n·m)` → `O(m)`. |

---

## 12. The Mental Model

```text
                  What memory exists?
                          │
      ┌───────────────────┼───────────────────┐
      ↓                   ↓                   ↓
  INPUT SPACE       AUXILIARY SPACE       OUTPUT SPACE
  (given, ignore)   (what you're          (state your
                     judged on)            convention)
                          │
        ┌─────────────────┼─────────────────┐
        ↓                 ↓                 ↓
    variables        allocations       recursion stack
      O(1)          size × count        depth × frame
                          │
                          ↓
              take the MAXIMUM ALIVE AT ONCE
                          │
                          ↓
                    drop constants
```
