package tn.esprit.main;

import tn.esprit.entities.Duration;
import tn.esprit.entities.Focus;
import tn.esprit.entities.Performance;
import tn.esprit.entities.TrainingSession;
import tn.esprit.service.PerformanceServices;
import tn.esprit.service.TrainingSessionServices;
import tn.esprit.utils.MyDatabase;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        MyDatabase db = MyDatabase.getInstance();
        //PerformanceServices perServ = new PerformanceServices();
        Scanner scanner = new Scanner(System.in);

        // ***********************    Adding Using the Prepared Statement      ***********************
        //Performance performance = new Performance(12.5f, 7.7f, 2, 13, Date.valueOf("2025-02-13"), 0);
//        PerformanceServices perServ = new PerformanceServices();
//        try{
//            perServ.addP(performance);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
        // ***********************    End         **********************


        // ***********************    Displaying the List of Performances         ***********************
//              PerformanceServices perServ = new PerformanceServices();
//            try{
//                System.out.println(perServ.returnList());
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
        // ***********************    End         **********************


        // ***********************    Deleting a performance with a given ID         **********************

//        try {
//            Performance perToDel = new Performance();
//            perToDel.setPerformance_id(1);
//            perServ.delete(perToDel);
//
//
//        }catch(SQLException e){
//            e.printStackTrace();
//
//        }
        // ***********************    End         **********************


        // ***********************    Updating a performance with a given ID         **********************
//        try{
//            System.out.println("Enter The id of the performance to update :");
//            int idToUpdate = scanner.nextInt();
//            System.out.print("Enter new speed: ");
//            float speed = scanner.nextFloat();
//
//            System.out.print("Enter new agility: ");
//            float agility = scanner.nextFloat();
//
//            System.out.print("Enter new number of goals: ");
//            int goals = scanner.nextInt();
//
//            System.out.print("Enter new number of assists: ");
//            int assists = scanner.nextInt();
//
//            System.out.print("Enter new record date (YYYY-MM-DD): ");
//            String dateInput = scanner.next();
//            Date dateRecorded = Date.valueOf(dateInput);
//
//            System.out.print("Enter new number of fouls: ");
//            int fouls = scanner.nextInt();
//            Performance updatedPerformance = new Performance(idToUpdate, speed, agility, goals, assists, dateRecorded, fouls);
//            perServ.update(updatedPerformance);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            scanner.close();
//        }
        // ***********************    End         **********************

// -------------------------- Training Session Testing ---------------------------------------------------------------------
        TrainingSessionServices service = new TrainingSessionServices();
        TrainingSession sesh = new TrainingSession();

        // ***********************    Adding     ***********************

//        TrainingSession newSession = new TrainingSession(
//                Focus.STRENGTH,
//                LocalTime.of(12, 30),
//                Duration.NINETY,
//                "Monastir",
//                "Agility Training session"
//
//        );
//        service.add(newSession);
//        service.addP(newSession);
//
//
        // ***********************    End         **********************

        // ***********************    Displaying the List of Training Sessions         ***********************

//        try {
//            List<TrainingSession> sessions = service.returnList();
//            for (TrainingSession session : sessions) {
//                System.out.println(session);
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
        // ***********************    End         **********************

        // ***********************    Deleting Training session       ***********************

//        try {
//            System.out.print("Enter the ID of the training session to delete: ");
//            int sessionId = scanner.nextInt();
//            scanner.nextLine();
//
//            TrainingSession trainSessionTodel = new TrainingSession();
//            trainSessionTodel.setTrainingSession_id(sessionId);
//            service.delete(trainSessionTodel);
//
//
//        }catch(SQLException e){
//            e.printStackTrace();
//
//        }
        // ***********************    End         **********************


        // ***********************    Updating a performance with a given ID         **********************
        try {
            System.out.print("Enter the ID of the training session to update: ");
            int sessionId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter new focus (AGILITY, STRENGTH, DRIBBLING, etc.): ");
            String focusInput = scanner.nextLine().toUpperCase();
            System.out.print("Enter new start time (HH:MM format): ");
            String timeInput = scanner.nextLine();
            LocalTime startTime = LocalTime.parse(timeInput);
            System.out.print("Enter new duration (45, 60, 90, 120): ");
            String durationInput = scanner.nextLine();
            System.out.print("Enter new location: ");
            String location = scanner.nextLine();
            System.out.print("Enter new session notes: ");
            String sessionNotes = scanner.nextLine();

            TrainingSession updatedSession = new TrainingSession(
                    sessionId,
                    Focus.fromString(focusInput.toUpperCase()),
                    startTime,
                    Duration.fromString(durationInput.toUpperCase()),
                    location,
                    sessionNotes
            );

            service.update(updatedSession);
            System.out.println("Training session updated successfully :>!");
        } catch (Exception e) {
            System.out.println("Error updating training session: :<" + e.getMessage());
        }finally {
            scanner.close();

        }


    }


    }


