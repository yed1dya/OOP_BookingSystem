package database;

import exceptions.*;
import users.*;

import java.util.*;

import static functions.Functions.*;
import static main.Main.*;

public class Hotel implements Accommodation {
    private static boolean created = false;
    private static int count = 0;
    private static HashMap<Integer,String> allPaymentOptions = new HashMap<>();
    private static HashMap<Integer,String> allRefundOptions = new HashMap<>();
    private String name;
    private int stars;
    private int bottomFloor;
    private int topFloor;
    private HashMap<Integer, Room> rooms;
    private List<Room> hotelRoomList;
    private double lowPrice, highPrice;
    private ArrayList<Integer> paymentOptions;
    private ArrayList<Integer> refundOptions;
    private ArrayList<HotelAmenity> hotelAmenities;
    private HashMap<MyDate,Reservation> reservations;
    private List<Subscriber> subscribers;

    public Hotel(String name, int stars,
                 int bottomFloor, int topFloor,
                 double lowPrice, double highPrice,
                 ArrayList<Integer> paymentOptions,
                 ArrayList<Integer> refundOptions){
        this.name = name;
        this.stars = stars;
        this.bottomFloor = bottomFloor;
        this.topFloor = topFloor;
        this.lowPrice = lowPrice;
        this.highPrice = highPrice;
        this.rooms = new HashMap<>();
        this.hotelRoomList = new LinkedList<>();
        this.paymentOptions = new ArrayList<>(paymentOptions);
        this.refundOptions = new ArrayList<>(refundOptions);
        this.hotelAmenities = new ArrayList<>();
        this.reservations = new HashMap<>();
        this.subscribers = new ArrayList<>();
        if(!created){
            allPaymentOptions.put(BANK_TRANSFER, "Bank Transfer");
            allPaymentOptions.put(CREDIT_CARD, "Credit card");
            allPaymentOptions.put(BIT_PAY, "Bit");
            allRefundOptions.put(BANK_TRANSFER, "Bank Transfer");
            allRefundOptions.put(CREDIT_CARD, "Credit card");
            allRefundOptions.put(BIT_PAY, "Bit");
            created = true;
        }
        count();
    }

