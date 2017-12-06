/*

Sai Srimat, Ziheng Song: Market Class

*/

import java.sql.*;
import java.util.Scanner;

public class Market{
	public static final String HOST = "jdbc:mysql://cs174a.engr.ucsb.edu:3306/srimatDB";
    public static final String USER = "srimat";
    public static final String PWD = "504";

	private boolean open = true;
	private String date = "";

	Connection connection = null;

	public Market()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
    		connection = DriverManager.getConnection(HOST,USER,PWD);
    		Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select market FROM Customer_Profile WHERE name = 'John Admin'");
			if(rs.next()){
				String market = rs.getString(1);
				if(market.equals("open")) {
					open = true;
					System.out.println("Stock Market is open now.");
				}
				else if(market.equals("closed")){
					open = false;
					System.out.println("Stock Market is closed now.");
				}
				else{
					setMarketOpen();
				}
			}
			rs = stmt.executeQuery("select today_date FROM Customer_Profile WHERE name = 'John Admin'");
			if(rs.next()){
				date = rs.getString(1);
				if(date.equals("")){
					updateDate("03/16/2013");
				}
				System.out.println("Today's date is: " + date);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public void updateDate(String newDate)
	{
		date = newDate;
		try{
			String query = "update Customer_Profile set today_date = '" + date + "' where name = 'John Admin'";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.executeUpdate();
			System.out.println("You have set today's date to " + date + ".");
			stmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public boolean isOpen()
	{
		if(open == true)
		{
			return true;
		}
		if(open == false)
		{
			return false;
		}
		return true;
	}

	public void setMarketOpen()
	{
		open = true;
		try{
			String query = "update Customer_Profile set market = 'open' where name = 'John Admin'";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.executeUpdate();
			System.out.println("\nStock Market is Open now!");
			stmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void setMarketClose()
	{
		open = false;
		try{
			String query = "update Customer_Profile set market = 'closed' where name = 'John Admin'";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.executeUpdate();
			System.out.println("\nStock Market is Closed now!");
			stmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void setDate(String newDate)
	{
		date = newDate;
		updateDate(newDate);
	}

	public void setStockPrice(String symbol, double newPrice)
	{
		try
		{
			String query = "update Stock set current_price = " + newPrice + " where symbol = '" + symbol + "'";
        	PreparedStatement stmt = connection.prepareStatement(query);
        	stmt.executeUpdate();
        	System.out.println("You have set the price of stock: " + symbol + " to: " + newPrice);
        	stmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void menu()
	{
		while(true){
			System.out.println("\nDo you wish to: ");
			System.out.println("(1) Open Market");
			System.out.println("(2) Close Market");
			System.out.println("(3) Set a new date for today");
			System.out.println("(4) Set a new price for a stock");
			System.out.println("(5) Exit Market");

			Scanner reader = new Scanner(System.in);
		  	String answer = reader.nextLine();
		  	String answer2;
		  	boolean quit = false;

			switch(answer){
				case "1":
					while(true) {
						System.out.println("\nAre you sure to Open the Market? Enter 'y' for yes or 'n'for no.");
		  				String close = reader.nextLine();
		  				if(close.equals("y")) {
		  					setMarketOpen();
			  				break;
			  			}
			  			else if(close.equals("n")) {
			  				break;
			  			}
					}
					break;
			  	case "2":
			  		while(true) {
		  				System.out.println("\nAre you sure to close the Market? Enter 'y' for yes or 'n'for no.");
		  				String close = reader.nextLine();
		  				if(close.equals("y")) {
		  					setMarketClose();
			  				break;
			  			}
			  			else if(close.equals("n")) {
			  				break;
			  			}
				  	}
					break;
				case "3":
		  			while(true) {
		  				System.out.println("\nEnter the date in XX/XX/XXXX format to be today's date:");
		  				String newDate = reader.nextLine();
		  				if(newDate.length() != 10) {
			  				System.out.println("Please provide a valid date.");
			  			}
			  			else{
			  				setDate(newDate);
			  				break;
			  			}
			  		}
		  			break;	
			  	case "4":
			  		while(true) {
		  				System.out.println("\nEnter the Symbol of the stock for which you wish to set a new price:");
		  				String symbol = reader.nextLine();
		  				System.out.println("\nEnter a new price that you wish to set for Stock " + symbol + ":");
		  				String price = reader.nextLine();
		  				if(symbol.length() != 3) {
			  				System.out.println("Please provide a correct Symbol.");
			  			}
			  			else{
			  				double newPrice = Double.parseDouble(price);
			  				setStockPrice(symbol, newPrice);
			  				break;
			  			}
			  		}
			  		break;
			  	case "5":
			  		quit = true;
			  		break;
			 	default:
			 		System.out.println("\nThe option you chose is not listed. Please provide a valid option.");
			 		break;
			}

			if(quit == true) {
		  		break;
		  	}
		  	while(true){
				System.out.println("\nDo you wish to go back to Market Interface? Enter 'y' for yes or 'q' to exit Market Interface.");
				answer2 = reader.nextLine();
				if(answer2.equals("y")) {
					break;
				}	
			  	if(answer2.equals("q")) {
			  		quit = true;
			  		break;
				}
	  			else{
	  				System.out.println("\nThe option you chose is not listed. Please provide a valid option.");
				}
			}
			if(quit == true) {
		  		break;
		  	}
  		}
	}
}