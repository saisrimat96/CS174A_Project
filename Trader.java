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
	public Trader(String name)
	{
		// try
		// {
		// 	Class.forName("com.mysql.jdbc.Driver");  
  //   		Connection con=DriverManager.getConnection("jdbc:mysql://cs174a.engr.ucsb.edu:3306/srimatDB","srimat","504");
		// 	String query = "INSERT INTO Customer_Profile (customerId,name,username,password,STATE,Phone,Email,taxId) VALUES (NULL,?,?,?,?,NULL,NULL,?)";
		// 	PreparedStatement stmt=con.prepareStatement(query);
		// 	stmt.setString(1, "Test");
		// 	stmt.setString(2, "TestUser");
		// 	stmt.setString(3, "TestPwd");
		// 	stmt.setString(4, "CA");
		// 	stmt.setInt(5, 123);
		// 	stmt.executeUpdate();
		// 	stmt.close();
  //       	//ResultSet rs=stmt.executeUpdate();
		// }
		// catch(Exception e)
		// {
		// 	System.out.println(e);
		// }
		this.name = name;
	}


	public void setUserName(String user)
	{
		username = user;
	}

	public void setPassword(String pwd)
	{
		password = pwd;
	}

	public void setState(String st)
	{
		state = st;
	}

	public void setPhone(String ph)
	{
		phone = ph;
	}

	public void setEmail(String em)
	{
		email = em;
	}

	public void setTaxId(int tax)
	{
		taxid = tax;
	}

	/*public void Deposit()
	{

	}

	public void Withdrawal()
	{
		
	}

	public void Buy()
	{
		
	}

	public void Sell()
	{
		
	}

	public int showBalance()
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

	public static void main(String []args) 
	{
		
		// try
		// {
		// 	Class.forName("com.mysql.jdbc.Driver");  
  //   		Connection con=DriverManager.getConnection(HOST,USER,PWD);
  //   	}
  //   	catch(Exception e)
  //   	{
  //   		System.out.println(e);
  //   	}
		Scanner reader = new Scanner(System.in);
		String []inputs = new String[2];
		System.out.println("Welcome! Please provide a valid username: ");
      	inputs[0] = reader.nextLine();
      	System.out.println("and now a valid password: ");
  		inputs[1] = reader.nextLine();
  		String username = inputs[0];
  		String password = inputs[1];
  		System.out.println(username + " " + password);

		Connection connection = null;
        Statement statement = null;

		try{
			Class.forName("com.mysql.jdbc.Driver");
		} 
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		
		try{
			connection=DriverManager.getConnection(HOST,USER,PWD);
			String QUERY = "select username, password from Customer_Profile where username=? and password=?";

			PreparedStatement myQuery = connection.prepareStatement(QUERY);
			myQuery.setString(1, username);
			myQuery.setString(2, password);
	        ResultSet resultSet = myQuery.executeQuery();
	        boolean empty = true;
	        while (resultSet.next()) {
	         	empty = false;
	            }
	        if(empty){
	        	System.out.println("Incorrect username or password. Please try again.");
	        }
	        else{
	        	System.out.println("Login successful! Welcome to your Trader Portal!");
	        }
        }
        catch(SQLException e)
        {
        	e.printStackTrace();
        }

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