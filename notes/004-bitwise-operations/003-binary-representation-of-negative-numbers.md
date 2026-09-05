# 003 — Binary Representation of Negative Numbers

> **One-line takeaway:** Negatives are stored in **two's complement** — flip every bit and add 1. It wins because it gives exactly **one zero** and lets the CPU use the **same adder** for addition and subtraction, with no special case for signs.

---

## 1. The Problem

Bits have no minus sign. With `n` bits you get `2ⁿ` patterns, and you must somehow split
them between positive and negative values.

```text
8 bits → 256 patterns → how do we fit −128 … +127 into them?
```

Three schemes were tried historically. Only the last one survives.

---

## 2. Attempt 1 — Sign-Magnitude

Use the leftmost bit as a sign flag, and the rest as the plain magnitude.

```text
+5  =  0000 0101
−5  =  1000 0101      ← just flip the top bit
        ↑
      sign bit
```

Simple to read, but it breaks arithmetic:

```text
❌ TWO ZEROS:     0000 0000 = +0
                  1000 0000 = −0        which one does == compare against?

❌ Addition fails: 5 + (−5)
                   0000 0101
                 + 1000 0101
                 ───────────
                   1000 1010  = −10     ✗ should be 0
```

The CPU would need to inspect the signs and *decide* whether to add or subtract.

---

## 3. Attempt 2 — One's Complement

Represent `−x` by flipping every bit of `x`.

```text
+5  =  0000 0101
−5  =  1111 1010      ← flip all bits
```

Better — addition almost works:

```text
   0000 0101   (5)
 + 1111 1010   (−5)
 ───────────
   1111 1111   = −0      ✗ still not plain 0
```

```text
❌ STILL TWO ZEROS:  0000 0000 = +0
                     1111 1111 = −0

❌ Needs an "end-around carry" fixup: any carry out of the top must be added back in
```

Closer, but still two zeros and still a special case.

---

## 4. Two's Complement — the Winner ✓

> **To negate a number: flip every bit, then add 1.**

```text
  5  =  0000 0101
flip  =  1111 1010        (one's complement)
  +1  =  1111 1011   = −5  ✓
```

Now everything just works:

```text
   0000 0101   (5)
 + 1111 1011   (−5)
 ───────────
 1 0000 0000
 ↑
 carry discarded (only 8 bits kept)  →  0000 0000  =  0  ✓
```

```text
✓ ONE zero            0000 0000 is the only zero
✓ Ordinary addition   the same adder handles positives and negatives
✓ Subtraction         a − b  is just  a + (−b)
```

> **This is why hardware uses it.** Subtraction circuitry disappears entirely — the CPU
> negates and adds.

---

## 5. 🧮 Why Flip-and-Add-1 Works

Flipping every bit of `x` gives `~x`. For any `x`:

```text
x + ~x  =  1111 1111  =  −1        (every column becomes 1)
```

Rearranging:

```text
~x      =  −1 − x
~x + 1  =  −x               ∎
```

So `flip + 1` is exactly negation. Two useful identities fall straight out:

```text
~x = −x − 1                (so ~0 = −1,  ~5 = −6)
−x = ~x + 1
```

### The modular view (the deeper reason)

With `n` bits, arithmetic is modulo `2ⁿ`. Two's complement stores `−x` as the pattern
`2ⁿ − x`:

```text
8-bit:  −5  →  256 − 5  =  251  =  1111 1011   ✓
```

Then `5 + (−5) = 5 + 251 = 256 ≡ 0 (mod 256)` — the carry falls off the top and the answer
is correct **for free**.

> **Two's complement isn't a trick; it's just arithmetic mod 2ⁿ.**

---

## 6. Reading a Negative Binary Number

Two equivalent methods.

### Method A — negate it back

```text
1111 1011   = ?
flip     →   0000 0100
+1       →   0000 0101  = 5
answer   →   −5
```

### Method B — weighted places, with a negative top bit

> **The most significant bit carries a negative weight.**

```text
bit      b7    b6   b5   b4   b3   b2   b1   b0
weight  −128   64   32   16    8    4    2    1
```

```text
1111 1011  =  −128 + 64 + 32 + 16 + 8 + 0 + 2 + 1  =  −5   ✓
```

```text
value = −b(n−1)·2^(n−1)  +  Σ bᵢ·2ⁱ    for i = 0 … n−2
```

> Method B is faster once it clicks, and it explains the range in the next section.

---

## 7. The Range Is Asymmetric

```text
n bits:   −2^(n−1)  …  +2^(n−1) − 1
```

| Width | Min | Max |
|---|---|---|
| 8 (`byte`) | −128 | 127 |
| 16 (`short`) | −32,768 | 32,767 |
| 32 (`int`) | −2,147,483,648 | 2,147,483,647 |
| 64 (`long`) | ≈ −9.2×10¹⁸ | ≈ 9.2×10¹⁸ |

**Why one extra negative?** Zero occupies a slot on the positive side, so the positives run
out one earlier:

```text
patterns with sign bit 0  →  0 … 127     (128 values, one of them is zero)
patterns with sign bit 1  →  −128 … −1   (128 values, none wasted on zero)
```

### The full 4-bit table (worth internalising)

```text
pattern   unsigned   two's complement
0000          0             0
0001          1            +1
0010          2            +2
0011          3            +3
0100          4            +4
0101          5            +5
0110          6            +6
0111          7            +7      ← largest positive
1000          8            −8      ← most negative (sign bit flips the weight)
1001          9            −7
1010         10            −6
1011         11            −5
1100         12            −4
1101         13            −3
1110         14            −2
1111         15            −1      ← all ones is always −1
```

