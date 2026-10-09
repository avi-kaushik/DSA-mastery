# 009 — Recursion on Arrays

> **One-line takeaway:** To make an array problem recursive, **shrink the array by one element** and let the recursive call handle the rest — that's `O(n)`. If the array is **sorted**, you can throw away *half* instead of one, and it becomes `O(log n)`.

📄 [`array.cpp`](../../programs/cpp/techniques/recursion/array.cpp) ·
📄 [`binary_search.cpp`](../../programs/cpp/techniques/recursion/binary_search.cpp)

---

## 1. Two Ways to Shrink an Array

There is no "remove an element" in C++ — instead you move a **boundary** and pretend the
rest isn't there. Two boundaries are possible, and both appear in `array.cpp`:

```text
FORWARD — move the start, index grows
   f(arr, size, index)        looks at arr[index], recurses with index + 1
   base: index >= size        ran off the end

BACKWARD — move the end, size shrinks
   f(arr, size)               looks at arr[size - 1], recurses with size - 1
   base: size == 0  or  size == 1
```

```text
forward     [ 1  3  9  15  14  9  37 ]        backward
             ↑ index →                                   ← size ↓
```

### Which one to use

| | Forward (`index + 1`) | Backward (`size − 1`) |
|---|---|---|
| **Natural for** | stop at the **first** thing you find | **combining** every element's result |
| **Used by** | `first_occurrence`, `is_sorted` | `count_occurrence`, `get_minimum`, `get_maximum` |
| **Reads like** | "scan from here to the end" | "answer for the first `size−1`, then fold in the last one" |

Both are `O(n)` time and `O(n)` stack. The choice is about which one is **easier to write
correctly**, not speed.

> **The backward form is the "build from a smaller problem" idea** — the same framing as
> subsets in [note 007](007-subsets-pattern.md). It's usually the easier one to reason about
> when the answer combines all elements.

---

## 2. Search — First and Last Occurrence

The same scan, pointed in opposite directions:

```cpp
// FIRST — scan left to right, return as soon as it matches
int first_occurrence(int arr[], int size, int target, int index = 0)
{
    if (index >= size)        return -1;      // ran off the end
    if (arr[index] == target) return index;   // found it
    return first_occurrence(arr, size, target, index + 1);
}

// LAST — scan right to left (wrapper starts at the end)
int last_occurrence_recursive(int arr[], int target, int index)
{
    if (index < 0)            return -1;      // ran off the start
    if (arr[index] == target) return index;
    return last_occurrence_recursive(arr, target, index - 1);
}

int last_occurrence(int arr[], int size, int target)
{
    return last_occurrence_recursive(arr, target, size - 1);
}
```

```text
arr = [ 1  3  9  15  14  9  37 ],  target = 9
index    0  1  2   3   4  5   6

first_occurrence → 2          last_occurrence → 5
```

> **The insight:** you don't need different logic for "first" vs "last" — only a different
> **direction**. Whichever end you start from, the first match you hit is the answer.

⚠️ Note the wrapper on `last_occurrence`. The caller shouldn't have to pass `size - 1`, so a
public function sets up the starting index and a private one does the recursion — the same
shape `palindrome.cpp` uses ([note 003 §7](003-writing-base-cases.md)).

---

## 3. Combine Results — Count, Minimum, Maximum

These three are the **same three lines** with a different way of folding in the last element:

```cpp
int count_occurrence(int arr[], int size, int target)
{
    if (size == 0) return 0;                            // empty → 0 matches
    return (arr[size - 1] == target ? 1 : 0)
         + count_occurrence(arr, size - 1, target);     // fold with +
}

int get_minimum(int arr[], int size)
{
    if (size == 1) return arr[0];                       // one element → it IS the min
    return min(get_minimum(arr, size - 1), arr[size - 1]);   // fold with min
}

int get_maximum(int arr[], int size)
{
    if (size == 1) return arr[0];
    return max(get_maximum(arr, size - 1), arr[size - 1]);   // fold with max
}
```

```text
answer(size)  =  fold( answer(size − 1) , arr[size − 1] )
```

| Problem | Fold with | Base case | Why that base |
|---|---|---|---|
| `count_occurrence` | `+` | `size == 0` → `0` | An empty array has zero matches |
| `get_minimum` | `min` | `size == 1` → `arr[0]` | An empty array has **no** minimum |
| `get_maximum` | `max` | `size == 1` → `arr[0]` | same |

> ⚠️ **Why the base cases differ** is worth remembering. Counting has a natural "nothing"
> answer (`0`), so it can stop at `size == 0`. Min and max don't — there's no sensible
> minimum of nothing — so they must stop at `size == 1`.
> This is the identity-element idea from [note 003 §5](003-writing-base-cases.md).

```text
arr = [ 1  3  9  15  14  9  37 ],  target = 9
count → 2      minimum → 1      maximum → 37
```

---

## 4. `is_sorted` — a Check That Stops Early

```cpp
bool is_sorted(int arr[], int size, int index = 0)
{
    if (index + 1 == size) return true;        // last element reached, nothing left to compare
    return arr[index] <= arr[index + 1] &&     // this pair is fine...
           is_sorted(arr, size, index + 1);    // ...and so is the rest
}
```

Two things to notice:

**It compares neighbours, not a running value.** Sorted means *every adjacent pair* is in
order, so one comparison per step is enough.

**`&&` short-circuits.** The moment a pair is out of order, the whole thing returns `false`
without recursing further.

