package invoiceSystem;

//------------------------------------------------------------Import Files------------------------------------------------------------
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import javax.swing.table.DefaultTableModel;

//Date & Time
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//End of Date Time

//------------------------------------------------------------Class inherited from JFrame------------------------------------------------------------
public class Invoice extends JFrame implements InvoiceDetailsInterface {
	private static final long serialVersionUID = 608513845517383584L;
	
	private JPanel contentPane;
//	Label Time & Time declared
	JLabel lblDateShow = new JLabel("New label");
	JLabel lblTimeShow = new JLabel("New label");
	
//------------------------------------------------------------MAIN Method------------------------------------------------------------
	public void NewScreenInvoice() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Invoice frame = new Invoice();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
//------------------------------------------------------------Get Sum of Column total from Table!------------------------------------------------------------
	public void getSum() {
		double sum = 0;
		for (int i =0; i < tableProductOrder.getRowCount(); i++) {
			sum = sum + Double.parseDouble(tableProductOrder.getValueAt(i, 4).toString());
		}
		txtTotal.setText(Double.toString(sum));
	}
	
//------------------------------------------------------------CHEKCS if product ID exists------------------------------------------------------------
	public boolean checkProductExists(String productId) {
		for (int row = 0; row < tableProductOrder.getRowCount(); row++) {
			String currentProductId = tableProductOrder.getValueAt(row, 0).toString();
			if (currentProductId.equals(productId)) {
				return true;
			}
		}
		return false;
	}
	
//------------------------------------------------------------CHECKS if product Name------------------------------------------------------------
	public boolean checkProductName(String productName) {
		String prod = txtProductName.getText();
		if (prod != "") {
			return true;
		}
		return false;
	}
	
//------------------------------------------------------------CHECKS last Order Id from XAMPP table------------------------------------------------------------
	public int checkLastOrderId () {
			//-------------Prepare Statement + ResultSEt-------------
		int lastOrderId = 0;
		try {
			pst = con.prepareStatement("SELECT orderId FROM db_order ORDER BY orderId DESC LIMIT 1");
			rs = pst.executeQuery();
			//-----------------------Gather Value in Result Set----------------------------
			if (rs.next() == true) {
				lastOrderId = rs.getInt("orderId");
				System.out.println(lastOrderId);
			}
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
		return lastOrderId;
	}
	
//------------------------------------------------------------DATE format Method------------------------------------------------------------
	private void dateFormat() {
//		Setting Date Format
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
//	Passing Date in Label
		lblDateShow.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDateShow.setText(dFormat.format(date));
	}
	
//------------------------------------------------------------TIME format Method------------------------------------------------------------
	private void timeFormat() {
//		Setting Time Format
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		LocalDateTime now = LocalDateTime.now();
		lblTimeShow.setText(dtf.format(now));
	}
	
//------------------------------------------------------------CONSTRUCTOR------------------------------------------------------------
	public Invoice() {
		setResizable(false);
		initialize();
		Connect();
		dateFormat();
		timeFormat();
	}
	
	
//-----Variables-----
	Connection con;
	PreparedStatement pst, pst1;
	ResultSet rs;
	
	public double finalTotal = 0;
	private JTable tableProductOrder;
	private JTextField txtProductId, txtProductName, txtPricePerUnit, txtQuantity, txtAmountReturned, txtAmountPaid, txtTotal, txtCustomerId;
	
	
//------------------------------------------------------------CONNECTION to Database------------------------------------------------------------
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
	
//------------------------------------------------------------FRAME CREATION------------------------------------------------------------
	private void initialize() {
		setTitle("PADI Invoice");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Invoice.class.getResource("/img/invoice.png")));

