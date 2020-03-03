package org.haivo_charity.repository;

import org.haivo_charity.model.Category;
import org.haivo_charity.model.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface VoteRepository extends CrudRepository<Vote, Long> {
    Iterable<Vote> findAllByCategories(Category category);
    Iterable<Vote> getAllByFinishDateIsNull();
    @Query("SELECT v " +
            "FROM Vote v " +
            "WHERE v.event IS NULL " +
            "AND v.isAccepted = true " +
            "ORDER BY v.created_at desc")
    Page<Vote> getListDateDesc(Pageable pageable);
    Long countAllByCategories(Category category);
    @Query("SELECT COUNT(v.id) " +
            "FROM Vote v " +
            "WHERE v.event IS NULL " +
            "AND v.isAccepted = true ")
    Long countAllVoteNotFinish();
    Page<Vote> findAllByCategoriesAndEventIsNull(Category category, Pageable pageable);
    List<Vote> findAllByCategoriesAndEventIsNull(Category category);
    Page<Vote> findAllByFinishDateIsNull(Pageable pageable);

    @Query("SELECT v " +
            "FROM Vote v " +
            "WHERE v.isAccepted = true " +
            "AND v.event IS null " +
            "AND v.tittle LIKE :tittle " +
            "ORDER BY v.created_at desc"
    )
    Page<Vote> findAllByTittle(@Param("tittle") String tittle, Pageable pageable);
    @Query("SELECT COUNT(v.id) " +
            "FROM Vote v " +
            "WHERE v.isAccepted = true " +
            "AND v.event IS null " +
            "AND v.tittle LIKE :tittle " +
            "ORDER BY v.created_at desc")
    Long countByTittle(@Param("tittle") String tittle);

    @Query("SELECT v " +
            "FROM Vote v " +
            "WHERE v.isAccepted = true " +
            "AND v.event IS NULL " +
            "ORDER BY v.created_at desc")
    Iterable<Vote> findAllVoteAccepted();
    @Query("SELECT v " +
            "FROM Vote v " +
            "WHERE v.isAccepted = true " +
            "AND v.event IS NULL " +
            "ORDER BY v.created_at desc")
    Page<Vote> findAllVoteAccepted(Pageable pageable);

    @Query("SELECT v FROM Vote v " +
            "WHERE v.isAccepted = false " +
            "ORDER BY v.proposal_at asc")
    Page<Vote> getListProposal(Pageable pageable);

    @Query("SELECT COUNT(v.id) FROM Vote v " +
            "WHERE v.isAccepted = false ")
    int countProposal();

    @Query("SELECT v FROM Vote v " +
            "WHERE v.isAccepted = false " +
            "AND v.proposal_by LIKE :proposalPeople " +
            "ORDER BY v.proposal_at asc")
    Page<Vote> getListProposalUser(@Param("proposalPeople") String proposalPeople, Pageable pageable);

    @Query("SELECT COUNT(v.id) FROM Vote v " +
            "WHERE v.isAccepted = false " +
            "AND v.proposal_by LIKE :proposalPeople ")
    int countUserProposal(@Param("proposalPeople") String proposalPeople);

    @Query("SELECT v FROM Vote v " +
            "WHERE v.isAccepted = true " +
            "AND v.proposal_by LIKE :proposalPeople " +
            "AND v.event IS NULL " +
            "ORDER BY v.id DESC ")
    Page<Vote> getListVoteProposalByUser
            (@Param("proposalPeople") String proposalPeople, Pageable pageable);
    @Query("SELECT COUNT(v.id) FROM Vote v " +
            "WHERE v.isAccepted = true " +
            "AND v.proposal_by LIKE :proposalPeople " +
            "AND v.event IS NULL " +
            "ORDER BY v.id DESC ")
    int countUserProposalAccepted(@Param("proposalPeople") String proposalPeople);

//    @Query("SELECT v FROM Vote v " +
//            "WHERE v.event IS NOT null " +
//            "AND v.proposal_by LIKE :proposalPeople")
//    Page<Vote> getListEventProposalByUser
//            (@Param("proposalPeople") String proposalPeople, Pageable pageable);
//    @Query("SELECT COUNT(v.id) FROM Vote v " +
//            "WHERE v.event IS NOT NULL " +
//            "AND v.proposal_by LIKE :proposalPeople ")
//    int countUserProposalEvent(@Param("proposalPeople") String proposalPeople);

    @Query("SELECT v From Vote v " +
            "WHERE v.event IS NULL " +
            "AND v.isAccepted=true " +
            "AND v.finishDate <= :today " +
            "ORDER BY v.id DESC ")
    Page<Vote> votesWerePast(@Param("today") Date today, Pageable pageable);
    @Query("SELECT COUNT(v) From Vote v " +
            "WHERE v.event IS NULL " +
            "AND v.isAccepted=true " +
            "AND v.finishDate <= :today " +
            "ORDER BY v.id DESC ")
    int amountOfVotesWerePast(@Param("today") Date today);
}
