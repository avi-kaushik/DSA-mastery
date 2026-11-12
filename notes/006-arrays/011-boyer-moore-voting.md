# 011 — Boyer–Moore Majority Vote: Winning by Cancellation

> **One-line takeaway:** If one value fills **more than half** the array, you can find it with one candidate and one counter: let every pair of **different** values cancel each other out. The majority has more supporters than everyone else combined, so it's the only value that can survive. `O(n)` time, `O(1)` space — but if a majority isn't **guaranteed**, the survivor must be **verified**.

---

## 1. The Problem This Concept Solves

> *Find the element that appears **more than `n/2`** times.*

```text
[8, 3, 4, 8, 8]       8 appears 3 times out of 5   →  8
[3, 3, 4, 2, 4, 4]    4 appears 3 times out of 6   →  none (exactly half isn't more than half)
```

> 💡 There can be **at most one** such element — two values each taking more than half would
> need more than `n` slots.

The standard tools all work, but each costs something:

| Approach | Time | Space | Cost |
|---|---|---|---|
| Count every element against every other | `O(n²)` | `O(1)` | slow |
| Hash map of frequencies | `O(n)` | `O(n)` | memory |
| Sort; the majority must sit at index `n/2` | `O(n log n)` | — | sorting |
| **Boyer–Moore** | **`O(n)`** | **`O(1)`** | needs a verification pass if not guaranteed |

Why the sorted middle works: a block of more than `n/2` equal values, wherever it starts,
must cover the middle index.

---

## 2. The Core Idea — Pair Up Rivals and Remove Them

**Analogy — a battlefield.** Every element is a soldier wearing its value as a flag. Whenever
two soldiers with **different** flags meet, they knock each other out. When the dust settles,
who can still be standing?

```text
soldiers:  8  3  4  8  8

8 vs 3 → both out
4 vs 8 → both out
8      → still standing   ✅
```

> 🔑 **The majority has more soldiers than all other armies combined.** Every knockout costs
> it at most one soldier and costs the rest at least one. It can't run out first.

You don't need to plan the pairings. Boyer–Moore does them **on the fly**, keeping just:

```text
candidate   the flag currently holding the field
vote        how many of its soldiers are still standing
```

```text
vote == 0             →  the field is empty: arr[i]'s flag takes over (candidate = arr[i])
arr[i] == candidate   →  reinforcement:  vote++
arr[i] != candidate   →  a rival knocks one out:  vote−−
```

```cpp
int candidate = 0, vote = 0;
for (int i = 0; i < n; i++) {
    if (vote == 0) candidate = arr[i];
    vote += (arr[i] == candidate) ? 1 : -1;
}
// candidate is the majority — IF one exists
```

### 🔍 Watch it run — `[2, 2, 1, 1, 1, 2, 2]`

```text
arr         2  2  1  1  1  2  2
candidate   2  2  2  2  1  1  2
vote        1  2  1  0  1  0  1      →  2   (appears 4 times out of 7 ✅)
```

The candidate changes along the way — the intermediate candidates mean nothing. Only the
final one matters.

---

## 3. Why It's Correct

Every `vote−−` removes **two different values** from consideration: one copy of the candidate
and one rival.

```text
Removing two DIFFERENT values never destroys a majority:
    majority count m > n/2
    after removing a pair:   if one was the majority →  m − 1 > (n − 2)/2   ✓
                             if neither was          →  m     > (n − 2)/2   ✓
```

At the end, what remains is `vote` copies of `candidate` with nothing left to cancel them. The
majority survives every cancellation, so it must be the one left. ∎

> 🔑 Interview sentence: **"each cancellation removes two different elements, which can never
> destroy the majority."**

---

## 4. The Catch — It Always Names Someone

Boyer–Moore **always** returns a candidate, even when no majority exists:

```text
[1, 2, 3]
1 takes the field (vote 1) · 2 cancels it (vote 0) · 3 takes the field (vote 1)
→ "3", but 3 appears once
```

The proof only says: *if* a majority exists, it's the candidate. So unless the problem
**guarantees** a majority, add a second pass:

```cpp
int count = 0;
for (int i = 0; i < n; i++) if (arr[i] == candidate) count++;
return count > n / 2 ? candidate : -1;      // still O(n) time, O(1) space
```

### 🧠 Pause and predict

After the first pass on `[8, 3, 4, 8, 8]`, `vote` is `1`. Does that mean `8` appears once?

