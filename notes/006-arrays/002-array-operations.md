# 002 — Basic Array Operations & Their Complexities

> **One-line takeaway:** **Reading** an array is cheap; **changing its shape** is expensive. Any insert or delete that isn't at the end has to **shift** every element after it to keep the array contiguous — and that shift is where `O(n)` comes from.

📄 [`InsertElement.java`](../../programs/java/arrays/basic/InsertElement.java) ·
📄 [`DeleteElement.java`](../../programs/java/arrays/basic/DeleteElement.java) ·
📄 [`SearchInteger.java`](../../programs/java/arrays/basic/SearchInteger.java)

---

## 1. The Complexity Table — With the *Why*

| Operation | Time | Why |
|---|---|---|
| Access / update `arr[i]` | `O(1)` | Address is computed ([note 001 §3](001-introduction-to-arrays.md)) |
| Search — unsorted | `O(n)` | No order to exploit; may check every element |
| Search — sorted | `O(log n)` | Binary search discards half each step |
| Insert at **end** (space available) | `O(1)` | Nothing after it to move |
| Insert at index `i` | `O(n)` | Shift `n − i` elements right |
| Insert at **start** | `O(n)` | Worst case — shift **all** `n` |
| Delete at **end** | `O(1)` | Just shrink the logical size |
| Delete at index `i` | `O(n)` | Shift `n − i − 1` elements left |
| Traverse | `O(n)` | Visit each element once |

> 🔑 **The rule behind the table:** cost = **number of elements that must move**.
> Average position is the middle → `~n/2` moves → `O(n)`.

All of these are `O(1)` extra space — the work happens in place.

---

## 2. Insert — Make a Gap, Then Fill It

```text
insert 98 at index 2       n = 5, capacity = 6

before   [ 10  20  30  40  50  _ ]
                  ←── shift right, starting from the END
after    [ 10  20  98  30  40  50 ]
```

```java
// arr has capacity > n; returns new size
static int insert(int[] arr, int n, int pos, int x) {
    for (int i = n; i > pos; i--)      // walk from the end towards pos
        arr[i] = arr[i - 1];
    arr[pos] = x;
    return n + 1;
}
```

> ⚠️ **Why shift from the end?** Shifting from the front overwrites `arr[pos+1]` before
> you've moved it — you'd copy the same value across the whole tail.

---

## 3. Delete — Close the Gap

```text
delete index 1

before   [ 10  20  30  40  50 ]
               shift left ──→ starting from the DELETED spot
after    [ 10  30  40  50  _ ]      logical size 5 → 4
```

```java
static int delete(int[] arr, int n, int pos) {
    for (int i = pos; i < n - 1; i++)  // n - 1, NOT n — reads arr[i + 1]
        arr[i] = arr[i + 1];
    return n - 1;
}
```

Direction flips for delete: shift **left**, starting at the gap. Opposite of insert.

> ⚠️ The loop bound is the classic bug. With `i < n`, the last iteration reads `arr[n]` —
> one past the end → `ArrayIndexOutOfBoundsException` in Java, silent garbage in C++.

---

## 4. Tricks When **Order Doesn't Matter**

The `O(n)` cost exists only to **preserve order**. Drop that requirement and both
operations become `O(1)`:

```text
delete index i  →  arr[i] = arr[n-1]; n--        (swap-with-last, "swap and pop")
insert anywhere →  arr[n++] = x                   (just append)
```

> 💡 Interview-worthy observation: always ask *"does the order need to be preserved?"*
> Many problems that look `O(n)` per operation are `O(1)` once the answer is "no".

---

## 5. Search — It All Depends on Sortedness

```text
unsorted   →  linear scan        O(n)        works always
sorted     →  binary search      O(log n)    requires sorted input
```

Keeping an array sorted makes search fast **but** makes insertion slower — you must find
the right slot *and* shift to make room. Classic read-vs-write trade-off.

| Keep array… | Search | Insert |
|---|---|---|
| Unsorted | `O(n)` | `O(1)` (append) |
| Sorted | `O(log n)` | `O(n)` (shift) |

---

## 6. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Shifting right **from the front** on insert | Overwrites values before they're moved |
| Delete loop `i < n` instead of `i < n − 1` | Reads one past the end |
| Confusing **size** with **capacity** | Inserting into a full fixed array silently drops the last element |
| Saying "insert is `O(1)`" without qualifying | Only at the **end**, and only if there's room |
| Binary search on unsorted data | Wrong answers, no error |

---

## 7. Quick Recall

| Question | Answer |
|---|---|
| **Access / update?** | `O(1)` |
| **Search unsorted / sorted?** | `O(n)` / `O(log n)` |
| **Insert / delete in the middle?** | `O(n)` — elements after the position must shift. |
| **Insert / delete at the end?** | `O(1)` (insert needs spare capacity). |
| **Worst position to insert?** | Index 0 — every element moves. |
| **Which direction to shift on insert?** | Right, iterating **from the end** backwards. |
| **On delete?** | Left, iterating **from the gap** forwards, up to `n − 2`. |
| **How to delete in `O(1)`?** | Overwrite with the last element — only if order doesn't matter. |
| **What's the real cost driver?** | Number of elements that move to preserve order and contiguity. |

---

## 8. The Mental Model

```text
                     Array operation
                           │
            ┌──────────────┴──────────────┐
            ↓                             ↓
     READ (access, update)         CHANGE SHAPE (insert, delete)
            │                             │
          O(1)                   at the end?  ── yes ──→ O(1)
                                          │
                                          no
                                          │
                               order must be preserved?
                                  ╱               ╲
                                yes                no
                                 │                  │
                          shift → O(n)       swap/append → O(1)
```
