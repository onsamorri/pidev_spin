package tn.esprit.Controllers;

import tn.esprit.entities.RecoveryPlan;
import tn.esprit.services.RecoveryPlanServices;

import java.sql.SQLException;

public class DeleteRecoveryPlanController {

    private RecoveryPlanServices recoveryPlanServices;

    public DeleteRecoveryPlanController() {
        recoveryPlanServices = new RecoveryPlanServices();
    }

    public void deleteRecoveryPlan(RecoveryPlan recoveryPlan) {
        try {
            recoveryPlanServices.delete(recoveryPlan);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}