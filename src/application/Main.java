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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class Main extends Application {
  
	@Override
	public void start(Stage primaryStage) {
		try {
		    GridPane gridpane = new GridPane();
		    gridpane.setHgap(10); // Horizontal gap between rows
		    gridpane.setVgap(10); // Vertical gap between columns
		    gridpane.setPadding(new Insets(0, 10, 0, 10)); // Distance between nodes and edges of pane
		    gridpane.setGridLinesVisible(false); // FIXME set to false when not testing
			Scene scene = new Scene(gridpane, 900, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			// --------- Creating Objects ---------
			
			// Set the application header
			primaryStage.setTitle("Food Program");
			
			
			// -- Food List Area: --
			
			Label foodListLabel = new Label("Food List"); // Food List title
			foodListLabel.setFont(new Font("Arial", 28));
			foodListLabel.setPadding(new Insets(10, 0, 10, 0));
			
			// HBox for buttons in food list area
            HBox foodListButtonsHBox = new HBox(); // Load, add, and save buttons in Food List area
            //foodListButtonsHBox.setPadding(new Insets(15, 12, 15, 12));
            foodListButtonsHBox.setSpacing(10);
            Button loadFoodsButton = new Button("Load Foods"); // FIXME change to a folder icon 
                                                               // with tooltip "Load from file"
            loadFoodsButton.setPrefSize(100, 20); // Sets width and height of button
            Button addFoodButton = new Button("Add Food"); // FIXME change to a "+" icon with
                                                           // tooltip "Add food"
            addFoodButton.setPrefSize(100, 20); // Sets width and height of button
            Button saveFoodsButton = new Button("Save Foods"); // FIXME change to a floppy disc icon with
                                                         // tooltip "Save foods"
            saveFoodsButton.setPrefSize(100, 20); // Sets width and height of button
            foodListButtonsHBox.getChildren().addAll(loadFoodsButton, addFoodButton, saveFoodsButton);
            
            // Master food list
            // Referencing this youtube tutorial: https://www.youtube.com/watch?v=GbzKr46VvD0&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG&index=15
            ObservableList<String> foods = FXCollections.observableArrayList();
            //ComboBox<String> foodListComboBox = new ComboBox<String>(foods);
            // Changing to be a ListView instead of a ComboBox
            ListView<String> foodList = new ListView<String>(foods);
            // The following line is for testing
            foodList.getItems().addAll("Blackberries", "Blueberries", "Raspberries", "Strawberries");
            // Makes multiple selections possible when hitting ctrl
            foodList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            
            
            // -- Buttons between Food List and Menu List: --
            
            // Add food to menu button
            Button addFoodtoMenuButton = new Button("Add to Menu");
            
            // Remove food from menu button
            Button removeFoodFromMenuButton = new Button("Remove From Menu");
            
            VBox addAndRemoveButtonsVBox = new VBox();
            addAndRemoveButtonsVBox.setPadding(new Insets(15, 12, 15, 12));
            addAndRemoveButtonsVBox.setSpacing(10);
            addAndRemoveButtonsVBox.getChildren().addAll(addFoodtoMenuButton, removeFoodFromMenuButton);
            
            
            // -- Menu List Area: --
            
            Label menuListLabel = new Label("Menu List"); // Menu List title
            menuListLabel.setFont(new Font("Arial", 28));
            menuListLabel.setPadding(new Insets(10, 0, 10, 0));
            
            // Menu food list
            ObservableList<String> menuFoods = FXCollections.observableArrayList();
            // ComboBox<String> menuFoodListComboBox = new ComboBox<String>(menuFoods);
            // Changing to be a ListView instead of a ComboBox
            ListView<String> menuList = new ListView<String>(menuFoods);
            
            Button analyzeMenuButton = new Button("Nutritional Analysis");
            analyzeMenuButton.setPrefHeight(20);
            
            
            // -- Filter Foods Area (Left Side): --
            
            Label filterFoodsLabel = new Label("Filter Foods"); // Filter Foods title
            filterFoodsLabel.setFont(new Font("Arial", 28));
            filterFoodsLabel.setPadding(new Insets(10, 0, 10, 0));
            
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
            Button addRuleButton = new Button("Add Filter");
            
            
            // -- Filter Foods Area (Right Side): --
            
			// Label and Field for entering a text search term
			Label nameFilterLabel = new Label("Search Term");
			TextField nameFilterField = new TextField();
	        nameFilterField.setPromptText("enter food name");
			
            Label nutrientFilterLabel = new Label("Nutrient Filters"); // Label for list of rules
            nutrientFilterLabel.setFont(new Font("Arial", 18));
			
            // View of the active rules
            ObservableList<String> rules =FXCollections.observableArrayList();
            ListView<String> ruleList = new ListView<>(rules);
            ruleList.setPrefWidth(100);
            ruleList.setPrefHeight(70);
            
			// Remove an active rule
			Button removeRuleButton = new Button("Remove Selected");
			
			// Run query button
			Button runSearch = new Button("Run Search");
			
			
			
			// --------- Adding objects to the pane ---------
			
			// -- Food List Area: --
			GridPane.setConstraints(foodListLabel, 0, 0, 2, 1); // Col span: 2
 			GridPane.setConstraints(foodListButtonsHBox, 0, 1, 2, 1); // Col span: 2
 			GridPane.setConstraints(foodList, 0, 2, 2, 2); // Col span: 2, Row span: 2
 			
 			// -- Buttons between Food List and Menu List: --
 			GridPane.setConstraints(addAndRemoveButtonsVBox, 2, 2);
 			
 			// -- Menu List Area: --
 			GridPane.setConstraints(menuListLabel, 3, 0, 2, 1); // Col span: 2
 			GridPane.setConstraints(menuList, 3, 1, 2, 2); // Col span: 2, Row span: 2
 			GridPane.setConstraints(analyzeMenuButton, 3, 3, 2, 1, HPos.LEFT, VPos.TOP); // Col span: 2
			
 			// -- Filter Foods Area (Left Side): --
 			GridPane.setConstraints(filterFoodsLabel, 0, 4, 2, 1); // Col span: 2
 			
 			GridPane.setConstraints(nutrientLabel, 0, 5);
 			nutrientLabel.setAlignment(Pos.CENTER_LEFT); // Vertically centers text label
 			GridPane.setConstraints(nutrientComboBox, 1, 5);
 			
 			GridPane.setConstraints(operatorLabel, 0, 6);
 			operatorLabel.setAlignment(Pos.CENTER_LEFT); // Vertically centers text label
 			GridPane.setConstraints(operatorComboBox, 1, 6);
 			
 			GridPane.setConstraints(valueLabel, 0, 7);
 			valueLabel.setAlignment(Pos.CENTER_LEFT); // Vertically centers text label
 			GridPane.setConstraints(nutrientValueField, 1, 7);
 			
 			GridPane.setConstraints(addRuleButton, 1, 8);
 			
 			// -- Filter Foods Area (Right Side): --
 			GridPane.setConstraints(nameFilterLabel, 3, 5);
 			GridPane.setConstraints(nameFilterField, 4, 5);
 			GridPane.setConstraints(nutrientFilterLabel, 3, 6, 2, 1); // Col span: 2
 			GridPane.setConstraints(ruleList, 3, 7, 2, 2); // Col span: 2, row span: 2
 			GridPane.setConstraints(removeRuleButton, 3, 9);
 			GridPane.setConstraints(runSearch, 4, 9);
 			
 			// Adds all nodes to the Grid Pane
 			gridpane.getChildren().addAll(foodListLabel, foodListButtonsHBox, foodList, 
 			    addAndRemoveButtonsVBox, menuListLabel, menuList, analyzeMenuButton, 
 			    filterFoodsLabel, nutrientLabel, nutrientComboBox, operatorLabel, operatorComboBox, 
 			    valueLabel, nutrientValueField, addRuleButton, nameFilterLabel, nameFilterField, 
 			    nutrientFilterLabel, ruleList, removeRuleButton, runSearch);
            primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}