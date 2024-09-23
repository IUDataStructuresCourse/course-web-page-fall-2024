# Example 1

```
public static void flood(WaterColor color,
                        LinkedList<Coord> flooded_list,
                        Tile[][] tiles, Integer board_size) {
    ArrayList<Coord> flooded_array = new ArrayList<>(flooded_list);
    for (int i = 0; i != flooded_array.size(); ++i) {
        Coord currentCoord = flooded_array.get(i);
        for (Coord neighbor : currentCoord.neighbors(board_size)) {
            boolean alreadyFlooded = false;
            for (Coord floodedCoord : flooded_list) {
                if (floodedCoord.equals(neighbor)) {
                    alreadyFlooded = true;
                    break;
                }
            }
            if (!alreadyFlooded && tiles[neighbor.getY()][neighbor.getX()].getColor().equals(color)) {
                flooded_array.add(neighbor);
                flooded_list.add(neighbor);
            }
        }
    }
}
```

# Example 2


```
public static void flood(WaterColor color,
                          LinkedList<Coord> flooded_list,
                          Tile[][] tiles,
                          Integer board_size) {
    LinkedList<Coord> coordsToCheck = new LinkedList<>(flooded_list);
    while (!coordsToCheck.isEmpty()) {
        Coord current = coordsToCheck.removeFirst();
        List<Coord> neighbors = current.neighbors(board_size);
        for (Coord neighbor : neighbors) {
            if (!flooded_list.contains(neighbor) &&
                    tiles[neighbor.getY()][neighbor.getX()].getColor() == color) {
                coordsToCheck.add(neighbor);
                flooded_list.add(neighbor);
            }
        }
    }
```


# Example 3

```
public static void flood(WaterColor color,
                         LinkedList<Coord> flooded_list,
                         Tile[][] tiles,
                         Integer board_size) {
    Set<Coord> visited = new HashSet<>(flooded_list);
    Deque<Coord> deque = new ArrayDeque<>(flooded_list);
    while (!deque.isEmpty()) {
        Coord current = deque.poll();
        for (Coord neighbor : current.neighbors(board_size)) {
            if (visited.add(neighbor) && tiles[neighbor.getY()][neighbor.getX()].getColor() == color) {
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
