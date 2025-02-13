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
        String query = "INSERT INTO `NutritionPlan` (`athlete_id`, `diet_type`, `allergies`, `calorie_intake`, `start_date`, `end_date`, `meal_plan`, `notes`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, nutritionPlan.getAthlete_id());
        ps.setString(2, nutritionPlan.getDietType().toString());
        ps.setString(3, nutritionPlan.getAllergies().toString());
        ps.setInt(4, nutritionPlan.getCalorie_intake());
        ps.setDate(5, Date.valueOf(nutritionPlan.getStart_date()));
        ps.setDate(6, Date.valueOf(nutritionPlan.getEnd_date()));
        ps.setString(7, String.join(", ", nutritionPlan.getMeal_plan()));
        ps.setString(8, nutritionPlan.getNotes());
        ps.executeUpdate();
        System.out.println("Nutrition Plan added!");
    }

    @Override
    public List<NutritionPlan> returnList() throws SQLException {
        String query = "SELECT * FROM `NutritionPlan`";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return buildNutritionPlanList(rs);
    }

    @Override
    public void delete(NutritionPlan nutritionPlan) throws SQLException {
        String query = "DELETE FROM `NutritionPlan` WHERE `nutrition_id` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, nutritionPlan.getNutrition_id());
        ps.executeUpdate();
        System.out.println("Nutrition Plan deleted!");
    }

    @Override
    public void update(NutritionPlan nutritionPlan) throws SQLException {
        String query = "UPDATE `NutritionPlan` SET `athlete_id` = ?, `diet_type` = ?, `allergies` = ?, `calorie_intake` = ?, `start_date` = ?, `end_date` = ?, `meal_plan` = ?, `notes` = ? WHERE `nutrition_id` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, nutritionPlan.getAthlete_id());
        ps.setString(2, nutritionPlan.getDietType().toString());
        ps.setString(3, nutritionPlan.getAllergies().toString());
        ps.setInt(4, nutritionPlan.getCalorie_intake());
        ps.setDate(5, Date.valueOf(nutritionPlan.getStart_date()));
        ps.setDate(6, Date.valueOf(nutritionPlan.getEnd_date()));
        ps.setString(7, String.join(", ", nutritionPlan.getMeal_plan()));
        ps.setString(8, nutritionPlan.getNotes());
        ps.setInt(9, nutritionPlan.getNutrition_id());
        ps.executeUpdate();
        System.out.println("Nutrition Plan updated!");
    }

    public List<NutritionPlan> sortByStartDate(boolean ascending) throws SQLException {
        String order = ascending ? "ASC" : "DESC";
        String query = "SELECT * FROM `NutritionPlan` ORDER BY `start_date` " + order;
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return buildNutritionPlanList(rs);
    }

    public NutritionPlan findById(int nutrition_id) throws SQLException {
        String query = "SELECT * FROM `NutritionPlan` WHERE `nutrition_id` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, nutrition_id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new NutritionPlan(
                    rs.getInt("nutrition_id"),
                    rs.getInt("athlete_id"),
                    DietType.valueOf(rs.getString("diet_type")),
                    Allergies.valueOf(rs.getString("allergies")),
                    rs.getInt("calorie_intake"),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date").toLocalDate(),
                    List.of(rs.getString("meal_plan").split(", ")),
                    rs.getString("notes")
            );
        } else {
            return null;
        }
    }

    // Search Nutrition Plans by Athlete ID and Diet Type
    public List<NutritionPlan> searchByAthleteIdAndDietType(int athleteId, DietType dietType) throws SQLException {
        String query = "SELECT * FROM `NutritionPlan` WHERE `athlete_id` = ? AND `diet_type` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, athleteId);
        ps.setString(2, dietType.toString());
        ResultSet rs = ps.executeQuery();
        return buildNutritionPlanList(rs);
    }

    // Advanced Filtering by these parameters: diet type, start date, end date
    public List<NutritionPlan> advancedFilter(DietType dietType, LocalDate startDate, LocalDate endDate) throws SQLException {
        List<NutritionPlan> nutritionPlans = new ArrayList<>();

        // Build the dynamic query
        StringBuilder query = new StringBuilder("SELECT * FROM `NutritionPlan` WHERE `start_date` BETWEEN ? AND ?");

        if (dietType != null) {
            query.append(" AND `diet_type` = ?");
        }

        PreparedStatement ps = con.prepareStatement(query.toString());
        ps.setDate(1, Date.valueOf(startDate));
        ps.setDate(2, Date.valueOf(endDate));

        if (dietType != null) {
            ps.setString(3, dietType.toString());
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            nutritionPlans.add(new NutritionPlan(
                    rs.getInt("nutrition_id"),
                    rs.getInt("athlete_id"),
                    DietType.valueOf(rs.getString("diet_type")),
                    Allergies.valueOf(rs.getString("allergies")),
                    rs.getInt("calorie_intake"),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date").toLocalDate(),
                    List.of(rs.getString("meal_plan").split(", ")),
                    rs.getString("notes")
            ));
        }
        return nutritionPlans;
    }

    private List<NutritionPlan> buildNutritionPlanList(ResultSet rs) throws SQLException {
        List<NutritionPlan> nutritionPlans = new ArrayList<>();
        while (rs.next()) {
            NutritionPlan nutritionPlan = new NutritionPlan(
                    rs.getInt("nutrition_id"),
                    rs.getInt("athlete_id"),
                    DietType.valueOf(rs.getString("diet_type")),
                    Allergies.valueOf(rs.getString("allergies")),
                    rs.getInt("calorie_intake"),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date").toLocalDate(),
                    List.of(rs.getString("meal_plan").split(", ")),
                    rs.getString("notes")
            );
            nutritionPlans.add(nutritionPlan);
        }
        return nutritionPlans;
    }
}
