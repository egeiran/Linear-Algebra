package linearalgebra;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class GridController {
    @FXML
    protected Canvas gridCanvas;

    private Matrix base;
    private Matrix newGrid = null;
    private Matrix inverseMatrix = null;

    private List<Matrix> matrixList = new ArrayList<>();

    @FXML
    private void initialize() {
        Point2D e1 = new Point2D(1, 0);
        Point2D e2 = new Point2D(0, 1);

        base = new Matrix(gridCanvas,
                e1, e2,
                Color.BLACK, Color.LIGHTGRAY);
        matrixList.add(base);
        base.drawGrid();
        gridCanvas.sceneProperty().addListener((obs, oldscene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(evt -> {
                    if (evt.getCode() == KeyCode.SPACE) {
                        matrixList.removeAll(matrixList);
                        matrixList.add(base);
                        handleNewGrid();
                    }
                    if (evt.getCode() == KeyCode.V) {
                        handleNewVector();
                    }
                    if (evt.getCode() == KeyCode.D) {
                        for (Matrix m : matrixList) {
                            m.emptyVectors();
                        }
                        reDraw();
                    }
                    if (evt.getCode() == KeyCode.P) {
                        if (newGrid != null) {
                            Matrix productMatrix = Calculator.matrixMultiplication(gridCanvas, base, newGrid);
                            productMatrix.drawGrid();
                        }
                    }
                    if (evt.getCode() == KeyCode.DIGIT1) {
                        matrixList.get(0).setHidden(!matrixList.get(0).getHidden());
                        reDraw();
                    }
                    if (matrixList.size() >= 2) {
                        if (evt.getCode() == KeyCode.DIGIT2) {
                            try {
                                matrixList.get(1).setHidden(!matrixList.get(1).getHidden());
                            } catch (Error e) {
                                throw new IllegalArgumentException("Matrix does not exist");
                            }
                            reDraw();
                        }
                    }
                    if (matrixList.size() >= 3) {
                        if (evt.getCode() == KeyCode.DIGIT3) {
                            try {
                                matrixList.get(2).setHidden(!matrixList.get(2).getHidden());
                            } catch (Error e) {
                                throw new IllegalArgumentException("Matrix does not exist");
                            }
                            reDraw();
                        }
                    }
                });
            }
        });
    }

    private void reDraw() {
        gridCanvas.getGraphicsContext2D().clearRect(0, 0, 800, 600);
        for (Matrix m : matrixList) {
            m.drawGrid();
        }
    }

    private void handleNewVector() {
        TextInputDialog dialog1 = new TextInputDialog("1,1");
        dialog1.setTitle("Ny vektor");
        dialog1.setHeaderText("Vektor v_1");
        dialog1.setContentText("Skriv v_1 som x,y:");
        Optional<String> res1 = dialog1.showAndWait();
        if (!res1.isPresent())
            return;

        Point2D v1 = parsePoint(res1.get());
        base.addVector(v1);
        if (newGrid != null) {
            newGrid.addVector(v1);
        }
        if (inverseMatrix != null) {
            for (Point2D v : base.getVectors()) {
                inverseMatrix.addVector(v);
            }
        }
        reDraw();
    }

    private void handleNewGrid() {
        TextInputDialog dialog1 = new TextInputDialog("1,0");
        dialog1.setTitle("Ny basisvektor");
        dialog1.setHeaderText("Basisvektor e_1");
        dialog1.setContentText("Skriv e_1 som x,y:");
        Optional<String> res1 = dialog1.showAndWait();
        if (!res1.isPresent())
            return;

        TextInputDialog dialog2 = new TextInputDialog("0,1");
        dialog2.setTitle("Ny basisvektor");
        dialog2.setHeaderText("Basisvektor e_2");
        dialog2.setContentText("Skriv e_2 som x,y:");
        Optional<String> res2 = dialog2.showAndWait();
        if (!res1.isPresent())
            return;

        Point2D e1 = parsePoint(res1.get());
        Point2D e2 = parsePoint(res2.get());

        newGrid = new Matrix(gridCanvas, e1, e2, Color.RED, Color.PINK);
        inverseMatrix = Calculator.findInverseMatrix(gridCanvas, newGrid);
        inverseMatrix.setHidden(true);
        for (Point2D v : base.getVectors()) {
            newGrid.addVector(v);
            inverseMatrix.addVector(v);
        }
        newGrid.drawGrid();
        matrixList.add(newGrid);
        matrixList.add(inverseMatrix);
        reDraw();
    }

    private Point2D parsePoint(String s) {
        try {
            String[] a = s.split(",");
            double x = Double.parseDouble(a[0].trim());
            double y = Double.parseDouble(a[1].trim());
            return new Point2D(x, y);
        } catch (Error e) {
            e.printStackTrace();
            System.out.println("Skriv på formatet (x, y) der x og y er Double.");
            return null;
        }
    }

    public Canvas getGridCanvas() {
        return gridCanvas;
    }
}
