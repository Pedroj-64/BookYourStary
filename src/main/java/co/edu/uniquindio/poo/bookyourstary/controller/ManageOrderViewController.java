package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.viewController.ManagerOrderViewController;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para soporte adicional a ManagerOrderViewController
 * Esta clase ayuda a resolver problemas con la visualización y funcionalidad del formulario de reservas
 */
public class ManageOrderViewController {

    /**
     * Inicializar el controlador de reservas con datos frescos
     */
    public static void initializeOrderView(ManagerOrderViewController viewController) {
        // Configurar fecha de inicio con la fecha actual
        viewController.getDateInicio().setValue(LocalDate.now());
        
        // Configurar fecha de fin con el día siguiente por defecto
        viewController.getDateFin().setValue(LocalDate.now().plusDays(1));
        
        // Configurar campo de número de huéspedes
        viewController.getNumHuespedes().setText("1");
    }
    
    /**
     * Actualizar los precios basados en las fechas seleccionadas
     */
    public static List<Double> calculatePricesForDates(List<Hosting> hostings, LocalDate startDate, LocalDate endDate) {
        List<Double> prices = new ArrayList<>();
        
        if (startDate != null && endDate != null && !startDate.isAfter(endDate)) {
            long days = ChronoUnit.DAYS.between(startDate, endDate);
            
            for (Hosting hosting : hostings) {
                double price = hosting.getPricePerNight() * days;
                // Aplicar descuentos si corresponde
                if (days > 7) {
                    price = price * 0.9; // 10% descuento para estancias mayores a 7 días
                } else if (days > 3) {
                    price = price * 0.95; // 5% descuento para estancias de 4-7 días
                }
                prices.add(price);
            }
        }
        
        return prices;
    }
    
    /**
     * Mostrar un resumen de los descuentos aplicados
     */
    public static void showDiscountSummary(LocalDate startDate, LocalDate endDate, TextArea textArea) {
        if (startDate == null || endDate == null) {
            textArea.setText("Seleccione fechas de inicio y fin para ver descuentos aplicables.");
            return;
        }
        
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        StringBuilder summary = new StringBuilder();
        
        summary.append("Duración de estancia: ").append(days).append(" días\n\n");
        summary.append("Descuentos aplicables:\n");
        
        if (days <= 0) {
            summary.append("Las fechas son inválidas. La fecha de fin debe ser posterior a la fecha de inicio.");
        } else if (days > 7) {
            summary.append("✓ 10% de descuento para estancias mayores a 7 días\n");
            summary.append("El precio mostrado ya incluye este descuento.");
        } else if (days > 3) {
            summary.append("✓ 5% de descuento para estancias de 4-7 días\n");
            summary.append("El precio mostrado ya incluye este descuento.");
        } else {
            summary.append("No hay descuentos aplicables para estancias menores a 4 días.");
        }
        
        textArea.setText(summary.toString());
    }
}
