# 004 — One-Pass Thinking: Carrying State

> **One-line takeaway:** Most array questions can be answered in a single left-to-right sweep — *if* you decide what to **remember** about the part you've already seen. That memory is the **state**. Choose the smallest state that still lets you answer, update it once per element, and `O(n²)` "compare everything with everything" collapses into `O(n)`.

---

## 1. The Problem This Concept Solves

Ask a beginner for the largest element and they'll often write something like "for each
element, check whether it beats every other element". That's `O(n²)`. It works — but it
throws away information. After looking at 1,000 elements, it has learned nothing it can
reuse for element 1,001.

A one-pass algorithm does the opposite. It walks once, and after each step it holds a
**summary** of everything seen so far:

```text
                 seen so far                     not yet seen
        [ a₀  a₁  a₂  …  aᵢ₋₁ ]  aᵢ  [ aᵢ₊₁ … aₙ₋₁ ]
          └──── summarised ────┘   ↑
                 as STATE          the only element you look at now
```

> 🔑 **The question that unlocks every one-pass algorithm:**
> *"What is the smallest thing I need to remember about the seen part, so that I can
> update the answer when one more element arrives?"*

If that "thing" is a few numbers, you get `O(n)` time and `O(1)` space.

---

## 2. The Core Idea — State Is a Compressed Prefix

Think of reading a long novel and being asked, at the end, "who is the tallest character?"
You don't reread the book. While reading, you keep **one** name in your head — the tallest
so far — and replace it whenever someone taller appears. That name is your state.

```text
state  =  answer for arr[0 … i−1]
update :  answer for arr[0 … i]  from  (state, arr[i])
```

That's the whole mechanism. Different questions need different state:

| Question | State you carry | Update with `arr[i]` |
|---|---|---|
| Largest element | `best` | `best = max(best, arr[i])` |
| Sum / count of matches | a running total | add `arr[i]` (or `1` if it matches) |
| Top two (second largest) | `first`, `second` | demote or replace — §5 |
| Is it sorted? | *nothing* — just the previous element `arr[i−1]` | fail if `arr[i] < arr[i−1]` |
| Does `x` exist? | a flag | set it and stop |
| Longest streak | current streak, best streak | extend or reset — [note 008](008-runs-and-boundaries.md) |
| Best subarray sum | best sum ending here, best overall | extend or restart — [note 010](010-kadanes-algorithm.md) |

> 💡 The last two rows are the same idea wearing bigger clothes. Once "carry a summary of
> the prefix" feels natural, Kadane, Boyer–Moore and prefix sums are just cleverer choices
> of state.

---

## 3. Seeding the State — the Bug Factory

Before the loop, the state must describe the **empty prefix** (or the first element).
Get this wrong and the algorithm is wrong on edge cases while looking perfect on examples.

### Option A — seed with a real element

```cpp
int best = arr[0];                   // the prefix [arr[0]] has max arr[0]
for (int i = 1; i < n; i++)
    best = max(best, arr[i]);
```

Always correct, because `arr[0]` is a genuine candidate.

### Option B — seed with the identity of the operation

```text
sum      →  0            (x + 0 = x)
product  →  1            (x × 1 = x)
count    →  0
max      →  −∞           (max(x, −∞) = x)   in C++: INT_MIN · Java: Integer.MIN_VALUE
min      →  +∞
```

This is the same identity-element idea used for base cases in
[Writing Base Cases §5](../005-recursion/003-writing-base-cases.md).

### 🧠 Pause and predict

What does this return for `[-5, -2, -9]`?

```cpp
int best = 0;
for (int i = 0; i < n; i++) best = max(best, arr[i]);
```

<details>
<summary>Answer</summary>

`0` — a value that isn't even in the array. `0` is the identity for **sum**, not for **max**.
Seed max with `arr[0]` (or `−∞`), never with `0`.

</details>

### Sentinels vs flags

A **sentinel** is a fake value that means "nothing yet" — `INT_MIN`, `−1`. It's convenient
but dangerous: real data can contain the sentinel.

```text
second largest of [INT_MIN, 5]   →  the true answer IS INT_MIN
if "second == INT_MIN" means "not found", the algorithm wrongly reports none
```

> 🔑 **When "no answer" is possible, carry a separate flag** (`bool found`) or use
> `std::optional<int>` / Java's `OptionalInt`. A flag can't collide with data.

---

## 4. Early Exit — "All" vs "Any" Questions

Some questions don't need the whole array:

| Question shape | Stop when… | Example |
|---|---|---|
| "Is it true for **all** elements/pairs?" | you find **one counter-example** → `false` | is sorted? all positive? |
| "Is there **any** element that…?" | you find **one witness** → `true` | contains `x`? any negative? |
| "What is the max / count / sum?" | never — every element can change the answer | largest, total |

```cpp
// "all adjacent pairs are in order" — one bad pair is enough to answer
for (int i = 1; i < n; i++)
    if (arr[i] < arr[i - 1]) return false;
return true;
```

> 💡 **Sortedness is a property of neighbours.** Because order is transitive, checking
> `n − 1` adjacent pairs proves the whole array is sorted. You never compare non-neighbours.

This is why "is sorted" has a **best case of `O(1)`** (the first pair is wrong) and a worst
case of `O(n)` (the array *is* sorted, so every pair must be checked).

---

## 5. Worked Example — When the State Is More Than One Number

