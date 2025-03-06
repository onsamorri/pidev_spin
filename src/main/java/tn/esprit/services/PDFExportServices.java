package tn.esprit.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import tn.esprit.entities.Performance;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class PDFExportServices {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, new BaseColor(112, 151, 117)); // Green color used in FXML
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font CELL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    private static final Font DATE_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);

    private static final BaseColor HEADER_BACKGROUND_COLOR = new BaseColor(214, 140, 69); // D68C45
    private static final BaseColor CELL_BACKGROUND_COLOR = new BaseColor(255, 255, 255); // White

    public void exportPerformanceDataToPDF(List<Performance> performanceList, String filePath) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add title
            Paragraph title = new Paragraph("Performance Data", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Add table
            PdfPTable table = new PdfPTable(5); // 5 columns
            table.setWidthPercentage(100);
            addTableHeader(table);
            addRows(table, performanceList);

            document.add(table);

            // Add system date
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            Paragraph date = new Paragraph("Printed on: " + currentDate, DATE_FONT);
            date.setAlignment(Element.ALIGN_RIGHT);
            date.setSpacingBefore(10);
            document.add(date);

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Speed", "Agility", "Goals", "Assists", "Fouls")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(HEADER_BACKGROUND_COLOR);
                    header.setPhrase(new Phrase(columnTitle, HEADER_FONT));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, List<Performance> performanceList) {
        for (Performance performance : performanceList) {
            addCell(table, String.valueOf(performance.getSpeed()));
            addCell(table, String.valueOf(performance.getAgility()));
            addCell(table, String.valueOf(performance.getNbr_goals()));
            addCell(table, String.valueOf(performance.getAssists()));
            addCell(table, String.valueOf(performance.getNbr_fouls()));
        }
    }

    private void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, CELL_FONT));
        cell.setBackgroundColor(CELL_BACKGROUND_COLOR);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }
}