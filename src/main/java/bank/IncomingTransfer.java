package bank;

public class IncomingTransfer extends Transfer{
    /**
     * Konstruktor mit drei Attributen
     *
     * @param newDate     Wert für date
     * @param newDescription
     * @param newAmount   Wert für amount
     */
    public IncomingTransfer(String newDate, String newDescription, double newAmount) {
        super(newDate, newAmount, newDescription);
    }

    /**
     * Konstruktor mit drei Attributen
     *
     * @param newDate        Wert für date
     * @param newDescription Wert für description
     * @param newAmount      Wert für amount
     * @param newSender      Wert für sender
     * @param newRecipient   Wert für recipient
     */
    public IncomingTransfer(String newDate, double newAmount, String newDescription, String newSender, String newRecipient) {
        super(newDate, newAmount, newDescription, newSender, newRecipient);
    }

    /**
     * Copy Constructor
     *
     * @param newTransfer neue Objekt festzulegen
     */
    public IncomingTransfer(Transfer newTransfer) {
        super(newTransfer);
    }

    @Override
    public double calculate() {
        return amount;
    }
}
