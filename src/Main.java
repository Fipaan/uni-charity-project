
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
		charity.print();
		printDiff(ep_name, "Misha", "Roman");
	}
}