		//		Avoid closing PARENT Frame
		//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1164, 653);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

//------------------------------------------------------------LOGO image TOP------------------------------------------------------------
		JLabel lblLogoImg = new JLabel("");
		lblLogoImg.setDebugGraphicsOptions(DebugGraphics.NONE_OPTION);
		lblLogoImg.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		lblLogoImg.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogoImg.setIcon(new ImageIcon(Invoice.class.getResource("/img/billing1.png")));
		lblLogoImg.setBounds(10, 20, 68, 57);
		contentPane.add(lblLogoImg);

//------------------------------------------------------------PADI Invoicing Title------------------------------------------------------------
		JLabel lblInvoicingTitle = new JLabel("PADI Invoicing");
		lblInvoicingTitle.setFont(new Font("Tahoma", Font.BOLD, 42));
		lblInvoicingTitle.setBounds(94, 10, 384, 75);
		contentPane.add(lblInvoicingTitle);

//------------------------------------------------------------Label Date: ------------------------------------------------------------
		JLabel lblDate = new JLabel("Date:");
		lblDate.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDate.setBounds(864, 20, 63, 28);
		contentPane.add(lblDate);

//------------------------------------------------------------Label Time: ------------------------------------------------------------
		JLabel lblTime = new JLabel("Time:");
		lblTime.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTime.setBounds(864, 58, 63, 27);
		contentPane.add(lblTime);

//		JLabel lblDateShow --> Declared global
		lblDateShow.setBounds(915, 25, 119, 18);
		contentPane.add(lblDateShow);
		

//		JLabel lblTimeShow --> Declared Globally
		lblTimeShow.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTimeShow.setBounds(915, 61, 67, 21);
		contentPane.add(lblTimeShow);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 92, 1130, 2);
		contentPane.add(separator);

//------------------------------------------------------------BILLED TO: cx details------------------------------------------------------------
		JLabel lblBilledTo = new JLabel("BILLED TO:");
		lblBilledTo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblBilledTo.setBounds(10, 104, 116, 27);
		contentPane.add(lblBilledTo);
		
//------------------------------------------------------------cx details area------------------------------------------------------------
		JTextArea textAddress = new JTextArea();
		textAddress.setLineWrap(true);
		textAddress.setFont(new Font("Courier New", Font.PLAIN, 16));
		textAddress.setBounds(10, 138, 223, 98);
		contentPane.add(textAddress);
		
//------------------------------------------------------------Customer ID search Box------------------------------------------------------------
		JLabel lblCustomerId = new JLabel("Customer ID:");
		lblCustomerId.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCustomerId.setBounds(864, 138, 118, 28);
		contentPane.add(lblCustomerId);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 246, 1130, 2);
		contentPane.add(separator_1);
		
//------------------------------------------------------------Product Details------------------------------------------------------------
		JLabel lblProductDetails = new JLabel("Product Details:");
		lblProductDetails.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblProductDetails.setBounds(10, 252, 157, 27);
		contentPane.add(lblProductDetails);
		
		JLabel lblProductId = new JLabel("Product ID");
		lblProductId.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblProductId.setBounds(10, 285, 75, 27);
		contentPane.add(lblProductId);
		
//------------------------------------------------------------PRODUCT ID search box------------------------------------------------------------
		txtProductId = new JTextField();
		txtProductId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				//Variable
				String productId = txtProductId.getText();
				
				try {
//------------------------------------------------------------Prepare Statement + ResultSEt------------------------------------------------------------
					pst = con.prepareStatement("select * from product where productId = ? ");
					pst.setString(1, productId);
					rs = pst.executeQuery();

//------------------------------------------------------------Gather Value in Result Set------------------------------------------------------------
					if (rs.next() == true) {
						String productName = rs.getString(2);
						String pricePerUnit = rs.getString(3);

//------------------------------------------------------------Passing value to TextBox------------------------------------------------------------
						txtProductName.setText(productName);
						txtPricePerUnit.setText(pricePerUnit);
						txtQuantity.setText("1");
					} else {

						//If search not found on DB -- DO NOT DISPLAY ANYTHING
						txtProductName.setText("");
						txtPricePerUnit.setText("");
						txtQuantity.setText("");
					}
				} catch (Exception e2) {
					System.out.println(e2);
				}
			}
		});
		txtProductId.setColumns(10);
		txtProductId.setBounds(87, 286, 96, 27);
		contentPane.add(txtProductId);
		
//------------------------------------------------------------LABEL PRODUCT DETAILS------------------------------------------------------------
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblName.setBounds(238, 285, 75, 27);
		contentPane.add(lblName);
		
		JLabel lblPricePerUnit = new JLabel("Price/Unit (Rs)");
		lblPricePerUnit.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblPricePerUnit.setBounds(564, 285, 103, 27);
		contentPane.add(lblPricePerUnit);
		
		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblQuantity.setBounds(813, 285, 75, 27);
		contentPane.add(lblQuantity);
		