```text
arr = [ 1  3  9  15  14  9  37 ]
          1≤3 ✓   3≤9 ✓   9≤15 ✓   15≤14 ✗  → stops here, returns false
```

### ⚠️ The empty-array hole

The base case is `index + 1 == size`. With `size == 0` that is `1 == 0` — never true — so
the function compares `arr[0]` and `arr[1]` on an array with **no elements**. That reads
past the end.

```cpp
if (size <= 1) return true;      // add this: empty and single-element arrays are sorted
```

> A base case that can never be *reached* is the same bug as having none
> ([note 003 §8](003-writing-base-cases.md)). `size == 1` works fine here; only `size == 0`
> falls through.

---

## 5. Sorted Array? Throw Away Half Instead of One

Everything above drops **one** element per call → `O(n)`. If the array is sorted, the middle
element tells you which **half** to discard → `O(log n)`.

📄 [`binary_search.cpp`](../../programs/cpp/techniques/recursion/binary_search.cpp)

```cpp
int binary_search(int arr[], int val, int low, int high)
{
    if (low > high) return -1;                 // range is empty → not found

    int mid = low + (high - low) / 2;

    if (arr[mid] == val)     return mid;                                  // found
    else if (arr[mid] > val) return binary_search(arr, val, low, mid - 1); // left half
    else                     return binary_search(arr, val, mid + 1, high);// right half
}
```

```text
arr = [ 10  20  30  40  50 ],  val = 40
index    0   1   2   3   4

low=0 high=4   mid=2 → 30 < 40   → go right
low=3 high=4   mid=3 → 40 = 40   → found at index 3

2 comparisons instead of 4
```

### Three things that matter

```text
low > high            the "not found" base case — the range has collapsed to nothing
mid - 1 / mid + 1     EXCLUDE mid, which was already checked — otherwise infinite recursion
low + (high-low)/2    same value as (low+high)/2, but can't overflow
```

> ⚠️ Only **one** of the two branches ever runs, so this is `T(n) = T(n/2) + O(1)` → `O(log n)`,
> not `2T(n/2)`. See [note 005, Pattern 3](005-recursion-problem-patterns.md).

```text
Shrink by one   O(n)        works on any array
Halve           O(log n)    requires a SORTED array
```

The recursive version uses `O(log n)` stack; the iterative loop version is `O(1)` space and
usually preferred in production.

---

## 6. Summary

| Problem | Direction | Base case | Time | Space |
|---|---|---|---|---|
| `first_occurrence` | forward | `index >= size` → `-1` | `O(n)` | `O(n)` |
| `last_occurrence` | backward | `index < 0` → `-1` | `O(n)` | `O(n)` |
| `count_occurrence` | backward | `size == 0` → `0` | `O(n)` | `O(n)` |
| `is_sorted` | forward | `index + 1 == size` → `true` | `O(n)` | `O(n)` |
| `get_minimum` | backward | `size == 1` → `arr[0]` | `O(n)` | `O(n)` |
| `get_maximum` | backward | `size == 1` → `arr[0]` | `O(n)` | `O(n)` |
| `binary_search` | **halving** | `low > high` → `-1` | **`O(log n)`** | `O(log n)` |

---

## 7. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| `is_sorted` with no `size <= 1` guard | Reads past the end of an empty array |
| `size == 0` base for min / max | There is no minimum of nothing — use `size == 1` |
| Recursing with `mid` instead of `mid ± 1` | Infinite recursion — mid gets rechecked forever |
| `(low + high) / 2` | Can overflow for large indices; use `low + (high − low) / 2` |
| Binary search on an unsorted array | Silently wrong answers — sorting is a precondition |
| Forgetting the wrapper | The caller shouldn't have to pass `size - 1` or `0` |
| Expecting `O(1)` space | Every one of these is `O(depth)` on the call stack |

---

## 8. Quick Recall

| Question | Answer |
|---|---|
| **How do you make an array problem recursive?** | Move a boundary: look at one element and recurse on the rest. |
| **The two directions?** | Forward (`index + 1`, base `index >= size`) and backward (`size − 1`, base `size == 0/1`). |
| **When is each easier?** | Forward to find the first match; backward when the answer combines every element. |
| **First vs last occurrence?** | The same scan in opposite directions — the first match you hit is the answer. |
| **The shape of count / min / max?** | `fold(answer(size − 1), arr[size − 1])` with `+`, `min` or `max`. |
| **Why do min and max stop at `size == 1`, not `0`?** | An empty array has no minimum; counting has a natural zero. |
| **How does `is_sorted` exit early?** | `&&` short-circuits on the first out-of-order pair. |
| **What breaks `is_sorted`?** | An empty array — `index + 1 == size` is never reached, so it reads out of bounds. |
| **Why is binary search `O(log n)`?** | Each call discards half the range, and only one branch runs: `T(n) = T(n/2) + O(1)`. |
| **Why `mid − 1` and `mid + 1`?** | `mid` was already compared; including it again would recurse forever. |
| **Why `low + (high − low)/2`?** | Same midpoint without the overflow risk of `low + high`. |

---

## 9. The Mental Model

```text
                    An array problem
                           │
                           ↓
                  Is the array SORTED?
                 ╱                     ╲
               no                      yes
                │                        │
        shrink by ONE              throw away HALF
                │                        │
     ┌──────────┴──────────┐      mid decides which side
     ↓                     ↓             │
  forward              backward          ↓
  index + 1            size − 1       O(log n)
     │                     │
  first match          fold results
  / early exit         (+ · min · max)
     │                     │
     └─────────┬───────────┘
               ↓
             O(n)
```
