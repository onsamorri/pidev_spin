package tn.esprit.services;

import tn.esprit.entities.ClaimAction;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ClaimActionServices implements Iservice<ClaimAction>{

    private Connection con;
    public ClaimActionServices(){
        con = MyDatabase.getInstance().getCon();
    }



    @Override
    public void add(ClaimAction claimaction) throws SQLException {
    String query = "INSERT INTO `claimaction`(`claimActionType`, `claimActionStartDate`, `claimActionEndDate`, `claimActionNotes`) VALUES ('"+ claimaction.getClaimActionType()+"','"+claimaction.getClaimActionStartDate()+"','"+claimaction.getClaimActionEndDate()+"','"+claimaction.getClaimActionNotes()+"')";
        Statement stm = con.createStatement();
        stm.executeUpdate(query);
        System.out.println("Claim Action added!");
    }

    @Override
    public void addP(ClaimAction claimAction) {

    }

    @Override
    public List<ClaimAction> returnList() {
        return null;
    }

    @Override
    public void delete(ClaimAction claim_action) {
        String query = "DELETE FROM claimAction WHERE claimActionId = ?";

        try (PreparedStatement kstmt = con.prepareStatement(query)) {
            kstmt.setInt(1, claim_action.getClaimActionId());
            int rowsDeleted = kstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Claim Action deleted successfully!");
            } else {
                System.out.println("No claim Action found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting claim action: " + e.getMessage());
        }
    }

    public void update(ClaimAction claim_action) throws SQLException {
        String query = "UPDATE claimaction SET claimActionType = ?, claimActionStartDate = ?, claimActionEndDate = ?, claimActionNotes = ? WHERE claimActionId = ?";

        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, claim_action.getClaimActionType().toString()); // Convert enum to String if needed
        pstmt.setDate(2, java.sql.Date.valueOf(claim_action.getClaimActionStartDate()));
        pstmt.setDate(3, java.sql.Date.valueOf(claim_action.getClaimActionEndDate()));
        pstmt.setString(4, claim_action.getClaimActionNotes());
        pstmt.setInt(5, claim_action.getClaimActionId()); // Use claim_action's ID

        int rowsUpdated = pstmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Claim Action updated successfully!");
        } else {
            System.out.println("No claim action found with the given ID.");
        }
    }
}
