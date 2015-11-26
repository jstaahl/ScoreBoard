package view.scoreboard;

import model.ScoreBoard;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jakestaahl on 11/19/15.
 */
public class ScoreBoardFrame extends JFrame {
    private final ScoreBoardPanel panel;

    public ScoreBoardFrame(ScoreBoardPanel panel) {
        super("###SCORE###");

        this.panel = panel;

        setUndecorated(true);
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(new Color(0, 0, 0, 0));
        JRootPane root = getRootPane();
        root.putClientProperty("Window.shadow", Boolean.FALSE );

        pack();
        setVisible(true);
    }

    public ScoreBoardPanel getScoreBoard() {
        return panel;
    }
}
