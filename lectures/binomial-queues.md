# Lecture: Binomial Queues

Overview:
1. Binomial Trees and Heaps
2. Binomial Forests and Priority Queues

## Binomial Trees and Heaps

Recall the binomial coefficient, n choose k, written 

    ( n )
    ( k )

which is the number of ways to choose k elements from a set of size n.
For example, there are three ways to choose 2 elements from a set of
size 3. For {a,b,c} we have {a,b}, {a,c}, {b,c}.  In general, we have:

    ( n )  =    n! / ( k!(n - k)! )
    ( k )


    ( 3 )  = 3! / ( 2!(3 - 2)! ) = 6 / 2 = 3
    ( 2 )

The name "binomial" comes from the Binomial Theorem, which describes
the expansion of powers of a binomial:

                n
    (x + y)ⁿ  = Σ   ( n ) * xⁿ⁻ᵏ * yᵏ
	            k=0 ( k )

For example,

    (x + y)⁴ = x⁴ + 4 x³ y + 6 x² y² + 4 x y³ + y⁴

Recall Pascal's Triangle
    
                   0=k  (diagonals)
    n=0          1 / 1
              ----/-- /
      1        1 / 1 / 2
            ----/---/-- /
      2      1 / 2 / 1 / 3
           ---/---/---/-- /
      3    1 / 3 / 3 / 1 / 4
         ---/---/---/---/--
      4  1 / 4 / 6 / 4 / 1 
    

From the triangle it is easy to see that each cell is the sum of
the two cells diagonally above it:

    ( n ) = ( n-1 ) + ( n-1 )
    ( k )   ( k-1 )   ( k   )


Def. A **binomial tree** Bₙ is a tree whose root has n children, where
the first child is Bₙ₋₁, the second is Bₙ₋₂, ..., on down to
the last child, which is B₀.

     Bₒ   B₁   B₂    B₃
     o    o    o     o---\
          |    |\    | \  \
          Bₒ   B₁ Bₒ  B₂ B₁ B₀

Consider the number of nodes at each depth within a binomial tree.

    depth
    0    o 1  o 1  o   1     _o    1     B4   1
              |    |\      _/ |\
    1         o 1  o o 2  o   o o  3          4
                   |      |\  |
    2              o   1  o o o    3          6
                          |
    3                     o        1          4
	
	4                                         1

So the name binomial tree comes from there being n choose k nodes at
depth k of tree Bn.

Each binomial tree Bn can be formed by taking two trees of B{n-1}
and putting one of them as a child of the other's root.

    B₂                B₃
    o                _o
    |\      B₂     _/ |\
    o o  ∪  o  =  o   o o
    |       |\    |\  |
    o       o o   o o o
            |     |
            o     o

Turning them on their side and aligning by depth:

    1     1 1       1 2 1
    +
      1     1 1       1 2 1
    =
    1 1   1 2 1     1 3 3 1

The height of a binomial tree Bₙ is n.  binomial tree Bₙ has 2ⁿ
nodes.  So height = log nodes. It's balanced!

Student exercise: draw B₄. how many nodes does it have?  How many
nodes at each depth?  Solution:

    depth
    0    o---\--\    1
         | \  \  \
    1    o   o  o  o 4
         |   |  |
    2    ooo oo o    6
         |   |
    3    ooo o       4
         |
    4    o           1


Def. A **binomial heap** Bn is a binomial tree where each node has a
key and they satisfy the max-heap property.

    class BinomialHeap<K> {
        K key;
        int height;
        PList<BinomialHeap<K>> children;
        BiPredicate<K, K> lessEq;
        ...
    }   

The `PList` class is for **persistent lists**. 

In general, a data structure is called **persistent** if it can
provide efficient access to older versions of the data structure.

The `PList` class represents lists in a way that you can add an
element to the list but still access the old list, prior to the
addition. The following is an exerpt from the `PList` class.

    class PList<T> {
        T data;
        final PList<T> next;
        PList(T d, PList<T> nxt) { data = d; next = nxt; }

        public static <T> PList<T> addFront(T first, PList<T> rest);
        public static <T> T getFirst(PList<T> n);
        public static <T> PList<T> getNext(PList<T> n);
        ...
    }   

