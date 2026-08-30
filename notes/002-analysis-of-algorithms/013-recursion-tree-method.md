# 013 — Recursion Tree Method

> **One-line takeaway:** Draw the recursion as a tree, write the **non-recursive cost** at each node, sum **per level**, then sum the levels. The whole answer comes down to one question: does the per-level cost **grow, stay flat, or shrink**?

---

## 1. What a Recursion Tree Is

> **A recursion tree turns a recurrence into a picture: each node is one call, labelled with
> the work that call does *itself* (excluding its children).**

```text
T(n) = 2T(n/2) + n

                        n                      ← level 0: root does n work
                     ╱     ╲
                 n/2         n/2               ← level 1
                ╱   ╲       ╱   ╲
             n/4    n/4   n/4    n/4           ← level 2
             ...    ...   ...    ...
              1  1  1  1  1  1  1  1           ← leaves (base cases)
```

```text
Total cost = Σ (cost of every node) = Σ (cost of each level)
```

Summing **level by level** is what makes it tractable.

---

## 2. Anatomy — the Four Quantities

For `T(n) = a·T(n/b) + f(n)`:

| Quantity | Formula | Meaning |
|---|---|---|
| **Nodes at level `i`** | `aⁱ` | Branching compounds |
| **Subproblem size at level `i`** | `n / bⁱ` | Shrinks by `b` each level |
| **Cost of ONE node at level `i`** | `f(n / bⁱ)` | Its own non-recursive work |
| **Cost of level `i`** | `aⁱ · f(n / bⁱ)` | nodes × cost each |

Depth and leaves:

```text
Depth:   n/b^d = 1   ⟹   d = log_b n
Leaves:  a^d = a^(log_b n) = n^(log_b a)
```

```text
Total = Σ (i = 0 to log_b n − 1)  aⁱ · f(n/bⁱ)   +   Θ(n^(log_b a))
        └──────────── internal levels ─────────┘      └── leaf level ──┘
```

> For **decrease & conquer** (`T(n−c)`), the depth is `n/c`, not `log n`, and the leaf count
> is `a^(n/c)`. Getting the depth right is the most common failure point.

---

## 3. The Procedure

```text
1. Draw the root, labelled f(n).
2. Give it `a` children, each of size n/b, labelled f(n/b).
3. Compute the cost of each level:  aⁱ · f(n/bⁱ).
4. Find the depth: solve n/b^d = 1  →  d = log_b n.
5. Count the leaves: a^d = n^(log_b a).
6. Decide the shape:
       level costs GROWING   → the LEAF level dominates
       level costs FLAT      → total = (per-level cost) × depth
       level costs SHRINKING → the ROOT dominates
7. Sum with the right series formula (geometric — see note 010).
```

---

## 4. 🧮 Case FLAT — `T(n) = 2T(n/2) + n` (Merge Sort)

```text
Level    Nodes      Size      Cost/node     Level cost
────────────────────────────────────────────────────────
  0        1          n           n              n
  1        2         n/2         n/2             n
  2        4         n/4         n/4             n
  i       2ⁱ        n/2ⁱ        n/2ⁱ             n      ← constant!
 ...
log n     n           1           1              n
```

Every level costs exactly `n`. Depth is `log₂ n + 1` levels:

```text
Total = n × (log₂ n + 1) = Θ(n log n)
```

```text
        n ──────────────────── n
      ╱   ╲
   n/2     n/2 ─────────────── n
   ╱ ╲     ╱ ╲
 n/4 n/4 n/4 n/4 ───────────── n
  ⋮                             ⋮      log n levels
  1  1  1 ... 1 ─────────────── n
                          ─────────
                          Σ = n log n
```

> **The signature of `n log n`:** flat level costs × logarithmic depth.

---

## 5. 🧮 Case LEAVES DOMINATE — `T(n) = 2T(n/2) + 1`

```text
Level    Nodes     Cost/node    Level cost
────────────────────────────────────────────
  0        1           1             1
  1        2           1             2
  2        4           1             4
  i       2ⁱ           1            2ⁱ        ← GROWING (r = 2)
 ...
log n     n            1             n        ← the last term
```

A growing geometric series is dominated by its last term (note 010):

```text
Total = 1 + 2 + 4 + ... + n = 2n − 1 = Θ(n)
```

```text
⟹ T(n) = Θ(n)          ← tree traversal, computing tree height/size
```

