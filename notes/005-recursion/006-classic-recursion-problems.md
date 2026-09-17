# 006 — Classic Recursion Problems

> **One-line takeaway:** Three famously tricky problems — and they are tricky for **three different reasons**. Rope Cutting is about representing *impossible*, Hanoi is about *moving the obstruction*, Josephus is about *renumbering*. Learn the reason, not the code.

---

## How These Notes Are Organised

Concept notes explain an idea. These are **problem cards** — a fixed layout so that
revising any problem takes the same shape:

```text
🎯 Problem      what is actually being asked
💡 Insight      the ONE sentence that unlocks it      ← read this first
🔁 Recurrence   the maths, before any code
💻 Code         the implementation
🔍 Trace        a small example, run by hand
⏱️ Complexity   time and space, with the reason
⚠️ Pitfalls     what goes wrong when you write it from memory
🔀 Variants     the follow-ups an interviewer will reach for
```

Each card also names its **pattern** from
[Recursion Problem Patterns](005-recursion-problem-patterns.md) — that's the transferable
part.

> **Revising in a hurry?** Read only the 💡 Insight lines. Three sentences, three problems.

---

# Card 1 — Rope Cutting

**Pattern 4 — Best-of-Choices** · 📄 [`rope_cutting.cpp`](../../programs/cpp/techniques/recursion/rope_cutting.cpp) · 📄 [`RopeCuttingProblem.java`](../../programs/java/techniques/recursion/intermediate/RopeCuttingProblem.java)

### 🎯 Problem

Given a rope of length `n` and three allowed piece lengths `a`, `b`, `c`, cut the rope into
the **maximum number of pieces**. Every piece must be exactly `a`, `b` or `c`, and the rope
must be used up **exactly**. If that's impossible, report so.

### 💡 Insight

> **At every step you have exactly three choices. Ask what the best result is for the
> remaining rope, take the maximum, and add 1 for the cut you just made.**
>
> The difficulty isn't the maximum — it's that "impossible" must be a value that can
> **poison** the branch it came from.

### 🔁 Recurrence

```text
f(n) = 1 + max( f(n−a), f(n−b), f(n−c) )      if that max is valid
f(0) = 0                                       rope used exactly  → SUCCESS
f(n) = −1   for n < 0                          overshot the rope  → IMPOSSIBLE
```

### 💻 Code

```cpp
int get_maximum_cuts(int n, int a, int b, int c)
{
    if (a <= 0 || b <= 0 || c <= 0) return -1;      // validation

    if (n == 0) return 0;                           // base: cut exactly
    if (n < 0)  return -1;                           // base: invalid branch

    int cuts = max({get_maximum_cuts(n - a, a, b, c),
                    get_maximum_cuts(n - b, a, b, c),
                    get_maximum_cuts(n - c, a, b, c)});

    return cuts >= 0 ? cuts + 1 : -1;                // propagate impossibility
}
```

### 🔍 Trace — `n = 5, a = 2, b = 5, c = 1`

```text
f(5) = max( f(3), f(0), f(4) ) + 1

  f(1) = max( f(-1), f(-4), f(0) ) + 1 = max(-1, -1, 0) + 1 = 1
  f(2) = max( f(0),  f(-3), f(1) ) + 1 = max( 0, -1, 1) + 1 = 2
  f(3) = max( f(1),  f(-2), f(2) ) + 1 = max( 1, -1, 2) + 1 = 3
  f(4) = max( f(2),  f(-1), f(3) ) + 1 = max( 2, -1, 3) + 1 = 4

f(5) = max( 3, 0, 4 ) + 1 = 5          ← five pieces of length 1
```

### ⏱️ Complexity

```text
Time  = O(3ⁿ)     three branches per level  (strictly O(3^(n/min(a,b,c))))
Space = O(n)      recursion depth
With memoisation on n:  O(n) time, O(n) space
```

### ⚠️ Pitfalls

| ✗ Mistake | What breaks |
|---|---|
| Returning `0` for `n < 0` | An overshooting branch looks like success — counts get inflated |
| Returning `0` for both bases | `0` is a *valid* answer at `n == 0`; you can't reuse it for failure |
| `return cuts + 1` unconditionally | Turns `−1` into `0`, silently laundering an impossible path into a valid one |
| Forgetting `#include <algorithm>` | `max({...})` (initializer-list form) won't compile |
| Assuming an answer always exists | `n = 7, a = b = c = 5` genuinely has none |

> **The whole problem in one line:** `0` means *"done, nothing more to cut"*; `−1` means
> *"this path cannot happen"*. Merging them is the bug.

### 🔀 Variants

```text
Memoise on n                    → O(n), and it becomes textbook DP
Minimum number of pieces        → swap max for min, sentinel becomes +∞
Count the number of ways        → replace max with +, base f(0) = 1
Coin change (min coins)         → identical shape, different combine
Unbounded knapsack              → same, with values instead of counts
```

---

# Card 2 — Tower of Hanoi

