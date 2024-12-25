
public class Main {
	private static Charity charity = new Charity();
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
	public static void main(String[] args) {
		String ep_name = "Supporting elder people";
		Donation elder_people = new Donation(ep_name, 10000);
		charity.addDonation(elder_people);
		charity.donate(ep_name, "Roman", 44.4f);
		charity.donate(ep_name, "Misha", 253.36f);
		Company company = new Company("My Company", 0.0f);
		company.donate("Roman", 33.0f);
		company.donate("Sasha", 37.0f);
		charity.donate(ep_name, company, "Roman", 3.2f);
		charity.print();
		Donor donor1 = charity.getDonation(ep_name).getDonor("Roman");
		Donor donor2 = charity.getDonation(ep_name).getDonor("Misha");
		Donor donor3 = charity.getDonation(ep_name).getDonor("My Company");
		System.out.printf("Hash for (%s: %s): %s\n", donor1.getName(), donor1.getDonated(), donor1.hashCode());
		System.out.printf("(%s) %s (%s)\n", donor1.toString(), donor1.equals(donor2) ? "=" : "!=", donor2.toString());
		System.out.printf("String representation for (%s): %s\n", donor1.toString(), donor1.stringRepresentation());
		System.out.printf("String representation for (%s): %s\n", donor3.toString(), donor3.stringRepresentation());
		printDiff(ep_name, "Misha", "Roman");
	}
}
