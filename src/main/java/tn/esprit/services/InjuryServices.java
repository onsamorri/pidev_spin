package tn.esprit.services;

import tn.esprit.entities.Injury;
import tn.esprit.entities.InjuryType;
import tn.esprit.entities.Severity;
import tn.esprit.entities.user;
import tn.esprit.entities.RecoveryPlan;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InjuryServices implements IService2<Injury> {
    private Connection connection;

    public InjuryServices() {
        connection = MyDatabase.getInstance().getCon();
    }

    @Override
    public void add(Injury injury) throws Exception {
        try {
            if (injury.getInjury_severity() == null) {
                throw new Exception("Severity cannot be null");
            }

            String sql = "INSERT INTO injury (injuryType, injuryDate, injury_severity, injury_description, user_id, recovery_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, injury.getInjuryType().name());
            preparedStatement.setDate(2, Date.valueOf(injury.getInjuryDate()));
            preparedStatement.setString(3, injury.getInjury_severity().name());
            preparedStatement.setString(4, injury.getInjury_description());
            preparedStatement.setInt(5, injury.getUser().getUser_id());

            // If the recoveryPlan is null, set recovery_id to null in the database
            if (injury.getRecoveryPlan() != null) {
                preparedStatement.setInt(6, injury.getRecoveryPlan().getRecovery_id());
            } else {
                preparedStatement.setNull(6, Types.INTEGER); // Set recovery_id to null
            }

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Error while adding injury: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Failed to add injury: " + e.getMessage());
        }
    }


    @Override
    public void addP(Injury injury) throws Exception {
        try {
            add(injury);
            System.out.println("Injury added successfully");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void delete(int injury_id) throws SQLException {
        String sql = "DELETE FROM injury WHERE injury_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, injury_id);
        int result = preparedStatement.executeUpdate();
        if (result == 0)
            System.out.println("Injury not found");
        else
            System.out.println("Injury deleted");
    }

    @Override
    public void update(int injury_id, Injury injury) throws SQLException {
        try {
            // Check for required fields, for example, check if injury_severity is null
            if (injury.getInjury_severity() == null) {
                throw new SQLException("Severity cannot be null");
            }

            // SQL query to update the injury record
            String sql = "UPDATE injury SET injuryType = ?, injuryDate = ?, injury_severity = ?, " +
                    "injury_description = ?, user_id = ?, recovery_id = ? WHERE injury_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            // Set the prepared statement parameters
            ps.setString(1, injury.getInjuryType().name());
            ps.setDate(2, Date.valueOf(injury.getInjuryDate()));
            ps.setString(3, injury.getInjury_severity().name());
            ps.setString(4, injury.getInjury_description());
            ps.setInt(5, injury.getUser().getUser_id());

            // If recoveryPlan is null, set recovery_id to null, else set it to recoveryPlan's recovery_id
            if (injury.getRecoveryPlan() != null) {
                ps.setInt(6, injury.getRecoveryPlan().getRecovery_id());
            } else {
                ps.setNull(6, Types.INTEGER); // Set recovery_id to null
            }

            ps.setInt(7, injury_id); // Set the injury_id for the update query

            // Execute the update and check the result
            int result = ps.executeUpdate();
            if (result == 0) {
                System.out.println("Injury not found");
            } else {
                System.out.println("Injury updated successfully");
            }
        } catch (SQLException e) {
            throw new SQLException("Error while updating injury: " + e.getMessage());
        }
    }


    @Override
    public List<Injury> returnList() throws SQLException {
        String query = "SELECT i.*, u.user_fname, u.user_lname, r.recovery_id, r.recovery_description FROM injury i " +
                "JOIN user u ON i.user_id = u.user_id " +
                "LEFT JOIN recoveryplan r ON i.recovery_id = r.recovery_id";
        Statement statement = connection.createStatement();
        List<Injury> injuries = new ArrayList<>();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            user user = new user();
            user.setUser_id(rs.getInt("user_id"));
            user.setUser_fname(rs.getString("user_fname"));
            user.setUser_lname(rs.getString("user_lname"));

            RecoveryPlan recoveryPlan = null;
            if (rs.getInt("recovery_id") != 0) {
                recoveryPlan = new RecoveryPlan();
                recoveryPlan.setRecovery_id(rs.getInt("recovery_id"));
                recoveryPlan.setRecovery_Description(rs.getString("recovery_description"));
            }

            Injury injury = new Injury(
                    rs.getInt("injury_id"),
                    InjuryType.valueOf(rs.getString("injuryType")),
                    rs.getDate("injuryDate").toLocalDate(),
                    Severity.valueOf(rs.getString("injury_severity")),
                    rs.getString("injury_description"),
                    user
            );
            injury.setRecoveryPlan(recoveryPlan);
            injuries.add(injury);
        }
        return injuries;
    }

    public Injury getInjuryByAthleteId(user athlete) throws SQLException {
        Injury injury = null;
        String sql = "SELECT * FROM injury WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, athlete.getUser_id());

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            injury = new Injury();
            injury.setInjury_id(resultSet.getInt("injury_id"));
            injury.setUser(athlete);
            injury.setInjury_description(resultSet.getString("injury_description"));
            injury.setInjuryDate(resultSet.getDate("injuryDate").toLocalDate());
            injury.setInjury_severity(Severity.valueOf(resultSet.getString("injury_severity")));
            injury.setInjuryType(InjuryType.valueOf(resultSet.getString("injuryType")));
        }

        resultSet.close();
        preparedStatement.close();

        return injury;
    }
}
