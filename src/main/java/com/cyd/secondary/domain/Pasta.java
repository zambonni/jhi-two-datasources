package com.cyd.secondary.domain;

import com.cyd.primary.domain.AbstractAuditingEntity;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "pasta")
public class Pasta extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
