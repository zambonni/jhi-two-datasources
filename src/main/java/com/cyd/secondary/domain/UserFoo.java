package com.cyd.secondary.domain;

import com.cyd.primary.domain.AbstractAuditingEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A user.
 */
@Entity
@Table(name = "jhi_user_foo")
public class UserFoo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
