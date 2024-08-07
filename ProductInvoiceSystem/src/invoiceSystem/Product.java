package invoiceSystem;

//------------------------------------------------------------Import Files------------------------------------------------------------
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;

//------------------------------------------------------------Class Product------------------------------------------------------------
public class Product extends ProductDetailsAbstract implements ProductDetailsInterface {

//------------------------------------------------------------Variables------------------------------------------------------------
//	Variable declared in ProductDetailsAbstract
	

	/**
	 * Launch the application.
	 */
	
//------------------------------------------------------------NewScreen to call on MainMeu------------------------------------------------------------
	@Override
	public void NewScreenProduct() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Product window = new Product();
					window.frmProduct.setVisible(true);
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
	public Product() {
		initialize();
		Connect();
		table_load();
		
		//Add placeholder Style
		addPlaceholderStyle(txtProductId);
	}
	
	
//------------------------------------------------------------Connection MySQL variable initialise------------------------------------------------------------
//	Variable declared in ProductDetailsAbstract
		
//------------------------------------------------------------Method - Making connection to XAMPP Server MySQL------------------------------------------------------------
		@Override
		public void Connect() {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
//				root + "" ---> means username & Password
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
				pst = con.prepareStatement("select * from product");
				rs = pst.executeQuery();
				table.setModel(DbUtils.resultSetToTableModel(rs));
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
	
	
//------------------------------------------------------------ADD placeholder Style - ProductId Box------------------------------------------------------------
		@Override
		public void addPlaceholderStyle(JTextField textField) {
			Font font = textField.getFont();			//Get Font
			font = font.deriveFont(Font.ITALIC);
			textField.setFont(font);					//Apply Font style to textBox
			textField.setForeground(Color.gray);		//Font colour
		}
		
//------------------------------------------------------------REMOVE placeholder Style - ProductId Box------------------------------------------------------------
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
		frmProduct = new JFrame();
		frmProduct.setResizable(false);
		frmProduct.setIconImage(Toolkit.getDefaultToolkit().getImage(Product.class.getResource("/img/product.png")));
		frmProduct.setTitle("Product");
		frmProduct.setBounds(100, 100, 851, 426);
		frmProduct.getContentPane().setLayout(null);
		
		JLabel lblProductsTable = new JLabel("Products Table");
		lblProductsTable.setHorizontalAlignment(SwingConstants.CENTER);
		lblProductsTable.setFont(new Font("Meiryo", Font.BOLD, 34));
		lblProductsTable.setBounds(0, 10, 837, 41);
		frmProduct.getContentPane().add(lblProductsTable);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Product details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(40, 61, 390, 220);
		frmProduct.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblProductId = new JLabel("Product ID");
		lblProductId.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblProductId.setBounds(10, 31, 110, 24);
		panel.add(lblProductId);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblName.setBounds(10, 79, 110, 24);
		panel.add(lblName);
		
		JLabel lblPricePerUnit = new JLabel("Price/unit");
		lblPricePerUnit.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblPricePerUnit.setBounds(10, 127, 110, 24);
		panel.add(lblPricePerUnit);
		
		JLabel lblInStock = new JLabel("In Stock");
		lblInStock.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblInStock.setBounds(10, 175, 110, 24);
		panel.add(lblInStock);
		
//------------------------------------------------------------ProductId TextBox------------------------------------------------------------
		txtProductId = new JTextField();
		txtProductId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
//		getText from txtbox
					String productId = txtProductId.getText();

//------------------------------------------------------------Prepare Statement + ResultSEt------------------------------------------------------------
					pst = con.prepareStatement("select name, pricePerUnit, Stock from product where productId = ? ");
					pst.setString(1, productId);
					ResultSet rs = pst.executeQuery();

//------------------------------------------------------------Gather Value in Result Set------------------------------------------------------------
					if (rs.next() == true) {
						String name = rs.getString(1);
						String pricePerUnit = rs.getString(2);
						String stock = rs.getString(3);

//------------------------------------------------------------Passing value to TextBox------------------------------------------------------------
						txtName.setText(name);
						txtPricePerUnit.setText(pricePerUnit);
						txtInStock.setText(stock);
					} else {

//		If search not found on DB -- DO NOT DISPLAY ANYTHING
						txtName.setText("");
						txtPricePerUnit.setText("");
						txtInStock.setText("");
					}
				} catch (Exception e2) {
					System.out.println(e2);
				}
			}
		});
		txtProductId.addFocusListener(new FocusAdapter() {
	
//------------------------------------------------------------FocusGained: ProductId textBox------------------------------------------------------------
			@Override
			public void focusGained(FocusEvent e) {
				if (txtProductId.getText().equals("Enter ProductID to autocomplete search")) {
					txtProductId.setText(null);
					txtProductId.requestFocus();

//					Remove placeholder Style
					removePlaceholderStyle(txtProductId);
				}
			}
			
//------------------------------------------------------------FocusLost: ProductId textBox------------------------------------------------------------
			@Override
			public void focusLost(FocusEvent e) {
				if (txtProductId.getText().length() == 0) {
					
//					Add placeholder Style
					addPlaceholderStyle(txtProductId);
					txtProductId.setText("Enter ProductID to autocomplete search");
				}
			}
		});
		txtProductId.setText("Enter ProductID to autocomplete search");
		txtProductId.setColumns(10);
		txtProductId.setBounds(130, 31, 250, 24);
		panel.add(txtProductId);
		
		txtName = new JTextField();
		txtName.setColumns(10);
		txtName.setBounds(130, 79, 250, 24);
		panel.add(txtName);
		
		txtPricePerUnit = new JTextField();
		txtPricePerUnit.setColumns(10);
		txtPricePerUnit.setBounds(130, 127, 250, 24);
		panel.add(txtPricePerUnit);
		
		txtInStock = new JTextField();
		txtInStock.setColumns(10);
		txtInStock.setBounds(130, 175, 250, 24);
		panel.add(txtInStock);
		
		JPanel panelButton = new JPanel();
		panelButton.setBorder(new TitledBorder(null, "Buttons", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelButton.setBounds(128, 310, 608, 67);
		frmProduct.getContentPane().add(panelButton);
		panelButton.setLayout(null);
		
//------------------------------------------------------------SAVE Button------------------------------------------------------------
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
//------------------------------------------------------------SAVING RECORD IN XAMPP mySQL------------------------------------------------------------
			public void actionPerformed(ActionEvent e) {
//		Variable to store TextBox info
				String name, pricePerUnit, stock;
				
//		custId = txtCustomerId.getText();
				name = txtName.getText();
				pricePerUnit = txtPricePerUnit.getText();;
				stock = txtInStock.getText();
				
				try {
					pst = con.prepareStatement("insert into product(name, pricePerUnit, stock) values(?,?,?)");
					pst.setString(1, name);
					pst.setString(2, pricePerUnit);
					pst.setString(3, stock);
					
//		Save it in server now
					pst.executeUpdate();
					
					JOptionPane.showMessageDialog(null, "Product details saved successfully!!!");
					

//		Refresh output and provide updated data -on TABLE
					table_load();
					
//		Clear TextBox
					txtProductId.setText("");
					txtName.setText("");
					txtPricePerUnit.setText("");
					txtInStock.setText("");
					
//		Request Focus on name
					txtName.requestFocus();
					
//			Add placeholder Style
					addPlaceholderStyle(txtProductId);
					txtProductId.setText("Enter ProductID to autocomplete search");
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2);
				}
			}
		});
		btnSave.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnSave.setBounds(10, 24, 110, 33);
		panelButton.add(btnSave);
		
		
