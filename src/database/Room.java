package database;

import exceptions.NoSuchReservationException;
import exceptions.RoomTakenException;
import users.Reservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Room implements RoomInterface {
    private boolean hold;
    private int roomNumber;
    private int beds;
    private double price;
    private Hotel hotel;
    private LinkedList<MyDate> dates;
    private ArrayList<RoomAmenity> roomAmenities;
    private HashMap<MyDate, Reservation> reservations;

    public Room(int roomNumber, int beds, double price,
                ArrayList<RoomAmenity> roomAmenities, Hotel hotel){
        this.roomNumber = roomNumber;
        this.beds = beds;
        this.price = price;
        this.roomAmenities = new ArrayList<>();
        this.roomAmenities.addAll(roomAmenities);
        this.dates = new LinkedList<>();
        this.reservations = new HashMap<>();
        this.hotel = hotel;
        hold = false;
    }
    public void reserve(MyDate checkIn, MyDate checkOut)
            throws RoomTakenException {
        MyDate date = checkIn;
        while(!date.equals(checkOut)){
            if(dates.contains(date)){
                throw new RoomTakenException("room already taken on "+date);
            }
            this.dates.add(date);
            date = date.next();
        }
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public ArrayList<Integer> getRoomNumbers() {
        ArrayList<Integer> ans = new ArrayList<>();
        ans.add(this.roomNumber);
        return ans;
    }
    public int getRoomNumber(){
        return this.roomNumber;
    }
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public int getBeds() {
        return beds;
    }
    public void setBeds(int beds) {
        this.beds = beds;
    }
    public LinkedList<MyDate> getDates() {
        return dates;
    }
    public void addDate(MyDate date) {
        if(!this.dates.contains(date)) {
            this.dates.add(date);
        }
    }
    public void removeDate(MyDate date)
            throws NoSuchReservationException {
        if(!this.dates.remove(date))
            throw new NoSuchReservationException("this date was already free");
    }
    public ArrayList<RoomAmenity> getRoomAmenities() {
        return roomAmenities;
    }
    public void addAmenity(RoomAmenity amenity){
        this.roomAmenities.add(amenity);
    }
    public void removeAmenity(String name){
        roomAmenities.removeIf(a -> a.getName().equals(name));
    }
    public ArrayList<String> getRoomAmenitiesNames() {
        ArrayList<String> ans = new ArrayList<>();
        for (RoomAmenity a : getRoomAmenities()){
            ans.add(a.getName());
        }
        return ans;
    }
    public HashMap<MyDate,Reservation> getReservations(){
        return this.reservations;
    }
    public void addReservation(Reservation r){
        this.reservations.put(r.getCheckIn(),r);
    }
    public Hotel getHotel() {
        return hotel;
    }
    public boolean isOnHold() {
        return hold;
    }
    public void setHold(boolean hold){
        this.hold = hold;
    }
    public String toString(){
        StringBuilder str = new StringBuilder("room number "+roomNumber+", price per night: "+price+", ");
        if(beds==1) str.append("1 bed");
        else str.append(beds).append(" beds");
        for(RoomAmenity a : roomAmenities){
            str.append(", ").append(a.getName());
        }
        return str.toString();
    }
}
