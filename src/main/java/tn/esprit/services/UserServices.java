package tn.esprit.services;

import tn.esprit.entities.*;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServices implements IService<user> {
    private Connection con;

    public UserServices() {
        con = MyDatabase.getInstance().getCon();
    }

    @Override
    public void add(user user) throws SQLException {
        if (user instanceof Admin) {
            System.out.println("Admins must be added manually in the database.");
            return; // Don't allow adding admins through this method
        }
        String query = "INSERT INTO `user`(`user_fname`, `user_lname`, `user_email`, `user_pwd`, `user_nbr`, `user_role`, `nb_teams`,`med_specialty`,`athlete_DoB`, `athlete_gender`,`athlete_address`, `athlete_height`, `athlete_weight`,`isInjured`,`athlete_regDate` )" + " VALUES (?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, user.getUser_fname());
            ps.setString(2, user.getUser_lname());
            ps.setString(3, user.getUser_email());
            ps.setString(4, user.getUser_pwd());
            ps.setString(5, user.getUser_nbr());
            ps.setString(6, user.getUser_role().name());
            if (user instanceof Coach) {
                Coach coach = (Coach) user;
                ps.setInt(7, coach.getNb_teams());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            if (user instanceof Medical_staff) {
                Medical_staff medical_staff = (Medical_staff) user;
                ps.setString(8, medical_staff.getSpeciality());
            } else {
                ps.setNull(8, java.sql.Types.VARCHAR);
            }
            if (user instanceof Athlete) {
                Athlete athlete = (Athlete) user;
                ps.setFloat(12, athlete.getAthlete_height());
                ps.setFloat(13, athlete.getAthlete_weight());
                ps.setString(10, athlete.getAthlete_gender());
                ps.setString(11, athlete.getAthlete_address());
                ps.setInt(14, athlete.getIsInjured());
                ps.setDate(9, athlete.getAthlete_DoB());
                ps.setDate(15, athlete.getAthlete_regDate());
            } else {
                ps.setNull(12, java.sql.Types.FLOAT);
                ps.setNull(13, java.sql.Types.FLOAT);
                ps.setNull(10, java.sql.Types.VARCHAR);
                ps.setNull(11, java.sql.Types.VARCHAR);
                ps.setNull(14, java.sql.Types.INTEGER);
                ps.setNull(9, java.sql.Types.DATE);
                ps.setNull(15, java.sql.Types.DATE);
            }
            ps.executeUpdate();
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    @Override
    public void delete(user user) {
        String query = "DELETE FROM user WHERE user_id = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Use the user object to get the user_id
            ps.setInt(1, user.getUser_id()); // Assuming you have a `getUser_id()` method in the `user` class
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User with ID " + user.getUser_id() + " deleted successfully.");
            } else {
                System.out.println("User with ID " + user.getUser_id() + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }


    @Override
    public void update(user updatedUser) {
        String query = "UPDATE user SET user_fname=?, user_lname=?, user_email=?, user_pwd=?, user_nbr=?, user_role=?, "
                + "nb_teams=?, med_specialty=?, athlete_DoB=?, athlete_gender=?, athlete_address=?, athlete_height=?, athlete_weight=?, isInjured=?, athlete_regDate=? WHERE user_id=?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, updatedUser.getUser_fname());
            ps.setString(2, updatedUser.getUser_lname());
            ps.setString(3, updatedUser.getUser_email());
            ps.setString(4, updatedUser.getUser_pwd());
            ps.setString(5, updatedUser.getUser_nbr());
            ps.setString(6, updatedUser.getUser_role().toString());

            // Set specific fields based on the user type (Coach, Medical_staff, Athlete, Admin)
            if (updatedUser instanceof Coach coach) {
                ps.setInt(7, coach.getNb_teams());
                ps.setNull(8, java.sql.Types.VARCHAR);
                ps.setNull(9, java.sql.Types.DATE);
                ps.setNull(10, java.sql.Types.VARCHAR);
                ps.setNull(11, java.sql.Types.VARCHAR);
                ps.setNull(12, java.sql.Types.FLOAT);
                ps.setNull(13, java.sql.Types.FLOAT);
                ps.setNull(14, java.sql.Types.INTEGER);
                ps.setNull(15, java.sql.Types.DATE);
            } else if (updatedUser instanceof Medical_staff medical_staff) {
                ps.setString(8, medical_staff.getSpeciality());
                ps.setNull(7, java.sql.Types.INTEGER);
                ps.setNull(9, java.sql.Types.DATE);
                ps.setNull(10, java.sql.Types.VARCHAR);
                ps.setNull(11, java.sql.Types.VARCHAR);
                ps.setNull(12, java.sql.Types.FLOAT);
                ps.setNull(13, java.sql.Types.FLOAT);
                ps.setNull(14, java.sql.Types.INTEGER);
                ps.setNull(15, java.sql.Types.DATE);
            } else if (updatedUser instanceof Athlete athlete) {
                ps.setDate(9, athlete.getAthlete_DoB());
                ps.setString(10, athlete.getAthlete_gender());
                ps.setString(11, athlete.getAthlete_address());
                ps.setFloat(12, athlete.getAthlete_height());
                ps.setFloat(13, athlete.getAthlete_weight());
                ps.setInt(14, athlete.getIsInjured());
                ps.setDate(15, athlete.getAthlete_regDate());
                ps.setNull(7, java.sql.Types.INTEGER);
                ps.setNull(8, java.sql.Types.VARCHAR);
            } else if (updatedUser instanceof Admin admin) {
                ps.setNull(7, java.sql.Types.INTEGER);
                ps.setNull(8, java.sql.Types.VARCHAR);
                ps.setNull(9, java.sql.Types.DATE);
                ps.setNull(10, java.sql.Types.VARCHAR);
                ps.setNull(11, java.sql.Types.VARCHAR);
                ps.setNull(12, java.sql.Types.FLOAT);
                ps.setNull(13, java.sql.Types.FLOAT);
                ps.setNull(14, java.sql.Types.INTEGER);
                ps.setNull(15, java.sql.Types.DATE);
            }

            ps.setInt(16, updatedUser.getUser_id()); // WHERE condition: use user_id from updatedUser
            ps.executeUpdate();
            System.out.println("Account updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating account: " + e.getMessage());
        }
    }


    @Override
    public List<user> getAll() {
        List<user> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int user_id = rs.getInt("user_id");
                String user_fname = rs.getString("user_fname");
                String user_lname = rs.getString("user_lname");
                String user_email = rs.getString("user_email");
                String user_pwd = rs.getString("user_pwd");
                String user_nbr = rs.getString("user_nbr");
                String user_role = rs.getString("user_role");

                if ("coach".equals(user_role)) {
                    users.add(new Coach(user_id, user_fname, user_lname, user_email, user_pwd,user_nbr,
                            rs.getInt("nb_teams")));
                }
                else if ("medical_staff".equals(user_role)) {
                    users.add(new Medical_staff(user_id, user_fname, user_lname, user_email, user_pwd,user_nbr,
                            rs.getString("med_specialty")));
                }
                else if ("athlete".equals(user_role)) {
                    users.add(new Athlete(user_id, user_fname, user_lname, user_email, user_pwd,user_nbr,
                            rs.getDate("athlete_DoB"), rs.getString("athlete_gender"),
                            rs.getString("athlete_address"), rs.getFloat("athlete_height"),
                            rs.getFloat("athlete_weight"), rs.getInt("isInjured"), rs.getDate("athlete_regDate")));
                }
                else if ("admin".equals(user_role)) {
                    users.add(new Admin(user_id, user_fname, user_lname, user_email, user_pwd,user_nbr));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving all users: " + e.getMessage());
        }
        return users;
    }

    //Leena

    public int getUserIdByName(String userName) throws SQLException {
        String query = "SELECT id FROM users WHERE name = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("id");
        } else {
            throw new SQLException("User not found");
        }
    }

}


