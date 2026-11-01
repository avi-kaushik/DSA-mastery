# 003 — Static vs Dynamic Arrays (+ Advantages & Disadvantages)

> **One-line takeaway:** A dynamic array is just a static array with a **growth plan**: when it's full, allocate one roughly **twice as big**, copy everything over, and carry on. Doubling makes the occasional `O(n)` copy so rare that `push_back` / `add` costs **`O(1)` amortized**.

---

## 1. Static Arrays

> **Size is fixed when the array is created. It can never grow or shrink.**

```cpp
// C++
int a[10];                     // size fixed at compile time — lives on the stack
int* b = new int[n];           // size chosen at runtime — heap — still fixed afterwards
std::array<int, 10> c{};       // compile-time size, but knows its size and never decays
```

```java
// Java
int[] d = new int[n];          // always heap, size may come from a variable — still fixed
```

> ⚠️ **"Static" means fixed size**, not "known at compile time" and not "on the stack".
> `new int[n]` is a static array whose size you decided late.

### Why can't it simply grow?

**Contiguity.** The memory right after the array may already belong to something else:

```text
[ a0 a1 a2 a3 ][ some other variable ][ … ]
              ↑
     can't extend here — the next bytes are taken
```

To get more room you need a **new, bigger block somewhere else**, and you must copy
everything into it. That's exactly what a dynamic array automates.

---

## 2. Dynamic Arrays — How `vector` / `ArrayList` Work Inside

A dynamic array is three things: a pointer to a heap block, a **size**, and a **capacity**.

```text
size      how many elements you've stored     (what you see)
capacity  how many slots are allocated        (what's reserved)

[ 10  20  30  40  _  _  _  _ ]      size = 4, capacity = 8
```

That's the same capacity/size model as `basic_operations.cpp` in
[note 002](002-array-operations.md) — plus one extra rule for when it's full.

### What happens on `push_back` / `add`

```text
if size < capacity:
    arr[size++] = x                          ← the normal, O(1) path
else:
    1. allocate a new block of ~2 × capacity
    2. copy all `size` elements over         ← O(n)
    3. free the old block
    4. arr[size++] = x
```

```text
[ 10 20 30 40 ]                 full! (size 4 = capacity 4)
        ↓  allocate 8, copy 4
[ 10 20 30 40 50 _ _ _ ]        size 5, capacity 8
```

---

## 3. Why Appending Is `O(1)` *Amortized*

### 🔍 Watch it grow — 9 pushes, starting at capacity 1

```text
push   size   capacity   copies this push
  1      1        1          0
  2      2        2          1      ← grow
  3      3        4          2      ← grow
  4      4        4          0
  5      5        8          4      ← grow
  6–8   6–8       8          0
  9      9       16          8      ← grow
                           ────
                    total   15   copies for 9 pushes
```

Copies happen rarely, and each grow copies as many elements as **all previous grows
combined** (`8 > 1 + 2 + 4`). That's the doubling sum:

```text
total copies to reach n  =  1 + 2 + 4 + … + n/2   <   n

n pushes  =  n writes  +  < n copies  =  < 2n work   →   O(1) per push, on average
```

### The "savings account" intuition

Charge every push **3 units** instead of 1:

```text
1 unit   pays for writing the element now
2 units  go into savings
```

Between two grows (capacity `c → 2c`), `c/2` new elements arrived, saving `c` units — exactly
enough to pay for copying all `c` elements. The expensive push is **pre-paid** by the cheap
ones. Constant cost per push → `O(1)` amortized.

### Why doubling, and not "+10 each time"?

```text
grow by +k     copies = k + 2k + 3k + … + n  ≈  n² / 2k   →  O(n) per push  ❌
grow by ×2     copies < n                                →  O(1) per push  ✅
```

> 🔑 Growth must be **geometric** (multiply), not **arithmetic** (add). With `+k`, the
> number of grows is `n/k` — still linear — and each one copies more. With `×2`, there are
> only `log n` grows.

| Library | Growth factor |
|---|---|
| GCC / Clang `std::vector` | 2× |
| MSVC `std::vector` | 1.5× |
| Java `ArrayList` | 1.5× (`old + old >> 1`), default capacity 10 |
| Python `list` | ~1.125× plus a small constant |

