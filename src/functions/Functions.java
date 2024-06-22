package functions;

import database.Database;
import database.MyDate;

import java.util.Random;
public abstract class Functions {
    public static final int BANK_TRANSFER = 1,
            CREDIT_CARD = 2, BIT_PAY = 3, MAX_YEAR = 3000;
    private static Random random = new Random();
    public static final boolean ALLOW_HYPHEN = true, ALLOW_SIMPLE_ID = false;

    public static boolean isAllInts(String input) {
        if(input==null) return false;
        if(input.isEmpty()) return false;
        for(int i=0; i<input.length(); i++){
            if((int)input.charAt(i)<48 || (int)input.charAt(i)>57){
                return false;
            }
        }
        return true;
    }
    public static boolean isAllIntsOrHyphen(String input) {
        if(input==null) return false;
        if(input.isEmpty()) return false;
        for(int i=0; i<input.length(); i++){
            if(((int)input.charAt(i)<48 || (int)input.charAt(i)>57)
                    && (int)input.charAt(i)!=45){
                return false;
            }
        }
        return true;
    }
    public static boolean isDouble(String input){
        if(input==null) return false;
        if(input.isEmpty()) return false;
        try {
            Double.parseDouble(input);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }
    public static boolean isValidEmail(String input) {
        if(input==null) return false;
        return input.contains("@");
    }
    public static boolean isValidGender(String input) {
        if(input==null) return false;
        return (input.equals("M") || input.equals("F") || input.equals("O"));
    }
    public static boolean isValidAge(String input) {
        if(input==null) return false;
        if(input.isEmpty()) return false;
        if(!isAllInts(input)) return false;
        int age = Integer.parseInt(input);
        return age>=0 && age<150;
    }
    public static boolean isValidName(String input) {
        if(input==null) return false;
        if(input.isEmpty()) return false;
        input = input.toUpperCase();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if(ALLOW_HYPHEN) alphabet+="-";
        for(int i=0; i<input.length(); i++){
            if(alphabet.indexOf(input.charAt(i))==-1){
                return false;
            }
        }
        return true;
    }
    public static boolean isValidID(String input) {
        if(input==null) return false;
        if(input.isEmpty()) return false;
        return (input.length()==9 && isAllInts(input)) ||
                (ALLOW_SIMPLE_ID && input.length()==1 && isAllInts(input));
    }
    public static boolean isValidLowStars(String input){
        if(!isAllInts(input)) return false;
        int num = Integer.parseInt(input);
        return num>0 && num<6;
    }
    public static boolean isValidLowPrice(String input){
        if(!isDouble(input)) return false;
        return Double.parseDouble(input)>=0;
    }
    public static boolean isValidHighPrice(String input, double lowPrice){
        if(!isDouble(input)) return false;
        return Double.parseDouble(input)>=lowPrice;
    }
    public static boolean isValidPeople(String input){
        if(!isAllInts(input)) return false;
        return Integer.parseInt(input)>0;
    }
    public static boolean isValidRooms(String input){
        if(!isAllInts(input)) return false;
        return Integer.parseInt(input)>0;
    }
    public static boolean isValidDay(String input){
        if(!isAllInts(input)) return false;
        int num = Integer.parseInt(input);
        return num>0 && num<32;
    }
    public static boolean isValidMonth(String input){
        if(!isAllInts(input)) return false;
        int num = Integer.parseInt(input);
        return num>0 && num<13;
    }
    public static boolean isValidYear(String input){
        if(!isAllInts(input)) return false;
        int num = Integer.parseInt(input);
        return num>0 && num<100;
    }
    public static boolean isValidCheckInDate(MyDate checkIn){
        return Database.data().getToday().compare(checkIn) >= 0;
    }
    public static boolean isValidCheckOutDate(MyDate checkOut, MyDate checkIn){
        return checkIn.compare(checkOut) > 0;
    }
    public static boolean isValidBoolean(String input){
        return input!=null && (input.equals("Y") || input.equals("N"));
    }
    public static boolean isValidTripleChoice(String input){
        return input!=null &&
                (input.equals("Y") || input.equals("N") || input.equals("A"));
    }
    public static boolean isValidPhone(String input){
        return isAllInts(input) && input.length()>=7;
    }
    public static double getRandomFraction(){
        double[] d = {0,0.25,0.5};
        int c = random.nextInt(0,d.length);
        return d[c];
    }
}
