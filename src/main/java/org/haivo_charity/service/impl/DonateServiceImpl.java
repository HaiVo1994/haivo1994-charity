package org.haivo_charity.service.impl;

import org.haivo_charity.model.Donate;
import org.haivo_charity.model.Volunteer;
import org.haivo_charity.model.Vote;
import org.haivo_charity.repository.DonateRepository;
import org.haivo_charity.service.DonateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class DonateServiceImpl implements DonateService {
    @Autowired
    private DonateRepository donateRepository;
//    @Override
//    public Iterable<Donate> findAllByVolunteer(Volunteer volunteer) {
//        return donateRepository.findAllByVolunteer(volunteer);
//    }

    @Override
    public Iterable<Donate> findAll() {
        return donateRepository.findAll();
    }

    @Override
    public Donate findById(Long id) {
        return donateRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<Donate> findAllByVote(Vote vote) {
        return donateRepository.findAllByVote(vote);
    }

    @Override
    public void save(Donate donate) {
        donateRepository.save(donate);
    }

    @Override
    public void remove(Long id) {
        donateRepository.deleteById(id);
    }

    @Override
    public Long getTotalDonate() {
        return donateRepository.sumDonate();
    }

    @Override
    public Long getTotalDonateOfVote(Vote vote) {
        return donateRepository.sumDonateOfVote(vote);
    }

    @Override
    public List<Donate> getListDonateOfVote(Vote vote, int page, int size) {
        return donateRepository.findAllByVote(vote, PageRequest.of(page,size)).toList();
    }

    @Override
    public int getAmountPage(Vote vote, int size) {
        int amountDonate = donateRepository.countAllByVote(vote);
        return (int) Math.ceil((double) amountDonate/size);
    }
}
