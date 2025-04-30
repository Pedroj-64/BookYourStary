package co.edu.uniquindio.poo.bookyourstary.service;

import co.edu.uniquindio.poo.bookyourstary.model.Room;
import co.edu.uniquindio.poo.bookyourstary.repository.RoomRepository;

import java.util.LinkedList;
import java.util.Optional;

public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void saveRoom(String roomNumber, double priceForNight, int maxGuests, String imageUrl, String description) {
        Room room = new Room(roomNumber, priceForNight, maxGuests, imageUrl, description);
        roomRepository.save(room);
    }

    public Optional<Room> findRoomById(String roomNumber) {
        return roomRepository.findById(roomNumber);
    }

    public LinkedList<Room> findAllRooms() {
        return roomRepository.findAll();
    }

    public void deleteRoom(String roomNumber) {
        Room room = findRoomById(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la habitación con ese número"));
        roomRepository.delete(room);
    }

    public LinkedList<Room> findAvailableRooms(int maxGuests) {
        return roomRepository.findByMaxGuests(maxGuests);
    }

    public double calculateTotalPrice(Room room, int numberOfNights) {
        return room.getPriceForNight() * numberOfNights;
    }
}
