package com.webill.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Meters entity.
 * @author ZINA LACINA.
 */
@ApiModel(description = "Meters entity. @author ZINA LACINA.")
@Entity
@Table(name = "meters")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Meters implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latituude")
    private Double latituude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "qr_code")
    private String qrCode;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "address_meters")
    private String addressMeters;

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

    public Double getLatituude() {
        return latituude;
    }

    public Meters latituude(Double latituude) {
        this.latituude = latituude;
        return this;
    }

    public void setLatituude(Double latituude) {
        this.latituude = latituude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Meters longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getQrCode() {
        return qrCode;
    }

    public Meters qrCode(String qrCode) {
        this.qrCode = qrCode;
        return this;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Meters enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getAddressMeters() {
        return addressMeters;
    }

    public Meters addressMeters(String addressMeters) {
        this.addressMeters = addressMeters;
        return this;
    }

    public void setAddressMeters(String addressMeters) {
        this.addressMeters = addressMeters;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Meters dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public Meters dateModified(Instant dateModified) {
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
        Meters meters = (Meters) o;
        if (meters.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), meters.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Meters{" +
            "id=" + getId() +
            ", latituude=" + getLatituude() +
            ", longitude=" + getLongitude() +
            ", qrCode='" + getQrCode() + "'" +
            ", addressMeters='" + getAddressMeters() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}
