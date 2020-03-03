package org.haivo_charity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "donate")
public class Donate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idDonate;
    private Long amount;
    private Date date;
    private String personName;
    @ColumnDefault(value = "0")
    private boolean isHided;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idOfvolunteerr")
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOfVote")
    @JsonIgnore
    private Vote vote;

    public Donate() {
    }

    public Long getIdDonate() {
        return idDonate;
    }

    public void setIdDonate(Long idDonate) {
        this.idDonate = idDonate;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public boolean isHided() {
        return isHided;
    }

    public void setHided(boolean hided) {
        isHided = hided;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
