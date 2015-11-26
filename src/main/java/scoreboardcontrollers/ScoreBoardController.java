package scoreboardcontrollers;

import model.ScoreBoard;
import model.ScoreBoardModifier;
import model.ScoreBoardUpdateListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jakestaahl on 11/19/15.
 */
public abstract class ScoreBoardController extends JPanel implements ScoreBoardModifier, ScoreBoardUpdateListener {
    private boolean isActivated;
    protected ScoreBoard scoreBoard;

    public ScoreBoardController(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
        isActivated = false;
    }

    public abstract String getDescription();

    public boolean isActivated() {
        return isActivated;
    }

    public final void setActivated(boolean activate) {
        if (activate) {
            isActivated = true;
            activate();
        } else {
            isActivated = false;
            deactivate();
        }
    }

    protected abstract void activate();

    protected abstract void deactivate();

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        enableComponents(this, enabled);
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

}
