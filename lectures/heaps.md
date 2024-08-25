# Lecture: Heaps and Priority Queues

Motivations:

 1. (Priority Queue) Need a data structure that returns the
    maximum-keyed element of a set. The keys are the priorities.

    We could implement a priority queue using an AVL tree, but we
    want to be more space efficient.

 2. Want to do in-place sorting in O(n log(n)).

 3. Want to be able to change the key (priority) associated with an element.

## Heaps

A **complete binary tree** is a binary tree in which every level, except
possibly the last, is filled. Here are some examples:

        o         _o_            _o_          o
	   / \       /   \          /   \        /
	  o   o     o     o        o     o      o
	           / \   / \      / \
		      o   o o   o    o   o

Non example

              o
		    /   \
		   o     o
		  /     /
	     o     o 


Def. A **max heap** is a complete binary tree in which for every node
n, its key is less or equal to its parent's key (except for the root):

    key(n) ≤ key(parent(n))

This is called the **heap property** or max-heap property.

(Not to be confused with the use of "heap" to mean a computer's main
 memory.)

Def. A **min heap** is similar, except that each node's key is greater
or equal to its parent's key.

Here is an example of a max heap. 
    
            __16__
           /      \
         14        10
        /  \      /  \
       /    \    /    \
      8      7  9      3
     / \    /
    2   4  1

Note that the maximum element must be the root, which makes this data
structure good for implementing a priority queue.

## Array Representation

The choice of using complete binary trees is to enable a
space-efficient representation.

We store the elements in an array left-to-right, one level at a time,
top-to-bottom.

              0  1  2  3  4  5  6  7  8  9 
          A=[16,14,10, 8, 7, 9, 3, 2, 4, 1]

Note that the root of the tree is stored at A[0] and for a node stored
at index i, the left child is stored at index 2i+1 and the right child
is stored at index 2(i+1).

    left(i) = 2*i + 1

    right(i) = 2*(i + 1)

    parent(i) =  floor( (i-1) / 2 )


Overview of the Heap operations

* `build_max_heap` creates a heap from an unordered array in O(n).
* `maximum` returns maximum element in O(1).
* `extract_max` removes the max in O(log(n)).
* `sortInPlace` sorts an array in-place in O(n  log(n)).
* `increase_key` updates the key of an element in the heap in O(log(n)).
* `insert` adds a new element to a heap in O(log(n)).

Heap class:

    public class Heap<E> {
            ArrayList<E> data;
            BiPredicate<E,E> lesseq;
            ...
    }
    
## `max_heapify` helper function

Many of the heap operations need to turn an array that is almost a max
heap, except for one element, into a max heap. The `max_heapify`
operation moves the element at position i into the correct position.

The tree rooted at i is not a max heap, but the subtrees
left(i) and right(i) are max heaps.

Example: 4 is less than one of its children so we swap it with
the larger child and repeat.

             ___16___
            /        \
          *4*         10
         /  \        /  \
        /    \      9    3
       14     7
      / \    /
     2   8  1

becomes

             ___16___
            /        \
          14          10
         /  \        /  \
        /    \      9    3
      *4*     7
      / \    /
     2   8  1

becomes

             ___16___
            /        \
          14          10
         /  \        /  \
        /    \      9    3
       8      7
      / \    /
     2  *4* 1

Java declaration for `max_heapify`:

    void max_heapify(int i, int heap_length);

What is the time complexity of max_heapify? O(log(n))
  
## `build_max_heap` method

    void build_max_heap() {
        int last_parent = data.size() / 2 - 1;
        for (int i = last_parent; i != -1; --i) {
            max_heapify(i, data.size());
        }
    }

Why does this procedure work? What is the loop invariant?
Answer: the invariant is that the trees rooted at
  positions from i+1 to the end are max heaps.

What is the time complexity?
Answer: O(n log(n)) is the easy answer, but not tight. 

