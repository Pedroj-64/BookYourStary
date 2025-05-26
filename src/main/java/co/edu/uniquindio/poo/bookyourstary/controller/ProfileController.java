package co.edu.uniquindio.poo.bookyourstary.controller;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import co.edu.uniquindio.poo.bookyourstary.model.Booking;
import co.edu.uniquindio.poo.bookyourstary.model.Client;
import co.edu.uniquindio.poo.bookyourstary.model.enums.BookingState;

import java.util.List;

public class ProfileController {
    private static ProfileController instance;
    private final MainController mainController;

    private ProfileController() {
        this.mainController = MainController.getInstance();
    }

    public static ProfileController getInstance() {
        if (instance == null) {
            instance = new ProfileController();
        }
        return instance;
    }

    /**
     * Obtiene el cliente actual de la sesión
     * 
     * @return Cliente actual o null si no hay cliente en sesión
     */
    public Client getCurrentClient() {
        Object usuario = mainController.getSessionManager().getUsuarioActual();
        if (usuario instanceof Client) {
            return (Client) usuario;
        }
        return null;
    }

    /**
     * Actualiza los datos del cliente
     * 
     * @param client   Cliente a actualizar
     * @param newName  Nuevo nombre
     * @param newEmail Nuevo email
     * @param newPhone Nuevo teléfono
     * @throws Exception si algún campo está vacío
     */
    public void updateClientInfo(Client client, String newName, String newEmail, String newPhone) throws Exception {
        if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
            throw new Exception("Todos los campos son obligatorios");
        }

        client.setName(newName);
        client.setEmail(newEmail);
        client.setPhoneNumber(newPhone);

        mainController.getClientService().updateClient(client);
    }

    /**
     * Obtiene las reservas del cliente
     * 
     * @param clientId ID del cliente
     * @return Lista de reservas
     */
    public List<Booking> getClientBookings(String clientId) {
        return mainController.getBookingService().getBookingsByClient(clientId);
    }

    /**
     * Cancela una reserva
     * 
     * @param bookingId ID de la reserva
     * @throws Exception si ocurre un error al cancelar la reserva
     */
    public void cancelBooking(String bookingId) throws Exception {
        mainController.getBookingService().cancelBooking(bookingId);
    }

    /**
     * Verifica si una reserva puede ser cancelada
     * 
     * @param booking Reserva a verificar
     * @return true si la reserva puede ser cancelada
     */
    public boolean canCancelBooking(Booking booking) {
        return booking != null && booking.getBookingState() == BookingState.CONFIRMED;
    }

    /**
     * Construye el texto con los detalles de una reserva
     * 
     * @param booking Reserva
     * @return String con los detalles formateados
     */
    public String buildBookingDetails(Booking booking) {
        if (booking == null)
            return "";

        StringBuilder details = new StringBuilder();
        details.append("Detalles de la Reserva\n\n");
        details.append("ID: ").append(booking.getBookingId()).append("\n");
        details.append("Alojamiento: ").append(booking.getHosting().getName()).append("\n");
        details.append("Tipo: ").append(booking.getHosting().getClass().getSimpleName()).append("\n");
        details.append("Check-in: ").append(booking.getStartDate()).append("\n");
        details.append("Check-out: ").append(booking.getEndDate()).append("\n");
        details.append("Huéspedes: ").append(booking.getNumberOfGuests()).append("\n");
        details.append("Precio Total: $").append(String.format("%.2f", booking.getTotalPrice())).append("\n");
        details.append("Estado: ").append(booking.getBookingState()).append("\n");
        details.append("\nInformación del Alojamiento\n");
        details.append("Ciudad: ").append(booking.getHosting().getCity().getName()).append("\n");
        details.append("Descripción: ").append(booking.getHosting().getDescription());

        return details.toString();
    }
}