**Pattern 5 — Solve, Act, Solve** · 📄 [`tower_of_hanoi.cpp`](../../programs/cpp/techniques/recursion/tower_of_hanoi.cpp) · 📄 [`TowerOfHanoi.java`](../../programs/java/techniques/recursion/intermediate/TowerOfHanoi.java)

### 🎯 Problem

Move `n` disks from the **source** rod to the **destination** rod using one **auxiliary**
rod, with two rules: move one disk at a time, and never place a larger disk on a smaller
one.

### 💡 Insight

> **You cannot move the biggest disk until everything above it is out of the way.**
>
> So: recursively move the top `n−1` disks to the auxiliary rod, move disk `n` to the
> destination, then recursively move those `n−1` disks on top of it. The *same* procedure
> handles both halves — only the roles of the rods swap.

```text
   move n−1 aside        move the big one        put n−1 back
   ┌───────────┐         ┌───────────┐          ┌───────────┐
   │ S → AUX   │   →     │  S → D    │    →     │ AUX → D   │
   └───────────┘         └───────────┘          └───────────┘
```

### 🔁 Recurrence

```text
T(n) = 2·T(n−1) + 1      T(0) = 0      →      T(n) = 2ⁿ − 1 moves
```

### 💻 Code

```cpp
void move_disks(int n, char source, char auxiliary, char destination)
{
    if (n == 0) return;

    // n−1 disks: source → auxiliary, using destination as the spare
    move_disks(n - 1, source, destination, auxiliary);

    cout << "Move " << n << " from " << source << " to " << destination << endl;

    // n−1 disks: auxiliary → destination, using source as the spare
    move_disks(n - 1, auxiliary, source, destination);
}
```

> **Read the two calls carefully.** They are not copies — the 2nd and 3rd arguments are
> permuted differently in each. That permutation *is* the algorithm.

### 🔍 Trace — `n = 3` (A → C, B spare)

```text
1.  Move 1 from A to C
2.  Move 2 from A to B
3.  Move 1 from C to B        ← n−1 = 2 disks now parked on B
4.  Move 3 from A to C        ← the big disk, moved exactly once
5.  Move 1 from B to A
6.  Move 2 from B to C
7.  Move 1 from A to C        ← the 2 disks rebuilt on top

Total = 7 = 2³ − 1 ✓
```

### ⏱️ Complexity

```text
Time  = O(2ⁿ)   — and this is OPTIMAL, not wasteful:
                  the answer itself contains 2ⁿ − 1 moves
Space = O(n)    — depth of the recursion, not the number of calls
```

> A good thing to say out loud: *"exponential time here isn't a weakness of the algorithm;
> the output is exponential, so no algorithm can do better."*

### ⚠️ Pitfalls

| ✗ Mistake | What breaks |
|---|---|
| Passing the same rod order to both calls | Disks end up on the wrong rod; the rule gets violated |
| Base case `n == 1` printing nothing | Off-by-one: you lose a move. `n == 0` returning is cleaner |
| Confusing "number of calls" with "stack depth" | `2ⁿ` calls made, but only `n` frames alive → space is `O(n)` |
| Trying to memoise it | Nothing repeats — every move is distinct. Memoisation does nothing here |

### 🔀 Variants

```text
Count moves only                → return 2ⁿ − 1, no printing
Which disk moves at step i      → disk = lowest set bit position of i  (bit trick)
First move direction            → n odd: source → destination
                                  n even: source → auxiliary
4 rods (Reve's puzzle)          → Frame–Stewart algorithm, no longer 2ⁿ
Iterative version               → legal-move rule, or simulate with an explicit stack
```

---

# Card 3 — Josephus Problem

**Pattern 6 — Position Remapping** · 📄 [`josepheus.cpp`](../../programs/cpp/techniques/recursion/josepheus.cpp) · 📄 [`JosephusProblem.java`](../../programs/java/techniques/recursion/intermediate/JosephusProblem.java)

### 🎯 Problem

`n` people stand in a circle, numbered `0 … n−1`. Starting from person `0`, every `k`-th
person is eliminated. Counting resumes from the next survivor. Find the **0-based index of
the last person remaining**.

### 💡 Insight

> **After the first elimination, you are looking at exactly the same problem with `n−1`
> people — just renumbered.**
>
> So don't simulate the circle. Solve the `n−1` case, then translate that answer back into
> the original numbering by shifting it `k` positions (mod `n`).

```text
n = 5, k = 3        0  1  2  3  4
                          ↑ person 2 is eliminated first

remaining circle    3  4  0  1          ← counting restarts at 3
renumbered as       0  1  2  3          ← this IS the n = 4 problem

so:  answer(5) = ( answer(4) + 3 ) mod 5
              └─ shift the smaller answer back by k ─┘
```

### 🔁 Recurrence

```text
J(n, k) = ( J(n−1, k) + k ) mod n
J(1, k) = 0
```

> The `mod n` uses the **current** circle size, which is why the shift has to be applied on
> the way *up*, not on the way down.

