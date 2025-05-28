package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.StadisticsMenuController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.util.ChartUtil;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
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

            // Verificar que los componentes FXML se hayan inyectado correctamente
            if (cbType == null) {
                throw new IllegalStateException("Error: cbType no se inicializó correctamente desde FXML");
            }
            if (occupancyPieChart == null) {
                throw new IllegalStateException("Error: occupancyPieChart no se inicializó correctamente desde FXML");
            }
            if (earningsBarChart == null) {
                throw new IllegalStateException("Error: earningsBarChart no se inicializó correctamente desde FXML");
            }
            if (popularityBarChart == null) {
                throw new IllegalStateException("Error: popularityBarChart no se inicializó correctamente desde FXML");
            }

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
        try {
            // Configurar ComboBox
            cbType.setItems(controller.getOpciones());
            cbType.setPromptText("Seleccione un tipo");

            // Configurar gráficos
            configureOccupancyChart();
            configureEarningsChart();
            configurePopularityChart();

            // Seleccionar el primer elemento después de configurar todo
            cbType.getSelectionModel().selectFirst();
        } catch (Exception e) {
            System.err.println("Error al configurar componentes de UI: " + e.getMessage());
            e.printStackTrace();
            MainController.showAlert(
                    "Error de configuración",
                    "No se pudieron configurar los componentes de la interfaz: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
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
        btnExit.setOnAction(event -> handleExitAction()); // Changed to handleExitAction
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

    // Modified to correctly handle window closing after navigation
    private void handleExitAction() {
        Stage currentStage = null;
        try {
            currentStage = (Stage) btnExit.getScene().getWindow();
        } catch (Exception e) {
            System.err.println("Critical error: Could not get current stage from btnExit: " + e.getMessage());
            e.printStackTrace();
            MainController.showAlert(
                    "Error de Interfaz",
                    "No se pudo obtener la referencia de la ventana actual. La navegación aún se intentará.",
                    Alert.AlertType.WARNING);
        }

        final Stage stageToClose = currentStage; // Final variable for use in lambda

        try {
            MainController.loadScene("MenuAdmin", 800, 600); // Navigate to Admin Menu
            
            // Close the original statistics window *after* the new scene is loaded
            if (stageToClose != null) {
                javafx.application.Platform.runLater(() -> {
                    try {
                        stageToClose.close();
                    } catch (Exception e) {
                        System.err.println("Error closing statistics window via Platform.runLater: " + e.getMessage());
                    }
                });
            } else {
                 System.err.println("Statistics window could not be scheduled for closing as its reference was not obtained.");
            }
        } catch (Exception e) {
            System.err.println("Error during navigation to MenuAdmin: " + e.getMessage());
            e.printStackTrace();
            MainController.showAlert(
                    "Error de Operación",
                    "Ocurrió un error al intentar volver al menú de administrador: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            
            // Fallback: if navigation failed but we have the stage, and it's still showing, try to close it.
            if (stageToClose != null && stageToClose.isShowing()) {
                try {
                    stageToClose.close();
                } catch (Exception closeEx) {
                    System.err.println("Error attempting to close stageToClose in fallback catch block: " + closeEx.getMessage());
                }
            }
        }
    }

    private void setupPopularityChart() {
        ChartUtil.applyModernChartStyle(popularityBarChart);
        popularityBarChart.setTitle("Popularidad por Ciudad");
        popularityBarChart.setAnimated(true);
        popularityBarChart.getXAxis().setLabel("Ciudad");
        popularityBarChart.getYAxis().setLabel("Cantidad de Reservas");

        // Cargar datos de popularidad
        try {
            var popularitySeries = controller.getPopularityByCitySeries();
            if (popularitySeries != null && !popularitySeries.getData().isEmpty()) {
                popularityBarChart.getData().add(popularitySeries);
                ChartUtil.addChartEffects(popularityBarChart);
            }
        } catch (Exception e) {
            handleChartError(e);
        }
    }

    @FXML
    public void salir() {
        MainController.loadScene("MenuAdmin", 600, 900);
    }
}