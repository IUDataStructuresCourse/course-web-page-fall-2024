## Solution for in-class exercise

```java
    public Node<K> insert(K key) {
        Node<K> n = find(key, root, null);
        if (n == null){
            root = new Node<K>(key, null, null);
            return root;
        } else if (lessThan.test(key, n.data)) {
            n.left = new Node<K>(key, null, null);
            return n.left;
        }  else if (lessThan.test(n.data, key)) {
            n.right = new Node<K>(key, null, null);
            return n.right;
        } else
            return null;
    }
```
