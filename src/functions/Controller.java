package functions;

import database.*;
import exceptions.*;
import org.jetbrains.annotations.Nullable;
import users.*;

import java.util.*;

import static functions.Functions.*;
import static main.Main.*;

/*
The Controller acts as the UI.
All input and output goes through the Controller.
 */
public abstract class Controller {
    static Scanner in = new Scanner(System.in);
    private static Database data = Database.data();
    private static ArrayList<Search> demoSearches = new ArrayList<>();

    // Main menu:
    public static void menu(Guest guest) {
        createDemoSearches();
        boolean go = true;
        while (guest==null) {
            guest = chooseGuest();
            if (guest==null){
                System.out.println("A) log in\nQ) quit");
                String input = in.next().toUpperCase();
                if(input.equalsIgnoreCase("Q")) return;
            }
        }
        while (go){
            if(guest==null){
                guest = chooseGuest();
            }
            switch (chooseAction()){
                case "Q" -> go=false;
                case "A" -> showReservations(guest);
                case "B" -> searchAndReserve(guest);
                case "C" -> cancelReservation(guest);
                case "D" -> editClientInfo(guest);
                case "E" -> guest = chooseGuest();
            }
        }
    }
    private static String chooseAction(){
        ArrayList<String> options = new ArrayList<>();
        options.add("A");
        options.add("B");
        options.add("C");
        options.add("D");
        options.add("E");
        options.add("Q");
        String input = "Z";
        while(!options.contains(input)) {
            if(input.equalsIgnoreCase("print")){
                printData(data,false);
            }
            if(input.equalsIgnoreCase("full")){
                printData(data,true);
            }
            System.out.println("""
                    
                    choose an action:
                    A) see my reservations
                    B) create a new reservation
                    C) cancel a reservation
                    D) change user info
                    E) sign out
                    Q) quit""");
            input = in.next().toUpperCase();
        }
        return input;
    }
    private static void editClientInfo(User client) {
        if(client==null) return;
        System.out.println(client);
        ArrayList<String> fields = new ArrayList<>();
        fields.add("first name");
        fields.add("last name");
        fields.add("ID");
        fields.add("phone number");
        fields.add("email");
        String input = "Z";
        System.out.println("what info would you like to change?");
        for(int i=0; i<fields.size(); i++){
            System.out.println((i+1)+") "+fields.get(i));
        }
        while(!isAllInts(input) ||
                (isAllInts(input) &&
                        (Integer.parseInt(input)<1 ||
                                Integer.parseInt(input)-1>=fields.size()))){
            System.out.print("enter number: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")) return;
        }
        switch (fields.get(Integer.parseInt(input)-1)){
            case "first name"-> System.out.println(client.setFirstName(getName()));
            case "last name"-> System.out.println(client.setLastName(getName()));
            case "ID"-> System.out.println(client.setID(getID()));
            case "phone number"-> System.out.println(client.setPhone(getPhoneNumber()));
            case "email"-> System.out.println(client.setEmail(getEmail()));
        }
    }
    // Methods to get client info from user:
    private static @Nullable String getName(){
        String input = "1";
        System.out.println("enter '_Q' to cancel");
        while (!isValidName(input)){
            System.out.print("enter name: ");
            input = in.next();
            if(input.equalsIgnoreCase("_Q")) return null;
        }
        return input;
    }
    private static @Nullable String getPhoneNumber(){
        String input = "Z";
        System.out.println("enter 'Q' to cancel");
        while(!isValidPhone(input)){
            System.out.print("enter phone number: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")) return null;
        }
        return input;
    }
    private static @Nullable String getEmail(){
        String input = "";
        System.out.println("enter '_Q' to cancel");
        while(!isValidEmail(input)){
            System.out.print("enter eMail: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("_Q")) return null;
        }
        return input;
    }
    private static @Nullable String getID(){
        String input = "Z";
        System.out.println("enter 'Q' to cancel");
        while(!isValidID(input) || data.guestExists(input)){
            if(data.guestExists(input)){
                System.out.println("this ID is already in database");
            }
            System.out.print("enter ID number: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")) return null;
        }
        return input;
    }

    private static void showReservations(Guest guest) {
        if(guest==null){
            System.out.println("guest is null");
            return;
        }
        HashMap<Integer, Reservation> reservations = guest.getReservations();
        if(reservations.isEmpty()){
            System.out.println("no reservations");
        }
        for(Map.Entry<Integer, Reservation> r : reservations.entrySet()){
            System.out.println(r+"\n");
        }
    }
    private static Guest chooseGuest(){
        ArrayList<String> options = new ArrayList<>();
        options.add("A");
        options.add("B");
        options.add("C");
        options.add("Q");
        String input = "Z";
        while(!options.contains(input)){
            System.out.println("""
                
                A) Create new client
                B) Choose client
                C) Use demo client
                Q) Quit""");
            input = in.next().toUpperCase();
        }
        Guest guest = null;
        if(input.equalsIgnoreCase("A")){
            guest = createGuestFromUserInput();
            if(guest==null){
                System.out.println("client could not be created");
            }
            try {
                data.addGuest(guest);
            }catch (GuestExistsException e){
                System.out.println("guest already exists");
            }catch (NullParamException e){
                System.out.println("null parameter passed. try again");
            }
        }
        if(input.equalsIgnoreCase("B")){
            guest = chooseGuestFromList();
        }
        if(input.equalsIgnoreCase("C")){
            try {
                Guest demoGuest = new Guest
                        ("Yedidya", "Evenchen", "207404997","0585902496","yevenchen@gmail.com");
                Database.data().addGuest(demoGuest);
                guest = demoGuest;
            }catch (NullParamException e){
                System.out.println("null guest cannot be added");
            }catch (GuestExistsException e){
                guest = data.getGuests().get("207404997");
            }
        }
        if(guest!=null){
            System.out.println("Hello, "+guest.getFirstName());
            ArrayList <String> messages = guest.getReminders();
            int size = messages.size();
            if(size!=0){
                if(size==1){
                    System.out.println("You have 1 new reminder:");
                }
                else {
                    System.out.println("You have "+size+" new reminders:");
                }
                int count = 1;
                for(String message : guest.getReminders()){
                    System.out.println(count+") "+message);
                    count++;
                }
            }
        }
        return guest;
    }
    private static @Nullable Guest createGuestFromUserInput(){
        String ID = getID();
        if(ID==null) return null;
        System.out.println("First name:");
        String firstName = getName();
        if(firstName==null) return null;
        System.out.println("Last name:");
        String lastName = getName();
        if(lastName==null) return null;
        String email = getEmail();
        if(email==null) return null;
        String phone = getPhoneNumber();
        if(phone==null) return null;
        try {
            return new Guest(firstName, lastName, ID, phone, email);
        }catch (GuestExistsException e){
            System.out.println("guest exists");
            return null;
        }
    }
    private static @Nullable Guest chooseGuestFromList(){
        String input = "Z";
        ArrayList<String> options = new ArrayList<>();
        options.add("A");
        options.add("B");
        options.add("Q");
        while(!options.contains(input)) {
            System.out.println("""
                        
                        A) see client list
                        B) search by ID
                        Q) quit""");
            input = in.next().toUpperCase();
        }
        if(input.equalsIgnoreCase("Q")) return null;
        if(input.equalsIgnoreCase("A")) {
            for(Map.Entry<String, Guest> g : data.getGuests().entrySet()){
                System.out.println(g.getValue());
            }
        }
        while (!isValidID(input)){
            System.out.print("enter client ID: ");
            input = in.next();
            if(input.equalsIgnoreCase("Q")) return null;
        }
        return data.getGuests().get(input);
    }
    private static MyDate getDateFromUserInput(){
        String input = "Z";
        while (!Functions.isValidYear(input)){
            System.out.print("enter year: 20");
            input = in.next();
            if(input.equalsIgnoreCase("Q")) return MyDate.current();
        }
        int year = Integer.parseInt(input)+2000;
        input = "Z";
        while (!Functions.isValidMonth(input)){
            System.out.print("enter month: ");
            input = in.next();
            if(input.equalsIgnoreCase("Q")) return MyDate.current();
        }
        int month = Integer.parseInt(input);
        input = "Z";
        while (!Functions.isValidDay(input)){
            System.out.print("enter day: ");
            input = in.next();
            if(input.equalsIgnoreCase("Q")) return MyDate.current();
        }
        int day = Integer.parseInt(input);
        return MyDate.date(year, month, day);
    }

    /**
     * The "workhorse" of the program.
     * Gets search parameters, searches and reserves the rooms.
     * @param guest the current guest that is logged in.
     */
    private static void searchAndReserve(Guest guest) {
        if(guest==null) return;
        Search search = guest.getSearch();
        if(search==null) return;
        ArrayList<String> options = new ArrayList<>();
        options.add("A");
        if(search.getCity()!=null) options.add("B");
        options.add("C");
        options.add("Q");
        String input = "Z";
        // Choose how to search:
        while (!options.contains(input)){
            System.out.println("\nA) input new search");
            if(search.getCity()!=null){
                System.out.println("B) use current search parameters");
            }
            System.out.println("""
                    C) use demo search
                    Q) quit""");
            input = in.next().toUpperCase();
            switch (input){
                case "A" -> updateSearchFromUserInput(search);
                case "C" -> updateSearchFromDemo(search);
                case "Q" -> {
                    search.releaseRooms();
                    return;
                }
            }
        }
        System.out.println("search:\n"+search+"\n");
        // Filter by search params:
        try {
            if(DEBUG) System.out.println("filtering...");
            search.filter();
            if(DEBUG) System.out.println("done");
        }catch (NullParamException e){
            System.out.println("reservation could not be completed.");
            search.releaseRooms();
            return;
        } catch (NoRoomsFoundException e) {
            System.out.println("could not find enough relevant rooms.");
            search.releaseRooms();
            return;
        }
        ArrayList<Accommodation> hotels = search.getHotels();
        if(hotels.isEmpty()){
            System.out.println("no relevant hotel found.");
            search.releaseRooms();
            return;
        }
        // Choose how to sort options:
        input = "Z";
        System.out.println("sort hotels by:");
        options.clear();
        ArrayList<String> sorts = Sort.getSorts();
        for(int i=0; i<sorts.size(); i++){
            options.add(""+(i+1));
            System.out.println((i+1)+") "+sorts.get(i));
        }
        while(!isAllInts(input) ||
                (isAllInts(input) &&
                        (Integer.parseInt(input)<1 ||
                                Integer.parseInt(input)-1>=sorts.size()))){
            System.out.print("enter number: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")){
                search.releaseRooms();
                return;
            }
        }
        search.sort(sorts.get(Integer.parseInt(input)-1));
        // Choose hotel:
        input = "Z";
        System.out.println("choose a hotel:");
        for(int i=0; i<hotels.size(); i++){
            System.out.println((i+1)+") "+hotels.get(i));
        }
        while(!isAllInts(input) ||
                (isAllInts(input) &&
                        (Integer.parseInt(input)<1 ||
                                Integer.parseInt(input)-1>=hotels.size()))){
            System.out.print("enter number: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")){
                search.releaseRooms();
                return;
            }
        }
        Hotel hotel = (Hotel) hotels.get(Integer.parseInt(input)-1);
        search.setHotel(hotel);
        // Search within hotel:
        try {
            search.releaseRooms();
            search.filter();
        }catch (NullParamException e){
            System.out.println("reservation could not be completed.");
            search.releaseRooms();
            return;
        }catch (NoRoomsFoundException e){
            System.out.println("could not find enough relevant rooms.");
            search.releaseRooms();
            return;
        }
        // Choose reservation option:
        input = "Z";
        System.out.println("\nchoose a room bundle:");
        ArrayList<RoomInterface> rooms = search.getSearchRoomList();
        if(rooms.isEmpty()){
            System.out.println("no available options.");
            search.releaseRooms();
            return;
        }
        for(int i=0; i<rooms.size(); i++){
            System.out.println((i+1)+") "+rooms.get(i));
        }
        while(!isAllInts(input) ||
                (isAllInts(input) &&
                        (Integer.parseInt(input)<1 ||
                                Integer.parseInt(input)-1>=search.getSearchRoomList().size()))){
            System.out.print("\nenter number: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")){
                search.clearHotels();
                search.releaseRooms();
                return;
            }
        }
        RoomInterface room = rooms.get(Integer.parseInt(input)-1);
        // Choose payment method:
        input = "Z";
        System.out.println("choose payment method:");
        ArrayList<Integer> paymentOptions = room.getHotel().getPaymentOptions();
        for(int i : paymentOptions){
            System.out.println(i+") "+Hotel.getAllPaymentOptions().get(i));
        }
        while(!isAllInts(input) ||
                (isAllInts(input) &&
                        !hotel.getPaymentOptions().contains(Integer.parseInt(input)))){
            System.out.print("enter number: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")){
                search.releaseRooms();
                return;
            }
        }
        // Get payment with helper methods and return reservation:
        try {
            String[] payInfo = getPaymentInfo(Integer.parseInt(input), guest, hotel);
            Reservation r = hotel.makeReservation(guest,search,room.getRoomNumbers(),room,payInfo);
            guest.addReservation(r);
            search.releaseRooms();
            System.out.println("\nreservation made successfully!\n"+r);
        }catch (RoomTakenException e){
            System.out.println("this room is already taken.\ntry a different room");
            search.releaseRooms();
        }catch (PaymentErrorException e){
            System.out.println("transaction error");
            search.releaseRooms();
        }
    }
    private static void cancelReservation(Guest guest) {
        if(guest==null) return;
        String input = "Z";
        HashMap<Integer,Reservation> reservations = guest.getReservations();
        for(Map.Entry<Integer, Reservation> res : reservations.entrySet()){
            System.out.println(res.getValue()+"\n");
        }
        while (!isAllInts(input) || (isAllInts(input) &&
                !guest.hasReservation(Integer.parseInt(input)))){
            System.out.print("enter reservation number: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")) return;
        }
        Reservation reservation = guest.getReservations().get(Integer.parseInt(input));
        Hotel hotel = reservation.getRooms().getHotel();
        ArrayList<Integer> refundOptions = hotel.getRefundOptions();
        for(int i : refundOptions){
            System.out.println(i+") "+Hotel.getAllRefundOptions().get(i));
        }
        if(refundOptions.isEmpty()){
            input = "Z";
            ArrayList<String> options = new ArrayList<>();
            options.add("Y");
            options.add("N");
            options.add("Q");
            while (!options.contains(input)){
                System.out.println("""
                    refund not available.
                    do you want to cancel reservation anyway?
                    [Y]es [N]o""");
                input = in.next().toUpperCase();
                if(input.equalsIgnoreCase("Q") || input.equalsIgnoreCase("N")) return;
                if(input.equalsIgnoreCase("Y")){
                    String[] info = {};
                    try {
                        guest.deleteReservation(reservation.getReservationNumber(), info);
                    }catch (NoSuchReservationException e){
                        System.out.println("no such reservation");
                    }catch (PaymentErrorException e){
                        System.out.println("transaction error");
                    }
                    return;
                }
            }
            System.out.println("reservation cancelled successfully");
        }
        input = "Z";
        System.out.println("choose refund option:");
        while(!isAllInts(input) ||
                (isAllInts(input) &&
                        !hotel.getRefundOptions().contains(Integer.parseInt(input)))){
            System.out.print("enter number: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")) return;
        }
        try {
            String[] payInfo = getRefundInfo(Integer.parseInt(input), guest, hotel);
            guest.deleteReservation(reservation.getReservationNumber(), payInfo);
        }catch (NoSuchReservationException e){
            System.out.println("no such reservation");
        }catch (PaymentErrorException e){
            System.out.println("transaction error");
        }
    }
    // Methods to get payment info from client, for the pay/refund:
    private static String[] getRefundInfo(int refundOption, Guest guest, Hotel hotel)
            throws PaymentErrorException {
        String[] s;
        switch (refundOption){
            case BANK_TRANSFER -> s = refundBankTransfer(guest, hotel);
            case CREDIT_CARD ->  s = refundCreditCard(guest, hotel);
            case BIT_PAY -> s = refundBitPay(guest, hotel);
            default -> throw new PaymentErrorException("no such refund option");
        }
        return s;
    }
    private static String[] getPaymentInfo(int paymentOption, Guest guest, Hotel hotel)
            throws PaymentErrorException {
        String[] s;
        switch (paymentOption){
            case BANK_TRANSFER -> s = payBankTransfer(guest, hotel);
            case CREDIT_CARD ->  s = payCreditCard(guest, hotel);
            case BIT_PAY -> s = payBitPay(guest, hotel);
            default -> throw new PaymentErrorException("no such payment option");
        }
        return s;
    }
    private static String[] payBankTransfer(Guest guest, Hotel hotel){
        if(guest==null) return null;
        System.out.println("enter your bank info:");
        System.out.print("bank name: ");
        String bank = in.next().toUpperCase();
        System.out.print("account number: ");
        String accNum = in.next().toUpperCase();
        return new String[]{"<transaction confirmation>","bank: "+bank,"accNum: "+accNum,"guest: "+guest,"hotel: "+hotel};
    }
    private static String[] payCreditCard(Guest guest, Hotel hotel){
        if(guest==null || hotel==null) return null;
        System.out.println("enter your credit card info:");
        System.out.print("card number: ");
        String num = in.next().toUpperCase();
        System.out.print("expiration date: ");
        MyDate date = getDateFromUserInput();
        System.out.print("CVV: ");
        String cvv = in.next().toUpperCase();
        return new String[]{"<credit card info>","cardNum: "+num,"exp: "+date,"cvv: "+cvv,"guest: "+guest,"hotel: "+hotel};
    }
    private static String[] payBitPay(Guest guest, Hotel hotel){
        if(guest==null || hotel==null) return null;
        System.out.print("enter transfer number: ");
        String num = in.next().toUpperCase();
        return new String[]{"<transfer confirmation>","bit: "+num,"guest: "+guest,"hotel: "+hotel};
    }
    private static String[] refundBankTransfer(Guest guest, Hotel hotel){
        if(guest==null || hotel==null) return null;
        System.out.println("enter your bank info:");
        System.out.print("bank name: ");
        String bank = in.next().toUpperCase();
        System.out.print("account number: ");
        String accNum = in.next().toUpperCase();
        return new String[]{"<transaction confirmation>","bank: "+bank,"accNum: "+accNum,"guest: "+guest,"hotel: "+hotel};
    }
    private static String[] refundCreditCard(Guest guest, Hotel hotel){
        if(guest==null || hotel==null) return null;
        System.out.println("enter your credit card info:");
        System.out.print("card number: ");
        String num = in.next().toUpperCase();
        System.out.print("expiration date: ");
        MyDate date = getDateFromUserInput();
        System.out.print("CVV: ");
        String cvv = in.next().toUpperCase();
        return new String[]{"<credit card info>","cardNum: "+num,"exp: "+date,"cvv: "+cvv,"guest: "+guest,"hotel: "+hotel};
    }
    private static String[] refundBitPay(Guest guest, Hotel hotel){
        if(guest==null || hotel==null) return null;
        System.out.print("enter phone number: ");
        String num = in.next().toUpperCase();
        return new String[]{"<transfer confirmation>","bit: "+num,"guest: "+guest,"hotel: "+hotel};
    }

    private static void updateSearchFromDemo(Search search){
        System.out.println("demo searches:");
        int i=1;
        ArrayList<String> options = new ArrayList<>();
        for(Search s : demoSearches){
            options.add(String.valueOf(i));
            System.out.println((i++)+") "+s.description());
        }
        String input = "Z";
        while(!options.contains(input)) {
            System.out.println("\nchoose a search:");
            input = in.next();
        }
        search.update(demoSearches.get(Integer.parseInt(input)-1));
    }
    private static void updateRoomsAndPeople(Search search){
        String input = "Z";
        while (!isValidPeople(input)){
            System.out.print("how many people? ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")) return;
        }
        search.setPeople(Integer.parseInt(input));
        input = "Z";
        while (!isValidRooms(input)){
            System.out.print("how many rooms? ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")) return;
        }
        search.setRooms(Integer.parseInt(input));
    }
    private static void updateSearchFromUserInput(Search search){
        if(search==null) return;
        search.clearHotels();
        String input = "Z";
        System.out.println("to quit, press Q");
        if(data.cityNames().isEmpty()){
            System.out.println("no cities");
            return;
        }
        System.out.println("cities:");
        for (String c : data.cityNames()){
            System.out.println(c);
        }
        boolean first = true;
        while(!data.cityNames().contains(input)){
            if(!first) System.out.print("city name: ");
            else first = false;
            input = in.nextLine().toUpperCase();
            if(input.equalsIgnoreCase("Q")) return;
        }
        search.setCity(data.getCities().get(input));
        input = "Z";
        while (!isValidLowStars(input)){
            System.out.print("minimum stars: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")) return;
        }
        int lowStars = Integer.parseInt(input);
        search.setLowStars(lowStars);
        input = "Z";
        while (!isValidLowPrice(input)){
            System.out.print("minimum price: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")) return;
        }
        double lowPrice = Double.parseDouble(input);
        search.setLowPrice(lowPrice);
        input = "Z";
        while (!isValidHighPrice(input, lowPrice)){
            System.out.print("maximum price: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")) return;
        }
        search.setHighPrice(Double.parseDouble(input));
        updateRoomsAndPeople(search);
        int rooms = search.getRooms(), people = search.getPeople();
        while(rooms>people){
            System.out.println("can't have less people than rooms");
            updateRoomsAndPeople(search);
            rooms = search.getRooms();
            people = search.getPeople();
        }
        System.out.println("check in date:");
        MyDate yesterday = Database.data().getToday().previous();
        MyDate checkIn = yesterday;
        while (!isValidCheckInDate(checkIn)){
            if(yesterday.compare(checkIn) < 0){
                System.out.println("check in date can't be a past date");
            }
            System.out.println("enter a valid date");
            checkIn = getDateFromUserInput();
        }
        search.setCheckIn(checkIn);
        MyDate checkOut = checkIn;
        System.out.println("check out date:");
        while (!isValidCheckOutDate(checkOut,checkIn)){
            if(checkOut.compare(checkIn) < 0){
                System.out.println("check out date can't be before check in");
            }
            System.out.println("enter a valid date");
            checkOut = getDateFromUserInput();
        }
        search.setCheckOut(checkOut);
        input = "Z";
        boolean lateCheckOut = false;
        System.out.print("do you want late checkout? ");
        while (!isValidBoolean(input)){
            System.out.print("[Y]es / [N]o: ");
            input = in.next().toUpperCase();
            if(input.equalsIgnoreCase("Q")) return;
            lateCheckOut = input.equalsIgnoreCase("Y");
        }
        search.setLateCheckOut(lateCheckOut);
        ArrayList<RoomAmenity> AvailableRoomAmenities = RoomAmenity.getAmenityList();
        if(AvailableRoomAmenities==null) AvailableRoomAmenities = new ArrayList<>();
        for(RoomAmenity a : AvailableRoomAmenities){
            input = "Z";
            while (!Functions.isValidBoolean(input)) {
                System.out.println("do you want a " + a.getName() + "? [Y]es / [N]o");
                input = in.next().toUpperCase();
                if(input.equalsIgnoreCase("Q")) return;
                if(input.equalsIgnoreCase("Y")) search.addRoomAmenity(a.getName());
                if(input.equalsIgnoreCase("N")) search.removeRoomAmenity(a.getName());
            }
        }
        ArrayList<HotelAmenity> AvailableHotelAmenities = HotelAmenity.getAmenityList();
        if(AvailableHotelAmenities==null) AvailableHotelAmenities = new ArrayList<>();
        for(HotelAmenity a : AvailableHotelAmenities){
            input = "Z";
            while (!Functions.isValidBoolean(input)) {
                System.out.println("do you want " + a.getName() + "? [Y]es / [N]o");
                input = in.next().toUpperCase();
                if(input.equalsIgnoreCase("Q")) return;
                if(input.equalsIgnoreCase("Y")) search.addHotelAmenity(a.getName());
                if(input.equalsIgnoreCase("N")) search.addHotelAmenity(a.getName());
            }
        }
    }

    private static void createDemoSearches() {
        String s = "";
        Search search = new Search(3,100,1500,4,2,
                MyDate.current(),MyDate.current().next().next(),true,s);
        search.setCity(data.getCities().get("TEL AVIV"));
        search.clearHotels();
        search.addRoomAmenity("Room Balcony");
        search.addHotelAmenity("Dinner");
        s = "tel aviv, 3 stars, 4 people in 2 rooms, ";
        s += search.getCheckIn()+" -> "+search.getCheckOut();
        s += ", "+search.getLowPrice()+" -> "+ search.getHighPrice();
        s += ", balcony, dinner, late checkOut";
        search.describe(s);
        demoSearches.add(search);

        search = new Search(5,1000,4500,3,2,
                MyDate.current(),MyDate.current().next().next(),true,s);
        search.setCity(data.getCities().get("JERUSALEM"));
        search.clearHotels();
        search.addRoomAmenity("Room Balcony");
        search.addHotelAmenity("Swimming Pool");
        search.addHotelAmenity("Breakfast");
        search.addHotelAmenity("Lunch");
        search.addHotelAmenity("Dinner");
        s = "jerusalem, 5 stars, 3 people in 2 rooms, ";
        s += search.getCheckIn()+" -> "+search.getCheckOut();
        s += ", "+search.getLowPrice()+" -> "+ search.getHighPrice();
        s += ", all inclusive";
        search.describe(s);
        demoSearches.add(search);

        search = new Search(2,100,15000,15,6,
                MyDate.current(),MyDate.current().next().next(),true,s);
        search.setCity(data.getCities().get("EILAT"));
        search.clearHotels();
        search.addRoomAmenity("Room Balcony");
        search.addHotelAmenity("Swimming Pool");
        s = "eilat, 2 stars, 15 people in 6 rooms, ";
        s += search.getCheckIn()+" -> "+search.getCheckOut();
        s += ", "+search.getLowPrice()+" -> "+ search.getHighPrice();
        s += ", balcony, pool, late checkOut";
        search.describe(s);
        demoSearches.add(search);

        search = new Search(4,100,20000,8,2,
                MyDate.current(),MyDate.current().next().next(),true,s);
        search.setCity(data.getCities().get("TVERYA"));
        search.clearHotels();
        search.addHotelAmenity("Swimming Pool");
        search.addHotelAmenity("Breakfast");
        s = "tverya, 4 stars, 8 people in 2 rooms, ";
        s += search.getCheckIn()+" -> "+search.getCheckOut();
        s += ", "+search.getLowPrice()+" -> "+ search.getHighPrice();
        s += ", pool, breakfast";
        search.describe(s);
        demoSearches.add(search);

        search = new Search(2,100,25000,15,12,
                MyDate.current(),MyDate.current().next().next(),true,s);
        search.setCity(data.getCities().get("EILAT"));
        search.clearHotels();
        search.addRoomAmenity("Room Balcony");
        search.addHotelAmenity("Swimming Pool");
        s = "eilat, 2 stars, 15 people in 12 rooms, ";
        s += search.getCheckIn()+" -> "+search.getCheckOut();
        s += ", "+search.getLowPrice()+" -> "+ search.getHighPrice();
        s += ", balcony, pool";
        search.describe(s);
        demoSearches.add(search);

        search = new Search(3,100,15000,15,3,
                MyDate.current(),MyDate.current().next().next(),false,s);
        search.setCity(data.getCities().get("TEL AVIV"));
        search.clearHotels();
        search.addRoomAmenity("Room Balcony");
        search.addHotelAmenity("Swimming Pool");
        search.addHotelAmenity("Lunch");
        s = "tel aviv, 3 stars, 15 people in 3 rooms, ";
        s += search.getCheckIn()+" -> "+search.getCheckOut();
        s += ", "+search.getLowPrice()+" -> "+ search.getHighPrice();
        s += ", balcony, pool, lunch";
        search.describe(s);
        demoSearches.add(search);

        search = new Search(5,100,1500,1,1,
                MyDate.current(),MyDate.current().next().next().
                next().next().next().next().next().next().next(),true,s);
        search.setCity(data.getCities().get("TEL AVIV"));
        search.clearHotels();
        search.addHotelAmenity("Swimming Pool");
        search.addHotelAmenity("Breakfast");
        search.addHotelAmenity("Lunch");
        s = "tel aviv, 5 stars, 1 person, ";
        s += search.getCheckIn()+" -> "+search.getCheckOut();
        s += ", "+search.getLowPrice()+" -> "+ search.getHighPrice();
        s += ", pool, breakfast, lunch, late checkOut";
        search.describe(s);
        demoSearches.add(search);

        search = new Search(4,100,150,7,1,
                MyDate.current(),MyDate.current().next().next(),true,s);
        search.setCity(data.getCities().get("TVERYA"));
        search.clearHotels();
        search.addHotelAmenity("Breakfast");
        search.addHotelAmenity("Lunch");
        s = "tverya, 4 stars, 7 people in 1 room, ";
        s += search.getCheckIn()+" -> "+search.getCheckOut();
        s += ", "+search.getLowPrice()+" -> "+ search.getHighPrice();
        s += ", breakfast, lunch";
        search.describe(s);
        demoSearches.add(search);

        search = new Search(4,100,9500,7,1,
                MyDate.current(),MyDate.current().next().next(),true,s);
        search.setCity(data.getCities().get("TVERYA"));
        search.clearHotels();
        search.addHotelAmenity("Breakfast");
        search.addHotelAmenity("Lunch");
        s = "tverya, 2 stars, 7 people in 1 room, ";
        s += search.getCheckIn()+" -> "+search.getCheckOut();
        s += ", "+search.getLowPrice()+" -> "+ search.getHighPrice();
        s += ", breakfast, lunch";
        search.describe(s);
        demoSearches.add(search);
    }
}
