# 001 — Introduction to Arrays

> **One-line takeaway:** An array is a block of **same-sized** slots laid out **back to back** in memory. Those two facts alone let the CPU *calculate* where `arr[i]` lives instead of *searching* for it — that is the whole reason access is `O(1)`.

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

---

## 2. Characteristics — and What Each One Buys You

Every array property has a consequence. Remember the **consequence**, not just the property.

| Property | What it means | Consequence |
|---|---|---|
| **Contiguous** | Elements sit next to each other, no gaps | `O(1)` access · cache-friendly · but needs one big free block |
| **Homogeneous** | Every element is the same type → same size | Address of any element is computable |
| **Index-based** | Elements are reached by position, not by name or link | Random access instead of sequential walking |
| **0-indexed** (C, C++, Java) | First element is `arr[0]` | Index = offset from the start (see §3) |
| **Fixed size** (static arrays) | Size is set when the array is created | Can't grow in place → dynamic arrays exist ([note 003](003-static-vs-dynamic-arrays.md)) |

> 💡 The first two rows do almost all the work. **Contiguous + same size** is the
> combination that makes an array an array.

---

## 3. Why Random Access Is `O(1)`

The address of any element is one formula:

```text
address(arr[i])  =  base_address  +  i × element_size
```

```text
base = 1000, int = 4 bytes

arr[0]  →  1000 + 0 × 4  =  1000
arr[3]  →  1000 + 3 × 4  =  1012
arr[999_999]  →  1000 + 999_999 × 4      ← same amount of work
```

One multiply and one add, **no matter how big `i` is**. That's `O(1)`.

### Why it needs both properties

```text
not contiguous   →  arr[3] could be anywhere      →  must follow links  →  O(n)
not same size    →  can't know how far to jump    →  must walk & measure →  O(n)
```

That's exactly a **linked list**: to reach node 3 you must visit nodes 0, 1, 2 first.

### Why indexing starts at 0

Because the index **is the offset**. `arr[0]` is zero steps from the base. With 1-based
indexing the formula would need an extra `- 1` on every single access.

> In C/C++, `arr[i]` is literally `*(arr + i)` — pointer arithmetic, scaled by the type size.
> That's also why `i[arr]` compiles. (Fun trivia, never write it.)

---

## 4. The Hidden Superpower: Cache Locality

Big-O treats all `O(n)` loops as equal. Real hardware doesn't.

When the CPU reads `arr[0]`, it pulls a whole **cache line** (typically 64 bytes = 16 ints)
from RAM. The next 15 reads are nearly free.

```text
array         [ 10 | 20 | 30 | 40 | … ]    one fetch → many elements   ✅ fast
linked list   10 → (somewhere) → 20 → (elsewhere) → 30     one fetch per node   ❌ slow
```

> This is why iterating an array is often **several times faster** than iterating a linked
> list of the same length, even though both are `O(n)`. Great point to raise in interviews.

---

## 5. Common Misconceptions

| ✗ Belief | ✓ Reality |
|---|---|
| "`O(1)` access means `O(1)` search" | Access needs the **index**. Finding a **value** is still `O(n)` (or `O(log n)` if sorted). |
| "An array of objects stores the objects" | In Java, `String[]` stores **references** (same size each) — the objects live elsewhere. Access is still `O(1)`; following the reference is a second hop. |
| "Going out of bounds just throws an error" | Java throws `ArrayIndexOutOfBoundsException`. C/C++ give **undefined behaviour** — it may silently read garbage. |
| "`int[] a = new int[n]` with runtime `n` is a dynamic array" | Size chosen at runtime, but still **fixed** once created. Dynamic = can **grow**. |

---

## 6. Quick Recall

| Question | Answer |
|---|---|
| **Define an array.** | Same-type elements in contiguous memory, accessed by index. |
| **The address formula?** | `base + i × size_of_element` |
| **Why is access `O(1)`?** | The address is *computed* in constant time; no traversal needed. |
| **Which two properties make it possible?** | Contiguity and every element having the same size. |
| **Why 0-based indexing?** | The index equals the offset from the base address. |
| **Why can't a linked list do this?** | Nodes are scattered; the only way to node `i` is through nodes `0…i−1`. |
| **Hidden practical advantage?** | Cache locality — neighbouring elements arrive in the same cache line. |
| **Access vs search?** | Access by index `O(1)`; search by value `O(n)`. |

---

## 7. The Mental Model

```text
           contiguous  +  same element size
                        │
                        ↓
        address = base + i × size     (pure arithmetic)
                        │
          ┌─────────────┼──────────────┐
          ↓             ↓              ↓
    O(1) access    cache-friendly   0-based indexing
                                    (index = offset)
```
