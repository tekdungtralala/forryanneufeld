package com.upwork.job.modelinput;

public class CustomerModel {

	private String customer_id;

	public CustomerModel() {
	}

	public CustomerModel(String ci) {
		this.customer_id = ci;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
}
