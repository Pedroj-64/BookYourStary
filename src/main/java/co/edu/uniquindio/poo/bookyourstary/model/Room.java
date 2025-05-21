package co.edu.uniquindio.poo.bookyourstary.model;

import java.io.Serializable;
import co.edu.uniquindio.poo.bookyourstary.model.enums.HostingType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que representa una habitación de hotel en el sistema.
 * Se añade NoArgsConstructor para compatibilidad con la serialización XML.
 */
@Getter
@Setter
@NoArgsConstructor
public class Room implements Serializable {

    private String RoomNumber;
    private double priceForNight;
    private int maxGuests;
    private String imageUrl;
    private String description;
    private HostingType hostingType = HostingType.ROOM;

    public Room(String roomNumber, double priceForNight, int maxGuests, String imageUrl, String description) {
        this.RoomNumber = roomNumber;
        this.priceForNight = priceForNight;
        this.maxGuests = maxGuests;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
