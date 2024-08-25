%.png: %.dot
	dot $< -Tpng > $@

%.pdf: %.dot
	dot $< -Tpdf > $@

%.html: %.md
	pandoc --mathml $< -o $@

DOT = digraph1.dot digraph2.dot \
    digraph3.dot digraph4.dot digraph5.dot \
	digraph6.dot digraph7.dot digraph8.dot digraph9.dot digraph10.dot \
	digraph11.dot digraph12.dot digraph13.dot digraph14.dot \
	graph1.dot graph2.dot graph3.dot graph4.dot graph5.dot digraph15.dot


PNG = $(DOT:%.dot=%.png)

PDF = $(DOT:%.dot=%.pdf)

graphs: ${PNG}

all: graphs countingsort bubblesort activity_selection quiz2 SCC

countingsort:
	~/bin/lmt lecture-notes.md
	javac src/CountingSort.java
	cd src ; java -ea -cp . CountingSort

bubblesort:
	~/bin/lmt lecture-notes.md
	javac src/BubbleSort.java
	cd src ; java -ea -cp . BubbleSort

activity_selection:
	~/bin/lmt lecture-notes.md
	javac src/ActivitySelection.java
	cd src ; java -ea -cp . ActivitySelection

quiz2:
	~/bin/lmt lecture-notes.md
	javac src/Quiz2.java
	cd src ; java -ea -cp . Quiz2

SCC:
	~/bin/lmt lecture-notes.md
	javac src/StronglyConnectedComponents.java
	cd src ; java -ea -cp . StronglyConnectedComponents

BFS:
	~/bin/lmt lecture-notes.md
	javac src/BreadthFirstSearch.java
	cd src ; java -ea -cp . BreadthFirstSearch

BellmanFord:
	~/bin/lmt lecture-notes.md
	javac src/BellmanFord.java
	cd src ; java -ea -cp . BellmanFord
