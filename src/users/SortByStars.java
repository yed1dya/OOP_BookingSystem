package users;

import database.Accommodation;

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
    public void sort(ArrayList<Accommodation> hotels){
        if(hotels==null || hotels.isEmpty()) return;
        hotels.sort(Comparator.comparingInt(Accommodation::getStars).reversed());
    }
}
