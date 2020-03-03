package org.haivo_charity.service;


import org.haivo_charity.model.Category;
import org.haivo_charity.model.Vote;
import org.haivo_charity.model.VoteImage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VoteService {
    Iterable<Vote> findAllVoteAccepted();
    Vote findById(Long id);
    void save(Vote vote);
    void remove(Long id);
    long getAmountDonate(Vote vote);
    Iterable<Category> findAllCategory(Vote vote);
    List<Vote> getListDateDesc(int size);

    List<Vote> findByVotes(Category category, int page, int size);
    List<Vote> findAllVoteAccepted(Pageable pageable);
    List<Vote> findByTittle(String tittle, Pageable pageable);
    long countByTittle(String tittle);
    void createProposal(
            Vote vote, List<VoteImage> voteImages
    );

    List<Vote> getListProposal(int page, int size);
    List<Vote> getListProposalUser(String userName, int page, int size);
    boolean acceptProposal(Long id, String admin);
    void editProposal(Vote vote, Vote newVote, boolean accept, String user);
    int getAmountPageProposal(int size);
    int getAmountPageUserProposal(String user, int size);

    List<Vote> getListVoteOfUser(String userName, int page, int size);
    int getAmountPageUserVote(String userName, int size);

    List<Vote> getVotesWerePast(int page, int size);
    int amountPageOfVotesWerePast(int size);
}
