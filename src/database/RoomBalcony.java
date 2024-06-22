package database;

public class RoomBalcony extends RoomAmenity {
    private static String name = "Room Balcony";
    public RoomBalcony(){
        super(name);
    }
    public String getName(){
        return name;
    }
}
