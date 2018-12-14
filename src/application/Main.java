package application;

import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Purpose: General purpose GUI program for reading a .csv with food data and allowing users to interact
 * with that data including applying filters based on nutritional content and food names, adding foods
 * to the data repository, creating meals from food items, and being able to compute basic nutritional
 * data about a given meal. 
 * 
 * Filename:	Main.java
 * Project:		Final Project (p5)
 * Authors: 	Aaron Whitaker, Adam Hughes, Colin Butkus, Sadie Sturgeon, Sarah Bonebright
 * Emails:		awhitaker3@wisc.edu, adam.hughes@wisc.edu, butkus2@wisc.edu, ssturgeon@wisc.edu, sbonebright@wisc.edu
 * Course:		CS400
 * Section:		004
 * 
 * Credit Outside Help
 * 
 * Online Sources: https://youtu.be/GbzKr46VvD0
 * 
 * Notes:
 * Known Bugs: 
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Class used for creating and displaying the GUI and 
 * launching the food program
 *
 */
public class Main extends Application {
	
	//Settings
	final static String Font = "Arial"; //now you can change the font anywhere in the application from one place
	final static String BannerFont = "Arial Bold";
	final static int SeparatorLength = 29;
	final static String FilterHorizontalSeparator = horizontalSeparator("v");
	final static String Equals = "==";
	final static String LessThanOrEquals = "<=";
	final static String GreaterThanOrEquals = ">=";
	
	final static int foodListY = 1;
	final static int foodListX = 0;
	final static int menuListX = 3;
	final static int filterY = 6;
  
	//Similar to a .NET Resources.resx file, these sections are used so we can easily change string values in the UI without poking around everywhere
	//in the code. It also means that we can avoid spelling mistakes if we reuse the strings because we only have to spell them once explicitly.
	final static String Calories = "Calories";
	final static String Fat = "Fat";
	final static String Carbs = "Carbs";
	final static String Protein = "Protein";
	final static String Fiber = "Fiber";
	
	//Captions/Labels
	final static String Title = "Food Program";
	final static String LoadFoodsCaption = "Load Foods";
	final static String AddFoodCaption = "Add Foods";
	final static String SaveFoodsCaption = "Save Foods";
	final static String AnalyzeNutritionCaption = "Nutritional Analysis";
	final static String FoodListLabel = "Food List";
	final static String FilterActionLabel = "Food and Nutrient Search";
	final static String MenuListLabel = "Menu List";
	final static String SearchFilterCaption = "New Search Filter";
	final static String NutrientLabel = "Nutrient";
	final static String OperatorLabel = "Operator";
	final static String SelectedFiltersLabel ="Selected Filters";
	final static String NutrientFiltersLabel = "Nutrient Filters";
	final static String NameFilterLabel = "Search Term";
	final static String RunSearchCaption = "Run Search";
	final static String ValueLabel = "Value";
	final static String RemoveRuleCaption = "Remove Selected Rule";
	
	//Prompts
	final static String NutrientPromptText = "enter positive numeric value (e.g. 1.4)";
	final static String FoodPromptText = "enter food name";
	
	//ToolTips
	final static String ExitToolTip = "Exit Program";
	final static String AddActionToolTip = "Add the selected foods to the menu";
	final static String RemoveFoodToolTip = "Remove the selected foods from the menu";
	final static String AddRuleToolTip = "Add the rule to the search";
	final static String RemoveRuleToolTip = "Remove the selected rules from the search";
	
	//Image files for ease of swapping out
	final static String BackgroundImageFile = "fruitBackgroundSmall.png";
	final static String XButtonImage = "delte.png";
	final static String RightArrowImage = "goFoward.png";
	final static String LeftArrowImage = "goBack.png";
	final static String SmallRightArrowImage = "smallGoFoward.png";
	final static String HorizontalDividerImage = "horizontalLine.png";
	
	//Testing
	final static boolean DEBUG = true;
	
	//Class Attributes
	private ObservableList<String> rules = FXCollections.observableArrayList();
	private ListView<String> ruleList = new ListView<String>(rules);
	private ObservableList<FoodItem> foods = FXCollections.observableArrayList();
    private ListView<FoodItem> foodList = new ListView<FoodItem>(foods);
    private ObservableList<FoodItem> menuFoods = FXCollections.observableArrayList();
    private ListView<FoodItem> menuList = new ListView<FoodItem>(menuFoods);
    private final static ObservableList<String> nutrients = 
            FXCollections.observableArrayList(
                Calories,
                Fat,
                Carbs,
                Fiber,
                Protein
            );
    private final static ObservableList<String> logicOptions = 
            FXCollections.observableArrayList(
                Equals,
                LessThanOrEquals,
                GreaterThanOrEquals
            );
	
