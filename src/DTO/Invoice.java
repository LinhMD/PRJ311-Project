package DTO;

import java.util.Date;
import java.util.Vector;

public class Invoice {
	int id;
	Date date;
	Customer customer;
	Vector<InvoiceDetail> details;

	public Invoice(int id, Date date) {
		this.id = id;
		this.date = date;
		this.details = new Vector<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Vector<InvoiceDetail> getDetails() {
		return details;
	}

	public void setDetails(Vector<InvoiceDetail> details) {
		this.details = details;
	}

	public void setCustomer(Customer customer){this.customer = customer;}

	public Customer getCustomer() {
		return customer;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date){
		this.date = date;
	}
}
