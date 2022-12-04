package bank;

import bank.exceptions.TransactionAttributeException;

import java.util.Objects;

/*Vererbung mit extends*/
public class Transfer extends Transaction {
    /**
     * Name des Absenders, der die Überweisung gemacht hat
     */
    private String sender;
    /**
     * Name des Empfängers, der die Überweisung bekommen hat
     */
    private String recipient;

    /* Setter und Getter*/

    /**
     * Legt das Attribut amount fest
     * @param amount neuer Wert für amount
     * prüft ob amount ein positiver Wert ist
     */
    @Override
    public void setAmount(double amount){

        if(amount<=0){
            throw new TransactionAttributeException("AMOUNT MAY NOT BE LOWER THAN 0!\n");
        }
        else{
            this.amount = amount;
        }
    }
    /**
     * Legt das Attribut sender fest
     * @param sender neuer Wert für sender
     */
    public void setSender(String sender){
        this.sender = sender;
    }
    /**
     * @return den aktuellen Wert von sender
     */
    public String getSender(){
        return sender;
    }
    /**
     * Legt das Attribut recipient fest
     * @param recipient neuer Wert von recipient
     */
    public void setRecipient(String recipient){
        this.recipient = recipient;
    }
    /**
     * @return den aktuellen Wert von recipient
     */
    public String getRecipient(){
        return recipient;
    }

    /*konstruktoren */
    /**
     * default Konstruktor
     */
    public Transfer(){
        super();
    }
    /**
     * Konstruktor mit drei Attributen
     * @param date Wert für date
     * @param description Wert für description
     * @param amount Wert für amount
     */
    public Transfer(String date, double amount, String description) {
        super(date, description, amount);
    }
    /**
     * Konstruktor mit fünf Attributen
     * @param date Wert für date
     * @param description Wert für description
     * @param amount Wert für amount
     * @param sender Wert für sender
     * @param recipient Wert für recipient
     */
    public Transfer(String date, double amount, String description, String sender, String recipient){
        this(date, amount, description);
        setSender(sender);
        setRecipient(recipient);
    }
    /**
     * Copy Konstruktor
     * @param Transfer kopieren
     * */
    public Transfer(Transfer Transfer){
        this(Transfer.date, Transfer.amount, Transfer.description, Transfer.sender, Transfer.recipient );
    }
    public void printObject(){
        System.out.println("-----Transfer-----");
        System.out.println("date: " + get_date());
        System.out.println(amount + "$");
        System.out.println(getDescription());
        System.out.println("Sender: " + sender);
        System.out.println("Recipient: " + recipient);
        System.out.println("--------------------------------------------");
    }

    /**
     * @return unveränderter Wert von amount
     */
    @Override
    public double calculate() {
        return amount;
    }

    /**
     * @return den Inhalt aller Klassenattribute
     */
    @Override
    public String toString() {
        return super.toString() + ", Sender: " + sender + ", Recipient: " + recipient + "\n";
    }

    /**
     * Zwei Objekte der Klasse Transfer zum Vergleich
     * @param obj das zu vergleichende Objekt
     * @return true, wenn beide sind gleich sonst false
     */
    /**
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Transfer transfer)
            return (super.equals(transfer) && sender.equals(transfer.sender)
                    && recipient.equals(transfer.recipient));
        return false;
    }
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Transfer transfer = (Transfer) o;
        return Objects.equals(sender, transfer.sender) && Objects.equals(recipient, transfer.recipient);
    }
}
