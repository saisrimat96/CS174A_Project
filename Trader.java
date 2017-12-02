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
			connection=DriverManager.getConnection(HOST,USER,PWD);
			Statement stmt=connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT balance FROM Market_Accounts WHERE taxId=" + taxid);
			if(rs.next()){
				double temp = rs.getDouble(1);
				System.out.println("Your Current Balance before Deposit: " + temp);
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
	        
	        String QUERY2 = "INSERT INTO `Transactions` (`transactionsId`,`date`,`type`,`amount`,`numShares`, `stockId`, `taxId`) VALUES (NULL, NULL,'Deposit'," + trans + ",NULL,NULL," + taxid + ");";
	        PreparedStatement myQuery1 = connection.prepareStatement(QUERY2);
	        myQuery1.executeUpdate();
	        myQuery1.close();
	        connection.close();

	        System.out.println("Your transaction was a success! New Current Balance is: " + amount);

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
			connection=DriverManager.getConnection(HOST,USER,PWD);
			Statement stmt=connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT balance FROM Market_Accounts WHERE taxId=" + taxid);
			if(rs.next()){
				double temp = rs.getDouble(1);
				System.out.println("Your Current Balance before Withdrawal: " + temp);
				amount = temp - trans;
			}
			rs.close();
			stmt.close();

			String QUERY1 = "update Market_Accounts set balance = ? where taxId = ?";
			


			PreparedStatement myQuery = connection.prepareStatement(QUERY1);
			myQuery.setDouble(1, amount);
			myQuery.setInt(2, taxid);
	        myQuery.executeUpdate();
	        myQuery.close();
	        
	        String QUERY2 = "INSERT INTO `Transactions` (`transactionsId`,`date`,`type`,`amount`,`numShares`, `stockId`, `taxId`) VALUES (NULL, NULL,'Deposit', -" + trans + ",NULL,NULL," + taxid + ");";
	        PreparedStatement myQuery1 = connection.prepareStatement(QUERY2);
	        myQuery1.executeUpdate();
	        myQuery1.close();
	        connection.close();

	        System.out.println("Your transaction was a success! New Current Balance is: " + amount);

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
				System.out.println("I'm sorry, but you do not have enough money to purchase these stocks.");
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
		        
		        String QUERY2 = "INSERT INTO `Transactions` (`transactionsId`,`date`,`type`,`amount`,`numShares`, `stockId`, `taxId`) VALUES (NULL, NULL,'Buy', -?, ?, NULL, ?);";
		        PreparedStatement myQuery1 = connection.prepareStatement(QUERY2);
		        myQuery1.setDouble(1,total_buy);
		        myQuery1.setInt(2,numShares);
		        myQuery1.setInt(3, taxid);
		        myQuery1.executeUpdate();
		        myQuery1.close();
		        connection.close();

		        System.out.println("Thank you! You have purchased:  ");
		        System.out.println(numShares + " shares of " + symbol);
		        System.out.println("Total cost of transaction: " + total_buy);
			}


		}
		catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }

	}
	/*

	public void Sell()
	{
		
	}

*/	
	//Show's current balance in trader's market account
	public void showBalance()
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
			ResultSet rs = stmt.executeQuery("SELECT balance FROM Market_Accounts WHERE taxId=" + taxid);
			if(rs.next()){
				double temp = rs.getDouble(1);
				System.out.println("Your Current Balance is: " + temp);
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
			if(rs.next()){
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
				System.out.println("Stock: " + sym + " ; Current Price: " + curr);
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
				System.out.println("Actor/Director Profile:-------");
				System.out.println("Name: " + name);
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
	

	// public void Movie()
	// {

	// }

	public void menu() 
	{
		while(true){
		
			Scanner reader = new Scanner(System.in);
			System.out.println("Welcome to the Trader Interface!");
			System.out.println("Do you wish to: ");
			System.out.println("(1) Make a Deposit");
			System.out.println("(2) Make a Withdrawal");
			System.out.println("(3) Buy Stocks");
			System.out.println("(4) Sell Stocks");
			System.out.println("(5) Show balance of your Market Account");
		  	String answer = reader.nextLine();

		  	switch(answer){
		  		case "1":
		  			System.out.println("How much money would you like to deposit?");
		  			String temp = reader.nextLine();
		  			Double amount = Double.parseDouble(temp);
		  			if(amount < 0){
		  				System.out.println("Please provide a dollar amount greater than 0");
		  				temp = reader.nextLine();
		  				amount = Double.parseDouble(temp);
		  				Deposit(taxid, amount);
		  			}
		  			else{
		  				Deposit(taxid, amount);
		  			}
		  		// case "2":
		  		// 	Withdrawal();
		  		// case "3":
		  		// 	Buy();
		 		default:
		 			System.out.println("The option you chose is not listed. Please provide a valid option");
		 			break;
		  	}

			// Scanner reader = new Scanner(System.in);
			// String []inputs = new String[2];
			// System.out.println("Welcome! Please provide a valid username: ");
		 //  	inputs[0] = reader.nextLine();
		 //  	System.out.println("and now a valid password: ");
			// inputs[1] = reader.nextLine();
			// String username = inputs[0];
			// String password = inputs[1];
			// System.out.println(username + " " + password);

			// Connection connection = null;
		 //    Statement statement = null;

			// try{
			// 	Class.forName("com.mysql.jdbc.Driver");
			// } 
			// catch(ClassNotFoundException e){
			// 	e.printStackTrace();
			// }
			
			// try{
			// 	connection=DriverManager.getConnection(HOST,USER,PWD);
			// 	String QUERY = "select username, password from Customer_Profile where username=? and password=?";

			// 	PreparedStatement myQuery = connection.prepareStatement(QUERY);
			// 	myQuery.setString(1, username);
			// 	myQuery.setString(2, password);
		 //        ResultSet resultSet = myQuery.executeQuery();
		 //        boolean empty = true;
		 //        while (resultSet.next()) {
		 //         	empty = false;
		 //            }
		 //        if(empty){
		 //        	System.out.println("Incorrect username or password. Please try again.");
		 //        }
		 //        else{
		 //        	System.out.println("Login successful! Welcome to your Trader Portal!");
		 //        }
		 //    }
		 //    catch(SQLException e)
		 //    {
		 //    	e.printStackTrace();
		 //    }
		}

    }

}