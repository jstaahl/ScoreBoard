package view.scoreboardcontroller;

import scoreboardcontrollers.ScoreBoardController;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by jakestaahl on 11/19/15.
 */
public class ScoreBoardControllerFrame extends JFrame implements ActionListener {
    private static final String MANUAL_ACTION_COMMAND = "MANUAL";
    private static final String HTTP_ACTION_COMMAND = "HTTP";

    private static final int PANEL_PADDING = 30;

    private int commandIdCounter;
    private JPanel radioButtonsPanel;
    private JPanel controllerPanel;
    private Map<String, JRadioButton> radioButtons;
    private Map<ScoreBoardController, String> controllers;
    private ButtonGroup radioButtonGroup;

    public ScoreBoardControllerFrame() {
        setTitle("Scoreboard Controller");

        commandIdCounter = 0;
        controllers = new HashMap<ScoreBoardController, String>();
        radioButtons = new HashMap<String, JRadioButton>();

        radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));
        radioButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controllerPanel = new JPanel();
        controllerPanel.setLayout(new BoxLayout(controllerPanel, BoxLayout.Y_AXIS));

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(radioButtonsPanel);
        add(controllerPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        radioButtonGroup = new ButtonGroup();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setVisible(true);
    }

    public void addScoreBoardController(ScoreBoardController controller) {
        String actionCommand = Integer.toString(commandIdCounter++);
        controllers.put(controller, actionCommand);
        JRadioButton radio = new JRadioButton(controller.getDescription());
        radio.setActionCommand(actionCommand);
        radio.addActionListener(this);

        radioButtons.put(actionCommand, radio);
        radioButtonGroup.add(radio);
        radioButtonsPanel.add(radio);

        Border padding = new EmptyBorder(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING);
        Border topBorder = new MatteBorder(1, 0, 0, 0, Color.GRAY);
        controller.setBorder(BorderFactory.createCompoundBorder(topBorder, padding));
        //!! use a compound border instead, just use a top border
        controllerPanel.add(controller);
        // deactivate the controller
        controller.setActivated(false);
        // deactivate the view elements in the controller
        controller.setEnabled(false);
    }

    public boolean removeScoreBoardController(ScoreBoardController controller) {
        String actionCommand = controllers.remove(controller);
        if (actionCommand != null) {
            JRadioButton radio = radioButtons.remove(actionCommand);
            radioButtonsPanel.remove(radio);
            controllerPanel.remove(controller);
            return true;
        }

        return false;
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        for (Map.Entry<ScoreBoardController, String> entry : controllers.entrySet()) {
            if (entry.getValue().equals(actionCommand)) {
                // activate the controller
                entry.getKey().setActivated(true);
                // activate the view elements in the controller
                entry.getKey().setEnabled(true);
            } else {
                // deactivate the controller
                entry.getKey().setActivated(false);
                // deactivate the view elements in the controller
                entry.getKey().setEnabled(false);
            }
        }
    }
}
