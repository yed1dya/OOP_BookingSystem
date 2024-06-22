package exceptions;

public class RoomTakenException extends Throwable {
    public RoomTakenException(String s){
        super(s);
    }
}
