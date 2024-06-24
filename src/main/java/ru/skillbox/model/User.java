
package ru.skillbox.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.Set;

import org.hibernate.annotations.SQLDelete;

@Entity
@Embeddable
@Table(
        name = "users"
)
@SQLDelete(
        sql = "update postgres.users_schema.users set deleted=true where id=?"
)
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String firstName;
    private String secondName;
    private String email;
    private boolean deleted = false;
    @ManyToMany
    @JoinTable(
            name = "subscription",
            joinColumns = {@JoinColumn(
                    name = "user_id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "subscriber_id"
            )}
    )
    private Set<User> subscribers;

    public Long getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getSecondName() {
        return this.secondName;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public Set<User> getSubscribers() {
        return this.subscribers;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(final String secondName) {
        this.secondName = secondName;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }

    public void setSubscribers(final Set<User> subscribers) {
        this.subscribers = subscribers;
    }
}
