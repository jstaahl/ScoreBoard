package scoreboardcontrollers;

import model.ScoreBoard;
import model.ScoreBoardModifier;
import view.scoreboard.ScoreBoardPanel;
import view.extra.SmoothFixedSizeNumberLabel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by jakestaahl on 11/19/15.
 */
public class ManualScoreBoardController extends ScoreBoardController {
    private static final int TEAM_NAME_COLS = 18;
    private static final Dimension SCORE_LABEL_DIMEN = new Dimension(50, 50);
    private static final Dimension SCORE_DEC_DIMEN = new Dimension(20, 30);
    private static final Dimension RESET_GAME_SCORE_DIMEN = new Dimension(60, 40);
    private static final Dimension RESET_ALL_SCORE_DIMEN = new Dimension(140, 40);
    private static final String DESCRIPTION = "Manual Score Board Control";

    private JLabel team1Label;
    private JLabel team2Label;
    private JTextField team1Name;
    private JTextField team2Name;
    private SmoothFixedSizeNumberLabel team1SmallScore;
    private SmoothFixedSizeNumberLabel team2SmallScore;
    private SmoothFixedSizeNumberLabel team1BigScore;
    private SmoothFixedSizeNumberLabel team2BigScore;

    private JButton team1SmallDec;
    private JButton team2SmallDec;
    private JButton team1BigDec;
    private JButton team2BigDec;
    private JButton resetGameScore;
    private JButton resetAllScore;

    private DocumentListener team1NameListener;
    private DocumentListener team2NameListener;

    public ManualScoreBoardController(ScoreBoard score) {
        super(score);

        initUI();
        addKeyBindings();
    }

