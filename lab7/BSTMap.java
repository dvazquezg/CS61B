import java.util.Iterator;
import java.util.Set;

/**
 *  Class that represents a BSTMap which implements the Map61B interface
 *  using a BST (Binary Search Tree) as its core data structure.
 * @author Daniel Vazquez Guevara
 * @param <K> data type of key
 * @param <V> data type of value
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root; // the root of tree

    /**
     * Represents a Node object of the BSTMap
     */
    private class Node {
        private K key;    // the key of node (ID)
        private V value;  // the node's data
        private Node left, right; //subtrees
        private int size; // number of nodes in subtree

        Node(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }

    BSTMap() {

    }

    /**
     * Removes all of the mappings from this map.
     */
    @Override
    public void clear() {
        root = null;
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     * @param key the key to be found
     * @return true if key is found, false otherwise
     */
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key to be searched is null.");
        }
        return get(root, key) != null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key to be retrieved is null.");
        }
        return get(root, key);
    }

    /**
     * Returns the Value for the specified key, if it exists.
     * @param node the node where the search starts
     * @param key the key to be found
     * @return true if key is found, false otherwise
     */
    private V get(Node node, K key) {
        if (node == null) {
            return null;
        }
        // check if key is smaller, greater or equals to current key
        int comparison = key.compareTo(node.key);
        // decide insertion location
        if (comparison < 0) {
            return get(node.left, key);
        } else if (comparison > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    @Override
    public int size() {
        if (root == null) {
            return  0;
        }
        return root.size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("No null elements allowed.");
        }
        if (value == null) {
            remove(key);
            return;
        }
        root = put(root, key, value);
    }

    private Node put(Node node, K key, V value) {
        if (node == null) {
            return new Node(key, value, 1);
        }
        // check if key is smaller, greater or equals to current key
        int comparison = key.compareTo(node.key);
        // decide insertion location
        if (comparison < 0) {
            node.left = put(node.left, key, value);
        } else if (comparison > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }
        node.size += 1;

        return node;
    }

    /**
     * Returns the number of key-value mappings in the subtree of current node
     * @param node the node
     * @return
     */
    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        return node.size;
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Not available feature.");
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("Not available feature.");
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("Not available feature.");
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException("Not available feature.");
    }

    /**
     * Prints BSTMap in order of increasing Key.
     */
    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node node) {
        if (node != null) {
            printInOrder(node.left);
            System.out.println("{" + node.key.toString() + ": " + node.value.toString() + "}");
            printInOrder(node.right);
        }
    }
}
