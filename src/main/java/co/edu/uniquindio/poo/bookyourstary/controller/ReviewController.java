package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import co.edu.uniquindio.poo.bookyourstary.service.ReviewService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;

public class ReviewController {


    @FXML
    private Spinner<Integer> ratingSpinner;

    @FXML
    private TextArea commentField;

    private final ReviewService reviewService = ReviewService.getInstance();
    private Hosting selectedHosting;

    @FXML
    public void handleSubmitReview() {
        try {
            int rating = ratingSpinner.getValue();
            String comment = commentField.getText();

            // Validar que el comentario no esté vacío
            if (comment.isEmpty()) {
                showAlert("Error", "El comentario no puede estar vacío.", Alert.AlertType.ERROR);
                return;
            }

            // Crear la reseña
            reviewService.createReview("user123", "hosting456", rating, comment);
            showAlert("Éxito", "¡Tu reseña ha sido guardada exitosamente!", Alert.AlertType.INFORMATION);

            // Limpiar los campos
            ratingSpinner.getValueFactory().setValue(3);
            commentField.clear();

        } catch (Exception e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setSelectedHosting(Hosting hosting) {
        this.selectedHosting = hosting;
    }
}

