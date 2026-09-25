# 008 — The Permutations Pattern (Fix a Position, Swap, Undo)

> **One-line takeaway:** Walk through the string **one position at a time**. At each position, try every remaining character there — swap it in, recurse, then **swap it back**. `n` choices, then `n−1`, then `n−2` … gives `n!` permutations.

---

## 1. What We Want

All orderings of `"ABC"` — same characters, every possible arrangement:

```text
ABC  ACB  BAC  BCA  CBA  CAB          → 6 permutations  (3! = 6)
```

---

## 2. First: How This Differs From Subsets

Both problems walk a tree, but they ask **different questions at each step**:

| | Subsets | Permutations |
|---|---|---|
| **The question** | for each **character**: in or out? | for each **position**: which character goes here? |
| **Branches per level** | always 2 | `n`, then `n−1`, then `n−2` … |
| **Answers** | `2ⁿ` | `n!` |
| **Order matters?** | no — `AB` and `BA` are the same subset | yes — `AB` and `BA` are different |
| **Shape of the code** | two calls | a **loop** of calls |

> **Spotting it:** the moment the problem says *"arrangements"*, *"orderings"* or
> *"rearrange"* — you're choosing what goes in each **slot**, not whether to keep each item.
> Two recursive calls become a loop.

---

## 3. Build It From a Smaller Problem

> **Fix one character at the front, and permute whatever is left.**

```text
permutations("ABC")  =  A + permutations("BC")     →  ABC, ACB
                     =  B + permutations("AC")     →  BAC, BCA
                     =  C + permutations("AB")     →  CAB, CBA
```

Each of the 3 choices leaves a smaller problem of size 2, which leaves one of size 1:

```text
3 choices × 2 choices × 1 choice  =  6  =  3!
```

That shrinking branch count is exactly why permutations are `n!` and subsets are `2ⁿ`.

---

## 4. How the Code Does It — the Swap Trick

📄 [`permutations.cpp`](../../programs/cpp/techniques/recursion/permutations.cpp)

To put a character at position `n`, you'd normally need a second string for "the characters
left over". The swap avoids that entirely:

```text
swap(s[n], s[i])   →  the chosen character is now at position n
                      everything still unused sits in s[n+1 …]
```

So the string splits itself into two parts, for free:

```text
"ABC",  n = 1        A │ B C
                  fixed │ still to arrange
```

Three steps per candidate:

```text
1. swap it in       put candidate i at position n
2. recurse          solve position n + 1
3. swap it back     ← BACKTRACK: restore the string for the next candidate
```

---

## 5. The Tree

```text
position 0         │                               "ABC"
                   │           ┌─────────────────────┼───────────────────┐
                   │         put A                 put B               put C
position 1         │         "ABC"                 "BAC"               "CBA"
                   │      ┌────┴────┐           ┌────┴────┐          ┌───┴───┐
                   │    put B     put C       put A     put C      put B   put A
position 2 · PRINT │    "ABC"     "ACB"       "BAC"     "BCA"      "CBA"   "CAB"
```

```text
Each level fixes ONE position
Branches shrink: 3 → 2 → 1
The string shown is what it looks like AFTER the swap
Bottom row = all positions fixed → print
```

> Look at the right-hand branch: after swapping `A` and `C` the string is `"CBA"`, so the
> leftovers are now `B, A` **in that order** — which is why `CBA` prints before `CAB`.

---

## 6. The Code

```cpp
void print_permutations(string &s, int n = 0)
{
    // Every position fixed → one complete permutation
    if (n == s.length()) {
        cout << s << " ";
        return;
    }

    // Try every remaining character at position n
    for (int i = n; i < s.length(); i++) {
        swap_characters(s, n, i);          // 1. put candidate i here
        print_permutations(s, n + 1);      // 2. solve the rest
        swap_characters(s, n, i);          // 3. undo — backtrack
    }
}
```

| Part | What it does |
|---|---|
| `if (n == length)` | Every position decided — print the arrangement |
| `for (i = n; …)` | The candidates: position `n` itself, plus everything after it |
| first swap | Move the candidate into position `n` |
| recursive call | Fill positions `n+1` onward |
| second swap | Put the string back the way it was |

