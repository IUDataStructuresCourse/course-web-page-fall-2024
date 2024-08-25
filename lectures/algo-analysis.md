## Time Complexity

We are interested in the running time as the inputs grow large.

We care about the growth rate of an algorithm's run-time more than
it's value at a particular point.
	  
We focus on the worst-case run-time of algorithms because we want to
offer guarantees to the user of an algorithm that hold for all
possible inputs.
	  
Our goal is to classify functions that grow at a similar rate,
ignoring details that don't matter.

Functions to think about: n, 10n, log n, n log n, 10 n², n² + n

https://www.desmos.com/calculator

## Big-O: Asymptotic Upper Bound

**Definition** (Big-O) For a given function g, we define **O(g)** as the
the set of functions that grow similarly or slower than g. More
precisely, f ∈ O(g) iff ∃ k c. ∀ n ≥ k. f(n) ≤ c g(n).

We say that g(n) is an *asymptotic upper bound* of all the functions
in the set O(g).

**Notation** We write f ≲ g iff f ∈ O(g), and say that f is
asymptotically less-or-equal to g.

Alternatively, the asympotic growth of functions can be characterized
using limits.

f ∈ O(g) iff lim{n→∞} f(n)/g(n) is greater or equal zero and not infinity.

Demonstrate the main idea and the role of k and c.

e.g. 4x versus x²

1. Is 4x ≲ x²?
2. Is x² ≲ 4x?
   
e.g. x + 10 versus x

1. Is x + 10 ≲ x?
2. Is x ≲ x + 10?

# Review of ∃

∃ means "there exists"

Proof rules:

(⇒∃) To prove ∃ x. P(x), choose x=k and show P(k).

(∃⇒) If you have an assumption ∃ x. P(x), then you have P(y)
   for any fresh variable y.
   
Let's see these rules used in an example.

## Example of ∃

We define "x is even" as ∃ k. 2 k = x.

Theorem. 6 is even
Proof. Choose k=3 and note that 2 × 3 = 6. QED.

Theorem. If x and y are even, then x + y is even.
Proof.
Assume x is even, so (∃ k. 2 k = x) so 2 k₁ = x (∃⇒).
Assume y is even, so (∃ k. 2 k = y) so 2 k₂ = y (∃⇒).

x + y = 2k₁ + 2k₂ = 2(k₁ + k₂)          (1)

We need to show that x + y is even.
It suffices to show that (∃ k. 2 k = x + y).
Choose k = k₁ + k₂ and note that 2(k₁ + k₂) = x + y by equation (1).
QED


## Review of ∀

∀ means "for all"

Proof rules:

     ∀ x. x + x = 2x 
	 fix y arbitrary number
	 y + y = 2y   (cause math)

To prove ∀ x. P(x), 
   (⇒∀) prove that P(y) is true for some unknown entity y.
   (Induction) If x is a natural number, use induction:
      * prove P(0)
	  * prove that P(k) implies P(1+k) for an unknown number k.

(∀⇒) If you have an assumption ∀ x. P(x), then you known P(y)
   for any choice of y.

## Example of ∀

Theorem. ∀ n. n is even implies n + 2 is even.
Proof.
 Let p be a arbitrary number. (⇒∀)
 Assume that p is even, that is, (∃k. 2 × k = p).
 So 2 × k₁ = p by (∃⇒)      (1)
 
 We need to show that p + 2 is even, that is, (∃ k. 2 × k = p + 2).
 
 Aside: We need prove an equation similar to 2 × k = p + 2 except with
 something else in the k position.
 Equation (1) has p on the right-hand side, so we could add 2 to both sides:
 2 × k₁ + 2 = p + 2
 then factor out the 2
 2 × (k₁ + 1) = p + 2             (2)
 and now we're in good shape because this equation is the same as 2 × k = p + 2
 except that we have (k₁ + 1) in place of k.
 Now back to the proof.

 Choose k = k₁ + 1 and note that 2 × (k₁ + 1) = p + 2 by equation (2).
QED.

Theorem. 2 is even.
Proof.
0 is even because 2 × 0 = 0.
2 is even by applying the above theorem and rule (∀⇒).
QED.


## Back to Big-O

Recall the  definition:

    f ≲ g iff ∃ k c. ∀ n ≥ k. f(n) ≤ c g(n).

**Example** Let's show that (n² + n + 10) ≲ n².
So we need to find a constant c such that

    n² + n + 10 ≤ c n²   when n is large.

We divide both sides by n².

    1 + 1/n + 10/n² ≤ c

When n is large, the terms 1/n and 10/n² get very small, so we need to
choose something a bit bigger than 1, so we choose c = 2.

Next we need to find a constant k such that

    n² + n + 10 ≤ 2 n²   when n is greater than k.

We compute a handful of values of these functions.

| n   | n² + n + 10  | 2n²  |
| --- | ------------ | ---- |
| 1   | 12           |  2   |
| 2   | 16           |  8   |
| 3   | 22           | 18   |
| 4   | 30           | 32   |
| 5   | 40           | 50   |
| 6   | 52           | 72   |

We see that from n=4 and onwards, 2n² is greater than n² + n + 10, so
we choose k = 4. We have some emprical evidence that we've made the
correct choices, but to know for sure, we prove the following theorem.

**Theorem** ∀ n. if n ≥ 4 then n² + n + 10 ≤ 2 n².

Proof. We proceed by induction on n.
* Base case (n = 0) The theorem is trivially true
    because it's not true that 0 ≥ 4.
* Induction case. Let n be any integer.
    If n ≥ 4 then n² + n + 10 ≤ 2 n² (IH). We need to show that

		(n+1)² + (n+1) + 10 ≤ 2 (n+1)²

	which we can rearrange to the following, separating out parts that
	match the induction hypothesis (IH) from the rest.

		(n² + n + 10) + (2n + 2) ≤ 2n² + (4n + 2)

	Thanks to the IH, it suffices to show that 

		2n + 2 ≤ 4n + 2

	which is true because n ≥ 4 ≥ 0.


