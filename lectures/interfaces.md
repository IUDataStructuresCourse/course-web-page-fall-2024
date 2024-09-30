## Java Interfaces, a quick reminder

An interface, aka. Abstract Data Type, acts as an intermediary between
data structures and algorithms.

An interface specifies some methods that are common to several
data structures and that are needed by one or more algorithms.

Example: the `Sequence` and `Iterator` interfaces

Enables visiting all the elements in order.

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

Example: Linear Search using the Sequence and Iterator Interfaces

    static <T> Iter<T> find_first_equal(Sequence<T> S, T x) {
       for (Iter<T> i = S.begin(); ! i.equals(S.end()); i.advance()) { // i != S.end()
          if (i.get() == x) {
             return i;
          }
       }
       return S.end();
    }

List implementation of Sequence 

    class LinkedList<T> implements Sequence<T> {
       Node<T> head;
       ...
       public class ListIter implements Iter<T> {
	     Node<T> position;
		 ListIter(Node<T> pos) { position = pos; }
		 T get() { return position.data; }
		 void advance() { 
		    position = position.next;
		 }
		 boolean equals(Iter<T> other) {
		    return this.position == other.position;
		 }
		 Iter<T> clone() { ... }
	   }
       ...
       Iter<T> begin() {
	     return new ListIter(this.head);
	   }
       Iter<T> end() {
	     return new ListIter(null);
	   }
	   
    }

## Why abstract data types like iterators?

Iterators add a fair amount of complexity, so there needs to be a
benefit that outweighs the cost, otherwise it would be better to do
without them.

Answer: code reuse.

Example: the `equals` algorithm for comparing the contents of two
sequences. Consider how much code is needed to implement this
algorithm for arrays (A), singly-linked lists (SL), doubly-linked
lists (DL), and combinations of them.  The algorithm has two
parameters, so you'd need 9 different versions of the equals
algorithm!

|     |  A  |  SL |  DL |
| --- | --- | --- | --- |
| A   |  1  |  2  |  3  |
| SL  |  4  |  5  |  6  |
| DL  |  7  |  8  |  9  |

Here's the code for the A-A combination

    static <T> boolean equals(T s1[], T s2[]) {
            int j = 0; 
            for (int i = 0; i != s1.length; ++i) {
                    if (j == s2.length || s1[i] != s2[j])
                            return false;
                    ++j;
            }
            return j == s2.length;
    }

Student in-class exercise: implement the SL-A version (without iterators).

*Abstraction* is the act of removing characteristics that are not
relevant to your immediate purpose while retaining the characteristics
that are necessary.

The `Sequence` and `Iter` interfaces provide an abstract view of a
sequence of elements for the purpose of algorithms that need to
traverse sequences and inspect their elements.

The algorithms speak the 'iterator' language (`i.get()`, `i.advance()`).
Each iterator implementation translates to a different 'data structure' language,
e.g., the array language (`A[i]`, `++i`).

    | Data Structure   | Iter Implementation            |   Abstraction                   | Algorithms   |
    | ---------------- | ------------------------------ | ------------------------------- | ------------ |
    | Array            | ArrayIter (`A[i]`,`++i`)       | Iter (`i.get()`,`i.advance()`)  | `equals`     |
    | LinkedList       | ListIter (`n.data`,`n=n.next`) |                                 | `find_first` |
    | DoublyLinkedList | DLIter   (`n.data`,`n=n.next`) |                                 | `max`        |

Using the `Sequence` and `Iter` interfaces, we can implement 1 version
of `equals` that does the same job as all 9 specific versions! (And
many more!)

    public static <T, U> boolean equals(Sequence<T> s1, Sequence<U> s2, BiPredicate<T,U> eq) {
        Iter<T> j = s2.begin();
        for (Iter<T> i = s1.begin(); ! i.equals(s1.end()); i.advance()) {
            if (j.equals(s2.end()) || ! eq.test(i.get(), j.get()))
                    return false;
            j.advance();
        }
        return j.equals(s2.end());
    }


    LinkedList<Integer> L = ...
    ArrayList<Integer> A = ...
    equals(L, A)

Student in-class exercise: implement a `max` algorithm that returns the maximum
element of a `Sequence`.

    public static <T> T max(Sequence<T> s, T zero, BiPredicate<T, T> less);

Here's what you need to know about `BiPredicate`:

    interface BiPredicate {
	    boolean test(T t, U u);
	}
    
Student in-class exercise: implement Sequence using an array


Now the algorithms on `Sequence` (`find_first_equal`, `equals`, etc.)  can
also be used with Array!

    Array<Integer> B = new Array<Integer>();
    for (int i = 0; i != 20; ++i) {
      B[i] = i;
    }
    Iter<Integer> i = find_first_equal(B, 10);
    assert i.get() == 10;

    Iter<Integer> i = find_first_equal(B, 30);
    assert i == B.end();

The solutions to the in-class exercises are [here](./interfaces-solutions.md).

## More Interfaces

### Stack (LIFO)

analogy: stack of pancakes

	interface Stack<E> {
		void push(E d);
		E pop();
		E peek();
		boolean empty();
	}

Example uses: 

* reverse an array
* check for matching parentheses (with multiple kinds: square, round, curly)
* parsing (e.g. HTML)
* depth-search search
* procedure call stack

Implementation:

* array (push is add to the end, pop removes from end)
* singly-linked list (push on front, pop from front)

### Queue (FIFO)

Analogy: checkout line at a grocery store 

	interface Queue<E> {
		void enqueue(E e); // aka. push
		E dequeue(); // aka. pop
		E front();
		boolean empty();
	}

Example uses: 

* breadth-first search, 
* requests to a shared resource (e.g., printer),
* interupt handling inside an OS kernel, 
* buffering to handle asynchronous communiation, such as interprocess IO,
	disk access, etc.

Implementation: 

* array
* doubly-linked list

### Set

Like a set in mathematics. A collection of elements where the ordering
of the elements is not important, only membership matters.
`Set` ignores duplicates.

	interface Set {
	   void insert(int e);
	   void remove();
	   boolean member(int e);
	   boolean empty();
	   Set union(Set other);
	   Set intersect(Set other);
	   Set difference(Set other);
	}

Implementations of `Set` are the topic of several future lectures.
