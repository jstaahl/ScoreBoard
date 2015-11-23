import javax.swing.*;
import java.awt.*;

/**
 * Created by jakestaahl on 11/19/15.
 */
public abstract class ScoreBoardController extends JPanel {
    protected ScoreBoardPanel scoreBoard;

    public ScoreBoardController(ScoreBoardPanel scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        enableComponents(this, enabled);
        if (enabled) {
            enableController(scoreBoard.getTeam1Name(), scoreBoard.getTeam2Name(),
                    scoreBoard.getTeam1BigPoints(), scoreBoard.getTeam2BigPoints(),
                    scoreBoard.getTeam1SmallPoints(), scoreBoard.getTeam2SmallPoints());
        }
    }

    private void enableComponents(Container container, boolean enable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container)component, enable);
            }
        }
    }

    public abstract void enableController(String team1Name, String team2Name,
                                          int team1BigScore, int team2BigScore,
                                          int team1SmallScore, int team2SmallScore);
}
