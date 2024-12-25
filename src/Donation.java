
import java.util.ArrayList;
import java.util.Comparator;

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
			if(this.donors.get(i).stringRepresentation() == "Company") {
				Company company = (Company) this.donors.get(i);
				company.print(prefix + "  ", suffix);
			} else {
				this.donors.get(i).print(prefix + "  ", suffix);
			}
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
	public ArrayList<Donor> filterByDonatedMore(float minValue) {
		ArrayList<Donor> result = new ArrayList<Donor>();
		for(Donor donor: this.donors) {
			if(donor.getDonated() >= minValue) {
				result.add(donor);
			}
		}
		return result;
	}
	public ArrayList<Person> searchByDonor(String name) {
		ArrayList<Person> result = new ArrayList<Person>();
		for(int i = 0; i < this.donors.size(); ++i) {
			Donor donor = this.donors.get(i);
			if(donor.stringRepresentation() == "Company") {
				Person person = donor.getPerson(name);
				if(person.getDonated() != 0) {
					boolean check = false;
					for(int j = 0; j < result.size(); ++j) {
						if(result.get(i).getName() == name) {
							result.get(i).setDonated(result.get(i).getDonated() + person.getDonated());
							check = true;
							break;
						}
					}
					if(!check) {
						result.add(person);
					}
				}
			} else {
				result.add(new Person(donor.getName(), donor.getDonated()));
			}
		}
		return result;
	}
	public void sortDonorsByName() {
		this.donors.sort(Comparator.comparing(Donor::getName));
	}
}
