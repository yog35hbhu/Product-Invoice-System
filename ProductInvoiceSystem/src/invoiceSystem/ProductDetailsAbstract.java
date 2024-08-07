package invoiceSystem;


//------------------------------------------------------------Import Files------------------------------------------------------------
import javax.swing.*;
import java.sql.*;

//------------------------------------------------------------Abstraction using 'abstract' Keyword------------------------------------------------------------
public abstract class ProductDetailsAbstract {
	
//	Declare Variables
//	Protected? -->  outside package by subclass only + within class & Package
	protected JFrame frmProduct;
	protected JTextField txtProductId, txtName, txtPricePerUnit, txtInStock;
	protected JTable table;
	protected Connection con;
	protected PreparedStatement pst;
	protected ResultSet rs;	
}
