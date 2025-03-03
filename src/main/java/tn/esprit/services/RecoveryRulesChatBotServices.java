package tn.esprit.services;

import tn.esprit.entities.*;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecoveryRulesChatBotServices {

    private Connection connection;

    public RecoveryRulesChatBotServices() {
        this.connection = MyDatabase.getInstance().getCon(); // Ensure a single connection instance
    }

    // Method to get user_id based on first name and last name
    private int getUserIdByName(String firstName, String lastName) throws SQLException {
        String query = "SELECT user_id FROM user WHERE user_fname = ? AND user_lname = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            } else {
                throw new SQLException("User not found");
            }
        }
    }

    public List<RecoveryPlan> getRecoveryPlansByUserName(String firstName, String lastName) throws SQLException {
        int userId = getUserIdByName(firstName, lastName); // Get user_id based on name
        List<RecoveryPlan> recoveryPlans = new ArrayList<>();
        String query = "SELECT * FROM recoveryplan WHERE user_id = ?";

        // Use the already established connection
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    RecoveryPlan recoveryPlan = new RecoveryPlan();
                    recoveryPlan.setRecovery_id(resultSet.getInt("recovery_id"));
                    recoveryPlan.setRecovery_Description(resultSet.getString("recovery_description"));
                    recoveryPlan.setRecovery_StartDate(resultSet.getDate("recovery_StartDate").toLocalDate());
                    recoveryPlan.setRecovery_EndDate(resultSet.getDate("recovery_EndDate").toLocalDate());
                    recoveryPlan.setRecovery_Status(RecoveryStatus.valueOf(resultSet.getString("recovery_Status")));
                    recoveryPlan.setRecovery_Goal(RecoveryGoal.valueOf(resultSet.getString("recovery_Goal")));


                    // Fetch user associated with this recovery plan
                    String userQuery = "SELECT * FROM user WHERE user_id = ?";
                    try (PreparedStatement userStatement = connection.prepareStatement(userQuery)) {
                        userStatement.setInt(1, resultSet.getInt("user_id"));
                        try (ResultSet userResultSet = userStatement.executeQuery()) {
                            if (userResultSet.next()) {
                                user user = new user();
                                user.setUser_id(userResultSet.getInt("user_id"));
                                user.setUser_fname(userResultSet.getString("user_fname"));
                                user.setUser_lname(userResultSet.getString("user_lname"));
                                user.setUser_email(userResultSet.getString("user_email"));
                                user.setUser_nbr(userResultSet.getString("user_nbr"));

                                recoveryPlan.setUser(user);  // Set the user in the recovery plan
                            }
                        }
                    }

                    recoveryPlans.add(recoveryPlan);
                }
            }
        }
        return recoveryPlans;
    }

    public List<Injury> getInjuriesByUserName(String firstName, String lastName) throws SQLException {
        int userId = getUserIdByName(firstName, lastName); // Get user_id based on name
        List<Injury> injuries = new ArrayList<>();
        String query = "SELECT * FROM injury WHERE user_id = ?";

        // Use the already established connection
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Injury injury = new Injury();

                    // Setting the injury details
                    injury.setInjury_id(resultSet.getInt("injury_id"));
                    injury.setInjuryType(InjuryType.valueOf(resultSet.getString("injuryType")));
                    injury.setInjuryDate(resultSet.getDate("injuryDate").toLocalDate());
                    injury.setInjury_severity(Severity.valueOf(resultSet.getString("injury_severity")));

                    // Fetch the associated user for this injury
                    String userQuery = "SELECT * FROM user WHERE user_id = ?";
                    try (PreparedStatement userStatement = connection.prepareStatement(userQuery)) {
                        userStatement.setInt(1, resultSet.getInt("user_id"));
                        try (ResultSet userResultSet = userStatement.executeQuery()) {
                            if (userResultSet.next()) {
                                user user = new user();
                                user.setUser_id(userResultSet.getInt("user_id"));
                                user.setUser_fname(userResultSet.getString("user_fname"));
                                user.setUser_lname(userResultSet.getString("user_lname"));
                                user.setUser_email(userResultSet.getString("user_email"));
                                user.setUser_nbr(userResultSet.getString("user_nbr"));

                                injury.setUser(user);  // Set the user in the injury
                            }
                        }
                    }

                    injuries.add(injury);
                }
            }
        }
        return injuries;
    }

    public RecoveryRules getRecoveryRulesByUserNameAndInjuryId(String userFname, String userLname, int injuryId) throws SQLException {
        String query = "SELECT r.*, i.injuryType, u.user_fname, u.user_lname, u.user_id " +
                "FROM recoveryrules r " +
                "JOIN injury i ON r.injury_id = i.injury_id " +
                "JOIN user u ON r.user_id = u.user_id " +
                "WHERE u.user_fname = ? AND u.user_lname = ? AND r.injury_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userFname);
            preparedStatement.setString(2, userLname);
            preparedStatement.setInt(3, injuryId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Injury injury = new Injury();
                    injury.setInjury_id(rs.getInt("injury_id"));
                    injury.setInjuryType(InjuryType.valueOf(rs.getString("injuryType")));

                    user user = new user();
                    user.setUser_id(rs.getInt("user_id"));
                    user.setUser_fname(rs.getString("user_fname"));
                    user.setUser_lname(rs.getString("user_lname"));

                    return new RecoveryRules(
                            rs.getInt("recoveryrules_id"),
                            new RecoveryPlan(), // Assume recoveryPlan is fetched from another source if needed
                            injury,
                            user,
                            calculateRecoveryPhase(rs.getDate("recovery_StartDate").toLocalDate(), rs.getDate("recovery_EndDate").toLocalDate()),
                            rs.getInt("days"),
                            rs.getString("recommendation"),
                            RecoveryRules.DietType.valueOf(rs.getString("dietType")),
                            rs.getString("nutritionDetails")
                    );
                }
            }
        }
        return null;
    }

    private RecoveryRules.Phase calculateRecoveryPhase(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (today.isBefore(startDate)) {
            return RecoveryRules.Phase.EARLY;
        } else if (today.isAfter(endDate)) {
            return RecoveryRules.Phase.LATE;
        } else {
            return RecoveryRules.Phase.MID;
        }
    }
}
