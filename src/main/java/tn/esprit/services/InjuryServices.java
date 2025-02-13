package tn.esprit.services;

import tn.esprit.entities.Injury;
import tn.esprit.entities.InjuryType;
import tn.esprit.entities.Severity;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InjuryServices implements IService<Injury> {

    private Connection con;

    public InjuryServices() {
        con = MyDatabase.getInstance().getCon();
    }

    @Override
    public void add(Injury injury) throws SQLException {
        String query = "INSERT INTO Injury (injuryType, severity, description, injuryDate) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, injury.getInjuryType().toString());
        ps.setString(2, injury.getSeverity().toString());
        ps.setString(3, injury.getDescription());
        ps.setDate(4, Date.valueOf(injury.getInjuryDate()));
        ps.executeUpdate();
        System.out.println("Injury added!");
    }

    @Override
    public List<Injury> returnList() throws SQLException {
        String query = "SELECT * FROM Injury";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return buildInjuryList(rs);
    }

    @Override
    public void delete(Injury injury) throws SQLException {
        String query = "DELETE FROM Injury WHERE injury_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, injury.getInjury_id());
        ps.executeUpdate();
        System.out.println("Injury deleted!");
    }

    @Override
    public void update(Injury injury) throws SQLException {
        String query = "UPDATE Injury SET injuryType = ?, severity = ?, description = ?, injuryDate = ? WHERE injury_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, injury.getInjuryType().toString());
        ps.setString(2, injury.getSeverity().toString());
        ps.setString(3, injury.getDescription());
        ps.setDate(4, Date.valueOf(injury.getInjuryDate()));
        ps.setInt(5, injury.getInjury_id());
        ps.executeUpdate();
        System.out.println("Injury updated!");
    }

    public List<Injury> searchByType(InjuryType type) throws SQLException {
        String query = "SELECT * FROM Injury WHERE injuryType = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, type.toString());
        ResultSet rs = ps.executeQuery();
        return buildInjuryList(rs);
    }

    public List<Injury> sortBySeverity(boolean ascending) throws SQLException {
        String order = ascending ? "ASC" : "DESC";
        String query = "SELECT * FROM Injury ORDER BY FIELD(severity, 'MILD', 'MODERATE', 'SEVERE', 'CRITICAL') " + order;
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return buildInjuryList(rs);
    }

    // Sort injuries by severity in ascending order (MILD to CRITICAL)
    public List<Injury> sortBySeverityAscending() throws SQLException {
        List<Injury> injuries = returnList();
        Collections.sort(injuries, Comparator.comparing(Injury::getSeverity));
        return injuries;
    }

    // Sort injuries by severity in descending order (CRITICAL to MILD)
    public List<Injury> sortBySeverityDescending() throws SQLException {
        List<Injury> injuries = returnList();
        Collections.sort(injuries, Comparator.comparing(Injury::getSeverity).reversed());
        return injuries;
    }

    // Find an injury by its ID
    public Injury findById(int injuryId) throws SQLException {
        String query = "SELECT * FROM Injury WHERE injury_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, injuryId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Injury(
                    rs.getInt("injury_id"),
                    InjuryType.valueOf(rs.getString("injuryType")),
                    rs.getString("description"),
                    rs.getDate("injuryDate").toLocalDate(),
                    Severity.valueOf(rs.getString("severity"))
            );
        } else {
            return null; // Injury not found
        }
    }

    /* Purpose: This method converts the ResultSet into a list of Injury objects.
    Each row in the ResultSet is turned into an Injury object and added to the list. */
    private List<Injury> buildInjuryList(ResultSet rs) throws SQLException {
        List<Injury> injuries = new ArrayList<>(); //Creates an empty list to store the Injury objects.
        while (rs.next()) { //Loops through each row in the ResultSet. For every row, an Injury object is created.
            Injury injury = new Injury(
                    rs.getInt("injury_id"),
                    InjuryType.valueOf(rs.getString("injuryType")),
                    rs.getString("description"),
                    rs.getDate("injuryDate").toLocalDate(),
                    Severity.valueOf(rs.getString("severity"))
            );
            injuries.add(injury);
        }
        return injuries;
    }
}
