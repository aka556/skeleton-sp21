package bstmap;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root;

    private class BSTNode {
        private K key;
        private V val;
        private BSTNode left;
        private BSTNode right;
        private int size;

        public BSTNode(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
       return getNode(root, key) != null;
    }

    /** Returns the key's node if it exists in the tree, null otherwise. */
    private BSTNode getNode(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return getNode(node.left, key);
        } else if (cmp > 0) {
            return getNode(node.right, key);
        } else {
            return node;
        }
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        BSTNode node = getNode(root, key);
        return node == null ? null : node.val;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return root == null ? 0 : root.size;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    /** Helper method to put a key-value pair into the BST. */
    private BSTNode put(BSTNode root, K key, V value) {
        if (root == null) {
            return new BSTNode(key, value, 1);
        }
        int cmp = key.compareTo(root.key);
        if (cmp < 0) {
            root.left = put(root.left, key, value);
            root.size += 1;
        } else if (cmp > 0) {
            root.right = put(root.right, key, value);
            root.size += 1;
        } else {
            root.val = value;
        }
        return root;
    }

    /** Now, we don't need to implement these methods. */
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    /** Implements the Iterable interface. */
    @Override
    public Iterator<K> iterator() throws UnsupportedOperationException {
        return new BSTMapIter();
    }

    public class BSTMapIter implements Iterator<K> {
        LinkedList<BSTNode> list;

        public BSTMapIter() {
            list = new LinkedList<>();
            list.addLast(root);
        }

        @Override
        public boolean hasNext() {
            return !list.isEmpty();
        }

        @Override
        public K next() {
            BSTNode node = list.removeFirst();
            list.addLast(node.left);
            list.addLast(node.right);
            return node.key;
        }
    }

    public void printInOrder() {
        printInOrder(root);
    }

    /** Print the BSTMap in order of increasing key. */
    private void printInOrder(BSTNode root) {
        if (root == null) {
            return;
        }
        printInOrder(root.left);
        System.out.println(root.key + ", ");
        printInOrder(root.right);
    }
}
