package tests;
import database.Hotel;
import database.MyDate;
import database.Room;
import exceptions.GuestExistsException;
import org.junit.Test;
import users.Guest;
import users.Receipt;
import users.Reservation;

import java.util.ArrayList;

import static org.junit.Assert.*;
public class TestReservation {
    @Test public void testReservation() throws GuestExistsException {
        MyDate d1 = MyDate.date(2024,3,5),
                d2 = MyDate.date(2024,3,6),
                d3 = MyDate.date(2024,3,8);
        Guest g1 = new Guest("first","even chen",
                "207404998","025902496","a@b.c");
        Hotel h1 = new Hotel("hotel",4,0,5,
                50,500,new ArrayList<>(),new ArrayList<>());
        Room r1 = new Room(1,2,100,new ArrayList<>(),h1);
        Receipt r2 = new Receipt(h1,200);
        Reservation r = new Reservation(d1,d1.next().next(),false,
                "",g1,r1,r2);
        assertEquals(d1,r.getCheckIn());
        assertEquals(d1.next().next(),r.getCheckOut());
        assertEquals(r1,r.getRooms());
        r.setCheckIn(r.getCheckIn().next());
        r.setCheckOut(r.getCheckOut().next());
        assertEquals(d3,r.getCheckOut());
        assertEquals(d2,r.getCheckIn());
        assertEquals("first",r.getGuest().getFirstName());
        assertFalse(r.isLateCheckOut());
        r.setLateCheckOut(true);
        assertTrue(r.isLateCheckOut());
        assertEquals("",r.getSpecialRequests());
        r.setSpecialRequests("request");
        assertEquals("request",r.getSpecialRequests());
    }
}
