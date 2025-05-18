package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.StadisticsMenuController;
import co.edu.uniquindio.poo.bookyourstary.model.AccommodationType;
import co.edu.uniquindio.poo.bookyourstary.util.ChartUtil;
import javafx.collections.FXCollections;
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
    @FXML private ComboBox<AccommodationType> cbType;
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
        cbType.setItems(FXCollections.observableArrayList(AccommodationType.values()));
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
        AccommodationType selectedType = cbType.getValue();

        occupancyPieChart.setData(
                controller.getOccupancyData(selectedType)
        );

        earningsBarChart.getData().setAll(
                controller.getEarningsSeries(selectedType)
        );

        popularityBarChart.getData().setAll(
                controller.getPopularityByCitySeries()
        );

        ChartUtil.addChartEffects(occupancyPieChart);
        ChartUtil.addChartEffects(earningsBarChart);
        ChartUtil.addChartEffects(popularityBarChart);
    }

    private void closeWindow() {
        ((Stage) btnExit.getScene().getWindow()).close();
    }
}