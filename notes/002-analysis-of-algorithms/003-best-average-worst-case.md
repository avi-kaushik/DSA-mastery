# 003 — Best, Average & Worst Case

> **One-line takeaway:** Complexity can depend on *which* input you get, not just how big it is. Best/average/worst describe **three different inputs**; Big-O/Θ/Ω describe **bounds on a function**. Don't confuse the two axes.

---

## 1. Why Cases Exist

For the same input **size** `n`, an algorithm may do wildly different amounts of work:

```java
int search(int[] arr, int x) {
    for (int i = 0; i < arr.length; i++)
        if (arr[i] == x) return i;      // may stop immediately, or never
    return -1;
}
```

```text
x is the first element  → 1 comparison
x is the last element   → n comparisons
x is absent             → n comparisons
```

Same `n`, three different costs. So we analyse three scenarios.

---

## 2. The Three Cases

| Case | Meaning | Notation used |
|---|---|---|
| **Best case** | The input that makes the algorithm do the **least** work | Lower bound on its own behaviour |
| **Average case** | Expected work over all inputs, weighted by probability | The realistic estimate |
| **Worst case** | The input that makes it do the **most** work | The guarantee — **the default** |

### Linear search worked out

```text
Best case    → target at index 0            → 1 comparison       → Θ(1)
Worst case   → target at the end or absent  → n comparisons      → Θ(n)
Average case → target equally likely anywhere
```

---

## 3. 🧮 Computing the Average Case

The average case is an **expected value**, so it needs probabilities:

```text
T_avg(n) = Σ  P(inputᵢ) × cost(inputᵢ)
```

For linear search, assume the target is present and equally likely at any of the `n`
positions, so `P = 1/n` for each, and finding it at index `i` costs `i + 1` comparisons:

```text
T_avg(n) = (1/n) × (1 + 2 + 3 + ... + n)
```

Using the arithmetic series sum `1 + 2 + ... + n = n(n+1)/2`:

```text
T_avg(n) = (1/n) × n(n+1)/2
         = (n + 1) / 2
```

Drop constants:

```text
T_avg(n) = Θ(n)
```

> Note the result: on average you scan **half** the array — but half of `n` is still `Θ(n)`.
> **Average case is usually the same order as worst case.** That's why we mostly quote worst case.

---

## 4. Why Worst Case Is the Default

- **It's a guarantee.** "Never slower than this" is a promise you can design around.
- **Average case needs an assumption** about the input distribution that is often unknown
  or wrong in practice.
- **Best case is nearly useless** — it describes a lucky input, and every algorithm looks
  great on its luckiest input. (Bogosort's best case is `O(n)`.)
- **Adversarial inputs are real** — hash collisions, already-sorted input to naive quicksort.

> When someone says "the complexity of this algorithm" with no qualifier, they mean the
> **worst case**.

---

## 5. Common Algorithms Across the Three Cases

| Algorithm | Best | Average | Worst | What triggers the worst case |
|---|---|---|---|---|
| Linear Search | `Θ(1)` | `Θ(n)` | `Θ(n)` | Target absent |
| Binary Search | `Θ(1)` | `Θ(log n)` | `Θ(log n)` | Target absent / at a leaf |
| Insertion Sort | `Θ(n)` | `Θ(n²)` | `Θ(n²)` | Reverse-sorted input |
| Selection Sort | `Θ(n²)` | `Θ(n²)` | `Θ(n²)` | Always the same — no early exit |
| Bubble Sort (optimised) | `Θ(n)` | `Θ(n²)` | `Θ(n²)` | Reverse-sorted input |
| Merge Sort | `Θ(n log n)` | `Θ(n log n)` | `Θ(n log n)` | Never varies |
| Quick Sort | `Θ(n log n)` | `Θ(n log n)` | `Θ(n²)` | Worst pivot every time (e.g. sorted input, first-element pivot) |
| Hash Table search | `Θ(1)` | `Θ(1)` | `Θ(n)` | All keys collide into one bucket |

Two patterns worth noticing:

```text
Selection sort / Merge sort → all three cases identical
                              (work doesn't depend on the input's arrangement)

Quick sort / Hashing        → excellent average, bad worst case
                              (used everywhere anyway, because the worst case is rare
                               and avoidable — randomised pivot, good hash function)
```

> **Quick sort beats merge sort in practice** despite a worse worst case: better constants,
> in-place, cache-friendly. Another reminder that Big-O isn't the whole story.

---

## 6. ⚠️ The Classic Confusion: Cases vs Notations

These are **independent axes**, and mixing them is the most common interview mistake.

```text
CASE      = which input?         (best / average / worst)
NOTATION  = which kind of bound? (O upper / Ω lower / Θ tight)
```

So all of these are meaningful and different:

```text
Worst case of insertion sort  = Θ(n²)      ← tight bound on the worst case
Best case of insertion sort   = Θ(n)       ← tight bound on the best case
Insertion sort is             = O(n²)      ← never worse than n²
Insertion sort is             = Ω(n)       ← never better than n
```

Wrong statements you should be able to spot:

```text
✗ "Big-O means worst case."         O is an upper bound; you can bound any case.
✗ "Ω means best case."              Ω is a lower bound; you can bound any case.
✗ "Insertion sort is Θ(n²)."        Only its WORST case is. Overall it isn't tight.
```

> A clean way to say it: *"Insertion sort runs in `Θ(n²)` in the worst case and `Θ(n)` in
> the best case."* — case first, notation second.

---

## 7. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What are best/average/worst case?** | The minimum, expected and maximum work over all inputs of size `n`. |
| **Which one do we usually use, and why?** | Worst case — it's a guarantee and needs no assumption about the input distribution. |
| **Why is best case rarely useful?** | It describes a lucky input; even a terrible algorithm looks good on it. |
| **How is average case computed?** | As an expected value: `Σ P(input) × cost(input)` — it requires a probability assumption. |
| **Average case of linear search?** | `(n+1)/2` comparisons = `Θ(n)`. |
| **Is Big-O the same as worst case?** | No. Big-O is an upper bound on a function; worst case is a choice of input. You can state Big-O of the best case too. |
| **Quick sort's worst case and why use it anyway?** | `O(n²)` on bad pivots, but `Θ(n log n)` average with small constants, in-place and cache-friendly; randomisation makes the worst case unlikely. |
| **An algorithm with all three cases equal?** | Selection sort and merge sort — their work doesn't depend on input arrangement. |

---

## 8. The Mental Model

```text
                Inputs of size n
                       │
      ┌────────────────┼────────────────┐
      ↓                ↓                ↓
  luckiest         typical          nastiest
   BEST            AVERAGE           WORST
      │                │                │
   optimistic     realistic         guarantee
   (ignore)      (needs P(x))       (default)
```
