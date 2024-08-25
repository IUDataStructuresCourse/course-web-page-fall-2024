# Review of Big-O

**Definition** (Big-O) For a given function g, we define **O(g)** as the
the set of functions that grow similarly or slower than g. More
precisely, 

    f ∈ O(g) iff ∃ k c. ∀ n ≥ k. f(n) ≤ c g(n).

**Notation** We write f ≲ g iff f ∈ O(g), and say that f is
asymptotically less-or-equal to g.

## Example: anagram detection

Two words are **anagrams** of each other if they contain the same
characters, that is, they are a rearrangement of each other.

examples: mary <-> army, silent <-> listen, doctor who <-> torchwood

1)
Do an array search on an array made of the letters of the second word
and search for each letter of the first word.
Cross off the letter you find.

for ...  n
  for ... n

O(n^2)

2) Generate all permutations of the first word.
Check whether the second word is in the list of permutations.

O(n!)

3) sort both words, trim spaces, check for equality.

O(n log(n)) + O(n) + O(n)
= O(n log(n))

4) create a hashmap that maps letters to # occurences for each word,
   then compare the hashmaps for equality

O(n)*

* assuming O(1) access to the hashtable










For the following algorithms, what's the time complexity? space
complexity?

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
  This is O(n lg n) time and O(1) space.

* Algorithm 3:
  Count letter occurences in both words and then compare
	  the number of occurences of each letter.
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
	 Let n be a number and assume n ≥ k₁ + k₂. (⇒∀)
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

	public static void insertion_sort(int[] A) { // let n = A.length 
		for (int j = 1; j != A.length; ++j) { // n iterations, body: O(1)+O(1)+O(n)+O(1)=O(n), for loop total: O(n)*O(n)=O(n^2)
			int key = A[j]; O(1)
			int i = j - 1;  O(1)
			while (i >= 0 && A[i] > key) { // n iterations, body: O(n), while loop total: O(1)*O(n) = O(n) 
				A[i+1] = A[i];  O(1)
				i -= 1;         O(1)
			}
			A[i+1] = key;       O(1)
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
* O(n²), O(n^3), etc.         polynomial
* O(2^n), O(3^n), etc.        exponential
* O(n!)                       factorial

O(5n)
O(n^n)? 

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

**Notation** f ≈ g means f ∈ Θ(g). We say f and g are asympotically equivalent.


# Reasoning about bounds.

* Symmetry: f ≈ g iff g ≈ f

* Transpose symmetry: f ≲ g iff g ≳ f

# Relationships between Θ, O, and Ω.

* Θ(g) ⊆ O(g), f ≈ g implies f ≲ g

* Θ(g) ⊆ Ω(g), f ≈ g implies g ≲ f

* Θ(g) = Ω(g) ∩ O(g), f ≈ g iff (f ≲ g and g ≲ f)

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

