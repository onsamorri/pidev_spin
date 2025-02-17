package tn.esprit.main;

import tn.esprit.entities.team;
import tn.esprit.entities.tournament;
import tn.esprit.services.TournamentService;
import tn.esprit.services.teamServices;
import tn.esprit.utils.MyDatabase;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MyDatabase db1 = MyDatabase.getInstance();
        teamServices ts = new teamServices();
        TournamentService tournamentService = new TournamentService();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       // MyDatabase db2 = MyDatabase.getInstance(); singleton

        System.out.println(db1);
       // System.out.println(db2);
       team team1 = new team("Hawks",20,"Basketball",0,0);

/*
        try {
            ts.add(team1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        team team2 = new team("Ballers",25,"Football",2,1);
        try {
            ts.addP(team2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try {
             ts.delete(3);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try {
            ts.update(4,team1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try {
            System.out.println(ts.returnList());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }*/
        try {
            // Create a new tournament
            tournament t1 = new tournament(
                    "Interclass competition 2025",
                    dateFormat.parse("2025-02-20"),
                    dateFormat.parse("2025-02-25"),
                    "Sports Arena",
                    "Standard Rules Apply",
                    15
            );

            // Add tournament
            tournamentService.add(t1);
            System.out.println("Tournament added successfully!");

            // Display all tournaments
            System.out.println("All Tournaments:");
            System.out.println(tournamentService.returnList());

            // Update tournament
            tournament updatedTournament = new tournament(
                    "Summer Championship 2024",
                    dateFormat.parse("2024-06-01"),
                    dateFormat.parse("2024-06-20"),
                    "Main Sports Arena",
                    "Updated Rules Apply",
                    32
            );
            tournamentService.update(4, updatedTournament);

            tournamentService.delete(6);

        } catch (SQLException | ParseException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

}
