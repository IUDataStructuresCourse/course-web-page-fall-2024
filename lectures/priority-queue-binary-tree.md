## From last time: Heaps

Recall from last time that a heap is a complete binary tree that is
organized so that they key of each node is greater-or-equal to it's
children.

            __16__
           /      \
         14        10
        /  \      /  \
       /    \    /    \
      8      7  9      3
     / \    /
    2   4  1

Heaps are represented efficiently as an array.
    
        0  1  2  3  4  5  6  7  8  9 
    A=[16,14,10, 8, 7, 9, 3, 2, 4, 1]
    
Overview of the Heap operations

* `build_max_heap` creates a heap from an unordered array in O(n).
* `maximum` returns maximum element in O(1).
* `extract_max` removes the max in O(log(n)).
* `sortInPlace` sorts an array in-place in O(n  log(n)).
* `increase_key` updates the key of an element in the heap in O(log(n)).
* `insert` adds a new element to a heap in O(log(n)).
	
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

## Code Review: Binary Trees with Next/Prev

### Node class with methods: next, first, last, etc.

    class Node {
        T data;
        Node left, right, parent;
        ...
        public Node next() {
            if (this.right != null) {
                return this.right.first();
            } else {
                return this.nextAncestor();
            }
        }
        
        public Node first() {
            Node node = this;
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }

        public Node nextAncestor() {
            Node node = this;
            while (node.parent != null && node == node.parent.right) {
                node = node.parent;
            }
            return node.parent;
        }
        ...
    }

### Iter class

    class Iter implements Iterator<T>, ReverseIterator<T> {
        private Node curr;

        Iter(Node c) {
            curr = c;
        }

        public String toString() {
            if (curr == null) return "null";
            else return curr.toString();
        }

        @Override
        public T get() {
            return curr.data;
        }

        @Override
        public void retreat() {
            curr = curr.previous();
        }

        @Override
        public void advance() {
            curr = curr.next();
        }

        @Override
        public boolean equals(Object other) {
            Iter iter = (Iter) other;
            return this.curr == iter.curr;
        }

        @Override
        public Iter clone() {
            return new Iter(curr);
        }
    }

## Testing rbegin and rend

	public class BinaryTreeTest {

		BinaryTree<Integer> T;

		@BeforeEach
		public void setUp() throws Exception {
			ArrayList<Integer> A = new ArrayList<>(Arrays.asList(1, 3, 4, 8, 9, 21, 32));
			T = new BinaryTree<>(A);

		}
		@Test
		public void test() {
			t2();
		}
		public void t2() {
			ArrayList<Integer> rexpected = new ArrayList<>(Arrays.asList(32, 21, 9, 8, 4,3,1));
			ArrayList<Integer> reverseReal = new ArrayList<>();
			for (BinaryTree<Integer>.Iter riter = T.rbegin(); 
			     ! riter.equals(T.rend());
				 riter.retreat()){
				reverseReal.add(riter.get());
			}
			assertEquals(rexpected,reverseReal);}
		}
    }
