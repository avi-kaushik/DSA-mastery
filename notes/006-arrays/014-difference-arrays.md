# 014 — Difference Arrays: Range Updates in `O(1)`

> **One-line takeaway:** To add `v` to every element of a range `[L, R]`, don't touch the range — write `+v` at `L` and `−v` at `R + 1`. After all updates, **one prefix-sum pass** turns those marks into the real values. A difference array records **where values change**; a prefix sum turns changes back into values. They're inverses — and together they turn `O(n · q)` work into `O(n + q)`.

---

## 1. The Problem This Concept Solves

> *Apply thousands of updates of the form "add `v` to every element from `L` to `R`", then
> report the final array.*

Doing each update directly walks the whole range: `O(range length)` per update, so
`O(n · q)` overall.

The same shape hides in many questions:

```text
"Which value is covered by the most ranges?"                 (maximum appearing element)
"How many trains are in the station at the busiest moment?"  (minimum platforms)
"Can one car carry all these passengers?"                    (car pooling)
```

Each is "many intervals, then look at every point".

---

## 2. The Core Idea — Record Changes, Not Values

> 🔑 **A range update only changes the array's *slope* in two places: it steps up at `L` and
> steps back down just after `R`.** Record only those two steps.

```text
add 1 to [2 … 5]:

index     0   1   2   3   4   5   6   7
mark      0   0  +1   0   0   0  −1   0       ← two writes, whatever the range length
running   0   0   1   1   1   1   0   0       ← prefix sum of the marks = the update ✅
```

**Analogy — counting people in a room.** You don't track everyone's position every minute.
At the door, you log `+1` when someone walks in and `−1` when someone walks out. The number
of people in the room at any moment is the **running total** of the log up to that moment.

Each range is a person: in at `L`, out after `R`.

---

## 3. Why the Prefix Sum Brings the Values Back

Define the difference array as "how much the value changes from one index to the next":

```text
diff[i] = value[i] − value[i − 1]          (with value[−1] = 0)
```

Adding up the changes telescopes back to the value:

```text
diff[0] + diff[1] + … + diff[i]
   = value[0] + (value[1] − value[0]) + … + (value[i] − value[i−1])
   = value[i]                                 everything else cancels
```

| | [Prefix sum](013-prefix-sums.md) | Difference array |
|---|---|---|
| Turns… | values → running totals | values → changes between neighbours |
| Makes cheap | range **queries** | range **updates** |
| Undone by | difference | prefix sum |

> 💡 Think calculus: prefix sum is **integration**, difference is **differentiation**. Each
> undoes the other. Prefix sums answer "how much in this range?"; difference arrays answer
> "add this much to this range".

---

## 4. The Template

```cpp
vector<long long> diff(n + 1, 0);              // one extra slot for R + 1 = n

for (auto [L, R, v] : updates) {               // each update: O(1)
    diff[L]     += v;
    diff[R + 1] -= v;
}

long long running = 0;
for (int i = 0; i < n; i++) {                  // one pass: O(n)
    running += diff[i];
    value[i] += running;
}
```

`O(n + q)` instead of `O(n · q)`.

> ⚠️ It works when **all updates happen before any read**. If reads and updates are mixed,
> you need a Fenwick tree or a segment tree with lazy propagation.

---

## 5. Worked Example — Which Value Is Covered Most Often?

> Given ranges `left[i] … right[i]`, find the value that appears in the most ranges (smallest
> on ties).

Each range is an update "add 1 to `[L, R]`". The final array is the **coverage** of every
value. The answer is where coverage peaks.

```text
ranges [1 … 3], [2 … 5], [5 … 7]

mark       +1 at 1, −1 at 4  ·  +1 at 2, −1 at 6  ·  +1 at 5, −1 at 8

index      0   1   2   3   4   5   6   7   8
diff       0  +1  +1   0  −1  +1  −1   0  −1
coverage   0   1   2   2   1   2   1   1   0
                   ↑
           first maximum → answer 2
```

Two small details that come straight from the concept:

- **Array size `max(right) + 2`** — the values go up to `max(right)`, and the `−1` for the
  widest range lands one past it.
- **Smallest on ties** — scan left to right and replace the answer only on a **strictly**
  greater coverage.

### 🧠 Pause and predict

What breaks if you write the `−1` at `R` instead of `R + 1`?

<details>
<summary>Answer</summary>

The range stops **one early** — the value `R` itself is never counted. `[2 … 5]` would only
cover `2, 3, 4`. Ranges here are **inclusive**, so the drop must happen after `R`.

For **half-open** ranges `[L, R)` (a passenger who gets off at stop `R` frees the seat *at*
`R`), the `−1` goes at `R` — always match the mark to the problem's endpoint convention.

