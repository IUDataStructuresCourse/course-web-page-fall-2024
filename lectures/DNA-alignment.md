# DNA Sequence Alignment (aka. Edit Distance) via Dynamic Programming

Overview

* Recursive solution to DNA Sequence Alignment
* Memoization

## Introduction to DNA Sequence Alignment

A **strand** of DNA is a string of molecules called bases: one of adenine
(A), guanine (G), cytosine (C), and thymine (T). Biologists are
interested in comparing the DNA of different organisms and determining
how similar they are.

One way to measure how similar two strands of DNA is to see how well
they align. That is, we try to line up the two sequences such that we
have as many matching characters across from each other as
possible. We can insert **gaps** (written as underscores) in either
sequence to try to help them line up.

The similarity of two X and Y strands is the `score` of the best
alignment. The score is computed as follows, using an auxiliary
function called `match` to process the score for individual
characters.

	static int match(char c1, char c2) {
		if (c1 == '_' || c2 == '_') {
			return -1;
		} else if (c1 == c2) {
			return 2;
		} else {
			return -2;
		}
	}

	static int score(char[] X, char[] Y) {
		int total = 0;
		for (int i = 0; i != X.length; ++i) {
			total += match(X[i], Y[i]);
		}
		return total;
	}

Example alignment:

Inputs:

	GATCGGCAT
	CAATGTGAATC

An alignment: (Categories: `|` for match, `!` for mismatch, `.` for gap)

	GA_TCGGCA_T_
	!|.|.|!!|.|.
	CAAT_GTGAATC

    score = (5 × 2) + (3 × -2) + (4 × -1) = 0

A better alignment:

	_GA_TCG_GCA_T_
	..|.|.|.|.|.|.
	C_AAT_GTG_AATC

    score = (6 × 2) + (8 × -1) = 4

Another example alignment:
    
Inputs:

		GAATTCAGTTA
		GGATCGA

An alignment:

	GAATTCAGTTA
	|!|.||.|..|
	GGA_TC_G__A

	score = (6 × 2) + (1 × -2) + (4 × -1) = 6

Another alignment with the same score:

	G_AATTCAGTTA
	|..|.||.|..|
	GG_A_TC_G__A

	score = (6 × 2) + (0 × -2) + (6 × -1) = 6

## Edit Distance

We can view the alignment problem as computing *edit distance*,
that is, how many edits are required to turn the first string
into the second string. The edits can be one of the following:

* change a character to a different one (corresponds to a mismatch)

* insert a character (corresponds to a gap in the first string)

* delete a character (corresponds to a gap in the second string)


## Recursively enumerate all of the feasible solutions

We can choose among the following options for each location in the
alignment:
      
1. Take a character from the **end** of each string and line them up.

	Inputs:

		X = GAATTCAGTTA
		Y = GGATCGA

	Partial Output:

		A     rest of X = GAATTCAGTT
		|
		A     rest of Y = GGATCG

2. Take a character from the end of X and put a gap on the other side.
   We call this choice a **deletion** because, to get from X to Y,
   we deleted a character.

	Partial Output:

		A     rest of X = GAATTCAGTT

		_     rest of Y = GGATCGA

3. Insert a gap in the first string and take a character from
   the end of Y.  We call this choice an **insertion** because,
   to get from X to Y, we inserted a character.

	Partial Output:

		_     rest of X = GAATTCAGTTA

		A     rest of Y = GGATCG


For each choice, recursively process the rest of X and Y, finding the
score for the rest, then add in the score for the current choice.

Return the max of all the choices.

## Subproblem identification

Instead of using the entire rest of X and Y as the inputs to the
recursive function, we can simply use two integers, i and j, to mark
how far into X and Y we currently are, that is, which prefix of X and
Y correspond to the current subproblem.

## Memoization 

To memoize the results, we can use a 2D table indexed by i and j, the
length of the prefix of X and Y.

	T[0][0] = 0
	T[0][j] = j * -1      for j = 1...|Y|
	T[i][0] = i * -1      for i = 1...|X|
	T[i][j] = max(M,I,D)  for j = 1...|Y| and i = 1...|X|
			  where
			  M = score(X[i-1], Y[j-1]) + T[i-1][j-1]   // M for match/mismatch
			  I = T[i][j-1] - 1                         // I for insert
			  D = T[i-1][j] - 1                         // D for delete

    Example:

          Y =            G     G     A
          j =      0  |  1  |  2  |  3
              --------------------------
        X i = 0 |  0  |I:-1 |I:-2 |I: -3
          G   1 |D:-1 |M:2  |M:1
          A   2 |D:-2 |D:1  |
          A   3 |D:-3 | 

    Evaluating the 3 options for each cell in the table (each prefix of X and Y)
	i=1, j=1  (align "G" and "G")
	  M=+2+0=+2  (diagonal NW) *** winner! ***
	  I=-1-1=-2  (left)
	  D=-1-1=-2  (up)

	i=2, j=1  (align "GA" and "G")
	  M=-2-1=-3  (diagonal NW)
	  I=-1-2=-3  (left)
	  D=-1+2=+1  (up)    *** winner! ***

	i=1, j=2  (align "G" and "GG")
	  M=+2-1=+1     *** winner! ***
	  I=-1+2=+1
	  D=-1-2=-3


    Complete Solution:

          Y =            G     G     A
          j =      0  |  1  |  2  |  3
              --------------------------
        X i = 0 |  0  |I:-1 |I:-2 |I:-3
          G   1 |D:-1 |M:2  |M:1  |I:0
          A   2 |D:-2 |D:1  |M:0  |M:3
          A   3 |D:-3 |D:0  |I:-1 |D:2

    The alignment:

        X= _GAA
            ||
        Y= GGA_
           *++*

    score = 2

* Student Exercise

    Input:

        X = CAG
        Y = TCAT

             Y =       T     C     A     T
             j = 0  |  1  |  2  |  3  |  4
        X i=0 ------------------------------
              |  0  |  -1 |  -2 |  -3 |  -4 
          C   | -1  |  -2 |  1  |  0  |  -1 
          A   | -2  |  -3 |  0  |  3  |  2
          G   | -3  |  -4 |  -1 |  2  |  1

    One solution:

        _CA_G
         ||
        TCAT_
        *++**

    score = 1

* What's the time complexity? Answer: O(mn), where m is the length
  of the first string and n is the length of the second.

* What the space complexity? Answer: O(mn)
