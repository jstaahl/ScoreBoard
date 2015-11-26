package model;

/**
 * Created by jakestaahl on 11/24/15.
 */
public interface ScoreBoardUpdateListener {
    public void notifyUpdateScore(ScoreBoardModifier modifier);

    public void notifyUpdateTeamNames(ScoreBoardModifier modifier);
}
