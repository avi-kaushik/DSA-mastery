# 008 — Runs & Boundaries: Thinking in Groups of Consecutive Elements

> **One-line takeaway:** Many arrays are really a sequence of **runs** — stretches of consecutive elements that "belong together". A run changes only at a **boundary**, where `arr[i]` stops agreeing with `arr[i − 1]`. Count inside a run, act at a boundary, and remember that the **last** run never sees a boundary — it must be closed by hand.

---

## 1. The Problem This Concept Solves

Look at these questions:

```text
"How many times does each value appear in this sorted array?"
"What's the longest streak of 1s?"
"Longest stretch where even and odd numbers alternate?"
"Fewest range-flips to make a binary array all the same?"
```

Each one is about **consecutive** elements. Treating elements one by one makes them feel
fiddly. Treating the array as a list of **groups** makes them easy:

```text
[ 10 10 10 | 20 | 30 30 ]            three runs:  10×3, 20×1, 30×2
[ 1 | 0 | 1 1 1 | 0 0 | 1 1 1 1 ]    runs of ones: 1, 3, 4
```

---

## 2. The Core Idea — Runs and the Boundaries Between Them

> 🔑 **A run is a maximal stretch where every element "continues" the previous one. A
> boundary is the index where that stops.**

"Continues" is whatever the problem says:

| Problem | `arr[i]` continues the run when… |
|---|---|
| Groups of equal values | `arr[i] == arr[i − 1]` |
| Streak of 1s | `arr[i] == 1` (and so did the previous) |
| Alternating even/odd | parity of `arr[i]` ≠ parity of `arr[i − 1]` |
| Strictly increasing stretch | `arr[i] > arr[i − 1]` |

Notice: most conditions compare a **pair of neighbours**, not a single value. That's the
whole flexibility of the concept — swap the condition and the same loop solves a new problem.

**Analogy — a train passing a level crossing.** You can't tell a carriage has ended until
you see the gap before the next one. And the **last** carriage is never followed by a gap —
if you only count at gaps, you'll forget it.

---

## 3. The Mechanics — and the Last-Run Trap

You only learn that a run has **ended** when you see the first element of the **next** run.

```text
[ 10 10 10 | 20 | 30 30 ]
              ↑    ↑
     boundary at i=3 tells you "the 10s ended at i=2"
     boundary at i=4 tells you "the 20s ended at i=3"
     …and nothing ever tells you the 30s ended.
```

> ⚠️ **The last run has no boundary after it.** It's the single most common bug in this
> family.

There are two ways to deal with it.

### Style A — act at each boundary, then close the last run after the loop

```text
count = 1
for i = 1 … n−1:
    if arr[i] continues:  count++
    else:                 report(run ending at i−1, count);  count = 1
report(final run, count)              ← the line everyone forgets
```

