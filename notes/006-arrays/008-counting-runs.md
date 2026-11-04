# 008 — Counting Runs: Frequencies, Max Consecutive Ones & Longest Even-Odd Subarray

> **One-line takeaway:** A **run** is a stretch of consecutive elements that belong together — equal values, all `1`s, or alternating parity. Count while the run continues, act when it **breaks**, and never forget the run that's still open when the loop ends. That last run is where almost every bug in this family hides.

📄 [`frequencies.cpp`](../../programs/cpp/data_structures/arrays/basic/frequencies.cpp) ·
📄 [`maximum_consecutive_ones.cpp`](../../programs/cpp/data_structures/arrays/basic/maximum_consecutive_ones.cpp) ·
📄 [`sub_array.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sub_array.cpp)

---

## 1. What Is a Run?

```text
[ 10 10 10 | 20 | 30 30 ]          runs of equal values:   10×3, 20×1, 30×2
[ 1 | 0 | 1 1 1 | 0 0 | 1 1 1 1 ]  runs of ones:           1, 3, 4
```

A run **ends** at a boundary — the place where `arr[i]` stops matching `arr[i − 1]`. You
only find out a run is over when you see the **first element of the next one**.

> 🔑 That's the whole difficulty: the **last** run has no "next element" to announce its
> end. The loop finishes and the run is still open.

---

## 2. The Run Template — and the Two Ways to Close the Last Run

```text
count = 1
for i = 1 … n−1:
    if arr[i] continues the run:   count++
    else:                          handle(run ending at i−1);   count = 1
handle(final run)                  ← style A: close it after the loop
```

```text
count = 0, best = 0
for i = 0 … n−1:
    count = continues ? count + 1 : reset
    best  = max(best, count)       ← style B: "close" on every step — nothing left over
```

| | Style A — handle on break | Style B — update every step |
|---|---|---|
| **Use when** | each run must be **reported** (print every group) | only the **best** run matters |
| **Last run** | needs an explicit line after the loop | handled automatically |
| **Example** | frequencies (Card 1) | max consecutive ones (Card 2) |

> 💡 Prefer style B whenever the answer is a running max or min — it's impossible to forget
> the last run.

---

# Card 1 — Frequencies in a Sorted Array

**Run template, style A** · 📄 [`frequencies.cpp`](../../programs/cpp/data_structures/arrays/basic/frequencies.cpp)

### 🎯 Problem

Print each distinct value of a **sorted** array with how many times it appears.

```text
[10, 10, 10, 20, 30, 30]   →   10: 3   20: 1   30: 2
```

### Three approaches

| Approach | Time | Space | Works on unsorted? |
|---|---|---|---|
| For each element, count it with an inner loop | `O(n²)` | `O(1)` | yes (but prints duplicates) |
| Hash map `value → count` | `O(n)` | `O(n)` | **yes** |
| **Run counting** (implemented) | `O(n)` | `O(1)` | no — needs equal values adjacent |

### 💡 Insight

> **Sorted ⇒ equal values are adjacent.** "Same group?" is just "same as my left
> neighbour?" — the same observation behind dedupe in [note 005](005-two-pointers.md). No
> map needed.

### 💻 Code

```cpp
void print_frequencies_of_elements_in_sorted_array(const int arr[], int size)
{
    if (size == 0) return;

    int current_frequency = 1;                      // arr[0] starts the first run
    for (int i = 1; i < size; i++)
    {
        if (arr[i] == arr[i - 1])
            current_frequency++;                    // run continues
        else
        {
            cout << "Element: " << arr[i - 1]       // run ended at i − 1
                 << ", Frequency: " << current_frequency << endl;
            current_frequency = 1;                  // arr[i] starts a new run
        }
    }

    cout << "Element: " << arr[size - 1]            // the final run never "breaks"
         << ", Frequency: " << current_frequency << endl;
}
```

### 🔍 Trace — `[10, 10, 10, 20, 30, 30]`

```text
i   arr[i]   = arr[i−1]?   action                    freq
1    10          ✓          count                      2
2    10          ✓          count                      3
3    20          ✗          print "10: 3", reset       1
4    30          ✗          print "20: 1", reset       1
5    30          ✓          count                      2
after loop                  print "30: 2"           ← without this line, 30 is lost
```

### ⏱️ Complexity

`O(n)` time, `O(1)` space.

### ⚠️ Pitfalls

| ✗ Mistake | Result |
|---|---|
| Missing the print after the loop | The last group disappears |
| Printing `arr[i]` when the run breaks | Reports the **new** value with the **old** count |
| No empty-array guard | `arr[size − 1]` is out of bounds |
| Using it on unsorted input | `[10, 20, 10]` prints `10: 1` twice |

### 🔀 Variants

| Follow-up | Approach |
|---|---|
| Unsorted array | Hash map (`unordered_map` / `HashMap`) → `O(n)` time, `O(n)` space |
| Frequency of **one given** value in a sorted array | Binary search for first and last occurrence → `last − first + 1` in `O(log n)` (📄 [`CountOccurenciesInSorted.java`](../../programs/java/arrays/searching/CountOccurenciesInSorted.java)) |
| Most frequent value in a sorted array | Run counting with a running max (style B) |
| Run-length encoding (`aaabcc → a3b1c2`) | The exact same loop on a string |

---

# Card 2 — Maximum Consecutive Ones

**Run template, style B** · 📄 [`maximum_consecutive_ones.cpp`](../../programs/cpp/data_structures/arrays/basic/maximum_consecutive_ones.cpp)

### 🎯 Problem

In a binary array, find the length of the longest run of `1`s.

```text
[1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1]   →   4
```

### 💡 Insight

> **Extend on `1`, reset on `0`, and update the best on _every_ step.** Then the run
> that's still open at the end is handled automatically.

### 💻 Code

```cpp
int maximum_consecutive_ones(const int arr[], int size)
{
    int maximum = 0, current = 0;
    for (int i = 0; i < size; i++)
    {
        if (arr[i] == 1) current++;          // extend the current run
        else             current = 0;        // a zero breaks it

        maximum = max(maximum, current);     // every step → no "after the loop" case
    }
    return maximum;
}
```

Note the seeds: `current = 0` and `maximum = 0` are **correct** here, unlike Kadane or
largest-element — "no ones at all" really does mean an answer of `0`.

### 🔍 Trace — `[1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1]`

```text
arr       1 0 1 1 1 0 0 1 1 1 1 0 1
current   1 0 1 2 3 0 0 1 2 3 4 0 1
maximum   1 1 1 2 3 3 3 3 3 3 4 4 4      → 4
```

### What goes wrong with style A here

```cpp
// ❌ updates maximum only when a zero breaks the run
if (arr[i] == 1) current++;
else { maximum = max(maximum, current); current = 0; }
```

```text
[0, 1, 1, 1]   → the run of 3 never meets a zero → returns 0
```

### ⏱️ Complexity

`O(n)` time, `O(1)` space.

### 🔀 Variants an interviewer reaches for

| Follow-up | Technique |
|---|---|
| Longest run if you may flip up to `k` zeros | **Sliding window** (below) |
| Longest run of **any** equal value | Same template, condition `arr[i] == arr[i − 1]` |
| Longest alternating even/odd subarray | Same template, condition "parity differs from previous" → **Card 3** |
| Count of runs of ones | Count the `0 → 1` transitions |

### The `k`-flips follow-up — run counting grows into a sliding window

> Instead of resetting on a zero, allow up to `k` zeros **inside** the window. When there
> are too many, shrink from the left until there aren't.

```cpp
int left = 0, zeros = 0, best = 0;
for (int right = 0; right < size; right++) {
    if (arr[right] == 0) zeros++;
    while (zeros > k)                         // too many zeros → shrink
        if (arr[left++] == 0) zeros--;
    best = max(best, right - left + 1);
}
```

```text
k = 0 gives exactly maximum consecutive ones.
Each index enters and leaves the window once → O(n).
```

The full technique — fixed and variable windows — is in [Sliding Window](014-sliding-window.md).

---

# Card 3 — Longest Alternating Even-Odd Subarray

**Run template, style B — on pairs** · 📄 [`sub_array.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sub_array.cpp) (`get_maximum_even_odd_length`)

