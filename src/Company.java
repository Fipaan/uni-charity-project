
import java.util.ArrayList;

public class Company extends Donor {
	private ArrayList<Person> persons = new ArrayList<Person>();
	public ArrayList<Person> getPersons() {
		return this.persons;
	}
	public void setPersons(ArrayList<Person> persons) {
		this.persons = persons;
	}
	public void addPerson(Person person) {
		this.persons.add(person);
	}
	public Company(String name, float donated) {
		super(name, donated);
	}
	@Override
	public String stringRepresentation() {
		return "Company";
	}
	@Override
	public void donate(float money) {
		System.out.println("ERROR: can't do this");
	}
	@Override
	public void donate(String name, float money) {
		super.setDonated(super.getDonated() + money);
		for(int i = 0; i < this.persons.size(); ++i) {
			if(this.persons.get(i).getName() == name) {
				this.persons.get(i).setDonated(this.persons.get(i).getDonated() + money);
				return;
			}
		}
		this.persons.add(new Person(name, money));
	}
	public float getDonated(String name) {
		for(Donor person: this.persons) {
			if(person.getName() == name) return person.getDonated();
		}
		return -1;
	}
	public Person getPerson(String name) {
		for(Person person: this.persons) {
			if(person.getName() == name) {
				return person;
			}
		}
		return new Person(name, 0);
	}
	@Override
	public void print(String prefix, String suffix) {
		System.out.printf("%s%s: %.2f$%s\n", prefix, super.getName(), super.getDonated(), suffix);
		for(Person person: this.persons) {
			System.out.printf("%s  %s: %.2f$%s\n", prefix, person.getName(), person.getDonated(), suffix);
		}
	}
}
