package database;

public class Breakfast extends HotelAmenity implements Meal{
    public Breakfast(String name) {
        super(name);
    }
    public Breakfast(){
        this("Breakfast");
    }
}
