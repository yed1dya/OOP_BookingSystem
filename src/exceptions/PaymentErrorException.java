package exceptions;

public class PaymentErrorException extends Throwable{
    public PaymentErrorException(String s){
        super(s);
    }
}
