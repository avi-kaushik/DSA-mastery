# 015 — Choosing the Right Array Technique

> **One-line takeaway:** Array problems rarely say which technique they want — but their **wording** does. "Contiguous", "sorted", "for each element vs everything to its left", "many range queries", "more than half"… each phrase points at one idea from this section. Learn the signals, and the right tool picks itself.

---

## 1. Why This Note Exists

The previous notes each taught one idea. In an interview, the hard part is the moment
**before** coding: *which idea is this?* This note is that moment, made explicit — a map from
what the problem **says** to what you should **reach for**.

> 🔑 Before writing code, translate the problem into one of these questions:
> *What do I need to remember? Can I discard something safely? Is it contiguous? Is it about
> a side, a range, or the whole array?*

---

## 2. Signals → Technique

| The problem says… | Reach for | Note |
|---|---|---|
| "largest / smallest / count / check whether…" | carry a small state in one pass | [004](004-one-pass-thinking.md) |
| "sorted" + "pair / triplet with sum…" | two pointers, opposite ends | [005](005-two-pointers.md) |
| "in place", "O(1) extra space", "remove / move / compact" | two pointers, read/write | [005](005-two-pointers.md) |
| "rotate", "shift by k", "circular", "wraps around" | ring view, `% n`, reversal trick | [006](006-circular-thinking-and-rotation.md) |
| "for each element, compared with everything to its left/right" | prefix / suffix extremes | [007](007-prefix-suffix-extremes.md) |
| "depends on the tallest on both sides" | prefix + suffix arrays (or two pointers) | [007](007-prefix-suffix-extremes.md) |
| "consecutive", "streak", "groups", "frequency in a sorted array" | runs & boundaries | [008](008-runs-and-boundaries.md) |
| "maximum number of…", "minimum number of…" with a natural order | greedy — **after** an exchange argument | [009](009-greedy-algorithms.md) |
| "maximum sum / product of a contiguous subarray" | Kadane ("best ending here") | [010](010-kadanes-algorithm.md) |
| "appears more than n/2 (n/k) times", "O(1) space" | Boyer–Moore cancellation | [011](011-boyer-moore-voting.md) |
| "k consecutive", "longest/shortest subarray such that…", "at most k…" | sliding window (if monotonic) | [012](012-sliding-window.md) |
| "sum from l to r", many queries · "left sum = right sum" | prefix sums | [013](013-prefix-sums.md) |
| "number of subarrays with sum k" (negatives possible) | prefix sums + hash map | [013](013-prefix-sums.md) |
| "add v to every element in [L, R]", many times · "most overlapping intervals" | difference array / sweep line | [014](014-difference-arrays.md) |

---

## 3. The Decision Tree

```text
                              Array problem
                                    │
              Is it about a CONTIGUOUS subarray / range?
                 ╱                                      ╲
               yes                                       no
                │                                         │
   ┌────────────┼──────────────┐              ┌───────────┼─────────────┐
   ↓            ↓              ↓              ↓           ↓             ↓
fixed size k  "best sum"    "sum equals k"  pairs in a   each element  "more than
or "longest   (max/min)     or many range   SORTED array vs one side   n/2 times"
with ≤ k …"       │         queries              │           │             │
   │           Kadane           │           two pointers  prefix /     Boyer–Moore
sliding window              prefix sums      (opposite)   suffix         (+ verify)
(needs non-                 (+ hash map if                extremes
 negatives)                  negatives)
                                                     
   many range UPDATES then reads → difference array          
   in-place remove / compact     → read/write pointers        
   rotation / wrap-around        → % n, reversal, complement  
   "consecutive equal / streak"  → runs & boundaries          
   "min/max number of …"         → greedy? find a counter-example first
```

---

## 4. Look-Alikes — and How to Tell Them Apart

These pairs get confused constantly. The distinguishing question is in the middle.

| Looks like… | Ask | Then it's… |
|---|---|---|
| Max subarray sum **vs** max sum of `k` consecutive | Is the length fixed? | Fixed → sliding window. Free → Kadane. |
| Subarray with sum `S` (window) **vs** (prefix + hash) | Can values be negative? | No → sliding window. Yes → prefix + hash. |
| Max difference **vs** stock with unlimited trades | One trade or many? | One → prefix min. Many → greedy (sum of rises). |
| Leaders **vs** next greater element | One max for the whole right side, or the *nearest* greater per element? | Whole side → suffix max. Nearest → stack. |
| Majority element **vs** most frequent element | Is it guaranteed `> n/2`? | Yes → Boyer–Moore. No → hash map. |
| Remove duplicates (sorted) **vs** (unsorted) | Are equal values adjacent? | Sorted → neighbours / runs. Unsorted → hash set. |
| Trapping rain water **vs** container with most water | Does every bar hold water, or only two lines matter? | Every bar → prefix/suffix max. Two lines → two pointers on area. |
| Range sum queries **vs** range add updates | Many reads, or many writes? | Reads → prefix sums. Writes → difference array. |

---

## 5. Complexity Signatures

When you know the target complexity, you can often guess the technique:

| Target | What usually achieves it |
|---|---|
| `O(n)` time, `O(1)` space | one pass with state · two pointers · sliding window · Kadane · Boyer–Moore |
| `O(n)` time, `O(n)` space | prefix/suffix arrays · prefix + hash map · difference array |
| `O(n log n)` | sort first, then a linear technique (greedy, two pointers on sorted data, sweep line) |
| `O(n + q)` for `q` queries | prefix sums (read-heavy) · difference array (write-heavy) |
| `O(log n)` per query **and** update | Fenwick / segment tree — beyond plain arrays |

