package org.example;

import java.util.ArrayList;

public class Company extends Donor {
	public static final String REPRESENTATION = "Company";
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	public ArrayList<Employee> getEmployees() {
		return this.employees;
	}
	public void setEmployees(ArrayList<Employee> employees) {
		this.employees.clear();
		this.employees.addAll(employees);
	}
	public void addEmployee(Employee employee) {
		super.donate(employee.getDonated());
		this.employees.add(employee);
	}
	public Company(String name, float donated) {
		super(name, donated);
	}
	public Company(String name, float donated, ArrayList<Employee> employees) {
		super(name, donated);
		this.setEmployees(employees);
	}
	@Override
	public String stringRepresentation() {
		return Company.REPRESENTATION;
	}
	@Override
	public void donate(float money) {
		System.out.println("ERROR: can't do this");
		System.exit(1);
	}
	public void donate(String name, float money) {
		super.setDonated(super.getDonated() + money);
		for(int i = 0; i < this.employees.size(); ++i) {
			if(this.employees.get(i).getName() == name) {
				this.employees.get(i).setDonated(this.employees.get(i).getDonated() + money);
				return;
			}
		}
		this.employees.add(new Employee(name, money));
	}
	public float getDonated(String name) {
		for(Employee employee: this.employees) {
			if(employee.getName() == name) return employee.getDonated();
		}
		return -1;
	}
	public Employee getEmployee(String name) {
		for(Employee employee: this.employees) {
			if(employee.getName() == name) {
				return employee;
			}
		}
		System.out.println("ERROR: didn't find employee");
		System.exit(1);
		return new Employee(name, 0);
	}
	@Override
	public void print(String prefix, String suffix) {
		System.out.printf("%s%s: %.2f$%s\n", prefix, super.getName(), super.getDonated(), suffix);
		for(Employee employee: this.employees) {
			System.out.printf("%s  %s: %.2f$%s\n", prefix, employee.getName(), employee.getDonated(), suffix);
		}
	}
}
