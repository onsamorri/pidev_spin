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
        String query = "INSERT INTO `RecoveryPlan` (`injury_id`, `goal`, `description`, `startDate`, `endDate`, `status`) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, recoveryPlan.getInjury_id());
        ps.setString(2, recoveryPlan.getGoal().toString());
        ps.setString(3, recoveryPlan.getDescription());
        ps.setDate(4, Date.valueOf(recoveryPlan.getStartDate()));
        ps.setDate(5, Date.valueOf(recoveryPlan.getEndDate()));
        ps.setString(6, recoveryPlan.getStatus().toString());
        ps.executeUpdate();
        System.out.println("Recovery Plan added!");
    }

    @Override
    public List<RecoveryPlan> returnList() throws SQLException {
        String query = "SELECT * FROM `RecoveryPlan`";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return buildRecoveryPlanList(rs);
    }

    @Override
    public void delete(RecoveryPlan recoveryPlan) throws SQLException {
        String query = "DELETE FROM `RecoveryPlan` WHERE `recoveryPlanId` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, recoveryPlan.getRecoveryPlanId());
        ps.executeUpdate();
        System.out.println("Recovery Plan deleted!");
    }

    @Override
    public void update(RecoveryPlan recoveryPlan) throws SQLException {
        String query = "UPDATE `RecoveryPlan` SET `goal` = ?, `description` = ?, `startDate` = ?, `endDate` = ?, `status` = ? WHERE `recoveryPlanId` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, recoveryPlan.getGoal().toString());
        ps.setString(2, recoveryPlan.getDescription());
        ps.setDate(3, Date.valueOf(recoveryPlan.getStartDate()));
        ps.setDate(4, Date.valueOf(recoveryPlan.getEndDate()));
        ps.setString(5, recoveryPlan.getStatus().toString());
        ps.setInt(6, recoveryPlan.getRecoveryPlanId());
        ps.executeUpdate();
        System.out.println("Recovery Plan updated!");
    }

    public List<RecoveryPlan> sortByStartDate(boolean ascending) throws SQLException {
        String order = ascending ? "ASC" : "DESC";
        String query = "SELECT * FROM `RecoveryPlan` ORDER BY `startDate` " + order;
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return buildRecoveryPlanList(rs);
    }

    public RecoveryPlan findById(int recoveryPlanId) throws SQLException {
        String query = "SELECT * FROM `RecoveryPlan` WHERE `recoveryPlanId` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, recoveryPlanId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new RecoveryPlan(
                    rs.getInt("recoveryPlanId"),
                    rs.getInt("injury_id"),
                    RecoveryPlanGoal.valueOf(rs.getString("goal")),
                    rs.getString("description"),
                    rs.getDate("startDate").toLocalDate(),
                    rs.getDate("endDate").toLocalDate(),
                    RecoveryPlanStatus.valueOf(rs.getString("status"))
            );
        } else {
            return null;
        }
    }

    public RecoveryPlan viewRecoveryPlan(int recoveryPlanId) throws SQLException {
        String query = "SELECT * FROM `RecoveryPlan` WHERE `recoveryPlanId` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, recoveryPlanId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new RecoveryPlan(
                    rs.getInt("recoveryPlanId"),
                    rs.getInt("injury_id"),
                    RecoveryPlanGoal.valueOf(rs.getString("goal")),
                    rs.getString("description"),
                    rs.getDate("startDate").toLocalDate(),
                    rs.getDate("endDate").toLocalDate(),
                    RecoveryPlanStatus.valueOf(rs.getString("status"))
            );
        } else {
            return null; // Return null if no recovery plan found
        }
    }


    // Search Recovery Plans by Injury ID and Status
    public List<RecoveryPlan> searchByInjuryIdAndStatus(int injuryId, RecoveryPlanStatus status) throws SQLException {
        String query = "SELECT * FROM `RecoveryPlan` WHERE `injury_id` = ? AND `status` = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, injuryId);
        ps.setString(2, status.toString());
        ResultSet rs = ps.executeQuery();
        return buildRecoveryPlanList(rs);
    }

    // Advanced Filtering by multiple parameters (status, start date, end date)
    public List<RecoveryPlan> advancedFilter(RecoveryPlanStatus filterStatus, LocalDate startDate, LocalDate endDate) throws SQLException {
        List<RecoveryPlan> recoveryPlans = new ArrayList<>();

        // Build the dynamic query
        StringBuilder query = new StringBuilder("SELECT * FROM `RecoveryPlan` WHERE `startDate` BETWEEN ? AND ?");

        if (filterStatus != null) {
            query.append(" AND `status` = ?");
        }

        PreparedStatement ps = con.prepareStatement(query.toString());
        ps.setDate(1, Date.valueOf(startDate));
        ps.setDate(2, Date.valueOf(endDate));

        if (filterStatus != null) {
            ps.setString(3, filterStatus.toString());
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            recoveryPlans.add(new RecoveryPlan(
                    rs.getInt("recoveryPlanId"),
                    rs.getInt("injury_id"),
                    RecoveryPlanGoal.valueOf(rs.getString("goal")),
                    rs.getString("description"),
                    rs.getDate("startDate").toLocalDate(),
                    rs.getDate("endDate").toLocalDate(),
                    RecoveryPlanStatus.valueOf(rs.getString("status"))
            ));
        }
        return recoveryPlans;
    }



    private List<RecoveryPlan> buildRecoveryPlanList(ResultSet rs) throws SQLException {
        List<RecoveryPlan> recoveryPlans = new ArrayList<>();
        while (rs.next()) {
            RecoveryPlan recoveryPlan = new RecoveryPlan(
                    rs.getInt("recoveryPlanId"),
                    rs.getInt("injury_id"),
                    RecoveryPlanGoal.valueOf(rs.getString("goal")),
                    rs.getString("description"),
                    rs.getDate("startDate").toLocalDate(),
                    rs.getDate("endDate").toLocalDate(),
                    RecoveryPlanStatus.valueOf(rs.getString("status"))
            );
            recoveryPlans.add(recoveryPlan);
        }
        return recoveryPlans;
    }
}
