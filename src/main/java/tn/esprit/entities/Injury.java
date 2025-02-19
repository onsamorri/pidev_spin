package tn.esprit.entities;

import java.time.LocalDate;

public class Injury {

    private int injury_id;
    private int user_id;
    private InjuryType injuryType;
    private String injury_description;
    private LocalDate injuryDate;
    private Severity injury_severity;


    public Injury(int injury_id, int user_id, InjuryType injuryType, String injury_description, LocalDate injuryDate, Severity injury_severity) {
        this.injury_id = injury_id;
        this.user_id = user_id;
        this.injuryType = injuryType;
        this.injury_description = injury_description;
        this.injuryDate = injuryDate;
        this.injury_severity = injury_severity;
    }

    public Injury(int injury_id) {
        this.injury_id = injury_id;
    }

    public Injury(int user_id, InjuryType injuryType, Severity injury_severity) {
        this.user_id = user_id;
        this.injuryType = injuryType;
        this.injury_severity = injury_severity;
    }

    public Injury(int user_id, InjuryType injuryType, Severity injury_severity, String injury_description, LocalDate injuryDate) {
        this.user_id = user_id;
        this.injuryType = injuryType;
        this.injury_severity = injury_severity;
        this.injury_description = injury_description;
        this.injuryDate = injuryDate;
    }

    public int getInjury_id() {
        return injury_id;
    }

    public int getUser_id() {
        return user_id;
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

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
                ", user_id=" + user_id +
                ", injuryType=" + injuryType +
                ", injury_description='" + injury_description + '\'' +
                ", injuryDate=" + injuryDate +
                ", injury_severity=" + injury_severity +
                '}';
    }
}
