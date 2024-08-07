package invoiceSystem;

//------------------------------------------------------------Import Files------------------------------------------------------------
import javax.swing.JTextField;

//------------------------------------------------------------ABSTRACTION 100%: Interface------------------------------------------------------------
public interface CustomerDetailsInterface {
	
//	Methods to implement in ALL
	public void NewScreenCustomer();
	public void Connect();
	public void table_load();
	public void addPlaceholderStyle(JTextField textField);
	public void removePlaceholderStyle(JTextField textField);
}
