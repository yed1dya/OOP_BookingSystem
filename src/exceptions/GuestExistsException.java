package exceptions;

public class GuestExistsException extends Throwable{
    public GuestExistsException(String s) {
        super(s);
    }
}
