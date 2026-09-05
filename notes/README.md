# 📒 DSA Notes

Short, revision-ready notes on Data Structures and Algorithms.

Each note is a **summary, not a deep dive** — enough to recall a whole topic in a few
minutes and to answer it confidently in an interview.

---

## 🗂️ How These Notes Are Organised

```text
notes/
├── README.md                         ← this index
└── <NNN>-<category>/                 ← one folder per topic area
    └── <NNN>-<topic>.md              ← one file per topic, in reading order
```

- Folder prefix `001-`, `002-`, … → order of the topic areas
- File prefix `001-`, `002-`, … → order within a topic area
- Every note ends with a **Quick Recall** table for last-minute revision.

---

## 📚 Index

### 001 — Getting Started

| # | Topic | Notes |
|---|---|---|
| 001 | Introduction to Data Structures & Algorithms | [001-introduction-to-dsa.md](001-getting-started/001-introduction-to-dsa.md) |
| 002 | Why Learn DSA: Advantages, Applications & Competitive Programming | [002-why-learn-dsa.md](001-getting-started/002-why-learn-dsa.md) |

### 002 — Analysis of Algorithms

| # | Topic | Notes |
|---|---|---|
| 001 | Analysis of Algorithms & Asymptotic Analysis | [001-asymptotic-analysis.md](002-analysis-of-algorithms/001-asymptotic-analysis.md) |
| 002 | Order of Growth | [002-order-of-growth.md](002-analysis-of-algorithms/002-order-of-growth.md) |
| 003 | Best, Average & Worst Case | [003-best-average-worst-case.md](002-analysis-of-algorithms/003-best-average-worst-case.md) |
| 004 | Asymptotic Notations (Overview) | [004-asymptotic-notations.md](002-analysis-of-algorithms/004-asymptotic-notations.md) |
| 005 | Big-O Notation (Upper Bound) | [005-big-o-notation.md](002-analysis-of-algorithms/005-big-o-notation.md) |
| 006 | Omega Notation (Lower Bound) | [006-omega-notation.md](002-analysis-of-algorithms/006-omega-notation.md) |
| 007 | Theta Notation (Tight Bound) | [007-theta-notation.md](002-analysis-of-algorithms/007-theta-notation.md) |
| 008 | Analysis of Loops | [008-analysis-of-loops.md](002-analysis-of-algorithms/008-analysis-of-loops.md) |
| 009 | Math Refresher for Complexity Analysis | [009-math-refresher.md](002-analysis-of-algorithms/009-math-refresher.md) |
| 010 | Progressions & Series for Algorithm Analysis | [010-progressions-and-series.md](002-analysis-of-algorithms/010-progressions-and-series.md) |
| 011 | Recurrence Relations | [011-recurrence-relations.md](002-analysis-of-algorithms/011-recurrence-relations.md) |
| 012 | Solving Recurrences: Iteration & Substitution | [012-solving-recurrences.md](002-analysis-of-algorithms/012-solving-recurrences.md) |
| 013 | Recursion Tree Method | [013-recursion-tree-method.md](002-analysis-of-algorithms/013-recursion-tree-method.md) |
| 014 | Master Theorem | [014-master-theorem.md](002-analysis-of-algorithms/014-master-theorem.md) |
| 015 | Space Complexity & Auxiliary Space | [015-space-complexity.md](002-analysis-of-algorithms/015-space-complexity.md) |

### 003 — Mathematics

| # | Topic | Notes |
|---|---|---|
| 001 | Number & Digit Basics | [001-number-and-digit-basics.md](003-mathematics/001-number-and-digit-basics.md) |
| 002 | GCD, LCM & the Euclidean Algorithm | [002-gcd-lcm-euclidean.md](003-mathematics/002-gcd-lcm-euclidean.md) |
| 003 | Primes, Divisors & Factorization | [003-primes-and-factorization.md](003-mathematics/003-primes-and-factorization.md) |
| 004 | Fast Exponentiation (Binary Exponentiation) | [004-exponentiation.md](003-mathematics/004-exponentiation.md) |
| 005 | Modular Arithmetic | [005-modular-arithmetic.md](003-mathematics/005-modular-arithmetic.md) |
| 006 | Factorials & Combinatorics | [006-factorial-and-combinatorics.md](003-mathematics/006-factorial-and-combinatorics.md) |
| 007 | Patterns & Cheat Sheet | [007-patterns-and-cheatsheet.md](003-mathematics/007-patterns-and-cheatsheet.md) |

### 004 — Bitwise Operations

| # | Topic | Notes |
|---|---|---|
| 001 | Introduction to Bitwise Operations | [001-introduction-to-bitwise-operations.md](004-bitwise-operations/001-introduction-to-bitwise-operations.md) |
| 002 | Bitwise Operators | [002-bitwise-operators.md](004-bitwise-operations/002-bitwise-operators.md) |
| 003 | Binary Representation of Negative Numbers | [003-binary-representation-of-negative-numbers.md](004-bitwise-operations/003-binary-representation-of-negative-numbers.md) |

---

## ✍️ Note Format Convention

Every note follows the same skeleton so revision is predictable:

```text
# <NNN> — <Title>
> One-line takeaway

1..n  Concept sections (definition → why → diagram → example)
      Quick Recall — interview one-liners table
      Mental model / summary diagram
```

Related code lives in [`programs/`](../programs/).
