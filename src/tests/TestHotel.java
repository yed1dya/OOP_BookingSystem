package tests;

import database.Breakfast;
import database.Hotel;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestHotel {
    @Test public void testHotel(){
        ArrayList<Integer> a = new ArrayList<>(), b = new ArrayList<>();
        a.add(1);
        b.add(2);
        Hotel hotel = new Hotel("h",4,0,5,
                10,1000,a,b);
        hotel.addHotelAmenity(new Breakfast());
        assertFalse(hotel.getHotelAmenities().isEmpty());
        hotel.removeHotelAmenity("Breakfast");
        assertTrue(hotel.getHotelAmenities().isEmpty());
    }
}
