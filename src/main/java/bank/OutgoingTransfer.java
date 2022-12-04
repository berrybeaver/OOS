package bank;

public class OutgoingTransfer extends Transfer {
    /**
     * Konstruktor mit drei Attributen
     *
     * @param newDate        Wert für date
     * @param description
     * @param newAmount      Wert für amount
     * @param newDescription Wert für description
     * @param recipient
     */
    public OutgoingTransfer(String newDate, String description, double newAmount, String newDescription, String recipient) {
        super(newDate, newAmount, newDescription);
    }

    /**
     * Konstruktor mit aller Attributen
     *
     * @param newDate        Wert für date
     * @param newDescription Wert für description
     * @param newAmount      Wert für amount
     * @param newSender      Wert für sender
     * @param newRecipient   Wert für recipient
     */
    public OutgoingTransfer(String newDate, double newAmount, String newDescription, String newSender, String newRecipient) {
        super(newDate, newAmount, newDescription, newSender, newRecipient);
    }

    /**
     * Copy Constructor
     *
     * @param newTransfer neue Objekt festzulegen
     */
    public OutgoingTransfer(Transfer newTransfer) {
        super(newTransfer);
    }

    @Override
    public double calculate() {
        return -amount;
    }
}
