package tn.esprit.services;

import tn.esprit.entities.ClaimAction;
import tn.esprit.entities.Claim;
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
        String query = "INSERT INTO `claimaction`(`claimId`, `claimActionType`, `claimActionStartDate`, `claimActionEndDate`, `claimActionNotes`) " +
                "VALUES ('" + claimaction.getClaim().getClaimId() + "', '" +
                claimaction.getClaimActionType() + "', '" +
                claimaction.getClaimActionStartDate() + "', '" +
                claimaction.getClaimActionEndDate() + "', '" +
                claimaction.getClaimActionNotes() + "')";

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
        String query = "UPDATE claimaction SET claimId = ?, claimActionType = ?, claimActionStartDate = ?, claimActionEndDate = ?, claimActionNotes = ? WHERE claimActionId = ?";

        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, claim_action.getClaim().getClaimId());
        pstmt.setString(2, claim_action.getClaimActionType().toString());
        pstmt.setDate(3, java.sql.Date.valueOf(claim_action.getClaimActionStartDate()));
        pstmt.setDate(4, java.sql.Date.valueOf(claim_action.getClaimActionEndDate()));
        pstmt.setString(5, claim_action.getClaimActionNotes());
        pstmt.setInt(6, claim_action.getClaimActionId());

        int rowsUpdated = pstmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Claim Action updated successfully!");
        } else {
            System.out.println("No claim action found with the given ID.");
        }
    }
}
