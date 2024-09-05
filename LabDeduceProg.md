# Concatenating a List of Lists

Define a function named `concat` that turns a list-of-lists into a
list. The `concat` function should have the following type.

```
concat : < E > fn List<List<E>> -> List<E>
```

In general, you may use any functions in `List.pf`.
The following shows an example use of the `concat` function.

```
define L13 = node(1, node(2, node(3, empty)))
define L45 = node(4, node(5, empty))
define L15 = node(1, node(2, node(3, node(4, node(5, empty)))))
assert concat(node(L13, node(L45, empty))) = L15
```

Use this `assert` statement and several of your own to test whether
your `concat` function behaves as expected.


# Quick Reverse, Accumulator-Passing Style

The `reverse` function in `List.pf` is `O(n²)` time because it invokes
append (`operator ++`) `n` times and append is `O(n)`. Define a
function named `quick_rev` that reverses the input list and that is
`O(n)` time. The `quick_rev` function should be generic and have the
following type.

```
quick_rev : < E > fn List<E> -> List<E>
```

Use `assert` statements to test whether your `quick_rev` function
really reverses the input.

Hint: we recommend that you define an auxilliary function that is
written in accumulator-passing style.

Consider the following function that sums the elements of a list:

```{.deduce #sum_list}
function sum(List<Nat>) -> Nat {
  sum(empty) = 0
  sum(node(x, xs)) = x + sum(xs)
}
```

We can change to accumulator-passing style by adding an extra
parameter that stores the total-so-far.

```{.deduce #sum_accum}
function sum_accum(List<Nat>, Nat) -> Nat {
  sum_accum(empty, total) = total
  sum_accum(node(x, xs), total) = sum_accum(xs, x + total)
}
```

(One side benefit of accumulator passing style is that the function is
tail recursive, which means that it uses `O(1)` space on the procedure
call stack, whereas `sum` uses `O(n)` space.)

```{.deduce #sum_accum_test}
define L13 = node(1, node(2, node(3, empty)))
assert sum(L13) = sum_accum(L13, 0)
```

# Cumulative Sum of a List

The **cumulative sum** of a list of numbers produces a list where each
element is the sum of all the elements of the input list up to and
including the index of the current element.
So if the input list is 
```
n0, n1, n2, ...
```
the output list is
```
n0, n0+n1, n0+n1+n2, ...
```

Define a function named `cumulative_sum` that performs this operation.
The `cumulative_sum` function should have the following type.

```
cumulative_sum : List<Nat> -> List<Nat>
```

Test your `cumulative_sum` function with several `assert` statements.



<!--
```{.deduce file=LabDeduceProg.pf}
import List
import Nat

<<sum_list>>
<<sum_accum>>
<<sum_accum_test>>

```
-->
