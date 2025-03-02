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

public class InjuryReportGenerator {
    private static final String FILE_NAME = "injury_report.xlsx";

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/pidev";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public void exportInjuriesToExcel() {
        List<Injury> injuries = getAllInjuries(); // Fetch injuries from database

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Injury Data");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Injury ID", "User First Name", "User Last Name", "Injury Type", "Injury Date", "Severity", "Description"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(getHeaderStyle(workbook));
            }

            // Fill rows with injury data
            int rowNum = 1;
            for (Injury injury : injuries) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(injury.getInjury_id());
                row.createCell(1).setCellValue(injury.getUser() != null ? injury.getUser().getUser_fname() : "N/A");
                row.createCell(2).setCellValue(injury.getUser() != null ? injury.getUser().getUser_lname() : "N/A");
                row.createCell(3).setCellValue(injury.getInjuryType().toString());
                row.createCell(4).setCellValue(injury.getInjuryDate().toString());
                row.createCell(5).setCellValue(injury.getInjury_severity().toString());
                row.createCell(6).setCellValue(injury.getInjury_description());
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

    // Fetching data from the database
    public List<Injury> getAllInjuries() {
        List<Injury> injuries = new ArrayList<>();
        String query = "SELECT * FROM injury";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Injury injury = new Injury();
                injury.setInjury_id(rs.getInt("injury_id"));
                injury.setInjuryType(InjuryType.valueOf(rs.getString("injuryType")));
                injury.setInjuryDate(LocalDate.parse(rs.getString("injuryDate")));
                injury.setInjury_severity(Severity.valueOf(rs.getString("injury_severity")));
                injury.setInjury_description(rs.getString("injury_description"));
                injury.setUser(getUserById(rs.getInt("user_id")));
                injuries.add(injury);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return injuries;
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
