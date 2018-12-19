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
 * Bills entity.
 * @author ZINA LACINA.
 */
@ApiModel(description = "Bills entity. @author ZINA LACINA.")
@Entity
@Table(name = "bills")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bills implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_month_reading")
    private Integer lastMonthReading;

    @Column(name = "current_month_reading")
    private Integer currentMonthReading;

    @Column(name = "deadline")
    private Instant deadline;

    @Column(name = "bill_file")
    private String billFile;

    @Column(name = "bill_image_upload_content")
    private String uploadedContent;

    @Column(name = "image_file")
    private String imageFile;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "not_rejected")
    private Boolean notRejected;

    @Column(name = "bill_code")
    private String billCode;

    @Column(name = "invoice_number")
    private Integer invoiceN;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_modified")
    private Instant dateModified;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User ownerUser;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User verifierUser;

    @ManyToOne
    @JsonIgnoreProperties("")
    private AssignMeters verifierMetricBill;

    @ManyToOne
    @JsonIgnoreProperties("")
    private BillSetting billSettingApp;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLastMonthReading() {
        return lastMonthReading;
    }

    public Bills lastMonthReading(Integer lastMonthReading) {
        this.lastMonthReading = lastMonthReading;
        return this;
    }

    public void setLastMonthReading(Integer lastMonthReading) {
        this.lastMonthReading = lastMonthReading;
    }

    public Integer getCurrentMonthReading() {
        return currentMonthReading;
    }

    public Bills currentMonthReading(Integer currentMonthReading) {
        this.currentMonthReading = currentMonthReading;
        return this;
    }

    public void setCurrentMonthReading(Integer currentMonthReading) {
        this.currentMonthReading = currentMonthReading;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public Bills deadline(Instant deadline) {
        this.deadline = deadline;
        return this;
    }

    public void setDeadline(Instant deadline) {
        this.deadline = deadline;
    }


    public String getBillFile() {
        return billFile;
    }

    public void setBillFile(String billFile) {
        this.billFile = billFile;
    }

    public String getUploadedContent() {
        return uploadedContent;
    }

    public void setUploadedContent(String uploadedContent) {
        this.uploadedContent = uploadedContent;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Double getAmount() {
        return amount;
    }

    public Bills amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Bills enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Boolean getNotRejected() {
        return notRejected;
    }

    public void setNotRejected(Boolean notRejected) {
        this.notRejected = notRejected;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Bills dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getBillCode() {
        return billCode;
    }


    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Integer getInvoiceN() {
        return invoiceN;
    }

    public void setInvoiceN(Integer invoiceN) {
        this.invoiceN = invoiceN;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public Bills dateModified(Instant dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }

    public User getOwnerUser() {
        return ownerUser;
    }

    public Bills ownerUser(User user) {
        this.ownerUser = user;
        return this;
    }

    public void setOwnerUser(User user) {
        this.ownerUser = user;
    }

    public User getVerifierUser() {
        return verifierUser;
    }

    public Bills verifierUser(User user) {
        this.verifierUser = user;
        return this;
    }

    public void setVerifierUser(User user) {
        this.verifierUser = user;
    }

    public AssignMeters getVerifierMetricBill() {
        return verifierMetricBill;
    }

    public Bills verifierMetricBill(AssignMeters assignMeters) {
        this.verifierMetricBill = assignMeters;
        return this;
    }

    public void setVerifierMetricBill(AssignMeters assignMeters) {
        this.verifierMetricBill = assignMeters;
    }

    public BillSetting getBillSettingApp() {
        return billSettingApp;
    }

    public Bills billSettingApp(BillSetting billSetting) {
        this.billSettingApp = billSetting;
        return this;
    }

    public void setBillSettingApp(BillSetting billSetting) {
        this.billSettingApp = billSetting;
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
        Bills bills = (Bills) o;
        if (bills.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bills.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Bills{" +
            "id=" + getId() +
            ", lastMonthReading=" + getLastMonthReading() +
            ", currentMonthReading=" + getCurrentMonthReading() +
            ", deadline='" + getDeadline() + "'" +
            ", billFile='" + getBillFile() + "'" +
            ", imageFile='" + getImageFile() + "'" +
            ", amount=" + getAmount() +
            ", enabled='" + isEnabled() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}