    public RoomInterface suggestRoomBundle(Search search)
            throws NullParamException, NoRoomsFoundException {
        if(search==null) return null;
        // Sort by price and beds:
        // Java uses stable sort, so the order of the first search is relatively preserved.
        // Even so, the number if beds is more important, so that is the second sort.
        hotelRoomList.sort(Comparator.comparingDouble(Room::getPrice));
        hotelRoomList.sort(Comparator.comparingInt(Room::getBeds));
        RoomBundle bundle = new RoomBundle();
        boolean couldNotFind = false;
        // Set how many people per room:
        int people = search.getPeople(), rooms = search.getRooms(),
                perRoom = (int) Math.ceil((double)people/rooms);
        // Create new temporary Search.
        // This Search will help assign a room for each subgroup of people:
        Search t = new Search(search);
        t.setRooms(1);
        while (people>0){
            if(DEBUG) System.out.println("p: "+people+", r: "+rooms);
            // If people can split into rooms evenly, do that:
            if(rooms>0 && people%rooms==0 && !couldNotFind){
                perRoom = people/rooms;
            }
            if(DEBUG) System.out.println("perRoom: "+perRoom);
            // Find a room for the size of the subgroup, or for the remaining people:
            t.setPeople(Math.min(perRoom,people));
            couldNotFind = false;
            ArrayList<RoomInterface> roomSuggestions = suggestRooms(t,1);
            if(!roomSuggestions.isEmpty()){
                // If an option was found, add it to the list,
                // and decrease that number of people from the group.
                Room room = (Room)roomSuggestions.get(0);
                // check that the price limit isn't exceeded:
                if(room.getPrice()+bundle.getPrice()<=search.getHighPrice()) {
                    bundle.addRoom((Room)roomSuggestions.get(0));
                    people -= perRoom;
                    rooms--;
                }
            }else{
                if(DEBUG) System.out.println("nothing found, decreasing perRoom");
                // If no option was found, try again with fewer people in the room:
                perRoom--;
                couldNotFind = true;
            }
            if(perRoom<=0){
                if(DEBUG) System.out.println("perRoom<=0");
                // If we couldn't find a way to split the group into rooms:
                search.releaseRooms();
                throw new NoRoomsFoundException("could not find enough relevant rooms.");
            }
        }
        return bundle;
    }
    // Gives a list of up to <limit> relevant rooms.
    // Helper method for suggestRoomBundle; returns one room for the search.
    public ArrayList<RoomInterface> suggestRooms(Search search, int limit){
        ArrayList<RoomInterface> relevantRooms = new ArrayList<>();
        if(limit<=0) return relevantRooms;
        int count = 0;
        // By default, roomsList is sorted (increasing) by the number of beds in each room.
        for(Room room : hotelRoomList){
            boolean relevant = !room.isOnHold();
            // Check if the room matches the general search parameters:
            if(room.getBeds()<search.getPeople() ||
                    room.getPrice()>search.getHighPrice() ||
                    room.getPrice()<search.getLowPrice()){
                relevant = false;
            }
            // Check if the room has all the required amenities:
            for(String a : search.getRoomAmenities()){
                if(!relevant) break;
                if(!room.getRoomAmenitiesNames().contains(a)){
                    relevant = false;
                }
            }
            // Check if the room is available on dates:
            MyDate date = search.getCheckIn(), checkOut = search.getCheckOut();
            while (date!=checkOut && relevant){
                if(room.getDates().contains(date)){
                    relevant = false;
                }
                date = date.next();
            }
            if(relevant){
                relevantRooms.add(room);
                // Set the room on hold,
                // so that it won't be suggested twice in the same bundle:
                room.setHold(true);
                count++;
            }
            if(count>=limit){
                return relevantRooms;
            }
        }
        return relevantRooms;
    }
    public Reservation makeReservation(Guest guest,
                                       Search search, ArrayList<Integer> roomNumbers,
                                       RoomInterface bundle, String[] payInfo)
            throws PaymentErrorException, RoomTakenException {
        return makeReservation(guest, search.getCheckIn(), search.getCheckOut(),
                search.getLateCheckOut(),roomNumbers,bundle,payInfo);
    }
    public Reservation makeReservation(Guest guest, MyDate checkIn,
                                       MyDate checkOut, boolean late,
                                       ArrayList<Integer> roomNumbers,
                                       RoomInterface room, String[] payInfo)
            throws PaymentErrorException, RoomTakenException {
        int days = MyDate.days(checkIn,checkOut);
        double price = 0;
        for(int num : roomNumbers){
            price+=this.rooms.get(num).getPrice();
        }
        Receipt receipt = pay(payInfo,price*days);
        room.reserve(checkIn,checkOut);
        Reservation reservation = new Reservation(checkIn,checkOut,late,"",guest,room,receipt);
        this.reservations.put(reservation.getCheckIn(),reservation);
        room.addReservation(reservation);
        this.subscribers.add(guest);
        return reservation;
    }
    public Receipt deleteReservation(Reservation r, String[] payInfo)
            throws PaymentErrorException {
        if(r==null){
            throw new PaymentErrorException("null reservation");
        }
        Receipt receipt = refund(payInfo, r);
        MyDate date = r.getCheckIn(), checkOut = r.getCheckOut();
        RoomBundle room = (RoomBundle) r.getRooms();
        while (date!=checkOut){
            room.removeDate(date);
            date = date.next();
        }
        return receipt;
    }

    /**
     * refunds payment to requested method
     * @param info contains the payment method info - credit card, bank transfer, etc.
     * @param r reservation
     * @return receipt
     * @throws PaymentErrorException if refund failed
     */
    private Receipt refund(String[] info, Reservation r)
            throws PaymentErrorException{
        if(r==null || info==null){
            throw new PaymentErrorException("bad info");
        }
        return new Receipt(this, (-1*r.getReceipt().getPrice()));
    }

