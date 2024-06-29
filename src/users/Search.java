package users;

import database.*;
import exceptions.NoRoomsFoundException;
import exceptions.NullParamException;

import java.util.ArrayList;

import static main.Main.*;

public class Search {
    private City city;
    private Accommodation hotel;
    private int lowStars, people, rooms;
    private double lowPrice, highPrice;
    private ArrayList<String> roomAmenities;
    private ArrayList<String> hotelAmenities;
    private ArrayList<Accommodation> hotels;
    private ArrayList<RoomInterface> roomsList;
    private MyDate checkIn, checkOut;
    private boolean lateCheckOut;
    private String description;

    public Search(){
        city = null;
        lowStars = 1;
        lowPrice = 0;
        people = 1;
        rooms = 1;
        highPrice = Integer.MAX_VALUE;
        roomAmenities = new ArrayList<>();
        hotelAmenities = new ArrayList<>();
        hotels = new ArrayList<>();
        roomsList = new ArrayList<>();
    }
    public Search(Search o){
        if(o==null) return;
        city = o.getCity();
        lowStars = o.getLowStars();
        lowPrice = o.getLowPrice();
        people = o.getPeople();
        rooms = o.getRooms();
        highPrice = o.getHighPrice();
        roomAmenities = o.getRoomAmenities();
        hotelAmenities = o.getHotelAmenities();
        hotels = o.getHotels();
        roomsList = o.getRoomsList();
        checkIn = o.getCheckIn();
        checkOut = o.getCheckOut();
        lateCheckOut = o.getLateCheckOut();
        hotel = o.getHotel();
        description = o.description();
    }
    public Search(int stars, double low, double high, int p,
                  int r, MyDate in, MyDate out, boolean late, String d){
        city = null;
        lowStars = stars;
        lowPrice = low;
        people = p;
        rooms = r;
        highPrice = high;
        checkIn = in;
        checkOut = out;
        lateCheckOut = late;
        description = d;
        roomAmenities = new ArrayList<>();
        hotelAmenities = new ArrayList<>();
        hotels = new ArrayList<>();
        roomsList = new ArrayList<>();
    }
    public void update(Search o){
        if(o==null) return;
        city = o.getCity();
        lowStars = o.getLowStars();
        lowPrice = o.getLowPrice();
        people = o.getPeople();
        rooms = o.getRooms();
        highPrice = o.getHighPrice();
        roomAmenities = o.getRoomAmenities();
        hotelAmenities = o.getHotelAmenities();
        hotels = o.getHotels();
        roomsList = o.getRoomsList();
        checkIn = o.getCheckIn();
        checkOut = o.getCheckOut();
        lateCheckOut = o.getLateCheckOut();
        hotel = o.getHotel();
        description = o.description();
    }

    public void getRoomsFromHotels() throws NullParamException, NoRoomsFoundException {
        for (Accommodation hotel : hotels){
            this.roomsList.add(hotel.suggestRoomBundle(this));
        }
        if(DEBUG && PRINT_ALL) System.out.println(roomsList);
    }
    public void releaseRooms(){
        ArrayList<RoomInterface> roomsOnHold = roomsList;
        for(RoomInterface room : roomsOnHold){
            if(room.getClass().getSimpleName().equals(Room.class.getSimpleName())){
                room.setHold(false);
            }
            if(room.getClass().getSimpleName().equals(RoomBundle.class.getSimpleName())){
                room.setHold(false);
            }
        }
    }

    public void filter() throws NullParamException, NoRoomsFoundException {
        roomsList.clear();
        if(DEBUG) System.out.println("roomsList:\n"+roomsList);
        if(hotel!=null){
            hotels.clear();
            hotels.add(hotel);
        }
        else {
            for (Accommodation h : city.getHotels()) {
                if(DEBUG) System.out.println("checking "+h);
                if (isRelevantHotel(h)) {
                    if(DEBUG) System.out.println(h+" is relevant");
                    hotels.add(h);
                }else if(DEBUG) System.out.println(h+" is not relevant");
            }
            if(DEBUG) System.out.println(hotels);
        }
        getRoomsFromHotels();
    }
    public void sort(String parameter){
        if(parameter==null) return;
        SortInterface sort = new Sort();
        switch (parameter.toLowerCase()){
            case "stars" -> sort = new SortByStars();
            case "price" -> sort = new SortByPrice();
        }
        sort.sort(this.hotels);
    }

