package org.haivo_charity.service.impl;

import org.haivo_charity.model.Account;
import org.haivo_charity.model.Donate;
import org.haivo_charity.model.Volunteer;
import org.haivo_charity.repository.DonateRepository;
import org.haivo_charity.repository.VolunteerRepository;
import org.haivo_charity.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;

public class VolunteerServiceImpl implements VolunteerService {
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private DonateRepository donateRepository;
    @Override
    public Iterable<Volunteer> findAll() {
        return volunteerRepository.findAll();
    }

    @Override
    public Volunteer findById(Long id) {
        return volunteerRepository.findById(id).orElse(null);
    }

    @Override
    public Volunteer findByName(String name) {
        return volunteerRepository.findFirstByNameOrderByIdDesc(name);
    }

    @Override
    public Volunteer findByPhone(String phone) {
        return volunteerRepository.findFirstByPhoneOrderByIdDesc(phone);
    }

    @Override
    public Volunteer findByEmail(String email) {
        return volunteerRepository.findFirstByEmailOrderByIdDesc(email);
    }

    @Override
    public Volunteer findByAllInfo(String name, String email, String phone) {
        return volunteerRepository.findFirstByNameAndEmailAndPhone(name, email, phone);
    }

    @Override
    public Iterable<Volunteer> getListVolunteerDonateMore(long amount) {
        return volunteerRepository.getListVolunteerDonateMore(amount);
    }

    @Override
    public Volunteer save(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    @Override
    public void remove(Long id) {
        volunteerRepository.deleteById(id);
    }

    @Override
    public long getAmountDonate(Volunteer volunteer) {
        Iterable<Donate> donates = donateRepository.findAllByVolunteer(volunteer);
        long amountDonate = 0;
        for (Donate donate : donates){
            amountDonate += donate.getAmount();
        }
        return amountDonate;
    }

    @Override
    public long getAmountDonors() {
        return volunteerRepository.findAllByDonatesIsNotNull().size();
    }

    @Override
    public Volunteer findByAccount(Account account) {
        return volunteerRepository.findByAccount(account);
    }
}
