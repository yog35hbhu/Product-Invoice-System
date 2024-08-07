package invoiceSystem;

public interface InvoiceDetailsInterface {
//	Methods mandatory used
	public void NewScreenInvoice();
	public void getSum();
	public boolean checkProductExists(String productId);
	public boolean checkProductName(String productName);
	public int checkLastOrderId ();
	public void Connect();
}
