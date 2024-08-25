## Review: AVL Invariant

Recall the definition of the AVL invariant

**Definition** The AVL Invariant: the height of two child subtrees may
only differ by 1, for every node in the tree.

## How can we maintain the AVL invariant during insert? (remove is similar)

1. Do the normal BST insert.

2. Fix the AVL property if needed.

    We may need to fix problems along the entire path
    from the point of insertion on up to the root.

Example insertion and rebalancing:

                 41
               /    \
             20      \
           /    \     65
          11     29  /
                /   50
              26

          insert(23) ==>

                 41
               /    \
             20      \
           /    \     65
          11     29  /
                /   50
              26
             /
           23

Node 29 breaks the AVL invariant.

### Tree Rotation

                    y                         x
                   / \    right_rotate(y)    / \
                  x   C  --------------->   A   y
                 / \     <-------------        / \
                A   B     left_rotate(x)      B   C

Rotations preserve the BST property and the in-order ordering.

    A x B y C  =  A x B y C

Insert example: let's use rotation to fix up our insert(23) example:

                       29
                      /    right_rotate(29)
                    26     ---------------->    26
                   /                           /  \
                 23                          23    29

However, in different situations, the way in which we use tree
rotation is different. So let's look at more situations.

### Insert example: insert(55)

                  41
                /    \
              20      65
             /  \     /
            11   29  50
                 /
               26

So 65 breaks the AVL invariant, and we have a zig-zag:

               65
              /
            50
              \
               55

A right rotation at 65 gives us a zag-zig, we're not making progress!

              65(y)                        50(x)
             /        right_rotate(65)       \
            50(x)     ---------------->      65(y)
              \                              /
               55(B)                       55(B)

Instead, let's try a left rotate at 50:

              65                           65
             /        left_rotate(50)     /
            50(x)     --------------->  55(y)
              \                         /
               55(y)                 50(x)

This looks familiar, now we can rotate right. 

                  65(y)                        55(x)
                 /      right_rotate(65)       /  \
               55(x)    --------------->    50(A)  65(y)
              /
            50(A)


### Another Insert Example

      _6[2]_
     /      \
    3[0]     8[1]
             /  \
          7[0]   10[0]
    
Insert 11:

      _6[3]_
     /      \
    3[0]     8[2]
             /  \
          7[0]   10[1]
         			 \
		         	  11[0]


### Student question

starting with an empty AVL tree, insert 

    14, 17, 11, 7, 4, 53, 13, 12, 8
        
Solution:

after insert 14, 17, 11, 7:

                       14
                      /  \
                    11    17
                   /
                  7

insert 4:

                       14
                      /  \
                    11    17
                   /
                  7
                 /
                4

Node 11 doesn't satisfy AVL.

rotate_right(11)

                    14
                   /  \
                  7    17
                 / \
                4   11

insert 54, 13, 12:

                       14
                      /  \
                     7    17
                    / \     \
                   4   11    54
                        \
                         13
                        /
                      12

Node 11 doesn't satisfy AVL.

        rotate_right(13)

                   11
                    \
                     12
                       \
                       13

        rotate_left(11)

                       14
                      /  \
                     7    17
                    / \    \
                   4   12   54
                      /  \
                     11   13

insert 8:

                           14
                          /  \
                         7    17
                        / \    \
                       4   12   54
                          /  \
                         11   13
                        /
                       8

Node 7 doesn't satisfy AVL. There's a zig-zag. 

        rotate_right(12)

                           14
                          /  \
                         7    17
                        / \    \
                       4   11   54
                          /  \
                         8    12
                               \
                               13

        left_rotate(7)

                           14
                          /  \
                         11   17
                        /  \    \
                       7   12   54
                      / \    \
                     4   8   13



## Example: Remove Node and fix AVL

Given the following AVL Tree, delete the node with key 8.
(This example has two nodes that end up violating the AVL
property.)

             8
           /   \
          5     10
         / \   / \
        2   6 9   11
       / \   \      \
      1   3   7      12
           \
            4
    
Solution: 
* Step 1: replace node 8 with node 9

               9
             /   \
            5     10
           / \     \
          2   6     11
         / \   \      \
        1   3   7      12
             \
              4

* Step 2: find lowest node that breaks the AVL property: node 10
* Step 3: rotate 10 left

               9
             /   \
            5      11
           / \    /  \
          2   6  10   12
         / \   \
        1   3   7
             \
              4

* Step 4: find lowest node that breaks the AVL property: node 9
* Step 5: rotate 9 right

              5
            /   \
           /     \
          2        9
         / \     /   \
        1   3   6     11
             \   \   /  \
              4   7 10   12


## Algorithm for fixing AVL property

add or remove using the BST algorithm: O(log n)

number of iterations of the below "repeat"? O(log n)
how much time per iteration (fix step): O(1)
O(log n) * O(1) = O(log n)

total for add/remove and rebalance: O(log n) +  O(log n) = O(log n)

Starting from the lowest changed node, repeat the following up to the root of
the tree (because there can be several AVL violations).
* check whether node x is AVL, if not do the following. O(1) (assuming we store the height of node)
* if height(x.left) ≤ height(x.right)    O(1)

    1. if height(x.right.left) ≤ height(x.right.right)

        let k = height(x.right.right)

                    x k+2                                y ≤k+2
                   / \          left_rotate(x)          / \
               ≤k A   y k+1     ===============>  ≤k+1 x   C k
                     / \                              / \
                ≤k B   C k                      ≤k A   B ≤k

    2. if height(x.right.left) > height(x.right.right)

        let k = height(x.right.left)

              k+2 x                               y k+1
                 / \                            /   \
            k-1 A   z k+1    R(z), L(x)      k x     z k
                   / \      =============>    / \   / \
                k y   D k-1              k-1 A   B C   D k-1
                 / \
                B   C <k

* if height(x.left) > height(x.right)

    1. if height(x.left.left) < height(x.left.right)  (note: strictly less!)

        let k = height(x.left.right)

                    x k+2                                 z k+1
                   / \                                  /   \
              k+1 y   D k-1      L(y), R(x)          k y     x k
                 / \            =============>        / \   / \
            k-1 A   z k                              A   B C   D    <k
                   / \
                  B   C <k

    2. if height(x.left.left) ≥ height(x.left.right)  (note: greater-equal!)

        let k = height(l.left.left)

                  x k+2                                  y k+1
                 / \         right_rotate(x)            / \
            k+1 y   C k-1    ===============>        k A   x k+1
               / \                                        / \
            k A   B ≤k                                ≤k B   C k-1


## Time Complexity of Insert and Remove for AVL Tree

O(h) = O(log n)

## Using AVL Trees for sorting

* insert n items: O(n log(n))

* in-order traversal: O(n)

* overall time complexity: O(n log(n))


## ADT's that can be implemented by AVL Trees

* priority queue:
  insert, delete, min
  
* ordered set:
  insert, delete, min, max, next, previous
  

