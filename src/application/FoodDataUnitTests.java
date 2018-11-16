package application;

/**Filename:	FoodDataUnitTests.java
* Project:		Final Project (p5)
* Authors: 		Adam Hughes
* Emails:		adam.hughes@wisc.edu
* Course:		CS400
* Section:		004
* 
* Notes:
* Credits: I used the method of https://dzone.com/articles/generate-random-alpha-numeric for how to build a random string 
*/

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class FoodDataUnitTests {
	
	FoodData foodData;
	static final Random r = new Random();
	static final int numFoods = 10;
	//need the method as that is the only way to vary numFoods without having to each time manually update the list
	//this is fine though, as we separately test the FoodItem class, and so the variance shouldn't affect the testing in this set of tests
	List<FoodItem> foodList = setupFoodList();
	static final String loadFilePath = "foodItems.txt";
	static final String saveFilePath = "saveFood.txt";	

	@Before
	public void setUp() {
		foodData = new FoodData();
	}

	@Test
	public void test00000_addFoodItem() {
		fail("Not yet implemented");
	}
	
	/**
	 * Helper method to setup some basic food items for testing with
	 * @return list of numFoods food items
	 */
	private List<FoodItem> setupFoodList() {
		//Setup
		List<FoodItem> ret = new ArrayList<FoodItem>();
		final String digits = "0123456789";
		final String letters = "abcdefghijklmnopqrstuvwxyz"; //character set
		StringBuilder id = new StringBuilder();
		StringBuilder name = new StringBuilder();
		
		//Setup some foods
		for (int i = 0; i < numFoods; ++i) {
			//Build random ID of a reasonable size
			for (int j = 0; j < r.nextInt(10) + 1; ++j) { //1-10 digit ID is reasonable
				id.append(digits.charAt(r.nextInt(10))); //add on a random digit
			}
			
			//Build random name of reasonable size
			for (int j = 0; j < r.nextInt(11) + 5; ++j) { //5-15 character name seems reasonable
				
				name.append(letters.charAt(r.nextInt(26))); //add a random letter
			}
			
			ret.add(new FoodItem(id.toString(), name.toString()));
		}
		
		return ret;
	}

}