    public MyDate getCheckIn() {
        return checkIn;
    }
    public void setCheckIn(MyDate checkIn) {
        this.checkIn = checkIn;
    }
    public MyDate getCheckOut() {
        return checkOut;
    }
    public void setCheckOut(MyDate checkOut) {
        this.checkOut = checkOut;
    }
    public boolean getLateCheckOut() {
        return lateCheckOut;
    }
    public void setLateCheckOut(boolean lateCheckOut) {
        this.lateCheckOut = lateCheckOut;
    }
    public ArrayList<RoomInterface> getRoomsList() {
        return roomsList;
    }
    public ArrayList<Accommodation> getHotels() {
        return hotels;
    }
    public void clearHotels(){
        hotels.clear();
        hotel = null;
    }
    public City getCity() {
        return city;
    }
    public void setCity(City city) {
        this.city = city;
    }
    public int getLowStars() {
        return lowStars;
    }
    public void setLowStars(int lowStars) {
        if(lowStars<1) return;
        this.lowStars = lowStars;
    }
    public int getPeople() {
        return people;
    }
    public void setPeople(int people) {
        if(people<=0) return;
        this.people = people;
    }
    public int getRooms() {
        return rooms;
    }
    public void setRooms(int rooms) {
        if(rooms<=0) return;
        this.rooms = rooms;
    }
    public double getLowPrice() {
        return lowPrice;
    }
    public void setLowPrice(double lowPrice) {
        if(lowPrice<0 || lowPrice>highPrice) return;
        this.lowPrice = lowPrice;
    }
    public double getHighPrice() {
        return highPrice;
    }
    public void setHighPrice(double highPrice) {
        if(highPrice<lowPrice) return;
        this.highPrice = highPrice;
    }
    public ArrayList<String> getRoomAmenities() {
        return roomAmenities;
    }
    public void addRoomAmenity(String amenity) {
        this.roomAmenities.add(amenity);
    }
    public void removeRoomAmenity(String amenity) {
        this.roomAmenities.remove(amenity);
    }
    public void addHotelAmenity(String amenity) {
        this.hotelAmenities.add(amenity);
    }
    public void removeHotelAmenity(String name){
        hotelAmenities.removeIf(a -> a.equals(name));
    }
    public ArrayList<String> getHotelAmenities() {
        return hotelAmenities;
    }
    public Accommodation getHotel(){
        return this.hotel;
    }
    public void setHotel(Accommodation hotel){
        this.hotel = hotel;
    }
    private boolean hasAmenity(String a, Accommodation h){
        if(h==null || a==null) return false;
        ArrayList<HotelAmenity> options = h.getHotelAmenities();
        for(HotelAmenity hotelAmenity : options){
            if(a.equals(hotelAmenity.getName())){
                return true;
            }
        }
        return false;
    }
    private boolean isRelevantHotel(Accommodation h){
        if(h==null) return false;
        if(lowPrice<=h.getHighPrice() &&
                highPrice>=h.getLowPrice() &&
                lowStars<=h.getStars()){
            for(String a : this.hotelAmenities) {
                if (!hasAmenity(a, h)) {
                    if(DEBUG) System.out.println("missing "+a);
                    return false;
                }
            }
            try{
                if(DEBUG) System.out.println("getting room bundle");
                RoomInterface r = h.suggestRoomBundle(this);
                return !r.getRoomNumbers().isEmpty();
            }catch (NoRoomsFoundException e){
                if(DEBUG) System.out.println("no rooms found in "+h);
                return false;
            }catch (NullParamException e){
                if(DEBUG) System.out.println("null param passed");
                return false;
            }
        }
        if(DEBUG) System.out.println("returning false");
        return false;
    }
    public String toString(){
        StringBuilder ans = new StringBuilder("city: " + city
                +"\nstars: " + lowStars
                +"\nprice: " + lowPrice + " -> " + highPrice
                +"\ncheck in: " + checkIn + "\ncheck out: " + checkOut
                +"\nlate check out: "+lateCheckOut
                +"\n"+people+" guests in "+rooms+" rooms"
                +"\nroom amenities: ");
        for(String s : roomAmenities){
            ans.append(s).append(", ");
        }
        ans.append("\nhotel amenities: ");
        for(String s : hotelAmenities){
            ans.append(s).append(", ");
        }
        return ans.toString();
    }
    public String description(){
        return description;
    }
}
