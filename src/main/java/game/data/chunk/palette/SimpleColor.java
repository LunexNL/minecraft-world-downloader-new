package game.data.chunk.palette;

import javafx.scene.paint.Color;

/**
 * Class to handle colours of blocks when building the overview image.
 */
public class SimpleColor {
    public static final SimpleColor BLACK = new SimpleColor(0, 0, 0);
    
    private final double r, g, b;

    /**
     * Parse from RGB int, where the rightmost three bytes are R, G and B respectively.
     */
    public SimpleColor(int full) {
        this.r = (full >> 16) & 0xFF;
        this.g = (full >> 8) & 0xFF;
        this.b = full & 0xFF;
    }

    public SimpleColor(double shade) {
        this(0xFF * shade, 0xFF * shade, 0xFF * shade);
    }

    private SimpleColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public SimpleColor(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Blends with another colour using the ratio as a multiplier.
     */
    public SimpleColor blendWith(SimpleColor other, double ratio) {
        double rNew = blendWith(this.r, other.r, ratio);
        double gNew = blendWith(this.g, other.g, ratio);
        double bNew = blendWith(this.b, other.b, ratio);

        return new SimpleColor(rNew, gNew, bNew);
    }

    private double blendWith(double v1, double v2, double ratio) {
        return v1 * ratio + v2 * (1 - ratio);
    }

    /**
     * Lighten or darken colour for elevation changes.
     */
    public SimpleColor shaderMultiply(double colorShader) {
        return new SimpleColor(
                this.r * colorShader,
                this.g * colorShader,
                this.b * colorShader
        );
    }

    public int toARGB() {
        int res = 0xFF000000;
        res |= toInt(r) << 16;
        res |= toInt(g) << 8;
        res |= toInt(b);

        return res;
    }

    public Color toJavaFxColor() {
        return Color.color(toDouble(r), toDouble(g), toDouble(b));
    }

    /**
     * Scales RGB values from 0-255 to 0.0-1.0, clamp them to ensure we don't go over.
     */
    private double toDouble(double v) {
        return Math.max(Math.min(1.0f, v / 255.0f), 0.0);
    }
    private int toInt(double v) {
        return (int) Math.round(Math.max(Math.min(255, v), 0));
    }

    @Override
    public String toString() {
        return "SimpleColor{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }

    /**
     * Highlight colour by marking it red.
     */
    public SimpleColor highlight() {
        return new SimpleColor(
                ((int) this.r) ^ 210,
                this.g,
                this.b
        );

    }
}
