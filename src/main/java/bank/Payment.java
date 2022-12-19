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
    private double outgoingInterest=0;

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
     * @param outgoingInterest neuer Wert für outgoingInterest
     */
    public void setOutgoingInterest(double outgoingInterest)throws TransactionAttributeException{
        if(0 <= outgoingInterest & outgoingInterest < 1) {
            this.outgoingInterest = outgoingInterest;
        }
        else{
            this.outgoingInterest = outgoingInterest;
            throw new TransactionAttributeException("OUTGOING INTEREST MUST BE IN BETWEEN 0 AND 1!\n");

        }
    }
    /**
     * @return den aktuellen Wert von outcomingInterest
     */
    public double getOutgoingInterest(){
        return outgoingInterest;
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
     * @param newDate Wert für date
     * @param newDescription Wert für description
     * @param newAmount Wert für amount
     */
    public Payment (String newDate, String newDescription, double newAmount) {
        super(newDate, newDescription, newAmount);
    }

    /**
     * Konstruktor mit aller Attribute
     * @param newDate Wert für date
     * @param newDescription Wert für description
     * @param newAmount Wert für amount
     * @param newIncomingInterest Wert für incomingInterest
     * @param newOutgoingInterest Wert für outgoingInterest
     */
    public Payment (String newDate, String newDescription, double newAmount, double newIncomingInterest, double newOutgoingInterest) {
        this(newDate, newDescription, newAmount);
        this.incomingInterest = newIncomingInterest;
        this.outgoingInterest = newOutgoingInterest;
    }

    /**
     * Copy Constructor
     * @param newPayment neue Objekt festzulegen
     */
    public Payment (Payment newPayment) {
        this(newPayment.date, newPayment.description, newPayment.amount, newPayment.incomingInterest, newPayment.outgoingInterest);
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
        System.out.println("Outgoing Interest: " + outgoingInterest);
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
            return (amount + outgoingInterest * amount);
    }

    /**
     * @return den Inhalt aller Klassenattribute
     */
    @Override
    public String toString() {
        return "Date: " + date + ", Description: " + description + ", Amount: " + amount + " €" + ", Incoming Interest: " + incomingInterest + ", Outgoing Interest: " + outgoingInterest + "\n";
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
        return Double.compare(payment.incomingInterest, incomingInterest) == 0 && Double.compare(payment.outgoingInterest, outgoingInterest) == 0;
    }
}
