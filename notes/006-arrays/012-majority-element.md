# 012 — Majority Element: Boyer–Moore Majority Vote

> **One-line takeaway:** Pair up **different** elements and cancel them out. A value that appears more than `n/2` times can't run out of copies before everyone else does — so it's the one left standing. One pass, one candidate, one counter: `O(n)` time, `O(1)` space. But if a majority isn't **guaranteed**, the survivor must be **verified** with a second pass.

📄 [`majority.cpp`](../../programs/cpp/data_structures/arrays/intermediate/majority.cpp) ·
📄 [`MajorityElement.java`](../../programs/java/arrays/basic/MajorityElement.java)

---

## 1. What We Want

The **majority element** appears **strictly more than `n/2` times**.

```text
[8, 3, 4, 8, 8]        n = 5, 8 appears 3 times  > 2.5   →  8
[3, 3, 4, 2, 4, 4]     n = 6, 4 appears 3 times  = 3     →  none (not MORE than half)
[1, 2, 3]              nobody                            →  none
```

> 💡 There can be **at most one** majority element — two values each taking more than half
> would need more than `n` slots.

---

## 2. The Approaches Before Boyer–Moore

| Approach | Idea | Time | Space |
|---|---|---|---|
| Count each element | for every `i`, count its copies with an inner loop | `O(n²)` | `O(1)` |
| Hash map | count frequencies, return any with count `> n/2` | `O(n)` | `O(n)` |
| Sort | if a majority exists, it **must** occupy the middle index `n/2` | `O(n log n)` | `O(1)`–`O(n)` |
| **Boyer–Moore** | cancel out pairs of different elements | **`O(n)`** | **`O(1)`** |

### Why the sorted middle works

```text
majority takes > n/2 consecutive slots after sorting — wherever that block starts,
it must cover index n/2

[3 3 3 3 7 9]     [1 2 5 5 5 5]     [1 5 5 5 5 9]
       ↑                 ↑                 ↑
     n/2 = 3            n/2 = 3          n/2 = 3        always the majority
```

Boyer–Moore gets the same answer without sorting and without a map.

---

## 3. 💡 The Insight — an Election Where Rivals Knock Each Other Out

> **Imagine every element is a voter for its own value. Whenever two voters for different
> values meet, both leave. The majority has more voters than everyone else combined — so
> when the dust settles, only its voters can remain.**

```text
8  3  4  8  8
8 vs 3  → both leave
4 vs 8  → both leave
8       → left standing   ✅
```

The algorithm does this pairing **on the fly** with just two variables:

```text
candidate   the value currently "holding the floor"
vote        how many of its voters haven't been cancelled yet
```

```text
vote == 0             →  the floor is empty: arr[i] becomes the new candidate
arr[i] == candidate   →  one more supporter:   vote++
arr[i] != candidate   →  a rival cancels one:  vote−−
```

---

## 4. 💻 Code (implemented)

```cpp
int get_majority_element(const int arr[], int size)
{
    if (size == 0) return 0;

    int vote = 0, candidate = 0;
    for (int i = 0; i < size; i++)
    {
        if (vote == 0)               // all previous votes cancelled out
            candidate = arr[i];      //   → new candidate

        if (arr[i] == candidate)     // supporter
            vote++;
        else                         // rival → cancel one vote
            vote--;
    }
    return candidate;                // ⚠️ assumes a majority EXISTS
}
```

---

## 5. 🔍 Trace — `[8, 3, 4, 8, 8]`

```text
i   arr[i]   vote before   action                       candidate   vote
0     8          0          new candidate, supporter        8         1
1     3          1          rival → cancel                  8         0
2     4          0          new candidate, supporter        4         1
3     8          1          rival → cancel                  4         0
4     8          0          new candidate, supporter        8         1
return 8  ✅
```

Notice the candidate changes **three times**. The intermediate candidates (`4`) mean nothing —
only the final one is meaningful, and only if a majority exists.

### 🔍 Trace — a longer one, `[2, 2, 1, 1, 1, 2, 2]`

```text
arr        2  2  1  1  1  2  2
candidate  2  2  2  2  1  1  2
vote       1  2  1  0  1  0  1        → 2   (appears 4 times out of 7 ✅)
```

---

## 6. Why It's Correct

Think of each `vote−−` as **deleting a pair of two different values**.

```text
1. Every cancellation removes one candidate copy and one non-candidate element —
   two DIFFERENT values.
2. Removing two different values from an array never destroys its majority:
       majority count m > n/2   →   after the pair:  (m − 1) > (n − 2)/2   still holds
       (and if neither was the majority, m stays and n shrinks — even better)
3. When the scan ends, what remains is `vote` copies of `candidate` with nothing to
   cancel them — the majority of the remaining elements is the candidate.
4. Since the original majority survives every cancellation, it must be that candidate.  ∎
```

