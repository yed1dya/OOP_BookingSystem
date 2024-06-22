package database;

import exceptions.*;
import users.*;

import java.util.ArrayList;
import java.util.HashMap;

/*
the general interface for an accommodation - hotel, spa, airB&B, etc.
 */
public interface Accommodation {
    /**
     * @param search the search parameters
     * @return a relevant room bundle
     * @throws NullParamException if a null param was passes
     * @throws NoRoomsFoundException if no relevant rooms were found
     */
    RoomInterface suggestRoomBundle(Search search)
            throws NullParamException, NoRoomsFoundException;
    /**
     * helper method for the room bundle.
     * @param search the current search parameters.
     * @param limit how many rooms to suggest.
     * @return a list of rooms that match the search
     */
    ArrayList<RoomInterface> suggestRooms(Search search, int limit);
    /**
     * makes the payment and reserves the rooms
     * @param guest the guest
     * @param search the search parameters
     * @param roomNumbers rooms to reserve
     * @param bundle the specific room or bundle
     * @param payInfo payment info
     * @return a reservation
     * @throws PaymentErrorException if payment wasn't completed
     * @throws RoomTakenException if a room was already taken
     */
    Reservation makeReservation(Guest guest, Search search, ArrayList<Integer> roomNumbers, RoomInterface bundle, String[] payInfo)
            throws PaymentErrorException, RoomTakenException;
    /**
     * helper method for makeReservation; gets the checkIn, checkOut, lateCheckOut explicitly.
     * @param guest guest
     * @param checkIn checkIn
     * @param checkOut checkOut
     * @param late late checkout
     * @param roomNumbers rooms numbers
     * @param room rooms or bundle
     * @param payInfo payment info
     * @return a reservation
     * @throws PaymentErrorException if payment wasn't completed
     * @throws RoomTakenException if a room was already taken
     */
    Reservation makeReservation(Guest guest, MyDate checkIn, MyDate checkOut,
                                       boolean late, ArrayList<Integer> roomNumbers, RoomInterface room, String[] payInfo)
            throws PaymentErrorException, RoomTakenException;

    /**
     * @param r reservation to cancel
     * @param payInfo how to refund.
     * @return receipt
     * @throws NoSuchReservationException if reservation does not exist
     * @throws PaymentErrorException if refund failed
     */
    Receipt deleteReservation(Reservation r, String[] payInfo)
            throws NoSuchReservationException, PaymentErrorException;

    /**
     * @return refund options that the accommodation offers
     */
    ArrayList<Integer> getRefundOptions();

    /**
     * @param p refund option to add
     */
    void addRefundOptions(int p);

    /**
     * @return payment options that the accommodation offers
     */
    ArrayList<Integer> getPaymentOptions();

    /**
     * @param p payment option to add
     */
    void addPaymentOptions(int p);
    void addRoom(Room room)
            throws RoomExistsException, NullParamException;
    void addRoom(int roomNumber, int beds, double price,
                        ArrayList<RoomAmenity> roomAmenities)
            throws RoomExistsException, NullParamException;
    void removeRoom(int roomNumber) throws NoSuchRoomException;
    void notifySubscribers(String message);

    /**
     * @param roomNumber room that is getting the amenity
     * @param amenity amenity to add
     */
    void addAmenity(int roomNumber, RoomAmenity amenity);

    /**
     * @param roomNumber room that is losing the amenity
     * @param amenity  amenity to remove
     */
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

    /**
     * @param roomNumber room whose price is changing
     * @param newPrice new price
     */
    void changePrice(int roomNumber, double newPrice);
}
