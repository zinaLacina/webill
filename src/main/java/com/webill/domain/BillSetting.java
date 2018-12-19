package com.webill.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.webill.domain.enumeration.TypeBill;

/**
 * BillSetting entity.
 * @author ZINA LACINA.
 */
@ApiModel(description = "BillSetting entity. @author ZINA LACINA.")
@Entity
@Table(name = "bill_setting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BillSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_bill")
    private TypeBill typeBill;

    @Column(name = "price_per_kw")
    private Double pricePerKW;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "tax")
    private Double tax;

    @Column(name = "processing")
    private Double processing;

    @Column(name = "enabled")
    private Boolean enabled;

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

    public TypeBill getTypeBill() {
        return typeBill;
    }

    public BillSetting typeBill(TypeBill typeBill) {
        this.typeBill = typeBill;
        return this;
    }

    public void setTypeBill(TypeBill typeBill) {
        this.typeBill = typeBill;
    }

    public Double getPricePerKW() {
        return pricePerKW;
    }

    public BillSetting pricePerKW(Double pricePerKW) {
        this.pricePerKW = pricePerKW;
        return this;
    }

    public void setPricePerKW(Double pricePerKW) {
        this.pricePerKW = pricePerKW;
    }

    public Double getDiscount() {
        return discount;
    }

    public BillSetting discount(Double discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTax() {
        return tax;
    }

    public BillSetting tax(Double tax) {
        this.tax = tax;
        return this;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getProcessing() {
        return processing;
    }

    public BillSetting processing(Double processing) {
        this.processing = processing;
        return this;
    }

    public void setProcessing(Double processing) {
        this.processing = processing;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public BillSetting enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public BillSetting dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public BillSetting dateModified(Instant dateModified) {
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
        BillSetting billSetting = (BillSetting) o;
        if (billSetting.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), billSetting.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BillSetting{" +
            "id=" + getId() +
            ", typeBill='" + getTypeBill() + "'" +
            ", pricePerKW=" + getPricePerKW() +
            ", discount=" + getDiscount() +
            ", tax=" + getTax() +
            ", processing=" + getProcessing() +
            ", enabled='" + isEnabled() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}
