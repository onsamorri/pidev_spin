package tn.esprit.main;

import tn.esprit.entities.*;
import tn.esprit.services.ClaimActionServices;
import tn.esprit.services.ClaimServices;
import tn.esprit.utils.MyDatabase;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args){
        MyDatabase db1 = MyDatabase.getInstance();

        Claim claim = new Claim(
                "Fraude",
                ClaimStatus.IN_REVIEW,
                LocalDate.parse("2025-02-04"),
                ClaimCategory.MISCONDUCT
        );

        ClaimServices cs = new ClaimServices();
        try {
            cs.add(claim);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        Claim claim2 = new Claim(
                "3onf",
                ClaimStatus.IN_REVIEW,
                LocalDate.parse("2025-02-17"),
                ClaimCategory.POLICY_VIOLATION
        );

        ClaimServices css = new ClaimServices();
        try {
            css.addP(claim2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        Claim updatedClaim = new Claim(
                "Alcohol Drinking",
                ClaimStatus.APPROVED,
                LocalDate.parse("2025-02-04"),
                ClaimCategory.MISCONDUCT
        );

        ClaimServices cs2 = new ClaimServices();
        try {
            updatedClaim.setClaimId(2);
            cs2.update(updatedClaim);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        Claim claimToDelete = new Claim();
        claimToDelete.setClaimId(13);

        ClaimServices cs3 = new ClaimServices();
        try {
            cs3.delete(claimToDelete);
        } catch (Exception e) {
            System.out.println(e.getMessage()); // Handle exceptions
        }

        ClaimAction claim_action = new ClaimAction(
                ClaimActionType.SUSPENSION,
                LocalDate.parse("2025-02-04"),
                LocalDate.parse("2025-02-20"),
                "Non justifiable"
        );

        ClaimActionServices cas = new ClaimActionServices();
        try {
            cas.add(claim_action);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        ClaimAction updatedClaimAction = new ClaimAction(
                ClaimActionType.FINE,
                LocalDate.parse("2025-02-04"),
                LocalDate.parse("2025-02-20"),
                "Issue has been resolved"
        );

        ClaimActionServices cas2 = new ClaimActionServices();
        try {
            updatedClaimAction.setClaimActionId(1);
            cas2.update(updatedClaimAction);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        ClaimAction claimActionToDelete = new ClaimAction();
        claimActionToDelete.setClaimActionId(6);

        ClaimActionServices cas3 = new ClaimActionServices();
        try {
            cas3.delete(claimActionToDelete);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
