
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class Main {
	private static Charity charity;
	public static final String donation_name = "Supporting elder people";
	private static final float DIFF_PERC = 0.001f;
	private static void printDiff(String donation_name, String first_person, String second_person) {
		String donated_more = charity.getDonation(donation_name).who_donated_more(first_person, second_person, DIFF_PERC);
		String donated_less = donated_more == first_person ? second_person : first_person;
		if (donated_more != null) {
			System.out.printf("%s is donated more then %s\n", donated_more, donated_less);
		} else {
			donated_more = donated_less == first_person ? second_person : first_person;
			System.out.printf("%s and %s donated equally\n", donated_more, donated_less);
		}
	}
	private static void load_charity(String charity_name) {
		try {
			DatabaseLinker.connect();
			charity = DatabaseLinker.get_charity(charity_name);
			System.out.println("[INFO] Charity loaded succesfully!");
		} catch(ClassNotFoundException e) {
			/*
			Charity c = new Charity(charity_name);
			Donation d = new Donation(donation_name, 1000.0f);
			d.addDonor(new Donor("Roman", 73.0f));
			d.addDonor(new Donor("Misha", 22.0f));
			Company company = new Company("My Company", 0.0f);
			company.addEmployee(new Employee("Roman", 12.3f));
			company.addEmployee(new Employee("Sasha", 17.92f));
			d.addDonor(company);
			c.addDonation(d);
			charity = c;
			*/
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	private static void save_charity(Charity charity) {
		try {
			DatabaseLinker.upsert_charity(charity);	
			System.out.println("[INFO] Charity saved succesfully!");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	public static void main(String[] args) {
		load_charity("Charity");
		Donor donor1 = charity.getDonation(donation_name).getDonor("Roman");
		Donor donor2 = charity.getDonation(donation_name).getDonor("Misha");
		Company donor3 = (Company) charity.getDonation(donation_name).getDonor("My Company");
		charity.print();
		System.out.printf("Hash for (%s: %s): %s\n", donor1.getName(), donor1.getDonated(), donor1.hashCode());
		System.out.printf("(%s) %s (%s)\n", donor1.toString(), donor1.equals(donor2) ? "=" : "!=", donor2.toString());
		System.out.printf("String representation for (%s): %s\n", donor1.toString(), donor1.stringRepresentation());
		System.out.printf("String representation for (%s): %s\n", donor3.toString(), donor3.stringRepresentation());
		printDiff(donation_name, "Misha", "Roman");
		save_charity(charity);
	}
}
