package tn.esprit.entities;

import java.time.LocalDate;

public class Injury {

    private int injury_id;
    private InjuryType injuryType;
    private String description;
    private LocalDate injuryDate;
    private Severity severity;

    public Injury(int injury_id, InjuryType injuryType, String description, LocalDate injuryDate, Severity severity) {
        this.injury_id = injury_id;
        this.injuryType = injuryType;
        this.description = description;
        this.injuryDate = injuryDate;
        this.severity = severity;
    }

    public Injury(int injury_id) {
        this.injury_id = injury_id;
    }

    public Injury(InjuryType injuryType, Severity severity) {
        this.injuryType = injuryType;
        this.severity = severity;
    }

    public Injury(InjuryType injuryType, Severity severity, String description, LocalDate injuryDate) {
        this.injuryType = injuryType;
        this.severity = severity;
        this.description = description;
        this.injuryDate = injuryDate;
    }

    public int getInjury_id() {
        return injury_id;
    }

    public InjuryType getInjuryType() {
        return injuryType;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getInjuryDate() {
        return injuryDate;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setInjuryType(InjuryType injuryType) {
        this.injuryType = injuryType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInjuryDate(LocalDate injuryDate) {
        this.injuryDate = injuryDate;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    @Override
    public String toString() {
        return "Injury{" +
                "injury_id=" + injury_id +
                ", injuryType=" + injuryType +
                ", description='" + description + '\'' +
                ", injuryDate=" + injuryDate +
                ", severity=" + severity +
                '}';
    }


}
