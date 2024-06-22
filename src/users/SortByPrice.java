package users;

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
    public void sort(ArrayList<Hotel> hotels){
        if (hotels==null) return;
        hotels.sort(Comparator.comparingDouble(Hotel::getLowPrice));
    }
}
