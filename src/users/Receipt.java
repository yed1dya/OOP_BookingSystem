package users;

import database.Hotel;

public class Receipt {
    private static int count = 0;
    private Hotel hotel;
    private double price;
    private int number;

    public Receipt(Hotel h, double p){
        this.hotel = h;
        this.price = p;
        this.number = ++count;
    }
    public int getNumber(){ return number; }
    public Hotel getHotel() {
        return hotel;
    }
    public double getPrice() {
        return price;
    }
    public String toString(){
        return "receipt number: "+getNumber()+"\nhotel: "+getHotel()+"\nprice: "+price+"\n";
    }
}
