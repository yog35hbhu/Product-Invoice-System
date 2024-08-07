package invoiceSystem;

//------------------------------------------------------------Import Files------------------------------------------------------------
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import net.proteanit.sql.DbUtils;


//------------------------------------------------------------Customer Class Start------------------------------------------------------------
/*extends Inherited from CustomerDetailsAbstract
 * implements? --> OOP Abstraction*/
public class Customer extends CustomerDetailsAbstract implements CustomerDetailsInterface {

//------------------------------------------------------------Variables------------------------------------------------------------
//	Variable declared in CustomerDetailsAbstract
	

	/**
	 * Launch the application.
	 */
	
	/*Remove main + make it a method NewScreen()
	 * To be called in another class
	 * in case of errors --> ti ena STATIC emba*/
	@Override
	public void NewScreenCustomer() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Customer window = new Customer();
					window.frmCustomer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
//------------------------------------------------------------CONSTRUCTOR------------------------------------------------------------
	public Customer() {
		initialize();
		Connect();
		table_load();
		
		//Add placeholder Style
		addPlaceholderStyle(txtCustomerId);
	}
	
	
//------------------------------------------------------------Connection MySQL variable initialise------------------------------------------------------------
//		Variable declared in abstract class CustomerDetailsAbstract
	
//------------------------------------------------------------Method - Making connection to XAMPP Server MySQL------------------------------------------------------------
	@Override
	public void Connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
//			root + "" ---> means username & Password
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/product_invoicing", "root", "");
			
			System.out.println("Database connected");
		} 
		catch (ClassNotFoundException ex) {
		}
		catch (SQLException ex) {
		}
	}
	
