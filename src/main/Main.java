package main;

import database.*;
import exceptions.*;
import functions.Controller;
import users.Guest;

import java.util.*;

import static database.Hotel.count;
import static functions.Functions.*;
import static database.MyDate.*;

public class Main {
    private static Random rand = new Random();
    public static final boolean DEBUG = true, PRINT_ALL = false;
    private static boolean A = true, B = true, C = true;

    public static void main(String[] args) {
        try {
            demo();
        }catch (Exception e){
            System.out.println("one of a number of things has gone wrong!");
            System.out.println("https://youtu.be/V3amj814LfY?t=184");
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
            main(new String[]{});
        }
    }
    public static void demo() {
        Scanner in = new Scanner(System.in);
        System.out.println("""
                To choose an action,
                enter the letter or number in the list.
                Input is not case sensitive,
                unless otherwise specified.
                Enjoy!""");
        String s, message = "\nA) use demo database";
        if(DEBUG) message += "\nB) use debug database";
        message += "\nQ) quit";
        boolean go = true;
        while (go){
            System.out.println(message);
            s = in.next().toUpperCase();
            switch (s) {
                case "A" -> {
                    initDemoDatabase();
                    go = false;
                }
                case "B" -> {
                    initDebugDatabase();
                    go = false;
                }
                case "Q" -> {
                    return;
                }
            }
        }
        if(DEBUG) {
            System.out.println("print database? [Y]es [N]o");
            if (in.next().equalsIgnoreCase("Y")) {
                System.out.println("print full database? [Y]es\nelse - only hotels");
                printData(Database.data(), in.next().equalsIgnoreCase("Y"));
            }
        }
        Controller.menu(null);
    }
    public static void initDebugDatabase(){
        try {
            Database database = Database.data();
            City Jerusalem = new City("Jerusalem");
            City TelAviv = new City("Tel Aviv");
            database.addCity(Jerusalem);
            database.addCity(TelAviv);
            Hotel hilton = new Hotel("Hilton", 5, 0, 1, 0, 10,
                    new ArrayList<>(), new ArrayList<>());
            Hotel dan = new Hotel("dan", 3, 1, 1, 0, 10,
                    new ArrayList<>(), new ArrayList<>());
            Jerusalem.addHotel(hilton);
            TelAviv.addHotel(dan);
            hilton.addPaymentOptions(1);
            hilton.addPaymentOptions(2);
            hilton.addRefundOptions(2);
            dan.addPaymentOptions(3);
            ArrayList<RoomAmenity> a = new ArrayList<>();
            a.add(new RoomBalcony());
            ArrayList<RoomAmenity> b = new ArrayList<>();
            b.add(new RoomBalcony());
            b.get(0).setPrice(50);
            b.get(0).setName("room balcony");
            hilton.addRoom(1, 2, 2000, a);
            hilton.addRoom(2, 2, 200, a);
            hilton.addAmenity(1, new HotTub());
            hilton.addAmenity(2, new HotTub());
            hilton.removeAmenity(2, "Hot Tub");
            dan.addRoom(1, 2, 150, b);
            dan.addRoom(2, 2, 2000, b);
            dan.addRoom(3, 1, 100, new ArrayList<>());
            MyDate date = date(2024, 6, 25);
            for (int i = 0; i < 5; i++) {
                hilton.getRooms().get(1).addDate(date);
                dan.getRooms().get(1).addDate(date.next().next());
                date = date.next();
            }
            for (int i = 0; i < 5; i++) {
                hilton.getRooms().get(2).addDate(date);
                dan.getRooms().get(2).addDate(date.next().next());
                dan.getRooms().get(1).addDate(date.next().next().next().next().next());
                date = date.next();
            }
            dan.setStars(4);
            dan.setBottomFloor(0);
            dan.setTopFloor(2);
            dan.setName("Dan");
            dan.getRooms().get(3).setPrice(99);
            dan.removeRoom(2);
            try {
                dan.removeRoom(2);
            } catch (NoSuchRoomException e) {
                System.out.println("room already removed");
            }
            dan.getRooms().get(3).setRoomNumber(2);
            dan.getRooms().get(3).setBeds(2);
            Guest demoGuest = new Guest
                    ("Yedidya", "Evenchen", "207404997", "0585902496", "yevenchen@gmail.com");
            Database.data().addGuest(demoGuest);
            System.out.println(dan.getReservations());
            dan.changePrice(1,225);
            dan.notifySubscribers("notification");
            RoomBundle bundle = new RoomBundle();
            System.out.println(bundle.getRooms());
        }catch (NullParamException | GuestExistsException | RoomExistsException | NoSuchRoomException e){
            System.out.println("one of a number of things has gone wrong! again!");
            System.out.println("https://youtu.be/V3amj814LfY?t=184");
        }
    }
    public static void initDemoDatabase() {
        Database database = Database.data();
        City Jerusalem = new City("Jerusalem");
        City TelAviv = new City("Tel Aviv");
        City Eilat = new City("Eilat");
        City Tverya = new City("Tverya");
        database.addCity(Jerusalem);
        database.addCity(TelAviv);
        database.addCity(Eilat);
        database.addCity(Tverya);
        for(int i=0; i<10; i++){
            String name = "hotel"+i;
            for (Map.Entry<String, City> c : database.getCities().entrySet()){
                c.getValue().addHotel(randomHotel(name));
            }
        }
        for(Map.Entry<String, City> c : database.getCities().entrySet()){
            for(Hotel h : c.getValue().getHotels()){
                int stars = h.getStars();
                if(rand.nextBoolean() || stars>=3 || A) h.addHotelAmenity(new Breakfast());
                if(rand.nextBoolean() || stars>=4 || B) h.addHotelAmenity(new Lunch());
                if(rand.nextBoolean() || stars>=2 || C) h.addHotelAmenity(new Dinner());
                A = false; B = false; C = false;
                if(rand.nextBoolean() || stars >= 3){
                    h.addHotelAmenity(new SwimmingPool());
                }
                for(int i=h.getBottomFloor(); i<= h.getTopFloor(); i++){
                    for(int j=0; j<100; j++){
                        int num = i*1000 + j;
                        Room room = randomRoom(num,h.getLowPrice(),h.getHighPrice(),h);
                        try {
                            h.addRoom(room);
                        }catch (RoomExistsException e){
                            System.out.println("room "+room.getRoomNumbers()+
                                    " already exists in hotel "+h.getName());
                        }catch (NullParamException e){
                            System.out.println("null room cannot be added");
                        }
                        for(int k=0; k<10; k++){
                            int year = rand.nextInt(2024,2027);
                            int month = rand.nextInt(1,13);
                            int day = rand.nextInt(1,32);
                            MyDate date = date(year,month,day);
                            for(int a = 0; a<rand.nextInt(2,6); a++){
                                room.addDate(date);
                                date = date.next();
                            }
                        }
                    }
                }
            }
        }
        try {
            Guest demoGuest = new Guest
                    ("Yedidya", "Evenchen", "207404997","0585902496","yevenchen@gmail.com");
            Database.data().addGuest(demoGuest);
        }catch (NullParamException e){
            System.out.println("null guest cannot be added");
        }catch (GuestExistsException e){
            System.out.println("guest already exists in database");
        }
    }
    public static Hotel randomHotel(String name){
        if(name==null){
            name = "hotel"+count();
        }
        int stars = rand.nextInt(1,6);
        if(stars<=3 && rand.nextBoolean()){
            stars++;
        }
        if(stars==4 && rand.nextBoolean()) stars++;
        int top = rand.nextInt(5,20);
        int low = rand.nextInt(stars*50,stars*100);
        int high = rand.nextInt(stars*750,stars*1000);
        ArrayList<Integer> p = new ArrayList<>();
        for (Map.Entry<Integer, String> n : Hotel.getAllPaymentOptions().entrySet()){
            if(rand.nextBoolean()) p.add(n.getKey());
        }
        if(p.isEmpty()) p.add(CREDIT_CARD);
        return new Hotel(name,stars,0,top,low,high,p,p);
    }
    public static Room randomRoom(int number, double low, double high, Hotel hotel){
        ArrayList<RoomAmenity> a = new ArrayList<>();
        if(rand.nextBoolean()) a.add(new HotTub());
        if(rand.nextBoolean()) a.add(new RoomBalcony());
        int beds = rand.nextInt(1,5);
        double price = rand.nextInt((int) low,(int) high);
        price += getRandomFraction();
        return new Room(number,beds,price,a,hotel);
    }
    public static void printData(Database database, boolean full){
        Comparator<MyDate> comp = MyDate::compare;
        int cities=0, hotels=0, rooms=0;
        for(Map.Entry<String, City> c : database.getCities().entrySet()){
            cities++;
            System.out.println("----------\nCITY: "+c.getValue());
            for(Hotel h : c.getValue().getHotels()){
                hotels++;
                System.out.println("----------\nHOTEL: ");
                System.out.println(h.hotelData());
                if(!full) continue;
                for (Map.Entry<Integer, Room> r : h.getRooms().entrySet()){
                    rooms++;
                    System.out.println("\nROOM: "+r.getValue());
                    r.getValue().getDates().sort(comp.reversed());
                    int i=0;
                    for(MyDate date : r.getValue().getDates()){
                        i++;
                        System.out.print(date+", ");
                    }
                    System.out.print(i+" days total\n");
                }
            }
        }
        System.out.println("\n"+cities+" cities, "+hotels+" hotels, "+rooms+" rooms.");
    }
}
