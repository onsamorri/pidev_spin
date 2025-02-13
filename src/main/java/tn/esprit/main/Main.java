package tn.esprit.main;

import tn.esprit.entities.*;
import tn.esprit.services.*;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Injury Services
            InjuryServices injuryServices = new InjuryServices();

            // Add a new Injury
            Injury injury1 = new Injury(1, InjuryType.FRACTURE, "Fracture in the leg", java.time.LocalDate.now(), Severity.SEVERE);
            injuryServices.add(injury1);

            // Return all injuries
            List<Injury> allInjuries = injuryServices.returnList();
            allInjuries.forEach(injury -> System.out.println(injury));

            // Delete an Injury
            injuryServices.delete(injury1);

            // Update an Injury
            injury1.setDescription("Updated description");
            injuryServices.update(injury1);

            // Search Injuries by Type
            List<Injury> fractures = injuryServices.searchByType(InjuryType.FRACTURE);
            fractures.forEach(injury -> System.out.println("Found by Type: " + injury));

            // Sort Injuries by Severity
            List<Injury> sortedBySeverity = injuryServices.sortBySeverityAscending();
            sortedBySeverity.forEach(injury -> System.out.println("Sorted by Severity: " + injury));

            // Find Injury by ID
            Injury foundInjury = injuryServices.findById(1);
            System.out.println("Found Injury by ID: " + foundInjury);

            // Recovery Plan Services
            RecoveryPlanServices recoveryPlanServices = new RecoveryPlanServices();

            // Add a new Recovery Plan
            RecoveryPlan recoveryPlan1 = new RecoveryPlan(1, 5, RecoveryPlanGoal.REHABILITATION, "Initial recovery plan", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(30), RecoveryPlanStatus.IN_PROGRESS);
            recoveryPlanServices.add(recoveryPlan1);

            // Return all Recovery Plans
            List<RecoveryPlan> allRecoveryPlans = recoveryPlanServices.returnList();
            allRecoveryPlans.forEach(recoveryPlan -> System.out.println(recoveryPlan));

            // Delete a Recovery Plan
            recoveryPlanServices.delete(recoveryPlan1);

            // Update a Recovery Plan
            recoveryPlan1.setStatus(RecoveryPlanStatus.COMPLETED);
            recoveryPlanServices.update(recoveryPlan1);

            // Search Recovery Plans by Injury ID and Status
            List<RecoveryPlan> plansByInjury = recoveryPlanServices.searchByInjuryIdAndStatus(1, RecoveryPlanStatus.IN_PROGRESS);
            plansByInjury.forEach(recoveryPlan -> System.out.println("Found by Injury ID and Status: " + recoveryPlan));

            // Sort Recovery Plans by Start Date (earliest to latest)
            List<RecoveryPlan> sortedByStartDate = recoveryPlanServices.sortByStartDate(true);
            sortedByStartDate.forEach(recoveryPlan -> System.out.println("Sorted by Start Date: " + recoveryPlan));

            // Find a Recovery Plan by ID
            RecoveryPlan foundRecoveryPlan = recoveryPlanServices.findById(1);
            System.out.println("Found Recovery Plan by ID: " + foundRecoveryPlan);

            // View Recovery Plan by ID
            RecoveryPlan viewPlan = recoveryPlanServices.viewRecoveryPlan(1);
            System.out.println("View Recovery Plan: " + viewPlan);

            // Advanced Filtering
            System.out.println("Advanced Filter: Recovery Plans with status COMPLETED in the last 30 days:");
            List<RecoveryPlan> filteredPlans = recoveryPlanServices.advancedFilter(
                    null, // status can be null!!!!
                    java.time.LocalDate.now().minusDays(30), // start date
                    java.time.LocalDate.now() // end date
            );
            filteredPlans.forEach(recoveryPlan -> System.out.println(recoveryPlan));


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
