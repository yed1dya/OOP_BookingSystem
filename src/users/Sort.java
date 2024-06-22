package users;

import database.Accommodation;
import database.Hotel;

import java.util.ArrayList;
import java.util.HashSet;

public class Sort implements SortInterface{
    protected static ArrayList<String> sorts = new ArrayList<>();
    @Override
    public void sort(ArrayList<Accommodation> hotels) {}
    public static ArrayList<String> getSorts(){
        return sorts;
    }
}
