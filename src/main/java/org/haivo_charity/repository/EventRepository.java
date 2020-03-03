package org.haivo_charity.repository;

import org.haivo_charity.model.Event;
import org.haivo_charity.model.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends CrudRepository<Event, Long> {
    Event findEventByVote(Vote vote);

    @Query("SELECT e FROM Event e " +
            "WHERE e.isFinished = false " +
            "AND e.registrationDeadline >= current_date " +
            "ORDER BY e.created_at asc")
    Page<Event> getAllEvent(Pageable pageable);
    @Query("SELECT count(e) FROM Event e " +
            "WHERE e.isFinished = false " +
            "AND e.registrationDeadline >= current_date ")
    int countEvent();

    @Query("SELECT e FROM Event e " +
            "WHERE e.isFinished = false " +
            "AND e.registrationDeadline < current_date " +
            "AND e.beginDate >= current_date " +
            "ORDER BY e.created_at asc")
    Page<Event> getAllEventMustAcceptRegister(Pageable pageable);
    @Query("SELECT count(e) FROM Event e " +
            "WHERE e.isFinished = false " +
            "AND e.registrationDeadline < current_date " +
            "AND e.beginDate >=current_date ")
    int countEventMustAcceptRegister();

    @Query("SELECT e FROM Event e " +
            "WHERE e.isFinished = false " +
            "AND e.finishDate < current_date " +
            "ORDER BY e.created_at asc")
    Page<Event> getAllEventMustFinish(Pageable pageable);
    @Query("SELECT count(e) FROM Event e " +
            "WHERE e.isFinished = false " +
            "AND e.finishDate < current_date ")
    int countEventMustFinish();

    @Query("SELECT e FROM Event e " +
            "WHERE e.vote.proposal_by = :userName " +
            "ORDER BY e.created_at asc ")
    Page<Event> getEventByUser(@Param("userName") String userName, Pageable pageable);
    @Query("SELECT count(e) FROM Event e " +
            "WHERE e.vote.proposal_by = :userName ")
    int countEventByUser(@Param("userName") String userName);

    @Query("SELECT count(e) FROM Event e " +
            "WHERE e.isFinished = true ")
    long countEventFinish();
}
