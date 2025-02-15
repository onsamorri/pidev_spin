package tn.esprit.main;

import tn.esprit.entities.*;
import tn.esprit.services.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // InjuryServices
            InjuryServices injuryServices = new InjuryServices();

            // Adding an injury using the constructor
            Injury injury1 = new Injury(1, 1, 1, InjuryType.FRACTURE, "Fracture in the leg", java.time.LocalDate.now(), Severity.SEVERE);
            injuryServices.add(injury1);



            // Return all injuries
            List<Injury> allInjuries = injuryServices.returnList();
            allInjuries.forEach(System.out::println);

            // Delete an injury
            injuryServices.delete(injury1);

            // Update an injury
            injury1.setInjury_description("Updated description");
            injuryServices.update(injury1);

            // Search injuries by Type
            List<Injury> fractures = injuryServices.searchByType(InjuryType.FRACTURE);
            fractures.forEach(injury -> System.out.println("Found by Type: " + injury));

            // Sort injuries by Severity
            List<Injury> sortedBySeverity = injuryServices.sortBySeverityAscending();
            sortedBySeverity.forEach(injury -> System.out.println("Sorted by Severity: " + injury));

            // Find injury by id
            Injury foundInjury = injuryServices.findById(1);
            System.out.println("Found Injury by ID: " + foundInjury);

            // Create an instance of RecoveryPlanServices
            RecoveryPlanServices recoveryPlanServices = new RecoveryPlanServices();

            // Create RecovertyPlan
            RecoveryPlan recoveryPlan1 = new RecoveryPlan(
                    32, // injury_id
                    11, // athlete_id
                    8, // coach_id
                    15, // medical_staff_id
                    RecoveryGoal.REHABILITATION,
                    "Recovery after leg fracture",
                    LocalDate.of(2025, 2, 15), // recovery_StartDate
                    LocalDate.of(2025, 2, 15), // recovery_EndDate
                    RecoveryStatus.IN_PROGRESS
            );

            try {
                // Add the recovery plan using the add method
                recoveryPlanServices.add(recoveryPlan1);
            } catch (SQLException e) {
                e.printStackTrace();
            }



            // Return all RecoveryPlans
            List<RecoveryPlan> allRecoveryPlans = recoveryPlanServices.returnList();
            allRecoveryPlans.forEach(System.out::println);

            // Delete a RecoveryPlan
            recoveryPlanServices.delete(recoveryPlan1);

            // Update a RecoveryPlan
            recoveryPlan1.setRecovery_Status(RecoveryStatus.COMPLETED);
            recoveryPlanServices.update(recoveryPlan1);

            // Search RecoveryPlans by Injury id and Status
            List<RecoveryPlan> plansByInjury = recoveryPlanServices.searchByInjuryIdAndStatus(1, RecoveryStatus.IN_PROGRESS);
            plansByInjury.forEach(recoveryPlan -> System.out.println("Found by Injury ID and Status: " + recoveryPlan));

            // in ascending order ASC true and DSC is false
            List<RecoveryPlan> sortedByStartDate = recoveryPlanServices.sortByRecoveryStartDate(true); // Ascending order
            sortedByStartDate.forEach(recoveryPlan -> System.out.println("Sorted by Start Date: " + recoveryPlan));


            // Find a RecoveryPlan by id
            RecoveryPlan foundRecoveryPlan = recoveryPlanServices.findById(1);
            System.out.println("Found Recovery Plan by ID: " + foundRecoveryPlan);

            // View RecoveryPlan by id
            RecoveryPlan viewPlan = recoveryPlanServices.viewRecoveryPlan(1);
            System.out.println("View Recovery Plan: " + viewPlan);

            // Advanced Filtering
            System.out.println("Advanced Filter: Recovery Plans with status COMPLETED in the last 30 days:");
            List<RecoveryPlan> filteredPlans = recoveryPlanServices.advancedFilter(
                    null,
                    java.time.LocalDate.now().minusDays(30),
                    java.time.LocalDate.now()
            );
            filteredPlans.forEach(System.out::println);

            // NutritionPlanServices
            NutritionPlanServices nutritionPlanServices = new NutritionPlanServices();

            // Add a new NutritionPlan (updated)
            NutritionPlan nutritionPlan1 = new NutritionPlan(1, 1, 2, DietType.VEGAN, Allergies.NONE, 2000, java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(7), "Breakfast: Eggs, Lunch: Chicken Salad, Dinner: Grilled Fish", "High protein diet");
            nutritionPlanServices.add(nutritionPlan1);

            // Return all NutritionPlans
            List<NutritionPlan> allNutritionPlans = nutritionPlanServices.returnList();
            allNutritionPlans.forEach(System.out::println);

            // Delete a NutritionPlan
            nutritionPlanServices.delete(nutritionPlan1);

            // Update a NutritionPlan
            nutritionPlan1.setNutrition_notes("Updated high-protein diet");
            nutritionPlanServices.update(nutritionPlan1);

            // Search NutritionPlans by athlete id and DietType
            List<NutritionPlan> nutritionPlansByDiet = nutritionPlanServices.searchByAthleteIdAndDietType(1, DietType.VEGAN);
            nutritionPlansByDiet.forEach(nutritionPlan -> System.out.println("Found by Athlete ID and Diet Type: " + nutritionPlan));

            // Sort NutritionPlans by StartDate (earliest to latest)
            List<NutritionPlan> sortedByStartDateNutrition = nutritionPlanServices.sortByStartDate(true);
            sortedByStartDateNutrition.forEach(nutritionPlan -> System.out.println("Sorted by Start Date: " + nutritionPlan));

            // Find a NutritionPlan by id
            NutritionPlan foundNutritionPlan = nutritionPlanServices.findById(1);
            System.out.println("Found Nutrition Plan by ID: " + foundNutritionPlan);

            // Advanced filtering
            System.out.println("Advanced Filter: Nutrition Plans in the last 7 days:");
            List<NutritionPlan> filteredNutritionPlans = nutritionPlanServices.advancedFilter(
                    DietType.VEGAN,
                    java.time.LocalDate.now().minusDays(7),
                    java.time.LocalDate.now()
            );
            filteredNutritionPlans.forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
