/*

Sai Srimat, Ziheng Song: Trader Class

*/


import java.sql.*;
import java.util.Scanner;


public class Trader
{
	String name;
	String username;
	String password;
	String state;
	String phone;
	String email;
	int taxid;
	public static final String HOST = "jdbc:mysql://cs174a.engr.ucsb.edu:3306/srimatDB";
    public static final String USER = "srimat";
    public static final String PWD = "504";
	
	
	//Constructor -
	public Trader(String username)
	{
		this.username = username;


		try
		{
			Class.forName("com.mysql.jdbc.Driver");  
    		Connection con=DriverManager.getConnection(HOST,USER,PWD);
			String query = "select * from Customer_Profile where username = ?";
			PreparedStatement stmt=con.prepareStatement(query);
			stmt.setString(1, username);
        	ResultSet rs=stmt.executeQuery();
        	while(rs.next())
        	{
        		name = rs.getString("name");
        		password = rs.getString("password");
        		state = rs.getString("STATE");
        		phone = rs.getString("Phone");
        		email = rs.getString("Email");
        		taxid = rs.getInt("taxId");
        	}
        	stmt.close();
        	con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	//Make Deposit: Takes in taxid and amount one wishes to deposit
	public void Deposit(int taxid, double amount)
	{
		Connection connection = null;
	    Statement statement = null;

		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		} 
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}

		try
		{
			double trans = amount;
			double temp = 0.0;
			connection=DriverManager.getConnection(HOST,USER,PWD);
			Statement stmt=connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT balance FROM Market_Accounts WHERE taxId=" + taxid);
			if(rs.next()){
				temp = rs.getDouble(1);
				System.out.println("\nYour Current Balance before Deposit: " + temp);
				amount += temp;
			}
			rs.close();
			stmt.close();

			String QUERY1 = "update Market_Accounts set balance = ? where taxId = ?";
			


			PreparedStatement myQuery = connection.prepareStatement(QUERY1);
			myQuery.setDouble(1, amount);
			myQuery.setInt(2, taxid);
	        myQuery.executeUpdate();
	        myQuery.close();
	        
	        String QUERY2 = "INSERT INTO `Transactions` (`transactionsId`,`date`,`type`,`amount`,`numShares`, `symbol`, `taxId`, `prev_balance`) VALUES (NULL, NULL,'Deposit'," + trans + ",NULL,NULL," + taxid + "," + temp + ");";
	        PreparedStatement myQuery1 = connection.prepareStatement(QUERY2);
	        myQuery1.executeUpdate();
	        myQuery1.close();
	        connection.close();

	        System.out.println("\nYour transaction was a success! New Current Balance is: " + amount);

	    }
	    catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	}