> 🔑 The key phrase for interviews: **"each cancellation removes two different elements,
> which preserves the majority."**

### ⚠️ The final `vote` is not the count

```text
[8, 3, 4, 8, 8]   final vote = 1, but 8 appears 3 times
```

`vote` is what's left **after** cancellations — it's only a lower bound. To report the
count, do a second pass.

---

## 7. ⚠️ Verification — When a Majority Isn't Guaranteed

Boyer–Moore **always** returns some candidate, even when there's no majority:

```text
[1, 2, 3]
i=0  cand 1, vote 1
i=1  rival → vote 0
i=2  cand 3, vote 1        → returns 3, but 3 appears once — NOT a majority ❌
```

The repo version documents the assumption: *"Assumes a majority element exists."* Without
that guarantee, add a **second pass**:

```cpp
int count = 0;
for (int i = 0; i < size; i++)
    if (arr[i] == candidate) count++;

return count > size / 2 ? candidate : -1;      // −1 (or a flag) = no majority
```

Still `O(n)` time — two passes — and `O(1)` space.

> 💡 Always ask the interviewer: *"Is a majority guaranteed to exist?"* The answer decides
> whether phase 2 is needed. Saying "I'd verify unless it's guaranteed" shows you
> understand the algorithm's limit.

| Version | Passes | Correct when no majority? |
|---|---|---|
| Phase 1 only (implemented) | 1 | ❌ returns an arbitrary candidate |
| Phase 1 + verification | 2 | ✅ |

---

## 8. ⏱️ Complexity

```text
Time    O(n)    one pass (two with verification)
Space   O(1)    candidate + vote
```

---

## 9. 🔀 Variants

### Elements appearing more than `n/3` times

At most **two** such elements can exist (three would need `> n` slots). Generalise: keep
**two** candidates and two votes; a third, different value cancels one vote from **each**.

```cpp
int c1 = 0, c2 = 0, v1 = 0, v2 = 0;
for (int i = 0; i < size; i++) {
    int x = arr[i];
    if      (x == c1)  v1++;
    else if (x == c2)  v2++;
    else if (v1 == 0)  { c1 = x; v1 = 1; }
    else if (v2 == 0)  { c2 = x; v2 = 1; }
    else               { v1--; v2--; }         // x cancels one of each
}
// then VERIFY both c1 and c2 with a counting pass — mandatory here
```

> The general rule: elements appearing more than `n/k` times → at most `k − 1` candidates.

### Others

| Follow-up | Approach |
|---|---|
| Return the **index** of the majority | Find the candidate, then any index where it appears |
| Majority in a **sorted** array | Check `arr[n/2]`; verify with first/last occurrence by binary search → `O(log n)` |
| Majority in a **stream** | Boyer–Moore works as-is — it only ever looks at the current element |
| Most **frequent** element (not necessarily `> n/2`) | Boyer–Moore doesn't apply — use a hash map |

---

## 10. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Skipping verification when a majority isn't guaranteed | `[1, 2, 3]` returns `3` |
| Using `>= n/2` instead of `> n/2` | `[3, 3, 4, 4]` → wrongly reports a majority |
| Treating the final `vote` as the frequency | It's only what survived cancellation |
| Changing the candidate **after** processing `arr[i]` | Order matters: check `vote == 0` first, then vote |
| Using Boyer–Moore for "most frequent" | It finds a **majority**, nothing weaker |
| Forgetting that `n/3` needs **two** candidates | One candidate can't track two possible answers |

---

## 11. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Majority element?** | Appears **more than** `n/2` times; at most one exists. |
| **Boyer–Moore idea?** | Cancel pairs of different elements; the majority survives. |
| **The three rules?** | `vote == 0` → new candidate. Same → `vote++`. Different → `vote−−`. |
| **Why does it work?** | Removing two different elements never destroys a majority. |
| **Complexity?** | `O(n)` time, `O(1)` space. |
| **When do you need a second pass?** | When a majority isn't guaranteed — count the candidate. |
| **Is the final vote the count?** | No — only what survived cancellation. |
| **Sorting approach?** | The majority must sit at index `n/2`. `O(n log n)`. |
| **`> n/3` version?** | Two candidates, two votes; verify both. |
| **General `> n/k`?** | At most `k − 1` candidates. |

---

## 12. The Mental Model

```text
           every element votes for itself
                         │
        two DIFFERENT votes meet → both cancel
                         │
      ┌──────────────────┼───────────────────┐
      ↓                  ↓                   ↓
  vote == 0         same as cand        different
  new candidate       vote++              vote−−
                         │
                         ↓
           survivor = the only possible majority
                         │
            majority guaranteed?  ── yes ──→ return it
                         │
                         no
                         ↓
          count it (2nd pass): > n/2 ?  →  answer / none
```
