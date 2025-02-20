package tn.esprit.Controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.entities.Injury;
import tn.esprit.entities.user;
import tn.esprit.services.InjuryServices;

import javafx.util.Callback;
import java.io.IOException;
import java.sql.SQLException;

public class ShowInjuryController {

    @FXML
    private TableView<Injury> InjuryTable;

    @FXML
    private TableColumn<Injury, Integer> InjuryIdColumn;

    @FXML
    private TableColumn<Injury, String> AthleteNameColumn;

    @FXML
    private TableColumn<Injury, String> InjuryTypeColumn;

    @FXML
    private TableColumn<Injury, String> InjuryDateColumn;

    @FXML
    private TableColumn<Injury, String> SeverityColumn;

    @FXML
    private TableColumn<Injury, Void> actionsColumn;

    private InjuryServices InjuryServices;
    private ObservableList<Injury> InjuryList;

    @FXML
    public void initialize() {
        InjuryServices = new InjuryServices();
        InjuryList = FXCollections.observableArrayList();

        // Set up the table columns
        InjuryIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInjury_id()).asObject());
        AthleteNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser_fname()));
        InjuryTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInjuryType().toString()));
        InjuryDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInjuryDate().toString()));
        SeverityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInjury_severity().toString()));

        // Load Injuries from the database
        loadInjuries();

        // Initialize actions column
        actionsColumn.setCellFactory(new Callback<TableColumn<Injury, Void>, TableCell<Injury, Void>>() {
            @Override
            public TableCell<Injury, Void> call(TableColumn<Injury, Void> param) {
                return new TableCell<Injury, Void>() {
                    private final Button updateButton = new Button("Update");
                    private final Button deleteButton = new Button("Delete");

                    {
                        updateButton.setOnAction(event -> {
                            Injury selectedInjury = getTableView().getItems().get(getIndex());
                            updateInjury(selectedInjury);
                        });

                        deleteButton.setOnAction(event -> {
                            Injury selectedInjury = getTableView().getItems().get(getIndex());
                            deleteInjury(selectedInjury);
                        });

                        HBox hBox = new HBox(updateButton, deleteButton);
                        hBox.setSpacing(10);
                        setGraphic(hBox);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(getGraphic());
                        }
                    }
                };
            }
        });
    }

    private void loadInjuries() {
        try {
            InjuryList.clear();
            InjuryList.addAll(InjuryServices.getAll());
            InjuryTable.setItems(InjuryList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load Injuries.");
        }
    }


    private void updateInjury(Injury selectedInjury) {
        if (selectedInjury != null) {
            openUpdateInjuryInterface(selectedInjury);
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a Injury to update.");
        }
    }

    private void openUpdateInjuryInterface(Injury selectedInjury) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateInjury.fxml"));
            Parent updateInjuryRoot = loader.load();

            UpdateInjuryController updateInjuryController = loader.getController();
            updateInjuryController.setInjuryData(selectedInjury);
            updateInjuryController.setShowInjuryController(this); // Pass the ShowInjuryController instance

            Stage updateInjuryStage = new Stage();
            updateInjuryStage.setTitle("Update Injury");
            updateInjuryStage.setScene(new Scene(updateInjuryRoot));
            updateInjuryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Update Injury interface.");
        }
    }

    private void deleteInjury(Injury selectedInjury) {
        if (selectedInjury != null) {
            DeleteInjuryController deleteInjuryController = new DeleteInjuryController();
            deleteInjuryController.deleteInjury(selectedInjury);
            loadInjuries(); // Refresh the list after deletion
            InjuryTable.refresh(); // Force the table to refresh its cells
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a Injury to delete.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void refreshTable() {
        loadInjuries(); // Reload the Injuries from the database
        InjuryTable.refresh(); // Refresh the table view
    }
}