public class MarketAcc {
    private int account_id;
    private double balance;
    //private ArrayList<Transaction> trans;
    
    public MarketAcc(int id) {
        account_id = id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }
      
}