package users;

import database.Accommodation;
import database.Hotel;

import java.util.ArrayList;

public interface SortInterface {
    String name = "sort";
    void sort(ArrayList<Accommodation> hotels);
}
