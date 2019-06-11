package Pogoda;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Pair;

public class AreaChartWeather extends AreaChart<Number, Number> {

    private ObservableList<Data<Number, Number>> verticalRangeMarkers;

    public AreaChartWeather(NumberAxis xAxsis, NumberAxis yAxsis) {
        super(xAxsis, yAxsis);
        verticalRangeMarkers = FXCollections.observableArrayList(data -> new Observable[] {data.XValueProperty()});
        verticalRangeMarkers = FXCollections.observableArrayList(data -> new Observable[] {data.YValueProperty()});
        verticalRangeMarkers.addListener((InvalidationListener)observable -> layoutPlotChildren());
    }

    @Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
        for (int index = 0; index < getDataSize(); index++) {
            final XYChart.Series<Number, Number> series = getData().get(index);
            final Group seriesGroup = (Group)series.getNode();
            final Path seriesLine = (Path)seriesGroup.getChildren().get(1);
            final Path fillPath = (Path)seriesGroup.getChildren().get(0);
            smooth(seriesLine.getElements(), fillPath.getElements());
        }
        for (Data<Number, Number> verticalRangeMarker : verticalRangeMarkers) {
            Rectangle rectangle = (Rectangle) verticalRangeMarker.getNode();
            rectangle.setX( getXAxis().getDisplayPosition(verticalRangeMarker.getXValue()) + 0.5);
            rectangle.setWidth( getXAxis().getDisplayPosition(verticalRangeMarker.getYValue()) - getXAxis().getDisplayPosition(verticalRangeMarker.getXValue()));
            rectangle.setY(0d);
            rectangle.setHeight(getBoundsInLocal().getHeight());
            rectangle.toBack();
        }
    }

    public void addVerticalRangeMarker(Data<Number, Number> marker) {
        if (verticalRangeMarkers.contains(marker)) return;
        Rectangle rectangle = new Rectangle(0,0,0,0);
        rectangle.setStroke(Color.TRANSPARENT);
        rectangle.setFill(Color.DARKOLIVEGREEN.deriveColor(1, 1, 1, 0.2));
        marker.setNode( rectangle);
        getPlotChildren().add(rectangle);
        verticalRangeMarkers.add(marker);
    }

    public int getDataSize() {
        final ObservableList<Series<Number, Number>> data = getData();
        return (data != null) ? data.size() : 0;
    }

    private static void smooth(ObservableList<PathElement> strokeElements,
                               ObservableList<PathElement> fillElements) {
        // as we do not have direct access to the data, first recreate
        // the list of all the data points we have
        final Point2D[] points = new Point2D[strokeElements.size()];
        for (int i = 0; i < strokeElements.size(); i++) {
            final PathElement element = strokeElements.get(i);
            if (element instanceof MoveTo) {
                final MoveTo move = (MoveTo) element;
                points[i] = new Point2D(move.getX(), move.getY());
            } else if (element instanceof LineTo) {
                final LineTo line = (LineTo) element;
                final double x = line.getX(), y = line.getY();
                points[i] = new Point2D(x, y);
            }
        }
        // next we need to know the zero Y value
        final double zeroY = ((MoveTo) fillElements.get(0)).getY();
        // now clear and rebuild elements
        strokeElements.clear();
        fillElements.clear();
        Pair<Point2D[], Point2D[]> controls = curveControlPoints(points);
        Point2D[] firstControls = controls.getKey();
        Point2D[] secondControls = controls.getValue();
        // start both paths
        strokeElements.add(new MoveTo(points[0].getX(), points[0].getY()));
        fillElements.add(new MoveTo(points[0].getX(), zeroY));
        fillElements.add(new LineTo(points[0].getX(), points[0].getY()));
        // add curves
        for (int i = 1; i < points.length; i++) {
            final int ci = i - 1;
            strokeElements.add(new CubicCurveTo(
                    firstControls[ci].getX(), firstControls[ci].getY(),
                    secondControls[ci].getX(), secondControls[ci].getY(),
                    points[i].getX(), points[i].getY()));
            fillElements.add(new CubicCurveTo(
                    firstControls[ci].getX(), firstControls[ci].getY(),
                    secondControls[ci].getX(), secondControls[ci].getY(),
                    points[i].getX(), points[i].getY()));
        }
        // end the paths
        fillElements.add(new LineTo(points[points.length - 1].getX(), zeroY));
        fillElements.add(new ClosePath());
    }

    /**
     * Calculate open-ended Bezier Spline Control Points.
     */
    public static Pair<Point2D[], Point2D[]> curveControlPoints(Point2D[] data) {
        Point2D[] firstControlPoints;
        Point2D[] secondControlPoints;
        int n = data.length - 1;
        if (n == 1) { // Special case: Bezier curve should be a straight line.
            firstControlPoints = new Point2D[1];
            // 3P1 = 2P0 + P3
            firstControlPoints[0] = new Point2D(
                    (2 * data[0].getX() + data[1].getX()) / 3,
                    (2 * data[0].getY() + data[1].getY()) / 3);

            secondControlPoints = new Point2D[1];
            // P2 = 2P1 â€“ P0
            secondControlPoints[0] = new Point2D(
                    2 * firstControlPoints[0].getX() - data[0].getX(),
                    2 * firstControlPoints[0].getY() - data[0].getY());
            return new Pair<Point2D[], Point2D[]>(firstControlPoints,
                    secondControlPoints);
        }

        // Calculate first Bezier control points
        // Right hand side vector
        double[] rhs = new double[n];

        // Set right hand side X values
        for (int i = 1; i < n - 1; ++i) {
            rhs[i] = 4 * data[i].getX() + 2 * data[i + 1].getX();
        }
        rhs[0] = data[0].getX() + 2 * data[1].getX();
        rhs[n - 1] = (8 * data[n - 1].getX() + data[n].getX()) / 2.0;
        // Get first control points X-values
        double[] x = GetFirstControlPoints(rhs);

        // Set right hand side Y values
        for (int i = 1; i < n - 1; ++i) {
            rhs[i] = 4 * data[i].getY() + 2 * data[i + 1].getY();
        }
        rhs[0] = data[0].getY() + 2 * data[1].getY();
        rhs[n - 1] = (8 * data[n - 1].getY() + data[n].getY()) / 2.0;
        // Get first control points Y-values
        double[] y = GetFirstControlPoints(rhs);

        // Fill output arrays.
        firstControlPoints = new Point2D[n];
        secondControlPoints = new Point2D[n];
        for (int i = 0; i < n; ++i) {
            // First control point
            firstControlPoints[i] = new Point2D(x[i], y[i]);
            // Second control point
            if (i < n - 1) {
                secondControlPoints[i] =
                        new Point2D(2 * data[i + 1].getX() - x[i + 1],
                                2 * data[i + 1].getY() - y[i + 1]);
            } else {
                secondControlPoints[i] =
                        new Point2D((data[n].getX() + x[n - 1]) / 2,
                                (data[n].getY() + y[n - 1]) / 2);
            }
        }
        return new Pair<Point2D[], Point2D[]>(firstControlPoints,
                secondControlPoints);
    }

    /**
     * Solves a tridiagonal system for one of coordinates (x or y) of first
     * Bezier control points.
     */
    private static double[] GetFirstControlPoints(double[] rhs) {
        int n = rhs.length;
        double[] x = new double[n]; // Solution vector.
        double[] tmp = new double[n]; // Temp workspace.
        double b = 2.0;
        x[0] = rhs[0] / b;
        for (int i = 1; i < n; i++) {// Decomposition and forward substitution.
            tmp[i] = 1 / b;
            b = (i < n - 1 ? 4.0 : 3.5) - tmp[i];
            x[i] = (rhs[i] - x[i - 1]) / b;
        }
        for (int i = 1; i < n; i++) {
            x[n - i - 1] -= tmp[n - i] * x[n - i]; // Backsubstitution.
        }
        return x;
    }
}
