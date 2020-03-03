package org.haivo_charity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "volunteer")
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String phone;
    @JsonIgnore
    private Date created_at;
    @JsonIgnore
    private Date deleted_at;
    @JsonIgnore
    private Date deleted_by;
    @JsonIgnore
    private Date updated_at;
    @JsonIgnore
    private Date updated_by;

    @OneToMany(mappedBy = "volunteer", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Donate> donates;

    @OneToOne
    private Account account;

    public Volunteer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    public Date getDeleted_by() {
        return deleted_by;
    }

    public void setDeleted_by(Date deleted_by) {
        this.deleted_by = deleted_by;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(Date updated_by) {
        this.updated_by = updated_by;
    }

    public List<Donate> getDonates() {
        return donates;
    }

    public void setDonates(List<Donate> donates) {
        this.donates = donates;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
