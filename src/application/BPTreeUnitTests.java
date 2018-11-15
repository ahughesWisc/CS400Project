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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BPTreeUnitTests {
	
	BPTree<Integer,Integer> tree; //main object
	final static int branchFact = 3; //branch factor of our tree, good for when we need to reinitialize
	final static int count = 100; //used for stress testing
	final static Random r = new Random(); //used for stress testing

	@Before
	public void setUp() throws Exception {
		tree = new BPTree<Integer, Integer>(branchFact); //initialize
	}

	@After
	public void tearDown() throws Exception {
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
		assertEquals(tree.valuesForKey(null).size(), 0);
		assertEquals(tree.valuesForKey(0).size(), 0);
		
		//simple test
		tree.insert(0, 0);
		assertEquals(tree.valuesForKey(0).size(), 1);
		
		//jives with removal
		tree.remove(0, 0);
		assertEquals(tree.valuesForKey(0).size(), 0);
		
		//more than one
		tree.insert(0, 0);
		tree.insert(0, 1);
		assertEquals(tree.valuesForKey(0).size(), 2);
		
		//no cross talk
		tree.insert(1, 0);
		assertEquals(tree.valuesForKey(0).size(), 2);
		
		//duplicate key value pair
		tree.insert(0, 0);
		assertEquals(tree.valuesForKey(0).size(), 2);
		
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
			assertEquals(tree.valuesForKey(0).size(), values.size()); //number of values for key 0 should be exactly the size of the set
		}
		
	}
	
	@Test
	public void test00101_rangeSearch() {
		
	}

}
