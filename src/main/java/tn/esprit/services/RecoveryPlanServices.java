package tn.esprit.services;

// Import necessary classes
import tn.esprit.entities.RecoveryPlan;
import tn.esprit.entities.RecoveryGoal;
import tn.esprit.entities.RecoveryStatus;
import tn.esprit.entities.User;
import tn.esprit.entities.Injury;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Service class for managing RecoveryPlan objects
public class RecoveryPlanServices implements IService<RecoveryPlan> {
    private Connection con; // Database connection object
    private UserServices userService; // Service for managing users
    private InjuryServices injuryService; // Service for managing injuries

    // Constructor to initialize database connection and services
    public RecoveryPlanServices() {
        con = MyDatabase.getInstance().getCon(); // Get database connection instance
        userService = new UserServices(); // Initialize UserServices
        injuryService = new InjuryServices(); // Initialize InjuryServices
    }

    // Method to add a new RecoveryPlan to the database
    @Override
    public void add(RecoveryPlan recoveryPlan) throws SQLException {
        // SQL query to insert a new recovery plan
        String query = "INSERT INTO recoveryplan (injury_id, user_id, recovery_Goal, recovery_Description, recovery_StartDate, recovery_EndDate, Recovery_Status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Using PreparedStatement to prevent SQL injection
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, recoveryPlan.getInjury().getInjury_id()); // Set injury ID
            ps.setInt(2, recoveryPlan.getUser().getUser_id()); // Set user ID
            ps.setString(3, recoveryPlan.getRecovery_Goal().toString()); // Set recovery goal (enum as string)
            ps.setString(4, recoveryPlan.getRecovery_Description()); // Set recovery description
            ps.setDate(5, java.sql.Date.valueOf(recoveryPlan.getRecovery_StartDate())); // Set start date
            ps.setDate(6, java.sql.Date.valueOf(recoveryPlan.getRecovery_EndDate())); // Set end date
            ps.setString(7, recoveryPlan.getRecovery_Status().toString()); // Set recovery status (enum as string)
            ps.executeUpdate(); // Execute query
            System.out.println("Recovery plan added!"); // Confirm addition
        } catch (SQLException e) {
            System.out.println("Error adding recovery plan: " + e.getMessage()); // Handle error
            throw e;
        }
    }

    // Method to retrieve all recovery plans from the database
    @Override
    public List<RecoveryPlan> getAll() throws SQLException {
        List<RecoveryPlan> recoveryPlans = new ArrayList<>(); // List to store recovery plans
        String query = "SELECT * FROM recoveryplan"; // SQL query to get all recovery plans

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) { // Loop through result set
                int recoveryPlan_id = rs.getInt("recovery_id"); // Get recovery plan ID
                int injury_id = rs.getInt("injury_id"); // Get injury ID
                int user_id = rs.getInt("user_id"); // Get user ID

                // Fetch Injury and User objects using their respective services
                Injury injury = injuryService.findById(injury_id);
                User user = userService.findUserById(con, user_id);

                RecoveryGoal recoveryGoal = RecoveryGoal.valueOf(rs.getString("recovery_Goal")); // Get recovery goal
                String recoveryDescription = rs.getString("recovery_Description"); // Get description
                LocalDate recoveryStartDate = rs.getDate("recovery_StartDate").toLocalDate(); // Get start date
                LocalDate recoveryEndDate = rs.getDate("recovery_EndDate").toLocalDate(); // Get end date
                RecoveryStatus recoveryStatus = RecoveryStatus.valueOf(rs.getString("Recovery_Status")); // Get status

                // Create a new RecoveryPlan object and add it to the list
                RecoveryPlan recoveryPlan = new RecoveryPlan(recoveryPlan_id, injury, user, recoveryGoal, recoveryDescription, recoveryStartDate, recoveryEndDate, recoveryStatus);
                recoveryPlans.add(recoveryPlan);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving recovery plans: " + e.getMessage()); // Handle error
            throw e;
        }
        return recoveryPlans; // Return list of recovery plans
    }

    // Method to delete a recovery plan from the database
    @Override
    public void delete(RecoveryPlan recoveryPlan) throws SQLException {
        String query = "DELETE FROM recoveryplan WHERE recovery_id = ?"; // SQL delete query

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, recoveryPlan.getRecovery_id()); // Set recovery plan ID
            int rowsDeleted = pstmt.executeUpdate(); // Execute query
            if (rowsDeleted > 0) {
                System.out.println("Recovery plan deleted successfully!"); // Confirm deletion
            } else {
                System.out.println("No recovery plan found with the given ID."); // Handle missing ID
            }
        } catch (SQLException e) {
            System.out.println("Error deleting recovery plan: " + e.getMessage()); // Handle error
            throw e;
        }
    }

    // Method to update an existing recovery plan in the database
    @Override
    public void update(RecoveryPlan recoveryPlan) throws SQLException {
        String query = "UPDATE recoveryplan SET injury_id = ?, user_id = ?, recovery_Goal = ?, recovery_Description = ?, recovery_StartDate = ?, recovery_EndDate = ?, Recovery_Status = ? WHERE recovery_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, recoveryPlan.getInjury().getInjury_id());
            stmt.setInt(2, recoveryPlan.getUser().getUser_id());
            stmt.setString(3, recoveryPlan.getRecovery_Goal().toString());
            stmt.setString(4, recoveryPlan.getRecovery_Description());
            stmt.setDate(5, java.sql.Date.valueOf(recoveryPlan.getRecovery_StartDate()));
            stmt.setDate(6, java.sql.Date.valueOf(recoveryPlan.getRecovery_EndDate()));
            stmt.setString(7, recoveryPlan.getRecovery_Status().toString());
            stmt.setInt(8, recoveryPlan.getRecovery_id());

            int rowsUpdated = stmt.executeUpdate(); // Execute update query
            if (rowsUpdated > 0) {
                System.out.println("Recovery plan updated successfully!");
            } else {
                System.out.println("No recovery plan found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating recovery plan: " + e.getMessage());
            throw e;
        }
    }

    // Method to find a recovery plan by its ID
    public RecoveryPlan findById(int recoveryPlan_id) throws SQLException {
        String query = "SELECT * FROM recoveryplan WHERE recovery_id = ?";
        RecoveryPlan recoveryPlan = null;

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, recoveryPlan_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int injury_id = rs.getInt("injury_id");
                int user_id = rs.getInt("user_id");

                Injury injury = injuryService.findById(injury_id);
                User user = userService.findUserById(con, user_id);

                RecoveryGoal recoveryGoal = RecoveryGoal.valueOf(rs.getString("recovery_Goal"));
                String recoveryDescription = rs.getString("recovery_Description");
                LocalDate recoveryStartDate = rs.getDate("recovery_StartDate").toLocalDate();
                LocalDate recoveryEndDate = rs.getDate("recovery_EndDate").toLocalDate();
                RecoveryStatus recoveryStatus = RecoveryStatus.valueOf(rs.getString("Recovery_Status"));

                recoveryPlan = new RecoveryPlan(recoveryPlan_id, injury, user, recoveryGoal, recoveryDescription, recoveryStartDate, recoveryEndDate, recoveryStatus);
            }
        }
        return recoveryPlan;
    }
}
