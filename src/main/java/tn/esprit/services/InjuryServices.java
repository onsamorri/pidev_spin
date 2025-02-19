package tn.esprit.services;

import tn.esprit.entities.Injury;
import tn.esprit.entities.InjuryType;
import tn.esprit.entities.Severity;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InjuryServices implements IService<Injury> {

    private Connection con;

    public InjuryServices() {
        con = MyDatabase.getInstance().getCon();
    }

    @Override
    public void add(Injury injury) throws SQLException {
        String query = "INSERT INTO Injury (user_id, injuryType, injury_severity, injury_description, injuryDate) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setInt(1, injury.getUser_id());
        ps.setString(2, injury.getInjuryType().toString());
        ps.setString(3, injury.getInjury_severity().toString());
        ps.setString(4, injury.getInjury_description());
        ps.setDate(5, Date.valueOf(injury.getInjuryDate()));

        ps.executeUpdate();
        System.out.println("Injury added!");
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
        String query = "UPDATE Injury SET user_id = ?, injuryType = ?, injury_severity = ?, injury_description = ?, injuryDate = ? WHERE injury_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, injury.getUser_id());
        ps.setString(2, injury.getInjuryType().toString());
        ps.setString(3, injury.getInjury_severity().toString());
        ps.setString(4, injury.getInjury_description());
        ps.setDate(5, Date.valueOf(injury.getInjuryDate()));
        ps.setInt(6, injury.getInjury_id());
        ps.executeUpdate();
        System.out.println("Injury updated!");
    }

    @Override
    public List<Injury> getAll() throws SQLException {
        String query = "SELECT * FROM Injury";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return buildInjuryList(rs);
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
        String query = "SELECT * FROM Injury ORDER BY FIELD(injury_severity, 'MILD', 'MODERATE', 'SEVERE', 'CRITICAL') " + order;
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return buildInjuryList(rs);
    }

    public List<Injury> sortBySeverityAscending() throws SQLException {
        List<Injury> injuries = getAll();
        injuries.sort(Comparator.comparing(Injury::getInjury_severity));
        return injuries;
    }

    public List<Injury> sortBySeverityDescending() throws SQLException {
        List<Injury> injuries = getAll();
        injuries.sort(Comparator.comparing(Injury::getInjury_severity).reversed());
        return injuries;
    }

    public Injury findById(int injuryId) throws SQLException {
        String query = "SELECT * FROM Injury WHERE injury_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, injuryId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Injury(
                    rs.getInt("injury_id"),
                    rs.getInt("user_id"),
                    InjuryType.valueOf(rs.getString("injuryType")),
                    rs.getString("injury_description"),
                    rs.getDate("injuryDate").toLocalDate(),
                    Severity.valueOf(rs.getString("injury_severity"))
            );
        }
        return null;
    }

    private List<Injury> buildInjuryList(ResultSet rs) throws SQLException {
        List<Injury> injuries = new ArrayList<>();
        while (rs.next()) {
            Injury injury = new Injury(
                    rs.getInt("injury_id"),
                    rs.getInt("user_id"),
                    InjuryType.valueOf(rs.getString("injuryType")),
                    rs.getString("injury_description"),
                    rs.getDate("injuryDate").toLocalDate(),
                    Severity.valueOf(rs.getString("injury_severity"))
            );
            injuries.add(injury);
        }
        return injuries;
    }
}
