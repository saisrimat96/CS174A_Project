/*

Sai Srimat, Ziheng Song: Manager Class

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
	
	
	//Constructor
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

	// public void Deposit(int taxid, double amount)
	// {
	// 	Connection connection = null;
	//     Statement statement = null;

	// 	try
	// 	{
	// 		Class.forName("com.mysql.jdbc.Driver");
	// 	} 
	// 	catch(ClassNotFoundException e){
	// 		e.printStackTrace();
	// 	}

	// 	try
	// 	{
	// 		connection=DriverManager.getConnection(HOST,USER,PWD);
	// 		String QUERY1 = "update Market_Accounts set balance = ";
	// 		String QUERY2 = "";

	// 		PreparedStatement myQuery = connection.prepareStatement(QUERY);
	// 		myQuery.setString(1, username);
	// 		myQuery.setString(2, password);
	//         ResultSet resultSet = myQuery.executeQuery();
	//         boolean empty = true;
	//         while (resultSet.next()) {
	//          	empty = false;
	//             }
	//         if(empty){
	//         	System.out.println("Incorrect username or password. Please try again.");
	//         }
	//         else{
	//         	System.out.println("Login successful! Welcome to your Trader Portal!");
	//         }
	//     }
	//     catch(SQLException e)
	//     {
	//     	e.printStackTrace();
	//     }


	// }
	/*
	public void Withdrawal()
	{
		
	}

	public void Buy()
	{
		
	}

	public void Sell()
	{
		
	}

	public double getBalance()
	{
	
	}

	public double showBalance()
	{

	}

	public void showTransactions()
	{

	}

	public void showCurrPrice()
	{

	}

	public void Movie()
	{

	}*/

	public void menu() 
	{
		// while(True){
		
		// 	Scanner reader = new Scanner(System.in);
		// 	System.out.println("Welcome to the Trader Interface!");
		// 	System.out.println("Do you wish to: ");
		// 	System.out.println("(1) Make a Deposit");
		// 	System.out.println("(2) Make a Withdrawal");
		// 	System.out.println("(3) Buy Stocks");
		// 	System.out.println("(4) Sell Stocks");
		// 	System.out.println("(5) Show balance of your Market Account");
		//   	String answer = reader.nextLine();

		//   	switch(answer){
		//   		case '1':
		//   			System.out.println("How much money would you like to deposit?");
		//   			String temp = reader.nextLine();
		//   			Double amount = Double.parseDouble(temp);
		//   			if(amount < 0){
		//   				System.out.println("Please provide a dollar amount greater than 0");
		//   				amount = reader.nextLine();
		//   				Deposit(amount);
		//   			}
		//   			else{
		//   				Deposit(amount);
		//   			}
		//   		case '2':
		//   			Withdrawal();
		//   		case '3':
		//   			Buy();
		//  		default:
		//  			System.out.println("The option you chose is not listed. Please provide a valid option");
		//  			break;
		//   	}

		// 	Scanner reader = new Scanner(System.in);
		// 	String []inputs = new String[2];
		// 	System.out.println("Welcome! Please provide a valid username: ");
		//   	inputs[0] = reader.nextLine();
		//   	System.out.println("and now a valid password: ");
		// 	inputs[1] = reader.nextLine();
		// 	String username = inputs[0];
		// 	String password = inputs[1];
		// 	System.out.println(username + " " + password);

		// 	Connection connection = null;
		//     Statement statement = null;

		// 	try{
		// 		Class.forName("com.mysql.jdbc.Driver");
		// 	} 
		// 	catch(ClassNotFoundException e){
		// 		e.printStackTrace();
		// 	}
			
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
		//}

     //  /* Object creation */
    	// Trader trader = new Trader("Sai");

    	// //Test setter methods
    	// trader.setUserName("srimat");
    	// trader.setPassword("1234");
    	// trader.setState("CA");
    	// trader.setPhone("6504549782");


     //   You can access instance variable as follows as well 
    	
     //  	System.out.println("Name:"+ trader.name );
     //  	System.out.println("Username:"+ trader.username );
     //  	System.out.println("Password:" + trader.password );
     //  	System.out.println("State:" + trader.state);
     //  	System.out.println("Phone:" + trader.phone);

    }

}