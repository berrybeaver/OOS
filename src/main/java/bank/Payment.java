package bank;

import bank.exceptions.TransactionAttributeException;

public class Payment extends Transaction  {
    /**
     * die Zinsen (positiver Wert in Prozent, 0 bis 1) bei einer Einzahlung anfallen
     */
    private double incomingInterest=0;
    /**
     * die Zinsen (positiver Wert in Prozent, 0 bis 1) bei einer Auszahlung anfallen
     */
    private double outcomingInterest=0;

    /* Setter und Getter*/
    /**
     * Legt das Attribut incomingInterest fest
     * @param incomingInterest neuer Wert für incomingInterest
     */
    public void setIncomingInterest(double incomingInterest){
        if(0 <= incomingInterest & incomingInterest<1) {
            this.incomingInterest = incomingInterest;
        }
        else{
            this.incomingInterest = incomingInterest;
            throw new TransactionAttributeException("INCOMING INTEREST MUST BE IN BETWEEN 0 AND 1!\n");
        }
    }
    /**
     * @return den aktuellen Wert von incomingInterest
     */
    public double getIncomingInterest(){
        return incomingInterest;
    }
    /**
     * Legt das Attribut outgoingInterest fest
     * @param outcomingInterest neuer Wert für outgoingInterest
     */
    public void setOutcomingInterest(double outcomingInterest)throws TransactionAttributeException{
        if(0 <= outcomingInterest & outcomingInterest < 1) {
            this.outcomingInterest = outcomingInterest;
        }
        else{
            this.outcomingInterest = outcomingInterest;
            throw new TransactionAttributeException("OUTGOING INTEREST MUST BE IN BETWEEN 0 AND 1!\n");

        }
    }
    /**
     * @return den aktuellen Wert von outcomingInterest
     */
    public double getOutcomingInterest(){
        return outcomingInterest;
    }

    /*Methoden und Konstruktoren*/

    /**
     * default Konstruktor
     */
    public Payment(){
        super();
    }
    /**
     * Konstruktor mit drei Attributen
     * @param date Wert für date
     * @param description Wert für description
     * @param amount Wert für amount
     */
    public Payment(String date, double amount, String description){
        super(date, description, amount);
    }

    /**
     * Konstruktor mit aller Attribute
     * @param date Wert für date
     * @param description Wert für description
     * @param amount Wert für amount
     * @param incomingInterest Wert für incomingInterest
     * @param outcomingInterest Wert für outcomingInterest
     */
    public Payment(String date, double amount, String description, double incomingInterest, double outcomingInterest){
        this(date, amount, description);
        setIncomingInterest(incomingInterest);
        setOutcomingInterest(outcomingInterest);
    }
    /**
     * Copy Konstruktor
     * @param Payment kopieren
     * */
    public Payment(Payment Payment){
        this(Payment.date, Payment.amount, Payment.description, Payment.incomingInterest, Payment.outcomingInterest );
    }

    public void printObject(){
        System.out.println("-----Payment-----");
        System.out.println("date: " + get_date());
        /*sodass wenn amount - ist, wird ein - Zeichnung vor dem Amount geschriebt*/
        if (amount < 0) {
            System.out.println(amount + "$");
        } else {
            System.out.println("+" + amount + "$");
        }
        System.out.println(getDescription());
        System.out.println("Incoming Interest: " + incomingInterest);
        System.out.println("Outcoming Interest: " + outcomingInterest);
        System.out.println("--------------------------------------------");
    }

    /**
     * @return amount nach Interest
     */
    @Override
    public double calculate() {
        if (amount >= 0) {
            return (amount - incomingInterest * amount);
        }
        else
            return (amount + outcomingInterest * amount);
    }

    /**
     * @return den Inhalt aller Klassenattribute
     */
    @Override
    public String toString() {
        return "Date: " + date + ", Description: " + description + ", Amount: " + amount + " €" + ", Incoming Interest: " + incomingInterest + ", Outgoing Interest: " + outcomingInterest + "\n";
    }

    /**
     * Zwei Objekte der Klasse Transfer zum Vergleich
     * @param obj das zu vergleichende Objekt
     * @return true, wenn beide sind gleich sonst false
     */
    /**
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Payment payment)
            return (super.equals(payment) && incomingInterest == payment.incomingInterest
                    && outcomingInterest == payment.outcomingInterest);
        return false;
    }
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Payment payment = (Payment) o;
        return Double.compare(payment.incomingInterest, incomingInterest) == 0 && Double.compare(payment.outcomingInterest, outcomingInterest) == 0;
    }
}
