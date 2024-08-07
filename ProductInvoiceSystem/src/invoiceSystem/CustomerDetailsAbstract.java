package invoiceSystem;

//------------------------------------------------------------Import Files------------------------------------------------------------
import javax.swing.*;
import java.sql.*;

//------------------------------------------------------------ABSTRACTION with keyword abstract------------------------------------------------------------
public abstract class CustomerDetailsAbstract {
//	Declaring Variables
//	Protected? -->  outside package by subclass only + within class & Package
	protected JFrame frmCustomer;
	protected JTextField txtCustomerId, txtFirstName, txtLastName, txtAddress, txtPhoneNumber;
	protected JTable table;
	protected Connection con;
	protected PreparedStatement pst;
	protected ResultSet rs;
	
}
