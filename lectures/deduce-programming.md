# Introduction to Deduce

The Deduce system has two parts: a programming language and a proof
language. The user of Deduce writes some functions in the Deduce
programming language and then writes proofs in the Deduce proof
language about the correctness of their funnctions. The Deduce system
checks that the proofs are correct.

In this lecture we introduce the Deduce programming language.  
After this lecture, we recommend that you work through the tutorial at
the following URL, and complete the exercises at the bottom of that
web page.

[https://github.com/jsiek/deduce/blob/main/FunctionalProgramming.md](https://github.com/jsiek/deduce/blob/main/FunctionalProgramming.md)


# Programming in Deduce

The main way to represent data in Deduce is with `union` types. 

## A Fruitful Example

The following statement defines a union type named `Fruit` and it
defines three kinds of fruit: apples, oranges, and bananas.

```
union Fruit {
  apple
  orange
  banana
}
```

We can create fruit values by mentioning one of the alternatives.  The
following statements put an apple into variable `f1` and a banana into
`f2`.

```
define f1 : Fruit = apple
define f2 : Fruit = banana
```

Of course, we all know that bananas tend to go rotten quickly, so we
should keep track of that information. We can accomplish that by
adding a parameter of type `bool` to the `banana` alternative:

```
union Fruit {
  apple
  orange
  banana(bool)
}
```

and now when we create a banana, we have to say whether it is rotten
or not.

```
define f3 : Fruit = banana(false)
define f4 : Fruit = banana(true)
```

We can inspect and dispatch on values of union type using Deduce's
`switch` statement. The following statement inspects the fruit `f4`
and figures out whether it is rotten or not. In the case for `banana`,
Deduce initializes the `r` variable to `true` (because `f4` is
`banana(true)`).

```
define r4 : bool = 
    switch f4 {
      case apple { false }
      case orange { false }
      case banana(r) { r }
    }
```

## Linked Lists via unions

A **linked list** is a data structure that represents a sequence of
elements.  Each element is stored inside a node and each node also
stores a link to the next node, or to the special empty value that
signifies the end of the list. The following is a linked list
that represents the sequence `7, 4, 5`.

```
   ___      ___      ___
  | 7 |--> | 4 |--> | 5 |-/
  -----    -----    -----
```


In Deduce we can define the type of linked lists of natural numbers
with the following `union` type. (`Nat` is the type of natural numbers
and is defined in `Nat.pf`.)

```
union NatList {
  Empty
  Node(Nat, NatList)
}
```

Then we can create the linked list for `7, 4, 5` with the following
statement that creates three nodes.

```
define L = Node(7, Node(4, Node(5, Empty)))
```

## Recursive Functions: List Length

The `length` function, defined below returns the number of elements in
a linked list. The length of an empty list is `0` and the length of a
list that starts with a node is one more than the length of the list
starting at the next node.

```
function len(NatList) -> Nat {
  len(Empty) = 0
  len(Node(n, next)) = 1 + len(next)
}
```

The length of list `L` defined above is `3`.

```
assert len(L) = 3
```

Recursive functions in Deduce are somewhat special to make sure they
always terminate.

* The first parameter of the function must be a union.
* The function definition must include a clause for every
  constructor in the union.
* The first argument of every recursive call must be a sub-part of the
  current constructor of the union.


## Generic Unions

One often needs to create lists with other kinds of elements, not just
`Nat`.  Thus, Deduce supports generic unions. Here is the generic
`List` type defined in `List.pf`.

```
union List<T> {
  empty
  node(T, List<T>)
}
```

For example, the sequence of numbers `1, 2, 3` is represented
by the following linked list.

```
define list_123 : List<Nat> = node(1, node(2, node(3, empty)))
```

## Generic Functions

```
function length<E>(List<E>) -> Nat {
  length(empty) = 0
  length(node(n, next)) = 1 + length(next)
}
```

## Import

The `import` declaration makes available the contents of another
Deduce file in the current file. For example, you can import the
contents of `Nat.pf` as follows

```
import Nat
```

## Printing Values

You can ask Deduce to print a value to standard output using the
`print` statement.

```
print five
```

The output is `5`.


## Functions (λ)

Functions are created with a λ expression.  Their syntax starts with
λ, followed by parameter names, then the body of the function enclosed
in braces.  For example, the following defines a function for
computing the area of a rectangle.

```
define area : fn Nat,Nat -> Nat = λ h, w { h * w }
```

The type of a function starts with `fn`, followed by the
parameter types, then `->`, and finally the return type.

To call a function, apply it to the appropriate number and type of
arguments.

```
print area(3, 4)
```

The output is `12`.

## Functions Returning Functions: nth element of list

The `nth` function retrieves the element at position `i` in list
`xs`. However, if `i` is greater or equal to the length of `xs`, then
`nth` returns the default value `d`.

```
function nth<T>(List<T>, T) -> (fn Nat -> T) {
  nth(empty, d) = λ i { d }
  nth(node(x, xs), d) = λ i {
    if i = 0 then
      x
    else
      nth(xs, d)(pred(i))
  }
}
```

(The `pred(n)` function is short for predecessor and computes `n - 1`,
except that `pred(0) = 0`.)

Here are examples of applying `nth` to the list `1, 2, 3`,
using `0` as the default value.

```
assert nth(list_123, 0)(0) = 1
assert nth(list_123, 0)(1) = 2
assert nth(list_123, 0)(2) = 3
assert nth(list_123, 0)(3) = 0
```

We formulated the `nth` operation in an unusual way. It has two
parameters and returns a function of one parameter that returns an
element. We could have instead made `nth` take three parameters and
directly return an element. We made this design choice because it
means we can use `nth` with several other functions and theorems that
work with functions of the type `fn Nat -> T`.