Any factor > 1 gives `O(1)` amortized. A smaller factor wastes less memory but grows more
often.

### Amortized ≠ average case

```text
average case   depends on the INPUT distribution — "usually fast, on random data"
amortized      a GUARANTEE over ANY sequence — "n operations always cost O(n) total"
```

A **single** push can still be `O(n)`. If one slow operation is unacceptable (real-time
systems, latency-critical paths), amortized `O(1)` isn't enough — pre-allocate instead.

---

## 4. Shrinking — and the Thrashing Trap

If an array halves its capacity whenever it's half full, alternating push/pop at the
boundary is a disaster:

```text
capacity 8, size 4 → pop  → size 3 … shrink to 4?   push → full → grow to 8   …
each step copies everything  →  O(n) per operation
```

**Fix:** shrink to half only when the array is **¼ full**. After shrinking it's half full,
so it takes many operations before the next resize in either direction.

Real libraries mostly **never shrink automatically**:

```text
C++    v.shrink_to_fit()     (a non-binding request)
Java   list.trimToSize()
```

---

## 5. Size vs Capacity in Practice ⚠️

The most common real-world confusion:

```cpp
// C++
vector<int> v;
v.reserve(100);      // capacity ≥ 100, size 0 — v[5] is STILL out of range
v.resize(100);       // size 100, all zeros    — v[5] is valid
```

```java
// Java
List<Integer> list = new ArrayList<>(100);   // capacity 100, size 0
list.get(5);                                 // IndexOutOfBoundsException!
list.set(5, 1);                              // also throws — set needs an existing element
```

| | C++ | Java |
|---|---|---|
| Number of elements | `v.size()` | `list.size()` |
| Allocated slots | `v.capacity()` | *(not exposed)* |
| Pre-allocate, size unchanged | `v.reserve(n)` | `new ArrayList<>(n)` / `ensureCapacity(n)` |
| Change the size | `v.resize(n)` | *(no direct equivalent — add elements)* |
| Release spare capacity | `shrink_to_fit()` | `trimToSize()` |

> 💡 If you know the final size up front, **reserve it**. You skip every reallocation and
> every append is truly `O(1)`.

---

## 6. The Hidden Costs of "Dynamic"

| Cost | Why it happens | Mitigation |
|---|---|---|
| Occasional latency spike | One push copies everything | Pre-size with `reserve(n)` / `new ArrayList<>(n)` |
| Wasted memory | Up to ~half the capacity can be empty right after a grow | `shrink_to_fit()` / `trimToSize()` |
| **Dangling pointers / iterators** (C++) | Reallocation moves the data; old addresses now point at freed memory | Don't hold pointers or iterators across `push_back`; store indices |
| Boxing (Java) | `ArrayList<Integer>` stores references to `Integer` objects, not raw `int`s | Use `int[]` in performance-critical code |

```cpp
vector<int> v = {1, 2, 3};
int* p = &v[0];
v.push_back(4);        // may reallocate
cout << *p;            // ❌ undefined behaviour if it did
```

---

## 7. Static vs Dynamic at a Glance

| | Static | Dynamic |
|---|---|---|
| Size | Fixed at creation | Grows (rarely shrinks) |
| Append | Only into spare room you manage yourself | `O(1)` amortized, `O(n)` worst |
| Memory | Exactly what you asked for | Some unused capacity |
| Overhead | None | Size/capacity bookkeeping + occasional copy |
| Examples | `int a[10]`, `int[]`, `std::array` | `std::vector`, `ArrayList`, Python `list` |
| Use when | Size is known and stable | Size is unknown or changes |

Access, search, and middle insert/delete costs are **identical** — underneath, both are one
contiguous block.

---

## 8. Advantages & Disadvantages of Arrays

Every pro and con traces back to **contiguous + same size** ([note 001](001-introduction-to-arrays.md)).

