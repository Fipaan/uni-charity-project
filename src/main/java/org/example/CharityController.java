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
            return null;
        }
    }

    @PostMapping("/post/{charity_name}/{donation_name}/donors")
    public int addDonor(@PathVariable String charity_name, @PathVariable String donation_name, @RequestBody Donor donor) {
        try {
            return DatabaseLinker.upsert_donor(DatabaseLinker.get_donation_id(charity_name, donation_name), donor) != -1 ? 1 : 0;
        } catch (Exception e) {
            return -1;
        }
    }

    @PostMapping("/post/{charity_name}/donations")
    public int addDonation(@PathVariable String charity_name, @RequestBody Donation donation) {
        try {
            return DatabaseLinker.upsert_donation(DatabaseLinker.get_charity_id(charity_name), donation) != -1 ? 1 : 0;
        } catch (Exception e) {
            return -1;
        }
    }
    @DeleteMapping("/remove/{charityName}/donations/{donationName}")
    public int removeDonation(@PathVariable String charityName, @PathVariable String donationName) {
        try {
            return DatabaseLinker.remove_donation(charityName, donationName) != -1 ? 1 : 0;
        } catch (Exception e) {
            return -1;
        }
    }
}