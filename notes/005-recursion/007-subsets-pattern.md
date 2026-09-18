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

## 7. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Printing in every call | Half-finished subsets get printed too |
| Only one recursive call | Prints one subset instead of all `2ⁿ` |
| Forgetting `depth + 1` | Stuck on the same character forever |
| Mixing up subsets and permutations | Subsets keep the order (`2ⁿ`); permutations rearrange (`n!`) |

---

## 8. Quick Recall

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

## 9. The Mental Model

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
