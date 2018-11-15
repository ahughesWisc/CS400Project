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

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BPTreeUnitTests {
	
	BPTree<Integer,Integer> tree;

	@Before
	public void setUp() throws Exception {
		tree = new BPTree<Integer, Integer>(3); //
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test10000_constructor() {
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
				fail(String.format("Constructor threw an exception with branch factor %d", goodBranchFactor)
						);
				}
			}
		//Spot checking some more values sampled at random
		Random r = new Random();
		int value;
		//Random bad
		for (int i = 0; i < 10; ++i) {
			try {
				value = -r.nextInt(Integer.MAX_VALUE);
				tree = new BPTree<Integer,Integer>(value); //select negative values at random to get some more coverage
			}
			catch (IllegalArgumentException e) { } //success
		}
		
		//Random good
		for (int i = 0; i < 10; ++i) {
			value = r.nextInt(Integer.MAX_VALUE);
			try {
				tree = new BPTree<Integer,Integer>(value); //select positive values at least 3 at random to get more coverage
			}
			catch (IllegalArgumentException e) { //failure
				fail(String.format("Constructor threw an exception with branch factor %d", value)
						);
				}
		}
		
	}

}
