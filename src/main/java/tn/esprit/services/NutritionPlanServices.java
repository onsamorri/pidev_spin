package tn.esprit.services;

import tn.esprit.entities.*;
import tn.esprit.utils.MyDatabase;
import tn.esprit.entities.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NutritionPlanServices implements IService<NutritionPlan> {

    private Connection con;
    private UserServices userService;

    public NutritionPlanServices() {
        con = MyDatabase.getInstance().getCon();
        userService = new UserServices();
    }

    @Override
    public void add(NutritionPlan nutritionPlan) throws SQLException {
        String query = "INSERT INTO nutritionplan (user_id, nutrition_dietType, nutrition_allergies, nutrition_calorie_intake, nutrition_start_date, nutrition_end_date, nutrition_meal_plan, nutrition_notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, nutritionPlan.getUser().getUser_id());
            ps.setString(2, nutritionPlan.getNutrition_dietType().toString());
            ps.setString(3, nutritionPlan.getNutrition_allergies().toString());
            ps.setInt(4, nutritionPlan.getNutrition_calorie_intake());
            ps.setDate(5, java.sql.Date.valueOf(nutritionPlan.getNutrition_start_date()));
            ps.setDate(6, java.sql.Date.valueOf(nutritionPlan.getNutrition_end_date()));
            ps.setString(7, nutritionPlan.getNutrition_meal_plan());
            ps.setString(8, nutritionPlan.getNutrition_notes());
            ps.executeUpdate();
            System.out.println("Nutrition Plan added!");
        } catch (SQLException e) {
            System.out.println("Error adding nutrition plan: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<NutritionPlan> getAll() throws SQLException {
        List<NutritionPlan> nutritionPlans = new ArrayList<>();
        String query = "SELECT * FROM nutritionplan";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int nutrition_id = rs.getInt("nutrition_id");
                int user_id = rs.getInt("user_id");
                User user = userService.findUserById(con, user_id);
                DietType dietType = DietType.valueOf(rs.getString("nutrition_dietType"));
                Allergies allergies = Allergies.valueOf(rs.getString("nutrition_allergies"));
                int calorieIntake = rs.getInt("nutrition_calorie_intake");
                LocalDate startDate = rs.getDate("nutrition_start_date").toLocalDate();
                LocalDate endDate = rs.getDate("nutrition_end_date").toLocalDate();
                String mealPlan = rs.getString("nutrition_meal_plan");
                String notes = rs.getString("nutrition_notes");

                NutritionPlan nutritionPlan = new NutritionPlan(nutrition_id, user, dietType, allergies, calorieIntake, startDate, endDate, mealPlan, notes);
                nutritionPlans.add(nutritionPlan);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving nutrition plans: " + e.getMessage());
            throw e;
        }
        return nutritionPlans;
    }

    @Override
    public void delete(NutritionPlan nutritionPlan) throws SQLException {
        String query = "DELETE FROM nutritionplan WHERE nutrition_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, nutritionPlan.getNutrition_id());
            ps.executeUpdate();
            System.out.println("Nutrition Plan deleted!");
        } catch (SQLException e) {
            System.out.println("Error deleting nutrition plan: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void update(NutritionPlan nutritionPlan) throws SQLException {
        String query = "UPDATE nutritionplan SET user_id = ?, nutrition_dietType = ?, nutrition_allergies = ?, nutrition_calorie_intake = ?, nutrition_start_date = ?, nutrition_end_date = ?, nutrition_meal_plan = ?, nutrition_notes = ? WHERE nutrition_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, nutritionPlan.getUser().getUser_id());
            ps.setString(2, nutritionPlan.getNutrition_dietType().toString());
            ps.setString(3, nutritionPlan.getNutrition_allergies().toString());
            ps.setInt(4, nutritionPlan.getNutrition_calorie_intake());
            ps.setDate(5, java.sql.Date.valueOf(nutritionPlan.getNutrition_start_date()));
            ps.setDate(6, java.sql.Date.valueOf(nutritionPlan.getNutrition_end_date()));
            ps.setString(7, nutritionPlan.getNutrition_meal_plan());
            ps.setString(8, nutritionPlan.getNutrition_notes());
            ps.setInt(9, nutritionPlan.getNutrition_id());
            ps.executeUpdate();
            System.out.println("Nutrition Plan updated!");
        } catch (SQLException e) {
            System.out.println("Error updating nutrition plan: " + e.getMessage());
            throw e;
        }
    }

    public NutritionPlan findById(int id) throws SQLException {
        String query = "SELECT * FROM nutritionplan WHERE nutrition_id = ?";
        NutritionPlan nutritionPlan = null;
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int nutrition_id = rs.getInt("nutrition_id");
                int user_id = rs.getInt("user_id");
                User user = userService.findUserById(con, user_id);
                DietType dietType = DietType.valueOf(rs.getString("nutrition_dietType"));
                Allergies allergies = Allergies.valueOf(rs.getString("nutrition_allergies"));
                int calorieIntake = rs.getInt("nutrition_calorie_intake");
                LocalDate startDate = rs.getDate("nutrition_start_date").toLocalDate();
                LocalDate endDate = rs.getDate("nutrition_end_date").toLocalDate();
                String mealPlan = rs.getString("nutrition_meal_plan");
                String notes = rs.getString("nutrition_notes");

                nutritionPlan = new NutritionPlan(nutrition_id, user, dietType, allergies, calorieIntake, startDate, endDate, mealPlan, notes);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving nutrition plan by ID: " + e.getMessage());
            throw e;
        }
        return nutritionPlan;
    }
}
