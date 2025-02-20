package tn.esprit.Controllers;

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
import tn.esprit.services.InjuryServices;
import javafx.util.Callback;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ShowInjuryController {

    @FXML
    private TableView<Injury> InjuryTable;

    @FXML
    private TableColumn<Injury, String> AthleteNameColumn;

    @FXML
    private TableColumn<Injury, String> AthleteLastNameColumn;

    @FXML
    private TableColumn<Injury, String> InjuryTypeColumn;

    @FXML
    private TableColumn<Injury, String> InjuryDescriptionColumn;

    @FXML
    private TableColumn<Injury, String> InjuryDateColumn;

    @FXML
    private TableColumn<Injury, String> InjurySeverityColumn;

    @FXML
    private TableColumn<Injury, Void> actionsColumn;

    private final InjuryServices injuryServices = new InjuryServices();
    private ObservableList<Injury> injuryList;

    @FXML
    public void initialize() {
        injuryList = FXCollections.observableArrayList();
        loadInjuries();

        // Bind columns to Injury attributes
        AthleteNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser() != null ? cellData.getValue().getUser().getUser_fname() : "Unknown"));

        AthleteLastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser() != null ? cellData.getValue().getUser().getUser_lname() : "Unknown"));

        InjuryTypeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getInjuryType().toString()));

        InjuryDescriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getInjury_description()));

        InjuryDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getInjuryDate().toString()));

        InjurySeverityColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getInjury_severity().toString()));

        // Initialize actions column
        actionsColumn.setCellFactory(createButtonCellFactory());

        InjuryTable.setItems(injuryList);
    }

    private void loadInjuries() {
        try {
            List<Injury> injuries = injuryServices.getAll();
            injuryList.setAll(injuries);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load injuries.");
        }
    }

    private Callback<TableColumn<Injury, Void>, TableCell<Injury, Void>> createButtonCellFactory() {
        return param -> new TableCell<>() {
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

    private void updateInjury(Injury selectedInjury) {
        if (selectedInjury != null) {
            openUpdateInjuryInterface(selectedInjury);
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select an injury to update.");
        }
    }

    private void openUpdateInjuryInterface(Injury selectedInjury) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateInjury.fxml"));
            Parent updateInjuryRoot = loader.load();

            UpdateInjuryController updateInjuryController = loader.getController();
            updateInjuryController.setInjuryData(selectedInjury);
            updateInjuryController.setShowInjuryController(this);

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
            new DeleteInjuryController().deleteInjury(selectedInjury);
            loadInjuries();
            InjuryTable.refresh();
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select an injury to delete.");
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
        loadInjuries();
        InjuryTable.refresh();
    }
}