> Half of all nodes in this tree are leaves. **When the tree branches faster than the work
> shrinks, the bottom level is the whole answer.**

---

## 6. 🧮 Case ROOT DOMINATES — `T(n) = T(n/2) + n`

Only **one** call per level, but the work halves:

```text
Level    Nodes    Cost/node    Level cost
───────────────────────────────────────────
  0        1          n             n
  1        1         n/2           n/2
  2        1         n/4           n/4       ← SHRINKING (r = ½)
 ...
log n      1          1             1
```

A shrinking geometric series is dominated by its **first** term:

```text
Total = n + n/2 + n/4 + ... < 2n = Θ(n)
```

```text
⟹ T(n) = Θ(n)          ← quickselect average case
```

> Same `Θ(n)` as the previous example, for the **opposite reason**. Recognising *which* level
> dominates is the real skill.

---

## 7. 🧮 Another Root-Dominated Case — `T(n) = 3T(n/4) + n²`

```text
Level    Nodes     Size      Cost/node        Level cost
──────────────────────────────────────────────────────────
  0        1        n           n²                n²
  1        3       n/4       (n/4)² = n²/16     3n²/16
  2        9      n/16       n²/256             9n²/256
  i       3ⁱ      n/4ⁱ       n²/16ⁱ            (3/16)ⁱ·n²
```

Geometric with ratio `r = 3/16 < 1`:

```text
Total = n² · Σ (3/16)ⁱ  <  n² · 1/(1 − 3/16)  =  (16/13)·n²  =  Θ(n²)
```

Leaves contribute `n^(log₄3) ≈ n^0.79`, which is far smaller than `n²`. Root wins.

```text
⟹ T(n) = Θ(n²)
```

---

## 8. 🧮 Unbalanced Tree — `T(n) = T(n/3) + T(2n/3) + n`

The master theorem **cannot** touch this (unequal subproblem sizes). The tree can.

```text
                       n                       ← n
                   ╱       ╲
              n/3            2n/3              ← n/3 + 2n/3 = n
             ╱   ╲          ╱     ╲
          n/9   2n/9    2n/9      4n/9         ← n
           ⋮      ⋮       ⋮         ⋮
```

Every **complete** level still sums to `n`. The two branches have different depths:

```text
Shortest path: n → n/3 → n/9 → ...   depth = log₃ n
Longest path:  n → 2n/3 → 4n/9 → ... depth = log_(3/2) n
```

```text
n·log₃ n  ≤  T(n)  ≤  n·log_(3/2) n
```

Both are `Θ(n log n)` (log bases are constants):

```text
⟹ T(n) = Θ(n log n)
```

> **Insight:** an uneven split still gives `n log n` as long as the split is by a *constant
> fraction*. Quick sort with a 1:9 pivot split is still `Θ(n log n)`. Only a split into
> `1` and `n−1` degrades it to `Θ(n²)`.

---

## 9. 🧮 Decrease & Conquer — `T(n) = 2T(n−1) + 1`

⚠️ Depth is `n`, **not** `log n`:

```text
Level    Nodes     Level cost
──────────────────────────────
  0        1            1
  1        2            2
  2        4            4
  i       2ⁱ           2ⁱ
 ...
 n−1     2ⁿ⁻¹        2ⁿ⁻¹
```

```text
Total = 1 + 2 + 4 + ... + 2ⁿ⁻¹ = 2ⁿ − 1 = Θ(2ⁿ)
```

```text
⟹ Tower of Hanoi
```

Compare with `T(n) = 2T(n/2) + 1` → `Θ(n)`. **Same branching factor, same work per node —
only the depth differs, and the answer goes from linear to exponential.**

---

## 10. The Three Shapes — Summary

```text
        ROOT-HEAVY              BALANCED               LEAF-HEAVY
     level cost shrinks      level cost flat        level cost grows
            ██                     ██                     ▁
            ▆                      ██                     ▃
            ▄                      ██                     ▅
            ▂                      ██                     ██
     ────────────────       ────────────────       ────────────────
      Θ(f(n))                Θ(f(n) · log n)        Θ(n^log_b a)
      = root cost            = level × depth        = leaf count
```

These are exactly the three cases of the master theorem (note 014). **The recursion tree is
where the master theorem comes from** — it isn't a separate idea.

---