The `addFront` method returns a new `PList` with the given `first`
element and whose subsequent elements are the same as the `rest`.

The `getFirst` method returns the first element of the list.

The `getNext` method returns the list as it was before the first
element was added.

Note that the `addFront` and `getNext` methods do not mutate (make
changes) to any `PList` objects.

The `PList` class includes several more methods that come in handy.


## Binomial Forests and Queues

Def. A **binomial forest** is a collection of binomial trees.

We can implement a priority queue with a forest of binomial heaps.
It's a forest to allow for numbers of nodes that are not powers
of 2. 

We'll store the forest in order of *increasing* height and we will not
allow two trees of the same height.  The forest is represented as a
persistent list.

    public class BinomialQueue<K> {
        PList<BinomialHeap<K>> forest;
        BiPredicate<K,K> lessEq;
        ...
    }

Binomial queues support an efficient union operation, as well as
insert and extract_max. To accomplish the union operation, we'll need
to know how to merge two binomial forests, which in turn will need a
way to link two binomial trees of the same height into one tree.

If two binomial trees have the same height, linking them is easy.
Just make one of the trees the first child of the other.  Pick the one
with the larger key to be on top to maintain the max heap property.

    Bₖ  ∪  Bₖ =  Bₖ₊₁
    B₂  ∪  B₂ =  B₃

    o       o      o______
    |\      |\     |   \  \
    o o  ∪  o o =  o    o  o
    |       |      |\   |
    o       o      o o  o
                   |
                   o

    // @precondition this.height == other.height
    BinomialHeap<K> link(BinomialHeap<K> other) {
        if (lessEq.test(other.key, this.key)) {
            PList<BinomialHeap<K>> kids = PList.addFront(other, this.children);
            return new BinomialHeap<>(this.key, this.height + 1, kids);
        } else {
            PList<BinomialHeap<K>> kids = PList.addFront(this, other.children);
            return new BinomialHeap<>(other.key, other.height + 1, kids);
        }
    }

Now, when merging two binomial forests, we'll also need a function
that inserts a single tree into a forest.  This is like the algorithm
for long-hand binary addition where each k is a digit.

### Insert Operation

Insert tree

    o
    |
    o

into forest

    B₀ B₁ B₂
    o  o  o
       |  |\
       o  o o
          |
          o

First we link the two B₁'s:

    o   o   o
    | ∪ | = |\
    o   o   o o
            |
            o

and we continue with the insert, which forces us to link the new B₂
with the other B₂ to get B₃ (see above), so we get the forest:

    B₀ B₃


You will implement the `insert` method in lab.

    static <K extends Comparable<K>> PList<BinomialHeap<K>>
    insert(BinomialHeap<K> n, PList<BinomialHeap<K>> ns);

### push

In the `BinomialQueue`, we use `insert` to implement `push` as follows.

    public void push(K key) {
        BinomialHeap<K> heap = new BinomialHeap<>(key, 0, null, lessEq);
        this.forest = insert(heap, this.forest);
    }

### Merge Operation

The merge function takes two binomial forests, ordered by increasing
height, and turns them into a single forest.  This operation
can be implemented with a couple different algorithms.
One of them is analogous to the merge of merge sort.
The other algorithm is analogous to addition of binary numbers.

Examples:

    merge [B₀ B₂] with [B₁ B₄] = [B₀,B₁,B₂,B₄],
    merge [B₀ B₁] with [B₁ B₄] = [B₀ B₂ B₄].

You will implement `merge` in lab.

    PList<BinomialHeap<K>>
    merge(PList<BinomialHeap<K>> ns1, PList<BinomialHeap<K>> ns2);

### pop

1. In the forest, find the tree with the largest key
2. Remove that tree, but merge all of its children into the forest.
   This requires reversing the children so that they are
   ordered by increasing height.

Here's the code:

    public K pop() {
        BinomialHeap<K> max = PList.find_max(this.forest);
        this.forest = PList.remove(max, this.forest);
        PList<BinomialHeap<K>> kids = PList.reverse(max.children, null);
        this.forest = merge(this.forest, kids);
        return max.key;
    }

