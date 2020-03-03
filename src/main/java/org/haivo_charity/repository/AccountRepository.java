package org.haivo_charity.repository;

import org.haivo_charity.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findFirstByUsername(String username);
    @Query("SELECT COUNT (a) FROM Account a " +
            "WHERE a.registerEvents.size>0")
    long countVolunteer();
}
