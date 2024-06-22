package users;

import database.Accommodation;

import java.util.ArrayList;

public class Sort implements SortInterface{
    protected static ArrayList<String> sorts = new ArrayList<>();
    @Override
    public void sort(ArrayList<Accommodation> hotels) {}
    public static ArrayList<String> getSorts(){
        return sorts;
    }
}
