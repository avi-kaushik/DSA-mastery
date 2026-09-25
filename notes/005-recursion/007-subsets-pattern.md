# 007 — The Subsets Pattern (Take / Don't Take)

> **One-line takeaway:** For each character, make one choice — **skip it or take it** — and recurse on the rest. Each choice doubles the number of subsets, so `n` characters give `2ⁿ` subsets.

---

## 1. What We Want

All subsets of `"ABC"` — every selection of characters, in their original order, including
none and all:

```text
""  A  B  C  AB  AC  BC  ABC          → 8 subsets
```

---

## 2. Build It From a Smaller Problem

> **Subsets of `"ABC"` = subsets of `"AB"`, plus the same list with `C` added to each.**

Start from the empty string and add one character at a time. Every step keeps the old list
and adds a copy with the new character:

```text
subsets("")     =  ""                                            → 1
subsets("A")    =  ""               +  A                         → 2
subsets("AB")   =  "", A            +  B, AB                     → 4
subsets("ABC")  =  "", A, B, AB     +  C, AC, BC, ABC            → 8
                   └── old list ──┘    └── old list + C ──┘
```

That's the whole idea:

```text
new list  =  old list                    (don't take the new character)
          +  old list with it added      (take the new character)
```

Each new character **doubles** the list: `1 → 2 → 4 → 8`. So `n` characters give **`2ⁿ`**
subsets.

---

## 3. How the Code Does It

📄 [`subsets.cpp`](../../programs/cpp/techniques/recursion/subsets.cpp)

The code uses the same idea, but works **from the front**: it decides `A` first, then
solves the smaller problem `"BC"` twice — once without `A`, once with it.

```text
subsets("ABC")  =  subsets("BC") without A   +   subsets("BC") with A
                =  "", C, B, BC              +   A, AC, AB, ABC
```

And `"BC"` is solved the same way, down to nothing:

```text
subsets("BC")   =  subsets("C") without B    +   subsets("C") with B
                =  "", C                     +   B, BC

subsets("C")    =  ""                        +   C
```

> This is also why the program prints **`"" C B BC A AC AB ABC`** — the "without A" half
> comes first, then the "with A" half.

---

## 4. The Decision Tree

Each level is one depth of the call stack, and decides one character:

```text
                   │                       ""
                   │            ┌───────────┴───────────┐
depth 0 · decide A │           ✗ A                     ✓ A
                   │           ""                      "A"
                   │      ┌─────┴─────┐           ┌─────┴─────┐
depth 1 · decide B │     ✗ B         ✓ B         ✗ B         ✓ B
                   │     ""          "B"         "A"        "AB"
                   │   ┌──┴──┐     ┌──┴──┐     ┌──┴──┐     ┌──┴──┐
depth 2 · decide C │  ✗C    ✓C    ✗C    ✓C    ✗C    ✓C    ✗C    ✓C
depth 3 · PRINT    │  ""    "C"   "B"  "BC"   "A"  "AC"  "AB"  "ABC"
```

**How to read it:**

```text
✗ = skip the character      ✓ = take the character
Each box value  = the subset built so far (`current`)
depth           = which character is being decided
depth 3         = all 3 decided → a finished subset → print it
```

> Only the **bottom row** gets printed. The rows above are half-finished subsets.

---

## 5. The Code

```cpp
void print_subsets(const string &pattern, string current = "", int depth = 0)
{
    // All characters decided → current is a finished subset
    if (pattern.length() == depth) {
        cout << current << " ";
        return;
    }

    // ✗ skip pattern[depth]
    print_subsets(pattern, current, depth + 1);

    // ✓ take pattern[depth]
    print_subsets(pattern, current + pattern[depth], depth + 1);
}
```

Just three parts:

| Part | What it does |
|---|---|
| `if (depth == length)` | Every character decided — print the subset |
| first call | Skip this character, move to the next |
| second call | Take this character, move to the next |

---

## 6. Complexity

```text
Subsets printed   2ⁿ
Time              O(n · 2ⁿ)     2ⁿ subsets, each up to n characters long
Space             O(n)          the call stack is n deep
```

> Strictly, each call keeps its own copy of `current`, so the memory held is `O(n²)`. Saying
> `O(n)` for the stack depth is the usual interview answer.

---

## 7. Same Tree, Different Question — Subset Sum

📄 [`subsets.cpp`](../../programs/cpp/techniques/recursion/subsets.cpp)

**Problem:** count how many subsets of an array add up to a target sum.

### The one idea that makes it easy

The obvious plan is to build each subset, add it up, and compare. That's a lot of work.

> **You never need the subset itself — only how much of the target is left.**
> So carry the **remaining sum** down instead of the subset. Taking an element means
> subtracting it. At the bottom, a subset worked if the remaining sum landed exactly on `0`.

