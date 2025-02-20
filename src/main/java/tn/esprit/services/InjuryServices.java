package tn.esprit.services;

import tn.esprit.entities.Injury;
import tn.esprit.entities.InjuryType;
import tn.esprit.entities.Severity;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InjuryServices implements IService<Injury> {

    private Connection con;

    public InjuryServices() {
        con = MyDatabase.getInstance().getCon();
    }

    @Override
    public void add(Injury injury) throws SQLException {
        String query = "INSERT INTO injury (user_id, injuryType, injury_description, injuryDate, injury_severity) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, injury.getUser_id());
            ps.setString(2, injury.getInjuryType().toString());
            ps.setString(3, injury.getInjury_description());
            ps.setDate(4, java.sql.Date.valueOf(injury.getInjuryDate()));
            ps.setString(5, injury.getInjury_severity().toString());
            ps.executeUpdate();
            System.out.println("Injury added!");
        } catch (SQLException e) {
            System.out.println("Error adding injury: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Injury> getAll() throws SQLException {
        List<Injury> injuries = new ArrayList<>();
        String query = "SELECT * FROM injury";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int injury_id = rs.getInt("injury_id");
                int user_id = rs.getInt("user_id");
                InjuryType injuryType = InjuryType.valueOf(rs.getString("injuryType"));
                String injuryDescription = rs.getString("injury_description");
                LocalDate injuryDate = rs.getDate("injuryDate").toLocalDate();
                Severity injurySeverity = Severity.valueOf(rs.getString("injury_severity"));

                Injury injury = new Injury(injury_id, user_id, injuryType, injuryDescription, injuryDate, injurySeverity);
                injuries.add(injury);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving injuries: " + e.getMessage());
            throw e;
        }

        return injuries;
    }


    @Override
    public void delete(Injury injury) throws SQLException {
        String query = "DELETE FROM injury WHERE injury_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, injury.getInjury_id());
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Injury deleted successfully!");
            } else {
                System.out.println("No injury found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting injury: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void update(Injury injury) throws SQLException {
        String query = "UPDATE injury SET user_id = ?, injuryType = ?, injury_description = ?, injuryDate = ?, injury_severity = ? WHERE injury_id = ?";

        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, injury.getUser_id());
        stmt.setString(2, injury.getInjuryType().toString());
        stmt.setString(3, injury.getInjury_description());
        stmt.setDate(4, java.sql.Date.valueOf(injury.getInjuryDate()));
        stmt.setString(5, injury.getInjury_severity().toString());
        stmt.setInt(6, injury.getInjury_id());

        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Injury updated successfully!");
        } else {
            System.out.println("No injury found with the given ID.");
        }
    }

    public Injury findById(int injury_id) throws SQLException {
        String query = "SELECT * FROM injury WHERE injury_id = ?";
        Injury injury = null;

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, injury_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int user_id = rs.getInt("user_id");
                InjuryType injuryType = InjuryType.valueOf(rs.getString("injuryType"));
                String injuryDescription = rs.getString("injury_description");
                LocalDate injuryDate = rs.getDate("injuryDate").toLocalDate();
                Severity injurySeverity = Severity.valueOf(rs.getString("injury_severity"));

                injury = new Injury(injury_id, user_id, injuryType, injuryDescription, injuryDate, injurySeverity);
            }
        }

        return injury;
    }

    public Integer getUser_id(String user_fname, String user_lname) throws SQLException {
        UserServices userService = new UserServices();
        return userService.getUser_id(con, user_fname, user_lname);

    }

}
