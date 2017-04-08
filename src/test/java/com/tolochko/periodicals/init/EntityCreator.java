package com.tolochko.periodicals.init;

import com.tolochko.periodicals.model.domain.periodical.Periodical;
import com.tolochko.periodicals.model.domain.periodical.PeriodicalCategory;
import com.tolochko.periodicals.model.domain.subscription.Subscription;
import com.tolochko.periodicals.model.domain.user.User;

import java.time.Instant;
import java.time.LocalDateTime;

public class EntityCreator {


    public static User createUser() {
        User.Builder userBuilder = new User.Builder();
        userBuilder.setFirstName("Tymur")
                .setLastName("Tolochko")
                .setEmail("tymurtolochko@gmail.com")
                .setAddress("Mazepy Street , 14")
                .setPassword("passwordd")
                .setRole(User.Role.getRole("user"));
        return userBuilder.build();
    }

    public static Periodical createPeriodical() {
        Periodical.Builder periodicalBuider = new Periodical.Builder();
        periodicalBuider.setName("Article")
                .setCategory(PeriodicalCategory.NEWS)
                .setPublisher("Wikipedia")
                .setDescription("Some test article published by wikipedia in news category")
                .setOneMonthCost(111)
                .setStatus(Periodical.Status.INACTIVE);

        return periodicalBuider.build();
    }

    public static Subscription createSubscription() {
        Subscription.Builder builder = new Subscription.Builder();
        builder.setUserId(68l)   /* testid in db*/
                .setPeriodicalId(42l)
                .setDeliveryAddress("testaddress")
                .setEndDate(LocalDateTime.of(2017, 6, 1, 12, 0, 0, 0))
                .setStatus(Subscription.Status.ACTIVE);

        return builder.build();
    }

}
