package tn.esprit.services;

import tn.esprit.entities.RecoveryRules;
import tn.esprit.entities.RecoveryPlan;
import tn.esprit.entities.Injury;
import tn.esprit.entities.Severity;
import tn.esprit.entities.InjuryType;
import tn.esprit.entities.RecoveryGoal;
import tn.esprit.entities.RecoveryStatus;
import tn.esprit.entities.user;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RecoveryRulesServices {

    private static Connection connection = MyDatabase.getInstance().getCon();

    // Fetch recovery rules based on injury ID
    public static List<RecoveryRules> getRecoveryRulesByInjuryId(int injuryId) {
        List<RecoveryRules> recoveryRulesList = new ArrayList<>();
        String query = "SELECT * FROM recoveryrules WHERE injury_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, injuryId);  // Setting the injury ID parameter
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Fetch related RecoveryPlan, Injury, and user based on the IDs
                int recoveryPlanId = rs.getInt("recoveryPlan_id");
                RecoveryPlan recoveryPlan = getRecoveryPlanById(recoveryPlanId);

                int injury_id = rs.getInt("injury_id");
                Injury injury = getInjuryById(injury_id);

                int user_id = rs.getInt("user_id");
                user user = getUserById(user_id);

                RecoveryRules recoveryRules = new RecoveryRules(
                        rs.getInt("recoveryrules_id"),
                        recoveryPlan,
                        injury,
                        user,
                        RecoveryRules.Phase.valueOf(rs.getString("phase")),
                        rs.getInt("days"),
                        rs.getString("recommendation"),
                        RecoveryRules.DietType.valueOf(rs.getString("dietType")),
                        rs.getString("nutritionDetails")
                );
                recoveryRulesList.add(recoveryRules);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recoveryRulesList;
    }

    // Fetch RecoveryPlan by ID
    private static RecoveryPlan getRecoveryPlanById(int id) {
        RecoveryPlan recoveryPlan = null;
        String query = "SELECT * FROM recoveryplan WHERE recovery_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Fetch related entities and populate the RecoveryPlan
                int injury_id = rs.getInt("injury_id");
                Injury injury = getInjuryById(injury_id);

                int user_id = rs.getInt("user_id");
                user user = getUserById(user_id);

                RecoveryPlan plan = new RecoveryPlan(
                        rs.getInt("recovery_id"),
                        injury,
                        user,
                        RecoveryGoal.fromString(rs.getString("recovery_Goal")),
                        rs.getString("recovery_Description"),
                        rs.getDate("recovery_StartDate").toLocalDate(),
                        rs.getDate("recovery_EnDate").toLocalDate(),
                        RecoveryStatus.fromString(rs.getString("recovery_Status"))
                );
                recoveryPlan = plan;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recoveryPlan;
    }

    // Fetch Injury by ID
    private static Injury getInjuryById(int id) {
        Injury injury = null;
        String query = "SELECT * FROM injury WHERE injury_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Injury injuryObject = new Injury(
                        rs.getInt("injury_id"),
                        InjuryType.valueOf(rs.getString("injuryType")),
                        rs.getDate("injuryDate").toLocalDate(),
                        Severity.valueOf(rs.getString("injury_severity")),
                        rs.getString("injury_description"),
                        getUserById(rs.getInt("user_id"))
                );
                injury = injuryObject;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return injury;
    }

    // Fetch user by ID
    private static user getUserById(int id) {
        user user = null;
        String query = "SELECT * FROM user WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new user(
                        rs.getInt("user_id"),
                        rs.getString("user_fname"),
                        rs.getString("user_lname"),
                        rs.getString("user_email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Calculate the number of days between the recovery start and end date
    public static int calculateRecoveryDays(RecoveryPlan recoveryPlan) {
        LocalDate recovery_StartDate = recoveryPlan.getRecovery_StartDate();
        LocalDate recovery_EndDate = recoveryPlan.getRecovery_EndDate();

        // Calculate the number of days between start and end date
        return (int) ChronoUnit.DAYS.between(recovery_StartDate, recovery_EndDate);
    }

    // Process the RecoveryRules based on calculated recovery days
    public static void processRecoveryRules(RecoveryRules recoveryRules) {
        // Fetch the related RecoveryPlan (already done in previous code)
        RecoveryPlan recoveryPlan = recoveryRules.getRecoveryPlan();

        // Calculate the number of days based on the recovery start and end date
        int recoveryDays = calculateRecoveryDays(recoveryPlan);

        // Calculate the phase based on the recovery days
        RecoveryRules.Phase phase = calculatePhase(recoveryDays);

        // Set the phase for the recoveryRules object
        recoveryRules.setPhase(phase);

        // Now, you can proceed to determine the diet type and nutrition details as before
        String dietType = determineDietType(phase, recoveryRules.getInjury().getInjuryType());
        recoveryRules.setDietType(RecoveryRules.DietType.valueOf(dietType));


        // Optionally fetch nutrition details from the API
        fetchAndAssignNutritionDetails(recoveryRules);
    }

    // Calculate the phase based on recovery days
    public static RecoveryRules.Phase calculatePhase(int recoveryDays) {
        if (recoveryDays <= 7) {
            return RecoveryRules.Phase.EARLY;
        } else if (recoveryDays <= 14) {
            return RecoveryRules.Phase.MID;
        } else {
            return RecoveryRules.Phase.LATE;
        }
    }

    // Determine diet type based on phase and injury type
    public static String determineDietType(RecoveryRules.Phase phase, InjuryType injuryType) {
        if (phase == RecoveryRules.Phase.EARLY) {
            return "HIGH_PROTEIN";
        } else if (phase == RecoveryRules.Phase.MID) {
            return "BALANCED_DIET";
        }

        // Use a switch case for the injuryType
        return switch (injuryType) {
            case SPRAIN -> "CALCIUM_RICH";
            case FRACTURE -> "HIGH_CALCIUM";
            case CONCUSSION -> "PROTEIN_RICH";
            case BRUISE -> "VITAMIN_C_RICH";
            default -> "MAINTENANCE";
        };
    }


    // Method to fetch and assign nutrition details from the API (already in your code)
    private static void fetchAndAssignNutritionDetails(RecoveryRules recoveryRules) {
        // Your logic to call the nutrition API and update recoveryRules
    }
}