//------------------------------------------------------------PRODUCT details Text Field------------------------------------------------------------
		txtProductName = new JTextField();
		txtProductName.setEditable(false);
		txtProductName.setColumns(10);
		txtProductName.setBounds(279, 285, 230, 27);
		contentPane.add(txtProductName);
		
		txtPricePerUnit = new JTextField();
		txtPricePerUnit.setEditable(false);
		txtPricePerUnit.setColumns(10);
		txtPricePerUnit.setBounds(662, 286, 96, 27);
		contentPane.add(txtPricePerUnit);
		
		txtQuantity = new JTextField();
		txtQuantity.setColumns(10);
		txtQuantity.setBounds(874, 286, 75, 27);
		contentPane.add(txtQuantity);

		
//------------------------------------------------------------Product ADD button------------------------------------------------------------
		JButton btnProductAdd = new JButton("   Add");
		btnProductAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String productIdToCheck = txtProductId.getText();
				boolean productExists = checkProductExists(productIdToCheck);
				DefaultTableModel model = (DefaultTableModel)tableProductOrder.getModel();
				
				//Variable orderId
				String orderId;
				String orderDate = lblDateShow.getText();
				String customerId = txtCustomerId.getText();
				
				//Variable OrderLine
				String productId = txtProductId.getText(); 
				String quantityOrdered = txtQuantity.getText();
				int counting = 0;
				
				try {
					//IF PRODUCT ID EXISTS------------------------------------------
					if (productExists) {
						JOptionPane.showMessageDialog(null, "Record already exists. Use UPDATE button instead!!!");
					} 
					else {
//------------------------------------------------------------Add orderDate + CustomerId to db_order------------------------------------------------------------
						pst1 = con.prepareStatement("insert into db_order(orderDate, custId) values(?,?)");
						pst1.setString(1, orderDate);
						pst1.setString(2, customerId);
						pst1.executeUpdate();
						
						counting = checkLastOrderId();
					}
				} catch (NumberFormatException | SQLException o) {
				    // Handle the NumberFormatException
				    JOptionPane.showMessageDialog(null, "Invalid number format: " + o.getMessage() + "\nCannot save to db_order");
				}
				
				
//				2 different try, catch case since details needs to be saved to db_orderline first & then linked to db_order to foreign key
				try {
					double pricePerUnit = Double.parseDouble(txtPricePerUnit.getText());
					int quantity = Integer.parseInt(txtQuantity.getText());
					double total = pricePerUnit * quantity;

					if (productExists) {
						JOptionPane.showMessageDialog(null, "Record already exists. Use UPDATE button instead!!!");
					} else {
					orderId = String.valueOf(counting);
					model.addRow(new Object[] 
						{txtProductId.getText(), txtProductName.getText(), pricePerUnit, quantity, total, counting});
					
//------------------------------------------------------------Add details to db_orderline------------------------------------------------------------
					pst = con.prepareStatement("insert into db_orderline(orderId, productId, quantityOrdered, totalPrice) values(?,?,?,?)");
					pst.setString(1, orderId);
					System.out.println(orderId);
					pst.setString(2, productId);
					pst.setString(3, quantityOrdered);
					pst.setDouble(4, total);
					pst.executeUpdate();
					}
				} catch (NumberFormatException | SQLException ret) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Invalid number format: " + ret.getMessage() + "\nCannot save to db_orderline");
				}
				
				//Return total sum after Adding
				getSum();
			}
		});
		btnProductAdd.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnProductAdd.setIcon(new ImageIcon(Invoice.class.getResource("/img/add.png")));
		btnProductAdd.setBounds(988, 289, 152, 21);
		contentPane.add(btnProductAdd);
		
//------------------------------------------------------------Product Update Button------------------------------------------------------------
		JButton btnProductUpdate = new JButton("Update");
		btnProductUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DefaultTableModel tblModel = (DefaultTableModel)tableProductOrder.getModel();	
				int selectedRow = tableProductOrder.getSelectedRow();
				String quantityOrdered = txtQuantity.getText(); 
				String productId = txtProductId.getText();
				try {
					if (selectedRow !=-1 ) {
						tblModel.setValueAt(txtProductId.getText(), selectedRow, 0);
						tblModel.setValueAt(txtProductName.getText(), selectedRow, 1);
						tblModel.setValueAt(txtPricePerUnit.getText(), selectedRow, 2);
						tblModel.setValueAt(txtQuantity.getText(), selectedRow, 3);


						String price = tblModel.getValueAt(selectedRow, 2).toString();
						double pricePerUnit = Double.parseDouble(price);
						String quanti = tblModel.getValueAt(selectedRow, 3).toString();
						int quantity = Integer.parseInt(quanti);
						double total = pricePerUnit * quantity;

//						pass Double to String
						String total1 = String.valueOf(total);

						tblModel.setValueAt(total, selectedRow, 4);

						
						String orderId = tblModel.getValueAt(tableProductOrder.getSelectedRow(), 5).toString();
						pst = con.prepareStatement("update db_orderline set quantityOrdered = ?, totalPrice = ? where (productId = ? AND orderId =?)");
						pst.setString(1, quantityOrdered);
						pst.setString(2, total1);
						pst.setString(3, productId);
						pst.setString(4, orderId);
						pst.executeUpdate();

						JOptionPane.showMessageDialog(null, "Record updated!!!");
						tableProductOrder.clearSelection();
						getSum();
					} else {
						JOptionPane.showMessageDialog(null, "Error. Please select Row to update!");
					}
				} catch (SQLException e4) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, e4);
				}
			}
		});
		btnProductUpdate.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnProductUpdate.setBounds(1065, 258, 75, 21);
		contentPane.add(btnProductUpdate);
		
		
		
