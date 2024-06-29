package database;

import exceptions.GuestExistsException;
import exceptions.NullParamException;
import users.Guest;
import users.SortByPrice;
import users.SortByStars;

import java.util.*;

/*
Design Pattern - Singleton:
There only needs to be one database,
so the constructor is private.
The 'data()' method returns the database instance,
or creates a new one if there isn't one yet.
 */
public class Database {
    private HashMap<String, City> cities;
    private HashMap<String, Guest> guests;
    private MyDate today = MyDate.current();

    private static Database data = null;
    private Database(){
        new SortByStars();
        new SortByPrice();
        this.cities = new HashMap<>();
        this.guests = new HashMap<>();
    }
    public static Database data(){
        if(data==null){
            data = new Database();
        }
        data.today = MyDate.current();
        return data;
    }
    public HashMap<String,City> getCities() {
        return cities;
    }
    public ArrayList<String> cityNames(){
        ArrayList<String> ans = new ArrayList<>();
        for(Map.Entry<String, City> city : cities.entrySet()){
            ans.add(city.getKey());
        }
        return ans;
    }
    public HashMap<String, Guest> getGuests() {
        return guests;
    }
    public void addCity(City city){
        cities.put(city.getName(), city);
    }
    public boolean guestExists(String ID){
        return this.guests.containsKey(ID);
    }
    public void addGuest(Guest guest)
            throws GuestExistsException, NullParamException {
        if(guest==null){
            throw new NullParamException("null guest passed as parameter" +
                    "in Database.addGuest(Guest guest)");
        }
        if(guestExists(guest.getID())){
            throw new GuestExistsException("this ID already exists" +
                    "in Database.addGuest(Guest guest)");
        }
        this.guests.put(guest.getID(), guest);
    }
    public MyDate getToday(){
        return today;
    }
    public String toString(){
        StringBuilder ans = new StringBuilder();
        for(Map.Entry<String, City> c : cities.entrySet()){
            ans.append(c.getValue()).append("\n");
        }
        return ans.toString();
    }
}
