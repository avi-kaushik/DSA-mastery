# 002 — Basic Array Operations & Their Complexities

> **One-line takeaway:** **Reading** an array is cheap; **changing its shape** is expensive. Any insert or delete that isn't at the end has to **shift** every element after it to keep the array contiguous — and that shift is where `O(n)` comes from.

📄 [`basic_operations.cpp`](../../programs/cpp/data_structures/arrays/basic/basic_operations.cpp) ·
📄 [`InsertElement.java`](../../programs/java/arrays/basic/InsertElement.java) ·
📄 [`DeleteElement.java`](../../programs/java/arrays/basic/DeleteElement.java) ·
📄 [`SearchInteger.java`](../../programs/java/arrays/basic/SearchInteger.java)

---

## 1. The Model: Capacity vs Size

A fixed array can't grow, so insert/delete only make sense if you separate **how much room
there is** from **how much is used**:

```text
capacity = 9      (slots allocated — never changes)
size     = 4      (slots in use — changes on insert / delete)

[ 4  9  14  18 | _  _  _  _  _ ]
  └── valid ──┘  └── free ───┘
  0 … size−1
```

`basic_operations.cpp` follows exactly this model:

```cpp
constexpr int array_capacity = 9;
int arr[array_capacity] = {};
int size = 0;

size = insert(arr, 4, 0, size, array_capacity);   // functions RETURN the new size
size = remove(arr, 12, size, array_capacity);
```

> 🔑 Every function that changes the shape **returns the new size**, and the caller must
> store it. Forget to, and the array and its size disagree — the source of most bugs here.

This is also, in miniature, how `std::vector` and `ArrayList` work inside
([note 003](003-static-vs-dynamic-arrays.md)).

---

## 2. The Complexity Table — With the *Why*

| Operation | Best | Worst | Why |
|---|---|---|---|
| Access / update `arr[i]` | `O(1)` | `O(1)` | Address is computed ([note 001 §5](001-introduction-to-arrays.md)) |
| Traverse | `O(n)` | `O(n)` | Visit each element once |
| Search — unsorted | `O(1)` (first slot) | `O(n)` | No order to exploit |
| Search — sorted | `O(1)` (middle) | `O(log n)` | Binary search halves the range |
| Insert at **end** (room left) | `O(1)` | `O(1)` | Nothing after it to move |
| Insert at index `i` | `O(1)` (at end) | `O(n)` (at 0) | Shift `size − i` elements right |
| Delete at **end** | `O(1)` | `O(1)` | Just shrink `size` |
| Delete at index `i` | `O(1)` (at end) | `O(n)` (at 0) | Shift `size − i − 1` elements left |
| Delete **by value** | `O(n)` | `O(n)` | Find it (`i` steps) + shift (`n − i` steps) — always `~n` total |

> 🔑 **The rule behind the table:** cost = **number of elements that must move**.

All of these use `O(1)` extra space — the work happens in place.

### Why "average" is still `O(n)`

If every insert position `0 … n` is equally likely, the shifts are `n, n−1, …, 1, 0`:

```text
average shifts = (n + (n−1) + … + 0) / (n + 1) = n / 2    →  O(n)
```

Half of `n` is still linear. Only inserting at the **end** escapes the cost.

> 💡 **Delete by value is `O(n)` even in the best case.** If the value is at index 0, the
> search is instant — but then *every* element shifts. If it's at the end, the shift is
> free — but the search took `n` steps. Search + shift always add up to about `n`.

---

## 3. Access, Update & Traverse

```cpp
int x = arr[i];             // read     O(1)
arr[i] = 42;                // update   O(1)  — same address formula, just a write

for (int i = 0; i < size; i++)     // traverse   O(n)
    cout << arr[i] << " ";
```

Nothing to shift, nothing to search. Note the loop bound is **`size`**, not `capacity` —
slots beyond `size` hold leftovers, not data.

---

## 4. Search — Linear Scan

```cpp
int search(const int arr[], int value, int size)
{
    for (int i = 0; i < size; i++)
        if (arr[i] == value)
            return i;          // first match wins
    return -1;                 // conventional "not found"
}
```

