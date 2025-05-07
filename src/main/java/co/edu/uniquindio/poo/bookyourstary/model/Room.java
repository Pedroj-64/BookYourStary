package co.edu.uniquindio.poo.bookyourstary.model;

import co.edu.uniquindio.poo.bookyourstary.model.enums.HostingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {

    private String RoomNumber;
    private double priceForNight;
    private int maxGuests;
    private String imageUrl;
    private String description;
    HostingType hostingType = HostingType.ROOM;

    public Room(String roomNumber, double priceForNight, int maxGuests, String imageUrl, String description) {
        this.RoomNumber = roomNumber;
        this.priceForNight = priceForNight;
        this.maxGuests = maxGuests;
        this.imageUrl = imageUrl;
        this.description = description;
    }

}
