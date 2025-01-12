
public class Main {
	private static Charity charity;
	public static final String charity_name = "Charity";
	public static final String donation_name = "Supporting elder people";
	public static final String donor1_name = "Roman";
	public static final String donor2_name = "Misha";
	public static final String company_name = "My Company";
	public static final String donor2_in_company_name = "Sasha";
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
		try {
			DatabaseLinker.connect();
			charity = DatabaseLinker.get_charity(charity_name);
			System.out.println("Charity loaded succesfully!");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		charity.print();
		Donor donor1 = charity.getDonation(donation_name).getDonor(donor1_name);
		Donor donor2 = charity.getDonation(donation_name).getDonor(donor2_name);
		Donor donor3 = charity.getDonation(donation_name).getDonor(company_name);
		System.out.printf("Hash for (%s: %s): %s\n", donor1.getName(), donor1.getDonated(), donor1.hashCode());
		System.out.printf("(%s) %s (%s)\n", donor1.toString(), donor1.equals(donor2) ? "=" : "!=", donor2.toString());
		System.out.printf("String representation for (%s): %s\n", donor1.toString(), donor1.stringRepresentation());
		System.out.printf("String representation for (%s): %s\n", donor3.toString(), donor3.stringRepresentation());
		printDiff(donation_name, donor2_name, donor1_name);
		try {
			DatabaseLinker.insert_charity(charity);
			System.out.println("Charity saved succesfully!");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
