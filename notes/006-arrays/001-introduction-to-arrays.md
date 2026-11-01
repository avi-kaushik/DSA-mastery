# 001 — Introduction to Arrays

> **One-line takeaway:** An array is a block of **same-sized** slots laid out **back to back** in memory. Those two facts alone let the CPU *calculate* where `arr[i]` lives instead of *searching* for it — that is the whole reason access is `O(1)`, and the root of every array strength and weakness.

---

## 1. What Is an Array?

> **An array is a collection of elements of the same type, stored in contiguous memory,
> and accessed by an integer index.**

Picture a street of identical houses numbered `0, 1, 2, …`. If you know where the street
starts and how wide each plot is, you can walk straight to house 37 without knocking on
the other 36 doors.

```text
index        0      1      2      3      4
          ┌──────┬──────┬──────┬──────┬──────┐
arr       │  10  │  20  │  30  │  40  │  50  │
          └──────┴──────┴──────┴──────┴──────┘
address    1000   1004   1008   1012   1016        (int = 4 bytes)
           ↑
       base address
```

Three words in the definition carry all the weight:

```text
same type     →  every slot is the same size
contiguous    →  no gaps between slots
index         →  position is the only "name" an element has
```

---

## 2. Declaring Arrays — C++ vs Java

```cpp
// C++
int a[5];                    // 5 ints, UNINITIALISED (garbage) if local
int b[5] = {};               // 5 zeros
int c[]  = {10, 20, 30};     // size inferred → 3
int* d   = new int[n];       // heap, size chosen at runtime; delete[] d later
std::array<int, 5> e{};      // fixed-size, knows its own size, no decay
```

```java
// Java
int[] a = new int[5];        // 5 zeros — always initialised
int[] c = {10, 20, 30};      // size inferred → 3
int n = c.length;            // a field, not a method
```

| | C++ built-in array | Java array |
|---|---|---|
| **Where it lives** | Stack (local) or heap (`new[]`) | Always heap — the variable holds a reference |
| **Default values** | Garbage for locals, zero for globals/statics | `0`, `false`, `0.0` or `null` — always |
| **Knows its length?** | No — you carry `size` yourself | Yes — `arr.length` |
| **Out of bounds** | **Undefined behaviour** — may silently read garbage | `ArrayIndexOutOfBoundsException` |
| **Size from a variable** | Only with `new[]` / `vector` (`int a[n]` is a non-standard GCC extension) | Always fine: `new int[n]` |
| **Assignment `b = a`** | Not allowed for arrays | Copies the **reference** — both names share one array |

> ⚠️ Java's `int[] b = a;` does **not** copy the array. Changing `b[0]` changes `a[0]`.
> Use `a.clone()` or `Arrays.copyOf(a, a.length)` for a real copy.

---

## 3. ⚠️ C++: Arrays Decay to Pointers — Why Every Function Takes `size`

Every program in this repo computes the size like this, and passes it alongside the array:

```cpp
int arr[] = {64, 12, 1, 57, 12, 54};
int size = sizeof arr / sizeof arr[0];       // 24 bytes / 4 bytes = 6
largest_element(arr, size);
```

Why not compute it inside the function?

```cpp
int largest_element(const int arr[], int size)   // arr is really `const int*` here
{
    sizeof arr;          // 8 on a 64-bit machine — the size of a POINTER, not the array
}
```

> 🔑 **When an array is passed to a function, it decays to a pointer to its first element.**
> The length information is lost. That's why `sizeof arr / sizeof arr[0]` works **only in
> the scope where the array was declared**, and why every array function takes `size`.

```text
in main         sizeof arr  =  6 × 4  = 24      ✅ real array
in a function   sizeof arr  =  8                ❌ just a pointer
```

Java never has this problem — the array object carries `length` with it.
In modern C++, `std::size(arr)` (C++17) gives a compile error instead of a wrong answer if
`arr` has decayed, and `std::array` / `std::vector` never decay at all.

---

## 4. Characteristics — and What Each One Buys You

Every array property has a consequence. Remember the **consequence**, not just the property.

| Property | What it means | Consequence |
|---|---|---|
| **Contiguous** | Elements sit next to each other, no gaps | `O(1)` access · cache-friendly · but needs one big free block |
| **Homogeneous** | Every element is the same type → same size | Address of any element is computable |
| **Index-based** | Elements are reached by position, not by name or link | Random access instead of sequential walking |
| **0-indexed** (C, C++, Java) | First element is `arr[0]` | Index = offset from the start (see §5) |
| **Fixed size** (static arrays) | Size is set when the array is created | Can't grow in place → dynamic arrays exist ([note 003](003-static-vs-dynamic-arrays.md)) |

> 💡 The first two rows do almost all the work. **Contiguous + same size** is the
> combination that makes an array an array.

---

## 5. Why Random Access Is `O(1)`

The address of any element is one formula:

```text
address(arr[i])  =  base_address  +  i × element_size
```

```text
base = 1000, int = 4 bytes

arr[0]        →  1000 + 0 × 4        =  1000
arr[3]        →  1000 + 3 × 4        =  1012
arr[999_999]  →  1000 + 999_999 × 4               ← same amount of work
```

One multiply and one add, **no matter how big `i` is**. That's `O(1)`.

### Why it needs both properties

