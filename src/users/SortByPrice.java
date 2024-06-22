package users;

import database.Accommodation;
import database.Hotel;

import java.util.ArrayList;
import java.util.Comparator;

public class SortByPrice extends Sort implements SortInterface {
    private static boolean created = false;
    public SortByPrice(){
        if(!created){
            created = true;
            Sort.sorts.add("price");
        }
    }
    @Override
    public void sort(ArrayList<Accommodation> hotels){
        if(hotels==null || hotels.isEmpty()) return;
        hotels.sort(Comparator.comparingDouble(Accommodation::getLowPrice));
    }
}