</details>

---

## 6. When the Coordinates Are Huge — Sweep Line

The difference array is indexed **by value**, so its size is the largest coordinate, not the
number of ranges. Three ranges ending near `10⁹` would need a billion slots.

The fix keeps the same idea but stores only the marks that exist:

```text
1. turn each range into two events:  (L, +1)  and  (R + 1, −1)
2. sort the 2n events by position
3. walk them in order, keeping a running total
```

`O(n log n)` time, `O(n)` space — independent of the coordinate size. This is the **sweep
line**: a difference array that only visits the positions where something changes.

> Minimum platforms is exactly this: arrivals are `+1`, departures are `−1`, and the answer
> is the peak running total.

---

## 7. The Family — Same Marks, Different Final Question

| Problem | Each range is… | After the prefix pass, ask… |
|---|---|---|
| Maximum appearing element | `+1` on `[L, R]` | where is coverage highest? |
| Minimum platforms / meeting rooms | `+1` while occupied | what's the peak? |
| Car pooling | `+passengers` on `[pickup, drop)` | does any point exceed capacity? |
| Corporate flight bookings | `+seats` on `[first, last]` | the final array itself |
| Range-add then print | `+v` on `[L, R]` | the final array itself |

> 💡 Whenever a problem says "many intervals, then something about every point", think
> **mark the edges, then sweep**.

---

## 8. Two Dimensions

To add `v` to a rectangle `(r1, c1) … (r2, c2)`, mark four corners:

```text
D[r1][c1] += v      D[r1][c2+1] −= v      D[r2+1][c1] −= v      D[r2+1][c2+1] += v
```

Then take the 2-D prefix sum. It's the inclusion–exclusion of
[2-D prefix sums](013-prefix-sums.md), run backwards.

---

## 9. Recognising Difference-Array Problems

```text
"add v to every element in [L, R]" — many times, then read     → difference array
"covered by the most intervals", "busiest moment", "peak load" → +1 / −1 marks, sweep
"does the load ever exceed capacity?"                           → marks + running total
coordinates up to 10⁹                                          → sort events (sweep line)
```

---

## 10. Common Mistakes

| ✗ Mistake | Why it's wrong |
|---|---|
| `−v` at `R` for inclusive ranges | The last value of each range is lost |
| Array sized `max(R) + 1` | `R + 1` writes out of bounds |
| Forgetting the prefix pass | You'd be reading the marks, not the values |
| `>=` when picking the peak | Returns the **largest** tied value, not the smallest |
| Huge or negative coordinates as indices | Giant array / negative index — sort events or compress |
| Interleaving updates and reads | Difference arrays need "all updates, then reads" |

---

## 11. Practice in This Repo

| Problem | Idea | 📄 |
|---|---|---|
| Maximum appearing element in ranges | `+1/−1` marks, prefix pass, first peak | [`maximum_appearing_element.cpp`](../../programs/cpp/data_structures/arrays/intermediate/maximum_appearing_element.cpp) · [`MaximumAppearingElementInRanges.java`](../../programs/java/arrays/basic/MaximumAppearingElementInRanges.java) |

---

## 12. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Difference array in one line?** | `+v` at `L`, `−v` at `R + 1`; a prefix sum rebuilds the values. |
| **Why does it work?** | Summing neighbour-to-neighbour changes telescopes back to the value. |
| **Relation to prefix sums?** | Inverses — like differentiation and integration. |
| **Prefix sum makes cheap…?** | Range **queries**. Difference array: range **updates**. |
| **Complexity?** | `O(1)` per update, `O(n)` to rebuild → `O(n + q)`. |
| **Array size for coverage of values up to `M`?** | `M + 2`. |
| **Smallest value on ties?** | Strict `>` while scanning left to right. |
| **Coordinates too large?** | Sweep line: sort the `±1` events, `O(n log n)`. |
| **Main limitation?** | All updates must come before the reads. |
| **Same idea elsewhere?** | Min platforms, meeting rooms, car pooling, flight bookings. |

---

## 13. The Mental Model

```text
             many "add v to [L, R]" updates
                           │
        don't paint the range — mark where it CHANGES
                           │
          diff[L] += v            diff[R + 1] −= v        O(1) each
                           │
              one prefix sum over the marks               O(n)
                           │
               real values / coverage at every index
                           │
       ┌───────────────────┼─────────────────────┐
       ↓                   ↓                     ↓
   peak coverage      exceeds capacity?     the final array
   (max appearing,    (car pooling)         (flight bookings)
    platforms)
                           │
        huge coordinates → sort the events (sweep line)
```
