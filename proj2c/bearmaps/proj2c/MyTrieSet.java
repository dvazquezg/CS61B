package bearmaps.proj2c;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Trie-based implementation of the TrieSet61B interface,
 * which represents a basic Trie.
 * @author Daniel Vazquez Guevara
 * @version 1.0
 */
public class MyTrieSet {

    private Node root; // root of inner tree

    /**
     * Creates a new instance of a MyTrieSet
     */
    public MyTrieSet() {
        root = new Node(); // creates an empty node
    }
    /**
     * Clears all items out of Trie
     */
    public void clear() {
        root = new Node();
    }

    /**
     * Returns true if the Trie contains KEY, false otherwise
     * @param key the key to be searched
     * @return
     */
    public boolean contains(String key) {
        if (key == null || key.length() < 1) {
            return false;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                return false; // fall off the tree
            } else {
                curr = curr.map.get(c); // mode tho next character node
            }
        }
        return curr.isKey; // returns whether or not the end of string is a key
    }

    /**
     * Inserts string KEY into Trie
     * @param key the key to be inserted
     */
    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(c, false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
    }

    /**
     * Returns a list of all words that start with PREFIX
     * @param prefix specific word prefix
     * @return
     */
    public List<String> keysWithPrefix(String prefix) {
        if (prefix == null || prefix.length() < 1) {
            return null;
        }
        Node curr = root;
        // place curr to the end of prefix
        for (int i = 0, n = prefix.length(); i < n; i++) {
            char c = prefix.charAt(i);
            if (!curr.map.containsKey(c)) {
                return null; // fall off the tree
            } else {
                curr = curr.map.get(c); // move to next node
            }
        }

        List<String> results = new ArrayList<>(); //list of results
        collectHelper(prefix, results, curr); // collect all keys from this node down
        return results;
    }

    /**
     * Collects all the entries from the trie into a list
     * @return list of all keys stored in trie
     */
    private List<String> collect() {
        List<String> results = new ArrayList<>();
        for (Character c : root.map.keySet()) {
            collectHelper(c.toString(), results, root.map.get(c));
        }
        return results;
    }

    /**
     * Recursive method to retrieve all keys from a given node
     * @param keyBuilder the string built so far
     * @param results the list of results to be fill
     * @param curr the node where the search will start
     */
    private void collectHelper(String keyBuilder, List<String> results, Node curr) {
        if (curr == null) {
            return;
        }
        // check if current node is a key
        if (curr.isKey) {
            results.add(keyBuilder); // add key to list of keys
        }
        // key looking for keys down this node
        for (Character c : curr.map.keySet()) {
            collectHelper(keyBuilder + c.toString(), results, curr.map.get(c));
        }
    }

    /**
     * Returns the longest prefix of KEY that exists in the Trie
     * Not required for Lab 9. If you don't implement this, throw an
     * UnsupportedOperationException.
     * @param key the word to be looked up
     * @return
     */
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException("Featured not implemented yet");
    }

    /**
     * Represents a node in the TrieSet
     */
    private class Node {
        private HashMap<Character, Node> map;
        private boolean isKey;
        /**
         * Default constructor (used for root instantiation)
         */
        Node() {
            map = new HashMap<>();
        }

        /**
         * Creates a new node for every character of string in trie
         * @param c the character to be added
         * @param isKey indicates if this node is a string
         */
        Node(char  c, boolean isKey) {
            map = new HashMap<>();
            this.isKey = isKey;
        }
    }
}
