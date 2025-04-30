package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Room;

public class RoomRepository {

    private final LinkedList<Room> rooms;

    public RoomRepository() {
        this.rooms = new LinkedList<>();
    }

    public void save(Room room) {
        rooms.add(room);
    }

    public Optional<Room> findById(String roomNumber) {
        return rooms.stream().filter(room -> room.getRoomNumber().equals(roomNumber)).findFirst();
    }

    public LinkedList<Room> findAll() {
        return new LinkedList<>(rooms);
    }

    public void delete(Room room) {
        rooms.remove(room);
    }

    public LinkedList<Room> findByMaxGuests(int maxGuests) {
        LinkedList<Room> avalibleRooms = new LinkedList<>();
        for (Room room : rooms) {
            if (room.getMaxGuests() >= maxGuests) {
                avalibleRooms.add(room);
            }
        }
        return avalibleRooms;
    }

}
