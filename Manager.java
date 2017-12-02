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


}