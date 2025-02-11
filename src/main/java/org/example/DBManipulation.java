package org.example;

import java.sql.SQLException;

public class DBManipulation {
	public static boolean remove_donation(Charity charity, String donation_name) throws SQLException {
		if(DatabaseLinker.remove_donation(charity.getName(), donation_name) == 1) {
			charity.removeDonation(donation_name);
			return true;
		}
		return false;
	}
	public static boolean remove_donor(Charity charity, String donation_name, String donor_name) throws SQLException {
		if(DatabaseLinker.remove_donor(charity.getName(), donation_name, donor_name) == 1) {
			charity.getDonation(donation_name).removeDonor(donor_name);
			return true;
		}
		return false;
	}
}
