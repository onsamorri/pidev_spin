package tn.esprit.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tn.esprit.entities.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecoveryPlanReportGenerator {
    private static final String FILE_NAME = "recovery_plan_report.xlsx";

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/pidev";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public void exportRecoveryPlansToExcel() {
        List<RecoveryPlan> recoveryPlans = getAllRecoveryPlans(); // Fetch recovery plans from database

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Recovery Plan Data");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Recovery ID", "User First Name", "User Last Name", "Goal", "Description", "Start Date", "End Date", "Status"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(getHeaderStyle(workbook));
            }

            // Fill rows with recovery plan data
            int rowNum = 1;
            for (RecoveryPlan recoveryPlan : recoveryPlans) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(recoveryPlan.getRecovery_id());
                row.createCell(1).setCellValue(recoveryPlan.getUser() != null ? recoveryPlan.getUser().getUser_fname() : "N/A");
                row.createCell(2).setCellValue(recoveryPlan.getUser() != null ? recoveryPlan.getUser().getUser_lname() : "N/A");
                row.createCell(3).setCellValue(recoveryPlan.getRecovery_Goal().toString());
                row.createCell(4).setCellValue(recoveryPlan.getRecovery_Description());
                row.createCell(5).setCellValue(recoveryPlan.getRecovery_StartDate().toString());
                row.createCell(6).setCellValue(recoveryPlan.getRecovery_EndDate().toString());
                row.createCell(7).setCellValue(recoveryPlan.getRecovery_Status().toString());
            }

            // Autosize columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Save to file
            try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME)) {
                workbook.write(fileOut);
            }
            System.out.println("Excel file created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to apply header style
    private CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    // Fetching recovery plans from the database
    public List<RecoveryPlan> getAllRecoveryPlans() {
        List<RecoveryPlan> recoveryPlans = new ArrayList<>();
        String query = "SELECT * FROM recoveryplan";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                RecoveryPlan recoveryPlan = new RecoveryPlan();
                recoveryPlan.setRecovery_id(rs.getInt("recovery_id"));
                recoveryPlan.setRecovery_Goal(RecoveryGoal.valueOf(rs.getString("recovery_Goal")));
                recoveryPlan.setRecovery_Description(rs.getString("recovery_Description"));
                recoveryPlan.setRecovery_StartDate(rs.getDate("recovery_StartDate").toLocalDate());
                recoveryPlan.setRecovery_EndDate(rs.getDate("recovery_EndDate").toLocalDate());
                recoveryPlan.setRecovery_Status(RecoveryStatus.valueOf(rs.getString("recovery_Status")));

                // Fetch and set the user details
                recoveryPlan.setUser(getUserById(rs.getInt("user_id")));

                recoveryPlans.add(recoveryPlan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recoveryPlans;
    }

    private user getUserById(int user_id) {
        String query = "SELECT user_id, user_fname, user_lname FROM user WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new user(
                        rs.getInt("user_id"),
                        rs.getString("user_fname"),
                        rs.getString("user_lname")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if user is not found
    }
}
