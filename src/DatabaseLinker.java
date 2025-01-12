import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;

public class DatabaseLinker {
	private static final String URL = "jdbc:postgresql://localhost:5432/UniCharity";

    // Read the environment variables set in ~/.bashrc
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

	private static Connection connection;

    // Create and return a connection to the database
    public static void connect() {
        try {
			DatabaseLinker.connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
    }
	public static int remove_donor(int donor_id) throws SQLException {
		String sql = "DELETE FROM donor WHERE donor_id = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, donor_id);
		int count = stmt.executeUpdate();
		return count;
	}
	public static int remove_donor(String charity_name, String donation_name, String donor_name) throws SQLException {
		String sql = "DELETE FROM donor WHERE name = ? AND donation_id = ? AND subclass != 'Employee'";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, donor_name);
		stmt.setInt(2, DatabaseLinker.get_donation_id(charity_name, donation_name));
		int count = stmt.executeUpdate();
		return count;
	}
	public static int remove_donation(int charity_id, String donation_name) throws SQLException {
		String sql = "DELETE FROM donation WHERE name = ? AND charity_id = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, donation_name);
		stmt.setInt(2, charity_id);
		int count = stmt.executeUpdate();
		return count;
	}
	public static int remove_donation(String charity_name, String donation_name) throws SQLException {
		return DatabaseLinker.remove_donation(DatabaseLinker.get_charity_id(charity_name), donation_name);
	}
	public static int remove_charity(String name) throws SQLException {
		String sql = "DELETE FROM charity WHERE name = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, name);
		int count = stmt.executeUpdate();
		return count;
	}	
	public static int upsert_employee(int donation_id, int company_id, Employee employee) throws SQLException {
		String sql = "SELECT * FROM Employee WHERE name = ? AND donation_id = ? AND company_id = ?";
		PreparedStatement stmt = DatabaseLinker.connection.prepareStatement(sql);
		stmt.setString(1, employee.getName());
		stmt.setInt(2, donation_id);
		stmt.setInt(3, company_id);
		int donor_id = -1;
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			donor_id = rs.getInt("donor_id");
			sql = "UPDATE Employee SET donation_id = ?, name = ?, donated = ?, subclass = ?, company_id = ? WHERE donor_id = ? RETURNING donor_id";
		} else {
			sql = "INSERT INTO Employee (donation_id, name, donated, subclass, company_id) VALUES (?, ?, ?, ?, ?) RETURNING donor_id";
		}
		stmt = DatabaseLinker.connection.prepareStatement(sql);
		stmt.setInt(1, donation_id);
		stmt.setString(2, employee.getName());
		stmt.setFloat(3, employee.getDonated());
		stmt.setString(4, employee.stringRepresentation());
		stmt.setInt(5, company_id);
		if(donor_id != -1) {
			stmt.setInt(6, donor_id);
		}
		rs = stmt.executeQuery();
		rs.next();
		return rs.getInt("donor_id");
	}
	public static int upsert_company(int donation_id, Company company) throws SQLException {
		String sql = "SELECT * FROM Donor WHERE name = ? AND donation_id = ?";
		PreparedStatement stmt = DatabaseLinker.connection.prepareStatement(sql);
		stmt.setString(1, company.getName());
		stmt.setInt(2, donation_id);
		int donor_id = -1;
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			donor_id = rs.getInt("donor_id");
			DatabaseLinker.remove_donor(donor_id);
		}
		sql = "INSERT INTO Donor (donation_id, name, donated, subclass) VALUES (?, ?, ?, ?) RETURNING donor_id";
		stmt = DatabaseLinker.connection.prepareStatement(sql);
		stmt.setInt(1, donation_id);
		stmt.setString(2, company.getName());
		stmt.setFloat(3, company.getDonated());
		stmt.setString(4, company.stringRepresentation());
		rs = stmt.executeQuery();
		rs.next();
		if(donor_id == -1) {
			donor_id = rs.getInt("donor_id");
			sql = "INSERT INTO CompanyData (donor_id) VALUES (?) RETURNING company_id";
		} else {
			sql = "SELECT company_id FROM CompanyData WHERE donor_id = ?";
		}
		stmt = DatabaseLinker.connection.prepareStatement(sql);
		stmt.setInt(1, donor_id);
		rs = stmt.executeQuery();
		rs.next();
		int company_id = rs.getInt("company_id");
		for(Employee employee : company.getEmployees()) {
			DatabaseLinker.upsert_employee(donation_id, company_id, employee);
		}
		return donor_id;
	}
	public static int upsert_donor(int donation_id, Donor donor) throws SQLException, ClassNotFoundException {
		switch(donor.stringRepresentation()) {
			case Donor.REPRESENTATION: break;
			case Company.REPRESENTATION: return DatabaseLinker.upsert_company(donation_id, (Company) donor);
			case Employee.REPRESENTATION: throw new ClassNotFoundException("Employee shouldn't be on top layer");
			default: throw new ClassNotFoundException("Unknown Donor type");
		}
		String sql = "SELECT * FROM Donor WHERE name = ? AND donation_id = ?";
		PreparedStatement stmt = DatabaseLinker.connection.prepareStatement(sql);
		stmt.setString(1, donor.getName());
		stmt.setInt(2, donation_id);
		int donor_id = -1;
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			donor_id = rs.getInt("donor_id");
			DatabaseLinker.remove_donor(donor_id);	
		}
		sql = "INSERT INTO Donor (donation_id, name, donated, subclass) VALUES (?, ?, ?, ?) RETURNING donor_id";
		stmt = DatabaseLinker.connection.prepareStatement(sql);
		stmt.setInt(1, donation_id);
		stmt.setString(2, donor.getName());
		stmt.setFloat(3, donor.getDonated());
		stmt.setString(4, donor.stringRepresentation());
		rs = stmt.executeQuery();
		rs.next();
		return rs.getInt("donor_id");
	}
	public static int upsert_donation(int charity_id, Donation donation) throws SQLException, ClassNotFoundException {
		String sql = "SELECT * FROM Donation WHERE name = ? AND charity_id = ?";
		PreparedStatement stmt = DatabaseLinker.connection.prepareStatement(sql);
		stmt.setString(1, donation.getName());
		stmt.setInt(2, charity_id);
		int donation_id = -1;
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			donation_id = rs.getInt("donation_id");
			DatabaseLinker.remove_donation(charity_id, donation.getName());
		}
		sql = "INSERT INTO Donation (charity_id, name, collected, goal, reached_goal) VALUES (?, ?, ?, ?, ?) RETURNING donation_id";
		stmt = DatabaseLinker.connection.prepareStatement(sql);
		stmt.setInt(1, charity_id);
		stmt.setString(2, donation.getName());
		stmt.setFloat(3, donation.getCollected());
		stmt.setFloat(4, donation.getGoal());
		stmt.setBoolean(5, donation.isReachedGoal());
		rs = stmt.executeQuery();
		rs.next();
		donation_id = rs.getInt("donation_id");
		for (Donor donor : donation.getDonors()) {
			DatabaseLinker.upsert_donor(donation_id, donor);
		}
		return donation_id;
	}
	public static int upsert_charity(Charity charity) throws SQLException, ClassNotFoundException {
		String sql = "SELECT * FROM Charity WHERE name = ?";
		PreparedStatement stmt = DatabaseLinker.connection.prepareStatement(sql);
		stmt.setString(1, charity.getName());
		int charity_id = -1;
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			charity_id = rs.getInt("charity_id");
			DatabaseLinker.remove_charity(charity.getName());
		}
		sql = "INSERT INTO Charity (name) VALUES (?) RETURNING charity_id";
		stmt = DatabaseLinker.connection.prepareStatement(sql);
		stmt.setString(1, charity.getName());
		rs = stmt.executeQuery();
		rs.next();
		charity_id = rs.getInt("charity_id");
		for (Donation donation : charity.getDonations()) {
			DatabaseLinker.upsert_donation(charity_id, donation);
		}
		return charity_id;
	}
	public static int get_charity_id(String name) throws SQLException {
		String sql = "SELECT * FROM Charity WHERE name = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			return rs.getInt("charity_id");
		}	
		return -1;
	}
	public static boolean is_charity_exists(String name) throws SQLException, ClassNotFoundException {
		return DatabaseLinker.get_charity_id(name) != -1;
	}
	public static Employee get_employee(int donor_id) throws SQLException, ClassNotFoundException {
		String sql = "SELECT * FROM Employee WHERE donor_id = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, donor_id);
		ResultSet rs = stmt.executeQuery();
		if(!rs.next()) throw new ClassNotFoundException("Employee not found");
		return new Employee(rs.getString("name"), rs.getFloat("donated"));
	}
	public static ArrayList<Employee> get_employees(int company_id) throws SQLException, ClassNotFoundException {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		String sql = "SELECT * FROM Employee WHERE company_id = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, company_id);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			employees.add(DatabaseLinker.get_employee(rs.getInt("donor_id")));
		}
		return employees;
	}
	public static int get_company_id(int donor_id) throws SQLException, ClassNotFoundException {
		String sql = "SELECT * FROM CompanyData WHERE donor_id = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, donor_id);
		ResultSet rs = stmt.executeQuery();
		if(!rs.next()) throw new ClassNotFoundException("Company not found");
		return rs.getInt("company_id");
	}
	public static Donor get_donor(int donor_id) throws SQLException, ClassNotFoundException {
		String sql = "SELECT * FROM Donor WHERE donor_id = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, donor_id);
		ResultSet rs = stmt.executeQuery();
		if(!rs.next()) return new Donor(null, 0.0f);
		switch(rs.getString("subclass")) {
			case Donor.REPRESENTATION: return new Donor(rs.getString("name"), rs.getFloat("donated"));
			case Company.REPRESENTATION: return new Company(
												 rs.getString("name"),
												 rs.getFloat("donated"),
												 DatabaseLinker.get_employees(DatabaseLinker.get_company_id(donor_id))
												 );
			case Employee.REPRESENTATION: return new Donor(null, -1.0f);
			default: throw new ClassNotFoundException("Unknown type of Donor");
		}
	}
	public static ArrayList<Donor> get_donors(int donation_id) throws SQLException, ClassNotFoundException {
		ArrayList<Donor> donors = new ArrayList<Donor>();
		String sql = "SELECT * FROM Donor WHERE donation_id = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, donation_id);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			Donor donor = DatabaseLinker.get_donor(rs.getInt("donor_id"));
			if(donor.getName() == null) continue;
			donors.add(donor);
		}
		return donors;
	}
	public static Charity get_charity(String name) throws SQLException, ClassNotFoundException {
		String sql = "SELECT * FROM Charity WHERE name = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();
		Charity charity = new Charity(name);
		if(!rs.next()) throw new ClassNotFoundException("Charity does not exists!");
		int charity_id = rs.getInt("charity_id");
		sql = "SELECT * FROM Donation WHERE charity_id = ?";
		stmt = connection.prepareStatement(sql);
		stmt.setInt(1, charity_id);
		rs = stmt.executeQuery();
		ArrayList<Donation> donations = new ArrayList<Donation>();
		while(rs.next()) {
			donations.add(new Donation(rs.getString("name"),
									   rs.getFloat("collected"),
									   rs.getFloat("goal"),
									   rs.getBoolean("reached_goal"),
									   DatabaseLinker.get_donors(rs.getInt("donation_id"))
									   ));	
		}
		charity.setDonations(donations);
		return charity;
	}
	public static int get_donation_id(int charity_id, String donation_name) throws SQLException {
		String sql = "SELECT * FROM donation WHERE name = ? AND charity_id = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, donation_name);
		stmt.setInt(2, charity_id);
		ResultSet rs = stmt.executeQuery();
		if(!rs.next()) return -1;
		return rs.getInt("donation_id");
	}
	public static int get_donation_id(String charity_name, String donation_name) throws SQLException {
		return DatabaseLinker.get_donation_id(DatabaseLinker.get_charity_id(charity_name), donation_name);
	}	
}
