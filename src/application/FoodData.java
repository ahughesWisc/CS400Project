package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    	List<List<String>> listOfLines = new ArrayList<>();
    	try (Stream<String> stream = Files.lines(Paths.get(filePath))) {

			listOfLines = stream
					.map(line -> Arrays.asList(line.split(",")))
					.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
			displayError("There was an error loading foods from" + filePath +". Adjust the file "
					+ "input, and use the Load Food and Add Food buttons to add foods to the program.");
		}
		catch (Exception e) {
			e.printStackTrace();
			displayError("There was an error loading foods from" + filePath +". Adjust the file "
					+ "input, and use the Load Food and Add Food buttons to add foods to the program.");
		}
		
		int failedLines = 0;
		String id = new String();
		String name = new String();
		String calories = new String();
		String fat = new String();
		String carbohydrate = new String();
		String fiber = new String();
		String protein = new String();
		//go through each line in input file and decide if the format is valid to save to our foodData structure
		//<id>,<food_name>,<calories>,<calorie_count>,<fat>,<fat_grams>,<carbohydrate>,<carbohydrate_grams>,<fiber>,<fiber_grams>,<protein>,<protein_grams>,
		//Example row of a valid data file:
		//556540ff5d613c9d5f5935a9,Stewarts_PremiumDarkChocolatewithMintCookieCrunch,calories,280,fat,18,carbohydrate,34,fiber,3,protein,3,
		for(int i =0; i < listOfLines.size();i++) {
			//List<String> tempList = itr.next(); //this is looking at each line from the input file
			if(isValidDataField(listOfLines.get(i))) {
				//parse out the food values
				id = listOfLines.get(i).get(0);
				name = listOfLines.get(i).get(1);
				calories = listOfLines.get(i).get(3);
				fat = listOfLines.get(i).get(5);
				carbohydrate = listOfLines.get(i).get(7);
				fiber = listOfLines.get(i).get(9);
				protein = listOfLines.get(i).get(11);
				
				if (addFoodtoFoodData(false,id,name,calories,fat,carbohydrate,fiber,protein,null) == false) {
					failedLines++;
				}

			} else {
				failedLines++;
			}

		}
		if (failedLines > 0) {
			displayError(failedLines + " lines of the input file failed to load");
		}
    	
    }
    
    private boolean addFoodtoFoodData(Boolean triggerErrors,String id, String name,String calories,String 
			fat,String carbohydrate,String fiber,String protein, Stage stage) {
		if (isUniqueID(id)) {
			if(id != null && name != null && calories != null && fat != null && carbohydrate != null &&
					fiber != null && protein != null){
				//check all fields are not zero length
				if(!id.isEmpty() && !name.isEmpty() && !calories.isEmpty() && !fat.isEmpty() && 
						!carbohydrate.isEmpty() && !fiber.isEmpty() && !protein.isEmpty()) {
					//check if the ID or name contain commas
					if (!id.contains(",") && !name.contains(",")) {

						//check if the nutrient values are positive doubles
						if(isPositiveDouble(calories) && isPositiveDouble(fat) && isPositiveDouble(carbohydrate) && 
								isPositiveDouble(fiber) && isPositiveDouble(protein)) {
							FoodItem foodItem = new FoodItem(id,name);
							foodItem.addNutrient("calories", Double.parseDouble(calories));
							foodItem.addNutrient("fat", Double.parseDouble(fat));
							foodItem.addNutrient("carbohydrate", Double.parseDouble(carbohydrate));
							foodItem.addNutrient("fiber", Double.parseDouble(fiber));
							foodItem.addNutrient("protein", Double.parseDouble(protein));
							addFoodItem(foodItem);
							//foods.add(foodItem);//need to do something about this shadow array used for display
							if (stage != null) {
								stage.close();
							}
							return true;
						} else {
							if (triggerErrors) {
								displayError("Nutrient values must be numeric and non-negative");
							}
						}
					} else {
						if (triggerErrors) {
							displayError("The ID and name must not contain commas");
						}
					}
				} else {
					if (triggerErrors) {
						displayError("All fields in the form must be filled out");
					}
				}
			} else {
				if (triggerErrors) {
					displayError("All fields in the form must be filled out");
				}
			}
		} else {
			if (triggerErrors) {
				displayError("The selected ID is already in use, select a unique ID");
			}
		}
		return false;
	}
    
	/**
	 * Used by loadFile to check if a given line is in the expected format
	 * @param lineInput an array of lines of data
	 * @return true if valid, false otherwise
	 */
	private static boolean isValidDataField(List<String> lineInput) {
		//first check if there are 12 seperate data fields that were parsed, split by commas in input file
		if(lineInput.size() == 12) {
			//now check if column 3,5,7,9,11 have the correct name of the nutrient: calories,
			//fat, carbohydrate, fiber and protein
			if(lineInput.get(2).equals("calories") && lineInput.get(4).equals("fat") && 
					lineInput.get(6).equals("carbohydrate") && lineInput.get(8).equals("fiber") && 
					lineInput.get(10).equals("protein")) { 
				//check if the column to the right of the nutrient contains a number that be of type double
				if(isPositiveDouble(lineInput.get(3)) && isPositiveDouble(lineInput.get(5)) &&
						isPositiveDouble(lineInput.get(7)) && isPositiveDouble(lineInput.get(9)) && 
						isPositiveDouble(lineInput.get(11))) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}else {
			return false;
		}
	}//end of isValidDataField()
	
	/**
	 * method to check if a string can be converted into a non-negative double
	 * @param str string to check
	 * @return true if string represents a positive double, false otherwise
	 */
	private static boolean isPositiveDouble(String str) {
		try {
			if(Double.parseDouble(str) >=0.0) {
				return true;
			}
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}//end of isPositiveDouble
	
	
	private void displayError(String message) {
		Stage loadPopupWindow =new Stage();
		String ErrorPopupTitle = "Error";
		String AcceptButtonCaption = "OK";
		
		loadPopupWindow.initModality(Modality.APPLICATION_MODAL);
		loadPopupWindow.setTitle(ErrorPopupTitle);

		ScrollPane scrollPane = new ScrollPane();
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		
		scrollPane.setPadding(new Insets(20, 12, 20, 12));
		gridPane.setHgap(10); // Horizontal gap between rows
		gridPane.setVgap(10); // Vertical gap between columns
		
		Text errorMessageText = new Text(message);
		errorMessageText.setWrappingWidth(275);
		Button acceptButton = new Button(AcceptButtonCaption);
		acceptButton.setPrefSize(80,40);   
		acceptButton.setOnAction(e -> loadPopupWindow.close());
		
		HBox acceptButtonHBox = new HBox(); // Load, add, and save buttons in Food List area
		acceptButtonHBox.setSpacing(10);
		acceptButtonHBox.getChildren().add(acceptButton);
		acceptButtonHBox.setAlignment(Pos.CENTER);
		
		gridPane.add(errorMessageText, 0, 0);
		gridPane.add(acceptButtonHBox, 0, 2);
		scrollPane.setContent(gridPane);
		Scene scene1= new Scene(scrollPane, 300, 200);

		//set the scene to the popup window
		loadPopupWindow.setScene(scene1);
		loadPopupWindow.showAndWait();	
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
