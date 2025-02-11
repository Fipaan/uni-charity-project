package org.example;

import java.util.Objects;

public class Donor {
	public static final String REPRESENTATION = "Donor";
	private String name;
	private float donated;
	public String stringRepresentation() {
		return Donor.REPRESENTATION;
	}
	@Override
	public String toString() {
		return this.stringRepresentation() + ": " + this.name + ", donated: " + String.format("%.2f", this.donated) + "$";
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
	public void print(String prefix, String suffix) {
		System.out.printf("%s%s: %.2f$%s\n", prefix, this.getName(), this.getDonated(), suffix);
	}
	public void print(String prefix) {
		this.print(prefix, "");
	}
	public void print() {
		this.print("", "");
	}
}
