package tn.esprit.services;

import tn.esprit.entities.Claim;
import tn.esprit.entities.ClaimAction;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ClaimServices implements Iservice<Claim> {

    private Connection con;

    public ClaimServices(){
        con = MyDatabase.getInstance().getCon();
    }

    @Override
    public void add(Claim claim) throws SQLException {
        String query = "INSERT INTO `claim`(`claimDescription`, `claimStatus`, `claimDate`, `claimCategory`) VALUES ('"+claim.getClaimDescription()+"','"+claim.getClaimStatus()+"','"+claim.getClaimDate()+"','"+claim.getClaimCategory()+"')";
        Statement stm = con.createStatement();
        stm.executeUpdate(query);
        System.out.println("Claim added!");
    }

    @Override
    public void addP(Claim claim) throws SQLException {
        String query = "INSERT INTO `claim`(`claimDescription`, `claimStatus`, `claimDate`, `claimCategory`) VALUES (?,?,?,?)";
        PreparedStatement ps =con.prepareStatement(query);
        ps.setString(1, claim.getClaimDescription());
        ps.setString(2, claim.getClaimStatus().toString());
        ps.setDate(3, java.sql.Date.valueOf(claim.getClaimDate()));
        ps.setString(4, claim.getClaimCategory().toString());
        ps.executeUpdate();
        System.out.println("Claim added with second method!");
    }

    @Override
    public List<Claim> returnList() {
        return null;
    }

    @Override
    public void delete(Claim claim) {
        String query = "DELETE FROM claim WHERE claimId = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, claim.getClaimId());
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Claim deleted successfully!");
            } else {
                System.out.println("No claim found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting claim: " + e.getMessage());
        }
    }

    @Override
    public void update(Claim claim) throws SQLException {
        String query = "UPDATE claim SET claimDescription = ?, claimStatus = ?, claimDate = ?, claimCategory = ? WHERE claimId = ?";

        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, claim.getClaimDescription());
        stmt.setString(2, claim.getClaimStatus().toString());
        stmt.setDate(3, java.sql.Date.valueOf(claim.getClaimDate()));
        stmt.setString(4, claim.getClaimCategory().toString());
        stmt.setInt(5, claim.getClaimId());

        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Claim updated successfully!");
        } else {
            System.out.println("No claim found with the given ID.");
        }
    }
}
