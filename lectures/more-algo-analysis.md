# Review of Big-O

**Definition** (Asymptotic Less-Than) A function f is asymptotically
slower or equal to function g, written **f ≲ g** if and only if

    ∃ k c. ∀ n ≥ k. f(n) ≤ c g(n).


**Definition** (Big-O) For a given function g, we define **O(g)** as the
the set of functions that are asymptotically slower or equal to g. More

    O(g) = { f | f ≲ g }


We use asymptotic comparisons for
1) the worst-case execution time of a program
2) the amount of space (memory) allocated by a program


## Student Exercise: anagram detection

Two words are **anagrams** of each other if they contain the same
characters, that is, they are a rearrangement of each other.

examples: mary <-> army, silent <-> listen, doctor who <-> torchwood

Think of an algorithm for detecting when two words are anagrams.
What is the time complexity of your method?

Solutions:

* Algorithm 0:
  Generate all permutations of the first word.
  This is O(n!) time and O(n) space.

* Algorithm 1:
  For each character in the first string
  check it off in the second string.
  This is O(n²) time and O(n) space.

* Algorithm 2:
  Sort both strings then 
  compare the sorted strings for equality
  This is O(n log n) time and O(1) space.

* Algorithm 3:
  Count letter occurrences in both words and then compare
	  the number of occurrences of each letter.
  This is O(n) time and O(k) space
  (where k is the number of characters in the alphabet).

## Reasoning about asymptotic upper bounds

* Polynomials:
    If f(n) = cᵢ nⁱ + ... + c₁ n¹ + c₀, 
	then f ≲ nⁱ.
	
* Addition:
    If f₁ ≲ g and f₂ ≲ g,
    then f₁ + f₂ ≲ g.
	
* Multiplication:
    If f₁ ≲ g₁ and f₂ ≲ g₂,
    then f₁ × f₂ ≲ g₁ × g₂.
	
* Reflexivity:
    f ≲ f
	
* Transitivity:
    f ≲ g and g ≲ h implies f ≲ h

## Proof of the rule for addition

	Theorem. If f₁ ≲ g and f₂ ≲ g, then f₁ + f₂ ≲ g.
	Proof.
	 Suppose f₁ ≲ g and f₂ ≲ g.
	 So   ∀n ≥ k₁. f₁(n) ≤ c₁ × g(n)    (1)
	 and  ∀n ≥ k₂. f₂(n) ≤ c₂ × g(n)    (2)
	 by (∃⇒).

	 We need to show that ∃k c. ∀n ≥ k. f₁(n) + f₂(n) ≤ c × g(n)
	 Choose k = k₁ + k₂ (⇒∃).
	 Choose c = c₁ + c₂ (⇒∃).
	 ∀n ≥ k₁ + k₂. f₁(n) + f₂(n) ≤ (c₁ + c₂) × g(n)
	 Let n be an arbitrary number and suppose n ≥ k₁ + k₂. (⇒∀)
	 We need to show that f₁(n) + f₂(n) ≤ (c₁ + c₂) × g(n)
	 equivalently: f₁(n) + f₂(n) ≤ c₁ × g(n) + c₂ × g(n)
	 From (1) and (2) we have
	   f₁(n) ≤ c₁ × g(n)
	   f₂(n) ≤ c₂ × g(n)
	 and therefore
	   f₁(n) + f₂(n) ≤ c₁ × g(n) + c₂ × g(n).
	QED

## Student Exercise

Show that 2 log n ≲ n / 2.

We need to choose k and c.

k=1, c=2
k=4, c=2
k=16, c=1

n    log n   2 log n   n / 2
1     0        0       1/2
2     1        2       1
4     2        4       2
8     3        6       4

16    4        8       8
32    5       10      16

∀ n ≥ 16. 2 log n ≤ n / 2

To make it easy to compute log n, let's look at powers of 2.

|  n  | log n | 2 log n | n/2 |
| --- | ----- | ------- | --- |
|  1  |   0   |    0    | 1/2 |
|  2  |   1   |    2    |  1  |
|  4  |   2   |    4    |  2  |
|  8  |   3   |    6    |  4  |
| 16  |   4   |    8    |  8  |
| 32  |   5   |   10    | 16  |
| 64  |   6   |   12    | 32  |

