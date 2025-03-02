package tn.esprit.entities;

public class results {
    private int tournamentId,teamId;


    public results() {
    }

    public results(int tournamentId, int teamId) {
        this.tournamentId = tournamentId;
        this.teamId = teamId;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "results{" +
                "tournamentId=" + tournamentId +
                ", teamId=" + teamId +
                '}';
    }
}
