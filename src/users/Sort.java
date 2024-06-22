package users;

import database.Hotel;

import java.util.ArrayList;
import java.util.HashSet;

public class Sort implements SortInterface{
    protected static ArrayList<String> sorts = new ArrayList<>();
    @Override
    public void sort(ArrayList<Hotel> hotels) {}
    public static ArrayList<String> getSorts(){
        return sorts;
    }
}
