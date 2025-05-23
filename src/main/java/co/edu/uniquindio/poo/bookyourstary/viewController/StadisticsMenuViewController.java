package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.StadisticsMenuController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.util.ChartUtil;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class StadisticsMenuViewController {
    private StadisticsMenuController controller;

    @FXML
    private TabPane tabPane;
    @FXML
    private ComboBox<String> cbType;
    @FXML
    private PieChart occupancyPieChart;
    @FXML
    private BarChart<String, Number> earningsBarChart;
    @FXML
    private BarChart<String, Number> popularityBarChart;
    @FXML
    private Button btnExit;

    @FXML
    public void initialize() {
        try {
            // Obtener el controlador a través de MainController
            controller = new StadisticsMenuController();

            configureUIComponents();
            loadInitialData();
            setupEventHandlers();

            // Configurar el gráfico de popularidad
            setupPopularityChart();
        } catch (Exception e) {
            System.err.println("Error al inicializar StadisticsMenuViewController: " + e.getMessage());
            e.printStackTrace();
            MainController.showAlert(
                    "Error de inicialización",
                    "No se pudo inicializar la vista de estadísticas: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    private void configureUIComponents() {
        // Configurar ComboBox
        cbType.setItems(controller.getOpciones());
        cbType.setPromptText("Seleccione un tipo");

        // Configurar gráficos
        configureOccupancyChart();
        configureEarningsChart();
        configurePopularityChart();

        // Seleccionar el primer elemento después de configurar todo
        cbType.getSelectionModel().selectFirst();
    }

    private void configureOccupancyChart() {
        ChartUtil.applyModernChartStyle(occupancyPieChart);
        occupancyPieChart.setTitle("Ocupación por Tipo");
        occupancyPieChart.setLabelsVisible(true);
        occupancyPieChart.setLegendVisible(true);
    }

    private void configureEarningsChart() {
        ChartUtil.applyModernChartStyle(earningsBarChart);
        earningsBarChart.setTitle("Ganancias por Tipo");
        earningsBarChart.setAnimated(true);
        earningsBarChart.getXAxis().setLabel("Alojamiento");
        earningsBarChart.getYAxis().setLabel("Ganancias ($)");
    }

    private void configurePopularityChart() {
        ChartUtil.applyModernChartStyle(popularityBarChart);
        popularityBarChart.setTitle("Popularidad por Ciudad");
        popularityBarChart.setAnimated(true);
        popularityBarChart.getXAxis().setLabel("Ciudad");
        popularityBarChart.getYAxis().setLabel("Cantidad de Reservas");
    }

    private void loadInitialData() {
        refreshCharts();
    }

    private void setupEventHandlers() {
        cbType.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> refreshCharts());
        btnExit.setOnAction(event -> closeWindow());
    }

    private void refreshCharts() {
        String selectedType = cbType.getValue();

        if (selectedType == null) {
            return;
        }

        try {
            // Mostrar estado de carga
            setChartsLoading(true);

            // Limpiar datos anteriores
            clearAllCharts();

            // Actualizar gráfico de ocupación con validación
            var occupancyData = controller.getOccupancyData(selectedType);
            if (!occupancyData.isEmpty()) {
                occupancyPieChart.setData(occupancyData);
                ChartUtil.addChartEffects(occupancyPieChart);
            }

            // Actualizar gráfico de ganancias con validación
            var earningsSeries = controller.getEarningsSeries(selectedType);
            if (earningsSeries != null && !earningsSeries.getData().isEmpty()) {
                earningsBarChart.getData().add(earningsSeries);
                ChartUtil.addChartEffects(earningsBarChart);
            }

            // Actualizar gráfico de popularidad con validación
            var popularitySeries = controller.getPopularityByCitySeries();
            if (popularitySeries != null && !popularitySeries.getData().isEmpty()) {
                popularityBarChart.getData().add(popularitySeries);
                ChartUtil.addChartEffects(popularityBarChart);
            }

        } catch (Exception e) {
            // Manejar el error y mostrar un mensaje al usuario
            handleChartError(e);
        } finally {
            // Restaurar estado normal
            setChartsLoading(false);
        }
    }

    private void clearAllCharts() {
        occupancyPieChart.setData(null);
        earningsBarChart.getData().clear();
        popularityBarChart.getData().clear();
    }

    private void setChartsLoading(boolean loading) {
        double opacity = loading ? 0.5 : 1.0;
        occupancyPieChart.setOpacity(opacity);
        earningsBarChart.setOpacity(opacity);
        popularityBarChart.setOpacity(opacity);

        // Deshabilitar el combo box durante la carga
        cbType.setDisable(loading);
    }

    private void handleChartError(Exception e) {
        occupancyPieChart.setTitle("Error al cargar datos");
        earningsBarChart.setTitle("Error al cargar datos");
        popularityBarChart.setTitle("Error al cargar datos");

        System.err.println("Error al actualizar gráficas: " + e.getMessage());
        e.printStackTrace();

        MainController.showAlert(
                "Error al cargar datos",
                "No se pudieron cargar los datos de las gráficas: " + e.getMessage(),
                Alert.AlertType.ERROR);
    }

    private void closeWindow() {
        ((Stage) btnExit.getScene().getWindow()).close();
    }

    private void setupPopularityChart() {
        try {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Popularidad por Ciudad");

            var popularitySeries = controller.getPopularityByCitySeries();
            if (popularitySeries != null && !popularitySeries.getData().isEmpty()) {
                popularityBarChart.getData().clear();
                popularityBarChart.getData().add(popularitySeries);
                ChartUtil.addChartEffects(popularityBarChart);
            }

            // Configurar animación
            popularityBarChart.setAnimated(true);

        } catch (Exception e) {
            System.err.println("Error al configurar gráfico de popularidad: " + e.getMessage());
            e.printStackTrace();
            MainController.showAlert(
                    "Error al cargar datos de popularidad",
                    "No se pudieron cargar los datos del gráfico de popularidad: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }
}