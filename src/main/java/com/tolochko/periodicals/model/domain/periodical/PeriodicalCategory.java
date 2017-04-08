package com.tolochko.periodicals.model.domain.periodical;

public enum PeriodicalCategory {
    BUSINESS("business"),
    SPORTS("sports"),
    SCIENCE("science"),
    TRAVELLING("travelling"),
    NEWS("news"),
    NATURE("nature"),
    ART("art");


    private String messageKey;

    PeriodicalCategory(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
