package tn.esprit.entities;

public class RecoveryRules {
    private int recoveryrules_id;
    private RecoveryPlan recoveryPlan;
    private Injury injury;
    private user user;
    private Phase phase;
    private int days;
    private String recommendation;
    private DietType dietType;
    private String nutritionDetails;

    public RecoveryRules(int recoveryrules_id, RecoveryPlan recoveryPlan, Injury injury, user user, Phase phase,
                         int days, String recommendation, DietType dietType, String nutritionDetails) {
        this.recoveryrules_id = recoveryrules_id;
        this.recoveryPlan = recoveryPlan;
        this.injury = injury;
        this.user = user;
        this.phase = phase;
        this.days = days;
        this.recommendation = recommendation;
        this.dietType = dietType;
        this.nutritionDetails = nutritionDetails;
    }

    public RecoveryRules(RecoveryPlan recoveryPlan, Injury injury, user user, Phase phase,
                         int days, String recommendation, DietType dietType, String nutritionDetails) {
        this.recoveryPlan = recoveryPlan;
        this.injury = injury;
        this.phase = phase;
        this.days = days;
        this.recommendation = recommendation;
        this.dietType = dietType;
        this.nutritionDetails = nutritionDetails;
    }

    public RecoveryRules() {
    }

    public int getRecoveryrules_id() {
        return recoveryrules_id;
    }

    public void setRecoveryrules_id(int recoveryrules_id) {
        this.recoveryrules_id = recoveryrules_id;
    }

    public RecoveryPlan getRecoveryPlan() {
        return recoveryPlan;
    }

    public void setRecoveryPlan(RecoveryPlan recoveryPlan) {
        this.recoveryPlan = recoveryPlan;
    }

    public Injury getInjury() {
        return injury;
    }

    public void setInjury(Injury injury) {
        this.injury = injury;
    }

    public user getUser() {
        return user;
    }

    public void setUser(user user) {
        this.user = user;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public DietType getDietType() {
        return dietType;
    }

    public void setDietType(DietType dietType) {
        this.dietType = dietType;
    }

    public String getNutritionDetails() {
        return nutritionDetails;
    }

    public void setNutritionDetails(String nutritionDetails) {
        this.nutritionDetails = nutritionDetails;
    }

    @Override
    public String toString() {
        return "RecoveryRules{" +
                "recoveryrules_id=" + recoveryrules_id +
                ", recoveryPlan=" + recoveryPlan +
                ", injury=" + injury +
                ", user=" + user +
                ", phase=" + phase +
                ", days=" + days +
                ", recommendation='" + recommendation + '\'' +
                ", dietType=" + dietType +
                ", nutritionDetails='" + nutritionDetails + '\'' +
                '}';
    }


    public enum Phase {
        EARLY, MID, LATE
    }

    public enum DietType {
        HIGH_PROTEIN, BALANCED_DIET, LOW_CARB, CALCIUM_RICH, PROTEIN_RICH, VITAMIN_C_RICH, MAINTENANCE, BALANCED, VEGAN, DETOX
    }
}
