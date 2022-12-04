package bank;

import java.util.Objects;

public abstract class Transaction implements CalculateBill {
    /**
     * stellt den Zeitpunkt der Transaktion mit dem Format als DD.MM.YYYY dar
     */
    protected String date;
    /**
     * stellt die Geldmenge der Transaktion dar
     */
    protected double amount;
    /**
     * eine zusätzliche Beschreibung des Vorgangs
     */
    protected String description;

    //setter&getter
    /**
     * Legt das Attribut date fest
     * @param date neuer Wert für date
     */
    public void setDate(String date){
        this.date = date;
    }
    /**
     * @return den aktuellen Wert von date
     */
    public String get_date(){
        return date;
    }
    /**
     * Legt das Attribute description fest
     * @param description neuer Wert für description
     */
    public void setDescription(String description){
        this.description=description;
    }
    /**
     * @return den aktuellen Wert von description
     */
    public String getDescription(){
        return description;
    }
    /**
     * Legt das Attribut amount fest
     * @param amount neuer Wert für amount
     */
    public void setAmount(double amount){
        this.amount = amount;
    }
    /**
     * @return den aktuellen Wert von amount
     */
    public double getAmount() {
        return amount;
    }

    //konstruktoren
    /**
     * Defaultkonstruktor für abstrakte Klasse
     */
    public Transaction(){
    }
    /**
     * Konstruktor für abstrakte Klasse
     * @param date Wert für date
     * @param description Wert für description
     * @param amount Wert für amount
     */
    public Transaction(String date, String description, double amount){
        setDate(date);
        setDescription(description);
        setAmount(amount);
    }

    /**
     * @return den Inhalt aller Klassenattribute
     */
    public String toString() {
        return "Date: " + date + ", Description: " + description + ", Amount: " + amount + " €";
    }

    /**
     * Zwei Transaktionsobjekte zum Vergleich
     * @param obj das zu vergleichende Objekt
     * @return true, wenn beide sind gleich sonst false
     */
    /**public boolean equals(Object obj) {
        if (obj instanceof Transaction transaction)
            return (this.date.equals(transaction.date) &&
                    this.description.equals(transaction.description) && this.amount == transaction.amount);
        return false;
    }
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0 && Objects.equals(date, that.date) && Objects.equals(description, that.description);
    }

}
