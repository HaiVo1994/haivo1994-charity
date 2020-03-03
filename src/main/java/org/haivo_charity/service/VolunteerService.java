package org.haivo_charity.service;

import org.haivo_charity.model.Account;
import org.haivo_charity.model.Volunteer;

public interface VolunteerService {
    Iterable<Volunteer> findAll();
    Volunteer findById(Long id);
    Volunteer findByName(String name);
    Volunteer findByPhone(String phone);
    Volunteer findByEmail(String email);
    Volunteer findByAllInfo(String name, String email, String phone);
    Iterable<Volunteer> getListVolunteerDonateMore(long amount);
    Volunteer save(Volunteer volunteer);
    void remove(Long id);
    long getAmountDonate(Volunteer volunteer);
    long getAmountDonors();

    Volunteer findByAccount(Account account);
}
