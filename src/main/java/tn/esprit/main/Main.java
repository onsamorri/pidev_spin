package tn.esprit.main;

import tn.esprit.entities.*;
import tn.esprit.services.*;
import tn.esprit.utils.MyDatabase;

import java.sql.Date;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Get the database instance
        MyDatabase db1 = MyDatabase.getInstance();

        // Create service objects
        InjuryServices injuryServices = new InjuryServices();
        RecoveryPlanServices recoveryPlanServices = new RecoveryPlanServices();
        NutritionPlanServices nutritionPlanServices = new NutritionPlanServices();
        UserServices userServices = new UserServices();

        try {
            // Get the database connection instance
            Connection con = MyDatabase.getInstance().getCon();

            // Adding a user (example: athlete)
            User athlete = new Athlete(2, "John", "Doe", "johndoe@example.com", "password", "123456789", Date.valueOf(LocalDate.of(1995, 5, 10)), "Male", "123 Main St", 5.9f, 160.5f, 0, Date.valueOf(LocalDate.now()));
            userServices.add(athlete);

            // Get the user ID by name
            int user_id = userServices.getUser_id(con, "John", "Doe");
            if (user_id == -1) {
                System.out.println("User not found!");
                return; // Exit if user not found
            }

            // Retrieve the User object using the findUserById method
            User user = userServices.findUserById(con, user_id);
            if (user == null) {
                System.out.println("User not found!");
                return; // Exit if user is not found
            }

            // Adding an injury
            Injury injury = new Injury(user, InjuryType.SPRAIN, "Ankle sprain", LocalDate.now(), Severity.MODERATE);
            injuryServices.add(injury);

            // Define recovery plan details
            RecoveryGoal recoveryGoal = RecoveryGoal.REHABILITATION;  // Example recovery goal from enum
            String recoveryDescription = "Recovery from sprain";  // Description of recovery
            LocalDate recoveryStartDate = LocalDate.now();  // Current date as start date
            LocalDate recoveryEndDate = LocalDate.now().plusDays(14);  // Recovery ends 14 days after the start date
            RecoveryStatus recoveryStatus = RecoveryStatus.IN_PROGRESS;  // Current status of recovery

            // Create the RecoveryPlan object with proper parameters
            RecoveryPlan recoveryPlan = new RecoveryPlan(injury, user, recoveryGoal, recoveryDescription, recoveryStartDate, recoveryEndDate, recoveryStatus);
            recoveryPlanServices.add(recoveryPlan);

            // Adding a nutrition plan
            NutritionPlan nutritionPlan = new NutritionPlan(0, 1, DietType.VEGAN, Allergies.NONE, 2000, LocalDate.now(), LocalDate.of(2025, 2, 28), "Balanced meal plan", "No notes");
            nutritionPlanServices.add(nutritionPlan);

            // Updating a user
            athlete.setUser_fname("Jonathan");
            userServices.update(athlete);

            // Updating an injury
            injury.setInjury_description("Severe ankle sprain");
            injuryServices.update(injury);

            // Updating a recovery plan
            recoveryPlan.setRecovery_Status(RecoveryStatus.COMPLETED);
            recoveryPlanServices.update(recoveryPlan);

            // Updating a nutrition plan
            nutritionPlan.setNutrition_meal_plan("Updated meal plan");
            nutritionPlanServices.update(nutritionPlan);

            // Deleting a user
            userServices.delete(athlete);

            // Deleting an injury
            injuryServices.delete(injury);

            // Deleting a recovery plan
            recoveryPlanServices.delete(recoveryPlan);

            // Deleting a nutrition plan
            nutritionPlanServices.delete(nutritionPlan);

            // Getting all users
            List<User> users = userServices.getAll();
            for (User u : users) {
                System.out.println(u);
            }

            // Getting all injuries
            List<Injury> injuries = injuryServices.getAll();
            for (Injury i : injuries) {
                System.out.println(i);
            }

            // Getting all recovery plans
            List<RecoveryPlan> recoveryPlans = recoveryPlanServices.getAll();
            for (RecoveryPlan rp : recoveryPlans) {
                System.out.println(rp);
            }

            // Getting all nutrition plans
            List<NutritionPlan> nutritionPlans = nutritionPlanServices.getAll();
            for (NutritionPlan np : nutritionPlans) {
                System.out.println(np);
            }

        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}
