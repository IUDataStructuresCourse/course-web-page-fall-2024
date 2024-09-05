
# Quick Reverse, Accumulator-Passing Style

The `reverse` function in `List.pf` is `O(n²)` time because it invokes
append (`operator ++`) `n` times and append is `O(n)`. Define a
function named `quick_rev` that reverses the input list and that is
`O(n)` time. Use `assert` statements to test that your `quick_rev`
function really reverses the input.

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





<!--
```{.deduce file=LabDeduceProg.pf}
import List
import Nat

<<sum_list>>
<<sum_accum>>
<<sum_accum_test>>

```
-->
