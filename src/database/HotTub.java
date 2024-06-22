package database;

public class HotTub extends RoomAmenity{
    private static String name = "Hot Tub";
    public HotTub(){
        super(name);
    }
    public String getName(){
        return name;
    }
}
