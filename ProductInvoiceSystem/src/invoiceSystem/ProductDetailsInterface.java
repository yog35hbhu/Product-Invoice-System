package invoiceSystem;
//------------------------------------------------------------Import Files------------------------------------------------------------
import javax.swing.*;
//------------------------------------------------------------ABSTRACTION 100%: Interface------------------------------------------------------------
public interface ProductDetailsInterface {
//	Methods mandatory used
	public void NewScreenProduct();
	public void Connect();
	public void table_load();
	public void addPlaceholderStyle(JTextField textField);
	public void removePlaceholderStyle(JTextField textField);
}
