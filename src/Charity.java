
import java.util.ArrayList;

public class Charity {
	private ArrayList<Donation> donations = new ArrayList<Donation>();
	public Charity() {

	}
	public void addDonation(Donation donation) {
		this.donations.add(donation);
	}
	public Donation getDonation(String name) throws RuntimeException {
		for(int i = 0; i < this.donations.size(); ++i) {
			if(this.donations.get(i).getName() == name) {
				return this.donations.get(i);
			}
		}
		throw new RuntimeException("Donation not found");
	}
	public void updateDonation(String name, Donation donation) throws RuntimeException {
		for(int i = 0; i < this.donations.size(); ++i) {
			if(this.donations.get(i).getName() == name) {
				this.donations.set(i, donation);
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
			this.getDonation(donation_name).donate(donor_name, donated);;
		} else {
			this.addDonor(donation_name, donor_name, donated);
		}
	}
}