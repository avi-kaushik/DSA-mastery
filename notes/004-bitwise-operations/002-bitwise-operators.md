# 002 — Bitwise Operators

> **One-line takeaway:** Six operators do everything: `&` keeps, `|` sets, `^` toggles, `~` flips, `<<` multiplies by 2, `>>` divides by 2. Java adds `>>>` because it has no unsigned types.

---

## 1. The Operators at a Glance

| Operator | Name | Effect | Mnemonic |
|---|---|---|---|
| `&` | AND | 1 only if **both** are 1 | **masks / keeps** bits |
| `\|` | OR | 1 if **either** is 1 | **sets** bits |
| `^` | XOR | 1 if they **differ** | **toggles** bits |
| `~` | NOT | flips every bit | **inverts** |
| `<<` | Left shift | shift left, fill with 0 | **× 2ⁿ** |
| `>>` | Signed right shift | shift right, copy sign bit | **÷ 2ⁿ (floor)** |
| `>>>` | Unsigned right shift | shift right, fill with 0 | **Java only** |

---

## 2. AND — `&`

```text
a  b  a&b
0  0   0
0  1   0
1  0   0
1  1   1        ← only when BOTH are 1
```

```text
  12  =  1100
   5  =  0101
       & ─────
   4  =  0100
```

**What it's for: masking — keeping only the bits you care about.**

```java
x & 1              // last bit → 1 if odd, 0 if even
x & 0xFF           // keep the lowest 8 bits, discard the rest
x & (1 << i)       // test whether bit i is set
ip & subnetMask    // extract the network part of an IP address
```

```text
Identity:  x & 0 = 0        x & x = x
           x & −1 = x       (−1 is all 1s)
```

---

## 3. OR — `|`

```text
a  b  a|b
0  0   0
0  1   1
1  0   1
1  1   1        ← 1 if EITHER is 1
```

```text
  12  =  1100
   5  =  0101
       | ─────
  13  =  1101
```

**What it's for: turning bits on, and combining flags.**

```java
x | (1 << i)                    // set bit i (leaves the others alone)
READ | WRITE                    // combine permission flags
```

```text
Identity:  x | 0 = x        x | x = x       x | −1 = −1
```

---

## 4. XOR — `^` (the interesting one)

```text
a  b  a^b
0  0   0
0  1   1
1  0   1
1  1   0        ← 1 when they DIFFER
```

```text
  12  =  1100
   5  =  0101
       ^ ─────
   9  =  1001
```

**What it's for: toggling, and cancelling pairs.**

### 🔑 The properties that make XOR special

```text
x ^ x = 0            a value cancels itself         ← the key one
x ^ 0 = x            identity
x ^ 1 = flip last bit
x ^ y = y ^ x        commutative
(x ^ y) ^ z = x ^ (y ^ z)   associative
if x ^ y = z  then  x ^ z = y  and  y ^ z = x       reversible
```

Because it's commutative, associative, and self-cancelling, **XOR-ing a list makes every
duplicated value vanish regardless of order**:

```text
4 ^ 1 ^ 2 ^ 1 ^ 2  =  4 ^ (1^1) ^ (2^2)  =  4 ^ 0 ^ 0  =  4
```

```java
x ^ (1 << i)        // toggle bit i
a ^= b; b ^= a; a ^= b;    // swap without a temp (⚠️ fails if a and b alias)
```

> XOR is also its own inverse, which is why it's the core of simple ciphers:
> `(plain ^ key) ^ key = plain`.

---

## 5. NOT — `~`

Flips every bit, including the sign bit.

```text
   5  =  0000 0000 0000 0000 0000 0000 0000 0101
  ~5  =  1111 1111 1111 1111 1111 1111 1111 1010  =  −6
```

```text
~x = −x − 1
```

```text
~0  = −1        ~(−1) = 0        ~5 = −6
```

⚠️ Unary `~` is **not** the same as logical `!`. It has no boolean meaning — `~` on a
`boolean` is a compile error in Java.

```java
x & ~(1 << i)      // clear bit i — the standard use of ~
x & ~mask          // set difference: everything in x that isn't in mask
```

---

## 6. Left Shift — `<<`

Shifts bits left, filling with zeros on the right.

```text
   5  =  0000 0101
5 << 1 =  0000 1010  = 10       ← × 2
5 << 2 =  0001 0100  = 20       ← × 4
```

```text
x << n  =  x × 2ⁿ
```

```java
1 << i        // a mask with only bit i set — the building block of everything
1 << n        // 2ⁿ, e.g. the number of subsets of an n-element set
```

⚠️ **Overflow:** shifting left discards the high bits. `1 << 31` is `Integer.MIN_VALUE`
(negative), and `1 << 32` is **not** 0 — see §8. Use `1L << i` when `i ≥ 31`.

---

## 7. Right Shifts — `>>` and `>>>`

### `>>` — signed (arithmetic) shift

Copies the **sign bit** into the vacated positions, preserving the sign.

```text
  20  =  0001 0100
20 >> 1 = 0000 1010  =  10       ✓
20 >> 2 = 0000 0101  =   5

 −20 >> 1  =  −10                ✓ stays negative
```

```text
x >> n  =  ⌊x / 2ⁿ⌋      ← FLOOR division
```

### `>>>` — unsigned (logical) shift

Always fills with **0**, so the result is never negative.

```text
 −20 >> 1   =  −10                    (sign preserved)
 −20 >>> 1  =  2147483638             (treated as unsigned)
```

```text
For x ≥ 0:   x >> n  ==  x >>> n      identical
For x < 0:   completely different
```

> **Why Java has `>>>`:** it lacks unsigned integer types, so it needs an explicit operator
> for logical shifting. C and C++ have no `>>>` — there, `>>` on an unsigned type already
> fills with zeros.

