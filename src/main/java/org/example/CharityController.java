package org.example;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/charities")
public class CharityController {

    @GetMapping("/get/{name}")
    public Charity getCharity(@PathVariable String name) {
        try {
            return DatabaseLinker.get_charity(name);
        } catch (Exception e) {
            return new Charity(String.format("Charity with name %s not found", name));
        }
    }

    @PostMapping("/post/{charity_name}/{donation_name}/donors")
    public String addDonor(@PathVariable String charity_name, @PathVariable String donation_name, @RequestBody Donor donor) {
        try {
            int donor_id = DatabaseLinker.upsert_donor(DatabaseLinker.get_donation_id(charity_name, donation_name), donor);
            return String.format("Successfully added Donor! (%d)", donor_id);
        } catch (Exception e) {
            return "Couldn't add Donor";
        }
    }

    @DeleteMapping("/remove/{charityName}/{donationName}/donors/{donorName}")
    public String removeDonor(@PathVariable String charityName, @PathVariable String donationName, @PathVariable String donorName) {
        try {
            int count = DatabaseLinker.remove_donor(charityName, donationName, donorName);
            return count > 0 ? "Successfully deleted Donor" : "Couldn't remove Donor";
        } catch (Exception e) {
            return "Couldn't remove Donor";
        }
    }

    @PostMapping("/post/{charity_name}/donations")
    public String addDonation(@PathVariable String charity_name, @RequestBody Donation donation) {
        try {
            int donation_id = DatabaseLinker.upsert_donation(DatabaseLinker.get_charity_id(charity_name), donation);
            return String.format("Successfully added Donation! (%d)", donation_id);
        } catch (Exception e) {
            return "Couldn't add Donation";
        }
    }
    @DeleteMapping("/remove/{charityName}/donations/{donationName}")
    public String removeDonation(@PathVariable String charityName, @PathVariable String donationName) {
        try {
            int count = DatabaseLinker.remove_donation(charityName, donationName);
            return count > 0 ? "Successfully deleted Donation" : "Couldn't remove Donation";
        } catch (Exception e) {
            return "Couldn't remove Donation";
        }
    }
}