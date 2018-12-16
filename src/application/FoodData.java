package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**Filename:	FoodItemUnitTests.java
* Project:		Final Project (p5)
* Authors: 		Adam Hughes, Sadie Sturgeon
* Emails:		adam.hughes@wisc.edu, ssturgeon@wisc.edu
* Course:		CS400
* Section:		004
* 
* Credit Outside Help
* 
* Online Sources: https://stackoverflow.com/questions/31683375/java-8-lambda-intersection-of-two-lists
* 
* Notes:
*/

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu)
 * @author ahughes (adam.hughes@wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {
    
    // List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;
    private List<String> usedIDs;
    
    
    /**
     * Public constructor
     */
    public FoodData() {
        this.foodItemList = new ArrayList<FoodItem>();
        this.indexes = new HashMap<String, BPTree<Double, FoodItem>>();
        indexes.put("calories", new BPTree<Double,FoodItem>(64));
        indexes.put("fat", new BPTree<Double,FoodItem>(64));
        indexes.put("carbohydrate", new BPTree<Double,FoodItem>(64));
        indexes.put("protein", new BPTree<Double,FoodItem>(64));
        indexes.put("fiber", new BPTree<Double,FoodItem>(64));
        this.usedIDs = new ArrayList<String>();
    }
    
    
    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#loadFoodItems(java.lang.String)
     */
    @Override
    public void loadFoodItems(String filePath) {
        // TODO : Complete
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByName(java.lang.String)
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
        String lcSub = substring.toLowerCase();
        List<FoodItem> filteredByNames = 
                        foodItemList.stream()
                        .filter(x -> x.getName().toLowerCase().contains(lcSub))
                        .collect(Collectors.toList());
             return filteredByNames;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByNutrients(java.util.List)
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
      List<FoodItem> filteredFoodList = new ArrayList<FoodItem>(); // FoodItems filtered by rules
      
      // If the rules list is empty, then returns an empty list
      if (rules.isEmpty()) {
        return filteredFoodList;
      }
      
      Iterator<String> rulesIt = rules.iterator(); // Iterator on all rules
      String rule; // Individual nutrient rule before splitting
      String[] splitRule; // Individual nutrient rule after splitting: <nutrient> <comparator> <value>
      String nutrient; // Nutrient name, case insensitive
      String comparator; // Comparator type
      double value; // Amount of nutrient: in count (calories) or grams (fat, carbohydrate, fiber, protein)
      int ruleListNum = 0; // Counts the rules as they are added to trigger list intersection
      
      // Parses rules list, performs range search on each rule, and intersects all filtered lists
      while (rulesIt.hasNext()) {
        rule = rulesIt.next();
        
        // Delimiter is " "
        splitRule = rule.split(" ");
        
        nutrient = splitRule[0].toLowerCase().trim();
        comparator = splitRule[1].trim();
        value = Double.parseDouble(splitRule[2]);
        
        // Finds the nutrient's BPTree
        BPTree<Double, FoodItem> bpTree = indexes.get(nutrient);
        
        // If key is null, returns empty list
        List<FoodItem> newFilteredFoodList = bpTree.rangeSearch(value, comparator);
        ++ruleListNum;
        
        // If more than one rule list has been created, then intersection is needed
        if (ruleListNum > 1) {
        
          // Gets intersection of two FoodItem lists
          List<FoodItem> intersection = newFilteredFoodList.stream()
              .filter(filteredFoodList::contains)
              .collect(Collectors.toList());
        
          filteredFoodList = intersection;
          
        } else {
          filteredFoodList = newFilteredFoodList;
        }
      }
      
      return filteredFoodList;
    }

    /* Adds a food item to the food list and the nutriant indices
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
        if (foodItem == null) {
        	return;
        }
    	
    	// add the food to the list
    	foodItemList.add(foodItem);
    	
    	// add all nutriant values to the relevant indices
    	BPTree<Double,FoodItem> calorieTree = indexes.get("calories");
    	calorieTree.insert(foodItem.getNutrientValue("calories"), foodItem);
    	indexes.put("calories", calorieTree);
    	
    	BPTree<Double,FoodItem> fatTree = indexes.get("fat");
    	fatTree.insert(foodItem.getNutrientValue("fat"), foodItem);
    	indexes.put("fat", fatTree);
    	
    	BPTree<Double,FoodItem> carbohydrateTree = indexes.get("carbohydrate");
    	carbohydrateTree.insert(foodItem.getNutrientValue("carbohydrate"), foodItem);
    	indexes.put("carbohydrate", carbohydrateTree);
    	
    	BPTree<Double,FoodItem> proteinTree = indexes.get("protein");
    	proteinTree.insert(foodItem.getNutrientValue("protein"), foodItem);
    	indexes.put("protein", proteinTree);
    	
    	BPTree<Double,FoodItem> fiberTree = indexes.get("fiber");
    	fiberTree.insert(foodItem.getNutrientValue("fiber"), foodItem);
    	indexes.put("fiber", fiberTree);
    	
    	//mark this ID as used
    	usedIDs.add(foodItem.getID());
    }

    /*
     * retrieves a list of all food items
     * @see skeleton.FoodDataADT#getAllFoodItems()
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemList;
    }
    
    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#saveFoodItems()
     */
    @Override
    public void saveFoodItems(String filename) {
    	String content = "";
    	try {
		Files.write(Paths.get(filename), content.getBytes());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//add to lines to file
		
		Iterator<FoodItem> itr = foodItemList.iterator();
		while(itr.hasNext()) {
			FoodItem foodItemTemp = (FoodItem) itr.next();
			String appendLineToFile = lineAppend(foodItemTemp);
			try {
				Files.write(Paths.get(filename), appendLineToFile.getBytes(),StandardOpenOption.APPEND);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
    }
    
	/**
	 * Produces the formatted string to append to a file when saving.
	 * @param foodItemTemp a food item to be written to a line in the file
	 * @return string in the expected format
	 */
	private String lineAppend(FoodItem foodItemTemp) {
		return String.format("%s,%s,calories,%f,fat,%f,carbohydrate,%f,fiber,%f,protein,%f\n",
				foodItemTemp.getID(),
				foodItemTemp.getName(),
				foodItemTemp.getNutrientValue("calories"),
				foodItemTemp.getNutrientValue("fat"),
				foodItemTemp.getNutrientValue("carbohydrate"),
				foodItemTemp.getNutrientValue("fiber"),
				foodItemTemp.getNutrientValue("protein"));
	}
    
    /**
     * check to make sure an ID can be used. IDs
     * must be unique
     * @param id
     * @return true if ID is non-null and has not yet been used
     */
    public boolean isUniqueID(String id) {
    	if (id == null) {
    		return false;
    	}
    	
    	if(usedIDs.contains(id)) {
    		return false;
    	}
    	
    	return true;
    }
    
}