```text
not contiguous   →  arr[3] could be anywhere      →  must follow links   →  O(n)
not same size    →  can't know how far to jump    →  must walk & measure →  O(n)
```

That's exactly a **linked list**: to reach node 3 you must visit nodes 0, 1, 2 first.

### Why indexing starts at 0

Because the index **is the offset**. `arr[0]` is zero steps from the base. With 1-based
indexing the formula would need an extra `− 1` on every single access.

> In C/C++, `arr[i]` is literally `*(arr + i)` — pointer arithmetic, automatically scaled by
> the type size. That's also why `i[arr]` compiles. (Fun trivia; never write it.)

### What about arrays of objects?

```text
Java  String[] names       each slot holds a REFERENCE (same size, e.g. 4–8 bytes)
                           the String objects themselves live elsewhere on the heap
```

Getting `names[i]` is still `O(1)` — every reference is the same size. Using the object is
a second hop to wherever it lives, which is why `int[]` iterates faster than `Integer[]`.

---

## 6. 2-D Arrays — the Same Formula, One Level Up

In C/C++, `int m[R][C]` is one contiguous block stored **row by row** (row-major):

```text
m = [ [1 2 3]
      [4 5 6] ]          R = 2, C = 3

memory:   1  2  3  4  5  6
          └ row 0 ┘└ row 1 ┘

address(m[i][j]) = base + (i × C + j) × element_size
```

Java does it differently: `int[][]` is an **array of references to row arrays**. Rows can
have different lengths ("jagged") and aren't guaranteed to be next to each other.

```text
C++   int m[2][3]      one block of 6 ints
Java  int[][] m        [ref0, ref1]  →  row0 = [1 2 3]   row1 = [4 5 6]   (separate objects)
```

> ⚠️ **Traverse row by row** (`for i … for j … m[i][j]`). Column-first jumps `C` elements
> per step and wastes the cache — same Big-O, much slower in practice.

---

## 7. The Hidden Superpower: Cache Locality

Big-O treats all `O(n)` loops as equal. Real hardware doesn't.

When the CPU reads `arr[0]`, it pulls a whole **cache line** (typically 64 bytes = 16 ints)
from RAM. The next 15 reads are nearly free. Hardware prefetchers also spot the sequential
pattern and fetch the *next* line before you ask.

```text
array         [ 10 | 20 | 30 | 40 | … ]                 one fetch → many elements   ✅
linked list   10 → (somewhere) → 20 → (elsewhere) → 30   one fetch per node          ❌
```

> This is why iterating an array is often **several times faster** than iterating a linked
> list of the same length, even though both are `O(n)`. Great point to raise in interviews.

---

## 8. Common Misconceptions

| ✗ Belief | ✓ Reality |
|---|---|
| "`O(1)` access means `O(1)` search" | Access needs the **index**. Finding a **value** is still `O(n)` (or `O(log n)` if sorted). |
| "An array of objects stores the objects" | Java `String[]` stores **references** — the objects live elsewhere. |
| "Going out of bounds just throws an error" | Only in Java. C/C++ give **undefined behaviour** — garbage, a crash, or nothing visible. |
| "`sizeof arr` gives the length inside a function" | In C++ it gives the size of a pointer. Pass `size`. |
| "`int[] b = a` copies the array" | Java copies the reference; both names point at one array. |
| "`new int[n]` with runtime `n` is a dynamic array" | Size chosen at runtime, but still **fixed** once created. Dynamic = can **grow**. |

---

## 9. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Loop `i <= size` | Reads `arr[size]` — one past the end |
| Local C++ array without `= {}` | Starts with garbage values |
| `sizeof` trick inside a function | Wrong length (pointer size ÷ element size) |
| Column-first traversal of a 2-D array | Correct but cache-unfriendly — noticeably slower |
| Comparing Java arrays with `==` | Compares references. Use `Arrays.equals(a, b)`. |

---

## 10. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Define an array.** | Same-type elements in contiguous memory, accessed by index. |
| **The address formula?** | `base + i × size_of_element` |
| **Why is access `O(1)`?** | The address is *computed* in constant time; no traversal needed. |
| **Which two properties make it possible?** | Contiguity and every element having the same size. |
| **Why 0-based indexing?** | The index equals the offset from the base address. |
| **Why can't a linked list do this?** | Nodes are scattered; the only way to node `i` is through nodes `0…i−1`. |
| **Why do C++ array functions take `size`?** | Arrays decay to pointers when passed; the length is lost. |
| **2-D address in row-major?** | `base + (i × C + j) × size` |
| **Java 2-D vs C++ 2-D?** | Java: array of row references (can be jagged). C++: one contiguous block. |
| **Hidden practical advantage?** | Cache locality — neighbouring elements arrive in the same cache line. |
| **Out of bounds in Java vs C++?** | Java throws; C++ is undefined behaviour. |

---

## 11. The Mental Model

```text
           contiguous  +  same element size
                        │
                        ↓
        address = base + i × size     (pure arithmetic)
                        │
     ┌──────────────┬───┴──────────┬────────────────┐
     ↓              ↓              ↓                ↓
 O(1) access   cache-friendly   0-based          2-D = same formula
                                (index = offset)  (i × C + j)
                        │
                        ↓
        but: fixed block → can't grow, middle changes must shift
                 (notes 002 & 003)
```