> Notice `1111` is `−1` at **every** width. That's why `~0 == −1` and why `x & −1 == x`.

---

## 8. ⚠️ The Most-Negative-Number Anomaly

`Integer.MIN_VALUE` has no positive counterpart — negating it overflows back to itself.

```text
MIN_VALUE      = 1000 0000 ... 0000  = −2,147,483,648
flip           = 0111 1111 ... 1111
+1             = 1000 0000 ... 0000  = MIN_VALUE again  ⚠️
```

```java
-Integer.MIN_VALUE   == Integer.MIN_VALUE      // true  (!)
Math.abs(Integer.MIN_VALUE) == Integer.MIN_VALUE  // true — abs returns a NEGATIVE
```

> **Interview-worthy:** `Math.abs` is not guaranteed to return a non-negative value. Any
> code doing `Math.abs(hash) % n` has a latent bug — use `(hash & 0x7FFFFFFF) % n` or a
> `long`.

---

## 9. Consequences You Actually Use

### Sign checking

```java
x < 0                 // readable
(x >>> 31) == 1       // sign bit as 0/1
x >> 31               // 0 for non-negative, −1 for negative (all ones)
```

### Branchless absolute value

```java
int mask = x >> 31;              // 0 or −1
int abs  = (x ^ mask) - mask;    // flip and add 1 only when negative
```

### `x & -x` — isolate the lowest set bit

Works *because* of two's complement:

```text
x     = 1011 0100
−x    = 0100 1100        (flip + 1)
x&−x  = 0000 0100        ← only the lowest set bit survives
```

Everything above the lowest set bit is inverted between `x` and `−x`, so AND kills it;
the lowest set bit itself is the one place both agree.

### Sign extension when widening

Copying a value into a wider type copies the **sign bit**, not zeros:

```java
byte b = -1;            // 1111 1111
int  i = b;             // 1111 1111 ... 1111 = −1   ✓ value preserved
int  u = b & 0xFF;      // 0000 0000 ... 1111 1111 = 255  ← unsigned reading
```

> `b & 0xFF` is the standard Java idiom for treating a `byte` as unsigned (0–255) — common
> when handling raw bytes from files or sockets.

### Arithmetic vs logical shift

```text
−20 >> 1   = −10                (sign bit copied in → stays negative)
−20 >>> 1  = 2,147,483,638      (zeros shifted in → huge positive)
```

See [Bitwise Operators](002-bitwise-operators.md).

### Overflow wraps around

```java
Integer.MAX_VALUE + 1 == Integer.MIN_VALUE     // true — silent wraparound
```

Java does **not** throw on integer overflow. Use `Math.addExact` when you need it to.

---

## 10. Comparison Summary

| Scheme | Negate by | Zeros | Addition works? | Used today |
|---|---|---|---|---|
| Sign-magnitude | Flip sign bit | 2 | ✗ needs sign logic | Only in floating point |
| One's complement | Flip all bits | 2 | ✗ needs end-around carry | Obsolete |
| **Two's complement** | Flip all bits, **+1** | **1** | ✓ plain binary addition | **Everywhere** |

> Floating point (`float`, `double`) *does* use sign-magnitude — which is why `−0.0` exists
> in Java and `0.0 == -0.0` is true while their bit patterns differ.

---

## 11. Quick Recall — Interview One-Liners

| Question | Crisp answer |
|---|---|
| **How are negatives stored?** | Two's complement — flip all bits and add 1. |
| **Why two's complement over the alternatives?** | It has a single zero and lets one adder handle both signs, so subtraction is just `a + (−b)`. |
| **Why does flip-and-add-1 work?** | `x + ~x = −1`, so `~x + 1 = −x`. |
| **The modular view?** | `−x` is stored as `2ⁿ − x`, so arithmetic mod `2ⁿ` gives the right answer and the carry falls off. |
| **How do you read a negative binary value?** | Either negate it back, or weight the MSB negatively: `−128 + 64 + …`. |
| **Range for `n` bits?** | `−2^(n−1)` to `2^(n−1) − 1`. |
| **Why one more negative than positive?** | Zero uses a slot on the positive side. |
| **What is `1111…1`?** | `−1`, at any width — hence `~0 == −1`. |
| **What is `~x` numerically?** | `−x − 1`. |
| **What's odd about `Integer.MIN_VALUE`?** | Negating it overflows to itself, so `Math.abs` can return a negative value. |
| **How do you check the sign with bits?** | `x >>> 31` gives 0/1; `x >> 31` gives 0 or −1 (useful as a mask). |
| **Why does `x & -x` isolate the lowest set bit?** | `−x` is `~x + 1`, so it matches `x` only at that bit. |
| **What happens on integer overflow in Java?** | It wraps silently: `MAX_VALUE + 1 == MIN_VALUE`. |
| **How do you read a `byte` as unsigned?** | `b & 0xFF` — widening alone sign-extends. |

---

## 12. The Mental Model

```text
                     Store −x in n bits
                             │
                             ↓
                    flip all bits, add 1
                             │
                             ↓
                   same as storing 2ⁿ − x
                             │
              ┌──────────────┼──────────────┐
              ↓              ↓              ↓
        MSB weighs      exactly one     a + (−b)
         −2^(n−1)          zero         is plain
              │              │           addition
              ↓              ↓              ↓
       range is       0 comparisons   no subtract
      asymmetric        are safe        circuit
```
