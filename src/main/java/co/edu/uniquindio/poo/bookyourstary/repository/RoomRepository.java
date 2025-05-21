package co.edu.uniquindio.poo.bookyourstary.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import co.edu.uniquindio.poo.bookyourstary.model.Room;

public class RoomRepository {

    private final List<Room> rooms;

    private static RoomRepository instance;

    private RoomRepository() {
        this.rooms = new LinkedList<>();
    }

    public static RoomRepository getInstance() {
        if (instance == null) {
            instance = new RoomRepository();
        }
        return instance;
    }

    public void save(Room room) {
        rooms.add(room);
    }

    public Optional<Room> findById(String roomNumber) {
        return rooms.stream().filter(room -> room.getRoomNumber().equals(roomNumber)).findFirst();
    }

    public List<Room> findAll() {
        return new LinkedList<>(rooms);
    }

    public void delete(Room room) {
        rooms.remove(room);
    }

    public List<Room> findByMaxGuests(int maxGuests) {
        LinkedList<Room> avalibleRooms = new LinkedList<>();
        for (Room room : rooms) {
            if (room.getMaxGuests() >= maxGuests) {
                avalibleRooms.add(room);
            }
        }
        return avalibleRooms;
    }

}
