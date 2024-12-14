
import java.util.ArrayList;

public class Donation {
	private String name;
	private ArrayList<Donor> donors = new ArrayList<Donor>();
	private float collected = 0.0f;
	private float goal;
	private boolean reached_goal = false;
	private String messageDonorNotFound() {
		String result = "Donor not found";
		if(this.donors.size() == 0) {
			result += "\nNo donors in this Donation";
		} else {
			result += "\nPossible donors:";
			for(int i = 0; i < this.donors.size(); ++i) {
				result += "\n    '" + this.donors.get(i).getName() + "'";
			}
		}
		return result;
	}
	public void addDonor(Donor donor) {
		this.collected += donor.getDonated();
		donors.add(donor);
	}
	public Donor getDonor(String name) {
		for(int i = 0; i < donors.size(); ++i) {
			if(donors.get(i).getName() == name) {
				return donors.get(i);
			}
		}
		return null;
	}
	public Donation(String name, float goal) {
		this.name = name;
		this.goal = goal;
	}
	public String donate(String name, float price) throws RuntimeException {
		for(int i = 0; i < donors.size(); ++i) {
			if(this.donors.get(i).getName() == name) {
				this.donors.get(i).donate(price);
				this.collected += price;
				this.reached_goal = this.collected > this.goal;
				return name;
			}
		}
		return null;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void print(String prefix, String suffix) {
		System.out.println("---------------------------------------");
		System.out.printf("%sDonation: %s%s\n", prefix, this.name, suffix);
		System.out.printf("%sProgress: %.2f/%.2f(%.2f%%)%s\n", prefix, this.collected, this.goal, this.collected / this.goal * 100.0f, suffix);
		
		System.out.printf("%sDonors: %d%s\n", prefix, this.donors.size(), suffix);
		for(int i = 0; i < this.donors.size(); ++i) {
			this.donors.get(i).print(prefix + "  ", suffix);
		}
		System.out.println("---------------------------------------"); 
	}
	public void print(String prefix) {
		this.print(prefix, "");
	}
	public void print() {
		this.print("", "");
	}
	public String who_donated_more(String first_donor, String second_donor, float diff) {
		float delta = this.getDonor(first_donor).getDonated() - this.getDonor(second_donor).getDonated();
		if(Math.abs(delta) < diff) {
			return null;
		}
		return delta > 0.0f ? first_donor : second_donor;
	}
	public String who_donated_more(String first_donor, String second_donor) {
		return this.who_donated_more(first_donor, second_donor, 0.0f);
	}
	public String who_donated_less(String first_donor, String second_donor, float diff) {
		float delta = this.getDonor(first_donor).getDonated() - this.getDonor(second_donor).getDonated();
		if(Math.abs(delta) < diff) {
			return null;
		}
		return delta < 0.0f ? first_donor : second_donor;
	}
	public String who_donated_less(String first_donor, String second_donor) {
		return this.who_donated_less(first_donor, second_donor, 0.0f);
	}
}
