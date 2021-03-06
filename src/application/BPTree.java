package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**Filename:	BPTree.java
* Project:		Final Project (p5)
* Authors: 		Adam Hughes
* Emails:		adam.hughes@wisc.edu
* Course:		CS400
* Section:		004
* 
* Notes: 
*/

/**
 * Implementation of a B+ tree to allow efficient access to
 * many different indexes of a large data set. 
 * BPTree objects are created for each type of index
 * needed by the program.  BPTrees provide an efficient
 * range search as compared to other types of data structures
 * due to the ability to perform log_m N lookups and
 * linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 * @author ahughes (adam.hughes@wisc.edu)
 * @author awhitaker (awhitaker3@wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
    private Node root;

    // Branching factor is the number of children nodes
    // for internal nodes of the tree
    private int branchingFactor;


    /**
     * Public constructor
     * 
     * @param branchingFactor
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) {
            throw new IllegalArgumentException(
                            "Illegal branching factor: " + branchingFactor);
        }
        this.branchingFactor = branchingFactor;
        root = new LeafNode();
    }


    /*
     * Calls the insert method on the root to insert a key,value pair
     * 
     * @see BPTreeADT#insert(java.lang.Object, java.lang.Object)
     */
    @Override
    public void insert(K key, V value) {
        if (key == null) {
            return;
        }
        root.insert(key, value);
    }


    /*
     * Verifies the format of the comparator and checks that the key is not null, then calls
     * rangeSearch on the root
     * 
     * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
     */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        if ((!comparator.contentEquals(">=") &&
                        !comparator.contentEquals("==") &&
                        !comparator.contentEquals("<=")) || key == null)
            return new ArrayList<V>();
        return root.rangeSearch(key, comparator);
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }


    /**
     * This abstract class represents any type of node in the tree This class is a super class of
     * the LeafNode and InternalNode types.
     * 
     * @author sapan
     */
    private abstract class Node {

        // List of keys
        List<K> keys;

        /**
         * Package constructor
         */
        Node() {
            this.keys = new ArrayList<K>();
        }

        /**
         * Inserts key and value in the appropriate leaf node and balances the tree if required by
         * splitting
         * 
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         * 
         * @return key
         */
        abstract K getFirstLeafKey();

        /**
         * Gets the new sibling created after splitting the node
         * 
         * @return Node
         */
        abstract Node split();

        /*
         * (non-Javadoc)
         * 
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         * 
         * @return boolean
         */
        abstract boolean isOverflow();

        public String toString() {
            return keys.toString();
        }

    } // End of abstract class Node

    /**
     * This class represents an internal node of the tree. This class is a concrete sub class of the
     * abstract Node class and provides implementation of the operations required for internal
     * (non-leaf) nodes.
     * 
     * @author sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;

        /**
         * Package constructor
         */
        InternalNode() {
            super();
            this.children = new ArrayList<Node>();
        }

        /**
         * Recursive method to return the smallest key in the subtree
         * 
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            return children.get(0).getFirstLeafKey();
        }

        /**
         * Checks if current node has too many children
         * 
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            return children.size() > branchingFactor;
        }

        /**
         * Inserts a key into a internal node, and recursively calls itself in order to insert the
         * value into the correct leaf node.
         * 
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key, V value) {
            Node child = getChild(key);
            child.insert(key, value);
            if (child.isOverflow()) {
                Node childSibling = child.split();
                K promotedKey = childSibling.getFirstLeafKey();
                int index = Collections.binarySearch(keys, promotedKey);
                if (index >= 0) {
                    children.set(index + 1, childSibling);
                } else {
                    keys.add(-index - 1, promotedKey);
                    children.add(-index, childSibling);
                }
            }

            // Check if root node is overflowing, and split if so.
            // Creates a new root
            if (root.isOverflow()) {
                Node sibling = split();
                InternalNode temp = new InternalNode();
                temp.keys.add(sibling.getFirstLeafKey());
                temp.children.add(this);
                temp.children.add(sibling);
                root = temp;
            }
        }


        /**
         * Method to split an internal node into two. Copies over half the keys and children from
         * original node into sibling, and then clears them from original node. Returns the newly
         * created sibling
         * 
         * @see BPTree.Node#split()
         */
        Node split() {
            int sibStart = keys.size() / 2 + 1;
            int sibEnd = keys.size();
            InternalNode sibling = new InternalNode();

            for (int i = sibStart; i < sibEnd; i++) {
                sibling.keys.add(keys.get(i));
                sibling.children.add(children.get(i));
            }
            sibling.children.add(children.get(sibEnd));

            for (int i = sibStart; i <= sibEnd; i++) {
                keys.remove(sibStart - 1);
                children.remove(sibStart);
            }

            return sibling;
        }

        /**
         * Recursive method that calls range search on the relevant child node
         * 
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
            return this.getChild(key).rangeSearch(key, comparator);
        }

        /**
         * Helper method to get a child node corresponding to a given key. Searches the key list to
         * find index of given key, and then returns node found in list of children at that index
         * 
         * @param key value used to search for child
         * @return child node associated with that key value
         */
        Node getChild(K key) {
            // use binarySearch rather than looping through key list
            int index = Collections.binarySearch(keys, key);
            if (index >= 0)
                index += 1;
            else
                index = -index - 1;
            return children.get(index);
        }

    } // End of class InternalNode


    /**
     * This class represents a leaf node of the tree. This class is a concrete sub class of the
     * abstract Node class and provides implementation of the operations that required for leaf
     * nodes.
     * 
     * @author sapan
     */
    private class LeafNode extends Node {

        // List of values
        List<ArrayList<V>> values;

        // Reference to the next leaf node
        LeafNode next;

        // Reference to the previous leaf node
        LeafNode previous;

        /**
         * Package constructor
         */
        LeafNode() {
            super();
            this.values = new ArrayList<ArrayList<V>>();
            this.next = null;
            this.previous = null;
        }


        /**
         * Returns the first key in the list, which will be the smallest
         * 
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            return keys.get(0);
        }

        /**
         * Checks if the list of values is too large
         * 
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            return values.size() > branchingFactor - 1;
        }

        /**
         * Inserts a key value pair into the node. If the key is already present, it adds the new
         * value to the corresponding ArrayList. Otherwise it inserts a new key and creates a new
         * value ArrayList
         * 
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
            int index = Collections.binarySearch(keys, key);
            if (index >= 0) {
                values.get(index).add(value);
            } else {
                keys.add(-index - 1, key);
                values.add(-index - 1, new ArrayList<V>());
                values.get(-index - 1).add(value);
            }
            // Check if root node is overflowing, and split if so.
            // This code should only be reached when root is LeafNode (ie: root is only node)
            // Creates a new root
            if (root.isOverflow()) {
                Node sibling = split();
                InternalNode temp = new InternalNode();
                temp.keys.add(sibling.getFirstLeafKey());
                temp.children.add(this);
                temp.children.add(sibling);
                root = temp;
            }
        }

        /**
         * Copies over half the keys and values from node to a newly created sibling node.
         * Simultaneously clears out these copied value from current node
         * 
         * @see BPTree.Node#split()
         */
        Node split() {
            int sibStart = keys.size() / 2 + 1;
            int sibEnd = keys.size();
            LeafNode sibling = new LeafNode();

            for (int i = sibStart; i < sibEnd; i++) {
                sibling.keys.add(keys.get(sibStart));
                sibling.values.add(values.get(sibStart));
                keys.remove(sibStart);
                values.remove(sibStart);
            }

            sibling.previous = this;
            this.next = sibling;

            return sibling;
        }

        /**
         * Method that returns a list of values given a search key and comparator. Uses the
         * linked-list nature of the leaf nodes to iterate through them and find all values
         * 
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
            ArrayList<V> combined = new ArrayList<V>();
            int index;
            LeafNode node;

            switch (comparator) {
                case ">=":
                    // Add in current key values and all subsequent values in current node
                    index = Collections.binarySearch(keys, key);
                    if (index < 0) {
                        index = -index - 1;
                    }
                    for (int i = index; i < values.size(); i++) {
                        combined.addAll(values.get(i));
                    }

                    // Iterate through subsequent leaf nodes and add in all their values
                    node = this;
                    while (node.next != null) {
                        node = node.next;
                        for (int i = 0; i < node.values.size(); i++) {
                            combined.addAll(node.values.get(i));
                        }
                    }
                    break;

                case "<=":
                    // Add in current key values and all previous values in current node
                    index = Collections.binarySearch(keys, key);
                    if (index < 0) {
                        index = -index - 2;
                    }
                    for (int i = index; i >= 0; i--) {
                        combined.addAll(values.get(i));
                    }

                    // Iterate through previous leaf nodes and add in all their values
                    node = this;
                    while (node.previous != null) {
                        node = node.previous;
                        for (int i = node.values.size() - 1; i >= 0; i--) {
                            combined.addAll(node.values.get(i));
                        }
                    }
                    break;

                case "==":
                    // just return values associated with current key, if presentF
                    if (!keys.contains(key))
                        break;
                    combined.addAll(values.get(keys.indexOf(key)));
                    break;
            }
            return combined;
        }

    } // End of class LeafNode


    /**
     * Contains a basic test scenario for a BPTree instance. It shows a simple example of the use of
     * this class and its related types.
     * 
     * @param args
     */
    public static void main(String[] args) {
     // create empty BPTree with branching factor of 3
        BPTree<Double, Double> bpTree = new BPTree<>(3);

        // create a pseudo random number generator
        Random rnd1 = new Random();

        // some value to add to the BPTree
        Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};

        // build an ArrayList of those value and add to BPTree also
        // allows for comparing the contents of the ArrayList 
        // against the contents and functionality of the BPTree
        // does not ensure BPTree is implemented correctly
        // just that it functions as a data structure with
        // insert, rangeSearch, and toString() working.
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < 400; i++) {
            Double j = dd[rnd1.nextInt(4)];
            list.add(j);
            bpTree.insert(j, j);
            System.out.println("\n\nTree structure:\n" + bpTree.toString());
        }
        List<Double> filteredValues = bpTree.rangeSearch(0.2d, ">=");
        System.out.println("Filtered values: " + filteredValues.toString());
    }


} // End of class BPTree