| ✅ Advantage | Because |
|---|---|
| `O(1)` random access | Address is computed, not searched |
| Cache-friendly, fast iteration | Neighbours share cache lines; prefetching works |
| No per-element overhead | No pointers stored, unlike linked-list nodes |
| Binary search possible when sorted | Needs an `O(1)` jump to the middle — impossible on a linked list |
| Foundation for other structures | Stacks, queues, heaps, hash tables, matrices are built on arrays |
| Simple | One block, one index — few ways to go wrong structurally |

| ❌ Disadvantage | Because |
|---|---|
| Middle insert/delete is `O(n)` | Must shift to stay contiguous |
| Fixed size (static) | Can't extend in place |
| Resizing costs a full copy (dynamic) | The new block must be contiguous too |
| Needs one large free block | Fragmented memory may fail a big allocation even with enough total free space |
| Can't mix types | Same-size requirement (Java works around it with references) |
| Wasted space | Static: over-allocated "just in case". Dynamic: spare capacity. |

### Array or linked list?

| Operation | Array / vector | Linked list |
|---|---|---|
| Access `i`-th | `O(1)` | `O(n)` |
| Insert / delete at front | `O(n)` | `O(1)` |
| Insert at end | `O(1)` amortized | `O(1)` with a tail pointer |
| Delete at end | `O(1)` | `O(n)` singly linked (must find the new tail) · `O(1)` doubly linked |
| Insert / delete in the middle, **given the position/node** | `O(n)` | `O(1)` |
| Search | `O(n)` (`O(log n)` sorted) | `O(n)` always |
| Memory per element | just the value | value + 1–2 pointers |
| Cache behaviour | excellent | poor |

```text
lots of index access / iteration / search            →  array
frequent insert/delete at a node you already hold,
and you never need index access                      →  linked list
```

> In practice, arrays (vectors) win far more often than Big-O suggests — finding the
> middle node of a linked list is itself `O(n)`, and the cache advantage frequently beats
> the list's `O(1)` splice for realistic sizes.

---

## 9. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| "`push_back` is always `O(1)`" | It's **amortized** `O(1)`; a single call can be `O(n)` |
| `reserve(n)` then `v[i] = …` | Out of range — reserve doesn't change size |
| `new ArrayList<>(n)` then `get(i)` | `IndexOutOfBoundsException` — capacity isn't size |
| Holding a pointer into a vector across `push_back` | Dangling after reallocation |
| Growing by a fixed amount in a custom array | `O(n²)` total for `n` appends |
| Shrinking at half full | Thrashing — `O(n)` per op at the boundary |

---

## 10. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Static vs dynamic?** | Fixed size vs grows by reallocating a bigger block. |
| **Is `new int[n]` dynamic?** | No — runtime-sized but fixed once created. |
| **Why can't a static array grow in place?** | The memory after it may be taken; contiguity forbids gaps. |
| **What happens when a dynamic array is full?** | Allocate ~2× capacity, copy, free the old block. |
| **Cost of append?** | `O(1)` amortized, `O(n)` worst case for a single append. |
| **Why doubling?** | Geometric growth keeps total copies < `n`; arithmetic growth gives `O(n²)` total. |
| **Amortized vs average?** | Amortized is a guarantee over any sequence; average depends on input. |
| **Size vs capacity?** | Elements stored vs slots allocated. |
| **`reserve` vs `resize`?** | `reserve` changes capacity only; `resize` changes size. |
| **Why shrink at ¼, not ½?** | Avoids thrashing at the boundary. |
| **C++ gotcha with `vector`?** | Reallocation invalidates pointers, references, and iterators. |
| **Biggest advantage of arrays?** | `O(1)` random access + cache locality. |
| **Biggest disadvantage?** | `O(n)` insert/delete in the middle; fixed size (static). |
| **Root cause of all pros and cons?** | Contiguous memory with same-size elements. |

---

## 11. The Mental Model

```text
                 static array (fixed block)
                           │
              wrap it with size + capacity
                           │
                           ↓
                     dynamic array
                           │
                full?  ── no ──→ write → O(1)
                  │
                 yes
                  │
     allocate 2× · copy all · free old → O(n), but rare
                  │
                  ↓
   copies 1 + 2 + 4 + … < n  →  O(1) amortized per append
                  │
                  ↓
   still ONE contiguous block → same access / shift costs as static
```
