import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by jakestaahl on 11/19/15.
 */
public class ScoreBoardFrame extends JFrame {
    private final ScoreBoardPanel panel;

    public ScoreBoardFrame(String team1Name, String team2Name) {
        super("###SCORE###");

        panel = new ScoreBoardPanel(team1Name, team2Name);

        setUndecorated(true);
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(new Color(0, 0, 0, 0));
        JRootPane root = getRootPane();
        root.putClientProperty("Window.shadow", Boolean.FALSE );

        pack();
        setVisible(true);
    }

    public ScoreBoardFrame() {
        this("", "");
    }

    public ScoreBoardPanel getScoreBoard() {
        return panel;
    }
}
