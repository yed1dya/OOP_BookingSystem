package tests;

import exceptions.GuestExistsException;
import org.junit.Test;
import users.Guest;

import static org.junit.Assert.*;
public class TestGuest {
    @Test public void testGettersAndSetters(){
        try {
            Guest g1 = new Guest("first","even chen",
                    "207404998","025902496","a@b.c");
            assertEquals("first",g1.getFirstName());
            assertEquals("even chen",g1.getLastName());
            assertEquals("207404998",g1.getID());
            assertEquals("025902496",g1.getPhone());
            assertEquals("a@b.c",g1.getEmail());
            g1.setEmail("d@e.f");
            g1.setID("207404997");
            g1.setFirstName("yedidya");
            g1.setLastName("even-chen");
            g1.setPhone("0585902496");
            assertEquals("yedidya",g1.getFirstName());
            assertEquals("even-chen",g1.getLastName());
            assertEquals("207404997",g1.getID());
            assertEquals("0585902496",g1.getPhone());
            assertEquals("d@e.f",g1.getEmail());
            assertTrue(g1.getReceipts().isEmpty());
        }catch (GuestExistsException e){
            System.out.println("guest  exists");
        }
    }
}