//------------------------------------------------------------SELECT All From Customer + Return in ResultSet------------------------------------------------------------
	@Override
	public void table_load() {
		try {
			pst = con.prepareStatement("select * from customer");
			rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//------------------------------------------------------------ADD placeholder Style - CustomerId Box------------------------------------------------------------
	@Override
	public void addPlaceholderStyle(JTextField textField) {
		Font font = textField.getFont();			//Get Font
		font = font.deriveFont(Font.ITALIC);
		textField.setFont(font);					//Apply Font style to textBox
		textField.setForeground(Color.gray);		//Font colour
	}
	
//------------------------------------------------------------REMOVE placeholder Style - CustomerId Box------------------------------------------------------------
	@Override
	public void removePlaceholderStyle(JTextField textField) {
		Font font = textField.getFont();			//Get Font
		font = font.deriveFont(Font.PLAIN|Font.BOLD);
		textField.setFont(font);					//Apply Font style to textBox
		textField.setForeground(Color.black);		//Font colour
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	
	//------------------------------------------------------------GUI------------------------------------------------------------
	private void initialize() {
		frmCustomer = new JFrame();
		frmCustomer.setResizable(false);
		frmCustomer.setTitle("Customer");
		frmCustomer.setIconImage(Toolkit.getDefaultToolkit().getImage(Customer.class.getResource("/img/customer.png")));
		frmCustomer.setBounds(100, 100, 851, 482);
		frmCustomer.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Customers Table");
		lblNewLabel.setFont(new Font("Meiryo", Font.BOLD, 34));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 10, 827, 41);
		frmCustomer.getContentPane().add(lblNewLabel);
		
		JPanel panelCustomerDetails = new JPanel();
		panelCustomerDetails.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Customer Details", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelCustomerDetails.setBounds(40, 61, 390, 272);
		frmCustomer.getContentPane().add(panelCustomerDetails);
		panelCustomerDetails.setLayout(null);
		
		JLabel lblCustomerId = new JLabel("Customer ID");
		lblCustomerId.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblCustomerId.setBounds(10, 31, 110, 24);
		panelCustomerDetails.add(lblCustomerId);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblFirstName.setBounds(10, 79, 110, 24);
		panelCustomerDetails.add(lblFirstName);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblLastName.setBounds(10, 127, 110, 24);
		panelCustomerDetails.add(lblLastName);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblAddress.setBounds(10, 175, 110, 24);
		panelCustomerDetails.add(lblAddress);
		
		JLabel lblPhoneNumber = new JLabel("Phone No.");
		lblPhoneNumber.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblPhoneNumber.setBounds(10, 223, 110, 24);
		panelCustomerDetails.add(lblPhoneNumber);
		
//------------------------------------------------------------Textbox CustomerId------------------------------------------------------------
		txtCustomerId = new JTextField();
		txtCustomerId.addKeyListener(new KeyAdapter() {
//			Overriding keyReleased() method --> where we set the text of the label when key is released
			@Override
			public void keyReleased(KeyEvent e) {
				try {
//			getText from txtbox
					String custId = txtCustomerId.getText();
					
//------------------------------------------------------------Prepare Statement + ResultSEt------------------------------------------------------------
					pst = con.prepareStatement("select firstName, lastName, address, phoneNumber from customer where custId = ? ");
					pst.setString(1, custId);
					ResultSet rs = pst.executeQuery();
			
//------------------------------------------------------------Gather Value in Result Set------------------------------------------------------------
					if (rs.next() == true) {
						String firstName = rs.getString(1);
						String lastName = rs.getString(2);
						String address = rs.getString(3);
						String phoneNumber = rs.getString(4);
						
//------------------------------------------------------------Passing value to TextBox------------------------------------------------------------
						txtFirstName.setText(firstName);
						txtLastName.setText(lastName);
						txtAddress.setText(address);
						txtPhoneNumber.setText(phoneNumber);
					} else {
						
				//If search not found on DB -- DO NOT DISPLAY ANYTHING
						txtFirstName.setText("");
						txtLastName.setText("");
						txtAddress.setText("");
						txtPhoneNumber.setText("");
					}
				} catch (Exception e2) {
					System.out.println(e2);
				}
			}
		});

//------------------------------------------------------------FocusGained Start------------------------------------------------------------
		txtCustomerId.addFocusListener(new FocusAdapter() {
			@Override		// Focus gained gets focus of TextBox - called by AWT
			public void focusGained(FocusEvent e) {
				if (txtCustomerId.getText().equals("Enter CustomerID to autocomplete search")) {
					txtCustomerId.setText(null);
					txtCustomerId.requestFocus();
					
//			Remove placeholder Style
					removePlaceholderStyle(txtCustomerId);
				}
			}
			
//------------------------------------------------------------FocusLost Start------------------------------------------------------------
			@Override		//FocusLost called by AWT to say focus is lost.
			public void focusLost(FocusEvent e) {
				if (txtCustomerId.getText().length() == 0) {
//			Add placeholder Style
					addPlaceholderStyle(txtCustomerId);
					txtCustomerId.setText("Enter CustomerID to autocomplete search");
				}
			}
		});
		
		
//------------------------------------------------------------TextField------------------------------------------------------------
		txtCustomerId.setText("Enter CustomerID to autocomplete search");
		txtCustomerId.setToolTipText("");
		txtCustomerId.setBounds(130, 31, 250, 24);
		panelCustomerDetails.add(txtCustomerId);
		txtCustomerId.setColumns(10);
		
		txtFirstName = new JTextField();
		txtFirstName.setColumns(10);
		txtFirstName.setBounds(130, 79, 250, 24);
		panelCustomerDetails.add(txtFirstName);
		
		txtLastName = new JTextField();
		txtLastName.setColumns(10);
		txtLastName.setBounds(130, 127, 250, 24);
		panelCustomerDetails.add(txtLastName);
		
		txtAddress = new JTextField();
		txtAddress.setColumns(10);
		txtAddress.setBounds(130, 175, 250, 24);
		panelCustomerDetails.add(txtAddress);
		
		txtPhoneNumber = new JTextField();
		txtPhoneNumber.setColumns(10);
		txtPhoneNumber.setBounds(130, 223, 250, 24);
		panelCustomerDetails.add(txtPhoneNumber);
		
		
//------------------------------------------------------------Panel Button located at the bottom of GUI------------------------------------------------------------
		JPanel panelButton = new JPanel();
		panelButton.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Buttons", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelButton.setBounds(128, 356, 608, 67);
		frmCustomer.getContentPane().add(panelButton);
		panelButton.setLayout(null);
		
		
//------------------------------------------------------------SAVE Button------------------------------------------------------------
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			
//------------------------------------------------------------SAVING RECORD IN XAMPP mySQL------------------------------------------------------------
			public void actionPerformed(ActionEvent e) {
//		Variable to store TextBox info
				String firstName, lastName, address, phoneNumber;
				
//		custId = txtCustomerId.getText();
				firstName = txtFirstName.getText();
				lastName = txtLastName.getText();
				address = txtAddress.getText();
				phoneNumber = txtPhoneNumber.getText();
				
				try {
					pst = con.prepareStatement("insert into customer(firstName, lastName, address, phoneNumber) values(?,?,?,?)");
					pst.setString(1, firstName);
					pst.setString(2, lastName);
					pst.setString(3, address);
					pst.setString(4, phoneNumber);
//			Save it in server now
					pst.executeUpdate();
					
					JOptionPane.showMessageDialog(null, "Customer details saved successfully!!!");
					
//			Refresh output and provide updated data -on TABLE
					table_load();
					
//			Clear TextBox
					txtCustomerId.setText("");
					txtFirstName.setText("");
					txtLastName.setText("");
					txtAddress.setText("");
					txtPhoneNumber.setText("");
					
//			Request Focus on FirstName
					txtFirstName.requestFocus();
					
//			Add placeholder Style
					addPlaceholderStyle(txtCustomerId);
					txtCustomerId.setText("Enter CustomerID to autocomplete search");
					
				} catch (Exception e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, e2);
				}
			}
		});
		btnSave.setBounds(10, 24, 110, 33);
		btnSave.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		panelButton.add(btnSave);
		
		
