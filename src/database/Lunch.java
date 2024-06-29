package database;

public class Lunch extends HotelAmenity implements Meal{
    public Lunch(String name) {
        super(name);
    }
    public Lunch(){
        this("Lunch");
    }
}
