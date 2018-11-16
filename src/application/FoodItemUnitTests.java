package application;

/**Filename:	FoodItemUnitTests.java
* Project:		Final Project (p5)
* Authors: 		Adam Hughes
* Emails:		adam.hughes@wisc.edu
* Course:		CS400
* Section:		004
* 
* Notes:
*/
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FoodItemUnitTests {

	FoodItem foodItem;
	static final String id = "ID";
	static final String name = "Name";
	
	@Before
	public void setUp() throws Exception {
		foodItem = new FoodItem(id, name);
	}

	@Test
	public void test00000_constructor() {
		//Exceptions
		try {
			foodItem = new FoodItem(null, null);
		}
		catch (IllegalArgumentException e) { //correct
			
		}
		try {
			foodItem = new FoodItem(id, null);
		}
		catch (IllegalArgumentException e) { //correct
			
		}
		try {
			foodItem = new FoodItem(null, name);
		}
		catch (IllegalArgumentException e) { //correct
			
		}
	}
	
	@Test
	public void test00001_getID() {
		assertEquals(id, foodItem.getID());
	}
	
	@Test
	public void test00010_getName() {
		assertEquals(name, foodItem.getName());
	}

}
