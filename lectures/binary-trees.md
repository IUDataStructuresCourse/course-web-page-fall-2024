# Binary Trees

## Motivation 

Taking stock with respect to Flood-it! and the Set interface

|                  |  array | linked list | sorted array |
| ---------------- |:------:|:-----------:|:------------:|
| membership test  | O(n)   | O(n)        | O(log(n))    |
| insertion        | O(1)   | O(1)        | O(n)         |

* How do we keep the array sorted when inserting more elements?

	Use binary search for insertion.

	What's the time complexity?

	O(n) because we still have to shift elements down.

* We'd like something that can do both membership test and insertion in
  O(lg n).

* Can we create a hybrid of the sorted array and the linked list?

	Yes: binary search trees!

* This lecture we discuss binary trees as a lead-up to discussing
  binary search trees in the next lecture.

## BinaryNode and BinaryTree Classes

	class BinaryNode<T> {
		T data;
		BinaryNode<T> left;
		BinaryNode<T> right;

		BinaryNode(T d, BinaryNode<T> l, BinaryNode<T> r) {
				data = d; left = l; right = r;
		}
	}

	public class BinaryTree<T> {
		BinaryNode<T> root;

		public BinaryTree() { root = null; }
		// ...
	}

We can traverse a binary tree in several different ways:
* preorder: me, left, right
* inorder: left, me, right
* postorder: left, right, me

preorder(node) {
   output node.data
   preorder(node.left)
   preorder(node.right)
}

inorder(node) {
   inorder(node.left)
   output node.data
   inorder(node.right)
}

postorder(node) {
   postorder(node.left)
   postorder(node.right)
   output node.data
}

Example:

                 10
                / \
               /   \
              5     14
             / \     \
            2   7     19

            pre:  10,5,2,7,14,19
            in:   2,5,7,10,14,19
            post: 2,7,5,19,14,10

Recursion Tree written as an "outline"
* pre(Node(10))
  * me: output 10
  * left: pre(Node(5)
    * me: output 5
	* left: pre(Node(2))
	  * me: output 2
	  * left: null
	  * right: null
	* right: pre(Node(7))
	  * me: output 7
	  * left: null
	  * right: null
  * right: pre(Node(14)
    * me: output 14
	* left: null
	* right: pre(Node(19))
	  * me: output 19
	  * left: null
	  * right: null

## Next and Previous operations with respect to inorder traversal

Running example binary tree:

          8
        /  \
       /    \
      3     10
     / \      \
    1   6      14
       / \    /
      4   7  13

inorder: 1, 3, 4, 6, 7, 8, 10, 13, 14

### Helper functions first and last

Find the first node according to an inorder traversal of the subtree.

Examples:

What's the first node in the above tree? answer: 1

What's the first node of the subtree rooted at 6? answer: 4

	public BinaryNode<T> first();
	
Algorithm: Go to the left as far as you can.

Find the last node according to an inorder traversal.

	public BinaryNode<T> last();

Algorithm: Go to the right as far as you can.

### Helper functions nextAncestor and prevAncestor

Given a node, find the ancestor that would come next
according to an inorder traversal, or return null
if there is none.

You may assume that each node has a `parent` attribute.

**Student in-class exercise**:

	BinaryNode<T> nextAncestor() {
	 if (this.parent == null) {
	   return null;
	 } else if (this.parent.right == this) {
	   return this.parent.nextAncestor();
	 } else if (this.parent.left == this) {
	   return this.parent;
	 }
	}
	
	
keep going up so long as the node is the right child, 
when left child, return parent
	
	

Solution:

	BinaryNode<T> nextAncestor() {
		if (parent != null && this == parent.right) {
			return parent.nextAncestor();
		} else {
			return parent;
		}
	}

Given a node, find the ancestor that would come before the node
according to an inorder traversal, or return null if there is
none.

	BinaryNode<T> prevAncestor();

### How to find the next node according to an inorder traversal?

Examples:

What is after 3? Answer: 4.

What is after 7? Answer: 8.

If their is a right child, get the first node in the subtree.
Otherwise, find the next ancestor.

	public BinaryNode<T> next() {
		if (right == null) {
		   return nextAncestor();
		} else {
		   return right.first();
		}
	}

What is the time complexity? answer: $O(h)$ where $h$ is
the height of the tree.

Finding the previous node is similar.
