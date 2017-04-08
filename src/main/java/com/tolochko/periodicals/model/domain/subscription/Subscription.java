package com.tolochko.periodicals.model.domain.subscription;

import java.time.Instant;
import java.time.LocalDateTime;


public class Subscription {
    private Long id;
    /**
     * The user_id this subscription belongs to.
     */
    private Long user_id;
    /**
     * The periodical_id this subscription is on.
     */
    private Long periodical;
    private String deliveryAddress;
    /**
     * The expiration date of this subscription. It can be prolonged by creating and paying a new invoice
     * for the same periodical.
     */
    private LocalDateTime endDate;
    /**
     * Is {@code active} when a subscription is created. Becomes {@code inactive} when
     * this subscription is expired.
     */
    private Status status;

    public enum Status {
        ACTIVE, INACTIVE
    }

    public static class Builder {
        private Subscription subscription;

        public Builder() {
            subscription = new Subscription();
        }

        public Builder setId(Long id) {
            subscription.setId(id);
            return this;
        }

        public Builder setUserId(Long userId) {
            subscription.setUser_id(userId);
            return this;
        }

        public Builder setPeriodicalId(Long periodicalId) {
            subscription.setPeriodical(periodicalId);
            return this;
        }

        public Builder setDeliveryAddress(String deliveryAddress) {
            subscription.setDeliveryAddress(deliveryAddress);
            return this;
        }

        public Builder setEndDate(LocalDateTime endDate) {
            subscription.setEndDate(endDate);
            return this;
        }

        public Builder setStatus(Status status) {
            subscription.setStatus(status);
            return this;
        }

        public Subscription build() {
            return subscription;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getPeriodicalId() {
        return periodical;
    }

    public void setPeriodical(Long periodicalId) {
        this.periodical = periodicalId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return String.format("Subscription{id=%d, user_id=%s, periodicalId=%s, deliveryAddress='%s', " +
                "endDate=%s, status=%s}", id, user_id, periodical, deliveryAddress, endDate, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subscription that = (Subscription) o;

        if (id != that.id) return false;
        if (user_id != that.user_id) return false;
        if (periodical != that.periodical) return false;
        if (deliveryAddress != null ? !deliveryAddress.equals(that.deliveryAddress) : that.deliveryAddress != null)
            return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = (int) (31 * result + user_id);
        result = (int) (31 * result + periodical);
        return result;
    }
}