### 🎯 Problem

Find the length of the longest **subarray** in which every pair of neighbours has
**opposite parity** (even, odd, even, … or odd, even, odd, …).

```text
[5, 10, 20, 6, 3, 8]      →  3      (6, 3, 8)
[2, 3, −8, 7, −1, 2, 3]   →  4      (2, 3, −8, 7)
[7, 9, 11]                →  1      (any single element qualifies)
```

### The naive version

```cpp
for (int i = 0; i < size; i++) {          // every start
    int len = 1;
    for (int j = i + 1; j < size && (arr[j] % 2 == 0) != (arr[j - 1] % 2 == 0); j++)
        len++;
    best = max(best, len);
}
```

`O(n²)` — and every run is re-walked from each of its starting points.

### 💡 Insight

> **It's a run, but the condition is about a _pair_, not a value.** "Does `arr[i]` continue
> the run?" means "does its parity differ from `arr[i − 1]`'s?". Same template as Card 2:
> extend on yes, reset to `1` on no, update the best every step.

A run that breaks at `i` can't be rescued by starting anywhere inside it — every later
start would hit the same broken pair at `i`. So restarting **at `i`** is never wrong.

### 💻 Code

```cpp
int get_maximum_even_odd_length(const int arr[], int size)
{
    if (size == 0) return 0;

    int maximum = 1, current = 1;                 // a single element is a valid run
    for (int i = 1; i < size; i++)
    {
        bool is_alternating = (arr[i] % 2 == 0) != (arr[i - 1] % 2 == 0);

        if (is_alternating) current++;            // pair alternates → extend
        else                current = 1;          // two same parities → restart AT i

        maximum = max(maximum, current);
    }
    return maximum;
}
```

