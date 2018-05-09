package assign4.Bank;

/**
 * Account class takes care of storing account
 * information and since it's data might be changed
 * by several threads it's changeBalance method
 * is synchronised
 */

public class Account {
    private Integer ID;
    private int balance;
    private int nOfTransactions;

    Account (int deposit, int ID) {
        this.balance = new Integer(deposit);
        this.ID = new Integer(ID);
        this.nOfTransactions = 0;
    }

    /**
     * Synchronised method for changing balance
     * @param difference
     */
    public synchronized void changeBalance (Integer difference) {
        balance += difference;
        nOfTransactions ++;
    }

    @Override
    public String toString () {
       return "acct:" + ID + " bal:" + balance + " trans:" + nOfTransactions;
    }

}
