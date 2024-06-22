package tests;

import org.junit.Test;
import users.Search;

import static org.junit.Assert.*;

public class TestSearch {
    @Test public void testSearch(){
        Search s1 = new Search();
        assertEquals(1,s1.getLowStars());
        s1.addHotelAmenity("Breakfast");
        assertFalse(s1.getHotelAmenities().isEmpty());
        s1.addHotelAmenity("Breakfast");
        s1.addHotelAmenity("Breakfast");
        s1.removeHotelAmenity("Breakfast");
        assertFalse(s1.getHotelAmenities().isEmpty());
        s1.removeHotelAmenity("Dinner");
        s1.removeHotelAmenity("breakfast");
        assertTrue(s1.getHotelAmenities().isEmpty());
        assertNull(s1.getHotel());
    }
}
