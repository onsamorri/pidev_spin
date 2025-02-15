package tn.esprit.entities;

import java.time.LocalDate;

public class Injury {

    private int injury_id;
    private int athlete_id;
    private int medical_staff_id;
    private InjuryType injuryType;
    private String injury_description;
    private LocalDate injuryDate;
    private Severity injury_severity;


    public Injury(int injury_id, int athlete_id, int medical_staff_id, InjuryType injuryType, String injury_description, LocalDate injuryDate, Severity injury_severity) {
        this.injury_id = injury_id;
        this.athlete_id = athlete_id;
        this.medical_staff_id = medical_staff_id;
        this.injuryType = injuryType;
        this.injury_description = injury_description;
        this.injuryDate = injuryDate;
        this.injury_severity = injury_severity;
    }

    public Injury(int injury_id) {
        this.injury_id = injury_id;
    }

    public Injury(int athlete_id, int medical_staff_id, InjuryType injuryType, Severity injury_severity) {
        this.athlete_id = athlete_id;
        this.medical_staff_id = medical_staff_id;
        this.injuryType = injuryType;
        this.injury_severity = injury_severity;
    }

    public Injury(int athlete_id, int medical_staff_id, InjuryType injuryType, Severity injury_severity, String injury_description, LocalDate injuryDate) {
        this.athlete_id = athlete_id;
        this.medical_staff_id = medical_staff_id;
        this.injuryType = injuryType;
        this.injury_severity = injury_severity;
        this.injury_description = injury_description;
        this.injuryDate = injuryDate;
    }

    public int getInjury_id() {
        return injury_id;
    }

    public int getAthlete_id() {
        return athlete_id;
    }

    public int getMedical_staff_id() {
        return medical_staff_id;
    }

    public InjuryType getInjuryType() {
        return injuryType;
    }

    public String getInjury_description() {
        return injury_description;
    }

    public LocalDate getInjuryDate() {  // updated method name
        return injuryDate;
    }

    public Severity getInjury_severity() {
        return injury_severity;
    }

    public void setAthlete_id(int athlete_id) {
        this.athlete_id = athlete_id;
    }

    public void setMedical_staff_id(int medical_staff_id) {
        this.medical_staff_id = medical_staff_id;
    }

    public void setInjuryType(InjuryType injuryType) {
        this.injuryType = injuryType;
    }

    public void setInjury_description(String injury_description) {
        this.injury_description = injury_description;
    }

    public void setInjuryDate(LocalDate injuryDate) {
        this.injuryDate = injuryDate;
    }

    public void setInjury_severity(Severity injury_severity) {
        this.injury_severity = injury_severity;
    }

    @Override
    public String toString() {
        return "Injury{" +
                "injury_id=" + injury_id +
                ", athlete_id=" + athlete_id +
                ", medical_staff_id=" + medical_staff_id +
                ", injuryType=" + injuryType +
                ", injury_description='" + injury_description + '\'' +
                ", injuryDate=" + injuryDate +
                ", injury_severity=" + injury_severity +
                '}';
    }
}
