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
 * Emails:		awhitaker3@wisc.edu, adam.hughes@wisc.edu, butkus2@wisc.edu, ssturgeon@wisc.edu, sbonebright@gmail.com
 * Course:		CS400
 * Section:		004
 * 
 * Notes:
 * Known Bugs: 
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		    GridPane root = new GridPane();
		    root.setHgap(10); // Horizontal gap between rows
		    root.setVgap(10); // Vertical gap between columns
		    root.setPadding(new Insets(0, 10, 0, 10)); // Distance between nodes and edges of pane
			Scene scene = new Scene(root, 1400, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			// filter line
			
			// Set the application header
			primaryStage.setTitle("Food Program");
			
			// Label and Field for entering a text search term
			Label nameFilterLabel = new Label("Search Term");
			TextField nameFilterField = new TextField();
			
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
			
			// combo box for selecting the oporator for the rule
			Label oporatorLabel = new Label("Oporator");
			ObservableList<String> logicOptions = 
				    FXCollections.observableArrayList(
				        "==",
				        "<=",
				        ">="
				    );
			ComboBox<String> oporatorComboBox = new ComboBox<String>(logicOptions);
			
			// text field for entering the nutrient value (example number of grams or number of calories)
			TextField nutrientValueField = new TextField("Value");
			
			// Remove an active rule
			Button removeRuleButton = new Button("Remove Rule");
			
			// Add a rule to the active rules
			Button addRuleButton = new Button("Add Rule");
			
			// View of the active rules
			ListView<String> ruleList = new ListView<>();
			ObservableList<String> rules =FXCollections.observableArrayList();
			ruleList.setItems(rules);
			ruleList.setPrefWidth(100);
			ruleList.setPrefHeight(70);
			
			nameFilterField.setPromptText("name here");

			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