The tight upper bound is O(n) which can be obtained by
observing that the time for `max_heapify` depends on the
height of the node, and `build_max_heap` calls `max_heapify
many times on nodes with a low height. We can obtain
a tighter bound by more accurately counting the time.

Consider how many nodes there can be in an n-element heap at each
height. The worst cast is a complete tree. For example,

         n = 7

          _o_        height 2, 1 node
         /   \
        o     o      height 1, 2 nodes
       / \   / \
      o   o o   o    height 0, 4 nodes

    num. nodes at height 2 =  1 = ⌈ n / 8 ⌉  =  ⌈ n / 2^(2+1) ⌉
    num. nodes at height 1 =  2 = ⌈ n / 4 ⌉  =  ⌈ n / 2^(1+1) ⌉
    num. nodes at height 0 =  4 = ⌈ n / 2 ⌉  =  ⌈ n / 2^(0+1) ⌉

In general, 

    num. nodes at hight h ≤ ⌈ n / 2^(h+1) ⌉

So we can sum these up, from `h=0` to `log(n)`, with `O(h)` cost for each:

               log(n)
    time(n) =  ∑      h × ⌈ n / 2^(h+1) ⌉
               h=0
    
Pull out the `n`, multiply the right-hand side by 2 (change `h+1` to `h`),
and remove the ceiling.

                   log(n)
    time(n) ≤  n × ∑      h / 2^h
                   h=0

Reorganize the `h / 2^h`

                   log(n)
    time(n) ≤  n × ∑      h × (1/2)^h        (1)
                   h=0

Next we focus on simplifying the summation.
Recall formula A.8.

    ∞
    ∑   k × x^k    =   x / (1 - x)², for |x| < 1           (A.8)
    k=0

Let `x = 1/2` and exchange `k` for `h` in A.8 to get the following.

    ∞
    ∑   h × (1/2)^h    =   (1/2) / (1 - (1/2))²   =  (1/2) / (1/4)  =  2
    h=0
   
Note that

    log(n)                      ∞
    ∑      h × (1/2)^h    ≤    ∑   h × (1/2)^h   =  2
    h=0                         h=0

So with inequation (1) we have
   
    time(n) ≤  n × 2

So the time complexity of `build_max_heap` is O(n).


## `maximum` method

    public E maximum() {
        return data.get(0);
    }
    
## `extract_max` method

Idea: first record the max element, which is at `data[0]`, then we
need to delete that element. But deleting the first element of an
array is expensive. So we move the last element of the heap to
`data[0]`, shrink the array, and re-establish the max heap
property with `max_heapify`.

    E extract_max() {
        E max = data.get(0);                   // O(1)
        data.set(0, data.get(data.size()-1));  // O(1)
        data.remove(data.size()-1);            // O(1)  (because at the end of the array)
        max_heapify(0, data.size());           // O(log(n))
        return max;                            // O(1)
    }
    
Time complexity: O(log n)
	
## `sortInPlace` method

Idea: swap the max (the root) with the last element, call `max_heapify`, 
then shrink the heap by 1.
Similar to `extract_max` but does a swap instead of move.

    static <E> void sortInPlace(ArrayList<E> A, 
                                BiPredicate<E,E> lessThanOrEqual)
    {
        Heap<E> H = new Heap<E>(lessThanOrEqual);    // O(1)
        H.data = A;           // O(1)
        H.build_max_heap();   // O(n)
        for (int i  = H.data.size() - 1; i != 0; --i) { // n iterations * O(log(n)) = O(n log(n))
            swap(H.data, 0, i);   // O(1)
            H.max_heapify(0, i);  // O(log(n))
        }
    }

Time complexity:
The for loop executes n times, and max_heapify is O(log(n)),
so we have O(n log(n)).

## `increase_key` method (used by insert)

The key of the object at position i has increased.
How should we move it to get a valid heap?
Example: Change the key of 9 to 20.

         ___16___
        /        \
      14          10
     /  \        /  \
    8    7     *9*    3

## Student exercise: come up with the algorithm for `increase_key`

Answer: the idea is to propagate the element up. Continuing the above
example,

         ___16___
        /        \
      14          10
     /  \        /  \
    8    7    *20*   3

becomes

         ___16___
        /        \
      14         *20*
     /  \        /  \
    8    7      10    3

becomes

         __*20*__
        /        \
      14          16
     /  \        /  \
    8    7      10    3

## `insert` method

    void insert(E k) {
        data.add(k);                   // O(1)
        increase_key(data.size() - 1); // O(log(n))
    }

## Priority Queues

- implement using a `Heap`
- the priorities are the keys
- `push`: implement with `insert`
- `pop`: implement with `extract_max`

In Java:

    class PriorityQueue<E> {
        Heap<E> heap;
        PriorityQueue(BiPredicate<E,E> lessThanOrEqual) {
            heap = new Heap<>(lessThanOrEqual);
        }
        void push(E key) {
            heap.insert(key);
        }
        E pop() {
            return heap.extract_max();
        }
    }