Typical use — a safe midpoint, and hash mixing:

```java
int mid = (lo + hi) >>> 1;      // correct even if lo + hi overflows
hash ^ (hash >>> 16);           // HashMap's bit-spreading step
```

---

## 8. ⚠️ Pitfalls

### Precedence — the number one bug

```text
Java/C precedence:   ==  !=   bind TIGHTER than   &  ^  |
```

```java
if (x & 1 == 0)      // parses as x & (1 == 0)   ✗ compile error in Java, silent bug in C
if ((x & 1) == 0)    // ✓ always parenthesise
```

### Shift distance is masked

```java
x << 32   // for int, uses 32 & 31 = 0  →  returns x UNCHANGED, not 0
x << 33   // same as x << 1
```

```text
int  → shift count uses the low 5 bits (0–31)
long → shift count uses the low 6 bits (0–63)
```

### `int` vs `long` literals

```java
1 << 40      // ✗ int literal → 1 << (40 & 31) = 1 << 8 = 256
1L << 40     // ✓ long shift
```

### Negative shift semantics

```text
−5 >> 1  = −3      floor(−2.5) = −3
−5 /  2  = −2      truncates toward zero
```

> `>>` and `/ 2` are **not** interchangeable for negative numbers.

### Others

```text
~ is not !                   ~ is bitwise, ! is boolean
& is not &&                  & always evaluates both sides (no short-circuit)
a ^= b; b ^= a; a ^= b;      breaks when a and b are the same variable/index
```

---

## 9. The Standard Idioms

Built from the operators above — these are the ones worth memorising:

| Task | Expression |
|---|---|
| Test bit `i` | `(x >> i & 1) == 1` or `(x & (1 << i)) != 0` |
| Set bit `i` | `x \| (1 << i)` |
| Clear bit `i` | `x & ~(1 << i)` |
| Toggle bit `i` | `x ^ (1 << i)` |
| Is odd | `(x & 1) == 1` |
| Multiply / divide by 2ⁿ | `x << n` / `x >> n` |
| `x mod 2ⁿ` (x ≥ 0) | `x & ((1 << n) - 1)` |
| Lowest set bit (isolate) | `x & -x` |
| Clear lowest set bit | `x & (x - 1)` |
| Is power of two | `x > 0 && (x & (x - 1)) == 0` |
| Count set bits | `Integer.bitCount(x)` |
| All 1s | `~0` (= −1) |

### Why `x & (x − 1)` clears the lowest set bit

```text
x     = 1011 0100
x − 1 = 1011 0011      borrowing flips the lowest 1 and everything below it
x&(x−1)=1011 0000      ← that bit is gone
```

### Why `x & −x` isolates it

```text
x     = 1011 0100
−x    = 0100 1100      (two's complement: ~x + 1)
x&−x  = 0000 0100      ← only the lowest set bit survives
```

> Both follow directly from two's complement — worth being able to derive on a whiteboard
> rather than recite.

---

## 10. Java Built-ins Worth Knowing

```java
Integer.bitCount(x)                // number of set bits
Integer.toBinaryString(x)          // binary as text (great for debugging)
Integer.highestOneBit(x)           // largest power of 2 ≤ x
Integer.lowestOneBit(x)            // same as x & -x
Integer.numberOfTrailingZeros(x)   // index of the lowest set bit
Integer.numberOfLeadingZeros(x)    // zeros above the highest set bit
Integer.reverse(x)                 // reverse all 32 bits
Long.bitCount(x)                   // 64-bit versions all exist
java.util.BitSet                   // growable bitset for more than 64 flags
```

---

## 11. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What do `&`, `\|`, `^` do?** | AND keeps bits set in both, OR sets bits from either, XOR sets bits that differ. |
| **Why is XOR special?** | `x^x = 0`, `x^0 = x`, and it's commutative and associative — so duplicates cancel in any order. |
| **`~x` as a number?** | `−x − 1`. |
| **`<<` and `>>` in arithmetic terms?** | `x << n` is `x × 2ⁿ`; `x >> n` is `⌊x / 2ⁿ⌋`. |
| **Difference between `>>` and `>>>`?** | `>>` copies the sign bit; `>>>` fills with zeros. They agree for non-negative values. |
| **Why does Java need `>>>`?** | It has no unsigned integer types, so logical shifting needs its own operator. |
| **`x << 32` in Java?** | Returns `x` — the shift count is masked to its low 5 bits for `int`. |
| **Check if `x` is a power of two?** | `x > 0 && (x & (x − 1)) == 0`. |
| **What does `x & (x − 1)` do?** | Clears the lowest set bit — repeat it to count set bits. |
| **What does `x & −x` do?** | Isolates the lowest set bit, using two's complement. |
| **Set / clear / toggle bit `i`?** | `x \| (1<<i)` / `x & ~(1<<i)` / `x ^ (1<<i)`. |
| **The precedence trap?** | `==` binds tighter than `&`, so always write `(x & 1) == 0`. |
| **`&` vs `&&`?** | `&` is bitwise and always evaluates both operands; `&&` is boolean and short-circuits. |

---

## 12. The Mental Model

```text
                      What do I need?
                            │
   ┌──────────┬─────────────┼─────────────┬──────────┐
   ↓          ↓             ↓             ↓          ↓
 KEEP       TURN ON      TOGGLE        FLIP      MOVE
 bits        bits         bits          all       bits
   │          │             │            │          │
   &          |             ^            ~       << >>
   │          │             │            │          │
 mask,     set flag,    toggle bit,   clear via  × 2ⁿ,
 test bit  union        cancel pairs   & ~mask   ÷ 2ⁿ

              build every mask from  1 << i
```
