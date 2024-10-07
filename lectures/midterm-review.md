# Lecture: Midterm Review

Topics

* Labs and Projects
    * Array Search
    * Flood It: time complexity, Java collection classes
	* Insertion Sort on Linked Lists with Deduce
    * Quicksort with Iterators 
* Arrays, array algorithms e.g. search, rotate, Java's ArrayList
* Half-open ranges
* Testing
* Math: log/exponent, sums, induction
* Linked lists, Java's LinkedList
* Abstract data types
    * interfaces
    * iterators
* Time complexity
* Binary Trees, next and prev


## Big-O

Def. f is asymptotically less-or-equal to g, written f ≲ g,
iff exists c k st. for all n >= k. f(n) <= c g(n).

Def. O(g) = { f | f ≲ g }

Simplest example

        f(n) = n

        g(n) = n²

        n ≲ n²? aka. n ∈ O(n²)?

        choose k = 0, c = 1

        for all n ≥ 0, n ≤ n² 

Example that requires a more interesting choice of k.

        show that n + 100 ≲ n²

        f(n) = n + 100

        g(n) = n²

        n   | n + 100 | n²
        --- | ------- | ----
        1   |  101    | 1
        2   |  102    | 4
		3   |  103    | 9
		... |  ...    | ...
        10  |  110    | 100
        11  |  111    | 121    <---
		12  |  112    | 144

        choose k = 11, c = 1
        for all n ≥ 11, n + 100 ≤ n²

Example that requires a more interesting choice of c.

        3 * n ≲ n

        f(n) = 3 n

        g(n) = n

        if we try c=1, no choice of k will work.

        choose c = 3, then any reasonable k will work
		
		    3 n / n <= c n / n 
			3 <= c

        for all n ≥ 0, 3n ≤ 3n.



        5n^2 + 3n + 10 in   O(n^2)
		   choose c=6

Exponents and logarithm

(2 * x) / 2 = x

log(2^x) = x

log(2^0) = log(1) = 0   (n = 1)
log(2^1) = log(2) = 1   (n = 2)
log(2^2) = log(4) = 2   (n = 4)
log(2^3) = 3   (n = 8)
log(2^4) = 4   (n = 16)


Ex. 3.1-1

prove that max(f(n),g(n)) ∈ O(f(n) + g(n))

where

    max(f(n),g(n))(n) = f(n) if f(n) ≥ g(n)
                        g(n) if g(n) > f(n)
Recall:

   f ∈ O(g) iff exists c k st. for all n >= k. f(n) <= c * g(n)

   nts. max(f(n),g(n)) ≤ c * (f(n) + g(n)) for all n >= k

   choose k = 0

   We have f(n) ≤ f(n) + g(n)
   and    g(n) ≤ f(n) + g(n)
   so     max(f(n), g(n)) ≤ f(n) + g(n)
   We choose c to be 1.



## Binary Trees

traversals: 

* pre-order: current, left, right
* in-order: left, current, right
* post-order; left, right, current

                     _____ 25____
                    /            \
                 __15__           50
                /      \         /
               10       22      35
              /  \     /       / 
             4    12  16      31 

pre order traversal: 25, 15, 10, 4, 12, 22, 16, 50, 35, 31
in order traversal:  4, 10, 12, 15, 16, 22, 25, 31, 35, 50
post order traversal: 4, 12, 10, 16, 22, 15, 31, 35, 50, 25

pre-order breadth first:
      25, 15, 50, 10, 22, 35, 4, 12, 16, 31


first/last methods

next/previous methods

    if right child, return right's first
    else find ancestor that comes next wrt. inorder


## Maximum of a Sequence using Iterators

Implement a `max` algorithm that returns the maximum element of a
sequence `s` or return `zero` if none of the elements are larger than
`zero`.

    public static <T> T max(Sequence<T> s, T zero, BiPredicate<T, T> less);

Here are the interfaces:

    interface Sequence<T> {
        Iter<T> begin();
        Iter<T> end();
    }

    interface Iter<T> {
        T get();
        void advance();
        boolean equals(Iter<T> other);
        Iter<T> clone();
    }

    interface BiPredicate {
	    boolean test(T t, U u);
	}

    public static <T> T max(Sequence<T> s, T zero, BiPredicate<T, T> less) {
	   T bestyet = zero;
	   for (iter = s.begin(); ! iter.equals(s.end()); iter.advance()) {
	      if (less.test(bestyet, iter.get()))
		     bestyet = iter.get();
	   }
	   return bestyet;
    }


## Linked Lists

### Sum-Append Proof

The sum of a list is defined as follows

    sum(empty) = 0
    sum(node(x, xs)) = x + sum(xs)

Recall that operator ++ appends two lists together:

    empty ++ ys = ys
    node(n, xs) ++ ys = node(n, xs ++ ys)

Prove that

    sum(xs ++ ys) = sum(xs) + sum(ys)

	
# Sums, Induction

Prove by induction that

	N
	Σ i^2  = N(N + 1)(2N+1) / 6
	i=0

Base case (N=0):

    0^2 = 0 = 0 / 6
	
Induction step:

	N+1
	Σ i^2
	i=0
	=  (by def. of Σ)
	N
	Σ i^2   + (N+1)^2
	i=0
    =  (by induction hypothesis)
    N(N+1)(2N+1) / 6  + (N+1)^2
	= 
    N(N+1)(2N+1) / 6  + N^2 + 2N + 1
	= 
    N(N+1)(2N+1) / 6  + (6N^2 + 12N + 6) / 6
	=
    (N(N+1)(2N+1) + 6N^2 + 12N + 6) / 6
	= 
	((N^2 + N)(2N+1) + 6N^2 + 12N + 6) / 6
	= 
	(2N^3 + 2N^2 + N^2 + N + 6N^2 + 12N + 6) / 6
	= 
	(2N^3 + 9N^2 + 13N + 6) / 6
	=
	((2N^3 + 6N^2 + 4N) + (3N^2 + 9N + 6)) / 6
    =
	(N^2 + 3N + 2)(2N + 3) / 6
    =
	(N+1)(N+2)(2(N+1) + 1) / 6

