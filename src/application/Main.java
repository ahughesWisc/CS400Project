package application;


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
 * Online Sources: https://youtu.be/GbzKr46VvD0, https://www.mkyong.com/java8/java-8-foreach-examples/
 * 
 * Notes: Minimal error handling occurs in the file processing, To be determined if this requires more work. foodItems.csv
 * has many blank lines at the end of file that creates an annoying popup saying  -Colin Butkus
 * Known Bugs: 
 */

import java.util.function.Function;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ScrollPane;
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
import javafx.scene.text.Text;

import java.util.*; 
import javafx.stage.Modality;
import javafx.stage.FileChooser;


import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javafx.scene.text.TextAlignment;

/**
 * Class used for creating and displaying the GUI and 
 * launching the food program
 *
 */
public class Main extends Application {

	//Data Structures
	//list to store each line of string from file
	static List<List<String>> listOfLines = new ArrayList<>();

	//Settings
	final static String Font = "Arial"; //now you can change the font anywhere in the application from one place
	final static String BannerFont = "Arial Bold";
	final static int SeparatorLength = 29;
	final static String FilterHorizontalSeparator = horizontalSeparator("v");
	final static String Equals = "==";
	final static String LessThanOrEquals = "<=";
	final static String GreaterThanOrEquals = ">=";
	final static String FoodFileName = "foodItems.csv";

	final static int foodListY = 1;
	final static int foodListX = 0;
	final static int menuListX = 3;
	final static int filterY = 7;

	//Similar to a .NET Resources.resx file, these sections are used so we can easily change string values in the UI without poking around everywhere
	//in the code. It also means that we can avoid spelling mistakes if we reuse the strings because we only have to spell them once explicitly.
	final static String Calories = "Calories";
	final static String Fat = "Fat";
	final static String Carbohydrate = "Carbohydrate";
	final static String Protein = "Protein";
	final static String Fiber = "Fiber";

	//Captions/Labels
	final static String Title = "Foodapolooza";
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
	final static String ClearSearchCaption = "Clear Search";
	final static String ValueLabel = "Value";
	final static String RemoveRuleCaption = "Remove Selected Rule";
	final static String AcceptButtonCaption = "OK";
	final static String AnalysisPopupTitle = "Nutritional Analysis";
	final static String ErrorPopupTitle = "Error";

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
	final static boolean DEBUG = false;

