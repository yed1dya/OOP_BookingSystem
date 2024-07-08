package database;

import exceptions.NoSuchReservationException;
import exceptions.NullParamException;
import exceptions.RoomTakenException;
import users.Reservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class RoomBundle implements RoomInterface{
    private ArrayList<Room> rooms;
    private Hotel hotel;
    private double price;

    public RoomBundle(){
        this.rooms = new ArrayList<>();
    }
    public void addRoom(Room room) throws NullParamException {
        if(room==null){
            throw new NullParamException("can't add null room to bundle");
        }
        this.rooms.add(room);
        this.hotel = room.getHotel();
        this.price += room.getPrice();
    }
    public ArrayList<Room> getRooms(){
        return this.rooms;
    }
    public void reserve(MyDate checkIn, MyDate checkOut) throws RoomTakenException {
        for (Room room : rooms){
            room.reserve(checkIn,checkOut);
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
        if(rooms.isEmpty()) return ans;
        for(Room room : rooms){
            ans.addAll(room.getRoomNumbers());
        }
        return ans;
    }
    public void setRoomNumber(int roomNumber) {}
    public int getBeds() {
        int beds = 0;
        for(Room room : rooms){
            beds+=room.getBeds();
        }
        return beds;
    }
    public void setBeds(int beds) {}
    public LinkedList<MyDate> getDates() {
        LinkedList<MyDate> dates = new LinkedList<>();
        for(Room room : rooms){
            dates.addAll(room.getDates());
        }
        return dates;
    }
    public void addDate(MyDate date) {
        for(Room room : rooms){
            room.addDate(date);
        }
    }
    public void removeDate(MyDate date){
        for(Room room : rooms){
            try {
                room.removeDate(date);
            }catch (NoSuchReservationException ignored){}
        }
    }
    public ArrayList<RoomAmenity> getRoomAmenities() {
        ArrayList<RoomAmenity> amenities = new ArrayList<>();
        for(Room room : rooms){
            amenities.addAll(room.getRoomAmenities());
        }
        return amenities;
    }
    public void addAmenity(RoomAmenity amenity) {}
    public void removeAmenity(String name) {}
    public ArrayList<String> getRoomAmenitiesNames() {
        ArrayList<String> names = new ArrayList<>();
        for(Room room : rooms){
            names.addAll(room.getRoomAmenitiesNames());
        }
        return names;
    }
    public HashMap<MyDate, Reservation> getReservations() {
        return null;
    }
    public void addReservation(Reservation r) {}
    public Hotel getHotel() {
        return this.hotel;
    }
    public void setHold(boolean hold){
        for(Room room : rooms){
            room.setHold(hold);
        }
    }
    public String toString(){
        StringBuilder str = new StringBuilder("rooms:");
        for (Room room : rooms){
            str.append("\n").append(room);
        }
        str.append("\n");
        str.append(rooms.size()).append(" rooms total\n");
        str.append("price per night: ").append(price);
        return str.toString();
    }
}
