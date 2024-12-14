
public class Donor {
	private String name;
	private float donated;
	public Donor(String name, float donated) {
		this.name = name;
		this.donated = donated;
	}
	public void donate(float money) {
		this.donated += money;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getDonated() {
		return this.donated;
	}
	public void print(String prefix, String suffix) {
		System.out.printf("%s%s: %.2f$%s\n", prefix, this.name, this.donated, suffix);
	}
	public void print(String prefix) {
		this.print(prefix, "");
	}
	public void print() {
		this.print("", "");
	}
}