	//Class Attributes
	private ObservableList<String> rules = FXCollections.observableArrayList();
	private ListView<String> ruleList = new ListView<String>(rules);
	private FoodData foodData = new FoodData();
	private ObservableList<FoodItem> foods = FXCollections.observableArrayList();
	private ListView<FoodItem> foodList = new ListView<FoodItem>(foods);
	private ObservableList<FoodItem> menuFoods = FXCollections.observableArrayList();
	private ListView<FoodItem> menuList = new ListView<FoodItem>(menuFoods);
	private final static ObservableList<String> nutrients = 
			FXCollections.observableArrayList(
					Calories,
					Fat,
					Carbohydrate,
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
			Scene scene = new Scene(borderpane, 768, 860); // width, height
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
			exitProgramButton.setOnMouseClicked(actionEvent -> primaryStage.close());
		

			AnchorPane ap = new AnchorPane();
			ap.getChildren().addAll(banner, exitProgramButton);
			AnchorPane.setLeftAnchor(banner, 0.0);
			AnchorPane.setRightAnchor(exitProgramButton, 0.0);
			borderpane.setTop(ap);
			ap.setMaxWidth(768);
			ap.setMinWidth(768);

			// comparator for sorting food lists by name
			Comparator<? super FoodItem> comparatorFoodItembyName = new Comparator<FoodItem>() {
				@Override
				public int compare(FoodItem food1, FoodItem food2) {
					return food1.getName().compareToIgnoreCase(food2.getName());
				}
			};
			
			// -- Food List Area: --
			Label foodCount = new Label(amountInTheFoodList(foods.size()));
			Label foodListLabel = new Label(FoodListLabel); // Food List title
			foodListLabel.setFont(new Font(Font, 28));
			foodListLabel.setPadding(new Insets(10, 10, 10, 0));

			// HBox for buttons in food list area
			HBox foodListButtonsHBox = new HBox(); // Load, add, and save buttons in Food List area
			foodListButtonsHBox.setSpacing(10);
			
			// button for adding foods from file
			Button loadFoodsButton = new Button(LoadFoodsCaption);
			loadFoodsButton.setPrefSize(100, 20); // Sets width and height of button
			loadFoodsButton.setOnAction(e -> {
				displayLoadFile();
				Collections.sort(foods,comparatorFoodItembyName);
				foodCount.setText(amountInTheFoodList(foods.size()));
			});//call displayLoadFile when Load Food Button clicked, opens popup window
			
			// button for adding foods from form
			Button addFoodButton = new Button(AddFoodCaption);
			addFoodButton.setPrefSize(100, 20); // Sets width and height of button
			addFoodButton.setOnAction(e -> {
				displayAddFood();
				Collections.sort(foods, comparatorFoodItembyName);
				foodCount.setText(amountInTheFoodList(foods.size()));
			});//call displayAddFood when Add Food Button clicked, opens popup window
			
			Button saveFoodsButton = new Button(SaveFoodsCaption);
			saveFoodsButton.setPrefSize(100, 20); // Sets width and height of button
			saveFoodsButton.setOnAction(e -> displaySaveFile());
			foodListButtonsHBox.getChildren().addAll(loadFoodsButton, addFoodButton, saveFoodsButton);

			// Master food list            
			if (DEBUG) { //testing only
				FoodItem blackberries = new FoodItem("1","Blackberries");
				blackberries.addNutrient("calories", 5.0);
				foodData.addFoodItem(blackberries);
				FoodItem blueberries = new FoodItem("A","Blueberries");
				foodData.addFoodItem(blueberries);
				FoodItem raspberries = new FoodItem("12312","Raspberries");
				foodData.addFoodItem(raspberries);
				FoodItem strawberries = new FoodItem("2","Strawberries");
				foodData.addFoodItem(strawberries);
				foods = FXCollections.observableArrayList(foodData.getAllFoodItems());
				foodList = new ListView<FoodItem>(foods);
			}
			

			
			foodList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			foodList.setPrefWidth(320);
			foodList.setPrefHeight(320);
			foodList.setMinHeight(320);

			
			//foodCount.textProperty().bind(foodList.itemsProperty().);

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
			addAndRemoveButtonsVBox.setSpacing(10);
			addAndRemoveButtonsVBox.setAlignment(Pos.CENTER);
			addAndRemoveButtonsVBox.getChildren().addAll(addFoodtoMenuButton, removeFoodFromMenuButton);

			

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
			menuList.setPrefWidth(320);
			menuList.setPrefHeight(320);

			Button analyzeMenuButton = new Button(AnalyzeNutritionCaption);
			analyzeMenuButton.setPrefHeight(20);
			analyzeMenuButton.setOnAction(e -> displayNutrients(menuFoods));


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
				if (isValidDoubleValue(value) && hasSelection(nutrient) && hasSelection(operator) && 
						!rules.contains(makeRule(nutrient, operator, value))) {
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
				rules.add("Fiber == 20");
				rules.add("Fat >= 5");
				rules.add("Protein >= 3");
			}
			ruleList.setPrefWidth(100);
			ruleList.setPrefHeight(70);

			// Remove an active rule

			Button removeRuleButton = new Button(RemoveRuleCaption);
			Tooltip removeRuleTooltip = new Tooltip(RemoveRuleToolTip);
			removeRuleButton.setTooltip(removeRuleTooltip);
			removeRuleButton.setOnAction(e -> {
				String selected = ruleList.getSelectionModel().getSelectedItem();
				rules.remove(selected);
			});

			//Run query button
			Button runSearch = new Button(RunSearchCaption);
			runSearch.setMinSize(100, 50);
			runSearch.setOnAction(e -> {runTheSearch(nameFilterField.getText(), rules, foodData, foods);
					foodCount.setText(amountInTheFoodList(foods.size()));});

			// Clear query button
			Button clearSearch = new Button(ClearSearchCaption);
			clearSearch.setMinSize(100, 50);
			clearSearch.setOnAction(e -> {
				clearTheSearch(foodData, foods);
				Collections.sort(foods, comparatorFoodItembyName);
				foodCount.setText(amountInTheFoodList(foods.size()));
			});


			// --------- Adding objects to the pane ---------


			// -- Food List Area: --
			GridPane.setConstraints(foodListLabel, foodListX, foodListY, 2, 1); // Col span: 2
			GridPane.setConstraints(foodListButtonsHBox, foodListX, foodListY + 1, 2, 1); // Col span: 2
			GridPane.setConstraints(foodList, foodListX, foodListY + 2, 2, 2); // Col span: 2, Row span: 2
			GridPane.setConstraints(foodCount, foodListX, foodListY+4, 3, 1); // Col span: 3

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
			GridPane.setConstraints(filterFoodsLabel, foodListX, filterY, 4, 1); // Col span: 3
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
			GridPane.setConstraints(clearSearch, menuListX + 1, filterY);

			VBox removeRuleButtonVBox = new VBox();
			removeRuleButtonVBox.getChildren().addAll(removeRuleButton);
			removeRuleButtonVBox.setAlignment(Pos.TOP_RIGHT);
			GridPane.setConstraints(removeRuleButtonVBox, menuListX + 1, filterY + 3, 1, 1);

			// Adds all nodes to the Grid Pane
			gridpane.getChildren().addAll(foodListLabel, foodListButtonsHBox, foodList, 
					addAndRemoveButtonsVBox, menuListLabel, menuList, analyzeMenuButton, 
					filterFoodsLabel, nutrientLabel, nutrientComboBox, operatorLabel, operatorComboBox, 
					valueLabel, nutrientValueField, nameFilterLabel, nameFilterField, 
					nutrientFilterLabel, ruleList, addRuleButtonVBox,activeFilterLabel,
					createRuleLabel,divider, removeRuleButtonVBox, fillerText, runSearch, 
					clearSearch,foodCount);
			primaryStage.show();

			// add foodItems from the directory foodItems.csv file
			try {
				File foodFile = new File(FoodFileName);
				loadFile(foodFile);
				Collections.sort(foods, comparatorFoodItembyName);
				foodCount.setText(foods.size() + " in the availible food list");
			} catch(Exception e) {
				displayError("There was an error loading foods from foodItems.csv. Use the Load Food and Add Food "
						+ "buttons to add foods to the program.");
			}
			
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
	private String makeRule(SingleSelectionModel<String> nutrient, SingleSelectionModel<String> operator,
			String value) {
		String nut = nutrient.getSelectedItem();
		String op = operator.getSelectedItem();
		return String.format("%s %s %s", 
				nut,
				op, 
				value);
	}

	/**
	 * Loads food from selected file and displays it.
	 * Method is called when the LoadFood button is clicked in the main GUI.
	 */
	public void displayLoadFile()
	{
		Stage loadPopupWindow =new Stage();
		FileChooser fileChooser = new FileChooser();

		//this object will be used to have user select file from their computer
		fileChooser.setTitle("Open Resource File");
		File selectedFile = fileChooser.showOpenDialog(loadPopupWindow);


		loadPopupWindow.initModality(Modality.APPLICATION_MODAL);
		loadPopupWindow.setTitle("Load File Pop Up Screen");

		//creating a gridpane to store the javafx buttons and text fields in
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.TOP_CENTER);
		gridPane.setPadding(new Insets(20, 12, 20, 12));
		gridPane.setHgap(10); // Horizontal gap between rows
		gridPane.setVgap(10); // Vertical gap between columns

		//create label to place on top of selected file
		Label fileLabel = new Label("File Selected"); 
		fileLabel.setFont(new Font(Font, 14));

		//create label to describe the file format
		Label fileFormatTitle = new Label("File Format Strictly Enforced - Each Input Line Must Follow:");
		
		
		Label fileFormat = new Label("<id>,<food_name>,<calories>,<calorie_count>,<fat>,<fat_grams>,<carbohydrate>,<carbohydrate_grams>,<fiber>,<fiber_grams>,<protein>,<protein_grams>"); 
		fileFormat.setWrapText(true);
		fileFormat.setFont(new Font(Font, 10));
		
		Label fileExampleFormatTitle = new Label("Example Format of Line of input:"); 
		
		Label fileExampleFormat = new Label("556540ff5d613c9d5f5935a9,Stewarts_PremiumDarkChocolatewithMintCookieCrunch,calories,280,fat,18,carbohydrate,34,fiber,3,protein,3"); 
		fileExampleFormat.setFont(new Font(Font, 10));
		Label fileFailFormat = new Label("Failure to adhere will result in line input being ignored or nutrition data stored in wrong data field"); 
		
		
		//fileFormat.setTextAlignment(TextAlignment.JUSTIFY);
		//create a cancel button to close out of load popup screen     
		Button cancelButton= new Button("Cancel");
		cancelButton.setPrefSize(80,40);     
		cancelButton.setOnAction(e -> loadPopupWindow.close());

		//create a load button that takes the selected file and parses it and saves it into our FoodData structure
		Button loadButton= new Button("Load");
		loadButton.setPrefSize(80,40);
		//loads file into data structure when load button is clicked
		loadButton.setOnAction(e -> { 
			
			// per the spec, loading a new food file should clear all food data.
			foodData = new FoodData();
			foods.clear();
			loadFile(selectedFile); 
			loadPopupWindow.close();
		});
		
		HBox buttonBox = new HBox();
		buttonBox.setSpacing(520);
		buttonBox.getChildren().addAll(cancelButton, loadButton);

		//Create text field that shows the path of the file selected
		TextField nameFilterField = new TextField("No File Selected");
		nameFilterField.setPrefWidth(600);
		nameFilterField.setDisable(true);


		//add buttons,label and textbox to grid
		gridPane.add(nameFilterField, 0, 1, 3, 1); 
		gridPane.add(fileLabel, 0, 0, 1, 1);
		gridPane.add(buttonBox, 0, 2, 3, 1);
		gridPane.add(fileFormatTitle, 0, 4, 1, 1);
		gridPane.add(fileFormat, 0, 5, 3, 1);
		gridPane.add(fileExampleFormatTitle,0,6,1,1);
		gridPane.add(fileExampleFormat,0,7,3,1);
		gridPane.add(fileFailFormat,0,8,3,1);
		//add gridPane to the scene      
		Scene scene1= new Scene(gridPane, 800, 300);

		//set the scene to the popup window
		loadPopupWindow.setScene(scene1);



		if( selectedFile == null ) {
			nameFilterField.setText("No File Selected");

		}
		else {
			nameFilterField.setText(selectedFile.toString());

		}

		//make the load Popup window visible
		loadPopupWindow.showAndWait();
	}//end of displayLoadFile()
	
