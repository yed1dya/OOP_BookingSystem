package database;

public class Dinner extends HotelAmenity implements Meal{
    public Dinner(String name) {
        super(name);
    }
    public Dinner(){
        this("Dinner");
    }
}