Use it when every run must be **reported** (e.g. print each value's frequency).

### Style B — update the answer on every step

```text
current = 0 (or 1), best = …
for each i:
    current = continues ? current + 1 : reset
    best    = max(best, current)      ← no run is ever "left open"
```

Use it when only the **best** run matters. The last run is handled automatically.

### 🧠 Pause and predict

What does this return for `[0, 1, 1, 1]`?

```cpp
int best = 0, cur = 0;
for (int i = 0; i < n; i++) {
    if (arr[i] == 1) cur++;
    else { best = max(best, cur); cur = 0; }
}
return best;
```

<details>
<summary>Answer</summary>

`0`. The streak of three 1s never meets a `0`, so it's never compared with `best`. Either
add `best = max(best, cur)` after the loop (style A), or move it inside the loop so it runs
every step (style B).

</details>

---

## 4. Seeding: `0` or `1`?

```text
streak of 1s           seed 0   — an array with no 1s really has an answer of 0
alternating parity     seed 1   — any single element is trivially a valid run
groups of equal values seed 1   — arr[0] starts the first group
```

Ask: *"what's the answer for a one-element array?"* That's your seed.

---

## 5. Sorting Turns "Equal" into "Adjacent"

> 💡 **In a sorted array, equal values form a single run.** "Have I seen this value
> before?" becomes "is it equal to my left neighbour?" — an `O(1)` check, no hash map.

That one fact powers several problems:

```text
frequencies in a sorted array     report each run's length                    O(n), O(1)
remove duplicates (sorted)        keep only the first element of each run     (two pointers, note 005)
keep at most k copies (sorted)    keep the first k of each run
```

On **unsorted** data, equal values are scattered — the run view doesn't apply. Use a hash
map instead (`O(n)` time and space).

---

## 6. Reasoning at the Level of Groups

Sometimes the real insight is about the **groups themselves**, not their contents.

### Example — fewest flips to make a binary array uniform

One **flip** inverts a contiguous range. What's the minimum number of flips to make every
element the same?

```text
[1 | 0 0 | 1 | 0 | 1 1]        groups: 1, 00, 1, 0, 11
```

**Step 1 — flip whole groups.** Flipping part of a group splits it and creates more work.
Flipping exactly one group merges it with its neighbours. So the answer is
`min(number of 0-groups, number of 1-groups)`.

**Step 2 — groups alternate.** Runs in a binary array go `A B A B …`. So the type that starts
the array has either the **same** number of groups as the other type, or **one more**:

```text
A B A B A      A-groups = B-groups + 1
A B A B        A-groups = B-groups
```

> 🔑 **So the type of `arr[0]` is never the cheaper one.** Flip every group that differs from
> `arr[0]` — no counting pass needed. At each boundary, if `arr[i] != arr[0]` a flip-range
> **starts** at `i`; otherwise one **ended** at `i − 1`. And — the last-run trap again — if
> the array ends inside a flip-range, close it after the loop.

```text
[0 1 1 0 0]     flip the 1-groups      →  "From 1 to 2"           (1 flip)
[1 0 0 1 0 1 1] flip the 0-groups      →  "From 1 to 2", "From 4 to 4"   (2 flips)
```

### Counting groups without listing them

```text
number of runs = number of boundaries + 1
```

That's often all you need: e.g. the minimum flips above is `⌊(boundaries + 1) / 2⌋`.

---

## 7. When the Run Is Allowed to Be Imperfect

"Longest streak of 1s if you may flip up to `k` zeros" breaks the run model — a zero no
longer ends the run outright. Instead, keep a **window** that may contain up to `k` zeros,
and shrink it from the left when it holds too many.

```text
runs            reset the moment the condition fails
sliding window  tolerate up to k failures inside; shrink when exceeded
```

A run with tolerance **is** a sliding window. See [Sliding Window](012-sliding-window.md).

---

## 8. A Detail That Bites — Parity of Negative Numbers

For "alternating even/odd" runs:

```text
C++ and Java:   −1 % 2 == −1         (the sign follows the dividend)

x % 2 == 1      ❌  says −1 is not odd
x % 2 == 0      ✅  even test is safe for negatives
x & 1           ✅  1 for odd, 0 for even, negatives included
```

---

## 9. Recognising Run Problems

```text
"consecutive", "streak", "longest stretch where …"        → run with a pair condition
"frequency of each element" in a SORTED array             → runs of equal values
"remove / keep k duplicates" in a SORTED array            → runs + two pointers
"groups", "segments", "flip a contiguous range"           → reason about the groups
"longest … if you may change up to k elements"            → sliding window
```

---

## 10. Common Mistakes

| ✗ Mistake | Why it's wrong |
|---|---|
| Forgetting the final run | It never meets a boundary |
| Updating the best only at a break | Same bug, in style B clothing |
| Reporting `arr[i]` as the run that just ended | `i` already belongs to the **new** run |
| Seeding with `1` when the answer can be `0` (or vice versa) | Wrong on edge cases |
| Using "equal to neighbour" on unsorted data | Equal values aren't adjacent |
| `x % 2 == 1` as an odd test | Fails for negatives |

---

## 11. Practice in This Repo

| Problem | Run condition / idea | 📄 |
|---|---|---|
| Frequencies in a sorted array | equal neighbours · style A | [`frequencies.cpp`](../../programs/cpp/data_structures/arrays/basic/frequencies.cpp) |
| Maximum consecutive ones | value is `1` · style B | [`maximum_consecutive_ones.cpp`](../../programs/cpp/data_structures/arrays/basic/maximum_consecutive_ones.cpp) |
| Longest alternating even-odd subarray | parity differs · seed `1` | [`sub_array.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sub_array.cpp) |
| Minimum group flips | group alternation · close the last range | [`flips.cpp`](../../programs/cpp/data_structures/arrays/intermediate/flips.cpp) |
| Remove duplicates / at most `k` | runs in sorted data + read/write | [`remove_elements.cpp`](../../programs/cpp/data_structures/arrays/basic/remove_elements.cpp) |

---

## 12. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **What's a run?** | A maximal stretch where each element continues the previous one. |
| **What's a boundary?** | The index where the "continues" condition fails. |
| **When do you learn a run ended?** | Only at the first element of the next run. |
| **#1 bug?** | Forgetting the last run — it has no boundary after it. |
| **Two ways to handle it?** | Close it after the loop (A), or update the answer every step (B). |
| **Why does sorting help?** | Equal values become one run — compare with the left neighbour. |
| **Seed `0` or `1`?** | Whatever the answer is for a single element. |
| **Number of runs?** | Boundaries + 1. |
| **Min flips to make a binary array uniform?** | Flip every group that differs from `arr[0]`. |
| **Why that type?** | Groups alternate, so `arr[0]`'s type never has fewer groups. |
| **Run with up to `k` exceptions?** | Sliding window. |

---

## 13. The Mental Model

```text
       [ run ][ run ][ run ] … [ last run ]
              ↑      ↑                     ↑
          boundaries (arr[i] stops continuing)    NO boundary here!
                          │
     ┌────────────────────┼─────────────────────┐
     ↓                    ↓                     ↓
 report each run      keep the best run     reason about the groups
 (style A)            (style B)             (alternation, counts)
 close the last       update every step     boundaries + 1 = runs
 run after the loop   → nothing left open
                          │
     sorted data: equal values = one run · tolerate k misses: sliding window
```
