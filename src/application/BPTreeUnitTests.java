package application;

/**Filename:	BPTreeUnitTests.java
* Project:		Final Project (p5)
* Authors: 		Adam Hughes
* Emails:		adam.hughes@wisc.edu
* Course:		CS400
* Section:		004
* 
* Notes: 
*/

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class BPTreeUnitTests {
	
	BPTree<Integer,Integer> tree; //main object
	List<Integer> expected; //holds testing expectations for 
	String comparator;
	final static int branchFact = 3; //branch factor of our tree, good for when we need to reinitialize
	final static int count = 100; //used for stress testing
	final static Random r = new Random(); //used for stress testing

	@Before
	public void setUp() throws Exception {
		tree = new BPTree<Integer, Integer>(branchFact); //initialize object used in all tests
	}

	@Test
	public void test00000_constructor() {
		//bad values testing
		for (int badBranchFactor = -11; badBranchFactor < 3 ; ++badBranchFactor ) {
			try {
				tree = new BPTree<Integer,Integer>(badBranchFactor);
			}
			catch (IllegalArgumentException e) { } //success			
		}
		
		//good values testing
		for (int goodBranchFactor = 3; goodBranchFactor < 23; ++goodBranchFactor) {
			try {
				tree = new BPTree<Integer,Integer>(goodBranchFactor);
			}
			catch (IllegalArgumentException e) { //failure
				fail(
						String.format("Constructor threw an exception with branch factor %d", goodBranchFactor)
						);
				}
			}
		//Spot checking some more values sampled at random
		
		int value;
		//Random bad
		for (int i = 0; i < 10; ++i) {
			try {
				value = -r.nextInt(Integer.MAX_VALUE); //don't shift as we've already tested numbers close to 2
				tree = new BPTree<Integer,Integer>(value); //select negative values at random to get some more coverage
			}
			catch (IllegalArgumentException e) { } //success
		}
		
		//Random good
		for (int i = 0; i < 10; ++i) {
			value = 3 + r.nextInt(Integer.MAX_VALUE - 2); //add 3 to ensure it is at least 3, make sure max argument is not too big for overflow
			try {
				tree = new BPTree<Integer,Integer>(value); //select positive values at least 3 at random to get more coverage
			}
			catch (IllegalArgumentException e) { //failure
				fail(
						String.format("Constructor threw an exception with branch factor %d", value)
						);
				}
		}
		
	}
	
	@Test
	public void test00001_insert() {
		//Setup
		
		
		//insert with nulls
		try {
			tree.insert(null, null);
			tree.insert(null, 0);
			tree.insert(0, null);
		}
		catch (Exception e) {
			fail("Threw an exception when attempting to insert null values");
		}
		
		//insert single keys
		for (int key = 0; key < 10; ++key) {
			try {
				tree.insert(key, 0);
			}
			
			catch (Exception e) {
				fail(
						String.format("Threw an exception attempting to insert with key %d", key)
						);
			}
		}
		
		//insert duplicate keys
		for (int key = 0; key < 10; ++key) {
			for (int value = 0; value < 10; ++value) {
				try {
					tree.insert(key, value);
				}
				catch (Exception e) {
					fail(
							String.format("Threw an exception inserting duplicate keys with key %d and value %d", key, value)
							);
				}
			}
		}
		
		//Stress testing
		int k = 0;
		int v = 0;
		for (int i = 0; i < count; ++i) {
			for (int j = 0; j < count; ++j) {
				try {
					k = r.nextInt();
					v = r.nextInt();
					tree.insert(k, v);
				}
				
				catch (Exception e) {
					fail(
							String.format("Threw an exception inserting with key %d and value %d", k, v)
							);
				}
			}
		}
	}
	
	@Test
	public void test00010_remove() {
		//Setup
		int key;
		int value;
		int[] keys = new int[count];
		int[] values = new int[count];
		
		
		//null testing
		try {
			tree.remove(null, null);
			tree.remove(null, 0);
			tree.remove(0, null);
		}
		
		catch (Exception e) {
			fail("Threw an exception removing with null key or value.");
		}
		
		//Remove from empty
		try {
			tree.remove(0, 0);
		}
		
		catch (Exception e) {
			fail("Threw an exception removing from an empty tree.");
		}
		
		//Setup with some values
		tree.insert(0, 0);
		tree.insert(0, 1);
		tree.insert(0, 2);
		tree.insert(1, 2);
		tree.insert(1, 3);
		tree.insert(4, 5);
		tree.insert(-5, 3);
		
		//Remove key that isn't there
		try {
			tree.remove(-1, 0);
		}
		
		catch (Exception e) {
			fail("Threw an exception removing non-existent key");
		}
		
		//Remove value that isn't there for valid key
		try {
			tree.remove(0, -1);
		}
		catch (Exception e) {
			fail("Threw an exception removing non-existent value for valid key.");
		}
		
		//No need for try/catch since we don't care about a custom fail message here
		tree.remove(0, 0);
		tree.remove(1, 2);
		tree.remove(-5, 3);
		tree.remove(1, 3);
		tree.remove(0, 2);
		tree.remove(4, 5);
		tree.remove(0, 1);
		
		//Stress testing
		for (int i = 0; i < count; ++i) {
			key = r.nextInt();
			value = r.nextInt();
			keys[i] = key;
			values[i] = value;
			tree.insert(key, value);
		}
		
		for (int i = 0; i < count; ++i) {
			tree.remove(keys[i], values[i]);
		}
	}
	
	@Test
	public void test00011_isEmpty() {
		//Setup
		
		int keyVal;
		int[] keyVals = new int[count];
		
		assertTrue(tree.isEmpty()); //new tree should be empty
		
		//nulls
		tree.insert(null, null);
		assertTrue(tree.isEmpty());
		
		tree.insert(null, 0);
		assertTrue(tree.isEmpty());
		
		tree.insert(0, null);
		assertTrue(tree.isEmpty());
		
		tree.insert(0, 0);
		assertFalse(tree.isEmpty()); //now we should have something
		
		tree.remove(0, 0);
		assertTrue(tree.isEmpty()); //should be empty again
		
		tree.insert(0, 1);
		assertFalse(tree.isEmpty());
		
		tree.remove(0, 0);
		assertFalse(tree.isEmpty()); //should not be empty if we remove something not there
		
		tree.insert(0, 0);
		tree.remove(0, 1);
		assertFalse(tree.isEmpty());
		
		tree.remove(0, 0);
		assertTrue(tree.isEmpty());
		
		//Stress testing
		for (int i = 0; i < count; ++i) {
			keyVal = r.nextInt();
			keyVals[i] = keyVal;
			tree.insert(keyVal, keyVal);
		}
		
		for (int i = 0; i < count; ++i) {
			tree.remove(keyVals[i], keyVals[i]);
		}
		
		assertTrue(tree.isEmpty());
	}
	
	@Test
	public void test00100_valuesForKey() {
		//nulls
		try {
			tree.valuesForKey(null);
		}
		catch (Exception e) {
			fail("Threw an exception trying to find values for null key");
		}
		
		//non-existent key
		try {
			tree.valuesForKey(0);
		}
		catch (Exception e) {
			fail("Threw an exception searching for values of key not in tree");
		}
		
		//Should return 0 for null or keys we don't have
		assertEquals(0, tree.valuesForKey(null).size());
		assertEquals(0, tree.valuesForKey(0).size());
		
		//simple test
		tree.insert(0, 0);
		assertEquals(1, tree.valuesForKey(0).size());
		
		//jives with removal
		tree.remove(0, 0);
		assertEquals(0, tree.valuesForKey(0).size());
		
		//more than one
		tree.insert(0, 0);
		tree.insert(0, 1);
		assertEquals(2, tree.valuesForKey(0).size());
		
		//no cross talk
		tree.insert(1, 0);
		assertEquals(2, tree.valuesForKey(0).size());
		
		//duplicate key value pair
		tree.insert(0, 0);
		assertEquals(2, tree.valuesForKey(0).size());
		
		//Stress testing
		for (int testNumber = 0; testNumber < count; ++testNumber) {
			//Setup
			tree = new BPTree<Integer, Integer>(branchFact); //clean out old stuff
			Set<Integer> values = new HashSet<Integer>();
			
			for (int i = 0; i < count; ++i) { //shove a bunch of integers into a set
				values.add(r.nextInt());
			}
			
			Iterator<Integer> itr = values.iterator();
			
			while (itr.hasNext()) { //now put all the (unique!) values with the same key
				tree.insert(0, itr.next());
			}
			
			//Finally check that our count agrees with the set size
			assertEquals(values.size(), tree.valuesForKey(0).size()); //number of values for key 0 should be exactly the size of the set
		}
		
	}
	
	@Test
	public void test00101_rangeSearchBasic() {
		//Setup
		expected = new ArrayList<Integer>();
		Set<String> screwyStrings = new HashSet<String>();
			//test cases
			screwyStrings.add("hi");
			screwyStrings.add("");
			screwyStrings.add(" ");
			screwyStrings.add("ding dong");
			screwyStrings.add("asdf");
			screwyStrings.add("asdf7,@#(*&$_W^$");
			screwyStrings.add("== ");
			screwyStrings.add(",<=");
			screwyStrings.add("> =");
			screwyStrings.add("=");
			screwyStrings.add("= =");
			screwyStrings.add("< =");
		Iterator<String> itr = screwyStrings.iterator();
		
		//totally empty tree
		assertEquals(new ArrayList<Integer>(), expected);
		//Screwy comparators
			//Screwy comparators for empty tree with null key
					itr = screwyStrings.iterator();
					while (itr.hasNext()) {
						comparator = itr.next();
						assertEquals(expected, tree.rangeSearch(null, comparator));
					}
			//Screwy comparators for empty tree with non-null, non-existent key
			itr = screwyStrings.iterator();
			while (itr.hasNext()) {
				comparator = itr.next();
				assertEquals(expected, tree.rangeSearch(0, comparator));
			}
			//Screwy comparators for non-empty tree and valid key
			tree.insert(0, 0);
			itr = screwyStrings.iterator();
			while (itr.hasNext()) {
				comparator = itr.next();
				assertEquals(expected, tree.rangeSearch(0, comparator));
			}
	}
	
	@Test
	public void test00110_rangeSearchEquals() {
		//Setup
		expected = new ArrayList<Integer>();
		comparator = "=="; //less likely to make typos and easier to see syntax
		
		//Nulls
		assertEquals(expected, tree.rangeSearch(null, comparator)); //valid operators + null key should return a new ArrayList
		
		//Non-existent key
		assertEquals(expected, tree.rangeSearch(0, comparator)); //valid operators + non-existent key should return a new ArrayList
		
		//test a few easy ones
		tree.insert(0, 0);
		expected.add(0);
		assertEquals(expected, tree.rangeSearch(0, comparator));	
		tree.insert(0, 1);
		tree.insert(0, 2);
		expected.add(1);
		expected.add(2);
		assertEquals(expected, tree.rangeSearch(0, comparator));
		
		//insert at other keys, should not affect the search for the search key with ==
		tree.insert(1, -1);
		assertEquals(expected, tree.rangeSearch(0, comparator));
		tree.insert(1, 10);
		tree.insert(2, 0);
		tree.insert(-20, 5);
		assertEquals(expected, tree.rangeSearch(0, comparator));
		
		//insert at our original key after having inserted at others
		tree.insert(0, 5);
		expected.add(5);
		assertEquals(expected, tree.rangeSearch(0, comparator));
		
		expected = new ArrayList<Integer>();
		expected.add(-1);
		expected.add(10);
		assertEquals(expected, tree.rangeSearch(1, comparator));
		
		//TODO: this needs some work depending on how we implement the BPTree values storage per key
//		//Stress testing
//		tree = new BPTree<Integer, Integer>(branchFact); //reset for stress test
//		expected = new ArrayList<Integer>(); //reset for stress test
//		Set<Integer> inserts = new TreeSet<Integer>(); //when they go into the BPTree the values should come out (per key) sorted so we use a TreeSet
//		int value;
//		for (int i = 0; i < count; ++i) { //shove a bunch of values into a set to ensure one copy per
//			inserts.add(r.nextInt());
//		}
//		Iterator<Integer> itr = inserts.iterator();
//		
//		while (itr.hasNext()) {
//			value = itr.next();
//			tree.insert(0, value);
//			expected.add(value);
//		}
//		assertEquals(expected, tree.rangeSearch(0, comparator)); //list out should be sorted list of values for given key
	}
	
	@Test
	public void test00111_rangeSearchGTE() {
		//Setup
		expected = new ArrayList<Integer>();
		comparator = ">="; //less likely to accidentally type the wrong one in the same test
		
		//Nulls
		assertEquals(expected, tree.rangeSearch(null, comparator)); //valid operators + null key should return a new ArrayList
		
		//Non-existent key
		assertEquals(expected, tree.rangeSearch(0, comparator)); //valid operators + non-existent key should return a new ArrayList
		
		//Basic case
		tree.insert(0, 0);
		expected.add(0);
		assertEquals(expected, tree.rangeSearch(0, comparator));
		
		//No false positives
		tree.insert(-1, 1);
		tree.insert(-2, 2);
		assertEquals(expected, tree.rangeSearch(0, comparator));
		
		//Extend to more than just one key that fits the criterion
		tree.insert(1, 3);
		expected.add(3);
		tree.insert(1, 4);
		expected.add(4);
		tree.insert(2, 5);
		expected.add(5);
		assertEquals(expected, tree.rangeSearch(0, comparator));
		
		//TODO: add stress test once resolution to value storage resolved
	}
	
	@Test
	public void test00111_rangeSearchLTE() {
		//Setup
		expected = new ArrayList<Integer>();
		comparator = "<="; //less likely to accidentally type the wrong one in the same test
		
		//Nulls
		assertEquals(expected, tree.rangeSearch(null, comparator)); //valid operators + null key should return a new ArrayList
		
		//Non-existent key
		assertEquals(expected, tree.rangeSearch(0, comparator)); //valid operators + non-existent key should return a new ArrayList
		
		//Basic case
		tree.insert(0, 0);
		expected.add(0);
		assertEquals(expected, tree.rangeSearch(0, comparator));
		
		//No false positives
		tree.insert(1, 1);
		tree.insert(2, 2);
		assertEquals(expected, tree.rangeSearch(0, comparator));
		
		//Extend to more than just one key that fits the criterion
		tree.insert(-1, 3);
		expected.add(3);
		tree.insert(-1, 4);
		expected.add(4);
		tree.insert(-2, 5);
		expected.add(5);
		assertEquals(expected, tree.rangeSearch(0, comparator));
		
		//TODO: add stress test once resolution to value storage resolved
	}
}
