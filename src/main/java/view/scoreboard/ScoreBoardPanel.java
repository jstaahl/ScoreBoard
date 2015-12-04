package view.scoreboard;

import model.ScoreBoard;
import model.ScoreBoardModifier;
import model.ScoreBoardUpdateListener;
import view.extra.SmoothFixedHeightLabel;
import view.extra.SmoothFixedSizeNumberLabel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;

/**
 * Created by jakestaahl on 11/17/15.
 */
public class ScoreBoardPanel extends JPanel implements ScoreBoardUpdateListener {
    private static final String BG_LEFT_FILE_NAME = "/images/graphic_stretch_1.png";
    private static final String BG_MIDDLE_FILE_NAME = "/images/graphic_stretch_2.png";
    private static final String BG_RIGHT_FILE_NAME = "/images/graphic_stretch_3.png";

    private static final int BG_MIDDLE_MIN_WIDTH = 70;

    public static final Font TEAM_NAME_FONT = new Font("Verdana", Font.BOLD, 28);
    public static final Font BIG_POINTS_FONT = new Font("Verdana", Font.BOLD, 28);
    public static final Font SMALL_POINTS_FONT = new Font("Verdana", Font.BOLD, 28);

    private JLabel team1NameLabel;
    private JLabel team2NameLabel;
    private JLabel team1SmallPointsLabel;
    private JLabel team2SmallPointsLabel;
    private JLabel team1BigPointsLabel;
    private JLabel team2BigPointsLabel;

    private Dimension panelSize;
    private float panelScale;

    private Image bgImgLeft;
    private Image bgImgMiddle;
    private Image bgImgRight;

    private Image bgImgMiddleScaled;

    private Dimension bgImgLeftSize;
    private Dimension bgImgMiddleScaledSize;
    private Dimension bgImgRightSize;

    private ScoreBoard scoreBoard;


    public ScoreBoardPanel(ScoreBoard scoreBoard) {
        setOpaque(false);

        this.scoreBoard = scoreBoard;

        try {
            bgImgLeft = ImageIO.read(this.getClass().getResourceAsStream(BG_LEFT_FILE_NAME));
            bgImgLeftSize = new Dimension(bgImgLeft.getWidth(null), bgImgLeft.getHeight(null));
            bgImgMiddle = ImageIO.read(this.getClass().getResourceAsStream(BG_MIDDLE_FILE_NAME));
            bgImgRight = ImageIO.read(this.getClass().getResourceAsStream(BG_RIGHT_FILE_NAME));
            bgImgRightSize = new Dimension(bgImgRight.getWidth(null), bgImgRight.getHeight(null));
        } catch (IOException ex) {
            System.out.println("Unable to read background image from file.");
            JOptionPane.showMessageDialog(null, "Unable to read background image from file.");
        }

        panelScale = 1.0f;

        createScoreBoard();
    }

    private void createScoreBoard() {
        calculateSizesAndScaleImages();

        Dimension bigScoreDimension = new Dimension(56, 48);
        Dimension smallScoreDimension = new Dimension(56, 48);
        team1NameLabel = new SmoothFixedHeightLabel(scoreBoard.getTeam1Name(), 48);
        team1NameLabel.setFont(TEAM_NAME_FONT);
        team1NameLabel.setVerticalAlignment(SwingConstants.CENTER);
        team1NameLabel.setForeground(Color.WHITE);
//        team1NameLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        team2NameLabel = new SmoothFixedHeightLabel(scoreBoard.getTeam2Name(), 48);
        team2NameLabel.setFont(TEAM_NAME_FONT);
        team2NameLabel.setVerticalAlignment(SwingConstants.CENTER);
        team2NameLabel.setForeground(Color.WHITE);
//        team2NameLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        team1SmallPointsLabel = new SmoothFixedSizeNumberLabel(scoreBoard.getTeam1SmallScore(), smallScoreDimension);
        team1SmallPointsLabel.setFont(SMALL_POINTS_FONT);
        team1SmallPointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        team1SmallPointsLabel.setVerticalAlignment(SwingConstants.CENTER);
        team1SmallPointsLabel.setForeground(Color.WHITE);
//        team1SmallPointsLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        team2SmallPointsLabel = new SmoothFixedSizeNumberLabel(scoreBoard.getTeam2SmallScore(), smallScoreDimension);
        team2SmallPointsLabel.setFont(SMALL_POINTS_FONT);
        team2SmallPointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        team2SmallPointsLabel.setVerticalAlignment(SwingConstants.CENTER);
        team2SmallPointsLabel.setForeground(Color.WHITE);
//        team2SmallPointsLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        team1BigPointsLabel = new SmoothFixedSizeNumberLabel(scoreBoard.getTeam1BigScore(), bigScoreDimension);
        team1BigPointsLabel.setFont(BIG_POINTS_FONT);
        team1BigPointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        team1BigPointsLabel.setVerticalAlignment(SwingConstants.CENTER);
//        team1BigPointsLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        team2BigPointsLabel = new SmoothFixedSizeNumberLabel(scoreBoard.getTeam2BigScore(), bigScoreDimension);
        team2BigPointsLabel.setFont(BIG_POINTS_FONT);
        team2BigPointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        team2BigPointsLabel.setVerticalAlignment(SwingConstants.CENTER);
//        team2BigPointsLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        this.setLayout(null);

        this.add(team1NameLabel);
        this.add(team1BigPointsLabel);
        this.add(team1SmallPointsLabel);
        this.add(team2NameLabel);
        this.add(team2BigPointsLabel);
        this.add(team2SmallPointsLabel);

        setComponentBounds();
    }

