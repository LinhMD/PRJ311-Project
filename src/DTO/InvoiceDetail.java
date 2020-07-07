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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		InvoiceDetail that = (InvoiceDetail) o;

		if (!Objects.equals(invoice, that.invoice)) return false;
		if (!Objects.equals(pet, that.pet)) return false;
		return Objects.equals(service, that.service);
	}

	@Override
	public int hashCode() {
		int result = invoice != null ? invoice.hashCode() : 0;
		result = 31 * result + (pet != null ? pet.hashCode() : 0);
		result = 31 * result + (service != null ? service.hashCode() : 0);
		return result;
	}
}