<details>
<summary>Answer</summary>

No — `8` appears 3 times. `vote` is what **survived** after cancellations, not the frequency.
It's only a lower bound on "how much the candidate outnumbers the rest". To get the real
count, do the verification pass.

</details>

> 💡 Always ask: *"Is a majority guaranteed?"* The answer decides whether the second pass is
> needed — and asking it shows you understand the algorithm's limit.

---

## 5. Generalising — More Than `n/k` Times

> At most **`k − 1`** values can each appear more than `n/k` times.

So for `> n/3`, keep **two** candidates and two votes. A value matching neither, when both
votes are positive, cancels one vote from **each** — a three-way knockout.

```cpp
int c1 = 0, c2 = 0, v1 = 0, v2 = 0;
for (int x : arr) {
    if      (x == c1) v1++;
    else if (x == c2) v2++;
    else if (v1 == 0) { c1 = x; v1 = 1; }
    else if (v2 == 0) { c2 = x; v2 = 1; }
    else              { v1--; v2--; }        // x knocks out one of each
}
// verification of BOTH candidates is mandatory here
```

The same cancellation argument applies: each knockout removes `k` **different** values, which
can't eliminate a value with more than `n/k` copies.

---

## 6. Cancellation as a General Way of Thinking

The deeper lesson isn't the code — it's the **proof technique**:

> *"If I can repeatedly remove a group of items that can't all be the answer, without
> destroying the answer, then whatever survives contains the answer."*

That reasoning shows up again in tournament-style problems ("find the celebrity", "find the
candidate who beats everyone") and in streaming algorithms that summarise data in `O(1)` or
`O(k)` memory. Boyer–Moore itself works on a **stream**: it only ever looks at the current
element.

---

## 7. Recognising the Concept

```text
"appears more than n/2 times" (or n/3, n/k)          → Boyer–Moore (+ verification)
"O(1) extra space" + "most votes" style questions     → cancellation
data arrives as a STREAM, one pass only               → Boyer–Moore still works
"most frequent element" (no majority promised)        → NOT Boyer–Moore — use a hash map
```

---

## 8. Common Mistakes

| ✗ Mistake | Why it's wrong |
|---|---|
| Skipping verification when no majority is promised | `[1, 2, 3]` returns `3` |
| `>= n/2` instead of `> n/2` | `[3, 3, 4, 4]` would wrongly have a majority |
| Treating the final `vote` as the frequency | It's only what survived cancellation |
| Using it for "most frequent" | It finds a **majority**, nothing weaker |
| One candidate for the `n/3` version | Two answers are possible — you need two candidates |

---

## 9. Practice in This Repo

| Problem | Idea | 📄 |
|---|---|---|
| Majority element (guaranteed to exist) | candidate + vote, one pass | [`majority.cpp`](../../programs/cpp/data_structures/arrays/intermediate/majority.cpp) |
| Majority element (Java) | same | [`MajorityElement.java`](../../programs/java/arrays/basic/MajorityElement.java) |

> The C++ version documents its assumption that a majority exists; without it, add the
> verification pass from §4.

---

## 10. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Majority element?** | Appears **more than** `n/2` times; at most one exists. |
| **The idea?** | Cancel pairs of different values; the majority can't run out first. |
| **The three rules?** | `vote == 0` → new candidate · same → `vote++` · different → `vote−−`. |
| **Why is it correct?** | Removing two different values never destroys a majority. |
| **When is a second pass needed?** | Whenever a majority isn't guaranteed. |
| **Is the final vote the count?** | No — only what survived. |
| **Sorting approach?** | The majority must be at index `n/2`. `O(n log n)`. |
| **More than `n/3`?** | Two candidates, two votes, verify both. |
| **General `n/k`?** | At most `k − 1` candidates. |
| **Complexity?** | `O(n)` time, `O(1)` space; works on streams. |

---

## 11. The Mental Model

```text
        every element = a soldier carrying its value's flag
                             │
          two DIFFERENT flags meet → both knocked out
                             │
       ┌─────────────────────┼─────────────────────┐
       ↓                     ↓                     ↓
   field empty          same flag             different flag
   new candidate        vote++                vote−−
                             │
           survivor = the ONLY possible majority
                             │
          guaranteed? ── yes ──→ answer
                             │ no
                             ↓
          count it: more than n/2?  →  answer / none
```
