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
