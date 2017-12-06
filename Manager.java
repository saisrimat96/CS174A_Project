/*

Sai Srimat, Ziheng Song: Manager Class

*/

import java.sql.*;
import java.util.*;

public class Manager
{
	private String username;
	Connection connection = null;

	public static final String HOST = "jdbc:mysql://cs174a.engr.ucsb.edu:3306/srimatDB";
    public static final String USER = "srimat";
    public static final String PWD = "504";
	
	//Constructor
	public Manager(String username)
	{
		this.username = username;

		try
		{
			Class.forName("com.mysql.jdbc.Driver");
    		connection = DriverManager.getConnection(HOST,USER,PWD);
		} 
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public void closeConnection()
	{
		try
		{
			connection.close();
		} 
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public void addInterest()
	{
		try
		{	
			ArrayList<Integer> taxidArray = new ArrayList<Integer>();
			Statement stmt0 = connection.createStatement();
			ResultSet rs = stmt0.executeQuery("SELECT taxId FROM Customer_Profile");
			while(rs.next()){
				taxidArray.add(rs.getInt(1));
			}
			stmt0.close();
			int taxid = 0;

			//Loop through all customers
			for (int n = 0; n < taxidArray.size(); n++){
				taxid = taxidArray.get(n);
				//Find initial balance
				double initial_balance = 0.0;
				Statement stmt = connection.createStatement();
				rs = stmt.executeQuery("SELECT prev_balance FROM Transactions WHERE transactionsId = (select MIN(transactionsId) FROM Transactions WHERE taxId = " + taxid + ")");
				if(rs.next()){
					initial_balance = rs.getDouble(1);
				}
				stmt.close();

				//Find final balance
				double final_balance = 0.0;
				Statement stmt2 = connection.createStatement();
				rs = stmt2.executeQuery("SELECT new_balance FROM Transactions WHERE transactionsId = (select MAX(transactionsId) FROM Transactions WHERE taxId = " + taxid + ")");
				if(rs.next()){
					final_balance = rs.getDouble(1);
				}
				stmt2.close();

				//Calculate the total earning/loss
				double total = 0.0;
				Statement stmt3 = connection.createStatement();
				rs = stmt3.executeQuery("SELECT SUM(earnings) FROM Transactions WHERE taxId = " + taxid);
				if(rs.next()){
					total = rs.getDouble(1);
				}
				stmt3.close();

				Statement stmt4 = connection.createStatement();
				rs = stmt4.executeQuery("SELECT Transactions.date, Transactions.new_balance FROM Transactions INNER JOIN ( SELECT date, MAX(transactionsId) AS latest FROM Transactions WHERE taxId = " + taxid + " GROUP BY date) AS newgroup ON newgroup.date = Transactions.date AND newgroup.latest = Transactions.transactionsId");

				ArrayList<String> dateArray = new ArrayList<String>();
				ArrayList<Double> balanceArray = new ArrayList<Double>();
				while(rs.next()){
					dateArray.add(rs.getString(1));
					balanceArray.add(rs.getDouble(2));
				}
				stmt4.close();
				double new_initial_balance = initial_balance;

				//Calculate the average daily balance
				int startDate = 1;
				double accrue_total = 0.0;
				int nowDate = 0;
				for (int i = 0; i < dateArray.size(); i++){
					nowDate = Integer.parseInt(dateArray.get(i).substring(4,5));
					accrue_total += new_initial_balance * (nowDate - startDate);
					startDate = nowDate;
					new_initial_balance = balanceArray.get(i);
				}
				accrue_total += final_balance * (30 - startDate);
				double average = accrue_total / 30;
				double accrue_interest = average * 1.03;

				String query = "update Market_Accounts SET balance = (balance + " + accrue_interest + ") where taxId = " + taxid;
	        	PreparedStatement stmt5 = connection.prepareStatement(query);
	        	stmt5.executeUpdate();
	        	stmt5.close();

	        	//Get today's date to update transactions
	        	String today_date = "";
	        	Statement stmt6 = connection.createStatement();
	        	rs = stmt6.executeQuery("select today_date FROM Customer_Profile WHERE name = 'John Admin'");
				if(rs.next()){
					today_date = rs.getString(1);
				}
				stmt6.close();
				System.out.println(final_balance + " " + accrue_interest);

	        	String query2 = "INSERT INTO `Transactions` (`transactionsId`,`date`,`type`,`amount`,`numShares`, `symbol`, `taxId`, `prev_balance`, `new_balance`, `earnings`) VALUES (NULL, ?,'Interest', ?, NULL, NULL, ?, ?, ?, ?);";
		        PreparedStatement stmt7 = connection.prepareStatement(query2);
		        stmt7.setString(1, today_date);
		        stmt7.setDouble(2, accrue_interest);
		        stmt7.setInt(3, taxid);
		       	stmt7.setDouble(4, initial_balance);
		       	stmt7.setDouble(5, (final_balance + accrue_interest));
		       	stmt7.setDouble(6, accrue_interest);
		        stmt7.executeUpdate();
		        stmt7.close();

	        	rs.close();
	        }
	        System.out.println("You have added interest for all customers.");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void genStatement(String name)
	{
		int taxid = 0;
		try
		{
			//Find the taxId and email of a given customer name from Customer_Profile
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Email, taxId FROM Customer_Profile WHERE name = '" + name + "'");
			if(rs.next()){
				String email = rs.getString(1);
				taxid = rs.getInt(2);
				System.out.println("Name: " + name);
				System.out.println("Email: " + email);
			}
			stmt.close();

	    	//List all transactions
			Statement stmt2 = connection.createStatement();
			rs = stmt2.executeQuery("SELECT * FROM Transactions WHERE taxId = " + taxid);
			while(rs.next()){
				int t1 = rs.getInt("transactionsId");
				String t2 = rs.getString("date");
				String t3 = rs.getString("type");
				Double t4 = rs.getDouble("amount");
				int t5 = rs.getInt("numShares");
				int t6 = rs.getInt("symbol");
				int t7 = rs.getInt("taxId");
				System.out.println("Trans History for: " + name);
				System.out.println("transactionsID: " + t1 + ", date: " + t2 + ", type: " + t3 + ", amount: " + t4 + ", numShares: " + t5 + ", symbol: " + t6 + ", taxId: " + t7);
			}
			stmt2.close();

			//Show initial balance
			double initial_balance = 0.0;
			Statement stmt3 = connection.createStatement();
			rs = stmt3.executeQuery("SELECT prev_balance FROM Transactions WHERE transactionsId = (select MIN(transactionsId) FROM Transactions WHERE taxId = " + taxid + ")");
			if(rs.next()){
				initial_balance = rs.getDouble(1);
				System.out.println("Initial balance: " + initial_balance);
			}
			stmt3.close();

			//Show final balance
			double final_balance = 0.0;
			Statement stmt4 = connection.createStatement();
			rs = stmt4.executeQuery("SELECT new_balance FROM Transactions WHERE transactionsId = (select MAX(transactionsId) FROM Transactions WHERE taxId = " + taxid + ")");
			if(rs.next()){
				final_balance = rs.getDouble(1);
				System.out.println("Final balance: " + final_balance);
			}
			stmt4.close();

			//Show the total earning/loss
			double total = 0.0;
			Statement stmt5 = connection.createStatement();
			rs = stmt5.executeQuery("SELECT SUM(earnings) FROM Transactions WHERE taxId = " + taxid);
			if(rs.next()){
				total = rs.getDouble(1);
			}
			stmt5.close();

			Statement stmt6 = connection.createStatement();
			rs = stmt6.executeQuery("SELECT Transactions.date, Transactions.new_balance FROM Transactions INNER JOIN ( SELECT date, MAX(transactionsId) AS latest FROM Transactions WHERE taxId = " + taxid + " GROUP BY date) AS newgroup ON newgroup.date = Transactions.date AND newgroup.latest = Transactions.transactionsId");

			ArrayList<String> dateArray = new ArrayList<String>();
			ArrayList<Double> balanceArray = new ArrayList<Double>();
			while(rs.next()){
				dateArray.add(rs.getString(1));
				balanceArray.add(rs.getDouble(2));
			}
			stmt6.close();
			rs.close();

			//Calculate the average daily balance
			int startDate = 1;
			double totalInterest = 0.0;
			double balance = initial_balance;
			int nowDate = 0;
			for (int i = 0; i < dateArray.size(); i++){
				nowDate = Integer.parseInt(dateArray.get(i).substring(4,5));
				totalInterest += initial_balance * (nowDate - startDate);
				startDate = nowDate;
				initial_balance = balanceArray.get(i);
			}
			totalInterest += final_balance * (30 - startDate);
			double average = totalInterest / 30;
			double accrue_interest = average * 1.03;

			//Calculate total earnings/loss (including interst)
			total += accrue_interest;

			if(total >= 0){
				System.out.println("Total earnings: " + total);
			}
			else if(total < 0){
				System.out.println("Total loss: " + (-1) * total);
			}
		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	}

	public void listActive()
	{
		try
		{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select Customer_Profile.name, (select sum(Transactions.numShares) from Transactions where Transactions.taxId = Customer_Profile.taxId) as sharesSum from Customer_Profile group by taxId, sharesSum having sharesSum >= 1000");
			while(rs.next()){
				String name = rs.getString(1);
				int shares_sum = rs.getInt(2);
				System.out.println("Name: " + name + ", Total Shares traded: " + shares_sum);
			}
			rs.close();
			stmt.close();

		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	}

	public void genDTER()
	{
		try
		{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select Customer_Profile.name, Customer_Profile.STATE, (select sum(Transactions.earnings) from Transactions where Transactions.taxId = Customer_Profile.taxId) as earningsSum from Customer_Profile group by taxId, earningsSum having earningsSum >= 10000");
			while(rs.next()){
				String name = rs.getString(1);
				String state = rs.getString(2);
				double earnings_sum = rs.getDouble(3);
				System.out.println("Name: " + name + ", State: " + state + ", Total Earnings: " + earnings_sum);
			}
			rs.close();
			stmt.close();

		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	}

	public void report(String name)
	{
		int taxid = 0;
		try
		{
			//Find the taxId of a given customer name from Customer_Profile
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT taxId FROM Customer_Profile WHERE name = '" + name + "'");
			if(rs.next()){
				taxid = rs.getInt(1);
				System.out.println("Name: " + name);
			}
			stmt.close();

	    	//List market account associated with the customer
			Statement stmt2 = connection.createStatement();
			rs = stmt2.executeQuery("SELECT balance FROM Market_Accounts WHERE taxId = " + taxid);
			if(rs.next()){
				Double balance = rs.getDouble(1);
				System.out.println("Market Account information for: " + name + ", taxId: " + taxid);
				System.out.println("Balance: " + balance);
			}
			stmt2.close();

	    	//List stock account associated with the customer
	    	System.out.println("Stock Account information for: " + name + ", taxId: " + taxid);
	    	Double balance2 = 0.0;
	    	String symbol = "";
			Statement stmt3 = connection.createStatement();
			rs = stmt3.executeQuery("SELECT balance, symbol FROM Stock_Accounts WHERE taxId = " + taxid);
			while(rs.next()){
				balance2 = rs.getDouble(1);
				symbol = rs.getString(2);
				System.out.println("Stock symbol: " + symbol + ", balance: " + balance2);
			}
			rs.close();
			stmt3.close();

		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }

	}

	public void deleteTransactions()
	{
		try
		{
			String query = "delete from Transactions";
        	PreparedStatement stmt = connection.prepareStatement(query);
        	stmt.executeUpdate();
        	System.out.println("You have deleted transactions from all accounts.");
        	stmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void menu()
	{
		System.out.println("\n----------Welcome to the Manager Interface!----------");
		while(true){
			System.out.println("\nDo you wish to: ");
			System.out.println("(1) Add Interest for all Market Accounts");
			System.out.println("(2) Generate Monthly Statement for a particular customer");
			System.out.println("(3) List Active Customers");
			System.out.println("(4) Generate Government Drug & Tax Evasion Report(DTER)");
			System.out.println("(5) Generate Customer Report");
			System.out.println("(6) Delete Transactions from all Accounts");
			System.out.println("(7) Quit Manager Interface");

			Scanner reader = new Scanner(System.in);
		  	String answer = reader.nextLine();

		  	switch(answer){
		  		case "1":
		  			while(true) {
		  				System.out.println("\nAre you sure to add Interest for all Market Accounts? Enter 'y' for yes or 'n' for no.");
		  				String answer1 = reader.nextLine();
		  				if(answer1.equals("y")) {
			  				addInterest();
			  				break;
			  			}
			  			else if(answer1.equals("n")) {
			  				break;
			  			}
			  			else{
			  				System.out.println("\nThe option you chose is not listed. Please provide a valid option.");
			  			}
			  		}
		  			break;
		  		case "2":
		  			System.out.println("\nWhich customer's statement would you like to generate?");
		  			String name2 = reader.nextLine();
		  			genStatement(name2);
		  			break;
		  		case "3":
		  			listActive();
		  			break;
		  		// case "4":
		  		// 	genDTER();
		  		case "5":
		  			System.out.println("\nWhich customer's report would you like to check?");
		  			String name5 = reader.nextLine();
		  			report(name5);
		  			break;
		  		case "6":
		  			while(true) {
			  			System.out.println("\nAre you sure to delete Transactions from all Accounts? Enter 'y' for yes or 'n' for no.");
			  			String answer6 = reader.nextLine();
			  			if(answer6.equals("y")) {
			  				deleteTransactions();
			  				break;
			  			}
			  			else if(answer6.equals("n")) {
			  				break;
			  			}
			  			else{
			  				System.out.println("\nThe option you chose is not listed. Please provide a valid option.");
			  			}
			  		}
		  			break;
		  		case "7":
		  			System.exit(0);
		 		default:
		 			System.out.println("\nThe option you chose is not listed. Please provide a valid option.");
		 			break;
		  	}

		  	while(true) {
		  		System.out.println("\nDo you wish to go back to Manager Interface? Enter 'y' for yes or 'q' to exit Manager Interface.");
		  		String answer2 = reader.nextLine();
		  		if(answer2.equals("q")) {
		  			System.exit(0);
		  		}
		  		else if(answer2.equals("y")) {
		  			break;
				}
  				else{
  					System.out.println("\nThe option you chose is not listed. Please provide a valid option.");
  				}
  			}
		}
	}
}