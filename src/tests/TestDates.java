package tests;

import database.MyDate;
import org.junit.Test;

import static database.MyDate.date;
import static org.junit.Assert.*;
public class TestDates {
    MyDate d1 = date(2024,3,15);
    MyDate d2 = date(2024,3,15);
    MyDate d3 = date(2024,3,16);
    @Test public void testDateEquals(){
        assertTrue(d1.equals(d2));
        assertTrue(d3.equals(d2.next()));
    }
    @Test public void testGetters(){
        assertEquals(2024,d1.getYear());
        assertEquals(3,d1.getMonth());
        assertEquals(15,d1.getDay());
    }
}
