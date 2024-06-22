package database;

import exceptions.NoSuchReservationException;
import exceptions.RoomTakenException;
import users.Reservation;

import java.util.ArrayList;
import java.util.HashMap;

public interface RoomInterface {
    /**
     * sets the room te be taken on the dates from check in to check out
     * @param checkIn check in date
     * @param checkOut check out date
     * @throws RoomTakenException if room is already taken
     */
    void reserve(MyDate checkIn, MyDate checkOut)
            throws RoomTakenException;
    double getPrice();
    void setPrice(double price);
    ArrayList<Integer> getRoomNumbers();
    void setRoomNumber(int roomNumber);
    int getBeds();
    void setBeds(int beds);
    ArrayList<MyDate> getDates();
    void addDate(MyDate date);
    void removeDate(MyDate date)
            throws NoSuchReservationException;
    ArrayList<RoomAmenity> getRoomAmenities();
    void addAmenity(RoomAmenity amenity);
    void removeAmenity(String name);
    ArrayList<String> getRoomAmenitiesNames();
    HashMap<MyDate, Reservation> getReservations();
    void addReservation(Reservation r);
    Hotel getHotel();
    void setHold(boolean hold);
}
