package com.aues.entites;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class EntiteAbstrait implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "create_Date", nullable = false, updatable = false)
    @CreatedDate
    protected Instant createDate = Instant.now();

    @Column(name = "modifie_Date", nullable = false)
    @LastModifiedDate
    protected Instant modifieDate = Instant.now();

    public Integer getId() {
        return id;
    }

    public Instant getModifieDate() {
        return modifieDate;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public void setModifieDate(Instant modifieDate) {
        this.modifieDate = modifieDate;
    }
}
