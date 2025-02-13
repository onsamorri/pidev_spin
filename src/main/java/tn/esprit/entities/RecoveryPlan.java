package tn.esprit.entities;

import java.time.LocalDate;

public class RecoveryPlan {

    private int recoveryPlanId;
    private int injury_id;
    private RecoveryPlanGoal goal;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private RecoveryPlanStatus status;

    // Constructor
    public RecoveryPlan(int recoveryPlanId, int injury_id, RecoveryPlanGoal goal, String description, LocalDate startDate, LocalDate endDate, RecoveryPlanStatus status) {
        this.recoveryPlanId = recoveryPlanId;
        this.injury_id = injury_id;
        this.goal = goal;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and Setters
    public int getRecoveryPlanId() {
        return recoveryPlanId;
    }

    public void setRecoveryPlanId(int recoveryPlanId) {
        this.recoveryPlanId = recoveryPlanId;
    }

    public int getInjury_id() {
        return injury_id;
    }

    public void setInjury_id(int injury_id) {
        this.injury_id = injury_id;
    }

    public RecoveryPlanGoal getGoal() {
        return goal;
    }

    public void setGoal(RecoveryPlanGoal goal) {
        this.goal = goal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public RecoveryPlanStatus getStatus() {
        return status;
    }

    public void setStatus(RecoveryPlanStatus status) {
        this.status = status;
    }
}
