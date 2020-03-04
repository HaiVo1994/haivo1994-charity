package org.haivo_charity.service.impl;

import org.haivo_charity.model.Category;
import org.haivo_charity.model.Donate;
import org.haivo_charity.model.Vote;
import org.haivo_charity.model.VoteImage;
import org.haivo_charity.model.support.RegExp;
import org.haivo_charity.repository.CategoryRepository;
import org.haivo_charity.repository.DonateRepository;
import org.haivo_charity.repository.VoteImageRepository;
import org.haivo_charity.repository.VoteRepository;
import org.haivo_charity.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoteServiceImpl implements VoteService {
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private DonateRepository donateRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VoteImageRepository voteImageRepository;
    @Override
    public Iterable<Vote> findAllVoteAccepted() {
        return voteRepository.findAllVoteAccepted();
    }

    @Override
    public Vote findById(Long id) {
        return voteRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Vote vote) {
        voteRepository.save(vote);
    }

    @Override
    public void remove(Long id) {
        voteImageRepository.deleteAllByVoteId(id);
        voteRepository.deleteById(id);
    }

    @Override
    public long getAmountDonate(Vote vote) {
        Iterable<Donate> donates = donateRepository.findAllByVote(vote);
        long amountDonate = 0;
        for (Donate donate : donates){
            amountDonate += donate.getAmount();
        }
        return amountDonate;
    }

    @Override
    public Iterable<Category> findAllCategory(Vote vote) {
        return categoryRepository.findAllByVotes(vote);
    }

    @Override
    public List<Vote> getListDateDesc(int size) {
        return voteRepository.getListDateDesc(PageRequest.of(0, size)).toList();
    }

    @Override
    public List<Vote> findByVotes(Category category, int page, int size) {
        List<Vote> votes = voteRepository.findAllByCategoriesAndEventIsNull(category);
        List<Vote> listVoteAccepted = new ArrayList<>();
        for (Vote vote: votes){
            if (vote.isAccepted()){
                listVoteAccepted.add(vote);
            }
        }
        int begin = page * size;
        List<Vote> result = new ArrayList<>();
        for (int i= begin; i<begin+size;i++){
            if (i<listVoteAccepted.size()){
                result.add(listVoteAccepted.get(i));
            }else {
                break;
            }
        }
        return result;
    }

    @Override
    public List<Vote> findAllVoteAccepted(Pageable pageable) {
        return voteRepository.findAllVoteAccepted(pageable).toList();
    }

    @Override
    public List<Vote> findByTittle(String tittle, Pageable pageable) {
        tittle = "%" + tittle + "%";
        return voteRepository.findAllByTittle(tittle, pageable).toList();
    }

    @Override
    public long countByTittle(String tittle) {
        tittle = "%" + tittle + "%";
        return voteRepository.countByTittle(tittle);
    }

    @Override
    public void createProposal(Vote vote, List<VoteImage> voteImages) {
        vote = voteRepository.save(vote);
        for (VoteImage voteImage: voteImages
             ) {
            voteImage.setVote(vote);
            voteImageRepository.save(voteImage);
        }
    }

    @Override
    public List<Vote> getListProposal(int page, int size) {
        return voteRepository.getListProposal(PageRequest.of(page,size)).toList();
    }

    @Override
    public List<Vote> getListProposalUser(String userName, int page, int size) {
        return voteRepository.getListProposalUser(userName, PageRequest.of(page,size)).toList();
    }

    @Override
    public boolean acceptProposal(Long id, String admin) {
        Vote vote = voteRepository.findById(id).orElse(null);
        if (vote!=null){
            if (vote.isAccepted()){
                return false;
            }
            else {
                vote.setAccepted(true);
                vote.setUpdated_at(new Date());
                vote.setUpdated_by(admin);
                voteRepository.save(vote);
                return true;
            }
        }
        return false;
    }

    @Override
    public void editProposal(Vote vote, Vote newVote, boolean accept, String user) {
        vote.setSummary(
                RegExp.removeHTML(newVote.getSummary())
        );
        vote.setCategories(newVote.getCategories());
        vote.setUpdated_by(user);
        vote.setUpdated_at(new Date());
        vote.setGoal(newVote.getGoal());
        vote.setFinishDate(newVote.getFinishDate());
        vote.setBeginDate(newVote.getBeginDate());
        vote.setTittle(
                RegExp.removeHTML(newVote.getTittle())
        );
        vote.setContent(
                RegExp.removeHTML(newVote.getContent())
        );
        vote.setAccepted(accept);
        vote.setLocalVote(
                RegExp.removeHTML(newVote.getLocalVote())
        );
        vote.setRepresentative(
                RegExp.removeHTML(newVote.getRepresentative())
        );
        voteRepository.save(vote);
    }

    @Override
    public int getAmountPageProposal(int size) {
        return (int) Math.ceil((double) voteRepository.countProposal()/size);
    }

    @Override
    public int getAmountPageUserProposal(String user, int size) {
        return (int) Math.ceil((double) voteRepository.countUserProposal(user)/size);
    }

    @Override
    public List<Vote> getListVoteOfUser(String userName, int page, int size) {
        return voteRepository.getListVoteProposalByUser(userName, PageRequest.of(page,size)).toList();
    }

    @Override
    public int getAmountPageUserVote(String userName, int size) {
        return (int) Math.ceil((double) voteRepository.countUserProposalAccepted(userName)/size);
    }

    @Override
    public List<Vote> getVotesWerePast(int page, int size) {
        return voteRepository.votesWerePast(new Date(), PageRequest.of(page,size)).toList();
    }

    @Override
    public int amountPageOfVotesWerePast(int size) {
        return (int) Math.ceil((double) voteRepository.amountOfVotesWerePast(new Date())/size);
    }
}
