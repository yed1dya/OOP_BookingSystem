package database;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class RoomAmenity {
    private static ArrayList<RoomAmenity> amenityList = new ArrayList<>();
    private String name;
    private double price;
    private HashMap<String, String> info;

    public RoomAmenity(String name){
        info = new HashMap<>();
        this.name = name;
        for (RoomAmenity a : amenityList){
            if(a.name.equals(this.getName())) return;
        }
        amenityList.add(this);
    }

    public static ArrayList<RoomAmenity> getAmenityList() {
        return amenityList;
    }
    public HashMap<String, String> getInfo() {
        return info;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String toString(){
        return name+", "+getPrice()+", "+getInfo();
    }
}
