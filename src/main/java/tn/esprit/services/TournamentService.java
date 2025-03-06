package tn.esprit.services;

import tn.esprit.entities.tournament;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TournamentService implements IService2<tournament> {
    private Connection connection;

    public TournamentService() {
        connection = MyDatabase.getInstance().getCon();
    }

    @Override
    public void add(tournament tournament) throws SQLException {
        String sql = "INSERT INTO tournament (tournamentName, tournamentStartDate, tournamentEndDate, tournamentLocation, tournamentTOS, tournamentNbteams, tournamentWinner) " +
                "VALUES (?, ?, ?, ?, ?, ?, NULL)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, tournament.getTournamentName());
        preparedStatement.setDate(2, new java.sql.Date(tournament.getTournamentStartDate().getTime()));
        preparedStatement.setDate(3, new java.sql.Date(tournament.getTournamentEndDate().getTime()));
        preparedStatement.setString(4, tournament.getTournamentLocation());
        preparedStatement.setString(5, tournament.getTournamentTOS());
        preparedStatement.setInt(6, tournament.getTournamentNbteams());
        preparedStatement.executeUpdate();
    }

    @Override
    public void addP(tournament tournament) throws SQLException {
        String sql = "INSERT INTO tournament (tournamentName, tournamentStartDate, tournamentEndDate, tournamentLocation, tournamentTOS, tournamentNbteams, tournamentWinner) " +
                "VALUES (?, ?, ?, ?, ?, ?, NULL)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, tournament.getTournamentName());
        preparedStatement.setDate(2, new java.sql.Date(tournament.getTournamentStartDate().getTime()));
        preparedStatement.setDate(3, new java.sql.Date(tournament.getTournamentEndDate().getTime()));
        preparedStatement.setString(4, tournament.getTournamentLocation());
        preparedStatement.setString(5, tournament.getTournamentTOS());
        preparedStatement.setInt(6, tournament.getTournamentNbteams());
        preparedStatement.executeUpdate();
        System.out.println("Tournament added successfully");
    }


    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM tournament WHERE tournamentId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        int test = preparedStatement.executeUpdate();
        if (test == 0)
            System.out.println("tournament not found");
        else
            System.out.println("tournament deleted");
    }

    @Override
    public void update(int id, tournament tournament) throws SQLException {
        String sql = "UPDATE tournament SET tournamentName = ?, tournamentStartDate = ?, tournamentEndDate = ?, " +
                "tournamentLocation = ?, tournamentTOS = ?, tournamentNbteams = ?, tournamentWinner = ? WHERE tournamentId = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, tournament.getTournamentName());
        ps.setDate(2, new java.sql.Date(tournament.getTournamentStartDate().getTime()));
        ps.setDate(3, new java.sql.Date(tournament.getTournamentEndDate().getTime()));
        ps.setString(4, tournament.getTournamentLocation());
        ps.setString(5, tournament.getTournamentTOS());
        ps.setInt(6, tournament.getTournamentNbteams());
        ps.setInt(7, tournament.getTournamentWinner()); // Ensure winner is updated
        ps.setInt(8, id);

        int test = ps.executeUpdate();
        if (test == 0) {
            System.out.println("Tournament not found");
        } else {
            System.out.println("Tournament updated successfully");
        }
    }


    @Override
    public List<tournament> returnList() throws SQLException {
        String query = "SELECT * FROM tournament";
        Statement stM = connection.createStatement();
        List<tournament> tournaments = new ArrayList<>();
        ResultSet rs = stM.executeQuery(query);

        while (rs.next()) {
            tournament t = new tournament(
                    rs.getInt("tournamentId"),
                    rs.getString("tournamentName"),
                    rs.getDate("tournamentStartDate"),
                    rs.getDate("tournamentEndDate"),
                    rs.getString("tournamentLocation"),
                    rs.getString("tournamentTOS"),
                    rs.getInt("tournamentNbteams"),
                    rs.getInt("tournamentWinner") // Ensure winner is retrieved
            );
            tournaments.add(t);
        }
        return tournaments;
    }


    public int getLastAddedTournamentId() throws SQLException {
        String query = "SELECT MAX(tournamentId) FROM tournament";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1; // Return -1 if no tournament is found
    }
    // Method to begin a transaction
    public void beginTransaction() throws SQLException {
        if (connection != null) {
            connection.setAutoCommit(false); // Disable auto-commit to start a transaction
        }
    }

    // Method to commit a transaction
    public void commitTransaction() throws SQLException {
        if (connection != null) {
            connection.commit(); // Commit the transaction
            connection.setAutoCommit(true); // Re-enable auto-commit
        }
    }

    // Method to roll back a transaction
    public void rollbackTransaction() {
        if (connection != null) {
            try {
                connection.rollback(); // Roll back the transaction
                connection.setAutoCommit(true); // Re-enable auto-commit
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }
}

