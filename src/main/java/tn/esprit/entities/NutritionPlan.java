package tn.esprit.entities;

import java.time.LocalDate;

public class NutritionPlan {

    private int nutrition_id;
    private User user; // Changed from user_id to User object

    private DietType nutrition_dietType;
    private Allergies nutrition_allergies;
    private int nutrition_calorie_intake;
    private LocalDate nutrition_start_date;
    private LocalDate nutrition_end_date;
    private String nutrition_meal_plan;
    private String nutrition_notes;

    public NutritionPlan(int nutrition_id, User user, DietType nutrition_dietType,
                         Allergies nutrition_allergies, int nutrition_calorie_intake, LocalDate nutrition_start_date,
                         LocalDate nutrition_end_date, String nutrition_meal_plan, String nutrition_notes) {
        this.nutrition_id = nutrition_id;
        this.user = user; // Changed to User object
        this.nutrition_dietType = nutrition_dietType;
        this.nutrition_allergies = nutrition_allergies;
        this.nutrition_calorie_intake = nutrition_calorie_intake;
        this.nutrition_start_date = nutrition_start_date;
        this.nutrition_end_date = nutrition_end_date;
        this.nutrition_meal_plan = nutrition_meal_plan;
        this.nutrition_notes = nutrition_notes;
    }

    public NutritionPlan(int nutrition_id) {
        this.nutrition_id = nutrition_id;
    }

    public int getNutrition_id() {
        return nutrition_id;
    }


    public User getUser() {  // Changed from getUser_id() to getUser() returning a User object
        return user;
    }

    public DietType getNutrition_dietType() {
        return nutrition_dietType;
    }

    public Allergies getNutrition_allergies() {
        return nutrition_allergies;
    }

    public int getNutrition_calorie_intake() {
        return nutrition_calorie_intake;
    }

    public LocalDate getNutrition_start_date() {
        return nutrition_start_date;
    }

    public LocalDate getNutrition_end_date() {
        return nutrition_end_date;
    }

    public String getNutrition_meal_plan() {
        return nutrition_meal_plan;
    }

    public String getNutrition_notes() {
        return nutrition_notes;
    }

    public void setUser(User user) {  // Changed to set a User object
        this.user = user;
    }

    public void setNutrition_dietType(DietType nutrition_dietType) {
        this.nutrition_dietType = nutrition_dietType;
    }

    public void setNutrition_allergies(Allergies nutrition_allergies) {
        this.nutrition_allergies = nutrition_allergies;
    }

    public void setNutrition_calorie_intake(int nutrition_calorie_intake) {
        this.nutrition_calorie_intake = nutrition_calorie_intake;
    }

    public void setNutrition_start_date(LocalDate nutrition_start_date) {
        this.nutrition_start_date = nutrition_start_date;
    }

    public void setNutrition_end_date(LocalDate nutrition_end_date) {
        this.nutrition_end_date = nutrition_end_date;
    }

    public void setNutrition_meal_plan(String nutrition_meal_plan) {
        this.nutrition_meal_plan = nutrition_meal_plan;
    }

    public void setNutrition_notes(String nutrition_notes) {
        this.nutrition_notes = nutrition_notes;
    }

    @Override
    public String toString() {
        return "NutritionPlan{" +
                "nutrition_id=" + nutrition_id +
                ", user=" + (user != null ? user.getUser_fname() + " " + user.getUser_lname() : "Unknown") +
                ", nutrition_dietType=" + nutrition_dietType +
                ", nutrition_allergies=" + nutrition_allergies +
                ", nutrition_calorie_intake=" + nutrition_calorie_intake +
                ", nutrition_start_date=" + nutrition_start_date +
                ", nutrition_end_date=" + nutrition_end_date +
                ", nutrition_meal_plan='" + nutrition_meal_plan + '\'' +
                ", nutrition_notes='" + nutrition_notes + '\'' +
                '}';
    }
}
