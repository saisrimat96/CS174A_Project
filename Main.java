import java.io.*;




public class Main{
	public static void main(String [ ] args)
	{
		Trader trader = new Trader("sai");
		//trader.Deposit(1022, 300.00);
		//trader.Withdrawal(100, 200.00);
		//trader.showBalance();
		//trader.showTransactions();
		//trader.showStockProf("SMD");
		//trader.Buy(100, 3, "STC");
		trader.menu();
	}
}