    public void setSize(Dimension size) {
        this.panelSize = size;
        createScoreBoard();
        JFrame parentFrame = (JFrame)SwingUtilities.getAncestorOfClass(JFrame.class, this);
        if (parentFrame != null) {
            parentFrame.pack();
        }
        repaint();
    }

    public void setScale(float scale) {
        this.panelScale = scale;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        AffineTransform at = g2.getTransform();

        at.scale(panelScale, panelScale);
        g2.setTransform(at);

        g.drawImage(bgImgLeft, 0, 0, null);
        g.drawImage(bgImgMiddleScaled, bgImgLeftSize.width, 0, null);
        g.drawImage(bgImgRight, bgImgLeftSize.width + bgImgMiddleScaledSize.width, 0, null);
    }

    public void notifyUpdateScore(ScoreBoardModifier modifier) {
        team1BigPointsLabel.setText(Integer.toString(scoreBoard.getTeam1BigScore()));
        team2BigPointsLabel.setText(Integer.toString(scoreBoard.getTeam2BigScore()));
        team1SmallPointsLabel.setText(Integer.toString(scoreBoard.getTeam1SmallScore()));
        team2SmallPointsLabel.setText(Integer.toString(scoreBoard.getTeam2SmallScore()));
    }

    public void notifyUpdateTeamNames(ScoreBoardModifier modifier) {
        team1NameLabel.setText(scoreBoard.getTeam1Name());
        team2NameLabel.setText(scoreBoard.getTeam2Name());
        calculateSizesAndScaleImages();
        setComponentBounds();

        JFrame parentFrame = (JFrame)SwingUtilities.getAncestorOfClass(JFrame.class, this);
        if (parentFrame != null) {
            parentFrame.pack();
        }
        repaint();
    }

    private void calculateSizesAndScaleImages() {
        FontMetrics fontMetrics = getFontMetrics(TEAM_NAME_FONT);
        int teamNameWidth = Math.max(fontMetrics.stringWidth(scoreBoard.getTeam1Name()), fontMetrics.stringWidth(scoreBoard.getTeam2Name()));

        int bgMiddleWidth = Math.max(teamNameWidth - 12, BG_MIDDLE_MIN_WIDTH);

        bgImgMiddleScaled = bgImgMiddle.getScaledInstance(bgMiddleWidth, bgImgLeftSize.height, Image.SCALE_REPLICATE);
        bgImgMiddleScaledSize = new Dimension(bgImgMiddleScaled.getWidth(null), bgImgMiddleScaled.getHeight(null));

        panelSize = new Dimension(bgImgLeftSize.width + bgImgMiddleScaledSize.width + bgImgRightSize.width,
                (Math.max(Math.max(bgImgLeftSize.height, bgImgMiddleScaledSize.height), bgImgRightSize.height)));
    }

    private void setComponentBounds() {
        Insets insets = getInsets();

        Dimension team1NameLabelSize = team1NameLabel.getPreferredSize();
        team1NameLabel.setBounds(27 + insets.left, 12 + insets.top, team1NameLabelSize.width, team1NameLabelSize.height);

        Dimension team1BigPointsLabelSize = team1BigPointsLabel.getPreferredSize();
        team1BigPointsLabel.setBounds(29 + bgImgLeftSize.width + bgImgMiddleScaledSize.width + insets.left, 12 + insets.top, team1BigPointsLabelSize.width, team1BigPointsLabelSize.height);

        Dimension team1SmallPointsLabelSize = team1SmallPointsLabel.getPreferredSize();
        team1SmallPointsLabel.setBounds(29 + bgImgLeftSize.width + bgImgMiddleScaledSize.width + team1BigPointsLabelSize.width + insets.left, 12 + insets.top, team1SmallPointsLabelSize.width, team1SmallPointsLabelSize.height);

        Dimension team2NameLabelSize = team2NameLabel.getPreferredSize();
        team2NameLabel.setBounds(27 + insets.left, 66 + insets.top, team2NameLabelSize.width, team2NameLabelSize.height);

        Dimension team2BigPointsLabelSize = team2BigPointsLabel.getPreferredSize();
        team2BigPointsLabel.setBounds(29 + bgImgLeftSize.width + bgImgMiddleScaledSize.width + insets.left, 66 + insets.top, team2BigPointsLabelSize.width, team2BigPointsLabelSize.height);

        Dimension team2SmallPointsLabelSize = team2SmallPointsLabel.getPreferredSize();
        team2SmallPointsLabel.setBounds(29 + bgImgLeftSize.width + bgImgMiddleScaledSize.width + team2BigPointsLabelSize.width + insets.left, 66 + insets.top, team2SmallPointsLabelSize.width, team2SmallPointsLabelSize.height);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int)Math.ceil(panelScale*panelSize.width), (int)Math.ceil(panelScale*panelSize.height));
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension((int)Math.ceil(panelScale*panelSize.width), (int)Math.ceil(panelScale*panelSize.height));
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension((int)Math.ceil(panelScale*panelSize.width), (int)Math.ceil(panelScale*panelSize.height));
    }
}