//------------------------------------------------------------Product DELETE Button------------------------------------------------------------
		JButton btnProductDelete = new JButton("Delete");
		btnProductDelete.addActionListener(new ActionListener() {
//------------------------------------------------------------REMOVE Selected order rows ------------------------------------------------------------
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tblModel = (DefaultTableModel)tableProductOrder.getModel();

				int selectedRow = tableProductOrder.getSelectedRow();
				
				int rs[] = tableProductOrder.getSelectedRows();
				if (selectedRow !=-1 ) {
					for (int i = rs.length-1; i >= 0 ; i--) {

						int k = rs[i];

						String orderId = tblModel.getValueAt(selectedRow, 5).toString();
						try {
							pst = con.prepareStatement("DELETE FROM db_orderline WHERE orderId = ?");
							pst.setString(1, orderId);
							pst.executeUpdate();
							
							pst1 = con.prepareStatement("DELETE FROM db_order WHERE orderId = ?");
							pst1.setString(1, orderId);
							pst1.executeUpdate();
							
						} catch (SQLException e5) {
							System.out.println(e5);
						}

						tblModel.removeRow(k);
						getSum();
						JOptionPane.showInternalMessageDialog(null, "Selected row Deleted!!!");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Error. No Row selected to delete!");
				}
			}
		});
		btnProductDelete.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnProductDelete.setBounds(988, 258, 68, 21);
		contentPane.add(btnProductDelete);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, 329, 1130, 7);
		contentPane.add(separator_2);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setBounds(965, 246, 2, 383);
		contentPane.add(separator_3);
		
		
//------------------------------------------------------------Scroll Pane for Jtable------------------------------------------------------------
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 346, 600, 250);
		contentPane.add(scrollPane);
		
		
//------------------------------------------------------------JTable Start------------------------------------------------------------
		tableProductOrder = new JTable();
		tableProductOrder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//Get table model
				DefaultTableModel tblModel = (DefaultTableModel)tableProductOrder.getModel();
				if (tableProductOrder.getSelectedRowCount() == 1) {
					//If single row is selected, then update
					String productId = tblModel.getValueAt(tableProductOrder.getSelectedRow(), 0).toString();
					String productName = tblModel.getValueAt(tableProductOrder.getSelectedRow(), 1).toString();
					String pricePerUnit = tblModel.getValueAt(tableProductOrder.getSelectedRow(), 2).toString();
					String quantity = tblModel.getValueAt(tableProductOrder.getSelectedRow(), 3).toString();
					
//			---------Prn data dns table met dns Text Field la
					txtProductId.setText(productId);
					txtProductName.setText(productName);
					txtPricePerUnit.setText(pricePerUnit);
					txtQuantity.setText(quantity);
					

				} else {
					if (tableProductOrder.getSelectedRowCount() == 0) {
		// -------------If table is empty
						JOptionPane.showMessageDialog(null, "Table is empty...");
					} else {
		// -------------If row NOT selected | Multiple row selected
						JOptionPane.showMessageDialog(null, "Please select single row for update...");
					}
				}
			}
		});
		scrollPane.setViewportView(tableProductOrder);
		tableProductOrder.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Product ID", "Product Name", "Price/Unit", "Quantity", "Extended Price", "Order ID"
			}
		));
		tableProductOrder.getColumnModel().getColumn(0).setPreferredWidth(5);
		tableProductOrder.getColumnModel().getColumn(1).setPreferredWidth(58);
		tableProductOrder.getColumnModel().getColumn(2).setPreferredWidth(20);
		tableProductOrder.getColumnModel().getColumn(3).setPreferredWidth(5);
		tableProductOrder.getColumnModel().getColumn(4).setPreferredWidth(20);
		tableProductOrder.getColumnModel().getColumn(5).setPreferredWidth(5);
		
