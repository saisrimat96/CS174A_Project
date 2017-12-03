/*

Sai Srimat, Ziheng Song: Manager Class

*/

import java.sql.*;
import java.util.Scanner;


public class Manager
{
	String username;

	public static final String HOST = "jdbc:mysql://cs174a.engr.ucsb.edu:3306/srimatDB";
    public static final String USER = "srimat";
    public static final String PWD = "504";
	
	
	//Constructor
	public Manager(String username)
	{
		this.username = username;
	}

	public void addInterest()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
    		Connection con = DriverManager.getConnection(HOST,USER,PWD);
			String query = "update Market_Accounts SET balance = (balance * 1.03)";
        	PreparedStatement stmt = con.prepareStatement(query);
        	stmt.executeUpdate();
        	System.out.println("You have added interest for all customers.");
        	stmt.close();
        	con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public void genStatement(String name)
	{
		Connection connection = null;
		int taxid = 0;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		} 
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		try
		{
			//Find the taxId and email of a given customer name from Customer_Profile
			connection = DriverManager.getConnection(HOST,USER,PWD);
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
			rs = stmt2.executeQuery("SELECT * FROM Transactions WHERE taxId=" + taxid);
			while(rs.next()){
				int t1 = rs.getInt("transactionsId");
				String t2 = rs.getString("date");
				String t3 = rs.getString("type");
				Double t4 = rs.getDouble("amount");
				int t5 = rs.getInt("numShares");
				int t6 = rs.getInt("stockId");
				int t7 = rs.getInt("taxId");
				System.out.println("Trans History for: " + name);
				System.out.println("transactionsID: " + t1 + ", date: " + t2 + ", type: " + t3 + ", amount: " + t4 + ", numShares: " + t5 + ", stockId: " + t6 + ", taxId: " + t7);
			}

			rs.close();
			stmt2.close();
			connection.close();
		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	}

	// public void listActive()
	// {
	// 	Connection connection = null;
	// 	int taxid = 0;
	// 	try
	// 	{
	// 		Class.forName("com.mysql.jdbc.Driver");
	// 	} 
	// 	catch(ClassNotFoundException e){
	// 		e.printStackTrace();
	// 	}

	// 	try
	// 	{
	// 		//Find the taxId and email of a given customer name from Customer_Profile
	// 		connection = DriverManager.getConnection(HOST,USER,PWD);
	// 		Statement stmt = connection.createStatement();
	// 		ResultSet rs = stmt.executeQuery("SELECT Email, taxId FROM Customer_Profile WHERE name = '" + name + "'");
	// 		if(rs.next()){
	// 			String email = rs.getString(1);
	// 			taxid = rs.getInt(2);
	// 			System.out.println("Name: " + name);
	// 			System.out.println("Email: " + email);
	// 		}
	// 		rs.close();
	// 		stmt.close();

	// 	}
	// 	catch(SQLException e)
	//     {
	//     	e.printStackTrace();
	//     }

	// 	try
	// 	{
	//     	//List all transactions
	// 		Statement stmt2 = connection.createStatement();
	// 		ResultSet rs2 = stmt2.executeQuery("SELECT * FROM Transactions WHERE taxId=" + taxid);
	// 		if(rs2.next()){
	// 			int t1 = rs2.getInt("transactionsId");
	// 			String t2 = rs2.getString("date");
	// 			String t3 = rs2.getString("type");
	// 			Double t4 = rs2.getDouble("amount");
	// 			int t5 = rs2.getInt("numShares");
	// 			int t6 = rs2.getInt("stockId");
	// 			int t7 = rs2.getInt("taxId");
	// 			System.out.println("Trans History for: " + name);
	// 			System.out.println("transactionsID: " + t1 + ", date: " + t2 + ", type: " + t3 + ", amount: " + t4 + ", numShares: " + t5 + ", stockId: " + t6 + ", taxId: " + t7);
	// 		}

	// 		rs2.close();
	// 		stmt2.close();
	// 		connection.close();
	// 	}
	// 	catch(SQLException e)
	//     {
	//     	e.printStackTrace();
	//     }
	// }

	// public void genDTER()
	// {

	// }

