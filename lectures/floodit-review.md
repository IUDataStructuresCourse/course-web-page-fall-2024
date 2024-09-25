# Example 1

O(n^2)
```
public static void flood(WaterColor color,
                        LinkedList<Coord> flooded_list,
                        Tile[][] tiles, Integer board_size) {
    ArrayList<Coord> flooded_array = new ArrayList<>(flooded_list); // O(n)
    for (int i = 0; i != flooded_array.size(); ++i) { // n iterations
        Coord currentCoord = flooded_array.get(i);    // O(1)
        for (Coord neighbor : currentCoord.neighbors(board_size)) { // O(4) = O(1)
            boolean alreadyFlooded = flooded_list.contains(neighbor);
            
            boolean alreadyFlooded = false;
            for (Coord floodedCoord : flooded_list) { // O(n)
                if (floodedCoord.equals(neighbor)) {
                    alreadyFlooded = true;
                    break;
                }
            }
            if (!alreadyFlooded && tiles[neighbor.getY()][neighbor.getX()].getColor().equals(color)) {
                flooded_array.add(neighbor); // O(1)*
                flooded_list.add(neighbor);  // O(1)
            }
        }
    }
}
```

# Example 2

O(n^2)
```
public static void flood(WaterColor color,
                          LinkedList<Coord> flooded_list,
                          Tile[][] tiles,
                          Integer board_size) {
    LinkedList<Coord> coordsToCheck = new LinkedList<>(flooded_list);
    while (!coordsToCheck.isEmpty()) { // iterations: n
        Coord current = coordsToCheck.removeFirst();
        List<Coord> neighbors = current.neighbors(board_size);
        for (Coord neighbor : neighbors) { // iterations: 4
            if (!flooded_list.contains(neighbor) &&  // O(n)
                    tiles[neighbor.getY()][neighbor.getX()].getColor() == color) {
                coordsToCheck.add(neighbor);
                flooded_list.add(neighbor);
            }
        }
    }
```









# Example 3

O(n)
```
public static void flood(WaterColor color,
                         LinkedList<Coord> flooded_list,
                         Tile[][] tiles,
                         Integer board_size) {
    Set<Coord> visited = new HashSet<>(flooded_list);
    Deque<Coord> deque = new ArrayDeque<>(flooded_list);
    while (!deque.isEmpty()) { // n iterations
        Coord current = deque.poll();
        for (Coord neighbor : current.neighbors(board_size)) { // 4 iterations
            if (visited.add(neighbor) && tiles[neighbor.getY()][neighbor.getX()].getColor() == color) { // O(1)
                flooded_list.add(neighbor);
                deque.add(neighbor);
            }
        }
    }
}
```

# Time Complexity of Java collection operations

* LinkedList
    * add: O(1)
    * get: O(n)
    * contains: O(n)
    * remove: O(1)
    * size: O(1)

* ArrayList
    * add: O(1)*
    * get: O(1)
    * set: O(1)
    * contains: O(n)
    * remove: O(n)
    * size: O(1)

* HashSet

* HashMap

TreeSet  (Balanced Binary Search Tree)
    * contains: O(log(n))
    
TreeMap  (Balanced Binary Search Tree)
