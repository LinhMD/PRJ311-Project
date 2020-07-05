package DTO;

import java.util.Objects;

public class InvoiceDetail{
	Invoice invoice;
	Pet pet;
	Service service;

	public InvoiceDetail(Invoice invoice, Pet pet, Service service) {
		this.invoice = invoice;
		this.pet = pet;
		this.service = service;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

}
