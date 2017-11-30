import java.sql.*;  
class MysqlCon{  
public static void main(String args[]){  
    try
    {  
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://cs174a.engr.ucsb.edu:3306/srimatDB","srimat","504");  
        //here Db is srimatDB name, srimat, 504 is username and password  
        Statement stmt=con.createStatement();  
        ResultSet rs=stmt.executeQuery("select * from Transactions");  
        while(rs.next())  
        System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
        con.close();  
    }
    catch(Exception e)
    { 
        System.out.println(e);
    }  
}  
}  
