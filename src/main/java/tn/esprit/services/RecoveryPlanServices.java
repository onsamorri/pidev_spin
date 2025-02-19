package tn.esprit.services;

import tn.esprit.entities.*;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecoveryPlanServices implements IService<RecoveryPlan> {

    private Connection con;

    public RecoveryPlanServices() {
        con = MyDatabase.getInstance().getCon();
    }

    @Override
    public void add(RecoveryPlan recoveryPlan) throws SQLException {
        String query = "INSERT INTO `RecoveryPlan` (`injury_id`, `recovery_Goal`, `recovery_Description`, `recovery_StartDate`, `recovery_EndDate`, `Recovery_Status`, `user_id`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);

        // Set the values for the query parameters
        ps.setInt(1, recoveryPlan.getInjury_id());
        ps.setString(2, recoveryPlan.getRecovery_Goal().toString());
        ps.setString(3, recoveryPlan.getRecovery_Description());
        ps.setDate(4, Date.valueOf(recoveryPlan.getRecovery_StartDate()));
        ps.setDate(5, Date.valueOf(recoveryPlan.getRecovery_EndDate()));
        ps.setString(6, recoveryPlan.getRecovery_Status().toString());
        ps.setInt(7, recoveryPlan.getUser_id()); // Replace athlete_id, coach_id, and medical_staff_id with user_id

        // Execute the update query
        ps.executeUpdate();

        System.out.println("Recovery Plan added!");
    }

    @Override
    public void delete(RecoveryPlan recoveryPlan) throws SQLException {
        String query = "DELETE FROM `RecoveryPlan` WHERE `recovery_id` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, recoveryPlan.getRecovery_id());
        ps.executeUpdate();
        System.out.println("Recovery Plan deleted!");
    }

    @Override
    public void update(RecoveryPlan recoveryPlan) throws SQLException {
        String query = "UPDATE `RecoveryPlan` SET `recovery_Goal` = ?, `recovery_Description` = ?, `recovery_StartDate` = ?, `recovery_EndDate` = ?, `Recovery_Status` = ?, `user_id` = ? WHERE `recovery_id` = ?";
        PreparedStatement ps = con.prepareStatement(query);

        // Setting values for the query parameters
        ps.setString(1, recoveryPlan.getRecovery_Goal().toString());
        ps.setString(2, recoveryPlan.getRecovery_Description());
        ps.setDate(3, Date.valueOf(recoveryPlan.getRecovery_StartDate()));
        ps.setDate(4, Date.valueOf(recoveryPlan.getRecovery_EndDate()));
        ps.setString(5, recoveryPlan.getRecovery_Status().toString());
        ps.setInt(6, recoveryPlan.getUser_id()); // Replace athlete_id, coach_id, and medical_staff_id with user_id
        ps.setInt(7, recoveryPlan.getRecovery_id());

        // Execute the update query
        ps.executeUpdate();
        System.out.println("Recovery Plan updated!");
    }

    @Override
    public List<RecoveryPlan> getAll() throws SQLException {
        String query = "SELECT * FROM `RecoveryPlan`";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return buildRecoveryPlanList(rs);
    }

    public List<RecoveryPlan> sortByRecoveryStartDate(boolean ascending) throws SQLException {
        String order = ascending ? "ASC" : "DESC";
        String query = "SELECT * FROM `RecoveryPlan` ORDER BY `recovery_StartDate` " + order;
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return buildRecoveryPlanList(rs);
    }

    public RecoveryPlan findById(int recoveryPlanId) throws SQLException {
        String query = "SELECT * FROM `RecoveryPlan` WHERE `recovery_id` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, recoveryPlanId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new RecoveryPlan(
                    rs.getInt("recovery_id"),
                    rs.getInt("injury_id"),
                    rs.getInt("user_id"), // Replace athlete_id, coach_id, and medical_staff_id with user_id
                    RecoveryGoal.valueOf(rs.getString("recovery_Goal")),
                    rs.getString("recovery_Description"),
                    rs.getDate("recovery_StartDate").toLocalDate(),
                    rs.getDate("recovery_EndDate").toLocalDate(),
                    RecoveryStatus.valueOf(rs.getString("Recovery_Status"))
            );
        } else {
            return null;
        }
    }

    public RecoveryPlan viewRecoveryPlan(int recoveryPlanId) throws SQLException {
        String query = "SELECT * FROM `RecoveryPlan` WHERE `recovery_id` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, recoveryPlanId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new RecoveryPlan(
                    rs.getInt("recovery_id"),
                    rs.getInt("injury_id"),
                    rs.getInt("user_id"), // Replace athlete_id, coach_id, and medical_staff_id with user_id
                    RecoveryGoal.valueOf(rs.getString("recovery_Goal")),
                    rs.getString("recovery_Description"),
                    rs.getDate("recovery_StartDate").toLocalDate(),
                    rs.getDate("recovery_EndDate").toLocalDate(),
                    RecoveryStatus.valueOf(rs.getString("Recovery_Status"))
            );
        } else {
            return null;
        }
    }

    // Search recovery plans by Injury ID and Status
    public List<RecoveryPlan> searchByInjuryIdAndStatus(int injuryId, RecoveryStatus status) throws SQLException {
        String query = "SELECT * FROM `RecoveryPlan` WHERE `injury_id` = ? AND `Recovery_Status` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, injuryId);
        ps.setString(2, status.toString());
        ResultSet rs = ps.executeQuery();
        return buildRecoveryPlanList(rs);
    }

    // Advanced filtering by multiple parameters (status, start date, end date)
    public List<RecoveryPlan> advancedFilter(RecoveryStatus filterStatus, LocalDate recovery_StartDate, LocalDate endDate) throws SQLException {
        List<RecoveryPlan> recoveryPlans = new ArrayList<>();

        // Building the dynamic query
        StringBuilder query = new StringBuilder("SELECT * FROM `RecoveryPlan` WHERE `recovery_StartDate` BETWEEN ? AND ?");

        if (filterStatus != null) {
            query.append(" AND `Recovery_Status` = ?");
        }

        PreparedStatement ps = con.prepareStatement(query.toString());
        ps.setDate(1, Date.valueOf(recovery_StartDate));
        ps.setDate(2, Date.valueOf(endDate));

        if (filterStatus != null) {
            ps.setString(3, filterStatus.toString());
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            recoveryPlans.add(new RecoveryPlan(
                    rs.getInt("recovery_id"),
                    rs.getInt("injury_id"),
                    rs.getInt("user_id"), // Replace athlete_id, coach_id, and medical_staff_id with user_id
                    RecoveryGoal.valueOf(rs.getString("recovery_Goal")),
                    rs.getString("recovery_Description"),
                    rs.getDate("recovery_StartDate").toLocalDate(),
                    rs.getDate("recovery_EndDate").toLocalDate(),
                    RecoveryStatus.valueOf(rs.getString("Recovery_Status"))
            ));
        }
        return recoveryPlans;
    }

    private List<RecoveryPlan> buildRecoveryPlanList(ResultSet rs) throws SQLException {
        List<RecoveryPlan> recoveryPlans = new ArrayList<>();
        while (rs.next()) {
            RecoveryGoal recoveryGoal = null;
            RecoveryStatus recoveryStatus = null;

            // Handle RecoveryGoal enum parsing
            try {
                recoveryGoal = RecoveryGoal.valueOf(rs.getString("recovery_Goal"));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid RecoveryGoal: " + rs.getString("recovery_Goal"));
            }

            // Handle RecoveryStatus enum parsing
            try {
                recoveryStatus = RecoveryStatus.valueOf(rs.getString("Recovery_Status"));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid RecoveryStatus: " + rs.getString("Recovery_Status"));
            }

            // If the recoveryStatus is null (invalid value in database), set it to a default (optional)
            if (recoveryStatus == null) {
                recoveryStatus = RecoveryStatus.PENDING;  // Set to a default value, e.g., PENDING
            }

            RecoveryPlan recoveryPlan = new RecoveryPlan(
                    rs.getInt("recovery_id"),
                    rs.getInt("injury_id"),
                    rs.getInt("user_id"), // Replace athlete_id, coach_id, and medical_staff_id with user_id
                    recoveryGoal,  // Use the parsed recoveryGoal
                    rs.getString("recovery_Description"),
                    rs.getDate("recovery_StartDate").toLocalDate(),
                    rs.getDate("recovery_EndDate").toLocalDate(),
                    recoveryStatus  // Use the parsed or default recoveryStatus
            );
            recoveryPlans.add(recoveryPlan);
        }
        return recoveryPlans;
    }
}
