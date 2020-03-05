package org.haivo_charity.repository;

import org.haivo_charity.model.Donate;
import org.haivo_charity.model.Volunteer;
import org.haivo_charity.model.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonateRepository extends CrudRepository<Donate, Long> {
    Iterable<Donate> findAllByVolunteer(Volunteer volunteer);
    Iterable<Donate> findAllByVote(Vote vote);
    Page<Donate> findAllByVote(Vote vote, Pageable pageable);
    int countAllByVote(Vote vote);

    @Query("SELECT SUM(amount) FROM Donate")
    Long sumDonate();

    @Query("SELECT SUM(amount) FROM Donate WHERE vote = :vote")
    Long sumDonateOfVote(@Param("vote") Vote vote);

    @Query("SELECT distinct d.vote  FROM Donate d " +
            "WHERE d.volunteer = :volunteer")
    Page<Vote> getListAccountDonate(@Param("volunteer") Volunteer volunteer, Pageable pageable);
    @Query("SELECT distinct d.vote  FROM Donate d " +
            "WHERE d.volunteer = :volunteer")
    List<Vote> getListAccountDonate(@Param("volunteer") Volunteer volunteer);
    @Query("SELECT SUM(d.amount) FROM Donate d " +
            "WHERE d.volunteer = :volunteer " +
            "AND d.vote = :vote")
    long amountAccountForVote(@Param("volunteer") Volunteer volunteer, @Param("vote") Vote vote);

}
