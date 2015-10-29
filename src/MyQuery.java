/*****************************
*****************************/
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.util.Date;
import java.lang.String;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MyQuery {

    private Connection conn = null;
	 private Statement statement = null;
	 private ResultSet resultSet = null;
    
    public MyQuery(Connection c)throws SQLException
    {
        conn = c;
        // Statements allow to issue SQL queries to the database
        statement = conn.createStatement();
    }
    
    //Totals on Orders query
    public void findTotalsOnOrders() throws SQLException
    {
    	
    	 String query =  "SELECT count(OrderNum) AS Total_Orders, " +
    			 "sum(OrdQty * UnitPrice) AS Total_Sales, " +
    			 "sum(OrdQty) AS Total_Items, " +
    			 "sum(UnitWeight) AS Total_Weight " +
    			 "FROM invoice NATURAL JOIN invoicelineitem JOIN inventory USING (SKU);";
   
    	 resultSet = statement.executeQuery(query); 

    }
    
    public void printTotalsOnOrders(PrintStream out) throws IOException, SQLException
    {
		out.println("SALES REPORT");
        while (resultSet.next()) {
			String totalOrders = resultSet.getString("Total_Orders");
			String totalSales = resultSet.getString("Total_Sales");
			String totalItems = resultSet.getString("Total_Items");
			String totalWeight = resultSet.getString("Total_Weight");
			String totalHeaderString = "Total Orders, Total Sales, Total Items, Total Weight";
			out.println(totalHeaderString);
			String finishedTotalsString = totalOrders + "," + totalSales + "," + totalItems  + "," +
			totalWeight;
			out.println(finishedTotalsString);
			out.println();
		} 
    }
    
		//Orders information query by cust#
    public void findAllOrders() throws SQLException
    {
   	 String query = "Select customer.Street, " +
   			 	 "customer.City, " +
   			 	 "customer.State, " +
   			 	 "customer.Zip, " +
   			 	 "CustNum, " +
   		         "OrderNum, " +
   		         "OrderDate, " +
   		         "Description, " +
   		         "SKU, " +
   		         "UnitPrice, " +
   		         "OrdQty AS Quantity, " +
   		         "UnitWeight, " +
   		         "sum(OrdQty * UnitPrice) AS Total_Price, " +
   		         "sum(OrdQty * UnitWeight) AS Total_Weight " +
   		    "FROM invoice join customer using (CustNum) NATURAL JOIN invoicelineitem JOIN inventory USING (SKU) " +
   		"GROUP BY OrderNum, CustNum, SKU;";

	 resultSet = statement.executeQuery(query); 
    }
    public void printAllOrders(PrintStream out, String email) throws IOException, SQLException
    {
    	int prevCustNum = 0;
		out.println("Orders:");
		int runningTotal = 0;
        while (resultSet.next()) {
			int custNum = resultSet.getInt("CustNum");
			String orderNum = resultSet.getString("OrderNum");
			String orderDate = resultSet.getString("OrderDate");
			String customerInfo = "Customer:," + custNum + "," + "Order#:," + orderNum + "," + "Order Date:," + orderDate;
			String streetAddress = resultSet.getString("customer.Street");
			String city = resultSet.getString("customer.City");
			String state = resultSet.getString("customer.State");
			String zip = resultSet.getString("customer.Zip");
			String sku = resultSet.getString("SKU");
			String description = resultSet.getString("Description");
			String unitPrice = resultSet.getString("UnitPrice");
			String quantity = resultSet.getString("Quantity");
			String unitWeight = resultSet.getString("UnitWeight");
			String totalPrice = resultSet.getString("Total_Price");
			String totalWeight = resultSet.getString("Total_Weight");
        	if(prevCustNum != custNum)
        	{
        		if(prevCustNum != 0)
        		{
            		out.println(", , , , , , , " + runningTotal);
        		}
        		runningTotal = 0;
        		out.println();
        		out.println(customerInfo);
        		out.println(streetAddress);
        		out.println(city + "," + state + "," + zip);
            	out.println("SKU:, Description:, Price:, Quantity:, Weight:, Total Price:, Total Weight:, Order Total:");
        	}
        	runningTotal = (int) (runningTotal + Double.parseDouble(totalPrice));
        	out.println(sku + "," + description + "," + unitPrice + "," + quantity + "," + unitWeight + "," + totalPrice + "," + totalWeight);
        	prevCustNum = resultSet.getInt("CustNum");
			
        }
		out.println(", , , , , , , " + runningTotal);
        //If we wanted to email sales report to someone.
        sendEmail("salesReport.csv", email);
    }
    public void sendEmail(String filename, String email) throws IOException, SQLException
    {
    	//Sender email
        String from = "ryanmalmoe@gmail.com";
        //Sender password
        //(Pass in emails password)
        String pass = "******";
        //Recipients email
        String[] to = { email }; // list of recipient email addresses
        String subject = "Java send mail example";
        String body = "Welcome to JavaMail!";
        
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            
            //Testing stuff
            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("Sales report");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);
            
            
            
            //
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    	
    }

}
