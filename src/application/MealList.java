package application;

import java.util.ArrayList;

public class MealList {
	
	private ArrayList<FoodItem> mealList;
	
	public MealList() {
		mealList = new ArrayList<FoodItem>();
	}
	
	public ArrayList<FoodItem> getMealList() {
		return mealList;
	}
	
	public void removeFood(FoodItem foodItem) {
		if (foodItem == null) {
			return;
		}
		mealList.remove(foodItem);
	}
	
	public void addFood(FoodItem foodItem) {
		if (foodItem == null) {
			return;
		}
		mealList.add(foodItem);
	}
}
