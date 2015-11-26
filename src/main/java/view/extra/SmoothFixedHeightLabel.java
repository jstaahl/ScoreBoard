package view.extra;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jakestaahl on 11/25/15.
 */
public class SmoothFixedHeightLabel extends JLabel {
    private int height;

    public SmoothFixedHeightLabel(String text, int height) {
        super(text);
        this.height = height;
    }


    public SmoothFixedHeightLabel(int height) {
        this("", height);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        super.paintComponent(g2);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension standard = super.getPreferredSize();
        if (height > 0) {
            return new Dimension(standard.width, height);
        } else {
            return standard;
        }
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension standard = super.getMaximumSize();
        if (height > 0) {
            return new Dimension(standard.width, height);
        } else {
            return standard;
        }
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension standard = super.getMinimumSize();
        if (height > 0) {
            return new Dimension(standard.width, height);
        } else {
            return standard;
        }
    }

}
