# 001 — Introduction to Data Structures & Algorithms

> **One-line takeaway:** A *data structure* is how you **store & organise** data; an *algorithm* is the **step-by-step procedure** that works on it. Good software = the right data structure + the right algorithm.

---

## 1. What Is a Data Structure?

> **A data structure is a particular way of storing and organising data in memory so that it can be accessed and modified efficiently.**

It is not just "a place to keep data" — it also defines:

```text
Data Structure
      │
      ├── How data is laid out in memory
      ├── How elements relate to each other
      └── Which operations are cheap / expensive
```

Every data structure is really a **contract of operations**:

```text
Array        → index access is O(1), insert in middle is O(n)
Linked List  → insert at head is O(1), random access is O(n)
Stack        → push/pop at one end only  (LIFO)
Queue        → insert at rear, remove at front (FIFO)
Hash Table   → average O(1) search / insert / delete
Tree         → hierarchical data, O(log n) if balanced
Graph        → arbitrary relationships between entities
```

### Real-world analogy

```text
Books thrown in a pile   → finding one takes forever
Books on a sorted shelf  → find one in a few steps
```

Same data, different organisation, **very** different cost.

---

## 2. Why Do We Need Data Structures?

Because **the data is the same, but the cost of using it is not.**

### Reason 1 — Efficiency

The right structure turns an impossible operation into a trivial one.

```text
Search in an unsorted array   → O(n)
Search in a sorted array      → O(log n)   (binary search)
Search in a hash table        → O(1) avg
```

At `n = 1,000,000`:

```text
O(n)      → ~1,000,000 steps
O(log n)  → ~20 steps
O(1)      → ~1 step
```

### Reason 2 — Scalability

Programs work on *huge* inputs. An `O(n²)` design that is fine for 100 records dies at 1,00,000 records. Data structures are what keep growth manageable.

### Reason 3 — Reusability & abstraction

A data structure gives a clean, well-understood interface (`push`, `pop`, `insert`, `find`) so you can reason about *what* it does without re-deriving *how* every time.

### Reason 4 — The problem itself demands it

Some problems only make sense with the right structure:

```text
Undo / Redo             → Stack
Printer / CPU jobs      → Queue
File system, XML/HTML   → Tree
Maps, social network    → Graph
Dictionary / cache      → Hash Table
Priority scheduling     → Heap
Autocomplete            → Trie
```

> **Core idea:** Choosing the data structure *is* most of the design decision. The algorithm often follows from it.

---

## 3. What Is an Algorithm?

> **An algorithm is a finite, well-defined sequence of steps that takes some input and produces the desired output.**

```text
Input  →  [ Algorithm: finite sequence of steps ]  →  Output
```

Example — find the maximum in an array:

```text
1. Set max = first element
2. For every remaining element:
       if element > max → max = element
3. Return max
```

That is language-independent. The **same algorithm** can be written in C++, Java, Python — implementation changes, algorithm does not.

### Characteristics of a good algorithm

| Property | Meaning |
|---|---|
| **Input** | Zero or more well-defined inputs |
| **Output** | At least one well-defined output |
| **Definiteness** | Every step is unambiguous |
| **Finiteness** | It must terminate after a finite number of steps |
| **Effectiveness** | Each step is basic enough to actually be carried out |
| **Correctness** | Produces the right output for *every* valid input |
| **Efficiency** | Uses reasonable time and memory |

> An infinite loop is **not** an algorithm — finiteness is part of the definition.

---

## 4. How Data Structures and Algorithms Relate

The classic formulation (Niklaus Wirth):

```text
Algorithms + Data Structures = Programs
```

```text
        Problem
           │
           ↓
   Choose Data Structure  ←──── decides which operations are cheap
           │
           ↓
   Design Algorithm       ←──── uses those operations
           │
           ↓
   Analyse Time & Space
           │
           ↓
       Optimise
```

The same algorithm gets a different complexity depending on the structure underneath it:

```text
Dijkstra with a plain array  → O(V²)
Dijkstra with a min-heap     → O(E log V)
```

Same algorithm. Different data structure. Different world.

---

## 5. Classification of Data Structures

```text
                    Data Structures
                           │
            ┌──────────────┴──────────────┐
            ↓                             ↓
        Primitive                     Non-Primitive
   (int, char, float,                       │
    boolean, pointer)          ┌────────────┴────────────┐
                               ↓                         ↓
                            Linear                  Non-Linear
                               │                         │
                  ┌────────────┼───────────┐      ┌──────┴──────┐
                  ↓            ↓           ↓      ↓             ↓
               Array      Linked List   Stack   Tree          Graph
                                        Queue   Heap / Trie
```