    /**
     * gets payment by requested method
     * @param info contains the payment method info - credit card, bank transfer, etc.
     * @param price reservation price
     * @return receipt
     * @throws PaymentErrorException if payment failed
     */
    private Receipt pay(String[] info, double price)
            throws PaymentErrorException{
        if(info==null){
            throw new PaymentErrorException("bad info");
        }
        return new Receipt(this, price);
    }
    public static HashMap<Integer, String> getAllRefundOptions() {
        return allRefundOptions;
    }
    public static HashMap<Integer, String> getAllPaymentOptions() {
        return allPaymentOptions;
    }
    public ArrayList<Integer> getRefundOptions() {
        return refundOptions;
    }
    public void addRefundOptions(int p) {
        if(!this.refundOptions.contains(p)) {
            this.refundOptions.add(p);
        }
    }
    public ArrayList<Integer> getPaymentOptions() {
        return paymentOptions;
    }
    public void addPaymentOptions(int p) {
        if(!this.paymentOptions.contains(p)) {
            this.paymentOptions.add(p);
        }
    }
    public void addRoom(Room room)
            throws RoomExistsException, NullParamException {
        if(room==null){
            throw new NullParamException("null room");
        }
        if(this.rooms.containsKey(room.getRoomNumbers().get(0))){
            throw new RoomExistsException("this hotel already has a room with this number");
        }
        this.rooms.put(room.getRoomNumber(), room);
        this.hotelRoomList.add(room);
        double price = room.getPrice();
        if(price<lowPrice){
            lowPrice = price;
        } else if(price>highPrice){
            highPrice = price;
        }
    }
    public void addRoom(int roomNumber, int beds, double price,
                        ArrayList<RoomAmenity> roomAmenities)
            throws RoomExistsException, NullParamException {
        Room room = new Room(roomNumber,beds,price,roomAmenities,this);
        addRoom(room);
    }
    public void removeRoom(int roomNumber)
            throws NoSuchRoomException{
        if(!rooms.containsKey(roomNumber)){
            throw new NoSuchRoomException("no such room number in this hotel");
        }
        Room room = rooms.remove(roomNumber);
        if(room.getPrice()==highPrice || room.getPrice()==lowPrice){
            updateHighLowPrice();
        }
    }
    private void updateHighLowPrice(){
        for(Map.Entry<Integer, Room> r : rooms.entrySet()){
            double price = r.getValue().getPrice();
            this.highPrice = Math.max(price, highPrice);
            this.lowPrice = Math.min(price, lowPrice);
        }
    }
    public void notifySubscribers(String message){
        for (Subscriber s : this.subscribers){
            s.notify(message);
        }
    }
    public void addAmenity(int roomNumber, RoomAmenity amenity){
        if(this.getRooms().containsKey(roomNumber)){
            this.getRooms().get(roomNumber).addAmenity(amenity);
        }
    }
    public void removeAmenity(int roomNumber, String amenity){
        if(this.getRooms().containsKey(roomNumber)){
            this.getRooms().get(roomNumber).removeAmenity(amenity);
        }
    }
    public ArrayList<HotelAmenity> getHotelAmenities(){
        return this.hotelAmenities;
    }
    public void addHotelAmenity(HotelAmenity amenity){
        this.hotelAmenities.add(amenity);
    }
    public void removeHotelAmenity(String name){
        hotelAmenities.removeIf(a -> a.getName().equals(name));
    }
    public int getBottomFloor() {
        return bottomFloor;
    }
    public void setBottomFloor(int bottomFloor) {
        this.bottomFloor = bottomFloor;
    }
    public int getTopFloor() {
        return topFloor;
    }
    public void setTopFloor(int topFloor) {
        this.topFloor = topFloor;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getStars() {
        return stars;
    }
    public void setStars(int stars) {
        this.stars = stars;
    }
    public HashMap<Integer, Room> getRooms() {
        return rooms;
    }
    public double getLowPrice() {
        return lowPrice;
    }
    public double getHighPrice() {
        return highPrice;
    }
    public HashMap<MyDate,Reservation> getReservations(){
        return this.reservations;
    }
    public void changePrice(int roomNumber, double newPrice){
        Room room = this.rooms.get(roomNumber);
        if(room==null) return;
        room.setPrice(newPrice);
        for(Map.Entry<MyDate, Reservation> r : room.getReservations().entrySet()){
            Reservation res = r.getValue();
            res.getGuest().notify("room price has changed. new price is "+newPrice);
        }
    }
    public static int count(){
        return count++;
    }
    public String hotelData(){
        StringBuilder str = new StringBuilder(name + ", " + stars + " stars" +
                "\nprices: " + lowPrice + " -> " + highPrice +
                "\nfloors: " + bottomFloor + " -> " + topFloor+
                "\ntotal "+rooms.size()+" rooms");
        for(HotelAmenity a : hotelAmenities){
            str.append(a.getName()).append(", ");
        }
        return str.toString();
    }
    public String toString(){
        return name+", "+stars+" stars";
    }
}