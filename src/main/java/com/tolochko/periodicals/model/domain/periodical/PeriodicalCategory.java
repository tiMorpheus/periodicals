package com.tolochko.periodicals.model.domain.periodical;

public enum PeriodicalCategory {
    BUSINESS("category.business"),
    SPORTS("category.sports"),
    SCIENCE("category.science"),
    TRAVELLING("category.travelling"),
    NEWS("category.news"),
    NATURE("category.nature"),
    ART("category.art");


    private String messageKey;

    PeriodicalCategory(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
