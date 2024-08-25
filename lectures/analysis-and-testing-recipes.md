
Lecture Overview

* Recipes for time complexity analysis
* Testing checklist


# Time Complexity Analysis Recipes

## Simple Statements

* Most are O(1)
* Some can be more expensive: method and function calls, object constructors.
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
        for (int i = 0; i != flooded_list.size(); ++i) { ... }  // O(n^2)
    }

The construction of the `flooded` HashSet is O(n).
The `for` loop is O(n^2).
Adding those together yields O(n^2).


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

So the time of the `if-then-else` is O(1) + O(n) + O(n) = O(n)

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

    int i = n;
    while (i > 0) {
        for (int j = 0; j < n; j++)
            System.out.println("*");
        i = i / 2;
    }

number of iterations of `while` loop is O(log(n))
time of body of `while` is the sum of
  time of `for` loop: ?
  the statement `i = i / 2;`: O(1)

number of iterations of `for` loop is O(n)
time of body of `for` is O(1)
time of `for` loop is O(n) * O(1) = O(n)

time of `while` loop is O(log(n)) * O(n) = O(n log(n))


## Recursive Functions

Recipe:

    time_of_function = recursion_depth * time_per_level

To determine `recursion_depth`, analyze how much the input size changes
in the recursive calls.
* If size gets smaller by 1 (or any constant amount), recursion depth is O(n).
* If size get divided in half (or more), recursion depth is O(log n).

To determine the `time_per_level`:

    time_per_level = number_calls_per_level (not big-O) * time_per_call

To determine `number_calls` occuring within each level, one needs to
analyze the code to see how many recursive calls can be spawned by one
call to the recursive function.

To determine `time_per_call`, analyze the time complexity of one call
to the recursive function, **ignoring the recursive calls**.

One complication is that the number of calls in each level often
changes (e.g. doubling at each level: 1, 2, 4, 8, ..., 2^d) and
the time_per_call also changes because the input size gets smaller
(e.g. cut in half at each level: n, n/2, n/4, ..., 1),
but in many examples, these two affects cancel out and the
time per level stays the same. (If not, one needs to use a more
advanced analysis called the Master Theorem.)

Example:

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
    
* `time_per_call` = O(1)  (allocate one node)

* `time_per_level` = `number_calls_per_level` * `time_per_call` = O(1)

* `time_of_function` = `recursion_depth` * `time_per_level` = O(n) * O(1) = O(n)

Example:

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

Example:

    static Node merge_sort(Node N) {
        if (N == null || N.next == null) {
            return N;
        } else {
            int n = Utils.length(N);
            Node left = merge_sort(Utils.take(N, n / 2));
            Node right = merge_sort(Utils.drop(N, n / 2));
            return merge(left, right);
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


# Testing Checklist

1. common cases (find in lecture, textbook, internet)
2. corner cases
3. code coverage -> more cases
4. big, randomized input

Let's go through some of the autograder's tests for the
`MergeSortList` and `NextPrevBinaryTree` labs.

## Common Case Test for `merge`

    @Test
    public void merge_small() {
        int[] A = {1,3,4,6,8,8,12,13};
        int[] B = {2,4,5,7,9,11,14};
        Node N1 = Utils.array_to_list(A);
        Node N2 = Utils.array_to_list(B);
        Node M = MergeSort.merge(N1, N2);
        assertArrayEquals(A, Utils.list_to_array(N1));
        assertArrayEquals(B, Utils.list_to_array(N2));
        assertTrue(Utils.is_sorted(M));
        assertTrue(Utils.is_permutation(M, Utils.append(N1, N2)));
    }

## Common Case Test for `merge_in_place`

    @Test
    public void merge_in_place() {
        int[] A = {1,3,4,6,8,8,12,13};
        int[] B = {2,4,5,7,9,11,14};
        Node N1 = Utils.array_to_list(A);
        Node N2 = Utils.array_to_list(B);
        Node M = MergeSort.merge_in_place(N1, N2);
        assertTrue(Utils.is_sorted(M));
        assertTrue(Utils.is_permutation(M, Utils.append(Utils.array_to_list(A),
                                                 Utils.array_to_list(B))));
        // Check that the merge was done in place.
        assertTrue(Utils.equals(M, N1));
    }

## Big, Randomized Test for merge `sort`

    @Test
    public void sort_big() {
        Random r = new Random(0);
        for (int n = 0; n != 100; ++n) {
            int[] A = new int[n];
            for (int i = 0; i != n; ++i)
                A[i] = r.nextInt(50);
            Node N = Utils.array_to_list(A);
            Arrays.sort(A);
            N = MergeSort.sort(N);
            int[] B = Utils.list_to_array(N);
            assertArrayEquals(A, B);
        }
    }

## Common and Big Tests for BinaryTree Iterator

    private void test_next_helper(ArrayList<Integer> expected,
                                  BinaryTree<Integer> T) {
        int j = 0;
        for (Iterator<Integer> i = T.begin(); !i.equals(T.end()); i.advance()) {
            assertTrue(j != expected.size()); // to catch error in Iter.equals
            assertEquals(expected.get(j).intValue(), i.get().intValue());
            ++j;
        }
    }

    public void test_advance() throws Exception {
        Integer expected[] = {2, 5, 5, 6, 7, 8};
        test_next_helper(new ArrayList<Integer>(Arrays.asList(expected)), T);
    }

    @Test
    public void test_advance_big() throws Exception {
        Random r = new Random(0);
        for (int n = 0; n != 100; ++n) {
            ArrayList<Integer> expected = new ArrayList<>();
            for (int i = 0; i != n; ++i)
                expected.add(r.nextInt(100));
            BinaryTree<Integer> bigT = new BinaryTree<>(expected);
            test_next_helper(expected, bigT);
        }
    }