Choose k = 16.
Choose c = 1.

    We need to show that ∀ n ≥ 16. 2 log n ≤ n / 2.
    Proof by induction.
    Base case n = 16. 
	  2 log 16 = 8 = n / 2.
    Induction step. Assume k > 16.
      Assume 2 log(k) ≤ k/2 (Induction Hypothesis).
      We need to show that 2 log (k + 1) ≤ (k + 1) / 2.
      Work backwards through the following:
      2 log(k + 1) ≤ 2 log(1.18 × k)
                   = 2 (log(1.18) + log(k))      (log(ab) = log(a) + log(b))
                   ≤ 2 (1/4  + log(k))           log(1.18) < 0.239 < 1/4
                   = 2(1/4) + 2 log(k)
                   ≤ 1/2 + 2 log(k) 
                   ≤ 1/2 + k/2   by IH
                   = (1 + k) / 2.
    QED

# Practice analyzing the time complexity of an algorithm: Insertion Sort

	public static void insertion_sort(int[] A) {
		for (int j = 1; j != A.length; ++j) {
			int key = A[j];
			int i = j - 1;
			while (i >= 0 && A[i] > key) {
				A[i+1] = A[i];
				i -= 1;
			}
			A[i+1] = key;
		}
	}


What is the time complexity of insertion_sort?

Answer:
* inner loop is O(n)
* outer loop is O(n)
* so the entire algorithm is O(n²)

# Time Complexity of Java collection operations

* LinkedList
	* add: O(1)
	* get: O(n)
	* contains: O(n)
	* remove: O(1)
    * size: O(1)

* ArrayList
	* add: O(1)*
	* get: O(1)
	* set: O(1)
	* contains: O(n)
	* remove: O(n)
    * size: O(1)

HashSet
HashMap

TreeSet  (Balanced Binary Search Tree)
    * contains: O(log(n))
TreeMap  (Balanced Binary Search Tree)

# Common complexity classes:

* O(1)                        constant
* O(log(n))                   logarithmic
* O(n)                        linear
* O(n log(n))
* O(n²), O(n³), etc.         polynomial
* O(2ⁿ), O(3ⁿ), etc.         exponential
* O(n!)                      factorial

O(5n)?
O(nⁿ)? 

# Lower bounds

**Definition** (Omega) For a given function g(n), we define **Ω(g)** as
the set of functions that grow at least as fast a g(n):

    f ∈ Ω(g) iff ∃ k c, ∀ n ≥ k, 0 ≤ c g(n) ≤ f(n).

**Notation** f ≳ g means f ∈ Ω(g). (Or instead write g ≲ f.)


# Tight bounds

**Definition** (Theta) For a given function g(n), **Θ(g)** is the set
of functions that grow at the same rate as g(n):

    f ∈ Θ(g) iff ∃ k c₁ c₂, ∀ n ≥ k, 0 ≤ c₁ g(n) ≤ f(n) ≤ c₂ g(n).

We say that g is an *asymptotically tight bound* for each function
in Θ(g).

**Notation** f ≈ g means f ∈ Θ(g). We say f and g are asymptotically equivalent.


# Reasoning about bounds.

* Symmetry: f ≈ g iff g ≈ f

* Transpose symmetry: f ≲ g iff g ≳ f

# Relationships between Θ, O, and Ω.

* f ≈ g implies f ≲ g, Θ(g) ⊆ O(g)

* f ≈ g implies g ≲ f, Θ(g) ⊆ Ω(g)

* f ≈ g iff (f ≲ g and g ≲ f), Θ(g) = Ω(g) ∩ O(g)

# Example: Merge Sort

Divide and conquer!

Split the array in half and sort the two halves.

Merge the two halves.

	private static int[] merge(int[] left, int[] right) {
	   int[] A = new int[left.length + right.length];
	   int i = 0;
	   int j = 0;
	   for (int k = 0; k != A.length; ++k) {
		   if (i < left.length
			   && (j ≥ right.length || left[i] <= right[j])) {
			  A[k] = left[i];
			  ++i;
		   } else if (j < right.length) {
			  A[k] = right[j];
			  ++j;
		   }
	   }
	   return A;
	}

	public static int[] merge_sort(int[] A) {
	   if (A.length > 1) {
		   int middle = A.length / 2;
		   int[] L = merge_sort(Arrays.copyOfRange(A, 0, middle));
		   int[] R = merge_sort(Arrays.copyOfRange(A, middle, A.length));
		   return merge(L, R);
	   } else {
		   return A;
	   }
	}    

What's the time complexity?

Recursion tree:

			   c*n                    = c*n
			 /     \
			/       \
	   c*n/2        c*n/2             = c*n
	   /  \         /   \
	  /    \       /     \     
	c*n/4  c*n/4  c*n/4  c*n/4        = c*n
	...

Height of the recursion tree is log(n).

So the total work is c n log(n).

Time complexity is O(n log(n)).


<!--  LocalWords:  mary torchwood QED ab IH int LinkedList ArrayList
 -->
<!--  LocalWords:  HashSet HashMap TreeSet TreeMap iff copyOfRange
 -->