> Seeds are `1`, not `0` like Card 2: here every non-empty array has an answer of at least
> `1`, because a lone element trivially "alternates".

### 🔍 Trace — `[2, 3, −8, 7, −1, 2, 3]`

```text
i   pair        parities   alternates?   current   maximum
1   2, 3        E O            ✓            2         2
2   3, −8       O E            ✓            3         3
3   −8, 7       E O            ✓            4         4
4   7, −1       O O            ✗            1         4      ← restart at −1
5   −1, 2       O E            ✓            2         4
6   2, 3        E O            ✓            3         4
answer → 4   (2, 3, −8, 7)
```

### ⚠️ The negative-number trap

```text
C++ and Java:   −1 % 2  ==  −1        (the sign follows the dividend)

arr[i] % 2 == 1     ❌  says −1 is NOT odd
arr[i] % 2 == 0     ✅  even test works for negatives
arr[i] & 1          ✅  1 for odd, 0 for even — negatives included (two's complement)
```

The repo version tests `% 2 == 0` on both sides, which is why `7, −1` is correctly seen as
odd, odd. (Bitwise parity: [Bitwise Operators](../004-bitwise-operations/002-bitwise-operators.md).)

### ⏱️ Complexity

`O(n)` time, `O(1)` space.

### 🔀 Variants — same loop, different pair condition

| Problem | "Continues the run" when… |
|---|---|
| Longest alternating **sign** subarray | `(arr[i] < 0) != (arr[i − 1] < 0)` |
| Longest strictly **increasing** subarray | `arr[i] > arr[i − 1]` |
| Longest subarray of **equal** values | `arr[i] == arr[i − 1]` |
| Longest **zig-zag** subarray (up, down, up …) | the sign of `arr[i] − arr[i−1]` differs from the previous difference's |

---

## 3. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Forgetting the final run (style A) | Last group / last streak lost |
| Updating the best only on a break | Streak at the end of the array ignored |
| On a break, using `arr[i]` as the finished run's value | `i` is already the **new** run |
| Assuming sortedness that isn't there | Same value split into separate "groups" |
| Seeding counters at `1` when the array can be empty | Wrong answer for `[]` |

---

## 4. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **What's a run?** | Consecutive elements satisfying the same condition. |
| **When do you learn a run ended?** | Only when you see the first element of the next one. |
| **#1 bug in run loops?** | Forgetting the final run after the loop ends. |
| **Two ways to close the last run?** | Handle it after the loop, or update the answer on every step. |
| **Why does sorting help frequency counting?** | Equal values are adjacent — compare with the left neighbour. |
| **Frequencies, unsorted?** | Hash map, `O(n)` time and space. |
| **Frequency of one value, sorted?** | First + last occurrence by binary search → `O(log n)`. |
| **On a break, which element's run ended?** | `arr[i − 1]`, not `arr[i]`. |
| **Max consecutive ones?** | `+1` on one, reset on zero, `best = max(best, current)` every step. |
| **With `k` flips allowed?** | Sliding window holding at most `k` zeros. |
| **Longest even-odd subarray?** | Run template on pairs: extend if parity differs from the previous, else reset to `1`. |
| **Why seed `1` there but `0` for consecutive ones?** | Any single element alternates; but an array may contain no `1`s at all. |
| **Parity of negatives?** | `−1 % 2 == −1` in C++/Java — test `% 2 == 0` or `& 1`, never `% 2 == 1`. |
| **Complexity?** | `O(n)` time, `O(1)` space. |

---

## 5. The Mental Model

```text
              walk once, compare with the previous
                            │
              ┌─────────────┴─────────────┐
              ↓                           ↓
        run continues                run breaks
          count++               handle the old run, reset
                                          │
                          ┌───────────────┴───────────────┐
                          ↓                               ↓
                 report every group                only the best matters
                 (frequencies)                     (consecutive ones,
                                                    even-odd: condition on PAIRS)
                 → close last run                  → update every step,
                   AFTER the loop                    nothing left over
                                                          │
                                                 allow k mismatches?
                                                          ↓
                                                   sliding window
```
