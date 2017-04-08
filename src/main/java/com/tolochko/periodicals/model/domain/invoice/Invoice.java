package com.tolochko.periodicals.model.domain.invoice;

import com.tolochko.periodicals.model.domain.periodical.Periodical;
import com.tolochko.periodicals.model.domain.user.User;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;


public class Invoice implements Serializable {
    private static final long serialVersionUID = 12412412488L;
    private Long id;
    private Long userId;
    private Long periodicalId;
    private int subscriptionPeriod;
    private long totalSum;
    private LocalDateTime creationDate;
    private LocalDateTime paymentDate;
    private Status status;

    public enum Status {
        NEW, PAID
    }

    public static class Builder {
        private Invoice invoice;

        public Builder() {
            invoice = new Invoice();
        }

        public Builder setId(long id) {
            invoice.setId(id);
            return this;
        }

        public Builder setUserId(Long userId) {
            invoice.setUserId(userId);
            return this;
        }

        public Builder setPeriodicalId(Long periodicalId) {
            invoice.setPeriodicalId(periodicalId);
            return this;
        }

        public Builder setTotalSum(long totalSum) {
            invoice.setTotalSum(totalSum);
            return this;
        }

        public Builder setSubscriptionPeriod(int subscriptionPeriod) {
            invoice.setSubscriptionPeriod(subscriptionPeriod);
            return this;
        }

        public Builder setStatus(Status status) {
            invoice.setStatus(status);
            return this;
        }

        public Builder setCreationDate(LocalDateTime creationDate) {
            invoice.setCreationDate(creationDate);
            return this;
        }

        public Builder setPaymentDate(LocalDateTime paymentDate) {
            invoice.setPaymentDate(paymentDate);
            return this;
        }

        public Invoice build() {
            return invoice;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPeriodicalId() {
        return periodicalId;
    }

    public void setPeriodicalId(Long periodicalId) {
        this.periodicalId = periodicalId;
    }

    public int getSubscriptionPeriod() {
        return subscriptionPeriod;
    }

    public void setSubscriptionPeriod(int subscriptionPeriod) {
        this.subscriptionPeriod = subscriptionPeriod;
    }

    public long getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(long totalSum) {
        this.totalSum = totalSum;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invoice invoice = (Invoice) o;

        if (subscriptionPeriod != invoice.subscriptionPeriod) return false;
        if (totalSum != invoice.totalSum) return false;
        if (id != null ? !id.equals(invoice.id) : invoice.id != null) return false;
        if (userId != null ? !userId.equals(invoice.userId) : invoice.userId != null) return false;
        if (periodicalId != null ? !periodicalId.equals(invoice.periodicalId) : invoice.periodicalId != null)
            return false;
        if (creationDate != null ? !creationDate.equals(invoice.creationDate) : invoice.creationDate != null)
            return false;
        if (paymentDate != null ? !paymentDate.equals(invoice.paymentDate) : invoice.paymentDate != null) return false;
        return status == invoice.status;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (periodicalId != null ? periodicalId.hashCode() : 0);
        result = 31 * result + subscriptionPeriod;
        result = 31 * result + (int) (totalSum ^ (totalSum >>> 32));
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (paymentDate != null ? paymentDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", userId=" + userId +
                ", periodicalId=" + periodicalId +
                ", subscriptionPeriod=" + subscriptionPeriod +
                ", totalSum=" + totalSum +
                ", creationDate=" + creationDate +
                ", paymentDate=" + paymentDate +
                ", status=" + status +
                '}';
    }
}
