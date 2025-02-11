package org.example;

import java.util.ArrayList;
import java.util.Objects;

public class Charity {
	private ArrayList<Donation> donations = new ArrayList<Donation>();
	private String name;
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Donation> getDonations() {
		return this.donations;
	}
	public void setDonations(ArrayList<Donation> donations) {
		this.donations.clear();
		this.donations.addAll(donations);
	}
	public Charity(String name) {
		this.name = name;
	}
	public void addDonation(Donation donation) {
		this.donations.add(donation);
	}
	public Donation getDonation(String name) throws RuntimeException {
		for(int i = 0; i < this.donations.size(); ++i) {
			if(Objects.equals(this.donations.get(i).getName(), name)) {
				return this.donations.get(i);
			}
		}
		return new Donation(null, -1.0f);
	}
	public void updateDonation(String name, Donation donation) throws RuntimeException {
		for(int i = 0; i < this.donations.size(); ++i) {
			if(Objects.equals(this.donations.get(i).getName(), name)) {
				this.donations.set(i, donation);
				return;
			}
		}
		throw new RuntimeException("Donation not found");
	}
	public void removeDonation(String name) throws RuntimeException {
		for(int i = 0; i < this.donations.size(); ++i) {
			if(Objects.equals(this.donations.get(i).getName(), name)) {
				this.donations.remove(i);
				return;
			}
		}
		throw new RuntimeException("Donation not found");
	}
	public void print() {
		System.out.println("=======================================");
		System.out.printf("Charities: %d\n", this.donations.size());
		for(int i = 0; i < this.donations.size(); ++i) {
			this.donations.get(i).print("  ");
		}
		System.out.println("=======================================");
	}
	public void addDonor(String donation_name, String donor_name, float donated) {
		this.getDonation(donation_name).addDonor(new Donor(donor_name, donated));;
	}	
	public void donate(String donation_name, String donor_name, float donated) {
		if(this.getDonation(donation_name).getDonor(donor_name) != null) {
			this.getDonation(donation_name).donate(donor_name, donated);
		} else {
			this.addDonor(donation_name, donor_name, donated);
		}
	}
	public void addCompany(String donation_name, Company company) {
		this.getDonation(donation_name).addDonor(company);
	}
	public void donate(String donation_name, Company company, String donor_name, float donated) {
		if(this.getDonation(donation_name).getDonor(company.getName()) == null) {
			this.addCompany(donation_name, company);
		}
		this.getDonation(donation_name).getDonor(company.getName()).donate(donor_name, donated);;
	}
}
