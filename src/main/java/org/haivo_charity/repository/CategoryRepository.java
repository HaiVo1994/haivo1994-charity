package org.haivo_charity.repository;

import org.haivo_charity.model.Category;
import org.haivo_charity.model.Vote;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Iterable<Category> findAllByVotes(Vote vote);
}
