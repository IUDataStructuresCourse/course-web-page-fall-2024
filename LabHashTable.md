# Lab: Hash Tables with Separate Chaining

## Background and Overview

The goal for this lab is to implement a hash table that uses separate
chaining. The `Map` interface in `Map.java` specifies
the methods that should be provided by your `HashTable` class.

## Student Support Code and Submission

+ Student support code is at [link](https://github.com/IUDataStructuresCourse/hash-table-student-support-code).
+ Submit your code file `HashTable.java` ([Problem 1](#problem-1-hash-table)) to
  [link](https://autograder.luddy.indiana.edu/web/project/1293).
+ Submit your test file `StudentTest.java` ([Problem 2](#problem-2-testing)) to
  [link](https://autograder.luddy.indiana.edu/web/project/1313).


## Problem Set

### Problem 1: Hash Table

The first thing you should do is complete the `hash` method. As you may recall, this should convert an object into an integer index into the hash table's array. Java provides a `hashCode()` method for every object, which returns an integer between `Integer.MIN_VALUE` and `Integer.MAX_VALUE`, which you should use for this step.


You will also complete the following methods in the `HashTable.java` file, implementing the `Map` interface. 

- `V get(K key) throws Exception`

    This method returns the value associated with the key,
    if there is one. Otherwise it throws an exception.

- `void put(K key, V value)`

    This method maps `key` to `value` in the table, overwriting any existing entry.
    A later call to `get` with this
    `key` should return this `value`.

- `void remove(K key)`

    This method removes any existing entry with the specified key in the table. A later call to `containsKey` with this `key` should return `false`.

- `boolean containsKey(K key)`

    This method returns `true` if there is an entry with the specified
    key in the hash table, and returns `false` otherwise.

You will also need to implement the constructor for `HashTable`, in
which you will need to initialize the table.  The constructor should
have one parameter, the initial table size:

   `public HashTable(int table_size);`
	
As usual, you may create any helper methods that you find useful.

### Problem 2: Testing

Write your test cases in `StudentTest.java` with the method named `test()`,
which should thoroughly test the hash table.

Test and debug your own code from Problem 1 locally and then submit both your code and your test cases to Autograder for grading.

The Autograder will apply your tests to buggy implementations of the `HashTable` class. You receive 1 point for each bug detected. The Autograder will also apply your tests to a correct implementation to rule out false positives.

-----------------

* You have reached the end of the lab. Hooray!