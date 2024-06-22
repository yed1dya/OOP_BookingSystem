package database;

import exceptions.*;
import users.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface Accommodation {
    RoomInterface suggestRoomBundle(Search search)
            throws NullParamException, NoRoomsFoundException;
    ArrayList<RoomInterface> suggestRooms(Search search, int limit);
    Reservation makeReservation(Guest guest, Search search, ArrayList<Integer> roomNumbers, RoomInterface room, String[] payInfo)
            throws PaymentErrorException, RoomTakenException;
    Reservation makeReservation(Guest guest, MyDate checkIn, MyDate checkOut,
                                       boolean late, ArrayList<Integer> roomNumbers, RoomInterface room, String[] payInfo)
            throws PaymentErrorException, RoomTakenException;
    Receipt deleteReservation(Reservation r, String[] payInfo)
            throws NoSuchReservationException, PaymentErrorException;
    ArrayList<Integer> getRefundOptions();
    void addRefundOptions(int p);
    ArrayList<Integer> getPaymentOptions();
    void addPaymentOptions(int p);
    void addRoom(Room room)
            throws RoomExistsException, NullParamException;
    void addRoom(int roomNumber, int beds, double price,
                        ArrayList<RoomAmenity> roomAmenities)
            throws RoomExistsException, NullParamException;
    void removeRoom(int roomNumber) throws NoSuchRoomException;
    void notifySubscribers(String message);
    void addAmenity(int roomNumber, RoomAmenity amenity);
    void removeAmenity(int roomNumber, String amenity);
    ArrayList<HotelAmenity> getHotelAmenities();
    void addHotelAmenity(HotelAmenity amenity);
    void removeHotelAmenity(String name);
    int getBottomFloor();
    void setBottomFloor(int bottomFloor);
    int getTopFloor();
    void setTopFloor(int topFloor);
    String getName();
    void setName(String name);
    int getStars();
    void setStars(int stars);
    HashMap<Integer, Room> getRooms();
    double getLowPrice();
    double getHighPrice();
    HashMap<MyDate,Reservation> getReservations();
    void changePrice(int roomNumber, double newPrice);
    void notify(String message);
}
