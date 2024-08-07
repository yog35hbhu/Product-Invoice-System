package invoiceSystem;

//------------------------------------------------------------Import Files------------------------------------------------------------
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//------------------------------------------------------------class MAIN------------------------------------------------------------
public class MainMenu {
	private JFrame frmMainMenu;
	
//------------------------------------------------------------MAIN Method------------------------------------------------------------
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu window = new MainMenu();
					window.frmMainMenu.setVisible(true);
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
	public MainMenu() {
		initialize();
	}
	
//------------------------------------------------------------Initialize the contents of the frame------------------------------------------------------------
	private void initialize() {
		frmMainMenu = new JFrame();
		frmMainMenu.setResizable(false);
		frmMainMenu.setIconImage(Toolkit.getDefaultToolkit().getImage(MainMenu.class.getResource("/img/homepage.png")));
		frmMainMenu.setTitle("Main Menu - Product Invoicing System");
		frmMainMenu.setBounds(100, 100, 428, 425);
		frmMainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMainMenu.getContentPane().setLayout(null);
		
		JLabel lblMainMenu = new JLabel("MAIN MENU");
		lblMainMenu.setFont(new Font("Meiryo", Font.BOLD, 34));
		lblMainMenu.setBounds(98, 10, 222, 65);
		frmMainMenu.getContentPane().add(lblMainMenu);
		
		
//------------------------------------------------------------Customers Button------------------------------------------------------------
		JButton btnCustomers = new JButton("Customers");
		btnCustomers.addActionListener(new ActionListener() {
			
//------------------------------------------------------------OnClick action button 1: Customers------------------------------------------------------------
			public void actionPerformed(ActionEvent e) {
				Customer customerObj = new Customer();
				customerObj.NewScreenCustomer();
			}
		});
		btnCustomers.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnCustomers.setBounds(150, 120, 128, 28);
		frmMainMenu.getContentPane().add(btnCustomers);
		
//------------------------------------------------------------Products Button------------------------------------------------------------
		JButton btnProducts = new JButton("Products");
		btnProducts.addActionListener(new ActionListener() {
//------------------------------------------------------------OnClick action button 2: Products------------------------------------------------------------
			public void actionPerformed(ActionEvent e) {
				Product productObj = new Product();
				productObj.NewScreenProduct();
			}
		});
		btnProducts.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnProducts.setBounds(150, 178, 128, 28);
		frmMainMenu.getContentPane().add(btnProducts);
		
//------------------------------------------------------------Invoice Button------------------------------------------------------------
		JButton btnInvoice = new JButton("Invoice");
		btnInvoice.addActionListener(new ActionListener() {
			
//------------------------------------------------------------OnClick action button 3: Invoice------------------------------------------------------------
			public void actionPerformed(ActionEvent e) {
				Invoice invoiceObj = new Invoice();
				invoiceObj.NewScreenInvoice();
			}
		});
		btnInvoice.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnInvoice.setBounds(150, 236, 128, 28);
		frmMainMenu.getContentPane().add(btnInvoice);
		
//------------------------------------------------------------Exit Button------------------------------------------------------------
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
//------------------------------------------------------------OnClick action button 4: EXIT------------------------------------------------------------
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnExit.setBounds(150, 294, 128, 28);
		frmMainMenu.getContentPane().add(btnExit);
	}
}
