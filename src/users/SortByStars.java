package users;

import database.Hotel;

import java.util.ArrayList;
import java.util.Comparator;

public class SortByStars extends Sort implements SortInterface {
    private static boolean created = false;
    public SortByStars(){
        if(!created){
            created = true;
            Sort.sorts.add("stars");
        }
    }
    @Override
    public void sort(ArrayList<Hotel> hotels){
        if (hotels==null) return;
        hotels.sort(Comparator.comparingInt(Hotel::getStars).reversed());
    }
}
