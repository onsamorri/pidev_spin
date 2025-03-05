package tn.esprit.entities;

public class Exercise {
    private String name;
    private int caloriesBurned;
    private int duration;
    private String level;

    // Constructor
    public Exercise(String name, int caloriesBurned, int duration, String level) {
        this.name = name;
        this.caloriesBurned = caloriesBurned;
        this.duration = duration;
        this.level = level;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
