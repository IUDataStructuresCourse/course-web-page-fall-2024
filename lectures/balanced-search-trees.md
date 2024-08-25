# Lecture: Balanced Binary Search Trees

Overview:

* Review BST Remove

* Segment Intersection Project

* Balanced Trees: AVL Trees

## Review `remove`  method of `BinarySearchTree`

Book 4.3.4.

* Case 1: no left child

```
          o              o
          |              |
        z=o              A
           \       ==>
            A
```

* Case 2: no right child

```
            o            o
            |            |
          z=o            A
           /       ==>
          A
```

* Case 3: two children

```
             |
           z=o
            / \
           A   B
```

The main idea is to replace z with the node after z, which is the
first node y in subtree B. We then recursively delete y from B.


Solution for `remove()` (similar to book Figure 4.25):

```java
    public void remove(K key) {
        root = remove_helper(root, key);
    }

    private Node remove_helper(Node<K> curr, K key) {
        if (curr == null) {
            return null;
        } else if (lessThan.test(key, curr.data)) { // remove in left subtree
            curr.left = remove_helper(curr.left, key);
            return curr;
        } else if (lessThan.test(curr.data, key)) { // remove in right subtree
            curr.right = remove_helper(curr.right, key);
            return curr;
        } else {      // remove this node
            if (curr.left == null) {
                return curr.right;
            } else if (curr.right == null) {
                return curr.left;
            } else {   // two children, replace with first of right subtree
                Node<K> min = curr.right.first();
                curr.data = min.data;
                curr.right = remove_helper(curr.right, min.data);
                return curr;
            }
        }
    }
```

What is the time complexity? $O(h)$, where $h$ is the height.

## Introduce the Segment Intersection Project. Demo the solution.

Given a set of n segments, are their any pairs that intersect?

Suppose we have a routine for testing whether 2 segments intersect.

Simplifications:

* No vertical segments
* No three-way (or more) intersections

Brute force: test all combinations O(n²)

A better algorithm: Line Sweep

* Sort all the end-points of the line segments from left to
  right (x-axis)
* Move the line sweep from left to right, stopping at each end point.
* Maintain a BST of all the segments that intersect the sweep
  line, sorted by where they cross the sweep line when they
  are first added to the BST.
* We maintain the invariant that each segment in the BST does not
  intersect with the next and previous segments in the BST.
* When you add a segment to the BST, check whether it intersects with
  the next and previous segments in the BST. If it does, stop.
* When you remove a segment from the BST, check whether the
  segment before it intersects with the segment after it.

We'll need fast membership testing, insert, remove, and next/previous.

## Motivation for balanced BSTs

Recall that search time is O(h), where h is the height of the tree.

Definition of height

    int compute_height(Node n) {
        if (n == null) {
            return -1;
        } else {
            int hl = compute_height(n.left);
            int hr = compute_height(n.right);
            return 1 + Math.max(hl, hr);
        }
    }

Example tree with heights in brackets:

                     41[3]
                   /       \
                20[2]       65[1]
               /    \        /
             11[0]  29[1] 50[0]
              /
            26[0]

The problem of unbalanced trees

                    o
                     \
                      o
                       \
                        o
                         \
                          o
                           \
                            o
                             \
                              o
                               \
                                o

height = n

vs.

                          o
                         / \
                        /   \
                       o     o
                      / \   / \
                     o   o o   o

height = log(n)


**Definition** A tree is *balanced* if its height is O(log n)
where n is the number of nodes in the tree.

Equivalently, the number of nodes is Ω(2ʰ) where h is the height.


## AVL Trees (Adelson-Velskii and Landis, 1962)

**Definition** The AVL Invariant: the height of two child subtrees may
only differ by 1, for every node in the tree.

Examples of trees that are AVL:

              o               o          o         o
             /               / \        / \       / \
            o               o   o      o   o     o   o
                                          /     /     \
                                         o     o       o

Examples of trees that are **not** AVL:

        o      o              o
       /        \            / \
      o          o          o   o
       \          \        / \
        o          o      o   o
                               \
                                o

Red-black trees are an alternative:
AVL is faster on lookup than red-black trees but slower on
insertion and removal because AVL is more rigidly balanced.

## Does the AVL invariant ensure that the tree is balanced?

Let N(h) represent the minimum number of nodes in an AVL tree of
height h. (The least-balanced possible scenario.)

    N(h) = N(h-1) + N(h-2) + 1

We want to show that

    h ≲ log₂ N(h)

To simplify, we have
    
    N(h-2) + N(h-2) + 1 < N(h-1) + N(h-2) + 1 = N(h)
    2·N(h-2) + 1 < N(h)
    2·N(h-2) < N(h)
    = 2·2·N(h-4)
    = 2·2·2·N(h-6)
    ...
    = 2^(h/2)       < N(h)

Take the log of both sides

    log₂ 2^(h/2) < log₂ N(h)
                                  (log₂ Aᴮ = B log₂ A)
    h/2 · log₂ 2 < log₂ N(h)
                                  (log₂ 2 = 1, i.e. 2¹ = 2)
    h/2 · 1      < log₂ N(h)
                                  (multiply both side by 2) 
    h            < 2 · log₂ N(h)

so we have

    h ≲ log₂ N(h)



