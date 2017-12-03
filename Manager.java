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

	public void menu()
	{
		while(True){
			System.out.println("Welcome to the Manager Interface!");
			System.out.println("Do you wish to: ");
			System.out.println("(1) Add Interest for Market Accounts");
			System.out.println("(2) Generate Monthly Statement for a particular customer");
			System.out.println("(3) List Active Customers");
			System.out.println("(4) Generate Government Drug & Tax Evasion Report(DTER)");
			System.out.println("(5) Generate Customer Report");
			System.out.println("(6) Delete Transactions");
			Scanner reader = new Scanner(System.in);
		  	String answer = reader.nextLine();

		  	switch(answer){
		  		case '1':
		  			addInterest();
		  		case '2':
		  			Withdrawal();
		  		case '3':
		  			Buy();
		 		default:
		 			System.out.println("The option you chose is not listed. Please provide a valid option");
		 			break;
		  	}
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

	// public void genStatement()
	// {

	// }

	// public void listActive()
	// {

	// }

	// public void genDTER()
	// {

	// }

	// public void report()
	// {

	// }

	// public void deleteTran()
	// {

	// }

}