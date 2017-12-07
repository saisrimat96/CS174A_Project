/*

Sai Srimat, Ziheng Song: Main Class

*/

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Main{
	public static final String HOST = "jdbc:mysql://cs174a.engr.ucsb.edu:3306/srimatDB";
    public static final String USER = "srimat";
    public static final String PWD = "504";
	
	public static void main(String [ ] args)
	{
		Scanner reader = new Scanner(System.in);
		String username;
		String password;
		
		while(true) {
			//Log into Stock Market Interface
			System.out.println("----------Welcome to Stock Market Interface!----------");
			System.out.println("Please provide a valid username: ");
			username = reader.nextLine();
			System.out.println("and now a valid password: ");
			password = reader.nextLine();
			if (username.equals("sai") && password.equals("ziheng")) {
				System.out.println("\nLog in successfully!");				
				while(true) {
					Market market = new Market();
					market.menu();
					//Log into StarsRus Interface
					while(true) {
				  		System.out.println("\nDo you wish to go to StarsRus Brokerage Interface? Enter 'y' for yes or 'n' for no.");
				  		String answer = reader.nextLine();
				  		if(answer.equals("y")) {
				  			System.out.println("----------Welcome to StarsRus Brokerage!----------");
							System.out.println("Are you a trader or a manager? Enter 't' for trader or 'm' for manager. Enter 'q' to exit.");
							String role = reader.nextLine();

							if (role.length() != 1) {
								System.out.println("Too many characters. Enter 't' or 'm' to choose your account type.");
							}
							else{
								//Log into Trader Interface
								if (role.equals("t")) {
									while(true) {
										System.out.println("Please provide a valid username: ");
										username = reader.nextLine();
									  	System.out.println("and now a valid password: ");
										password = reader.nextLine();
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
						        				System.out.println("Log in successfully!");
						        				connection.close();
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
								}
								//Log into Manager Interface
								if (role.equals("m")) {
									while(true) {
										System.out.println("Please provide a valid username:");
										username = reader.nextLine();
										System.out.println("and now a valid password: ");
										password = reader.nextLine();
										if(username.equals("admin") && password.equals("secret")) {
											System.out.println("Log in successfully!");
											Manager manager = new Manager("admin");
											manager.menu();
											manager.closeConnection();
											break;
										}
										else{
											System.out.println("Incorrect username or password. Please try again.");
										}
						   			}
								}
								//Quit 
								if (role.equals("q")) {
									break;
								}
								else {
									System.out.println("Incorrect input.");
								}
							}
				 		}
				  		else if(answer.equals("n")) {
							break;
			  			}
			  			else{
			  				System.out.println("\nThe option you chose is not listed. Please provide a valid option.");
				 		}
					}
					System.out.println("Do you wish to go back to Stock Market Interface? Enter 'y' for yes or 'n' for no.");
					String goback = reader.nextLine();
					if (goback.equals("y")) {
						continue;
					}
					if (goback.equals("n")) {
						break;
					}
				}
			}
			else{
				System.out.println("\nIncorrect username or password. Please try again.");
				continue;
			}	
			reader.close();
			break;
		}
		System.exit(0);
	}
}
