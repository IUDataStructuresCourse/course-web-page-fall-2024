# Lab: Huffman Coding

## Background and Overview

The lab is to finish a `Heap` class

## Support Code and Submission

+ Student support code is at [link](https://github.com/IUDataStructuresCourse/HuffmanCoding-student-support-code/).
+ Submit your code file `Heap.java` [link](https://autograder.luddy.indiana.edu/web/project/1310).
+ Submit your test file `StudentTest.java` [link](https://autograder.luddy.indiana.edu/web/project/1317).


## Problem Set

### Problem 1: Heap Implementation

  #### Heap(Comparator<E> comparator) 

  
  
  #### peek()

  Returns the top of this heap. This will be the highest priority key.
  
  @throws NoSuchElementException if the heap is empty.


  #### insert(E key) 


  
  #### delete() 

  Removes and returns the highest priority key in this heap.

  @throws NoSuchElementException if the heap is empty.
  
  #### swap(int i, int j) 

  Exchanges the elements in the heap at the given indices in keys.
  
  #### size()

  Returns the number of keys in this heap.
  
  #### getLeft(int p)
  
  Returns the index of the left child of p.

  #### getRight(int p)

  Returns the index of the right child of p.
  
  #### getParent(int p) 
  
  Returns the index of the parent of p.

### Problem 2: Testing

Write test cases in `StudentTest.java` with one method named `test()`.
This method should thoroughly test the `Heap` class.

Test and debug your own code from Problem 1 locally and then submit both your code
and your test cases to Autograder for grading.

The Autograder will apply your tests to buggy implementations of the
`Heap` class. You receive 1 point for each bug detected.
The Autograder will also apply your tests to a correct implementation
to rule out false positives.

-----------------

* You have reached the end of the lab. Yay!