	public void report(String name)
	{
		Connection connection = null;
		int taxid = 0;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		} 
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		try
		{
			//Find the taxId of a given customer name from Customer_Profile
			connection = DriverManager.getConnection(HOST,USER,PWD);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT taxId FROM Customer_Profile WHERE name = '" + name + "'");
			if(rs.next()){
				taxid = rs.getInt(1);
				System.out.println("Name: " + name);
			}
			stmt.close();

	    	//List market account associated with the customer
			Statement stmt2 = connection.createStatement();
			rs = stmt2.executeQuery("SELECT balance FROM Market_Accounts WHERE taxId=" + taxid);
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
			rs = stmt3.executeQuery("SELECT balance, symbol FROM Stock_Accounts WHERE taxId=" + taxid);
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
			Class.forName("com.mysql.jdbc.Driver");
    		Connection con = DriverManager.getConnection(HOST,USER,PWD);
			String query = "delete from Transactions";
        	PreparedStatement stmt = con.prepareStatement(query);
        	stmt.executeUpdate();
        	System.out.println("You have deleted transactions from all accounts.");
        	stmt.close();
        	con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public void menu()
	{
		while(true){
			System.out.println("----------Welcome to the Manager Interface!----------");
			System.out.println("Do you wish to: ");
			System.out.println("(1) Add Interest for all Market Accounts");
			System.out.println("(2) Generate Monthly Statement for a particular customer");
			System.out.println("(3) List Active Customers");
			System.out.println("(4) Generate Government Drug & Tax Evasion Report(DTER)");
			System.out.println("(5) Generate Customer Report");
			System.out.println("(6) Delete Transactions from all Accounts");

			Scanner reader = new Scanner(System.in);
		  	String answer = reader.nextLine();
		  	Connection connection = null;
			Statement statement = null;

		  	switch(answer){
		  		case "1":
		  			while(true) {
		  				System.out.println("Are you sure to add Interest for all Market Accounts? Enter 'y' for yes or 'n' for no.");
		  				String answer1 = reader.nextLine();
		  				if(answer1.equals("y")) {
			  				addInterest();
			  				break;
			  			}
			  			else if(answer1.equals("n")) {
			  				break;
			  			}
			  			else{
			  				System.out.println("The option you chose is not listed. Please provide a valid option.");
			  			}
			  		}
		  			break;
		  		case "2":
		  			System.out.println("Which customer's statement would you like to generate?");
		  			String name2 = reader.nextLine();
		  			genStatement(name2);
		  			while(true) {
		  				System.out.println("Do you wish to go back to manager menu? Enter 'y' for yes or 'q' to exit Manager Interface.");
		  				String answer2 = reader.nextLine();
		  				if(answer2.equals("q")) {
		  					System.exit(0);
		  				}
		  				else if(answer2.equals("y")) {
		  					break;
		  				}
		  				else{
		  					System.out.println("The option you chose is not listed. Please provide a valid option.");
		  				}
		  			}
		  			break;
		  		// case "3":
		  		// 	listActive();
		  		// case "4":
		  		// 	genDTER();
		  		case "5":
		  			System.out.println("Which customer's report would you like to check?");
		  			String name5 = reader.nextLine();
		  			report(name5);
		  			while(true) {
		  				System.out.println("Do you wish to go back to manager menu? Enter 'y' for yes or 'q' to exit Manager Interface.");
		  				String answer2 = reader.nextLine();
		  				if(answer2.equals("q")) {
		  					System.exit(0);
		  				}
		  				else if(answer2.equals("y")) {
		  					break;
		  				}
		  				else{
		  					System.out.println("The option you chose is not listed. Please provide a valid option.");
		  				}
		  			}
		  			break;
		  		case "6":
		  			while(true) {
			  			System.out.println("Are you sure to delete Transactions from all Accounts? Enter 'y' for yes or 'n' for no.");
			  			String answer6 = reader.nextLine();
			  			if(answer6.equals("y")) {
			  				deleteTransactions();
			  				break;
			  			}
			  			else if(answer6.equals("n")) {
			  				break;
			  			}
			  			else{
			  				System.out.println("The option you chose is not listed. Please provide a valid option.");
			  			}
			  		}
		  			break;
		 		default:
		 			System.out.println("The option you chose is not listed. Please provide a valid option");
		 			break;
		  	}
		}
	}
}