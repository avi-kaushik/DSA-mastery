# 🚀 DSA-Mastery

> Your ultimate guide to mastering Data Structures and Algorithms — with clean code, clear concepts, and complete confidence.

---

## 📌 Overview

This repository is a well-organized, topic-wise collection of Data Structures and Algorithms
concepts, problems, and solutions. It has two halves that work together:

- **[`notes/`](notes/)** — short, revision-ready notes on each topic. Written to be read in a
  few minutes before an interview, ending with a **Quick Recall** table of one-liners.
- **[`programs/`](programs/)** — runnable implementations, one problem per file, each with a
  Javadoc explanation and its **time and space complexity** documented in-place.

The goal is to build a strong foundation in DSA and prepare for technical interviews with
clarity and depth — not to collect solutions, but to be able to explain them.

---

## 🗂️ Repository Structure

```text
DSA-Mastery/
├── notes/                         ← concept notes, ordered and categorised
│   ├── README.md                  ← notes index
│   └── 001-getting-started/
│       ├── 001-introduction-to-dsa.md
│       └── 002-why-learn-dsa.md
│
├── programs/                      ← implementations
│   └── java/
│       ├── arrays/
│       ├── hashing/
│       ├── linkedlists/
│       ├── stack/
│       ├── queue/
│       ├── dequeue/
│       └── techniques/
│
├── .devcontainer/                 ← VS Code Dev Container definition
├── Dockerfile                     ← Java 21 (Eclipse Temurin) + g++ environment
└── docker-run.bat                 ← one-click container start on Windows
```

---

## 📒 Notes

Concept notes live in **[`notes/`](notes/)** and are indexed in
**[`notes/README.md`](notes/README.md)**.

Naming convention:

```text
notes/<NNN>-<category>/<NNN>-<topic>.md
```

- The folder prefix orders the topic areas; the file prefix orders topics within an area.
- Every note follows the same skeleton: definition → why it matters → diagram → example →
  **Quick Recall** table → mental model.

---

### Sub-folder convention

```text
basic/          → fundamental operations and warm-up problems
intermediate/   → the standard interview-level problems
hard/           → advanced problems (e.g. LRU Cache)
common/         → shared building blocks reused by that topic
                  (e.g. Node, IntStack, IntQueue)
searching/      → topic-specific groupings where useful
multidimensional/
```

---

## ✍️ Code Conventions

Every implementation file follows the same rules, so any file reads the same way:

- **One problem per file**, named after the problem (`NextGreaterElement.java`).
- **Package rooted at the repository root** — e.g. `package programs.java.arrays.basic;`.
  This is why all commands below are run from the repo root.
- **A class-level comment** stating what the problem is.
- **Javadoc on the solution method**, including the approach and an explicit
  **Time Complexity** and **Space Complexity** note.
- **A `main` method** in each file with sample input, so any file can be run standalone.

Example header:

```java
package programs.java.arrays.basic;

// Class to check if the given array is sorted in ascending order or not.
class CheckSorted {

    /**
     * ...approach...
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
}
```

---

## 🛠️ Getting Started

Requires **JDK 21** and a **C++ compiler (g++)** — or run it in the provided container, which
already has both.

### Option 1 — VS Code Dev Container (recommended)

Open the repo in VS Code and choose **Reopen in Container**. The
[`.devcontainer/`](.devcontainer/devcontainer.json) definition builds the
[`Dockerfile`](Dockerfile), mounts the repo at `/code`, and installs the Java and C++
extensions.

### Option 2 — Docker on Windows

```bat
docker-run.bat
```

Builds the image, starts a container named `dsa-mastery` with the repo bind-mounted at
`/code`, and leaves it running. Then attach with:

```bash
docker exec -it dsa-mastery bash
```

### Option 3 — Local toolchain

Nothing to install beyond a JDK 21 and a g++ on your `PATH`.

### Compile and run a program

**Java** — always run from the **repository root**, because the packages are rooted there:

```bash
# compile
javac programs/java/arrays/basic/CheckSorted.java

# run (note: dots, not slashes — this is the package name)
java programs.java.arrays.basic.CheckSorted
```

To keep compiled classes out of the source tree:

```bash
javac -d out programs/java/arrays/basic/CheckSorted.java
java -cp out programs.java.arrays.basic.CheckSorted
```

`*.class` files are already git-ignored.

**C++** — compile to a binary and run it:

```bash
# compile
mkdir -p out
g++ -std=c++17 -O2 -o out/CheckSorted programs/cpp/arrays/basic/CheckSorted.cpp

# run
./out/CheckSorted
```

Compiled binaries and object files are git-ignored too.

---

## 🎯 How to Use This Repo

```text
Read the note        →  understand the concept and its trade-offs
Implement it         →  write the program yourself first
Compare              →  check against the version here
Revise               →  re-read only the Quick Recall tables before an interview
```

> The value is in being able to explain **why** a solution works — the code is just the
> proof that you understood it.