### Linear vs Non-Linear

```text
Linear      → elements arranged in a sequence, one after another
              (Array, Linked List, Stack, Queue)

Non-Linear  → elements arranged hierarchically or arbitrarily
              (Tree, Graph, Heap)
```

### Static vs Dynamic

```text
Static   → size fixed at compile/creation time   (Array)
Dynamic  → size grows/shrinks at runtime         (Linked List, ArrayList, Vector)
```

### Contiguous vs Linked allocation

```text
Contiguous → one memory block, index math gives O(1) access   (Array)
Linked     → scattered nodes joined by pointers/references    (Linked List, Tree)
```

---

## 6. Common Operations on Any Data Structure

Whatever the structure, you will keep meeting the same operation set:

```text
Traversal  → visit every element
Search     → find an element
Insertion  → add an element
Deletion   → remove an element
Update     → modify an element
Sorting    → arrange in order
Merging    → combine two structures
```

> **Interview habit:** for every data structure you learn, memorise this table — the cost of each of these operations. That table *is* the data structure.

---

## 7. How to Choose the Right Data Structure

Ask, in order:

```text
1. What operations will be performed most often?
2. How frequent is each operation? (read-heavy vs write-heavy)
3. Is the data ordered? Does order need to be preserved?
4. Is the size known in advance, or does it grow?
5. What are the memory constraints?
6. Do I need fast lookup, fast insert, or fast ordered traversal?
```

Quick mapping:

| Need | Use |
|---|---|
| Fast random access by index | Array |
| Frequent insert/delete at ends or middle | Linked List |
| Last-in-first-out behaviour | Stack |
| First-in-first-out behaviour | Queue |
| Fast lookup by key | Hash Table |
| Sorted data + fast search | Balanced BST |
| Always need the min/max quickly | Heap |
| Relationships / connections | Graph |
| Prefix / word lookup | Trie |

---

## 8. Why DSA Matters for Interviews & Real Work

```text
Interviews  → tests problem-solving, not syntax memorisation
Performance → the difference between 20 s and 20 ms
Scale       → correct code that doesn't scale is still broken code
Foundation  → databases, OS, compilers, networks are all built on DSA
```

The loop an interviewer expects you to follow:

```text
Understand the problem
        ↓
Clarify constraints
        ↓
Brute force + its complexity
        ↓
Find the bottleneck
        ↓
Pick a better data structure
        ↓
Optimal approach + complexity
        ↓
Edge cases
```

---

## 9. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What is a data structure?** | A way of storing and organising data in memory so that operations on it are efficient. |
| **Why do we need data structures?** | Because efficiency depends on organisation — the same data, organised differently, changes operation cost from O(n) to O(1). |
| **What is an algorithm?** | A finite sequence of unambiguous steps that transforms input into the desired output. |
| **Properties of an algorithm?** | Input, output, definiteness, finiteness, effectiveness — plus correctness and efficiency. |
| **Difference between DS and algorithm?** | A data structure is the *organisation of data*; an algorithm is the *procedure operating on it*. Programs = Algorithms + Data Structures. |
| **Linear vs non-linear?** | Linear = sequential arrangement (array, list, stack, queue). Non-linear = hierarchical/arbitrary (tree, graph, heap). |
| **Static vs dynamic?** | Static has a fixed size at creation (array); dynamic grows/shrinks at runtime (linked list). |
| **Is an abstract data type (ADT) the same as a data structure?** | No. An ADT is the *interface* (what operations exist, e.g. "Stack: push/pop"); the data structure is the *implementation* (array-based or linked-list-based). |
| **How do you pick a data structure?** | By the operations you perform most and their required complexity, plus ordering, size and memory constraints. |

---

## 10. The Mental Model to Carry Forward

```text
                        DATA
                          │
                          ↓
              How do I organise it?      → DATA STRUCTURE
                          │
                          ↓
              What steps do I run on it? → ALGORITHM
                          │
                          ↓
              How does it scale?         → COMPLEXITY ANALYSIS
                          │
                          ↓
              Can it be cheaper?         → OPTIMISATION
```

> **Every topic in DSA is this same loop, repeated over a different structure and a different problem.**