	/**
	 * Starts the program by launching the GUI
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
		    BorderPane borderpane = new BorderPane();
		    GridPane gridpane = new GridPane();
		    gridpane.setHgap(10); // Horizontal gap between rows
		    gridpane.setVgap(10); // Vertical gap between columns
		    gridpane.setPadding(new Insets(0, 12, 20, 12)); // Distance between nodes and edges of pane
		    
		    if (DEBUG) { //testing only
		    	gridpane.setGridLinesVisible(true);
		    }
		    
		    borderpane.setCenter(gridpane);
			Scene scene = new Scene(borderpane, 768, 830); // width, height
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			// --------- Creating Objects ---------
			
			// Set the application header
			primaryStage.setTitle(Title);	

			// Background
			Image background = new Image(getClass().getResourceAsStream(BackgroundImageFile));
			BackgroundImage fruitBackground = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, 
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
			gridpane.setBackground(new Background(fruitBackground));
			
			// Banner
			HBox banner = new HBox();
			Label bannerTitle = new Label(Title);
			bannerTitle.setFont(new Font(BannerFont, 22));
			bannerTitle.setPadding(new Insets(8, 10, 8, 10));
			banner.getChildren().add(bannerTitle);
			
			// Exit button for the program
			Image imageExit = new Image(getClass().getResourceAsStream(XButtonImage));
            Button exitProgramButton = new Button();
            exitProgramButton.setGraphic(new ImageView(imageExit));
            Tooltip exitProgramTooltip = new Tooltip(ExitToolTip);
            exitProgramButton.setTooltip(exitProgramTooltip);
            
            AnchorPane ap = new AnchorPane();
            ap.getChildren().addAll(banner, exitProgramButton);
            AnchorPane.setLeftAnchor(banner, 0.0);
            //AnchorPane.setRightAnchor(exitProgramButton, 48.0); // Sets position of exitProgramButton
            AnchorPane.setRightAnchor(exitProgramButton, 0.0);
            borderpane.setTop(ap);
            ap.setMaxWidth(768);
            ap.setMinWidth(768);
			
			// -- Food List Area: --
	
			Label foodListLabel = new Label(FoodListLabel); // Food List title
			foodListLabel.setFont(new Font(Font, 28));
			foodListLabel.setPadding(new Insets(10, 10, 10, 0));
			
			// HBox for buttons in food list area
            HBox foodListButtonsHBox = new HBox(); // Load, add, and save buttons in Food List area
            //foodListButtonsHBox.setPadding(new Insets(15, 12, 15, 12));
            foodListButtonsHBox.setSpacing(10);
            Button loadFoodsButton = new Button(LoadFoodsCaption);
            loadFoodsButton.setPrefSize(100, 20); // Sets width and height of button
            Button addFoodButton = new Button(AddFoodCaption);
            addFoodButton.setPrefSize(100, 20); // Sets width and height of button
            Button saveFoodsButton = new Button(SaveFoodsCaption);
            saveFoodsButton.setPrefSize(100, 20); // Sets width and height of button
            foodListButtonsHBox.getChildren().addAll(loadFoodsButton, addFoodButton, saveFoodsButton);
            
            // Master food list            
            if (DEBUG) { //testing only
            	FoodItem blackberries = new FoodItem("1","Blackberries");
            	FoodItem blueberries = new FoodItem("A","Blueberries");
            	FoodItem raspberries = new FoodItem("12312","Raspberries");
            	FoodItem strawberries = new FoodItem("2","Strawberries");
            	foodList.getItems().addAll(blackberries,blueberries,raspberries,strawberries);
            }
            
            // Makes multiple selections possible when hitting ctrl
            foodList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            foodList.setPrefWidth(320); // Formerly 323
            foodList.setPrefHeight(320);
            foodList.setMinHeight(320);
            
            
            // -- Buttons between Food List and Menu List: --
            
            // Add food to menu button
            Image imageArrowRight = new Image(getClass().getResourceAsStream(RightArrowImage));
            Button addFoodtoMenuButton = new Button();
            addFoodtoMenuButton.setGraphic(new ImageView(imageArrowRight));
            Tooltip addFoodTooltip = new Tooltip(AddActionToolTip);
            addFoodtoMenuButton.setTooltip(addFoodTooltip);
            
            // Remove food from menu button
            Image imageArrowLeft = new Image(getClass().getResourceAsStream(LeftArrowImage));
            Button removeFoodFromMenuButton = new Button();
            removeFoodFromMenuButton.setGraphic(new ImageView(imageArrowLeft));
            Tooltip removeFoodTooltip = new Tooltip(RemoveFoodToolTip);
            removeFoodFromMenuButton.setTooltip(removeFoodTooltip);
            
            VBox addAndRemoveButtonsVBox = new VBox();
            //addAndRemoveButtonsVBox.setPadding(new Insets(15, 12, 15, 12));
            addAndRemoveButtonsVBox.setSpacing(10);
            addAndRemoveButtonsVBox.setAlignment(Pos.CENTER);
            addAndRemoveButtonsVBox.getChildren().addAll(addFoodtoMenuButton, removeFoodFromMenuButton);
            
            // comparator for sorting food lists by name
            Comparator<? super FoodItem> comparatorFoodItembyName = new Comparator<FoodItem>() {
            	@Override
            	public int compare(FoodItem food1, FoodItem food2) {
            		return food1.getName().compareToIgnoreCase(food2.getName());
            	}
            };
            
            // add a food from the food list to the menu list
            addFoodtoMenuButton.setOnAction(e -> {
            	FoodItem selectedFood = foodList.getSelectionModel().getSelectedItem();
            	if (selectedFood != null) {
            		menuFoods.add(selectedFood);
            	}
            	Collections.sort(menuFoods, comparatorFoodItembyName);
            });
            
            // remove a food from the menu list
            removeFoodFromMenuButton.setOnAction(e -> {
            	FoodItem selectedFood = menuList.getSelectionModel().getSelectedItem();
            	if (selectedFood != null) {
            		menuFoods.remove(selectedFood);
            	}
            });
            
            
            // -- Menu List Area: --
            
            Label menuListLabel = new Label(MenuListLabel); // Menu List title
            menuListLabel.setFont(new Font(Font, 28));
            menuListLabel.setPadding(new Insets(10, 0, 10, 0));
            
            // Menu food list
            menuList.setPrefWidth(320); // Formerly 323
            menuList.setPrefHeight(320);
            
            Button analyzeMenuButton = new Button(AnalyzeNutritionCaption);
            analyzeMenuButton.setPrefHeight(20);
            
            
            // -- Filter Foods Area (Left Side): --
            
            Label filterFoodsLabel = new Label(FilterActionLabel); // Filter Foods title
            filterFoodsLabel.setFont(new Font(Font, 28));
            filterFoodsLabel.setPadding(new Insets(10, 0, 10, 0));
            
            Label fillerText = new Label(FilterHorizontalSeparator);
            
            Label createRuleLabel = new Label(SearchFilterCaption); 
            createRuleLabel.setFont(new Font(Font, 20));
            createRuleLabel.setPadding(new Insets(10, 0, 10, 0));
            
            // label and combo box for selecting the nutrient for the rule
            Label nutrientLabel = new Label(NutrientLabel);
            
            ComboBox<String> nutrientComboBox = new ComboBox<String>(nutrients);
            
            // combo box for selecting the operator for the rule
            Label operatorLabel = new Label(OperatorLabel);
            
            ComboBox<String> operatorComboBox = new ComboBox<String>(logicOptions);
            
            // text field for entering the nutrient value (example number of grams or number of calories)
            Label valueLabel = new Label(ValueLabel);
            TextField nutrientValueField = new TextField();
            nutrientValueField.setPromptText(NutrientPromptText);
            
            // Add a rule to the active rules
            
            
            Image imageSmallArrowRight = new Image(getClass().getResourceAsStream(SmallRightArrowImage));
            Button addRuleButton = new Button();
            addRuleButton.setGraphic(new ImageView(imageSmallArrowRight));
            Tooltip addRuleTooltip = new Tooltip(AddRuleToolTip);
            addRuleButton.setTooltip(addRuleTooltip);
            addRuleButton.setOnAction(e -> {
            	SingleSelectionModel<String> nutrient = nutrientComboBox.getSelectionModel();
        		SingleSelectionModel<String> operator = operatorComboBox.getSelectionModel();
            	String value = nutrientValueField.getText();
            	//ensure we have valid input and don't already have this rule
            	if (isValidDoubleValue(value) && hasSelection(nutrient) && hasSelection(operator) && !rules.contains(makeRule(nutrient, operator, value))) {
            		rules.add(makeRule(nutrient, operator, value));
            	}
            });
            
            VBox addRuleButtonVBox = new VBox();
            addRuleButtonVBox.getChildren().addAll(addRuleButton);
            
            // -- Filter Foods Area (Right Side): --
            
            // Label for side
            Label activeFilterLabel = new Label(SelectedFiltersLabel);
            activeFilterLabel.setFont(new Font(Font, 20));
            activeFilterLabel.setPadding(new Insets(10, 0, 10, 0));
            
			// Label and Field for entering a text search term
			Label nameFilterLabel = new Label(NameFilterLabel);
			TextField nameFilterField = new TextField();
	        nameFilterField.setPromptText(FoodPromptText);
			
            Label nutrientFilterLabel = new Label(NutrientFiltersLabel); // Label for list of rules
	
            // View of the active rules
            if (DEBUG) {
            	rules.add("Rule1");
            	rules.add("Rule2");
            	rules.add("Rule3");
            }
            ruleList.setPrefWidth(100);
            ruleList.setPrefHeight(70);
            
			// Remove an active rule

			Button removeRuleButton = new Button(RemoveRuleCaption);
			//removeRuleButton.setGraphic(new ImageView(imageExit));
            Tooltip removeRuleTooltip = new Tooltip(RemoveRuleToolTip);
            removeRuleButton.setTooltip(removeRuleTooltip);
            removeRuleButton.setOnAction(e -> {
            	String selected = ruleList.getSelectionModel().getSelectedItem();
            	rules.remove(selected);
            	});
			
			// Run query button
			Button runSearch = new Button(RunSearchCaption);
			runSearch.setMinSize(100, 50);
	
			 HBox searchHBox = new HBox();
			 searchHBox.setPadding(new Insets(0, 0, 0, 0));
			 searchHBox.setSpacing(50);
			 searchHBox.getChildren().addAll(filterFoodsLabel); // Formerly runSearch was included

			 
			// --------- Adding objects to the pane ---------
			

			// -- Food List Area: --
			GridPane.setConstraints(foodListLabel, foodListX, foodListY, 2, 1); // Col span: 2
 			GridPane.setConstraints(foodListButtonsHBox, foodListX, foodListY + 1, 2, 1); // Col span: 2
 			GridPane.setConstraints(foodList, foodListX, foodListY + 2, 2, 2); // Col span: 2, Row span: 2
 			
 			// -- Buttons between Food List and Menu List: --
 			GridPane.setConstraints(addAndRemoveButtonsVBox, foodListX + 2, foodListY + 2, 1, 2);
 			
 			// -- Menu List Area: --
 			GridPane.setConstraints(menuListLabel, menuListX, foodListY, 2, 1); // Col span: 2
 			GridPane.setConstraints(menuList, menuListX, foodListY + 2, 2, 2); // Col span: 2, Row span: 2
 			GridPane.setConstraints(analyzeMenuButton, menuListX, foodListY + 1, 2, 1, HPos.LEFT, VPos.TOP); // Col span: 2
			
 			// -- Horizontal Separator --
 			
 			Image horizontalLineImage = new Image(getClass().getResourceAsStream(HorizontalDividerImage));
 			ImageView horizontalLine = new ImageView(horizontalLineImage);
 			HBox divider = new HBox();
 			divider.getChildren().addAll(horizontalLine);
 			divider.setAlignment(Pos.CENTER_LEFT); // Formerly CENTER
 			GridPane.setConstraints(divider, 0, filterY - 1, 5, 1); // Formerly Col span: 6
 			
 			// -- Filter Foods Area (Left Side): --
 			GridPane.setConstraints(searchHBox, foodListX, filterY, 4, 1); // Col span: 3
 			GridPane.setConstraints(createRuleLabel, foodListX, filterY + 1, 2, 1); // Col span: 2
 			GridPane.setConstraints(fillerText, foodListX, filterY + 2, 2, 1); // Col span: 2
 			GridPane.setHalignment(fillerText, HPos.CENTER);
 			GridPane.setValignment(fillerText, VPos.TOP);
 			
 			GridPane.setConstraints(nutrientLabel, foodListX, filterY + 3);
 			GridPane.setConstraints(nutrientComboBox, foodListX + 1, filterY + 3);
 			
 			GridPane.setConstraints(operatorLabel, foodListX, filterY + 4);
 			GridPane.setConstraints(operatorComboBox, foodListX + 1, filterY + 4);
 			
 			GridPane.setConstraints(valueLabel, foodListX, filterY + 5);
 			GridPane.setConstraints(nutrientValueField, foodListX + 1, filterY + 5);
 			
 			// -- Buttons between filter area and filter list area
 			addRuleButtonVBox.setAlignment(Pos.CENTER);
 			GridPane.setConstraints(addRuleButtonVBox, foodListX + 2, foodListY + 9);
 			
 			// -- Filter Foods Area (Right Side): --
 			GridPane.setConstraints(activeFilterLabel, menuListX, filterY + 1, 2, 1);
 			GridPane.setConstraints(nameFilterLabel, menuListX, filterY + 2);
 			GridPane.setConstraints(nameFilterField, menuListX + 1, filterY + 2);
 			GridPane.setConstraints(nutrientFilterLabel, menuListX, filterY + 3, 2, 1); // Col span: 2
 			GridPane.setValignment(nutrientFilterLabel, VPos.CENTER);
 			GridPane.setConstraints(ruleList, menuListX, filterY + 4, 2, 2); // Col span: 2, row span: 2
 			GridPane.setConstraints(runSearch, menuListX, filterY);
 			
 			VBox removeRuleButtonVBox = new VBox();
 			removeRuleButtonVBox.getChildren().addAll(removeRuleButton);
 			removeRuleButtonVBox.setAlignment(Pos.TOP_RIGHT);
 			GridPane.setConstraints(removeRuleButtonVBox, menuListX + 1, filterY + 3, 1, 1);
 			
 			// Adds all nodes to the Grid Pane
 			gridpane.getChildren().addAll(foodListLabel, foodListButtonsHBox, foodList, 
 			    addAndRemoveButtonsVBox, menuListLabel, menuList, analyzeMenuButton, 
 			   searchHBox, nutrientLabel, nutrientComboBox, operatorLabel, operatorComboBox, 
 			    valueLabel, nutrientValueField, nameFilterLabel, nameFilterField, 
 			    nutrientFilterLabel, ruleList, addRuleButtonVBox,activeFilterLabel,
 			    createRuleLabel,divider, removeRuleButtonVBox, fillerText, runSearch);
            primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * launches the program
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	//Private helper functions
	/**
	 * Generates a horizontal separator of the given character
	 * @param character visible part of separator repetition
	 * @return string separator
	 */
	private static String horizontalSeparator(String character) {
		String ret = "";
		for (int i = 0; i < SeparatorLength; ++ i) {
			ret += character + " ";
		}
		
		return ret;
	}
	/**
	 * Determines if user has an opetion selected
	 * @param value combo box selected item
	 * @return true if an operator is selected
	 */
	private boolean hasSelection(SingleSelectionModel<String> value) {
		if (value == null || value.getSelectedItem() == null) {
			return false;
		}
		return value.getSelectedItem().length() > 0;
	}
	
	/**
	 * User input validator for numeric input.
	 * @param value string being validated
	 * @return true if value matches pattern for double
	 */
	private boolean isValidDoubleValue(String value) {
		boolean good1 = Pattern.matches("^[0-9]+([.][0-9]+)?$", value);
		boolean good2 = Pattern.matches("^[.][0-9]+$", value);
		
		return good1 || good2;
	}
	
	/**
	 * Makes a rule from selections and values
	 * @param nutrient nutrient combo box selection
	 * @param operator operator combo box selection
	 * @param value string representing a double
	 * @return rule concatenating string form of nutrient, operator, and value
	 */
	private String makeRule(SingleSelectionModel<String> nutrient, SingleSelectionModel<String> operator, String value) {
		String nut = nutrient.getSelectedItem();
		String op = operator.getSelectedItem();
		return String.format("%s%s%s", 
				nut,
				op, 
				value);
	}
}