> 💡 If an interviewer says *"can you do it in `O(1)` space?"* after your `O(n)`-space solution,
> they're usually hinting at **two pointers** (rain water) or at **replacing a stored array
> with a running variable** (equilibrium point, leaders).

---

## 6. Common Upgrade Paths

Interviews often move along these arrows. Knowing the next step is half the answer.

```text
nested loops over pairs                →  two pointers (sorted) or hashing (unsorted)
recompute every window                 →  sliding window
re-sum every range                     →  prefix sums
paint every range                      →  difference array
prefix + suffix ARRAYS                 →  two pointers or a single running variable (O(1) space)
rotate by one, k times                 →  reversal trick
"try every subarray"                   →  Kadane ("best ending here")
hash map to find the majority          →  Boyer–Moore (O(1) space)
```

---

## 7. Every Array Program in This Repo, by Concept

| Concept | Programs | 📄 |
|---|---|---|
| One-pass state | largest, second largest, is sorted, search | [`largest_elements.cpp`](../../programs/cpp/data_structures/arrays/basic/largest_elements.cpp) · [`basic_operations.cpp`](../../programs/cpp/data_structures/arrays/basic/basic_operations.cpp) |
| Array operations | insert, delete, search, reverse | [`basic_operations.cpp`](../../programs/cpp/data_structures/arrays/basic/basic_operations.cpp) |
| Two pointers | reverse, remove value, remove duplicates, at most `k`, move zeros | [`basic_operations.h`](../../programs/cpp/include/arrays/basic_operations.h) · [`remove_elements.cpp`](../../programs/cpp/data_structures/arrays/basic/remove_elements.cpp) · [`move_zeros.cpp`](../../programs/cpp/data_structures/arrays/basic/move_zeros.cpp) |
| Circular thinking | left rotate by one / by `k` / reversal | [`left_rotate.cpp`](../../programs/cpp/data_structures/arrays/basic/left_rotate.cpp) |
| Prefix / suffix extremes | leaders, maximum difference, trapping rain water | [`leaders.cpp`](../../programs/cpp/data_structures/arrays/basic/leaders.cpp) · [`maximum_difference.cpp`](../../programs/cpp/data_structures/arrays/basic/maximum_difference.cpp) · [`trapping_rain_water.cpp`](../../programs/cpp/data_structures/arrays/intermediate/trapping_rain_water.cpp) |
| Runs & boundaries | frequencies, max consecutive ones, even-odd subarray, group flips | [`frequencies.cpp`](../../programs/cpp/data_structures/arrays/basic/frequencies.cpp) · [`maximum_consecutive_ones.cpp`](../../programs/cpp/data_structures/arrays/basic/maximum_consecutive_ones.cpp) · [`sub_array.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sub_array.cpp) · [`flips.cpp`](../../programs/cpp/data_structures/arrays/intermediate/flips.cpp) |
| Greedy | stock buy & sell (unlimited) | [`stocks.cpp`](../../programs/cpp/data_structures/arrays/intermediate/stocks.cpp) |
| Kadane | max subarray sum, max circular subarray sum | [`sub_array.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sub_array.cpp) |
| Boyer–Moore | majority element | [`majority.cpp`](../../programs/cpp/data_structures/arrays/intermediate/majority.cpp) |
| Sliding window | max sum of `k` consecutive, subarray with given sum | [`sliding_window.cpp`](../../programs/cpp/data_structures/arrays/intermediate/sliding_window.cpp) |
| Prefix sums | range sum queries, equilibrium point | [`prefix_sum.cpp`](../../programs/cpp/data_structures/arrays/intermediate/prefix_sum.cpp) · [`equilibrium.cpp`](../../programs/cpp/data_structures/arrays/intermediate/equilibrium.cpp) |
| Difference arrays | maximum appearing element in ranges | [`maximum_appearing_element.cpp`](../../programs/cpp/data_structures/arrays/intermediate/maximum_appearing_element.cpp) |

---

## 8. Quick Recall — Interview One-Liners

| Signal | Technique |
|---|---|
| "contiguous", fixed length `k` | sliding window (fixed) |
| "contiguous", longest/shortest with a condition | sliding window (variable) — if monotonic |
| "contiguous", best sum | Kadane |
| "contiguous", sum exactly `k`, negatives | prefix sums + hash map |
| sorted + pair | two pointers |
| in place + remove/compact | read/write pointers |
| each element vs one side | prefix/suffix extreme |
| both sides | prefix + suffix arrays → two pointers for `O(1)` space |
| more than half | Boyer–Moore + verify |
| many range reads | prefix sums |
| many range writes | difference array |
| rotate / circular | `% n`, reversal, complement |
| "min/max number of…" | greedy — only with an exchange argument |

---

## 9. The Mental Model

```text
                     read the problem's WORDS
                               │
   ┌───────────┬───────────┬───┴──────┬────────────┬─────────────┐
   ↓           ↓           ↓          ↓            ↓             ↓
 "contiguous" "sorted"   "each vs   "range"     "more than    "min/max
   │          "in place"  a side"    queries/    half"         number of"
   │             │          │        updates        │             │
 window ·    two         prefix /   prefix sums · Boyer–Moore   greedy?
 Kadane ·    pointers    suffix     difference                 (prove it)
 prefix+hash             extremes   arrays
                               │
            then ask: what do I REMEMBER, and what can I safely DISCARD?
```