//------------------------------------------------------------Label Calculation details------------------------------------------------------------
		JLabel lblCalculationDetails = new JLabel("Calculation Details");
		lblCalculationDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblCalculationDetails.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblCalculationDetails.setBounds(620, 346, 340, 35);
		contentPane.add(lblCalculationDetails);
		
		JLabel lblTotal = new JLabel("Total");
		lblTotal.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblTotal.setBounds(630, 435, 123, 27);
		contentPane.add(lblTotal);
		
		JLabel lblAmountReturned = new JLabel("Amount Returned");
		lblAmountReturned.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblAmountReturned.setBounds(630, 529, 123, 27);
		contentPane.add(lblAmountReturned);
		
		JLabel lblAmountPaid = new JLabel("Amount Paid");
		lblAmountPaid.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblAmountPaid.setBounds(633, 482, 123, 27);
		contentPane.add(lblAmountPaid);
		
//------------------------------------------------------------Calculation details: Amount Returned------------------------------------------------------------
		txtAmountReturned = new JTextField();
		txtAmountReturned.setEditable(false);
		txtAmountReturned.setColumns(10);
		txtAmountReturned.setBounds(750, 530, 100, 27);
		contentPane.add(txtAmountReturned);
		
		
//------------------------------------------------------------Calculation details: Amount Paid------------------------------------------------------------
		txtAmountPaid = new JTextField();
		txtAmountPaid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * Pass paid amount from String to Double
				 * Same applies for Grand total*/
				String paidAmount = txtAmountPaid.getText();
				double paidAmountToDouble = Double.parseDouble(paidAmount);
				String grandTot = txtTotal.getText();
				double grandTotal = Double.parseDouble(grandTot);
				
				//Initial finalTotal declared as 0.
				finalTotal = paidAmountToDouble - grandTotal;
//				From Double to String + pass to Amount Returned
				String finalTotal1 = String.valueOf(finalTotal);
				
				txtAmountReturned.setText(finalTotal1);
				txtAmountReturned.setEditable(false);
			}
		});
		txtAmountPaid.setColumns(10);
		txtAmountPaid.setBounds(750, 483, 100, 27);
		contentPane.add(txtAmountPaid);
		
//------------------------------------------------------------Calculation details: txtTotal------------------------------------------------------------
		txtTotal = new JTextField();
		txtTotal.setColumns(10);
		txtTotal.setBounds(750, 435, 100, 27);
		contentPane.add(txtTotal);
		
//------------------------------------------------------------SAVE button------------------------------------------------------------
		JButton btnFormSave = new JButton("Save");
		btnFormSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
