package org.haivo_charity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "tag",
//            joinColumns = @JoinColumn(name = "voteId"),
//            inverseJoinColumns = @JoinColumn(name = "categoryId")
//    )
    @JsonIgnore
    private List<Vote> votes;

    public Category() {
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

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
