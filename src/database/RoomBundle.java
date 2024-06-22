package database;

import exceptions.NoSuchReservationException;
import exceptions.NullParamException;
import exceptions.RoomTakenException;
import users.Reservation;

import java.util.ArrayList;
import java.util.HashMap;

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

    @Override
    public void reserve(MyDate checkIn, MyDate checkOut) throws RoomTakenException {
        for (Room room : rooms){
            room.reserve(checkIn,checkOut);
        }
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public ArrayList<Integer> getRoomNumbers() {
        ArrayList<Integer> ans = new ArrayList<>();
        if(rooms.isEmpty()) return ans;
        for(Room room : rooms){
            ans.addAll(room.getRoomNumbers());
        }
        return ans;
    }

    @Override
    public void setRoomNumber(int roomNumber) {}

    @Override
    public int getBeds() {
        int beds = 0;
        for(Room room : rooms){
            beds+=room.getBeds();
        }
        return beds;
    }

    @Override
    public void setBeds(int beds) {}

    @Override
    public ArrayList<MyDate> getDates() {
        ArrayList<MyDate> dates = new ArrayList<>();
        for(Room room : rooms){
            dates.addAll(room.getDates());
        }
        return dates;
    }

    @Override
    public void addDate(MyDate date) {
        for(Room room : rooms){
            room.addDate(date);
        }
    }

    @Override
    public void removeDate(MyDate date){
        for(Room room : rooms){
            try {
                room.removeDate(date);
            }catch (NoSuchReservationException ignored){}
        }
    }

    @Override
    public ArrayList<RoomAmenity> getRoomAmenities() {
        ArrayList<RoomAmenity> amenities = new ArrayList<>();
        for(Room room : rooms){
            amenities.addAll(room.getRoomAmenities());
        }
        return amenities;
    }

    @Override
    public void addAmenity(RoomAmenity amenity) {}

    @Override
    public void removeAmenity(String name) {}

    @Override
    public ArrayList<String> getRoomAmenitiesNames() {
        ArrayList<String> names = new ArrayList<>();
        for(Room room : rooms){
            names.addAll(room.getRoomAmenitiesNames());
        }
        return names;
    }

    @Override
    public HashMap<MyDate, Reservation> getReservations() {
        return null;
    }

    @Override
    public void addReservation(Reservation r) {}

    public Hotel getHotel() {
        return this.hotel;
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

    public void setHold(boolean hold){
        for(Room room : rooms){
            room.setHold(hold);
        }
    }
}