    private void initUI() {
        team1SmallDec = new JButton("-");
        team1SmallDec.setPreferredSize(SCORE_DEC_DIMEN);
        team1SmallDec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                scoreBoard.team1RemoveSmallPoint(ManualScoreBoardController.this);
            }
        });
        team2SmallDec = new JButton("-");
        team2SmallDec.setPreferredSize(SCORE_DEC_DIMEN);
        team2SmallDec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                scoreBoard.team2RemoveSmallPoint(ManualScoreBoardController.this);
            }
        });

        team1BigDec = new JButton("-");
        team1BigDec.setPreferredSize(SCORE_DEC_DIMEN);
        team1BigDec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                scoreBoard.team1RemoveBigPoint(ManualScoreBoardController.this);
            }
        });
        team2BigDec = new JButton("-");
        team2BigDec.setPreferredSize(SCORE_DEC_DIMEN);
        team2BigDec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                scoreBoard.team2RemoveBigPoint(ManualScoreBoardController.this);
            }
        });

        resetAllScore = new JButton("Reset Score Board");
        resetAllScore.setPreferredSize(RESET_ALL_SCORE_DIMEN);
        resetAllScore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                scoreBoard.setPoints(ManualScoreBoardController.this, 0, 0, 0, 0);
            }
        });

        resetGameScore = new JButton("New Set");
        resetGameScore.setPreferredSize(RESET_GAME_SCORE_DIMEN);
        resetGameScore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                scoreBoard.setSmallPoints(ManualScoreBoardController.this, 0, 0);
            }
        });

        team1Label = new JLabel("Team 1:");
        team1Name = new JTextField(TEAM_NAME_COLS);
        team1NameListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                scoreBoard.setTeam1Name(ManualScoreBoardController.this, team1Name.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                scoreBoard.setTeam1Name(ManualScoreBoardController.this, team1Name.getText());
            }

            public void insertUpdate(DocumentEvent e) {
                scoreBoard.setTeam1Name(ManualScoreBoardController.this, team1Name.getText());
            }
        };
        team1Name.getDocument().addDocumentListener(team1NameListener);

        team2Label = new JLabel("Team 2:");
        team2Name = new JTextField(TEAM_NAME_COLS);
        team2NameListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                scoreBoard.setTeam2Name(ManualScoreBoardController.this, team2Name.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                scoreBoard.setTeam2Name(ManualScoreBoardController.this, team2Name.getText());
            }

            public void insertUpdate(DocumentEvent e) {
                scoreBoard.setTeam2Name(ManualScoreBoardController.this, team2Name.getText());
            }
        };
        team2Name.getDocument().addDocumentListener(team2NameListener);

        team1SmallScore = new SmoothFixedSizeNumberLabel(0, SCORE_LABEL_DIMEN);
        team1SmallScore.setHorizontalAlignment(SwingConstants.CENTER);
        team1SmallScore.setVerticalAlignment(SwingConstants.CENTER);
        team1SmallScore.setOpaque(true);
        team1SmallScore.setBackground(Color.BLUE);
        team1SmallScore.setForeground(Color.WHITE);
        team1SmallScore.setFont(ScoreBoardPanel.SMALL_POINTS_FONT);
        team1SmallScore.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                scoreBoard.team1ScoreSmallPoint(ManualScoreBoardController.this);
            }
        });

        team2SmallScore = new SmoothFixedSizeNumberLabel(0, SCORE_LABEL_DIMEN);
        team2SmallScore.setHorizontalAlignment(SwingConstants.CENTER);
        team2SmallScore.setVerticalAlignment(SwingConstants.CENTER);
        team2SmallScore.setOpaque(true);
        team2SmallScore.setBackground(Color.BLUE);
        team2SmallScore.setForeground(Color.WHITE);
        team2SmallScore.setFont(ScoreBoardPanel.SMALL_POINTS_FONT);
        team2SmallScore.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                scoreBoard.team2ScoreSmallPoint(ManualScoreBoardController.this);
            }
        });

        team1BigScore = new SmoothFixedSizeNumberLabel(0, SCORE_LABEL_DIMEN);
        team1BigScore.setHorizontalAlignment(SwingConstants.CENTER);
        team1BigScore.setVerticalAlignment(SwingConstants.CENTER);
        team1BigScore.setOpaque(true);
        team1BigScore.setBackground(Color.YELLOW);
        team1BigScore.setForeground(Color.BLACK);
        team1BigScore.setFont(ScoreBoardPanel.BIG_POINTS_FONT);
        team1BigScore.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                scoreBoard.team1ScoreBigPoint(ManualScoreBoardController.this);
            }
        });

        team2BigScore = new SmoothFixedSizeNumberLabel(0, SCORE_LABEL_DIMEN);
        team2BigScore.setHorizontalAlignment(SwingConstants.CENTER);
        team2BigScore.setVerticalAlignment(SwingConstants.CENTER);
        team2BigScore.setOpaque(true);
        team2BigScore.setBackground(Color.YELLOW);
        team2BigScore.setForeground(Color.BLACK);
        team2BigScore.setFont(ScoreBoardPanel.BIG_POINTS_FONT);
        team2BigScore.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                scoreBoard.team2ScoreBigPoint(ManualScoreBoardController.this);
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JSeparator sep;

        c.gridy = 0; c.gridx = 0;
        add(team1Label, c);
        c.gridy = 0; c.gridx = 1;
        add(team1Name, c);

        c.gridy = 0; c.gridx = 2;
        c.fill = GridBagConstraints.VERTICAL;
        sep = new JSeparator(JSeparator.VERTICAL);
        sep.setForeground(Color.BLACK);
        add(sep, c);
        c.fill = GridBagConstraints.NONE;

        c.gridy = 0; c.gridx = 3;
        add(team1BigDec, c);
        c.gridy = 0; c.gridx = 4;
        add(team1BigScore, c);

        c.gridy = 0; c.gridx = 5;
        c.fill = GridBagConstraints.VERTICAL;
        sep = new JSeparator(JSeparator.VERTICAL);
        sep.setForeground(Color.BLACK);
        add(sep, c);
        c.fill = GridBagConstraints.NONE;

        c.gridy = 0; c.gridx = 6;
        add(team1SmallDec, c);
        c.gridy = 0; c.gridx = 7;
        add(team1SmallScore, c);

        c.gridy = 1; c.gridx = 0;
        add(team2Label, c);
        c.gridy = 1; c.gridx = 1;
        add(team2Name, c);

        c.gridy = 1; c.gridx = 2;
        c.fill = GridBagConstraints.VERTICAL;
        sep = new JSeparator(JSeparator.VERTICAL);
        sep.setForeground(Color.BLACK);
        add(sep, c);
        c.fill = GridBagConstraints.NONE;

        c.gridy = 1; c.gridx = 3;
        add(team2BigDec, c);
        c.gridy = 1; c.gridx = 4;
        add(team2BigScore, c);

        c.gridy = 1; c.gridx = 5;
        c.fill = GridBagConstraints.VERTICAL;
        sep = new JSeparator(JSeparator.VERTICAL);
        sep.setForeground(Color.BLACK);
        add(sep, c);
        c.fill = GridBagConstraints.NONE;

        c.gridy = 1; c.gridx = 6;
        add(team2SmallDec, c);
        c.gridy = 1; c.gridx = 7;
        add(team2SmallScore, c);
        c.gridy = 2; c.gridx = 1;
        add(resetAllScore, c);
        c.gridy = 2; c.gridx = 6; c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(resetGameScore, c);

    }

    private void addKeyBindings() {
        KeyEventDispatcher scoreControlDispatcher = new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP && e.getID() == KeyEvent.KEY_PRESSED) {
                    scoreBoard.team1ScoreSmallPoint(ManualScoreBoardController.this);
                    return true;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && e.getID() == KeyEvent.KEY_PRESSED) {
                    scoreBoard.team2ScoreSmallPoint(ManualScoreBoardController.this);
                    return true;
                }
                return false;
            }
        };
        KeyboardFocusManager focusManager = KeyboardFocusManager.
                getCurrentKeyboardFocusManager();
        focusManager.addKeyEventDispatcher(scoreControlDispatcher);
    }


    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    protected void activate() {

    }

    @Override
    protected void deactivate() {

    }


    public void notifyUpdateTeamNames(ScoreBoardModifier modifier) {
        if (modifier != this) {
            this.team1Name.getDocument().removeDocumentListener(team1NameListener);
            this.team1Name.setText(scoreBoard.getTeam1Name());
            this.team1Name.getDocument().addDocumentListener(team1NameListener);

            this.team2Name.getDocument().removeDocumentListener(team2NameListener);
            this.team2Name.setText(scoreBoard.getTeam2Name());
            this.team2Name.getDocument().addDocumentListener(team2NameListener);
        }
    }

    public void notifyUpdateScore(ScoreBoardModifier modifier) {
        this.team1BigScore.setValue(scoreBoard.getTeam1BigScore());
        this.team2BigScore.setValue(scoreBoard.getTeam2BigScore());
        this.team1SmallScore.setValue(scoreBoard.getTeam1SmallScore());
        this.team2SmallScore.setValue(scoreBoard.getTeam2SmallScore());
    }
}