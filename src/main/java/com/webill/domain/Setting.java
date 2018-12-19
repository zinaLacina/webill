package com.webill.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Setting entity.
 * @author ZINA LACINA.
 */
@ApiModel(description = "Setting entity. @author ZINA LACINA.")
@Entity
@Table(name = "setting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_logo")
    private String companyLogo;


    @Column(name = "company_number")
    private String companyNumber;

    @Column(name = "company_address")
    private String companyAddress;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_modified")
    private Instant dateModified;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Setting companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public Setting companyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
        return this;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }


    public String getCompanyNumber() {
        return companyNumber;
    }

    public Setting companyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
        return this;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Setting dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public Setting dateModified(Instant dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
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
        Setting setting = (Setting) o;
        if (setting.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), setting.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Setting{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", companyLogo='" + getCompanyLogo() + "'" +
            ", companyNumber='" + getCompanyNumber() + "'" +
            ", companyAddress='" + getCompanyAddress() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}
