package tn.esprit.services;

import tn.esprit.entities.*;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class RecoveryRulesChatBotServices {
    private Connection connection = MyDatabase.getInstance().getCon();

    public String getChatBotResponse(String userMessage, user currentUser) {
        if (currentUser == null) return "User not authenticated.";

        String lowerMessage = userMessage.trim().toLowerCase();

        // Greeting handling
        if (lowerMessage.equals("hello") || lowerMessage.equals("hi")) {
            return getWelcomeMessage();
        }

        try {
            List<Injury> injuries = getInjuriesByUserName(currentUser.getUser_fname(), currentUser.getUser_lname());
            List<RecoveryPlan> recoveryPlans = getRecoveryPlansByUserName(currentUser.getUser_fname(), currentUser.getUser_lname());

            if (injuries.isEmpty() || recoveryPlans.isEmpty()) return "No recovery data found.";

            StringBuilder response = new StringBuilder();
            boolean nutritionAdded = false;
            boolean exerciseAdded = false;

            for (RecoveryPlan recoveryPlan : recoveryPlans) {
                int daysLeft = calculateRecoveryDays(recoveryPlan);
                RecoveryRules.Phase phase = determineRecoveryPhase(recoveryPlan);

                // Recovery phase
                if (lowerMessage.contains("recovery phase") || lowerMessage.contains("progress")) {
                    response.append("Recovery Plan ID ").append(recoveryPlan.getRecovery_id()).append(":\n")
                            .append("You're in the ").append(phase).append(" phase. ")
                            .append("You have ").append(daysLeft).append(" days left.\n\n");
                }
            }

            // Use the first injury for nutrition/exercise (avoid repeating)
            if (!injuries.isEmpty()) {
                Injury injury = injuries.get(0);
                Map<String, String> advice = getNutritionAndRecommendation(injury.getInjuryType());

                // Nutrition
                if ((lowerMessage.contains("eat") || lowerMessage.contains("nutrition") || lowerMessage.contains("diet") || lowerMessage.contains("food")) && !nutritionAdded) {
                    response.append("Recommended diet: ").append(advice.get("nutrition")).append("\n");
                    nutritionAdded = true;
                }

                // Exercise
                if ((lowerMessage.contains("exercise") || lowerMessage.contains("workout") || lowerMessage.contains("training") || lowerMessage.contains("activity")) && !exerciseAdded) {
                    response.append("Recommended activity: ").append(advice.get("recommendation")).append("\n");
                    exerciseAdded = true;
                }
            }

            // Full recovery plan request
            if (lowerMessage.contains("recovery plan") || lowerMessage.contains("show my recovery plan") || lowerMessage.contains("details of my recovery")) {
                response.append("Your recovery plans:\n");
                for (RecoveryPlan recoveryPlan : recoveryPlans) {
                    response.append("Plan ID ").append(recoveryPlan.getRecovery_id()).append(":\n")
                            .append("Goal: ").append(recoveryPlan.getRecovery_Goal()).append("\n")
                            .append("Description: ").append(recoveryPlan.getRecovery_Description()).append("\n")
                            .append("Start Date: ").append(recoveryPlan.getRecovery_StartDate()).append("\n")
                            .append("End Date: ").append(recoveryPlan.getRecovery_EndDate()).append("\n")
                            .append("Status: ").append(recoveryPlan.getRecovery_Status()).append("\n\n");
                }
            }

            return response.toString().trim().isEmpty() ? "I didn't understand that. Try asking about recovery phase, nutrition, or exercises." : response.toString();

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving data: " + e.getMessage();
        }
    }

    private String getWelcomeMessage() {
        return """
        Welcome to the Recovery ChatDoc! 🤖 Here are some things you can ask:
        - "What is my recovery phase?" → Get your current recovery phase and remaining days.
        - "What should I eat for recovery?" → Get nutrition advice based on your injury.
        - "What exercises can I do?" → Get activity recommendations.
        - "Show my recovery plan" → View your full recovery plan details.
        - "Hi" or "Hello" → See this message again.
        
        How can I assist you today?
        """;
    }


    private int getUserIdByName(String user_fname, String user_lname) throws SQLException {
        String query = "SELECT user_id FROM user WHERE user_fname = ? AND user_lname = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user_fname);
            statement.setString(2, user_lname);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            } else {
                throw new SQLException("User not found");
            }
        }
    }

    public List<RecoveryPlan> getRecoveryPlansByUserName(String user_fname, String user_lname) throws SQLException {
        int user_id = getUserIdByName(user_fname, user_lname); // Get user_id based on name
        List<RecoveryPlan> recoveryPlans = new ArrayList<>();
        String query = "SELECT * FROM recoveryplan WHERE user_id = ?";

        // Use the already established connection
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    RecoveryPlan recoveryPlan = new RecoveryPlan();
                    recoveryPlan.setRecovery_id(resultSet.getInt("recovery_id"));
                    recoveryPlan.setRecovery_Description(resultSet.getString("recovery_Description"));
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

    public List<Injury> getInjuriesByUserName(String user_fname, String user_lname) throws SQLException {
        int user_id = getUserIdByName(user_fname, user_lname); // Get user_id based on name
        List<Injury> injuries = new ArrayList<>();
        String query = "SELECT * FROM injury WHERE user_id = ?";

        // Use the already established connection
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user_id);
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

    private Injury getInjuryByUser(int user_id) throws SQLException {
        String query = "SELECT * FROM injury WHERE user_id = ? LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Create user object
                user u = getUserById(user_id);
                // Create RecoveryPlan object
                RecoveryPlan recoveryPlan = getRecoveryPlanByInjuryId(rs.getInt("injury_id"));
                // Return Injury object with all dependencies
                return new Injury(
                        rs.getInt("injury_id"),
                        InjuryType.valueOf(rs.getString("injuryType")),
                        rs.getDate("injuryDate").toLocalDate(),
                        Severity.valueOf(rs.getString("injury_severity")),
                        rs.getString("injury_description"),
                        u, // Pass user object
                        recoveryPlan
                );
            }
        }
        return null;
    }

    private user getUserById(int user_id) throws SQLException {
        String query = "SELECT * FROM user WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new user(rs.getInt("user_id"), rs.getString("user_fname"),  rs.getString("user_lname"), rs.getString("user_email"));
            }
        }
        return null;
    }

    private RecoveryPlan getRecoveryPlanByInjuryId(int injury_id) throws SQLException {
        String query = "SELECT * FROM recoveryplan WHERE injury_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, injury_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new RecoveryPlan(
                        rs.getInt("recovery_id"),
                        null, // Injury will be set later
                        getUserById(rs.getInt("user_id")),
                        RecoveryGoal.valueOf(rs.getString("recovery_Goal")),
                        rs.getString("recovery_Description"),
                        rs.getDate("recovery_StartDate").toLocalDate(),
                        rs.getDate("recovery_EndDate").toLocalDate(),
                        RecoveryStatus.valueOf(rs.getString("recovery_Status"))
                );
            }
        }
        return null;
    }


    private RecoveryPlan getRecoveryPlanByUser(int user_id) throws SQLException {
        String query = "SELECT * FROM recoveryplan WHERE user_id = ? LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new RecoveryPlan(rs.getInt("recovery_id"), RecoveryGoal.valueOf(rs.getString("recovery_Goal")),
                        rs.getString("recovery_description"), rs.getDate("recovery_StartDate").toLocalDate(),
                        rs.getDate("recovery_EndDate").toLocalDate(), RecoveryStatus.valueOf(rs.getString("recovery_Status")));
            }
        }
        return null;
    }

    private int calculateRecoveryDays(RecoveryPlan recoveryPlan) {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), recoveryPlan.getRecovery_EndDate());
    }

    private RecoveryRules.Phase determineRecoveryPhase(RecoveryPlan recoveryPlan) {
        int daysLeft = calculateRecoveryDays(recoveryPlan);

        if (daysLeft >= 1 && daysLeft <= 5) {
            return RecoveryRules.Phase.EARLY;
        } else if (daysLeft >= 6 && daysLeft <= 10) {
            return RecoveryRules.Phase.MID;
        } else {
            return RecoveryRules.Phase.LATE;
        }
    }


    private Map<String, String> getNutritionAndRecommendation(InjuryType injuryType) {
        Map<String, String> advice = new HashMap<>();
        switch (injuryType) {
            case SPRAIN -> {
                advice.put("nutrition", "Anti-inflammatory foods like turmeric, ginger, and leafy greens.");
                advice.put("recommendation", "Gentle stretching and ice therapy.");
            }
            case FRACTURE -> {
                advice.put("nutrition", "Calcium and vitamin D-rich foods like dairy, almonds, and fish.");
                advice.put("recommendation", "Rest and light movement when advised by a doctor.");
            }
            case CONCUSSION -> {
                advice.put("nutrition", "Omega-3-rich foods like salmon and flaxseeds.");
                advice.put("recommendation", "Complete rest; avoid screen time.");
            }
            case BRUISE -> {
                advice.put("nutrition", "Iron-rich foods like spinach and red meat.");
                advice.put("recommendation", "Cold compress and light massage.");
            }
            default -> {
                advice.put("nutrition", "Balanced diet with proteins and vitamins.");
                advice.put("recommendation", "Consult your physician.");
            }
        }
        return advice;
    }

}
