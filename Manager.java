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
		System.out.println(username);
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

        	stmt.close();
        	con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	// public void setUserName()
	// {

	// }

	// public void setPassword()
	// {

	// }

	// public void setAddress()
	// {

	// }

	// public void setState()
	// {

	// }

	// public void setPhone()
	// {

	// }

	// public void setEmail()
	// {

	// }

	// public void setTaxId()
	// {

	// }
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
	}

}