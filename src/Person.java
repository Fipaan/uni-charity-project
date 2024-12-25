
public class Person extends Donor {
	public Person(String name, float donated) {
		super(name, donated);
	}
	@Override
	public String stringRepresentation() {
		return "Donor";
	}
	@Override
	public void donate(float money) {
		super.setDonated(super.getDonated() + money);
	}
	@Override
	public void donate(String name, float money) {
		System.out.println("ERROR: can't do this");
	}
	public float getDonated(String name) {
		if(super.getName() != name) return -1;
		return super.getDonated();
	}
	public Person getPerson(String name) {
		if(super.getName() == name) return this;
		return new Person(name, 0);
	}
	public void print(String prefix, String suffix) {
		System.out.printf("%s%s: %.2f$%s\n", prefix, super.getName(), super.getDonated(), suffix);
	}
}
