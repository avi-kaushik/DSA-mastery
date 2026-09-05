# 001 — Introduction to Bitwise Operations

> **One-line takeaway:** Numbers are already stored as bits. Bitwise operations skip the arithmetic layer and work on those bits directly — which makes them **fast**, **memory-cheap**, and the natural fit for anything involving flags, sets, or hardware.

---

## 1. What Are Bitwise Operations?

> **A bitwise operation applies a logical rule to every bit of a number independently,
> in parallel.**

Ordinary arithmetic treats a number as a *value*. Bitwise treats it as a *pattern of bits*.

```text
        12  =  0000 1100
         5  =  0000 0101
                ─────────
12 &  5  =  4  =  0000 0100      ← each column handled on its own
12 |  5  = 13  =  0000 1101
12 ^  5  =  9  =  0000 1001
```

Every column is computed at the same time by the hardware. There is no carrying, no
borrowing, no loop — just one CPU instruction.

---

## 2. Refresher: How Numbers Are Stored

### Binary place values

```text
bit position   7    6    5    4    3    2    1    0
place value  128   64   32   16    8    4    2    1
                                   ↑
                          value = Σ (bit × 2^position)
```

```text
  13  =  8 + 4 + 1  =  0000 1101
  ─────────────────────────────────
   bit 3 = 1  →  8
   bit 2 = 1  →  4
   bit 1 = 0  →  0
   bit 0 = 1  →  1
```

> **Bit `i` is set** means the value contains `2^i`. That single sentence explains most
> bit tricks.

### Powers of two worth knowing on sight

```text
2⁰=1   2¹=2   2²=4   2³=8   2⁴=16   2⁵=32   2⁶=64   2⁷=128
2⁸=256   2¹⁰=1,024 ≈ 10³   2¹⁶=65,536   2²⁰≈10⁶   2³⁰≈10⁹
```

### Sizes in Java

| Type | Bits | Range |
|---|---:|---|
| `byte` | 8 | −128 … 127 |
| `short` | 16 | −32,768 … 32,767 |
| `char` | 16 | 0 … 65,535 (**unsigned**) |
| `int` | 32 | ≈ −2.1×10⁹ … 2.1×10⁹ |
| `long` | 64 | ≈ ±9.2×10¹⁸ |

⚠️ **Java has no unsigned `int`.** Every integer type except `char` is signed.

### Two's complement — how negatives are stored

```text
To negate a number:  flip all bits, then add 1
```

```text
  5  =  0000 0101
 ~5  =  1111 1010        (flip)
 +1  =  1111 1011  = −5  ✓
```

```text
The leftmost bit is the SIGN bit:   0 → non-negative,  1 → negative
```

Two consequences you will actually use:

```text
~x  =  −x − 1                     (so ~0 = −1, ~5 = −6)
−x  =  ~x + 1
```

> This is why `x & -x` isolates the lowest set bit — see
> [Bitwise Operators](002-bitwise-operators.md).

---

## 3. Advantages of Bitwise Operations

### ⚡ Speed

A bitwise op is typically a **single CPU instruction** — among the cheapest operations a
processor performs.

```text
x * 2      →  x << 1        cheap shift instead of multiply
x / 2      →  x >> 1        (see the caveat in §6)
x % 2      →  x & 1         cheap mask instead of a division
```

### 💾 Memory

One `int` stores **32 independent true/false values**.

```text
boolean[32]  →  32 bytes in Java (1 byte each, often padded)
int          →   4 bytes           ← 8× smaller
```

Scaled up: a set of 1,000,000 flags is ~1 MB as `boolean[]` but ~125 KB as a bitset.

### 🎯 Whole-set operations in one step

If a set is a bitmask, set algebra becomes arithmetic:

```text
union         a | b
intersection  a & b
difference    a & ~b
symmetric diff a ^ b
```

All `O(1)` for up to 64 elements, instead of looping.

### ✂️ Elegance

Several classic problems collapse to one line:

```text
Find the number appearing once (all others twice)   →  XOR everything
Check power of two                                  →  (x & (x−1)) == 0
Swap without a temp variable                        →  a ^= b; b ^= a; a ^= b;
```

### 🔒 Atomicity

A single word of flags can be read or written atomically, which matters for concurrent
code and hardware registers.

---

## 4. Applications — Where It's Actually Used

