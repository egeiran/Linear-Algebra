package linearalgebra;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class Calculator {
    public static double calculateDeterminant(Point2D v1, Point2D v2) {
        try {
            return (v1.getX() * v2.getY()) - (v1.getY() * v2.getX());
        } catch (Error e) {
            return 0.0;
        }
    }

    public static Point2D vectorMatrixMultiplication(Point2D v, Matrix matrix) {
        Point2D b1 = matrix.getE1();
        Point2D b2 = matrix.getE2();
        return new Point2D(v.getX() * b1.getX() + v.getY() * b2.getX(), v.getX() * b1.getY() + v.getY() * b2.getY());
    }

    public static Matrix matrixMultiplication(Canvas canvas, Matrix m1, Matrix m2) {
        double a = m1.getE1().getX();
        double b = m1.getE1().getY();
        double c = m1.getE2().getX();
        double d = m1.getE2().getY();

        double e = m2.getE1().getX();
        double f = m2.getE1().getY();
        double g = m2.getE2().getX();
        double h = m2.getE2().getY();

        Point2D v1 = new Point2D(
                a * e + b * g,
                c * e + d * g);
        Point2D v2 = new Point2D(
                a * f + b * h,
                c * f + d * h);
        return new Matrix(canvas, v1, v2, Color.BLUE, Color.LIGHTBLUE);
    }

    public static Matrix findInverseMatrix(Canvas canvas, Matrix matrix) {
        double determinant = calculateDeterminant(matrix.getE1(), matrix.getE2());

        Point2D v1 = new Point2D(
                matrix.getE2().getY(),
                (-1) * matrix.getE1().getY()).multiply(1 / determinant);
        Point2D v2 = new Point2D(
                (-1) * matrix.getE2().getX(),
                matrix.getE1().getX()).multiply(1 / determinant);

        Matrix returnMatrix = new Matrix(canvas, v1, v2, Color.GREEN, Color.LIGHTGREEN);
        return returnMatrix;
    }

    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        // Matrix base = new Matrix(canvas, new Point2D(1, 0), new Point2D(0, 1),
        // Color.BLUE, Color.BLUE);
        Matrix sMatrix = new Matrix(canvas, new Point2D(1, 0), new Point2D(0, -1), Color.BLUE, Color.BLUE);
        Matrix rMatrix = new Matrix(canvas, new Point2D(-1 / Math.sqrt(2), 1 / Math.sqrt(2)),
                new Point2D(-1 / Math.sqrt(2), -1 / Math.sqrt(2)), Color.BLUE, Color.BLUE);

        System.out.println(Calculator.matrixMultiplication(canvas, sMatrix, rMatrix));
        System.out.println(Calculator.matrixMultiplication(canvas, rMatrix, sMatrix));
    }
}