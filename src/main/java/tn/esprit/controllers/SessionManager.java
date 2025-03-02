package tn.esprit.controllers;

import tn.esprit.entities.user;

public class SessionManager {
    private static SessionManager instance;
    private user authenticatedUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setAuthenticatedUser(user user) {
        this.authenticatedUser = user;
    }

    public user getAuthenticatedUser() {
        return this.authenticatedUser;
    }

    // Method to clear the session (Logout)
    public void logout() {
        this.authenticatedUser = null;  // Clear the session
    }
}
