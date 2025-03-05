package tn.esprit.services;

import java.util.ArrayList;
import java.util.List;

public class FavoriteExercises {
    private List<String> favoriteExercises = new ArrayList<>();

    // Add exercise to favorites
    public void addFavoriteExercise(String exerciseName) {
        if (!favoriteExercises.contains(exerciseName)) {
            favoriteExercises.add(exerciseName);
            System.out.println(exerciseName + " added to favorites!");
        }
    }

    // Remove exercise from favorites
    public void removeFavoriteExercise(String exerciseName) {
        // Check if the exercise is present in the favorites
        if (favoriteExercises.contains(exerciseName)) {
            favoriteExercises.remove(exerciseName); // Remove it from the list
            System.out.println(exerciseName + " removed from favorites!");
        } else {
            System.out.println(exerciseName + " not found in favorites.");
        }
    }


    // Get all favorite exercises
    public List<String> getFavoriteExercises() {
        return favoriteExercises;
    }
}
