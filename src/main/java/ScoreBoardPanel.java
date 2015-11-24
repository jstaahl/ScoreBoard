import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;

/**
 * Created by jakestaahl on 11/17/15.
 */
public class ScoreBoardPanel extends JPanel {
    private static final String BG_LEFT_FILE_NAME = "graphic_stretch_1.png";
    private static final String BG_MIDDLE_FILE_NAME = "graphic_stretch_2.png";
    private static final String BG_RIGHT_FILE_NAME = "graphic_stretch_3.png";

    private static final int BG_MIDDLE_MIN_WIDTH = 70;

    private static final float COL_1_WIDTH = 0.5f;
    private static final float COL_2_WIDTH = 0.25f;
    private static final float COL_3_WIDTH = 0.25f;

    public static final Font TEAM_NAME_FONT = new Font("Verdana", Font.BOLD, 28);
    public static final Font BIG_POINTS_FONT = new Font("Verdana", Font.BOLD, 28);
    public static final Font SMALL_POINTS_FONT = new Font("Verdana", Font.BOLD, 28);

    private String team1Name;
    private String team2Name;
    private Color team1Color;
    private Color team2Color;
    private int server;

    private JLabel team1NameLabel;
    private JLabel team2NameLabel;
    private SmoothFixedSizeNumberLabel team1SmallPointsLabel;
    private SmoothFixedSizeNumberLabel team2SmallPointsLabel;
    private SmoothFixedSizeNumberLabel team1BigPointsLabel;
    private SmoothFixedSizeNumberLabel team2BigPointsLabel;

    private Dimension panelSize;
    private float panelScale;

    private Image bgImgLeft;
    private Image bgImgMiddle;
    private Image bgImgRight;

    private Image bgImgMiddleScaled;

    private Dimension bgImgLeftSize;
    private Dimension bgImgMiddleScaledSize;
    private Dimension bgImgRightSize;


    public ScoreBoardPanel(String team1Name, String team2Name) {
        setOpaque(false);

        this.team1Name = team1Name;
        this.team2Name = team2Name;

        try {
            bgImgLeft = ImageIO.read(ScoreBoardPanel.class.getResourceAsStream(BG_LEFT_FILE_NAME));
            bgImgLeftSize = new Dimension(bgImgLeft.getWidth(null), bgImgLeft.getHeight(null));
            bgImgMiddle = ImageIO.read(ScoreBoardPanel.class.getResourceAsStream(BG_MIDDLE_FILE_NAME));
            bgImgRight = ImageIO.read(ScoreBoardPanel.class.getResourceAsStream(BG_RIGHT_FILE_NAME));
            bgImgRightSize = new Dimension(bgImgRight.getWidth(null), bgImgRight.getHeight(null));
        } catch (IOException ex) {
            System.out.println("Unable to read background image from file.");
            JOptionPane.showMessageDialog(null, "Unable to read background image from file.");
        }

        panelScale = 1.0f;

        createScoreBoard();
    }

    public ScoreBoardPanel() {
        this("", "");
    }

