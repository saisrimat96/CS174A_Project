
import java.sql.*;
import java.util.Scanner;

public class Market{
	public static final String HOST = "jdbc:mysql://cs174a.engr.ucsb.edu:3306/srimatDB";
    public static final String USER = "srimat";
    public static final String PWD = "504";

	private boolean open = true;
	private String date = "3/16/2013";

	Connection connection = null;

	public Market()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
    		connection = DriverManager.getConnection(HOST,USER,PWD);
		} 
		catch(Exception e)
		{
			System.out.println(e);
		}
		System.out.println("imhere2");
		updateDate(date);
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
	}

	public void setMarketClose()
	{
		open = false;
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
		System.out.println("\nStock Market is Open now!");
		while(true){
			System.out.println("\nDo you wish to: ");
			System.out.println("(1) Set a new date for today");
			System.out.println("(2) Set a new price for a stock");
			System.out.println("(3) Close Market");
			System.out.println("(4) Quit Market");

			Scanner reader = new Scanner(System.in);
		  	String answer = reader.nextLine();
		  	String answer2;
		  	boolean quit = false;

			switch(answer){
		  		case "1":
		  			while(true) {
			  				System.out.println("\nEnter the date in XX/XX/XXXX format to be today's date.");
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
			  	case "2":
			  		break;
			  	case "3":
			  		while(true) {
			  				System.out.println("\nAre you sure to close the Market Enter 'y' for yes or 'n'for no.");
			  				String close = reader.nextLine();
			  				if(close.equals("y")) {
			  					setMarketClose();
				  				System.out.println("Market is closed now!");
				  				break;
				  			}
				  			else if(close.equals("n")) {
				  				break;
				  			}
				  	}
					break;
			  	case "4":
			  		quit = true;
			  		break;
			 	default:
			 		System.out.println("\nThe option you chose is not listed. Please provide a valid option.");
			 		break;
			}

			if(quit == true) {
		  		break;
		  	}
			System.out.println("\nDo you wish to go back to Market Interface? Enter 'y' for yes or 'q' to exit Market Interface.");
			answer2 = reader.nextLine();
			if(answer2.equals("y")) {
			continue;
			}	
		  	if(answer2.equals("q")) {
		  		break;
			}
  			else{
  				System.out.println("\nThe option you chose is not listed. Please provide a valid option.");
			}
  		}
	}
}