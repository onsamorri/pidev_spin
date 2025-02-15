package tn.esprit.entities;

import java.time.LocalDate;

public class RecoveryPlan {

    private int recovery_id;
    private int injury_id;
    private int athlete_id;
    private int coach_id;
    private int medical_staff_id;
    private RecoveryGoal recovery_Goal;
    private String recovery_Description;
    private LocalDate recovery_StartDate;
    private LocalDate recovery_EndDate;
    private RecoveryStatus Recovery_Status;

    // Constructor
    public RecoveryPlan(int injury_id, int athlete_id, int coach_id, int medical_staff_id,
                        RecoveryGoal recovery_Goal, String recovery_Description,
                        LocalDate recovery_StartDate, LocalDate recovery_EndDate,
                        RecoveryStatus Recovery_Status) {
        this.injury_id = injury_id;
        this.athlete_id = athlete_id;
        this.coach_id = coach_id;
        this.medical_staff_id = medical_staff_id;
        this.recovery_Goal = recovery_Goal;
        this.recovery_Description = recovery_Description;
        this.recovery_StartDate = recovery_StartDate;
        this.recovery_EndDate = recovery_EndDate;
        this.Recovery_Status = Recovery_Status;
    }

    // Constructor
    public RecoveryPlan(int recovery_id, int injury_id, int athlete_id, int coach_id, int medical_staff_id,
                        RecoveryGoal recovery_Goal, String recovery_Description,
                        LocalDate recovery_StartDate, LocalDate recovery_EndDate,
                        RecoveryStatus Recovery_Status) {
        this.recovery_id = recovery_id;
        this.injury_id = injury_id;
        this.athlete_id = athlete_id;
        this.coach_id = coach_id;
        this.medical_staff_id = medical_staff_id;
        this.recovery_Goal = recovery_Goal;
        this.recovery_Description = recovery_Description;
        this.recovery_StartDate = recovery_StartDate;
        this.recovery_EndDate = recovery_EndDate;
        this.Recovery_Status = Recovery_Status;
    }

    // Getters and Setters
    public int getRecovery_id() {
        return recovery_id;
    }

    public void setRecovery_id(int recovery_id) {
        this.recovery_id = recovery_id;
    }

    public int getInjury_id() {
        return injury_id;
    }

    public void setInjury_id(int injury_id) {
        this.injury_id = injury_id;
    }

    public int getAthlete_id() {
        return athlete_id;
    }

    public void setAthlete_id(int athlete_id) {
        this.athlete_id = athlete_id;
    }

    public int getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(int coach_id) {
        this.coach_id = coach_id;
    }

    public int getMedical_staff_id() {
        return medical_staff_id;  // Get medical_staff_id
    }

    public void setMedical_staff_id(int medical_staff_id) {
        this.medical_staff_id = medical_staff_id;  // Set medical_staff_id
    }

    public RecoveryGoal getRecovery_Goal() {
        return recovery_Goal;
    }

    public void setRecovery_Goal(RecoveryGoal recovery_Goal) {
        this.recovery_Goal = recovery_Goal;
    }

    public String getRecovery_Description() {
        return recovery_Description;
    }

    public void setRecovery_Description(String recovery_Description) {
        this.recovery_Description = recovery_Description;
    }

    public LocalDate getRecovery_StartDate() {
        return recovery_StartDate;
    }

    public void setRecovery_StartDate(LocalDate recovery_StartDate) {
        this.recovery_StartDate = recovery_StartDate;
    }

    public LocalDate getRecovery_EndDate() {
        return recovery_EndDate;
    }

    public void setRecovery_EndDate(LocalDate recovery_EndDate) {
        this.recovery_EndDate = recovery_EndDate;
    }

    public RecoveryStatus getRecovery_Status() {
        return Recovery_Status;
    }

    public void setRecovery_Status(RecoveryStatus Recovery_Status) {
        this.Recovery_Status = Recovery_Status;
    }
}
