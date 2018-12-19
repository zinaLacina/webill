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
 * HistoryMeters entity.
 * @author ZINA LACINA.
 */
@ApiModel(description = "HistoryMeters entity. @author CreativeGoup.")
@Entity
@Table(name = "assign_meters")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AssignMeters implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date_contract")
    private Instant startDateContract;

    @Column(name = "end_date_contract")
    private Instant endDateContract;

    @Column(name = "reason_end")
    private String reasonEnd;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User metersUser;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Meters historyMeterUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDateContract() {
        return startDateContract;
    }

    public void setStartDateContract(Instant startDateContract) {
        this.startDateContract = startDateContract;
    }

    public Instant getEndDateContract() {
        return endDateContract;
    }

    public void setEndDateContract(Instant endDateContract) {
        this.endDateContract = endDateContract;
    }


    public String getReasonEnd() {
        return reasonEnd;
    }

    public AssignMeters reasonEnd(String reasonEnd) {
        this.reasonEnd = reasonEnd;
        return this;
    }

    public void setReasonEnd(String reasonEnd) {
        this.reasonEnd = reasonEnd;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public AssignMeters enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AssignMeters createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    public AssignMeters updateAt(Instant updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public User getMetersUser() {
        return metersUser;
    }

    public AssignMeters metersUser(User user) {
        this.metersUser = user;
        return this;
    }

    public void setMetersUser(User user) {
        this.metersUser = user;
    }

    public Meters getHistoryMeterUser() {
        return historyMeterUser;
    }

    public AssignMeters historyMeterUser(Meters meters) {
        this.historyMeterUser = meters;
        return this;
    }

    public void setHistoryMeterUser(Meters meters) {
        this.historyMeterUser = meters;
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
        AssignMeters assignMeters = (AssignMeters) o;
        if (assignMeters.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), assignMeters.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AssignMeters{" +
            "id=" + getId() +
            ", Startdate=" + getStartDateContract() +
            ", Enddate=" + getEndDateContract() +
            ", reasonEnd='" + getReasonEnd() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
