import java.sql.*; 
public class JDBC2013
{

	public static void main(String args[]) 
	{
		System.out.println("Copyright 2004, R.G.Baldwin");
		// createDB();
		try
		{
			Statement stmt;
			ResultSet rs;

			//Register the JDBC driver for MySQL.
			Class.forName("com.mysql.jdbc.Driver");
			//Define URL of database server for
			// database named TestDB on the localhost
			// with the default port number 3306.
			//String url = "jdbc:mysql://localhost:3306/JunkDB"; 
			String url = "jdbc:mysql://localhost:3306/rmalmoe_4";
			//Get a connection to the database for a // user named auser with the password
			// drowssap, which is password spelled // backwards.
			Connection con = DriverManager.getConnection(url,"root", "");
			//Display URL and connection information
			System.out.println("URL: " + url); 
			System.out.println("Connection: " + con);
			//Get a Statement object
			stmt = con.createStatement();
			//As a precaution, delete myTable if it // already exists as residue from a
			// previous run. Otherwise, if the table // already exists and an attempt is made // to create it, an exception will be
			// thrown.
			try
			{
				stmt.executeUpdate("DROP TABLE myTable");
			}
			catch(Exception e)
			{
				System.out.print(e);
				System.out.println("No existing table to delete"); 
			}//end catch
			//Create a table in the database named
			// myTable.
			stmt.executeUpdate("CREATE TABLE myTable(id int, val char(15) not null)");
			//Insert some values into the table
			int x =stmt.executeUpdate("INSERT INTO myTable(id, val) VALUES(1,'One')"); 
			stmt.executeUpdate("INSERT INTO myTable(id, val) VALUES(2,'Two')"); 
			stmt.executeUpdate("INSERT INTO myTable(id, val) VALUES(3,'Three')"); 
			stmt.executeUpdate("INSERT INTO myTable(id, val) VALUES(4,'Four')"); 
			stmt.executeUpdate("INSERT INTO myTable(id, val) VALUES(5,'Five')");
			//Get another statement object initialized
			// as shown.
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY); stmt.execute("DELETE from myTable WHERE id = 1");
					//Query the database, storing the result
					// in an object of type ResultSet
					rs = stmt.executeQuery("SELECT * from myTable ORDER BY id");
					//Use the methods of class ResultSet in a // loop to display all of the data in the // database.
					System.out.println("Display all results:"); 
					while(rs.next())
					{
						int theInt= rs.getInt("id");
						String str = rs.getString("val");
						System.out.println("\t id= " + theInt + "\t value = " + str);
					}//end while loop

					//Display the data in a specific row using // the rs.absolute method. System.out.println("Display row number 2:"); if( rs.absolute(2) )
					{
						int theInt= rs.getInt("id");
						String str = rs.getString("val"); System.out.println("\tid= " + theInt + "\tstr = " + str);
					}//end if
					//Delete the table and close the connection // to the database //stmt.executeUpdate("DROP TABLE myTable"); con.close();
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
		}//end catch
	}//end main


	public static void createDB()
	{
		Statement stmt;
		try
		{
			//Register the JDBC driver for MySQL.
			Class.forName("com.mysql.jdbc.Driver");
			//Define URL of database server for
			// database named mysql on the localhost
			// with the default port number 3306.
			String url = "jdbc:mysql://localhost:3306/mysql";
			//Get a connection to the database for a
			// user named root with a blank password.
			// This user is the default administrator
			// having full privileges to do anything.
			Connection con = DriverManager.getConnection(url,"root", "");
			//Display URL and connection information
			System.out.println("URL: " + url); System.out.println("Connection: " + con);
			//Get a Statement object
			stmt = con.createStatement();
			//Create the new database
			stmt.executeUpdate(
					"CREATE DATABASE JunkDB");
			//Register a new user named auser on the // database named JunkDB with a password // drowssap enabling several different // privileges.
			stmt.executeUpdate(
					"GRANT SELECT,INSERT,UPDATE,DELETE," + "CREATE,DROP " +
							"ON JunkDB.* TO 'auser'@'localhost' " +
					"IDENTIFIED BY 'drowssap';");
			con.close();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}//end catch

	}
}
	