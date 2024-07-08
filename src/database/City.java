package database;

import java.util.LinkedList;

public class City {
    private String name;
    private LinkedList<Hotel> hotels;

    public City(String name){
        this.name = name.toUpperCase();
        this.hotels = new LinkedList<>();
    }
    public String getName() {
        return name;
    }
    public LinkedList<Hotel> getHotels() {
        return hotels;
    }
    public void addHotel(Hotel hotel){
        this.hotels.add(hotel);
    }
    public String toString(){
        StringBuilder ans = new StringBuilder(name + ": ");
        for(Hotel h : hotels){
            ans.append(h.getName()).append(", ");
        }
        return ans.toString();
    }
}
