package users;

import database.MyDate;
import database.RoomInterface;

public class Reservation {
    private static int counter = 1;
    private int reservationNumber;
    private MyDate checkIn, checkOut;
    private boolean lateCheckOut;
    private String specialRequests;
    private Guest guest;
    private RoomInterface rooms;
    private Receipt receipt;

    public Reservation(MyDate checkIn, MyDate checkOut, boolean late,
                       String specialRequests, Guest guest, RoomInterface rooms,
                       Receipt receipt){
        this.reservationNumber = counter++;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.lateCheckOut = late;
        this.specialRequests = specialRequests;
        this.rooms = rooms;
        this.guest = guest;
        this.receipt = receipt;
    }

    public Receipt getReceipt() {
        return receipt;
    }
    public Guest getGuest() {
        return guest;
    }
    public RoomInterface getRooms() {
        return rooms;
    }
    public int getReservationNumber() {
        return reservationNumber;
    }
    public MyDate getCheckIn() {
        return checkIn;
    }
    public void setCheckIn(MyDate checkIn) {
        this.checkIn = checkIn;
    }
    public MyDate getCheckOut() {
        return checkOut;
    }
    public void setCheckOut(MyDate checkOut) {
        this.checkOut = checkOut;
    }
    public boolean isLateCheckOut() {
        return lateCheckOut;
    }
    public void setLateCheckOut(boolean lateCheckOut) {
        this.lateCheckOut = lateCheckOut;
    }
    public String getSpecialRequests() {
        return specialRequests;
    }
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
    public String toString(){
        StringBuilder str = new StringBuilder("reservation number: " + reservationNumber
                + "\nGuest: " + guest
                + "\nHotel: " + rooms.getHotel());
        str.append("\nCheck in: ").append(checkIn).append("\nCheck out: ")
                .append(checkOut).append("\nLate check out: ").
                append(lateCheckOut).append("\n").append(rooms);
        if(!specialRequests.isEmpty()){
            str.append("\n").append(specialRequests);
        }
        str.append("\n").append(receipt);
        return str.toString();
    }
}