//------------------------------------------------------------UPDATE Button------------------------------------------------------------
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
//------------------------------------------------------------On Click Update------------------------------------------------------------
			public void actionPerformed(ActionEvent e) {
//			Variable to store TextBox info
				String custId, firstName, lastName, address, phoneNumber;
				
//			getText from txtField		
				custId = txtCustomerId.getText();
				firstName = txtFirstName.getText();
				lastName = txtLastName.getText();
				address = txtAddress.getText();
				phoneNumber = txtPhoneNumber.getText();
								
				try {
//					update customer set firstName = ?, lastName = ?, address = ?, phoneNumber = ? where custId = ?
					pst = con.prepareStatement("update customer set firstName = ?, lastName = ?, address = ?, phoneNumber = ? where custId = ?");
					
					pst.setString(1, firstName);
					pst.setString(2, lastName);
					pst.setString(3, address);
					pst.setString(4, phoneNumber);
					pst.setString(5, custId);
//			Update in table
					pst.executeUpdate();				
					JOptionPane.showMessageDialog(null, "Customer details updated successfully!");
					
//			Refresh output and provide updated data -on TABLE
					table_load();
					
//			Clear TextBox
					txtCustomerId.setText("");
					txtFirstName.setText("");
					txtLastName.setText("");
					txtAddress.setText("");
					txtPhoneNumber.setText("");
					
//			Request Focus on FirstName
					txtFirstName.requestFocus();
					
//			Add placeholder Style
					addPlaceholderStyle(txtCustomerId);
					txtCustomerId.setText("Enter CustomerID to autocomplete search");
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2);
				}
			}
		});
		btnUpdate.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnUpdate.setBounds(170, 24, 110, 33);
		panelButton.add(btnUpdate);
		
		
//------------------------------------------------------------DELETE Button------------------------------------------------------------
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
//			Variable
				String custId;
				
//			getText from txtField
				custId = txtCustomerId.getText();								
				try {
					pst = con.prepareStatement("delete from customer where custId = ?");
					pst.setString(1, custId);
					
//			Update in table
					pst.executeUpdate();	
					
					JOptionPane.showMessageDialog(null, "Successfully deleted Record customer ID: " + custId);
					
//			Refresh output and provide updated data -on TABLE
					table_load();
					
//			Clear TextBox
					txtCustomerId.setText("");
					txtFirstName.setText("");
					txtLastName.setText("");
					txtAddress.setText("");
					txtPhoneNumber.setText("");
					
//			Request Focus on FirstName
					txtFirstName.requestFocus();
					
//			Add placeholder Style
					addPlaceholderStyle(txtCustomerId);
					txtCustomerId.setText("Enter CustomerID to autocomplete search");
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2);
				}
			}
		});
		btnDelete.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnDelete.setBounds(330, 24, 110, 33);
		panelButton.add(btnDelete);
		
		
//------------------------------------------------------------CLEAR Button------------------------------------------------------------
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
//		Clear TextBox
				txtCustomerId.setText("");
				txtFirstName.setText("");
				txtLastName.setText("");
				txtAddress.setText("");
				txtPhoneNumber.setText("");
				
//		Request Focus on FirstName after CLEAR
				txtFirstName.requestFocus();
				
//		Add placeholder Style
				addPlaceholderStyle(txtCustomerId);
				txtCustomerId.setText("Enter CustomerID to autocomplete search");
			}
		});
		btnClear.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnClear.setBounds(490, 24, 110, 33);
		panelButton.add(btnClear);
		
		JScrollPane resultSetPane = new JScrollPane();
		resultSetPane.setBounds(440, 61, 387, 272);
		frmCustomer.getContentPane().add(resultSetPane);
		
//------------------------------------------------------------TABLE to return resultSet------------------------------------------------------------
		table = new JTable();
		resultSetPane.setViewportView(table);
	}
}
