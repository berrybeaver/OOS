package bank.exceptions;

public class TransactionAttributeException extends ArithmeticException{
    public TransactionAttributeException(String errorMsg){
        super(errorMsg);
    }
}
