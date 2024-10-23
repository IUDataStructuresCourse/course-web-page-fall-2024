# Time Complexity Analysis Recipes

## Simple Statements

* Most are O(1)
* Some can be more expensive: method and function calls, object constructors (`new`).
    1. If the method is a standard one (`get` on `LinkedList`), lookup up the
      time complexity.
    2. If you have access to the code for the method, analyze the code.
    3. If all else fails, do empirical tests that measure the time for
      different input sizes.

For many calls on the same line, add their time complexity. (See the next item.)

## Composing Statements Sequentially

Recipe:

    time_of_sequence = time_of_first + time_of_second + ...

Example:

    public static void flood(WaterColor color,
                            LinkedList<Coord> flooded_list,
                            Tile[][] tiles, Integer board_size) {
        HashSet<Coord> flooded = new HashSet<>(flooded_list);   // O(n)
        for (int i = 0; i != flooded_list.size(); ++i) { ... }  // O(n²)
    }

The construction of the `flooded` HashSet is O(n).
The `for` loop is O(n²).
Adding those together yields O(n²).


## If-Then-Else Statements

Recipe:

    time_of_if_then_else = time_of_condition + time_of_then + time_of_else

Example:

    static boolean is_rotated(int[] A_orig, int[] A_new) {
        if (A_orig.length < 2) {
            return Arrays.equals(A_orig, A_new);
        } else {
            boolean result = A_new[0] == A_orig[A_orig.length - 1];
            for (int i = 0; i != A_orig.length - 1; ++i) {
                result = result && (A_orig[i] == A_new[i + 1]);
            }
            return result;
        }
    }

The time of the condition `A_orig.length < 2` is O(1).
The time of the then-branch `Arrays.equals(A_orig, A_new)` is O(n).
The time of the else-branch is O(n) (we shall discuss loops below).

So the time of this `if-then-else` is O(1) + O(n) + O(n) = O(n)

## If-Then Statements

Recipe:

    time_of_if_then = time_of_condition + time_of_then

## Loops

Recipe:

    time_of_loop = number_iterations * time_of_body

Example:

    for (int i = 0; i != A_orig.length - 1; ++i) {
        result = result && (A_orig[i] == A_new[i + 1]);
    }

number of iterations is O(n)
time of body is O(1)
time of `for` loop is O(n) * O(1) = O(n)

Example (nested loops):

    int i = n; // O(1)
    while (i > 0) { // log n iterations * O(n) = O(n log n)
        for (int j = 0; j < n; j++)  // n iterations * O(1) = O(n)
            System.out.println("*"); // O(1)
        i = i / 2;                   // O(1)
    }

number of iterations of `while` loop is O(log(n))
time of body of `while` is the sum of
  time of `for` loop: ?
  the statement `i = i / 2;`: O(1)

number of iterations of `for` loop is O(n)
time of body of `for` is O(1)
time of `for` loop is O(n) * O(1) = O(n)

time of `while` loop is O(log(n)) * O(n) = O(n log(n))

total: O(n log(n))

## Recursive Functions

Recipe:

    time_of_function = recursion_depth * time_per_level

To determine `recursion_depth`, analyze how much the input size changes
in the recursive calls.
* If size gets smaller by 1 (or any constant amount), recursion depth is O(n).
* If size get divided in half (or any fraction), recursion depth is O(log n).

To determine the `time_per_level`:

    time_per_level = number_calls_per_level (not big-O) * time_per_call
    (do this for each level if the amount changes on each level)

To determine `number_calls_per_level`, one needs to analyze the code
to see how many recursive calls can be spawned by one call to the
recursive function.

To determine `time_per_call`, analyze the time complexity of one call
to the recursive function, **ignoring the recursive calls**.

One complication is that the number of calls in each level often
changes (e.g. doubling at each level: 1, 2, 4, 8, ..., 2^d) and
the time_per_call also changes because the input size gets smaller
(e.g. cut in half at each level: n, n/2, n/4, ..., 1),
but in many examples, these two affects cancel out and the
time per level stays the same. (If not, one needs to use a more
advanced analysis called the Master Theorem.)

### Append Example

    static Node append(Node N1, Node N2) {
        if (N1 == null)
            return N2;
        else {
            return new Node(N1.data, append(N1.next, N2));
        }
    }

analysis:

* `recursion_depth` = O(n), input size reduced by one: `append(N1.next, N2)`

* `number_calls_per_level` = 1 (only one call to append)

   append(L)
   |
   append(L.next)
   |
   append(L.next.next)


* `time_per_call` = O(1)  (allocate one node)

* `time_per_level` = `number_calls_per_level` * `time_per_call` = O(1)

* `time_of_function` = `recursion_depth` * `time_per_level` = O(n) * O(1) = O(n)

### Binary Search Tree Example

Assume the tree is AVL, so it is balanced.

    protected Node<K> find(K key, Node<K> curr, Node<K> parent) {
        if (curr == null)
            return parent;
        else if (lessThan.test(key, curr.data))
            return find(key, curr.left, curr);
        else if (lessThan.test(curr.data, key))
            return find(key, curr.right, curr);
        else
            return curr;
    }

analysis:

* `recursion_depth` = O(log(n)), input size cut in half:
   `find(key, curr.left, curr)`

* `number_calls_per_level` = 1 (only one call to `find`)
    
* `time_per_call` = O(1)  (assuming call to `test` is O(1))

* `time_per_level` = `number_calls_per_level` * `time_per_call` = O(1)

* `time_of_function` = `recursion_depth` * `time_per_level` 
   = O(log(n)) * O(1) = O(log(n))

### Merge Sort Example

    static Node merge(Node A, Node B) {
        if (A == null) {
            return B;
        } else if (B == null) {
            return A;
        } else if (A.data <= B.data) {
            return new Node(A.data, merge(A.next, B));
        } else {
            return new Node(B.data, merge(A, B.next));
        }
    }

    static Node merge_sort(Node N) {
        if (N == null || N.next == null) {
            return N;
        } else {
            int n = Utils.length(N); // O(n)
            Node left = merge_sort(Utils.take(N, n / 2)); // take: O(n)
            Node right = merge_sort(Utils.drop(N, n / 2)); // drop: O(n)
            return merge(left, right); // O(n)
        }
    }

analysis:

* `recursion_depth` = O(log(n)), input size cut in half:
   `merge_sort(Utils.take(N, n / 2)`

* `number_calls_per_level` = 1, 2, 4, 8, ... 
   doubling because there are 2 recursive calls to `merge_sort`
    
* `time_per_call` = n, n/2, n/4, n/8, ...
    (Utils.length + Utils.take + Utils.drop + merge)

* `time_per_level` = `number_calls_per_level` * `time_per_call` = O(n)

* `time_of_function` = `recursion_depth` * `time_per_level` 
   = O(log(n)) * O(n) = O(n log(n))

## Insertion Sort

    function insert(List<Nat>,Nat) -> List<Nat> {
      insert(empty, x) = node(x, empty)
      insert(node(y, next), x) =
        if x ≤ y then
          node(x, node(y, next))
        else
          node(y, insert(next, x))
    }

    function insertion_sort(List<Nat>) -> List<Nat> {
      insertion_sort(empty) = empty
      insertion_sort(node(x, xs')) = insert(insertion_sort(xs'), x)
    }


time = recursion depth * time per level
       n * 

time per level = # calls  *   time inside insertion_sort
                     1             O(n)

level 1:    insertion_sort(L)
level 2:    insertion_sort(L)
...
level n:    insertion_sort(L)
