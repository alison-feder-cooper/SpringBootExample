package com.rewards.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column
    private long delta;

    //TODO datetime for the transfer creation

    //for hibernate
    protected Transfer() {

    }

    public Transfer(User user, long delta) {
        this.user = user;
        this.delta = delta;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public long getDelta() {
        return delta;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", user=" + user.getId() +
                ", delta=" + delta +
                '}';
    }
}
