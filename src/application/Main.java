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
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,1400,800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			// filter line
			primaryStage.setTitle("Food Program");
			Button criteriaButton = new Button("Create Criteria");
			Label nameFilterLabel = new Label("Search Term");
			TextField nameFilterField = new TextField();
			
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
			ObservableList<String> logicOptions = 
				    FXCollections.observableArrayList(
				        "==",
				        "<=",
				        ">="
				    );
			
			ComboBox<String> oporatorComboBox = new ComboBox<String>(logicOptions);
			nameFilterField.setPromptText("name here");

			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
