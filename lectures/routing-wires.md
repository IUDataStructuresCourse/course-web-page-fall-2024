# Code Review for Routing Wires

## Process goals in order, try BFS to connect each start and end point. (8/11)

```
public class Routing {
    public static ArrayList<Wire> findPaths(Board board, ArrayList<Endpoints> goals) {
        ArrayList<Wire> paths = new ArrayList<>();
        for (Endpoints point : goals) {
			List<Coord> path = bfs(board, point.start, point.end);
			
			if (path == null)
			  return null;
		    paths.add(new Wire(point.id, path));
			markPath(board, path, point.id);
	
			if (path != null) {
				paths.add(new Wire(point.id, path));
				markPath(board, path, point.id);
			}
        }
        return paths;
    }
    private static List<Coord> bfs(Board board, Coord start, Coord end) {
        Queue<Coord> queue = new LinkedList<>();
        Map<Coord, Coord> parentMap = new HashMap<>();
        Set<Coord> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            Coord current = queue.poll();
            if (current.equals(end))
                return reconstructPath(parentMap, current);
            for (Coord neighbor : board.adj(current)) {
                boolean isEnd = neighbor.equals(end);
                boolean isPassable = !board.isObstacle(neighbor) && (!board.isOccupied(neighbor) || isEnd);
                if (!visited.contains(neighbor) && isPassable) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                }
            }
        }
        return null;
    }
    private static List<Coord> reconstructPath(Map<Coord, Coord> parentMap, Coord current) {
        List<Coord> path = new ArrayList<>();
        Coord step = current;
		// O(n^2)
        while (step != null && parentMap.containsKey(step)) { // containsKey is O(1), O(n) iterations
            path.add(0, step);   // add is O(n) !
            step = parentMap.get(step);
        }
        if (step != null) {  // Add the initial step if it's not null (should be the start)
            path.add(0, step);
        }
        return path;
    }
    private static void markPath(Board board, List<Coord> path, int id) {
        for (Coord coord : path) {
            if (!board.isOccupied(coord) || coord.equals(path.get(path.size() - 1))) {
                board.putValue(coord, id);
            }
        }
    }
}
```

## If BFS fails on a goal, remove all other wires, place this one, and start over. (11/11)

```
public static ArrayList<Wire> findPaths(Board board, ArrayList<Endpoints> goals) {
	ArrayList<Wire> paths = new ArrayList<>();
	Set<Integer> sol = new HashSet<>();
	int i = 0;
	while (i < goals.size()) {
		if(sol.contains(i)) {
			i++;
			continue;
		}
		Endpoints goal = goals.get(i);
		Coord start = goal.start;
		Coord end = goal.end;
		ArrayList<Coord> route = bfs(board, start, end, goal.id);
		if (route != null) {
			Wire wire = new Wire(goal.id, route);
			updateBoard(board, route, goal);
			paths.add(wire);
			// didn't add to sol?
			i++;
		} else {
			for (Wire curr : paths) {
				board.removeWire(curr);
				// remove them from the sol
			}
			paths.clear();
			ArrayList<Coord> newRoute = bfs(board, start, end, goal.id);
			Wire wire = new Wire(goal.id, newRoute);
			updateBoard(board, newRoute, goal);
			paths.add(wire);
			sol.add(i);
			i = 0;
		}
	}
	return paths;
}
```

## Backtracking (11/11)

```
public static ArrayList<Wire>
findPaths(Board board, ArrayList<Endpoints> goals) {
	if (goals.isEmpty())
		return null;
	ArrayList<Endpoints> needsRouted = (ArrayList<Endpoints>)goals.clone();
	ArrayList<Wire> paths = new ArrayList<>();
	backtrack(board, goals, needsRouted, paths);
	return paths;
}
private static void
backtrack(Board board, ArrayList<Endpoints> goals, ArrayList<Endpoints> needsRouted, ArrayList<Wire> paths){
	for (Endpoints goal: goals) {
		if (needsRouted.contains(goal)) { // O(n), use a HashSet instead
			Coord start = goal.start;
			Coord end = goal.end;
			ArrayList<Coord> path = bfs(board, start, end);
			if (path!=null) {
				Wire wire = new Wire(goal.id, path);
				board.placeWire(wire);
				needsRouted.remove(goal);
				paths.add(wire);
				backtrack(board, goals, needsRouted, paths);
				if (needsRouted.isEmpty()) {
					return ;
				}
				board.removeWire(wire);
				needsRouted.add(goal);
				paths.remove(wire);
			}
		}
	}
}

```

## Try starting at different places in the goals array (10/11)

```
public static ArrayList<Wire>
findPaths(Board board, ArrayList<Endpoints> goals) {
	ArrayList<Wire> wires = new ArrayList<>();
	int startingIndex = 0;
	int currentIndex = 0;
	int goalsProcessed = 0;
	while (goalsProcessed < goals.size()) {
		Endpoints goal = goals.get(currentIndex);
		ArrayList<Coord> path = BuildWire(board, goal);
		if (path != null) {
			Wire wire = new Wire(goal.id, path);
			wires.add(wire);
			board.placeWire(wire);
			currentIndex++;
			if (currentIndex == goals.size())
				currentIndex = 0;
			goalsProcessed++;
		} else {
			for(int i = 0; i < wires.size(); i++) {
				board.removeWire(wires.get(i));
				wires.remove(i);
			}
			goalsProcessed = 0;
			startingIndex++;
			currentIndex = startingIndex;
		}
	}
	return wires;
}
```

## Succeeded, mysteriously (11/11)

```
public static ArrayList<Wire> findPaths(Board board, ArrayList<Endpoints> goals) {
	ArrayList<Endpoints> ordered = orderGoals(goals);
	ArrayList<Wire> paths = new ArrayList<>();
	if (findPathsRecursive(board, ordered, 0, paths)) {
		return paths;
	} else {
		return new ArrayList<>();
	}
}
private static ArrayList<Endpoints> orderGoals(ArrayList<Endpoints> goals) {
	ArrayList<Endpoints> high = new ArrayList<>();
	ArrayList<Endpoints> low = new ArrayList<>();
	for (Endpoints goal : goals) {
		if (goal.id == 1) {
			high.add(goal);
		} else {
			low.add(goal);
		}
	}
	low.addAll(high);
	return low;
}
private static boolean findPathsRecursive(Board board, ArrayList<Endpoints> goals, int index, ArrayList<Wire> paths) {
	if (index == goals.size()) {
		return true;
	}
	Endpoints goal = goals.get(index);
	Map<Coord, Coord> previous = new HashMap<>();
	if (bfsHelper(board, goal, previous)) {
		ArrayList<Coord> path = getPathCoordinates(previous, goal);
		Wire newWire = new Wire(goal.id, path);
		paths.add(newWire);
		board.placeWire(newWire);
		if (findPathsRecursive(board, goals, index + 1, paths)) {
			return true;
		} else {
			board.removeWire(newWire);
			paths.remove(paths.size() - 1);
		}
	}
	return false;
}
```

Passes all the autograder tests, but fails on this board:

	|  0   2   0   0   0   0 |
	|  1   0   0   0   2   0 |
	| -1   0   0   0   0   0 |
	|  0   0   0   1   0   0 |

