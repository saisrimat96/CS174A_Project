import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Main{
	public static final String HOST = "jdbc:mysql://cs174a.engr.ucsb.edu:3306/srimatDB";
    public static final String USER = "srimat";
    public static final String PWD = "504";
	
	public static void main(String [ ] args)
	{

		System.out.println("Welcome to StarsRus Brokerage!");
		System.out.println("Are you a trader or a manager? Enter t for trader and m for manager. Enter q to exit.");
		
		while(true) {
			Scanner reader = new Scanner(System.in);
			String role = reader.nextLine();

			if(role.length() != 1) {
				System.out.println(role);
				System.out.println("Too many characters. Enter t or m to log into your account.");
			}
			else{
				if (role.equals("t")) {
					String username = "";
					while(true) {
						System.out.println("Welcome! Please provide a valid username: ");
				  		username = reader.nextLine();
					  	System.out.println("and now a valid password: ");
						String password = reader.nextLine();
						//System.out.println(username + " " + password);
						Connection connection = null;
					    Statement statement = null;

						try{
							Class.forName("com.mysql.jdbc.Driver");
						} 
						catch(ClassNotFoundException e){
							e.printStackTrace();
						}

						try
						{
							connection = DriverManager.getConnection(HOST,USER,PWD);
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
		        				System.out.println("Log in successful! Welcome to your Trader Portal!");
		        				Trader trader = new Trader(username);
		        				trader.menu();
		        				break;
		        			}
		    			}
		    			catch(SQLException e)
		    			{
		    				e.printStackTrace();
		    			}
		    			
		    			
	    			}
					
					// break;

				}
				if (role.equals("m")) {
					while(true) {
						System.out.println("Enter your username.");
						String username = reader.nextLine();
						System.out.println("Enter your password.");
						String password = reader.nextLine();
						if(username.equals("admin") && password.equals("secret")) {
							System.out.println("Log in successfully! Welcome to your Manager Portal!");
							Manager manager = new Manager("admin");
							manager.menu();
							break;
						}
						else{
							System.out.println("Incorrect username or password. Please try again.");
						}
	    			}
	    			
					break;
				}
				if (role.equals("q")) {
					break;
				}
				else {
					System.out.println("Incorrect input.");
				}
			}
		}

		System.exit(0);

	}
}