	/**
	 * Saves food data to a file. If the file provided by the user has no extension, it is assumed to be a .csv file
	 */
	public void displaySaveFile()
	{
		Stage loadPopupWindow =new Stage();
		FileChooser fileChooser = new FileChooser();

		File saveFile = fileChooser.showSaveDialog(loadPopupWindow);
		//save empty file first
		String content = "";
		String filePath = saveFile.getAbsolutePath();
		System.out.println(saveFile.getName());
		System.out.println(saveFile.getName().contains("."));
		if(!saveFile.getName().contains(".")) {
			filePath = filePath + ".csv";
		}

		try {
			Files.write(Paths.get(filePath), content.getBytes());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//add to lines to file
		
		Iterator<FoodItem> itr = foodData.getAllFoodItems().iterator();
		while(itr.hasNext()) {
			FoodItem foodItemTemp = (FoodItem) itr.next();
			String appendLineToFile = lineAppend(foodItemTemp);
			try {
				Files.write(Paths.get(filePath), appendLineToFile.getBytes(),StandardOpenOption.APPEND);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
	}//end of save to file
	
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
	 * Displays a nutritional analysis of the foods in the menu
	 * @param menuFoods list of food items in the meal to be analyzed
	 */
	public static void displayNutrients(ObservableList<FoodItem> menuFoods) {
		// set up the stage and the gridpane
		Stage loadPopupWindow =new Stage();

		loadPopupWindow.initModality(Modality.APPLICATION_MODAL);
		loadPopupWindow.setTitle(AnalysisPopupTitle);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPadding(new Insets(20, 12, 20, 12));

		// variables for storing combined information
		double calories = 0;
		double fat = 0;
		double carbohydrate = 0;
		double protein = 0;
		double fiber = 0;
		String foods = "";

		// convert the menuFoods list into a map of unique food items to the volume of items
		Map<FoodItem,Long> menuMap = menuFoods.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		// iterate through the map to calculate nutrition information
		for (Map.Entry<FoodItem, Long> entry : menuMap.entrySet()) {
			
			// format food names in a list
			if (foods == "") {
				foods = "\t" + entry.getValue() + "x " + entry.getKey().getName();
			} else {
				foods = foods + "\n\t\t\t\t " + entry.getValue() + "x " + entry.getKey().getName();
			}

			// add nutrient values
			calories = calories + entry.getValue()*entry.getKey().getNutrientValue("calories");	
			fat = fat + entry.getValue()*entry.getKey().getNutrientValue("fat");
			carbohydrate = carbohydrate + entry.getValue()*entry.getKey().getNutrientValue("carbohydrate");
			protein = protein + entry.getValue()*entry.getKey().getNutrientValue("protein");
			fiber = fiber + entry.getValue()*entry.getKey().getNutrientValue("fiber");
		}

		// create text for displaying nutrients
		Text output = new Text("");
		if (menuFoods.size() > 0 ) {
			output = new Text("Foods in Meal:\t" + foods + "\n\n" + "Total calories:\t\t" + calories +
			"\n" + "Total fat(g):\t\t" + fat +"\nTotal carbs(g):\t\t" + carbohydrate + "\nTotal protein(g):\t" +
			protein + "\nTotal fiber(g):\t\t" + fiber);
		}
		else {
			output = new Text("\n\n\n\t\t\t\tAdd some foods to the meal to be analyzed!");
		}
				
		
		// add all elements to the grid
		scrollPane.setContent(output);
		
		//add gridPane to the scene      
		Scene scene1= new Scene(scrollPane, 500, 400);

		//set the scene to the popup window
		loadPopupWindow.setScene(scene1);
		loadPopupWindow.showAndWait();
	}

	/**
	 * Displays added food.
	 * Method is called when the addFood button is clicked from main GUI
	 */
	public void displayAddFood()
	{
		Stage loadPopupWindow =new Stage();

		loadPopupWindow.initModality(Modality.APPLICATION_MODAL);
		loadPopupWindow.setTitle("Add Food Pop Up Screen");

		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setPadding(new Insets(20, 12, 20, 12));
		gridPane.setHgap(10); // Horizontal gap between rows
		gridPane.setVgap(10); // Vertical gap between columns

		//create labels for name of food and nutrients for user input
		Label idLabel = new Label(MakeLabel("ID"));
		idLabel.setFont(new Font(Font,14));
		
		Label nameLabel = new Label(MakeLabel("Name")); 
		nameLabel.setFont(new Font(Font, 14));

		Label caloriesLabel = new Label(MakeLabel(Calories)); 
		caloriesLabel.setFont(new Font(Font, 14));

		Label fatLabel = new Label(MakeLabel(Fat)); 
		fatLabel.setFont(new Font(Font, 14));

		Label carbLabel = new Label(MakeLabel(Carbohydrate)); 
		carbLabel.setFont(new Font(Font, 14));

		Label fiberLabel = new Label(MakeLabel(Fiber)); 
		fiberLabel.setFont(new Font(Font, 14));

		Label proteinLabel = new Label(MakeLabel(Protein)); 
		proteinLabel.setFont(new Font(Font, 14));

		TextField idField = new TextField();
		idField.setPrefWidth(100);
		
		TextField nameField = new TextField();
		nameField.setPrefWidth(100);

		TextField caloriesField = new TextField();
		caloriesField.setPrefWidth(100);

		TextField fatField = new TextField();
		fatField.setPrefWidth(100);

		TextField carbField = new TextField();
		carbField.setPrefWidth(100);

		TextField fiberField = new TextField();
		fiberField.setPrefWidth(100);

		TextField proteinField = new TextField();
		proteinField.setPrefWidth(100);

		//create a cancel button to close out of load popup screen     
		Button cancelButton= new Button("Cancel");
		cancelButton.setPrefSize(80,40);     
		cancelButton.setOnAction(e -> loadPopupWindow.close());

		//create a add button that takes the user input from textfields and saves it into our FoodData structure
		Button loadButton= new Button("Add");
		loadButton.setPrefSize(80,40);
		loadButton.setOnAction(e -> {
			addFoodtoFoodData(true,idField.getText(),nameField.getText(),caloriesField.getText(),fatField.getText(),
					carbField.getText(),fiberField.getText(),proteinField.getText(),loadPopupWindow);
		});

		//add buttons,label and textbox to grid
		gridPane.add(idLabel, 0, 0,1,1);
		gridPane.add(idField, 1, 0,1,1);
		gridPane.add(nameField, 1, 1, 1, 1); 
		gridPane.add(nameLabel, 0, 1, 1, 1);
		gridPane.add(caloriesLabel, 0, 2, 1, 1);
		gridPane.add(caloriesField, 1,2,1,1);
		gridPane.add(fatLabel, 0, 3, 1, 1);
		gridPane.add(fatField, 1,3,1,1);
		gridPane.add(carbLabel, 0, 4, 1, 1);
		gridPane.add(carbField, 1,4,1,1);
		gridPane.add(fiberLabel, 0, 5, 1, 1);
		gridPane.add(fiberField, 1,5,1,1);
		gridPane.add(proteinLabel, 0, 6, 1, 1);
		gridPane.add(proteinField, 1,6,1,1);
		gridPane.add(cancelButton, 0, 7, 1, 1);
		gridPane.add(loadButton, 1, 7, 1, 1);

		//add gridPane to the scene      
		Scene scene1= new Scene(gridPane, 275, 325);

		//set the scene to the popup window
		loadPopupWindow.setScene(scene1);
		loadPopupWindow.showAndWait();

	}//end of displayAddFood()
	
	/**
	 * Generates a label from a string
	 * @param str string to be the label
	 * @return str plus colon plus space
	 */
	private String MakeLabel(String str) {
		return String.format("%s: ", str);
	}
	
	/**
	 * Validates user entered data and adds foods to the food data.
	 * May optionally display error messages based on first parameter
	 * @param triggerErrors
	 * @param id
	 * @param name
	 * @param calories
	 * @param fat
	 * @param carbohydrate
	 * @param fiber
	 * @param protein
	 * @param stage
	 * @return true if successful
	 */
	private boolean addFoodtoFoodData(Boolean triggerErrors,String id, String name,String calories,String 
			fat,String carbohydrate,String fiber,String protein, Stage stage) {
		if (foodData.isUniqueID(id)) {
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
							foodData.addFoodItem(foodItem);
							foods.add(foodItem);
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
	 * Used to produce repeatedly used string
	 * @param number number of items available
	 * @return the user string needed to describe availability in the food list based on the amount
	 */
	private String amountInTheFoodList(int number) {
		return String.format("%d in the available food list", number);
	}
	
	/**
	 * displays an error message with an okay button to the user
	 * @param message - String message to display
	 */
	private void displayError(String message) {
		Stage loadPopupWindow =new Stage();

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
	
	/**
	 * Loads the selected file into a list and saves it into the ID list, name list,
	 * and hashmap list of nutrients
	 * @param selectedFile name of the file
	 */
	private void loadFile(File selectedFile) {

		//check if file selected was not null
		if( selectedFile == null ) {
			System.out.println("File Name Missing");

		}
		else {

			try (Stream<String> stream = Files.lines(Paths.get(selectedFile.toString()))) {

				listOfLines = stream
						.map(line -> Arrays.asList(line.split(",")))
						.collect(Collectors.toList());

			} catch (IOException e) {
				e.printStackTrace();
				displayError("There was an error loading foods from" + selectedFile +". Adjust the file "
						+ "input, and use the Load Food and Add Food buttons to add foods to the program.");
			}
			catch (Exception e) {
				e.printStackTrace();
				displayError("There was an error loading foods from" + selectedFile +". Adjust the file "
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
		}//end of else statement
	}//end of loadFile(File selectedFile)
	
	/**
	 * Performs the requested search.
	 * Method is called when the runSearch button is clicked from main GUI
	 * 
	 * @param foodName name of the food to search for
	 * @param rules the list of rules to search for
	 * @param foodData FoodData object that enables searching
	 * @param foods list of foods to run the search on
	 */
	public static void runTheSearch(String foodName, ObservableList<String> 
	    rules, FoodData foodData, ObservableList<FoodItem> foods) {
	  
	  List<FoodItem> nameSearchResults = new ArrayList<FoodItem>(); // Stores the results of filtering by name
	  List<FoodItem> nutrientSearchResults = new ArrayList<FoodItem>(); // Stores the results of filtering by nutrients
	  List<FoodItem> result = new ArrayList<FoodItem>(); // Stores the final intersected food list

	  // If no foodName is entered, then the text field returns an empty String. Checks for empty String
	  if (foodName.length() > 0) {
	    nameSearchResults = foodData.filterByName(foodName);
	  }

	  // Checks there are foods to search
	  if (foods.size() > 0) {
	    nutrientSearchResults = foodData.filterByNutrients(rules);
	  }
	  
	  // Intersects lists, if needed
	  if (nameSearchResults != null && nutrientSearchResults != null) {
	    
	    if (nameSearchResults.size() > 0 && nutrientSearchResults.size() > 0) {

	      result = nameSearchResults.stream()
	          .filter(nutrientSearchResults::contains)
	          .collect(Collectors.toList());

	    } else if (nameSearchResults.size() > 0) {
	      result = nameSearchResults;

	    } else if (nutrientSearchResults.size() > 0) {
	      result = nutrientSearchResults;
	    }

	  } else if (nameSearchResults != null) {
	    result = nameSearchResults;

	  } else if (nutrientSearchResults != null) {
	    result = nutrientSearchResults;
	  }
	  
	  // If results isn't empty, then it is sorted alphabetically, case insensitive
	  if (!result.isEmpty()) {
	    result.sort(Comparator.comparing(FoodItem::getName, String.CASE_INSENSITIVE_ORDER));
	  }
	  
	  // Clear foods
	  foods.clear();
	  
	  // Rebuild foods
	  result.forEach(foodItem -> foods.add(foodItem));
	      
	} // end of runTheSearch

	/**
	 * Clears search parameters.
	 * Method is called when the clearSearch button is clicked from main GUI
	 * 
	 * @param foodData FoodData object used to rebuild food list
	 * @param foods list of foods to display in the food list
	 */
	public static void clearTheSearch(FoodData foodData, ObservableList<FoodItem> foods) {
	  
	  // Clear foods
	  foods.clear();
	  
	  // Rebuild foods
	  foodData.getAllFoodItems().forEach(foodItem -> foods.add(foodItem));
	} // end of clearTheSearch
	

}