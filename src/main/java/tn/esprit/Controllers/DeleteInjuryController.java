package tn.esprit.Controllers;

import tn.esprit.entities.Injury;
import tn.esprit.services.InjuryServices;

import java.sql.SQLException;

public class DeleteInjuryController {

    private InjuryServices injuryServices;

    public DeleteInjuryController() {
        injuryServices = new InjuryServices();
    }

    public void deleteInjury(Injury injury) {
        try {
            injuryServices.delete(injury);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}