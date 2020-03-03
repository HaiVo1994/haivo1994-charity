package org.haivo_charity.repository;

import org.haivo_charity.model.Account;
import org.haivo_charity.model.Donate;
import org.haivo_charity.model.Volunteer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VolunteerRepository extends CrudRepository<Volunteer,Long> {
    public Iterable<Volunteer> findAllByDonates(Donate donate);

    @Query("SELECT vol FROM Volunteer vol INNER JOIN Donate don ON don.volunteer = vol " +
            "WHERE SUM(don.amount)>= :amount " +
            "GROUP BY vol")
    public Iterable<Volunteer> getListVolunteerDonateMore(@Param("amount") Long amount);

    public Volunteer findFirstByNameOrderByIdDesc(String name);
    public Volunteer findFirstByEmailOrderByIdDesc(String email);
    public Volunteer findFirstByPhoneOrderByIdDesc(String phone);
    public Volunteer findFirstByNameAndEmailAndPhone(String name, String email, String phone);

    public List<Volunteer> findAllByDonatesIsNotNull();

    public Volunteer findByAccount(Account account);
}
