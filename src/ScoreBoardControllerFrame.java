import sun.awt.VerticalBagLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by jakestaahl on 11/19/15.
 */
public class ScoreBoardControllerFrame extends JFrame implements ActionListener {
    private static final String MANUAL_ACTION_COMMAND = "MANUAL";
    private static final String HTTP_ACTION_COMMAND = "HTTP";

    private static final int PANEL_PADDING = 30;

    private ScoreBoardController manualController;
    private ScoreBoardController httpController;

    public ScoreBoardControllerFrame(ScoreBoardController manualController,
                                     ScoreBoardController httpController) {
        setTitle("Scoreboard Controller");
        this.manualController = manualController;
        this.httpController = httpController;

        manualController.setBorder(new EmptyBorder(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));
        httpController.setBorder(new EmptyBorder(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JRadioButton manualRadioButton = new JRadioButton("Manual Score Board Control");
        manualRadioButton.setActionCommand(MANUAL_ACTION_COMMAND);
        manualRadioButton.addActionListener(this);
        JRadioButton httpRadioButton = new JRadioButton("Automated Score Board Control from Live Score URL");
        httpRadioButton.setActionCommand(HTTP_ACTION_COMMAND);
        httpRadioButton.addActionListener(this);
        ButtonGroup radioButtons = new ButtonGroup();
        radioButtons.add(manualRadioButton);
        radioButtons.add(httpRadioButton);

        add(manualRadioButton);
        add(httpRadioButton);

        add(new JSeparator());

        //!! these should be selectable with radio buttons. only one enabled at a time. (Automatically stop url scoring when switching away from that controller)
        //!! Switch to http, start, stop, switch away, switch back, start -> it hangs.... why???

        add(manualController);
        add(new JSeparator());
        add(httpController);

        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setVisible(true);

        manualController.setEnabled(false);
        httpController.setEnabled(false);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(MANUAL_ACTION_COMMAND)) {
            manualController.setEnabled(true);
            httpController.setEnabled(false);
        } else if (e.getActionCommand().equals(HTTP_ACTION_COMMAND)) {
            manualController.setEnabled(false);
            httpController.setEnabled(true);
        }
    }
}
