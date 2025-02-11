package org.example;

public class Employee extends Donor {
	public static final String REPRESENTATION = "Employee";
	public Employee(String name, float donated) {
		super(name, donated);
	}
	@Override
	public String stringRepresentation() {
		return Employee.REPRESENTATION;
	}
}
