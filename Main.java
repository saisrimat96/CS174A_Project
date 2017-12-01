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
					//Trader trader = new Trader();
					break;
				}
				if (role.equals("m")) {
					//Manager mnager = new Manager();
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
