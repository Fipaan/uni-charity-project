
import java.util.ArrayList;
import java.util.Comparator;

public class Donation {
	private ArrayList<Donor> donors = new ArrayList<Donor>();
	private String name;
	private float collected = 0.0f;
	private float goal;
	private boolean reached_goal = false;
	public float getCollected() {
		return this.collected;
	}
	public float getGoal() {
		return this.goal;
	}
	public boolean isReachedGoal() {
		return this.reached_goal;
	}
	public ArrayList<Donor> getDonors() {
		return this.donors;
	}
	public void setDonors(ArrayList<Donor> donors) {
		this.donors.clear();
		this.donors.addAll(donors);
	}
	public void addDonor(Donor donor) {
		this.collected += donor.getDonated();
		donors.add(donor);
	}
	public Donor getDonor(String name) {
		for(int i = 0; i < donors.size(); ++i) {
			if(Global.compareStrings(donors.get(i).getName(), name)) {
				return donors.get(i);
			}
		}
		return null;
	}
	public Donation(String name, float goal) {
		this.name = name;
		this.goal = goal;
	}
	public Donation(String name, float collected, float goal, boolean reached_goal) {
		this.name = name;
		this.collected = collected;
		this.goal = goal;
		this.reached_goal = reached_goal;
	}
	public Donation(String name, float collected, float goal, boolean reached_goal, ArrayList<Donor> donors) {
		this.name = name;
		this.collected = collected;
		this.goal = goal;
		this.reached_goal = reached_goal;
		this.setDonors(donors);;
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
		System.out.printf("%sProgress: %.2f$/%.2f$(%.2f%%)%s\n", prefix, this.collected, this.goal, this.collected / this.goal * 100.0f, suffix);
		
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
	public ArrayList<Donor> searchByDonor(String name) {
		ArrayList<Donor> result = new ArrayList<Donor>();
		for(int i = 0; i < this.donors.size(); ++i) {
			Donor donor = this.donors.get(i);
			if(donor.stringRepresentation() == "Company") {
				Company company = (Company) donor;
				Employee employee = company.getEmployee(name);
				if(employee.getDonated() > 0) {
					boolean check = false;
					for(int j = 0; j < result.size(); ++j) {
						if(result.get(i).getName() == name) {
							result.get(i).setDonated(result.get(i).getDonated() + employee.getDonated());
							check = true;
							break;
						}
					}
					if(!check) {
						result.add(donor);
					}
				}
			} else {
				result.add(new Donor(donor.getName(), donor.getDonated()));
			}
		}
		return result;
	}
	public void sortDonorsByName() {
		this.donors.sort(Comparator.comparing(Donor::getName));
	}
}
