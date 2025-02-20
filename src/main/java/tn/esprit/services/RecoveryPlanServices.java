package tn.esprit.services;

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

public class RecoveryPlanServices implements IService<RecoveryPlan> {
    private Connection con;
    private UserServices userService;
    private InjuryServices injuryService;  // Assuming a service for Injury exists

    public RecoveryPlanServices() {
        con = MyDatabase.getInstance().getCon();
        userService = new UserServices();
        injuryService = new InjuryServices();  // Initialize the InjuryServices
    }

    @Override
    public void add(RecoveryPlan recoveryPlan) throws SQLException {
        String query = "INSERT INTO recoveryplan (injury_id, user_id, recovery_Goal, recovery_Description, recovery_StartDate, recovery_EndDate, Recovery_Status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, recoveryPlan.getInjury().getInjury_id());
            ps.setInt(2, recoveryPlan.getUser().getUser_id());
            ps.setString(3, recoveryPlan.getRecovery_Goal().toString());
            ps.setString(4, recoveryPlan.getRecovery_Description());
            ps.setDate(5, java.sql.Date.valueOf(recoveryPlan.getRecovery_StartDate()));
            ps.setDate(6, java.sql.Date.valueOf(recoveryPlan.getRecovery_EndDate()));
            ps.setString(7, recoveryPlan.getRecovery_Status().toString());
            ps.executeUpdate();
            System.out.println("Recovery plan added!");
        } catch (SQLException e) {
            System.out.println("Error adding recovery plan: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<RecoveryPlan> getAll() throws SQLException {
        List<RecoveryPlan> recoveryPlans = new ArrayList<>();
        String query = "SELECT * FROM recoveryplan";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int recoveryPlan_id = rs.getInt("recovery_id");
                int injury_id = rs.getInt("injury_id");
                int user_id = rs.getInt("user_id");

                // Fetch Injury and User objects
                Injury injury = injuryService.findById(injury_id);  // Fetch Injury by ID
                User user = userService.findUserById(con, user_id);  // Fetch User by ID

                RecoveryGoal recoveryGoal = RecoveryGoal.valueOf(rs.getString("recovery_Goal"));
                String recoveryDescription = rs.getString("recovery_Description");
                LocalDate recoveryStartDate = rs.getDate("recovery_StartDate").toLocalDate();
                LocalDate recoveryEndDate = rs.getDate("recovery_EndDate").toLocalDate();
                RecoveryStatus recoveryStatus = RecoveryStatus.valueOf(rs.getString("Recovery_Status"));

                // Create RecoveryPlan object
                RecoveryPlan recoveryPlan = new RecoveryPlan(recoveryPlan_id, injury, user, recoveryGoal, recoveryDescription, recoveryStartDate, recoveryEndDate, recoveryStatus);
                recoveryPlans.add(recoveryPlan);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving recovery plans: " + e.getMessage());
            throw e;
        }

        return recoveryPlans;
    }

    @Override
    public void delete(RecoveryPlan recoveryPlan) throws SQLException {
        String query = "DELETE FROM recoveryplan WHERE recovery_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, recoveryPlan.getRecovery_id());
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Recovery plan deleted successfully!");
            } else {
                System.out.println("No recovery plan found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting recovery plan: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void update(RecoveryPlan recoveryPlan) throws SQLException {
        String query = "UPDATE recoveryplan SET injury_id = ?, user_id = ?, recovery_Goal = ?, recovery_Description = ?, recovery_StartDate = ?, recovery_EndDate = ?, Recovery_Status = ? WHERE recovery_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, recoveryPlan.getInjury().getInjury_id());  // Use Injury object
            stmt.setInt(2, recoveryPlan.getUser().getUser_id());  // Use User object
            stmt.setString(3, recoveryPlan.getRecovery_Goal().toString());
            stmt.setString(4, recoveryPlan.getRecovery_Description());
            stmt.setDate(5, java.sql.Date.valueOf(recoveryPlan.getRecovery_StartDate()));
            stmt.setDate(6, java.sql.Date.valueOf(recoveryPlan.getRecovery_EndDate()));
            stmt.setString(7, recoveryPlan.getRecovery_Status().toString());
            stmt.setInt(8, recoveryPlan.getRecovery_id());

            int rowsUpdated = stmt.executeUpdate();
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

    public RecoveryPlan findById(int recoveryPlan_id) throws SQLException {
        String query = "SELECT * FROM recoveryplan WHERE recovery_id = ?";
        RecoveryPlan recoveryPlan = null;

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, recoveryPlan_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int injury_id = rs.getInt("injury_id");
                int user_id = rs.getInt("user_id");

                // Fetch Injury and User objects
                Injury injury = injuryService.findById(injury_id);  // Fetch Injury by ID
                User user = userService.findUserById(con, user_id);  // Fetch User by ID

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

    public User getUserByName(String user_fname, String user_lname) throws SQLException {
        return userService.getUserByName(con, user_fname, user_lname);
    }
}
