import java.io.*;
import java.util.Scanner;

public class Main{
	public static void main(String [ ] args)
	{
		// Trader trader = new Trader("Sai");

		// trader.setUserName("srimat");
	 //    trader.setPassword("1234");
	 //    trader.setState("CA");
	 //    trader.setPhone("6504549782");

	 //    System.out.println("Name:"+ trader.name );
	 //    System.out.println("Username:"+ trader.username );
	 //    System.out.println("Password:" + trader.password );
	 //    System.out.println("State:" + trader.state);
	 //  	System.out.println("Phone:" + trader.phone);

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
				//System.out.println(role);
				if (role.equals("t")) {
					System.out.println("Successful!");
					// try
					// {
					// 	connection = DriverManager.getConnection(HOST,USER,PWD);
					// 	String QUERY = "select username, password from Customer_Profile where username=? and password=?";

					// 	PreparedStatement myQuery = connection.prepareStatement(QUERY);
					// 	myQuery.setString(1, username);
					// 	myQuery.setString(2, password);
		   //      		ResultSet resultSet = myQuery.executeQuery();
		   //      		boolean empty = true;
	    //     			while (resultSet.next()) {
	    //      				empty = false;
	    //         		}
	    //     			if(empty){
	    //     				System.out.println("Incorrect username or password. Please try again.");
	    //     			}
	    //     			else{
	    //     				System.out.println("Log in successfully! Welcome to your Manager Portal!");
	    //     			}
	    // 			}
	    // 			catch(SQLException e)
	    // 			{
	    // 				e.printStackTrace();
	    // 			}
					//Trader trader = new Trader();
					break;
				}
				if (role.equals("m")) {
					while(true) {
						System.out.println("Enter your username.");
						String username = reader.nextLine();
						System.out.println("Enter your password.");
						String password = reader.nextLine();
						if(username.equals("admin") && password.equals("secret")) {
							System.out.println("Log in successfully!");
							Manager manager = new Manager("admin");
							manager.addInterest();
							break;
						}
	    			}
					break;
				}
				if (role.equals("q")) {
					break;
				}
				else {
					System.out.println("Incorrect input. Enter t or m to log into your account.");
				}
			}
		}

		System.exit(0);
	}
}
