package com.robyrodriguez.stackbuster.types;

/**
 * Badge types
 */
public enum BadgeType {
    /** 1k views */
    POPULAR_QUESTION(1000),
    /** 2.5k views */
    NOTABLE_QUESTION(2500),
    /** 10k views */
    FAMOUS_QUESTION(10000),
    /** 25 shared clicks */
    ANNOUNCER(25),
    /** 300 shared clicks */
    BOOSTER(300),
    /** 1k shared clicks */
    PUBLICIST(1000);

    private int clicks;

    BadgeType(int clicks) {
        this.clicks = clicks;
    }

    public int getClicks() {
        return clicks;
    }

    public boolean isUserInvolved() {
        return this == ANNOUNCER || this == BOOSTER || this == PUBLICIST;
    }
}
