
import java.util.Objects;

public abstract class Donor {
	private String name;
	private float donated;
	public abstract String stringRepresentation();
	@Override
	public String toString() {
		return this.stringRepresentation() + ": " + this.name + ", donated: " + this.donated + "$";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Donor donor = (Donor) obj;
		return Objects.equals(this.name, donor.name);
	}
	@Override
	public int hashCode() {
		return Objects.hash(this.stringRepresentation(), this.name);
	}
	public Donor(String name, float donated) {
		this.name = name;
		this.donated = donated;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public abstract float getDonated(String name);
	public float getDonated() {
		return this.donated;
	}
	public void setDonated(float donated) {
		this.donated = donated;
	}
	public void donate(float money) {
		this.donated += money;
	}
	public void donate(String name, float money) {
		System.out.println("ERROR: can't do this");
	}
	public abstract Person getPerson(String name);
	public abstract void print(String prefix, String suffix);
	public void print(String prefix) {
		this.print(prefix, "");
	}
	public void print() {
		this.print("", "");
	}
}
