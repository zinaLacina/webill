package com.webill.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Notifications entity.
 * @author ZINA LACINA.
 */
@ApiModel(description = "Notifications entity. @author ZINA LACINA.")
@Entity
@Table(name = "notifications")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Notifications implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "checked")
    private Boolean checked;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User senderUser;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User receiverUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public Notifications message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isChecked() {
        return checked;
    }

    public Notifications checked(Boolean checked) {
        this.checked = checked;
        return this;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Notifications createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    public Notifications updateAt(Instant updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public Notifications senderUser(User user) {
        this.senderUser = user;
        return this;
    }

    public void setSenderUser(User user) {
        this.senderUser = user;
    }

    public User getReceiverUser() {
        return receiverUser;
    }

    public Notifications receiverUser(User user) {
        this.receiverUser = user;
        return this;
    }

    public void setReceiverUser(User user) {
        this.receiverUser = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notifications notifications = (Notifications) o;
        if (notifications.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notifications.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Notifications{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", checked='" + isChecked() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
