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
import tn.esprit.entities.Claim;
import tn.esprit.services.ClaimServices;

import javafx.util.Callback;
import java.io.IOException;
import java.sql.SQLException;

public class ShowClaimController {

    @FXML
    private TableView<Claim> claimsTable;

    @FXML
    private TableColumn<Claim, Integer> claimIdColumn;

    @FXML
    private TableColumn<Claim, String> claimDescriptionColumn;

    @FXML
    private TableColumn<Claim, String> claimStatusColumn;

    @FXML
    private TableColumn<Claim, String> claimDateColumn;

    @FXML
    private TableColumn<Claim, String> claimCategoryColumn;

    @FXML
    private TableColumn<Claim, Void> actionsColumn;

    private ClaimServices claimServices;
    private ObservableList<Claim> claimList;

    @FXML
    public void initialize() {
        claimServices = new ClaimServices();
        claimList = FXCollections.observableArrayList();

        // Set up the table columns
        claimIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getClaimId()).asObject());
        claimDescriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClaimDescription()));
        claimStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClaimStatus().toString()));
        claimDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClaimDate().toString()));
        claimCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClaimCategory().toString()));

        // Load claims from the database
        loadClaims();

        // Initialize actions column
        actionsColumn.setCellFactory(new Callback<TableColumn<Claim, Void>, TableCell<Claim, Void>>() {
            @Override
            public TableCell<Claim, Void> call(TableColumn<Claim, Void> param) {
                return new TableCell<Claim, Void>() {
                    private final Button updateButton = new Button("Update");
                    private final Button deleteButton = new Button("Delete");
                    private final Button submitActionButton = new Button("Submit Action");

                    {
                        updateButton.setOnAction(event -> {
                            Claim selectedClaim = getTableView().getItems().get(getIndex());
                            updateClaim(selectedClaim);
                        });

                        deleteButton.setOnAction(event -> {
                            Claim selectedClaim = getTableView().getItems().get(getIndex());
                            deleteClaim(selectedClaim);
                        });

                        submitActionButton.setOnAction(event -> {
                            Claim selectedClaim = getTableView().getItems().get(getIndex());
                            submitClaimAction(selectedClaim);
                        });

                        HBox hBox = new HBox(updateButton, deleteButton, submitActionButton);
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

    private void loadClaims() {
        try {
            claimList.clear();
            claimList.addAll(claimServices.returnList());
            claimsTable.setItems(claimList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load claims.");
        }
    }

    private void updateClaim(Claim selectedClaim) {
        if (selectedClaim != null) {
            openUpdateClaimInterface(selectedClaim);
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a claim to update.");
        }
    }

    private void openUpdateClaimInterface(Claim selectedClaim) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateClaimInterface.fxml"));
            Parent updateClaimRoot = loader.load();

            UpdateClaimController updateClaimController = loader.getController();
            updateClaimController.setClaimData(selectedClaim);
            updateClaimController.setShowClaimController(this); // Pass the ShowClaimController instance

            Stage updateClaimStage = new Stage();
            updateClaimStage.setTitle("Update Claim");
            updateClaimStage.setScene(new Scene(updateClaimRoot));
            updateClaimStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Update Claim interface.");
        }
    }

    private void deleteClaim(Claim selectedClaim) {
        if (selectedClaim != null) {
            DeleteClaimController deleteClaimController = new DeleteClaimController();
            deleteClaimController.deleteClaim(selectedClaim);
            loadClaims(); // Refresh the list after deletion
            claimsTable.refresh(); // Force the table to refresh its cells
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a claim to delete.");
        }
    }

    private void submitClaimAction(Claim selectedClaim) {
        if (selectedClaim != null) {
            openAddClaimActionInterface(selectedClaim);
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a claim to submit an action.");
        }
    }

    private void openAddClaimActionInterface(Claim selectedClaim) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddClaimActionInterface.fxml"));
            Parent addClaimActionRoot = loader.load();

            AddClaimActionController addClaimActionController = loader.getController();
            addClaimActionController.setClaimData(selectedClaim);

            Stage addClaimActionStage = new Stage();
            addClaimActionStage.setTitle("Submit Claim Action");
            addClaimActionStage.setScene(new Scene(addClaimActionRoot));
            addClaimActionStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Submit Claim Action interface.");
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
        loadClaims(); // Reload the claims from the database
        claimsTable.refresh(); // Refresh the table view
    }
}