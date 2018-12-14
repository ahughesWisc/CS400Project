package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**Filename:	FoodItemUnitTests.java
* Project:		Final Project (p5)
* Authors: 		Adam Hughes
* Emails:		adam.hughes@wisc.edu
* Course:		CS400
* Section:		004
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
    
    
    /**
     * Public constructor
     */
    public FoodData() {
        // TODO : Complete
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
        // TODO : Complete
        return null;
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
      
      // FIXME assumes rules is correctly formatted
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
        
          // Source: https://stackoverflow.com/questions/31683375/java-8-lambda-intersection-of-two-lists
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

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
        // TODO : Complete
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#getAllFoodItems()
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        // TODO : Complete
        return null;
    }
    
    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#saveFoodItems()
     */
    @Override
    public void saveFoodItems(String filename) {
    	// TODO : Complete
    }

}
