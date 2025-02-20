package tn.esprit.entities;
import tn.esprit.entities.User;


public class Admin extends User {

    public Admin() {
        super();
    }
    public Admin(int user_id, String user_fname, String user_lname, String user_email, String user_pwd, String user_nbr) {
        super(user_id, user_fname, user_lname, user_email, user_pwd, user_nbr, user_role.ADMIN);

    }
    public Admin(String user_fname, String user_lname, String user_email, String user_pwd, String user_nbr) {
        super(user_fname, user_lname, user_email, user_pwd, user_nbr, user_role.ADMIN);

    }

    @Override
    public String toString() {
        return super.toString() + "Admin{}";
    }
}