	//Make Withdrawal: Takes in taxid and amount one wishes to Withdrawal
	public void Withdrawal(int taxid, double amount)
	{
		Connection connection = null;
	    Statement statement = null;

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
			double trans = amount;
			double temp = 0.0;
			connection=DriverManager.getConnection(HOST,USER,PWD);
			Statement stmt=connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT balance FROM Market_Accounts WHERE taxId=" + taxid);
			if(rs.next()){
				temp = rs.getDouble(1);
				System.out.println("\nYour Current Balance before Withdrawal: " + temp);
				amount = temp - trans;
			}
			rs.close();
			stmt.close();

			if(amount < 0)
			{
				System.out.println("\nI'm very sorry, but you do not have enough money in your account to make this withdrawal");
			}
			else
			{
				String QUERY1 = "update Market_Accounts set balance = ? where taxId = ?";
				


				PreparedStatement myQuery = connection.prepareStatement(QUERY1);
				myQuery.setDouble(1, amount);
				myQuery.setInt(2, taxid);
		        myQuery.executeUpdate();
		        myQuery.close();
		        
		        String QUERY2 = "INSERT INTO `Transactions` (`transactionsId`,`date`,`type`,`amount`,`numShares`, `symbol`, `taxId`, `prev_balance`) VALUES (NULL, NULL,'Withdrawal', -" + trans + ",NULL,NULL," + taxid + ","+ temp + ");";
		        PreparedStatement myQuery1 = connection.prepareStatement(QUERY2);
		        myQuery1.executeUpdate();
		        myQuery1.close();
		        connection.close();

		        System.out.println("\nYour transaction was a success! New Current Balance is: " + amount);
			}
			

	    }
	    catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	}

	//Buy stocks
	public void Buy(int taxid, int numShares, String symbol)
	{
		int value = 20;
		double pre_balance = 0.0;
		double current_stock = 0.0;
		Connection connection = null;

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
			connection=DriverManager.getConnection(HOST,USER,PWD);
			Statement stmt=connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT balance FROM Market_Accounts WHERE taxId=" + taxid);
			while(rs.next())
			{
				pre_balance = rs.getDouble("balance");
			}


			String QUERY = "SELECT current_price FROM Stock WHERE symbol=?";
			PreparedStatement query = connection.prepareStatement(QUERY);
			query.setString(1, symbol);
			rs = query.executeQuery();

			if(rs.next()){
				current_stock = rs.getDouble(1);
			}

			double total_buy = (numShares * current_stock) + value;

			if(pre_balance < total_buy)
			{
				System.out.println("\nI'm sorry, but you do not have enough money to purchase these stocks.");
			}
			else
			{
				String QUERY1 = "update Market_Accounts set balance = ? where taxId = ?";
				double post_balance = pre_balance - total_buy;

				PreparedStatement myQuery = connection.prepareStatement(QUERY1);
				myQuery.setDouble(1, post_balance);
				myQuery.setInt(2, taxid);
		        myQuery.executeUpdate();
		        myQuery.close();
		        
		        String QUERY2 = "INSERT INTO `Transactions` (`transactionsId`,`date`,`type`,`amount`,`numShares`, `symbol`, `taxId`, `prev_balance`) VALUES (NULL, NULL,'Buy', -?, ?, ?, ?, ?);";
		        PreparedStatement myQuery1 = connection.prepareStatement(QUERY2);
		        myQuery1.setDouble(1,total_buy);
		        myQuery1.setInt(2,numShares);
		        myQuery1.setString(3, symbol);
		        myQuery1.setInt(4, taxid);
		        myQuery1.setDouble(5, pre_balance);
		        myQuery1.executeUpdate();
		        myQuery1.close();
		        connection.close();

		        System.out.println("\nThank you! You have purchased:  ");
		        System.out.println(numShares + " shares of " + symbol);
		        System.out.println("Total cost of transaction: " + total_buy);
			}


		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	//Sell stock. Takes in taxid, number of shares, and stock symbol
	public void Sell(int taxid, int numShares, String symbol)
	{
		int value = 20;
		double prev_price = 0.0;
		double curr_price = 0.0;

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
			connection=DriverManager.getConnection(HOST,USER,PWD);
			Statement stmt=connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT purchase_price FROM Stock_Accounts WHERE taxId=" + taxid + "and symbol="+symbol+");");
			while(rs.next())
			{
				prev_price = rs.getDouble(1);
			}

			String QUERY = "SELECT current_price FROM Stock WHERE symbol=?";
			PreparedStatement query = connection.prepareStatement(QUERY);
			query.setString(1, symbol);
			rs = query.executeQuery();

			if(rs.next()){
				curr_price = rs.getDouble(1);
			}

			
		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }

	}

	//Show's current balance in trader's market account
	public void showBalance()
	{
		Connection connection = null;
	    Statement statement = null;

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
			connection=DriverManager.getConnection(HOST,USER,PWD);
			Statement stmt=connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT balance FROM Market_Accounts WHERE taxId=" + taxid);
			if(rs.next()){
				double temp = rs.getDouble(1);
				System.out.println("\nYour Current Balance is: " + temp);
			}
			rs.close();
			stmt.close();
			connection.close();
		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	}

	//Show's trader's transaction history
	public void showTransactions()
	{
		Connection connection = null;
	    Statement statement = null;

		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		} 
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}

		try
		{
			connection=DriverManager.getConnection(HOST,USER,PWD);
			Statement stmt=connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Transactions WHERE taxId=" + taxid);
			System.out.println("\nTrans History for: " + name);
			while(rs.next()){
				int t1 = rs.getInt("transactionsId");
				String t2 = rs.getString("date");
				String t3 = rs.getString("type");
				Double t4 = rs.getDouble("amount");
				int t5 = rs.getInt("numShares");
				int t6 = rs.getInt("stockId");
				int t7 = rs.getInt("taxId");
				System.out.println("\ntransactionsID: " + t1 + ", date: " + t2 + ", type: " + t3 + ", amount: " + t4 + ", numShares: " + t5 + ", stockId: " + t6 + ", taxId: " + t7);

			}
			rs.close();
			stmt.close();
			connection.close();
		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	}

	//Retrieves current price of a stock and the Actor or Director's Profile
	public void showStockProf(String symbol)
	{
		Connection connection = null;
	    Statement statement = null;

		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		} 
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}

		try
		{
			connection=DriverManager.getConnection(HOST,USER,PWD);
			String query = "SELECT symbol, current_price FROM Stock where symbol=?";
			PreparedStatement myquery = connection.prepareStatement(query);
			myquery.setString(1, symbol);
			ResultSet rs = myquery.executeQuery();

			if(rs.next()){
				String sym = rs.getString(1);
				String curr = rs.getString(2);
				System.out.println("\nStock: " + sym + " ; Current Price: " + curr);
			}
			rs.close();
			myquery.close();

			String query2 = "SELECT name, role, dob FROM Actor_Director_Profile where symbol=?";
			PreparedStatement myquery2 = connection.prepareStatement(query2);
			myquery2.setString(1, symbol);
			ResultSet rs2 = myquery2.executeQuery();

			if(rs2.next())
			{
				String name = rs2.getString(1);
				String role = rs2.getString(2);
				String dob = rs2.getString(3);
				System.out.println();
				System.out.println("\nActor/Director Profile:-------");
				System.out.println("\nName: " + name);
				System.out.println("Role: " + role);
				System.out.println("Birthday: " + dob);
			}
			myquery2.close();
			rs2.close();
			connection.close();
		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	//Shows information about movie. Function takes in string: Movie Title
	public void Movie(String title)
	{
		Connection connection = null;
	    

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
			connection=DriverManager.getConnection("jdbc:mysql://cs174a.engr.ucsb.edu:3306/moviesDB",USER,PWD);
			String query = "SELECT title, rating, production_year FROM Movies WHERE title=?";
			PreparedStatement myquery = connection.prepareStatement(query);
			myquery.setString(1, title);
			ResultSet rs = myquery.executeQuery();
			if(rs.next()){
				String name = rs.getString("title");
				double rate = rs.getDouble("rating");
				int year = rs.getInt("production_year");
				
				System.out.println("\nHere is the information you requested: ");
				System.out.println("\nMovie title: " + name);
				System.out.println("Movie rating: " + rate);
				System.out.println("Movie production year: " + year);
			}
			rs.close();
			myquery.close();
			connection.close();
		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	}

	//Select movies that were rated five stars between certain number of years
	public void topMovie(int start, int end)
	{
		Connection connection = null;
	    
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
			connection=DriverManager.getConnection("jdbc:mysql://cs174a.engr.ucsb.edu:3306/moviesDB",USER,PWD);
			String query = "SELECT title, production_year FROM Movies WHERE production_year >= ? AND production_year <= ? AND rating=5.0";
			PreparedStatement myquery = connection.prepareStatement(query);
			myquery.setInt(1, start);
			myquery.setInt(2, end);
			ResultSet rs = myquery.executeQuery();
			if(rs.next()){
				String name = rs.getString("title");
				int year = rs.getInt("production_year");
				
				System.out.println("\n--------Movies that were rated 5.0 between years of " + start + " and " + end + "--------");
				System.out.println("\nTitle: " + name);
				System.out.println("Production year: " + year);
			}
			else
			{
				System.out.println("\nI'm sorry, there are no top movies in the years specified.");
			}
			rs.close();
			myquery.close();
			connection.close();
		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	}

	//Find reviews for a particular movie. Takes String input.
	public void reviews(String title)
	{
		Connection connection = null;
	    
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
			connection=DriverManager.getConnection("jdbc:mysql://cs174a.engr.ucsb.edu:3306/moviesDB",USER,PWD);
			String query = "SELECT author, review FROM Movies, Reviews WHERE title=? and Movies.id = Reviews.movie_id";
			PreparedStatement myquery = connection.prepareStatement(query);
			myquery.setString(1, title);
			ResultSet rs = myquery.executeQuery();
			System.out.println("\nHere are reviews for the movie: " + title);
			while(rs.next()){
				String author = rs.getString("author");
				String review = rs.getString("review");
				System.out.println("Author: " + author + " said " + review);
			}
			rs.close();
			myquery.close();
			connection.close();
		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	}

	public void menu() 
	{
		while(true){
		
			Scanner reader = new Scanner(System.in);
			System.out.println("----------Welcome to the Trader Interface!----------");

			System.out.println("\nDo you wish to: ");


			System.out.println("(1) Make a Deposit");
			System.out.println("(2) Make a Withdrawal");
			System.out.println("(3) Buy Stocks");
			System.out.println("(4) Sell Stocks");
			System.out.println("(5) View balance of your Market Account");
			System.out.println("(6) View your transactions");
			System.out.println("(7) Movie information");
			System.out.println("or 'q' to exit");
		  	String answer = reader.nextLine();

		  	switch(answer){
		  		case "1":
		  			System.out.println("\nHow much money would you like to deposit?");
		  			String temp = reader.nextLine();
		  			Double amount = Double.parseDouble(temp);
		  			if(amount < 0){
		  				System.out.println("Please provide a dollar amount greater than 0");
		  				temp = reader.nextLine();
		  				amount = Double.parseDouble(temp);
		  				Deposit(taxid, amount);	
		  				break;	
		  			}
		  			else{
		  				Deposit(taxid, amount);
		  				break;	  				
		  			}

		  		case "2":
		  			System.out.println("\nHow much money would you like to withdraw?");
		  			temp = reader.nextLine();
		  			amount = Double.parseDouble(temp);
		  			if(amount < 0){
		  				System.out.println("Please provide a dollar amount greater than 0");
		  				temp = reader.nextLine();
		  				amount = Double.parseDouble(temp);
		  				Withdrawal(taxid, amount);
		  				break;		  				
		  			}
		  			else{
		  				Withdrawal(taxid, amount);
		  				break;
		  			}
		  			

		  		case "3":
		  			System.out.println("\nPlease input the symbol you desire to purchase: ");
		  			String sym= reader.nextLine();
		  			System.out.println("Next...How many shares do you wish to purchase?: ");
		  			temp = reader.nextLine();
		  			int amt = Integer.parseInt(temp);
		  			if(amt < 0){
		  				System.out.println("Please provide a number greater than 0");
		  				temp = reader.nextLine();
		  				amt = Integer.parseInt(temp);
		  				Buy(taxid, amt, sym);
		  				break;
		  			}
		  			else{
		  				Buy(taxid, amt, sym);
		  				break;	
		  			}
		  			

		  		// case "4":
		  		//
		  		case "5":
		  			showBalance();
		  			break;
		  			

		  		case "6":
		  			showTransactions();
		  			break;
		  			

		  		case "7":
		  			System.out.println("--------Movie Information--------");
		  			System.out.println("Do you wish to view: ");
		  			System.out.println("(1) View information about a specific movie?");
		  			System.out.println("(2) Discover top rated movies");
		  			System.out.println("(3) View movie reviews");
		  			String ans = reader.nextLine();
		  			if(ans.equals("1"))
		  			{
		  				System.out.println("Please input a movie title: ");
		  				temp = reader.nextLine();
		  				Movie(temp);
		  				break;
		  			}
		  			else if(ans.equals("2"))
		  			{
		  				System.out.println("Please input a start year: ");
		  				String s = reader.nextLine();
		  				int start = Integer.parseInt(s);
		  				System.out.println("Please input an end year: ");
		  				String e = reader.nextLine();
		  				int end = Integer.parseInt(e);
		  				topMovie(start,end);
		  				break;
		  			}
		  			else if(ans.equals("3"))
		  			{
		  				System.out.println("Please input a movie title: ");
		  				temp = reader.nextLine();
		  				reviews(temp);
		  				break;
		  			}

				case "q":
					System.out.println("\nHave a nice day!");
					System.exit(0);


		 		default:
		 			System.out.println("The option you chose is not listed. Please provide a valid option");
		 			break;
		  	}

		  	while(true) {
		  				System.out.println("Do you wish to go back to Trader Menu? Enter 'y' for yes or 'q' to exit Manager Interface.");
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
		
		  	
		}

    }

}