    private void createScoreBoard() {
        FontMetrics fontMetrics = getFontMetrics(TEAM_NAME_FONT);
        int teamNameWidth = Math.max(fontMetrics.stringWidth(team1Name), fontMetrics.stringWidth(team2Name));

        int bgMiddleWidth = Math.max(teamNameWidth - 12, BG_MIDDLE_MIN_WIDTH);

        bgImgMiddleScaled = bgImgMiddle.getScaledInstance(bgMiddleWidth, bgImgLeftSize.height, Image.SCALE_REPLICATE);
        bgImgMiddleScaledSize = new Dimension(bgImgMiddleScaled.getWidth(null), bgImgMiddleScaled.getHeight(null));

        panelSize = new Dimension(bgImgLeftSize.width + bgImgMiddleScaledSize.width + bgImgRightSize.width,
                (Math.max(Math.max(bgImgLeftSize.height, bgImgMiddleScaledSize.height), bgImgRightSize.height)));

        Dimension nameLabelDimension = new Dimension(bgImgLeftSize.width + bgImgMiddleScaledSize.width - 15, 48);
        Dimension bigScoreDimension = new Dimension(56, 48);
        Dimension smallScoreDimension = new Dimension(56, 48);
        team1NameLabel = new SmoothFixedSizeLabel(team1Name, nameLabelDimension);
        team1NameLabel.setFont(TEAM_NAME_FONT);
        team1NameLabel.setVerticalAlignment(SwingConstants.CENTER);
        team1NameLabel.setForeground(Color.WHITE);
//        team1NameLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        team2NameLabel = new SmoothFixedSizeLabel(team2Name, nameLabelDimension);
        team2NameLabel.setFont(TEAM_NAME_FONT);
        team2NameLabel.setVerticalAlignment(SwingConstants.CENTER);
        team2NameLabel.setForeground(Color.WHITE);
//        team2NameLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        team1SmallPointsLabel = new SmoothFixedSizeNumberLabel(team1SmallPointsLabel == null ? 0 : team1SmallPointsLabel.getValue(), smallScoreDimension);
        team1SmallPointsLabel.setFont(SMALL_POINTS_FONT);
        team1SmallPointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        team1SmallPointsLabel.setVerticalAlignment(SwingConstants.CENTER);
        team1SmallPointsLabel.setForeground(Color.WHITE);
//        team1SmallPointsLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        team2SmallPointsLabel = new SmoothFixedSizeNumberLabel(team2SmallPointsLabel == null ? 0 : team2SmallPointsLabel.getValue(), smallScoreDimension);
        team2SmallPointsLabel.setFont(SMALL_POINTS_FONT);
        team2SmallPointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        team2SmallPointsLabel.setVerticalAlignment(SwingConstants.CENTER);
        team2SmallPointsLabel.setForeground(Color.WHITE);
//        team2SmallPointsLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        team1BigPointsLabel = new SmoothFixedSizeNumberLabel(team1BigPointsLabel == null ? 0 : team1BigPointsLabel.getValue(), bigScoreDimension);
        team1BigPointsLabel.setFont(BIG_POINTS_FONT);
        team1BigPointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        team1BigPointsLabel.setVerticalAlignment(SwingConstants.CENTER);
//        team1BigPointsLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        team2BigPointsLabel = new SmoothFixedSizeNumberLabel(team2BigPointsLabel == null ? 0 : team2BigPointsLabel.getValue(), bigScoreDimension);
        team2BigPointsLabel.setFont(BIG_POINTS_FONT);
        team2BigPointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        team2BigPointsLabel.setVerticalAlignment(SwingConstants.CENTER);
//        team2BigPointsLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        this.setLayout(null);
        Insets insets = this.getInsets();
        this.removeAll();

        this.add(team1NameLabel);
        this.add(team1BigPointsLabel);
        this.add(team1SmallPointsLabel);
        this.add(team2NameLabel);
        this.add(team2BigPointsLabel);
        this.add(team2SmallPointsLabel);

        Dimension team1NameLabelSize = team1NameLabel.getPreferredSize();
        team1NameLabel.setBounds(27 + insets.left, 12 + insets.top, team1NameLabelSize.width, team1NameLabelSize.height);

        Dimension team1BigPointsLabelSize = team1BigPointsLabel.getPreferredSize();
        team1BigPointsLabel.setBounds(43 + team1NameLabelSize.width + insets.left, 12 + insets.top, team1BigPointsLabelSize.width, team1BigPointsLabelSize.height);

        Dimension team1SmallPointsLabelSize = team1SmallPointsLabel.getPreferredSize();
        team1SmallPointsLabel.setBounds(43 + team1NameLabelSize.width + team1BigPointsLabelSize.width + insets.left, 12 + insets.top, team1SmallPointsLabelSize.width, team1SmallPointsLabelSize.height);

        Dimension team2NameLabelSize = team2NameLabel.getPreferredSize();
        team2NameLabel.setBounds(27 + insets.left, 66 + insets.top, team2NameLabelSize.width, team2NameLabelSize.height);

        Dimension team2BigPointsLabelSize = team2BigPointsLabel.getPreferredSize();
        team2BigPointsLabel.setBounds(43 + team2NameLabelSize.width + insets.left, 66 + insets.top, team2BigPointsLabelSize.width, team2BigPointsLabelSize.height);

        Dimension team2SmallPointsLabelSize = team2SmallPointsLabel.getPreferredSize();
        team2SmallPointsLabel.setBounds(43 + team2NameLabelSize.width + team2BigPointsLabelSize.width + insets.left, 66 + insets.top, team2SmallPointsLabelSize.width, team2SmallPointsLabelSize.height);
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

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
        createScoreBoard();
        JFrame parentFrame = (JFrame)SwingUtilities.getAncestorOfClass(JFrame.class, this);
        if (parentFrame != null) {
            parentFrame.pack();
        }
        repaint();
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
        createScoreBoard();
        JFrame parentFrame = (JFrame)SwingUtilities.getAncestorOfClass(JFrame.class, this);
        if (parentFrame != null) {
            parentFrame.pack();
        }
        repaint();
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setScale(float scale) {
        this.panelScale = scale;
    }

    public void setTeam1Color(Color color) {
        team1Color = color;
        //!! how to repaint?
    }

    public void setTeam2Color(Color color) {
        team2Color = color;
        //!! how to repaint?
    }

    public void setSmallPoints(int team1, int team2) {
        team1SmallPointsLabel.setValue(team1);
        team2SmallPointsLabel.setValue(team2);
        setServerUnknown();
    }

    public int getTeam1SmallPoints() {
        return team1SmallPointsLabel.getValue();
    }

    public int getTeam2SmallPoints() {
        return team2SmallPointsLabel.getValue();
    }

    public void setBigPoints(int team1, int team2) {
        team1BigPointsLabel.setValue(team1);
        team2BigPointsLabel.setValue(team2);
    }

    public int getTeam1BigPoints() {
        return team1BigPointsLabel.getValue();
    }

    public int getTeam2BigPoints() {
        return team2BigPointsLabel.getValue();
    }

    public int team1ScoreBigPoint() {
        return team1BigPointsLabel.incrementValue();
    }

    public int team2ScoreBigPoint() {
        return team2BigPointsLabel.incrementValue();
    }

    public int team1RemoveBigPoint() {
        return team1BigPointsLabel.decrementValue();
    }

    public int team2RemoveBigPoint() {
        return team2BigPointsLabel.decrementValue();
    }

    public int team1ScoreSmallPoint() {
        setServerTeam1();
        return team1SmallPointsLabel.incrementValue();
    }

    public int team2ScoreSmallPoint() {
        setServerTeam2();
        return team2SmallPointsLabel.incrementValue();
    }

    public int team1RemoveSmallPoint() {
        setServerUnknown();
        return team1SmallPointsLabel.decrementValue();
    }

    public int team2RemoveSmallPoint() {
        setServerUnknown();
        return team2SmallPointsLabel.decrementValue();
    }

    public void setServerTeam1() {
        server = 0;
    }

    public void setServerTeam2() {
        server = 1;
    }

    public void setServerUnknown() {
        server = -1;
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
