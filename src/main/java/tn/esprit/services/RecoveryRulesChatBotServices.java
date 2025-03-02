package tn.esprit.services;

import tn.esprit.entities.RecoveryRules;
import tn.esprit.entities.user;
import tn.esprit.entities.Injury;

import tn.esprit.entities.RecoveryPlan;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecoveryRulesChatBotServices {

    private Connection connection;

    public RecoveryRulesChatBotServices() {
        connection = MyDatabase.getInstance().getCon();
    }

    // Method to get recovery rules based on user id and injury id
    public RecoveryRules getRecoveryRulesByUserIdAndInjuryId(int userId, int injuryId) throws SQLException {
        // Fetch recovery rules for an athlete based on user_id and injury_id
        String query = "SELECT r.*, i.injuryType, u.user_fname, u.user_lname " +
                "FROM recoveryrules r " +
                "JOIN injury i ON r.injury_id = i.injury_id " +
                "JOIN user u ON r.user_id = u.user_id " +
                "WHERE r.user_id = ? AND r.injury_id = ?";  // Removed the role condition

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, injuryId);

        ResultSet rs = preparedStatement.executeQuery();

        // If a result is found
        if (rs.next()) {
            Injury injury = new Injury();
            injury.setInjury_id(rs.getInt("injury_id"));
            injury.setInjuryType(tn.esprit.entities.InjuryType.valueOf(rs.getString("injuryType")));

            user user = new user();
            user.setUser_id(rs.getInt("user_id"));
            user.setUser_fname(rs.getString("user_fname"));
            user.setUser_lname(rs.getString("user_lname"));

            RecoveryRules recoveryRules = new RecoveryRules(
                    rs.getInt("recoveryrules_id"),
                    new RecoveryPlan(), // Assume recoveryPlan is fetched from another source if needed
                    injury,
                    user,
                    RecoveryRules.Phase.valueOf(rs.getString("phase")),
                    rs.getInt("days"),
                    rs.getString("recommendation"),
                    RecoveryRules.DietType.valueOf(rs.getString("dietType")),
                    rs.getString("nutritionDetails")
            );
            return recoveryRules;
        }

        return null;  // Return null if no record is found for the given user and injury ID
    }


    // Method to fetch recovery rules by a specific user (this could be part of the chatbot conversation flow)
    public List<RecoveryRules> getRecoveryRulesByUser(user user) throws SQLException {
        String query = "SELECT r.*, i.injuryType, i.injuryDate, u.user_fname, u.user_lname " +
                "FROM recoveryrules r " +
                "JOIN injury i ON r.injury_id = i.injury_id " +
                "JOIN user u ON r.user_id = u.user_id " +
                "WHERE r.user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, user.getUser_id());
        ResultSet rs = preparedStatement.executeQuery();

        List<RecoveryRules> recoveryRulesList = new ArrayList<>();
        while (rs.next()) {
            Injury injury = new Injury();
            injury.setInjury_id(rs.getInt("injury_id"));
            injury.setInjuryType(tn.esprit.entities.InjuryType.valueOf(rs.getString("injuryType")));
            injury.setInjuryDate(rs.getDate("injuryDate").toLocalDate());

            user userObj = new user();
            userObj.setUser_id(rs.getInt("user_id"));
            userObj.setUser_fname(rs.getString("user_fname"));
            userObj.setUser_lname(rs.getString("user_lname"));

            RecoveryRules recoveryRules = new RecoveryRules(
                    rs.getInt("recoveryrules_id"),
                    new RecoveryPlan(),  // Same as above, assume this is available if needed
                    injury,
                    userObj,
                    RecoveryRules.Phase.valueOf(rs.getString("phase")),
                    rs.getInt("days"),
                    rs.getString("recommendation"),
                    RecoveryRules.DietType.valueOf(rs.getString("dietType")),
                    rs.getString("nutritionDetails")
            );
            recoveryRulesList.add(recoveryRules);
        }
        return recoveryRulesList;
    }

    // Method to query recovery rules based on injury type
    public List<RecoveryRules> getRecoveryRulesByInjuryType(String injuryType) throws SQLException {
        String query = "SELECT r.*, i.injuryType, u.user_fname, u.user_lname " +
                "FROM recoveryrules r " +
                "JOIN injury i ON r.injury_id = i.injury_id " +
                "JOIN user u ON r.user_id = u.user_id " +
                "WHERE i.injuryType = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, injuryType);
        ResultSet rs = preparedStatement.executeQuery();

        List<RecoveryRules> recoveryRulesList = new ArrayList<>();
        while (rs.next()) {
            Injury injury = new Injury();
            injury.setInjury_id(rs.getInt("injury_id"));
            injury.setInjuryType(tn.esprit.entities.InjuryType.valueOf(rs.getString("injuryType")));

            user user = new user();
            user.setUser_id(rs.getInt("user_id"));
            user.setUser_fname(rs.getString("user_fname"));
            user.setUser_lname(rs.getString("user_lname"));

            RecoveryRules recoveryRules = new RecoveryRules(
                    rs.getInt("recoveryrules_id"),
                    new RecoveryPlan(),
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
        return recoveryRulesList;
    }
}
