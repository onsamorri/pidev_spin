package tn.esprit.entities;

import tn.esprit.services.UserServices;

import java.time.LocalDate;

public class Injury {

    private int injury_id;
    private InjuryType injuryType;
    private String injury_description;
    private LocalDate injuryDate;
    private Severity injury_severity;
    private User user;

    // Constructor with all attributes
    public Injury(int injury_id, User user, InjuryType injuryType, String injury_description, LocalDate injuryDate, Severity injury_severity) {
        this.injury_id = injury_id;
        this.user = user;
        this.injuryType = injuryType;
        this.injury_description = injury_description;
        this.injuryDate = injuryDate;
        this.injury_severity = injury_severity;
    }

    public Injury(User user, InjuryType injuryType, String injury_description, LocalDate injuryDate, Severity injury_severity) {

        this.user = user;
        this.injuryType = injuryType;
        this.injury_description = injury_description;
        this.injuryDate = injuryDate;
        this.injury_severity = injury_severity;
    }

    public User getUser() {
        return user;
    }

    // Other getters and setters
    public int getInjury_id() {
        return injury_id;
    }

    public InjuryType getInjuryType() {
        return injuryType;
    }

    public String getInjury_description() {
        return injury_description;
    }


    public LocalDate getInjuryDate() {
        return injuryDate;
    }

    public Severity getInjury_severity() {
        return injury_severity;
    }


    public void setInjury_id(int injury_id) {
        this.injury_id = injury_id;
    }


    public void setUser(User user) {
        this.user = user;
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
                ", user_id=" + (user != null ? user.getUser_id() : "Unknown") +
                ", user_name=" + (user != null ? user.getUser_fname() + " " + user.getUser_lname() : "Unknown") +
                ", injuryType=" + injuryType +
                ", injury_description='" + injury_description + '\'' +
                ", injuryDate=" + injuryDate +
                ", injury_severity=" + injury_severity +
                '}';
    }


}
