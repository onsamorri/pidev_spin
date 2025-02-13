package tn.esprit.entities;

import java.time.LocalDate;
import java.util.List;

public class NutritionPlan {

    private int nutrition_id;
    private int athlete_id; // FK Athlete table
    private DietType dietType;
    private Allergies allergies;
    private int calorie_intake;
    private LocalDate start_date;
    private LocalDate end_date;
    private List<String> meal_plan; // List of meal descriptions
    private String notes;

    public NutritionPlan(int nutrition_id, int athlete_id, DietType dietType, Allergies allergies,
                         int calorie_intake, LocalDate start_date, LocalDate end_date,
                         List<String> meal_plan, String notes) {
        this.nutrition_id = nutrition_id;
        this.athlete_id = athlete_id;
        this.dietType = dietType;
        this.allergies = allergies;
        this.calorie_intake = calorie_intake;
        this.start_date = start_date;
        this.end_date = end_date;
        this.meal_plan = meal_plan;
        this.notes = notes;
    }

    public NutritionPlan(int nutrition_id) {
        this.nutrition_id = nutrition_id;
    }

    public int getNutrition_id() {
        return nutrition_id;
    }

    public int getAthlete_id() {
        return athlete_id;
    }

    public DietType getDietType() {
        return dietType;
    }

    public Allergies getAllergies() {
        return allergies;
    }

    public int getCalorie_intake() {
        return calorie_intake;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public List<String> getMeal_plan() {
        return meal_plan;
    }

    public String getNotes() {
        return notes;
    }

    public void setAthlete_id(int athlete_id) {
        this.athlete_id = athlete_id;
    }

    public void setDietType(DietType dietType) {
        this.dietType = dietType;
    }

    public void setAllergies(Allergies allergies) {
        this.allergies = allergies;
    }

    public void setCalorie_intake(int calorie_intake) {
        this.calorie_intake = calorie_intake;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public void setMeal_plan(List<String> meal_plan) {
        this.meal_plan = meal_plan;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "NutritionPlan{" +
                "nutrition_id=" + nutrition_id +
                ", athlete_id=" + athlete_id +
                ", dietType=" + dietType +
                ", allergies=" + allergies +
                ", calorie_intake=" + calorie_intake +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", meal_plan=" + meal_plan +
                ", notes='" + notes + '\'' +
                '}';
    }
}
