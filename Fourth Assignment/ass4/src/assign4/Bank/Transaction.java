package assign4.Bank;

/**
 * Immutable class which stores transaction information.
 * Since all the immutable objects which are used
 * in this class are final, I don't think that getter methods
 * are necessary
 */

public class Transaction {

    public final Integer fromID;
    public final Integer toID;
    public final Integer amount;

    Transaction (int fromID, int toID, int amount) {
        this.fromID = fromID;
        this.toID = toID;
        this.amount = amount;
    }

    public boolean equals (Object o) {
        Transaction temp = (Transaction) o;
        return (temp.fromID == fromID && temp.toID == toID && temp.amount == amount);
    }

    Transaction (String[] ints) throws NumberFormatException {
        this (Integer.parseInt(ints[0]), Integer.parseInt(ints[1]), Integer.parseInt(ints[2]));
    }
}
