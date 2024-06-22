package tests;

import database.MyDate;
import org.junit.Test;

import static functions.Functions.*;
import static org.junit.Assert.*;
public class TestFunctions {
    @Test public void testAllInts(){
        assertFalse(isAllInts(""));
        assertTrue(isAllInts("12345"));
        assertFalse(isAllInts("123 5"));
    }
    @Test public void testAllIntsOrHyphen(){
        assertFalse(isAllIntsOrHyphen(""));
        assertFalse(isAllIntsOrHyphen("12-a32"));
        assertFalse(isAllIntsOrHyphen("12- 32"));
        assertTrue(isAllIntsOrHyphen("1234-53"));
        assertTrue(isAllIntsOrHyphen("-23"));
    }
    @Test public void testDouble(){
        assertFalse(isDouble(""));
        assertTrue(isDouble("-3434.34345"));
        assertFalse(isDouble("2/2"));
    }
    @Test public void testEmail(){
        assertFalse(isValidEmail(""));
        assertFalse(isValidEmail("asdf"));
        assertTrue(isValidEmail("a@b.c"));
    }
    @Test public void testGender(){
        assertFalse(isValidGender("Male"));
        assertFalse(isValidGender(""));
        assertTrue(isValidGender("M"));
    }
    @Test public void testAge(){
        assertFalse(isValidAge("150"));
        assertFalse(isValidAge("10.0"));
        assertFalse(isValidAge(""));
        assertTrue(isValidAge("149"));
        assertTrue(isValidAge("0"));
        assertTrue(isValidAge("0000"));
    }
    @Test public void testName(){
        if(ALLOW_HYPHEN){
            assertTrue(isValidName("even-chen"));
        }else{
            assertFalse(isValidName("even-chen"));
        }
        assertTrue(isValidName("yedidya"));
        assertFalse(isValidName(""));
    }
    @Test public void testID(){
        assertTrue(isValidID("207404997"));
        if(ALLOW_SIMPLE_ID) {
            assertTrue(isValidID("1"));
        }else{
            assertFalse(isValidID("12345678"));
        }
        assertFalse(isValidID("123 56789"));
        assertFalse(isValidID("123.456789"));
        assertFalse(isValidID(""));
    }
    @Test public void testStars(){
        for(int i=1; i<=5; i++){
            String s = ""+i;
            assertTrue(isValidLowStars(s));
            assertFalse(isValidLowStars("-"+s));
        }
    }
    @Test public void testPrices(){
        assertTrue(isValidLowPrice("534.343"));
        assertFalse(isValidLowPrice("-1"));
        assertTrue(isValidHighPrice("323.556",323.556));
        assertFalse(isValidHighPrice("323.556",323.557));
    }
    @Test public void testBeds(){
        assertTrue(isValidPeople("1"));
        assertFalse(isValidPeople("0"));
        assertFalse(isValidPeople("-1"));
    }
    @Test public void testDate(){
        assertTrue(isValidDay("31"));
        assertTrue(isValidDay("1"));
        assertFalse(isValidDay("0"));
        assertFalse(isValidDay("-1"));
        assertFalse(isValidDay("32"));
        assertTrue(isValidMonth("12"));
        assertTrue(isValidMonth("1"));
        assertFalse(isValidMonth("0"));
        assertFalse(isValidMonth("-1"));
        assertFalse(isValidMonth("13"));
        assertTrue(isValidYear(""+(MAX_YEAR -1)));
        assertTrue(isValidYear("1"));
        assertFalse(isValidYear("0"));
        assertFalse(isValidYear("-1"));
        assertFalse(isValidYear(""+ MAX_YEAR));
    }
    @Test public void testCheckout(){
        MyDate d1 = MyDate.date(2022,5,17);
        assertFalse(isValidCheckOutDate(d1,d1.next()));
        assertFalse(isValidCheckOutDate(d1,d1));
        assertTrue(isValidCheckOutDate(d1.next(),d1));
    }
    @Test public void testBoolean(){
        assertTrue(isValidBoolean("Y"));
        assertFalse(isValidBoolean("true"));
    }
    @Test public void testTriple(){
        assertTrue(isValidTripleChoice("A"));
        assertFalse(isValidTripleChoice("false"));
    }
    @Test public void testPhone(){
        assertTrue(isValidPhone("0000000"));
        assertFalse(isValidPhone("12-3456-433"));
    }
    @Test public void testFraction(){
        assertTrue(getRandomFraction()<1);
    }
}
