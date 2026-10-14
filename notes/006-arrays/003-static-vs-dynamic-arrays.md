# 003 — Static vs Dynamic Arrays (+ Advantages & Disadvantages)

> **One-line takeaway:** A dynamic array is just a static array with a **growth plan**: when it's full, allocate one roughly **twice as big**, copy everything over, and carry on. Doubling makes the occasional `O(n)` copy so rare that `push_back` / `add` costs **`O(1)` amortized**.

---

## 1. Static Arrays

> **Size is fixed when the array is created. It can never grow or shrink.**

```cpp
int a[10];                 // C++: size fixed at compile time (stack)
int* b = new int[n];       // C++: size chosen at runtime (heap) — still fixed afterwards
```
```java
int[] c = new int[n];      // Java: always on the heap, still fixed once created
```

> ⚠️ "Static" means **fixed size**, not "known at compile time" or "on the stack".
> `new int[n]` is a static array whose size you just decided late.

**Why can't it simply grow?** Contiguity. The memory right after the array may already
belong to something else, so extending in place isn't guaranteed.

---

## 2. Dynamic Arrays — `vector` / `ArrayList`

A dynamic array tracks two numbers:

```text
size      how many elements you've stored     (what you see)
capacity  how many slots are allocated        (what's reserved)

[ 10  20  30  40  _  _  _  _ ]      size = 4, capacity = 8
```

When `size == capacity` and you add one more:

```text
1. allocate a new array of ~2 × capacity
2. copy all old elements over                ← O(n)
3. free the old array
4. insert the new element

[ 10 20 30 40 ]  full!
        ↓ copy
[ 10 20 30 40 50 _ _ _ ]
```

---

## 3. Why Appending Is `O(1)` *Amortized*

Most appends are `O(1)`. Occasionally one triggers an `O(n)` copy. Averaged over many
appends:

```text
copies made while growing to n with doubling:
   1 + 2 + 4 + 8 + … + n/2  <  n

n appends  →  n writes + < n copies  =  < 2n work  →  O(1) per append, on average
```

### Why doubling and not "+10 each time"?

```text
grow by +k     copies = k + 2k + 3k + … + n  ≈  n²/2k   →  O(n) per append  ❌
grow by ×2     copies < n                                →  O(1) per append  ✅
```

> 🔑 Growth must be **geometric** (multiply), not **arithmetic** (add). Any factor > 1 works;
> real libraries use 1.5× – 2× (Java `ArrayList` uses 1.5×; GCC's `vector` uses 2×).
> A smaller factor wastes less memory but copies more often.

> **Amortized ≠ average case.** Amortized is a *guarantee* over any sequence of operations —
> no luck or random input involved. A *single* append can still be `O(n)`.

### Shrinking (bonus)

If you shrink to half when the array is half full, alternating add/remove at that boundary
copies every time → `O(n)` per op ("thrashing"). Fix: **shrink to half only when ¼ full.**
(Java `ArrayList` never auto-shrinks; use `trimToSize()`.)

---

## 4. The Hidden Costs of "Dynamic"

| Cost | Why it happens | Mitigation |
|---|---|---|
| Occasional latency spike | One append copies everything | Pre-size: `reserve(n)` / `new ArrayList<>(n)` |
| Wasted memory | Up to ~half the capacity can be empty | `shrink_to_fit()` / `trimToSize()` |
| **Dangling pointers / iterators** (C++) | Reallocation moves the data; old addresses are invalid | Don't hold pointers across `push_back`; use indices |
| Boxing (Java) | `ArrayList<Integer>` stores references to objects, not raw `int`s | Use `int[]` for performance-critical code |

> 💡 If you know the final size up front, **reserve it**. You skip every reallocation and
> keep every append truly `O(1)`.

---

## 5. Static vs Dynamic at a Glance

| | Static | Dynamic |
|---|---|---|
| Size | Fixed at creation | Grows (and maybe shrinks) |
| Append | Only if spare room you manage yourself | `O(1)` amortized |
| Memory | Exactly what you asked for | Some unused capacity |
| Overhead | None | Size/capacity bookkeeping + occasional copy |
| Examples | `int a[10]`, `int[]`, `std::array` | `std::vector`, `ArrayList`, Python `list` |
| Use when | Size is known and stable | Size is unknown or changes |

Access, search and middle insert/delete costs are **identical** — underneath, both are a
contiguous block.

---

## 6. Advantages & Disadvantages of Arrays

Every pro and con traces back to **contiguous + same size** ([note 001](001-introduction-to-arrays.md)).

| ✅ Advantage | Because |
|---|---|
| `O(1)` random access | Address is computed |
| Cache-friendly, fast iteration | Neighbours share cache lines |
| No per-element overhead | No pointers stored, unlike linked-list nodes |
| Simple; foundation for others | Stacks, queues, heaps, hash tables, matrices are built on arrays |
| Binary search possible when sorted | Needs `O(1)` jump to the middle — impossible on a linked list |

| ❌ Disadvantage | Because |
|---|---|
| Middle insert/delete is `O(n)` | Must shift to stay contiguous |
| Fixed size (static) | Can't extend in place |
| Resizing costs a full copy (dynamic) | New block must be contiguous too |
| Needs one large free block | Fragmented memory may fail a big allocation even with enough total free space |
| Can't mix types | Same-size requirement (Java works around it with references) |

### Array or linked list?

```text
lots of access by index / iteration / search       →  array
frequent insert/delete in the middle, at a known
node, and you never need index access               →  linked list
```

> In practice, arrays (vectors) win far more often than Big-O suggests — the cache
> advantage frequently beats the linked list's `O(1)` insertion for realistic sizes.

---

## 7. Quick Recall

| Question | Answer |
|---|---|
| **Static vs dynamic?** | Fixed size vs grows by reallocating a bigger block. |
| **Is `new int[n]` dynamic?** | No — runtime-sized but fixed once created. |
| **What happens when a dynamic array is full?** | Allocate ~2× capacity, copy, free the old block. |
| **Cost of append?** | `O(1)` amortized, `O(n)` worst case for a single append. |
| **Why doubling?** | Geometric growth keeps total copies < `n`; arithmetic growth gives `O(n²)` total. |
| **Size vs capacity?** | Elements stored vs slots allocated. |
| **Why shrink at ¼, not ½?** | Avoids thrashing at the boundary. |
| **C++ gotcha with `vector`?** | Reallocation invalidates pointers, references and iterators. |
| **Biggest advantage of arrays?** | `O(1)` random access + cache locality. |
| **Biggest disadvantage?** | `O(n)` insert/delete in the middle; fixed size (static). |
| **Root cause of all pros and cons?** | Contiguous memory with same-size elements. |

---

## 8. The Mental Model

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
        total copies < n  →  O(1) amortized per append
```