//------------------------------------------------------------UPDATE Button------------------------------------------------------------
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			
//------------------------------------------------------------UPDATE Button: Action Performed------------------------------------------------------------
			public void actionPerformed(ActionEvent e) {
//				Variable to store TextBox info
						String productId, name, pricePerUnit, stock;
						
//				custId = txtCustomerId.getText();
						productId = txtProductId.getText();
						name = txtName.getText();
						pricePerUnit = txtPricePerUnit.getText();;
						stock = txtInStock.getText();
						
						try {
							pst = con.prepareStatement("update product set name = ?, pricePerUnit = ?, stock = ? where productId = ?");
							pst.setString(1, name);
							pst.setString(2, pricePerUnit);
							pst.setString(3, stock);
							pst.setString(4, productId);
							
//				Save it in server now
							pst.executeUpdate();

							JOptionPane.showMessageDialog(null, "Product details updated successfully!");
							

//				Refresh output and provide updated data -on TABLE
							table_load();
							
//				Clear TextBox
							txtProductId.setText("");
							txtName.setText("");
							txtPricePerUnit.setText("");
							txtInStock.setText("");
							
//				Request Focus on name
							txtName.requestFocus();
							
//				Add placeholder Style
							addPlaceholderStyle(txtProductId);
							txtProductId.setText("Enter ProductID to autocomplete search");
							
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, e2);
						}
			}
		});
		btnUpdate.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnUpdate.setBounds(170, 24, 110, 33);
		panelButton.add(btnUpdate);
		
		
//------------------------------------------------------------DELETE button------------------------------------------------------------
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
//------------------------------------------------------------DELETE Function - Action Performed------------------------------------------------------------
			public void actionPerformed(ActionEvent e) {
//			variable
				String productId;
				
//			getText from txtField
				productId = txtProductId.getText();
				try {
					pst = con.prepareStatement("delete from product where productId = ?");
					pst.setString(1, productId);
					
//			Update in table
				pst.executeUpdate();	
				
				JOptionPane.showMessageDialog(null, "Successfully deleted Record Product ID: " + productId);
					
//			Refresh output and provide updated data -on TABLE
				table_load();
				
				
//			Clear TextBox
				txtProductId.setText("");
				txtName.setText("");
				txtPricePerUnit.setText("");
				txtInStock.setText("");
				
//				Request Focus on name
				txtName.requestFocus();
				
//		Add placeholder Style
				addPlaceholderStyle(txtProductId);
				txtProductId.setText("Enter ProductID to autocomplete search");
				
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2);
				}
			}
		});
		btnDelete.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnDelete.setBounds(330, 24, 110, 33);
		panelButton.add(btnDelete);
		
//------------------------------------------------------------CLEAR button------------------------------------------------------------
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
//------------------------------------------------------------Clear button: Action Performed------------------------------------------------------------
			public void actionPerformed(ActionEvent e) {
				
				
//		Clear TextBox
				txtProductId.setText("");
				txtName.setText("");
				txtPricePerUnit.setText("");
				txtInStock.setText("");
				
//		Request Focus on name
				txtName.requestFocus();
				
//		Add placeholder Style
				addPlaceholderStyle(txtProductId);
				txtProductId.setText("Enter ProductID to autocomplete search");
			}
		});
		btnClear.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnClear.setBounds(490, 24, 110, 33);
		panelButton.add(btnClear);
		
		JScrollPane resultSetPane = new JScrollPane();
		resultSetPane.setBounds(440, 61, 387, 220);
		frmProduct.getContentPane().add(resultSetPane);
		
		table = new JTable();
		resultSetPane.setViewportView(table);
	}
}
