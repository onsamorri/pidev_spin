package tn.esprit.services;

import tn.esprit.entities.*;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NutritionPlanServices implements IService<NutritionPlan> {

    private Connection con;

    public NutritionPlanServices() {
        con = MyDatabase.getInstance().getCon();
    }

    @Override
    public void add(NutritionPlan nutritionPlan) throws SQLException {
        String query = "INSERT INTO nutritionplan (user_id, nutrition_dietType, nutrition_allergies, nutrition_calorie_intake, nutrition_start_date, nutrition_end_date, nutrition_meal_plan, nutrition_notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, nutritionPlan.getUser_id());
        ps.setString(2, nutritionPlan.getNutrition_dietType().toString());
        ps.setString(3, nutritionPlan.getNutrition_allergies().toString());
        ps.setInt(4, nutritionPlan.getNutrition_calorie_intake());
        ps.setDate(5, Date.valueOf(nutritionPlan.getNutrition_start_date()));
        ps.setDate(6, Date.valueOf(nutritionPlan.getNutrition_end_date()));
        ps.setString(7, nutritionPlan.getNutrition_meal_plan());
        ps.setString(8, nutritionPlan.getNutrition_notes());
        ps.executeUpdate();
        System.out.println("Nutrition Plan added!");
    }

    @Override
    public List<NutritionPlan> getAll() throws SQLException {
        String query = "SELECT * FROM nutritionplan";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return buildNutritionPlanList(rs);
    }

    @Override
    public void delete(NutritionPlan nutritionPlan) throws SQLException {
        String query = "DELETE FROM nutritionplan WHERE nutrition_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, nutritionPlan.getNutrition_id());
        ps.executeUpdate();
        System.out.println("Nutrition Plan deleted!");
    }

    @Override
    public void update(NutritionPlan nutritionPlan) throws SQLException {
        String query = "UPDATE nutritionplan SET user_id = ?, nutrition_dietType = ?, nutrition_allergies = ?, nutrition_calorie_intake = ?, nutrition_start_date = ?, nutrition_end_date = ?, nutrition_meal_plan = ?, nutrition_notes = ? WHERE nutrition_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, nutritionPlan.getUser_id());
        ps.setString(2, nutritionPlan.getNutrition_dietType().toString());
        ps.setString(3, nutritionPlan.getNutrition_allergies().toString());
        ps.setInt(4, nutritionPlan.getNutrition_calorie_intake());
        ps.setDate(5, Date.valueOf(nutritionPlan.getNutrition_start_date()));
        ps.setDate(6, Date.valueOf(nutritionPlan.getNutrition_end_date()));
        ps.setString(7, nutritionPlan.getNutrition_meal_plan());
        ps.setString(8, nutritionPlan.getNutrition_notes());
        ps.setInt(9, nutritionPlan.getNutrition_id());
        ps.executeUpdate();
        System.out.println("Nutrition Plan updated!");
    }

    public NutritionPlan findById(int id) throws SQLException {
        String query = "SELECT * FROM nutritionplan WHERE nutrition_id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToNutritionPlan(resultSet);
            }
        }
        return null;
    }

    private NutritionPlan mapResultSetToNutritionPlan(ResultSet resultSet) throws SQLException {
        return new NutritionPlan(
                resultSet.getInt("nutrition_id"),
                resultSet.getInt("user_id"),
                DietType.valueOf(resultSet.getString("nutrition_dietType")),
                Allergies.valueOf(resultSet.getString("nutrition_allergies")),
                resultSet.getInt("nutrition_calorie_intake"),
                resultSet.getDate("nutrition_start_date").toLocalDate(),
                resultSet.getDate("nutrition_end_date").toLocalDate(),
                resultSet.getString("nutrition_meal_plan"),
                resultSet.getString("nutrition_notes")
        );
    }

    private List<NutritionPlan> buildNutritionPlanList(ResultSet rs) throws SQLException {
        List<NutritionPlan> nutritionPlans = new ArrayList<>();
        while (rs.next()) {
            DietType dietType = null;
            try {
                dietType = DietType.valueOf(rs.getString("nutrition_dietType"));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid DietType: " + rs.getString("nutrition_dietType"));
            }

            NutritionPlan nutritionPlan = new NutritionPlan(
                    rs.getInt("nutrition_id"),
                    rs.getInt("user_id"),
                    dietType,
                    Allergies.valueOf(rs.getString("nutrition_allergies")),
                    rs.getInt("nutrition_calorie_intake"),
                    rs.getDate("nutrition_start_date").toLocalDate(),
                    rs.getDate("nutrition_end_date").toLocalDate(),
                    rs.getString("nutrition_meal_plan"),
                    rs.getString("nutrition_notes")
            );
            nutritionPlans.add(nutritionPlan);
        }
        return nutritionPlans;
    }
}
