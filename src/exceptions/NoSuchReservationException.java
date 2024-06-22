package exceptions;

public class NoSuchReservationException extends Throwable{
    public NoSuchReservationException(String s){
        super(s);
    }
}
