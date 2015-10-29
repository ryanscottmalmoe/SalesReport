import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class TestMyQuery {
        public static void main(String[] args) throws IOException {    
        	PrintStream out = null;
        	try{
        		//Opening output file to print report to.
        		out = new PrintStream(new FileOutputStream("salesReport.csv"));
        	}
        	catch (Exception e)
        	{
        		System.out.println(e);
        		System.exit(-1);
        	}
            try {
            		//Creating a connection
                    Connection conn = getConnection();
                    MyQuery mquery = new MyQuery(conn);
          /* ----------SALES REPORT QUERIES--------------------------*/
                    
                    // Totals
                    mquery.findTotalsOnOrders();
                    mquery.printTotalsOnOrders(out);
                    
                    //Blank email for now
                    //String email = args[1];
                    String email = "Isaacc@oillab.com";
                    // Order info 
                    mquery.findAllOrders();
                    mquery.printAllOrders(out, email);
                    

                    conn.close();
            } catch (SQLException e) {
                    e.printStackTrace();
            }
        }
        
        public static Connection getConnection() throws SQLException{
            Connection connection; 
            try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (InstantiationException e1) {
                    e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
            }
            //Create a connection to the database
            String serverName = "localhost";
            String mydatabase = "SalesOrders";
            String url = "jdbc:mysql://" + serverName + "/" + mydatabase; // a JDBC url
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
            return connection;
        }
}
