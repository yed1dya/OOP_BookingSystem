package users;

import database.Database;
import exceptions.GuestExistsException;
import exceptions.NoSuchReservationException;
import exceptions.PaymentErrorException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Guest implements User, Subscriber {
    private Search search;
    private String firstName;
    private String lastName;
    private String ID;
    private String phone;
    private String email;
    private HashMap<Integer,Reservation> reservations;
    private LinkedList<Receipt> receipts;
    private LinkedList<String> reminders;

    public Guest(String firstName, String lastName, String ID, String phone, String email)
            throws GuestExistsException {
        if(Database.data().guestExists(ID)){
            throw new GuestExistsException("this ID number already exists in database");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.ID = ID;
        this.phone = phone;
        this.email = email;
        this.reservations = new HashMap<>();
        this.search = new Search();
        this.receipts = new LinkedList<>();
        this.reminders = new LinkedList<>();
    }
    public HashMap<Integer,Reservation> getReservations() {
        return reservations;
    }
    public void addReservation(Reservation r){
        reservations.put(r.getReservationNumber(),r);
    }
    public void deleteReservation(int number, String[] payInfo)
            throws NoSuchReservationException, PaymentErrorException {
        for(Map.Entry<Integer, Reservation> r : reservations.entrySet()){
            Reservation res = r.getValue();
            if(res.getReservationNumber()==number){
                Receipt receipt = res.getRooms().getHotel().deleteReservation(res, payInfo);
                if(receipt!=null){
                    reservations.remove(r.getKey());
                    receipts.add(receipt);
                    return;
                }
            }
        }
        throw new NoSuchReservationException("no reservation with this number in this guest");
    }
    @Override
    public void notify(String message) {
        this.reminders.add(message);
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getID() {
        return ID;
    }
    public String setFirstName(String firstName) {
        if(firstName==null){
            return "name wasn't changed";
        }
        this.firstName = firstName;
        return "first name changed successfully to "+firstName;
    }
    public String setLastName(String lastName) {
        if(lastName==null){
            return "name wasn't changed";
        }
        this.lastName = lastName;
        return "last name changed successfully to "+lastName;
    }
    public String setID(String ID) {
        if(ID==null){
            return "ID wasn't changed";
        }
        if(!Database.data().guestExists(ID)) {
            this.ID = ID;
            return "ID changed successfully to "+ID;
        }
        return "that ID already exists in database";
    }
    public String setPhone(String phone) {
        if(phone==null){
            return "phone number wasn't changed";
        }
        this.phone = phone;
        return "phone number changed successfully to "+phone;
    }
    public String setEmail(String email) {
        if(email==null){
            return "email wasn't changed";
        }
        this.email = email;
        return "email changed successfully to "+email;
    }
    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }
    public LinkedList<Receipt> getReceipts() {
        return receipts;
    }
    public Search getSearch() {
        return search;
    }
    public boolean hasReservation(int number){
        return reservations.containsKey(number);
    }
    public LinkedList<String> getReminders(){
        return this.reminders;
    }
    public String toString(){
        return firstName+" "+lastName+", "+ID+", "+phone+", "+email;
    }
}
