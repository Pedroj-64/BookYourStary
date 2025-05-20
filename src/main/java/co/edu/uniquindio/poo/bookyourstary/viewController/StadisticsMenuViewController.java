package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.StadisticsMenuController;
import co.edu.uniquindio.poo.bookyourstary.util.ChartUtil;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class StadisticsMenuViewController {

    private final StadisticsMenuController controller = new StadisticsMenuController();

    @FXML private TabPane tabPane;
    @FXML private ComboBox<String> cbType;
    @FXML private PieChart occupancyPieChart;
    @FXML private BarChart<String, Number> earningsBarChart;
    @FXML private BarChart<String, Number> popularityBarChart;
    @FXML private Button btnExit;

    @FXML
    public void initialize() {
        configureUIComponents();
        loadInitialData();
        setupEventHandlers();
    }

    private void configureUIComponents() {
        cbType.setItems(controller.getOpciones());
        cbType.getSelectionModel().selectFirst();

        ChartUtil.applyModernChartStyle(occupancyPieChart);
        ChartUtil.applyModernChartStyle(earningsBarChart);
        ChartUtil.applyModernChartStyle(popularityBarChart);
    }

    private void loadInitialData() {
        refreshCharts();
    }

    private void setupEventHandlers() {
        cbType.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> refreshCharts()
        );
        btnExit.setOnAction(event -> closeWindow());
    }

    private void refreshCharts() {
        String selectedType = cbType.getValue();

        if (selectedType != null) {
            // Actualizar gráfico de ocupación
            occupancyPieChart.setData(controller.getOccupancyData(selectedType));

            // Actualizar gráfico de ganancias
            earningsBarChart.getData().clear();
            earningsBarChart.getData().add(controller.getEarningsSeries(selectedType));

            // Actualizar gráfico de popularidad
            popularityBarChart.getData().clear();
            popularityBarChart.getData().add(controller.getPopularityByCitySeries());

            // Aplicar efectos
            ChartUtil.addChartEffects(occupancyPieChart);
            ChartUtil.addChartEffects(earningsBarChart);
            ChartUtil.addChartEffects(popularityBarChart);
        }
    }

    private void closeWindow() {
        ((Stage) btnExit.getScene().getWindow()).close();
    }
}