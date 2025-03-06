package tn.esprit.services;

import tn.esprit.entities.results;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class resultsServices implements IService2<results> {

    private Connection connection;

    public resultsServices() {
        connection = MyDatabase.getInstance().getCon();
    }

    @Override
    public void add(results result) throws SQLException {
        String query = "INSERT INTO results (tournamentId, teamId) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, result.getTournamentId());
        statement.setInt(2, result.getTeamId());
        statement.executeUpdate();
    }

    @Override
    public void addP(results result) throws SQLException {
        add(result); // Assuming addP has the same logic as add
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM results WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    @Override
    public void update(int id, results result) throws SQLException {
        String query = "UPDATE results SET tournamentId = ?, teamId = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, result.getTournamentId());
        statement.setInt(2, result.getTeamId());
        statement.setInt(3, id);
        statement.executeUpdate();
    }

    @Override
    public List<results> returnList() throws SQLException {
        List<results> resultsList = new ArrayList<>();
        String query = "SELECT * FROM results";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
                 results r = new results(
                    resultSet.getInt("tournamentId"),
                    resultSet.getInt("teamId")
            );
            resultsList.add(r);
        }
        return resultsList;
    }
    public void delete(int tournamentId, int teamId) throws SQLException {
        String query = "DELETE FROM results WHERE tournamentId = ? AND teamId = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, tournamentId);
        ps.setInt(2, teamId);
        ps.executeUpdate();
    }
    public void deleteByTournamentId(int tournamentId) throws SQLException {
        String query = "DELETE FROM results WHERE tournamentId = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, tournamentId);
        ps.executeUpdate();
    }
}
