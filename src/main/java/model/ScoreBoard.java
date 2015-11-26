package model;

import scoreboardcontrollers.ManualScoreBoardController;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jakestaahl on 11/23/15.
 */
public class ScoreBoard {

    private String team1Name;
    private String team2Name;
    private int team1BigScore;
    private int team2BigScore;
    private int team1SmallScore;
    private int team2SmallScore;

    private Set<ScoreBoardUpdateListener> listeners;

    public ScoreBoard(String team1Name, String team2Name,
                      int team1BigScore, int team2BigScore,
                      int team1SmallScore, int team2SmallScore) {
        this.team1Name = team1Name;
        this.team2Name = team2Name;
        this.team1BigScore = team1BigScore;
        this.team2BigScore = team2BigScore;
        this.team1SmallScore = team1SmallScore;
        this.team2SmallScore = team2SmallScore;

        listeners = new HashSet<ScoreBoardUpdateListener>();
    }

    public ScoreBoard() {
        this("team1", "team2", 0, 0, 0, 0);
    }

    public void addUpdateListener(ScoreBoardUpdateListener listener) {
        listener.notifyUpdateTeamNames(null);
        listener.notifyUpdateScore(null);
        listeners.add(listener);
    }

    public boolean removeUpdateListener(ScoreBoardUpdateListener listener) {
        return listeners.remove(listener);
    }

    public synchronized void setTeam1Name(ScoreBoardModifier modifier, String team1Name) {
        if (modifier.isActivated()) {
            this.team1Name = team1Name;
            notifyUpdateTeamNames(modifier);
        }
    }

    public synchronized void setTeam2Name(ScoreBoardModifier modifier, String team2Name) {
        if (modifier.isActivated()) {
            this.team2Name = team2Name;
            notifyUpdateTeamNames(modifier);
        }
    }

    public synchronized void setTeamNames(ScoreBoardModifier modifier, String team1Name, String team2Name) {
        if (modifier.isActivated()) {
            this.team1Name = team1Name;
            this.team2Name = team2Name;
            notifyUpdateTeamNames(modifier);
        }
    }

    public synchronized void setBigPoints(ScoreBoardModifier modifier, int team1BigScore, int team2BigScore) {
        if (modifier.isActivated()) {
            this.team1BigScore = team1BigScore;
            this.team2BigScore = team2BigScore;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void setSmallPoints(ScoreBoardModifier modifier, int team1SmallScore, int team2SmallScore) {
        if (modifier.isActivated()) {
            this.team1SmallScore = team1SmallScore;
            this.team2SmallScore = team2SmallScore;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void setPoints(ScoreBoardModifier modifier, int team1BigScore, int team2BigScore, int team1SmallScore, int team2SmallScore) {
        if (modifier.isActivated()) {
            this.team1BigScore = team1BigScore;
            this.team2BigScore = team2BigScore;
            this.team1SmallScore = team1SmallScore;
            this.team2SmallScore = team2SmallScore;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void setAll(ScoreBoardModifier modifier, String team1Name, String team2Name, int team1BigScore,
                       int team2BigScore, int team1SmallScore, int team2SmallScore) {
        if (modifier.isActivated()) {
            this.team1Name = team1Name;
            this.team2Name = team2Name;
            this.team1BigScore = team1BigScore;
            this.team2BigScore = team2BigScore;
            this.team1SmallScore = team1SmallScore;
            this.team2SmallScore = team2SmallScore;
            notifyUpdateTeamNames(modifier);
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void setTeam1BigScore(ScoreBoardModifier modifier, int team1BigScore) {
        if (modifier.isActivated()) {
            this.team1BigScore = team1BigScore;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void setTeam2BigScore(ScoreBoardModifier modifier, int team2BigScore) {
        if (modifier.isActivated()) {
            this.team2BigScore = team2BigScore;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void setTeam1SmallScore(ScoreBoardModifier modifier, int team1SmallScore) {
        if (modifier.isActivated()) {
            this.team1SmallScore = team1SmallScore;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void setTeam2SmallScore(ScoreBoardModifier modifier, int team2SmallScore) {
        if (modifier.isActivated()) {
            this.team2SmallScore = team2SmallScore;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void team1ScoreBigPoint(ScoreBoardModifier modifier) {
        if (modifier.isActivated()) {
            team1BigScore++;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void team2ScoreBigPoint(ScoreBoardModifier modifier) {
        if (modifier.isActivated()) {
            team2BigScore++;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void team1RemoveBigPoint(ScoreBoardModifier modifier) {
        if (modifier.isActivated()) {
            team1BigScore--;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void team2RemoveBigPoint(ScoreBoardModifier modifier) {
        if (modifier.isActivated()) {
            team2BigScore--;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void team1ScoreSmallPoint(ScoreBoardModifier modifier) {
        if (modifier.isActivated()) {
            team1SmallScore++;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void team2ScoreSmallPoint(ScoreBoardModifier modifier) {
        if (modifier.isActivated()) {
            team2SmallScore++;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void team1RemoveSmallPoint(ScoreBoardModifier modifier) {
        if (modifier.isActivated()) {
            team1SmallScore--;
            notifyUpdateScore(modifier);
        }
    }

    public synchronized void team2RemoveSmallPoint(ScoreBoardModifier modifier) {
        if (modifier.isActivated()) {
            team2SmallScore--;
            notifyUpdateScore(modifier);
        }
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public int getTeam1BigScore() {
        return team1BigScore;
    }

    public int getTeam2BigScore() {
        return team2BigScore;
    }

    public int getTeam1SmallScore() {
        return team1SmallScore;
    }

    public int getTeam2SmallScore() {
        return team2SmallScore;
    }

    private void notifyUpdateScore(ScoreBoardModifier modifier) {
        for (ScoreBoardUpdateListener listener : listeners) {
            listener.notifyUpdateScore(modifier);
        }
    }

    private void notifyUpdateTeamNames(ScoreBoardModifier modifier) {
        for (ScoreBoardUpdateListener listener : listeners) {
            listener.notifyUpdateTeamNames(modifier);
        }
    }
}
