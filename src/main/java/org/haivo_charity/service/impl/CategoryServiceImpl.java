package org.haivo_charity.service.impl;

import org.haivo_charity.model.Category;
import org.haivo_charity.model.Vote;
import org.haivo_charity.repository.CategoryRepository;
import org.haivo_charity.repository.VoteRepository;
import org.haivo_charity.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void remove(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Iterable<Category> findAllByVote(Vote vote) {
        return categoryRepository.findAllByVotes(vote);
    }

    @Override
    public long countVoteVyCategory(Category category) {
        List<Vote> votes = voteRepository.findAllByCategoriesAndEventIsNull(category);
        long count = 0;
        for(Vote vote:votes){
            if (vote.isAccepted())
                count++;
        }
//        return voteRepository.countAllByCategories(category);
        return count;
    }


    @Override
    public long countAll() {
        return voteRepository.countAllVoteNotFinish();
    }

}