//------------------------------------------------------------Save invoice as PDF to send electronically to customer------------------------------------------------------------
				try {
//					getText from txtbox
					String custId = txtCustomerId.getText();

//------------------------------------------------------------Prepare Statement + ResultSEt------------------------------------------------------------
					pst = con.prepareStatement("select firstName, lastName, address, phoneNumber from customer where custId = ? ");
					pst.setString(1, custId);
					rs = pst.executeQuery();

//------------------------------------------------------------Gather Value in Result Set------------------------------------------------------------
					if (rs.next() == true) {
						String firstName = rs.getString(1);
						String lastName = rs.getString(2);
						String address = rs.getString(3);
						String phoneNumber = rs.getString(4);
						
//						Concatenate
						String fullName = firstName + " " + lastName;
						String fullCustomerDetails = fullName + "\n" + address + "\n" + phoneNumber;
							
//------------------------------------------------------------IText PDF------------------------------------------------------------
					Document document = new Document();
					/*String path = "C:\\Users\\bhuru\\OneDrive\\Documents\\JAVA files\\ProductInvoiceSystem\\PDF generated\\" + fullName + " " + lblDateShow.getText() +".pdf";*/
					String path = "C:\\Users\\bhuru\\Desktop\\ProductInvoiceSystem\\PDF generated\\" + fullName + " " + lblDateShow.getText() +".pdf";
					
//					PdfWriter.getInstance(document, new FileOutputStream(path + fullName + " " + lblDateShow.getText()) + ".pdf");
					PdfWriter.getInstance(document, new FileOutputStream(path));
					
//					Open Document
					document.open();
				
					Paragraph paragraph1 = new Paragraph("PADI Invoicing System \n");
					document.add(paragraph1);
					
					Paragraph paragraph2 = new Paragraph("Date: " + lblDateShow.getText() + "\n" + "Time: " + lblTimeShow.getText() + "\n\n" + "BILLED TO: \n" + fullCustomerDetails + "\n\n");
					document.add(paragraph2);
					PdfPTable tb1 = new PdfPTable(4);
					tb1.addCell("Product Name");
					tb1.addCell("Price/Unit");
					tb1.addCell("Quantity");
					tb1.addCell("Extended Price");
					
//					Loop
//					Get row count from total product customer ordered
					for (int i = 0; i < tableProductOrder.getRowCount(); i++) {
						
//						String for all Rows in Invoice application Table
						String productName = tableProductOrder.getValueAt(i, 0).toString();
						String pricePerUnit = tableProductOrder.getValueAt(i, 1).toString();
						String quantity = tableProductOrder.getValueAt(i, 2).toString();
						String extendedPrice = tableProductOrder.getValueAt(i, 3).toString();
						
						tb1.addCell(productName);
						tb1.addCell(pricePerUnit);
						tb1.addCell(quantity);
						tb1.addCell(extendedPrice);
					}
					document.add(tb1);
					
					Paragraph paragraph3 = new Paragraph("\nTotal: Rs " + txtTotal.getText() + "\nAmount Paid: Rs " + txtAmountPaid.getText() + "\nAmount Returned: Rs " + txtAmountReturned.getText() + "\n\n\t\t\tThank you for visiting!");
					document.add(paragraph3);
					document.close();
					
					
					JOptionPane.showMessageDialog(null, "Invoice Generated");
//					setVisible(false);
//					new Invoice().setVisible(true);
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2);
				}
			}
			});

//------------------------------------------------------------SAVE button------------------------------------------------------------
		btnFormSave.setIcon(new ImageIcon(Invoice.class.getResource("/img/save.png")));
		btnFormSave.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnFormSave.setBounds(988, 374, 152, 35);
		contentPane.add(btnFormSave);
		
		
//------------------------------------------------------------RESET button------------------------------------------------------------
		JButton btnFormReset = new JButton("Reset");
		btnFormReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new Invoice().setVisible(true);
			}
		});
		btnFormReset.setIcon(new ImageIcon(Invoice.class.getResource("/img/Reset.png")));
		btnFormReset.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnFormReset.setBounds(988, 459, 152, 35);
		contentPane.add(btnFormReset);
		
		
//------------------------------------------------------------CLOSE button------------------------------------------------------------
		JButton btnFormClose = new JButton("Close");
		btnFormClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnFormClose.setIcon(new ImageIcon(Invoice.class.getResource("/img/close.png")));
		btnFormClose.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnFormClose.setBounds(988, 544, 152, 35);
		contentPane.add(btnFormClose);
		
//------------------------------------------------------------CustomerID text field------------------------------------------------------------
		txtCustomerId = new JTextField();
		txtCustomerId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String custId;
				custId = txtCustomerId.getText();
				try {
					//------------------------------------------------------------Prepare Statement + ResultSEt------------------------------------------------------------
					pst = con.prepareStatement("select firstName, lastName, address, phoneNumber from customer where custId = ? ");
					pst.setString(1, custId);
					rs = pst.executeQuery();

					//------------------------------------------------------------Gather Value in Result Set------------------------------------------------------------
					if (rs.next() == true) {
						String firstName = rs.getString(1);
						String lastName = rs.getString(2);
						String address = rs.getString(3);
						String phoneNumber = rs.getString(4);
						
						//			Concatenate Full Name + Details for Customer
						String fullName = firstName + " " + lastName;
						String fullCustomerDetails = fullName + "\n" + address + "\n" + phoneNumber;

						//------------------------------------------------------------Passing value to TextBox------------------------------------------------------------
						textAddress.setText(fullCustomerDetails);
					} else {

						//If search not found on DB -- DO NOT DISPLAY ANYTHING
						textAddress.setText("");
					}
				} catch (Exception e2) {
					System.out.println(e2);
				}			
			}
		});
		txtCustomerId.setBounds(965, 137, 96, 28);
		contentPane.add(txtCustomerId);
		txtCustomerId.setColumns(10);
	}
}