```text
arr = [ 4  9  14 ],  value = 9   →  checks 4, 9  →  returns 1
arr = [ 4  9  14 ],  value = 7   →  checks all 3 →  returns −1
```

| Case | Comparisons |
|---|---|
| Best (at index 0) | 1 |
| Worst (last or absent) | `n` |
| Average (present, uniform) | `~n/2` |

If the array is **sorted**, binary search does it in `O(log n)` — see
[Recursion on Arrays §5](../005-recursion/009-recursion-on-arrays.md). Keeping an array
sorted, though, makes inserts harder (§7).

> `const int arr[]` — the function promises not to modify the array. Use it for every
> read-only function; the compiler then catches accidental writes.

---

## 5. Insert — Make a Gap, Then Fill It

```text
insert 98 at index 2        size = 5, capacity = 6

before   [ 10  20  30  40  50  _ ]
                   ←── shift right, starting from the END
after    [ 10  20  98  30  40  50 ]        size = 6
```

```cpp
int insert(int arr[], int value, int position, int size, int capacity)
{
    if (size == capacity)                 throw runtime_error("Array is full.");
    if (position < 0 || position > size)  throw out_of_range("Invalid insertion position.");

    for (int i = size; i > position; i--)  // walk from the end towards position
        arr[i] = arr[i - 1];

    arr[position] = value;
    return size + 1;
}
```

### 🔍 Trace — insert `98` at `2` into `[10 20 30 40 50]`

```text
i = 5   arr[5] = arr[4]   →  [10 20 30 40 50 50]
i = 4   arr[4] = arr[3]   →  [10 20 30 40 40 50]
i = 3   arr[3] = arr[2]   →  [10 20 30 30 40 50]
stop (i == 2)
arr[2] = 98               →  [10 20 98 30 40 50]       3 shifts = size − position
```

### Why shift from the end?

```text
shifting from the FRONT (wrong):
i = 3   arr[3] = arr[2]   →  [10 20 30 30 50 _]    ← 40 is gone forever
i = 4   arr[4] = arr[3]   →  [10 20 30 30 30 _]    ← copying the same 30 everywhere
```

> ⚠️ Moving right means each element's destination is the slot **after** it. Start from the
> back so you always write into a slot you've already emptied.

### Valid positions are `0 … size` (inclusive)

```text
position = 0       insert at the front   → shift everything  O(n)
position = size    insert at the end     → shift nothing     O(1)   ← this is "append"
position > size    would leave a gap      → invalid
```

---

## 6. Delete — Close the Gap

```text
delete value 20 (found at index 1)

before   [ 10  20  30  40  50 ]
                shift left ──→  starting from the DELETED spot
after    [ 10  30  40  50 | 50 ]       size = 4  (the trailing 50 is now outside size — ignored)
```

```cpp
int remove(int arr[], int value, int size, int capacity)
{
    int position = 0;
    while (position < size && arr[position] != value)   // 1. find it
        position++;

    if (position == size) return size;                  // not found → unchanged

    for (int i = position; i < size - 1; i++)           // 2. shift left
        arr[i] = arr[i + 1];

    return size - 1;
}
```

Direction flips for delete: shift **left**, iterating **forwards** from the gap — each
element moves into the slot just vacated before it. Exactly the mirror of insert.

> ⚠️ **The loop bound is the classic bug.** The body reads `arr[i + 1]`, so the last valid
> `i` is `size − 2` → condition `i < size − 1`. With `i < size`, the last iteration reads
> `arr[size]` — one past the end. (This is the bug in
> [`DeleteElement.java`](../../programs/java/arrays/basic/DeleteElement.java), which loops
> to `arr.length`.)

> 💡 You don't need to clear the old last slot. Once `size` shrinks, it's outside the valid
> range. Java's `DeleteElement` sets it to `0` only because it has no separate `size`.

---

## 7. Tricks When **Order Doesn't Matter**

The `O(n)` cost exists only to **preserve order**. Drop that requirement and both operations
become `O(1)`:

