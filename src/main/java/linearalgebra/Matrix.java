package linearalgebra;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Matrix {
    protected final Canvas gridCanvas;
    GraphicsContext gc;
    Point2D origin;

    private Point2D v1, v2;
    private boolean hidden = false;
    private Color primaryColor, secondaryColor;

    private List<Point2D> vectors = new ArrayList<>();

    private double scale = 25;

    private double width, height;

    public Matrix(Canvas gridCanvas, Point2D v1, Point2D v2,
            Color primaryColor, Color secondaryColor) {
        this.gridCanvas = gridCanvas;
        this.width = gridCanvas.getWidth();
        this.height = gridCanvas.getHeight();
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;

        gc = this.gridCanvas.getGraphicsContext2D();
        origin = new Point2D(width / 2, height / 2);

        this.v1 = v1;
        this.v2 = v2;
    }

    public void setE1(Point2D e1) {
        this.v1 = e1;
    }

    public void setE2(Point2D e2) {
        this.v2 = e2;
    }

    public Point2D getE1() {
        return v1;
    }

    public Point2D getE2() {
        return v2;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean getHidden() {
        return hidden;
    }

    public List<Point2D> getVectors() {
        return vectors;
    }

    private boolean checkValidity(Point2D v1, Point2D v2) {
        double c = v1.dotProduct(v2) / (v1.magnitude() * v2.magnitude());
        if (Math.abs(c) > 1 - 1e-6) {
            return false;
        }
        return true;
    }

    protected void drawGrid() {
        if (!checkValidity(v1, v2)) {
            throw new IllegalStateException("Ikke mulig å tegne");
        }
        if (hidden)
            return;

        Point2D u1 = new Point2D(v1.getX() * scale, v1.getY() * -scale);
        Point2D u2 = new Point2D(v2.getX() * scale, v2.getY() * -scale);

        double minLength = origin.distance(0, 0);
        double u1amount = minLength / u1.magnitude() + 1;
        double u2amount = minLength / u2.magnitude() + 1;

        Point2D u1Start = origin.add(u1.multiply(2 * -(int) u1amount));
        Point2D u1End = origin.add(u1.multiply(2 * (int) u1amount));
        Point2D u2Start = origin.add(u2.multiply(2 * -(int) u2amount));
        Point2D u2End = origin.add(u2.multiply(2 * (int) u2amount));

        gc.setStroke(secondaryColor);
        gc.setLineWidth(1);

        for (int i = 0; i <= u2amount * 10; i++) {
            gc.strokeLine(u1Start.add(u2.multiply(i)).getX(), u1Start.add(u2.multiply(i)).getY(),
                    u1End.add(u2.multiply(i)).getX(), u1End.add(u2.multiply(i)).getY());
            gc.strokeLine(u1Start.add(u2.multiply(-i)).getX(), u1Start.add(u2.multiply(-i)).getY(),
                    u1End.add(u2.multiply(-i)).getX(), u1End.add(u2.multiply(-i)).getY());
        }

        for (int i = 0; i <= u1amount * 10; i++) {
            gc.strokeLine(u2Start.add(u1.multiply(i)).getX(), u2Start.add(u1.multiply(i)).getY(),
                    u2End.add(u1.multiply(i)).getX(), u2End.add(u1.multiply(i)).getY());
            gc.strokeLine(u2Start.add(u1.multiply(-i)).getX(), u2Start.add(u1.multiply(-i)).getY(),
                    u2End.add(u1.multiply(-i)).getX(), u2End.add(u1.multiply(-i)).getY());
        }

        gc.setStroke(primaryColor);
        gc.setLineWidth(2);
        gc.strokeLine(u1Start.getX(), u1Start.getY(), u1End.getX(), u1End.getY());
        gc.strokeLine(u2Start.getX(), u2Start.getY(), u2End.getX(), u2End.getY());

        for (Point2D vector : vectors) {
            drawVector(vector);
        }
    }

    public void addVector(Point2D v) {
        vectors.add(v);
    }

    public void drawVector(Point2D v) {
        Point2D disp = Calculator.vectorMatrixMultiplication(v, this);

        gc.setStroke(primaryColor);
        gc.setLineWidth(2);
        gc.strokeLine(
                origin.getX(), origin.getY(),
                origin.getX() + disp.getX() * scale,
                origin.getY() - disp.getY() * scale);
    }

    public void emptyVectors() {
        vectors.removeAll(vectors);
    }

    @Override
    public String toString() {
        return "Matrix: \n - Vektor 1: " + v1 + "\n - Vektor 2: " + v2 + "\n";
    }
}