## 11. Recursion Tree for Space

The same tree answers space, but you read it differently:

```text
TIME  = total work in ALL nodes         (sum the whole tree)
SPACE = work along ONE root-to-leaf path (the deepest path alive at once)
```

```text
Hanoi:  2ⁿ nodes → time Θ(2ⁿ)
        depth n  → space Θ(n)

Merge sort: log n depth → Θ(log n) stack, plus Θ(n) merge buffer → Θ(n)
```

> Only one path is alive at any moment, because siblings execute **sequentially**.

---

## 12. Common Mistakes

| ✗ Mistake | ✓ Correct |
|---|---|
| Using depth `log n` for `T(n−1)` recurrences | `n−c` recurrences have depth `n/c` |
| Labelling a node with `T(n/2)` | Label it with `f(n/2)` — its own work only, or you double-count |
| Assuming every level costs the same | Only true when `a·f(n/b) = f(n)`; check it |
| Ignoring the leaf level | For leaf-heavy trees it **is** the answer: `n^(log_b a)` |
| Summing an unbalanced tree as if complete | Bound it between the shortest and longest path depths |
| Confusing node count with depth | Time ← nodes; space ← depth |

---

## 13. Reference Table

| Recurrence | Level cost | Depth | Dominant | Result |
|---|---|---|---|---|
| `2T(n/2) + n` | flat `n` | `log n` | all levels | `Θ(n log n)` |
| `2T(n/2) + 1` | grows `2ⁱ` | `log n` | leaves | `Θ(n)` |
| `2T(n/2) + n²` | shrinks | `log n` | root | `Θ(n²)` |
| `T(n/2) + n` | shrinks | `log n` | root | `Θ(n)` |
| `T(n/2) + 1` | flat `1` | `log n` | all levels | `Θ(log n)` |
| `3T(n/4) + n²` | shrinks | `log₄ n` | root | `Θ(n²)` |
| `4T(n/2) + n` | grows | `log n` | leaves | `Θ(n²)` |
| `T(n/3)+T(2n/3)+n` | flat `n` | `log_(3/2) n` | all levels | `Θ(n log n)` |
| `2T(n−1) + 1` | grows `2ⁱ` | `n` | leaves | `Θ(2ⁿ)` |
| `T(n−1) + n` | shrinks | `n` | all levels | `Θ(n²)` |

---

## 14. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What is a recursion tree?** | A picture of the recursion where each node holds its own non-recursive cost; total cost = sum over all nodes, computed level by level. |
| **Cost of level `i`?** | `aⁱ · f(n/bⁱ)` — `aⁱ` nodes each costing `f(n/bⁱ)`. |
| **Depth and leaf count?** | Depth `log_b n`; leaves `a^(log_b n) = n^(log_b a)`. |
| **The three shapes?** | Level costs shrink → root dominates; flat → `f(n)·log n`; grow → leaves dominate (`n^log_b a`). |
| **Why is merge sort `n log n`?** | Every level costs `n` and there are `log n` levels. |
| **Why is `2T(n/2)+1` only `Θ(n)`?** | Level costs form a growing GP summing to `2n−1`; the leaves are the whole cost. |
| **Why is `T(n/2)+n` only `Θ(n)`?** | Shrinking GP `n + n/2 + n/4 + … < 2n`; the root dominates. |
| **How do you handle `T(n/3)+T(2n/3)+n`?** | Each full level costs `n`; depth is between `log₃n` and `log_{3/2}n`, both `Θ(log n)` → `Θ(n log n)`. |
| **Depth for `T(n−1)` recurrences?** | `n`, not `log n` — this is why Hanoi is `Θ(2ⁿ)` while `2T(n/2)+1` is `Θ(n)`. |
| **Tree for time vs space?** | Time = all nodes; space = one root-to-leaf path (the depth). |

---

## 15. The Mental Model

```text
              Draw the tree
                    │
                    ↓
        Cost of level i = aⁱ · f(n/bⁱ)
                    │
                    ↓
        How does that change with i?
                    │
    ┌───────────────┼───────────────┐
    ↓               ↓               ↓
 SHRINKING        FLAT           GROWING
 (GP r<1)      (constant)        (GP r>1)
    ↓               ↓               ↓
 root wins    level × depth     leaves win
  Θ(f(n))     Θ(f(n) log n)    Θ(n^log_b a)
```
