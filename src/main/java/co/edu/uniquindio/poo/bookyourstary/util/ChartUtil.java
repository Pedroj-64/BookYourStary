package co.edu.uniquindio.poo.bookyourstary.util;

import javafx.scene.chart.Chart;
import javafx.scene.chart.PieChart;
import javafx.scene.effect.DropShadow;

public class ChartUtil {

    public static void applyModernChartStyle(Chart chart) {
        chart.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0;");
        chart.getStyleClass().add("modern-chart");
    }

    public static void addChartEffects(Chart chart) {
        if (chart instanceof PieChart) {
            ((PieChart) chart).getData().forEach(data -> {
                data.getNode().setOnMouseEntered(e -> data.getNode().setEffect(new DropShadow()));
                data.getNode().setOnMouseExited(e -> data.getNode().setEffect(null));
            });
        }
    }
}