| Area | How bits are used |
|---|---|
| **Flags & permissions** | Unix `chmod` (`rwx` = 3 bits), feature toggles, Java's `Modifier` constants |
| **Bitmask DP** | Subset states in TSP, assignment problems — `1 << n` states |
| **Hashing** | `HashMap` indexes buckets with `(n − 1) & hash`; Bloom filters set bits |
| **Compression** | Bit packing, Huffman codes, varint encoding |
| **Cryptography** | XOR ciphers, AES, hash functions, PRNGs (xorshift) |
| **Graphics** | RGBA packed in one 32-bit int, alpha blending, chess **bitboards** |
| **Networking** | Subnet masks, CIDR (`ip & mask`), TCP flag bits |
| **Databases** | Bitmap indexes for low-cardinality columns |
| **Embedded / OS** | Hardware registers, interrupt masks, page-table entries |
| **Error detection** | Parity bits, checksums, Hamming codes |
| **Data structures** | `BitSet`, Fenwick tree (`i & −i`), binary tries for max-XOR queries |

### Real examples from the Java library

```java
// HashMap: fast modulo, valid because capacity is a power of two
index = (capacity - 1) & hash;

// HashMap: mix high bits into low bits so they affect the index
hash = h ^ (h >>> 16);

// ArrayList growth: newCap = oldCap * 1.5, done with a shift
newCapacity = oldCapacity + (oldCapacity >> 1);
```

> `(n − 1) & hash` works only because capacity is a power of two — that's *why* `HashMap`
> rounds capacity up to one. A nice thing to mention in an interview.

---

## 5. What This Buys You in Interviews

Bit manipulation shows up as a recognisable family of problems:

```text
XOR-based        single number, missing number, two non-repeating numbers
Counting bits    number of set bits, count bits for 0..n
Powers of two    is power of 2, next power of 2
Subsets          generate all 2ⁿ subsets, bitmask DP
Manipulation     set/clear/toggle/check the i-th bit, swap, reverse bits
Arithmetic       add/multiply/divide without operators
```

> The pattern to recognise: **if the answer involves subsets, pairing, parity, or flags,
> bits are probably the intended tool.**

---

## 6. ⚠️ Caveats

```text
Readability       (x & (x-1)) == 0  is not obvious; comment it

Signed shifts     x >> 1 is FLOOR division, x / 2 truncates toward zero
                  −5 >> 1  = −3      (floor)
                  −5 /  2  = −2      (truncate)      ⚠️ NOT the same

Width assumptions int is 32 bits in Java, but n bits in C depends on the platform

Shift distance    Java masks the shift count: x << 33 is x << 1 for int
                                              (low 5 bits for int, 6 for long)

Precedence        & | ^ bind LOOSER than == in Java/C
                  x & 1 == 0   parses as   x & (1 == 0)   ✗ always parenthesise

Premature use     the JIT already turns x * 2 into a shift; use bits for CLARITY
                  of intent (flags, sets), not to outsmart the compiler
```

---

## 7. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **What is a bitwise operation?** | A logical rule applied to every bit of a value independently, in one CPU instruction. |
| **Why are they fast?** | They map directly to single hardware instructions — no carrying or looping. |
| **Main advantages?** | Speed, 32× memory saving for flags, `O(1)` set operations, and concise solutions. |
| **How are negatives stored?** | Two's complement: flip all bits and add 1; the leftmost bit is the sign. |
| **What is `~x` numerically?** | `−x − 1`. |
| **How many bits in a Java `int` / `long`?** | 32 and 64, both signed; Java has no unsigned `int`. |
| **Real-world uses?** | Permissions, subnet masks, bitmask DP, hashing, compression, graphics, bitboards, hardware registers. |
| **Where does Java itself use bits?** | `HashMap` index `(n−1) & hash` and hash spreading `h ^ (h >>> 16)`; `ArrayList` growth `cap + (cap >> 1)`. |
| **Why must `HashMap` capacity be a power of two?** | So `(n−1) & hash` is equivalent to `hash % n` but far cheaper. |
| **Is `x >> 1` the same as `x / 2`?** | Not for negatives — `>>` floors (`−5 >> 1 = −3`), `/` truncates (`−5 / 2 = −2`). |
| **Biggest pitfall in Java/C?** | Operator precedence — `x & 1 == 0` is not what you meant; parenthesise. |

---

## 8. The Mental Model

```text
              A number is a PATTERN OF BITS
                          │
        ┌─────────────────┼─────────────────┐
        ↓                 ↓                 ↓
   treat it as        treat it as       treat it as
    a value            a SET             FLAGS
        │                 │                 │
   + − × ÷           & | ^ ~           set/clear/test
        │                 │                 │
   arithmetic       union/intersect     permissions,
                    O(1) set algebra    config, state
                          │
                          ↓
              one CPU instruction each
```
