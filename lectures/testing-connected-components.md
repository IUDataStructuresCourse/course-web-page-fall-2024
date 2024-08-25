# Testing Connected Components

    @Test
    public void testRandomGraphs() {
        for (int n = 1; n != 100; ++n) {
            UndirectedAdjacencyList G = generateRandomGraph(n);
            HashMap<Integer,Integer> rep = new HashMap<>();
            ConnectedComponents.connected_components(G, rep);
            
			// compare to alternative trusted implementation
            HashMap<Integer,Integer> good_rep = new HashMap<>();
			good_connected_components(G, good_rep);
			
			// assertEquals(rep, good_rep); no! solutions are not unique
			
			// all pairs of vertices
			for (Integer u : G.vertices()) {
			  for (Integer v : G.vertices()) {
				 // path from u to v iff rep[u] == rep[v]
				 if (good_rep[u] == good_rep[v]) {
				   assertTrue(rep[u] == rep[v])
				 } else {
				   assertFalse(rep[u] == rep[v])
				 }
			  }
			}
			
        }
    }

How do we check whether `connected_components` produced the correct output?
That is, whether `rep` is correct?





[Lab 6: Connected Components](https://iudatastructurescourse.github.io/course-web-page-spring-2024/lab6)


Student Solution:

	for (int i = 0; i < numVertices; i++) {
		if (!representative.containsKey(i)) {
			continue; // Skip if the vertex is not present in any component
		}
		int rep = representative.get(i);
		for (int j = 0; j < numVertices; j++) {
			if (representative.containsKey(j) && representative.get(j) == rep) {
				assertEquals(rep, representative.get(j).intValue());
			}
		}
	}

Student Solution:

	for (int j = 1; j < numVertices; j++) {
		for (int k = 0; k < j; k++) {
			if (g.hasEdge(j, k)) {
				assertEquals(rep.get(j), rep.get(k));
			}
		}
	}

Student Solution:

	Integer rep = representative.get(0);
	for (int i = 1; i < 1000; i++) {
		assertEquals(rep, representative.get(i));
	}

Instructor Solution:

    for (V u : G.vertices()) {
        for (V v : G.vertices()) {
            if (is_connected(u, v, G)) {
                assertTrue(rep.get(u).equals(rep.get(v)));
            } else {
                assertFalse(rep.get(u).equals(rep.get(v)));
            }
        }
    }

How to implemented `is_connected`?

* Breadth-first Search
* Union-find
* Depth-first Search (this is the lab, so don't choose this option for testing)

Here's the breadth-first version, a simple adaptation of the
code from the lecture notes on March 19.

    <V> boolean is_connected(V start, V end, Graph<V> G) {
        // BFS
        Queue<V> Q = new LinkedList<V>();
        HashSet<V> visited = new HashSet<>();
        Q.add(start);
        visited.add(start);
        while (! Q.isEmpty()) {
            V u = Q.remove();
            if (u.equals(end)) {
                return true;
            }
            for (V v : G.adjacent(u)) {
                if (! visited.contains(v)) {
                    Q.add(v);
                    visited.add(v);
                }
            }
        }
        return false;
    }
