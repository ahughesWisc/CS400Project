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
 * Online Sources: https://youtu.be/GbzKr46VvD0
 * 
 * Notes:
 * Known Bugs: 
 */

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
import javafx.scene.control.SelectionMode;
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
		    gridpane.setGridLinesVisible(false); // FIXME set to false when not testing
		    borderpane.setCenter(gridpane);
			Scene scene = new Scene(borderpane, 768, 830); // width, height
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			// --------- Creating Objects ---------
			
			// Set the application header
			primaryStage.setTitle("Food Program");	

			// Background
			Image background = new Image(getClass().getResourceAsStream("fruitBackgroundSmall.png"));
			BackgroundImage fruitBackground = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, 
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
			gridpane.setBackground(new Background(fruitBackground));
			
			// Banner
			HBox banner = new HBox();
			Label bannerTitle = new Label("Food Program");
			bannerTitle.setFont(new Font("Arial Bold", 22));
			bannerTitle.setPadding(new Insets(8, 10, 8, 10));
			banner.getChildren().add(bannerTitle);
			
			// Exit button for the program
			Image imageSmallArrowLeft = new Image(getClass().getResourceAsStream("delte.png"));
            Button exitProgramButton = new Button();
            exitProgramButton.setGraphic(new ImageView(imageSmallArrowLeft));
            Tooltip exitProgramTooltip = new Tooltip("Exit Program");
            exitProgramButton.setTooltip(exitProgramTooltip);
            
            AnchorPane ap = new AnchorPane();
            ap.getChildren().addAll(banner, exitProgramButton);
            AnchorPane.setLeftAnchor(banner, 0.0);
            AnchorPane.setRightAnchor(exitProgramButton, 48.0); // Sets position of exitProgramButton
            borderpane.setTop(ap);
			
			// -- Food List Area: --
	
			Label foodListLabel = new Label("Food List"); // Food List title
			foodListLabel.setFont(new Font("Arial", 28));
			foodListLabel.setPadding(new Insets(10, 10, 10, 0));
			
			// HBox for buttons in food list area
            HBox foodListButtonsHBox = new HBox(); // Load, add, and save buttons in Food List area
            //foodListButtonsHBox.setPadding(new Insets(15, 12, 15, 12));
            foodListButtonsHBox.setSpacing(10);
            Button loadFoodsButton = new Button("Load Foods");
            loadFoodsButton.setPrefSize(100, 20); // Sets width and height of button
            Button addFoodButton = new Button("Add Food");
            addFoodButton.setPrefSize(100, 20); // Sets width and height of button
            Button saveFoodsButton = new Button("Save Foods");
            saveFoodsButton.setPrefSize(100, 20); // Sets width and height of button
            foodListButtonsHBox.getChildren().addAll(loadFoodsButton, addFoodButton, saveFoodsButton);
            
            // Master food list
            ObservableList<String> foods = FXCollections.observableArrayList();
            ListView<String> foodList = new ListView<String>(foods);
            // The following line is for testing
            foodList.getItems().addAll("Blackberries", "Blueberries", "Raspberries", "Strawberries");
            // Makes multiple selections possible when hitting ctrl
            foodList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            foodList.setPrefWidth(323);
            foodList.setPrefHeight(320);
            foodList.setMinHeight(320);
            
            
            // -- Buttons between Food List and Menu List: --
            
            // Add food to menu button
            Image imageArrowRight = new Image(getClass().getResourceAsStream("goFoward.png"));
            Button addFoodtoMenuButton = new Button();
            addFoodtoMenuButton.setGraphic(new ImageView(imageArrowRight));
            Tooltip addFoodTooltip = new Tooltip("Add the selected foods to the menu");
            addFoodtoMenuButton.setTooltip(addFoodTooltip);
            
            // Remove food from menu button
            Image imageArrowLeft = new Image(getClass().getResourceAsStream("goBack.png"));
            Button removeFoodFromMenuButton = new Button();
            removeFoodFromMenuButton.setGraphic(new ImageView(imageArrowLeft));
            Tooltip removeFoodTooltip = new Tooltip("Remove the selected foods from the menu");
            removeFoodFromMenuButton.setTooltip(removeFoodTooltip);
            
            VBox addAndRemoveButtonsVBox = new VBox();
            //addAndRemoveButtonsVBox.setPadding(new Insets(15, 12, 15, 12));
            addAndRemoveButtonsVBox.setSpacing(10);
            addAndRemoveButtonsVBox.setAlignment(Pos.CENTER);
            addAndRemoveButtonsVBox.getChildren().addAll(addFoodtoMenuButton, removeFoodFromMenuButton);
            
            
            // -- Menu List Area: --
            
            Label menuListLabel = new Label("Menu List"); // Menu List title
            menuListLabel.setFont(new Font("Arial", 28));
            menuListLabel.setPadding(new Insets(10, 0, 10, 0));
            
            // Menu food list
            ObservableList<String> menuFoods = FXCollections.observableArrayList();
            ListView<String> menuList = new ListView<String>(menuFoods);
            menuList.setPrefWidth(323);
            menuList.setPrefHeight(320);
            
            Button analyzeMenuButton = new Button("Nutritional Analysis");
            analyzeMenuButton.setPrefHeight(20);
            
            
            // -- Filter Foods Area (Left Side): --
            
            Label filterFoodsLabel = new Label("Food and Nutrient Search"); // Filter Foods title, formerly "Filter Foods"
            filterFoodsLabel.setFont(new Font("Arial", 28));
            filterFoodsLabel.setPadding(new Insets(10, 0, 10, 0));
            
            Label fillerText = new Label("v v v v v v v v v v v v v v v v v v v v v v v v v v v v v");
            
            Label createRuleLabel = new Label("New Search Filter"); // Formerly "Create a Rule"
            createRuleLabel.setFont(new Font("Arial", 20));
            createRuleLabel.setPadding(new Insets(10, 0, 10, 0));
            
            // label and combo box for selecting the nutrient for the rule
            Label nutrientLabel = new Label("Nutrient");
            ObservableList<String> nutrients = 
                    FXCollections.observableArrayList(
                        "Calories",
                        "Fat",
                        "Carbs",
                        "Fiber",
                        "Protein"
                    );
            ComboBox<String> nutrientComboBox = new ComboBox<String>(nutrients);
            
            // combo box for selecting the operator for the rule
            Label operatorLabel = new Label("Operator");
            ObservableList<String> logicOptions = 
                    FXCollections.observableArrayList(
                        "==",
                        "<=",
                        ">="
                    );
            ComboBox<String> operatorComboBox = new ComboBox<String>(logicOptions);
            
            // text field for entering the nutrient value (example number of grams or number of calories)
            Label valueLabel = new Label("Value");
            TextField nutrientValueField = new TextField();
            nutrientValueField.setPromptText("enter value");
            
            // Add a rule to the active rules
            Image imageSmallArrowRight = new Image(getClass().getResourceAsStream("smallGoFoward.png"));
            Button addRuleButton = new Button();
            addRuleButton.setGraphic(new ImageView(imageSmallArrowRight));
            Tooltip addRuleTooltip = new Tooltip("Add the rule to the search");
            addRuleButton.setTooltip(addRuleTooltip);
            
            VBox addRuleButtonVBox = new VBox();
            addRuleButtonVBox.getChildren().addAll(addRuleButton);
            
            // -- Filter Foods Area (Right Side): --
            
            // Label for side
            Label activeFilterLabel = new Label("Selected Filters");
            activeFilterLabel.setFont(new Font("Arial", 20));
            activeFilterLabel.setPadding(new Insets(10, 0, 10, 0));
            
			// Label and Field for entering a text search term
			Label nameFilterLabel = new Label("Search Term");
			TextField nameFilterField = new TextField();
	        nameFilterField.setPromptText("enter food name");
			
            Label nutrientFilterLabel = new Label("Nutrient Filters"); // Label for list of rules
	
            // View of the active rules
            ObservableList<String> rules =FXCollections.observableArrayList();
            ListView<String> ruleList = new ListView<>(rules);
            ruleList.setPrefWidth(100);
            ruleList.setPrefHeight(70);
            
			// Remove an active rule

			Button removeRuleButton = new Button();
			removeRuleButton.setGraphic(new ImageView(imageSmallArrowLeft));
            Tooltip removeRuleTooltip = new Tooltip("Remove the selected rules from the search");
            removeRuleButton.setTooltip(removeRuleTooltip);
			
			// Run query button
			Button runSearch = new Button("Run Search");
			runSearch.setMinSize(100, 50);
	
			 HBox searchHBox = new HBox();
			 searchHBox.setPadding(new Insets(0, 0, 0, 0));
			 searchHBox.setSpacing(50);
			 searchHBox.getChildren().addAll(filterFoodsLabel); // Formerly runSearch was included

			 
			// --------- Adding objects to the pane ---------
			

			// -- Food List Area: --
			int foodListY = 1;
			int foodListX = 0;
			int menuListX = 3;
			int filterY = 6;
			GridPane.setConstraints(foodListLabel, foodListX, foodListY, 2, 1); // Col span: 2
 			GridPane.setConstraints(foodListButtonsHBox, foodListX, foodListY+1, 2, 1); // Col span: 2
 			GridPane.setConstraints(foodList, foodListX, foodListY+2, 2, 2); // Col span: 2, Row span: 2
 			
 			// -- Buttons between Food List and Menu List: --
 			GridPane.setConstraints(addAndRemoveButtonsVBox, foodListX+2, foodListY+2,1,2);
 			
 			// -- Menu List Area: --
 			GridPane.setConstraints(menuListLabel, menuListX, foodListY, 2, 1); // Col span: 2
 			GridPane.setConstraints(menuList, menuListX, foodListY+2, 2, 2); // Col span: 2, Row span: 2
 			GridPane.setConstraints(analyzeMenuButton, menuListX, foodListY+1, 2, 1, HPos.LEFT, VPos.TOP); // Col span: 2
			
 			// -- Horizontal Separator --
 			
 			Image horizontalLineImage = new Image(getClass().getResourceAsStream("horizontalLine.png"));
 			ImageView horizontalLine = new ImageView(horizontalLineImage);
 			HBox divider = new HBox();
 			divider.getChildren().addAll(horizontalLine);
 			divider.setAlignment(Pos.CENTER_LEFT); // Formerly CENTER
 			GridPane.setConstraints(divider, 0, filterY-1, 5, 1); // Formerly Col span: 6
 			
 			// -- Filter Foods Area (Left Side): --
 			GridPane.setConstraints(searchHBox, foodListX, filterY, 4, 1); // Col span: 3
 			GridPane.setConstraints(createRuleLabel, foodListX, filterY+1, 2, 1); // Col span: 2
 			GridPane.setConstraints(fillerText, foodListX, filterY+2, 2, 1); // Col span: 2
 			GridPane.setHalignment(fillerText, HPos.CENTER);
 			GridPane.setValignment(fillerText, VPos.TOP);
 			
 			GridPane.setConstraints(nutrientLabel, foodListX, filterY+3);
 			GridPane.setConstraints(nutrientComboBox, foodListX+1, filterY+3);
 			
 			GridPane.setConstraints(operatorLabel, foodListX, filterY+4);
 			GridPane.setConstraints(operatorComboBox, foodListX+1, filterY+4);
 			
 			GridPane.setConstraints(valueLabel, foodListX, filterY+5);
 			GridPane.setConstraints(nutrientValueField, foodListX+1, filterY+5);
 			
 			// -- Buttons between filter area and filter list area
 			addRuleButtonVBox.setAlignment(Pos.CENTER);
 			GridPane.setConstraints(addRuleButtonVBox, foodListX+2, foodListY+9);
 			
 			// -- Filter Foods Area (Right Side): --
 			GridPane.setConstraints(activeFilterLabel, menuListX, filterY+1, 2, 1);
 			GridPane.setConstraints(nameFilterLabel, menuListX, filterY+2);
 			GridPane.setConstraints(nameFilterField, menuListX+1, filterY+2);
 			GridPane.setConstraints(nutrientFilterLabel, menuListX, filterY+3, 2, 1); // Col span: 2
 			GridPane.setValignment(nutrientFilterLabel, VPos.CENTER);
 			GridPane.setConstraints(ruleList, menuListX, filterY+4, 2, 2); // Col span: 2, row span: 2
 			GridPane.setConstraints(runSearch, menuListX, filterY);
 			
 			VBox removeRuleButtonVBox = new VBox();
 			removeRuleButtonVBox.getChildren().addAll(removeRuleButton);
 			removeRuleButtonVBox.setAlignment(Pos.TOP_RIGHT);
 			GridPane.setConstraints(removeRuleButtonVBox, menuListX+1, filterY+3, 1, 1);
 			
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
}