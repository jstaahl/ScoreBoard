import java.awt.*;

/**
 * Created by jakestaahl on 11/18/15.
 */
public class SmoothFixedSizeNumberLabel extends SmoothFixedSizeLabel {
    private int value;

    public SmoothFixedSizeNumberLabel(int value, Dimension size) {
        super(String.valueOf(value), size);
        this.value = value;
    }

    public SmoothFixedSizeNumberLabel(Dimension size) {
        super("0", size);
        value = 0;
    }

    public void setValue(int value) {
        this.value = value;
        setText(String.valueOf(value));
    }

    public int getValue() {
        return value;
    }

    public int incrementValue() {
        setText(String.valueOf(++value));
        return value;
    }

    public int decrementValue() {
        setText(String.valueOf(--value));
        return value;
    }
}
