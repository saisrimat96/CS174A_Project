import java.io.*;
import DBPackage.*;



public class Main{
	public static void main(String [ ] args)
	{
		Trader trader = new Trader("Sai");

		trader.setUserName("srimat");
	    trader.setPassword("1234");
	    trader.setState("CA");
	    trader.setPhone("6504549782");

	    System.out.println("Name:"+ trader.name );
	    System.out.println("Username:"+ trader.username );
	    System.out.println("Password:" + trader.password );
	    System.out.println("State:" + trader.state);
	  	System.out.println("Phone:" + trader.phone);
	}
}