```text
delete index i  →  arr[i] = arr[size − 1];  size--     ("swap and pop")
insert anywhere →  arr[size++] = x                     (just append)
```

```text
delete index 1 from [ 10 20 30 40 50 ]   →   [ 10 50 30 40 ]      1 write instead of 3
```

> 💡 Interview-worthy question to always ask: *"Does the order need to be preserved?"*
> Many problems that look `O(n)` per operation are `O(1)` once the answer is "no".

### The read-vs-write trade-off of keeping it sorted

| Keep the array… | Search | Insert | Delete |
|---|---|---|---|
| Unsorted | `O(n)` | `O(1)` (append) | `O(n)` to find, then `O(1)` swap-and-pop |
| Sorted | `O(log n)` | `O(n)` (slot found fast, still shift) | `O(n)` (slot found fast, still shift) |

Sorting buys fast **reads** with slow **writes**. Read-heavy data → keep it sorted.
Write-heavy data → don't.

---

## 8. Edge Cases Interviewers Probe

| Case | What should happen | How `basic_operations.cpp` handles it |
|---|---|---|
| Insert into a full array | Reject | `throw runtime_error("Array is full.")` |
| Insert at `position > size` | Reject — it would leave a hole | `throw out_of_range(...)` |
| Insert at `position == size` | Allowed — an append | Loop runs zero times |
| Delete a value that's absent | Size unchanged | `return size` |
| Delete from an empty array | Reject or no-op | `throw` when `size <= 0` |
| Duplicate values | Only the **first** is removed | `while` stops at the first match |

---

## 9. Library Equivalents — Same Costs Underneath

| Operation | C++ `std::vector` | Java `ArrayList` | Cost |
|---|---|---|---|
| Access | `v[i]` / `v.at(i)` (checked) | `list.get(i)` | `O(1)` |
| Append | `v.push_back(x)` | `list.add(x)` | `O(1)` amortized |
| Insert at `i` | `v.insert(v.begin() + i, x)` | `list.add(i, x)` | `O(n)` — shifts |
| Delete at `i` | `v.erase(v.begin() + i)` | `list.remove(i)` | `O(n)` — shifts |
| Search | `std::find(v.begin(), v.end(), x)` | `list.indexOf(x)` | `O(n)` |

> The library doesn't make middle insertion cheaper — `ArrayList.add(i, x)` is a
> `System.arraycopy` shift, `vector::insert` is a `memmove`-style shift. Fast constant
> factor, same `O(n)`.

---

## 10. Common Mistakes

| ✗ Mistake | Result |
|---|---|
| Shifting right **from the front** on insert | Overwrites values before they're moved |
| Delete loop `i < size` instead of `i < size − 1` | Reads one past the end |
| Not storing the returned size | Array contents and `size` disagree |
| Looping to `capacity` instead of `size` | Prints / processes leftover junk |
| Saying "insert is `O(1)`" without qualifying | Only at the **end**, and only if there's room |
| Binary search on unsorted data | Wrong answers, no error |

---

## 11. Quick Recall — Interview One-Liners

| Question | Answer |
|---|---|
| **Capacity vs size?** | Slots allocated vs slots in use. Functions return the new size. |
| **Access / update?** | `O(1)` |
| **Search unsorted / sorted?** | `O(n)` / `O(log n)` |
| **Insert / delete in the middle?** | `O(n)` — elements after the position must shift. |
| **Insert / delete at the end?** | `O(1)` (insert needs spare capacity). |
| **Average insert cost?** | `n/2` shifts → still `O(n)`. |
| **Which direction on insert?** | Shift right, iterating **from the end** backwards. |
| **On delete?** | Shift left, iterating **from the gap** forwards, while `i < size − 1`. |
| **Valid insert positions?** | `0 … size` inclusive; `size` means append. |
| **Why is delete-by-value always `O(n)`?** | Search steps + shift steps ≈ `n` wherever the value is. |
| **How to delete in `O(1)`?** | Overwrite with the last element — only if order doesn't matter. |
| **Real cost driver?** | Number of elements that move to preserve order and contiguity. |

---

## 12. The Mental Model

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
                       (insert: from end,
                        delete: from gap)
```