**Second largest distinct element** is the classic test of whether you can design state.

The state is a pair `(first, second)`, plus a `found` flag. Every new element falls into
exactly one case:

```text
x >  first                      new champion:  second = first,  first = x   ← DEMOTION
x == first                      a duplicate of the champion — ignore it
second < x < first              new runner-up: second = x
x <= second                     irrelevant
```

```text
[5, 10, 10, 8]

seed     first = 5,  second = —
10       > 5   → demote:  first = 10, second = 5
10       == 10 → ignore            ← the duplicate must NOT become second
8        between 5 and 10 → second = 8
answer   8
```

> 🔑 **Designing state = listing the cases.** If you can say, for every possible new
> element, exactly how the state changes, the code writes itself.

### Is one pass actually better than two?

```text
two passes:   pass 1 finds max · pass 2 finds the largest element < max      O(2n) = O(n)
one pass:     carry (first, second)                                           O(n)
```

Both are `O(n)`. "One pass" is not magic — a **constant number** of passes is still linear.
Under interview pressure, the two-pass version is simpler and just as good. One pass
matters when the data is a **stream** you can only read once.

---

## 6. Why `O(n)` Is the Floor

> **Any element you skip could be the answer.** If an algorithm for "largest element"
> never looked at index 37, an adversary could put the maximum exactly there.

So every correct algorithm must read all `n` elements → `Ω(n)`. A one-pass algorithm with
`O(1)` work per element is **optimal**. This "you must at least read the input" argument is
the standard way to justify a lower bound in interviews.

And sorting first (`O(n log n)`) to read off the max or second max is strictly worse —
mention it only to dismiss it.

---

## 7. Doing Several Things in One Pass

Independent states can share a loop:

```cpp
int lo = arr[0], hi = arr[0], sum = 0;
for (int i = 0; i < n; i++) {
    lo = min(lo, arr[i]);
    hi = max(hi, arr[i]);
    sum += arr[i];
}
```

**Fun fact for interviews:** finding min **and** max separately costs `2n` comparisons. Taking
elements **in pairs** — compare the two with each other first, then the smaller against
`lo` and the larger against `hi` — costs only `3n/2`. Same `O(n)`, fewer comparisons.

---

## 8. Recognising One-Pass Problems

```text
"find the largest / smallest / second largest …"
"count how many …",  "sum of …"
"check whether the array is … (sorted, all positive, …)"
"does the array contain …"
"for each element, compare with everything BEFORE it"   → carry a summary of the prefix
```

If the question is about **each element versus all earlier elements**, the summary of the
earlier elements is your state. That's exactly where [Prefix & Suffix Extremes](007-prefix-suffix-extremes.md)
picks up.

---

## 9. Common Mistakes

| ✗ Mistake | Why it's wrong |
|---|---|
| Seeding max with `0` | Wrong on all-negative arrays |
| Using a sentinel when "no answer" is possible | Real data can equal the sentinel |
| Not handling duplicates of the champion | `[10, 10, 5]` → "second largest" becomes `10` |
| Forgetting the empty-array case | `arr[0]` is out of bounds |
| Sorting to answer a max/min question | `O(n log n)` for an `O(n)` problem |
| Scanning the whole array for an "all"/"any" question | Misses the early exit |

---

## 10. Practice in This Repo

| Problem | What it exercises | 📄 |
|---|---|---|
| Largest element | single-value state, seeding with `arr[0]` | [`largest_elements.cpp`](../../programs/cpp/data_structures/arrays/basic/largest_elements.cpp) |
| Second largest | pair state, demotion, `found` flag | [`largest_elements.cpp`](../../programs/cpp/data_structures/arrays/basic/largest_elements.cpp) |
| Is sorted · linear search | early exit on a counter-example / witness | [`basic_operations.cpp`](../../programs/cpp/data_structures/arrays/basic/basic_operations.cpp) |
| Is sorted (recursive) | the same idea, recursively | [Recursion on Arrays](../005-recursion/009-recursion-on-arrays.md) |

---

## 11. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **What is "state" in a one-pass algorithm?** | A compressed summary of the elements seen so far. |
| **The design question?** | What's the smallest thing to remember so one more element can update the answer? |
| **How do you seed it?** | With a real element (`arr[0]`) or the operation's identity (`0`, `1`, `±∞`). |
| **Why not seed max with `0`?** | All-negative arrays would return `0`. |
| **Sentinel vs flag?** | Sentinels can collide with real data; a flag can't. |
| **When can you exit early?** | "All" questions on the first counter-example; "any" questions on the first witness. |
| **Why is checking neighbours enough for sortedness?** | Order is transitive. |
| **Is two passes worse than one?** | No — both `O(n)`. One pass matters for streams. |
| **Why can't you beat `O(n)`?** | Any unread element could be the answer. |
| **Min and max with fewer comparisons?** | Process in pairs → `3n/2` instead of `2n`. |

---

## 12. The Mental Model

```text
          [ already seen ]   arr[i]   [ not yet seen ]
                 │              │
                 ↓              │
        summarised as STATE ────┤
                                ↓
                  new state = update(state, arr[i])
                                │
     ┌──────────────────────────┼──────────────────────────┐
     ↓                          ↓                          ↓
  seed it right            exit early?                 O(n) · O(1)
  arr[0] or identity       all → counter-example       and Ω(n) says
  flag, not sentinel       any → witness               you can't do better
```
