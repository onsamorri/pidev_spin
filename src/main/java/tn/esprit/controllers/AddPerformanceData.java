package tn.esprit.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.services.PDFExportServices;
import tn.esprit.services.PerformanceServices;
import tn.esprit.entities.Performance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AddPerformanceData {
    @FXML
    private TextField performance_speed;

    @FXML
    private TextField performance_agility;

    @FXML
    private TextField performance_nbr_goals;

    @FXML
    private TextField performance_assists;

    @FXML
    private DatePicker performance_date_recorded;

    @FXML
    private TextField performance_nbr_fouls;

    @FXML
    private Button submitButton;

    @FXML
    private TableView<Performance> performanceTable;


    @FXML
    private TableColumn<Performance, Float> colSpeed;

    @FXML
    private TableColumn<Performance, Float> colAgility;

    @FXML
    private TableColumn<Performance, Integer> colGoals;

    @FXML
    private TableColumn<Performance, Integer> colAssists;

    @FXML
    private TableColumn<Performance, Date> colDate;

    @FXML
    private TableColumn<Performance, Integer> colFouls;

    @FXML
    private TableColumn<Performance, Void> colActions;
    @FXML
    private ImageView backBtn;
    @FXML
    private Button importCsvButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchBtn;
    @FXML
    private Button exportPdfBtn;
    @FXML
    private ComboBox<String> sortComboBox;

    private final PerformanceServices performanceService = new PerformanceServices();
    private final ObservableList<Performance> performanceList = FXCollections.observableArrayList();
    private PDFExportServices pdfExportService = new PDFExportServices();




    @FXML
    public void initialize() {
        colSpeed.setCellValueFactory(new PropertyValueFactory<>("speed"));
        colAgility.setCellValueFactory(new PropertyValueFactory<>("agility"));
        colGoals.setCellValueFactory(new PropertyValueFactory<>("nbr_goals"));
        colAssists.setCellValueFactory(new PropertyValueFactory<>("assists"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date_recorded"));
        colFouls.setCellValueFactory(new PropertyValueFactory<>("nbr_fouls"));

        backBtn.setOnMouseClicked(event -> switchBackToCoachFront());
        performanceTable.setItems(performanceList);
        updatePerformanceList();
        addActionButtonsToTable();
        importCsvButton.setOnAction(event -> importCsv(event));
        searchBtn.setOnAction(event -> search(searchField.getText()));
        exportPdfBtn.setOnAction(event -> exportToPDF());
    }

    @FXML
    void addPerformanceData(ActionEvent event) {
        if (!validateInputs()) {
            showAlert("Error", "Please correct the highlighted fields.");
            return;
        }

        try {
            float speed = Float.parseFloat(performance_speed.getText());
            float agility = Float.parseFloat(performance_agility.getText());
            int goals = Integer.parseInt(performance_nbr_goals.getText());
            int assists = Integer.parseInt(performance_assists.getText());
            int fouls = Integer.parseInt(performance_nbr_fouls.getText());

            LocalDate localDate = performance_date_recorded.getValue();
            Date date = Date.valueOf(localDate);

            Performance performance = new Performance(speed, agility, goals, assists, date, fouls);

            performanceService.addP(performance);

            showAlert("Success", "Performance Data Added Successfully!");

            clearFields();

            updatePerformanceList();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for speed, agility, goals, assists, and fouls.");
        } catch (SQLException e) {
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    private void switchBackToCoachFront() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Coachfront.fxml"));
            Parent root = loader.load();

            Coachfront controller = loader.getController();
            controller.initialize();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Welcome Coach");
            stage.setUserData(this);
            stage.show();
            Stage currentStage = (Stage) backBtn.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open coach front screen: " + e.getMessage());
        }
    }
    private boolean validateInputs() {
        boolean valid = true;

        // Validate speed
        try {
            Float.parseFloat(performance_speed.getText());
            performance_speed.setBorder(Border.EMPTY);
        } catch (NumberFormatException e) {
            performance_speed.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            valid = false;
        }

        // Validate agility
        try {
            Float.parseFloat(performance_agility.getText());
            performance_agility.setBorder(Border.EMPTY);
        } catch (NumberFormatException e) {
            performance_agility.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            valid = false;
        }

        // Validate goals
        try {
            Integer.parseInt(performance_nbr_goals.getText());
            performance_nbr_goals.setBorder(Border.EMPTY);
        } catch (NumberFormatException e) {
            performance_nbr_goals.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            valid = false;
        }

        // Validate assists
        try {
            Integer.parseInt(performance_assists.getText());
            performance_assists.setBorder(Border.EMPTY);
        } catch (NumberFormatException e) {
            performance_assists.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            valid = false;
        }

        // Validate fouls
        try {
            Integer.parseInt(performance_nbr_fouls.getText());
            performance_nbr_fouls.setBorder(Border.EMPTY);
        } catch (NumberFormatException e) {
            performance_nbr_fouls.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            valid = false;
        }

        // Validate date
        if (performance_date_recorded.getValue() == null) {
            performance_date_recorded.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            valid = false;
        } else {
            performance_date_recorded.setBorder(Border.EMPTY);
        }

        return valid;
    }

    private void clearFields() {
        performance_speed.clear();
        performance_agility.clear();
        performance_nbr_goals.clear();
        performance_assists.clear();
        performance_nbr_fouls.clear();
        performance_date_recorded.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    void updatePerformanceList() {
        performanceList.clear();
        try {
            List<Performance> performances = performanceService.returnList();
            performanceList.addAll(performances);
            performanceTable.refresh();
        } catch (SQLException e) {
            showAlert("Error", "Failed to load performance data: " + e.getMessage());
        }
    }

    private void addActionButtonsToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");
            private final Button statsButton = new Button("View Stats");
            private final HBox pane = new HBox(updateButton, deleteButton, statsButton);

            {
                updateButton.setStyle("-fx-background-color: #BCCCE0; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #D68C45; -fx-text-fill: white;");
                statsButton.setStyle("-fx-background-color: #709775; -fx-text-fill: white;");
                pane.setSpacing(5);

                // Update Button Action
                updateButton.setOnAction(event -> {
                    Performance performance = getTableView().getItems().get(getIndex());
                    openUpdatePerformanceScreen(performance);
                });

                // Delete Button Action
                deleteButton.setOnAction(event -> {
                    Performance performance = getTableView().getItems().get(getIndex());
                    deletePerformance(performance);
                });

                // View Stats Button Action
                statsButton.setOnAction(event -> {
                    Performance performance = getTableView().getItems().get(getIndex());
                    openStatsWindow(performance);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    private void openUpdatePerformanceScreen(Performance performance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatePerformance.fxml"));
            Parent root = loader.load();

            UpdatePerformance controller = loader.getController();
            controller.initData(performance);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Performance");
            stage.setUserData(this);
            stage.show();
            updatePerformanceList();
        } catch (IOException e) {
            showAlert("Error", "Failed to open update screen: " + e.getMessage());
        }
    }

    private void deletePerformance(Performance performance) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this record?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    performanceService.delete(performance);
                    performanceList.remove(performance);
                    showAlert("Success", "Performance record deleted successfully!");
                } catch (SQLException e) {
                    showAlert("Error", "Failed to delete performance: " + e.getMessage());
                }
            }
        });
    }
    //importing a csv file
    @FXML
    private void importCsv(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                boolean isFirstLine = true;
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue; // Skip the header row
                    }
                    String[] values = line.split(",");
                    if (values.length == 6) {
                        float speed = Float.parseFloat(values[0]);
                        float agility = Float.parseFloat(values[1]);
                        int goals = Integer.parseInt(values[2]);
                        int assists = Integer.parseInt(values[3]);
                        Date date = Date.valueOf(values[4]);
                        int fouls = Integer.parseInt(values[5]);

                        Performance performance = new Performance(speed, agility, goals, assists, date, fouls);
                        performanceService.addP(performance);
                    }
                }
                showAlert("Success", "CSV data imported successfully!");
                updatePerformanceList();
            } catch (IOException | SQLException | NumberFormatException e) {
                showAlert("Error", "Failed to import CSV data: " + e.getMessage());
            }
        }
    }

    //search method
    @FXML
    private void search(String keyword) {
        ObservableList<Performance> filteredList = FXCollections.observableArrayList();
        try {
            float keywordFloat = Float.parseFloat(keyword);
            for (Performance performance : performanceList) {
                if (performance.getSpeed() == keywordFloat ||
                        performance.getAgility() == keywordFloat ||
                        Integer.toString(performance.getNbr_goals()).contains(keyword) ||
                        Integer.toString(performance.getAssists()).contains(keyword) ||
                        performance.getDate_recorded().toString().contains(keyword) ||
                        Integer.toString(performance.getNbr_fouls()).contains(keyword)) {
                    filteredList.add(performance);
                }
            }
        } catch (NumberFormatException e) {
            // If keyword is not a float, continue with string comparison for other fields
            for (Performance performance : performanceList) {
                if (Integer.toString(performance.getNbr_goals()).contains(keyword) ||
                        Integer.toString(performance.getAssists()).contains(keyword) ||
                        performance.getDate_recorded().toString().contains(keyword) ||
                        Integer.toString(performance.getNbr_fouls()).contains(keyword)) {
                    filteredList.add(performance);
                }
            }
        }
        performanceTable.setItems(filteredList);
    }
    //stats
    private void openStatsWindow(Performance performance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stats.fxml"));
            Parent root = loader.load();

            StatsController statsController = loader.getController();
            statsController.displayPerformanceStats(performance);

            Stage stage = new Stage();
            stage.setTitle("Performance Statistics");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to open stats window: " + e.getMessage());
        }
    }
    //PDF
    @FXML
    private void exportToPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            pdfExportService.exportPerformanceDataToPDF(performanceList, file.getAbsolutePath());
        }
    }
    //sorting for two criteria

    @FXML
    void sortTableView(ActionEvent event) {
        try {
            String selectedSortOption = sortComboBox.getValue();
            List<Performance> sortedPerformances = null;

            switch (selectedSortOption) {
                case "Highest Speed":
                    sortedPerformances = performanceList.stream()
                            .sorted(Comparator.comparingDouble(Performance::getSpeed).reversed())
                            .collect(Collectors.toList());
                    break;
                case "Least Fouls":
                    sortedPerformances = performanceList.stream()
                            .sorted(Comparator.comparingInt(Performance::getNbr_fouls))
                            .collect(Collectors.toList());
                    break;
                default:
                    break;
            }

            if (sortedPerformances != null) {
                performanceList.setAll(sortedPerformances);
                performanceTable.refresh();
            } else {
                showAlert("Error", "Sorted performances list is null.");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}
