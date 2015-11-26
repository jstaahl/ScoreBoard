package view.extra;

import javax.swing.*;
import java.awt.*;

public class SmoothFixedSizeLabel extends JLabel {
    private Dimension size = null;

    public SmoothFixedSizeLabel(String text, Dimension size) {
        super(text);
        this.size = size;
    }


    public SmoothFixedSizeLabel(Dimension size) {
        this("", size);
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
        return size != null ? size : super.getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return size != null ? size : super.getMaximumSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return size != null ? size : super.getMinimumSize();
    }


}