### 💻 Code

```cpp
int josepheus(int n, int k)
{
    if (n == 0) return 0;                 // base case

    int i = josepheus(n - 1, k);          // solve the smaller circle

    return (i + k) % n;                   // translate back to this circle
}
```

Iterative version — same recurrence, `O(1)` space:

```cpp
int josephus(int n, int k) {
    int res = 0;
    for (int i = 2; i <= n; ++i) res = (res + k) % i;
    return res;
}
```

### 🔍 Trace — `n = 7, k = 3`

```text
J(1) = 0
J(2) = (0 + 3) % 2 = 1
J(3) = (1 + 3) % 3 = 1
J(4) = (1 + 3) % 4 = 0
J(5) = (0 + 3) % 5 = 3
J(6) = (3 + 3) % 6 = 0
J(7) = (0 + 3) % 7 = 3      ← survivor is index 3 (person 4, if counting from 1)
```

### ⏱️ Complexity

```text
Recursive:  O(n) time, O(n) stack
Iterative:  O(n) time, O(1) space
Simulation: O(n·k) with a list, or O(n log n) with a BIT — both worse
```

> The whole point of the pattern: it replaces an `O(n·k)` simulation with an `O(n)` formula.

### ⚠️ Pitfalls

| ✗ Mistake | What breaks |
|---|---|
| Returning a 1-based answer | This recurrence is **0-based**. For 1-based, return `J + 1` |
| `% (n − 1)` instead of `% n` | The shift belongs to the circle you're returning *to* |
| Applying the shift before recursing | The transform happens on the way **up**, after the smaller answer exists |
| Base case `J(0) = 0` vs `J(1) = 0` | Both work here — `(0+k) % 1 == 0` — but know why |
| Simulating with a list to "be safe" | Correct but `O(n·k)`; the recurrence is the expected answer |

### 🔀 Variants

```text
1-based answer          → J(n,k) + 1
k = 2 closed form       → write n = 2^m + l, survivor (1-based) = 2l + 1
                          equivalently: rotate n's binary representation left by one bit
Find the elimination order → simulate, or use an order-statistics tree
Last TWO survivors      → same recurrence, different base case
Huge n, small k         → O(k log n) skipping formula
```

---

## Comparing the Three

The reason these are grouped: **same difficulty label, completely different reasoning.**

| | Rope Cutting | Tower of Hanoi | Josephus |
|---|---|---|---|
| **Pattern** | Best-of-choices | Solve–act–solve | Position remapping |
| **Recurrence** | `1 + max(f(n−a),f(n−b),f(n−c))` | `2T(n−1) + 1` | `(J(n−1)+k) mod n` |
| **Calls per level** | 3 | 2 | 1 |
| **Time** | `O(3ⁿ)` → `O(n)` memoised | `O(2ⁿ)` — optimal | `O(n)` |
| **The hard part** | Representing *impossible* | Permuting the rod arguments | Seeing the renumbering |
| **Memoisation helps?** | ✅ Yes, hugely | ❌ No — nothing repeats | ❌ No — already linear |
| **Leads to** | Dynamic programming | Divide & conquer | Closed-form maths |

> **The takeaway:** "exponential" means different things. Rope cutting is exponential
> because it *recomputes*; Hanoi is exponential because the *answer* is. Only the first one
> can be fixed.

---

## Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **Rope cutting recurrence?** | `f(n) = 1 + max(f(n−a), f(n−b), f(n−c))`, with `f(0)=0` and `f(n<0) = −1`. |
| **Why `−1` and not `0` for impossible?** | `0` is a legitimate answer at `n == 0`; a separate sentinel is needed so invalid branches can't be counted. |
| **How do you make rope cutting efficient?** | Memoise on `n` — `O(3ⁿ)` becomes `O(n)`. It's the DP recurrence already. |
| **Hanoi in one sentence?** | Move `n−1` disks aside, move the largest disk, move the `n−1` back. |
| **How many Hanoi moves?** | `2ⁿ − 1`, from `T(n) = 2T(n−1) + 1`. |
| **Is Hanoi's exponential time a problem?** | No — the output has `2ⁿ−1` moves, so it's optimal. Memoisation can't help. |
| **Hanoi time vs space?** | `O(2ⁿ)` time (total calls) but only `O(n)` space (stack depth). |
| **Josephus recurrence?** | `J(n,k) = (J(n−1,k) + k) mod n`, `J(1,k) = 0`, 0-based. |
| **Why does the `+k` shift work?** | After one elimination it's the same problem with `n−1` people renumbered, so the smaller answer shifts back by `k`. |
| **Josephus for `k = 2`?** | With `n = 2^m + l`, the 1-based survivor is `2l + 1`. |
| **Josephus in `O(1)` space?** | Iterate the recurrence: `res = (res + k) % i` for `i = 2 … n`. |
| **What do these three have in common?** | Only the label "intermediate" — they use three different patterns, which is exactly why they're worth grouping. |