```text
build the subset, then add it up      ✗ slow and fiddly
subtract as you go, check for 0       ✓ one int travels down the tree
```

### The code

```cpp
int subset_sum(int arr[], int depth, int sum)
{
    // No elements left — did we land exactly on the target?
    if (depth == 0)
        return sum == 0 ? 1 : 0;

    // ✗ skip arr[depth - 1]  → the sum still needed is unchanged
    int exclude = subset_sum(arr, depth - 1, sum);

    // ✓ use arr[depth - 1]   → the sum still needed shrinks
    int include = subset_sum(arr, depth - 1, sum - arr[depth - 1]);

    // count what both halves found
    return exclude + include;
}
```

### The tree — `{4, 2, 6}`, target `6`

```text
depth 3 · use 6?   │                          sum=6
                   │             ┌──────────────┴──────────────┐
                   │            ✗ 6                           ✓ 6
depth 2 · use 2?   │           sum=6                         sum=0
                   │      ┌──────┴──────┐               ┌──────┴──────┐
                   │     ✗ 2           ✓ 2             ✗ 2           ✓ 2
depth 1 · use 4?   │    sum=6         sum=4           sum=0        sum=-2
                   │   ┌──┴───┐      ┌──┴───┐        ┌──┴───┐      ┌──┴───┐
                   │  ✗4     ✓4     ✗4     ✓4       ✗4     ✓4     ✗4     ✓4
depth 0 · sum left │   6      2      4      0        0     -4     -2     -6
           count?  │   ✗      ✗      ✗      ✓        ✓      ✗      ✗      ✗
```

```text
Two leaves land on 0  →  answer 2      the subsets {4, 2} and {6}
```

### What actually changed from `print_subsets`

The tree is identical. Only three things differ:

| | `print_subsets` | `subset_sum` |
|---|---|---|
| **What travels down** | the subset built so far | the sum still needed |
| **At the bottom** | print it | `return sum == 0 ? 1 : 0` |
| **Combining the two calls** | nothing — both just print | `exclude + include` |

> **That's the pattern to recognise.** Skip/take stays the same; a problem changes only
> *what you carry down* and *what you do at the bottom*.

### Which end it starts from

`print_subsets` decides the **first** character and counts `depth` up.
`subset_sum` decides the **last** element and counts `depth` down (`arr[depth - 1]`).

Same tree, mirrored — and counting down is exactly the "build from a smaller problem" idea
from §2:

```text
count(first 3 elements) = count(first 2, same target)          ← skip the 3rd
                        + count(first 2, target − arr[2])      ← use the 3rd
```

### Complexity

```text
Time    O(2ⁿ)     two calls per element; each leaf is a single comparison
Space   O(n)      the call stack
```

> Note there's **no extra `n`** here, unlike `print_subsets`'s `O(n · 2ⁿ)` — nothing is
> built or printed, just one integer passed around.

### Watch out

| ✗ Mistake | Result |
|---|---|
| Returning only `include` | Half the tree is ignored — you must add both |
| `return sum == 0` at `depth == 0` only if something was picked | The empty subset legitimately sums to 0; for target 0 the answer includes it |
| Assuming equal values are one subset | It counts by **position**, so `{4, …, 4}` gives two different subsets |
| Adding `if (sum < 0) return 0;` blindly | A valid speed-up **only when all numbers are positive**; with negatives a branch can recover |

---

## 8. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Printing in every call | Half-finished subsets get printed too |
| Only one recursive call | Prints one subset instead of all `2ⁿ` |
| Forgetting `depth + 1` | Stuck on the same character forever |
| Mixing up subsets and permutations | Subsets keep the order (`2ⁿ`); permutations rearrange (`n!`) |

---

## 9. Quick Recall

| Question | Answer |
|---|---|
| **The idea in one line?** | For each character: skip it or take it, then move on. |
| **How do you build subsets of `"ABC"` from `"AB"`?** | Keep all subsets of `"AB"`, then add a copy of each with `C` attached. |
| **Why `2ⁿ`?** | Every new character doubles the list: `1 → 2 → 4 → 8`. |
| **What does `depth` mean?** | Which character is being decided right now. |
| **When do you print?** | Only when `depth == n` — every character has been decided. |
| **Why does the output start with `"" C B BC`?** | The code handles "without A" before "with A". |
| **Time?** | `O(n · 2ⁿ)`. |
| **Space?** | `O(n)` for the call stack. |

---

## 10. The Mental Model

```text
          one character  →  skip it  or  take it
                                 │
                                 ↓
                  move to the next character (depth + 1)
                                 │
                                 ↓
                  all decided?  →  print current
                                 │
                                 ↓
               n characters  →  2ⁿ subsets
```