> `i` starts at `n`, not `0` — positions before `n` are already fixed and must not move.
> Starting at `n` also means "leave this character where it is" is one of the choices.

---

## 7. ⚠️ Why the Swap Back Matters

This is the line that's easiest to forget, and the reason the output silently goes wrong.

Each loop iteration assumes the string looks the way it did at the start of the loop. If
you don't restore it, the next iteration starts from a scrambled string:

```text
with the undo     ABC ACB BAC BCA CBA CAB      ← 6 distinct, correct
without the undo  ABC ACB CAB CBA ABC ACB      ← ABC and ACB repeat, BAC and BCA vanish
```

> **It doesn't crash and it doesn't look obviously broken** — you still get six lines. That
> is exactly what makes it dangerous. Undo is not optional cleanup; it's part of the
> algorithm.

---

## 8. Complexity

```text
Permutations    n!
Time            O(n · n!)    n! results, each n characters to print
Space           O(n)         call stack only — the swaps happen in place
```

The swap approach is nice on memory: no new string per call, so nothing but the stack.

```text
n = 5   →   120
n = 10  →   3,628,800
n = 12  →   479,001,600      ← already too slow
```

> Permutations blow up faster than subsets. `2ⁿ` is fine to about `n = 20`; `n!` is not
> usable past roughly `n = 10`.

---

## 9. Two Things This Version Does Not Do

**The output is not in alphabetical order:**

```text
this code     ABC ACB BAC BCA CBA CAB
alphabetical  ABC ACB BAC BCA CAB CBA      ← last two differ
```

Swapping disturbs the order of the leftover characters, so the results come out in tree
order, not sorted order. Sort the string and generate differently if order matters.

**Repeated characters give repeated permutations:**

```text
"AAB"  →  AAB ABA AAB ABA BAA BAA      6 results, only 3 distinct
```

The code swaps by **position**, and two identical letters sit in different positions. To
get distinct results you have to skip a candidate you've already tried at this position.

---

## 10. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Forgetting the swap back | Duplicates and missing permutations — and no crash |
| Starting the loop at `0` instead of `n` | Disturbs already-fixed positions; wrong output |
| Base case `n == length - 1` | Misses the last position, or prints early |
| Expecting alphabetical order | The swap method gives tree order |
| Expecting distinct results with repeats | It permutes positions, not values |
| `i < s.length()` with `int i` | `g++ -Wall` warns (signed vs unsigned); cast with `(int)s.size()` |

---

## 11. Quick Recall

| Question | Answer |
|---|---|
| **The idea in one line?** | For each position, try every remaining character there, then undo the swap. |
| **How is this different from subsets?** | Subsets ask "in or out?" per element (`2ⁿ`); permutations ask "which one goes here?" per position (`n!`). |
| **How do you build it from a smaller problem?** | Fix one character at the front, permute the rest: `A + perms("BC")`, etc. |
| **Why `n!`?** | `n` choices for the first slot, `n−1` for the next, and so on. |
| **What does the swap achieve?** | It places the candidate and leaves all unused characters in `s[n+1…]`, so no extra array is needed. |
| **Why swap back?** | The next loop iteration needs the original string; without it you get duplicates and missing results. |
| **Why does the loop start at `i = n`?** | Earlier positions are already fixed, and it lets the character stay where it is. |
| **Time and space?** | `O(n · n!)` time, `O(n)` space — swaps are in place. |
| **Is the output sorted?** | No — swapping reorders the leftovers, so results come out in tree order. |
| **What about duplicate characters?** | You get duplicate permutations; skipping repeated candidates at a position fixes it. |

---

## 12. The Mental Model

```text
             position n  →  which character goes here?
                                   │
                    try each candidate i from n onward
                                   │
                        ┌──────────┴──────────┐
                        ↓          ↓          ↓
                     swap in    recurse    swap back
                   (place it)  (n + 1)     (restore)
                                   │
                                   ↓
                  all positions fixed  →  print the string
                                   │
                                   ↓
                   n · (n−1) · … · 1  =  n!
```
